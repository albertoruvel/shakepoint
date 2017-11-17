package com.shakepoint.web.data.v1.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity(name = "Code")
@Table(name = "purchase_qrcode")
public class ShakepointPurchaseQRCode {

    @Id
    private String id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "cashed")
    private boolean cashed;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "purchase_id")
    private String purchaseId;

    public ShakepointPurchaseQRCode() {
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
}
