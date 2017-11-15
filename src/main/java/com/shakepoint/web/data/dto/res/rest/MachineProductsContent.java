package com.shakepoint.web.data.dto.res.rest;

import com.shakepoint.web.data.dto.res.MachineDTO;
import com.shakepoint.web.data.dto.res.MachineProduct;
import com.shakepoint.web.data.dto.res.TechnicianDTO;
import com.shakepoint.web.data.entity.Product;

import java.util.List;

public class MachineProductsContent {
	private int alertedProducts;
	private List<MachineProduct> machineProducts;
	private List<Product> products;
	private MachineDTO machine;
	private TechnicianDTO technician;
	private List<TechnicianDTO> technicians;
	public MachineProductsContent() {
		super();
	}
	public int getAlertedProducts() {
		return alertedProducts;
	}
	public void setAlertedProducts(int alertedProducts) {
		this.alertedProducts = alertedProducts;
	}
	public List<MachineProduct> getMachineProducts() {
		return machineProducts;
	}
	public void setMachineProducts(List<MachineProduct> machineProducts) {
		this.machineProducts = machineProducts;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public MachineDTO getMachine() {
		return machine;
	}
	public void setMachine(MachineDTO machine) {
		this.machine = machine;
	}
	public TechnicianDTO getTechnician() {
		return technician;
	}
	public void setTechnician(TechnicianDTO technician) {
		this.technician = technician;
	}
	public List<TechnicianDTO> getTechnicians() {
		return technicians;
	}
	public void setTechnicians(List<TechnicianDTO> technicians) {
		this.technicians = technicians;
	} 
	
	
}
