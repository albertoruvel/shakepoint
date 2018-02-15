package com.shakepoint.web.controller.rest;

import java.security.Principal;
import java.util.List;

import com.shakepoint.web.data.v1.dto.rest.request.PurchaseEventRequest;
import com.shakepoint.web.data.v1.dto.rest.request.PurchaseRequest;
import com.shakepoint.web.data.v1.dto.rest.request.UserProfileRequest;
import com.shakepoint.web.data.dto.res.rest.*;
import com.shakepoint.web.data.v1.dto.rest.response.*;
import com.shakepoint.web.data.v1.entity.ShakepointProduct;
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

    @RequestMapping(value = "/search_machine", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public
    @ResponseBody
    MachineSearch searchMachine(@RequestParam(value = "longitude", required = true) double longitude,
                                @RequestParam(value = "latitude", required = true) double latitude) {
        return shopFacade.searchMachine(longitude, latitude);
    }

    @RequestMapping(value = "/search_machine_by_name", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public @ResponseBody List<MachineSearch> searchByName(@RequestParam(value ="name")String machineName){
        return shopFacade.searchMachinesByName(machineName);
    }

    //todo: convert to dto
    @RequestMapping(value = "/get_products", method = RequestMethod.GET,
            produces = "application/json")
    public
    @ResponseBody
    GetMachineProductsDTO getMachineProducts(@RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber,
                                        @RequestParam(value = "machine_id", required = true) String machineId) {
        return shopFacade.getMachineProducts(machineId, pageNumber);
    }

    @RequestMapping(value = "/request_buy", method = RequestMethod.POST,
            produces = "application/json", consumes = "application/json")
    public
    @ResponseBody
    PurchaseResponse requestPurchase(@RequestBody PurchaseRequest request, @AuthenticationPrincipal Principal p) {
        return shopFacade.requestPurchase(request, p);
    }

    @RequestMapping(value = "/get_active_codes", method = RequestMethod.GET,
            produces = "application/json")
    public List<PurchaseCodeResponse> getUserActiveQrCodes(@AuthenticationPrincipal Principal p,
                                                           @RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber,
                                                           @RequestParam(value = "machine_id", required = true) String machineId) {
        return shopFacade.getActiveQrCodes(p, machineId, pageNumber);
    }

    @RequestMapping(value = "/confirm_purchase", method = RequestMethod.POST,
            produces = "application/json", consumes = "application/json")
    public PurchaseQRCode confirmPurchase(@RequestBody PurchaseEventRequest request, @AuthenticationPrincipal Principal p) {
        return shopFacade.confirmPurchase(request, p);
    }

    @RequestMapping(value = "/get_purchases", method = RequestMethod.GET,
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

    @RequestMapping(value = "/save_profile", method = RequestMethod.POST,
            produces = "application/json", consumes = "application/json")
    public
    @ResponseBody
    UserProfileResponse saveUserProfile(@AuthenticationPrincipal Principal p, @RequestBody UserProfileRequest request) {
        return shopFacade.saveProfile(p, request);
    }
}
