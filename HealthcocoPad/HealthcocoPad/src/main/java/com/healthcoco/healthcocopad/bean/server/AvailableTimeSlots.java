package com.healthcoco.healthcocopad.bean.server;

import org.parceler.Parcel;

/**
 * Created by neha on 07/05/16.
 */
@Parcel
public class AvailableTimeSlots {
    private String time;
    private Boolean isAvailable;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
