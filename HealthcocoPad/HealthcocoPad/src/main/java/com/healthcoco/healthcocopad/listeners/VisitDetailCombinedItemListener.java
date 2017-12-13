package com.healthcoco.healthcocopad.listeners;

import android.view.View;

import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;


/**
 * Created by neha on 02/09/16.
 */
public interface VisitDetailCombinedItemListener {
    public User getUser();

    public String getLoginedUser();

    public RegisteredPatientDetailsUpdated getSelectedPatient();

    public void doPrint(String visitId);

    public void sendSms(String mobileNumber);

    public void sendEmail(String emailAddress);

    public void editVisit(String visitId);

    public void setVisitHeader(View visitHeader);

    public void saveAsTemplate(Prescription prescription);

    public void cloneVisit(String uniqueId);

    public void openRecord(Records records);
}
