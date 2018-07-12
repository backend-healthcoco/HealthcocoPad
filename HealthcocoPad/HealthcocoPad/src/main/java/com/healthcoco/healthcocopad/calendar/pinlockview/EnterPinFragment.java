package com.healthcoco.healthcocopad.calendar.pinlockview;

import android.animation.ObjectAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.KioskActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.KioskSubItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;


public class EnterPinFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised, PinLockListener {

    public static final String TAG = "EnterPinFragment";

    private static final int PIN_LENGTH = 4;

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private TextView mTextTitle;
    private TextView mTextAttempts;
    private PatientRegistrationDetailsListener registrationDetailsListener;

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
        mTextAttempts = (TextView) view.findViewById(R.id.attempts);
        mTextTitle = (TextView) view.findViewById(R.id.title);
        mIndicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);
        mPinLockView = (PinLockView) view.findViewById(R.id.pinlockView);


        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(PIN_LENGTH);
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);
    }

    @Override
    public void initListeners() {
        mPinLockView.setPinLockListener(this);
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
            registrationDetailsListener.readyToMoveNext(KioskSubItemType.BLOGS.ordinal());
        } else {
            shake();
            mTextAttempts.setText(getString(R.string.pinlock_wrongpin));
            mPinLockView.resetPinLockView();
        }
    }

    @Override
    public void onEmpty() {

    }
}
