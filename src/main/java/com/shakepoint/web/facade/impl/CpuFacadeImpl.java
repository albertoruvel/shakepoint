package com.shakepoint.web.facade.impl;

import java.security.Principal;

import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.data.dto.cpu.QrCodeValidation;
import com.shakepoint.web.data.dto.cpu.QrCodeValidationRequest;
import com.shakepoint.web.data.dto.plc.PlcResponse;
import com.shakepoint.web.data.entity.PurchaseQRCode;
import org.springframework.beans.factory.annotation.Autowired;

import com.shakepoint.web.facade.CpuFacade;

public class CpuFacadeImpl implements CpuFacade{
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	
	@Override 
	public QrCodeValidation validateQrCode(QrCodeValidationRequest request, Principal principal){
		QrCodeValidation validation = new QrCodeValidation();; 
		//check if code is already cashed, or unauthorized 
		if(purchaseRepository.validateQrCode(request.getQrCodeId())){
			//the qr code is valid to get cashed 
			validation.setValidated(true);
			validation.setValidationMessage("The QR Code is ready to get cashed");
		}else{
			validation.setValidated(false);
			validation.setValidationMessage("The QR Code has been cashed or is not authorized");
		}
		return validation; 
	}
	
	
	@Override 
	public String confirmMachineDispense(String purchaseId){
		//set purchase status as cashed
		purchaseRepository.updateQRStatus(purchaseId, PurchaseQRCode.QRCodeStatus.CASHED);
		return "OK"; 
	}


	@Override
	public String updateMachine(PlcResponse plcResponse) {

		return null;
	}


	@Override
	public String updateMachineStatus(String machineId, int status) {
		
		return null;
	}
}
