package com.healthcoco.healthcocopad.dialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.AppointmentTimeSlotDetails;
import com.healthcoco.healthcocopad.bean.server.AvailableTimeSlots;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AppointmentSlotsType;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.CreatedByType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
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

/**
 * Created by Shreshtha on 03-06-2017.
 */

public class NextReviewOnDialogFragment extends HealthCocoDialogFragment implements GsonRequest.ErrorListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, View.OnClickListener,
        HealthcocoTextWatcherListener, PopupWindowListener {
    public static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public static final String TAG_INTENT_SELECTED_TIME_SLOT = "selectedTimeSlot";
    private Button btDone;
    private TextView tvSelectedTime;
    private TextView tvSelectedDate;
    //    private TextView tvSelectedTimeSlot;
    private TextView tvAppointmentSlotDuration;
    private AvailableTimeSlots selectedTimeSlot;
    private AppointmentTimeSlotDetails appointmentTimeSlotDetails;
    private CheckBox cbSmsPatient;
    private CheckBox cbEmailPatient;
    private AppointmentRequest appointment;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private ArrayList<AvailableTimeSlots> availableTimeSlotsArrayList;
    private DoctorClinicProfile clinicProfile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_next_review_on, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        init();
        setWidthHeight(0.40, 0.60);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
        mActivity.initPopupWindows(tvAppointmentSlotDuration, PopupWindowType.APPOINTMENT_SLOT, PopupWindowType.APPOINTMENT_SLOT.getList(), this);
    }

    @Override
    public void initViews() {
        cbEmailPatient = (CheckBox) view.findViewById(R.id.cb_email_patient);
        cbSmsPatient = (CheckBox) view.findViewById(R.id.cb_sms_patient);
        tvSelectedDate = (TextView) view.findViewById(R.id.tv_selected_date);
//        tvSelectedTimeSlot = (TextView) view.findViewById(R.id.tv_selected_time_slot);
        btDone = (Button) view.findViewById(R.id.bt_done);
        tvSelectedTime = (TextView) view.findViewById(R.id.tv_selected_time);
        tvAppointmentSlotDuration = (TextView) view.findViewById(R.id.tv_appointment_slot);
        initActionbarTitle(getResources().getString(R.string.next_review_on));
    }

    @Override
    public void initListeners() {
        tvSelectedDate.setOnClickListener(this);
        tvSelectedTime.setOnClickListener(this);
        tvSelectedDate.addTextChangedListener(new HealthcocoTextWatcher(tvSelectedDate, this));
        btDone.setOnClickListener(this);
        initCrossButton();
    }

    @Override
    public void initData() {
//        tvSelectedTimeSlot.setText("");
//        if (appointmentTimeSlotDetails != null) {
//            if (appointmentTimeSlotDetails.getAppointmentSlot() != null) {
//                LogUtils.LOGD(TAG, "Time " + appointmentTimeSlotDetails.getAppointmentSlot().getTime());
//            }
//            if (!Util.isNullOrEmptyList(appointmentTimeSlotDetails.getSlots())) {
//                LogUtils.LOGD(TAG, "Slots Available " + appointmentTimeSlotDetails.getSlots().size());
//            }
//        }
        if (clinicProfile != null) {
            if (clinicProfile.getAppointmentSlot() != null) {
                tvAppointmentSlotDuration.setTag(clinicProfile.getAppointmentSlot().getTime());
                String formattedString = Math.round(clinicProfile.getAppointmentSlot().getTime()) + " " + Util.getValidatedValue(clinicProfile.getAppointmentSlot().getTimeUnit().getValueToDisplay());
                tvAppointmentSlotDuration.setText(formattedString);
            }
        }
    }

    private void refreshSelectedDate(long date) {
        tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, date));
    }

    private void clearPreviousAlerts() {
        tvSelectedDate.setActivated(false);
        tvAppointmentSlotDuration.setActivated(false);
        tvSelectedTime.setActivated(false);


    }

    @Override
    public void afterTextChange(View v, String s) {
        switch (v.getId()) {
            case R.id.tv_selected_date:
                LogUtils.LOGD(TAG, "TextVieew Selected Date ");
                getAppointmentSlotNew(DATE_FORMAT_USED_IN_THIS_SCREEN, s);
                break;
        }
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

//    @Override
//    public void onEmptyListFound(AutoCompleteTextView autoCompleteTextView) {
//        switch (autoCompleteTextView.getId()) {
//            case R.id.autotv_selected_time_slot:
//                Util.showToast(mActivity, R.string.no_slots_available);
//                break;
//        }
//    }
//
//    @Override
//    public void scrollToPosition(int position) {
//
//    }

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
                        return;
                    }
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
                    selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                    clinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
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
            case R.id.bt_done:
                if (user != null) {
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        validateData();
                    } else
                        Util.showToast(mActivity, R.string.user_offline);
                }
                break;
            case R.id.tv_selected_date:
                openDatePickerDialog((TextView) v);
                break;
            case R.id.tv_selected_time:
                openTimePickerDialog(null, (TextView) v);
                break;
        }
    }

    private void openTimePickerDialog(final String selectedFromTime, final TextView tvToTime) {
        String defaultPickerTime = selectedFromTime;
        String textTime = Util.getValidatedValueOrNull(tvToTime);
        boolean isTextShown = false;
        if (!Util.isNullOrBlank(textTime)) {
            isTextShown = true;
            defaultPickerTime = textTime;
        }
        final Calendar calendar = DateTimeUtil.getCalendarInstanceFromFormattedTime(BookAppointmentDialogFragment.TIME_FORMAT, defaultPickerTime, isTextShown, BookAppointmentDialogFragment.DEFAULT_TIME_INTERVAL);

        final TimePickerDialog datePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                long selectedFromDateTimeMillis = getSelectedFromDateTime(hourOfDay, minute);
                int msg = 0;
                if (!DateTimeUtil.selectedTimeIsGreaterThanTime(selectedFromDateTimeMillis, DateTimeUtil.getCurrentDateLong(DATE_FORMAT_USED_IN_THIS_SCREEN + "" + BookAppointmentDialogFragment.TIME_FORMAT))) {
                    msg = R.string.time_to_should_be_greater_than_current_time;
                }

                if (msg == 0) {
                    LogUtils.LOGD(TAG, "Time lesser");
                    tvToTime.setText(DateTimeUtil.getFormattedDateTime(BookAppointmentDialogFragment.TIME_FORMAT, selectedFromDateTimeMillis));
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

    private long getSelectedFromDateTime(int hourOfDay, int minute) {
        long selectedDatePickerTime = DateTimeUtil.getLongFromFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, Util.getValidatedValueOrNull(tvSelectedDate));
        Calendar calendar1 = DateTimeUtil.getCalendarInstance();
        calendar1.setTimeInMillis(selectedDatePickerTime);
        calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar1.set(Calendar.MINUTE, minute);
        return calendar1.getTimeInMillis();
    }

    private long getSelectedFromDateTime(int year, int month, int day) {
        long selectedDatePickerTime = DateTimeUtil.getLongFromFormattedDateTime(BookAppointmentDialogFragment.TIME_FORMAT, Util.getValidatedValueOrNull(tvSelectedTime));
        Calendar calendar1 = DateTimeUtil.getCalendarInstance();
        calendar1.setTimeInMillis(selectedDatePickerTime);
        calendar1.set(Calendar.YEAR, year);
        calendar1.set(Calendar.MONTH, month);
        calendar1.set(Calendar.DAY_OF_MONTH, day);
        return calendar1.getTimeInMillis();
    }

    private void validateData() {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String selectedTime = String.valueOf(tvSelectedTime.getText()).trim();
        String selectedDate = String.valueOf(tvSelectedDate.getText()).trim();
        String selectedSlotDuration = String.valueOf(tvAppointmentSlotDuration.getText()).trim();

        if (Util.isNullOrBlank(selectedDate)) {
            msg = getResources().getString(R.string.please_select_date);
            errorViewList.add(tvSelectedDate);
        } else if (Util.isNullOrBlank(selectedTime)) {
            msg = getResources().getString(R.string.please_select_time_slot);
            errorViewList.add(tvSelectedTime);
        } else if (Util.isNullOrBlank(selectedSlotDuration)) {
            msg = getResources().getString(R.string.please_select_appointment_slot_duration);
            errorViewList.add(tvSelectedTime);
        } /*else if (selectedAppointmentSlot == null || selectedTimeSlot == null) {
            msg = getResources().getString(R.string.please_select_time_slot);
        }*/
        if (Util.isNullOrBlank(msg))
            addAppointment(selectedDate);
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addAppointment(String selecetdDate) {
        Float selectedFromTimeInMinutes = 0f;
        if (tvSelectedTime.getTag() != null)
            selectedFromTimeInMinutes = DateTimeUtil.getMinutesFromFormattedTime((long) tvSelectedTime.getTag());

        appointment = new AppointmentRequest();
        appointment.setCreatedBy(CreatedByType.DOCTOR);
        appointment.setDoctorId(user.getUniqueId());
        appointment.setHospitalId(user.getForeignHospitalId());
        appointment.setLocationId(user.getForeignLocationId());
        appointment.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);

        appointment.setNotifyPatientBySms(cbSmsPatient.isChecked());
        appointment.setNotifyPatientByEmail(cbEmailPatient.isChecked());

        appointment.setFromDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, selecetdDate));
        appointment.setToDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, selecetdDate));
        appointment.setTime(new WorkingHours(selectedFromTimeInMinutes, selectedFromTimeInMinutes + (float) tvAppointmentSlotDuration.getTag()));


        appointment.setState(AppointmentStatusType.CONFIRM);

        getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_NEXT_REVIEW, new Intent().putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(appointment)));
        getDialog().dismiss();

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
        }
    }

    @Override
    public void onEmptyListFound() {
        Util.showToast(mActivity, R.string.no_slots_available);
    }
}
