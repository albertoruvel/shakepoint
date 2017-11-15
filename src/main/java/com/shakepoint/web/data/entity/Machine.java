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

@SqlResultSetMapping(name = "MachineEntityMapper",
        columns = {
                @ColumnResult(name = "id"),
                @ColumnResult(name = "name"),
                @ColumnResult(name = "description"),
                @ColumnResult(name = "location"),
                @ColumnResult(name = "creationDate"),
                @ColumnResult(name = "addedBy"),
                @ColumnResult(name = "technicianId"),
                @ColumnResult(name = "state"),
                @ColumnResult(name = "city"),
        })
public class Machine {
    private String id;
    private String name;
    private String description;
    private String location;
    private String creationDate;
    private String addedBy;
    private String technicianId;
    private String state;
    private String city;


    public Machine() {
        id = UUID.randomUUID().toString();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public boolean hasTechnician() {
        return this.technicianId != null;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static enum Status {
        OFF(0), ON(1), IDLE(2), FAIL(3), NOT_VALID(-1);

        int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static Status get(int value) {
            switch (value) {
                case 0:
                    return OFF;
                case 1:
                    return ON;
                case 2:
                    return IDLE;
                case 3:
                    return FAIL;
                default:
                    return NOT_VALID;
            }
        }
    }
}
