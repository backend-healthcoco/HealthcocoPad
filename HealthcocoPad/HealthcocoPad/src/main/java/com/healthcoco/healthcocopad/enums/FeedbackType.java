package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

import java.util.HashMap;

/**
 * Created by neha on 23/01/16.
 */
public enum FeedbackType {
    HELP_US("HELP_US"), REFERRER("REFERRER"),
    PRESCRIPTION("PRESCRIPTION"), APPOINTMENT("APPOINTMENT"),
    LAB("LAB"), RECOMMENDATION("RECOMMENDATION"),
    REPORT("REPORT"),

    DOCTOR(new HashMap<Integer, Integer>() {
        {
//            put(R.id.tv_name_for_feedback, R.string.doctor_name);
            put(R.id.parent_rating_five_star, R.string.how_was_your_overall_experience_with);
            put(R.id.parent_clinic_name, R.string.which_clinic_did_you_visit);
            put(R.id.parent_health_problem_treatment, R.string.for_which_health_problem);
            put(R.id.parent_experience, R.string.share_your_experience_with);
        }
    });

    private String type;
    private HashMap<Integer, Integer> visibleViewIdsList;

    private FeedbackType(String type) {
        this.type = type;
    }

    FeedbackType(HashMap<Integer, Integer> visibleViewIdsHashMap) {
        this.visibleViewIdsList = visibleViewIdsHashMap;
    }

    public String getType() {
        return type;
    }

    public HashMap<Integer, Integer> getVisibleViewIdsHashMap() {
        return visibleViewIdsList;
    }
}
