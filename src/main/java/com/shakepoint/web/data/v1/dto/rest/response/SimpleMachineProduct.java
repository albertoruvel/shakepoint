package com.shakepoint.web.data.v1.dto.rest.response;

public class SimpleMachineProduct {
    private String id;
    private String productName;
    private String productLogoUrl;
    private int type;
    private int slotNumber;

    public SimpleMachineProduct(String id, String productName, String productLogoUrl, int type, int slotNumber) {
        this.id = id;
        this.productName = productName;
        this.productLogoUrl = productLogoUrl;
        this.type = type;
        this.slotNumber = slotNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductLogoUrl() {
        return productLogoUrl;
    }

    public void setProductLogoUrl(String productLogoUrl) {
        this.productLogoUrl = productLogoUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }
}
