package com.healthcoco.healthcocopad.dialogFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.ResetPassword;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.Locale;

/**
 * Created by Shreshtha on 23-01-2017.
 */

public class ForgotPasswordDialogFragment extends HealthCocoDialogFragment implements
        View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, TextWatcher {

    private EditText editEmailAddress;
    private String emailAddress;
    private TextView btReset;
    private TextView btCancel;

    public ForgotPasswordDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.dialog_fragment_forgot_password, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        Bundle b = getArguments();
        if (b != null) {
            emailAddress = b.getString(HealthCocoConstants.TAG_EMAIL_ID);
            init();
        }
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
    }

    @Override
    public void initViews() {
        editEmailAddress = (EditText) view.findViewById(R.id.edit_email_address);
        btReset = (TextView) view.findViewById(R.id.bt_reset);
        btCancel = (TextView) view.findViewById(R.id.bt_cancel);
//        initSaveCancelFontAwesomeTextView(R.string.reset, this);
    }

    @Override
    public void initListeners() {
        btCancel.setOnClickListener(this);
        editEmailAddress.addTextChangedListener(this);
        btReset.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (!Util.isNullOrBlank(emailAddress)) {
            enableSaveButton(true);
            editEmailAddress.setText(Util.getValidatedValue(emailAddress));
        } else
            enableSaveButton(false);
        editEmailAddress.setSelection(editEmailAddress.getText().length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reset:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    validateData();
                else
                    Util.showToast(mActivity, getResources().getString(R.string.user_offline));
                break;
            case R.id.bt_cancel:
                getDialog().cancel();
                break;
            default:
                break;
        }
    }

    private void validateData() {
        String msg = null;
        String emailId = String.valueOf(editEmailAddress.getText());
        if (Util.isNullOrBlank(emailId))
            msg = getResources().getString(R.string.please_enter_email_address);
        else if (!Util.isValidEmail(emailId))
            msg = getResources().getString(R.string.please_enter_valid_email_address);
        if (!Util.isNullOrBlank(msg)) {
            Util.showToast(mActivity, msg);
        } else {
            sendEmail(emailId);
        }
    }

    private void sendEmail(String emailId) {
        mActivity.showLoading(false);
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setEmailAddress(emailId);
        resetPassword.setUsername(emailId);
        WebDataServiceImpl.getInstance(mApp).addUpdateCommonMethod(WebServiceType.RESET_PASSWORD, ResetPassword.class, resetPassword, this, this);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            switch (response.getWebServiceType()) {
                case RESET_PASSWORD:
                    Util.showToast(mActivity, getResources().getString(R.string.email_sent_to) + Util.getValidatedValueOrNull(editEmailAddress));
                    dismiss();
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = String.valueOf(s).toLowerCase(Locale.ENGLISH);
        if (Util.isNullOrBlank(text))
            enableSaveButton(false);
        else
            enableSaveButton(true);
    }

    private void enableSaveButton(boolean isEnabled) {
        btReset.setClickable(isEnabled);
        btReset.setEnabled(isEnabled);
    }
}
