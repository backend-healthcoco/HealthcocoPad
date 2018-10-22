package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.NutrientCategoryType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;

import org.parceler.Parcel;

@Parcel
public class NutrientResponse extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(NutrientResponse.class.getSimpleName());


    private String uniqueId;
    private String name;
    private QuantityType type;
    private NutrientCategoryType category;
    private String nutrientCode;
    private Boolean discarded;

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

    public QuantityType getType() {
        return type;
    }

    public void setType(QuantityType type) {
        this.type = type;
    }

    public String getNutrientCode() {
        return nutrientCode;
    }

    public void setNutrientCode(String nutrientCode) {
        this.nutrientCode = nutrientCode;
    }

    public NutrientCategoryType getCategory() {
        return category;
    }

    public void setCategory(NutrientCategoryType category) {
        this.category = category;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }
}
