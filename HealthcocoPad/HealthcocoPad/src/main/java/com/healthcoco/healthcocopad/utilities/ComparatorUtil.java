package com.healthcoco.healthcocopad.utilities;

import android.text.TextUtils;

import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.CityResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.ReceiptResponse;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.Specialities;
import com.healthcoco.healthcocopad.bean.server.SyncAll;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;

import java.sql.Date;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public class ComparatorUtil {
    public static Comparator<Object> cityListComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            CityResponse city1 = (CityResponse) object1;
            CityResponse city2 = (CityResponse) object2;
            String cityName1 = TextUtils.isEmpty(city1.getCity()) ? "" : city1.getCity().trim().toUpperCase(Locale.getDefault());
            String cityName2 = TextUtils.isEmpty(city2.getCity()) ? "" : city2.getCity().trim().toUpperCase(Locale.getDefault());
            return cityName1.compareTo(cityName2);
        }
    };
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
    public static Comparator<VisitDetails> visitDateComparator = new Comparator<VisitDetails>() {

        @Override
        public int compare(VisitDetails visit1, VisitDetails visit2) {
            try {
                if (visit1.getVisitedTime() != null && visit2.getVisitedTime() != null) {
                    Date date1 = new Date(visit1.getVisitedTime());
                    Date date2 = new Date(visit2.getVisitedTime());
                    return date2.compareTo(date1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
    public static Comparator<Object> dosageListComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            try {
                Date date1 = new Date(((DrugDosage) object1).getCreatedTime());
                Date date2 = new Date(((DrugDosage) object2).getCreatedTime());
                return date2.compareTo(date1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
    public static Comparator<Object> durationUnitListComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            try {
                Date date1 = new Date(((DrugDurationUnit) object1).getCreatedTime());
                Date date2 = new Date(((DrugDurationUnit) object2).getCreatedTime());
                return date2.compareTo(date1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
    public static Comparator<Object> directionListComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            try {
                Date date1 = new Date(((DrugDirection) object1).getCreatedTime());
                Date date2 = new Date(((DrugDirection) object2).getCreatedTime());
                return date2.compareTo(date1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
    public static Comparator<Object> referrefByNameComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            Reference reference1 = (Reference) object1;
            Reference reference2 = (Reference) object2;
            String name1 = TextUtils.isEmpty(reference1.getReference()) ? "" : reference1.getReference().trim().toUpperCase(Locale.getDefault());
            String name2 = TextUtils.isEmpty(reference2.getReference()) ? "" : reference2.getReference().trim().toUpperCase(Locale.getDefault());
            return name1.compareTo(name2);
        }
    };

    public static Comparator<SyncAll> syncListComparator = new Comparator<SyncAll>() {

        @Override
        public int compare(SyncAll syncAll1, SyncAll syncAll2) {
            Integer position1 = syncAll1.getPosition();
            Integer position2 = syncAll2.getPosition();
            return position1.compareTo(position2);
        }
    };

    public static Comparator<TempTemplate> templateListDateComparator = new Comparator<TempTemplate>() {

        @Override
        public int compare(TempTemplate tempTemplate1, TempTemplate tempTemplate2) {
            try {
                if (tempTemplate1.getCreatedTime() != null && tempTemplate2.getCreatedTime() != null) {
                    Date date1 = new Date(tempTemplate1.getCreatedTime());
                    Date date2 = new Date(tempTemplate2.getCreatedTime());
                    return date2.compareTo(date1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
    public static Comparator<Object> updatedTimeComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            try {
                Long updatedTime1 = 0l;
                Long updatedTime2 = 0l;
                if (object1 instanceof UserGroups) {
                    updatedTime1 = ((UserGroups) object1).getUpdatedTime();
                    updatedTime2 = ((UserGroups) object2).getUpdatedTime();
                }
                return updatedTime1.compareTo(updatedTime2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
    public static Comparator<Object> calendarEventsFromToTimeComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            try {
                CalendarEvents calendarEvents1 = (CalendarEvents) object1;
                CalendarEvents calendarEvents2 = (CalendarEvents) object2;
                Float timeFrom1 = 0f;
                Float timeFrom2 = 0f;
                if (calendarEvents1.getTime() != null && calendarEvents1.getTime().getFromTime() != null) {
                    timeFrom1 = calendarEvents1.getTime().getFromTime();
                }
                if (calendarEvents2.getTime() != null && calendarEvents2.getTime().getFromTime() != null) {
                    timeFrom2 = calendarEvents2.getTime().getFromTime();
                }
                long timeInMillis1 = TimeUnit.MINUTES.toMillis(Math.round(timeFrom1));
                long timeInMillis2 = TimeUnit.MINUTES.toMillis(Math.round(timeFrom2));
                Date date1 = new Date(timeInMillis1);
                Date date2 = new Date(timeInMillis2);
                return date1.compareTo(date2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
    public static Comparator<Object> appointmentFromDateComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            try {
                CalendarEvents appointment1 = (CalendarEvents) object1;
                CalendarEvents appointment2 = (CalendarEvents) object2;
                Date date1 = new Date(appointment1.getFromDate());
                Date date2 = new Date(appointment2.getFromDate());
                return date2.compareTo(date1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
    public static Comparator<ClinicalNotes> clinicalNotesDateComparator = new Comparator<ClinicalNotes>() {

        @Override
        public int compare(ClinicalNotes clinicalNotes1, ClinicalNotes clinicalNotes2) {
            if (clinicalNotes1.getCreatedTime() != null && clinicalNotes2.getCreatedTime() != null) {
                Date date1 = new Date(clinicalNotes1.getCreatedTime());
                Date date2 = new Date(clinicalNotes2.getCreatedTime());
                return date2.compareTo(date1);
            }
            return 0;
        }
    };
    public static Comparator<Prescription> prescriptionDateComparator = new Comparator<Prescription>() {

        @Override
        public int compare(Prescription prescription1, Prescription prescription2) {
            Date date1 = new Date(prescription1.getCreatedTime());
            Date date2 = new Date(prescription2.getCreatedTime());
            return date2.compareTo(date1);
        }
    };
    public static Comparator<Records> reportsDateComparator = new Comparator<Records>() {

        @Override
        public int compare(Records records1, Records records2) {
            if (records1.getCreatedTime() != null && records2.getCreatedTime() != null) {
                Date date1 = new Date(records1.getCreatedTime());
                Date date2 = new Date(records2.getCreatedTime());
                return date2.compareTo(date1);
            }
            return 0;
        }
    };

    public static Comparator<Treatments> treatmentDateComparator = new Comparator<Treatments>() {

        @Override
        public int compare(Treatments treatment1, Treatments treatment2) {
            if (treatment1.getCreatedTime() != null && treatment2.getCreatedTime() != null) {
                Date date1 = new Date(treatment1.getCreatedTime());
                Date date2 = new Date(treatment2.getCreatedTime());
                return date2.compareTo(date1);
            }
            return 0;
        }
    };

    public static Comparator<Invoice> invoiceDateComparator = new Comparator<Invoice>() {

        @Override
        public int compare(Invoice invoice1, Invoice invoice2) {
            if (invoice1.getCreatedTime() != null && invoice2.getCreatedTime() != null) {
                Date date1 = new Date(invoice1.getCreatedTime());
                Date date2 = new Date(invoice2.getCreatedTime());
                return date2.compareTo(date1);
            }
            return 0;
        }
    };

    public static Comparator<ReceiptResponse> receiptDateComparator = new Comparator<ReceiptResponse>() {

        @Override
        public int compare(ReceiptResponse receiptResponse1, ReceiptResponse receiptResponse2) {
            if (receiptResponse1.getReceivedDate() != null && receiptResponse2.getReceivedDate() != null) {
                Date date1 = new Date(receiptResponse1.getReceivedDate());
                Date date2 = new Date(receiptResponse2.getReceivedDate());
                return date2.compareTo(date1);
            }
            return 0;
        }
    };
}
