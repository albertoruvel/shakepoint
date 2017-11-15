/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.data.dto.res;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

/**
 * @author Alberto Rubalcaba
 */
@SqlResultSetMapping(name = "ProductDTOMapper",
        columns = {
            @ColumnResult(name = "id"),
            @ColumnResult(name = "name"),
            @ColumnResult(name = "logoUrl"),
            @ColumnResult(name = "percentage"),
            @ColumnResult(name = "slotNumber")
        })
public class ProductDTO {
    private String id;
    private String name;
    private String logoUrl;
    private int percentage;
    private int slotNumber;

    public ProductDTO() {
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }
}
