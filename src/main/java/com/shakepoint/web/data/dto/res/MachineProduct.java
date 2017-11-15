package com.shakepoint.web.data.dto.res;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
		name = "MachineProductMapping",
		columns = {
				@ColumnResult(name = "id"),
				@ColumnResult(name = "productId"),
				@ColumnResult(name = "machineId"),
				@ColumnResult(name = "slotNumber"),
				@ColumnResult(name = "productName"),
				@ColumnResult(name = "productPrice"),
				@ColumnResult(name = "productLogoUrl")
		}
)
public class MachineProduct {
	
	private boolean success;
	private String message; 
	private String id; 
	private String productId;
	private String machineId; 
	private int slotNumber;
	private String creationDate; 
	private boolean combo; 
	
	private String productName; 
	private double productPrice;
	private String productLogoUrl; 
	
	public MachineProduct() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public int getSlotNumber() {
		return slotNumber;
	}
	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductLogoUrl() {
		return productLogoUrl;
	}
	public void setProductLogoUrl(String productLogoUrl) {
		this.productLogoUrl = productLogoUrl;
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
	public boolean isCombo() {
		return combo;
	}
	public void setCombo(boolean combo) {
		this.combo = combo;
	} 
	
	
}
