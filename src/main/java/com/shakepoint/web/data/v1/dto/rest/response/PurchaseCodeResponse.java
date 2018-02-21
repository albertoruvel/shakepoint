package com.shakepoint.web.data.v1.dto.rest.response;

public class PurchaseCodeResponse {
    private String imageUrl;

    public PurchaseCodeResponse() {
    }

    public PurchaseCodeResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageurl) {
        this.imageUrl = imageurl;
    }


}
