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
import com.healthcoco.healthcocopad.bean.server.Leave;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.ReferDoctor;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LeaveFromScreenType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.HealthcocoPopupWindow;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
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
public class AddReferalDialogFragment extends HealthCocoDialogFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, PopupWindowListener {

    public static final String TAG_FROM_SCREEN_TYPE = "isFromEMRFragment";
    public List<RegisteredDoctorProfile> registeredDoctorProfileList;
    private TextView tvPatientName;
    private TextView tvDate;
    private EditText editDetails;
    private TextView tvReferredBy;
    private TextView tvReferredTo;
    private TextView tvRequestDate;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private ReferDoctor referDoctor;
    private ReferDoctor referDoctorToSend;
    private String referId;
    private LeaveFromScreenType leaveFromScreenType;
    private HealthcocoPopupWindow doctorsListPopupWindow;
    private LinkedHashMap<String, RegisteredDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_doctor_referal, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int ordinal = Parcels.unwrap(bundle.getParcelable(TAG_FROM_SCREEN_TYPE));
            referId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
            leaveFromScreenType = LeaveFromScreenType.values()[ordinal];
        }
        if (leaveFromScreenType != null)
            init();
        setWidthHeight(0.80, 0.90);

        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {

        tvPatientName = (TextView) view.findViewById(R.id.tv_patient_name);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvReferredTo = (TextView) view.findViewById(R.id.tv_referred_to);
        tvReferredBy = (TextView) view.findViewById(R.id.tv_referred_by);
        editDetails = (EditText) view.findViewById(R.id.edit_Details);
        tvRequestDate = (TextView) view.findViewById(R.id.tv_request_date);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        tvDate.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (selectedPatient != null)
            tvPatientName.setText(selectedPatient.getLocalPatientName());

        if (leaveFromScreenType != null)
            switch (leaveFromScreenType) {
                case ADD_NEW:
                    initActionbarTitle(getResources().getString(R.string.add_refer_to_doctor));
                    tvRequestDate.setText(DateTimeUtil.getCurrentFormattedDate(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH));
                    tvReferredBy.setText("Dr. " + user.getFirstName());
                    break;
                case RESCHEDULE:
                    initActionbarTitle(getResources().getString(R.string.add_refer_to_doctor));
                    tvPatientName.setText(referDoctor.getLocalPatientName());
                    tvDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, referDoctor.getDate()));
                    tvRequestDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, referDoctor.getRequestDate()));
                    tvReferredBy.setText(referDoctor.getReferredBy());
                    tvReferredTo.setText(referDoctor.getReferredTo());
                    editDetails.setText(referDoctor.getDetails());
                    break;
                case VIEW:
                    initActionbarTitle(getResources().getString(R.string.add_refer_to_doctor));
                    tvPatientName.setText(referDoctor.getLocalPatientName());
                    tvDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, referDoctor.getDate()));
                    tvRequestDate.setText(DateTimeUtil.getFormattedDateTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, referDoctor.getRequestDate()));
                    tvReferredBy.setText(referDoctor.getReferredBy());
                    tvReferredTo.setText(referDoctor.getReferredTo());
                    editDetails.setText(referDoctor.getDetails());
                    editDetails.setEnabled(false);
                    tvReferredTo.setEnabled(false);
                    tvReferredBy.setEnabled(false);
                    tvDate.setEnabled(false);
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
                    if (!Util.isNullOrEmptyList(registeredDoctorProfileList))
                        fromHashMapAndRefresh(registeredDoctorProfileList);
                    break;
                case ADD_LEAVE:
                    Util.showToast(mActivity, R.string.submitted);
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
                    if (!Util.isNullOrBlank(referId))
                        referDoctor = LocalDataServiceImpl.getInstance(mApp).getReferDoctor(referId);
                    if (referDoctor != null)
                        selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(referDoctor.getPatientId());
                    else
                        selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                    registeredDoctorProfileList = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorDetails(user.getForeignLocationId());
                }
                break;
            case ADD_LEAVE:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.ADD_LEAVE);

                LocalDataServiceImpl.getInstance(mApp).addReferDoctor(referDoctorToSend);
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
            case R.id.tv_date:
                openDatePickerDialog((TextView) v, new Date().getTime() - 10000);
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
        } else if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvDate))) {
            msg = getResources().getString(R.string.please_select_from_date);
            errorViewList.add(tvDate);
        } else if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvReferredBy))) {
            msg = getResources().getString(R.string.please_select_refferred_by_doctor);
            errorViewList.add(tvReferredBy);
        } else if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvReferredTo))) {
            msg = getResources().getString(R.string.please_select_refferred_to_doctor);
            errorViewList.add(tvReferredTo);
        } else if (Util.isNullOrBlank(Util.getValidatedValueOrNull(editDetails))) {
            msg = getResources().getString(R.string.please_write_details);
            errorViewList.add(editDetails);
        }
        if (Util.isNullOrBlank(msg))
            addReferDoctor();
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addReferDoctor() {
        mActivity.showLoading(false);
        referDoctorToSend = new ReferDoctor();
        referDoctorToSend.setCreatedBy(user.getFirstName());
        referDoctorToSend.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);

        if (!Util.isNullOrBlank(referId)) {
            referDoctorToSend.setDoctorId(referDoctor.getDoctorId());
            referDoctorToSend.setHospitalId(referDoctor.getHospitalId());
            referDoctorToSend.setLocationId(referDoctor.getLocationId());
            referDoctorToSend.setCreatedBy(referDoctor.getCreatedBy());
        } else {
            referDoctorToSend.setDoctorId(user.getUniqueId());
            referDoctorToSend.setHospitalId(user.getForeignHospitalId());
            referDoctorToSend.setLocationId(user.getForeignLocationId());
            referDoctorToSend.setCreatedBy(user.getFirstName());
        }
        referDoctorToSend.setLocalPatientName(Util.getValidatedValueOrNull(tvPatientName));
        referDoctorToSend.setDetails(Util.getValidatedValueOrNull(editDetails));
        referDoctorToSend.setReferredBy(Util.getValidatedValueOrNull(tvReferredBy));
        referDoctorToSend.setReferredTo(Util.getValidatedValueOrNull(tvReferredTo));
        referDoctorToSend.setDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(
                DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, Util.getValidatedValueOrNull(tvDate)));
        referDoctorToSend.setRequestDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(
                DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, Util.getValidatedValueOrNull(tvRequestDate)));

        if (!Util.isNullOrBlank(referId)) {
            referDoctorToSend.setUniqueId(referId);
            referDoctorToSend.setCreatedTime(referDoctor.getCreatedTime());
        } else {
            referDoctorToSend.setUniqueId(DateTimeUtil.getCurrentDateLong().toString());
            referDoctorToSend.setCreatedTime(DateTimeUtil.getCurrentDateLong());
        }
        if (referDoctorToSend != null && !Util.isNullOrBlank(referDoctorToSend.getReferId())) {
            referDoctorToSend.setReferId(referDoctor.getReferId());
        } else {
            referDoctorToSend.setReferId(Util.generateKey("R"));
        }
        referDoctorToSend.setUpdatedTime(DateTimeUtil.getCurrentDateLong());

        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_LEAVE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case DOCTOR_LIST:
                if (object instanceof RegisteredDoctorProfile) {
                    RegisteredDoctorProfile doctorProfile = (RegisteredDoctorProfile) object;
                    tvReferredTo.setText(doctorProfile.getFirstNameWithTitle());
//                    tvReferredTo.setTag(doctorProfile.getUserId());
                }
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }


    private void fromHashMapAndRefresh(List<RegisteredDoctorProfile> responseList) {
        if (responseList.size() > 1) {
            tvReferredTo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_drug, 0);
            tvReferredTo.setEnabled(true);

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
                mActivity.initPopupWindows(tvReferredTo, PopupWindowType.DOCTOR_LIST, new ArrayList<Object>(clinicDoctorListHashMap.values()), this);
            }
        } else {
            tvReferredTo.setText("Dr. " + user.getFirstName());
        }
    }

}
