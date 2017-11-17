package com.shakepoint.web.data.v1.dto.rest.response;

public class AuthenticationResult {
	private String message; 
	private String authenticationToken;
	private boolean success;
	
	public AuthenticationResult() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}


	
	
	
}
