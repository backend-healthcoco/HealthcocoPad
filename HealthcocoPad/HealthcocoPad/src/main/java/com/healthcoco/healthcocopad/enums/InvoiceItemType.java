package com.healthcoco.healthcocopad.enums;

/**
 * Created by Shreshtha on 04-07-2017.
 */
public enum InvoiceItemType {
    PRODUCT("PRODUCT"), TEST("TEST"), SERVICE("SERVICE");
    private String type;

    private InvoiceItemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
