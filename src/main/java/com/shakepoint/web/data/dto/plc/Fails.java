package com.shakepoint.web.data.dto.plc;

public class Fails{
	
	private String engine; 
	private String piston; 
	private String sensor; 
	private String chiller; 
	private String filter;
	public Fails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getPiston() {
		return piston;
	}
	public void setPiston(String piston) {
		this.piston = piston;
	}
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public String getChiller() {
		return chiller;
	}
	public void setChiller(String chiller) {
		this.chiller = chiller;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	@Override
	public String toString() {
		return "Fail [engine=" + engine + ", piston=" + piston + ", sensor=" + sensor + ", chiller=" + chiller
				+ ", filter=" + filter + "]";
	} 
	
	
}
