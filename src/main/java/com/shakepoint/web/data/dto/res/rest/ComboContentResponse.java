package com.shakepoint.web.data.dto.res.rest;

import com.shakepoint.web.data.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ComboContentResponse {
	private Product combo;
	private List<Product> products;
	private List<Product> comboProducts;
	
	
	public ComboContentResponse() {
		super();
		products = new ArrayList();
		comboProducts = new ArrayList();
	}
	public Product getCombo() {
		return combo;
	}
	public void setCombo(Product combo) {
		this.combo = combo;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public List<Product> getComboProducts() {
		return comboProducts;
	}
	public void setComboProducts(List<Product> comboProducts) {
		this.comboProducts = comboProducts;
	}
	
	
}
