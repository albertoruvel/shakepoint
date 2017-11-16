/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.facade.impl;

import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.dto.res.*;
import com.shakepoint.web.data.dto.res.rest.*;
import com.shakepoint.web.data.entity.Machine;
import com.shakepoint.web.data.entity.MachineProductModel;
import com.shakepoint.web.data.entity.Product;
import com.shakepoint.web.data.entity.User;
import com.shakepoint.web.data.security.SecurityRole;
import com.shakepoint.web.facade.AdminFacade;

import com.shakepoint.web.util.ShakeUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Alberto Rubalcaba
 **/
public class AdminFacadeImpl implements AdminFacade{

	@Autowired
    private ProductRepository productRepository;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private MachineRepository machineRepository;
	
	@Autowired
    private PurchaseRepository purchaseRepository;

    private final Logger log = Logger.getLogger(getClass());
    
    @Override 
    public TotalIncomeValues getTotalIncomeValues(String from, String to, SimpleDateFormat sdf){
    	TotalIncomeValues values = null;
    	List<Double> doubleValues = null; 
    	String[] range = null; 
    	Date fromDate = null; 
    	Date toDate = null; 
    	try{
    		fromDate = sdf.parse(from); 
    		toDate = sdf.parse(to); 
    		range = getDateRange(fromDate, toDate);
    		//get values 
    		doubleValues = purchaseRepository.getTotalIncomeValues(range);
    		values = new TotalIncomeValues(); 
    		values.setRange(range);
    		values.setValues(doubleValues);
    		
    	}catch(Exception ex){
    		return null; 
    	}
    	return values; 
    }
    
    
    
    @Override 
    public PerMachineValues getIndexPerMachineValues(String from, String to, SimpleDateFormat sdf){
    	Map<String, List<Double>> map = null;
    	PerMachineValues values = new PerMachineValues();
    	//get the range 
    	String[] range = null;
    	Date fromDate = null; 
    	Date toDate = null; 
    	try{
    		fromDate = sdf.parse(from);
    		toDate = sdf.parse(to); 
    		range = getDateRange(fromDate, toDate);
    		map = purchaseRepository.getPerMachineValues(range); 
    		values.setFromDate(from);
    		values.setRange(range);
    		values.setToDate(to);
    		values.setValues(map);
    	}catch(ParseException ex){
    		return null; 
    	} 	
    	
    	return values; 
    }
    
    @Override 
    public AdminIndexContent getIndexContent(){
    	AdminIndexContent content = new AdminIndexContent();
    	content.setActiveMachines(machineRepository.getActiveMachines());
    	content.setAlertedMachines(machineRepository.getAlertedMachines());
    	content.setRegisteredTechnicians(userRepository.getRegisteredTechnicians());
    	content.setTodayTotal(purchaseRepository.getTodayTotalPurchases());
    	//per machine values 
    	PerMachineValues perMachine = new PerMachineValues();
    	Calendar calendar = Calendar.getInstance();
    	//create range dates 
    	final Date toDate = calendar.getTime();  
    	 
    	calendar.add(Calendar.DAY_OF_YEAR, -7);
    	final Date fromDate = calendar.getTime();  
    	//create a range array 
    	String[] range = getDateRange(fromDate, toDate); 
    	
    	/**perMachine.setFromDate(ShakeUtils.SIMPLE_DATE_FORMAT.format(fromDate));
    	perMachine.setToDate(ShakeUtils.SIMPLE_DATE_FORMAT.format(toDate));
    	perMachine.setRange(range);
    	Map<String, List<Double>> values = purchaseRepository.getPerMachineValues(range);
    	
    	perMachine.setValues(values);**/
    	String from = ShakeUtils.SIMPLE_DATE_FORMAT.format(fromDate);
    	String to = ShakeUtils.SIMPLE_DATE_FORMAT.format(toDate); 
    	perMachine = getIndexPerMachineValues(from, to, ShakeUtils.SIMPLE_DATE_FORMAT);
    	
    	content.setPerMachineValues(perMachine);
    	TotalIncomeValues totalIncome = getTotalIncomeValues(from, to, ShakeUtils.SIMPLE_DATE_FORMAT); 
    	totalIncome.setRange(range);
    	
    	content.setTotalIncomeValues(totalIncome);
    	
    	return content;
    }
    
    private String[] getDateRange(Date from, Date to){
    	//get day difference
    	int days = Math.round((to.getTime() - from.getTime()) / 86400000); //convert to days 
    	final String[] range = new String[days];
    	long time = from.getTime();
    	String date = ""; 
    	for(int i = 0; i < days; i ++){
    		date = ShakeUtils.SIMPLE_DATE_FORMAT.format(new Date(time));
    		//add the date to the range array 
    		range[i] = date; 
    		time = time + 86400000; 
    	}
    	return range;
    }
    
    @Override 
    public ModelAndView getShakepointUsers(int pageNumber){
    	ModelAndView mav = new ModelAndView("/admin/users");
    	
    	List<ShakePointUser> page = null;
    	try{
    		page = userRepository.getUsers(pageNumber);
    		mav.addObject("users", page); 
    	}catch(Exception ex){
    		
    	}
    	return mav; 
    }
    
    @Override 
    public TechnicianMachinesContent getTechnicianMachinesContent(String techId){
    	TechnicianMachinesContent content = new TechnicianMachinesContent();
    	//get technician 
    	TechnicianDTO dto = userRepository.getTechnician(techId);
    	content.setTechnician(dto);
    	List<MachineDTO> allMachines = machineRepository.getMachines(1);
    	content.setAllMachines(allMachines);
        List<MachineDTO> asignedMachines = machineRepository.getTechnicianAsignedMachines(techId, 1);
    	content.setAsignedMachines(asignedMachines);
    	return content; 
    }
    
    @Override 
    public ModelAndView getTechnicianMachinesView(String techId){
    	ModelAndView mav = new ModelAndView("/admin/technician-machines");
    	mav.addObject("technician_id", techId); 
    	return mav; 
    }
    
    
    @Override
    public MachineProduct addMachineProduct(String machineId, String productId, int slotNumber, Principal principal){
    	final String userId = userRepository.getUserId(principal.getName());
    	MachineProduct response = null;
    	MachineProductModel entity = new MachineProductModel();
    	entity.setAvailablePercentage(100);
    	entity.setMachineId(machineId);
    	entity.setProductId(productId);
    	entity.setSlotNumber(slotNumber);
    	String message = "No se puede agregar el paquete a la m�quina. \nLa m�quina debe de contener los productos del paquete para ofrecerlos: \n";
    	String productsMessage = ""; 
    	//check the product 
    	Product p = productRepository.getProduct(productId);
    	if(p.isCombo()){
    		boolean success = true; 
    		//check if the machine already has the needed products 
    		List<Product> products = productRepository.getComboProducts(p.getId(), 1);
    		for(Product cp : products){
    			if(! machineRepository.containProduct(machineId, cp.getId())){
        			productsMessage += cp.getName() + "\n";
        			if(success)
        				success = false; 
        		}
    		}
    		if(success){
    			//add the relationship
    			entity.setUpdatedBy(userId);
    			// add the new entity 
    	    	machineRepository.addMachineProduct(entity);
    	    	response = machineRepository.getMachineProduct(entity.getId());
    	    	response.setSuccess(success);
    	    	response.setCombo(true);
    	    	
    		}else{
    			response = new MachineProduct(); 
    			response.setSuccess(false); 
    			response.setMessage(message + productsMessage);
    		}
    	}else{
    		//product is not a combo
    		//add the relationship
			entity.setUpdatedBy(userId);
			// add the new entity 
	    	machineRepository.addMachineProduct(entity);
	    	response = machineRepository.getMachineProduct(entity.getId());
	    	response.setCombo(false);
	    	response.setSuccess(true);
    	}
    	
    	return response; 
    }
    
    @Override
    public Product deleteMachineProduct(String id){
    	Product p = null;
    	
    	//delete the machine product 
    	machineRepository.deleteMachineProduct(id);
        return productRepository.getProduct(id);
    }

	@Override
    public ModelAndView getProducts(int pageNumber) {
        if(pageNumber <= 0)
            pageNumber = 1; 
        ModelAndView mav = new ModelAndView("admin/products"); 
        //get a products page
        List<Product> page = null;
        try{
            page = productRepository.getProducts(pageNumber); 
            mav.addObject("products", page); 
        }catch(Exception ex){
            log.error("Could not get products: " + ex.getMessage());
            mav.addObject("error", "Could not get products list");
            mav.setViewName("admin/error");   
        }
        return mav; 
    }

    @Override
    public ModelAndView getNewProductView() {
        ModelAndView mav = new ModelAndView("admin/new-product"); 
        //create a new product 
        Product product = new Product();
        mav.addObject("product", product);
        
        return mav; 
    }
    
    @Override
    public ModelAndView getComboView(String productId){
    	ModelAndView mav = new ModelAndView("/admin/combo");
    	//get product 
    	Product p = productRepository.getProduct(productId); 
    	if(p.isCombo()){
    		mav.addObject("productId", productId); 
    	}else{
    		mav.addObject("error", "Este producto no es un paquete, asegurate de seleccionar un producto que se haya definido como paquete"); 
    	}
    	return mav; 
    }
    
    private String createFile(String productId, MultipartFile file)throws IOException{
    	//write the file 
    	String url = String.format(ShakeUtils.LOCALHOST_LOGOS_FORMAT, productId + ".png"); 
    	File createdFile = new File(ShakeUtils.LOGOS_RESOURCES_FOLDER + productId + ".png"); 
    	createdFile.createNewFile(); 
    	FileOutputStream stream = new FileOutputStream(createdFile); 
    	stream.write(file.getBytes());
    	stream.close();
    	return url; 
    }
    
    @Override
    public ModelAndView createNewProduct(Product product, RedirectAttributes atts, MultipartFile file){
        ModelAndView mav = null;
        String createdId = ""; 
        //validate product
        if(product != null){
            //validate product
            if(validateProduct(product, file)){
            	if(file != null){
            		//create the file on logos folder 
            		byte[] bytes = null; 
            		try{
            			//create the file
            			bytes = file.getBytes();
            			//save the file 
            			String filePath = createFile(product.getId(), file);
            			product.setLogoUrl(filePath);
            		}catch(Exception ex){
            			log.error("Could not write logo file");
            		} 
            		
            	}
            	//product is valid
                if(product.isCombo()){
                	//create a new combo
                	product.setProductType(Product.ProductType.COMBO);
                	//save the product
                    createdId = productRepository.createProduct(product);
                    mav = new ModelAndView("redirect:/admin/success");
                    atts.addFlashAttribute("message", "Se ha creado el paquete con ID: " + createdId); 
                    atts.addFlashAttribute("newCombo", createdId); 
                }else{
                	product.setProductType(Product.ProductType.SIMPLE);
                	createdId = productRepository.createProduct(product);
                    mav = new ModelAndView("redirect:/admin/success");
                    atts.addFlashAttribute("message", "Se ha guardado el producto con ID: " + createdId);
                }
            }else{
                //product is not valid
                mav = new ModelAndView("redirect:/admin/error");
                atts.addFlashAttribute("error", "No se han llenado todos los campos");
            }
        }else{
            //product is null
            mav = new ModelAndView("redirect:/admin/error");
            atts.addFlashAttribute("error", "No se ha podido crear producto");
        }
        return mav;
    }
    
    @Override
    public ModelAndView getTechnicians(int pageNumber){
        ModelAndView mav = new ModelAndView(); 
        
        if(pageNumber <= 0)
            pageNumber = 1; 
        
        List<TechnicianDTO> techs = null;
        try{
            techs = userRepository.getTechnicians(pageNumber);
            mav.setViewName("admin/technicians");
            mav.addObject("technicians", techs);
        }catch(Exception ex){
            log.error("Could not get technicians: " + ex.getMessage());
            mav.setViewName("admin/technicians");
            mav.addObject("error", "No se pudieron obtener los t�cnicos de ShakePoint");
        }
        return mav; 
    }
    
    @Override
    public ModelAndView createTechnicianView(){
        ModelAndView mav = new ModelAndView("admin/new-technician"); 
        TechnicianDTO dto = new TechnicianDTO(); 
        mav.addObject("technician", dto);
        return mav; 
    }
    
    @Override
    public ModelAndView createNewTechnician(TechnicianDTO dto, Principal principal, RedirectAttributes attr){
        ModelAndView mav = new ModelAndView(); 
        //get principal id 
        String username = principal.getName();
        final String id = userRepository.getUserId(username);
        User user = getUserFromTechnician(dto, id);
        //add the new technician
        try{
            String newId = userRepository.addUser(user);
            attr.addFlashAttribute("message", "The technician has been added successfuly");
            mav.setViewName("redirect:/admin/success");
        }catch(Exception ex){
            log.error("Could not add technician: " + ex.getMessage());
            mav.setViewName("/shared/error");
            mav.addObject("error", "No se ha podido crear t�cnico, v�anse los Logs del servidor");
        }
        return mav; 
    }
    
    @Override
    public ModelAndView getMachinesView(int pageNumber){
        ModelAndView mav = new ModelAndView(); 
        List<MachineDTO> page = null;
        
        try{
            page = machineRepository.getMachines(pageNumber);
            mav.setViewName("/admin/machines");
            mav.addObject("machines", page);
        }catch(Exception ex){
            log.error("Error: " + ex.getMessage());
        }
        return mav; 
    }
    
    @Override
    public ModelAndView createMachineView(){
        ModelAndView mav = new ModelAndView("admin/new-machine"); 
        MachineDTO dto = new MachineDTO();
        mav.addObject("machine", dto);
        //get technicians
        List<TechnicianDTO> techs = userRepository.getTechnicians(1);
        mav.addObject("technicians", techs);
        //get all products
        List<Product> products = productRepository.getProducts(1);
        mav.addObject("products", products);
        return mav; 
    }
    
    @Override
    public ModelAndView createNewMachine(MachineDTO dto,Principal principal, RedirectAttributes attrs){
        ModelAndView mav = new ModelAndView(); 
        //create a new machine 
        Machine machine = getMachineFromDTO(dto, principal);
        logMachine(machine);
        
        //add the new machine
        String id = null;
        try{
            id = machineRepository.addMachine(machine);
            mav.setViewName("redirect:/admin/success");
            attrs.addFlashAttribute("message", "The machine with ID " + id +" has been created");
            attrs.addFlashAttribute("message_code", MessageCodes.MACHINE_CREATED.toString());
            attrs.addFlashAttribute("machine_id", machine.getId());
            //TODO: send an email to all super admins
        }catch(Exception ex){
            log.error("Could not add machine: " + ex.getMessage());
            mav.addObject("error", "No se ha podido crear la m�quina");
            mav.setViewName("redirect:/admin/error");
        }
        
        return mav; 
    }
    
    @Override 
    public String updateTechnicianMachine(String techId, String machineId, int option){
    	String result = "";
    	
    	switch(option){
    		case 0:
    			//delete
    			machineRepository.deleteTechnicianMachine(machineId);
    			result = "OK"; 
    			break;
    		case 1: 
    			machineRepository.addTechnicianMachine(techId, machineId);
    			result = "OK"; 
    			break; 
    		default: 
    			result = "FAIL"; 
    			break; 
    	}
    	return result; 
    }
    
    /**private TechnicianMachine getTechnicianMachine(String techId, String machineId){
    	TechnicianMachine tm = new TechnicianMachine();
    	//tm.set(ShakeUtils.DATE_FORMAT.format(new Date()));
    	tm.setTechnicianId(techId);
    	tm.setMachineId(machineId);
    	
    	return tm; 
    }**/
    
    @Override 
    public MachineProductsContent getMachineProductsContent(String machineId){
    	MachineProductsContent content = new MachineProductsContent();
    	int alertedProducts = machineRepository.getAlertedproducts(machineId);
    	List<MachineProduct> productsPage = machineRepository.getMachineProducts(machineId, 1);
    	List<Product> all = productRepository.getProducts(1);
    	MachineDTO machine = machineRepository.getMachine(machineId);
    	//if the machine has a technician
    	TechnicianDTO technician = null; 
    	if(machine != null && machine.getTechnicianId() != null && ! machine.getTechnicianId().isEmpty()){
    		technician = userRepository.getTechnician(machine.getTechnicianId());
    	}
    	
    	
    	List<TechnicianDTO> technicians = userRepository.getTechnicians(1);
    	
    	content.setAlertedProducts(alertedProducts);
    	content.setMachine(machine);
    	content.setMachineProducts(productsPage);
    	content.setProducts(all);
    	content.setTechnician(technician);
    	content.setTechnicians(technicians);
    	
    	return content; 
    }
    
    @Override
    public ModelAndView createMachineProductsView(String machineId){
        ModelAndView mav = new ModelAndView("admin/machine-products");
        mav.addObject("machine_id", machineId); 
        return mav;
    }
    
    private void logMachine(Machine machine){
        log.info("Name: " + machine.getName());
        log.info("Descrition: " + machine.getDescription());
        log.info("Location: " + machine.getLocation());
        log.info("Technician ID: " + machine.getTechnicianId());
        log.info("Added by: " + machine.getAddedBy());
    }
    
    private Machine getMachineFromDTO(MachineDTO dto, Principal principal){
        Machine machine = new Machine(); 
        machine.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        machine.setDescription(dto.getDescription());
        machine.setName(dto.getName());
        machine.setTechnicianId(dto.getTechnicianId());
        machine.setLocation(dto.getLocation().replace("(", "\u0000").replace(")", "\u0000"));
        machine.setAddedBy(userRepository.getUserId(principal.getName()));
        return machine; 
    }
    
    private User getUserFromTechnician(TechnicianDTO dto, String addedBy){
        User user = new User(); 
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
        final String encryptedPass = encoder.encode(dto.getPassword());
        user.setPassword(encryptedPass);
        user.setConfirmed(false);
        user.setActive(true);
        user.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        user.setAddedBy(addedBy);
        user.setRole(SecurityRole.TECHNICIAN.toString());
        return user; 
    }
    
    private boolean validateProduct(Product product, MultipartFile file){
        if(product.getName() == null || product.getName().isEmpty())
            return false;
        if(product.getLogoUrl() == null || product.getLogoUrl().isEmpty()){
        	//check if there is a file present 
        	if(file == null)
        		return false;
        }
        else if(product.getPrice().doubleValue() <= 0)
            return false;
        return true;
    }

    static enum MessageCodes{
        MACHINE_CREATED(1);
        
        int value; 
        MessageCodes(int val){
            this.value = val; 
        }
        
        @Override
        public String toString(){
            return String.valueOf(value);
        }
    }

	@Override
	public ComboContentResponse getComboContent(String productId) {
		ComboContentResponse response = new ComboContentResponse(); 
		//get current combo product 
		Product combo = productRepository.getProduct(productId); 
		if(combo.isCombo()){
			//get all products 
			List<Product> comboProducts = productRepository.getComboProducts(productId, 1);
			List<Product> allProducts = productRepository.getProducts(1, Product.ProductType.SIMPLE);
			
			response.setCombo(combo);
			response.setComboProducts(comboProducts);
			response.setProducts(allProducts);
		}
		return response; 
	}
	
	@Override 
	public Product updateComboProduct(String comboId, String productId, int value){
		Product p = null;
		
		//switch among values 
		switch(value){
			case 0:
				//delete
                log.info("Deleting combo product");
				p = productRepository.deleteComboProduct(comboId, productId); 
				break; 
			case 1:
				//add
                log.info("Adding new combo product");
				p = productRepository.addComboProduct(comboId, productId);
				break; 
		}
		return p; 
	}
}
