package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

/**
 * Created by neha on 30/06/16.
 */
public class ForeignDiagnosesTable  extends SugarRecord {
    @Unique
    private String customUniqueId;
    private String foreignUniqueId;
    private String diagnosesId;

    public ForeignDiagnosesTable() {
    }

    public ForeignDiagnosesTable(String foreignUniqueId, String diagnosesId) {
        this.foreignUniqueId = foreignUniqueId;
        this.diagnosesId = diagnosesId;
        this.customUniqueId = foreignUniqueId + diagnosesId;
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

    public String getDiagnosesId() {
        return diagnosesId;
    }

    public void setDiagnosesId(String diagnosesId) {
        this.diagnosesId = diagnosesId;
    }
}
