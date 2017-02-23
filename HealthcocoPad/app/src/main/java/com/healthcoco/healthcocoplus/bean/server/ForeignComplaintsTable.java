package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by neha on 30/06/16.
 */
public class ForeignComplaintsTable extends SugarRecord {
    @Unique
    private String customUniqueId;
    private String foreignUniqueId;
    private String complaintsId;

    public ForeignComplaintsTable() {
    }

    public ForeignComplaintsTable(String foreignUniqueId, String complaintsId) {
        this.foreignUniqueId = foreignUniqueId;
        this.complaintsId = complaintsId;
        this.customUniqueId = foreignUniqueId + complaintsId;
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

    public String getComplaintsId() {
        return complaintsId;
    }

    public void setComplaintsId(String complaintsId) {
        this.complaintsId = complaintsId;
    }
}
