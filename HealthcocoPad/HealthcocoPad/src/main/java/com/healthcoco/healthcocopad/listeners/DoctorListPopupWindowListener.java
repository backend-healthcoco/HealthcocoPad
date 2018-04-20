package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.enums.PopupWindowType;

import java.util.ArrayList;

/**
 * Created by neha on 03/05/17.
 */

public interface DoctorListPopupWindowListener {
    public void onDoctorSelected(ArrayList<RegisteredDoctorProfile> objects);

    public void onEmptyListFound();
}
