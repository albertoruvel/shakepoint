package com.shakepoint.web.controller.rest;

import java.security.Principal;
import java.util.List;

import com.shakepoint.web.data.v1.dto.rest.request.ConfirmPurchaseRequest;
import com.shakepoint.web.data.v1.dto.rest.request.PurchaseRequest;
import com.shakepoint.web.data.v1.dto.rest.request.UserProfileRequest;
import com.shakepoint.web.data.dto.res.rest.*;
import com.shakepoint.web.data.v1.dto.rest.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shakepoint.web.facade.ShopFacade;

@RequestMapping("/rest/shop")
@RestController
public class ShopRestController {

    @Autowired
    private ShopFacade shopFacade;

    @RequestMapping(value = "/productDetails", method = RequestMethod.GET, produces = "application/json")
    public ProductDTO getProductDetails(@RequestParam(value = "productId")String productId){
        return shopFacade.getProductDetails(productId);
    }

    @RequestMapping(value = "/searchMachine", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public
    @ResponseBody
    MachineSearch searchMachine(@RequestParam(value = "longitude", required = true) double longitude,
                                @RequestParam(value = "latitude", required = true) double latitude) {
        return shopFacade.searchMachine(longitude, latitude);
    }

    @RequestMapping(value = "/searchMachineByName", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public @ResponseBody List<MachineSearch> searchByName(@RequestParam(value ="name")String machineName){
        return shopFacade.searchMachinesByName(machineName);
    }

    //todo: convert to dto
    @RequestMapping(value = "/getProducts", method = RequestMethod.GET,
            produces = "application/json")
    public
    @ResponseBody
    GetMachineProductsDTO getMachineProducts(@RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber,
                                        @RequestParam(value = "machineId", required = true) String machineId) {
        return shopFacade.getMachineProducts(machineId, pageNumber);
    }


    @RequestMapping(value = "/getActiveCodes", method = RequestMethod.GET,
            produces = "application/json")
    public List<PurchaseCodeResponse> getUserActiveQrCodes(@AuthenticationPrincipal Principal p,
                                                           @RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber,
                                                           @RequestParam(value = "machineId", required = true) String machineId) {
        return shopFacade.getActiveQrCodes(p, machineId, pageNumber);
    }

    @RequestMapping(value = "/confirmPurchase", method = RequestMethod.POST,
            produces = "application/json", consumes = "application/json")
    public PurchaseQRCode confirmPurchase(@RequestBody ConfirmPurchaseRequest request, @AuthenticationPrincipal Principal p) {
        return shopFacade.confirmPurchase(request, p);
    }

    @RequestMapping(value = "/getAvailablePurchaseForMachine", method = RequestMethod.GET,
            produces = "application/json", consumes = "applicaiton/json")
    public AvailablePurchaseResponse getAvailablePurchaseForMachine(@RequestParam(value = "productId") String productId, @RequestParam(value = "machineiD") String machineId){
        return shopFacade.getAvailablePurchaseForMachine(productId, machineId);
    }


    @RequestMapping(value = "/getPurchases", method = RequestMethod.GET,
            produces = "application/json")
    public List<UserPurchaseResponse> getUserPurchases(@AuthenticationPrincipal Principal p,
                                                       @RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber) {
        return shopFacade.getUserPurchases(p, pageNumber);
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET,
            produces = "application/json")
    public UserProfileResponse getUserProfile(@AuthenticationPrincipal Principal principal) {
        return shopFacade.getUserProfile(principal);
    }

    @RequestMapping(value = "/saveProfile", method = RequestMethod.POST,
            produces = "application/json", consumes = "application/json")
    public
    @ResponseBody
    UserProfileResponse saveUserProfile(@AuthenticationPrincipal Principal p, @RequestBody UserProfileRequest request) {
        return shopFacade.saveProfile(p, request);
    }
}
