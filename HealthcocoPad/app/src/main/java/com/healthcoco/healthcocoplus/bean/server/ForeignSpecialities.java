package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;

/**
 * Created by neha on 04/02/16.
 */
public class ForeignSpecialities extends SugarRecord {
    private String foreignUniqueId;
    private String specialities;

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    public String getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String specialities) {
        this.specialities = specialities;
    }
}
