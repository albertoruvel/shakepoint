/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.data.dto.res;

import com.shakepoint.web.data.v1.dto.mvc.response.MachineProductData;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
@SqlResultSetMapping(
        name = "MachineDTOMapping",
        columns = {
                @ColumnResult(name = "id"),
                @ColumnResult(name = "name"),
                @ColumnResult(name = "description"),
                @ColumnResult(name = "location"),
                @ColumnResult(name = "slots"),
                @ColumnResult(name = "state"),
                @ColumnResult(name = "city"),
                @ColumnResult(name = "technicianName"),
                @ColumnResult(name = "technicianId"),
                @ColumnResult(name = "productsCount"),
        }
)
public class MachineDTO {
    private String id; 
    private String name; 
    private String description; 
    private String location; 
    private int slots; 
    private String state; 
    private String city; 
    
    //technician data 
    private String technicianName; 
    private String technicianId;
    
    private int productsCount;
    private List<MachineProductData> products;

    public MachineDTO() {
        products = new ArrayList();
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

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public int getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
    }

    public List<MachineProductData> getProducts() {
        return products;
    }

    public void setProducts(List<MachineProductData> products) {
        this.products = products;
    }

	public int getSlots() {
		return slots;
	}

	public void setSlots(int slots) {
		this.slots = slots;
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
    
    
	
    
}
