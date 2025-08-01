package com.healthcoco.healthcocopad.calendar.pinlockview;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
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
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.KioskPin;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.KioskScreenType;
import com.healthcoco.healthcocopad.enums.KioskSubItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.KioskTabListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;


public class EnterPinFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised, PinLockListener, View.OnClickListener {

    public static final String TAG = "EnterPinFragment";

    private static final int PIN_LENGTH = 4;
    private static final String DEFAULT_PIN = "1234";

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private TextView tvTitle;
    private TextView tvAttempts;
    private TextView tvForgotPin;
    private KioskTabListener kioskTabListener;
    private Button btHome;
    private User user;
    private String pinCode;

    public EnterPinFragment(KioskTabListener kioskTabListener) {
        super();
        this.kioskTabListener = kioskTabListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_enter_pin, container, false);
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
        tvTitle = (TextView) view.findViewById(R.id.title);
        mIndicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);
        mPinLockView = (PinLockView) view.findViewById(R.id.pinlockView);
        btHome = (Button) view.findViewById(R.id.bt_kiosk_home);
        tvForgotPin = (TextView) view.findViewById(R.id.tv_forgot_pin);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(PIN_LENGTH);
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);
    }

    @Override
    public void initListeners() {
        mPinLockView.setPinLockListener(this);
        btHome.setOnClickListener(this);
        tvForgotPin.setOnClickListener(this);
    }


    private void shake() {
        ObjectAnimator objectAnimator = new ObjectAnimator().ofFloat(mPinLockView, "translationX",
                0, 25, -25, 25, -25, 15, -15, 6, -6, 0).setDuration(1000);
        objectAnimator.start();
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
                    KioskPin kioskPin = LocalDataServiceImpl.getInstance(mApp).getKioskPin(user.getUniqueId());
                    if (kioskPin != null)
                        pinCode = kioskPin.getPin();
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
                        if (Util.isNullOrBlank(pinCode)) {
                            pinCode = DEFAULT_PIN;
                        }
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }


    @Override
    public void onComplete(String pin) {
        if (pin.equals(pinCode)) {
//            setResult(RESULT_OK);
//            finish();
            kioskTabListener.onHomeButtonClick(KioskScreenType.HOME.ordinal());
        } else {
            shake();
            tvAttempts.setText(getString(R.string.pinlock_wrongpin));
            mPinLockView.resetPinLockView();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onPinEntered(String key) {

    }

    @Override
    public void onPinDeleted() {

    }

    @Override
    public boolean isReset() {
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_kiosk_home) {
            kioskTabListener.onHomeButtonClick(KioskScreenType.KIOSK.ordinal());

        } else if (id == R.id.tv_forgot_pin) {
            mActivity.openVerifyAdminFragment(this, HealthCocoConstants.REQUEST_CODE_LOCATION_ADMIN, SuggestionType.PASSWORD);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_LOCATION_ADMIN) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_LOCATION_ADMIN) {
                openCommonOpenUpActivity(CommonOpenUpFragmentType.CHANGE_PIN, "CHANGE_PIN", null, HealthCocoConstants.REQUEST_CODE_CHANGE_PIN);
            }
        }
        if (requestCode == HealthCocoConstants.REQUEST_CODE_CHANGE_PIN) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_CHANGE_PIN) {
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

}

