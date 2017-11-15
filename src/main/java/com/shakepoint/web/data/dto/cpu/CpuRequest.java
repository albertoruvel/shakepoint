package com.shakepoint.web.data.dto.cpu;

public class CpuRequest {
	
	private String id;
	private Command command; 
	private String product;
	
	public CpuRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Command getCommand() {
		return command;
	}
	public void setCommand(Command command) {
		this.command = command;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public static enum Command{
		TURNON("turnon"), SHUTDOWN("shutdown"), STATUS("status"), IDLE("idle"), OVERWRITE("overwrite"), DISPENSE("dispense"); 
		String value; 
		Command(String value){
			this.value = value; 
		}
		
		public static Command parse(String command){
			if(command.toLowerCase().equals("turnon"))
				return Command.TURNON; 
			else if(command.toLowerCase().equals("shutdown"))
				return Command.SHUTDOWN;
			else if(command.toLowerCase().equals("status"))
				return Command.STATUS;
			else if(command.toLowerCase().equals("idle"))
				return Command.IDLE;
			else if(command.toLowerCase().equals("overwrite"))
				return Command.OVERWRITE;
			else return Command.DISPENSE; 
		}
	}
}
