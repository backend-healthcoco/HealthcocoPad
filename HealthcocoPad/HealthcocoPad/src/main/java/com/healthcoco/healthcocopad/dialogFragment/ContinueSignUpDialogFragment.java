package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.healthcoco.healthcocopad.bean.request.DoctorSignupHandheldContinueRequest;
import com.healthcoco.healthcocopad.bean.server.CityResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundTask;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.LoginSignupFragment;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by neha on 11/01/16.
 */
public class ContinueSignUpDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        LocalDoInBackgroundListener, CommonListDialogItemClickListener {
    public static final String TAG_IS_FROM_LOGIN_SCREEN = "isFromLoginScreen";
    private Button btJoin;
    private EditText editClinicName;
    private EditText editAddress;
    private TextView editCity;
    private CommonListDialogFragment commonListDialogFragment;
    private ArrayList<CityResponse> citiesResponse;
    private LoginResponse doctor;
    private User user;
    private CityResponse selectedCity;
    private EditText editRegistrationNumber;
    private boolean openCitiesListScreen;
    private boolean isFromLoginScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_continue_signup, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setWidthHeight(0.60, 0.70);
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        isFromLoginScreen = intent.getBooleanExtra(TAG_IS_FROM_LOGIN_SCREEN, false);
        getCitiesList(false);
        doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
        }
    }

    private void getCitiesList(boolean openCitiesListScreen) {
        this.openCitiesListScreen = openCitiesListScreen;
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getCities(CityResponse.class, this, this);
    }

    @Override
    public void initViews() {
        editClinicName = (EditText) view.findViewById(R.id.edit_clinic_name);
        editAddress = (EditText) view.findViewById(R.id.edit_clinic_address);
        editCity = (TextView) view.findViewById(R.id.edit_city);
        editRegistrationNumber = (EditText) view.findViewById(R.id.edit_registration_number);
        btJoin = (Button) view.findViewById(R.id.bt_join);
    }

    @Override
    public void initListeners() {
        btJoin.setOnClickListener(this);
        editCity.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_join:
                validateData();
                break;
            case R.id.edit_city:
                if (!Util.isNullOrEmptyList(citiesResponse))
                    openListPopUp(CommonListDialogType.CITY, citiesResponse);
                else
                    getCitiesList(true);
                break;
        }
    }

    private void openListPopUp(CommonListDialogType popupType, ArrayList<?> list) {
        commonListDialogFragment = new CommonListDialogFragment(this, popupType, list);
        commonListDialogFragment.show(mFragmentManager, CommonListDialogFragment.class.getSimpleName());
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        clearPreviousAlerts();
        String clinicName = String.valueOf(editClinicName.getText());
        String city = String.valueOf(editCity.getText());
        String address = String.valueOf(editAddress.getText());
        String registrationNumber = String.valueOf(editRegistrationNumber.getText());
        if (Util.isNullOrBlank(clinicName)) {
            errorViewList.add(editClinicName);
            msg = getResources().getString(R.string.please_enter_clinic_name);
        } else if (Util.isNullOrBlank(address)) {
            errorViewList.add(editAddress);
            msg = getResources().getString(R.string.please_enter_address);
        } else if (Util.isNullOrBlank(registrationNumber)) {
            errorViewList.add(editRegistrationNumber);
            msg = getResources().getString(R.string.please_enter_registration_number);
        } else if (selectedCity == null || Util.isNullOrBlank(city)) {
            errorViewList.add(editCity);
            msg = getResources().getString(R.string.please_enter_city);
        }

        if (Util.isNullOrBlank(msg)) {
            continueSignUp(clinicName, address, registrationNumber);
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void clearPreviousAlerts() {
        editClinicName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        editCity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        editRegistrationNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        editAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    private void continueSignUp(String clinicName, String address, String registrationNumber) {
        mActivity.showLoading(false);
        DoctorSignupHandheldContinueRequest continueRequest = new DoctorSignupHandheldContinueRequest();
        continueRequest.setUserId(user.getUniqueId());
        continueRequest.setLocationName(clinicName);
        continueRequest.setRegisterNumber(registrationNumber);
        continueRequest.setStreetAddress(address);
        continueRequest.setCity(selectedCity.getCity());
        continueRequest.setLatitude(selectedCity.getLatitude());
        continueRequest.setLongitude(selectedCity.getLongitude());
        WebDataServiceImpl.getInstance(mApp).signUpContinueVerification(LoginResponse.class, continueRequest, this, this);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case SIGN_UP_CONTINUE_VERIFICATION:
                    if (response.isValidData(response)) {
                        LoginResponse doctor = (LoginResponse) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addDoctor(doctor);
                        sendBroadcastToOriginScreen();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_CONTINUE_SIGNUP, getActivity().getIntent());
                        getDialog().dismiss();
                    }
                    break;
                case GET_CITIES:
                    LogUtils.LOGD(TAG, "Success GET_CITIES");
                    citiesResponse = (ArrayList<CityResponse>) (ArrayList<?>) response.getDataList();
                    LogUtils.LOGD(TAG, "onResponse citiesResponse Size " + citiesResponse.size() + " isDataFromLocal " + response.isDataFromLocal());
                    if (!response.isDataFromLocal()) {
                        new LocalDataBackgroundTask(mActivity, LocalBackgroundTaskType.ADD_CITIES, this).execute();
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void sendBroadcastToOriginScreen() {
        if (isFromLoginScreen)
            Util.sendBroadcast(mApp, LoginDialogFragment.INTENT_SIGNUP_SUCCESS);
        else {
            LoginSignupFragment.IS_FROM_CONTINUE_SIGNUP_SUCCESS = true;
            Util.sendBroadcast(mApp, LoginSignupFragment.INTENT_SIGNUP_SUCCESS);
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        switch (volleyResponseBean.getWebServiceType()) {
            case GET_CITIES:
                break;
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
    public VolleyResponseBean doInBackground(LocalBackgroundTaskType localBackgroundTaskType) {
        VolleyResponseBean volleyResponseBean = null;
        switch (localBackgroundTaskType) {
            case ADD_CITIES:
                LocalDataServiceImpl.getInstance(mApp).addCities(citiesResponse);
        }
        return null;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
        if (volleyResponseBean != null)
            if (!Util.isNullOrBlank(volleyResponseBean.getErrMsg()))
                onErrorResponse(volleyResponseBean, "");
            else
                onResponse(volleyResponseBean);
    }

    @Override
    public void onDialogItemClicked(CommonListDialogType popupType, Object object) {
        switch (popupType) {
            case CITY:
                selectedCity = (CityResponse) object;
                editCity.setText(selectedCity.getCity());
                break;
        }
        if (commonListDialogFragment != null)
            commonListDialogFragment.dismiss();
    }
}
