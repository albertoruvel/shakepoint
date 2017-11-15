package com.shakepoint.web.data.dto.res.rest;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "UserPurchaseResponseMapper", columns = {
		@ColumnResult(name = "purchaseId"),
		@ColumnResult(name = "productid"),
		@ColumnResult(name = "machineId"),
		@ColumnResult(name = "total"),
		@ColumnResult(name = "productName"),
		@ColumnResult(name = "machineName"),
		@ColumnResult(name = "purchaseDate"),
		@ColumnResult(name = "cashed"),
		@ColumnResult(name = "qrCodeUrl"),
		@ColumnResult(name = "productLogoUrl"),
		@ColumnResult(name = "status"),
})
public class UserPurchaseResponse {
	
	private String purchaseId; 
	private String productid; 
	private String machineId; 
	private double total; 
	private String productName; 
	private String machineName; 
	private String purchaseDate; 
	private boolean cashed;
	private String qrCodeUrl; 
	private String productLogoUrl;
	private int status; 
	
	public UserPurchaseResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public boolean isCashed() {
		return cashed;
	}
	public void setCashed(boolean cashed) {
		this.cashed = cashed;
	}
	public String getProductLogoUrl() {
		return productLogoUrl;
	}
	public void setProductLogoUrl(String productLogoUrl) {
		this.productLogoUrl = productLogoUrl;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getQrCodeUrl() {
		return qrCodeUrl;
	}
	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	} 
	
	
}
