package com.shakepoint.web.util;

import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.data.dto.res.rest.UserProfileResponse;
import com.shakepoint.web.data.dto.res.rest.UserPurchaseResponse;
import com.shakepoint.web.data.v1.dto.mvc.request.NewProductRequest;
import com.shakepoint.web.data.v1.dto.mvc.request.NewTechnicianRequest;
import com.shakepoint.web.data.v1.dto.mvc.response.*;
import com.shakepoint.web.data.v1.dto.rest.request.SignupRequest;
import com.shakepoint.web.data.v1.dto.rest.request.UserProfileRequest;
import com.shakepoint.web.data.security.SecurityRole;
import com.shakepoint.web.data.v1.dto.rest.response.ProductDTO;
import com.shakepoint.web.data.v1.dto.rest.response.PurchaseCodeResponse;
import com.shakepoint.web.data.v1.dto.rest.response.SimpleMachineProduct;
import com.shakepoint.web.data.v1.entity.*;
import com.shakepoint.web.data.v1.dto.mvc.request.NewMachineRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransformationUtils {
    public static Product createProductFromDto(NewProductRequest product) {
        Product p = new Product();
        p.setName(product.getName());
        p.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        p.setDescription(product.getDescription());
        p.setLogoUrl(product.getLogoUrl());
        p.setPrice(product.getPrice());
        p.setType(product.isCombo() ? ProductType.COMBO : ProductType.SIMPLE);
        p.setEngineUseTime(product.getEngineUseTime());
        return p;
    }

    public static User getUser(SignupRequest request, BCryptPasswordEncoder encoder) {
        User user = new User();
        user.setActive(true);
        user.setConfirmed(false);
        user.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(SecurityRole.MEMBER.toString());
        return user;
    }

    public static List<Technician> createTechnicianDtoList(List<User> techs) {
        List<Technician> list = new ArrayList();
        Technician dto;
        for (User user : techs) {
            dto = new Technician();
            dto.setCreationDate(user.getCreationDate());
            dto.setName(user.getName());
            dto.setActive(user.isActive());
            dto.setEmail(user.getEmail());
            dto.setId(user.getId());
            list.add(dto);
        }
        return list;
    }

    public static VendingMachine getMachineFromDTO(NewMachineRequest dto, String id) {
        VendingMachine machine = new VendingMachine();
        machine.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        machine.setDescription(dto.getDescription());
        machine.setName(dto.getName());

        machine.setLocation(dto.getLocation().replace("(", "\u0000").replace(")", "\u0000"));
        machine.setAddedBy(id);
        machine.setActive(false);
        machine.setError(false);
        machine.setSlots(6);
        return machine;
    }

    public static UserProfile getProfile(String userId, UserProfileRequest request) {
        UserProfile profile = new UserProfile();
        try {
            //creates a date
            Date date = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(request.getBirthday());
            //format with shakepoint default format
            String birth = ShakeUtils.SIMPLE_DATE_FORMAT.format(date);
            profile.setBirthday(birth);
        } catch (ParseException ex) {

        }
        profile.setHeight(request.getHeight());
        profile.setWeight(request.getWeight());
        return profile;
    }

    public static Technician createTechnician(User dto) {
        return new Technician(dto.getId(), dto.getName(), dto.getEmail(), dto.getCreationDate(), dto.isActive());
    }

    public static List<SimpleProduct> createSimpleProducts(List<Product> page) {
        List<SimpleProduct> products = new ArrayList();
        for (Product p : page) {
            products.add(createSimpleProduct(p));
        }
        return products;
    }

    public static List<SimpleMachine> createSimpleMachines(List<VendingMachine> page) {
        List<SimpleMachine> machines = new ArrayList();
        for (VendingMachine m : page) {
            machines.add(new SimpleMachine(m.getId(), m.getName(), m.getDescription()));
        }

        return machines;
    }

    public static User getUserFromTechnician(NewTechnicianRequest dto, String addedBy) {
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

    public static SimpleMachineProduct createSimpleMachineProduct(VendingMachineProductStatus machineProduct) {
        return new SimpleMachineProduct(machineProduct.getId(), machineProduct.getProduct().getName(),
                machineProduct.getProduct().getLogoUrl(), machineProduct.getProduct().getType().getValue(),
                machineProduct.getSlotNumber());
    }

    public static List<SimpleMachineProduct> createSimpleMachineProducts(List<VendingMachineProductStatus> s) {
        List<SimpleMachineProduct> list = new ArrayList();
        for (VendingMachineProductStatus status : s) {
            list.add(createSimpleMachineProduct(status));
        }

        return list;
    }

    public static SimpleProduct createSimpleProduct(Product p) {
        return new SimpleProduct(p.getId(), p.getName(), p.getCreationDate(), p.getPrice(), p.getLogoUrl());
    }

    public static List<PurchaseCodeResponse> createPurchaseCodes(List<Purchase> activeCodes) {
        List<PurchaseCodeResponse> codes = new ArrayList();
        for (Purchase code : activeCodes) {
            codes.add(new PurchaseCodeResponse(code.getQrCodeUrl(), code.getProduct().getName(), code.getMachine().getName(), code.getPurchaseDate()));
        }
        return codes;
    }

    public static UserProfileResponse createUserProfile(UserProfile userProfile) {
        UserProfileResponse response = new UserProfileResponse(userProfile.getUser().getName(), userProfile.getUser().getId(),
                userProfile.getUser().getCreationDate(), true, userProfile.getBirthday(), userProfile.getWeight(),
                userProfile.getHeight(), getTotalPurchases(userProfile.getUser().getPurchases()), userProfile.getUser().getEmail());
        return response;
    }

    public static double getTotalPurchases(List<Purchase> purchases) {
        double total = 0;
        for (Purchase p : purchases) {
            total += p.getTotal();
        }
        return total;
    }

    public static List<TechnicianMachine> createTechnicianMachinesList(List<VendingMachine> machines, MachineRepository instance) {
        List<TechnicianMachine> dtos = new ArrayList();
        for (VendingMachine machine : machines) {
            dtos.add(new TechnicianMachine(machine.getId(), machine.getName(), machine.getDescription(), instance.isMachineAlerted(machine.getId()),
                    instance.getMachineProducts(machine.getId()).size(), 6));
        }
        return dtos;
    }

    public static List<ProductDTO> createProducts(List<Product> entities) {
        List<ProductDTO> productsList = new ArrayList();
        for (Product p : entities) {
            productsList.add(new ProductDTO(p.getId(), p.getName(), p.getPrice(), p.getDescription(), p.getLogoUrl()));
        }
        return productsList;
    }

    public static List<UserPurchaseResponse> createPurchases(List<Purchase> purchases) {
        List<UserPurchaseResponse> ps = new ArrayList();

        for (Purchase p : purchases){
            ps.add(new UserPurchaseResponse(p.getId(), p.getTotal(), p.getProduct().getName(), p.getMachine().getName(), p.getPurchaseDate()));
        }
        return ps;
    }

    public static List<ProductLevelDescription> createProductLevelDescriptions(List<VendingMachineProductStatus> products) {
        List<ProductLevelDescription> descriptions = new ArrayList();

        for (VendingMachineProductStatus status : products){
            descriptions.add(createProductLevelDescription(status));
        }
        return descriptions;
    }

    public static ProductLevelDescription createProductLevelDescription(VendingMachineProductStatus status){
        return new ProductLevelDescription(
                status.getPercentage() < 30 ? true : false,
                status.getProduct().getId(),
                status.getProduct().getName(),
                0,
                status.getProduct().getLogoUrl()
        );
    }
}
