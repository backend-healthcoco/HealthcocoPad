package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.WorkingHours;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
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
    protected static LinkedHashMap<String, CustomListData> customListDataHashMap = new LinkedHashMap<>();
    protected boolean isAddedOnSuccess;
    protected Long createdTime;
    protected Long updatedTime;
    protected Boolean isSelected;
    protected Boolean isStartDate;
    protected Boolean isEndDate;
    //fields used in local
    protected Long fromDateFormattedMillis;
    protected Long toDateFormattedMillis;
    @Unique
    private String uniqueId;
    private String createdBy;
    private String cancelledBy;
    private AppointmentStatusType state;
    private AppointmentType type;
    private WorkingHours time;
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
    private Boolean notifyDoctorByEmail;
    private Boolean notifyDoctorBySms;
    private Boolean notifyPatientByEmail;
    private Boolean notifyPatientBySms;
    @Ignore
    private String localPatientName;
    private String mobileNumber;
    private String status;

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
//        try {
//            Calendar calendarFromDate = Calendar.getInstance();
//            calendarFromDate.setTimeInMillis(fromDate);
//
//            Calendar FromTime = Calendar.getInstance();
//            FromTime.setTimeInMillis(DateTimeUtil.getFormattedTime(time.getFromTime()));
//
//            calendarFromDate.set(Calendar.HOUR_OF_DAY, calendarFromDate.get(Calendar.HOUR_OF_DAY));
//            calendarFromDate.set(Calendar.MINUTE, calendarFromDate.get(Calendar.MINUTE));
//            calendarFromDate.set(Calendar.SECOND, calendarFromDate.get(Calendar.SECOND));
//            return calendarFromDate.getTimeInMillis();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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

//    public static ArrayList<CustomListData> getSectionedDataListCalendar(long selectedTimeMillis, List<CalendarEvents> list) {
//        customListDataHashMap = new LinkedHashMap<>();
//        LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading Start");
//        for (CalendarEvents calendarEvents :
//                list) {
//            if (CalendarFragment.asyncTask != null && !CalendarFragment.asyncTask.isCancelled()) {
//                String formattedFromDayHeader = DateTimeUtil.getFormattedDateTime(CalendarFragment.DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN, calendarEvents.getFromDate());
//                String formattedToDayHeader = DateTimeUtil.getFormattedDateTime(CalendarFragment.DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN, calendarEvents.getToDate());
////                if (calendarEvents.getFromDate() != null && calendarEvents.getToDate() != null && !(calendarEvents.getFromDate().equals(calendarEvents.getToDate()))) {
////                    List<String> formattedDatesBetween = getFormattedDatesBetween(CalendarFragment.DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN,
////                            DateTimeUtil.getConvertedLong(CalendarFragment.MONTH_FORMAT_FOR_THIS_SCREEN, selectedTimeMillis),
////                            CalendarFragment.MONTH_FORMAT_FOR_THIS_SCREEN,
////                            calendarEvents.getFromDate(), calendarEvents.getToDate());
////
////                    if (!Util.isNullOrEmptyList(formattedDatesBetween)) {
////                        LogUtils.LOGD(TAG, "formattedDatesBetween size " + formattedDatesBetween.size());
////                        for (String date :
////                                formattedDatesBetween) {
////                            if (CalendarFragment.asyncTask != null && !CalendarFragment.asyncTask.isCancelled()) {
////                                try {
////                                    CalendarEvents updatedCalendarEvent = new CalendarEvents();
////                                    ReflectionUtil.copy(updatedCalendarEvent, calendarEvents);
////                                    LogUtils.LOGD(TAG, "formattedDatesBetween date " + date + " ID " + updatedCalendarEvent.getAppointmentId());
////                                    String formattedHeaderNew = date;
////                                    updatedCalendarEvent.setFromDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(CalendarFragment.DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN, formattedHeaderNew));
////                                    updatedCalendarEvent.setToDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(CalendarFragment.DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN, formattedHeaderNew));
////
////                                    String formattedFromDate = DateTimeUtil.getFormattedDateTime(CalendarFragment.DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN, updatedCalendarEvent.getFromDate());
////                                    if (formattedFromDate.equals(formattedFromDayHeader)) {
////                                        updatedCalendarEvent.setIsStartDate(true);
////                                        calendarEvents.setIsStartDate(true);
////                                    } else
////                                        updatedCalendarEvent.setIsStartDate(false);
////                                    if (formattedToDayHeader.equals(formattedHeaderNew)) {
////                                        updatedCalendarEvent.setIsEndDate(true);
////                                        calendarEvents.setIsEndDate(true);
////                                    } else
////                                        updatedCalendarEvent.setIsEndDate(false);
////                                    if (!updatedCalendarEvent.getIsStartDate() && !updatedCalendarEvent.getIsEndDate())
////                                        updatedCalendarEvent.setIsAllDayEvent(true);
////                                    formCustomListDataHashMap(formattedHeaderNew, updatedCalendarEvent);
////                                } catch (Exception e) {
////                                    e.printStackTrace();
////                                }
////                            } else {
////                                break;
////                            }
////                        }
////
////                    }
////                }
//                // false :  if the selected month is June and fromdate is May, true : otherwise
//                if (DateTimeUtil.getFormattedDateTime(CalendarFragment.MONTH_FORMAT_FOR_THIS_SCREEN, selectedTimeMillis)
//                        .equals(DateTimeUtil.getFormattedDateTime(CalendarFragment.MONTH_FORMAT_FOR_THIS_SCREEN, calendarEvents.getFromDate())))
//                    formCustomListDataHashMap(formattedFromDayHeader, calendarEvents);
//            } else {
//                break;
//            }
//        }
//        LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading Complete");
//        String selectedFormattedDate = DateTimeUtil.getFormattedDateTime(CalendarFragment.DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN, selectedTimeMillis);
//        if (selectedFormattedDate != null
//                && (selectedFormattedDate.equals(DateTimeUtil.getCurrentFormattedDate(CalendarFragment.DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN)))
//                && customListDataHashMap.get(selectedFormattedDate) != null) {
//            CalendarFragment.isSelectedPositionSet = false;
//            CustomListData customListData = customListDataHashMap.get(selectedFormattedDate);
//            LinkedHashMap<String, Object> hashMap = customListData.getHeaderDataListHashMap();
//            LinkedHashMap<String, Object> hashMapUpdated = new LinkedHashMap<>();
//            if (!Util.isNullOrEmptyList(hashMap)) {
//                for (Object o :
//                        hashMap.values()) {
//                    if (o instanceof CalendarEvents) {
//                        try {
//                            CalendarEvents calendarEvents = new CalendarEvents();
//                            ReflectionUtil.copy(calendarEvents, (CalendarEvents) o);
//                            String formattedHeader = DateTimeUtil.getFormattedDateTime(CalendarFragment.DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN, calendarEvents.getFromDate());
//                            LogUtils.LOGD(TAG, "iSSelected date " + formattedHeader);
//                            calendarEvents.setIsSelected(true);
//                            hashMapUpdated.put(calendarEvents.getCustomUniqueId(), calendarEvents);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                customListData.setHeaderDataListHashMap(hashMapUpdated);
//                customListDataHashMap.put(selectedFormattedDate, customListData);
//            }
//        }
//        return new ArrayList<>(customListDataHashMap.values());
//    }

//    private static void formCustomListDataHashMap(String formattedHeader, CalendarEvents events) {
//        try {
//            CalendarEvents calendarEvents = new CalendarEvents();
//            ReflectionUtil.copy(calendarEvents, events);
//            CustomListData customListData = new CustomListData();
//            LinkedHashMap<String, Object> headersListHashMap = null;
//            if (customListDataHashMap.get(formattedHeader) != null) {
//                customListData = customListDataHashMap.get(formattedHeader);
//                headersListHashMap = customListData.getHeaderDataListHashMap();
//            }
//
//            if (headersListHashMap == null)
//                headersListHashMap = new LinkedHashMap<>();
//            headersListHashMap.put(calendarEvents.getCustomUniqueId(), (Object) calendarEvents);
//            customListData.setHeader(formattedHeader);
//            customListData.setHeaderDataListHashMap(headersListHashMap);
//            customListDataHashMap.put(formattedHeader, customListData);
//
////            sorting calendarItems inside hashmap
//            if (!Util.isNullOrEmptyList(headersListHashMap))
//                Collections.sort(Arrays.asList(headersListHashMap.values()), ComparatorUtil.calendarEventsFromDateComparator);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static List<String> getFormattedDatesBetween(String headerFormat, long selectedTimeMillis, String selectedTimeFormat, Long fromMilli, Long toMilli) {
//        List<String> dates = new ArrayList<>();
//        Date fromDate = new Date(fromMilli);
//        Date toDate = new Date(toMilli);
//        Date selectedDate = new Date(selectedTimeMillis);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(fromDate);
//        while (cal.getTime().before(toDate) || cal.getTime().equals(toDate)) {
//            Date calDatInSelectedDateFormat = getFormattedDate(selectedTimeFormat, cal.getTime().getTime());
//            if (calDatInSelectedDateFormat.equals(selectedDate)) {
//                dates.add(DateTimeUtil.getFormattedDateTime(headerFormat, cal.getTime().getTime()));
//            }
//            cal.add(Calendar.DATE, 1);
//        }
//        return dates;
//    }

//    private static Date getFormattedDate(String format, Long fromMilli) {
//        try {
//            return new Date(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(format, DateTimeUtil.getFormattedDateTime(format, fromMilli)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
