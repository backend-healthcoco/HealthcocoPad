package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

/**
 * Created by Prashant on 21/02/2018.
 */
@Parcel
public class DataDynamicField extends SugarRecord {
    @Ignore
    private PrescriptionDynamicField prescriptionDynamicField;
    @Ignore
    private ClinicalNotesDynamicField clinicalNotesDynamicField;
    @Ignore
    private DischargeSummaryDynamicFields dischargeSummaryDynamicFields;
    @Ignore
    private TreatmentDynamicFields treatmentDynamicFields;

    public PrescriptionDynamicField getPrescriptionDynamicField() {
        return prescriptionDynamicField;
    }

    public void setPrescriptionDynamicField(PrescriptionDynamicField prescriptionDynamicField) {
        this.prescriptionDynamicField = prescriptionDynamicField;
    }

    public ClinicalNotesDynamicField getClinicalNotesDynamicField() {
        return clinicalNotesDynamicField;
    }

    public void setClinicalNotesDynamicField(ClinicalNotesDynamicField clinicalNotesDynamicField) {
        this.clinicalNotesDynamicField = clinicalNotesDynamicField;
    }

    public DischargeSummaryDynamicFields getDischargeSummaryDynamicFields() {
        return dischargeSummaryDynamicFields;
    }

    public void setDischargeSummaryDynamicFields(DischargeSummaryDynamicFields dischargeSummaryDynamicFields) {
        this.dischargeSummaryDynamicFields = dischargeSummaryDynamicFields;
    }

    public TreatmentDynamicFields getTreatmentDynamicFields() {
        return treatmentDynamicFields;
    }

    public void setTreatmentDynamicFields(TreatmentDynamicFields treatmentDynamicFields) {
        this.treatmentDynamicFields = treatmentDynamicFields;
    }
}
