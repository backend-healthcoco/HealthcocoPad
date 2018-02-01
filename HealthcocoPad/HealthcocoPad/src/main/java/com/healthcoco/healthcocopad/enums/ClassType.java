package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.EcgDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.EchoSuggestions;
import com.healthcoco.healthcocopad.bean.server.GeneralExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.HistoryPresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.HolterSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndicationOfUsgSuggestions;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.MenstrualHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.NotesSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObstetricHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.PaSuggestions;
import com.healthcoco.healthcocopad.bean.server.Patient;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProvisionalDiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.PsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PvSuggestions;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;

/**
 * Created by Shreshtha on 25-02-2017.
 */
public enum ClassType {
    GROUPS(UserGroups.class),
    REFERENCE(Reference.class),
    HISTORY(Disease.class),
    UI_PERMISSION(null),
    DRUG(Drug.class),
    TEMPLATE(TempTemplate.class),
    DIRECTIONS(DrugDirection.class),
    FREQUENCY_DOSAGE(DrugDosage.class),
    TREATMENT(Treatments.class),
    PATIENT(Patient.class),
    CLINICAL_NOTES(ClinicalNotes.class),
    PRESCRIPTION(Prescription.class),
    BILLING(null),

//    CLINICAL_NOTE(ClinicalNotes.class),

    PRESENT_COMPLAINT(PresentComplaintSuggestions.class),

    COMPLAINT(ComplaintSuggestions.class),

    HISTORY_OF_PRESENT_COMPLAINT(HistoryPresentComplaintSuggestions.class),

    MENSTRUAL_HISTORY(MenstrualHistorySuggestions.class),

    OBSTETRIC_HISTORY(ObstetricHistorySuggestions.class),

    GENERAL_EXAMINATION(GeneralExaminationSuggestions.class),

    SYSTEMIC_EXAMINATION(SystemicExaminationSuggestions.class),

    OBSERVATION(ObservationSuggestions.class),

    INVESTIGATIONS(InvestigationSuggestions.class),

    PROVISIONAL_DIAGNOSIS(ProvisionalDiagnosisSuggestions.class),

    DIAGNOSIS(DiagnosisSuggestions.class),

    ECG(EcgDetailSuggestions.class),

    ECHO(EchoSuggestions.class),

    XRAY(XrayDetailSuggestions.class),

    HOLTER(HolterSuggestions.class),

    PA(PaSuggestions.class),

    PV(PvSuggestions.class),

    PS(PsSuggestions.class),

    INDICATION_OF_USG(IndicationOfUsgSuggestions.class),

    NOTES(NotesSuggestions.class);

    private final Class<?> class1;

    ClassType(Class<?> class1) {
        this.class1 = class1;
    }

    public Class<?> getClassType() {
        return class1;
    }
}
