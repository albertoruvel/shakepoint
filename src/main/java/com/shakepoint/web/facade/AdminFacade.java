/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.facade;

import java.security.Principal;
import java.text.SimpleDateFormat;

import com.shakepoint.web.data.dto.res.MachineProduct;
import com.shakepoint.web.data.entity.Product;
import com.shakepoint.web.data.v1.dto.mvc.request.NewMachineRequest;
import com.shakepoint.web.data.v1.dto.mvc.request.NewTechnicianRequest;
import com.shakepoint.web.data.v1.dto.mvc.response.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Alberto Rubalcaba
 */
public interface AdminFacade {
    //PRODUCTS
    public ModelAndView getProducts(int pageNumber);

    public ModelAndView getNewProductView();

    public ModelAndView createNewProduct(Product product, RedirectAttributes atts, MultipartFile file);

    //TECHNICIANS
    public ModelAndView getTechnicians();

    public ModelAndView createTechnicianView();

    public ModelAndView createNewTechnician(NewTechnicianRequest dto, Principal principal, RedirectAttributes attrs);

    //MACHINES
    public ModelAndView getMachinesView(int pageNumber);

    public ModelAndView createMachineView();

    public ModelAndView createNewMachine(NewMachineRequest dto, Principal principal, RedirectAttributes attrs);

    public ModelAndView createMachineProductsView(String machineId);

    public MachineProductsContent getMachineProductsContent(String machineId);

    public String updateTechnicianMachine(String techId, String machineId, int option);

    public MachineProduct addMachineProduct(String machineId, String productId, int slotNumber, Principal principal);

    public Product deleteMachineProduct(String id);

    public ModelAndView getTechnicianMachinesView(String techId);

    public TechnicianMachinesContent getTechnicianMachinesContent(String technicianId);

    public ModelAndView getShakepointUsers(int pageNumber);

    public AdminIndexContent getIndexContent();

    public PerMachineValues getIndexPerMachineValues(String from, String to, SimpleDateFormat sdf);

    public TotalIncomeValues getTotalIncomeValues(String from, String to, SimpleDateFormat sdf);

    public ModelAndView getComboView(String productId);

    public ComboContentResponse getComboContent(String productId);

    public Product updateComboProduct(String comboId, String productId, int value);
}
