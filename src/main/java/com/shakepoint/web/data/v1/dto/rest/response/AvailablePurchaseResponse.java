package com.shakepoint.web.data.v1.dto.rest.response;

public class AvailablePurchaseResponse {
    private String purchaseId;

    public AvailablePurchaseResponse() {
    }

    public AvailablePurchaseResponse(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }
}
