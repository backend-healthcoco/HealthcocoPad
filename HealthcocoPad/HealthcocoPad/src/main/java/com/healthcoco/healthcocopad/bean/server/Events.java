package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.AppointmentType;
import com.healthcoco.healthcocopad.enums.CalendarStatus;
import com.healthcoco.healthcocopad.fragments.PatientAppointmentDetailFragment;
import com.healthcoco.healthcocopad.skscustomclasses.CustomListData;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Prashant on 26/05/18.
 */
@Parcel
public class Events extends SugarRecord {

    protected static final String TAG = Events.class.getSimpleName();

    protected Long createdTime;
    protected Long updatedTime;

    //fields used in local
    protected Long fromDateFormattedMillis;
    protected Long toDateFormattedMillis;
    protected String doctorIdsJsonString;
    @Unique
    private String uniqueId;
    private String createdBy;
    private AppointmentStatusType state;
    private WorkingHours time;
    private String locationId;
    private String hospitalId;
    private String doctorId;
    private String forDoctor;
    private String explanation;
    private Long fromDate;
    private Long toDate;
    private Boolean isAllDayEvent;
    private Boolean isCalenderBlocked;
    private String subject;
    private Boolean isRescheduled;
    @Ignore
    private PatientCard patientCard;
    private String patientId;
    @Ignore
    private String localPatientName;
    private int fromDateMonth;
    private int fromDateYear;
    private int toDateYear;
    private int toDateMonth;
    @Ignore
    private ArrayList<String> doctorIds;

    public String getLocalPatientName() {
        return localPatientName;
    }

    public void setLocalPatientName(String localPatientName) {
        this.localPatientName = localPatientName;
    }

    public Long getFromDateFormattedMillis() {
        if (fromDateFormattedMillis == null)
            fromDateFormattedMillis = 0l;
        return fromDateFormattedMillis;
    }

    public void setFromDateFormattedMillis(Long fromDateFormattedMillis) {
        this.fromDateFormattedMillis = fromDateFormattedMillis;
    }

    public Long getToDateFormattedMillis() {
        if (toDateFormattedMillis == null)
            toDateFormattedMillis = 0l;
        return toDateFormattedMillis;
    }

    public void setToDateFormattedMillis(Long toDateFormattedMillis) {
        this.toDateFormattedMillis = toDateFormattedMillis;
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

    public AppointmentStatusType getState() {
        return state;
    }

    public void setState(AppointmentStatusType state) {
        this.state = state;
    }

    public WorkingHours getTime() {
        return time;
    }

    public void setTime(WorkingHours time) {
        this.time = time;
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public Boolean getIsAllDayEvent() {
        return isAllDayEvent;
    }

    public void setIsAllDayEvent(Boolean isAllDayEvent) {
        this.isAllDayEvent = isAllDayEvent;
    }

    public Boolean getIsCalenderBlocked() {
        return isCalenderBlocked;
    }

    public void setIsCalenderBlocked(Boolean isCalenderBlocked) {
        this.isCalenderBlocked = isCalenderBlocked;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getIsRescheduled() {
        return isRescheduled;
    }

    public void setIsRescheduled(Boolean isRescheduled) {
        this.isRescheduled = isRescheduled;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Long getLongDateTime(Long fromDate, WorkingHours time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fromDate);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                DateTimeUtil.getHoursFromMinutes(Math.round(time.getFromTime())), DateTimeUtil.getMinutes(Math.round(time.getFromTime())));
        LogUtils.LOGD(TAG, "TimeInMillis " + calendar.getTimeInMillis());
        return calendar.getTimeInMillis();
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Boolean getAllDayEvent() {
        return isAllDayEvent;
    }

    public void setAllDayEvent(Boolean allDayEvent) {
        isAllDayEvent = allDayEvent;
    }

    public Boolean getCalenderBlocked() {
        return isCalenderBlocked;
    }

    public void setCalenderBlocked(Boolean calenderBlocked) {
        isCalenderBlocked = calenderBlocked;
    }

    public Boolean getRescheduled() {
        return isRescheduled;
    }

    public void setRescheduled(Boolean rescheduled) {
        isRescheduled = rescheduled;
    }

    public PatientCard getPatientCard() {
        return patientCard;
    }

    public void setPatientCard(PatientCard patientCard) {
        this.patientCard = patientCard;
    }

    public int getFromDateMonth() {
        return fromDateMonth;
    }

    public void setFromDateMonth(int fromDateMonth) {
        this.fromDateMonth = fromDateMonth;
    }

    public int getFromDateYear() {
        return fromDateYear;
    }

    public void setFromDateYear(int fromDateYear) {
        this.fromDateYear = fromDateYear;
    }

    public int getToDateYear() {
        return toDateYear;
    }

    public void setToDateYear(int toDateYear) {
        this.toDateYear = toDateYear;
    }

    public int getToDateMonth() {
        return toDateMonth;
    }

    public void setToDateMonth(int toDateMonth) {
        this.toDateMonth = toDateMonth;
    }

    public ArrayList<String> getDoctorIds() {
        return doctorIds;
    }

    public void setDoctorIds(ArrayList<String> doctorIds) {
        this.doctorIds = doctorIds;
    }

    public String getDoctorIdsJsonString() {
        return doctorIdsJsonString;
    }

    public void setDoctorIdsJsonString(String doctorIdsJsonString) {
        this.doctorIdsJsonString = doctorIdsJsonString;
    }

    public String getForDoctor() {
        return forDoctor;
    }

    public void setForDoctor(String forDoctor) {
        this.forDoctor = forDoctor;
    }
}
