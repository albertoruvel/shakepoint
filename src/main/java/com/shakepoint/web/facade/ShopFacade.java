package com.shakepoint.web.facade;

import com.shakepoint.web.data.v1.dto.rest.request.PurchaseEventRequest;
import com.shakepoint.web.data.v1.dto.rest.request.PurchaseRequest;
import com.shakepoint.web.data.v1.dto.rest.request.UserProfileRequest;
import com.shakepoint.web.data.dto.res.rest.*;
import com.shakepoint.web.data.v1.dto.rest.response.PurchaseCodeResponse;
import com.shakepoint.web.data.v1.dto.rest.response.PurchaseQRCode;
import com.shakepoint.web.data.v1.dto.rest.response.MachineSearch;
import com.shakepoint.web.data.v1.dto.rest.response.PurchaseResponse;
import com.shakepoint.web.data.v1.entity.ShakepointProduct;

import java.security.Principal;
import java.util.List;


public interface ShopFacade {
	 public MachineSearch searchMachine(double longitude, double latitude);
	 public List<ShakepointProduct> getMachineProducts(String machineId, int pageNumber);
	 public PurchaseResponse requestPurchase(PurchaseRequest request, Principal principal);
	 public List<PurchaseCodeResponse> getActiveQrCodes(Principal p, String machineId, int pageNumber);
	 public PurchaseQRCode confirmPurchase(PurchaseEventRequest request, Principal p);
	 public List<UserPurchaseResponse> getUserPurchases(Principal p, int pageNumber);
	 public UserProfileResponse getUserProfile(Principal principal);
	 public UserProfileResponse saveProfile(Principal p, UserProfileRequest request);
	 //public List<Combo> getMachineCombos(String machineId, int pageNumber);
}
