package com.healthcoco.healthcocoplus.utilities;

import android.text.TextUtils;

import com.healthcoco.healthcocoplus.bean.server.Disease;
import com.healthcoco.healthcocoplus.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.bean.server.Specialities;
import com.healthcoco.healthcocoplus.bean.server.UserGroups;

import java.sql.Date;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public class ComparatorUtil {

    public static Comparator<Object> specialityListComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            Specialities speciality1 = (Specialities) object1;
            Specialities speciality2 = (Specialities) object2;
            String name1 = TextUtils.isEmpty(speciality1.getSpeciality()) ? "" : speciality1.getSpeciality().trim().toUpperCase(Locale.getDefault());
            String name2 = TextUtils.isEmpty(speciality2.getSpeciality()) ? "" : speciality2.getSpeciality().trim().toUpperCase(Locale.getDefault());
            return name1.compareTo(name2);
        }
    };
    public static Comparator<Object> groupDateComparator = new Comparator<Object>() {

        @Override
        public int compare(Object o1, Object o2) {
            UserGroups group1 = (UserGroups) o1;
            UserGroups group2 = (UserGroups) o2;
            if (group1.getCreatedTime() != null && group2.getCreatedTime() != null) {
                Date date1 = new Date(group1.getCreatedTime());
                Date date2 = new Date(group2.getCreatedTime());
                return date2.compareTo(date1);
            }
            return 0;
        }
    };
    public static Comparator<RegisteredPatientDetailsUpdated> patientsNameComparator = new Comparator<RegisteredPatientDetailsUpdated>() {

        @Override
        public int compare(RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated1, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated2) {
            String patientName1 = TextUtils.isEmpty(registeredPatientDetailsUpdated1.getLocalPatientName()) ? "" : registeredPatientDetailsUpdated1.getLocalPatientName().trim().toUpperCase(Locale.getDefault());
            String patientName2 = TextUtils.isEmpty(registeredPatientDetailsUpdated2.getLocalPatientName()) ? "" : registeredPatientDetailsUpdated2.getLocalPatientName().trim().toUpperCase(Locale.getDefault());
            return patientName1.compareTo(patientName2);
        }
    };
    public static Comparator<Object> diseaseDateComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            Disease disease1 = ((Disease) object1);
            Disease disease2 = ((Disease) object2);
            if (disease1.getCreatedTime() != null && disease2.getCreatedTime() != null) {
                Date date1 = new Date(disease1.getCreatedTime());
                Date date2 = new Date(disease2.getCreatedTime());
                return date2.compareTo(date1);
            }
            return 0;
        }
    };
    public static Comparator<HistoryDetailsResponse> historyDateComparator = new Comparator<HistoryDetailsResponse>() {

        @Override
        public int compare(HistoryDetailsResponse history1, HistoryDetailsResponse history2) {
            if (history1.getCreatedTime() != null && history2.getCreatedTime() != null) {
                Date date1 = new Date(history1.getCreatedTime());
                Date date2 = new Date(history2.getCreatedTime());
                return date2.compareTo(date1);
            }
            return 0;
        }
    };
}
