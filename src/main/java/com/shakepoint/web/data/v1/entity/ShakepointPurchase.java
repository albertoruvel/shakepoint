package com.shakepoint.web.data.v1.entity;

import com.shakepoint.web.core.machine.PurchaseStatus;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Purchase")
@Table(name = "purchase")
public class ShakepointPurchase {
    @Id
    private String id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private PurchaseStatus status;

    @Column(name = "purchase_date")
    private String purchaseDate;

    @Column(name = "total")
    private double total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id")
    private ShakepointMachine machine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ShakepointProduct product;

    @Column(name = "reference")
    private String reference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private ShakepointUser user;

    @Column(name = "qr_image_url")
    private String qrCodeUrl;

    public ShakepointPurchase() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ShakepointMachine getMachine() {
        return machine;
    }

    public void setMachine(ShakepointMachine machine) {
        this.machine = machine;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ShakepointUser getUser() {
        return user;
    }

    public void setUser(ShakepointUser user) {
        this.user = user;
    }

    public ShakepointProduct getProduct() {
        return product;
    }

    public void setProduct(ShakepointProduct product) {
        this.product = product;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
}
