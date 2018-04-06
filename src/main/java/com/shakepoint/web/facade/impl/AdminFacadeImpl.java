package com.shakepoint.web.facade.impl;

import com.shakepoint.integration.jms.client.handler.JmsHandler;
import com.shakepoint.web.core.machine.MachineMessageCode;
import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.core.shop.PayWorksClientService;
import com.shakepoint.web.core.shop.PayWorksMode;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    @Autowired
    private JmsHandler jmsHandler;

    @Value("${com.shakepoint.web.nutritional.tmp}")
    private String nutritionalDataTmpFolder;

    @Autowired
    private PayWorksClientService payWorksClientService;

    private static final String MACHINE_CONNECTION_QUEUE_NAME = "machine_connection";
    private static final String DELETE_MEDIA_CONTENT_QUEUE_NAME = "delete_media_content";
    private static final String NUTRITIONAL_DATA_QUEUE_NAME = "nutritional_data";
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
            range = ShakeUtils.getDateRange(fromDate, toDate);
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
            range = ShakeUtils.getDateRange(fromDate, toDate);
            List<VendingMachine> machines = machineRepository.getMachines(1);
            map = purchaseRepository.getPerMachineValues(range, machines);
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
        String[] range = ShakeUtils.getDateRange(fromDate, toDate);
        String from = ShakeUtils.SIMPLE_DATE_FORMAT.format(fromDate);
        String to = ShakeUtils.SIMPLE_DATE_FORMAT.format(toDate);
        perMachine = getIndexPerMachineValues(from, to, ShakeUtils.SIMPLE_DATE_FORMAT);

        content.setPerMachineValues(perMachine);
        TotalIncomeValues totalIncome = getTotalIncomeValues(from, to, ShakeUtils.SIMPLE_DATE_FORMAT);
        totalIncome.setRange(range);

        content.setTotalIncomeValues(totalIncome);

        PayWorksMode[] modesEnum = PayWorksMode.values();
        String[] modes = new String[modesEnum.length];
        for (int i = 0; i < modesEnum.length; i++) {
            modes[i] = modesEnum[i].getValue();
        }
        content.setModes(modes);

        return content;
    }


    @Override
    public ModelAndView getShakepointUsers(int pageNumber) {
        ModelAndView mav = new ModelAndView("/admin/users");

        List<User> page = null;
        try {
            page = userRepository.getUsers(pageNumber);
            List<UserDescription> descs = new ArrayList();
            for (User user : page) {
                double total = 0;
                for (Purchase purchase : user.getPurchases()) {
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
        User dto = userRepository.getTechnician(techId);
        content.setTechnician(TransformationUtils.createTechnician(dto));
        List<VendingMachine> allMachines = machineRepository.getMachines(1);
        content.setAllMachines(TransformationUtils.createSimpleMachines(allMachines));
        List<VendingMachine> assignedMachines = machineRepository.getTechnicianMachines(techId, 1);
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
        final User addedBy = userRepository.getUserByEmail(principal.getName());
        SimpleMachineProduct response = null;
        VendingMachineProductStatus newMachineProduct = new VendingMachineProductStatus();
        newMachineProduct.setPercentage(100);
        VendingMachine machine = machineRepository.getMachine(machineId);
        newMachineProduct.setMachine(machine);
        Product product = productRepository.getProduct(productId);
        newMachineProduct.setProduct(product);
        newMachineProduct.setSlotNumber(slotNumber);
        //add the relationship
        newMachineProduct.setUpdatedBy(addedBy);
        // add the new entity
        machineRepository.addMachineProduct(newMachineProduct);
        response = TransformationUtils.createSimpleMachineProduct(machineRepository.getMachineProduct(newMachineProduct.getId()));

        return response;
    }

    @Override
    public SimpleProduct deleteMachineProduct(String id) {
        VendingMachineProductStatus status = machineRepository.getMachineProduct(id);
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
        List<Product> page = null;
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
        //get all product types
        ProductType[] types = ProductType.values();
        String[] typesValues = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            typesValues[i] = types[i].getName();
        }
        mav.addObject("product", product);
        mav.addObject("availableProductTypes", typesValues);

        return mav;
    }

    @Override
    public String deleteMediaContent() {
        //send a jms message to delete everything on s3
        log.info("Will delete S3 content, sending JMS message to connector....");
        jmsHandler.send(DELETE_MEDIA_CONTENT_QUEUE_NAME, "Do it!");
        return "OK";
    }

    @Override
    public void writePayWorksMode(String mode) {
        log.info("Will write PayWorks mode");
        //translate mode
        final PayWorksMode currentMode = PayWorksMode.get(mode);
        payWorksClientService.savePayWorksMode(currentMode);
    }


    @Override
    public ModelAndView createNewProduct(NewProductRequest product, RedirectAttributes atts, MultipartFile file) {
        ModelAndView mav = null;
        //validate product
        if (product != null) {
            //validate product
            if (validateProduct(product, file)) {
                Product productEntity = new Product();
                productEntity.setType(ProductType.getProductType(product.getProductType()));
                productEntity = TransformationUtils.createProductFromDto(product);
                productRepository.createProduct(productEntity);
                processFile(file, productEntity.getId());
                jmsHandler.send(NUTRITIONAL_DATA_QUEUE_NAME, productEntity.getId());
                mav = new ModelAndView("redirect:/admin/success");
                atts.addFlashAttribute("message", "Se ha guardado el producto con ID: " + productEntity.getId());
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

    void processFile(MultipartFile file, final String productId) {
        try {
            byte[] bytes = file.getBytes();
            File tmpFile = new File(nutritionalDataTmpFolder + File.separator + productId + ".jpg");
            FileOutputStream stream = new FileOutputStream(tmpFile);
            stream.write(bytes);
            stream.close();
        } catch (IOException ex) {
            log.error("Could not read file from request", ex);
        } catch (NullPointerException ex) {
            log.error("Provided file is null", ex);
        }

    }

    @Override
    public ModelAndView getTechnicians() {
        ModelAndView mav = new ModelAndView();
        List<User> techs = null;
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
        User user = TransformationUtils.getUserFromTechnician(dto, id);
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
        List<VendingMachine> page = null;

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
        List<Product> products = productRepository.getProducts(1);
        mav.addObject("products", products);
        return mav;
    }

    @Override
    public ModelAndView createNewMachine(NewMachineRequest dto, Principal principal, RedirectAttributes attrs) {
        ModelAndView mav = new ModelAndView();
        String currentUser = userRepository.getUserId(principal.getName());
        //create a new machine 
        VendingMachine machine = TransformationUtils.getMachineFromDTO(dto, currentUser);
        machine.setTechnician(userRepository.getTechnician(dto.getTechnicianId()));
        try {
            machineRepository.addMachine(machine);
            mav.setViewName("redirect:/admin/success");
            attrs.addFlashAttribute("message", "The machine with ID " + machine.getId() + " has been created");
            attrs.addFlashAttribute("message_code", MachineMessageCode.MACHINE_CREATED.toString());
            attrs.addFlashAttribute("machine_id", machine.getId());
            //start machine
            jmsHandler.send(MACHINE_CONNECTION_QUEUE_NAME, machine.getId());
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
        User technician = userRepository.getTechnician(techId);
        VendingMachine machine = machineRepository.getMachine(machineId);
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
        VendingMachine machine = machineRepository.getMachine(machineId);
        machine.setProducts(null);//Avoid lazy loading errors when sending this to the UI

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
        else if (file.getOriginalFilename().isEmpty())
            return false;
        return true;
    }

}
