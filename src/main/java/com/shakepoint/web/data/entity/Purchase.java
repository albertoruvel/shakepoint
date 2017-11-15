package com.shakepoint.web.data.entity;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;
import java.util.UUID;

@SqlResultSetMapping(name = "PurchaseMapper", columns = {
        @ColumnResult(name = "id"),
        @ColumnResult(name = "status"),
        @ColumnResult(name = "purchaseDate"),
        @ColumnResult(name = "total"),
        @ColumnResult(name = "machineId"),
        @ColumnResult(name = "productId"),
        @ColumnResult(name = "userId")
})
public class Purchase  {
    private String id;
    private PurchaseStatus status;
    private String purchaseDate;
    private double total;
    private String machineId;
    private String productId;
    private String userId;

    public Purchase() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PurchaseStatus getStatus() {
        return status;
    }


    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }


    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static enum PurchaseStatus {
        REQUESTED(0), // the purchase has been requested
        PAID(1),      //the purchase has been paid and authorized by a qr code
        NOT_VALID(999), //the purchase is not valid
        PRE_AUTHORIZED(666); //purchase is pre authorized per machine


        int value;

        PurchaseStatus(int value) {
            this.value = value;
        }

        public int getValue(Purchase.PurchaseStatus status) {
            return Integer.parseInt(status.toString());
        }

        public int getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return String.valueOf(this.value);
        }

        public static PurchaseStatus get(int value) {
            switch (value) {
                case 0:
                    return REQUESTED;
                case 1:
                    return PAID;
                default:
                    return NOT_VALID;
            }
        }
    }

}
