package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Prashant on 26/05/18.
 */
@Parcel
public class MultipleDoctorEvents extends SugarRecord {

    protected static final String TAG = MultipleDoctorEvents.class.getSimpleName();

    private String doctorId;
    private String eventId;
    private Long fromDate;
    private Long toDate;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }
}
