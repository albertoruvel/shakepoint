package com.shakepoint.web.data.dto.res.rest;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "PurchaseCodeResponseMapper", columns = {
        @ColumnResult(name = "id"),
        @ColumnResult(name = "purchaseId"),
        @ColumnResult(name = "total"),
        @ColumnResult(name = "creationDate"),
        @ColumnResult(name = "productName"),
        @ColumnResult(name = "imageUrl")
})
public class PurchaseCodeResponse {
    private String id;
    private String purchaseId;
    private double total;
    private String creationDate;
    private String productName;
    private String imageUrl;

    public PurchaseCodeResponse() {
        super();
        // TODO Auto-generated constructor stub
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
