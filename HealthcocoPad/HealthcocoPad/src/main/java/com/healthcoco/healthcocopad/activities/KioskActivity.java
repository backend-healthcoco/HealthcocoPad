package com.healthcoco.healthcocopad.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.HealthcocoFCMListener;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.calendar.pinlockview.EnterPinFragment;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.KioskScreenType;
import com.healthcoco.healthcocopad.enums.KioskSubItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.fragments.AboutDoctorFragment;
import com.healthcoco.healthcocopad.fragments.KioskFragment;
import com.healthcoco.healthcocopad.listeners.KioskTabListener;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;

/**
 * Created by Prashant on 11/07/2018.
 */

public class KioskActivity extends HealthCocoFragment implements PatientRegistrationDetailsListener {

    private Intent intent;
    private FragmentTransaction transaction;
    private KioskTabListener kioskTabListener;

    public KioskActivity(KioskTabListener kioskTabListener) {
        super();
        this.kioskTabListener = kioskTabListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_kiosk, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
//        intent = getIntent();
//        fragmentOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, -1);
        initMembers();
        initFragment();
    }


    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }


    private void initMembers() {
        LogUtils.LOGD(TAG, "initMembers");
        transaction = mActivity.getSupportFragmentManager().beginTransaction();
    }

    private void initFragment() {
        openFragment(new KioskFragment(this));
    }


    private void replaceFragment(HealthCocoFragment fragment) {
        transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_animation_fade_in, R.anim.fragment_animation_fade_out);
        transaction.replace(R.id.layout_fragment_kiosk, fragment);
        transaction.commit();
    }

    private void openFragment(HealthCocoFragment fragment) {
        transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_fragment_kiosk, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void readyToMoveNext(Object object, boolean isEditPatient) {
        if (object != null) {
            int ordinal = (int) object;
            KioskScreenType screenType = KioskScreenType.values()[ordinal];
            switch (screenType) {
                case PINVIEW:
                    replaceFragment(new EnterPinFragment(this));
                    break;
                case KIOSK:
                    replaceFragment(new KioskFragment(this));
                    break;
                case HOME:
                   /* Intent intent = new Intent(this, HomeActivity.class);
//                    intent.putExtra(HealthcocoFCMListener.TAG_NOTIFICATION_RESPONSE, notificationResponseData);
                    startActivity(intent);
                    finish();*/
                    kioskTabListener.onHomeButtonClick();
                    break;
            }
        }
    }

    @Override
    public boolean isFromPatientRegistarion() {
        return false;
    }
}
