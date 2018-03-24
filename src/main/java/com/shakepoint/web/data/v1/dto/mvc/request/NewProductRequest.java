package com.shakepoint.web.data.v1.dto.mvc.request;

import org.springframework.web.multipart.MultipartFile;

public class NewProductRequest {
    private String name;
    private double price;
    private String description;
    private String logoUrl;
    private boolean combo;
    private String engineUseTime;
    private String productType;

    public NewProductRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public boolean isCombo() {
        return combo;
    }

    public void setCombo(boolean combo) {
        this.combo = combo;
    }

    public String getEngineUseTime() {
        return engineUseTime;
    }

    public void setEngineUseTime(String engineUseTime) {
        this.engineUseTime = engineUseTime;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
