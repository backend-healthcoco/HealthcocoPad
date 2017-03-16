package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public enum CommonListDialogType {

    SPECIALITY(R.string.speciality, R.string.search_speciality, false, null, R.string.no_result_found_for_your_search),
    QUALIFICATION(R.string.qualification, R.string.search_qualification, true, null, R.string.no_result_found_for_search_press_enter),
    COLLEGE_UNIVERSITY_INSTITUTE(R.string.college_university, R.string.search_college_university_institute, true, null, R.string.no_result_found_for_search_press_enter),
    REFERRED_BY(R.string.referred_by, R.string.referred_by_settings, true, null, R.string.no_result_found_for_search_press_enter),
    YEAR(R.string.year, R.string.year, false, null, R.string.no_result_found_for_your_search),
    BLOOD_GROUP(R.string.blood_group, R.string.search_blood_group, false, null, R.string.no_result_found_for_your_search),
    ADVANCED_SEARCH_OPTION(R.string.advance_search, R.string.search_patient, false, null, R.string.no_result_found_for_your_search),
    PROFESSION(R.string.profession, R.string.search_profession, false, null, R.string.no_result_found_for_your_search),
    MEDICAL_COUNCIL(R.string.medical_council, R.string.search_medical_council, true, null, R.string.no_result_found_for_search_press_enter),
    DIAGNOSTIC_TESTS(R.string.lab_test, R.string.search_lab_test, false, ActionbarType.ACTION_BAR_DIALOG, R.string.no_tests_found),
    PROFESSIONAL_MEMBERSHIP(R.string.professional_membership, R.string.search_professional_membership, true, null, R.string.no_result_found_for_search_press_enter),
    CITY(R.string.city, R.string.search_city, false, null, R.string.no_result_found_for_your_search);

    private final int noResultFoundTextId;
    private ActionbarType actionbarType;
    private int hintId;
    private boolean isAddCustomAllowed;
    private int titleId;

    CommonListDialogType(int titleId, int hintId, boolean isAddCustomAllowed, ActionbarType actionbarType, int noResultFoundTextId) {
        this.titleId = titleId;
        this.hintId = hintId;
        this.isAddCustomAllowed = isAddCustomAllowed;
        this.actionbarType = actionbarType;
        this.noResultFoundTextId = noResultFoundTextId;
    }

    public ActionbarType getActionBarType() {
        return actionbarType;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getHint() {
        return hintId;
    }

    public boolean isAddCustomAllowed() {
        return isAddCustomAllowed;
    }

    public int getNoResultFoundTextId() {
        return noResultFoundTextId;
    }
}
