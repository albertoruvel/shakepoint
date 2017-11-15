package com.shakepoint.web.data.dto.res;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "ShakepointUserMapper",
        columns = {
		    @ColumnResult(name = "id"),
		    @ColumnResult(name = "name"),
		    @ColumnResult(name = "email"),
		    @ColumnResult(name = "purchasesTotal")
        })
public class ShakePointUser {
    private String id;
    private String name;
    private String email;
    private double purchasesTotal;


    public ShakePointUser() {
        super();
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

    public double getPurchasesTotal() {
        return purchasesTotal;
    }

    public void setPurchasesTotal(double purchasesTotal) {
        this.purchasesTotal = purchasesTotal;
    }


}
