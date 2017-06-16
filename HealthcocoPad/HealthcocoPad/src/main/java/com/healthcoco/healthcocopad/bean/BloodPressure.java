package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by neha on 14/04/16.
 */
@Parcel
public class BloodPressure{
    private String systolic;
    private String diastolic;

    public String getSystolic() {
        return systolic;
    }

    public void setSystolic(String systolic) {
        this.systolic = systolic;
    }

    public String getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(String diastolic) {
        this.diastolic = diastolic;
    }

    public boolean areAllFieldsNull() {
        if (Util.isNullOrBlank(systolic)
                && Util.isNullOrBlank(diastolic))
            return true;
        return false;
    }
}
