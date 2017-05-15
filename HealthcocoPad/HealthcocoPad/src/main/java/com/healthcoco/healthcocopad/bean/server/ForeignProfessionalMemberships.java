package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

/**
 * Created by neha on 04/02/16.
 */
public class ForeignProfessionalMemberships extends SugarRecord {
    private String foreignUniqueId;
    private String professionalMemberships;

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    public String getProfessionalMemberships() {
        return professionalMemberships;
    }

    public void setProfessionalMemberships(String professionalMemberships) {
        this.professionalMemberships = professionalMemberships;
    }
}
