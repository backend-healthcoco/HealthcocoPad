package com.healthcoco.healthcocopad.calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.calendar.weekview.WeekViewEvent;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.CheckedOutQueueFragment;
import com.healthcoco.healthcocopad.fragments.EngagedQueueFragment;
import com.healthcoco.healthcocopad.fragments.PatientAppointmentDetailFragment;
import com.healthcoco.healthcocopad.fragments.ScheduledQueueFragment;
import com.healthcoco.healthcocopad.fragments.WaitingQueueFragment;
import com.healthcoco.healthcocopad.listeners.DoctorListPopupWindowListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.HealthcocoPopupWindow;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.enums.AppointmentStatusType.ALL;
import static com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType.APPOINTMENTS_QUEUE_ADD_NEW;

/**
 * Created by Prashant on 24/04/2018.
 */

public class CalendarFragment extends BaseFragment implements LocalDoInBackgroundListenerOptimised {

    public static final String DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public static final int MAX_SIZE = 10;
    public static final int MAX_NUMBER_OF_EVENTS = 30;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList;
    public List<RegisteredDoctorProfile> registeredDoctorProfileList;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private User user;
    private User loggedInUser;
    private long selectedMonthDayYearInMillis;
    private long curentMonthDayYearInMillis;
    private ArrayList<CalendarEvents> calenderEventList = null;
    private AppointmentStatusType appointmentStatusType = ALL;
    private LinkedHashMap<String, RegisteredDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();
    private HealthcocoPopupWindow doctorsListPopupWindow;
    private boolean isSingleDoctor = false;
    private boolean isInitialLoading = true;
    private boolean receiversRegistered;
    private FloatingActionButton floatingActionButton;
    private HorizontalScrollView horizontalScrollView;
    private View.OnClickListener clickListener;
    private ScheduledQueueFragment scheduledQueueFragment;
    private WaitingQueueFragment waitingQueueFragment;
    private EngagedQueueFragment engagedQueueFragment;
    private CheckedOutQueueFragment checkedOutQueueFragment;

    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initListeners() {
    }


    public void getCalendarEventsList(boolean showLoading) {
        if (showLoading)
            mActivity.showLoading(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.CALENDAR_EVENTS, selectedMonthDayYearInMillis);
        WebDataServiceImpl.getInstance(mApp).getCalendarEvents(CalendarEvents.class, registeredDoctorProfileList, user.getForeignLocationId(), user.getForeignHospitalId(), selectedMonthDayYearInMillis, 0, this, this);
    }


    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        super.onErrorResponse(volleyResponseBean, errorMessage);
//        refreshData();
        isInitialLoading = false;
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        Calendar calendar = DateTimeUtil.getCalendarInstance();
//                        onDateSetonDateSet(null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        selectedMonthDayYearInMillis = calendar.getTimeInMillis();
                        curentMonthDayYearInMillis = selectedMonthDayYearInMillis;
//                        tvSelectedDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
                        getCalendarEventsList(true);
                        return;
                    }
                    break;
                case GET_CALENDAR_EVENTS:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        refreshData((ArrayList<CalendarEvents>) (ArrayList<?>) response.getDataList());
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_CALENDAR_EVENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    } else {
//                        refreshData();
                        isInitialLoading = false;
                        mActivity.hideLoading();
                    }
                    break;
                case ADD_APPOINTMENT:
//                    refreshData();
                    isInitialLoading = false;
                    mActivity.hideLoading();
                    break;
                case GET_REGISTER_DOCTOR:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        registeredDoctorProfileList = (ArrayList<RegisteredDoctorProfile>) (ArrayList) response.getDataList();
//                        initSelectedDoctorClinicData();
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_REGISTER_DOCTOR, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        mActivity.hideLoading();
                    }
                    if (!response.isUserOnline())
                        onNetworkUnavailable(response.getWebServiceType());
                    break;
                default:
                    break;
            }
        }
    }

    private void refreshDoctorsList() {
        mActivity.showLoading(true);
        WebDataServiceImpl.getInstance(mApp).getRegisterDoctor(RegisteredDoctorProfile.class, user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    try {
                        loggedInUser = new User();
                        ReflectionUtil.copy(loggedInUser, doctor.getUser());
                    } catch (Exception e) {
                        e.printStackTrace();
                        loggedInUser = doctor.getUser();
                    }
                    user = doctor.getUser();
                    registeredDoctorProfileList = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorDetails(user.getForeignLocationId());
                }
                return volleyResponseBean;
            case ADD_CALENDAR_EVENTS:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.ADD_APPOINTMENT);

                LocalDataServiceImpl.getInstance(mApp).addCalendarEventsList(
                        (ArrayList<CalendarEvents>) (ArrayList<?>) response
                                .getDataList());
                return volleyResponseBean;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }


    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {
    }

    private void refreshData(ArrayList<CalendarEvents> dataList) {
        this.events.clear();
        for (CalendarEvents event : dataList) {
            this.events.add(toWeekViewEvent(event));
        }
        getWeekView().notifyDatasetChanged();
        getWeekView().goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return events;
    }

    public WeekViewEvent toWeekViewEvent(CalendarEvents event) {

        WeekViewEvent weekViewEvent = new WeekViewEvent();
        String patientName = "";

        if (event.getPatient() != null) {
            PatientCard patient = event.getPatient();
            patientName = Util.getValidatedValue(patient.getLocalPatientName());

            if (!Util.isNullOrBlank(patientName)) {
                weekViewEvent.setName(patientName);
            } else {
                if (!Util.isNullOrBlank(event.getPatient().getFirstName()))
                    patientName = event.getPatient().getFirstName();
            }
        }
        if (event.getTime() != null && event.getTime().getFromTime() != null && event.getTime().getToTime() != null) {
            WorkingHours workingHours = event.getTime();

            Calendar startTime = Calendar.getInstance();
            startTime.setTimeInMillis(event.getFromDate());
//            startTime.set(0, 0, 0, 0, Math.round(workingHours.getFromTime()), 00);
            startTime.set(Calendar.MINUTE, Math.round(workingHours.getFromTime()));


            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(event.getToDate());
//            endTime.set(0, 0, 0, 0, Math.round(workingHours.getToTime()), 00);
            endTime.set(Calendar.MINUTE, Math.round(workingHours.getToTime()));


            weekViewEvent.setId(event.getUniqueId());
            weekViewEvent.setName(patientName);
            weekViewEvent.setStartTime(startTime);
            weekViewEvent.setEndTime(endTime);
            weekViewEvent.setColor(Color.parseColor(Util.getColorCode(event.getPatient().getColorCode())));
            weekViewEvent.setStrokeColor(Color.parseColor(event.getPatient().getColorCode()));
            events.add(weekViewEvent);
        }


      /*  startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);

        // Initialize start and end time.
        Calendar now = Calendar.getInstance();
        Calendar startTime = (Calendar) now.clone();
        startTime.setTimeInMillis(start.getTime());
        startTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
        startTime.set(Calendar.MONTH, now.get(Calendar.MONTH));
        startTime.set(Calendar.DAY_OF_MONTH, getDayOfMonth());
        Calendar endTime = (Calendar) startTime.clone();
        endTime.setTimeInMillis(end.getTime());
        endTime.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
        endTime.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
        endTime.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH));*/

        // Create an week view event.
       /* WeekViewEvent weekViewEvent = new WeekViewEvent();
        weekViewEvent.setName(getName());
        weekViewEvent.setStartTime(startTime);
        weekViewEvent.setEndTime(endTime);
        weekViewEvent.setColor(Color.parseColor(getColor()));
*/
        return weekViewEvent;
    }

}
