package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;

/**
 * Created by neha on 10/12/15.
 */
public interface CommonEMRItemClickListener {

    public void showLoading(boolean showLoading);


    public RegisteredPatientDetailsUpdated getSelectedPatient();

    public User getUser();

    public String getLoginedUser();

    public void openEmrScreen(HistoryFilterType historyFilterType);

}
