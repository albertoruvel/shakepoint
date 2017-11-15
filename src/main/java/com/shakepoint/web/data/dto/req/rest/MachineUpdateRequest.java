package com.shakepoint.web.data.dto.req.rest;

public class MachineUpdateRequest {
	private String machineId; 
	private String productId;
	public MachineUpdateRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	} 
	
	
}
