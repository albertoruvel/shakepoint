/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository;

import com.shakepoint.web.data.dto.res.ProductDTO;
import com.shakepoint.web.data.entity.Combo;
import com.shakepoint.web.data.entity.Product;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface ProductRepository {
    public List<Product> getProducts(int pageNumber, Product.ProductType type);
    public List<Product> getProducts(int pageNumber);
    public String createProduct(Product p);
    public List<ProductDTO> getMachineProductsDTO(String machineId, int pageNumber);
    public List<Product> getMachineProducts(String machineId, int pageNumber);
    public Product getProduct(String id); 
    
    //create a combo product
    public void createComboProduct(Combo combo);
    public List<Combo> getMachineCombos(String machineId, int pageNumber);
	public List<Product> getComboProducts(String productId, int i);
	public Product deleteComboProduct(String comboId, String productId);
	public Product addComboProduct(String comboId, String productId);
	//public Page<ComboResponse> getMachineCombosResponse(String machineId, int pageNumber);
	public List<Product> getProducts(String machineId, int pageNumber, Product.ProductType simple);
}
