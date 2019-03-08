package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class VaccineCustomResponse extends SugarRecord {
    private String duration;
    private Long dueDate;
    private ArrayList<VaccineResponse> vaccineResponse;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<VaccineResponse> getVaccineResponse() {
        return vaccineResponse;
    }

    public void setVaccineResponse(ArrayList<VaccineResponse> vaccineResponse) {
        this.vaccineResponse = vaccineResponse;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }
}
