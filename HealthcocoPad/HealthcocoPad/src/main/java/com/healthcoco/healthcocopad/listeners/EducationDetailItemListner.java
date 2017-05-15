package com.healthcoco.healthcocopad.listeners;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocopad.bean.server.Education;

/**
 * Created by neha on 03/02/16.
 */
public interface EducationDetailItemListner {
    public void onQualificationClicked(TextView textView, Education education);

    public void onCollegeUniversityClicked(TextView textView, Education education);

    public void onDeleteEducationDetailClicked(View view, Education education);

    public void addEducationDetailToList(Education education);
}
