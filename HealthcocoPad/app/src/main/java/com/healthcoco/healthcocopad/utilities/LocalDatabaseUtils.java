package com.healthcoco.healthcocopad.utilities;

import com.google.gson.Gson;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shreshtha on 21-01-2017.
 */
public class LocalDatabaseUtils {
    private static final String TAG = LocalDatabaseUtils.class.getSimpleName();
    public static final String KEY_FOREIGN_CUSTOM_HISTORY_ID = "foreign_custom_history_id";
    public static final String ID = "\"id\"";
    public static final String ID_REPLACED = "\"uniqueId\"";
    public static final String FROM = "\"from\"";
    public static final String FROM_VALUE = "\"fromValue\"";
    public static final String TO = "\"to\"";
    public static final String TO_VALUE = "\"toValue\"";
    public static final String KEY_FOREIGN_UNIQUE_ID = "foreign_unique_id";
    public static final String KEY_DOCTOR_ID = "doctor_id";
    public static final String KEY_PRESCRIBED_BY_DOCTOR_ID = "prescribed_by_doctor_id";
    public static final String KEY_PRESCRIBED_BY_LOCATION_ID = "prescribed_by_location_id";
    public static final String KEY_PRESCRIBED_BY_HOSPITAL_ID = "prescribed_by_hospital_id";
    public static final String KEY_FOREIGN_CLINICAL_NOTES_ID = "foreign_clinical_notes_id";
    public static final String KEY_FOREIGN_TEMPLATE_ID = "foreign_template_id";
    public static final String KEY_FOREIGN_TABLE_KEY = "foreign_table_key";
    public static final String KEY_IS_FROM_CALENDAR_API = "is_from_calendar_api";
    public static final String KEY_IS_ADDED_ON_SUCCESS = "is_added_on_success";
    public static final String KEY_LOCAL_PATIENT_NAME = "local_patient_name";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_PID = "pid";
    public static final String KEY_EMAIL_ADDRESS = "email_address";
    public static final String KEY_REFERENCE = "reference";
    public static final String KEY_REFERRED_BY = "referred_by";
    public static final String KEY_REFERRED_BY_JSON_STRING = "referred_by_json_string";

    public static final String KEY_PROFESSION = "profession";
    public static final String KEY_BLOOD_GROUP = "blood_group";

    public static final String KEY_VISIT_ID = "visit_id";
    public static final String KEY_HOSPITAL_ID = "hospital_id";
    public static final String KEY_LOCATION_ID = "location_id";
    public static final String KEY_PATIENT_ID = "patient_id";
    public static final String KEY_FOREIGN_PRESCRIPTION_ID = "foreign_prescription_id";
    public static final String KEY_FOREIGN_DURATION_ID = "foreign_duration_id";
    public static final String KEY_FOREIGN_GROUP_ID = "foreign_group_id";
    public static final String KEY_FOREIGN_DRUG_DURATION_UNIT_ID = "foreign_drug_duration_unit";
    public static final String KEY_UNIQUE_ID = "unique_id";
    public static final String KEY_APPOINTMENT_ID = "appointment_id";
    public static final String KEY_VISITED_TIME = "visited_time";
    public static final String KEY_DISCARDED = "discarded";
    public static final String KEY_DOSAGE = "dosage";
    public static final String KEY_UNIT = "unit";
    public static final String KEY_DIRECTION = "direction";
    public static final int BOOLEAN_FALSE_VALUE = 0;
    public static final int BOOLEAN_TRUE_VALUE = 1;
    public static final String KEY_NAME = "name";

    public static final String KEY_UPDATED_TIME = "updated_time";
    public static final String IS_DRUG_FROM_GET_DRUGS_LIST = "is_drug_from_get_drugs_list";

    public static final String KEY_FOREIGN_LOCATION_ID = "foreign_location_id";
    public static final String KEY_FOREIGN_TABLE_ID = "foreign_table_id";

    public static final String KEY_CREATED_TIME = "created_time";
    public static final String KEY_CUSTOM_UNIQUE_ID = "custom_unique_id";

    public static final String KEY_FOREIGN_HOSPITAL_ID = "foreign_hospital_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FOREIGN_PATIENT_ID = "foreign_patient_id";
    public static final String JSON_KEY_REFERENCE = "reference";
    public static final String KEY_GROUP_IDS_JSON_STRING = "group_ids_json_string";
    public static final String KEY_TABLE_NAME = "table_name";
    public static String KEY_FOREIGN_ROLE_ID = "foreign_role_id";
    public static final String KEY_FOREIGN_MEDICAL_HISTORY_ID = "foreign_medical_history_id";
    public static final String KEY_HISTORY_FILTER_TYPE = "history_filter_type";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_IS_PART_OF_CONSULTANT_DOCTOR = "is_part_of_consultant_doctor";
    public static final String KEY_CONSULTANT_DOCTOR_IDS_JSON_STRING = "consultant_doctor_ids_json_string";


    public static String getSearchTermEqualsIgnoreCaseQuery(String filedName, String value) {
        return filedName + " LIKE \"%" + value + "%\"" + " COLLATE NOCASE ";
    }

    public static String getSearchFromJsonQuery(String filedName, String jsonKey, String jsonValue) {
        return filedName + " LIKE '%" + getFormattedJsonKeyValuePair(jsonKey, jsonValue) + "%'" + " COLLATE NOCASE ";
    }

    public static String unicodeEscaped(char ch) {
        if (ch < 0x10) {
            return "\\u000" + Integer.toHexString(ch);
        } else if (ch < 0x100) {
            return "\\u00" + Integer.toHexString(ch);
        } else if (ch < 0x1000) {
            return "\\u0" + Integer.toHexString(ch);
        }
        return "\\u" + Integer.toHexString(ch);
    }

    /**
     * Converts the string to the unicode format '\u0020'.
     * <p>
     * This format is the Java source code format.
     * <p>
     * If <code>null</code> is passed in, <code>null</code> will be returned.
     * <p>
     * <pre>
     *   CharUtils.unicodeEscaped(null) = null
     *   CharUtils.unicodeEscaped(' ')  = "\u0020"
     *   CharUtils.unicodeEscaped('A')  = "\u0041"
     * </pre>
     *
     * @param ch the character to convert, may be null
     * @return the escaped unicode string, null if null input
     */
    public static String unicodeEscaped(Character ch) {
        if (ch == null) {
            return null;
        }
        return unicodeEscaped(ch.charValue());
    }


    private static String getFormattedJsonKeyValuePair(String jsonKey, String jsonValue) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(jsonKey, jsonValue);
        String formattedJsonKeyValuePair = new Gson().toJson(hashMap, Map.class);
        return formattedJsonKeyValuePair.substring(1, formattedJsonKeyValuePair.length() - 1);
    }


    public static String getPrefixedColumnsString(Class<?> class1, boolean isMainClass) {
        String tableName = " " + StringUtil.toSQLName(class1.getSimpleName());
        String prefixedClumnNames = " " + tableName + ".[ID],";
        List<Field> fieldsList = Arrays.asList(class1.getDeclaredFields());
        for (Field field : fieldsList) {
            Class<?> fieldClass = field.getType();
            if (SugarRecord.class.isAssignableFrom(fieldClass)) {
                prefixedClumnNames = prefixedClumnNames + getPrefixedColumnsString(fieldClass, false);
            }

            List<Annotation> annotaionsList = Arrays.asList(field.getAnnotations());
            LogUtils.LOGD("Patient", "Filed : " + field.getName());
            String columnName = StringUtil.toSQLName(field.getName());
            boolean isIgnored = false;
            if (field.isAnnotationPresent(Ignore.class)) {
                isIgnored = true;
            }
            if (!isIgnored && !(java.lang.reflect.Modifier.isStatic(field.getModifiers()))) {
                prefixedClumnNames = prefixedClumnNames + tableName + ".[" + columnName + "]";
                prefixedClumnNames = prefixedClumnNames + ",";
            }
        }
        if (prefixedClumnNames.endsWith(",") && isMainClass)
            prefixedClumnNames = prefixedClumnNames.substring(0, prefixedClumnNames.length() - 1);
        LogUtils.LOGD(TAG, "Select Query " + prefixedClumnNames);
        return prefixedClumnNames;
    }
}
