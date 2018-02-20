package com.shakepoint.web.facade;

import com.shakepoint.web.data.v1.dto.rest.request.ConfirmPurchaseRequest;
import com.shakepoint.web.data.v1.dto.rest.request.PurchaseRequest;
import com.shakepoint.web.data.v1.dto.rest.request.UserProfileRequest;
import com.shakepoint.web.data.dto.res.rest.*;
import com.shakepoint.web.data.v1.dto.rest.response.*;

import java.security.Principal;
import java.util.List;


public interface ShopFacade {
    public MachineSearch searchMachine(double longitude, double latitude);

    public GetMachineProductsDTO getMachineProducts(String machineId, int pageNumber);

    public List<PurchaseCodeResponse> getActiveQrCodes(Principal p, String machineId, int pageNumber);

    public PurchaseQRCode confirmPurchase(ConfirmPurchaseRequest request, Principal p);

    public List<UserPurchaseResponse> getUserPurchases(Principal p, int pageNumber);

    public UserProfileResponse getUserProfile(Principal principal);

    public UserProfileResponse saveProfile(Principal p, UserProfileRequest request);

    public List<MachineSearch> searchMachinesByName(String machineName);

    //public List<Combo> getMachineCombos(String machineId, int pageNumber);
    public ProductDTO getProductDetails(String productId);

    public AvailablePurchaseResponse getAvailablePurchaseForMachine(String productId, String machineId);
}
