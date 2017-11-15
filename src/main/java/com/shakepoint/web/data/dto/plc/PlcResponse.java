package com.shakepoint.web.data.dto.plc;

import java.util.List;

/**
 * This class is sent from the PLC to the CPU 
 * @author Alberto
 *
 */
public class PlcResponse {
	private String productId; 
	private String id; //machine id
	private List<Level> levels;
	private Fails fails; 
	private String dispense;
	private String machineId; 
	public PlcResponse() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Level> getLevels() {
		return levels;
	}
	public void setLevels(List<Level> levels) {
		this.levels = levels;
	}
	public Fails getFails() {
		return fails;
	}
	public void setFails(Fails fails) {
		this.fails = fails;
	}
	public String getDispense() {
		return dispense;
	}
	public void setDispense(String dispense) {
		this.dispense = dispense;
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
	
	
}
