package com.healthcoco.healthcocopad.enums;

public enum QuantityType {

    DAYS("DAYS", "days"),
    QTY("QTY", "qty"),
    KG("KG", "Kg"),
    G("G", "gm"),
    MG("MG", "mgm"),
    UG("UG", "ugm"),
    IU("IU", "IU"),
    TABLE_SPOON("TABLE_SPOON", "Table Spoon"),
    TEA_SPOON("TEA_SPOON", "Tea Spoon"),
    PERCENT("PERCENT", "%"),
    CUP("CUP", "Cup"),
    BOWL("BOWL", "Bowl"),
    LITRE("LITRE", "Ltr"),
    MILI_LITRE("MILI_LITRE", "ml"),
    GLASS("GLASS", "glass"),
    CAL("CAL", "Cal"),
    KCAL("KCAL", "Kcal");


    private String quantityType;
    private String unit;

    QuantityType(String quantityType, String unit) {
        this.quantityType = quantityType;
        this.unit = unit;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public String getUnit() {
        return unit;
    }
}
