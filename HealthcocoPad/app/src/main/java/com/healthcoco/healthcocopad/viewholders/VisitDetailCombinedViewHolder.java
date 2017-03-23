package com.healthcoco.healthcocopad.viewholders;

import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.BloodPressure;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.Complaint;
import com.healthcoco.healthcocopad.bean.server.Diagnoses;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTestsPrescription;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.Investigation;
import com.healthcoco.healthcocopad.bean.server.Notes;
import com.healthcoco.healthcocopad.bean.server.Observation;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.bean.server.VitalSigns;
import com.healthcoco.healthcocopad.enums.ClinicalNotesPermissionType;
import com.healthcoco.healthcocopad.enums.ClinicalNotesType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.List;

/**
 * Created by neha on 02/09/16.
 */
public class VisitDetailCombinedViewHolder extends HealthCocoViewHolder implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private static final String DEFAULT_SUFIX_VITAL_SIGNS = ", ";
    private VisitDetails visitDetail;
    private LinearLayout layoutVitalSigns;
    //    private LinearLayout layoutDiagrams;
    private LinearLayout containerDiagrams;
    private TextViewFontAwesome btPrint;
    private TextViewFontAwesome btSms;
    private TextViewFontAwesome btEdit;
    private TextViewFontAwesome btEmail;
    private LinearLayout containerDrugsList;
    private LinearLayout layoutDiscardedClinicalNote;
    private LinearLayout layoutPrescriptionDiscarded;
    private LinearLayout layoutTreatmentDiscarded;
    private LinearLayout parentDiagnosticTests;
    private LinearLayout containerDiagnosticTests;
    private ClinicalNotes clinicalNote;

    //permissions Views
    private LinearLayout layoutComplaints;
    private TextViewFontAwesome tvComplaints;
    private LinearLayout layoutObservations;
    private TextView tvObservations;
    private LinearLayout layoutInvestigations;
    private TextView tvInvestigations;
    private LinearLayout layoutDiagnosis;
    private TextView tvDiagnoses;
    private TextView tvNotes;
    private LinearLayout layoutNotes;
    private LinearLayout layoutPresentComplaints;
    private TextViewFontAwesome tvPresentComplaints;
    private LinearLayout layoutPresentComplaintsHistory;
    private TextViewFontAwesome tvHistoryPresentComplaints;
    private LinearLayout layoutMenstrualHistory;
    private TextViewFontAwesome tvMenstrualHistory;
    private LinearLayout layoutObstetricHistory;
    private TextViewFontAwesome tvObstetricHistory;
    private LinearLayout layoutGeneralExamination;
    private TextViewFontAwesome tvGeneralExamination;
    private LinearLayout layoutSystemExamination;
    private TextViewFontAwesome tvSystemExamination;
    private LinearLayout layoutProvisionalDiagnosis;
    private TextViewFontAwesome tvProvisionalDiagnosis;
    private LinearLayout layoutECGDetails;
    private TextViewFontAwesome tvEcgDetails;
    private LinearLayout layoutEcho;
    private TextViewFontAwesome tvEcho;
    private LinearLayout layoutXrayDetails;
    private TextViewFontAwesome tvXrayDetails;
    private LinearLayout layoutHolter;
    private TextViewFontAwesome tvHolter;
    private LinearLayout layoutPA;
    private TextViewFontAwesome tvPA;
    private LinearLayout layoutPV;
    private TextViewFontAwesome tvPV;
    private LinearLayout layoutPS;
    private TextViewFontAwesome tvPS;
    private LinearLayout layoutInidicationOfUsg;
    private TextViewFontAwesome tvIndicationOfUSG;
    private TextView tvVID;
    private VisitDetailCombinedItemListener listItemClickListener;
    private TextView tvCreatedBy;
    private TextView tvVisitDate;
    private LinearLayout layoutDiscardedTreatment;

    public VisitDetailCombinedViewHolder(HealthCocoActivity mActivity, VisitDetailCombinedItemListener listItemClickListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.listItemClickListener = listItemClickListener;
    }

    @Override
    public void setData(Object object) {
        this.visitDetail = (VisitDetails) object;
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_patient_visits_combined, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View contentView) {
        initClinicalNotesView(contentView);
        initPrescriptionsView(contentView);
        initTreatmentsView(contentView);
        initHeaderViews(contentView);
        btSms = (TextViewFontAwesome) contentView.findViewById(R.id.bt_sms);
        btEdit = (TextViewFontAwesome) contentView.findViewById(R.id.bt_edit);
        btEmail = (TextViewFontAwesome) contentView.findViewById(R.id.bt_email);
        btPrint = (TextViewFontAwesome) contentView.findViewById(R.id.bt_print);
    }

    private void initHeaderViews(View contentView) {
        tvVID = (TextView) contentView.findViewById(R.id.tv_vid);
        tvCreatedBy = (TextView) contentView.findViewById(R.id.tv_created_by_visit);
        tvVisitDate = (TextView) contentView.findViewById(R.id.tv_visit_date);
    }

    private void initClinicalNotesView(View contentView) {
        layoutVitalSigns = (LinearLayout) contentView.findViewById(R.id.layout_vital_signs);
        layoutPresentComplaints = (LinearLayout) contentView.findViewById(R.id.layout_present_complaints);
        tvPresentComplaints = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_present_complaints);
        layoutComplaints = (LinearLayout) contentView.findViewById(R.id.layout_complaints);
        tvComplaints = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_complaints);
        layoutPresentComplaintsHistory = (LinearLayout) contentView.findViewById(R.id.layout_history_of_present_complaints);
        tvHistoryPresentComplaints = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_history_of_present_complaints);
        layoutMenstrualHistory = (LinearLayout) contentView.findViewById(R.id.layout_menstrual_history);
        tvMenstrualHistory = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_menstrual_history);
        layoutObstetricHistory = (LinearLayout) contentView.findViewById(R.id.layout_obstetric_history);
        tvObstetricHistory = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_obstetric_history);
        layoutGeneralExamination = (LinearLayout) contentView.findViewById(R.id.layout_general_examination);
        tvGeneralExamination = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_general_examination);
        layoutSystemExamination = (LinearLayout) contentView.findViewById(R.id.layout_system_examination);
        tvSystemExamination = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_system_examination);
        layoutObservations = (LinearLayout) contentView.findViewById(R.id.layout_observations);
        tvObservations = (TextView) contentView.findViewById(R.id.tv_text_observations);
        layoutInvestigations = (LinearLayout) contentView.findViewById(R.id.layout_investigations);
        tvInvestigations = (TextView) contentView.findViewById(R.id.tv_text_investigations);
        layoutProvisionalDiagnosis = (LinearLayout) contentView.findViewById(R.id.layout_provisional_diagnosis);
        tvProvisionalDiagnosis = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_provision_diagnosis);
        layoutDiagnosis = (LinearLayout) contentView.findViewById(R.id.layout_diagnosis);
        tvDiagnoses = (TextView) contentView.findViewById(R.id.tv_text_diagnoses);
        layoutECGDetails = (LinearLayout) contentView.findViewById(R.id.layout_ecg_details);
        tvEcgDetails = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_ecg_details);
        layoutEcho = (LinearLayout) contentView.findViewById(R.id.layout_echo);
        tvEcho = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_echo);
        layoutXrayDetails = (LinearLayout) contentView.findViewById(R.id.layout_xray_details);
        tvXrayDetails = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_xray_details);
        layoutHolter = (LinearLayout) contentView.findViewById(R.id.layout_holter);
        tvHolter = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_holter);
        layoutPA = (LinearLayout) contentView.findViewById(R.id.layout_pa);
        tvPA = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_pa);
        layoutPV = (LinearLayout) contentView.findViewById(R.id.layout_pv);
        tvPV = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_pv);
        layoutPS = (LinearLayout) contentView.findViewById(R.id.layout_ps);
        tvPS = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_ps);
        layoutInidicationOfUsg = (LinearLayout) contentView.findViewById(R.id.layout_inidication_of_usg);
        tvIndicationOfUSG = (TextViewFontAwesome) contentView.findViewById(R.id.tv_text_inidication_of_usg);

        layoutNotes = (LinearLayout) contentView.findViewById(R.id.layout_notes);
        tvNotes = (TextView) contentView.findViewById(R.id.tv_text_notes);
//        layoutDiagrams = (LinearLayout) contentView.findViewById(R.id.layout_diagrams);
        containerDiagrams = (LinearLayout) contentView.findViewById(R.id.container_diagrams);
        layoutDiscardedClinicalNote = (LinearLayout) contentView.findViewById(R.id.layout_discarded_clinical_note);
        layoutDiscardedTreatment = (LinearLayout) contentView.findViewById(R.id.layout_discarded_treatment);
    }

    private void initPrescriptionsView(View contentView) {
        containerDrugsList = (LinearLayout) contentView.findViewById(R.id.container_drugs_list);
        layoutPrescriptionDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded_prescription);
        parentDiagnosticTests = (LinearLayout) contentView.findViewById(R.id.parent_diagnostic_tests);
        containerDiagnosticTests = (LinearLayout) contentView.findViewById(R.id.container_diagnostic_tests);
    }

    private void initTreatmentsView(View contentView) {
        layoutTreatmentDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded_treatment);
    }

    private void initListeners() {
        btPrint.setOnClickListener(this);
        btEdit.setOnClickListener(this);
        btEmail.setOnClickListener(this);
        btSms.setOnClickListener(this);
    }

    @Override
    public void applyData() {
        if (!Util.isNullOrEmptyList(visitDetail.getClinicalNotes())) {
            List<ClinicalNotes> clinicalNotesList = visitDetail.getClinicalNotes();
            applyClinicalNoteData(clinicalNotesList.get(0));
        }
        if (!Util.isNullOrEmptyList(visitDetail.getPrescriptions())) {
            List<Prescription> prescriptionsList = visitDetail.getPrescriptions();
            applyPrescriptionsData(prescriptionsList.get(0));
        }
        tvVID.setText(mActivity.getResources().getString(R.string.vid) + Util.getValidatedValue(visitDetail.getUniqueEmrId()));
        tvCreatedBy.setText(Util.getValidatedValue(visitDetail.getCreatedBy()));
        tvVisitDate.setText(DateTimeUtil.getFormatedDate(visitDetail.getCreatedTime()));
    }

    private void applyPrescriptionsData(Prescription prescription) {
        containerDrugsList.removeAllViews();
        if (!Util.isNullOrEmptyList(prescription.getItems())) {
            containerDrugsList.setVisibility(View.VISIBLE);
            for (DrugItem drug : prescription.getItems()) {
                int index = prescription.getItems().indexOf(drug);
//                PrescribedDrugDoseItemViewholder view = new PrescribedDrugDoseItemViewholder(mActivity);
//                view.setData(drug);
//                containerDrugsList.addView(view);
//                if (index != prescription.getItems().size() - 1 || !Util.isNullOrEmptyList(prescription.getDiagnosticTests()))
//                    view.setDividerVisibility(true);
//                else
//                    view.setDividerVisibility(false);
            }
        } else
            containerDrugsList.setVisibility(View.GONE);

        checkIsPrescriptionDiscarded(prescription.getDiscarded());
        initDiagnosticTests(prescription);
    }

    private void applyClinicalNoteData(ClinicalNotes clinicalNote) {
        this.clinicalNote = clinicalNote;
        clearAllViews();
        VitalSigns vitalSigns = clinicalNote.getVitalSigns();
        if (vitalSigns != null && !vitalSigns.areAllFieldsNull()) {
            layoutVitalSigns.setVisibility(View.VISIBLE);
            String text = "";
            if (!Util.isNullOrBlank(vitalSigns.getPulse()))
                text = text + vitalSigns.getFormattedPulse(mActivity, vitalSigns.getPulse()) + DEFAULT_SUFIX_VITAL_SIGNS;

            if (!Util.isNullOrBlank(vitalSigns.getTemperature()))
                text = text + vitalSigns.getFormattedTemprature(mActivity, vitalSigns.getTemperature()) + DEFAULT_SUFIX_VITAL_SIGNS;
            if (!Util.isNullOrBlank(vitalSigns.getWeight()))
                text = text + vitalSigns.getFormattedWeight(mActivity, vitalSigns.getWeight()) + DEFAULT_SUFIX_VITAL_SIGNS;
            BloodPressure bloodPressure = vitalSigns.getBloodPressure();
            if (bloodPressure != null && !Util.isNullOrBlank(bloodPressure.getSystolic()) && !Util.isNullOrBlank(bloodPressure.getDiastolic())) {
                text = text + vitalSigns.getFormattedBloodPressureValue(mActivity, bloodPressure) + DEFAULT_SUFIX_VITAL_SIGNS;
            }
            if (!Util.isNullOrBlank(vitalSigns.getBreathing()))
                text = text + vitalSigns.getFormattedBreathing(mActivity, vitalSigns.getBreathing()) + DEFAULT_SUFIX_VITAL_SIGNS;
            if (!Util.isNullOrBlank(vitalSigns.getSpo2()))
                text = text + vitalSigns.getFormattedSpo2(mActivity, vitalSigns.getSpo2()) + DEFAULT_SUFIX_VITAL_SIGNS;
            if (text.endsWith(DEFAULT_SUFIX_VITAL_SIGNS))
                text = text.substring(0, text.length() - DEFAULT_SUFIX_VITAL_SIGNS.length());
//            tvVitalSIgnsDetails.setText(text);
        }

        if (!Util.isNullOrBlank(clinicalNote.getPresentComplaint())) {
            layoutPresentComplaints.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PRESENT_COMPLAINT, clinicalNote.getPresentComplaint());
        }
        if (!Util.isNullOrBlank(clinicalNote.getComplaint())) {
            layoutComplaints.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.COMPLAINT, clinicalNote.getComplaint());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPresentComplaintHistory())) {
            layoutPresentComplaintsHistory.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT, clinicalNote.getPresentComplaintHistory());
        }
        if (!Util.isNullOrBlank(clinicalNote.getMenstrualHistory())) {
            layoutMenstrualHistory.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.MENSTRUAL_HISTORY, clinicalNote.getMenstrualHistory());
        }
        if (!Util.isNullOrBlank(clinicalNote.getObstetricHistory())) {
            layoutObstetricHistory.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.OBSTETRIC_HISTORY, clinicalNote.getObstetricHistory());
        }
        if (!Util.isNullOrBlank(clinicalNote.getGeneralExam())) {
            layoutGeneralExamination.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.GENERAL_EXAMINATION, clinicalNote.getGeneralExam());
        }
        if (!Util.isNullOrBlank(clinicalNote.getSystemExam())) {
            layoutSystemExamination.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION, clinicalNote.getSystemExam());
        }
        if (!Util.isNullOrBlank(clinicalNote.getObservation())) {
            layoutObservations.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.OBSERVATION, clinicalNote.getObservation());
        }
        if (!Util.isNullOrBlank(clinicalNote.getInvestigation())) {
            layoutInvestigations.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.INVESTIGATIONS, clinicalNote.getInvestigation());
        }
        if (!Util.isNullOrBlank(clinicalNote.getProvisionalDiagnosis())) {
            layoutProvisionalDiagnosis.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS, clinicalNote.getProvisionalDiagnosis());
        }
        if (!Util.isNullOrBlank(clinicalNote.getDiagnosis())) {
            layoutDiagnosis.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.DIAGNOSIS, clinicalNote.getDiagnosis());
        }
        if (!Util.isNullOrBlank(clinicalNote.getEcgDetails())) {
            layoutECGDetails.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.ECG, clinicalNote.getEcgDetails());
        }
        if (!Util.isNullOrBlank(clinicalNote.getEcho())) {
            layoutEcho.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.ECHO, clinicalNote.getEcho());
        }
        if (!Util.isNullOrBlank(clinicalNote.getxRayDetails())) {
            layoutXrayDetails.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.XRAY, clinicalNote.getxRayDetails());
        }
        if (!Util.isNullOrBlank(clinicalNote.getHolter())) {
            layoutHolter.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.HOLTER, clinicalNote.getHolter());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPa())) {
            layoutPA.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PA, clinicalNote.getPa());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPv())) {
            layoutPV.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PV, clinicalNote.getPv());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPs())) {
            layoutPS.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PS, clinicalNote.getPs());
        }
        if (!Util.isNullOrBlank(clinicalNote.getIndicationOfUSG())) {
            layoutInidicationOfUsg.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.INDICATION_OF_USG, clinicalNote.getIndicationOfUSG());
        }
        if (!Util.isNullOrBlank(clinicalNote.getNote())) {
            layoutNotes.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.NOTES, clinicalNote.getNote());
        }
        if (!Util.isNullOrEmptyList(clinicalNote.getDiagrams())) {
//            layoutDiagrams.setVisibility(View.VISIBLE);
            initDiagramsPagerAdapter(clinicalNote.getDiagrams());
        }
        checkIsClinicalNoteDiscarded(clinicalNote.getDiscarded());

    }

    private void initSuggestionsText(ClinicalNotesPermissionType clinicalNotesType, String text) {
        switch (clinicalNotesType) {
            case PRESENT_COMPLAINT:
                tvPresentComplaints.setText(text);
                break;
            case COMPLAINT:
                tvComplaints.setText(text);
                break;
            case HISTORY_OF_PRESENT_COMPLAINT:
                tvHistoryPresentComplaints.setText(text);
                break;
            case MENSTRUAL_HISTORY:
                tvMenstrualHistory.setText(text);
                break;
            case OBSTETRIC_HISTORY:
                tvObstetricHistory.setText(text);
                break;
            case GENERAL_EXAMINATION:
                tvGeneralExamination.setText(text);
                break;
            case SYSTEMIC_EXAMINATION:
                tvSystemExamination.setText(text);
                break;
            case INVESTIGATIONS:
                tvInvestigations.setText(text);
                break;
            case OBSERVATION:
                tvObservations.setText(text);
                break;
            case PROVISIONAL_DIAGNOSIS:
                tvProvisionalDiagnosis.setText(text);
                break;
            case DIAGNOSIS:
                tvDiagnoses.setText(text);
                break;
            case ECG:
                tvEcgDetails.setText(text);
                break;
            case ECHO:
                tvEcho.setText(text);
                break;
            case XRAY:
                tvXrayDetails.setText(text);
                break;
            case HOLTER:
                tvHolter.setText(text);
                break;
            case PA:
                tvPA.setText(text);
                break;
            case PV:
                tvPV.setText(text);
                break;
            case PS:
                tvPS.setText(text);
                break;
            case INDICATION_OF_USG:
                tvIndicationOfUSG.setText(text);
                break;
            case NOTES:
                tvNotes.setText(text);
                break;
        }
    }


    private void initDiagnosticTests(Prescription prescription) {
        containerDiagnosticTests.removeAllViews();
        boolean isAdded = false;
        if (!Util.isNullOrEmptyList(prescription.getDiagnosticTests())) {
            List<DiagnosticTestsPrescription> testsPrescriptionsList = prescription.getDiagnosticTests();
            for (DiagnosticTestsPrescription testsPrescription :
                    testsPrescriptionsList) {
                if (testsPrescription.getTest() != null && !Util.isNullOrBlank(testsPrescription.getTest().getTestName())) {
                    isAdded = true;
                    DiagnosticTest diagnosticTest = testsPrescription.getTest();
//                    View itemDiagnosticTest = inflater.inflate(R.layout.item_diagnostic_test_prescription_point, null);
//                    TextView tvTest = (TextView) itemDiagnosticTest.findViewById(R.id.tv_text);
//                    tvTest.setText(Util.getValidatedValue(diagnosticTest.getTestName()));
//                    containerDiagnosticTests.addView(itemDiagnosticTest);
                }
            }
        }
        if (isAdded) {
            parentDiagnosticTests.setVisibility(View.VISIBLE);
        } else {
            parentDiagnosticTests.setVisibility(View.GONE);
        }
    }

    private void checkIsPrescriptionDiscarded(boolean isDiscarded) {
        if (isDiscarded)
            layoutPrescriptionDiscarded.setVisibility(View.VISIBLE);
        else layoutPrescriptionDiscarded.setVisibility(View.GONE);
    }

    private void checkIsClinicalNoteDiscarded(boolean isDiscarded) {
        if (isDiscarded)
            layoutDiscardedClinicalNote.setVisibility(View.VISIBLE);
        else layoutDiscardedClinicalNote.setVisibility(View.GONE);
    }

    private void checkIsTreatmentDiscarded(boolean isDiscarded) {
        if (isDiscarded)
            layoutDiscardedTreatment.setVisibility(View.VISIBLE);
        else layoutDiscardedTreatment.setVisibility(View.GONE);
    }

    private void clearAllViews() {
        tvPresentComplaints.setText("");
        tvComplaints.setText("");
        tvHistoryPresentComplaints.setText("");
        tvMenstrualHistory.setText("");
        tvObstetricHistory.setText("");
        tvGeneralExamination.setText("");
        tvSystemExamination.setText("");
        tvObservations.setText("");
        tvInvestigations.setText("");
        tvProvisionalDiagnosis.setText("");
        tvDiagnoses.setText("");
        tvEcgDetails.setText("");
        tvEcho.setText("");
        tvXrayDetails.setText("");
        tvHolter.setText("");
        tvPA.setText("");
        tvPV.setText("");
        tvPS.setText("");
        tvIndicationOfUSG.setText("");
        tvNotes.setText("");
        containerDiagrams.removeAllViews();

        layoutPresentComplaints.setVisibility(View.GONE);
        layoutComplaints.setVisibility(View.GONE);
        layoutPresentComplaintsHistory.setVisibility(View.GONE);
        layoutMenstrualHistory.setVisibility(View.GONE);
        layoutObstetricHistory.setVisibility(View.GONE);
        layoutGeneralExamination.setVisibility(View.GONE);
        layoutSystemExamination.setVisibility(View.GONE);
        layoutObservations.setVisibility(View.GONE);
        layoutInvestigations.setVisibility(View.GONE);
        layoutProvisionalDiagnosis.setVisibility(View.GONE);
        layoutDiagnosis.setVisibility(View.GONE);
        layoutECGDetails.setVisibility(View.GONE);
        layoutEcho.setVisibility(View.GONE);
        layoutXrayDetails.setVisibility(View.GONE);
        layoutHolter.setVisibility(View.GONE);
        layoutPA.setVisibility(View.GONE);
        layoutPV.setVisibility(View.GONE);
        layoutPS.setVisibility(View.GONE);
        layoutInidicationOfUsg.setVisibility(View.GONE);
        layoutNotes.setVisibility(View.GONE);
//        layoutDiagrams.setVisibility(View.GONE);
        layoutVitalSigns.setVisibility(View.GONE);
    }

    private void initClinicalNotesSubViews(ClinicalNotesType clinicalNotesType, List<Object> complaints) {
        for (Object object : complaints) {
//            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.item_text_clinical_notes, null);
//
//            if (complaints.indexOf(object) == complaints.size() - 1)
//                setValueAndAddView(true, clinicalNotesType, layout, object);
//            else
//                setValueAndAddView(false, clinicalNotesType, layout, object);
        }
    }

    private void setValueAndAddView(boolean isLastText, ClinicalNotesType clinicalNotesType, LinearLayout textView, Object object) {
        switch (clinicalNotesType) {
            case COMPLAINTS:
                Complaint complaint = (Complaint) object;
                setTextToTextViewIn(tvComplaints, complaint.getComplaint());
                break;
            case OBSERVATIONS:
                Observation observation = (Observation) object;
                setTextToTextViewIn(tvObservations, observation.getObservation());
                break;
            case INVESTIGATION:
                Investigation investigation = (Investigation) object;
                setTextToTextViewIn(tvInvestigations, investigation.getInvestigation());
                break;
            case DIAGNOSIS:
                Diagnoses diagnoses = (Diagnoses) object;
                setTextToTextViewIn(tvDiagnoses, diagnoses.getDiagnosis());
                break;
            case NOTE:
                Notes notes = (Notes) object;
                setTextToTextViewIn(tvNotes, notes.getNote());
                break;
        }
    }

    /**
     * setting text to textview having bullet color as blue_tranlucent and text color as black
     *
     * @param tvTextView: textview where text is to be displayed
     * @param text:       text to be appneded
     */
    private void setTextToTextViewIn(TextView tvTextView, String text) {
        String textModified = mActivity.getResources().getString(R.string.bullet_with_space) + text;
        tvTextView.setText(Util.getValidatedValueOrBlankWithoutTrimming(tvTextView) + Html.fromHtml(textModified) + "  ");
    }

    private void initDiagramsPagerAdapter(List<Diagram> list) {
        LogUtils.LOGD(TAG, "Diagrams size " + list.size());
        containerDiagrams.removeAllViews();
        for (Diagram diagram :
                list) {
            if (!Util.isNullOrBlank(diagram.getDiagramUrl())) {
                int index = list.indexOf(diagram);
                ClinicalNoteItemDiagramViewHolder viewHolder = new ClinicalNoteItemDiagramViewHolder(mActivity);
                viewHolder.initData(diagram);
                if (index < list.size() - 1) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mActivity.getResources().getDimensionPixelOffset(R.dimen.width_height_clinical_note_item_diagram), mActivity.getResources().getDimensionPixelOffset(R.dimen.width_height_clinical_note_item_diagram));
                    layoutParams.setMargins(0, 0, mActivity.getResources().getDimensionPixelOffset(R.dimen.padding_between_clinical_item_diagram), 0);
                    viewHolder.setLayoutParams(layoutParams);
                }
                containerDiagrams.addView(viewHolder);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {

    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sms:
                listItemClickListener.sendSms("");
                break;
            case R.id.bt_edit:
                listItemClickListener.editVisit("");
                break;
            case R.id.bt_email:
                listItemClickListener.sendEmail("");
                break;
            case R.id.bt_print:
                listItemClickListener.doPrint(visitDetail.getUniqueId());
                break;
        }
    }
}
