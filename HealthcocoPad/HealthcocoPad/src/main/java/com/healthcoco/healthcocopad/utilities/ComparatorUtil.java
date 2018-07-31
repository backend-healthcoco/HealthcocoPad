package com.healthcoco.healthcocopad.utilities;

import android.text.TextUtils;

import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.CityResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.Complaint;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.EcgDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.EchoSuggestions;
import com.healthcoco.healthcocopad.bean.server.Events;
import com.healthcoco.healthcocopad.bean.server.GeneralExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocopad.bean.server.HistoryPresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.HolterSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndicationOfUsgSuggestions;
import com.healthcoco.healthcocopad.bean.server.Investigation;
import com.healthcoco.healthcocopad.bean.server.InvestigationNote;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.Leave;
import com.healthcoco.healthcocopad.bean.server.MenstrualHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.NotesSuggestions;
import com.healthcoco.healthcocopad.bean.server.Observation;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObstetricHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.PaSuggestions;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProvisionalDiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.PsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PvSuggestions;
import com.healthcoco.healthcocopad.bean.server.ReceiptResponse;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.Specialities;
import com.healthcoco.healthcocopad.bean.server.SyncAll;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.calendar.weekview.WeekViewEvent;
import com.healthcoco.healthcocopad.enums.ClassType;

import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    public static Comparator<Object> eventsFromToTimeComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            try {
                Events calendarEvents1 = (Events) object1;
                Events calendarEvents2 = (Events) object2;
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
    public static Comparator<InvestigationNote> investigationNoteDateComparator = new Comparator<InvestigationNote>() {

        @Override
        public int compare(InvestigationNote investigationNote1, InvestigationNote investigationNote2) {
            if (investigationNote1.getCreatedTime() != null && investigationNote2.getCreatedTime() != null) {
                Date date1 = new Date(investigationNote1.getCreatedTime());
                Date date2 = new Date(investigationNote2.getCreatedTime());
                return date2.compareTo(date1);
            }
            return 0;
        }
    };
    public static Comparator<Leave> leaveDateComparator = new Comparator<Leave>() {

        @Override
        public int compare(Leave leave1, Leave leave2) {
            if (leave1.getCreatedTime() != null && leave2.getCreatedTime() != null) {
                Date date1 = new Date(leave1.getCreatedTime());
                Date date2 = new Date(leave2.getCreatedTime());
                return date2.compareTo(date1);
            }
            return 0;
        }
    };
    public static Comparator<WeekViewEvent> weekViewEventComparator = new Comparator<WeekViewEvent>() {

        @Override
        public int compare(WeekViewEvent weekViewEvent, WeekViewEvent weekViewEvent2) {
            if (weekViewEvent.getStartTime() != null && weekViewEvent2.getStartTime() != null) {
                Date date1 = new Date(weekViewEvent.getStartTime().getTimeInMillis());
                Date date2 = new Date(weekViewEvent2.getStartTime().getTimeInMillis());
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

    public static void commonDateComparator(final ClassType classType, final List<?> list) {
        Comparator<Object> commonComparator = new Comparator<Object>() {

            @Override
            public int compare(Object object1, Object object2) {
                try {
                    Date date1 = null;
                    Date date2 = null;
                    switch (classType) {
                        case REFERENCE:
                            date1 = new Date(((Reference) object1).getCreatedTime());
                            date2 = new Date(((Reference) object2).getCreatedTime());
                            break;
                        case HISTORY:
                            date1 = new Date(((Disease) object1).getCreatedTime());
                            date2 = new Date(((Disease) object2).getCreatedTime());
                            break;
                        case DRUG:
                            date1 = new Date(((Drug) object1).getCreatedTime());
                            date2 = new Date(((Drug) object2).getCreatedTime());
                            break;
                        case DIRECTIONS:
                            date1 = new Date(((DrugDirection) object1).getCreatedTime());
                            date2 = new Date(((DrugDirection) object2).getCreatedTime());
                            break;
                        case FREQUENCY_DOSAGE:
                            date1 = new Date(((DrugDosage) object1).getCreatedTime());
                            date2 = new Date(((DrugDosage) object2).getCreatedTime());
                            break;
                        case COMPLAINT:
                            date1 = new Date(((ComplaintSuggestions) object1).getCreatedTime());
                            date2 = new Date(((ComplaintSuggestions) object2).getCreatedTime());
                            break;
                        case PA:
                            date1 = new Date(((PaSuggestions) object1).getCreatedTime());
                            date2 = new Date(((PaSuggestions) object2).getCreatedTime());
                            break;
                        case PRESENT_COMPLAINT:
                            date1 = new Date(((PresentComplaintSuggestions) object1).getCreatedTime());
                            date2 = new Date(((PresentComplaintSuggestions) object2).getCreatedTime());
                            break;
                        case ECG:
                            date1 = new Date(((EcgDetailSuggestions) object1).getCreatedTime());
                            date2 = new Date(((EcgDetailSuggestions) object2).getCreatedTime());
                            break;
                        case DIAGNOSIS:
                            date1 = new Date(((DiagnosisSuggestions) object1).getCreatedTime());
                            date2 = new Date(((DiagnosisSuggestions) object2).getCreatedTime());
                            break;
                        case ECHO:
                            date1 = new Date(((EchoSuggestions) object1).getCreatedTime());
                            date2 = new Date(((EchoSuggestions) object2).getCreatedTime());
                            break;
                        case HISTORY_OF_PRESENT_COMPLAINT:
                            date1 = new Date(((HistoryPresentComplaintSuggestions) object1).getCreatedTime());
                            date2 = new Date(((HistoryPresentComplaintSuggestions) object2).getCreatedTime());
                            break;
                        case GENERAL_EXAMINATION:
                            date1 = new Date(((GeneralExaminationSuggestions) object1).getCreatedTime());
                            date2 = new Date(((GeneralExaminationSuggestions) object2).getCreatedTime());
                            break;
                        case HOLTER:
                            date1 = new Date(((HolterSuggestions) object1).getCreatedTime());
                            date2 = new Date(((HolterSuggestions) object2).getCreatedTime());
                            break;
                        case INDICATION_OF_USG:
                            date1 = new Date(((IndicationOfUsgSuggestions) object1).getCreatedTime());
                            date2 = new Date(((IndicationOfUsgSuggestions) object2).getCreatedTime());
                            break;
                        case INVESTIGATIONS:
                            date1 = new Date(((InvestigationSuggestions) object1).getCreatedTime());
                            date2 = new Date(((InvestigationSuggestions) object2).getCreatedTime());
                            break;
                        case MENSTRUAL_HISTORY:
                            date1 = new Date(((MenstrualHistorySuggestions) object1).getCreatedTime());
                            date2 = new Date(((MenstrualHistorySuggestions) object2).getCreatedTime());
                            break;
                        case OBSERVATION:
                            date1 = new Date(((ObservationSuggestions) object1).getCreatedTime());
                            date2 = new Date(((ObservationSuggestions) object2).getCreatedTime());
                            break;
                        case NOTES:
                            date1 = new Date(((NotesSuggestions) object1).getCreatedTime());
                            date2 = new Date(((NotesSuggestions) object2).getCreatedTime());
                            break;
                        case OBSTETRIC_HISTORY:
                            date1 = new Date(((ObstetricHistorySuggestions) object1).getCreatedTime());
                            date2 = new Date(((ObstetricHistorySuggestions) object2).getCreatedTime());
                            break;
                        case PROVISIONAL_DIAGNOSIS:
                            date1 = new Date(((ProvisionalDiagnosisSuggestions) object1).getCreatedTime());
                            date2 = new Date(((ProvisionalDiagnosisSuggestions) object2).getCreatedTime());
                            break;
                        case PS:
                            date1 = new Date(((PsSuggestions) object1).getCreatedTime());
                            date2 = new Date(((PsSuggestions) object2).getCreatedTime());
                            break;
                        case PV:
                            date1 = new Date(((PvSuggestions) object1).getCreatedTime());
                            date2 = new Date(((PvSuggestions) object2).getCreatedTime());
                            break;
                        case SYSTEMIC_EXAMINATION:
                            date1 = new Date(((SystemicExaminationSuggestions) object1).getCreatedTime());
                            date2 = new Date(((SystemicExaminationSuggestions) object2).getCreatedTime());
                            break;
                        case TREATMENT:
                            date1 = new Date(((TreatmentService) object1).getCreatedTime());
                            date2 = new Date(((TreatmentService) object2).getCreatedTime());
                            break;
                        case XRAY:
                            date1 = new Date(((XrayDetailSuggestions) object1).getCreatedTime());
                            date2 = new Date(((XrayDetailSuggestions) object2).getCreatedTime());
                            break;
                        case GROUPS:
                            date1 = new Date(((UserGroups) object1).getCreatedTime());
                            date2 = new Date(((UserGroups) object2).getCreatedTime());
                            break;


                    }
                    return date2.compareTo(date1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        };
        Collections.sort(list, commonComparator);
    }

}
