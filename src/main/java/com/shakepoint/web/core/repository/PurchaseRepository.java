package com.shakepoint.web.core.repository;

import com.shakepoint.web.data.dto.res.CpuQRCode;
import com.shakepoint.web.data.dto.res.rest.PurchaseCodeResponse;
import com.shakepoint.web.data.dto.res.rest.UserPurchaseResponse;
import com.shakepoint.web.data.entity.Purchase;
import com.shakepoint.web.data.entity.PurchaseQRCode;

import java.util.List;
import java.util.Map;

public interface PurchaseRepository {
	public Purchase getPurchase(String purchaseId);
	public boolean validateQrCode(String qrCodeId); 
	public double getTodayTotalPurchases();
	public Map<String, List<Double>> getPerMachineValues(String[] range);
	public List<Double> getTotalIncomeValues(String[] range);
	public void createPurchase(Purchase purchase); 
	public List<PurchaseCodeResponse> getActiveCodes(String userId, String machineId, int pageNumber);
	public void confirmPurchase(String purchaseId, String reference);
	public PurchaseQRCode getCode(String purchaseId);
	public void createQrCode(PurchaseQRCode code);
	public List<UserPurchaseResponse> getUserPurchases(String userId, int pageNumber);
	public PurchaseQRCode getQrCode(String qrCode);
	public void updateQRStatus(String purchaseId, PurchaseQRCode.QRCodeStatus cashed);
	public List<CpuQRCode> getQrCodes(PurchaseQRCode.QRCodeStatus status, String machineId);
}
