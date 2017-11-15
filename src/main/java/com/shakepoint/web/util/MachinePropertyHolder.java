package com.shakepoint.web.util;

public class MachinePropertyHolder {
	private String currentMachineId; 
	private int currentMachinePort;
	private String serverIp;
	private String productionServerIp; 
	private int serverPort; 
	private String userId; 
	private String userPassword; 
	
	public MachinePropertyHolder() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCurrentMachineId() {
		return currentMachineId;
	}
	public void setCurrentMachineId(String currentMachine) {
		this.currentMachineId = currentMachine;
	}
	public int getCurrentMachinePort() {
		return currentMachinePort;
	}
	public void setCurrentMachinePort(int currentMachinePort) {
		this.currentMachinePort = currentMachinePort;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getProductionServerIp() {
		return productionServerIp;
	}
	public void setProductionServerIp(String productionServerIp) {
		this.productionServerIp = productionServerIp;
	} 
	
	
}
