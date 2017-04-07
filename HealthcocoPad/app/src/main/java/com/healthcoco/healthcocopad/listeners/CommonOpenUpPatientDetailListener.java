package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;

/**
 * Created by neha on 07/04/17.
 */

public interface CommonOpenUpPatientDetailListener {
    public User getUser();
    public RegisteredPatientDetailsUpdated getSelectedPatientDetails();
}
