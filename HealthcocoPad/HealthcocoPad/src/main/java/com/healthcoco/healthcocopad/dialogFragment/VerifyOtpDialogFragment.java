package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.OtpVerification;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 11/02/16.
 */
public class VerifyOtpDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvMessage;
    private Button btVerify;
    private EditText editOtpNumber;
    private Button btRegenerateOtp;
    private Handler handlerEnableDisableOtp;
    private Runnable runnableEnableDisableOtp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_verify_otp, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setWidthHeight(0.60, 0.50);
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
        editOtpNumber = (EditText) view.findViewById(R.id.edit_otp_number);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        btVerify = (Button) view.findViewById(R.id.bt_verify);
        btRegenerateOtp = (Button) view.findViewById(R.id.bt_regenerate_otp);
    }

    @Override
    public void initListeners() {
        btVerify.setOnClickListener(this);
        btRegenerateOtp.setOnClickListener(this);
        initCrossButton();
    }

    @Override
    public void initData() {
        tvMessage.setText(getResources().getString(R.string.otp_sent_to) + selectedPatient.getMobileNumber() + ".");
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
        btRegenerateOtp.setEnabled(b);
        btRegenerateOtp.setClickable(b);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_verify) {
            validateData();
        } else if (id == R.id.bt_regenerate_otp) {
            generateOtp();
        }
    }

    private void validateData() {
        String msg = "";
        String otpNumber = Util.getValidatedValueOrNull(editOtpNumber);

        if (Util.isNullOrBlank(otpNumber)) {
            msg = getResources().getString(R.string.please_enter_otp);
        }

        if (Util.isNullOrBlank(msg))
            verifyOtp(otpNumber);
        else
            Util.showToast(mActivity, msg);
    }

    private void verifyOtp(String otpNumber) {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).verifyOtp(OtpVerification.class,
                selectedPatient.getDoctorId(), selectedPatient.getLocationId(),
                selectedPatient.getHospitalId(), selectedPatient.getUserId(), otpNumber, this, this);
    }

    private void generateOtp() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).generateOtp(OtpVerification.class,
                selectedPatient.getDoctorId(), selectedPatient.getLocationId(),
                selectedPatient.getHospitalId(), selectedPatient.getUserId(), this, this);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        LocalDataServiceImpl.getInstance(mApp).addUpdateGeneratedOtpTime(selectedPatient.getUserId());
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
            case VERIFY_OTP:
                LocalDataServiceImpl.getInstance(mApp).addUpdateGeneratedOtpTime(selectedPatient.getUserId());
                getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_VERIFY_OTP, HealthCocoConstants.RESULT_CODE_VERIFY_OTP, null);
                dismiss();
                Util.showToast(mActivity, R.string.verification_complete);
                break;
            case GENERATE_OTP:
                LocalDataServiceImpl.getInstance(mApp).addUpdateGeneratedOtpTime(selectedPatient.getUserId());
                Util.showToast(mActivity, R.string.otp_success);
                enableGenerateButton(false);
                startHandlerToEnable(GlobalRecordAccessDialogFragment.DEFAULT_ENABLE_DISABLE_TIME);
                break;
        }
        mActivity.hideLoading();
    }
}
