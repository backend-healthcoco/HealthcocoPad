package com.healthcoco.healthcocopad.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.AppointmentSlotAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AppointmentRequestToSend;
import com.healthcoco.healthcocopad.bean.request.RegisterNewPatientRequest;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.AppointmentSlot;
import com.healthcoco.healthcocopad.bean.server.AppointmentTimeSlotDetails;
import com.healthcoco.healthcocopad.bean.server.AvailableTimeSlots;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.custom.ExistingPatientAutoCompleteAdapter;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.custom.ScrollViewWithRelativeLayoutHeader;
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
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.popupwindow.HealthcocoPopupWindow;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ExpandableGridView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by Prashant on 14/07/2018.
 */

public class FollowUpAppointmentFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, AdapterView.OnItemClickListener, HealthcocoTextWatcherListener, AutoCompleteTextViewListener, PopupWindowListener {
    public static final String TIME_SLOT_FORMAT_USED_IN_THIS_SCREEN = "hh:mm aaa";
    public static final String TIME_FORMAT_RECEIVED_FROM_SERVER = "H:mm";
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    private static final int REQUEST_CODE_BOOK_APPOINTMENT = 101;
    public List<RegisteredDoctorProfile> registeredDoctorProfileList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private AppointmentTimeSlotDetails appointmentTimeSlotDetails;
    private TextView tvSelectedDate;
    private AppointmentSlotAdapter adapter;
    private AvailableTimeSlots selectedTimeSlot;
    private CheckBox cbEmailDoctor;
    private CheckBox cbEmailPatient;
    private CheckBox cbSmsDoctor;
    private CheckBox cbSmsPatient;
    //    private EditText editMobileNumber;
    private LinkedHashMap<String, RegisteredDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();
    private HealthcocoPopupWindow doctorsListPopupWindow;
    private EditText editPatientName;
    private DoctorClinicProfile doctorClinicProfile;
    private GridView gvTimeSlots;
    private TextView tvClinicClosed;
    private TextView tvSubmit;
    private TextView tvClinicName;
    private LinearLayout layoutDoctorName;
    private TextView tvDoctorName;
    private String patientUniqueId;
    private String mobileNumber;
    private String name;
    private AppointmentSlot selectedAppointmentSlot;
    private PatientRegistrationDetailsListener registrationDetailsListener;

    public FollowUpAppointmentFragment(PatientRegistrationDetailsListener registrationDetailsListener) {
        super();
        this.registrationDetailsListener = registrationDetailsListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_follow_up_appointment, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
    }

    @Override
    public void initViews() {
        cbEmailDoctor = (CheckBox) view.findViewById(R.id.cb_email_doctor);
        cbEmailPatient = (CheckBox) view.findViewById(R.id.cb_email_patient);
        cbSmsDoctor = (CheckBox) view.findViewById(R.id.cb_sms_doctor);
        cbSmsPatient = (CheckBox) view.findViewById(R.id.cb_sms_patient);

        editPatientName = (EditText) view.findViewById(R.id.edit_patient_name);
        layoutDoctorName = (LinearLayout) view.findViewById(R.id.layout_doctor_name);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        tvSubmit = (TextView) view.findViewById(R.id.tv_submit);
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);

        gvTimeSlots = (GridView) view.findViewById(R.id.gv_time_Slots);
        tvSelectedDate = (TextView) view.findViewById(R.id.tv_selected_date);
        tvClinicClosed = (TextView) view.findViewById(R.id.tv_clinic_closed);
        refreshSelectedDate(DateTimeUtil.getCurrentDateLong());
    }

    @Override
    public void initListeners() {
        tvSelectedDate.setOnClickListener(this);
        tvSelectedDate.addTextChangedListener(new HealthcocoTextWatcher(tvSelectedDate, this));

        gvTimeSlots.setOnItemClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    public void initData() {
    }

    private void initAdapter() {
        adapter = new AppointmentSlotAdapter(mActivity);
        gvTimeSlots.setAdapter(adapter);
    }

    private void refreshSelectedDate(long date) {
        tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, date));
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
                        tvSelectedDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
                        initData();
                        if (doctorClinicProfile != null)
                            tvClinicName.setText(Util.getValidatedValue(doctorClinicProfile.getLocationName()));
                        if (!Util.isNullOrEmptyList(registeredDoctorProfileList))
                            fromHashMapAndRefresh(registeredDoctorProfileList);
                        return;
                    }
                    break;
                case GET_APPOINTMENT_TIME_SLOTS:
                    if (response.getData() != null && response.getData() instanceof AppointmentTimeSlotDetails) {
                        appointmentTimeSlotDetails = (AppointmentTimeSlotDetails) response.getData();
                        refreshAppointmentSlotDetails(appointmentTimeSlotDetails);
                    }
                    break;

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
                    registeredDoctorProfileList = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorDetails(user.getForeignLocationId());
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
        int id = v.getId();

        if (id == R.id.tv_selected_date) {
            openDatePickerDialog((TextView) v);
        } else if (id == R.id.tv_submit) {
            if (user != null) {
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    validateData();
                } else {
                    Util.showToast(mActivity, R.string.user_offline);
                }
            }
        }
    }

    private void validateData() {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String selectedDate = String.valueOf(tvSelectedDate.getText()).trim();

      /*  if (selectedPatient == null && Util.isNullOrBlank(mobileNumber)*//* && Util.isNullOrBlank(patientName)*//*) {
            msg = getResources().getString(R.string.please_select_patient_or_add_new);
        } else if (!Util.isNullOrBlank(mobileNumber) && !Util.isValidMobileNo(mobileNumber)) {
            msg = getResources().getString(R.string.please_enter_valid_mobile_no_or_select_existing_patient);
//        } else if (!Util.isNullOrBlank(mobileNumber) && Util.isNullOrBlank(patientName)) {
            msg = getResources().getString(R.string.please_enter_patient_name_or_select_existing_patient);
        } else*/
        if (Util.isNullOrBlank(selectedDate)) {
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
        Float selectedFromTimeInMinutes = DateTimeUtil.getMinutesFromFormattedDateime(TIME_FORMAT_RECEIVED_FROM_SERVER,
                selectedTimeSlot.getTime());

        AppointmentRequestToSend appointment = new AppointmentRequestToSend();
        appointment.setCreatedBy(CreatedByType.DOCTOR);
        appointment.setDoctorId(user.getUniqueId());
        appointment.setHospitalId(user.getForeignHospitalId());
        appointment.setLocationId(user.getForeignLocationId());
        if (!Util.isNullOrBlank(patientUniqueId))
            appointment.setPatientId(patientUniqueId);
        else
            appointment.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);

        if (!Util.isNullOrBlank(name))
            appointment.setLocalPatientName(name);
        if (!Util.isNullOrBlank(mobileNumber))
            appointment.setMobileNumber(mobileNumber);

        appointment.setNotifyDoctorBySms(false);
        appointment.setNotifyDoctorByEmail(false);

        appointment.setNotifyPatientBySms(false);
        appointment.setNotifyPatientByEmail(true);
//
//        appointment.setExplanation(Util.getValidatedValueOrNull(editNote));
        appointment.setFromDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, selecetdDate));
        appointment.setToDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, selecetdDate));
        appointment.setTime(new WorkingHours(selectedFromTimeInMinutes, selectedFromTimeInMinutes + selectedAppointmentSlot.getTime()));


        appointment.setState(AppointmentStatusType.NEW);

        registrationDetailsListener.readyToMoveNext(appointment, false);
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

    @Override
    public void afterTextChange(View v, String s) {
        if (v.getId() == R.id.tv_selected_date) {
            LogUtils.LOGD(TAG, "TextVieew Selected Date ");
            getAppointmentSlotNew(DATE_FORMAT_USED_IN_THIS_SCREEN, s);
            if (!DateTimeUtil.isCurrentDateSelected(DATE_FORMAT_USED_IN_THIS_SCREEN,
                    Util.getValidatedValueOrNull(tvSelectedDate))) {
                // your code here if needed
            }
        }
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case DOCTOR_LIST:
                if (object instanceof RegisteredDoctorProfile) {
                    RegisteredDoctorProfile doctorProfile = (RegisteredDoctorProfile) object;
                    tvDoctorName.setText(doctorProfile.getFirstNameWithTitle());
                    user.setUniqueId(doctorProfile.getUserId());
                    getAppointmentSlotNew(DATE_FORMAT_USED_IN_THIS_SCREEN, Util.getValidatedValueOrNull(tvSelectedDate));
                }
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }

    private void fromHashMapAndRefresh(List<RegisteredDoctorProfile> responseList) {
        if (responseList.size() > 1) {
            tvDoctorName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_drug, 0);
            tvDoctorName.setEnabled(true);
            tvDoctorName.setText("Dr. " + user.getFirstName());

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
                mActivity.initPopupWindows(tvDoctorName, PopupWindowType.DOCTOR_LIST, new ArrayList<Object>(clinicDoctorListHashMap.values()), this);
            }
        } else {
            tvDoctorName.setText("Dr. " + user.getFirstName());
        }
    }


    public void initDataFromPreviousFragment(Object object, boolean isEditPatient) {
        RegisterNewPatientRequest patientRequest = (RegisterNewPatientRequest) object;
        if (patientRequest != null) {
            if (!Util.isNullOrBlank(patientRequest.getUserId()))
                patientUniqueId = patientRequest.getUserId();

            if (!Util.isNullOrBlank(patientUniqueId)) {
                if (!Util.isNullOrBlank(patientUniqueId)) {
                    if (isEditPatient) {
                        RegisteredPatientDetailsUpdated patient = LocalDataServiceImpl.getInstance(mApp).getPatient(patientUniqueId, user.getForeignLocationId());
                        if (patient != null)
                            initPatientDetails(patient);
                        else {
                            AlreadyRegisteredPatientsResponse alreadyRegisteredPatient = LocalDataServiceImpl.getInstance(mApp).getALreadyRegisteredPatient(patientUniqueId);
                            if (alreadyRegisteredPatient != null) {
                                initPatientDetails(alreadyRegisteredPatient);
                            }
                        }
                    }
                }
            }
        }
    }

    private void initPatientDetails(Object patientDetails) {

        if (patientDetails instanceof RegisteredPatientDetailsUpdated) {
            RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated = (RegisteredPatientDetailsUpdated) patientDetails;
            name = Util.getValidatedValue(registeredPatientDetailsUpdated.getLocalPatientName());
            mobileNumber = Util.getValidatedValue(registeredPatientDetailsUpdated.getMobileNumber());

        } else if (patientDetails instanceof AlreadyRegisteredPatientsResponse) {
            AlreadyRegisteredPatientsResponse alreadyRegisteredPatient = (AlreadyRegisteredPatientsResponse) patientDetails;
            mobileNumber = Util.getValidatedValue(alreadyRegisteredPatient.getMobileNumber());
            name = Util.getValidatedValue(alreadyRegisteredPatient.getFirstName());
        }

    }
}
