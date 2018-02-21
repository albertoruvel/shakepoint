package com.shakepoint.web.data.v1.dto.rest.response;

public class PurchaseCodeResponse {
    private String imageUrl;
    private String productId;
    private String machineName;
    private String purchaseDate;

    public PurchaseCodeResponse() {
    }

    public PurchaseCodeResponse(String imageUrl, String productid, String machineName, String purchaseDate) {
        this.imageUrl = imageUrl;
        this.productId = productid;
        this.machineName = machineName;
        this.purchaseDate = purchaseDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productid) {
        this.productId = productid;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public PurchaseCodeResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageurl) {
        this.imageUrl = imageurl;
    }


}
