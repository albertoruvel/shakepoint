package com.shakepoint.web.data.dto.plc;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "PlcProductMapper",
        columns = {
                @ColumnResult(name = "productId"),
                @ColumnResult(name = "availablePercentage"),
                @ColumnResult(name = "name"),
                @ColumnResult(name = "description"),
                @ColumnResult(name = "slotNumber")
        })
public class PlcProduct {
    private String productId;
    private int availablePercentage;
    private String name;
    private String description;
    private int slotNumber;

    public PlcProduct() {
        super();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getAvailablePercentage() {
        return availablePercentage;
    }

    public void setAvailablePercentage(int availablePercentage) {
        this.availablePercentage = availablePercentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }


}
