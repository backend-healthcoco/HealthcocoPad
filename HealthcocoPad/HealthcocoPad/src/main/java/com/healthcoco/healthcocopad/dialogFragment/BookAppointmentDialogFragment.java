package com.healthcoco.healthcocopad.dialogFragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.AppointmentSlotAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AppointmentRequestToSend;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.AppointmentSlot;
import com.healthcoco.healthcocopad.bean.server.AppointmentTimeSlotDetails;
import com.healthcoco.healthcocopad.bean.server.AvailableTimeSlots;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsNew;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.custom.CustomEditText;
import com.healthcoco.healthcocopad.custom.ExistingPatientAutoCompleteAdapter;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AppointmentSlotsType;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.CreatedByType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.CalendarFragment;
import com.healthcoco.healthcocopad.fragments.CommonOpenUpPatientDetailFragment;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.NotificationResponseDataFragment;
import com.healthcoco.healthcocopad.fragments.PatientAppointmentDetailFragment;
import com.healthcoco.healthcocopad.fragments.QueueFragment;
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.listeners.DrawableClickListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.HealthcocoPopupWindow;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class BookAppointmentDialogFragment extends HealthCocoDialogFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, AdapterView.OnItemClickListener, HealthcocoTextWatcherListener, AutoCompleteTextViewListener, PopupWindowListener, DrawableClickListener {
    public static final String INTENT_REFRESH_SELECTED_PATIENT = "com.healthcoco.REFRESH_SELECTED_PATIENT";
    public static final String TIME_SLOT_FORMAT_USED_IN_THIS_SCREEN = "hh:mm aaa";
    public static final String TIME_FORMAT_RECEIVED_FROM_SERVER = "H:mm";
    public static final int DEFAULT_TIME_INTERVAL = 15;
    public static final String TIME_FORMAT = "hh:mm aaa";
    public static final String TAG_FROM_SCREEN_TYPE = "isFromCalendarFragment";
    public static final String TAG_SELECTED_USER_DATA = "clinicDataObject";
    public static final String TAG_APPOINTMENT_ID = "appointmentId";
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    private static final int REQUEST_CODE_BOOK_APPOINTMENT = 101;
    public List<RegisteredDoctorProfile> registeredDoctorProfileList;
    private User user;
    private RegisteredPatientDetailsNew selectedPatient;
    private AppointmentTimeSlotDetails appointmentTimeSlotDetails;
    private TextView tvSelectedDate;
    private AppointmentSlotAdapter adapter;
    private Button btSelectPatient;
    private LinearLayout containerPatientProfileHeader;
    private LinearLayout layoutSelectTimeSlot;
    private BookAppointmentFromScreenType bookAppointmentFromScreenType;
    private LinkedHashMap<String, RegisteredDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();
    private HealthcocoPopupWindow doctorsListPopupWindow;
    private AvailableTimeSlots selectedTimeSlot;
    private String appointmentId;
    private Long selectedTime;
    private CalendarEvents selectedAppointment;
    private EditText editNote;
    private boolean receiversRegistered;
    private CheckBox cbEmailDoctor;
    private CheckBox cbEmailPatient;
    private CheckBox cbSmsDoctor;
    private CheckBox cbSmsPatient;
    private LinearLayout containerSelectPatient;
    private TextView tvSelectedTime;
    private TextView tvAppointmentSlotDuration;
    private Button btAddNewPatient;
    private CustomEditText editMobileNumber;
    private ImageButton btClearMobileNumber;
    private FrameLayout containerDetailsAddNewPatient;
    private ExistingPatientAutoCompleteAdapter existingPatientAutotvAdapter;
    private AutoCompleteTextView autotvPatientName;
    BroadcastReceiver refreshSelectedContactReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getAndRefreshSelectedPatient(false);
        }
    };
    BroadcastReceiver refreshPatientProfileReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getAndRefreshSelectedPatient(false);
        }
    };
    private LinearLayout loadingExistingPatientsList;
    private LinearLayout parentAddSelectPatient;
    private TextView tvClinicName;
    private LinearLayout layoutDoctorName;
    private TextView tvDoctorName;
    private DoctorClinicProfile doctorClinicProfile;
    //    private GridView gvTimeSlots;
//    private ImageButton btPreviousDate;
//    private ImageButton btNextDate;
    private TextView tvClinicClosed;
    private AppointmentSlot selectedAppointmentSlot;
    private boolean isMobileNumberOptional;
    private RadioGroup radioGroupGender;
    private TextView tvBirthDay;
    private EditText editAge;
    private LinearLayout containerAge;
    private LinearLayout containerGender;
    private EditText editPnum;
    private LinearLayout layoutPnum;
    private boolean isPnumChange = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_book_appointment, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {

            //receiver for filter refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_REFRESH_SELECTED_PATIENT);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshSelectedContactReceiver, filter);

            //receiver for refreshing global access records
            IntentFilter filter1 = new IntentFilter();
            filter1.addAction(CommonOpenUpPatientDetailFragment.INTENT_REFRESH_PATIENT_PROFILE);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshPatientProfileReceiver, filter1);

            receiversRegistered = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int ordinal = Parcels.unwrap(bundle.getParcelable(TAG_FROM_SCREEN_TYPE));
            appointmentId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
            selectedTime = bundle.getLong(HealthCocoConstants.TAG_SELECTED_DATE_TIME_IN_MILLIS);
            bookAppointmentFromScreenType = BookAppointmentFromScreenType.values()[ordinal];
        }
        if (bookAppointmentFromScreenType != null)
            init();
        setWidthHeight(0.80, 0.90);
        if (!Util.isNullOrZeroNumber(selectedTime)) {
            initData();
        }
        mActivity.showLoading(false);
        if (!Util.isNullOrBlank(appointmentId))
            initActionbarTitle(getResources().getString(R.string.edit_reschedule_appointment));
        else
            initActionbarTitle(getResources().getString(R.string.new_appointment));

        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        mActivity.initPopupWindows(tvAppointmentSlotDuration, PopupWindowType.APPOINTMENT_SLOT, PopupWindowType.APPOINTMENT_SLOT.getList(), this);
        initPatientProfile();
    }

    @Override
    public void initViews() {
        btSelectPatient = (Button) view.findViewById(R.id.bt_select_patient);
        containerPatientProfileHeader = (LinearLayout) view.findViewById(R.id.container_patient_profile_header);
        containerSelectPatient = (LinearLayout) view.findViewById(R.id.container_select_patient);
        editNote = (EditText) view.findViewById(R.id.edit_note);
        cbEmailDoctor = (CheckBox) view.findViewById(R.id.cb_email_doctor);
        cbEmailPatient = (CheckBox) view.findViewById(R.id.cb_email_patient);
        cbSmsDoctor = (CheckBox) view.findViewById(R.id.cb_sms_doctor);
        cbSmsPatient = (CheckBox) view.findViewById(R.id.cb_sms_patient);

        btAddNewPatient = (Button) view.findViewById(R.id.bt_add_new_patient);
        editMobileNumber = (CustomEditText) view.findViewById(R.id.edit_mobile_number);
        btClearMobileNumber = (ImageButton) view.findViewById(R.id.bt_clear_mobile_number);
        autotvPatientName = (AutoCompleteTextView) view.findViewById(R.id.autotv_patient_name);
        containerDetailsAddNewPatient = (FrameLayout) view.findViewById(R.id.container_details_add_new_patient);
        loadingExistingPatientsList = (LinearLayout) view.findViewById(R.id.loading_existing_patients_list);
        parentAddSelectPatient = (LinearLayout) view.findViewById(R.id.parent_add_select_patient);
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);
        layoutDoctorName = (LinearLayout) view.findViewById(R.id.layout_doctor_name);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);


        tvSelectedTime = (TextView) view.findViewById(R.id.tv_selected_time);
        tvAppointmentSlotDuration = (TextView) view.findViewById(R.id.tv_appointment_slot);
        tvSelectedDate = (TextView) view.findViewById(R.id.tv_selected_date);
        layoutSelectTimeSlot = (LinearLayout) view.findViewById(R.id.layout_select_time_slot);
        tvClinicClosed = (TextView) view.findViewById(R.id.tv_clinic_closed);
        radioGroupGender = (RadioGroup) view.findViewById(R.id.rg_gender_select);
        tvBirthDay = (TextView) view.findViewById(R.id.tv_birthday);
        editAge = (EditText) view.findViewById(R.id.edit_age);
        containerAge = (LinearLayout) view.findViewById(R.id.container_age);
        containerGender = (LinearLayout) view.findViewById(R.id.container_gender_age);
        editPnum = (EditText) view.findViewById(R.id.edit_pnum);
        layoutPnum = (LinearLayout) view.findViewById(R.id.layout_pnum);
        tvBirthDay.setOnClickListener(this);
        refreshSelectedDate(DateTimeUtil.getCurrentDateLong());
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        tvSelectedDate.setOnClickListener(this);
        tvSelectedDate.addTextChangedListener(new HealthcocoTextWatcher(tvSelectedDate, this));
        btSelectPatient.setOnClickListener(this);
        tvSelectedTime.setOnClickListener(this);
        editMobileNumber.setDrawableClickListener(this);
        // switch (bookAppointmentFromScreenType) {
        //     case CALENDAR_LIST_ADD_NEW:
        //         containerPatientProfileHeader.setOnClickListener(this);
        //         break;
        //     case APPOINTMENTS_QUEUE_ADD_NEW:
        //         containerPatientProfileHeader.setOnClickListener(this);
        //         break;
        //     default:
        //         parentAddSelectPatient.setVisibility(View.GONE);
        //         break;
        // }
        if (bookAppointmentFromScreenType == BookAppointmentFromScreenType.CALENDAR_LIST_ADD_NEW) {
            containerPatientProfileHeader.setOnClickListener(this);
        } else if (bookAppointmentFromScreenType == BookAppointmentFromScreenType.APPOINTMENTS_QUEUE_ADD_NEW) {
            containerPatientProfileHeader.setOnClickListener(this);
        } else {
            parentAddSelectPatient.setVisibility(View.GONE);
        }
        btAddNewPatient.setOnClickListener(this);
        btClearMobileNumber.setOnClickListener(this);
        editMobileNumber.addTextChangedListener(new HealthcocoTextWatcher(editMobileNumber, this));
        autotvPatientName.addTextChangedListener(new HealthcocoTextWatcher(autotvPatientName, this));
        editPnum.addTextChangedListener(new HealthcocoTextWatcher(editPnum, this));
    }

    @Override
    public void initData() {
//        tvClinicName.setText(doctorClinicProfile.getLocationName());

        if (!Util.isNullOrZeroNumber(selectedTime)) {
            tvSelectedTime.setText(DateTimeUtil.getFormttedTime(selectedTime));
            refreshSelectedDate(selectedTime);
        }
        if (selectedAppointment != null && selectedAppointment.getTime() != null && selectedAppointment.getTime().getFromTime() != null)
            tvSelectedTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(selectedAppointment.getTime().getFromTime())));
        if (selectedAppointment != null && (!Util.isNullOrBlank(selectedAppointment.getDoctorName()))) {
            tvDoctorName.setText(selectedAppointment.getDoctorName());
            user.setUniqueId(selectedAppointment.getDoctorId());
            tvDoctorName.setEnabled(false);
            tvDoctorName.setClickable(false);
        } else if (user != null)
            tvDoctorName.setText("Dr. " + user.getFirstName());
        if (doctorClinicProfile != null) {
            tvClinicName.setText(Util.getValidatedValue(doctorClinicProfile.getLocationName()));
            if (doctorClinicProfile.getAppointmentSlot() != null) {
                tvAppointmentSlotDuration.setTag(doctorClinicProfile.getAppointmentSlot().getTime());
                String formattedString = Math.round(doctorClinicProfile.getAppointmentSlot().getTime()) + " " + Util.getValidatedValue(doctorClinicProfile.getAppointmentSlot().getTimeUnit().getValueToDisplay());
                tvAppointmentSlotDuration.setText(formattedString);
            }
        }
        if (doctorClinicProfile.getPidHasDate())
            layoutPnum.setVisibility(View.GONE);
        else layoutPnum.setVisibility(View.VISIBLE);
    }


    private void refreshSelectedDate(long date) {
        tvSelectedTime.setTag(date);
        tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, date));
    }

    private void initPatientProfile() {
        if (selectedPatient == null) {
            showSelectedPatientHeader(false);
        } else {
            showSelectedPatientHeader(true);
        }
        if (containerPatientProfileHeader.getVisibility() == View.VISIBLE && selectedPatient != null) {
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selectedPatient.getUserId();
            initActionPatientDetailActionBarNew(PatientProfileScreenType.IN_EMR_HEADER, view, selectedPatient);
        }
    }

    private void showSelectedPatientHeader(boolean b) {
        if (b) {
            showDetailsAddNewPatient(false);
            containerPatientProfileHeader.setVisibility(View.VISIBLE);
            btSelectPatient.setVisibility(View.GONE);
        } else {
            selectedPatient = null;
//            clearActionPatientDetailActionBar(view);
            containerPatientProfileHeader.setVisibility(View.GONE);
            btSelectPatient.setVisibility(View.VISIBLE);
        }

    }

    private void openDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, calendar, textView);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                long selectedFromDateTimeMillis = getSelectedFromDateTime(year, monthOfYear, dayOfMonth);
                textView.setText(DateTimeUtil.getFormattedTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, year, monthOfYear, dayOfMonth, 0, 0, 0));
                tvSelectedTime.setTag(selectedFromDateTimeMillis);
                int msg = 0;
                if (!DateTimeUtil.selectedTimeIsGreaterThanTime(selectedFromDateTimeMillis, DateTimeUtil.getCurrentDateLong())) {
                    msg = R.string.time_to_should_be_greater_than_current_time;
                }

                if (msg == 0) {
                    LogUtils.LOGD(TAG, "Time lesser");
                    tvSelectedTime.setTag(selectedFromDateTimeMillis);
                } else {
                    Util.showToast(mActivity, msg);
                    LogUtils.LOGD(TAG, "Time greater");
                    tvSelectedTime.setText("");
                    openTimePickerDialog(DateTimeUtil.getCurrentDateLong().toString(), tvSelectedTime);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
        datePickerDialog.show();
    }

    private void openTimePickerDialog(final String selectedFromTime, final TextView tvToTime) {
        String defaultPickerTime = selectedFromTime;
        String textTime = Util.getValidatedValueOrNull(tvToTime);
        boolean isTextShown = false;
        if (!Util.isNullOrBlank(textTime)) {
            isTextShown = true;
            defaultPickerTime = textTime;
        }
        final Calendar calendar = DateTimeUtil.getCalendarInstanceFromFormattedTime(TIME_FORMAT, defaultPickerTime, isTextShown, DEFAULT_TIME_INTERVAL);

        final TimePickerDialog datePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                long selectedFromDateTimeMillis = getSelectedFromDateTime(hourOfDay, minute);
                int msg = 0;
                if (!DateTimeUtil.selectedTimeIsGreaterThanTime(selectedFromDateTimeMillis, DateTimeUtil.getCurrentDateLong(DATE_FORMAT_USED_IN_THIS_SCREEN + "" + TIME_FORMAT))) {
                    msg = R.string.time_to_should_be_greater_than_current_time;
                }

                if (msg == 0) {
                    LogUtils.LOGD(TAG, "Time lesser");
                    tvToTime.setText(DateTimeUtil.getFormattedDateTime(TIME_FORMAT, selectedFromDateTimeMillis));
                    tvSelectedTime.setTag(selectedFromDateTimeMillis);
                } else {
                    openTimePickerDialog(selectedFromTime, tvToTime);
                    Util.showToast(mActivity, msg);
                    LogUtils.LOGD(TAG, "Time greater");
                }
            }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        datePickerDialog.show();
    }

    private long getSelectedFromDateTime(int year, int month, int day) {
        long selectedDatePickerTime = DateTimeUtil.getLongFromFormattedDateTime(TIME_FORMAT, Util.getValidatedValueOrNull(tvSelectedTime));
        Calendar calendar1 = DateTimeUtil.getCalendarInstance();
        calendar1.setTimeInMillis(selectedDatePickerTime);
        calendar1.set(Calendar.YEAR, year);
        calendar1.set(Calendar.MONTH, month);
        calendar1.set(Calendar.DAY_OF_MONTH, day);
        return calendar1.getTimeInMillis();
    }

    private long getSelectedFromDateTime(int hourOfDay, int minute) {
        long selectedDatePickerTime = DateTimeUtil.getLongFromFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, Util.getValidatedValueOrNull(tvSelectedDate));
        Calendar calendar1 = DateTimeUtil.getCalendarInstance();
        calendar1.setTimeInMillis(selectedDatePickerTime);
        calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar1.set(Calendar.MINUTE, minute);
        return calendar1.getTimeInMillis();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            // switch (response.getWebServiceType()) {
            //     case FRAGMENT_INITIALISATION:
            //         ...
            //     case ADD_APPOINTMENT:
            //         ...
            //     case SEARCH_PATIENTS:
            //         ...
            //     case GET_CHECK_PNUM_EXIST:
            //         ...
            //     default:
            //         break;
            // }
            if (response.getWebServiceType() == WebServiceType.FRAGMENT_INITIALISATION) {
                if (user != null && !Util.isNullOrBlank(user.getUniqueId())) {
                    tvSelectedDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
                    initData();
                    initSelectedAppointment();
                    initPatientProfile();
                    if (!Util.isNullOrEmptyList(registeredDoctorProfileList))
                        fromHashMapAndRefresh(registeredDoctorProfileList);
                    return;
                }
            } else if (response.getWebServiceType() == WebServiceType.ADD_APPOINTMENT) {
                if (response.isValidData(response) && response.getData() instanceof CalendarEvents) {
                    CalendarEvents calendarEvents = (CalendarEvents) response.getData();
                    calendarEvents.setIsAddedOnSuccess(true);
                    if (bookAppointmentFromScreenType == BookAppointmentFromScreenType.CALENDAR_LIST_ADD_NEW
                            || bookAppointmentFromScreenType == BookAppointmentFromScreenType.CALENDAR_LIST_RESCHEDULE ||
                            bookAppointmentFromScreenType == BookAppointmentFromScreenType.APPOINTMENTS_QUEUE_ADD_NEW) {
                        LocalDataServiceImpl.getInstance(mApp).addCalendarEventsUpdated(calendarEvents);
                        Util.sendBroadcast(mApp, ContactsListFragment.INTENT_GET_CONTACT_LIST_SERVER);
                    } else
                        LocalDataServiceImpl.getInstance(mApp).addAppointment(calendarEvents);
                    Util.showToast(mActivity, R.string.appointment_created);
                    sendBroadcasts(calendarEvents.getAppointmentId());
                    getDialog().dismiss();
                }
            } else if (response.getWebServiceType() == WebServiceType.SEARCH_PATIENTS) {
                if (!Util.isNullOrEmptyList(response.getDataList())) {
                    ArrayList<AlreadyRegisteredPatientsResponse> list = (ArrayList<AlreadyRegisteredPatientsResponse>) (ArrayList<?>) response
                            .getDataList();
                    LocalDataServiceImpl.getInstance(mApp).addAlreadyRegisteredPatients(list);
                    showSearchedPatientsListPopUp(list);
                } else {
                    autotvPatientName.setAdapter(null);
                }
                loadingExistingPatientsList.setVisibility(View.GONE);
                return;
            } else if (response.getWebServiceType() == WebServiceType.GET_CHECK_PNUM_EXIST) {
                if (response.getData() instanceof Boolean) {
                    boolean isDataSuccess = (boolean) response.getData();
                    if (isDataSuccess) {
                        showExistedPnumAlert();
                    } else bookAppointment();
                }
            }
        }
        mActivity.hideLoading();
    }

    private void bookAppointment() {
        if (user != null) {
            Util.checkNetworkStatus(mActivity);
            if (HealthCocoConstants.isNetworkOnline) {
                validateData();
            } else
                Util.showToast(mActivity, R.string.user_offline);
        }
    }

    private void showExistedPnumAlert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.check_pnum_exist);
        alertBuilder.setMessage(R.string.validation_text_for_pnum);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    private void showSearchedPatientsListPopUp(List<AlreadyRegisteredPatientsResponse> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
                existingPatientAutotvAdapter = new ExistingPatientAutoCompleteAdapter(mActivity, R.layout.autocomplete_existing_patient,
                        list);
                autotvPatientName.setThreshold(0);
                autotvPatientName.setAdapter(existingPatientAutotvAdapter);
                autotvPatientName.setDropDownAnchor(autotvPatientName.getId());
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                autotvPatientName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        autotvPatientName.setText("");
                        AlreadyRegisteredPatientsResponse existingPatient = existingPatientAutotvAdapter.getSelectedObject(position);
                        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = existingPatient.getUserId();
                        if (existingPatient != null) {
                            if (existingPatient.getIsPartOfClinic()) {
                                getAndRefreshSelectedPatient(true);
                            } else {
                                LogUtils.LOGD(TAG, "SELECTED_PATIENTS_USER_ID selected " + HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                                showSelectedPatientHeader(false);
                                autotvPatientName.setText(existingPatient.getFirstName());
                                autotvPatientName.setVisibility(View.VISIBLE);
                                if (!Util.isNullOrBlank(existingPatient.getGender())) {
                                    RadioButton radioButton = (RadioButton) radioGroupGender.findViewWithTag(Util.getValidatedValue(existingPatient.getGender()).toUpperCase());
                                    if (radioButton != null)
                                        radioButton.setChecked(true);
                                }
                                if (!Util.isNullOrBlank(Util.getDOB(existingPatient.getDob()))) {
                                    containerAge.setVisibility(View.GONE);
                                    tvBirthDay.setText(Util.getDOB(existingPatient.getDob()));
                                } else containerAge.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
//                requestFocusOnEditText(autotvPatientName);
                autotvPatientName.showDropDown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSelectedAppointment() {
        if (selectedAppointment != null) {
            tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, selectedAppointment.getFromDate()));
            editNote.setText(Util.getValidatedValue(selectedAppointment.getExplanation()));
        }
    }

    private void sendBroadcasts(String aptId) {
        Intent intent = new Intent();
        // switch (bookAppointmentFromScreenType) {
        //     case NOTIFICATION_APPOINTMENTS_LIST_RESCHEDULE:
        //         intent.setAction(NotificationResponseDataFragment.INTENT_GET_NOTIFICATION_APPOINTMENT_LIST_LOCAL);
        //         break;
        //     case APPOINTMENTS_QUEUE_RESCHEDULE:
        //         intent.setAction(QueueFragment.INTENT_GET_APPOINTMENT_LIST_LOCAL);
        //         break;
        //     default:
        //         intent.setAction(PatientAppointmentDetailFragment.INTENT_GET_APPOINTMENT_LIST_LOCAL);
        //         break;
        // }
        if (bookAppointmentFromScreenType == BookAppointmentFromScreenType.NOTIFICATION_APPOINTMENTS_LIST_RESCHEDULE) {
            intent.setAction(NotificationResponseDataFragment.INTENT_GET_NOTIFICATION_APPOINTMENT_LIST_LOCAL);
        } else if (bookAppointmentFromScreenType == BookAppointmentFromScreenType.APPOINTMENTS_QUEUE_RESCHEDULE) {
            intent.setAction(QueueFragment.INTENT_GET_APPOINTMENT_LIST_LOCAL);
        } else {
            intent.setAction(PatientAppointmentDetailFragment.INTENT_GET_APPOINTMENT_LIST_LOCAL);
        }
        intent.putExtra(TAG_APPOINTMENT_ID, aptId);
        LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        sendBroadcastToCalendarFragment();
    }

    private void sendBroadcastToCalendarFragment() {
        try {
            Intent intent = new Intent(CalendarFragment.INTENT_GET_CALENDAR_EVENTS_LIST_LOCAL);
            intent.putExtra(HealthCocoConstants.TAG_SELECTED_DATE_TIME_IN_MILLIS,
                    DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, Util.getValidatedValueOrNull(tvSelectedDate)));
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        // switch (response.getLocalBackgroundTaskType()) {
        //     case GET_FRAGMENT_INITIALISATION_DATA:
        //         ...
        //         break;
        // }
        if (response.getLocalBackgroundTaskType() == LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA) {
            volleyResponseBean = new VolleyResponseBean();
            volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
            LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
            if (doctor != null && doctor.getUser() != null) {
                user = doctor.getUser();
                if (!Util.isNullOrBlank(appointmentId)) {
                    selectedAppointment = LocalDataServiceImpl.getInstance(mApp).getAppointment(appointmentId);
                    if (!Util.isNullOrBlank(selectedAppointment.getPatientId()))
                        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selectedAppointment.getPatientId();
                }
                if (bookAppointmentFromScreenType != BookAppointmentFromScreenType.APPOINTMENTS_QUEUE_ADD_NEW)
                    selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatientNew(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                registeredDoctorProfileList = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorDetails(user.getForeignLocationId());
                doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
                if (doctorClinicProfile != null && doctorClinicProfile.getMobileNumberOptional() != null)
                    isMobileNumberOptional = doctorClinicProfile.getMobileNumberOptional();
            }
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onClick(View v) {
        // switch (v.getId()) {
        //     case R.id.tv_selected_date:
        //         ...
        //     case R.id.tv_selected_time:
        //         ...
        //     case R.id.container_patient_profile_header:
        //     case R.id.bt_select_patient:
        //         ...
        //     case R.id.bt_save:
        //         ...
        //     case R.id.bt_clear_mobile_number:
        //         ...
        //     case R.id.bt_add_new_patient:
        //         ...
        //     case R.id.bt_register_new_patient:
        //         ...
        //     case R.id.tv_birthday:
        //         ...
        // }
        int id = v.getId();
        if (id == R.id.tv_selected_date) {
            openDatePickerDialog((TextView) v);
        } else if (id == R.id.tv_selected_time) {
            openTimePickerDialog(null, (TextView) v);
        } else if (id == R.id.container_patient_profile_header || id == R.id.bt_select_patient) {
            openContactsListScreen();
        } else if (id == R.id.bt_save) {
            if (isPnumChange)
                checkForPnumExist();
            else if (user != null) {
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    validateData();
                } else
                    Util.showToast(mActivity, R.string.user_offline);
            }
        } else if (id == R.id.bt_clear_mobile_number) {
            editMobileNumber.setText("");
        } else if (id == R.id.bt_add_new_patient) {
            showDetailsAddNewPatient(true);
        } else if (id == R.id.bt_register_new_patient) {
            autotvPatientName.setVisibility(View.VISIBLE);
            autotvPatientName.setEnabled(true);
            requestFocusOnEditText(autotvPatientName);
        } else if (id == R.id.tv_birthday) {
            openBirthDatePickerDialog();
        }
    }

    private void checkForPnumExist() {
        clearPreviousAlerts();
        String msg = null;
        EditText selectedEditText = null;
        String pnum = Util.getValidatedValueOrNull(editPnum);
        if (Util.isNullOrBlank(pnum)) {
            selectedEditText = editPnum;
            msg = getResources().getString(R.string.please_enter_pnum);
        }
        if (Util.isNullOrBlank(msg)) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).checkForPnumExist(Boolean.class, user.getForeignLocationId(), user.getForeignHospitalId(),
                    pnum, this, this);
        } else {
            Util.showErrorOnEditText(selectedEditText);
            Util.showAlert(mActivity, msg);
        }
    }

    private void openBirthDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(calendar, Util.getValidatedValueOrNull(tvBirthDay));
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvBirthDay.setText(DateTimeUtil.getBirthDateFormat(dayOfMonth, monthOfYear + 1, year));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(DateTimeUtil.getMaxDate(new Date().getTime()));
        datePickerDialog.show();
    }

    private void showDetailsAddNewPatient(boolean b) {
        if (b) {
            containerDetailsAddNewPatient.setVisibility(View.VISIBLE);
            btAddNewPatient.setVisibility(View.GONE);
            requestFocusOnEditText(editMobileNumber);
        } else {
            containerDetailsAddNewPatient.setVisibility(View.GONE);
            btAddNewPatient.setVisibility(View.VISIBLE);
            editMobileNumber.setText("");
            autotvPatientName.setText("");
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = null;
        }
    }

    private void openContactsListScreen() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.CONTACTS_LIST.ordinal());
        intent.putExtra(ContactsListFragment.TAG_IS_IN_HOME_ACTIVITY, false);
        intent.putExtra(TAG_SELECTED_USER_DATA, Parcels.wrap(user));
        mActivity.startActivityForResult(intent, REQUEST_CODE_BOOK_APPOINTMENT);
//        openCommonOpenUpActivity(CommonOpenUpFragmentType.CONTACTS_LIST, ContactsListFragment.TAG_IS_IN_HOME_ACTIVITY, false, REQUEST_CODE_BOOK_APPOINTMENT);
    }

    private void validateData() {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String selectedTime = String.valueOf(tvSelectedTime.getText()).trim();
        String selectedSlotDuration = String.valueOf(tvAppointmentSlotDuration.getText()).trim();
        String selectedDate = String.valueOf(tvSelectedDate.getText()).trim();
        String mobileNumber = String.valueOf(editMobileNumber.getText()).trim();
        String patientName = String.valueOf(autotvPatientName.getText()).trim();

        if (selectedPatient == null && Util.isNullOrBlank(mobileNumber) && Util.isNullOrBlank(patientName)) {
            msg = getResources().getString(R.string.please_select_patient_or_add_new);
        }
        if (selectedPatient == null && !isMobileNumberOptional && Util.isNullOrBlank(mobileNumber)) {
            msg = getResources().getString(R.string.enter_patient_mobile_number);
        } else if (!Util.isNullOrBlank(mobileNumber) && !Util.isValidMobileNo(mobileNumber)) {
            msg = getResources().getString(R.string.please_enter_valid_mobile_no_or_select_existing_patient);
        } else if (!Util.isNullOrBlank(mobileNumber) && Util.isNullOrBlank(patientName)) {
            msg = getResources().getString(R.string.please_enter_patient_name_or_select_existing_patient);
        } else if (Util.isNullOrBlank(selectedDate)) {
            msg = getResources().getString(R.string.please_select_date);
            errorViewList.add(tvSelectedDate);
        } else if (Util.isNullOrBlank(selectedTime)) {
            msg = getResources().getString(R.string.please_select_time_slot);
            errorViewList.add(tvSelectedTime);
        } else if (Util.isNullOrBlank(selectedSlotDuration)) {
            msg = getResources().getString(R.string.please_select_appointment_slot_duration);
            errorViewList.add(tvSelectedTime);
        }
        if (Util.isNullOrBlank(msg))
            addAppointment(selectedDate);
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addAppointment(String selecetdDate) {
        mActivity.hideLoading();
        mActivity.showLoading(false);
        Float selectedFromTimeInMinutes = 0f;
        if (tvSelectedTime.getTag() != null)
            selectedFromTimeInMinutes = DateTimeUtil.getMinutesFromFormattedTime((long) tvSelectedTime.getTag());

        AppointmentRequestToSend appointment = new AppointmentRequestToSend();
        appointment.setCreatedBy(CreatedByType.DOCTOR);
        if (!Util.isNullOrBlank(appointmentId)) {
            appointment.setDoctorId(selectedAppointment.getDoctorId());
            appointment.setHospitalId(selectedAppointment.getHospitalId());
            appointment.setLocationId(selectedAppointment.getLocationId());
        } else {
            appointment.setDoctorId(user.getUniqueId());
            appointment.setHospitalId(user.getForeignHospitalId());
            appointment.setLocationId(user.getForeignLocationId());
        }
        appointment.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        appointment.setLocalPatientName(Util.getValidatedValueOrNull(autotvPatientName));
        appointment.setMobileNumber(Util.getValidatedValueOrNull(editMobileNumber));

        appointment.setNotifyDoctorBySms(cbSmsDoctor.isChecked());
        appointment.setNotifyDoctorByEmail(cbEmailDoctor.isChecked());

        appointment.setNotifyPatientBySms(cbSmsPatient.isChecked());
        appointment.setNotifyPatientByEmail(cbEmailPatient.isChecked());

        appointment.setExplanation(Util.getValidatedValueOrNull(editNote));
        appointment.setFromDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, selecetdDate));
        appointment.setToDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, selecetdDate));
        appointment.setTime(new WorkingHours(selectedFromTimeInMinutes, selectedFromTimeInMinutes + (float) tvAppointmentSlotDuration.getTag()));
        appointment.setDob(DateTimeUtil.getDob(String.valueOf(tvBirthDay.getText())));
        if (!doctorClinicProfile.getPidHasDate())
            appointment.setPnum(Util.getValidatedValueOrNull(editPnum));
        String age = Util.getValidatedValueOrNull(editAge);
        if (!Util.isNullOrBlank(age))
            appointment.setAge(Integer.parseInt(age));

        //setting gender
        View checkedRadioButton = view.findViewById(radioGroupGender.getCheckedRadioButtonId());
        if (checkedRadioButton != null)
            appointment.setGender(String.valueOf(checkedRadioButton.getTag()));


        if (!Util.isNullOrBlank(appointmentId))
            appointment.setAppointmentId(appointmentId);
        if (doctorClinicProfile.isVaccinationModuleOn())
            appointment.setChild(true);

        switch (bookAppointmentFromScreenType) {
            case CALENDAR_LIST_RESCHEDULE:
            case APPOINTMENTS_LIST_RESCHEDULE:
            case NOTIFICATION_APPOINTMENTS_LIST_RESCHEDULE:
            case APPOINTMENTS_QUEUE_RESCHEDULE:
                appointment.setState(AppointmentStatusType.RESCHEDULE);
                break;
            default:
                appointment.setState(AppointmentStatusType.NEW);
                break;
        }
        WebDataServiceImpl.getInstance(mApp).addAppointment(CalendarEvents.class, appointment, this, this);
    }

    private void clearPreviousAlerts() {
        tvSelectedDate.setActivated(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AvailableTimeSlots availableTimeSlots = (AvailableTimeSlots) adapter.getItem(position);
        if (!availableTimeSlots.getIsAvailable()) {
            selectedTimeSlot = null;
            Util.showToast(mActivity, R.string.selected_time_slot_is_not_available);
        } else {
            selectedTimeSlot = availableTimeSlots;
            adapter.setSelected(position);
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BOOK_APPOINTMENT) {
//            if (resultCode == HealthCocoConstants.RESULT_CODE_CONTACTS_LIST) {
//            }
        } else if (requestCode == HealthCocoConstants.PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                ContentResolver cr = mActivity.getContentResolver();
                Cursor cursor = cr.query(contactData, null, null, null, null);
                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    @SuppressLint("Range") Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String phone = null;
                    if (hasPhone > 0) {
                        Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (cp != null && cp.moveToFirst()) {
                            phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            cp.close();
                        }                    // Todo something when contact number selected
                        editMobileNumber.setText(Util.getValidMobileNumber(phone));
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        sendBroadcasts(null);
        mActivity.hideLoading();
        if (bookAppointmentFromScreenType == BookAppointmentFromScreenType.CALENDAR_LIST_ADD_NEW
                || bookAppointmentFromScreenType == BookAppointmentFromScreenType.CALENDAR_LIST_RESCHEDULE
                || bookAppointmentFromScreenType == BookAppointmentFromScreenType.APPOINTMENTS_QUEUE_RESCHEDULE)
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = null;
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshSelectedContactReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshPatientProfileReceiver);
    }

    @Override
    public void onEmptyListFound(AutoCompleteTextView autoCompleteTextView) {
        switch (autoCompleteTextView.getId()) {
//            case R.id.autotv_selected_time_slot:
//                Util.showToast(mActivity, R.string.no_slots_available);
//                break;
        }
    }

    @Override
    public void scrollToPosition(int position) {

    }

    private void getAndRefreshSelectedPatient(boolean isExistingPatientPartOfClinic) {
        LogUtils.LOGD(TAG, "SELECTED_PATIENTS_USER_ID selected " + HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatientNew(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (isExistingPatientPartOfClinic && selectedPatient == null) {
            mActivity.showLoading(false);
            Util.sendBroadcastParcelableData(mApp, ContactsListFragment.INTENT_REFRESH_CONTACTS_BY_USERID_LIST_FROM_SERVER, HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
            return;
        } else {
            selectedPatient = LocalDataServiceImpl.getInstance(mApp).getSelectedPatientNew(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
            mActivity.hideLoading();
        }
        editMobileNumber.setText("");
        autotvPatientName.setText("");
        initPatientProfile();
    }

    @Override
    public void afterTextChange(View v, String s) {
        // switch (v.getId()) {
        //     case R.id.edit_pnum:
        //         ...
        //     case R.id.autotv_patient_name:
        //         ...
        //     case R.id.edit_mobile_number:
        //         ...
        // }
        int id = v.getId();
        if (id == R.id.edit_pnum) {
            if (selectedPatient == null)
                isPnumChange = true;
            else if (selectedPatient != null)
                if (!s.equals(selectedPatient.getPnum())) {
                    isPnumChange = true;
                }
        } else if (id == R.id.autotv_patient_name) {
            if (!Util.isNullOrBlank(s) && containerPatientProfileHeader.getVisibility() == View.VISIBLE)
                showSelectedPatientHeader(false);
        } else if (id == R.id.edit_mobile_number) {
            LogUtils.LOGD(TAG, "Edit Mobile Number");
            autotvPatientName.setText("");
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = null;
            if (Util.isValidMobileNo(s)) {
                mActivity.hideSoftKeyboard();
                getExistingPatientsList(s);
            }
        }
    }

    private void getExistingPatientsList(String mobileNo) {
        loadingExistingPatientsList.setVisibility(View.VISIBLE);
        WebDataServiceImpl.getInstance(mApp).getAlreadyRegisteredPatients(AlreadyRegisteredPatientsResponse.class, mobileNo, user, this, this);
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        // switch (popupWindowType) {
        //     case APPOINTMENT_SLOT:
        //         ...
        //     case DOCTOR_LIST:
        //         ...
        // }
        if (popupWindowType == PopupWindowType.APPOINTMENT_SLOT) {
            if (object != null && object instanceof AppointmentSlotsType) {
                AppointmentSlotsType appointmentSlotsType = (AppointmentSlotsType) object;
                String formattedString = Math.round(appointmentSlotsType.getTime()) + " " + Util.getValidatedValue(appointmentSlotsType.getUnits().getValueToDisplay());
                tvAppointmentSlotDuration.setText(formattedString);
                tvAppointmentSlotDuration.setTag(appointmentSlotsType.getTime());
            }
        } else if (popupWindowType == PopupWindowType.DOCTOR_LIST) {
            if (object instanceof RegisteredDoctorProfile) {
                RegisteredDoctorProfile doctorProfile = (RegisteredDoctorProfile) object;
                tvDoctorName.setText(doctorProfile.getFirstNameWithTitle());
                user.setUniqueId(doctorProfile.getUserId());
            }
        }
    }

    @Override
    public void onEmptyListFound() {

    }


    private void fromHashMapAndRefresh(List<RegisteredDoctorProfile> responseList) {
        if (responseList.size() > 1) {
            tvDoctorName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_drug, 0);
            tvDoctorName.setEnabled(true);

            if (!Util.isNullOrEmptyList(responseList)) {
                for (RegisteredDoctorProfile clinicDoctorProfile :
                        responseList) {
                    clinicDoctorListHashMap.put(clinicDoctorProfile.getUserId(), clinicDoctorProfile);
                }
            }
//        notifyAdapter(new ArrayList<ClinicDoctorProfile>(clinicDoctorListHashMap.values()));
            if (doctorsListPopupWindow != null)
                doctorsListPopupWindow.notifyAdapter(new ArrayList<Object>(clinicDoctorListHashMap.values()));
            else {

                if (selectedAppointment == null)
                    mActivity.initPopupWindows(tvDoctorName, PopupWindowType.DOCTOR_LIST, new ArrayList<Object>(clinicDoctorListHashMap.values()), this);
            }
        } else {
            tvDoctorName.setText("Dr. " + user.getFirstName());
        }
    }


    @Override
    public void onClick(DrawablePosition target) {
        // switch (target) {
        //     case RIGHT:
        //         requestPermission();
        //         break;
        // }
        if (target == DrawableClickListener.DrawablePosition.RIGHT) {
            requestPermission();
        }
    }

    public void requestPermission() {
        requestAppPermissions(new
                String[]{Manifest.permission.READ_CONTACTS}, HealthCocoActivity.REQUEST_PERMISSIONS);
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int requestCode) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(getContext(), permission);
        }
        requestPermissions(requestedPermissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, HealthCocoConstants.PICK_CONTACT);
        }
    }

}
