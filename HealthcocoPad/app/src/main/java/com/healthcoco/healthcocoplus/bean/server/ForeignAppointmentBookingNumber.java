package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by neha on 04/02/16.
 */
public class ForeignAppointmentBookingNumber extends SugarRecord {
    @Unique
    private String customUniqueId;
    private String foreignUniqueId;
    private String appintmentBookingNumber;

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    public String getAppintmentBookingNumber() {
        return appintmentBookingNumber;
    }

    public void setAppintmentBookingNumber(String appintmentBookingNumber) {
        this.appintmentBookingNumber = appintmentBookingNumber;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }
}
