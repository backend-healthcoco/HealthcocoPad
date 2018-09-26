package com.healthcoco.healthcocopad.enums;

public enum QuantityType {

    DAYS("DAYS"), QTY("QTY"), KG("KG"), GM("GM"), MGM("MGM"), TABLE_SPOON("TABLE_SPOON"), TEA_SPOON("TEA_SPOON"),
    PERCENT("PERCENT"), CUP("CUP"), BOWL("BOWL"), LITRE("LITRE"), MILI_LITRE("MILI_LITRE"), GLASS("GLASS"), CAL("CAL");

    private String quantityType;

    QuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public String getQuantityType() {
        return quantityType;
    }

}
