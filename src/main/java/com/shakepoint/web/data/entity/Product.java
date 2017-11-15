/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.data.entity;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alberto Rubalcaba
 */
@SqlResultSetMapping(name = "ProductEntityMapper",
        columns = {
            @ColumnResult(name = "id"),
            @ColumnResult(name = "name"),
            @ColumnResult(name = "logoUrl"),
            @ColumnResult(name = "price"),
            @ColumnResult(name = "creationDate"),
            @ColumnResult(name = "description"),
            @ColumnResult(name = "productType"),
            @ColumnResult(name = "combo"),
        })
public class Product {
    private String id;
    protected String name;
    protected String logoUrl;
    protected BigDecimal price;
    protected String creationDate;
    protected String description;
    protected ProductType productType;
    protected boolean combo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCombo() {
        return combo;
    }

    public void setCombo(boolean combo) {
        this.combo = combo;
    }

    public Product() {
        id = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }


    public static enum ProductType {
        SIMPLE(0), COMBO(1), NOT_VALID(9);

        int value;

        ProductType(int value) {
            this.value = value;
        }

        public static ProductType get(int value) {
            switch (value) {
                case 0:
                    return SIMPLE;
                case 1:
                    return COMBO;
                default:
                    return NOT_VALID;
            }
        }

        public int getValue() {
            return this.value;
        }
    }

}
