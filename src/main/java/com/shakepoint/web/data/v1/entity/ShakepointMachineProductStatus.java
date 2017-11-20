package com.shakepoint.web.data.v1.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "MachineProductStatus")
@Table(name = "machine_product")
public class ShakepointMachineProductStatus {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id")
    private ShakepointMachine machine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ShakepointProduct product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "updated_by")
    private ShakepointUser updatedBy;

    @Column(name = "available_percentage")
    private int percentage;

    @Column(name = "slot_number")
    private int  slotNumber;

    public ShakepointMachineProductStatus() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShakepointMachine getMachine() {
        return machine;
    }

    public void setMachine(ShakepointMachine machine) {
        this.machine = machine;
    }

    public ShakepointProduct getProduct() {
        return product;
    }

    public void setProduct(ShakepointProduct product) {
        this.product = product;
    }

    public ShakepointUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(ShakepointUser updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }
}
