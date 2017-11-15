package com.shakepoint.web.facade;

import com.shakepoint.web.data.dto.req.rest.PurchaseEventRequest;
import com.shakepoint.web.data.dto.req.rest.PurchaseRequest;
import com.shakepoint.web.data.dto.req.rest.UserProfileRequest;
import com.shakepoint.web.data.dto.res.rest.*;
import com.shakepoint.web.data.entity.Combo;
import com.shakepoint.web.data.entity.Product;
import com.shakepoint.web.data.entity.PurchaseQRCode;

import java.security.Principal;
import java.util.List;


public interface ShopFacade {
	 public MachineSearch searchMachine(double longitude, double latitude);
	 public List<Product> getMachineProducts(String machineId, int pageNumber);
	 public PurchaseResponse requestPurchase(PurchaseRequest request, Principal principal);
	 public List<PurchaseCodeResponse> getActiveQrCodes(Principal p, String machineId, int pageNumber);
	 public PurchaseQRCode confirmPurchase(PurchaseEventRequest request, Principal p);
	 public List<UserPurchaseResponse> getUserPurchases(Principal p, int pageNumber);
	 public UserProfile getUserProfile(Principal principal);
	 public UserProfile saveProfile(Principal p, UserProfileRequest request);
	 public List<Combo> getMachineCombos(String machineId, int pageNumber);
}
