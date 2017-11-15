package com.shakepoint.web.util;

import com.shakepoint.web.data.dto.plc.PlcProduct;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
	public static final String SHARED_FILE = ShakeUtils.SHARED_FOLDER + "machine-products.txt";
	
	public static void writeMachineProducts(List<PlcProduct> products)throws IOException{
		File file = new File(SHARED_FILE);
		if(! file.exists())
			file.createNewFile(); 
		//create a buffered writer   
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
		//add the number of products 
		writer.write(String.valueOf(products.size()));
		writer.newLine();
		
		//write all products 
		for(PlcProduct plc : products){
			writer.write(formatProduct(plc));
			writer.newLine(); 
		}
		writer.flush();
		writer.close(); 
	}
	
	public static List<PlcProduct> readMachineProducts()throws IOException{
		List<PlcProduct> products = new ArrayList();
		BufferedReader reader = new BufferedReader(new FileReader(new File(SHARED_FILE))); 
		//read number of products 
		int productsSize = Integer.parseInt(reader.readLine());
		for(int i = 0; i < productsSize; i ++){
			//read the new line 
			String line = reader.readLine(); 
			PlcProduct product = parseProduct(line); 
			products.add(product); 
		}
		reader.close(); 
		return products; 
	}
	
	private static PlcProduct parseProduct(String line){
		String[] array = line.split("__"); 
		PlcProduct product = new PlcProduct();
		product.setProductId(array[0]);
		product.setName(array[1]); 
		product.setDescription(array[2]);
		product.setAvailablePercentage(Integer.parseInt(array[3]));
		product.setSlotNumber(Integer.parseInt(array[4]));
		return product; 
	}
	
	private static String formatProduct(PlcProduct product){
		StringBuilder builder = new StringBuilder();
		builder.append(product.getProductId()); 
		builder.append("__"); 
		builder.append(product.getName()); 
		builder.append("__");
		builder.append(product.getDescription()); 
		builder.append("__"); 
		builder.append(product.getAvailablePercentage());
		builder.append("__"); 
		builder.append(product.getSlotNumber()); 
		return builder.toString(); 
	}
}
