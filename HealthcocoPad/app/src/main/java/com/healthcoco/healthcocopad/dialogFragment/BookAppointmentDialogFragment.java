package com.healthcoco.healthcocopad.dialogFragment;

import android.app.DatePickerDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.AppointmentSlotAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.AppointmentSlot;
import com.healthcoco.healthcocopad.bean.server.AppointmentTimeSlotDetails;
import com.healthcoco.healthcocopad.bean.server.AvailableTimeSlots;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.ExistingPatientAutoCompleteAdapter;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.custom.ScrollViewWithRelativeLayoutHeader;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.CreatedByType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.CalendarFragment;
import com.healthcoco.healthcocopad.fragments.CommonOpenUpPatientDetailFragment;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.PatientAppointmentDetailFragment;
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;
import com.healthcoco.healthcocopad.views.ExpandableGridView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class BookAppointmentDialogFragment extends HealthCocoDialogFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, AdapterView.OnItemClickListener, HealthcocoTextWatcherListener, AutoCompleteTextViewListener {
    public static final String INTENT_REFRESH_SELECTED_PATIENT = "com.healthcoco.REFRESH_SELECTED_PATIENT";
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public static final String TIME_SLOT_FORMAT_USED_IN_THIS_SCREEN = "hh:mm aaa";
    public static final String TIME_FORMAT_RECEIVED_FROM_SERVER = "H:mm";
    public static final String TAG_FROM_SCREEN_TYPE = "isFromCalendarFragment";
    public static final String TAG_APPOINTMENT_ID = "appointmentId";
    private static final int REQUEST_CODE_BOOK_APPOINTMENT = 101;

    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private AppointmentTimeSlotDetails appointmentTimeSlotDetails;
    private TextView tvSelectedDate;
    private AppointmentSlotAdapter adapter;
    private Button btSelectPatient;
    private LinearLayout containerPatientProfileHeader;
    private BookAppointmentFromScreenType bookAppointmentFromScreenType;
    private AvailableTimeSlots selectedTimeSlot;
    private String appointmentId;
    private CalendarEvents selectedAppointment;
    private EditText editNote;
    private boolean receiversRegistered;
    private CheckBox cbEmailDoctor;
    private CheckBox cbEmailPatient;
    private CheckBox cbSmsDoctor;
    private CheckBox cbSmsPatient;
    private LinearLayout containerSelectPatient;
    private Button btAddNewPatient;
    private EditText editMobileNumber;
    private ImageButton btClearMobileNumber;
    private FrameLayout containerDetailsAddNewPatient;
    private ExistingPatientAutoCompleteAdapter existingPatientAutotvAdapter;
    private AutoCompleteTextView autotvPatientName;
    private LinearLayout loadingExistingPatientsList;
    private LinearLayout parentAddSelectPatient;
    private TextView tvClinicName;
    private DoctorClinicProfile doctorClinicProfile;
    private ExpandableGridView gvTimeSlots;
    private ImageButton btPreviousDate;
    private ImageButton btNextDate;
    private RelativeLayout headerOne;
    private TextView tvClinicClosed;
    private AppointmentSlot selectedAppointmentSlot;
    private ScrollViewWithRelativeLayoutHeader svContainer;
    private RelativeLayout header;
    private ImageButton btPreviousDateHeader;
    private ImageButton btNextDateHeader;
    private TextView tvSelectedDateHeader;
//    private TextView tvSelectedTimeSlot;
//    private TextView tvSelectedTimeSlotHeader;

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
        int ordinal = Parcels.unwrap(bundle.getParcelable(TAG_FROM_SCREEN_TYPE));
        appointmentId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
        bookAppointmentFromScreenType = BookAppointmentFromScreenType.values()[ordinal];
        if (bookAppointmentFromScreenType != null)
            init();
        setWidthHeight(0.80, 0.90);
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initScrollView();
        initAdapter();
        initPatientProfile();
    }

    private void initScrollView() {
        svContainer.addFixedHeader(header);
        svContainer.addChildHeaders(headerOne);
        svContainer.build();
    }

    @Override
    public void initViews() {
        svContainer = (ScrollViewWithRelativeLayoutHeader) view.findViewById(R.id.scrollview_appointment);
        btSelectPatient = (Button) view.findViewById(R.id.bt_select_patient);
        containerPatientProfileHeader = (LinearLayout) view.findViewById(R.id.container_patient_profile_header);
        containerSelectPatient = (LinearLayout) view.findViewById(R.id.container_select_patient);
        editNote = (EditText) view.findViewById(R.id.edit_note);
        cbEmailDoctor = (CheckBox) view.findViewById(R.id.cb_email_doctor);
        cbEmailPatient = (CheckBox) view.findViewById(R.id.cb_email_patient);
        cbSmsDoctor = (CheckBox) view.findViewById(R.id.cb_sms_doctor);
        cbSmsPatient = (CheckBox) view.findViewById(R.id.cb_sms_patient);

        btAddNewPatient = (Button) view.findViewById(R.id.bt_add_new_patient);
        editMobileNumber = (EditText) view.findViewById(R.id.edit_mobile_number);
        btClearMobileNumber = (ImageButton) view.findViewById(R.id.bt_clear_mobile_number);
        autotvPatientName = (AutoCompleteTextView) view.findViewById(R.id.autotv_patient_name);
        containerDetailsAddNewPatient = (FrameLayout) view.findViewById(R.id.container_details_add_new_patient);
        loadingExistingPatientsList = (LinearLayout) view.findViewById(R.id.loading_existing_patients_list);
        parentAddSelectPatient = (LinearLayout) view.findViewById(R.id.parent_add_select_patient);
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);

        gvTimeSlots = (ExpandableGridView) view.findViewById(R.id.gv_time_Slots);

        tvSelectedDate = (TextView) view.findViewById(R.id.tv_selected_date);
//        tvSelectedTimeSlot = (TextView) view.findViewById(R.id.tv_selected_time_slot);
        btPreviousDate = (ImageButton) view.findViewById(R.id.bt_previuos_date);
        btNextDate = (ImageButton) view.findViewById(R.id.bt_next_date);
        header = (RelativeLayout) view.findViewById(R.id.header);
        headerOne = (RelativeLayout) view.findViewById(R.id.header_one);
        tvSelectedDateHeader = (TextView) view.findViewById(R.id.tv_selected_date_header);
//        tvSelectedTimeSlotHeader = (TextView) view.findViewById(R.id.tv_selected_time_slot_header);
        btPreviousDateHeader = (ImageButton) view.findViewById(R.id.bt_previuos_date_header);
        btNextDateHeader = (ImageButton) view.findViewById(R.id.bt_next_date_header);
        tvClinicClosed = (TextView) view.findViewById(R.id.tv_clinic_closed);
        tvClinicClosed.setMinimumHeight((int) (ScreenDimensions.SCREEN_HEIGHT * 0.70));
        gvTimeSlots.setExpanded(true);
        refreshSelectedDate(DateTimeUtil.getCurrentDateLong());
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.new_appointment));
        tvSelectedDate.setOnClickListener(this);
        tvSelectedDate.addTextChangedListener(new HealthcocoTextWatcher(tvSelectedDate, this));
        btSelectPatient.setOnClickListener(this);
        switch (bookAppointmentFromScreenType) {
            case CALENDAR_LIST_ADD_NEW:
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
        gvTimeSlots.setOnItemClickListener(this);
        btPreviousDate.setOnClickListener(this);
        btNextDate.setOnClickListener(this);
        tvSelectedDateHeader.setOnClickListener(this);
        btPreviousDateHeader.setOnClickListener(this);
        btNextDateHeader.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tvClinicName.setText(doctorClinicProfile.getLocationName());
    }

    private void initAdapter() {
        adapter = new AppointmentSlotAdapter(mActivity);
        gvTimeSlots.setAdapter(adapter);
    }

    private void refreshSelectedDate(long date) {
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
                textView.setText(DateTimeUtil.getFormattedTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, year, monthOfYear, dayOfMonth, 0, 0, 0));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
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
                        header.setVisibility(View.GONE);
                        tvSelectedDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
                        initData();
                        initSelectedAppointment();
                        initPatientProfile();
                        return;
                    }
                    break;
                case GET_APPOINTMENT_TIME_SLOTS:
                    if (response.getData() != null && response.getData() instanceof AppointmentTimeSlotDetails) {
                        appointmentTimeSlotDetails = (AppointmentTimeSlotDetails) response.getData();
                        refreshAppointmentSlotDetails(appointmentTimeSlotDetails);
                    }
                    break;
                case ADD_APPOINTMENT:
                    if (response.isValidData(response) && response.getData() instanceof CalendarEvents) {
                        CalendarEvents calendarEvents = (CalendarEvents) response.getData();
                        calendarEvents.setIsAddedOnSuccess(true);
                        if (bookAppointmentFromScreenType == BookAppointmentFromScreenType.CALENDAR_LIST_ADD_NEW
                                || bookAppointmentFromScreenType == BookAppointmentFromScreenType.CALENDAR_LIST_RESCHEDULE)
                            LocalDataServiceImpl.getInstance(mApp).addCalendarEventsUpdated(calendarEvents);
                        else
                            LocalDataServiceImpl.getInstance(mApp).addAppointment(calendarEvents);
                        Util.showToast(mActivity, R.string.appointment_created);
                        sendBroadcasts(calendarEvents.getAppointmentId());
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

    private void refreshAppointmentSlotDetails(AppointmentTimeSlotDetails appointmentTimeSlotDetails) {
        if (appointmentTimeSlotDetails != null) {
            if (appointmentTimeSlotDetails.getAppointmentSlot() != null) {
                this.selectedAppointmentSlot = appointmentTimeSlotDetails.getAppointmentSlot();
                LogUtils.LOGD(TAG, "Time " + appointmentTimeSlotDetails.getAppointmentSlot().getTime());
            }
            notifyAdapter(appointmentTimeSlotDetails.getSlots());
        }
    }

    private void notifyAdapter(ArrayList<AvailableTimeSlots> list) {
        if (!Util.isNullOrEmptyList(list)) {
            tvClinicClosed.setVisibility(View.GONE);
            gvTimeSlots.setVisibility(View.VISIBLE);
            adapter.setListData(list);
            adapter.notifyDataSetChanged();
        } else {
            tvClinicClosed.setVisibility(View.VISIBLE);
            gvTimeSlots.setVisibility(View.GONE);
        }
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
        if (selectedAppointment != null) {
            tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, selectedAppointment.getFromDate()));
            editNote.setText(Util.getValidatedValue(selectedAppointment.getExplanation()));
        }
    }

    private void sendBroadcasts(String aptId) {
        Intent intent = new Intent(PatientAppointmentDetailFragment.INTENT_GET_APPOINTMENT_LIST_LOCAL);
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
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                    selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                    doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
                    if (!Util.isNullOrBlank(appointmentId))
                        selectedAppointment = LocalDataServiceImpl.getInstance(mApp).getAppointment(appointmentId);
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
            case R.id.tv_selected_date_header:
            case R.id.tv_selected_date:
                openDatePickerDialog((TextView) v);
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
                showDetailsAddNewPatient(true);
                break;
            case R.id.bt_register_new_patient:
                autotvPatientName.setVisibility(View.VISIBLE);
                autotvPatientName.setEnabled(true);
                requestFocusOnEditText(autotvPatientName);
                break;
            case R.id.bt_next_date_header:
            case R.id.bt_next_date:
                long nextDateTimeInMillis = DateTimeUtil.getNextDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN,
                        Util.getValidatedValueOrNull(tvSelectedDate)));
                tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, nextDateTimeInMillis));
                break;
            case R.id.bt_previuos_date_header:
            case R.id.bt_previuos_date:
                long previousDateTimeInMillis = DateTimeUtil.getPreviousDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN,
                        Util.getValidatedValueOrNull(tvSelectedDate)));
                tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, previousDateTimeInMillis));
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
            btAddNewPatient.setVisibility(View.VISIBLE);
            editMobileNumber.setText("");
            autotvPatientName.setText("");
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = null;
        }
    }

    private void openContactsListScreen() {
        openCommonOpenUpActivity(CommonOpenUpFragmentType.CONTACTS_LIST, ContactsListFragment.TAG_IS_IN_HOME_ACTIVITY, false, REQUEST_CODE_BOOK_APPOINTMENT);
    }

    private void validateData() {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String selectedDate = String.valueOf(tvSelectedDate.getText()).trim();
        String mobileNumber = String.valueOf(editMobileNumber.getText()).trim();
        String patientName = String.valueOf(autotvPatientName.getText()).trim();

        if (selectedPatient == null && Util.isNullOrBlank(mobileNumber) && Util.isNullOrBlank(patientName)) {
            msg = getResources().getString(R.string.please_select_patient_or_add_new);
        } else if (!Util.isNullOrBlank(mobileNumber) && !Util.isValidMobileNo(mobileNumber)) {
            msg = getResources().getString(R.string.please_enter_valid_mobile_no_or_select_existing_patient);
        } else if (!Util.isNullOrBlank(mobileNumber) && Util.isNullOrBlank(patientName)) {
            msg = getResources().getString(R.string.please_enter_patient_name_or_select_existing_patient);
        } else if (Util.isNullOrBlank(selectedDate)) {
            msg = getResources().getString(R.string.please_select_date);
            errorViewList.add(tvSelectedDate);
        } else if (selectedAppointmentSlot == null || selectedTimeSlot == null) {
            msg = getResources().getString(R.string.please_select_time_slot);
        }
        if (Util.isNullOrBlank(msg))
            addAppointment(selectedDate);
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addAppointment(String selecetdDate) {
        mActivity.showLoading(false);
        Float selectedFromTimeInMinutes = DateTimeUtil.getMinutesFromFormattedDateime(TIME_FORMAT_RECEIVED_FROM_SERVER,
                selectedTimeSlot.getTime());

        AppointmentRequest appointment = new AppointmentRequest();
        appointment.setCreatedBy(CreatedByType.DOCTOR);
        appointment.setDoctorId(user.getUniqueId());
        appointment.setHospitalId(user.getForeignHospitalId());
        appointment.setLocationId(user.getForeignLocationId());
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
        appointment.setTime(new WorkingHours(selectedFromTimeInMinutes, selectedFromTimeInMinutes + selectedAppointmentSlot.getTime()));

        if (!Util.isNullOrBlank(appointmentId))
            appointment.setAppointmentId(appointmentId);

        switch (bookAppointmentFromScreenType) {
            case CALENDAR_LIST_RESCHEDULE:
            case APPOINTMENTS_LIST_RESCHEDULE:
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

    private void getAppointmentSlotNew(String format, String displayedDate) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            mActivity.showLoading(false);
            if (displayedDate.equalsIgnoreCase(DateTimeUtil.getCurrentFormattedDate(format))) {
                displayedDate = DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN);
            }
            WebDataServiceImpl.getInstance(mApp).getAppointmentSlotsDetails(AppointmentTimeSlotDetails.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, displayedDate), this, this);
        } else
            Util.showToast(mActivity, R.string.user_offline);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BOOK_APPOINTMENT) {
//            if (resultCode == HealthCocoConstants.RESULT_CODE_CONTACTS_LIST) {
//            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bookAppointmentFromScreenType == BookAppointmentFromScreenType.CALENDAR_LIST_ADD_NEW || bookAppointmentFromScreenType == BookAppointmentFromScreenType.CALENDAR_LIST_RESCHEDULE)
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

    BroadcastReceiver refreshSelectedContactReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getAndRefreshSelectedPatient(false);
        }
    };

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
            case R.id.tv_selected_date:
                LogUtils.LOGD(TAG, "TextVieew Selected Date ");
                tvSelectedDateHeader.setText(s);
                getAppointmentSlotNew(DATE_FORMAT_USED_IN_THIS_SCREEN, s);
                if (!DateTimeUtil.isCurrentDateSelected(DATE_FORMAT_USED_IN_THIS_SCREEN,
                        Util.getValidatedValueOrNull(tvSelectedDate))) {
                    setPreviousButtonVisibility(View.VISIBLE);
                } else
                    setPreviousButtonVisibility(View.GONE);
                break;
        }
    }

    private void setPreviousButtonVisibility(int visibility) {
        btPreviousDate.setVisibility(visibility);
        btPreviousDateHeader.setVisibility(visibility);
    }

    private void getExistingPatientsList(String mobileNo) {
        loadingExistingPatientsList.setVisibility(View.VISIBLE);
        WebDataServiceImpl.getInstance(mApp).getAlreadyRegisteredPatients(AlreadyRegisteredPatientsResponse.class, mobileNo, user, this, this);
    }

    BroadcastReceiver refreshPatientProfileReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getAndRefreshSelectedPatient(false);
        }
    };
}
