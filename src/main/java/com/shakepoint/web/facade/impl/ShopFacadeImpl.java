package com.shakepoint.web.facade.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shakepoint.web.core.email.EmailAsyncSender;
import com.shakepoint.web.core.email.Template;
import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.v1.dto.rest.request.PurchaseEventRequest;
import com.shakepoint.web.data.v1.dto.rest.request.PurchaseRequest;
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
        List<UserPurchaseResponse> response = null;
        final String userId = userRepository.getUserId(p.getName());
        response = purchaseRepository.getUserPurchases(userId, pageNumber);
        return response;
    }

    @Override
    public PurchaseQRCode confirmPurchase(PurchaseEventRequest request, Principal p) {
        PurchaseQRCode code = null;
        //update the purchase

        //validates request
        purchaseRepository.confirmPurchase(request.getReference(), request.getId());

        //code = purchaseRepository.getCode(request.getReference());

        return code;
    }

    @Override
    public PurchaseResponse requestPurchase(PurchaseRequest request, Principal principal) {
        PurchaseResponse response = new PurchaseResponse();
        final ShakepointUser user = userRepository.getUserByEmail(principal.getName());
        ShakepointPurchase purchase = null;
        ShakepointProduct product = null;
        ShakepointPurchaseQRCode code = null;
        String qrCode = "";
        String resourcesQrCode = "";
        List<ShakepointProduct> comboProducts = null;


        if (request == null) {
            response.setMessage("There is no purchase content");
            response.setPurchaseId(null);
            response.setSuccess(false);
            response.setTotal(0.0);
        } else {
            final List<String> productNames = new ArrayList<String>();

            //create a new purchase
            purchase = TransformationUtils.getPurchase(request);
            purchase.setUser(user);
            //set user id

            //check if the product is a combo
            product = productRepository.getProduct(request.getProductId());
            ShakepointMachine machine = machineRepository.getMachine(request.getMachineId());

            purchase.setMachine(machine);
            purchase.setProduct(product);
            purchaseRepository.createPurchase(purchase);
            if (product.getType() == ProductType.COMBO) {
                //get combo products
                comboProducts = productRepository.getComboProducts(product.getId(), 1);
                //iterate to create a qr code with each product
                for (ShakepointProduct p : comboProducts) {
                    //get a different qr code
                    code = TransformationUtils.getQrCode(purchase);
                    code.setPurchase(purchase);
                    //create the code image
                    qrCode = qrCodeCreator.createQRCode(purchase.getId(), purchase.getMachine().getId(),
                            p.getId(), code.getId());
                    //add qr code to resources folder
                    //TODO: add qr url from code entity
                    //update qr code
                    code.setImageUrl(resourcesQrCode);
                    //add qr code
                    purchaseRepository.createQrCode(code);
                    productNames.add(p.getDescription());
                }
                //create response
                response = getPurchaseResponse("Purchase created as pre-authorized purchase", purchase.getId(),
                        true, purchase.getTotal(), "N/A");
            } else {
                //create the qr code
                code = TransformationUtils.getQrCode(purchase);
                qrCode = qrCodeCreator.createQRCode(purchase.getId(), purchase.getMachine().getId(),
                        purchase.getProduct().getId(), code.getId());
                //add qr code to resources folder (MAPPED TO A URL BY THIS HOST)
                //TODO: add url from code entity
                //update qr code
                code.setImageUrl(resourcesQrCode);
                code.setPurchase(purchase);
                //addd qr cocde
                purchaseRepository.createQrCode(code);
                //create a purchase response
                response = getPurchaseResponse("Purchase created as pre-authorized purchase", purchase.getId(),
                        true, purchase.getTotal(), resourcesQrCode);
                productNames.add(product.getDescription());
            }
            //Send successfully purchase email
            if (!productNames.isEmpty()) {
                final Map<String, Object> params = new HashMap<String, Object>(1);
                params.put("productNames", productNames);
                emailSender.sendEmail(principal.getName(), Template.SUCCESSFULL_PURCHASE, params);
            }
        }
        return response;
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

    /**
     * private String moveQrCode(String tmpPath) {
     * File file = new File(tmpPath);
     * File resFile = new File(ShakeUtils.QR_CODES_RESOURCES_FOLDER + "/" + file.getName());
     * <p>
     * //create an input stream
     * try {
     * file.renameTo(resFile);
     * } catch (Exception ex) {
     * <p>
     * }
     * return String.format(ShakeUtils.LOCALHOST_QR_CODE_FORMAT, resFile.getName());
     * }
     **/


    //todo: convert to dto
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
        //get products from machine
        ShakepointMachine machine = machineRepository.getMachine(machineId);
        //get statuses
        List<ShakepointMachineProductStatus> productsStatus = machine.getProducts();
        List<ShakepointProduct> products = new ArrayList();
        for (ShakepointMachineProductStatus p : productsStatus) {
            products.add(p.getProduct());
        }
        return new GetMachineProductsDTO(TransformationUtils.createProducts(products));


        //List<ShakepointProduct> products = productRepository.getProducts(machineId, 1, ProductType.SIMPLE);
        //return new GetMachineProductsDTO(TransformationUtils.createProducts(products));
    }

    @Override
    public List<PurchaseCodeResponse> getActiveQrCodes(Principal p, String machineId, int pageNumber) {
        //get user id
        String userId = userRepository.getUserId(p.getName());
        List<PurchaseCodeResponse> page = TransformationUtils.createPurchaseCodes(purchaseRepository.getActiveCodes(userId, machineId, pageNumber));
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
