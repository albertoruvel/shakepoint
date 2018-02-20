package com.shakepoint.web.data.v1.dto.rest.request;

public class ConfirmPurchaseRequest {
    private String purchaseId;
    private String reference;

    public ConfirmPurchaseRequest() {
        super();
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
