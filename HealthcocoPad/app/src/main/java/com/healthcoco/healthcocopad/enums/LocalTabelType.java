package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.utilities.StringUtil;

import static com.healthcoco.healthcocopad.enums.ClinicalNotesType.DIAGRAMS;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public enum LocalTabelType {
    CITIES, SPECIALITIES, DRUG_DOSAGE, DRUG_DURATION_UNIT, DRUG_DIRECTION, STRENGTH_UNIT, REGISTERED_PATIENTS_DETAILS,
    USER_GROUP(UserGroups.class),
    COMPLAINT, OBSERVATION, INVESTIGATION, DIAGNOSIS, REFERENCE, DISEASE, TEMP_DRUG, RECORDS,
    TEMPLATE, COMPLAINT_SUGGESTIONS, OBSERVATION_SUGGESTIONS, INVESTIGATION_SUGGESTIONS,
    DIAGNOSIS_SUGGESTIONS, DRUG_TYPE, PROFESSION, HISTORY_DETAIL_RESPONSE, VISIT_DETAILS, DIAGRAMS,
    PRESENT_COMPLAINT_SUGGESTIONS,
    HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS,
    NOTES_SUGGESTIONS,
    MENSTRUAL_HISTORY_SUGGESTIONS,
    OBSTETRIC_HISTORY_SUGGESTIONS,
    GENERAL_EXAMINATION_SUGGESTIONS,
    SYSTEMIC_EXAMINATION_SUGGESTIONS,
    PROVISIONAL_DIAGNOSIS_SUGGESTIONS,
    ECG_DETAILS_SUGGESTIONS,
    ECHO_SUGGESTIONS,
    X_RAY_DETAILS_SUGGESTIONS,
    HOLTER_SUGGESTIONS,
    PA_SUGGESTIONS,
    PV_SUGGESTIONS,
    PS_SUGGESTIONS,
    INDICATION_OF_USG_SUGGESTIONS, APPOINTMENT;

    private String tableName = null;

    LocalTabelType(Class<?> tableName) {
        this.tableName = StringUtil.toSQLName(tableName.getSimpleName());
    }

    LocalTabelType() {

    }

    public String getTableName() {
        return tableName;
    }
}