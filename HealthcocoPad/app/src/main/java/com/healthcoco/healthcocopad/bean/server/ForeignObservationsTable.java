package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

/**
 * Created by neha on 30/06/16.
 */
public class ForeignObservationsTable extends SugarRecord {
    @Unique
    private String customUniqueId;
    private String foreignUniqueId;
    private String observationId;

    public ForeignObservationsTable() {
    }

    public ForeignObservationsTable(String foreignUniqueId, String observationId) {
        this.foreignUniqueId = foreignUniqueId;
        this.observationId = observationId;
        this.customUniqueId = foreignUniqueId + observationId;
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

    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }
}
