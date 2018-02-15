package com.shakepoint.web.data.v1.dto.rest.response;

public class ProductDTO {
    private String id;
    private String name;
    private double price;
    private String description;
    private String logoUrl;

    public ProductDTO(String id, String name, double price, String description, String logoUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.logoUrl = logoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
