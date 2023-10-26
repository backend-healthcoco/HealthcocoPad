package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.BloodPressure;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VitalSigns;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.ClinicalNotesPermissionType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.AddClinicalNotesVisitNormalFragment;
import com.healthcoco.healthcocopad.fragments.CommonOpenUpPatientDetailFragment;
import com.healthcoco.healthcocopad.fragments.PatientClinicalNotesDetailFragment;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.HorizontalRecyclerViewItemDecoration;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neha on 19/03/16.
 */
public class ClinicalNotesListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, HealthcocoRecyclerViewItemClickListener {

    private static final String TAG = ClinicalNotesListItemViewHolder.class.getSimpleName();
    private static final int REQUEST_CODE_CLINICAL_NOTES = 111;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, dd MMM yyyy";
    private CommonEMRItemClickListener commonEMRItemClickListener;
    private User user;
    private String loginedUser;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private VisitDetailCombinedItemListener detailCombinedItemListener;
    private LinearLayout layoutDiscarded;
    private LinearLayout btHistory;
    private LinearLayout btEdit;
    private TextView tvDate;
    private ClinicalNotes clinicalNote;
    private LinearLayout btEmail;
    private TextView tvNotedBy;
    private RecyclerView containerDiagrams;
    private LinearLayout layoutDiagrams;
    private TextView tvCNID;
    private LinearLayout layoutVitalSigns;
    //    private TextView tvVitalSIgnsDetails;
    private LinearLayout containerBottomButtons;
    private TextView tvLabelGlobalRecord;
    private TextView tvLabelNotedBy;
    private LinearLayout btPrint;
    //permissions Views
    private TextView tvComplaints;
    private TextView tvObservations;
    private TextView tvInvestigations;
    private TextView tvDiagnoses;
    private TextView tvNotes;
    private LinearLayout layoutComplaints;
    private LinearLayout layoutObservations;
    private LinearLayout layoutInvestigations;
    private LinearLayout layoutDiagnosis;
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
    private TextView tvBodyTemprature;
    private TextView tvWeight;
    private TextView tvHeartRate;
    private TextView tvBloodPressure;
    private TextView tvRespiratoryRate;
    private TextView tvSpo2;
    private TextView tvBsa;
    private TextView tvBmi;
    private TextView tvHeight;
    private ImageView imageView;
    private LinearLayout btDiscard;
    private TextView textViewNextReviewDate;
    private LinearLayout layoutNextReviewDetail;
    private LinearLayout layoutEntComplaints;
    private LinearLayout layoutPcNose;
    private LinearLayout layoutPcEars;
    private LinearLayout layoutPcThroat;
    private LinearLayout layoutPcOralCavity;
    private LinearLayout layoutPersonalHistory;
    private LinearLayout layoutTobacco;
    private LinearLayout layoutAlcohol;
    private LinearLayout layoutSmoking;
    private LinearLayout layoutDiet;
    private LinearLayout layoutOccupation;
    private LinearLayout layoutGeneralHistory;
    private LinearLayout layoutDrugs;
    private LinearLayout layoutAllergies;
    private LinearLayout layoutMedical;
    private LinearLayout layoutSurgical;
    private LinearLayout layoutEntExamination;
    private LinearLayout layoutNoseExam;
    private LinearLayout layoutOralCavityThroatExam;
    private LinearLayout layoutIndirectLarygoscopyExam;
    private LinearLayout layoutNeckExam;
    private LinearLayout layoutEarsExam;
    private LinearLayout layoutPastHistory;
    private LinearLayout layoutFamilyHistory;
    private LinearLayout layoutProcedures;

    private TextView tvProcedures;
    private TextView tvPcNose;
    private TextView tvPcEars;
    private TextView tvPcThroat;
    private TextView tvPcOralCavity;
    private TextView tvTobacco;
    private TextView tvAlcohol;
    private TextView tvSmoking;
    private TextView tvDiet;
    private TextView tvOccupation;
    private TextView tvDrugs;
    private TextView tvAllergies;
    private TextView tvMedical;
    private TextView tvSurgical;
    private TextView tvNoseExam;
    private TextView tvOralCavityThroatExam;
    private TextView tvIndirectLarygoscopyExam;
    private TextView tvNeckExam;
    private TextView tvEarsExam;
    private TextView tvPastHistory;
    private TextView tvFamilyHistory;
    private TextView tvPriorConsultations;
    private LinearLayout layoutPriorConsultations;
    private HealthcocoRecyclerViewAdapter adapter;

    public ClinicalNotesListItemViewHolder(HealthCocoActivity mActivity,
                                           Object listenerObject, boolean isInEmrList) {
        super(mActivity);
        if (!isInEmrList) {
            this.detailCombinedItemListener = (VisitDetailCombinedItemListener) listenerObject;
            this.user = detailCombinedItemListener.getUser();
            this.selectedPatient = detailCombinedItemListener.getSelectedPatient();
            this.loginedUser = detailCombinedItemListener.getLoginedUser();
        } else {
            this.commonEMRItemClickListener = (CommonEMRItemClickListener) listenerObject;
            this.user = commonEMRItemClickListener.getUser();
            this.selectedPatient = commonEMRItemClickListener.getSelectedPatient();
            this.loginedUser = commonEMRItemClickListener.getLoginedUser();

        }
    }

    @Override
    public void setData(Object object) {
        this.clinicalNote = (ClinicalNotes) object;
    }

    @Override
    public void applyData() {
        if (clinicalNote.getCreatedTime() != null)
            tvDate.setText(DateTimeUtil.getFormatedDate(clinicalNote.getCreatedTime()));

        if (!Util.isNullOrBlank(clinicalNote.getUniqueEmrId())) {
            tvCNID.setVisibility(View.VISIBLE);
            tvCNID.setText(mActivity.getResources().getString(R.string.cnid) + clinicalNote.getUniqueEmrId());
        } else tvCNID.setVisibility(View.GONE);
        if (!Util.isNullOrBlank(clinicalNote.getCreatedBy()))
            tvNotedBy.setText(clinicalNote.getCreatedBy());
        else
            tvNotedBy.setText("");

        clearAllViews();
        VitalSigns vitalSigns = clinicalNote.getVitalSigns();
        if (vitalSigns != null && !vitalSigns.areAllFieldsNull()) {
            layoutVitalSigns.setVisibility(View.VISIBLE);
            if (!Util.isNullOrBlank(vitalSigns.getPulse()))
                tvHeartRate.setText(vitalSigns.getFormattedPulse(mActivity, vitalSigns.getPulse()));
            if (!Util.isNullOrBlank(vitalSigns.getTemperature()))
                tvBodyTemprature.setText(vitalSigns.getFormattedTemprature(mActivity, vitalSigns.getTemperature()));
            if (!Util.isNullOrBlank(vitalSigns.getWeight()))
                tvWeight.setText(vitalSigns.getFormattedWeight(mActivity, vitalSigns.getWeight()));
            BloodPressure bloodPressure = vitalSigns.getBloodPressure();
            if (bloodPressure != null && !Util.isNullOrBlank(bloodPressure.getSystolic()) && !Util.isNullOrBlank(bloodPressure.getDiastolic())) {
                tvBloodPressure.setText(vitalSigns.getFormattedBloodPressureValue(mActivity, bloodPressure));
            }
            if (!Util.isNullOrBlank(vitalSigns.getBreathing()))
                tvRespiratoryRate.setText(vitalSigns.getFormattedBreathing(mActivity, vitalSigns.getBreathing()));
            if (!Util.isNullOrBlank(vitalSigns.getSpo2()))
                tvSpo2.setText(vitalSigns.getFormattedSpo2(mActivity, vitalSigns.getSpo2()));
            if (!Util.isNullOrBlank(vitalSigns.getBmi()))
                tvBmi.setText(vitalSigns.getFormattedBmi(mActivity, vitalSigns.getBmi()));
            if (!Util.isNullOrBlank(vitalSigns.getBsa()))
                tvBsa.setText(vitalSigns.getFormattedBsa(mActivity, vitalSigns.getBsa()));
            if (!Util.isNullOrBlank(vitalSigns.getHeight()))
                tvHeight.setText(vitalSigns.getFormattedHeight(mActivity, vitalSigns.getHeight()));
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
        if (!Util.isNullOrBlank(clinicalNote.getPcNose())) {
            layoutEntComplaints.setVisibility(View.VISIBLE);
            layoutPcNose.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PC_NOSE, clinicalNote.getPcNose());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPcOralCavity())) {
            layoutEntComplaints.setVisibility(View.VISIBLE);
            layoutPcOralCavity.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PC_ORAL_CAVITY, clinicalNote.getPcOralCavity());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPcThroat())) {
            layoutEntComplaints.setVisibility(View.VISIBLE);
            layoutPcThroat.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PC_THROAT, clinicalNote.getPcThroat());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPcEars())) {
            layoutEntComplaints.setVisibility(View.VISIBLE);
            layoutPcEars.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PC_EARS, clinicalNote.getPcEars());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPersonalHistoryAlcohol())) {
            layoutAlcohol.setVisibility(View.VISIBLE);
            layoutPersonalHistory.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.ALCOHOL, clinicalNote.getPersonalHistoryAlcohol());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPersonalHistoryOccupation())) {
            layoutOccupation.setVisibility(View.VISIBLE);
            layoutPersonalHistory.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.OCCUPATION, clinicalNote.getPersonalHistoryOccupation());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPersonalHistoryTobacco())) {
            layoutTobacco.setVisibility(View.VISIBLE);
            layoutPersonalHistory.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.TOBACCO, clinicalNote.getPersonalHistoryTobacco());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPersonalHistoryDiet())) {
            layoutDiet.setVisibility(View.VISIBLE);
            layoutPersonalHistory.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.DIET, clinicalNote.getPersonalHistoryDiet());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPersonalHistorySmoking())) {
            layoutSmoking.setVisibility(View.VISIBLE);
            layoutPersonalHistory.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.SMOKING, clinicalNote.getPersonalHistorySmoking());
        }
        if (!Util.isNullOrBlank(clinicalNote.getGeneralHistoryDrugs())) {
            layoutGeneralHistory.setVisibility(View.VISIBLE);
            layoutDrugs.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.DRUGS, clinicalNote.getGeneralHistoryDrugs());
        }
        if (!Util.isNullOrBlank(clinicalNote.getGeneralHistoryAllergies())) {
            layoutGeneralHistory.setVisibility(View.VISIBLE);
            layoutAllergies.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.ALLERGIES, clinicalNote.getGeneralHistoryAllergies());
        }
        if (!Util.isNullOrBlank(clinicalNote.getGeneralHistorySurgical())) {
            layoutGeneralHistory.setVisibility(View.VISIBLE);
            layoutSurgical.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.SURGICAL, clinicalNote.getGeneralHistorySurgical());
        }
        if (!Util.isNullOrBlank(clinicalNote.getGeneralHistoryMedicine())) {
            layoutGeneralHistory.setVisibility(View.VISIBLE);
            layoutMedical.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.MEDICINE, clinicalNote.getGeneralHistoryMedicine());
        }
        if (!Util.isNullOrBlank(clinicalNote.getNoseExam())) {
            layoutEntExamination.setVisibility(View.VISIBLE);
            layoutNoseExam.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.NOSE_EXAM, clinicalNote.getNoseExam());
        }
        if (!Util.isNullOrBlank(clinicalNote.getOralCavityThroatExam())) {
            layoutEntExamination.setVisibility(View.VISIBLE);
            layoutOralCavityThroatExam.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.ORAL_CAVITY_THROAT_EXAM, clinicalNote.getOralCavityThroatExam());
        }
        if (!Util.isNullOrBlank(clinicalNote.getIndirectLarygoscopyExam())) {
            layoutEntExamination.setVisibility(View.VISIBLE);
            layoutIndirectLarygoscopyExam.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.INDIRECT_LARYGOSCOPY_EXAM, clinicalNote.getIndirectLarygoscopyExam());
        }
        if (!Util.isNullOrBlank(clinicalNote.getNeckExam())) {
            layoutEntExamination.setVisibility(View.VISIBLE);
            layoutNeckExam.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.NECK_EXAM, clinicalNote.getNeckExam());
        }
        if (!Util.isNullOrBlank(clinicalNote.getEarsExam())) {
            layoutEntExamination.setVisibility(View.VISIBLE);
            layoutEarsExam.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.EARS_EXAM, clinicalNote.getEarsExam());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPastHistory())) {
            layoutPastHistory.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PAST_HISTORY, clinicalNote.getPastHistory());
        }
        if (!Util.isNullOrBlank(clinicalNote.getFamilyHistory())) {
            layoutFamilyHistory.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.FAMILY_HISTORY, clinicalNote.getFamilyHistory());
        }
        if (!Util.isNullOrBlank(clinicalNote.getProcedureNote())) {
            layoutProcedures.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PROCEDURES, clinicalNote.getProcedureNote());
        }
        if (!Util.isNullOrBlank(clinicalNote.getPriorConsultations())) {
            layoutPriorConsultations.setVisibility(View.VISIBLE);
            initSuggestionsText(ClinicalNotesPermissionType.PRIOR_CONSULTATIONS, clinicalNote.getPriorConsultations());
        }
        if (!Util.isNullOrEmptyList(clinicalNote.getDiagrams())) {
            layoutDiagrams.setVisibility(View.VISIBLE);
            initDiagramsPagerAdapter(clinicalNote.getDiagrams());
        }

        checkIsDiscarded(clinicalNote.getDiscarded());

        if (clinicalNote.getInHistory() != null && clinicalNote.getInHistory()) {
            btHistory.setSelected(true);
        } else {
            btHistory.setSelected(false);
        }
        if (detailCombinedItemListener != null) {
            containerBottomButtons.setVisibility(View.GONE);
            tvLabelGlobalRecord.setVisibility(View.VISIBLE);
            tvLabelNotedBy.setVisibility(View.GONE);
            tvNotedBy.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            layoutNextReviewDetail.setVisibility(View.GONE);
        } else {
            if (!user.getForeignLocationId().equalsIgnoreCase(clinicalNote.getLocationId())) {
                containerBottomButtons.setVisibility(View.GONE);
                tvLabelGlobalRecord.setVisibility(View.VISIBLE);
                tvLabelNotedBy.setVisibility(View.GONE);
                tvNotedBy.setVisibility(View.GONE);
            } else if (user != null && (RoleType.isAdmin(user.getRoleTypes()))) {
                containerBottomButtons.setVisibility(View.VISIBLE);
                tvLabelGlobalRecord.setVisibility(View.GONE);
                tvLabelNotedBy.setVisibility(View.VISIBLE);
                tvNotedBy.setVisibility(View.VISIBLE);
            } else {
                containerBottomButtons.setVisibility(View.VISIBLE);
                tvLabelGlobalRecord.setVisibility(View.GONE);
                tvLabelNotedBy.setVisibility(View.VISIBLE);
                tvNotedBy.setVisibility(View.VISIBLE);
            }

            imageView.setVisibility(View.VISIBLE);
            if (clinicalNote.getAppointmentRequest() != null && clinicalNote.getAppointmentId() != null) {
                AppointmentRequest appointmentRequest = clinicalNote.getAppointmentRequest();
                String formattedTime = DateTimeUtil.getFormattedTime(0, Math.round(appointmentRequest.getTime().getFromTime()));
                String formattedDate = DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, appointmentRequest.getFromDate());
                textViewNextReviewDate.setText(formattedDate + " " + formattedTime);
                layoutNextReviewDetail.setVisibility(View.VISIBLE);
            } else {
                layoutNextReviewDetail.setVisibility(View.GONE);
            }
        }
        checkRollType();
    }

    private void checkRollType() {
        if (user != null && (!RoleType.isAdmin(user.getRoleTypes()))) {
            if (!loginedUser.equals(user.getUniqueId())) {
                btDiscard.setVisibility(View.GONE);
                btEdit.setVisibility(View.GONE);
            }
        }
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
            case PC_ORAL_CAVITY:
                tvPcOralCavity.setText(text);
                break;
            case PC_NOSE:
                tvPcNose.setText(text);
                break;
            case PC_EARS:
                tvPcEars.setText(text);
                break;
            case PC_THROAT:
                tvPcThroat.setText(text);
                break;
            case TOBACCO:
                tvTobacco.setText(text);
                break;
            case ALCOHOL:
                tvAlcohol.setText(text);
                break;
            case SMOKING:
                tvSmoking.setText(text);
                break;
            case DIET:
                tvDiet.setText(text);
                break;
            case OCCUPATION:
                tvOccupation.setText(text);
                break;
            case ALLERGIES:
                tvAllergies.setText(text);
                break;
            case DRUGS:
                tvDrugs.setText(text);
                break;
            case MEDICINE:
                tvMedical.setText(text);
                break;
            case SURGICAL:
                tvSurgical.setText(text);
                break;
            case INDIRECT_LARYGOSCOPY_EXAM:
                tvIndirectLarygoscopyExam.setText(text);
                break;
            case NOSE_EXAM:
                tvNoseExam.setText(text);
                break;
            case NECK_EXAM:
                tvNeckExam.setText(text);
                break;
            case EARS_EXAM:
                tvEarsExam.setText(text);
                break;
            case ORAL_CAVITY_THROAT_EXAM:
                tvOralCavityThroatExam.setText(text);
                break;
            case PAST_HISTORY:
                tvPastHistory.setText(text);
                break;
            case FAMILY_HISTORY:
                tvFamilyHistory.setText(text);
                break;
            case PROCEDURES:
                tvProcedures.setText(text);
                break;
            case PRIOR_CONSULTATIONS:
                tvPriorConsultations.setText(text);
                break;

        }
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.temp_item_clinic_note, null);
        initViews(contentView);
        initListeners();
        initCurrentPlanAdapter();
        return contentView;
    }

    private void initCurrentPlanAdapter() {
        int spacingInPixels = mActivity.getResources().getDimensionPixelSize(R.dimen.item_spacing_diagram_list_item);
        containerDiagrams.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        containerDiagrams.addItemDecoration(new HorizontalRecyclerViewItemDecoration(spacingInPixels));

        adapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.DIAGRAM_LIST, this, this);
        containerDiagrams.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initViews(View contentView) {
        tvDate = (TextView) contentView.findViewById(R.id.tv_date);
        tvCNID = (TextView) contentView.findViewById(R.id.tv_cnid);
        layoutVitalSigns = (LinearLayout) contentView.findViewById(R.id.layout_vital_signs);
        imageView = (ImageView) contentView.findViewById(R.id.image_view);

        textViewNextReviewDate = (TextView) contentView.findViewById(R.id.textView_next_review_date_for_clinical_note);
        layoutNextReviewDetail = (LinearLayout) contentView.findViewById(R.id.layout_next_review_detail_for_clinical_note);

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
        layoutDiagrams = (LinearLayout) contentView.findViewById(R.id.layout_diagrams);
        containerDiagrams = (RecyclerView) contentView.findViewById(R.id.container_diagrams);

        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded);

        btHistory = (LinearLayout) contentView.findViewById(R.id.bt_history);
        btEdit = (LinearLayout) contentView.findViewById(R.id.bt_edit);
        btEmail = (LinearLayout) contentView.findViewById(R.id.bt_email);
        btPrint = (LinearLayout) contentView.findViewById(R.id.bt_print);
        btDiscard = (LinearLayout) contentView.findViewById(R.id.bt_discard);

        tvNotedBy = (TextView) contentView.findViewById(R.id.tv_noted_by);
        containerBottomButtons = (LinearLayout) contentView.findViewById(R.id.container_bottom_buttons_clinical_note);
        tvLabelGlobalRecord = (TextView) contentView.findViewById(R.id.tv_label_global_record);
        tvLabelNotedBy = (TextView) contentView.findViewById(R.id.tv_label_created_by);

        tvBodyTemprature = (TextViewFontAwesome) contentView.findViewById(R.id.tv_body_temprature);
        tvWeight = (TextViewFontAwesome) contentView.findViewById(R.id.tv_weight);
        tvHeartRate = (TextViewFontAwesome) contentView.findViewById(R.id.tv_heart_rate);
        tvBloodPressure = (TextViewFontAwesome) contentView.findViewById(R.id.tv_blood_pressure);
        tvRespiratoryRate = (TextViewFontAwesome) contentView.findViewById(R.id.tv_respiratory_rate);
        tvSpo2 = (TextViewFontAwesome) contentView.findViewById(R.id.tv_spo2);
        tvBmi = (TextViewFontAwesome) contentView.findViewById(R.id.tv_bmi);
        tvBsa = (TextViewFontAwesome) contentView.findViewById(R.id.tv_bsa);
        tvHeight = (TextViewFontAwesome) contentView.findViewById(R.id.tv_height);

        layoutEntComplaints = (LinearLayout) contentView.findViewById(R.id.layout_ent_complaints);
        layoutPcNose = (LinearLayout) contentView.findViewById(R.id.layout_pc_nose);
        layoutPcEars = (LinearLayout) contentView.findViewById(R.id.layout_pc_ears);
        layoutPcThroat = (LinearLayout) contentView.findViewById(R.id.layout_pc_throat);
        layoutPcOralCavity = (LinearLayout) contentView.findViewById(R.id.layout_pc_oral_cavity);
        layoutPersonalHistory = (LinearLayout) contentView.findViewById(R.id.layout_personal_history);
        layoutTobacco = (LinearLayout) contentView.findViewById(R.id.layout_tobacco);
        layoutAlcohol = (LinearLayout) contentView.findViewById(R.id.layout_alcohol);
        layoutSmoking = (LinearLayout) contentView.findViewById(R.id.layout_smoking);
        layoutDiet = (LinearLayout) contentView.findViewById(R.id.layout_diet);
        layoutOccupation = (LinearLayout) contentView.findViewById(R.id.layout_occupation);
        layoutGeneralHistory = (LinearLayout) contentView.findViewById(R.id.layout_general_history);
        layoutDrugs = (LinearLayout) contentView.findViewById(R.id.layout_drugs);
        layoutAllergies = (LinearLayout) contentView.findViewById(R.id.layout_allergies);
        layoutMedical = (LinearLayout) contentView.findViewById(R.id.layout_medicine);
        layoutSurgical = (LinearLayout) contentView.findViewById(R.id.layout_surgical);
        layoutEntExamination = (LinearLayout) contentView.findViewById(R.id.layout_ent_examinatin);
        layoutNoseExam = (LinearLayout) contentView.findViewById(R.id.layout_nose_exam);
        layoutOralCavityThroatExam = (LinearLayout) contentView.findViewById(R.id.layout_oral_cavity_throat_exam);
        layoutIndirectLarygoscopyExam = (LinearLayout) contentView.findViewById(R.id.layout_indirect_larygoscopy_exam);
        layoutNeckExam = (LinearLayout) contentView.findViewById(R.id.layout_neck_exam);
        layoutEarsExam = (LinearLayout) contentView.findViewById(R.id.layout_ears_exam);
        layoutPastHistory = (LinearLayout) contentView.findViewById(R.id.layout_past_history);
        layoutFamilyHistory = (LinearLayout) contentView.findViewById(R.id.layout_family_history);
        layoutProcedures = (LinearLayout) contentView.findViewById(R.id.layout_procedure_notes);

        tvProcedures = (TextView) contentView.findViewById(R.id.tv_text_procedure_notes);
        layoutPriorConsultations = (LinearLayout) contentView.findViewById(R.id.layout_prior_consultations);

        tvPriorConsultations = (TextView) contentView.findViewById(R.id.tv_text_prior_consultations);
        tvPcNose = (TextView) contentView.findViewById(R.id.tv_text_pc_nose);
        tvPcEars = (TextView) contentView.findViewById(R.id.tv_text_pc_ears);
        tvPcThroat = (TextView) contentView.findViewById(R.id.tv_text_pc_throat);
        tvPcOralCavity = (TextView) contentView.findViewById(R.id.tv_text_pc_oral_cavity);
        tvTobacco = (TextView) contentView.findViewById(R.id.tv_text_tobacco);
        tvAlcohol = (TextView) contentView.findViewById(R.id.tv_text_alcohol);
        tvSmoking = (TextView) contentView.findViewById(R.id.tv_text_smoking);
        tvDiet = (TextView) contentView.findViewById(R.id.tv_text_diet);
        tvOccupation = (TextView) contentView.findViewById(R.id.tv_text_occupation);
        tvDrugs = (TextView) contentView.findViewById(R.id.tv_text_drugs);
        tvAllergies = (TextView) contentView.findViewById(R.id.tv_text_allergies);
        tvMedical = (TextView) contentView.findViewById(R.id.tv_text_medicine);
        tvSurgical = (TextView) contentView.findViewById(R.id.tv_text_surgical);
        tvNoseExam = (TextView) contentView.findViewById(R.id.tv_text_nose_exam);
        tvOralCavityThroatExam = (TextView) contentView.findViewById(R.id.tv_text_oral_cavity_throat_exam);
        tvIndirectLarygoscopyExam = (TextView) contentView.findViewById(R.id.tv_text_indirect_larygoscopy_exam);
        tvNeckExam = (TextView) contentView.findViewById(R.id.tv_text_neck_exam);
        tvEarsExam = (TextView) contentView.findViewById(R.id.tv_text_ears_exam);
        tvPastHistory = (TextView) contentView.findViewById(R.id.tv_text_past_history);
        tvFamilyHistory = (TextView) contentView.findViewById(R.id.tv_text_family_history);

        View headerCreatedByClinicalNote = contentView.findViewById(R.id.container_header_created_by_clinical_note);
        View containerPrescribedBy = contentView.findViewById(R.id.container_prescribed_by);
        if (detailCombinedItemListener != null) {
            headerCreatedByClinicalNote.setVisibility(View.GONE);
            containerPrescribedBy.setVisibility(View.GONE);
            containerBottomButtons.setVisibility(View.GONE);
            layoutNextReviewDetail.setVisibility(View.GONE);
        } else {
            containerPrescribedBy.setVisibility(View.VISIBLE);
            headerCreatedByClinicalNote.setVisibility(View.VISIBLE);
            containerBottomButtons.setVisibility(View.VISIBLE);
        }
    }

    private void initListeners() {
        btDiscard.setOnClickListener(this);
        btHistory.setOnClickListener(this);
        btEdit.setOnClickListener(this);
        btEmail.setOnClickListener(this);
        btPrint.setOnClickListener(this);
    }

    private void checkIsDiscarded(Boolean isDiscarded) {
        if (isDiscarded != null && isDiscarded)
            layoutDiscarded.setVisibility(View.VISIBLE);
        else layoutDiscarded.setVisibility(View.GONE);
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
        tvPcNose.setText("");
        tvProcedures.setText("");
        tvPriorConsultations.setText("");
        tvPcEars.setText("");
        tvPcThroat.setText("");
        tvPcOralCavity.setText("");
        tvTobacco.setText("");
        tvAlcohol.setText("");
        tvSmoking.setText("");
        tvDiet.setText("");
        tvOccupation.setText("");
        tvDrugs.setText("");
        tvAllergies.setText("");
        tvMedical.setText("");
        tvSurgical.setText("");
        tvNoseExam.setText("");
        tvOralCavityThroatExam.setText("");
        tvIndirectLarygoscopyExam.setText("");
        tvNeckExam.setText("");
        tvEarsExam.setText("");
        tvFamilyHistory.setText("");
        tvPastHistory.setText("");

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
        layoutDiagrams.setVisibility(View.GONE);
        layoutVitalSigns.setVisibility(View.GONE);
        layoutEntComplaints.setVisibility(View.GONE);
        layoutPcNose.setVisibility(View.GONE);
        layoutPcEars.setVisibility(View.GONE);
        layoutPcThroat.setVisibility(View.GONE);
        layoutPcOralCavity.setVisibility(View.GONE);
        layoutPersonalHistory.setVisibility(View.GONE);
        layoutTobacco.setVisibility(View.GONE);
        layoutAlcohol.setVisibility(View.GONE);
        layoutSmoking.setVisibility(View.GONE);
        layoutDiet.setVisibility(View.GONE);
        layoutOccupation.setVisibility(View.GONE);
        layoutGeneralHistory.setVisibility(View.GONE);
        layoutDrugs.setVisibility(View.GONE);
        layoutAllergies.setVisibility(View.GONE);
        layoutMedical.setVisibility(View.GONE);
        layoutSurgical.setVisibility(View.GONE);
        layoutEntExamination.setVisibility(View.GONE);
        layoutNoseExam.setVisibility(View.GONE);
        layoutOralCavityThroatExam.setVisibility(View.GONE);
        layoutIndirectLarygoscopyExam.setVisibility(View.GONE);
        layoutNeckExam.setVisibility(View.GONE);
        layoutEarsExam.setVisibility(View.GONE);
        layoutPastHistory.setVisibility(View.GONE);
        layoutFamilyHistory.setVisibility(View.GONE);
        layoutProcedures.setVisibility(View.GONE);
        layoutPriorConsultations.setVisibility(View.GONE);
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
        adapter.setListData((ArrayList<Object>) (Object) list);
        containerDiagrams.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        for (Diagram diagram :
//                list) {
//            if (!Util.isNullOrBlank(diagram.getDiagramUrl())) {
//                int index = list.indexOf(diagram);
//                ClinicalNoteItemDiagramViewHolder viewHolder = new ClinicalNoteItemDiagramViewHolder(mActivity);
//                viewHolder.setData(diagram);
////                if (index < list.size() - 1) {
////                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mActivity.getResources().getDimensionPixelOffset(R.dimen.width_height_clinical_note_item_diagram), mActivity.getResources().getDimensionPixelOffset(R.dimen.width_height_clinical_note_item_diagram));
////                    layoutParams.setMargins(0, 0, mActivity.getResources().getDimensionPixelOffset(R.dimen.padding_between_clinical_item_diagram), 0);
////                    viewHolder.setLayoutParams(layoutParams);
////                }
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_history:
                if (commonEMRItemClickListener != null) {
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        if (clinicalNote.getInHistory() != null && clinicalNote.getInHistory()) {
                            int msgId = R.string.confirm_remove_clinical_notes_from_history;
                            showConfirmationAlert(v.getId(), null, mActivity.getResources().getString(msgId));
                        } else
                            onAddRemoveHistoryClicked(clinicalNote);
                    } else onNetworkUnavailable(null);
                }
                break;
            case R.id.bt_edit:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    if (detailCombinedItemListener != null) {
                        detailCombinedItemListener.editVisit("");
                    } else {
                        openAddClinicalNotesFragment(clinicalNote.getUniqueId());
                    }
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_email:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    if (detailCombinedItemListener != null)
                        detailCombinedItemListener.sendEmail("");
                    else
                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_CLINICAL_NOTES,
                                AddUpdateNameDialogType.EMAIL, clinicalNote.getUniqueId(), clinicalNote.getDoctorId(),
                                clinicalNote.getLocationId(), clinicalNote.getHospitalId());
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_discard:
                LogUtils.LOGD(TAG, "Discard");
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    int msgId = R.string.confirm_discard_clinical_notes_message;
                    int titleId = R.string.confirm_discard_clinical_notes_title;
                    showConfirmationAlert(v.getId(), mActivity.getResources().getString(titleId), mActivity.getResources().getString(msgId));
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_print:
            case R.id.tv_print:
                LogUtils.LOGD(TAG, "Print");
                if (detailCombinedItemListener != null) {
                    detailCombinedItemListener.doPrint("");
                } else {
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        mActivity.showLoading(false);
                        WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_CLINICAL_NOTES_PDF_URL, clinicalNote.getUniqueId(), this, this);
                    } else onNetworkUnavailable(null);
                }
                break;
            default:
                break;
        }
    }

    private void openAddClinicalNotesFragment(String clinicalNoteId) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_VISITS.ordinal());
        if (!Util.isNullOrBlank(clinicalNoteId))
            intent.putExtra(AddClinicalNotesVisitNormalFragment.TAG_CLINICAL_NOTE_ID, clinicalNoteId);
        intent.putExtra(HealthCocoConstants.TAG_VISIT_ID, Parcels.wrap(clinicalNote.getVisitId()));
        intent.putExtra(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_CLINICAL_NOTES);
        mActivity.startActivityForResult(intent, REQUEST_CODE_CLINICAL_NOTES);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        if (commonEMRItemClickListener != null)
            commonEMRItemClickListener.showLoading(false);
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        if (commonEMRItemClickListener != null)
            Util.showToast(mActivity, R.string.user_offline);
        commonEMRItemClickListener.showLoading(false);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case GET_CLINICAL_NOTES_PDF_URL:
                    if (response.getData() != null && response.getData() instanceof String) {
                        mActivity.openEnlargedImageDialogFragment(true, (String) response.getData());
                    }
                    break;
                case DISCARD_CLINICAL_NOTES:
                    LogUtils.LOGD(TAG, "Success DISCARD_PRESCRIPTION");
                    clinicalNote.setDiscarded(!clinicalNote.getDiscarded());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updateClinicalNote(clinicalNote);
                    sendBroadcasts();
                    break;
                case ADD_TO_HISTORY_CLINICAL_NOTE:
                    Util.showToast(mActivity, mActivity.getResources().getString(R.string.added_to_history));
                    clinicalNote.setInHistory(!clinicalNote.getInHistory());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updateClinicalNote(clinicalNote);
                    sendBroadcasts();
                    break;
                case REMOVE_HISTORY_CLINICAL_NOTE:
                    Util.showToast(mActivity, mActivity.getResources().getString(R.string.removed_from_history));
                    clinicalNote.setInHistory(!clinicalNote.getInHistory());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updateClinicalNote(clinicalNote);
                    sendBroadcasts();
                    break;
                default:
                    break;
            }
        }
        if (commonEMRItemClickListener != null) commonEMRItemClickListener.showLoading(false);
        mActivity.hideLoading();
    }

    private void sendBroadcasts() {
        Util.sendBroadcasts(mApp, new ArrayList<String>() {{
            add(PatientClinicalNotesDetailFragment.INTENT_GET_CLINICAL_NOTES_LIST_LOCAL);
        }});
    }

    private void showConfirmationAlert(final int viewId, String title, String msg) {
        if (Util.isNullOrBlank(title))
            title = mActivity.getResources().getString(R.string.confirm);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (viewId) {
                    case R.id.bt_discard:
                        onDiscardedClicked(clinicalNote);
                        break;
                    case R.id.bt_history:
                        onAddRemoveHistoryClicked(clinicalNote);
                        break;
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    public void onAddRemoveHistoryClicked(Object object) {
        ClinicalNotes clinicalNotes = (ClinicalNotes) object;
        if (!clinicalNotes.getInHistory()) {
            if (commonEMRItemClickListener != null)
                commonEMRItemClickListener.showLoading(true);
            WebDataServiceImpl.getInstance(mApp).addToHistory(ClinicalNotes.class, WebServiceType.ADD_TO_HISTORY_CLINICAL_NOTE, clinicalNotes.getUniqueId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
        } else {
            if (commonEMRItemClickListener != null)
                commonEMRItemClickListener.showLoading(true);
            WebDataServiceImpl.getInstance(mApp).removeFromHistory(ClinicalNotes.class, WebServiceType.REMOVE_HISTORY_CLINICAL_NOTE, clinicalNotes.getUniqueId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
        }
    }

    public void onDiscardedClicked(Object object) {
        ClinicalNotes clinicalNotes = (ClinicalNotes) object;
        if (commonEMRItemClickListener != null)
            commonEMRItemClickListener.showLoading(true);
        WebDataServiceImpl.getInstance(mApp).discardClinicalNotes(ClinicalNotes.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), clinicalNotes.getUniqueId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, this, this);
    }

    @Override
    public void onItemClicked(Object object) {

    }
}
