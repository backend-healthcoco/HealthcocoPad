package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.DoctorContactUs;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Specialities;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.DeviceType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.enums.WebViewType;
import com.healthcoco.healthcocopad.fragments.LoginSignupFragment;
import com.healthcoco.healthcocopad.fragments.WebViewFragments;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

import static com.healthcoco.healthcocopad.enums.WebServiceType.GET_SPECIALITIES;

/**
 * Created by Shreshtha on 19-01-2017.
 */

public class ContactUsDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, CommonListDialogItemClickListener, LocalDoInBackgroundListenerOptimised {
    private Button bt_cancel;
    private EditText editName;
    private EditText editMobileNo;
    private EditText editEmailAddress;
    private EditText editCity;
    private EditText editPromoCode;
    private TextView tvSpeciality;
    private ArrayList<Specialities> specialitiesResponse;
    private boolean openSpecialitiesScreen;
    private AutoCompleteTextView autoTvTitle;
    private RadioGroup radioGroupGender;
    private Button btGetStarted;
    private Button btAlreadyHaveAnAccount;
    private TextView tvTermsOfService;
    private TextView tvPrivacyPolicy;
    public static final ArrayList<Object> TITLES_LIST = new ArrayList<Object>() {{
        add("Dr.");
        add("Mr.");
        add("Ms.");
    }};
    private boolean isFromLoginScreen;
    public static final String TAG_IS_FROM_LOGIN_SCREEN = "isFromLoginScreen";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_contact_us, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = mActivity.getIntent();
        if (intent != null)
            isFromLoginScreen = intent.getBooleanExtra(TAG_IS_FROM_LOGIN_SCREEN, false);
        init();
        setWidthHeight(0.60, 0.75);
    }

    public void init() {
        initViews();
        initListeners();
        initAutoTvAdapter(autoTvTitle, AutoCompleteTextViewType.DOCTOR_TITLES, TITLES_LIST);
    }

    public void initListeners() {
        editCity.setOnClickListener(this);
        tvSpeciality.setOnClickListener(this);
        btGetStarted.setOnClickListener(this);
        btAlreadyHaveAnAccount.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        tvTermsOfService.setOnClickListener(this);
        tvPrivacyPolicy.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    public void initViews() {
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        editName = (EditText) view.findViewById(R.id.edit_name);
        editPromoCode = (EditText) view.findViewById(R.id.edit_promo_code);
        editMobileNo = (EditText) view.findViewById(R.id.edit_mobile_number);
        editEmailAddress = (EditText) view.findViewById(R.id.edit_email_address);
        editCity = (EditText) view.findViewById(R.id.edit_city);
        tvSpeciality = (TextView) view.findViewById(R.id.tv_speciality);
        autoTvTitle = (AutoCompleteTextView) view.findViewById(R.id.autotv_title);
        radioGroupGender = (RadioGroup) view.findViewById(R.id.rg_gender_select);
        btGetStarted = (Button) view.findViewById(R.id.bt_get_started);
        btAlreadyHaveAnAccount = (Button) view.findViewById(R.id.bt_alreay_have_an_account);
        tvTermsOfService = (TextView) view.findViewById(R.id.tv_terms_of_service);
        tvPrivacyPolicy = (TextView) view.findViewById(R.id.tv_privacy_policy);
    }

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, AutoCompleteTextViewType autoCompleteTextViewType, ArrayList<Object> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
                AutoCompleteTextViewAdapter textViewAdapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_grey_background,
                        list, autoCompleteTextViewType);
                autoCompleteTextView.setThreshold(0);
                autoCompleteTextView.setAdapter(textViewAdapter);
                autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_speciality:
                if (!Util.isNullOrEmptyList(specialitiesResponse))
                    openListPopUp(CommonListDialogType.SPECIALITY, specialitiesResponse);
                else
                    getSpecialitiesList(true);
                break;
            case R.id.bt_get_started:
                validateData();
                break;
            case R.id.bt_alreay_have_an_account:
                openLoginDialogFragment();
                break;
            case R.id.bt_cancel:
                getDialog().cancel();
                break;
            case R.id.tv_terms_of_service:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    openCommonOpenUpActivity(CommonOpenUpFragmentType.TERMS_OF_SERVICE, WebViewFragments.TAG_WEB_VIEW_TYPE, WebViewType.TERMS_OF_SERVICE.ordinal(), 0);
                } else Util.showToast(mActivity, R.string.user_offline);
                break;
            case R.id.tv_privacy_policy:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    openCommonOpenUpActivity(CommonOpenUpFragmentType.PRIVACY_POLICY, WebViewFragments.TAG_WEB_VIEW_TYPE, WebViewType.PRIVACY_POLICY.ordinal(), 0);
                } else Util.showToast(mActivity, R.string.user_offline);
                break;
            default:
                break;
        }
    }

    private void openLoginDialogFragment() {
        getDialog().dismiss();
        LoginDialogFragment dialogFragment = new LoginDialogFragment();
        dialogFragment.show(mFragmentManager, dialogFragment.getClass().getSimpleName());
    }

    private void getSpecialitiesList(boolean openSpecialitiesScreen) {
        this.openSpecialitiesScreen = openSpecialitiesScreen;
        mActivity.showLoading(false);
        long updatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.SPECIALITIES);
        WebDataServiceImpl.getInstance(mApp).getSpecialities(Specialities.class, updatedTime, this, this);
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String name = Util.getValidatedValueOrNull(editName);
        View checkedRadioButton = view.findViewById(radioGroupGender.getCheckedRadioButtonId());
        String mobileNo = Util.getValidatedValueOrNull(editMobileNo);
        String emailAddress = Util.getValidatedValueOrNull(editEmailAddress);
        String speciality = Util.getValidatedValueOrNull(tvSpeciality);
        String city = Util.getValidatedValueOrNull(editCity);

        if (Util.isNullOrBlank(name)) {
            msg = getResources().getString(R.string.please_enter_name);
            errorViewList.add(editName);
        } else if (checkedRadioButton == null) {
            msg = getResources().getString(R.string.please_select_gender);
        } else if (Util.isNullOrBlank(mobileNo)) {
            msg = getResources().getString(R.string.please_enter_mobile_no);
            errorViewList.add(editMobileNo);
        } else if (!Util.isValidMobileNo(mobileNo)) {
            msg = getResources().getString(R.string.please_enter_valid_mobile_no);
            errorViewList.add(editMobileNo);
        } else if (Util.isNullOrBlank(emailAddress)) {
            msg = getResources().getString(R.string.please_enter_email_address);
            errorViewList.add(editEmailAddress);
        } else if (!Util.isValidEmail(emailAddress)) {
            msg = getResources().getString(R.string.please_enter_valid_email_address);
            errorViewList.add(editEmailAddress);
        } else if (Util.isNullOrBlank(speciality)) {
            msg = getResources().getString(R.string.please_select_speciality);
            errorViewList.add(tvSpeciality);
        } else if (Util.isNullOrBlank(city)) {
            msg = getResources().getString(R.string.please_select_city);
            errorViewList.add(editCity);
        }

        if (Util.isNullOrBlank(msg)) {
            sendContactUsRequest(name, String.valueOf(checkedRadioButton.getTag()), mobileNo, emailAddress, speciality, city);
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void sendContactUsRequest(String name, String gender, String mobileNo, String emailAddress, final String speciality, String city) {
        mActivity.showLoading(false);
        DoctorContactUs doctorContactUs = new DoctorContactUs(Util.getValidatedValueOrNull(autoTvTitle), name, gender, mobileNo, emailAddress, new ArrayList<String>() {{
            add(speciality);
        }}, city, DeviceType.ANDROID_PAD);
        if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(editPromoCode))) {
            doctorContactUs.setMrCode(Util.getValidatedValueOrNull(editPromoCode));
        }
        WebDataServiceImpl.getInstance(mApp).sendContactUsRequest(String.class, doctorContactUs, this, this);
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
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case GET_SPECIALITIES:
                    LogUtils.LOGD(TAG, "Success GET_SPECIALITIES");
                    if (response.isDataFromLocal()) {
                        specialitiesResponse = (ArrayList<Specialities>) (ArrayList<?>) response.getDataList();
                        if (!Util.isNullOrEmptyList(specialitiesResponse))
                            LogUtils.LOGD(TAG, "Success onResponse specialitiesResponse Size " + specialitiesResponse.size() + " isDataFromLocal " + response.isDataFromLocal());
                        if (!response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            getSpecialitiesList(false);
                            return;
                        }
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        if (specialitiesResponse == null)
                            specialitiesResponse = new ArrayList<>();
                        specialitiesResponse.addAll((ArrayList<Specialities>) (ArrayList<?>) response
                                .getDataList());
                        response.setIsFromLocalAfterApiSuccess(true);
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_SPECIALITIES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    if (openSpecialitiesScreen)
                        onClick(tvSpeciality);
                    break;
                case DOCTOR_CONTACT_US:
                    LogUtils.LOGD(TAG, "Success DOCTOR_CONTACT_US");
                    if (response.getData() != null && response.getData() instanceof String) {
                        LogUtils.LOGD(TAG, "Success DOCTOR_CONTACT_US send broadcast");
                        sendBroadcastToOriginScreen((String) response.getData());
                        getDialog().dismiss();
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void sendBroadcastToOriginScreen(String message) {
        if (isFromLoginScreen)
            Util.sendBroadcast(mApp, LoginDialogFragment.INTENT_SIGNUP_SUCCESS, message);
        else {
            LoginSignupFragment.IS_FROM_CONTINUE_SIGNUP_SUCCESS = true;
            Util.sendBroadcast(mApp, LoginSignupFragment.INTENT_SIGNUP_SUCCESS, message);
        }
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_SPECIALITIES:
                LocalDataServiceImpl.getInstance(mApp).addSpecialities((ArrayList<Specialities>) (ArrayList<?>) response.getDataList());
            case GET_SPECIALITIES:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSpecialitiesListVolleyResponse(GET_SPECIALITIES, null, null);
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
    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {
        switch (commonListDialogType) {
            case SPECIALITY:
                if (object instanceof Specialities) {
                    Specialities selectedSpeciality = (Specialities) object;
                    tvSpeciality.setText(selectedSpeciality.getSuperSpeciality());
                }
                break;
        }
        if (commonListDialogFragment != null)
            commonListDialogFragment.dismiss();
    }
}