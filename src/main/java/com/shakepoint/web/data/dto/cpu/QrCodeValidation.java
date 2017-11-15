package com.shakepoint.web.data.dto.cpu;

public class QrCodeValidation {
	private boolean validated;
	private String validationMessage; 
	
	public QrCodeValidation() {
		super();
		// TODO Auto-generated constructor stub
	}


	public boolean isValidated() {
		return validated;
	}


	public void setValidated(boolean validated) {
		this.validated = validated;
	}


	public String getValidationMessage() {
		return validationMessage;
	}


	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}
	
}
