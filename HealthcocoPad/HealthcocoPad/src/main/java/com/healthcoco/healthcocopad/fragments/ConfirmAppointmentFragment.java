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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.AppointmentSlotAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AppointmentRequestToSend;
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
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.CreatedByType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
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
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by Prashant on 14/07/2018.
 */

public class ConfirmAppointmentFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener {
    public static final String TIME_SLOT_FORMAT_USED_IN_THIS_SCREEN = "hh:mm aaa";
    public static final String TIME_FORMAT_RECEIVED_FROM_SERVER = "H:mm";
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    private static final int REQUEST_CODE_BOOK_APPOINTMENT = 101;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private AppointmentTimeSlotDetails appointmentTimeSlotDetails;
    private TextView tvSelectedDate;
    private TextView tvSelectedTime;
    private EditText editNote;
    private CheckBox cbEmailDoctor;
    private CheckBox cbEmailPatient;
    private CheckBox cbSmsDoctor;
    private CheckBox cbSmsPatient;
    private TextView tvMobileNumber;
    private TextView tvPatientName;
    private RegisteredDoctorProfile registeredDoctorProfile;
    private DoctorClinicProfile doctorClinicProfile;
    private TextView tvSubmit;
    private TextView tvClinicName;
    private LinearLayout layoutDoctorName;
    private TextView tvDoctorName;
    private PatientRegistrationDetailsListener registrationDetailsListener;
    private AppointmentRequestToSend appointment;

    public ConfirmAppointmentFragment(PatientRegistrationDetailsListener registrationDetailsListener) {
        super();
        this.registrationDetailsListener = registrationDetailsListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_confirm_appointment, container, false);
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
    }

    @Override
    public void initViews() {
        editNote = (EditText) view.findViewById(R.id.edit_note);
        cbEmailDoctor = (CheckBox) view.findViewById(R.id.cb_email_doctor);
        cbEmailPatient = (CheckBox) view.findViewById(R.id.cb_email_patient);
        cbSmsDoctor = (CheckBox) view.findViewById(R.id.cb_sms_doctor);
        cbSmsPatient = (CheckBox) view.findViewById(R.id.cb_sms_patient);

        tvMobileNumber = (TextView) view.findViewById(R.id.tv_mobile_number);
        tvPatientName = (TextView) view.findViewById(R.id.tv_patient_name);
        layoutDoctorName = (LinearLayout) view.findViewById(R.id.layout_doctor_name);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        tvSubmit = (TextView) view.findViewById(R.id.tv_submit);
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);

        tvSelectedDate = (TextView) view.findViewById(R.id.tv_selected_date);
        tvSelectedTime = (TextView) view.findViewById(R.id.tv_selected_time);
    }

    @Override
    public void initListeners() {

        tvSubmit.setOnClickListener(this);
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
                        if (doctorClinicProfile != null)
                            tvClinicName.setText(Util.getValidatedValue(doctorClinicProfile.getLocationName()));
                    }
                    break;
                case ADD_APPOINTMENT:
                    if (response.isValidData(response) && response.getData() instanceof CalendarEvents) {
                        CalendarEvents calendarEvents = (CalendarEvents) response.getData();
                        calendarEvents.setIsAddedOnSuccess(true);
                        LocalDataServiceImpl.getInstance(mApp).addAppointment(calendarEvents);
                        Util.showToast(mActivity, R.string.appointment_created);
                        mActivity.finish();
                    }
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
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
                    doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
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
            case R.id.tv_submit:
                if (user != null) {
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        validateData();
                    } else
                        Util.showToast(mActivity, R.string.user_offline);
                }
                break;
        }
    }

    private void validateData() {
        if (appointment != null)
            addAppointment();
    }

    private void addAppointment() {
        mActivity.showLoading(false);

        appointment.setExplanation(Util.getValidatedValueOrNull(editNote));

        WebDataServiceImpl.getInstance(mApp).addAppointment(CalendarEvents.class, appointment, this, this);
    }


    public void initDataFromPreviousFragment(Object object, boolean isEditPatient) {
        appointment = (AppointmentRequestToSend) object;
        if (appointment != null) {
            registeredDoctorProfile = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorProfile(appointment.getDoctorId());
            if (registeredDoctorProfile != null)
                initData();
        }
    }

    private void initData() {
        tvSelectedDate.setText(DateTimeUtil.getFormatedDateAndTime(DATE_FORMAT_USED_IN_THIS_SCREEN, appointment.getFromDate()));
        if (registeredDoctorProfile != null)
            tvDoctorName.setText(Util.getValidatedValue(registeredDoctorProfile.getFirstNameWithTitle()));
        tvPatientName.setText(appointment.getLocalPatientName());
        tvMobileNumber.setText(appointment.getMobileNumber());
        if (appointment.getTime() != null && appointment.getTime().getFromTime() != null && appointment.getTime().getToTime() != null) {
            WorkingHours workingHours = appointment.getTime();
            tvSelectedTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getFromTime())) + " - "
                    + DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getToTime())));
        }
    }
}
