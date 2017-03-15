package com.healthcoco.healthcocoplus.dialogFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoDialogFragment;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.OtpVerification;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.DateTimeUtil;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.Util;

/**
 * Created by neha on 10/02/16.
 */
public class GlobalRecordAccessDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    public static final int DEFAULT_ENABLE_DISABLE_TIME = 60;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvMessage;
    private Button btGenerateOtp;
    private Handler handlerEnableDisableOtp;
    private Runnable runnableEnableDisableOtp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_global_record_access, container, false);
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
        selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (selectedPatient != null) {
            initViews();
            initListeners();
            initData();
        }
    }

    @Override
    public void initViews() {
        initActionbarTitle(getResources().getString(R.string.global_record_access));
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        btGenerateOtp = (Button) view.findViewById(R.id.bt_generate_otp);
    }

    @Override
    public void initListeners() {
        btGenerateOtp.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tvMessage.setText(getResources().getString(R.string.alert_global_record_access) + selectedPatient.getLocalPatientName() + ".");
        long lastOtpGeneratedTime = LocalDataServiceImpl.getInstance(mApp).getLastGeneratedOtpTime(selectedPatient.getUserId());
        long lastOtpGeneratedTimeInSeconds = DateTimeUtil.getDifferenceInSecondsFromCurrent(lastOtpGeneratedTime);
        if (lastOtpGeneratedTimeInSeconds > 0 && lastOtpGeneratedTimeInSeconds < DEFAULT_ENABLE_DISABLE_TIME) {
            dismissThisDailogAndOpenVerifyOtpDialog();
        } else {
            enableGenerateButton(true);
        }
    }

    private void startHandlerToEnable(long secondsRemaining) {
        handlerEnableDisableOtp = new Handler();
        runnableEnableDisableOtp = new Runnable() {

            @Override
            public void run() {
                enableGenerateButton(true);
            }
        };
        handlerEnableDisableOtp.postDelayed(runnableEnableDisableOtp, secondsRemaining * 1000);
    }

    private void enableGenerateButton(boolean b) {
        btGenerateOtp.setEnabled(b);
        btGenerateOtp.setClickable(b);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_generate_otp:
//                getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_GLOBAL_RECORDS_ACCESS,
//                        HealthCocoConstants.RESULT_CODE_GLOBAL_RECORDS_ACCESS, null);
//                dismiss();
                generateOtp();
                break;
        }
    }

    private void generateOtp() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).generateOtp(OtpVerification.class,
                selectedPatient.getDoctorId(), selectedPatient.getLocationId(),
                selectedPatient.getHospitalId(), selectedPatient.getUserId(), this, this);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
//        LocalDataServiceImpl.getInstance(mApp).addUpdateGeneratedOtpTime(selectedPatient.getUserId());
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        mActivity.hideLoading();
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case GENERATE_OTP:
                LocalDataServiceImpl.getInstance(mApp).addUpdateGeneratedOtpTime(selectedPatient.getUserId());
                Util.showToast(mActivity, R.string.otp_success);
                dismissThisDailogAndOpenVerifyOtpDialog();
                break;
        }
        mActivity.hideLoading();
    }

    private void dismissThisDailogAndOpenVerifyOtpDialog() {
        getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_GLOBAL_RECORDS_ACCESS,
                HealthCocoConstants.RESULT_CODE_GLOBAL_RECORDS_ACCESS, null);
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (handlerEnableDisableOtp != null && runnableEnableDisableOtp != null)
            handlerEnableDisableOtp.removeCallbacks(runnableEnableDisableOtp);
    }
}
