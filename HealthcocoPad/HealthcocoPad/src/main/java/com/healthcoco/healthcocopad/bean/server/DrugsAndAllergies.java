package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Shreshtha on 16-03-2017.
 */
@Parcel
public class DrugsAndAllergies extends SugarRecord {
    @Ignore
    List<Drug> drugs;
    private String drugIdsJsonString;
    private String allergies;
    @Unique
    private String patientId;

    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDrugIdsJsonString() {
        return drugIdsJsonString;
    }

    public void setDrugIdsJsonString(String drugIdsJsonString) {
        this.drugIdsJsonString = drugIdsJsonString;
    }
}
