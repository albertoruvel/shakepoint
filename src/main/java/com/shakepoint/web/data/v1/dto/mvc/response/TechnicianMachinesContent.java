package com.shakepoint.web.data.v1.dto.mvc.response;

import com.shakepoint.web.data.v1.entity.ShakepointMachine;

import java.util.List;

public class TechnicianMachinesContent {
	private Technician technician;
	private List<ShakepointMachine> allMachines;
	private List<ShakepointMachine> asignedMachines;
	public TechnicianMachinesContent() {
		super();
	}
	public Technician getTechnician() {
		return technician;
	}
	public void setTechnician(Technician technician) {
		this.technician = technician;
	}
	public List<ShakepointMachine> getAllMachines() {
		return allMachines;
	}
	public void setAllMachines(List<ShakepointMachine> allMachines) {
		this.allMachines = allMachines;
	}
	public List<ShakepointMachine> getAsignedMachines() {
		return asignedMachines;
	}
	public void setAsignedMachines(List<ShakepointMachine> asignedMachines) {
		this.asignedMachines = asignedMachines;
	} 
	
}
