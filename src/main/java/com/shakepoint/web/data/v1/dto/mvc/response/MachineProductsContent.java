package com.shakepoint.web.data.v1.dto.mvc.response;

import com.shakepoint.web.data.dto.res.MachineDTO;
import com.shakepoint.web.data.dto.res.MachineProduct;
import com.shakepoint.web.data.v1.dto.mvc.response.Technician;
import com.shakepoint.web.data.v1.entity.ShakepointMachine;
import com.shakepoint.web.data.v1.entity.ShakepointProduct;

import java.util.List;

public class MachineProductsContent {
	private int alertedProducts;
	private List<ShakepointMachine> machineProducts;
	private List<ShakepointProduct> products;
	private ShakepointMachine machine;
	private Technician technician;
	private List<Technician> technicians;
	public MachineProductsContent() {
		super();
	}
	public int getAlertedProducts() {
		return alertedProducts;
	}
	public void setAlertedProducts(int alertedProducts) {
		this.alertedProducts = alertedProducts;
	}
	public List<ShakepointMachine> getMachineProducts() {
		return machineProducts;
	}
	public void setMachineProducts(List<ShakepointMachine> machineProducts) {
		this.machineProducts = machineProducts;
	}
	public List<ShakepointProduct> getProducts() {
		return products;
	}
	public void setProducts(List<ShakepointProduct> products) {
		this.products = products;
	}
	public ShakepointMachine getMachine() {
		return machine;
	}
	public void setMachine(ShakepointMachine machine) {
		this.machine = machine;
	}
	public Technician getTechnician() {
		return technician;
	}
	public void setTechnician(Technician technician) {
		this.technician = technician;
	}
	public List<Technician> getTechnicians() {
		return technicians;
	}
	public void setTechnicians(List<Technician> technicians) {
		this.technicians = technicians;
	} 
	
	
}
