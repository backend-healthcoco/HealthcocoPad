package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by Prashant on 24/09/2018.
 */

@Parcel
public class RecipeItem extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(RecipeItem.class.getSimpleName());

    protected String foreignMealId;
    protected String customUniqueId;

    private String uniqueId;
    private String name;
    private double value;
    private QuantityType type;

    public String getForeignMealId() {
        return foreignMealId;
    }

    public void setForeignMealId(String foreignMealId) {
        this.foreignMealId = foreignMealId;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public QuantityType getType() {
        return type;
    }

    public void setType(QuantityType type) {
        this.type = type;
    }
}
