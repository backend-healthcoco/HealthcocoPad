package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.DoctorWorkingSchedule;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class AddEditLocaleVisitDetailsRequest {

    private List<DoctorWorkingSchedule> workingSchedules;
    private Boolean isTwentyFourSevenOpen;
    private String uniqueId;
    private String doctorId;
    private String locationId;

    public List<DoctorWorkingSchedule> getWorkingSchedules() {
        return workingSchedules;
    }

    public void setWorkingSchedules(List<DoctorWorkingSchedule> workingSchedules) {
        this.workingSchedules = workingSchedules;
    }

    public Boolean getTwentyFourSevenOpen() {
        return isTwentyFourSevenOpen;
    }

    public void setTwentyFourSevenOpen(Boolean twentyFourSevenOpen) {
        isTwentyFourSevenOpen = twentyFourSevenOpen;
    }

    public Boolean getIsTwentyFourSevenOpen() {
        return isTwentyFourSevenOpen;
    }

    public void setIsTwentyFourSevenOpen(Boolean isTwentyFourSevenOpen) {
        this.isTwentyFourSevenOpen = isTwentyFourSevenOpen;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return "AddEditLocaleVisitDetailsRequest [localeWorkingSchedules=" + workingSchedules
                + ", isTwentyFourSevenOpen=" + isTwentyFourSevenOpen + "]";
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
