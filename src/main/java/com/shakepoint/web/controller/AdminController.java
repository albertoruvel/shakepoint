/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.controller;

import com.shakepoint.web.data.v1.dto.mvc.request.NewMachineRequest;
import com.shakepoint.web.data.v1.dto.mvc.request.NewProductRequest;
import com.shakepoint.web.data.v1.dto.mvc.request.NewTechnicianRequest;
import com.shakepoint.web.data.v1.dto.mvc.response.*;
import com.shakepoint.web.data.v1.dto.rest.response.SimpleMachineProduct;
import com.shakepoint.web.facade.AdminFacade;
import java.security.Principal;


import com.shakepoint.web.util.ShakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Alberto Rubalcaba
 */
@Controller
@RequestMapping(value={ "/admin" })
public class AdminController {
    
    @Autowired
    private AdminFacade adminFacade;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public ModelAndView indexView(){
        return new ModelAndView("admin/index");
    }

    @RequestMapping(value = "/delete_s3_content", method = RequestMethod.POST)
    public String deleteMediaContent(){
        return adminFacade.deleteMediaContent();
    }
    
    @RequestMapping(value="/get_index_content", method=RequestMethod.GET)
    public @ResponseBody
    AdminIndexContent getIndexContent(){
    	return adminFacade.getIndexContent(); 
    }
    
    @RequestMapping(value="/get_index_per_machine_values", method=RequestMethod.GET, 
    		produces="application/json")
    public @ResponseBody
    PerMachineValues getIndexPerMachineValues(
    									@RequestParam(value="from", required=true)String from, 
    									@RequestParam(value="to", required=true)String to){
    	return adminFacade.getIndexPerMachineValues(from, to, ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT);
    }
    
    @RequestMapping(value="/get_total_income_values", method=RequestMethod.GET, 
    		produces="application/json")
    public @ResponseBody
    TotalIncomeValues getIndexTotalIncomeValues(
    									@RequestParam(value="from", required=true)String from, 
    									@RequestParam(value="to", required=true)String to){
    	return adminFacade.getTotalIncomeValues(from, to, ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT);
    }
    
    @RequestMapping(value="/products", method=RequestMethod.GET)
    public ModelAndView productsView(@RequestParam(value="page_number", required=false, defaultValue="1")int pageNumber){
        return adminFacade.getProducts(pageNumber);
    }
    
    @RequestMapping(value="/new-product", method=RequestMethod.GET)
    public ModelAndView newProductView(){
        return adminFacade.getNewProductView(); 
    }
    
    @RequestMapping(value="/new-product", method=RequestMethod.POST)
    public ModelAndView createProduct(@ModelAttribute("product")NewProductRequest product, RedirectAttributes atts,
                                      @RequestParam("file")MultipartFile file){
        return adminFacade.createNewProduct(product, atts, file);
    }
    
    @RequestMapping(value="/error", method=RequestMethod.GET)
    public ModelAndView error(RedirectAttributes atts){
        return new ModelAndView("shared/error");
    }
    
    @RequestMapping(value="/success", method=RequestMethod.GET)
    public ModelAndView success(RedirectAttributes atts){
        return new ModelAndView("shared/success");
    }
    
    @RequestMapping(value="/technicians", method=RequestMethod.GET)
    public ModelAndView techniciansView(){
        return adminFacade.getTechnicians();
    }
    
    @RequestMapping(value="/new-technician", method=RequestMethod.GET)
    public ModelAndView createTechnicianView(){
        return adminFacade.createTechnicianView(); 
    }
    
    @RequestMapping(value="/new-technician", method=RequestMethod.POST)
    public ModelAndView createTechnician(@ModelAttribute("technician") NewTechnicianRequest dto,
            @AuthenticationPrincipal Principal principal, RedirectAttributes atts){
        
        return adminFacade.createNewTechnician(dto, principal, atts); 
    }
    
    @RequestMapping(value="/machines", method=RequestMethod.GET)
    public ModelAndView machinesView(@RequestParam(value="page_number", required=false, defaultValue="1")int pageNumber){
        return adminFacade.getMachinesView(pageNumber); 
    }
    
    @RequestMapping(value="/new-machine", method=RequestMethod.GET)
    public ModelAndView createMachineView(){
        return adminFacade.createMachineView(); 
    }
    
    @RequestMapping(value="/new-machine", method=RequestMethod.POST)
    public ModelAndView createMachine(@ModelAttribute("machine") NewMachineRequest dto, @AuthenticationPrincipal Principal principal, RedirectAttributes atts){
        return adminFacade.createNewMachine(dto, principal, atts); 
    }
    
    @RequestMapping(value="/machine/{id}/products", method=RequestMethod.GET)
    public ModelAndView machineProductsView(@PathVariable(value="id")String id){
        return adminFacade.createMachineProductsView(id);
    }
    
    @RequestMapping(value="/get_machine_products_content", method=RequestMethod.GET, 
    		produces = "application/json")
    public @ResponseBody
    MachineProductsContent machineProductsContent(@RequestParam(value="machine_id", required=true)String machineId){
        return adminFacade.getMachineProductsContent(machineId); 
    }
    
    @RequestMapping(value="/update_technician_machine", method=RequestMethod.POST, 
    		produces = "application/json")
    public @ResponseBody ResponseEntity<String> updateTechnicianMachine(@RequestParam(value="machine_id", required=true)String machineId, 
    													@RequestParam(value="tech_id", required=true)String techId, 
    													@RequestParam(value="option", required=true)int option){
        return new ResponseEntity<String>(adminFacade.updateTechnicianMachine(techId, machineId, option), HttpStatus.OK); 
    }
    
    @RequestMapping(value="/add_machine_product", method=RequestMethod.POST, 
    		produces = "application/json")
    public @ResponseBody
    SimpleMachineProduct addMachineProduct(@RequestParam(value="machine_id", required=true)String machineId,
                                           @RequestParam(value="product_id", required=true)String productId,
                                           @RequestParam(value="slot_number", required=true)int slotNumber,
                                           @AuthenticationPrincipal Principal auth){
        return adminFacade.addMachineProduct(machineId, productId, slotNumber, auth);  
    }

    //todo: convert to dto
    @RequestMapping(value="/delete_machine_product", method=RequestMethod.POST, 
    		produces = "application/json")
    public @ResponseBody
    SimpleProduct deleteMachineProduct(@RequestParam(value="mp_id", required=true)String machineProductId){
        return adminFacade.deleteMachineProduct(machineProductId);  
    }
    
    @RequestMapping(value="/technician/{id}/machines", method=RequestMethod.GET)
    public ModelAndView getTechnicianMachines(@PathVariable(value="id")String techId){
    	return adminFacade.getTechnicianMachinesView(techId); 
    }
    
    @RequestMapping(value="/get_technician_machines_content", method=RequestMethod.GET, 
    		produces = "application/json")
    public @ResponseBody
    TechnicianMachinesContent getTechnicianMachinesContent(@RequestParam(value="technician_id", required=true)String techId){
        return adminFacade.getTechnicianMachinesContent(techId);  
    }
    
    @RequestMapping(value="/users", method=RequestMethod.GET)
    public ModelAndView getUsersView(@RequestParam(value="page_number", required=false, defaultValue="1")int pageNumber){
    	return adminFacade.getShakepointUsers(pageNumber);
    }
    
    @RequestMapping(value="/product/{productId}/edit", method=RequestMethod.GET)
    public ModelAndView editCombo(@PathVariable("productId")String productId){
    	return adminFacade.getComboView(productId); 
    }
}
