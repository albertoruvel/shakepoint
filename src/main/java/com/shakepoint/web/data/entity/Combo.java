package com.shakepoint.web.data.entity;


import java.util.ArrayList;
import java.util.List;

public class Combo extends Product{
	
	private List<Product> items;
	static final int MAX_SIZE = 6;
	
	
	public Combo(Product p){
		super.setId(p.getId());
		this.combo = true; 
		this.creationDate = p.creationDate; 
		this.description = p.description; 
		this.logoUrl = p.logoUrl; 
		this.name = p.name; 
		this.price = p.price; 
		this.productType = p.productType;
	}
	
	public Combo() {
		super();
		items = new ArrayList<Product>();
	}

	public List<Product> getItems() {
		return items;
	}
	
	
	public void setItems(List<Product> items) {
		this.items = items;
	}
	
	@Override
	public boolean isCombo(){
		return true; 
	}
	
	public double getSavings(){
		//iterate over products to get a total 
		double total = 0.0; 
		for(Product p : items)
			total += p.getPrice().doubleValue();
		return total - this.price.doubleValue(); 
		
	}
	
	
}
