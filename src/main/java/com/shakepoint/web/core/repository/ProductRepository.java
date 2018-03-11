/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository;

import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.data.v1.entity.Product;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface ProductRepository {
    public List<Product> getProducts(int pageNumber, ProductType type);
    public List<Product> getProducts(int pageNumber);
    public void createProduct(Product p);
    public List<Product> getMachineProducts(String machineId, int pageNumber);
    public Product getProduct(String id);
	public List<Product> getComboProducts(String productId, int i);
	public List<Product> getProducts(String machineId, int pageNumber, ProductType simple);
}
