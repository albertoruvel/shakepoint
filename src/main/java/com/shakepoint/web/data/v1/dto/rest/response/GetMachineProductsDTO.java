package com.shakepoint.web.data.v1.dto.rest.response;

import java.util.List;

public class GetMachineProductsDTO {
    private List<ProductDTO> products;

    public GetMachineProductsDTO(List<ProductDTO> products) {
        this.products = products;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
