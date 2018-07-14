package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by Prashant on 12/07/2018.
 */
@Parcel
public class KioskPin extends SugarRecord {
    @Unique
    private String uniqueId;
    private String doctorId;
    private Boolean discarded;
    private String pin;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
