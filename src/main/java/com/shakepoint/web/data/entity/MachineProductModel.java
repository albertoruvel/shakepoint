package com.shakepoint.web.data.entity;

import java.util.UUID;

public class MachineProductModel {
	private String id;
	private int slotNumber; 
	private String updatedBy; 
	private int availablePercentage; 
	private String machineId; 
	private String productId;
	public MachineProductModel() {
		id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSlotNumber() {
		return slotNumber;
	}
	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public int getAvailablePercentage() {
		return availablePercentage;
	}
	public void setAvailablePercentage(int availablePercentage) {
		this.availablePercentage = availablePercentage;
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
