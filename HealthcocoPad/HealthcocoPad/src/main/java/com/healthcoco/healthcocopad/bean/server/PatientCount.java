package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

@org.parceler.Parcel
public class PatientCount extends SugarRecord {

    private String doctorId;
    private String locationId;
    private String hospitalId;
    private long count;
    private boolean syncCompleted;


    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isSyncCompleted() {
        return syncCompleted;
    }

    public void setSyncCompleted(boolean syncCompleted) {
        this.syncCompleted = syncCompleted;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}
