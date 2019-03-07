package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class VaccineBrandAssociationRequest extends SugarRecord {
    @Unique
    private String uniqueId;
    private String name;
    @Ignore
    private ArrayList<VaccineBrandResponse> vaccineBrandAssociationResponses;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<VaccineBrandResponse> getVaccineBrandAssociationResponses() {
        return vaccineBrandAssociationResponses;
    }

    public void setVaccineBrandAssociationResponses(ArrayList<VaccineBrandResponse> vaccineBrandAssociationResponses) {
        this.vaccineBrandAssociationResponses = vaccineBrandAssociationResponses;
    }
}
