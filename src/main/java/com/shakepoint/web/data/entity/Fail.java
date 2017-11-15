package com.shakepoint.web.data.entity;

import com.shakepoint.web.data.dto.plc.Fails;

import java.util.UUID;

public class Fail {
	private String id;
	private String failDate; 
	private String message; 
	private String machineId; 
	
	//fails creates the message property using the toString() object method
	private Fails fail;

	public Fail() {
		id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFailDate() {
		return failDate;
	}

	public void setFailDate(String failDate) {
		this.failDate = failDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Fails getFail() {
		return fail;
	}

	public void setFail(Fails fail) {
		this.fail = fail;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	
	
}
