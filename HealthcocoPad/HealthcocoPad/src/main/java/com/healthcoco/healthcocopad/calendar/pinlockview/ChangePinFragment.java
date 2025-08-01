package com.healthcoco.healthcocopad.calendar.pinlockview;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.KioskPin;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.KioskScreenType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;


public class ChangePinFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised, PinLockListener, View.OnClickListener {

    public static final String TAG = "EnterPinFragment";

    private static final int PIN_LENGTH = 4;

    private PinLockView mPinLockView;
    private TextView tvTitle;
    private TextView tvAttempts;
    private TextView tvEnteredValue;
    private TextView tvSubmit;
    private User user;
    private String pinCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_pin, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        tvAttempts = (TextView) view.findViewById(R.id.attempts);
        tvEnteredValue = (TextView) view.findViewById(R.id.tv_enter_value);
        tvTitle = (TextView) view.findViewById(R.id.title);
        mPinLockView = (PinLockView) view.findViewById(R.id.pinlockView);
        tvSubmit = (TextView) view.findViewById(R.id.tv_submit);

        mPinLockView.setPinLength(PIN_LENGTH);
    }

    @Override
    public void initListeners() {
        mPinLockView.setPinLockListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                }
                return volleyResponseBean;
        }
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                    }
                    break;
                case ADD_EDIT_PIN:
                    if (response.isValidData(response)) {
                        LocalDataServiceImpl.getInstance(mApp).
                                addKioskPin(response.getData());
                        Util.showToast(mActivity, getString(R.string.pin_successfully_changed));
                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_CHANGE_PIN, null);
//                     onActivityResult(HealthCocoConstants.REQUEST_CODE_CHANGE_PIN, Activity.RESULT_OK, null);
                        mActivity.hideLoading();
                        mActivity.finish();
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }


    @Override
    public void onComplete(String pin) {
//        Util.showToast(mActivity, getString(R.string.max_length_reached));
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onPinEntered(String key) {
        pinCode = key;
        tvEnteredValue.setText(key);
    }

    @Override
    public void onPinDeleted() {
        pinCode = pinCode.substring(0, pinCode.length() - 1);
        tvEnteredValue.setText(pinCode);
    }

    @Override
    public boolean isReset() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_submit) {
            validateData();
        }
    }

    private void validateData() {
        String pinNumber = (String.valueOf(tvEnteredValue.getText()));
        String msg = null;
        if (Util.isNullOrBlank(pinNumber)) {
            msg = getResources().getString(R.string.please_enter_valid_pin);
        } else if (pinNumber.length() < 4)
            msg = getResources().getString(R.string.please_enter_valid_pin);

        if (Util.isNullOrBlank(msg)) {
            changePin(pinNumber);
        } else {
//            if (!isMobileNumberOptional)
            Util.showAlert(mActivity, msg);
        }
    }

    private void changePin(String pin) {
        mActivity.showLoading(false);
        KioskPin kioskPin = new KioskPin();
        kioskPin.setPin(pin);
        kioskPin.setDoctorId(user.getUniqueId());
        WebDataServiceImpl.getInstance(mApp).addSuggestion(KioskPin.class, WebServiceType.ADD_EDIT_PIN, kioskPin, this, this);
    }
}
