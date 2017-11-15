package com.shakepoint.web.core.qr;

public interface QrCodeCreator {
	public String createQRCode(String purchaseid, String machineId, String productId, String qrCodeId);
	public String readQrCode(String qrCodeId); 
}
