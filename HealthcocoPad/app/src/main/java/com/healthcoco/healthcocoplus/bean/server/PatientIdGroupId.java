package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by neha on 04/12/15.
 */
public class PatientIdGroupId extends SugarRecord {
    @Unique
    private String patientIdGroupIdUnique;
    private String foreignPatientId;
    private String localPatientName;
    private String foreignGroupId;


    public String getPatientIdGroupIdUnique() {
        return patientIdGroupIdUnique;
    }

    public void setPatientIdGroupIdUnique(String patientIdGroupIdUnique) {
        this.patientIdGroupIdUnique = patientIdGroupIdUnique;
    }

    public String getForeignGroupId() {
        return foreignGroupId;
    }

    public void setForeignGroupId(String foreignGroupId) {
        this.foreignGroupId = foreignGroupId;
    }

    public String getForeignPatientId() {
        return foreignPatientId;
    }

    public void setForeignPatientId(String foreignPatientId) {
        this.foreignPatientId = foreignPatientId;
    }

    public String getLocalPatientName() {
        return localPatientName;
    }

    public void setLocalPatientName(String localPatientName) {
        this.localPatientName = localPatientName;
    }
}
