package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import java.io.Serializable;

public class Duration extends SugarRecord implements Serializable {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(Duration.class.getSimpleName());
    private String value;
    private DrugDurationUnit durationUnit;
    protected String foreignDrugDurationUnit;
    @Unique
    protected String customUniqueId;
    protected String foreignTableId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DrugDurationUnit getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(DrugDurationUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    @Override
    public String toString() {
        return "Duration [value=" + value + ", durationUnit=" + durationUnit + "]";
    }

    public String getForeignDrugDurationUnit() {
        return foreignDrugDurationUnit;
    }

    public void setForeignDrugDurationUnit(String foreignDrugDurationUnit) {
        this.foreignDrugDurationUnit = foreignDrugDurationUnit;
    }

    public String getCustomUniqueId() {
        customUniqueId = foreignTableId + foreignDrugDurationUnit + value;
        return customUniqueId;
    }

    public String getForeignTableId() {
        return foreignTableId;
    }

    public void setForeignTableId(String foreignTableId) {
        this.foreignTableId = foreignTableId;
    }
}
