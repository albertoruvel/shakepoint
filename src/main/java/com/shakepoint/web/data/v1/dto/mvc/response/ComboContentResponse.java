package com.shakepoint.web.data.v1.dto.mvc.response;

import com.shakepoint.web.data.v1.entity.ShakepointProduct;

import java.util.ArrayList;
import java.util.List;

public class ComboContentResponse {
	private MachineProductData combo;
	private List<ShakepointProduct> products;
	private List<ShakepointProduct> comboProducts;
	
	
	public ComboContentResponse() {
		super();
		products = new ArrayList();
		comboProducts = new ArrayList();
	}
	public MachineProductData getCombo() {
		return combo;
	}
	public void setCombo(MachineProductData combo) {
		this.combo = combo;
	}
	public List<ShakepointProduct> getProducts() {
		return products;
	}
	public void setProducts(List<ShakepointProduct> products) {
		this.products = products;
	}
	public List<ShakepointProduct> getComboProducts() {
		return comboProducts;
	}
	public void setComboProducts(List<ShakepointProduct> comboProducts) {
		this.comboProducts = comboProducts;
	}
	
	
}
