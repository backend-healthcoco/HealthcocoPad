package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.NutrientCategoryType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

@Parcel
public class Nutrients extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(Nutrients.class.getSimpleName());

    protected String foreignMealId;
    protected String customUniqueId;

    private String uniqueId;
    private String name;
    @Ignore
    private NutrientCategoryType category;
    private double value;
    @Ignore
    private QuantityType type;
    private double inPercent;
    private String note;


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

    public NutrientCategoryType getCategory() {
        return category;
    }

    public void setCategory(NutrientCategoryType category) {
        this.category = category;
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

    public double getInPercent() {
        return inPercent;
    }

    public void setInPercent(double inPercent) {
        this.inPercent = inPercent;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
