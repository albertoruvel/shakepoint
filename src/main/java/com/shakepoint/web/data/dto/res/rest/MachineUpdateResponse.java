package com.shakepoint.web.data.dto.res.rest;

import com.shakepoint.web.data.dto.res.CpuQRCode;

import java.util.List;

public class MachineUpdateResponse {
	private String message; 
	private List<CpuQRCode> codes;
	
	public MachineUpdateResponse() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<CpuQRCode> getCodes() {
		return codes;
	}

	public void setCodes(List<CpuQRCode> codes) {
		this.codes = codes;
	}
	
	
}
