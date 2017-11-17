/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

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

    
    public static final String TMP_FOLDER = "C:/shakepoint-tmp/";
    public static final String QR_CODES_TMP_FOLDER = TMP_FOLDER + "qr-tmp/";
    public static final String QR_CODES_RESOURCES_FOLDER = TMP_FOLDER + "qr/";
    
    public static final String LOGOS_RESOURCES_FOLDER = TMP_FOLDER + "logos/"; 
    
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy HH:mm");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
    public static final SimpleDateFormat SLASHES_SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    /**
	 * Creates a PLC Response from a LinkedHashMap
	 * @param map
	 * @param machineId
	 * @return
	 */
	/**public static PlcResponse getPlcResponse(LinkedHashMap<String, Object>map, String machineId){
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
	}**/
}
