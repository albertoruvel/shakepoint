package com.shakepoint.web.data.v1.dto.rest.request;

public class PurchaseEventRequest {
	private String id; 
	private int authCode; 
	private String reference; 
	private String total; 
	private String event;
	public PurchaseEventRequest() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAuthCode() {
		return authCode;
	}
	public void setAuthCode(int authCode) {
		this.authCode = authCode;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	} 
	
	
}
