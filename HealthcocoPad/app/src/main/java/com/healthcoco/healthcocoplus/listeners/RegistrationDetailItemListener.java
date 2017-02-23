package com.healthcoco.healthcocoplus.listeners;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocoplus.bean.server.DoctorRegistrationDetail;

/**
 * Created by neha on 03/02/16.
 */
public interface RegistrationDetailItemListener {
    public void onMedicalCouncilClicked(TextView textView, DoctorRegistrationDetail registrationDetail);

    public void onDeleteRegistrationDetailClicked(View view, DoctorRegistrationDetail registrationDetail);

    public void addRegistrationDetailToList(DoctorRegistrationDetail registrationDetail);
}
