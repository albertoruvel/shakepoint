package com.shakepoint.web.core.shop;

import org.springframework.beans.factory.annotation.Value;

public class RetrofitConfiguration {

    @Value("${com.shakepoint.web.admin.banorte.url}")
    private String serverUrl;

    public String getServerUrl() {
        return serverUrl;
    }
}
