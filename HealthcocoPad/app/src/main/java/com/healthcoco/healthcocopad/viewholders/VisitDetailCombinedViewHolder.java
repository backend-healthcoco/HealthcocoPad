package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.PatientTreatment;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neha on 02/09/16.
 */
public class VisitDetailCombinedViewHolder extends HealthCocoViewHolder implements View.OnClickListener {

    private VisitDetails visitDetail;
    private LinearLayout btPrint;
    private LinearLayout btSms;
    private LinearLayout btEdit;
    private LinearLayout btEmail;
    private LinearLayout btClone;
    private LinearLayout btHistory;
    private LinearLayout btSaveAsTemplate;
    private TextView tvVID;
    private VisitDetailCombinedItemListener listItemClickListener;
    private TextView tvCreatedBy;
    private TextView tvVisitDate;
    private LinearLayout containerPrescription;
    private LinearLayout containerClinicalNotes;
    private LinearLayout containerTreatment;
    private LinearLayout containerReports;
    private LinearLayout btOpen;

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
        initHeaderViews(contentView);
        initClinicalNotesView(contentView);
        initPrescriptionsView(contentView);
        initTreatmentsView(contentView);
        initReportsView(contentView);
        initBottomButtonViews(contentView);
    }

    private void initHeaderViews(View contentView) {
        tvVID = (TextView) contentView.findViewById(R.id.tv_vid);
        tvCreatedBy = (TextView) contentView.findViewById(R.id.tv_created_by_visit);
        tvVisitDate = (TextView) contentView.findViewById(R.id.tv_visit_date);
    }

    private void initBottomButtonViews(View contentView) {
        btOpen = (LinearLayout) contentView.findViewById(R.id.bt_open_visit);
        btSms = (LinearLayout) contentView.findViewById(R.id.bt_sms_visit);
        btEmail = (LinearLayout) contentView.findViewById(R.id.bt_email_visit);
        btEdit = (LinearLayout) contentView.findViewById(R.id.bt_edit_visit);
        btPrint = (LinearLayout) contentView.findViewById(R.id.bt_print_visit);
        btClone = (LinearLayout) contentView.findViewById(R.id.bt_clone_visit);
        btHistory = (LinearLayout) contentView.findViewById(R.id.bt_history_visit);
        btSaveAsTemplate = (LinearLayout) contentView.findViewById(R.id.bt_save_as_template_visit);
    }

    private void initClinicalNotesView(View contentView) {
        LogUtils.LOGD(TAG, "initClinicalViews ");
        containerClinicalNotes = (LinearLayout) contentView.findViewById(R.id.container_clinical_notes);
        ClinicalNotesListItemViewHolder clinicalNotesListItemViewHolder = new ClinicalNotesListItemViewHolder(mActivity, listItemClickListener, false);
        containerClinicalNotes.addView(clinicalNotesListItemViewHolder.getContentView());
        containerClinicalNotes.setTag(clinicalNotesListItemViewHolder);
    }

    private void initPrescriptionsView(View contentView) {
        LogUtils.LOGD(TAG, "initPrescriptionViews ");
        containerPrescription = (LinearLayout) contentView.findViewById(R.id.container_prescription);
        PrescriptionListItemViewHolder prescriptionListItemViewHolder = new PrescriptionListItemViewHolder(mActivity, listItemClickListener, false);
        containerPrescription.addView(prescriptionListItemViewHolder.getContentView());
        containerPrescription.setTag(prescriptionListItemViewHolder);
    }

    private void initReportsView(View contentView) {
        containerReports = (LinearLayout) contentView.findViewById(R.id.container_reports);
        LogUtils.LOGD(TAG, "initReportsViews ");
        ReportsListItemViewHolder reportsListItemViewHolder = new ReportsListItemViewHolder(mActivity, listItemClickListener, false);
        containerReports.addView(reportsListItemViewHolder.getContentView());
        containerReports.setTag(reportsListItemViewHolder);
    }

    private void initTreatmentsView(View contentView) {
        containerTreatment = (LinearLayout) contentView.findViewById(R.id.container_treatments);
        TreatmentListItemViewHolder treatmentListItemViewHolder = new TreatmentListItemViewHolder(mActivity, listItemClickListener, false);
        containerTreatment.addView(treatmentListItemViewHolder.getContentView());
        containerTreatment.setTag(treatmentListItemViewHolder);
    }

    private void initListeners() {
        btOpen.setOnClickListener(this);
        btPrint.setOnClickListener(this);
        btEdit.setOnClickListener(this);
        btEmail.setOnClickListener(this);
        btSms.setOnClickListener(this);
        btClone.setOnClickListener(this);
        btSaveAsTemplate.setOnClickListener(this);
    }

    @Override
    public void applyData() {
        setVisibilityOfButtons(View.GONE);
//        containerClinicalNotes.removeAllViews();
//        containerPrescription.removeAllViews();
//        containerTreatment.removeAllViews();
//        containerReports.removeAllViews();
        if (!Util.isNullOrEmptyList(visitDetail.getClinicalNotes())) {
            containerClinicalNotes.setVisibility(View.VISIBLE);
            setButtonVisible(VisitedForType.CLINICAL_NOTES);
            List<ClinicalNotes> clinicalNotesList = visitDetail.getClinicalNotes();
            for (ClinicalNotes clinicalNotes : clinicalNotesList) {
                ClinicalNotesListItemViewHolder clinicalNotesListItemViewHolder = (ClinicalNotesListItemViewHolder) containerClinicalNotes.getTag();
//                ClinicalNotesListItemViewHolder clinicalNotesListItemViewHolder = new ClinicalNotesListItemViewHolder(mActivity, listItemClickListener, false);
//                containerClinicalNotes.addView(clinicalNotesListItemViewHolder.getContentView());
                clinicalNotesListItemViewHolder.setData(clinicalNotes);
                clinicalNotesListItemViewHolder.applyData();
            }
        } else
            containerClinicalNotes.setVisibility(View.GONE);
        if (!Util.isNullOrEmptyList(visitDetail.getPrescriptions())) {
            containerPrescription.setVisibility(View.VISIBLE);
            setButtonVisible(VisitedForType.PRESCRIPTION);
            List<Prescription> prescriptionsList = visitDetail.getPrescriptions();
            for (Prescription prescription : prescriptionsList) {
                PrescriptionListItemViewHolder prescriptionListItemViewHolder = (PrescriptionListItemViewHolder) containerPrescription.getTag();
//                PrescriptionListItemViewHolder prescriptionListItemViewHolder = new PrescriptionListItemViewHolder(mActivity, listItemClickListener, false);
//                containerPrescription.addView(prescriptionListItemViewHolder.getContentView());
                prescriptionListItemViewHolder.setData(prescription);
                prescriptionListItemViewHolder.applyData();
            }
        } else containerPrescription.setVisibility(View.GONE);
        if (!Util.isNullOrEmptyList(visitDetail.getPatientTreatments())) {
            containerTreatment.setVisibility(View.VISIBLE);
            setButtonVisible(VisitedForType.TREATMENT);
            List<PatientTreatment> patientTreatmentList = visitDetail.getPatientTreatments();
            for (PatientTreatment treatment : patientTreatmentList) {
                TreatmentListItemViewHolder treatmentListItemViewHolder = (TreatmentListItemViewHolder) containerPrescription.getTag();
//                TreatmentListItemViewHolder treatmentListItemViewHolder = new TreatmentListItemViewHolder(mActivity, listItemClickListener, false);
//                containerTreatment.addView(treatmentListItemViewHolder.getContentView());
                treatmentListItemViewHolder.setData(treatment);
                treatmentListItemViewHolder.applyData();
            }
        } else
            containerTreatment.setVisibility(View.GONE);

        if (!Util.isNullOrEmptyList(visitDetail.getRecords())) {
            containerReports.setVisibility(View.VISIBLE);
            setButtonVisible(VisitedForType.REPORTS);
            for (Records record : visitDetail.getRecords()) {
                ReportsListItemViewHolder reportsListItemViewHolder = (ReportsListItemViewHolder) containerReports.getTag();
//                TreatmentListItemViewHolder treatmentListItemViewHolder = new TreatmentListItemViewHolder(mActivity, listItemClickListener, false);
//                containerTreatment.addView(treatmentListItemViewHolder.getContentView());
                reportsListItemViewHolder.setData(record);
                reportsListItemViewHolder.applyData();
            }
        } else
            containerReports.setVisibility(View.GONE);
        tvVID.setText(mActivity.getResources().getString(R.string.vid) + Util.getValidatedValue(visitDetail.getUniqueEmrId()));
        tvCreatedBy.setText(Util.getValidatedValue(visitDetail.getCreatedBy()));
        tvVisitDate.setText(DateTimeUtil.getFormatedDate(visitDetail.getVisitedTime()));
    }

    private void setButtonVisible(VisitedForType visitedForType) {
        ArrayList<Integer> visibleButtonIds = visitedForType.getVisibleButtonIdsList();
        if (!Util.isNullOrEmptyList(visibleButtonIds)) {
            for (Integer viewId :
                    visibleButtonIds) {
                switch (viewId) {
                    case R.id.bt_open_visit:
                        btOpen.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_email_visit:
                        btEmail.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_sms_visit:
                        btSms.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_history_visit:
                        btHistory.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_save_as_template_visit:
                        btSaveAsTemplate.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_clone_visit:
                        btClone.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_print_visit:
                        btPrint.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_edit_visit:
                        btEdit.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    private void setVisibilityOfButtons(int visibility) {
        btOpen.setVisibility(visibility);
        btHistory.setVisibility(visibility);
        btPrint.setVisibility(visibility);
        btEdit.setVisibility(visibility);
        btEmail.setVisibility(visibility);
        btSms.setVisibility(visibility);
        btClone.setVisibility(visibility);
        btSaveAsTemplate.setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sms_visit:
                listItemClickListener.sendSms(visitDetail.getUniqueId());
                break;
            case R.id.bt_edit_visit:
                listItemClickListener.editVisit(visitDetail.getVisitId());
                break;
            case R.id.bt_email_visit:
                listItemClickListener.sendEmail(visitDetail.getUniqueId());
                break;
            case R.id.bt_print_visit:
                listItemClickListener.doPrint(visitDetail.getUniqueId());
                break;
            case R.id.bt_save_as_template_visit:
                for (Prescription prescription :
                        visitDetail.getPrescriptions()) {
                    listItemClickListener.saveAsTemplate(prescription);
                }
                break;
            case R.id.bt_clone_visit:
                listItemClickListener.cloneVisit(visitDetail.getUniqueId());
                break;
            case R.id.bt_open_visit:
                for (Records records :
                        visitDetail.getRecords()) {
                    listItemClickListener.openRecord(records);
                }
                break;
        }
    }
}
