/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository.impl;


import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.data.v1.entity.Product;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public Product getProduct(String id) {

        try {
            return (Product) em.createQuery("SELECT p FROM Product p WHERE p.id = :id")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get product", ex);
            return null;
        }
    }

    @Override
    public List<Product> getProducts(int pageNumber) {
        try {
            return em.createQuery("SELECT p FROM Product p").getResultList();
        } catch (Exception ex) {
            log.error("Could not get all products", ex);
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void createProduct(Product p) {
        try {
            em.persist(p);
        } catch (Exception ex) {
            log.error("Could not create product", ex);
        }
    }

    private static final String GET_MACHINE_PRODUCTS_SIMPLE = "select p.id, p.name, p.description, p.logo_url, p.price, p.type as productType "
            + "from product p "
            + "inner join machine_product m on p.id = m.product_id "
            + "where m.machine_id = ?";

    @Override
    public List<Product> getProducts(String machineId, int pageNumber, ProductType type) {
        try {
            return em.createNativeQuery(GET_MACHINE_PRODUCTS_SIMPLE)
                    .setParameter(1, machineId)
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
            return em.createNativeQuery("SELECT p FROM Product p WHERE p.machineId = :machineId")
                    .setParameter("machineId", machineId)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get machine products", ex);
            return null;
        }
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

}

