/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository;

import com.shakepoint.web.core.machine.ProductType;
import com.shakepoint.web.data.v1.dto.rest.response.Combo;
import com.shakepoint.web.data.v1.entity.ShakepointProduct;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface ProductRepository {
    public List<ShakepointProduct> getProducts(int pageNumber, ProductType type);
    public List<ShakepointProduct> getProducts(int pageNumber);
    public void createProduct(ShakepointProduct p);
    public List<ShakepointProduct> getMachineProducts(String machineId, int pageNumber);
    public ShakepointProduct getProduct(String id);
    public List<Combo> getMachineCombos(String machineId, int pageNumber);
	public List<ShakepointProduct> getComboProducts(String productId, int i);
	public void deleteComboProduct(String comboId, String productId);
	public void addComboProduct(String comboId, String productId);
	public List<ShakepointProduct> getProducts(String machineId, int pageNumber, ProductType simple);
}
