package com.shakepoint.web.facade.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shakepoint.web.core.email.EmailAsyncSender;
import com.shakepoint.web.core.email.Template;
import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.core.machine.PurchaseStatus;
import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.core.shop.PayWorksClientService;
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

    @Autowired
    private PayWorksClientService payWorksClientService;

    private final Logger log = Logger.getLogger(getClass());
    private static final String EMAIL_SENDER_QUEUE_NAME = "shakepoint.integration.email.send";


    @Override
    public UserProfileResponse saveProfile(Principal p, UserProfileRequest request) {
        //get user id
        final User user = userRepository.getUserByEmail(p.getName());
        final String userId = userRepository.getUserId(p.getName());
        //get profile
        UserProfile existingProfile = userRepository.getUserProfile(userId);
        if (existingProfile == null) {
            UserProfile profile = TransformationUtils.getProfile(userId, request);
            profile.setUser(user);
            log.info("Creating user profile");
            userRepository.saveProfile(profile);
        } else {
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
        List<Purchase> purchases = purchaseRepository.getUserPurchases(userId, pageNumber);
        List<UserPurchaseResponse> response = TransformationUtils.createPurchases(purchases);
        return response;
    }

    @Override
    public PurchaseQRCode confirmPurchase(ConfirmPurchaseRequest request, Principal p) {
        PurchaseQRCode code = null;
        Purchase purchase = purchaseRepository.getPurchase(request.getPurchaseId());
        User user;
        if (purchase == null) {
            log.error(String.format("No purchase found for %s", request.getPurchaseId()));
            return new PurchaseQRCode(null, false, "No se ha podido encontrar la compra para la máquina que se especificó");
        } else if (purchase.getStatus() == PurchaseStatus.AUTHORIZED || purchase.getStatus() == PurchaseStatus.CASHED) {
            //trying to buy  a purchase with another status
            log.info("Trying to buy an already authorized or cashed purchase");
            return new PurchaseQRCode(null, false, "La compra especificada ya ha sido comprada por alguien mas, refresca los productos y vuelve a intentar");
        } else {
            user = userRepository.getUserByEmail(p.getName());
            PaymentDetails paymentDetails = payWorksClientService.authorizePayment(request.getCardNumber(), request.getCardExpirationDate(), request.getCvv(), purchase.getTotal());
            if (paymentDetails == null) {
                log.info("No payment details from payworks");
                return new PurchaseQRCode(null, false, "Ha ocurrido un problema al realizar el pago, intenta nuevamente");
            } else if (paymentDetails.getAuthCode() != null && paymentDetails.getPayworksResult().equals("A")) {
                //payment went well
                purchase.setUser(user);
                purchase.setStatus(PurchaseStatus.AUTHORIZED);
                purchase.setReference(paymentDetails.getReference());
                purchaseRepository.update(purchase);
                //send an email
                List<String> productNames = new ArrayList();
                productNames.add(purchase.getProduct().getName());
                Map<String, Object> args = new HashMap();
                args.put("productNames", productNames);
                emailSender.sendEmail(user.getEmail(), Template.SUCCESSFUL_PURCHASE, args);
                log.info("Payment went successfully");
                return new PurchaseQRCode(purchase.getQrCodeUrl(), true, "Compra realizada con éxito");
            } else if (paymentDetails.getPayworksResult().equals("D")) {
                //declined
                log.info("Declined");
                return new PurchaseQRCode(null, false, "La tarjeta proporcionada ha sido declinada");
            } else if (paymentDetails.getPayworksResult().equals("T")) {
                log.info("Timeout on provider");
                return new PurchaseQRCode(null, false, "No se ha obtenido respuesta del autorizador, revisa los datos e intenta nuevamente");
            } else {
                log.info("Rejected");
                return new PurchaseQRCode(null, false, "La tarjeta proporcionada ha sido rechazada");
            }
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
        List<VendingMachine> machines = machineRepository.searchByName(machineName);
        List<MachineSearch> machineSearches = new ArrayList();
        for (VendingMachine m : machines) {
            machineSearches.add(new MachineSearch(m.getId(), m.getName(), 0));
        }
        return machineSearches;
    }

    @Override
    public ProductDTO getProductDetails(String productId) {
        Product product = productRepository.getProduct(productId);
        ProductDTO dto = new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDescription(),
                product.getLogoUrl(), ProductType.getProductTypeForClient(product.getType()), product.getNutritionalDataUrl());
        return dto;
    }

    public AvailablePurchaseResponse getAvailablePurchaseForMachine(String productId, String machineId) {
        //get available products
        List<Purchase> purchases = purchaseRepository.getAvailablePurchasesForMachine(productId, machineId);
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
        List<VendingMachine> machines = machineRepository.getMachines(1);
        log.info(String.format("Found %d registered machines", machines.size()));
        MachineSearch search = new MachineSearch();
        double distance = 1000000; // high distance to get accurate results
        String[] array = null;
        int currentIndex = 0;
        for (int i = 0; i < machines.size(); i++) {
            if (machines.get(i).getLocation() == null || machines.get(i).getLocation().isEmpty())
                continue;
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
        VendingMachine machine = machines.get(currentIndex);
        log.info(String.format("Got machine %s from distance search", machine.getName()));
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
        List<VendingMachineProductStatus> productsStatus = machineRepository.getMachineProducts(machineId);
        log.info(String.format("Number of products for machine %d", productsStatus.size()));
        List<Product> products = new ArrayList();
        for (VendingMachineProductStatus p : productsStatus) {
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
        final User user = userRepository.getUserByEmail(principal.getName());
        UserProfileResponse profile = null;
        try {
            UserProfile userProfile = userRepository.getUserProfile(user.getId());
            if (userProfile == null) {
                profile = new UserProfileResponse(user.getName(), user.getId(), user.getCreationDate(), false, null, 0.0, 0.0, 0.0, user.getEmail());
            } else {
                profile = TransformationUtils.createUserProfile(userProfile);
            }
        } catch (Exception ex) {

        }
        return profile;
    }

}
