package com.shakepoint.web.data.dto.plc;

public class Level {
	private String product; 
	private String level;
	public Level() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Level(String product, String level) {
		super();
		this.product = product;
		this.level = level;
	}


	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	} 
	
	
}
