package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTestsPrescription;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.OptionsPopupWindow;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.OptionsTypePopupWindow;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.AddEditNormalVisitPrescriptionFragment;
import com.healthcoco.healthcocopad.fragments.CommonOpenUpPatientDetailFragment;
import com.healthcoco.healthcocopad.fragments.PatientPrescriptionDetailFragment;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neha on 12/03/16.
 */
public class PrescriptionListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private static final String TAG = PrescriptionListItemViewHolder.class.getSimpleName();
    private static final int REQUEST_CODE_PRESCRIPTION_LIST = 155;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, dd MMM yyyy";
    private User user;
    private String loginedUser;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private VisitDetailCombinedItemListener detailCombinedItemListener;
    private TextView tvPrescribedBy;
    private TextView tvDate;
    private Prescription prescription;
    private LinearLayout containerDrugsList;
    private LinearLayout btHistory;
    private LinearLayout btSms;
    private LinearLayout btEmail;
    private ImageButton btOptions;
    private OptionsPopupWindow popupWindow;
    private CommonEMRItemClickListener commonEmrClickListener;
    private LinearLayout layoutDiscarded;
    private TextView tvPid;
    private LinearLayout parentDiagnosticTests;
    private LinearLayout containerDiagnosticTests;
    private LinearLayout containerBottomButtons;
    private TextView tvLabelGlobalRecord;
    private TextView tvLabelPrescribedBy;
    private LinearLayout btEdit;
    private LinearLayout btPrint;
    private LinearLayout layoutAdvice;
    private LinearLayout containerParentDrugsList;
    private LinearLayout btClone;
    private ImageView imageView;
    private TextView textViewNextReviewDate;
    private LinearLayout layoutNextReviewDetail;

    public PrescriptionListItemViewHolder(HealthCocoActivity mActivity,
                                          Object listenerObject, boolean isInPrescriptionsList) {
        super(mActivity);
        if (!isInPrescriptionsList) {
            this.detailCombinedItemListener = (VisitDetailCombinedItemListener) listenerObject;
            this.user = detailCombinedItemListener.getUser();
            this.selectedPatient = detailCombinedItemListener.getSelectedPatient();
            this.loginedUser = detailCombinedItemListener.getLoginedUser();
        } else {
            this.commonEmrClickListener = (CommonEMRItemClickListener) listenerObject;
            this.user = commonEmrClickListener.getUser();
            this.selectedPatient = commonEmrClickListener.getSelectedPatient();
            this.loginedUser = commonEmrClickListener.getLoginedUser();
        }
    }

    @Override
    public void setData(Object object) {
        this.prescription = (Prescription) object;
    }

    @Override
    public void applyData() {
        if (!Util.isNullOrBlank(prescription.getCreatedBy()))
            tvPrescribedBy.setText(prescription.getCreatedBy());
        else
            tvPrescribedBy.setText("");
        tvDate.setText(DateTimeUtil.getFormatedDate(prescription.getCreatedTime()));
        if (!Util.isNullOrBlank(prescription.getUniqueEmrId())) {
            tvPid.setVisibility(View.VISIBLE);
            tvPid.setText(mActivity.getResources().getString(R.string.rx_id) + prescription.getUniqueEmrId());
        } else
            tvPid.setVisibility(View.GONE);
        containerDrugsList.removeAllViews();
        if (!Util.isNullOrEmptyList(prescription.getItems())) {
            containerParentDrugsList.setVisibility(View.VISIBLE);
            for (DrugItem drug : prescription.getItems()) {
                PrescribedDrugDoseItemViewholder view = new PrescribedDrugDoseItemViewholder(mActivity);
                view.setData(drug);
                containerDrugsList.addView(view);
            }
        } else
            containerParentDrugsList.setVisibility(View.GONE);

        checkIsDiscarded(prescription.getDiscarded());

        if (prescription.getInHistory() != null && prescription.getInHistory()) {
            btHistory.setSelected(true);
        } else {
            btHistory.setSelected(false);
        }

        initDiagnosticTests();
        if (detailCombinedItemListener != null) {
            btOptions.setVisibility(View.GONE);
            containerBottomButtons.setVisibility(View.GONE);
            tvLabelGlobalRecord.setVisibility(View.VISIBLE);
            tvLabelPrescribedBy.setVisibility(View.GONE);
            tvPrescribedBy.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            layoutNextReviewDetail.setVisibility(View.GONE);
        } else {
            if (!user.getUniqueId().equalsIgnoreCase(prescription.getDoctorId())) {
                btOptions.setVisibility(View.GONE);
                containerBottomButtons.setVisibility(View.GONE);
                tvLabelGlobalRecord.setVisibility(View.VISIBLE);
                tvLabelPrescribedBy.setVisibility(View.GONE);
                tvPrescribedBy.setVisibility(View.GONE);
            } else {
                btOptions.setVisibility(View.VISIBLE);
                containerBottomButtons.setVisibility(View.VISIBLE);
                tvLabelGlobalRecord.setVisibility(View.GONE);
                tvLabelPrescribedBy.setVisibility(View.VISIBLE);
                tvPrescribedBy.setVisibility(View.VISIBLE);
            }
            imageView.setVisibility(View.VISIBLE);
            if (prescription.getAppointmentRequest() != null && prescription.getAppointmentId() != null) {
                layoutNextReviewDetail.setVisibility(View.VISIBLE);
                AppointmentRequest appointmentRequest = prescription.getAppointmentRequest();
                String formattedTime = DateTimeUtil.getFormattedTime(0, Math.round(appointmentRequest.getTime().getFromTime()));
                String formattedDate = DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, appointmentRequest.getFromDate());
                textViewNextReviewDate.setText(formattedDate + " " + formattedTime);
            } else {
                layoutNextReviewDetail.setVisibility(View.GONE);
            }
        }
        checkRollType();
    }

    private void checkRollType() {
        if (user != null && (!RoleType.isAdmin(user.getRoleTypes()))) {
            if (!loginedUser.equals(user.getUniqueId())) {
                btOptions.setVisibility(View.GONE);
                btEdit.setVisibility(View.GONE);
            }
        }
    }


    private void initDiagnosticTests() {
        containerDiagnosticTests.removeAllViews();
        boolean isAdded = false;
        if (!Util.isNullOrEmptyList(prescription.getDiagnosticTests())) {
            List<DiagnosticTestsPrescription> testsPrescriptionsList = prescription.getDiagnosticTests();
            for (DiagnosticTestsPrescription diagnosticTest :
                    testsPrescriptionsList) {
                if (diagnosticTest.getTest() != null) {
                    isAdded = true;
                    View itemDiagnosticTest = inflater.inflate(R.layout.item_diagnostic_test_prescription_point, null);
                    TextView tvTest = (TextView) itemDiagnosticTest.findViewById(R.id.tv_text);
                    tvTest.setText(Util.getValidatedValue(diagnosticTest.getTest().getTestName()));
                    containerDiagnosticTests.addView(itemDiagnosticTest);
                }
            }
        }
        if (isAdded) {
            if (Util.isNullOrEmptyList(prescription.getItems()))
                popupWindow.hidePopupOption(R.id.tv_save_as_template);
            parentDiagnosticTests.setVisibility(View.VISIBLE);
        } else {
            initOptionsPopupWindow();
            parentDiagnosticTests.setVisibility(View.GONE);
        }

        if (prescription.getAdvice() != null && !prescription.getAdvice().equals("")) {
            LinearLayout containerAdvice = (LinearLayout) layoutAdvice.findViewById(R.id.container_advice);
            containerAdvice.removeAllViews();
            TextView tvAdvice = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
            tvAdvice.setText(Util.getValidatedValue(prescription.getAdvice()));
            containerAdvice.addView(tvAdvice);
            layoutAdvice.setVisibility(View.VISIBLE);
        } else layoutAdvice.setVisibility(View.GONE);
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.item_prescription, null);
        initViews(contentView);
        initListeners();
        initOptionsPopupWindow();
        return contentView;
    }

    private void initViews(View contentView) {
        tvDate = (TextView) contentView.findViewById(R.id.tv_date);
        tvPrescribedBy = (TextView) contentView.findViewById(R.id.tv_prescribed_by);
        containerDrugsList = (LinearLayout) contentView.findViewById(R.id.container_drugs_list);
        containerParentDrugsList = (LinearLayout) contentView.findViewById(R.id.container_parent_drugs_list);
        imageView = (ImageView) contentView.findViewById(R.id.image_view);
        btHistory = (LinearLayout) contentView.findViewById(R.id.bt_history);
        btSms = (LinearLayout) contentView.findViewById(R.id.bt_sms);
        btEmail = (LinearLayout) contentView.findViewById(R.id.bt_email);
        btEdit = (LinearLayout) contentView.findViewById(R.id.bt_edit);
        btPrint = (LinearLayout) contentView.findViewById(R.id.bt_print);
        btClone = (LinearLayout) contentView.findViewById(R.id.bt_clone);

        textViewNextReviewDate = (TextView) contentView.findViewById(R.id.textView_next_review_date_for_prescription);
        layoutNextReviewDetail = (LinearLayout) contentView.findViewById(R.id.layout_next_review_detail_for_prescription);

        btOptions = (ImageButton) contentView.findViewById(R.id.bt_options);
        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded);
        tvPid = (TextView) contentView.findViewById(R.id.tv_pid);
        parentDiagnosticTests = (LinearLayout) contentView.findViewById(R.id.parent_diagnostic_tests);
        containerDiagnosticTests = (LinearLayout) contentView.findViewById(R.id.container_diagnostic_tests);
        containerBottomButtons = (LinearLayout) contentView.findViewById(R.id.container_bottom_buttons_prescription);
        tvLabelGlobalRecord = (TextView) contentView.findViewById(R.id.tv_label_global_record);
        tvLabelPrescribedBy = (TextView) contentView.findViewById(R.id.tv_label_prescribed_by);
        layoutAdvice = (LinearLayout) contentView.findViewById(R.id.layout_parent_advice);

        View headerCreatedByPrescription = contentView.findViewById(R.id.container_header_created_by_prescription);
        View containerPrescribedBy = contentView.findViewById(R.id.container_prescribed_by);
        if (detailCombinedItemListener != null) {
            headerCreatedByPrescription.setVisibility(View.GONE);
            containerPrescribedBy.setVisibility(View.GONE);
            containerBottomButtons.setVisibility(View.GONE);
            layoutNextReviewDetail.setVisibility(View.GONE);
        } else {
            headerCreatedByPrescription.setVisibility(View.VISIBLE);
            containerPrescribedBy.setVisibility(View.VISIBLE);
            containerBottomButtons.setVisibility(View.VISIBLE);
        }
    }

    private void initListeners() {
        btClone.setOnClickListener(this);
        btHistory.setOnClickListener(this);
        btSms.setOnClickListener(this);
        btEmail.setOnClickListener(this);
        btOptions.setOnClickListener(this);
        btPrint.setOnClickListener(this);
        btEdit.setOnClickListener(this);
    }

    private void initOptionsPopupWindow() {
        popupWindow = new OptionsPopupWindow(mActivity, OptionsTypePopupWindow.PRESCRIPTIONS, this);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(popupWindow.getPopupView());
    }

    private void checkIsDiscarded(boolean isDiscarded) {
        if (isDiscarded)
            layoutDiscarded.setVisibility(View.VISIBLE);
        else layoutDiscarded.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_history:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    if (prescription.getInHistory() != null && prescription.getInHistory()) {
                        int msgId = R.string.confirm_remove_prescription_from_history;
                        showConfirmationAlert(v.getId(), null, mActivity.getResources().getString(msgId));
                    } else
                        onAddRemoveHistoryClicked();
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_sms:
                if (selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getMobileNumber())) {
                    if (detailCombinedItemListener != null)
                        detailCombinedItemListener.sendSms(Util.getValidatedValue(selectedPatient.getMobileNumber()));
                    else
                        showConfirmationAlert(v.getId(), null, mActivity.getResources().getString(R.string.confirm_sms_prescription) + selectedPatient.getMobileNumber() + "?");
                } else
                    Util.showToast(mActivity, mActivity.getResources().getString(R.string.mobile_no_not_found) + selectedPatient.getLocalPatientName());
                break;
            case R.id.bt_email:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    if (detailCombinedItemListener != null)
                        detailCombinedItemListener.sendEmail("");
                    else
                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_PRESCRIPTION, AddUpdateNameDialogType.EMAIL, prescription.getUniqueId());
                else onNetworkUnavailable(null);
                break;
            case R.id.bt_options:
                popupWindow.showOptionsWindow(v);
                break;
            case R.id.tv_discard:
                LogUtils.LOGD(TAG, "Discard");
                if (commonEmrClickListener != null) {
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        int msgId = R.string.confirm_discard_prescription_message;
                        int titleId = R.string.confirm_discard_prescription_title;
                        showConfirmationAlert(v.getId(), mActivity.getResources().getString(titleId), mActivity.getResources().getString(msgId));
                    } else onNetworkUnavailable(null);
                }
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
                        WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_PRESCRIPTION_PDF_URL, prescription.getUniqueId(), this, this);
                    } else onNetworkUnavailable(null);
                }
                popupWindow.dismiss();

                break;
            case R.id.tv_save_as_template:
                LogUtils.LOGD(TAG, "save template");
                if (commonEmrClickListener != null) {
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline)
                        openNewTemplatesFragment();
                    else onNetworkUnavailable(null);
                }
                popupWindow.dismiss();
                break;
            case R.id.bt_edit:
            case R.id.tv_edit:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    if (detailCombinedItemListener != null) {
                        detailCombinedItemListener.editVisit("");
                    } else {
                        openAddNewPrescriptionScreen(false);
                    }
                } else
                    onNetworkUnavailable(null);
                popupWindow.dismiss();
                break;
            case R.id.bt_clone:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    if (detailCombinedItemListener != null) {
                        detailCombinedItemListener.editVisit("");
                    } else {
                        openAddNewPrescriptionScreen(true);
                    }
                } else
                    onNetworkUnavailable(null);
                popupWindow.dismiss();

                break;
            default:
                break;
        }
    }

    private void openNewTemplatesFragment() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_NEW_TEMPLATE.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_PRESCRIPTION_ID, prescription.getUniqueId());
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_NEW_TEMPLATE_FRAGMENT, true);
        mActivity.startActivity(intent);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
        if (commonEmrClickListener != null)
            commonEmrClickListener.showLoading(false);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
        if (commonEmrClickListener != null)
            commonEmrClickListener.showLoading(false);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case GET_PRESCRIPTION_PDF_URL:
                    if (response.getData() != null && response.getData() instanceof String) {
                        mActivity.openEnlargedImageDialogFragment(true, (String) response.getData());
                    }
                    break;
                case SEND_SMS_PRESCRIPTION:
                    Util.showToast(mActivity, mActivity.getResources().getString(R.string.sms_sent_to) + selectedPatient.getMobileNumber());
                    mActivity.hideLoading();
                    break;
                case DISCARD_PRESCRIPTION:
                    LogUtils.LOGD(TAG, "Success DISCARD_PRESCRIPTION");
                    Prescription prescription = this.prescription;
                    prescription.setDiscarded(!prescription.getDiscarded());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updatePrescription(prescription);
                    sendBroadcasts();
                    break;
                case ADD_TO_HISTORY_PRESCRIPTION:
                    Util.showToast(mActivity, mActivity.getResources().getString(R.string.added_to_history));
                    Prescription prescription1 = this.prescription;
                    prescription1.setInHistory(!prescription1.getInHistory());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updatePrescription(prescription1);
                    break;
                case REMOVE_HISTORY_PRESCRIPTION:
                    Util.showToast(mActivity, mActivity.getResources().getString(R.string.removed_from_history));
                    Prescription prescription2 = this.prescription;
                    prescription2.setInHistory(!prescription2.getInHistory());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updatePrescription(prescription2);
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
        if (commonEmrClickListener != null)
            commonEmrClickListener.showLoading(false);
    }

    private void sendBroadcasts() {
        Util.sendBroadcasts(mApp, new ArrayList<String>() {{
            add(PatientPrescriptionDetailFragment.INTENT_GET_PRESCRIPTION_LIST_LOCAL);
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
                    case R.id.tv_discard:
                        onDiscardedClicked();
                        popupWindow.dismiss();
                        break;
                    case R.id.bt_history:
                        onAddRemoveHistoryClicked();
                        break;
                    case R.id.bt_sms:
                        mActivity.showLoading(false);
                        WebDataServiceImpl.getInstance(mApp).sendSms(WebServiceType.SEND_SMS_PRESCRIPTION, prescription.getUniqueId(),
                                user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                                selectedPatient.getMobileNumber(), PrescriptionListItemViewHolder.this, PrescriptionListItemViewHolder.this);

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

    public void onAddRemoveHistoryClicked() {
        if (!prescription.getInHistory()) {
            if (commonEmrClickListener != null) commonEmrClickListener.showLoading(true);
            WebDataServiceImpl.getInstance(mApp).addToHistory(Prescription.class, WebServiceType.ADD_TO_HISTORY_PRESCRIPTION, prescription.getUniqueId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
        } else {
            if (commonEmrClickListener != null) commonEmrClickListener.showLoading(true);
            WebDataServiceImpl.getInstance(mApp).removeFromHistory(Prescription.class, WebServiceType.REMOVE_HISTORY_PRESCRIPTION, prescription.getUniqueId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
        }
    }

    public void onDiscardedClicked() {
        if (commonEmrClickListener != null)
            commonEmrClickListener.showLoading(true);
        WebDataServiceImpl.getInstance(mApp).discardPrescription(Prescription.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), prescription.getUniqueId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, this, this);
    }

    private void openAddNewPrescriptionScreen(boolean isFromClone) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_VISITS.ordinal());
        if (isFromClone)
            intent.putExtra(HealthCocoConstants.TAG_IS_FROM_CLONE, Parcels.wrap(true));
        if (prescription != null)
            intent.putExtra(AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_ID, prescription.getUniqueId());
        intent.putExtra(HealthCocoConstants.TAG_VISIT_ID, Parcels.wrap(prescription.getVisitId()));
        intent.putExtra(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_PRESCRIPTION);
        mActivity.startActivityForResult(intent, REQUEST_CODE_PRESCRIPTION_LIST);
    }
}
