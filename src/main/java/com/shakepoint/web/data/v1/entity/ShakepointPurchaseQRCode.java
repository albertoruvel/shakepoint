package com.shakepoint.web.data.v1.entity;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_id")
    private ShakepointPurchase purchase;

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

    public ShakepointPurchase getPurchase() {
        return purchase;
    }

    public void setPurchase(ShakepointPurchase purchase) {
        this.purchase = purchase;
    }
}
