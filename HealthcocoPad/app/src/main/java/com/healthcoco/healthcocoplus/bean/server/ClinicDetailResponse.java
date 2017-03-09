package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by neha on 30/08/16.
 */
@Parcel
public class ClinicDetailResponse extends SugarRecord {
    //represents locationId
    @Unique
    private String uniqueId;
    @Ignore
    private Location location;
    @Ignore
    private List<ClinicDoctorProfile> doctors;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Location getLocation() {
        return location;
    }

    public List<ClinicDoctorProfile> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<ClinicDoctorProfile> doctors) {
        this.doctors = doctors;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
