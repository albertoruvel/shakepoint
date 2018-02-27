package com.shakepoint.web.facade.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shakepoint.web.core.email.EmailAsyncSender;
import com.shakepoint.web.core.email.Template;
import com.shakepoint.web.core.machine.PurchaseStatus;
import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.v1.dto.rest.request.ConfirmPurchaseRequest;
import com.shakepoint.web.data.v1.dto.rest.request.UserProfileRequest;
import com.shakepoint.web.data.dto.res.rest.*;
import com.shakepoint.web.data.v1.dto.rest.response.*;
import com.shakepoint.web.data.v1.entity.*;
import com.shakepoint.web.util.TransformationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.shakepoint.web.core.qr.QrCodeCreator;
import com.shakepoint.web.facade.ShopFacade;

public class ShopFacadeImpl implements ShopFacade {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QrCodeCreator qrCodeCreator;

    @Autowired
    private EmailAsyncSender emailSender;

    private final Logger log = Logger.getLogger(getClass());
    private static final String EMAIL_SENDER_QUEUE_NAME = "shakepoint.integration.email.send";


    @Override
    public UserProfileResponse saveProfile(Principal p, UserProfileRequest request) {
        //get user id
        final ShakepointUser user = userRepository.getUserByEmail(p.getName());
        final String userId = userRepository.getUserId(p.getName());
        //get profile
        ShakepointUserProfile existingProfile = userRepository.getUserProfile(userId);
        if (existingProfile == null) {
            ShakepointUserProfile profile = TransformationUtils.getProfile(userId, request);
            profile.setUser(user);
            log.info("Creating user profile");
            userRepository.saveProfile(profile);
        } else {
            existingProfile.setAge(request.getAge());
            existingProfile.setBirthday(request.getBirthday());
            existingProfile.setHeight(request.getHeight());
            existingProfile.setWeight(request.getWeight());
            log.info("Updating user profile");
            userRepository.updateProfile(existingProfile);
        }

        return getUserProfile(p);
    }


    @Override
    public List<UserPurchaseResponse> getUserPurchases(Principal p, int pageNumber) {
        final String userId = userRepository.getUserId(p.getName());
        List<ShakepointPurchase> purchases = purchaseRepository.getUserPurchases(userId, pageNumber);
        List<UserPurchaseResponse> response = TransformationUtils.createPurchases(purchases);
        return response;
    }

    @Override
    public PurchaseQRCode confirmPurchase(ConfirmPurchaseRequest request, Principal p) {
        PurchaseQRCode code = null;
        ShakepointPurchase purchase = purchaseRepository.getPurchase(request.getPurchaseId());
        ShakepointUser user;
        if (purchase == null) {
            return new PurchaseQRCode(null);
        } else {
            user = userRepository.getUserByEmail(p.getName());

            purchase.setUser(user);
            purchase.setStatus(PurchaseStatus.AUTHORIZED);
            purchase.setReference("${paymentMethodReference}");
            purchaseRepository.update(purchase);
            //send an email
            List<String> productNames = new ArrayList();
            productNames.add(purchase.getProduct().getName());
            Map<String, Object> args = new HashMap();
            args.put("productNames", productNames);
            emailSender.sendEmail(user.getEmail(), Template.SUCCESSFULL_PURCHASE, args);

            return new PurchaseQRCode(purchase.getQrCodeUrl());
        }
    }

    private PurchaseResponse getPurchaseResponse(String message, String purchaseId, boolean success, double total, String qrCodeUrl) {
        PurchaseResponse response = new PurchaseResponse();
        response.setMessage(message);
        response.setPurchaseId(purchaseId);
        response.setSuccess(success);
        response.setTotal(total);
        response.setQrCodeUrl(qrCodeUrl);
        return response;
    }


    @Override
    public List<MachineSearch> searchMachinesByName(String machineName) {
        List<ShakepointMachine> machines = machineRepository.searchByName(machineName);
        List<MachineSearch> machineSearches = new ArrayList();
        for (ShakepointMachine m : machines) {
            machineSearches.add(new MachineSearch(m.getId(), m.getName(), 0));
        }
        return machineSearches;
    }

    @Override
    public ProductDTO getProductDetails(String productId) {
        ShakepointProduct product = productRepository.getProduct(productId);
        ProductDTO dto = new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDescription(), product.getLogoUrl());
        return dto;
    }

    public AvailablePurchaseResponse getAvailablePurchaseForMachine(String productId, String machineId) {
        //get available products
        List<ShakepointPurchase> purchases = purchaseRepository.getAvailablePurchasesForMachine(productId, machineId);
        log.info(String.format("Got a total of %d available purchases", purchases.size()));
        if (purchases.isEmpty()) {
            return new AvailablePurchaseResponse(null);
        } else {
            //get the first one
            return new AvailablePurchaseResponse(purchases.get(0).getId());
        }
    }

    @Override
    public MachineSearch searchMachine(double longitude, double latitude) {
        //get all machines
        List<ShakepointMachine> machines = machineRepository.getMachines(1);
        MachineSearch search = new MachineSearch();
        double distance = 1000000; // high distance to get accurate results
        String[] array = null;
        int currentIndex = 0;
        for (int i = 0; i < machines.size(); i++) {
            array = machines.get(i).getLocation().split(",");
            double long1 = Double.parseDouble(array[0]);
            double lat1 = Double.parseDouble(array[1]);
            //get distance
            double tmpDistance = distance(lat1, long1, latitude, longitude);
            if (tmpDistance < distance) {
                distance = tmpDistance;
                currentIndex = i;
            }

        }
        ShakepointMachine machine = machines.get(currentIndex);
        search.setMachineId(machine.getId());
        search.setMachineName(machine.getName());
        search.setDistance(distance);
        return search;
    }


    /**
     * Get distance in mts from 2 points
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param
     * @return
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    //todo: convert to dto
    @Override
    public GetMachineProductsDTO getMachineProducts(String machineId, int pageNumber) {
        List<ShakepointMachineProductStatus> productsStatus = machineRepository.getMachineProducts(machineId);
        log.info(String.format("Number of products for machine %d", productsStatus.size()));
        List<ShakepointProduct> products = new ArrayList();
        for (ShakepointMachineProductStatus p : productsStatus) {
            products.add(p.getProduct());
        }
        List<ProductDTO> productsDTO = TransformationUtils.createProducts(products);
        log.info("Creating response...");

        return new GetMachineProductsDTO(productsDTO);
    }

    @Override
    public List<PurchaseCodeResponse> getActiveQrCodes(Principal p, String machineId, int pageNumber) {
        //get user id
        String userId = userRepository.getUserId(p.getName());
        List<PurchaseCodeResponse> page = TransformationUtils.createPurchaseCodes(purchaseRepository.getAuthorizedPurchases(userId, machineId, pageNumber));
        return page;
    }

    @Override
    public UserProfileResponse getUserProfile(Principal principal) {
        final String userId = userRepository.getUserId(principal.getName());
        UserProfileResponse profile = null;
        try {
            ShakepointUserProfile userProfile = userRepository.getUserProfile(userId);
            if (userProfile == null) {
                profile = new UserProfileResponse(null, null, null, false, 0, null, 0.0, 0.0, 0.0, null);
            } else {
                profile = TransformationUtils.createUserProfile(userProfile);
            }
        } catch (Exception ex) {

        }
        return profile;
    }

}
