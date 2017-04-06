package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neha on 28/03/17.
 */

public enum AdvanceSearchOptionsType {
    PATIENT_NAME(R.string.patient_name, R.string.enter_patient_name),
    PATIENT_ID(R.string.patient_id, R.string.enter_patient_id),
    MOBILE_NUMBER(R.string.mobile_number, R.string.enter_mobile_number),
    EMAIL(R.string.email, R.string.enter_email),
    BLOOD_GROUP(R.string.blood_group, R.string.select_blood_group, LocalBackgroundTaskType.GET_BLOOD_GROUP),
    PROFESSION(R.string.profession, R.string.select_profession, LocalBackgroundTaskType.GET_PROFESSION_LIST),
    REFERENCE(R.string.reference, R.string.select_reference, LocalBackgroundTaskType.GET_REFERENCE_ACTIVATED_LIST),
    GENERAL_SEARCH(R.string.name_mobile_number, R.string.name_mobile_number);

    private final int valueId;
    private final int hintId;
    private LocalBackgroundTaskType localBackgroundTaskType = null;

    AdvanceSearchOptionsType(int valueId, int hintId, LocalBackgroundTaskType localBackgroundTaskType) {
        this.valueId = valueId;
        this.hintId = hintId;
        this.localBackgroundTaskType = localBackgroundTaskType;
    }


    AdvanceSearchOptionsType(int valueId, int hintId) {
        this.valueId = valueId;
        this.hintId = hintId;
    }

    public int getValueId() {
        return valueId;
    }

    public int getHintId() {
        return hintId;
    }

    public LocalBackgroundTaskType getLocalBackgroundTaskType() {
        return localBackgroundTaskType;
    }

    public static List<Object> getSearchedptionsTypeValues() {
        List<Object> list = new ArrayList<>();
        for (AdvanceSearchOptionsType searchOptionsType :
                AdvanceSearchOptionsType.values()) {
            switch (searchOptionsType) {
                case GENERAL_SEARCH:
                    break;
                default:
                    list.add(searchOptionsType);
                    break;
            }
        }
        return list;
    }
}
