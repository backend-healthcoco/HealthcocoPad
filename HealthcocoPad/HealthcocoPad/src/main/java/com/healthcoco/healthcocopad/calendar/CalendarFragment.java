package com.healthcoco.healthcocopad.calendar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.calendar.weekview.DateTimeInterpreter;
import com.healthcoco.healthcocopad.calendar.weekview.MonthLoader;
import com.healthcoco.healthcocopad.calendar.weekview.WeekView;
import com.healthcoco.healthcocopad.calendar.weekview.WeekViewEvent;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.CalendarViewType;
import com.healthcoco.healthcocopad.fragments.PatientAppointmentDetailFragment;
import com.healthcoco.healthcocopad.fragments.QueueFragment;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.AppointmentDetailsPopupWindow;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.healthcoco.healthcocopad.enums.AppointmentStatusType.CONFIRM;
import static com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType.APPOINTMENTS_QUEUE_ADD_NEW;
import static com.healthcoco.healthcocopad.fragments.QueueFragment.TAG_CHANGED_DATE;
import static com.healthcoco.healthcocopad.fragments.QueueFragment.TAG_IS_FROM_CALENDAR;


public class CalendarFragment extends HealthCocoFragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.ScrollListener, LocalDoInBackgroundListenerOptimised, WeekView.EmptyViewLongPressListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_DAY = 3;
    public ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList = null;
    CalendarEvents calendarEvents;
    private int textSizeHeader = 16;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private ArrayList<CalendarEvents> calendarEventsList = new ArrayList<>();
    private HashMap<String, CalendarEvents> calendarEventsHashMap = new HashMap<>();
    private boolean isInitialLoading = true;
    private AppointmentStatusType appointmentStatusType = CONFIRM;
    private User user;
    private long selectedMonthDayYearInMillis;
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        mWeekView = (WeekView) view.findViewById(R.id.weekView);

        getWeekView().goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    @Override
    public void initListeners() {
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setScrollListener(this);
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);
    }


    private void fromHashMapAndRefresh(ArrayList<CalendarEvents> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (CalendarEvents calendarEvent :
                    responseList) {
                calendarEventsHashMap.put(calendarEvent.getUniqueId(), calendarEvent);
            }
        }
        notifyAdapter(new ArrayList<>(calendarEventsHashMap.values()));
    }

    private void notifyAdapter(ArrayList<CalendarEvents> calendarEventsArrayList) {
        events = new ArrayList<WeekViewEvent>();
        if (!Util.isNullOrEmptyList(calendarEventsArrayList)) {
            for (CalendarEvents event : calendarEventsArrayList) {
                this.events.add(toWeekViewEvent(event));
            }
            Collections.sort(events, ComparatorUtil.weekViewEventComparator);
        }
        getWeekView().notifyDatasetChanged();
    }

    private void getListFromLocal(boolean initialLoading) {
        if (initialLoading) {
            showLoadingOverlay(true);
        } else {
            showLoadingOverlay(false);
        }
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_CALENDAR_EVENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case GET_CALENDAR_EVENTS:
                    if (response.isDataFromLocal()) {
                        ArrayList<CalendarEvents> responseDataList = (ArrayList<CalendarEvents>) (ArrayList<?>) response.getDataList();
                        LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading onResponse");
                        if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading Complete");
                            isInitialLoading = false;
                            fromHashMapAndRefresh(responseDataList);
                            return;
                        }
                        fromHashMapAndRefresh(responseDataList);
                        return;
                    } else {
                        getListFromLocal(false);
                    }
                    break;
                case CHANGE_APPOINTMENT_STATUS:
                    mActivity.hideLoading();
                    if (response.getData() != null) {
                        boolean data = (boolean) response.getData();
                    }
                    break;
                default:
                    break;
            }
        }

        isInitialLoading = false;

        showLoadingOverlay(false);

    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                }
                return volleyResponseBean;
            case GET_CALENDAR_EVENTS:
                if (user != null)
                    volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).
                            getCalendarEventsListResponseMonthWise(WebServiceType.GET_CALENDAR_EVENTS, appointmentStatusType,
                                    clinicDoctorProfileList, user.getForeignHospitalId(),
                                    user.getForeignLocationId(), null, selectedMonthDayYearInMillis,
                                    null, null);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    public void reFreshCalendar(long selectedMonthDayYearInMillis, ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList) {
        this.selectedMonthDayYearInMillis = selectedMonthDayYearInMillis;
        this.clinicDoctorProfileList = clinicDoctorProfileList;
        getListFromLocal(true);
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        int x = (int) eventRect.centerX();
        int y = (int) eventRect.centerY();

        AppointmentDetailsPopupWindow doctorListPopupWindow = new AppointmentDetailsPopupWindow(mActivity, null, event.getCalendarEvent());
        doctorListPopupWindow.setOutsideTouchable(true);
        doctorListPopupWindow.setContentView(doctorListPopupWindow.getPopupView());
//        y = x + y;
        doctorListPopupWindow.showOptionsWindow(mWeekView, x, y);
//        Toast.makeText(getActivity(), "Clicked " + event.getCalendarEvent().getAppointmentId(), Toast.LENGTH_SHORT).show();
    }


    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return events;
    }

    @Override
    public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
        if ((newFirstVisibleDay != null) && (oldFirstVisibleDay != null)) {
            selectedMonthDayYearInMillis = newFirstVisibleDay.getTimeInMillis();

            Intent intent = new Intent();
            intent.setAction(QueueFragment.INTENT_CHANGE_CALENDAR_DATE);
            intent.putExtra(TAG_CHANGED_DATE, selectedMonthDayYearInMillis);
            intent.putExtra(TAG_IS_FROM_CALENDAR, true);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        }

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
            startTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            startTime.set(Calendar.MINUTE, Math.round(workingHours.getFromTime()));

            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(event.getToDate());
            endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            endTime.set(Calendar.MINUTE, Math.round(workingHours.getToTime()));

            weekViewEvent.setTime((DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getFromTime()))) + getString(R.string.text_dash_with_space) +
                    (DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getToTime()))));

            for (RegisteredDoctorProfile registeredDoctorProfile : clinicDoctorProfileList) {
                if (event.getDoctorId().equals(registeredDoctorProfile.getUserId())) {
                    weekViewEvent.setColor(Color.parseColor(Util.getColorCode(registeredDoctorProfile.getColorCode())));
                    weekViewEvent.setStrokeColor(Color.parseColor(registeredDoctorProfile.getColorCode()));
                    weekViewEvent.setTextColor(Util.getTextColor(registeredDoctorProfile.getColorCode()));
                }
            }


            weekViewEvent.setId(event.getUniqueId());
            weekViewEvent.setName(patientName);
            weekViewEvent.setStartTime(startTime);
            weekViewEvent.setEndTime(endTime);
            weekViewEvent.setCalendarEvent(event);
//            events.add(weekViewEvent);
        }
        return weekViewEvent;
    }


    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 12 ? (hour - 12) + ":00" + " PM" : (hour == 0 ? "12:00 AM" : hour + ":00" + " AM");
            }
        });
    }

    public void gotoDate(long selectedMonthDayYearInMillis, boolean isFromCalendar) {
        this.selectedMonthDayYearInMillis = selectedMonthDayYearInMillis;
        if (!isFromCalendar) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selectedMonthDayYearInMillis);
            getWeekView().goToDate(calendar);
        }
    }


    public void changeCalendarViewType(CalendarViewType calendarViewType) {
        switch (calendarViewType) {
            case ONE_DAY:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeHeader, getResources().getDisplayMetrics()));
                }
                break;
            case THREE_DAY:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(TYPE_WEEK_DAY);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeHeader, getResources().getDisplayMetrics()));
                }
                break;
        }
        gotoDate(selectedMonthDayYearInMillis, false);
        getWeekView().goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    public void bookWalkInAppointment(Calendar time) {
        BookAppointmentDialogFragment dialogFragment = new BookAppointmentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(HealthCocoConstants.TAG_SELECTED_DATE_TIME_IN_MILLIS, time.getTimeInMillis());
        bundle.putParcelable(BookAppointmentDialogFragment.TAG_FROM_SCREEN_TYPE, Parcels.wrap(APPOINTMENTS_QUEUE_ADD_NEW.ordinal()));
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(dialogFragment, PatientAppointmentDetailFragment.REQUEST_CODE_APPOINTMENTS_LIST);
        dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());

    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        bookWalkInAppointment(time);
    }
}