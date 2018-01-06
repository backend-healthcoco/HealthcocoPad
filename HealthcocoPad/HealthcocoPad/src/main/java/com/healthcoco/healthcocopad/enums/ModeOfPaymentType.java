package com.healthcoco.healthcocopad.enums;

/**
 * Created by Shreshtha on 07-07-2017.
 */

public enum ModeOfPaymentType {
    CASH("CASH"), WALLET("WALLET"), CARD("CARD");
    private String type;

    private ModeOfPaymentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
