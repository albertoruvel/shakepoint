package com.shakepoint.web.data.dto.res;

import com.shakepoint.web.data.entity.Purchase;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "CpuQrCodeMapper", columns = {
        @ColumnResult(name = "codeId"),
        @ColumnResult(name = "status"),
        @ColumnResult(name = "codeUrl"),
        @ColumnResult(name = "purchaseId"),
        @ColumnResult(name = "purchaseDate"),
        @ColumnResult(name = "productId"),
})
public class CpuQRCode {
    private String codeId;
    private Purchase.PurchaseStatus status;
    private String codeUrl;
    private String purchaseId;
    private String purchaseDate;
    private String productId;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public Purchase.PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(Purchase.PurchaseStatus status) {
        this.status = status;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
