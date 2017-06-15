package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.WorkingHours;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.AppointmentType;
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
 * Created by neha on 11/05/16.
 */
@Parcel
public class CalendarEvents extends SugarRecord {
    protected static final String TAG = CalendarEvents.class.getSimpleName();
    @Unique
    private String uniqueId;
    protected boolean isAddedOnSuccess;
    protected Long createdTime;
    protected Long updatedTime;
    private String createdBy;
    private String cancelledBy;
    private AppointmentStatusType state;
    private AppointmentType type;
    @Ignore
    private WorkingHours time;
    protected String workingHoursJson;
    private String locationId;
    private String hospitalId;
    private String doctorId;
    private String doctorName;
    private String explanation;
    private String clinicNumber;
    private String locationName;
    private String appointmentId;
    private Long fromDate;
    private Long toDate;
    private Boolean isAllDayEvent;
    private Boolean isCalenderBlocked;
    private Double latitude;
    private Double longitude;
    private String subject;
    private Boolean isFeedbackAvailable;
    private Boolean isRescheduled;
    @Ignore
    private PatientCard patient;
    private String patientId;
    private Boolean isFromCalendarAPI;

    protected static LinkedHashMap<String, CustomListData> customListDataHashMap = new LinkedHashMap<>();
    protected Boolean isSelected;
    protected Boolean isStartDate;
    protected Boolean isEndDate;

    //fields used in local
    protected Long fromDateFormattedMillis;
    protected Long toDateFormattedMillis;

    private Boolean notifyDoctorByEmail;
    private Boolean notifyDoctorBySms;
    private Boolean notifyPatientByEmail;
    private Boolean notifyPatientBySms;
    @Ignore
    private String localPatientName;
    private String mobileNumber;

    public String getLocalPatientName() {
        return localPatientName;
    }

    public void setLocalPatientName(String localPatientName) {
        this.localPatientName = localPatientName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getNotifyDoctorByEmail() {
        return notifyDoctorByEmail;
    }

    public void setNotifyDoctorByEmail(Boolean notifyDoctorByEmail) {
        this.notifyDoctorByEmail = notifyDoctorByEmail;
    }

    public Boolean getNotifyDoctorBySms() {
        return notifyDoctorBySms;
    }

    public void setNotifyDoctorBySms(Boolean notifyDoctorBySms) {
        this.notifyDoctorBySms = notifyDoctorBySms;
    }

    public Boolean getNotifyPatientByEmail() {
        return notifyPatientByEmail;
    }

    public void setNotifyPatientByEmail(Boolean notifyPatientByEmail) {
        this.notifyPatientByEmail = notifyPatientByEmail;
    }

    public Boolean getNotifyPatientBySms() {
        return notifyPatientBySms;
    }

    public void setNotifyPatientBySms(Boolean notifyPatientBySms) {
        this.notifyPatientBySms = notifyPatientBySms;
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

    public Boolean getIsSelected() {
        if (isSelected == null)
            isSelected = false;
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Boolean getIsStartDate() {
        if (isStartDate == null)
            isStartDate = false;
        return isStartDate;
    }

    public void setIsStartDate(Boolean isStartDate) {
        this.isStartDate = isStartDate;
    }

    public Boolean getIsEndDate() {
        if (isEndDate == null)
            isEndDate = false;
        return isEndDate;
    }

    public void setIsEndDate(Boolean isEndDate) {
        this.isEndDate = isEndDate;
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

    public AppointmentType getType() {
        return type;
    }

    public void setType(AppointmentType type) {
        this.type = type;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getClinicNumber() {
        return clinicNumber;
    }

    public void setClinicNumber(String clinicNumber) {
        this.clinicNumber = clinicNumber;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getIsFeedbackAvailable() {
        return isFeedbackAvailable;
    }

    public void setIsFeedbackAvailable(Boolean isFeedbackAvailable) {
        this.isFeedbackAvailable = isFeedbackAvailable;
    }

    public Boolean getIsRescheduled() {
        return isRescheduled;
    }

    public void setIsRescheduled(Boolean isRescheduled) {
        this.isRescheduled = isRescheduled;
    }

    public PatientCard getPatient() {
        return patient;
    }

    public void setPatient(PatientCard patient) {
        this.patient = patient;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Boolean getIsFromCalendarAPI() {
        if (isFromCalendarAPI == null)
            isFromCalendarAPI = false;
        return isFromCalendarAPI;
    }

    public void setIsFromCalendarAPI(Boolean isFromCalendarAPI) {
        this.isFromCalendarAPI = isFromCalendarAPI;
    }

    public boolean isAddedOnSuccess() {
        return isAddedOnSuccess;
    }

    public void setIsAddedOnSuccess(boolean isAddedOnSuccess) {
        this.isAddedOnSuccess = isAddedOnSuccess;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Long getLongDateTime(Long fromDate, WorkingHours time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fromDate);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                DateTimeUtil.getHoursFromMinutes(Math.round(time.getFromTime())), DateTimeUtil.getMinutes(Math.round(time.getFromTime())));
        LogUtils.LOGD(TAG, "TimeInMillis " + calendar.getTimeInMillis());
        return calendar.getTimeInMillis();
    }

    public static ArrayList<CustomListData> getSectionedDataAppointment(List<CalendarEvents> list) {
        ArrayList<CustomListData> sksListData = new ArrayList<CustomListData>();
        String currentHeader = "";
        List<CalendarEvents> headerDataList = null;
        CustomListData currentCustomListData = null;
        if (!Util.isNullOrEmptyList(list)) {
            for (CalendarEvents appointment : list) {
                String formattedCreatedTime = DateTimeUtil.getFormattedDateTime(PatientAppointmentDetailFragment.DATE_FORMAT_USED_IN_THIS_SCREEN, appointment.getFromDate());
                if (!currentHeader.equalsIgnoreCase(formattedCreatedTime)) {
                    if (currentCustomListData != null && headerDataList != null) {
                        currentCustomListData.setHeaderDataList(headerDataList);
                        sksListData.add(currentCustomListData);
                    }
                    currentCustomListData = new CustomListData();
                    headerDataList = new ArrayList<CalendarEvents>();
                    currentHeader = formattedCreatedTime;
                    currentCustomListData.setHeader("" + currentHeader);
                    headerDataList.add(appointment);
                } else
                    headerDataList.add(appointment);
            }
            if (currentCustomListData != null) {
                currentCustomListData.setHeaderDataList(headerDataList);
                sksListData.add(currentCustomListData);
            }
        }
        return sksListData;
    }

    public String getWorkingHoursJson() {
        return workingHoursJson;
    }

    public void setWorkingHoursJson(String workingHoursJson) {
        this.workingHoursJson = workingHoursJson;
    }
}
