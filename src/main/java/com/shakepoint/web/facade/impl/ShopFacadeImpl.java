package com.shakepoint.web.facade.impl;

import java.io.File;
import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.dto.req.rest.PurchaseEventRequest;
import com.shakepoint.web.data.dto.req.rest.PurchaseRequest;
import com.shakepoint.web.data.dto.req.rest.UserProfileRequest;
import com.shakepoint.web.data.dto.res.MachineDTO;
import com.shakepoint.web.data.dto.res.rest.*;
import com.shakepoint.web.data.entity.*;
import com.shakepoint.web.util.ShakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.shakepoint.web.core.qr.QrCodeCreator;
import com.shakepoint.web.facade.ShopFacade;
public class ShopFacadeImpl implements ShopFacade{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MachineRepository machineRepository;
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired 
	private ProductRepository productRepository;
	
	@Autowired 
	private QrCodeCreator qrCodeCreator; 

	
	@Override 
	public UserProfile saveProfile(Principal p, UserProfileRequest request){
		//get user id 
		final String userId = userRepository.getUserId(p.getName());
		Profile profile = getProfile(userId, request);
		UserProfile userProfile = null; 
		//check if user already has a profile 
		if(userRepository.hasProfile(userId)){
			userRepository.updateProfile(profile); 
			//return the current profile;
			userProfile = userRepository.getUserProfile(userId) ;
		}else{
			//save the current profile  
			userRepository.saveProfile(profile);
			userProfile = userRepository.getUserProfile(userId); 
		}
		return userProfile; 
	}
	
	private Profile getProfile(String userId, UserProfileRequest request){
		Profile profile = new Profile();
		profile.setAge(request.getAge());
		try{
			//creates a date 
			Date date = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(request.getBirthday());
			//format with shakepoint default format
			String birth = ShakeUtils.SIMPLE_DATE_FORMAT.format(date); 
			profile.setBirthday(birth);
		}catch(ParseException ex){

		}
		profile.setHeight(request.getHeight());
		profile.setUserId(userId);
		profile.setWeight(request.getWeight());
		return profile; 
	}
	
	@Override 
	public List<UserPurchaseResponse> getUserPurchases(Principal p, int pageNumber){
		List<UserPurchaseResponse> response = null;
		final String userId = userRepository.getUserId(p.getName()); 
		response = purchaseRepository.getUserPurchases(userId, pageNumber); 
		return response; 
	}
	
	@Override
	public PurchaseQRCode confirmPurchase(PurchaseEventRequest request, Principal p){
		PurchaseQRCode code = null; 
		//update the purchase
		
		//validates request
		purchaseRepository.confirmPurchase(request.getReference(), request.getId());
		//check if the purchased item is a combo package 
		/**if(purchaseRepository.isComboPurchase(request.getReference())){
			
		}else{
			
		}**/
		code = purchaseRepository.getCode(request.getReference());
		return code; 
	}
	
	@Override
	public PurchaseResponse requestPurchase(PurchaseRequest request, Principal principal){
		PurchaseResponse response = new PurchaseResponse();
		final String userId = userRepository.getUserId(principal.getName()); 
		Purchase purchase = null;
		Product product = null;
		PurchaseQRCode code = null; 
		String qrCode = "";
		String resourcesQrCode = ""; 
		List<Product> comboProducts = null;
		if(request == null){
			response.setMessage("There is no purchase content");
			response.setPurchaseId(null);
			response.setSuccess(false);
			response.setTotal(0.0);
		}else{
			//create a new purchase 
			purchase = getPurchase(request, userId);
			//check if the product is a combo 
			product = productRepository.getProduct(purchase.getProductId());
			//add it 
			purchaseRepository.createPurchase(purchase);
			if(product.isCombo()){
				//get combo products 
				comboProducts = productRepository.getComboProducts(product.getId(), 1); 
				//iterate to create a qr code with each product 
				for(Product p : comboProducts){
					//get a different qr code
					code = getQrCode(purchase);
					//create the code image 
					qrCode = qrCodeCreator.createQRCode(purchase.getId(), purchase.getMachineId(), 
							p.getId(), code.getId());
					//add qr code to resources folder 
					resourcesQrCode = moveQrCode(qrCode); 
					//update qr code 
					code.setImageUrl(resourcesQrCode);
					//add qr code 
					purchaseRepository.createQrCode(code);
				}
				//create response
				response = getPurchaseResponse("Purchase created as pre-authorized purchase", purchase.getId(), 
						true, purchase.getTotal(), "N/A"); 
			}else{
				//create the qr code 
				code = getQrCode(purchase);  
				qrCode = qrCodeCreator.createQRCode(purchase.getId(), purchase.getMachineId(), 
						purchase.getProductId(), code.getId());
				//add qr code to resources folder (MAPPED TO A URL BY THIS HOST)
				resourcesQrCode = moveQrCode(qrCode); 
				//update qr code 
				code.setImageUrl(resourcesQrCode);
				//addd qr cocde
				purchaseRepository.createQrCode(code); 
				//create a purchase response 
				response = getPurchaseResponse("Purchase created as pre-authorized purchase", purchase.getId(), 
						true, purchase.getTotal(), resourcesQrCode); 
			}
		}
		return response; 
	}
	
	private PurchaseResponse getPurchaseResponse(String message, String purchaseId, boolean success, double total, String qrCodeUrl){
		PurchaseResponse response = new PurchaseResponse();
		response.setMessage(message);
		response.setPurchaseId(purchaseId);
		response.setSuccess(success);
		response.setTotal(total);
	    response.setQrCodeUrl(qrCodeUrl);
	    return response; 
	}
	
	private String moveQrCode(String tmpPath){
		File file = new File(tmpPath);
		File resFile = new File(ShakeUtils.QR_CODES_RESOURCES_FOLDER + "/" +file.getName());
		 
		//create an input stream 
		try{
			file.renameTo(resFile); 
		}catch(Exception ex){
			
		}
		return String.format(ShakeUtils.LOCALHOST_QR_CODE_FORMAT, resFile.getName());  
	}
	
	private PurchaseQRCode getQrCode(Purchase purchase){
		PurchaseQRCode code = new PurchaseQRCode();
		code.setCashed(false);
		code.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
		code.setPurchaseId(purchase.getId());
		return code; 
	}
	
	private Purchase getPurchase(PurchaseRequest request, String userId) {
		Purchase purchase = new Purchase(); 
		purchase.setMachineId(request.getMachineId());
		purchase.setProductId(request.getProductId());
		purchase.setPurchaseDate(ShakeUtils.DATE_FORMAT.format(new Date()));
		purchase.setStatus(Purchase.PurchaseStatus.PRE_AUTHORIZED);
		purchase.setTotal(request.getPrice());
		purchase.setUserId(userId);
		
		return purchase;
	}

	@Override
	public MachineSearch searchMachine(double longitude, double latitude) {
		//get all machines
		List<MachineDTO> machines = machineRepository.getMachines(1);
		MachineSearch search = new MachineSearch();
		double distance = 1000000; // high distance to get accurate results 
		String[] array = null;  
		int currentIndex = 0; 
		for(int i = 0; i < machines.size(); i ++){
			array = machines.get(i).getLocation().split(",");
			double long1 = Double.parseDouble(array[0]); 
			double lat1 = Double.parseDouble(array[1]);
			//get distance 
			double tmpDistance = distance(lat1, long1, latitude, longitude);
			if(tmpDistance < distance){
				distance = tmpDistance;
				currentIndex = i; 
			}
				 
		} 
		MachineDTO machine = machines.get(currentIndex);
		search.setMachineId(machine.getId());
		search.setMachineName(machine.getName());
		
		return search; 
	}
	
	
	/**
	 * Get distance in mts from 2 points
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @param
	 * @return
	 */
	private static double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;

		return (dist);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}


	@Override
	public List<Product> getMachineProducts(String machineId, int pageNumber) {
		return productRepository.getProducts(machineId, pageNumber, Product.ProductType.SIMPLE);
	}

	@Override
	public List<PurchaseCodeResponse> getActiveQrCodes(Principal p, String machineId, int pageNumber) {
		//get user id 
		String userId = userRepository.getUserId(p.getName());
		List<PurchaseCodeResponse> page = purchaseRepository.getActiveCodes(userId, machineId, pageNumber);
		return page; 
	}

	@Override
	public UserProfile getUserProfile(Principal principal) {
		final String userId = userRepository.getUserId(principal.getName()); 
		UserProfile profile = null; 
		try{
			profile = userRepository.getUserProfile(userId);
			if(profile.isAvailableProfile()){
				profile.setPurchases(purchaseRepository.getUserPurchases(userId, 1));
			}
		}catch(Exception ex){
			
		}
		return profile;
	}

	@Override
	public List<Combo> getMachineCombos(String machineId, int pageNumber) {
		List<Combo> combos = null;
		try{
			combos = productRepository.getMachineCombos(machineId, pageNumber);
		}catch(Exception ex){

		}
		return combos;
	}
	
}
