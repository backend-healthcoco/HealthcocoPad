package com.healthcoco.healthcocopad.dialogFragment;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.Leave;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LeaveFromScreenType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class AddLeaveDialogFragment extends HealthCocoDialogFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, PopupWindowListener {

    public static final String TAG_FROM_SCREEN_TYPE = "isFromEMRFragment";

    private TextView tvPatientName;
    private TextView tvLeavetype;
    private TextView tvFromDate;
    private TextView tvToDate;
    private EditText editReason;
    private TextView tvRequestDate;
    private TextView tvStatus;
    private TextView tvCancelDate;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private LinearLayout layoutStatus;
    private LinearLayout layoutCancelDate;
    private User user;
    private Leave leave;
    private Leave leaveToSend;
    private String leaveId;
    private LeaveFromScreenType leaveFromScreenType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_leave, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int ordinal = Parcels.unwrap(bundle.getParcelable(TAG_FROM_SCREEN_TYPE));
            leaveId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
            leaveFromScreenType = LeaveFromScreenType.values()[ordinal];
        }
        if (leaveFromScreenType != null)
            init();
        setWidthHeight(0.80, 0.90);
     /*   if (!Util.isNullOrBlank(appointmentId))
            initActionbarTitle(getResources().getString(R.string.please_reschedule_appointment));
        else
            initActionbarTitle(getResources().getString(R.string.new_appointment));
*/
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        mActivity.initPopupWindows(tvLeavetype, PopupWindowType.LEAVE_TYPE, PopupWindowType.LEAVE_TYPE.getList(), this);
    }

    @Override
    public void initViews() {

        tvPatientName = (TextView) view.findViewById(R.id.tv_patient_name);
        tvLeavetype = (TextView) view.findViewById(R.id.tv_leave_type);
        tvFromDate = (TextView) view.findViewById(R.id.tv_from_date);
        tvToDate = (TextView) view.findViewById(R.id.tv_to_date);
        editReason = (EditText) view.findViewById(R.id.edit_reason);
        tvRequestDate = (TextView) view.findViewById(R.id.tv_request_date);
        tvStatus = (TextView) view.findViewById(R.id.tv_status);
        tvCancelDate = (TextView) view.findViewById(R.id.tv_cancel_date);
        layoutStatus = (LinearLayout) view.findViewById(R.id.layout_status);
        layoutCancelDate = (LinearLayout) view.findViewById(R.id.layout_cancel_date);

        layoutStatus.setVisibility(View.GONE);
        layoutCancelDate.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (selectedPatient != null)
            tvPatientName.setText(selectedPatient.getLocalPatientName());

        if (leaveFromScreenType != null)
            switch (leaveFromScreenType) {
                case ADD_NEW:
                    initActionbarTitle(getResources().getString(R.string.add_leave));
                    tvRequestDate.setText(DateTimeUtil.getCurrentFormattedDate(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH));
                    break;
                case RESCHEDULE:
                    initActionbarTitle(getResources().getString(R.string.edit_leave));
                    tvPatientName.setText(leave.getLocalPatientName());
                    tvLeavetype.setText(leave.getLeaveType());
                    tvFromDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, leave.getFromDate()));
                    tvFromDate.setTag(leave.getFromDate());
                    tvToDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, leave.getToDate()));
                    tvToDate.setTag(leave.getToDate());
                    tvRequestDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, leave.getRequestDate()));
                    tvRequestDate.setTag(leave.getRequestDate());
                    editReason.setText(leave.getReason());
                    if (!Util.isNullOrBlank(leave.getStatus())) {
                        tvStatus.setText(leave.getStatus());
                        tvCancelDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, leave.getCancelledDate()));
                        tvCancelDate.setTag(leave.getCancelledDate());
                        layoutStatus.setVisibility(View.VISIBLE);
                        layoutCancelDate.setVisibility(View.VISIBLE);
                    }
                    break;
                case VIEW:
                    initActionbarTitle(getResources().getString(R.string.leave));
                    tvPatientName.setText(leave.getLocalPatientName());
                    tvLeavetype.setText(leave.getLeaveType());
                    tvFromDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, leave.getFromDate()));
                    tvToDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, leave.getToDate()));
                    tvRequestDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, leave.getRequestDate()));
                    editReason.setText(leave.getReason());
                    if (!Util.isNullOrBlank(leave.getStatus())) {
                        tvStatus.setText(leave.getStatus());
                        tvCancelDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, leave.getRequestDate()));
                        layoutStatus.setVisibility(View.VISIBLE);
                        layoutCancelDate.setVisibility(View.VISIBLE);
                    }
                    editReason.setEnabled(false);
                    tvLeavetype.setEnabled(false);
                    tvFromDate.setEnabled(false);
                    tvToDate.setEnabled(false);
                    break;
            }
    }

    private void openDatePickerDialog(final TextView textView, long minDate) {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, calendar, textView);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                long selectedFromDateTimeMillis = getSelectedFromDateTime(year, monthOfYear, dayOfMonth);
                textView.setText(DateTimeUtil.getFormattedTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, year, monthOfYear, dayOfMonth, 0, 0, 0));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(minDate);/*new Date().getTime() - 10000);*/
        datePickerDialog.show();
    }

    private long getSelectedFromDateTime(int year, int month, int day) {
        Calendar calendar1 = DateTimeUtil.getCalendarInstance();
        calendar1.set(Calendar.YEAR, year);
        calendar1.set(Calendar.MONTH, month);
        calendar1.set(Calendar.DAY_OF_MONTH, day);
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
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId())) {
                        initData();
                    }
                    break;
                case ADD_LEAVE:
                    Util.showToast(mActivity, R.string.leave_added);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_ADD_NEW_LEAVE, null);
                    mActivity.hideLoading();
                    dismiss();
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
                    if (!Util.isNullOrBlank(leaveId))
                        leave = LocalDataServiceImpl.getInstance(mApp).getLeave(leaveId);
                    if (leave != null)
                        selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(leave.getPatientId());
                    else
                        selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                }
                break;
            case ADD_LEAVE:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.ADD_LEAVE);

                LocalDataServiceImpl.getInstance(mApp).addLeave(leaveToSend);
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
            case R.id.tv_from_date:
                openDatePickerDialog((TextView) v, new Date().getTime() - 10000);
                break;
            case R.id.tv_to_date:
                if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvFromDate))) {
                    Calendar minDate = DateTimeUtil.setCalendarDefaultvalue(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, DateTimeUtil.getCalendarInstance(), tvFromDate);
                    openDatePickerDialog((TextView) v, minDate.getTimeInMillis());
                } else {
                    Util.showAlert(mActivity, R.string.please_select_from_date_first);
                }
                break;
            case R.id.bt_save:
                if (user != null) {
                    validateData();
                }
                break;
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;

        if (selectedPatient == null && Util.isNullOrBlank(Util.getValidatedValueOrNull(tvPatientName))) {
            msg = getResources().getString(R.string.please_select_patient_or_add_new);
            errorViewList.add(tvPatientName);
        } else if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvFromDate))) {
            msg = getResources().getString(R.string.please_select_from_date);
            errorViewList.add(tvFromDate);
        } else if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvToDate))) {
            msg = getResources().getString(R.string.please_select_to_date);
            errorViewList.add(tvToDate);
        }
        if (Util.isNullOrBlank(msg))
            addAppointment();
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addAppointment() {
        mActivity.showLoading(false);
        leaveToSend = new Leave();
        leaveToSend.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);

        if (!Util.isNullOrBlank(leaveId)) {
            leaveToSend.setDoctorId(leave.getDoctorId());
            leaveToSend.setHospitalId(leave.getHospitalId());
            leaveToSend.setLocationId(leave.getLocationId());
            leaveToSend.setCreatedBy(leave.getCreatedBy());
        } else {
            leaveToSend.setDoctorId(user.getUniqueId());
            leaveToSend.setHospitalId(user.getForeignHospitalId());
            leaveToSend.setLocationId(user.getForeignLocationId());
            leaveToSend.setCreatedBy(user.getFirstName());
        }
        leaveToSend.setLocalPatientName(Util.getValidatedValueOrNull(tvPatientName));
        leaveToSend.setReason(Util.getValidatedValueOrNull(editReason));
        leaveToSend.setLeaveType(Util.getValidatedValueOrNull(tvLeavetype));
        leaveToSend.setStatus(Util.getValidatedValueOrNull(tvStatus));
        leaveToSend.setFromDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(
                DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, Util.getValidatedValueOrNull(tvFromDate)));
        leaveToSend.setToDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(
                DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, Util.getValidatedValueOrNull(tvToDate)));
        leaveToSend.setRequestDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(
                DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, Util.getValidatedValueOrNull(tvRequestDate)));

        if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvCancelDate)))
            leaveToSend.setCancelledDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(
                    DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, Util.getValidatedValueOrNull(tvCancelDate)));


        if (!Util.isNullOrBlank(leaveId)) {
            leaveToSend.setUniqueId(leaveId);
            leaveToSend.setCreatedTime(leave.getCreatedTime());
        } else {
            leaveToSend.setUniqueId(DateTimeUtil.getCurrentDateLong().toString());
            leaveToSend.setCreatedTime(DateTimeUtil.getCurrentDateLong());
        }
        if (leaveToSend != null && !Util.isNullOrBlank(leaveToSend.getLeaveId())) {
            leaveToSend.setLeaveId(leave.getLeaveId());
        } else {
            leaveToSend.setLeaveId(Util.generateKey("L"));
        }
        leaveToSend.setUpdatedTime(DateTimeUtil.getCurrentDateLong());

        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_LEAVE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case DOCTOR_LIST:
                if (object instanceof RegisteredDoctorProfile) {
                    RegisteredDoctorProfile doctorProfile = (RegisteredDoctorProfile) object;
//                    tvDoctorName.setText(doctorProfile.getFirstNameWithTitle());
//                    user.setUniqueId(doctorProfile.getUserId());
                }
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }


}
