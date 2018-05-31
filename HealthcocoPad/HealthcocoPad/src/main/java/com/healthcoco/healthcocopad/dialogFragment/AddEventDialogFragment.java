package com.healthcoco.healthcocopad.dialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.EventRequest;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.Events;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.custom.ExistingPatientAutoCompleteAdapter;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AddEventsFromScreenType;
import com.healthcoco.healthcocopad.enums.AppointmentSlotsType;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.CommonOpenUpPatientDetailFragment;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.EventFragment;
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
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
public class AddEventDialogFragment extends HealthCocoDialogFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, HealthcocoTextWatcherListener, AutoCompleteTextViewListener, PopupWindowListener {
    public static final String INTENT_REFRESH_SELECTED_PATIENT = "com.healthcoco.REFRESH_SELECTED_PATIENT";
    public static final int DEFAULT_TIME_INTERVAL = 15;
    public static final String TIME_FORMAT = "hh:mm aaa";
    public static final String TAG_FROM_SCREEN_TYPE = "isFromCalendarFragment";
    public static final String TAG_SELECTED_USER_DATA = "clinicDataObject";
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    private static final int REQUEST_CODE_BOOK_APPOINTMENT = 101;
    public List<RegisteredDoctorProfile> registeredDoctorProfileList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private LinkedHashMap<String, RegisteredDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();
    private HealthcocoPopupWindow doctorsListPopupWindow;
    private TextView tvSelectedDate;
    private Button btSelectPatient;
    private LinearLayout containerPatientProfileHeader;
    private AddEventsFromScreenType addEventsFromScreenType;
    private String appointmentId;
    private Long selectedTime;
    private Events selectedEvent;
    private EditText editNote;
    private EditText editTitle;
    private boolean receiversRegistered;
    private CheckBox cbEmailDoctor;
    private CheckBox cbEmailPatient;
    private LinearLayout containerSelectPatient;
    private TextView tvSelectedTime;
    private TextView tvAppointmentSlotDuration;
    private Button btAddNewPatient;
    private EditText editMobileNumber;
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
    private LinearLayout layoutDoctorName;
    private TextView tvClinicName;
    private TextView tvDoctorName;
    private DoctorClinicProfile doctorClinicProfile;
    private TextView tvClinicClosed;
    private boolean isMobileNumberOptional;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_event, container, false);
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
            addEventsFromScreenType = AddEventsFromScreenType.values()[ordinal];
        }
        if (addEventsFromScreenType != null)
            init();
        setWidthHeight(0.80, 0.90);
        if (!Util.isNullOrZeroNumber(selectedTime)) {
            initData();
        }
        mActivity.showLoading(false);
        if (!Util.isNullOrBlank(appointmentId))
            initActionbarTitle(getResources().getString(R.string.reschedule_event));
        else
            initActionbarTitle(getResources().getString(R.string.new_event));

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
        editTitle = (EditText) view.findViewById(R.id.et_subject);
        cbEmailDoctor = (CheckBox) view.findViewById(R.id.cb_email_doctor);
        cbEmailPatient = (CheckBox) view.findViewById(R.id.cb_email_patient);

        btAddNewPatient = (Button) view.findViewById(R.id.bt_add_new_patient);
        editMobileNumber = (EditText) view.findViewById(R.id.edit_mobile_number);
        btClearMobileNumber = (ImageButton) view.findViewById(R.id.bt_clear_mobile_number);
        autotvPatientName = (AutoCompleteTextView) view.findViewById(R.id.autotv_patient_name);
        containerDetailsAddNewPatient = (FrameLayout) view.findViewById(R.id.container_details_add_new_patient);
        loadingExistingPatientsList = (LinearLayout) view.findViewById(R.id.loading_existing_patients_list);
        parentAddSelectPatient = (LinearLayout) view.findViewById(R.id.parent_add_select_patient);
        layoutDoctorName = (LinearLayout) view.findViewById(R.id.layout_doctor_name);
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);


        tvSelectedTime = (TextView) view.findViewById(R.id.tv_selected_time);
        tvAppointmentSlotDuration = (TextView) view.findViewById(R.id.tv_appointment_slot);
        tvSelectedDate = (TextView) view.findViewById(R.id.tv_selected_date);
        tvClinicClosed = (TextView) view.findViewById(R.id.tv_clinic_closed);
        refreshSelectedDate(DateTimeUtil.getCurrentDateLong());
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        tvSelectedDate.setOnClickListener(this);
        tvSelectedDate.addTextChangedListener(new HealthcocoTextWatcher(tvSelectedDate, this));
        btSelectPatient.setOnClickListener(this);
        tvSelectedTime.setOnClickListener(this);
        switch (addEventsFromScreenType) {
            case EVENTS_LIST_ADD_NEW:
                containerPatientProfileHeader.setOnClickListener(this);
                break;
            default:
                parentAddSelectPatient.setVisibility(View.GONE);
                break;
        }
        btAddNewPatient.setOnClickListener(this);
        btClearMobileNumber.setOnClickListener(this);
        editMobileNumber.addTextChangedListener(new HealthcocoTextWatcher(editMobileNumber, this));
        autotvPatientName.addTextChangedListener(new HealthcocoTextWatcher(autotvPatientName, this));
    }

    @Override
    public void initData() {
//        tvClinicName.setText(doctorClinicProfile.getLocationName());

        if (!Util.isNullOrZeroNumber(selectedTime)) {
            tvSelectedTime.setText(DateTimeUtil.getFormttedTime(selectedTime));
            refreshSelectedDate(selectedTime);
        }
        if (selectedEvent != null && selectedEvent.getTime() != null && selectedEvent.getTime().getFromTime() != null)
            tvSelectedTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(selectedEvent.getTime().getFromTime())));
        if (doctorClinicProfile != null) {
            tvClinicName.setText(Util.getValidatedValue(doctorClinicProfile.getLocationName()));
            if (doctorClinicProfile.getAppointmentSlot() != null) {
                tvAppointmentSlotDuration.setTag(doctorClinicProfile.getAppointmentSlot().getTime());
                String formattedString = Math.round(doctorClinicProfile.getAppointmentSlot().getTime()) + " " + Util.getValidatedValue(doctorClinicProfile.getAppointmentSlot().getTimeUnit().getValueToDisplay());
                tvAppointmentSlotDuration.setText(formattedString);
            }
        }
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
            initActionPatientDetailActionBar(PatientProfileScreenType.IN_EMR_HEADER, view, selectedPatient);
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
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId())) {
                        tvSelectedDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
                        initData();
                        initSelectedAppointment();
                        initPatientProfile();
                        if (!Util.isNullOrEmptyList(registeredDoctorProfileList))
                            fromHashMapAndRefresh(registeredDoctorProfileList);
                        tvDoctorName.setText("Dr. " + user.getFirstName());
                        return;
                    }
                    break;
                case ADD_EVENT:
                    if (response.isValidData(response) && response.getData() instanceof Events) {
                        Events events = (Events) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addEventsUpdated(events);
                        Util.showToast(mActivity, R.string.event_created);
                        events.setDoctorName(String.valueOf(tvDoctorName.getText()).trim());
                        sendBroadcasts(events.getUniqueId());
                        getDialog().dismiss();
                    }
                    break;

                case SEARCH_PATIENTS:
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
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void showSearchedPatientsListPopUp(List<AlreadyRegisteredPatientsResponse> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
                existingPatientAutotvAdapter = new ExistingPatientAutoCompleteAdapter(mActivity, R.layout.autocomplete_existing_patient,
                        list);
                autotvPatientName.setThreshold(0);
                autotvPatientName.setAdapter(existingPatientAutotvAdapter);
                autotvPatientName.setDropDownAnchor(autotvPatientName.getId());
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
                                editMobileNumber.setText(existingPatient.getMobileNumber());
                            }
                        }
                    }
                });
                requestFocusOnEditText(autotvPatientName);
                autotvPatientName.showDropDown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSelectedAppointment() {
        if (selectedEvent != null) {
            tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, selectedEvent.getFromDate()));
            editNote.setText(Util.getValidatedValue(selectedEvent.getExplanation()));
            editTitle.setText(Util.getValidatedValue(selectedEvent.getSubject()));
        }
    }

    private void sendBroadcasts(String eventId) {
        Intent intent = new Intent();
        switch (addEventsFromScreenType) {
            case EVENTS_LIST_ADD_NEW:
                intent.setAction(EventFragment.INTENT_GET_EVENT_LIST_LOCAL);
                break;
        }

        intent.putExtra("eventId", eventId);
//        if (intent != null && intent.getAction() != null)
        LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
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
                    user = doctor.getUser();
                   /* if (!Util.isNullOrBlank(appointmentId)) {
                        selectedAppointment = LocalDataServiceImpl.getInstance(mApp).getAppointment(appointmentId);
                        if (!Util.isNullOrBlank(selectedAppointment.getPatientId()))
                            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selectedAppointment.getPatientId();
                    }*/
//                    if (bookAppointmentFromScreenType != BookAppointmentFromScreenType.APPOINTMENTS_QUEUE_ADD_NEW)
//                        selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                    registeredDoctorProfileList = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorDetails(user.getForeignLocationId());
                    doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
                    if (doctorClinicProfile != null && doctorClinicProfile.getMobileNumberOptional() != null)
                        isMobileNumberOptional = doctorClinicProfile.getMobileNumberOptional();
                }
                break;
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
        switch (v.getId()) {
            case R.id.tv_selected_date:
                openDatePickerDialog((TextView) v);
                break;
            case R.id.tv_selected_time:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.container_patient_profile_header:
            case R.id.bt_select_patient:
                openContactsListScreen();
                break;
            case R.id.bt_save:
                if (user != null) {
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        validateData();
                    } else
                        Util.showToast(mActivity, R.string.user_offline);
                }
                break;
            case R.id.bt_clear_mobile_number:
                editMobileNumber.setText("");
                break;
            case R.id.bt_add_new_patient:
//                showDetailsAddNewPatient(true);
                break;
            case R.id.bt_register_new_patient:
//                autotvPatientName.setVisibility(View.VISIBLE);
//                autotvPatientName.setEnabled(true);
//                requestFocusOnEditText(autotvPatientName);
                break;
        }
    }

    private void showDetailsAddNewPatient(boolean b) {
        if (b) {
            containerDetailsAddNewPatient.setVisibility(View.VISIBLE);
            btAddNewPatient.setVisibility(View.GONE);
            requestFocusOnEditText(editMobileNumber);
        } else {
            containerDetailsAddNewPatient.setVisibility(View.GONE);
//            btAddNewPatient.setVisibility(View.VISIBLE);
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
        String title = String.valueOf(editTitle.getText()).trim();
        String note = String.valueOf(editNote.getText()).trim();

        if (selectedPatient == null && Util.isNullOrBlank(mobileNumber) && Util.isNullOrBlank(patientName)) {
            msg = getResources().getString(R.string.please_select_patient_or_add_new);
        }
        if (title == null || Util.isNullOrBlank(title)) {
            msg = getResources().getString(R.string.please_add_title);
        }
        if (note == null || Util.isNullOrBlank(note)) {
            msg = getResources().getString(R.string.please_add_note);
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

        EventRequest eventRequest = new EventRequest();
//        appointment.setCreatedBy(CreatedByType.DOCTOR);
        if (!Util.isNullOrBlank(appointmentId)) {
            eventRequest.setDoctorId(selectedEvent.getDoctorId());
            eventRequest.setHospitalId(selectedEvent.getHospitalId());
            eventRequest.setLocationId(selectedEvent.getLocationId());
        } else {
            eventRequest.setDoctorId(user.getUniqueId());
            eventRequest.setHospitalId(user.getForeignHospitalId());
            eventRequest.setLocationId(user.getForeignLocationId());
        }
        eventRequest.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (selectedPatient != null) {
            eventRequest.setLocalPatientName(selectedPatient.getFirstName());
            eventRequest.setMobileNumber(selectedPatient.getMobileNumber());
        } else {
            eventRequest.setLocalPatientName(Util.getValidatedValueOrNull(autotvPatientName));
            eventRequest.setMobileNumber(Util.getValidatedValueOrNull(autotvPatientName));
        }
        eventRequest.setExplanation(Util.getValidatedValueOrNull(editNote));
        eventRequest.setSubject(Util.getValidatedValueOrNull(editTitle));
        eventRequest.setFromDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, selecetdDate));
        eventRequest.setToDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, selecetdDate));
        eventRequest.setTime(new WorkingHours(selectedFromTimeInMinutes, selectedFromTimeInMinutes + (float) tvAppointmentSlotDuration.getTag()));
        eventRequest.setPatientRequired(true);

        if (!Util.isNullOrBlank(appointmentId))
            eventRequest.setUniqueId(appointmentId);

        switch (addEventsFromScreenType) {
            case EVENTS_LIST_RESCHEDULE:
                eventRequest.setState(AppointmentStatusType.RESCHEDULE);
                break;
            default:
                eventRequest.setState(AppointmentStatusType.NEW);
                break;
        }
        WebDataServiceImpl.getInstance(mApp).addEvent(Events.class, eventRequest, this, this);
    }

    private void clearPreviousAlerts() {
        tvSelectedDate.setActivated(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        sendBroadcasts(null);
        mActivity.hideLoading();
        if (addEventsFromScreenType == AddEventsFromScreenType.EVENTS_LIST_ADD_NEW
                || addEventsFromScreenType == AddEventsFromScreenType.EVENTS_LIST_RESCHEDULE)
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
        selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (isExistingPatientPartOfClinic && selectedPatient == null) {
            mActivity.showLoading(false);
            Util.sendBroadcast(mApp, ContactsListFragment.INTENT_REFRESH_CONTACTS_LIST_FROM_SERVER);
            return;
        } else
            mActivity.hideLoading();
        editMobileNumber.setText("");
        autotvPatientName.setText("");
        initPatientProfile();
    }

    @Override
    public void afterTextChange(View v, String s) {
        switch (v.getId()) {
            case R.id.autotv_patient_name:
                if (!Util.isNullOrBlank(s) && containerPatientProfileHeader.getVisibility() == View.VISIBLE)
                    showSelectedPatientHeader(false);
                break;
            case R.id.edit_mobile_number:
                LogUtils.LOGD(TAG, "Edit Mobile Number");
                autotvPatientName.setText("");
                HealthCocoConstants.SELECTED_PATIENTS_USER_ID = null;
                if (Util.isValidMobileNo(s)) {
                    getExistingPatientsList(s);
                }
                break;
        }
    }

    private void getExistingPatientsList(String mobileNo) {
        loadingExistingPatientsList.setVisibility(View.VISIBLE);
        WebDataServiceImpl.getInstance(mApp).getAlreadyRegisteredPatients(AlreadyRegisteredPatientsResponse.class, mobileNo, user, this, this);
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case APPOINTMENT_SLOT:
                if (object != null && object instanceof AppointmentSlotsType) {
                    AppointmentSlotsType appointmentSlotsType = (AppointmentSlotsType) object;
                    String formattedString = Math.round(appointmentSlotsType.getTime()) + " " + Util.getValidatedValue(appointmentSlotsType.getUnits().getValueToDisplay());
                    tvAppointmentSlotDuration.setText(formattedString);
                    tvAppointmentSlotDuration.setTag(appointmentSlotsType.getTime());
                }
                break;
            case DOCTOR_LIST:
                if (object instanceof RegisteredDoctorProfile) {
                    RegisteredDoctorProfile doctorProfile = (RegisteredDoctorProfile) object;
                    tvDoctorName.setText(doctorProfile.getFirstNameWithTitle());
                    user.setUniqueId(doctorProfile.getUserId());
                }
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

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
            else
                mActivity.initPopupWindows(tvDoctorName, PopupWindowType.DOCTOR_LIST, new ArrayList<Object>(clinicDoctorListHashMap.values()), this);

        } else {
            tvDoctorName.setText("Dr. " + user.getFirstName());

        }
    }

}
