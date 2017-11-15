package com.shakepoint.web.data.entity;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;
import java.util.UUID;

@SqlResultSetMapping(name = "PurchaseQRCodeMapper", columns = {
        @ColumnResult(name = "id"),
        @ColumnResult(name = "imageUrl"),
        @ColumnResult(name = "cashed"),
        @ColumnResult(name = "creationDate"),
        @ColumnResult(name = "purchaseId"),
})
public class PurchaseQRCode {
    private String id;
    private String imageUrl;
    private boolean cashed;
    private String creationDate;
    private String purchaseId;

    public PurchaseQRCode() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isCashed() {
        return cashed;
    }

    public void setCashed(boolean cashed) {
        this.cashed = cashed;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public static enum QRCodeStatus {
        AUTHORIZED(1), CASHED(2), NOT_VALID(9);
        int value;

        QRCodeStatus(int value) {
            this.value = value;
        }

        public QRCodeStatus get(int value) {
            switch (value) {
                case 0:
                    return AUTHORIZED;
                case 1:
                    return CASHED;
                default:
                    return NOT_VALID;
            }
        }

        public int getValue(QRCodeStatus status) {
            return status.getValue();
        }

        public int getValue() {
            return value;
        }
    }
}
