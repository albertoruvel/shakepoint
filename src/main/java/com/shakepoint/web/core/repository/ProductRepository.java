/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository;

import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.data.dto.res.ProductDTO;
import com.shakepoint.web.data.entity.Combo;
import com.shakepoint.web.data.entity.Product;
import com.shakepoint.web.data.v1.entity.ShakepointProduct;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface ProductRepository {
    public List<Product> getProducts(int pageNumber, ProductType type);
    public List<ShakepointProduct> getProducts(int pageNumber);
    public void createProduct(ShakepointProduct p);
    public List<ProductDTO> getMachineProductsDTO(String machineId, int pageNumber);
    public List<Product> getMachineProducts(String machineId, int pageNumber);
    public Product getProduct(String id);
    public List<Combo> getMachineCombos(String machineId, int pageNumber);
	public List<Product> getComboProducts(String productId, int i);
	public Product deleteComboProduct(String comboId, String productId);
	public Product addComboProduct(String comboId, String productId);
	public List<Product> getProducts(String machineId, int pageNumber, ProductType simple);
}
