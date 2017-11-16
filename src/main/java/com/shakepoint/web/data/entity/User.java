/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.data.entity;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;
import java.util.UUID;

/**
 * @author Alberto Rubalcaba
 */
@SqlResultSetMapping(name = "UserMapper", columns = {
        @ColumnResult(name = "id"),
        @ColumnResult(name = "name"),
        @ColumnResult(name = "email"),
        @ColumnResult(name = "password"),
        @ColumnResult(name = "confirmed"),
        @ColumnResult(name = "creationDate"),
        @ColumnResult(name = "role"),
        @ColumnResult(name = "active"),
        @ColumnResult(name = "addedBy"),
        @ColumnResult(name = "lastSignin"),
})
public class User {


    private String id;
    private String name;
    private String email;
    private String password;
    private boolean confirmed;
    private String creationDate;
    private String role;
    private boolean active;
    private String addedBy;
    private String lastSignin;

    public User() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getLastSignin() {
        return lastSignin;
    }

    public void setLastSignin(String lastSignin) {
        this.lastSignin = lastSignin;
    }


}
