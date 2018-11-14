package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

public enum LifeStyleType {

    SENDETARY("SENDETARY", R.string.sedentary),
    MODERATE("MODERATE", R.string.moderate),
    HEAVY("HEAVY", R.string.heavy);

    private String type;
    private int typeTitle;

    LifeStyleType(String type, int typeTitle) {
        this.type = type;
        this.typeTitle = typeTitle;
    }

    public String getType() {
        return type;
    }

    public int getTypeTitle() {
        return typeTitle;
    }
}
