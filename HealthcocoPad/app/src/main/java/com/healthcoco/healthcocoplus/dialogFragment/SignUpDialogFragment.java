package com.healthcoco.healthcocoplus.dialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoDialogFragment;
import com.healthcoco.healthcocoplus.activities.CommonActivity;
import com.healthcoco.healthcocoplus.activities.HomeActivity;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.enums.WebViewType;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Shreshtha on 19-01-2017.
 */

public class SignUpDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    private View view;
    private Button bt_cancel;
    private EditText editUserName;
    private EditText editPassword;
    private Button btLogin;
    private Button btForgotPassword;
    private TextView tvTermsOfService;
    private TextView tvPrivacyPolicy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view = inflater.inflate(R.layout.dialog_fragment_login, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
//        if (DevConfig.PRE_FILLED_FORM) {
//            preFillForm();
//        }
    }

    public void init() {
        initViews();
        initListeners();
        initData();
    }

    private void preFillForm() {
        editUserName.setText("abhijeet.shukla@healthcoco.com");
        editPassword.setText("neha1234");
    }

    public void initListeners() {
        bt_cancel.setOnClickListener(this);
        btForgotPassword.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        tvTermsOfService.setOnClickListener(this);
        tvPrivacyPolicy.setOnClickListener(this);
    }

    public void initData() {

    }

    public void initViews() {
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        editUserName = (EditText) view.findViewById(R.id.edit_username);
        editPassword = (EditText) view.findViewById(R.id.edit_password);
        btLogin = (Button) view.findViewById(R.id.bt_login);
        btForgotPassword = (Button) view.findViewById(R.id.bt_forgot_password);
        tvTermsOfService = (TextView) view.findViewById(R.id.tv_terms_of_service);
        tvPrivacyPolicy = (TextView) view.findViewById(R.id.tv_privacy_policy);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                getDialog().cancel();
                break;
            case R.id.bt_login:
                break;
            case R.id.tv_terms_of_service:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.TERMS_OF_SERVICE, WebViewType.TERMS_OF_SERVICE.ordinal(), 0);
                break;
            case R.id.tv_privacy_policy:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.PRIVACY_POLICY, WebViewType.PRIVACY_POLICY.ordinal(), 0);
                break;
            default:
                break;
        }
    }

//    private void validateData() {
//        ArrayList<View> errorViewList = new ArrayList<>();
//        String userName = Util.getValidatedValueOrNull(editUserName);
//        String password = Util.getValidatedValueOrBlankWithoutTrimming(editPassword);
//        String msg = null;
//        if (Util.isNullOrBlank(userName) && Util.isNullOrBlank(password)) {
//            msg = getResources().getString(R.string.please_enter_email_id_And_password_to_login);
//            errorViewList.add(editUserName);
//            errorViewList.add(editPassword);
//        } else if (Util.isNullOrBlank(userName)) {
//            msg = getResources().getString(R.string.please_enter_email_id_to_login);
//            errorViewList.add(editUserName);
//        } else if (!Util.isValidEmail(userName)) {
//            msg = getResources().getString(R.string.please_enter_valid_email_address);
//            errorViewList.add(editUserName);
//        } else if (Util.isNullOrBlank(password)) {
//            errorViewList.add(editPassword);
//            msg = getResources().getString(R.string.please_enter_password_to_login);
//        }
//        if (Util.isNullOrBlank(msg))
//            loginUser(userName, password);
//        else {
//            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
//        }
//    }
//
//    private void loginUser(String userName, String password) {
//        User user = new User();
//        user.setUsername(userName);
//        user.setPassword(Util.getSHA3SecurePassword(password));
//        WebDataServiceImpl.getInstance(mApp).loginUser(LoginResponse.class, user, this, this);
//    }

    /**
     * @param fragmentType
     * @param intentData
     * @param requestCode
     */
    private void openCommonOpenUpActivity(CommonOpenUpFragmentType fragmentType, Object intentData, int requestCode) {
        Intent intent = new Intent(mActivity, CommonActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (intentData != null) {
            intent.putExtra(HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA, (Serializable) intentData);
        }
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }

    private void openResetPasswordDialogFragment(String emailAddress) {
        Bundle args = new Bundle();
        args.putString(HealthCocoConstants.TAG_EMAIL_ID, emailAddress);
        ForgotPasswordDialogFragment dialogFragment = new ForgotPasswordDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(mFragmentManager, dialogFragment.getClass().getSimpleName());
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String userName = Util.getValidatedValueOrNull(editUserName);
        String password = Util.getValidatedValueOrBlankWithoutTrimming(editPassword);
        String msg = null;
        if (Util.isNullOrBlank(userName) && Util.isNullOrBlank(password)) {
            msg = getResources().getString(R.string.please_enter_email_id_And_password_to_login);
            errorViewList.add(editUserName);
            errorViewList.add(editPassword);
//            editUserName.setError(msg);
        } else if (Util.isNullOrBlank(userName)) {
            msg = getResources().getString(R.string.please_enter_email_id_to_login);
            errorViewList.add(editUserName);
        } else if (!Util.isValidEmail(userName)) {
            msg = getResources().getString(R.string.please_enter_valid_email_address);
            errorViewList.add(editUserName);
        } else if (Util.isNullOrBlank(password)) {
            errorViewList.add(editPassword);
            msg = getResources().getString(R.string.please_enter_password_to_login);
        }
        if (Util.isNullOrBlank(msg))
            loginUser(userName, password);
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void loginUser(String userName, String password) {
        mActivity.showLoading(false);
        User user = new User();
        user.setUsername(userName);
        user.setPassword(Util.getSHA3SecurePassword(password));
        WebDataServiceImpl.getInstance(mApp).loginUser(LoginResponse.class, user, this, this);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
//        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.isValidData(response)) {
            LoginResponse doctor = (LoginResponse) response.getData();
            if (doctor.getUser() != null && doctor.getUser().getUserState() != null) {
                LocalDataServiceImpl.getInstance(mApp).addDoctor(doctor);
                switch (doctor.getUser().getUserState()) {
                    case USERSTATEINCOMPLETE:
                        openContinueSignUpFragment(doctor);
                        break;
                    case USERSTATECOMPLETE:
                        openHomeActivity();
                        break;
                    case NOTVERIFIED:
                        Util.showAlert(mActivity, R.string.title_please_verify, R.string.alert_verify);
                        break;
                    case NOTACTIVATED:
                        Util.showAlert(mActivity, R.string.title_activation, R.string.alert_activation);
                        break;
                }
            }
        }
        mActivity.hideLoading();
    }

    private void openContinueSignUpFragment(LoginResponse doctor) {
        LogUtils.LOGD(TAG, "Open Continue Signup");
//        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
//        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.CONTINUE_SIGN_UP.ordinal());
//        intent.putExtra(ContinueSignUpFragment.TAG_IS_FROM_LOGIN_SCREEN, true);
//        if (loginResponse != null && loginResponse.getUser() != null)
//            intent.putExtra(HealthCocoConstants.TAG_DOCTOR_USER_ID, loginResponse.getUser().getUniqueId());
//        startActivity(intent);
//        ((CommonOpenUpActivity) mActivity).finish();
    }

    private void openHomeActivity() {
        Intent intent = new Intent(mActivity, HomeActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_LOGIN_SIGNUP, true);
        startActivity(intent);
        getDialog().dismiss();
        mActivity.finish();
    }

}