package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

/**
 * Created by neha on 04/02/16.
 */
public class ForieignAdditionalNumbers extends SugarRecord {
    private String foreignUniqueId;
    private String additionalNumber;
    @Unique
    private String customUniqueId;

    public String getAdditionalNumber() {
        return additionalNumber;
    }

    public void setAdditionalNumber(String additionalNumber) {
        this.additionalNumber = additionalNumber;
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }
}
