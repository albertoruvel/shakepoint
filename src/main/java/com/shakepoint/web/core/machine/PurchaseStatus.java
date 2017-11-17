package com.shakepoint.web.core.machine;

public enum PurchaseStatus {
    REQUESTED(0), // the purchase has been requested
    PAID(1),      //the purchase has been paid and authorized by a qr code
    NOT_VALID(999), //the purchase is not valid
    PRE_AUTHORIZED(666); //purchase is pre authorized per machine


    int value;

    PurchaseStatus(int value) {
        this.value = value;
    }

    public int getValue(PurchaseStatus status) {
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
