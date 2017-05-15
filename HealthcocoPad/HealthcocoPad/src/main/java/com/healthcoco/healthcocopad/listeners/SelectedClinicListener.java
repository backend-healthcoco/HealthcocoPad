package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;

/**
 * Created by Shreshtha on 28-04-2017.
 */

public interface SelectedClinicListener {
    public void onSelectedClinicCheckClicked(DoctorClinicProfile doctorClinicProfile);
}
