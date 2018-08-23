package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Prashant on 02-07-2018.
 */
@Parcel
public class KioskTabPermission extends SugarRecord {
    //fields used in local
    protected String tagsJsonString;
    @Unique
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
    private Boolean discarded;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    @Ignore
    private ArrayList<String> TabPermission;

    public String getTagsJsonString() {
        return tagsJsonString;
    }

    public void setTagsJsonString(String tagsJsonString) {
        this.tagsJsonString = tagsJsonString;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
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

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public ArrayList<String> getTabPermission() {
        return TabPermission;
    }

    public void setTabPermission(ArrayList<String> tabPermission) {
        TabPermission = tabPermission;
    }
}
