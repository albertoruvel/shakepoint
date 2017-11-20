package com.shakepoint.web.facade.impl;

import com.shakepoint.web.core.machine.MachineMessageCode;
import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.v1.dto.mvc.request.NewProductRequest;
import com.shakepoint.web.data.v1.dto.mvc.response.*;
import com.shakepoint.web.data.v1.dto.rest.response.SimpleMachineProduct;
import com.shakepoint.web.data.v1.entity.*;
import com.shakepoint.web.data.v1.dto.mvc.request.NewMachineRequest;
import com.shakepoint.web.data.v1.dto.mvc.request.NewTechnicianRequest;
import com.shakepoint.web.facade.AdminFacade;

import com.shakepoint.web.util.ShakeUtils;
import com.shakepoint.web.util.TransformationUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Alberto Rubalcaba
 **/
public class AdminFacadeImpl implements AdminFacade {

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
    public TotalIncomeValues getTotalIncomeValues(String from, String to, SimpleDateFormat sdf) {
        TotalIncomeValues values = null;
        List<Double> doubleValues = null;
        String[] range = null;
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = sdf.parse(from);
            toDate = sdf.parse(to);
            range = getDateRange(fromDate, toDate);
            //get values
            doubleValues = purchaseRepository.getTotalIncomeValues(range);
            values = new TotalIncomeValues();
            values.setRange(range);
            values.setValues(doubleValues);

        } catch (Exception ex) {
            return null;
        }
        return values;
    }


    @Override
    public PerMachineValues getIndexPerMachineValues(String from, String to, SimpleDateFormat sdf) {
        Map<String, List<Double>> map = null;
        PerMachineValues values = new PerMachineValues();
        //get the range
        String[] range = null;
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = sdf.parse(from);
            toDate = sdf.parse(to);
            range = getDateRange(fromDate, toDate);
            map = purchaseRepository.getPerMachineValues(range);
            values.setFromDate(from);
            values.setRange(range);
            values.setToDate(to);
            values.setValues(map);
        } catch (ParseException ex) {
            return null;
        }

        return values;
    }

    @Override
    public AdminIndexContent getIndexContent() {
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

    private String[] getDateRange(Date from, Date to) {
        //get day difference
        int days = Math.round((to.getTime() - from.getTime()) / 86400000); //convert to days
        final String[] range = new String[days];
        long time = from.getTime();
        String date = "";
        for (int i = 0; i < days; i++) {
            date = ShakeUtils.SIMPLE_DATE_FORMAT.format(new Date(time));
            //add the date to the range array
            range[i] = date;
            time = time + 86400000;
        }
        return range;
    }

    @Override
    public ModelAndView getShakepointUsers(int pageNumber) {
        ModelAndView mav = new ModelAndView("/admin/users");

        List<ShakepointUser> page = null;
        try {
            page = userRepository.getUsers(pageNumber);
            List<UserDescription> descs = new ArrayList();
            for (ShakepointUser user : page) {
                double total = 0;
                for (ShakepointPurchase purchase : user.getPurchases()) {
                    total += purchase.getTotal();
                }
                descs.add(new UserDescription(user.getId(), user.getName(), user.getEmail(), total));
            }
            mav.addObject("users", descs);
        } catch (Exception ex) {

        }
        return mav;
    }

    @Override
    public TechnicianMachinesContent getTechnicianMachinesContent(String techId) {
        TechnicianMachinesContent content = new TechnicianMachinesContent();
        //get technician
        ShakepointUser dto = userRepository.getTechnician(techId);
        content.setTechnician(TransformationUtils.createTechnician(dto));
        List<ShakepointMachine> allMachines = machineRepository.getMachines(1);
        content.setAllMachines(TransformationUtils.createSimpleMachines(allMachines));
        List<ShakepointMachine> assignedMachines = machineRepository.getTechnicianMachines(techId, 1);
        content.setAsignedMachines(TransformationUtils.createSimpleMachines(assignedMachines));
        return content;
    }

    @Override
    public ModelAndView getTechnicianMachinesView(String techId) {
        ModelAndView mav = new ModelAndView("/admin/technician-machines");
        mav.addObject("technician_id", techId);
        return mav;
    }


    @Override
    public SimpleMachineProduct addMachineProduct(String machineId, String productId, int slotNumber, Principal principal) {
        final ShakepointUser addedBy = userRepository.getUserByEmail(principal.getName());
        SimpleMachineProduct response = null;
        ShakepointMachineProductStatus newMachineProduct = new ShakepointMachineProductStatus();
        newMachineProduct.setPercentage(100);
        ShakepointMachine machine = machineRepository.getMachine(machineId);
        newMachineProduct.setMachine(machine);
        ShakepointProduct product = productRepository.getProduct(productId);
        newMachineProduct.setProduct(product);
        newMachineProduct.setSlotNumber(slotNumber);
        String message = "No se puede agregar el paquete a la maquina.\nLa maquina debe de contener los productos del paquete para ofrecerlos: \n";
        String productsMessage = "";
        //check the product
        ShakepointProduct p = productRepository.getProduct(productId);
        if (p.getType() == ProductType.COMBO) {
            boolean success = true;
            //check if the machine already has the needed products
            List<ShakepointProduct> products = productRepository.getComboProducts(p.getId(), 1);
            for (ShakepointProduct cp : products) {
                if (!machineRepository.containProduct(machineId, cp.getId())) {
                    productsMessage += cp.getName() + "\n";
                    if (success)
                        success = false;
                }
            }
            if (success) {
                //add the relationship
                newMachineProduct.setUpdatedBy(addedBy);
                // add the new entity
                machineRepository.addMachineProduct(newMachineProduct);
                response = TransformationUtils.createSimpleMachineProduct(machineRepository.getMachineProduct(newMachineProduct.getId()));

            } else {
                response = new SimpleMachineProduct(null, null, null, 0, 0);
            }
        } else {
            //product is not a combo
            //add the relationship
            newMachineProduct.setUpdatedBy(addedBy);
            // add the new entity
            machineRepository.addMachineProduct(newMachineProduct);
            response = TransformationUtils.createSimpleMachineProduct(machineRepository.getMachineProduct(newMachineProduct.getId()));
        }

        return response;
    }

    @Override
    public SimpleProduct deleteMachineProduct(String id) {
        ShakepointMachineProductStatus status = machineRepository.getMachineProduct(id);
        //delete the machine product
        machineRepository.deleteMachineProduct(id);
        return TransformationUtils.createSimpleProduct(productRepository.getProduct(status.getProduct().getId()));
    }

    @Override
    public ModelAndView getProducts(int pageNumber) {
        if (pageNumber <= 0)
            pageNumber = 1;
        ModelAndView mav = new ModelAndView("admin/products");
        //get a products page
        List<ShakepointProduct> page = null;
        try {
            page = productRepository.getProducts(pageNumber);
            List<SimpleProduct> simpleProducts = TransformationUtils.createSimpleProducts(page);
            mav.addObject("products", simpleProducts);
        } catch (Exception ex) {
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
        NewProductRequest product = new NewProductRequest();
        mav.addObject("product", product);

        return mav;
    }

    @Override
    public ModelAndView getComboView(String productId) {
        ModelAndView mav = new ModelAndView("/admin/combo");
        //get product
        ShakepointProduct p = productRepository.getProduct(productId);
        if (p.getType() == ProductType.COMBO) {
            mav.addObject("productId", productId);
        } else {
            mav.addObject("error", "Este producto no es un paquete, asegurate de seleccionar un producto que se haya definido como paquete");
        }
        return mav;
    }


    @Override
    public ModelAndView createNewProduct(NewProductRequest product, RedirectAttributes atts, MultipartFile file) {
        ModelAndView mav = null;
        //validate product
        if (product != null) {
            //validate product
            if (validateProduct(product, file)) {
                //TODO: should create file here and upload to S3 or save it
                //TODO: check if file have content
                ShakepointProduct productEntity = new ShakepointProduct();
                //product is valid
                if (product.isCombo()) {
                    //create a new combo
                    productEntity.setType(ProductType.COMBO);
                    //save the product
                    productEntity = TransformationUtils.createProductFromDto(product);
                    productRepository.createProduct(productEntity);
                    mav = new ModelAndView("redirect:/admin/success");
                    atts.addFlashAttribute("message", "Se ha creado el paquete con ID: " + productEntity.getId());
                    atts.addFlashAttribute("newCombo", productEntity.getId());
                } else {
                    productEntity.setType(ProductType.SIMPLE);
                    productEntity = TransformationUtils.createProductFromDto(product);
                    productRepository.createProduct(productEntity);
                    mav = new ModelAndView("redirect:/admin/success");
                    atts.addFlashAttribute("message", "Se ha guardado el producto con ID: " + productEntity.getId());
                }
            } else {
                //product is not valid
                mav = new ModelAndView("redirect:/admin/error");
                atts.addFlashAttribute("error", "No se han llenado todos los campos");
            }
        } else {
            //product is null
            mav = new ModelAndView("redirect:/admin/error");
            atts.addFlashAttribute("error", "No se ha podido crear producto");
        }
        return mav;
    }

    @Override
    public ModelAndView getTechnicians() {
        ModelAndView mav = new ModelAndView();
        List<ShakepointUser> techs = null;
        try {
            techs = userRepository.getTechnicians();
            List<Technician> dtos = TransformationUtils.createTechnicianDtoList(techs);
            mav.setViewName("admin/technicians");
            mav.addObject("technicians", dtos);
        } catch (Exception ex) {
            log.error("Could not get technicians: " + ex.getMessage());
            mav.setViewName("admin/technicians");
            mav.addObject("error", "No se pudieron obtener los t�cnicos de ShakePoint");
        }
        return mav;
    }

    @Override
    public ModelAndView createTechnicianView() {
        ModelAndView mav = new ModelAndView("admin/new-technician");
        NewTechnicianRequest dto = new NewTechnicianRequest();
        mav.addObject("technician", dto);
        return mav;
    }

    @Override
    public ModelAndView createNewTechnician(NewTechnicianRequest dto, Principal principal, RedirectAttributes attr) {
        ModelAndView mav = new ModelAndView();
        //get principal id 
        String username = principal.getName();
        final String id = userRepository.getUserId(username);
        ShakepointUser user = TransformationUtils.getUserFromTechnician(dto, id);
        //add the new technician
        try {
            userRepository.addShakepointUser(user);
            attr.addFlashAttribute("message", "The technician has been added successfuly");
            mav.setViewName("redirect:/admin/success");
        } catch (Exception ex) {
            log.error("Could not add technician: " + ex.getMessage());
            mav.setViewName("/shared/error");
            mav.addObject("error", "No se ha podido crear t�cnico, v�anse los Logs del servidor");
        }
        return mav;
    }

    @Override
    public ModelAndView getMachinesView(int pageNumber) {
        ModelAndView mav = new ModelAndView();
        List<ShakepointMachine> page = null;

        try {
            page = machineRepository.getMachines(pageNumber);
            List<SimpleMachine> simpleMachines = TransformationUtils.createSimpleMachines(page);
            mav.setViewName("/admin/machines");
            mav.addObject("machines", simpleMachines);
        } catch (Exception ex) {
            log.error("Error: " + ex.getMessage());
        }
        return mav;
    }

    @Override
    public ModelAndView createMachineView() {
        ModelAndView mav = new ModelAndView("admin/new-machine");
        NewMachineRequest dto = new NewMachineRequest();
        mav.addObject("machine", dto);
        //get technicians
        List<Technician> techs = TransformationUtils.createTechnicianDtoList(userRepository.getTechnicians());
        mav.addObject("technicians", techs);
        //get all products
        List<ShakepointProduct> products = productRepository.getProducts(1);
        mav.addObject("products", products);
        return mav;
    }

    @Override
    public ModelAndView createNewMachine(NewMachineRequest dto, Principal principal, RedirectAttributes attrs) {
        ModelAndView mav = new ModelAndView();
        String currentUser = userRepository.getUserId(principal.getName());
        //create a new machine 
        ShakepointMachine machine = TransformationUtils.getMachineFromDTO(dto, currentUser);
        machine.setTechnician(userRepository.getTechnician(dto.getTechnicianId()));
        try {
            machineRepository.addMachine(machine);
            mav.setViewName("redirect:/admin/success");
            attrs.addFlashAttribute("message", "The machine with ID " + machine.getId() + " has been created");
            attrs.addFlashAttribute("message_code", MachineMessageCode.MACHINE_CREATED.toString());
            attrs.addFlashAttribute("machine_id", machine.getId());
            //TODO: send an email to all super admins
        } catch (Exception ex) {
            log.error("Could not add machine: " + ex.getMessage());
            mav.addObject("error", "No se ha podido crear la m�quina");
            mav.setViewName("redirect:/admin/error");
        }

        return mav;
    }

    @Override
    public String updateTechnicianMachine(String techId, String machineId, int option) {
        ShakepointUser technician = userRepository.getTechnician(techId);
        ShakepointMachine machine = machineRepository.getMachine(machineId);
        switch (option) {
            case 0:
                //delete
                machine.setTechnician(null);
                machineRepository.updateMachine(machine);
                return "OK";
            case 1:
                machine.setTechnician(technician);
                machineRepository.updateMachine(machine);
                return "OK";
            default:
                return "FAIL";
        }
    }

    //todo: convert to dtos CORRECTLY
    @Override
    public MachineProductsContent getMachineProductsContent(String machineId) {
        MachineProductsContent content = new MachineProductsContent();
        int alertedProducts = machineRepository.getAlertedproducts(machineId);
        List<SimpleMachineProduct> productsPage = TransformationUtils.createSimpleMachineProducts(machineRepository.getMachineProducts(machineId));
        List<SimpleProduct> all = TransformationUtils.createSimpleProducts(productRepository.getProducts(1));
        ShakepointMachine machine = machineRepository.getMachine(machineId);
        //if the machine has a technician
        Technician technician = null;
        if (machine != null && machine.getTechnician() != null) {
            technician = TransformationUtils.createTechnician(userRepository.getTechnician(machine.getTechnician().getId()));
        }


        List<Technician> technicians = TransformationUtils.createTechnicianDtoList(userRepository.getTechnicians());

        content.setAlertedProducts(alertedProducts);
        content.setMachine(machine);
        content.setMachineProducts(productsPage);
        content.setProducts(all);
        content.setTechnician(technician);
        content.setTechnicians(technicians);

        return content;
    }

    @Override
    public ModelAndView createMachineProductsView(String machineId) {
        ModelAndView mav = new ModelAndView("admin/machine-products");
        mav.addObject("machine_id", machineId);
        return mav;
    }

    private boolean validateProduct(NewProductRequest product, MultipartFile file) {
        if (product.getName() == null || product.getName().isEmpty())
            return false;
        if (product.getLogoUrl() == null || product.getLogoUrl().isEmpty()) {
            //check if there is a file present
            if (file == null)
                return false;
        } else if (product.getPrice() <= 0)
            return false;
        return true;
    }


    /**
     @Override public ComboContentResponse getComboContent(String productId) {
     ComboContentResponse response = new ComboContentResponse();
     //get current combo product
     ShakepointProduct combo = productRepository.getProduct(productId);
     if(combo.getType() == ProductType.COMBO){
     //get all products
     List<ShakepointProduct> comboProducts = productRepository.getComboProducts(productId, 1);
     List<ShakepointProduct> allProducts = productRepository.getProducts(1, ProductType.SIMPLE);

     response.setCombo(combo);
     response.setComboProducts(comboProducts);
     response.setProducts(allProducts);
     }
     return response;
     }**/

    /**@Override public ProductEntityOld updateComboProduct(String comboId, String productId, int value){
    ProductEntityOld p = null;

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
    }**/

}
