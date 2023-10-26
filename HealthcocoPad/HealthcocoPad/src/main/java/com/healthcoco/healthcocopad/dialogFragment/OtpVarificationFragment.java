package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.OtpVerification;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Prashant on 11-08-2018.
 */
public class OtpVarificationFragment extends HealthCocoDialogFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener {

    private User user;
    private EditText editFirstDigit;
    private EditText editSecondDigit;
    private EditText editThirdDigit;
    private EditText editForthDigit;
    private EditText editFifthDigit;
    private EditText editSixthDigit;

    private TextView tvVerify;
    private TextView tvCancel;
    private TextView tvResend;
    private TextView tvEnterOtp;

    private String patientId;
    private String patientMobile;

    private Button btSave;
    private ImageButton btCross;
    private TextView tvTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_otp_varification, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            patientId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
            patientMobile = bundle.getString(HealthCocoConstants.TAG_MOBILE_NUMBER);
        }
        if (!Util.isNullOrBlank(patientMobile)) {
            init();
//            setWidthHeight(0.80, 0.90);
            generateOtp();
        }
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        editFirstDigit = (EditText) view.findViewById(R.id.edit_first_digit);
        editSecondDigit = (EditText) view.findViewById(R.id.edit_second_digit);
        editThirdDigit = (EditText) view.findViewById(R.id.edit_third_digit);
        editForthDigit = (EditText) view.findViewById(R.id.edit_forth_digit);
        editFifthDigit = (EditText) view.findViewById(R.id.edit_fifth_digit);
        editSixthDigit = (EditText) view.findViewById(R.id.edit_sixth_digit);

        tvVerify = (TextView) view.findViewById(R.id.tv_verify);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvResend = (TextView) view.findViewById(R.id.tv_resend);
        tvEnterOtp = (TextView) view.findViewById(R.id.tv_enter_otp);

        btCross = (ImageButton) view.findViewById(R.id.bt_cross);
        btSave = (Button) view.findViewById(R.id.bt_save);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);

        tvTitle.setText(getString(R.string.otp_verification));
        btSave.setVisibility(View.INVISIBLE);
        btCross = (ImageButton) view.findViewById(R.id.bt_cross);

        if (!Util.isNullOrBlank(patientMobile))
            tvEnterOtp.setText(String.format(getResources().getString(R.string.please_enter_the_otp_sent_to_your_phone), patientMobile));
    }

    @Override
    public void initListeners() {

        editFirstDigit.addTextChangedListener(new GenericTextWatcher(editFirstDigit));
        editSecondDigit.addTextChangedListener(new GenericTextWatcher(editSecondDigit));
        editThirdDigit.addTextChangedListener(new GenericTextWatcher(editThirdDigit));
        editForthDigit.addTextChangedListener(new GenericTextWatcher(editForthDigit));
        editFifthDigit.addTextChangedListener(new GenericTextWatcher(editFifthDigit));
        editSixthDigit.addTextChangedListener(new GenericTextWatcher(editSixthDigit));

        tvVerify.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvResend.setOnClickListener(this);
        btCross.setOnClickListener(this);
    }

    @Override
    public void initData() {

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
                case VERIFY_OTP:
                    getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_FEEDBACK_OTP,
                            HealthCocoConstants.RESULT_CODE_FEEDBACK_OTP, null);
                    dismiss();
                    Util.showToast(mActivity, R.string.verification_complete);
                    break;
                case GENERATE_OTP:
                    Util.showToast(mActivity, R.string.otp_success);
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void generateOtp() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).generateOtp(OtpVerification.class, patientMobile, this, this);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
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
            case R.id.tv_verify:
                validateData();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.bt_cross:
                dismiss();
                break;
            case R.id.tv_resend:
                generateOtp();
                break;
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;


        String otp = Util.getValidatedValueOrBlankWithoutTrimming(editFirstDigit) +
                Util.getValidatedValueOrBlankWithoutTrimming(editSecondDigit) +
                Util.getValidatedValueOrBlankWithoutTrimming(editThirdDigit) +
                Util.getValidatedValueOrBlankWithoutTrimming(editForthDigit) +
                Util.getValidatedValueOrBlankWithoutTrimming(editFifthDigit) +
                Util.getValidatedValueOrBlankWithoutTrimming(editSixthDigit);

        if (Util.isNullOrBlank(otp) || otp.length() < 6) {
            msg = getResources().getString(R.string.please_enter_valid_otp);
            errorViewList.add(editSixthDigit);
        }
        if (Util.isNullOrBlank(msg))
            verifyOtp(otp);
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void verifyOtp(String otpNumber) {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).verifyOtp(OtpVerification.class,
                patientMobile, otpNumber, this, this);
    }


    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.edit_first_digit:
                    if (text.length() == 1)
                        editSecondDigit.requestFocus();
                    break;
                case R.id.edit_second_digit:
                    if (text.length() == 1)
                        editThirdDigit.requestFocus();
                    if (text.length() == 0)
                        editFirstDigit.requestFocus();
                    break;
                case R.id.edit_third_digit:
                    if (text.length() == 1)
                        editForthDigit.requestFocus();
                    if (text.length() == 0)
                        editSecondDigit.requestFocus();
                    break;
                case R.id.edit_forth_digit:
                    if (text.length() == 1)
                        editFifthDigit.requestFocus();
                    if (text.length() == 0)
                        editThirdDigit.requestFocus();
                    break;
                case R.id.edit_fifth_digit:
                    if (text.length() == 1)
                        editSixthDigit.requestFocus();
                    if (text.length() == 0)
                        editForthDigit.requestFocus();
                    break;
                case R.id.edit_sixth_digit:
                    if (text.length() == 0)
                        editFifthDigit.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

}
