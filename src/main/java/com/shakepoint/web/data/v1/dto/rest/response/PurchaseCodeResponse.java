package com.shakepoint.web.data.v1.dto.rest.response;

public class PurchaseCodeResponse {
    private String id;
    private String purchaseId;
    private double total;
    private String creationDate;
    private String productName;
    private String imageUrl;

    public PurchaseCodeResponse() {
    }

    public PurchaseCodeResponse(String id, String purchaseId, double total, String creationDate, String productName, String imageUrl) {
        this.id = id;
        this.purchaseId = purchaseId;
        this.total = total;
        this.creationDate = creationDate;
        this.productName = productName;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String codeId) {
        this.id = codeId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageurl) {
        this.imageUrl = imageurl;
    }


}
