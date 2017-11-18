/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository.impl;


import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.data.entity.ProductEntityOld;
import com.shakepoint.web.data.v1.dto.rest.response.Combo;
import com.shakepoint.web.data.entity.ComboProduct;
import com.shakepoint.web.data.v1.entity.ShakepointProduct;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alberto Rubalcaba
 */
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private EntityManager em;

    private final Logger log = Logger.getLogger(getClass());

    public ProductRepositoryImpl() {

    }

    @Override
    public ShakepointProduct getProduct(String id) {

        try {
            return (ShakepointProduct) em.createQuery("SELECT p FROM Product WHERE p.id = :id")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get product", ex);
            return null;
        }
    }

    @Override
    public List<ShakepointProduct> getProducts(int pageNumber, ProductType type) {
        try {
            return em.createQuery("SELECT ´FROM Product p WHERE p.type = :type")
                    .setParameter("type", type.getValue())
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get products list", ex);
            return null;
        }
    }
    @Override
    public List<ShakepointProduct> getProducts(int pageNumber) {
        try {
            return em.createQuery("SELECT p FROM Product p").getResultList();
        } catch (Exception ex) {
            log.error("Could not get all products", ex);
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void createProduct(ShakepointProduct p) {
        try {
            em.persist(p);
        } catch (Exception ex) {
            log.error("Could not create product", ex);
        }
    }

    private static final String GET_MACHINE_PRODUCTS_SIMPLE = "select p.id, p.name, p.description, p.logo_url as logoUrl, p.creation_date as creationDate, p.price, p.type as productType"
            + "from product p "
            + "inner join machine_product m on p.id = m.product_id "
            + "where m.machine_id = ? and p.type = ?";

    @Override
    public List<ShakepointProduct> getProducts(String machineId, int pageNumber, ProductType type) {
        try {
            return em.createNativeQuery(GET_MACHINE_PRODUCTS_SIMPLE)
                    .setParameter(1, machineId)
                    .setParameter(2, type.getValue())
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get products", ex);
            return null;
        }
    }

    private static final String GET_MACHINE_PRODUCTS = "select p.id, p.name, p.description, p.logo_url as logoUrl, p.creation_date as creationDate, p.price, p.type as productType from product p inner join machine_product m on "
            + "p.id = m.product_id where m.machine_id = ?";
    @Override
    public List<ShakepointProduct> getMachineProducts(String machineId, int pageNumber) {
        try {
            return em.createNativeQuery("SELECT p FROM Product p WHERE p.machineId = :machineId")
                    .setParameter("machineId", machineId)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get machine products", ex);
            return null;
        }
    }


    private static final String GET_MACHINE_COMBOS = "select p.id, p.name, p.description, p.logo_url as logoUrl, p.creation_date as creationDate, p.price, p.type as productType "
            + "from machine_product mp "
            + "inner join product p on mp.product_id = p.id "
            + "where mp.machine_id = ? and p.type = 1"; // type 1 means combo, 0 means simple product

    @Override
    public List<Combo> getMachineCombos(String machineId, int pageNumber) {
        List<ShakepointProduct> page = null;
        //get all combo products
        List<Combo> combos = new ArrayList();
        try {
            page = em.createNativeQuery(GET_MACHINE_COMBOS)
                    .setParameter(1, machineId)
                    .getResultList();
            //iterates over each product
            Combo c = null;
            List<ShakepointProduct> ps = null;
            for (ShakepointProduct p : page) {
                //create a new combo
                c = new Combo();
                ps = getComboProducts(c.getId(), 1);
                c.setItems(ps);
                combos.add(c);
            }
        } catch (Exception ex) {
            log.error("Could not get machine combo products", ex);
            return null;
        }
        return combos;
    }

    private static final String GET_COMBO_PRODUCTS = "select p.id, p.name, p.price, p.creation_date as creationDate, p.description, p.logo_url as logoUrl, p.type as productType "
            + "from combo_product cp "
            + "inner join product p on cp.combo_item = p.id "
            + "where cp.product_id = ?";

    @Override
    public List<ShakepointProduct> getComboProducts(String productId, int i) {
        try {
            return em.createNativeQuery(GET_COMBO_PRODUCTS)
                    .setParameter(1, productId).getResultList();
        } catch (Exception ex) {
            log.error("Could not get products from combo: " + ex.getMessage());
            return null;
        }
    }

    private static final String DELETE_COMBO_PRODUCT = "delete from combo_product where combo_item = ? and product_id = ?";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void deleteComboProduct(String comboId, String productId) {
        try {
            em.createNativeQuery(DELETE_COMBO_PRODUCT)
                    .setParameter(1, comboId)
                    .setParameter(2, productId).executeUpdate();

        } catch (Exception ex) {
            log.error("Could not delete combo product", ex);
        }
    }


    private static final String ADD_COMBO_PRODUCT = "insert into combo_product(id, combo_item, product_id) values(?, ?, ?)";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void addComboProduct(String comboId, String productId) {
        ComboProduct cp = new ComboProduct();
        cp.setItemId(comboId);
        cp.setProductId(productId);
        try {
            em.createNativeQuery(ADD_COMBO_PRODUCT)
                    .setParameter(1, cp.getId())
                    .setParameter(2, cp.getItemId())
                    .setParameter(3, cp.getProductId())
                    .executeUpdate();
        } catch (Exception ex) {
            log.error("Could not add new combo product", ex);
        }
    }

}

