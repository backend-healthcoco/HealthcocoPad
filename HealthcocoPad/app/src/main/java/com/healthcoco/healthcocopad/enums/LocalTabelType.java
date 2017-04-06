package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.utilities.StringUtil;

import static com.healthcoco.healthcocopad.enums.ClinicalNotesType.DIAGRAMS;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public enum LocalTabelType {
    CITIES, SPECIALITIES, DRUG_DOSAGE, DRUG_DURATION_UNIT, DRUG_DIRECTION, STRENGTH_UNIT, REGISTERED_PATIENTS_DETAILS,
    USER_GROUP(UserGroups.class),
    COMPLAINT, OBSERVATION, INVESTIGATION, DIAGNOSIS, REFERENCE, DISEASE, TEMP_DRUG, RECORDS, TEMPLATE, COMPLAINT_SUGGESTIONS, OBSERVATION_SUGGESTIONS, INVESTIGATION_SUGGESTIONS, DIAGNOSIS_SUGGESTIONS, DRUG_TYPE, PROFESSION, HISTORY_DETAIL_RESPONSE, VISIT_DETAILS, DIAGRAMS;


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
