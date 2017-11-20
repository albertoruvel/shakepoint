package com.shakepoint.web.data.v1.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "ComboProduct")
@Table(name = "combo_product")
public class ShakepointComboProduct {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ShakepointProduct comboProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "combo_item")
    private ShakepointProduct product;

    public ShakepointComboProduct() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShakepointProduct getComboProduct() {
        return comboProduct;
    }

    public void setComboProduct(ShakepointProduct comboProduct) {
        this.comboProduct = comboProduct;
    }

    public ShakepointProduct getProduct() {
        return product;
    }

    public void setProduct(ShakepointProduct product) {
        this.product = product;
    }
}
