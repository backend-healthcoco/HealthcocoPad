package com.healthcoco.healthcocopad.enums;

import android.os.Parcel;

import com.healthcoco.healthcocopad.utilities.LocalDatabaseUtils;

/**
 * Created by neha on 20/03/16.
 */
public enum BooleanTypeValues {
    TRUE(LocalDatabaseUtils.BOOLEAN_TRUE_VALUE, true), FALSE(LocalDatabaseUtils.BOOLEAN_FALSE_VALUE, false);

    private final int booleanTypeValue;
    private final boolean booleanFlag;

    BooleanTypeValues(int booleanTrueValue, boolean booleanFlag) {
        this.booleanTypeValue = booleanTrueValue;
        this.booleanFlag = booleanFlag;
    }

    public static Boolean readBoolean(Parcel in) {
        return (in.readInt() == BooleanTypeValues.FALSE.getBooleanIntValue()) ? false : true;
    }

    public static int writeBoolean(Boolean twentyFourSevenOpen) {
        return (!twentyFourSevenOpen || twentyFourSevenOpen == null ? BooleanTypeValues.FALSE.getBooleanIntValue() : BooleanTypeValues.TRUE.getBooleanIntValue());
    }

    public int getBooleanIntValue() {
        return booleanTypeValue;
    }

    public boolean getBooleanFlag() {
        return booleanFlag;
    }
}
