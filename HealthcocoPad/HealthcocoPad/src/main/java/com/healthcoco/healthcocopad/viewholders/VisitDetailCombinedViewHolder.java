package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neha on 02/09/16.
 */
public class VisitDetailCombinedViewHolder extends HealthCocoViewHolder implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {

    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, dd MMM yyyy";
    private VisitDetails visitDetail;
    private LinearLayout btPrint;
    private LinearLayout btSms;
    private User user;
    private String loginedUser;
    private LinearLayout btEdit;
    private LinearLayout btEmail;
    private LinearLayout btClone;
    private LinearLayout btHistory;
    private LinearLayout btDiscard;
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
    private LinearLayout containerBottomButtons;
    private LinearLayout layoutDiscarded;
    private TextView textViewNextReviewDate;
    private LinearLayout layoutNextReviewDetail;
    private Boolean isDiscarded;

    public VisitDetailCombinedViewHolder(HealthCocoActivity mActivity, VisitDetailCombinedItemListener listItemClickListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.listItemClickListener = listItemClickListener;
        this.user = listItemClickListener.getUser();
        this.loginedUser = listItemClickListener.getLoginedUser();

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
        textViewNextReviewDate = (TextView) contentView.findViewById(R.id.textView_next_review_date_for_visit);
        layoutNextReviewDetail = (LinearLayout) contentView.findViewById(R.id.layout_next_review_detail_for_visit);
        layoutNextReviewDetail.setVisibility(View.GONE);
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
        btDiscard = (LinearLayout) contentView.findViewById(R.id.bt_discard_visit);
        btSaveAsTemplate = (LinearLayout) contentView.findViewById(R.id.bt_save_as_template_visit);
        containerBottomButtons = (LinearLayout) contentView.findViewById(R.id.container_bottom_buttons_combined);
        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded_visit);
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
        TreatmentListItemViewHolder treatmentListItemViewHolder = new TreatmentListItemViewHolder(mActivity, listItemClickListener, false, null);
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
        btDiscard.setOnClickListener(this);
        btSaveAsTemplate.setOnClickListener(this);
    }

    @Override
    public void applyData() {
        checkIsDiscarded(visitDetail.getDiscarded());
        setVisibilityOfButtons(View.GONE);
        if (!Util.isNullOrEmptyList(visitDetail.getClinicalNotes())) {
            containerClinicalNotes.setVisibility(View.VISIBLE);
            setButtonVisible(VisitedForType.CLINICAL_NOTES);
            List<ClinicalNotes> clinicalNotesList = visitDetail.getClinicalNotes();
            if (isDiscarded) clinicalNotesList.get(0).setDiscarded(false);
            for (ClinicalNotes clinicalNotes : clinicalNotesList) {
                ClinicalNotesListItemViewHolder clinicalNotesListItemViewHolder = (ClinicalNotesListItemViewHolder) containerClinicalNotes.getTag();
                clinicalNotesListItemViewHolder.setData(clinicalNotes);
                clinicalNotesListItemViewHolder.applyData();
            }
        } else
            containerClinicalNotes.setVisibility(View.GONE);
        if (!Util.isNullOrEmptyList(visitDetail.getPrescriptions())) {
            containerPrescription.setVisibility(View.VISIBLE);
            setButtonVisible(VisitedForType.PRESCRIPTION);
            List<Prescription> prescriptionsList = visitDetail.getPrescriptions();
            if (isDiscarded) prescriptionsList.get(0).setDiscarded(false);
            for (Prescription prescription : prescriptionsList) {
                PrescriptionListItemViewHolder prescriptionListItemViewHolder = (PrescriptionListItemViewHolder) containerPrescription.getTag();
                prescriptionListItemViewHolder.setData(prescription);
                prescriptionListItemViewHolder.applyData();
            }
        } else containerPrescription.setVisibility(View.GONE);
        if (!Util.isNullOrEmptyList(visitDetail.getPatientTreatment())) {
            containerTreatment.setVisibility(View.VISIBLE);
            setButtonVisible(VisitedForType.TREATMENT);
            List<Treatments> patientTreatmentList = visitDetail.getPatientTreatment();
            if (isDiscarded) patientTreatmentList.get(0).setDiscarded(false);
            for (Treatments treatment : patientTreatmentList) {
                TreatmentListItemViewHolder treatmentListItemViewHolder = (TreatmentListItemViewHolder) containerTreatment.getTag();
                treatmentListItemViewHolder.setData(treatment);
                treatmentListItemViewHolder.applyData();
            }
        } else
            containerTreatment.setVisibility(View.GONE);

        if (!Util.isNullOrEmptyList(visitDetail.getRecords())) {
            containerReports.setVisibility(View.VISIBLE);
            setButtonVisible(VisitedForType.REPORTS);
            List<Records> recordsList = visitDetail.getRecords();
            if (isDiscarded) recordsList.get(0).setDiscarded(false);
            for (Records record : recordsList) {
                ReportsListItemViewHolder reportsListItemViewHolder = (ReportsListItemViewHolder) containerReports.getTag();
                reportsListItemViewHolder.setData(record);
                reportsListItemViewHolder.applyData();
            }
        } else
            containerReports.setVisibility(View.GONE);

        tvVID.setText(mActivity.getResources().getString(R.string.vid) + Util.getValidatedValue(visitDetail.getUniqueEmrId()));
        tvCreatedBy.setText(Util.getValidatedValue(visitDetail.getCreatedBy()));
        tvVisitDate.setText(DateTimeUtil.getFormatedDate(visitDetail.getVisitedTime()));

        if (!Util.isNullOrBlank(visitDetail.getAppointmentId())) {
            if (visitDetail.getAppointmentRequest() != null) {
                AppointmentRequest appointmentRequest = visitDetail.getAppointmentRequest();
                String formattedTime = DateTimeUtil.getFormattedTime(0, Math.round(appointmentRequest.getTime().getFromTime()));
                String formattedDate = DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, appointmentRequest.getFromDate());
                textViewNextReviewDate.setText(formattedDate + " " + formattedTime);
                layoutNextReviewDetail.setVisibility(View.VISIBLE);
            }
        } else {
            layoutNextReviewDetail.setVisibility(View.GONE);
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

    private void checkIsDiscarded(Boolean isDiscarded) {
        this.isDiscarded = isDiscarded;
        if (visitDetail.getDiscarded() != null && visitDetail.getDiscarded()) {
            layoutDiscarded.setVisibility(View.VISIBLE);
        } else layoutDiscarded.setVisibility(View.GONE);
        if (isDiscarded != null && isDiscarded)
            containerBottomButtons.setVisibility(View.GONE);
        else containerBottomButtons.setVisibility(View.VISIBLE);
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
                        btSaveAsTemplate.setVisibility(View.GONE);
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
                    case R.id.bt_discard:
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
//        btDiscard.setVisibility(visibility);
        btSaveAsTemplate.setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sms_visit:
                listItemClickListener.sendSms(visitDetail.getUniqueId());
                break;
            case R.id.bt_edit_visit:
                listItemClickListener.editVisit(visitDetail.getUniqueId());
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
            case R.id.bt_discard_visit:
                LogUtils.LOGD(TAG, "Discard");
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    int msgId = R.string.confirm_discard_clinical_notes_message;
                    int titleId = R.string.confirm_discard_visit_title;
                    showConfirmationAlert(v.getId(), mActivity.getResources().getString(titleId), mActivity.getResources().getString(msgId));
                } else onNetworkUnavailable(null);
                break;
        }
    }

    private void discardVisit() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).discardVisit(VisitDetails.class, visitDetail.getUniqueId(), this, this);

    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {

                case DISCARD_PATIENT_VISIT:
                    if (response.getData() != null && response.getData() instanceof VisitDetails) {
                        visitDetail = (VisitDetails) response.getData();
                        applyData();
                        mActivity.hideLoading();
                        LocalDataServiceImpl.getInstance(mApp).updateDiscardedValueInVisit(visitDetail);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {

    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {

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
                    case R.id.bt_discard_visit:
                        discardVisit();
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

}
