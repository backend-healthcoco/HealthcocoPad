package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Shreshtha on 16-03-2017.
 */
@Parcel
public class DrugsAndAllergies extends SugarRecord {
    List<Drug> drugs;
    private String allergies;

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
}
