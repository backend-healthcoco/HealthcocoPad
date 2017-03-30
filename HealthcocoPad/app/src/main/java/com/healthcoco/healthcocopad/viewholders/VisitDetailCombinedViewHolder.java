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
    private LinearLayout containerClinocalNotes;
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
        initBottomButtonViews(contentView);
    }

    private void initHeaderViews(View contentView) {
        tvVID = (TextView) contentView.findViewById(R.id.tv_vid);
        tvCreatedBy = (TextView) contentView.findViewById(R.id.tv_created_by_visit);
        tvVisitDate = (TextView) contentView.findViewById(R.id.tv_visit_date);
    }

    private void initBottomButtonViews(View contentView) {
        btOpen = (LinearLayout) contentView.findViewById(R.id.bt_open);
        btSms = (LinearLayout) contentView.findViewById(R.id.bt_sms);
        btEmail = (LinearLayout) contentView.findViewById(R.id.bt_email);
        btEdit = (LinearLayout) contentView.findViewById(R.id.bt_edit);
        btPrint = (LinearLayout) contentView.findViewById(R.id.bt_print);
        btClone = (LinearLayout) contentView.findViewById(R.id.bt_clone);
        btHistory = (LinearLayout) contentView.findViewById(R.id.bt_history);
        btSaveAsTemplate = (LinearLayout) contentView.findViewById(R.id.bt_save_as_template);
    }

    private void initClinicalNotesView(View contentView) {
        containerClinocalNotes = (LinearLayout) contentView.findViewById(R.id.container_clinical_notes);
        containerReports = (LinearLayout) contentView.findViewById(R.id.container_reports);
    }

    private void initPrescriptionsView(View contentView) {
        containerPrescription = (LinearLayout) contentView.findViewById(R.id.container_prescription);
    }

    private void initTreatmentsView(View contentView) {
        containerTreatment = (LinearLayout) contentView.findViewById(R.id.container_treatments);
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
        containerClinocalNotes.removeAllViews();
        containerPrescription.removeAllViews();
        containerTreatment.removeAllViews();
        containerReports.removeAllViews();
        if (!Util.isNullOrEmptyList(visitDetail.getClinicalNotes())) {
            setButtonVisible(VisitedForType.CLINICAL_NOTES);
            List<ClinicalNotes> clinicalNotesList = visitDetail.getClinicalNotes();
            for (ClinicalNotes clinicalNotes : clinicalNotesList) {
                ClinicalNotesListItemViewHolder clinicalNotesListItemViewHolder = new ClinicalNotesListItemViewHolder(mActivity, listItemClickListener, false);
                containerClinocalNotes.addView(clinicalNotesListItemViewHolder.getContentView());
                clinicalNotesListItemViewHolder.setData(clinicalNotes);
                clinicalNotesListItemViewHolder.applyData();
            }
        }
        if (!Util.isNullOrEmptyList(visitDetail.getPrescriptions())) {
            setButtonVisible(VisitedForType.PRESCRIPTION);
            List<Prescription> prescriptionsList = visitDetail.getPrescriptions();
            for (Prescription prescription : prescriptionsList) {
                PrescriptionListItemViewHolder prescriptionListItemViewHolder = new PrescriptionListItemViewHolder(mActivity, listItemClickListener, false);
                containerPrescription.addView(prescriptionListItemViewHolder.getContentView());
                prescriptionListItemViewHolder.setData(prescription);
                prescriptionListItemViewHolder.applyData();
            }
        }
        if (!Util.isNullOrEmptyList(visitDetail.getPatientTreatments())) {
            setButtonVisible(VisitedForType.TREATMENT);
            List<PatientTreatment> patientTreatmentList = visitDetail.getPatientTreatments();
            for (PatientTreatment treatment : patientTreatmentList) {
                TreatmentListItemViewHolder treatmentListItemViewHolder = new TreatmentListItemViewHolder(mActivity, listItemClickListener, false);
                containerTreatment.addView(treatmentListItemViewHolder.getContentView());
                treatmentListItemViewHolder.setData(treatment);
                treatmentListItemViewHolder.applyData();
            }
        }
        if (!Util.isNullOrEmptyList(visitDetail.getRecords())) {
            setButtonVisible(VisitedForType.REPORTS);
            for (Records record : visitDetail.getRecords()) {
                ReportsListItemViewHolder reportViewHolder = new ReportsListItemViewHolder(mActivity, listItemClickListener, false);
                View convertView = reportViewHolder.getContentView();
                reportViewHolder.setData(record);
                reportViewHolder.applyData();
                containerReports.addView(convertView);
            }
        }
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
                    case R.id.bt_open:
                        btOpen.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_email:
                        btEmail.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_sms:
                        btSms.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_history:
                        btHistory.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_save_as_template:
                        btSaveAsTemplate.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_clone:
                        btClone.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_print:
                        btPrint.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_edit:
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
            case R.id.bt_sms:
                listItemClickListener.sendSms(visitDetail.getUniqueId());
                break;
            case R.id.bt_edit:
                listItemClickListener.editVisit(visitDetail.getVisitId());
                break;
            case R.id.bt_email:
                listItemClickListener.sendEmail(visitDetail.getUniqueId());
                break;
            case R.id.bt_print:
                listItemClickListener.doPrint(visitDetail.getUniqueId());
                break;
            case R.id.bt_save_as_template:
                for (Prescription prescription :
                        visitDetail.getPrescriptions()) {
                    listItemClickListener.saveAsTemplate(prescription);
                }
                break;
            case R.id.bt_clone:
                listItemClickListener.cloneVisit(visitDetail.getUniqueId());
                break;
        }
    }
}
