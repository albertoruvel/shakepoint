package com.shakepoint.web.data.v1.dto.mvc.response;

import com.shakepoint.web.data.v1.dto.rest.response.SimpleMachineProduct;
import com.shakepoint.web.data.v1.entity.ShakepointMachine;
import com.shakepoint.web.data.v1.entity.ShakepointProduct;

import java.util.List;

public class MachineProductsContent {
	private int alertedProducts;
	private List<SimpleMachineProduct> machineProducts;
	private List<SimpleProduct> products;
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
	public List<SimpleMachineProduct> getMachineProducts() {
		return machineProducts;
	}
	public void setMachineProducts(List<SimpleMachineProduct> machineProducts) {
		this.machineProducts = machineProducts;
	}
	public List<SimpleProduct> getProducts() {
		return products;
	}
	public void setProducts(List<SimpleProduct> products) {
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
