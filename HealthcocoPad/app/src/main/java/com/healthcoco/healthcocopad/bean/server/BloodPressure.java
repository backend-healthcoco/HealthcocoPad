package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import java.io.Serializable;

/**
 * Created by neha on 14/04/16.
 */
public class BloodPressure extends SugarRecord implements Serializable {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(BloodPressure.class.getSimpleName());
    @Unique
    protected String foreignTableId;
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

    public String getForeignTableId() {
        return foreignTableId;
    }

    public void setForeignTableId(String foreignTableId) {
        this.foreignTableId = foreignTableId;
    }

    public boolean areAllFieldsNull() {
        if (Util.isNullOrBlank(systolic)
                && Util.isNullOrBlank(diastolic))
            return true;
        return false;
    }
}
