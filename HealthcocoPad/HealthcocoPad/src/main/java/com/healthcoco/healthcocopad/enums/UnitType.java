package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

public enum UnitType {
    PERCENT("PERCENT", R.string.percentage), INR("INR", R.string.inr);

    private int symbolId;
    private String unit;

    private UnitType(String unit, int symbolId) {
        this.unit = unit;
        this.symbolId = symbolId;
    }

    public String getUnit() {
        return unit;
    }

    public int getSymbolId() {
        return symbolId;
    }

}
