package com.shakepoint.web.data.dto.res;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
        name = "TechnicianMachineDTOMapping",
        columns = {
                @ColumnResult(name = "id"),
                @ColumnResult(name = "name"),
                @ColumnResult(name = "description"),
                @ColumnResult(name = "slots"),
                @ColumnResult(name = "products"),
                @ColumnResult(name = "alerted"),
        }
)
public class TechnicianMachine {
    private String id;
    private String name;
    private String description;
    private int slots;
    private int products;
    private boolean alerted;

    public TechnicianMachine() {
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

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    public boolean isAlerted() {
        return alerted;
    }

    public void setAlerted(boolean alerted) {
        this.alerted = alerted;
    }


}
