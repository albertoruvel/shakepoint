package com.shakepoint.web.util;

import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.core.machine.PurchaseStatus;
import com.shakepoint.web.data.v1.dto.mvc.request.NewProductRequest;
import com.shakepoint.web.data.v1.dto.rest.request.PurchaseRequest;
import com.shakepoint.web.data.v1.dto.rest.request.SignupRequest;
import com.shakepoint.web.data.v1.dto.rest.request.UserProfileRequest;
import com.shakepoint.web.data.v1.dto.mvc.response.Technician;
import com.shakepoint.web.data.security.SecurityRole;
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
        return p;
    }

    public static ShakepointUser getUser(SignupRequest request, BCryptPasswordEncoder encoder) {
        ShakepointUser user = new ShakepointUser();
        user.setActive(true);
        user.setAddedBy("");
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
        machine.setTechnicianId(dto.getTechnicianId());
        machine.setLocation(dto.getLocation().replace("(", "\u0000").replace(")", "\u0000"));
        machine.setAddedBy(id);
        machine.setActive(false);
        machine.setError(false);
        machine.setSlots(6);
        return machine;
    }

    public static ShakepointPurchase getPurchase(PurchaseRequest request, String userId) {
        ShakepointPurchase purchase = new ShakepointPurchase();
        purchase.setMachineId(request.getMachineId());
        purchase.setProductId(request.getProductId());
        purchase.setPurchaseDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        purchase.setStatus(PurchaseStatus.PRE_AUTHORIZED);
        purchase.setTotal(request.getPrice());
        purchase.setUserId(userId);
        purchase.setReference(""); //TODO: CHECK REFERENCE HERE
        return purchase;
    }

    public static ShakepointUserProfile getProfile(String userId, UserProfileRequest request) {
        ShakepointUserProfile profile = new ShakepointUserProfile();
        profile.setAge(request.getAge());
        try {
            //creates a date
            Date date = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(request.getBirthday());
            //format with shakepoint default format
            String birth = ShakeUtils.SIMPLE_DATE_FORMAT.format(date);
            profile.setBirthday(birth);
        } catch (ParseException ex) {

        }
        profile.setHeight(request.getHeight());
        profile.setUserId(userId);
        profile.setWeight(request.getWeight());
        return profile;
    }

    public static ShakepointPurchaseQRCode getQrCode(ShakepointPurchase purchase) {
        ShakepointPurchaseQRCode code = new ShakepointPurchaseQRCode();
        code.setCashed(false);
        code.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        code.setPurchaseId(purchase.getId());
        return code;
    }

    public static Technician createTechnician(ShakepointUser dto) {
        return new Technician(dto.getId(), dto.getName(), dto.getEmail(), dto.getCreationDate(), dto.isActive());
    }
}
