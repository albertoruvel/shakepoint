package com.shakepoint.web.data.v1.dto.rest.response;

public class PurchaseQRCode {
    private String imageUrl;

    public PurchaseQRCode() {
    }

    public PurchaseQRCode(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
