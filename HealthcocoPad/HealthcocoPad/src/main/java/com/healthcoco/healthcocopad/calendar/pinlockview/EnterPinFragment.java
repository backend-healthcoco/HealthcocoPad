package com.healthcoco.healthcocopad.calendar.pinlockview;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.enums.KioskScreenType;
import com.healthcoco.healthcocopad.enums.KioskSubItemType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;


public class EnterPinFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised, PinLockListener, View.OnClickListener {

    public static final String TAG = "EnterPinFragment";

    private static final int PIN_LENGTH = 4;

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private TextView tvTitle;
    private TextView tvAttempts;
    private PatientRegistrationDetailsListener registrationDetailsListener;
    private Button btHome;

    public EnterPinFragment(PatientRegistrationDetailsListener registrationDetailsListener) {
        super();
        this.registrationDetailsListener = registrationDetailsListener;
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
//        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(PIN_LENGTH);
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);
    }

    @Override
    public void initListeners() {
        mPinLockView.setPinLockListener(this);
        btHome.setOnClickListener(this);
    }


    private void shake() {
        ObjectAnimator objectAnimator = new ObjectAnimator().ofFloat(mPinLockView, "translationX",
                0, 25, -25, 25, -25, 15, -15, 6, -6, 0).setDuration(1000);
        objectAnimator.start();
    }


    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        return null;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onComplete(String pin) {
        if (pin.equals("1234")) {
//            setResult(RESULT_OK);
//            finish();
            registrationDetailsListener.readyToMoveNext(KioskScreenType.HOME.ordinal());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_kiosk_home:
                registrationDetailsListener.readyToMoveNext(KioskScreenType.KIOSK.ordinal());
                break;
        }
    }
}
