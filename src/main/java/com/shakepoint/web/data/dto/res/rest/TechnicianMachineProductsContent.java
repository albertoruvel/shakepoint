package com.shakepoint.web.data.dto.res.rest;

import com.shakepoint.web.data.dto.res.MachineDTO;
import com.shakepoint.web.data.dto.res.ProductDTO;

import java.util.List;

public class TechnicianMachineProductsContent {
	private List<ProductDTO> products;
	private MachineDTO machine;
	public TechnicianMachineProductsContent() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<ProductDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}
	public MachineDTO getMachine() {
		return machine;
	}
	public void setMachine(MachineDTO machine) {
		this.machine = machine;
	}
	
	
}
