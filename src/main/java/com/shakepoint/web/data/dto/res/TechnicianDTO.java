/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.data.dto.res;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

/**
 * @author Alberto Rubalcaba
 */
@SqlResultSetMapping(name = "TechnicianDTOMapper", columns = {
        @ColumnResult(name = "id"),
        @ColumnResult(name = "name"),
        @ColumnResult(name = "email"),
        @ColumnResult(name = "password"),
        @ColumnResult(name = "creationDate"),
        @ColumnResult(name = "active"),
        @ColumnResult(name = "addedBy"),
})
public class TechnicianDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String creationDate;
    private boolean active;
    private String addedBy;

    public TechnicianDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
