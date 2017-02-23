package com.healthcoco.healthcocoplus.listeners;

import android.view.View;

import com.healthcoco.healthcocoplus.bean.server.Achievement;
import com.healthcoco.healthcocoplus.bean.server.DoctorExperienceDetail;

/**
 * Created by Shreshtha on 21-02-2017.
 */

public interface ExperienceDetailItemListener {
    public void onDeleteExperienceDetailClicked(View view, DoctorExperienceDetail experienceDetail);

    public void addExperienceDetailToList(DoctorExperienceDetail experienceDetail);
}
