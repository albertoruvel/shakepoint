package com.shakepoint.web.data.security;

public enum SecurityRole {
    TECHNICIAN("technician"), SUPER_ADMIN("super-admin"), MEMBER("member");
    private String value;
    SecurityRole(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
