package com.shakepoint.web.data.dto.res.rest;

public class ConfirmationResponse {
	private boolean success; 
	private String message;
	public ConfirmationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	} 
	
	
}
