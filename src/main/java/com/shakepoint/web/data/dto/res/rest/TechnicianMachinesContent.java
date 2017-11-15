package com.shakepoint.web.data.dto.res.rest;


import com.shakepoint.web.data.dto.res.MachineDTO;
import com.shakepoint.web.data.dto.res.TechnicianDTO;

import java.util.List;

public class TechnicianMachinesContent {
	private TechnicianDTO technician;
	private List<MachineDTO> allMachines;
	private List<MachineDTO> asignedMachines;
	public TechnicianMachinesContent() {
		super();
	}
	public TechnicianDTO getTechnician() {
		return technician;
	}
	public void setTechnician(TechnicianDTO technician) {
		this.technician = technician;
	}
	public List<MachineDTO> getAllMachines() {
		return allMachines;
	}
	public void setAllMachines(List<MachineDTO> allMachines) {
		this.allMachines = allMachines;
	}
	public List<MachineDTO> getAsignedMachines() {
		return asignedMachines;
	}
	public void setAsignedMachines(List<MachineDTO> asignedMachines) {
		this.asignedMachines = asignedMachines;
	} 
	
}
