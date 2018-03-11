package com.shakepoint.web.facade.impl;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

import com.google.gson.Gson;
import com.shakepoint.email.EmailQueue;
import com.shakepoint.integration.jms.client.handler.JmsHandler;
import com.shakepoint.web.core.email.Template;
import com.shakepoint.web.core.repository.*;
import com.shakepoint.web.data.v1.dto.mvc.request.MachineProductOrderRequest;
import com.shakepoint.web.data.v1.dto.mvc.request.ProductOrderItemRequest;
import com.shakepoint.web.data.v1.dto.mvc.response.*;
import com.shakepoint.web.data.v1.entity.*;
import com.shakepoint.web.util.ShakeUtils;
import com.shakepoint.web.util.TransformationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import com.shakepoint.web.facade.PartnerFacade;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class PartnerFacadeImpl implements PartnerFacade {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private FailRepository failRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JmsHandler jmsHandler;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Value("${com.shakepoint.web.admin.orders.emails}")
    private String ordersAdminEmails;

    private final Logger log = Logger.getLogger(getClass());


    @Override
    public ModelAndView getIndexView(Principal principal) {
        return new ModelAndView("/tech/index");
    }

    //todo: convert to dto
    @Override
    public ModelAndView getAlertsView(Principal principal, int pageNumber) {
        ModelAndView mav = new ModelAndView("/tech/alerts");
        final String id = userRepository.getUserId(principal.getName());
        List<VendingMachine> machines = machineRepository.getAlertedMachines(id, pageNumber);
        if (machines != null) {
            mav.addObject("machines", machines);
        }
        return mav;
    }

    @Override
    public ModelAndView getMachinesView(Principal principal, int pageNumber) {
        ModelAndView mav = new ModelAndView("/tech/machines");
        //get technician id
        String id = userRepository.getUserId(principal.getName());
        //get technician machines
        List<VendingMachine> machines = machineRepository.getTechnicianMachines(id, pageNumber);
        List<TechnicianMachine> machinesDtos = TransformationUtils.createTechnicianMachinesList(machines, machineRepository);
        //add machines
        mav.addObject("machines", machinesDtos);
        return mav;
    }

    public ModelAndView getFailsView(Principal principal, int pageNumber) {
        ModelAndView mav = new ModelAndView("/tech/fails");
        String id = userRepository.getUserId(principal.getName());
        List<VendingMachine> machines = machineRepository.getFailedMachines(id, pageNumber);

        mav.addObject("machines", machines);
        return mav;
    }

    @Override
    public ModelAndView getMachineFailsLogView(String machienId, Principal principal, int pageNumber) {
        // TODO Auto-generated method stub
        ModelAndView mav = new ModelAndView("/tech/log");
        return mav;
    }

    public ModelAndView getMachineProductsView(String machineId, Principal principal, int pageNumber) {
        ModelAndView mav = new ModelAndView("/tech/products");
        //get machine
        VendingMachine vendingMachine = machineRepository.getMachine(machineId);
        if (vendingMachine == null) {
            //TODO: redirect to another place here
        } else {
            //get all products for machine
            List<VendingMachineProductStatus> products = machineRepository.getMachineProducts(machineId);
            List<ProductLevelDescription> descriptions = TransformationUtils.createProductLevelDescriptions(products);
            mav.addObject("products", new MachineProductOrderRequest(descriptions, machineId));
        }
        return mav;
    }

    @Override
    public String createPartnerOrder(Principal principal, String machineId, List<ProductOrderItemRequest> request, RedirectAttributes atts) {
        PartnerProductOrder order = new PartnerProductOrder();
        order.setOrderDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        order.setStatus(PartnerProductOrderStatus.REQUESTED);
        //get user
        String partnerId = userRepository.getUserId(principal.getName());
        order.setPartner(userRepository.getTechnician(partnerId));

        VendingMachine vendingMachine = machineRepository.getMachine(machineId);
        if (vendingMachine == null) {
            //TODO: set some error messages here
            return "redirect:/partner/machines";
        }
        order.setMachineId(vendingMachine.getId());
        List<HashMap<String, Object>> products = new ArrayList();
        HashMap<String, Object> product;
        //check products for order
        PartnerProductItem item;
        for (ProductOrderItemRequest desc : request) {
            if (desc.getQuantity() > 0) {
                product = new HashMap();
                product.put("id", desc.getProductId());
                product.put("productName", productRepository.getProduct(desc.getProductId()).getName());
                product.put("quantity", desc.getQuantity());
                products.add(product);

                //get product and create order
                item = new PartnerProductItem();
                item.setOrderId(order.getId());
                item.setProductId(desc.getProductId());
                item.setQuantity(desc.getQuantity());
                order.getProducts().add(item);
            }
        }
        if (order.getProducts().isEmpty()) {
            log.info("Will skip order as no products are on it");
            return "FAIL";
        }

        //save order
        userRepository.saveUserOrder(order);

        //get user email
        User user = userRepository.getTechnician(partnerId);
        //create partner email variables
        Map<String, Object> variables = new HashMap();
        variables.put("userName", user.getName());
        variables.put("products", products);

        jmsHandler.send(EmailQueue.NAME, new Gson().toJson(
                new com.shakepoint.email.model.Email(user.getEmail(), Template.NEW_PARTNER_ORDER_REQUEST_CLIENT.getTemplateName(),
                        Template.NEW_PARTNER_ORDER_REQUEST_CLIENT.getSubject(), variables)));

        //create variables for admin email
        variables = new HashMap();
        variables.put("partnerName", user.getName());
        variables.put("products", products);

        //get emails
        String[] array = ordersAdminEmails.split(",");
        for (String email : array) {
            jmsHandler.send(EmailQueue.NAME, new Gson().toJson(
                    new com.shakepoint.email.model.Email(email, Template.NEW_PARTNER_ORDER_REQUEST_ADMIN.getTemplateName(),
                            Template.NEW_PARTNER_ORDER_REQUEST_ADMIN.getSubject(), variables)));
        }
        log.info("Order emails sent");
        atts.addAttribute("message", "Se ha creado tu solicitud Shakepoint con ID: " + order.getId());

        return "OK";
    }

    @Override
    public PartnerIndexContent getIndexContent(Principal principal, String from, String to) {
        Date now;
        Date before;
        PartnerIndexContent content = new PartnerIndexContent();
        try {
            if (from != null && to != null) {
                //set dates
                now = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(to);
                before = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(from);
            } else {
                //set default
                now = new Date();
                final String nowString = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.format(now);
                now = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(nowString);
                Calendar beforeCalendar = Calendar.getInstance();
                beforeCalendar.setTime(now);
                beforeCalendar.set(Calendar.DAY_OF_MONTH, -5);
                before = beforeCalendar.getTime();
                final String beforeString = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.format(before);
                before = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(beforeString);
            }
            //get dates
            String[] range = ShakeUtils.getDateRange(before, now);
            content.setRange(range);

            //get machines
            List<VendingMachine> machines = machineRepository.getTechnicianMachines(userRepository.getUserId(principal.getName()), 1);

            //get per machine total incomes
            Map<String, List<Double>> values = purchaseRepository.getPerMachineValues(range, machines);
            PerMachineValues perMachineValues = new PerMachineValues();
            perMachineValues.setFromDate(ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.format(before));
            perMachineValues.setRange(range);
            perMachineValues.setToDate(ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.format(now));
            perMachineValues.setValues(values);
            content.setPerMachineValues(perMachineValues);

            //get principal id
            String id = userRepository.getUserId(principal.getName());
            //get user info
            User user = userRepository.getTechnician(id);
            Technician technicianDto = TransformationUtils.createTechnician(user);
            content.setPartner(technicianDto);
            int alertedMachines = machineRepository.getAlertedMachines(id);
            content.setAlertedMachines(alertedMachines);
            String lastSignin = userRepository.getLastSignin(id);
            content.setLastSignin(lastSignin);

            List<MachineProductCountItem> items = new ArrayList();
            MachineProductCountItem item;
            //check machines
            for (VendingMachine machine : machines) {
                item = new MachineProductCountItem();
                item.setMachineName(machine.getName());
                log.info("Will check machine " + machine.getName());
                List<VendingMachineProductStatus> products = machineRepository.getMachineProducts(machine.getId());
                log.info(String.format("Contains %d products", products.size()));
                String[] array = new String[products.size()];
                List<Integer> data = new ArrayList();
                Integer dataValue;
                //get productos array
                for (int i = 0; i < products.size(); i++) {
                    VendingMachineProductStatus status = products.get(i);
                    array[i] = status.getProduct().getName();
                    dataValue = purchaseRepository.getProductCountForDateRange(status.getProduct().getId(), range, machine.getId());
                    data.add(dataValue);
                    log.info(String.format("Product %s contains %d buys", array[i], dataValue));
                }
                item.setProducts(array);
                item.setData(data);
                item.setMachineId(machine.getId());
                items.add(item);
            }
            content.setMachines(items);
            return content;
        } catch (ParseException ex) {
            log.error("Could not parse dates");
            return new PartnerIndexContent();
        }

    }
}
