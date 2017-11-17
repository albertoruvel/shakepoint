/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository.impl;


import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.data.dto.res.ProductDTO;
import com.shakepoint.web.data.entity.Combo;
import com.shakepoint.web.data.entity.ComboProduct;
import com.shakepoint.web.data.entity.Product;
import com.shakepoint.web.data.v1.entity.ShakepointProduct;
import com.shakepoint.web.util.ShakeUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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


    private static final String GET_PRODUCT = "select id, name, price, creation_date as creationDate, description, logo_url as logoUrl, type as productType from product where id = ?";

    @Override
    public Product getProduct(String id) {
        Product p = null;
        try {
            p = (Product) em.createNativeQuery(GET_PRODUCT)
                    .setParameter(1, id).getSingleResult();
            return p;
        } catch (Exception ex) {
            log.error("Could not get product", ex);
        }
        return p;
    }

    private static final String GET_PRODUCTS = "select id, name, price, creation_date as creationDate, logo_url as logoUrl, description, type as productType from product where type = ?";

    @Override
    public List<Product> getProducts(int pageNumber, ProductType type) {
        try {
            return em.createNativeQuery(GET_PRODUCTS)
                    .setParameter(1, type.getValue())
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get products list", ex);
            return null;
        }
    }

    private static final String GET_ALL_PRODUCTS = "select id, name, price, creation_date as creationDate, logo_url as logoUrl, description, type as productType from product";

    @Override
    public List<ShakepointProduct> getProducts(int pageNumber) {
        List<Product> page = null;

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

    private static final String GET_MACHINE_DTO_PRODUCTS = "select p.id, p.name, p.logo_url as logoUrl, mp.available_percentage as percentage from "
            + "product p inner join machine_product mp on p.id = mp.product_id where mp.machine_id = ?";

    @Override
    public List<ProductDTO> getMachineProductsDTO(String machineId, int pageNumber) {
        List<ProductDTO> page = null;
        try {
            return em.createNativeQuery(GET_MACHINE_DTO_PRODUCTS)
                    .setParameter(1, machineId)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get machine products", ex);
            return null;
        }
    }

    private static final String GET_MACHINE_PRODUCTS_SIMPLE = "select p.id, p.name, p.description, p.logo_url as logoUrl, p.creation_date as creationDate, p.price, p.type as productType"
            + "from product p "
            + "inner join machine_product m on p.id = m.product_id "
            + "where m.machine_id = ? and p.type = ?";

    @Override
    public List<Product> getProducts(String machineId, int pageNumber, ProductType type) {
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
    public List<Product> getMachineProducts(String machineId, int pageNumber) {
        try {
            return em.createNativeQuery(GET_MACHINE_PRODUCTS)
                    .setParameter(1, machineId)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get machine products", ex);
            return null;
        }
    }

    private static final String CREATE_COMBO_PRODUCT = "insert into combo_product(id, combo_item, product_id) values(?, ?, ?)";

    private void createComboProduct(ComboProduct cp) {
        try {
            em.createNativeQuery(CREATE_COMBO_PRODUCT)
                    .setParameter(1, cp.getId())
                    .setParameter(2, cp.getItemId())
                    .setParameter(3, cp.getProductId())
                    .executeUpdate();
        } catch (Exception ex) {
            log.error("Could not add combo product", ex);
        }
    }

    private ComboProduct getComboProduct(String comboId, Product p) {
        ComboProduct cp = new ComboProduct();
        cp.setItemId(p.getId());
        cp.setProductId(comboId);

        return cp;
    }


    private static final String GET_MACHINE_COMBOS = "select p.id, p.name, p.description, p.logo_url as logoUrl, p.creation_date as creationDate, p.price, p.type as productType "
            + "from machine_product mp "
            + "inner join product p on mp.product_id = p.id "
            + "where mp.machine_id = ? and p.type = 1"; // type 1 means combo, 0 means simple product

    @Override
    public List<Combo> getMachineCombos(String machineId, int pageNumber) {
        List<Product> page = null;
        //get all combo products
        List<Combo> combos = new ArrayList();
        try {
            page = em.createNativeQuery(GET_MACHINE_COMBOS)
                    .setParameter(1, machineId)
                    .getResultList();
            //iterates over each product
            Combo c = null;
            List<Product> ps = null;
            for (Product p : page) {
                //create a new combo
                c = new Combo(p);
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
    public List<Product> getComboProducts(String productId, int i) {
        try {
            return em.createNativeQuery(GET_COMBO_PRODUCTS)
                    .setParameter(1, productId).getResultList();
        } catch (Exception ex) {
            log.error("Could not get products from combo: " + ex.getMessage());
            return null;
        }
    }

    private static final String DELETE_COMBO_PRODUCT = "delete from combo_product where combo_item = ? and product_id = ?";

    @Override
    public Product deleteComboProduct(String comboId, String productId) {
        try {
            em.createNativeQuery(DELETE_COMBO_PRODUCT)
                    .setParameter(1, comboId)
                    .setParameter(2, productId).executeUpdate();
            //get product
            return getProduct(productId);
        } catch (Exception ex) {
            log.error("Could not delete combo product", ex);
            return null;
        }
    }


    private static final String ADD_COMBO_PRODUCT = "insert into combo_product(id, combo_item, product_id) values(?, ?, ?)";

    @Override
    public Product addComboProduct(String comboId, String productId) {
        ComboProduct cp = new ComboProduct();
        cp.setItemId(comboId);
        cp.setProductId(productId);
        try {
            em.createNativeQuery(ADD_COMBO_PRODUCT)
                    .setParameter(1, cp.getId())
                    .setParameter(2, cp.getItemId())
                    .setParameter(3, cp.getProductId())
                    .executeUpdate();
            return getProduct(productId);
        } catch (Exception ex) {
            log.error("Could not add new combo product", ex);
            return null;
        }
    }

}

