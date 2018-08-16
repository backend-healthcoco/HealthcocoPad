package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;

/**
 * Created by Prashant on 14-08-2018.
 */

public interface PatientVideoListItemClickListeners {

    public void onEditClicked(Object object);

    public void onDiscardClicked(Object object);

    public void onVideoClicked(Object object);

    public boolean isFromSetting();

}
