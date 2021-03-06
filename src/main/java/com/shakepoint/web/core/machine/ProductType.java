package com.shakepoint.web.core.machine;

public enum ProductType {
    PROTEIN(0, "PROTEIN", "Proteína"), AMINO_ACID(1, "AMINO_ACID", "Aminoácido"), OXIDE(9, "OXIDE", "Pre-Entreno");

    int value;
    String name;
    String clientValue;

    ProductType(int value, String name, String clientValue) {
        this.value = value;
        this.name = name;
        this.clientValue = clientValue;
    }

    public static ProductType getProductType(String value) {
        if (value.toUpperCase().equals("PROTEIN")) {
            return PROTEIN;
        } else if (value.toUpperCase().equals("AMINO_ACID")) {
            return AMINO_ACID;
        } else if (value.toUpperCase().equals("OXIDE")) {
            return OXIDE;
        } else return null;
    }

    public static String getProductTypeForClient(ProductType type){
        switch (type){
            case AMINO_ACID:
                return AMINO_ACID.clientValue;
            case OXIDE:
                return OXIDE.clientValue;
            case PROTEIN:
                return PROTEIN.clientValue;
            default:
                return null;
        }
    }

    public String getName(){
        return name;
    }
}
