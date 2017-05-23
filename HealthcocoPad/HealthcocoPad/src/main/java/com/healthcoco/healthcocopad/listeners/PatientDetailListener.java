package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;

/**
 * Created by Mohit on 02/03/16.
 */
public interface PatientDetailListener {
    public void onListViewSizeSet(int listViewSize);

    public void setPatientProfileViewSize(int size);

    public void setPatientVisitsViewSize(int size);

    public void setHeaderYLocationOnScreen(int locationY);

    public RegisteredPatientDetailsUpdated getSelectedPatient();

    public User getUser();
}
