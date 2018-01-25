package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 19-Dec-17.
 */

public interface CBSelectedItemTypeListener {

    public void onCBSelectedItemTypeCheckClicked(boolean isChecked, Object object);

    public boolean isSelectAll();

    public void setSelectAllSelected(boolean isSelected);

    public ArrayList<String> getDoctorProfileArrayList();

    public boolean isInitialLaunch();
}
