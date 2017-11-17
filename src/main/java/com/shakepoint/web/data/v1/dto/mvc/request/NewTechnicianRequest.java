package com.shakepoint.web.data.v1.dto.mvc.request;

public class NewTechnicianRequest {
    private String name;
    private String email;
    private String password;

    public NewTechnicianRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
