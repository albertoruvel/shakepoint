package com.shakepoint.web.data.v1.dto.rest.response;

public class MachineSearch {
	private String machineId; 
	private String machineName;
	private double distance;

	public MachineSearch() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}
}
