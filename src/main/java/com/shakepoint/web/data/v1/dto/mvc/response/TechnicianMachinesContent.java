package com.shakepoint.web.data.v1.dto.mvc.response;


import com.shakepoint.web.data.dto.res.MachineDTO;
import com.shakepoint.web.data.v1.dto.mvc.response.Technician;

import java.util.List;

public class TechnicianMachinesContent {
	private Technician technician;
	private List<MachineDTO> allMachines;
	private List<MachineDTO> asignedMachines;
	public TechnicianMachinesContent() {
		super();
	}
	public Technician getTechnician() {
		return technician;
	}
	public void setTechnician(Technician technician) {
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
