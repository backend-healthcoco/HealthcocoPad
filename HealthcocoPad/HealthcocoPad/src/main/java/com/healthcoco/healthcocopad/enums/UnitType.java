package com.healthcoco.healthcocopad.enums;

import android.content.Context;

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

    public static UnitType getUnitType(Context context, String value) {
        UnitType unitType = PERCENT;
        if (context.getResources().getString(INR.getSymbolId()).equalsIgnoreCase(value))
            return INR;
        return unitType;
    }

}
