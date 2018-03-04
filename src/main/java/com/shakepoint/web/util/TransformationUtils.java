package com.shakepoint.web.util;

import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.core.machine.PurchaseStatus;
import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.data.dto.res.rest.UserProfileResponse;
import com.shakepoint.web.data.dto.res.rest.UserPurchaseResponse;
import com.shakepoint.web.data.v1.dto.mvc.request.NewProductRequest;
import com.shakepoint.web.data.v1.dto.mvc.request.NewTechnicianRequest;
import com.shakepoint.web.data.v1.dto.mvc.response.SimpleMachine;
import com.shakepoint.web.data.v1.dto.mvc.response.SimpleProduct;
import com.shakepoint.web.data.v1.dto.mvc.response.TechnicianMachine;
import com.shakepoint.web.data.v1.dto.rest.request.PurchaseRequest;
import com.shakepoint.web.data.v1.dto.rest.request.SignupRequest;
import com.shakepoint.web.data.v1.dto.rest.request.UserProfileRequest;
import com.shakepoint.web.data.v1.dto.mvc.response.Technician;
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
    public static ShakepointProduct createProductFromDto(NewProductRequest product) {
        ShakepointProduct p = new ShakepointProduct();
        p.setName(product.getName());
        p.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        p.setDescription(product.getDescription());
        p.setLogoUrl(product.getLogoUrl());
        p.setPrice(product.getPrice());
        p.setType(product.isCombo() ? ProductType.COMBO : ProductType.SIMPLE);
        p.setEngineUseTime(product.getEngineUseTime());
        return p;
    }

    public static ShakepointUser getUser(SignupRequest request, BCryptPasswordEncoder encoder) {
        ShakepointUser user = new ShakepointUser();
        user.setActive(true);
        user.setConfirmed(false);
        user.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(SecurityRole.MEMBER.toString());
        return user;
    }

    public static List<Technician> createTechnicianDtoList(List<ShakepointUser> techs) {
        List<Technician> list = new ArrayList();
        Technician dto;
        for (ShakepointUser user : techs) {
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

    public static ShakepointMachine getMachineFromDTO(NewMachineRequest dto, String id) {
        ShakepointMachine machine = new ShakepointMachine();
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

    public static ShakepointUserProfile getProfile(String userId, UserProfileRequest request) {
        ShakepointUserProfile profile = new ShakepointUserProfile();
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

    public static Technician createTechnician(ShakepointUser dto) {
        return new Technician(dto.getId(), dto.getName(), dto.getEmail(), dto.getCreationDate(), dto.isActive());
    }

    public static List<SimpleProduct> createSimpleProducts(List<ShakepointProduct> page) {
        List<SimpleProduct> products = new ArrayList();
        for (ShakepointProduct p : page) {
            products.add(createSimpleProduct(p));
        }
        return products;
    }

    public static List<SimpleMachine> createSimpleMachines(List<ShakepointMachine> page) {
        List<SimpleMachine> machines = new ArrayList();
        for (ShakepointMachine m : page) {
            machines.add(new SimpleMachine(m.getId(), m.getName(), m.getDescription()));
        }

        return machines;
    }

    public static ShakepointUser getUserFromTechnician(NewTechnicianRequest dto, String addedBy) {
        ShakepointUser user = new ShakepointUser();
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

    public static SimpleMachineProduct createSimpleMachineProduct(ShakepointMachineProductStatus machineProduct) {
        return new SimpleMachineProduct(machineProduct.getId(), machineProduct.getProduct().getName(),
                machineProduct.getProduct().getLogoUrl(), machineProduct.getProduct().getType().getValue(),
                machineProduct.getSlotNumber());
    }

    public static List<SimpleMachineProduct> createSimpleMachineProducts(List<ShakepointMachineProductStatus> s) {
        List<SimpleMachineProduct> list = new ArrayList();
        for (ShakepointMachineProductStatus status : s) {
            list.add(createSimpleMachineProduct(status));
        }

        return list;
    }

    public static SimpleProduct createSimpleProduct(ShakepointProduct p) {
        return new SimpleProduct(p.getId(), p.getName(), p.getCreationDate(), p.getPrice(), p.getLogoUrl());
    }

    public static List<PurchaseCodeResponse> createPurchaseCodes(List<ShakepointPurchase> activeCodes) {
        List<PurchaseCodeResponse> codes = new ArrayList();
        for (ShakepointPurchase code : activeCodes) {
            codes.add(new PurchaseCodeResponse(code.getQrCodeUrl(), code.getProduct().getName(), code.getMachine().getName(), code.getPurchaseDate()));
        }
        return codes;
    }

    public static UserProfileResponse createUserProfile(ShakepointUserProfile userProfile) {
        UserProfileResponse response = new UserProfileResponse(userProfile.getUser().getName(), userProfile.getUser().getId(),
                userProfile.getUser().getCreationDate(), true, userProfile.getBirthday(), userProfile.getWeight(),
                userProfile.getHeight(), getTotalPurchases(userProfile.getUser().getPurchases()), userProfile.getUser().getEmail());
        return response;
    }

    public static double getTotalPurchases(List<ShakepointPurchase> purchases) {
        double total = 0;
        for (ShakepointPurchase p : purchases) {
            total += p.getTotal();
        }
        return total;
    }

    public static List<TechnicianMachine> createTechnicianMachinesList(List<ShakepointMachine> machines, MachineRepository instance) {
        List<TechnicianMachine> dtos = new ArrayList();
        for (ShakepointMachine machine : machines) {
            dtos.add(new TechnicianMachine(machine.getId(), machine.getName(), machine.getDescription(), instance.isMachineAlerted(machine.getId()),
                    instance.getMachineProducts(machine.getId()).size(), 6));
        }
        return dtos;
    }

    public static List<ProductDTO> createProducts(List<ShakepointProduct> entities) {
        List<ProductDTO> productsList = new ArrayList();
        for (ShakepointProduct p : entities) {
            productsList.add(new ProductDTO(p.getId(), p.getName(), p.getPrice(), p.getDescription(), p.getLogoUrl()));
        }
        return productsList;
    }

    public static List<UserPurchaseResponse> createPurchases(List<ShakepointPurchase> purchases) {
        List<UserPurchaseResponse> ps = new ArrayList();

        for (ShakepointPurchase p : purchases){
            ps.add(new UserPurchaseResponse(p.getId(), p.getTotal(), p.getProduct().getName(), p.getMachine().getName(), p.getPurchaseDate()));
        }
        return ps;
    }
}
