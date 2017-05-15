package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

/**
 * Created by neha on 30/06/16.
 */
public class ForeignInvestigationsTable extends SugarRecord {
    @Unique
    private String customUniqueId;
    private String foreignUniqueId;
    private String investigationId;

    public ForeignInvestigationsTable() {
    }

    public ForeignInvestigationsTable(String foreignUniqueId, String investigationId) {
        this.foreignUniqueId = foreignUniqueId;
        this.investigationId = investigationId;
        this.customUniqueId = foreignUniqueId + investigationId;
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

    public String getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(String investigationId) {
        this.investigationId = investigationId;
    }
}
