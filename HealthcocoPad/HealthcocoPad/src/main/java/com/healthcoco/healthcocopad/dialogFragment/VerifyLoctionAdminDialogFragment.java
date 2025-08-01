package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.UserVerification;
import com.healthcoco.healthcocopad.bean.server.KioskPin;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewSuggestionListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Prashant on 03-01-2018.
 */

public class VerifyLoctionAdminDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    public static final String TAG_SGGESTION = "suggestionTag";
    private EditText editPassword;
    private EditText editPin;
    private EditText editEmail;
    private TextView tvTitle;
    private LinearLayout layoutPassword;
    private LoginResponse doctor;
    private Bundle bundle;
    private User user;
    private String uniqueId;
    private SuggestionType suggestionType;
    private String editedSuggestion;


    public VerifyLoctionAdminDialogFragment(SuggestionType suggestionType) {
        this.suggestionType = suggestionType;
    }

    public VerifyLoctionAdminDialogFragment(AddNewSuggestionListener addNewSuggestionListener) {
        this.suggestionType = addNewSuggestionListener.getSuggestionType();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_verify_admin_doctor, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bundle = getArguments();
        if (bundle != null) {
            uniqueId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
            editedSuggestion = bundle.getString(TAG_SGGESTION);
        }
        init();
    }

    @Override
    public void init() {
        doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initData();
        }
    }

    @Override
    public void initViews() {
        editPassword = (EditText) view.findViewById(R.id.edit_password);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        editPin = (EditText) view.findViewById(R.id.edit_pin);
        editEmail = (EditText) view.findViewById(R.id.edit_email);
        layoutPassword = (LinearLayout) view.findViewById(R.id.layout_password);
        tvTitle.setText(getString(R.string.verification));
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this, getString(R.string.save));
    }

    @Override
    public void initData() {
        switch (suggestionType) {
            case PASSWORD:
                layoutPassword.setVisibility(View.VISIBLE);
                editPin.setVisibility(View.GONE);
                editPassword.setVisibility(View.VISIBLE);
                editEmail.setVisibility(View.VISIBLE);
                initSaveCancelButton(this, getString(R.string.submit));
                break;
            case PIN:
                layoutPassword.setVisibility(View.VISIBLE);
                editPin.setVisibility(View.VISIBLE);
                editPassword.setVisibility(View.GONE);
                initSaveCancelButton(this, getString(R.string.submit));
                break;
            default:
                layoutPassword.setVisibility(View.GONE);
                editPin.setVisibility(View.GONE);
                editPassword.setVisibility(View.GONE);
                initListeners();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_save) {
            validateData();
        }
    }

    private void validateData() {
        String msg = null;
        String suggestion = "";
        String username = "";
        switch (suggestionType) {
            case PASSWORD:
                suggestion = String.valueOf(editPassword.getText());
                username = String.valueOf(editEmail.getText());
                if (Util.isNullOrBlank(username)) {
                    msg = getResources().getString(R.string.please_enter_email_address);
                }
                if (!Util.isValidEmail(username)) {
                    msg = getResources().getString(R.string.please_enter_valid_email_address);
                }
                if (Util.isNullOrBlank(suggestion)) {
                    msg = getResources().getString(R.string.please_enter_password);
                }
                break;
            case PIN:
                username = String.valueOf(editPassword.getText());
                suggestion = String.valueOf(editPin.getText());
                if (Util.isNullOrBlank(suggestion)) {
                    msg = getResources().getString(R.string.please_enter_pin);
                }
                if (Util.isNullOrBlank(username)) {
                    msg = getResources().getString(R.string.please_enter_email_address);
                }
                if (Util.isValidEmail(username)) {
                    msg = getResources().getString(R.string.please_enter_valid_email_address);
                }
                break;
        }
        if (Util.isNullOrBlank(msg)) {
            verifyUser(suggestion);
        } else
            Util.showToast(mActivity, msg);
    }

    private void verifyUser(String suggestion) {
        switch (suggestionType) {
            case PASSWORD:
                checkIsLocationAdmin(String.valueOf(editEmail.getText()), suggestion, doctor.getUser().getForeignLocationId());
                break;
            case PIN:
                changePin(String.valueOf(editEmail.getText()), suggestion);
                break;
        }
    }


    private void checkIsLocationAdmin(String userName, String password, String locationId) {
        mActivity.showLoading(false);
        UserVerification userVerification = new UserVerification();
        userVerification.setUsername(userName);
        userVerification.setPassword(Util.getSHA3SecurePassword(password));
        userVerification.setLocationId(locationId);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(UserVerification.class, WebServiceType.IS_LOCATION_ADMIN, userVerification, this, this);
//        isLocationAdmin(UserVerification.class, userVerification, this, this);
    }

    private void changePin(String doctorId, String pin) {
        mActivity.showLoading(false);
        KioskPin kioskPin = new KioskPin();
        kioskPin.setPin(pin);
        kioskPin.setDoctorId(doctorId);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(KioskPin.class, WebServiceType.ADD_EDIT_PIN, kioskPin, this, this);
    }


    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        Util.showToast(mActivity, errorMsg);
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case ADD_EDIT_PIN:
                if (response.isValidData(response)) {
                    LocalDataServiceImpl.getInstance(mApp).
                            addKioskPin(response.getData());
                    Util.showToast(mActivity, getString(R.string.pin_successfully_changed));
                }
                break;
            case IS_LOCATION_ADMIN:
                if (response.isValidData(response)) {
                    boolean isAdmin = (boolean) response.getData();
                    mActivity.hideLoading();
                    if (isAdmin) {
                        getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_LOCATION_ADMIN, HealthCocoConstants.RESULT_CODE_LOCATION_ADMIN, null);
                        dismiss();
                    } else {
                        Util.showToast(mActivity, getString(R.string.please_enter_valid_email_password));
                        return;
                    }
                }
                break;
        }
        mActivity.hideLoading();

        getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, HealthCocoConstants.RESULT_CODE_REFERENCE_LIST, null);

        dismiss();

    }


}
