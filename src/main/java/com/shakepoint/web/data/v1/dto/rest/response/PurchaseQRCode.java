package com.shakepoint.web.data.v1.dto.rest.response;

public class PurchaseQRCode {
    private String imageUrl;
    private boolean success;
    private String message;

    public PurchaseQRCode() {
    }

    public PurchaseQRCode(String imageUrl, boolean success, String message) {
        this.imageUrl = imageUrl;
        this.success = success;
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
