package com.shakepoint.web.core.machine;
public enum ProductType {
    SIMPLE(0), COMBO(1), NOT_VALID(9);

    int value;

    ProductType(int value) {
        this.value = value;
    }

    public static ProductType get(int value) {
        switch (value) {
            case 0:
                return SIMPLE;
            case 1:
                return COMBO;
            default:
                return NOT_VALID;
        }
    }

    public int getValue() {
        return value;
    }
}
