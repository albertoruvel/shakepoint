package com.shakepoint.web.core.repository;

import com.shakepoint.web.data.v1.dto.rest.response.PurchaseCodeResponse;
import com.shakepoint.web.data.dto.res.rest.UserPurchaseResponse;
import com.shakepoint.web.data.v1.entity.ShakepointPurchase;
import com.shakepoint.web.data.v1.entity.ShakepointPurchaseQRCode;

import java.util.List;
import java.util.Map;

public interface PurchaseRepository {
	public ShakepointPurchase getPurchase(String purchaseId);

	public double getTodayTotalPurchases();
	public Map<String, List<Double>> getPerMachineValues(String[] range);
	public List<Double> getTotalIncomeValues(String[] range);
	public void createPurchase(ShakepointPurchase purchase);
	public List<ShakepointPurchaseQRCode> getActiveCodes(String userId, String machineId, int pageNumber);
	public void confirmPurchase(String purchaseId, String reference);
	public void createQrCode(ShakepointPurchaseQRCode code);
	public List<ShakepointPurchase> getUserPurchases(String userId, int pageNumber);

	public void update(ShakepointPurchase purchase);
}
