package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

/**
 * Created by neha on 22/12/15.
 */
public class VisitedForTypeTable extends SugarRecord {
    private String visitId;
    private VisitedForType visitedForType;
    @Unique
    protected String customUniqueId;

    public VisitedForType getVisitedForType() {
        return visitedForType;
    }

    public void setVisitedForType(VisitedForType visitedForType) {
        this.visitedForType = visitedForType;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }
}
