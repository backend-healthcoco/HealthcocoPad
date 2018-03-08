package com.healthcoco.healthcocopad.enums;

/**
 * Created by Prashant on 08/03/2018.
 */

public enum FontStyleType {

    BOLD("bold"),
    ITALIC("italic");

    private final String type;


    FontStyleType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
