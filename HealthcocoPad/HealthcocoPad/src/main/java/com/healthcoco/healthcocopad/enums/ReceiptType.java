package com.healthcoco.healthcocopad.enums;

/**
 * Created by Shreshtha on 07-07-2017.
 */

public enum ReceiptType {
    INVOICE("INVOICE"), ADVANCE("ADVANCE"), REFUND("REFUND");;

    private String type;

    private ReceiptType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
