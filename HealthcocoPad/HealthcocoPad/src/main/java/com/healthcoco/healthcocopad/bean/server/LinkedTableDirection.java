package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

/**
 * Created by neha on 25/07/16.
 */
public class LinkedTableDirection extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(LinkedTableDirection.class.getSimpleName());
    @Unique
    private String uniqueId;
    private String foreignTableId;
    private String foreignTableKey;
    private String drugId;
    private String directionId;

    public LinkedTableDirection() {
    }

    public LinkedTableDirection(String foreignTableId, String foreignTableKey, String drugId, String directionId) {
        this.foreignTableId = foreignTableId;
        this.foreignTableKey = foreignTableKey;
        this.drugId = drugId;
        this.directionId = directionId;
        this.uniqueId = foreignTableKey + foreignTableId + drugId + directionId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getForeignTableId() {
        return foreignTableId;
    }

    public void setForeignTableId(String foreignTableId) {
        this.foreignTableId = foreignTableId;
    }

    public String getForeignTableKey() {
        return foreignTableKey;
    }

    public void setForeignTableKey(String foreignTableKey) {
        this.foreignTableKey = foreignTableKey;
    }

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getDirectionId() {
        return directionId;
    }

    public void setDirectionId(String directionId) {
        this.directionId = directionId;
    }
}
