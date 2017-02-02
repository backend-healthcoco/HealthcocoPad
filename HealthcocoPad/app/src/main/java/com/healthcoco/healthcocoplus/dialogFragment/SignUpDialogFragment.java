package com.healthcoco.healthcocoplus.dialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoDialogFragment;
import com.healthcoco.healthcocoplus.activities.CommonActivity;
import com.healthcoco.healthcocoplus.activities.HomeActivity;
import com.healthcoco.healthcocoplus.bean.server.Specialities;
import com.healthcoco.healthcocoplus.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocoplus.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocoplus.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocoplus.enums.CommonListDialogType;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.LocalTabelType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.enums.WebViewType;
import com.healthcoco.healthcocoplus.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocoplus.listeners.LocalDoInBackgroundListenerOptimised;
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

import static com.healthcoco.healthcocoplus.enums.WebServiceType.GET_SPECIALITIES;

/**
 * Created by Shreshtha on 19-01-2017.
 */

public class SignUpDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, CommonListDialogItemClickListener, LocalDoInBackgroundListenerOptimised {
    private View view;
    private Button bt_cancel;
    private EditText editName;
    private EditText editMobileNo;
    private EditText editEmailAddress;
    private EditText editCity;
    private TextView tvSpeciality;
    private ArrayList<Specialities> specialitiesResponse;
    private CommonListDialogFragment commonListDialogFragment;
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
        Intent intent = mActivity.getIntent();
        if (intent != null)
            isFromLoginScreen = intent.getBooleanExtra(TAG_IS_FROM_LOGIN_SCREEN, false);
        init();
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
                openCommonOpenUpActivity(CommonOpenUpFragmentType.TERMS_OF_SERVICE, WebViewType.TERMS_OF_SERVICE.ordinal(), 0);
                break;
            case R.id.tv_privacy_policy:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.PRIVACY_POLICY, WebViewType.PRIVACY_POLICY.ordinal(), 0);
                break;
            default:
                break;
        }
    }

    private void openLoginDialogFragment() {
        getDialog().dismiss();
        Bundle args = new Bundle();
        LoginDialogFragment dialogFragment = new LoginDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(mFragmentManager, dialogFragment.getClass().getSimpleName());
    }

    private void getSpecialitiesList(boolean openSpecialitiesScreen) {
        this.openSpecialitiesScreen = openSpecialitiesScreen;
        mActivity.showLoading(false);
        long updatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.SPECIALITIES);
        WebDataServiceImpl.getInstance(mApp).getSpecialities(Specialities.class, updatedTime, this, this);
    }

    private void openListPopUp(CommonListDialogType popupType, ArrayList<?> list) {
        commonListDialogFragment = new CommonListDialogFragment(this, popupType, list);
        commonListDialogFragment.show(mFragmentManager, CommonListDialogFragment.class.getSimpleName());
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
            errorViewList.add(editName);
            msg = getResources().getString(R.string.please_enter_name);
        } else if (checkedRadioButton == null) {
            msg = getResources().getString(R.string.please_select_gender);
        } else if (Util.isNullOrBlank(mobileNo)) {
            errorViewList.add(editMobileNo);
            msg = getResources().getString(R.string.please_enter_mobile_no);
        } else if (!Util.isValidMobileNo(mobileNo)) {
            errorViewList.add(editMobileNo);
            msg = getResources().getString(R.string.please_enter_valid_mobile_no);
        } else if (Util.isNullOrBlank(emailAddress)) {
            errorViewList.add(editEmailAddress);
            msg = getResources().getString(R.string.please_enter_email_address);
        } else if (!Util.isValidEmail(emailAddress)) {
            errorViewList.add(editEmailAddress);
            msg = getResources().getString(R.string.please_enter_valid_email_address);
        } else if (Util.isNullOrBlank(speciality)) {
            errorViewList.add(tvSpeciality);
            msg = getResources().getString(R.string.please_select_speciality);
        } else if (Util.isNullOrBlank(city)) {
            errorViewList.add(editCity);
            msg = getResources().getString(R.string.please_select_city);
        }

        if (Util.isNullOrBlank(msg)) {
            sendContactUsRequest(name, String.valueOf(checkedRadioButton.getTag()), mobileNo, emailAddress, speciality, city);
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void sendContactUsRequest(String name, String s, String mobileNo, String emailAddress, String speciality, String city) {
        mActivity.showLoading(false);
//        DoctorContactUs doctorContactUs = new DoctorContactUs(Util.getValidatedValueOrNull(autoTvTitle), name, gender, mobileNo, emailAddress, new ArrayList<String>() {{
//            add(speciality);
//        }}, city);
//        WebDataServiceImpl.getInstance(mApp).sendContactUsRequest(String.class, doctorContactUs, this, this);
    }

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
            }
        }
        mActivity.hideLoading();
    }

    private void openHomeActivity() {
        Intent intent = new Intent(mActivity, HomeActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_LOGIN_SIGNUP, true);
        startActivity(intent);
        getDialog().dismiss();
        mActivity.finish();
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