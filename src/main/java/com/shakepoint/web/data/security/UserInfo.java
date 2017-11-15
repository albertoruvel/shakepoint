/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.data.security;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

/**
 * @author Alberto Rubalcaba
 */
@SqlResultSetMapping(name = "UserInfoMapper",
        columns = {
                @ColumnResult(name = "email"),
                @ColumnResult(name = "password"),
                @ColumnResult(name = "role")
        })
public class UserInfo {
    private String email;
    private String password;
    private String role;

    public UserInfo() {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
