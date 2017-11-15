/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.util;

import com.shakepoint.web.data.dto.cpu.CpuRequest;
import com.shakepoint.web.data.dto.plc.Fails;
import com.shakepoint.web.data.dto.plc.Level;
import com.shakepoint.web.data.dto.plc.PlcResponse;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Alberto Rubalcaba
 */
public class ShakeUtils {
	
	public static final String SUPER_ADMIN_EMAIL = "albertoruvel@gmail.com"; 
	
    public static String encodePassword(String rawPassword){
        String genPassword = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha-256");
            messageDigest.update(rawPassword.getBytes());
            byte[] data = messageDigest.digest(); 
            StringBuilder sb = new StringBuilder(); 
            for(int i = 0; i < data.length; i ++){
                sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1)); 
            }
            genPassword = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
        }
        return genPassword;
    }
    
    public static final String[] STATES_OF_MEXICO = {
    	"Aguascalientes", "Baja California", "Baja California Sur", 
    	"Campeche", "Cohauila", "Colima", "Chiapas", "Chihuahua", 
    	"Distrito Federal", "Durango", "Guanajuato", "Guerrero", "Hidalgo", 
    	"Jalisco", "México", "Michoacán", "Morelos", "Nayarit", "Nuevo León", 
    	"Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí", 
    	"Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", 
    	"Yucatán", "Zacatecas"
    }; 
    
    public static CpuRequest getCpuTurnonRequest(String machineId){
    	CpuRequest request = new CpuRequest();
    	request.setCommand(CpuRequest.Command.TURNON);
    	request.setId(machineId);
    	request.setProduct(null);
    	return request;
    }
    
    public static String getCurrentHostIp(){
    	String ip = ""; 
    	try{
    		InetAddress address = InetAddress.getLocalHost();
    		ip = address.getHostAddress(); 
    	}catch(Exception ex){
    		
    	}
    	return ip; 
    }
    
    public static CpuRequest getCpuShutdownRequest(String machineId){
    	CpuRequest request = new CpuRequest(); 
    	request.setCommand(CpuRequest.Command.SHUTDOWN);
    	request.setId(machineId);
    	request.setProduct(null);
    	return request;
    }
    
    public static CpuRequest getCpuStatusRequest(String machineId){
    	CpuRequest request = new CpuRequest(); 
    	request.setCommand(CpuRequest.Command.STATUS);
    	request.setId(machineId);
    	request.setProduct(null);
    	return request;
    }
    
    public static CpuRequest getCpuDispenseRequest(String machineId, String productId){
    	CpuRequest request = new CpuRequest(); 
    	request.setCommand(CpuRequest.Command.DISPENSE);
    	request.setId(machineId);
    	request.setProduct(productId);
    	return request;
    }
    
    public static final String LOCALHOST_QR_CODE_FORMAT = "http://" + ShakeUtils.getCurrentHostIp() + ":8080/static/codes/%s";
    public static final String PRODUCTION_QR_CODE_FORMAT = "http://52.89.178.166/static/codes/%s";
    
    public static final String LOCALHOST_LOGOS_FORMAT = "http://" + ShakeUtils.getCurrentHostIp() + ":8080/static/logos/%s";
    public static final String PRODUCTION_LOGOS_FORMAT = "http://52.89.178.166/static/logos/%s"; 
    
    public static final String TMP_FOLDER = "C:/shakepoint-tmp/";
    public static final String SHARED_FOLDER = TMP_FOLDER + "properties/shared/"; 
    public static final String QR_CODES_TMP_FOLDER = TMP_FOLDER + "qr-tmp/";
    public static final String QR_CODES_RESOURCES_FOLDER = TMP_FOLDER + "qr/";
    public static final String QR_CODES_RESOURCES_PRODUCTION_FOLDER = ""; 
    
    public static final String LOGOS_RESOURCES_FOLDER = TMP_FOLDER + "logos/"; 
    
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy HH:mm");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
    public static final SimpleDateFormat SLASHES_SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    public static final boolean DEBUG = false; 
    
    public static String getHostQrCodeFormat(){
    	if(DEBUG)
    		return LOCALHOST_QR_CODE_FORMAT; 
    	else 
    		return PRODUCTION_QR_CODE_FORMAT; 
    }
    
    public static String getHostLogosFormat(){
    	if(DEBUG)return LOCALHOST_LOGOS_FORMAT; 
    	else return PRODUCTION_LOGOS_FORMAT; 
    }
    /**
	 * Creates a PLC Response from a LinkedHashMap
	 * @param map
	 * @param machineId
	 * @return
	 */
	public static PlcResponse getPlcResponse(LinkedHashMap<String, Object>map, String machineId){
		PlcResponse plcResponse = new PlcResponse();
		plcResponse.setDispense((String)map.get("dispense"));
		LinkedHashMap<String, Object> failsMap = (LinkedHashMap<String, Object>)map.get("fails");
		Fails fails = new Fails();
		fails.setChiller((String)failsMap.get("chiller"));
		fails.setEngine((String)failsMap.get("engine"));
		fails.setFilter((String)failsMap.get("filter"));
		fails.setPiston((String)failsMap.get("piston"));
		fails.setSensor((String)failsMap.get("sensor"));
		plcResponse.setFails(fails);
		plcResponse.setId((String)map.get("id"));
		List<Map<String, Object>> levelsMap = (List<Map<String, Object>>)map.get("levels");
		Level level = null;
		List<Level> levels = new ArrayList();
		for(Map<String, Object> levelMap : levelsMap){
			level = new Level((String)levelMap.get("product"), (String)levelMap.get("level"));
			levels.add(level); 
		}
		plcResponse.setLevels(levels);
		plcResponse.setMachineId(machineId);
		plcResponse.setProductId((String)map.get("productId"));
		return plcResponse; 
	}
}
