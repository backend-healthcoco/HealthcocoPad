package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Leave;
import com.healthcoco.healthcocopad.bean.server.ReferDoctor;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import static com.healthcoco.healthcocopad.enums.PatientDetailTabType.PATIENT_DETAIL_INVOICE;

/**
 * Created by Shreshtha on 25-03-2017.
 */
public class ReferDoctorListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {

    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, dd MMM yyyy";
    private static final String TAG = ReferDoctorListItemViewHolder.class.getSimpleName();
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private VisitDetailCombinedItemListener detailCombinedItemListener;
    private TextView tvTreatmentBy;
    private TextView tvDate;
    private String loginedUser;
    private LinearLayout containerTreatmentList;
    private LinearLayout btHistory;
    private LinearLayout btEmail;
    private CommonEMRItemClickListener commonEmrClickListener;
    private LinearLayout layoutDiscarded;
    private TextView tvTreatmentid;
    private LinearLayout containerBottomButtons;
    private TextView tvLabelGlobalRecord;
    private TextView tvLabelTreatmentBy;
    private LinearLayout btEdit;
    private LinearLayout btPrint;
    private LinearLayout btDiscard;
    private LinearLayout btGenerateInvoice;
    private LinearLayout layoutAdvice;
    private ImageView imageView;
    private LinearLayout containerParentTreatmentsList;
    private TextView tvGrandTotal;
    private TextView tvTotalDiscount;
    private TextView tvTotalCost;
    private LinearLayout layoutNextReviewDetail;
    private TextView textViewNextReviewDate;
    private SelectedTreatmentItemClickListener listItemClickListeners;

    private TextView tvReferDate;
    private TextView tvReferredBy;
    private TextView tvReferredTo;
    private TextView tvDetails;
    private TextView tvRequestDate;
    private LinearLayout layoutDetails;
    private ReferDoctor referDoctor;

    public ReferDoctorListItemViewHolder(HealthCocoActivity mActivity,
                                         Object listenerObject, SelectedTreatmentItemClickListener listItemClickListeners) {
        super(mActivity);

        this.commonEmrClickListener = (CommonEMRItemClickListener) listenerObject;
        this.user = commonEmrClickListener.getUser();
        this.selectedPatient = commonEmrClickListener.getSelectedPatient();
        this.listItemClickListeners = listItemClickListeners;
        this.loginedUser = commonEmrClickListener.getLoginedUser();

    }

    @Override
    public void setData(Object object) {
        this.referDoctor = (ReferDoctor) object;
    }

    @Override
    public void applyData() {
        if (referDoctor != null) {
            if (!Util.isNullOrBlank(referDoctor.getCreatedBy()))
                tvTreatmentBy.setText(referDoctor.getCreatedBy());
            else
                tvTreatmentBy.setText("");
            tvDate.setText(DateTimeUtil.getFormatedDate(referDoctor.getCreatedTime()));
            if (!Util.isNullOrBlank(referDoctor.getReferId())) {
                tvTreatmentid.setVisibility(View.VISIBLE);
                tvTreatmentid.setText(mActivity.getResources().getString(R.string.refer_id) + referDoctor.getReferId());
            } else
                tvTreatmentid.setVisibility(View.GONE);

            if (!Util.isNullOrBlank(referDoctor.getReferredBy()))
                tvReferredBy.setText(referDoctor.getReferredBy());
            else
                tvReferredBy.setText("-");

            if (!Util.isNullOrBlank(referDoctor.getReferredTo()))
                tvReferredTo.setText(referDoctor.getReferredTo());
            else {
                tvReferredTo.setText("-");
            }

            if (!Util.isNullOrBlank(referDoctor.getDetails())) {
                tvDetails.setText(referDoctor.getDetails());
                layoutDetails.setVisibility(View.VISIBLE);
            } else {
                tvDetails.setText("-");
                layoutDetails.setVisibility(View.GONE);
            }

            if (!Util.isNullOrZeroNumber(referDoctor.getDate()))
                tvReferDate.setText(DateTimeUtil.getFormatedDate(referDoctor.getDate()));
            else
                tvReferDate.setText("-");

            if (!Util.isNullOrZeroNumber(referDoctor.getRequestDate()))
                tvRequestDate.setText(DateTimeUtil.getFormatedDate(referDoctor.getRequestDate()));
            else
                tvRequestDate.setText("-");


            checkIsDiscarded(referDoctor.isDiscarded());
            checkRollType();
        }
    }

    private void checkRollType() {
        if (user != null && (!RoleType.isAdmin(user.getRoleTypes()))) {
            if (!loginedUser.equals(user.getUniqueId())) {
                btDiscard.setVisibility(View.GONE);
                btEdit.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_refer_doctor, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View contentView) {
        tvDate = (TextView) contentView.findViewById(R.id.tv_date);

        tvTreatmentBy = (TextView) contentView.findViewById(R.id.tv_treatment_by);
        imageView = (ImageView) contentView.findViewById(R.id.image_view);

        btHistory = (LinearLayout) contentView.findViewById(R.id.bt_history);
        btEmail = (LinearLayout) contentView.findViewById(R.id.bt_email);
        btEdit = (LinearLayout) contentView.findViewById(R.id.bt_edit);
        btPrint = (LinearLayout) contentView.findViewById(R.id.bt_print);
        btDiscard = (LinearLayout) contentView.findViewById(R.id.bt_discard);
        btGenerateInvoice = (LinearLayout) contentView.findViewById(R.id.bt_generate_invoice);

        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded);
        tvTreatmentid = (TextView) contentView.findViewById(R.id.tv_treatment_id);
        containerBottomButtons = (LinearLayout) contentView.findViewById(R.id.container_bottom_buttons_treatment);
        tvLabelGlobalRecord = (TextView) contentView.findViewById(R.id.tv_label_global_record);
        tvLabelTreatmentBy = (TextView) contentView.findViewById(R.id.tv_label_treatment_by);


        tvReferDate = (TextView) contentView.findViewById(R.id.tv_refer_date);
        tvReferredBy = (TextView) contentView.findViewById(R.id.tv_referred_by);
        tvReferredTo = (TextView) contentView.findViewById(R.id.tv_referred_to);
        tvRequestDate = (TextView) contentView.findViewById(R.id.tv_request_date);
        tvDetails = (TextView) contentView.findViewById(R.id.tv_details);

        layoutDetails = (LinearLayout) contentView.findViewById(R.id.layout_details);

        View headerCreatedByTreatment = contentView.findViewById(R.id.container_header_created_by_treatment);
        View containerTreatmentBy = contentView.findViewById(R.id.container_treatment_by);
        if (detailCombinedItemListener != null) {
            btEdit.setVisibility(View.VISIBLE);
            btPrint.setVisibility(View.GONE);
            btHistory.setVisibility(View.GONE);
            headerCreatedByTreatment.setVisibility(View.GONE);
            containerTreatmentBy.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            containerBottomButtons.setVisibility(View.GONE);
        } else {
            btHistory.setVisibility(View.GONE);
            headerCreatedByTreatment.setVisibility(View.VISIBLE);
            containerTreatmentBy.setVisibility(View.VISIBLE);
            containerBottomButtons.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }


        if (user.getUiPermissions().getTabPermissions().contains(PATIENT_DETAIL_INVOICE.getValue()))
            btGenerateInvoice.setVisibility(View.GONE);
        else btGenerateInvoice.setVisibility(View.GONE);


    }

    private void initListeners() {
        btHistory.setOnClickListener(this);
        btEmail.setOnClickListener(this);
        btPrint.setOnClickListener(this);
        btEdit.setOnClickListener(this);
        btDiscard.setOnClickListener(this);
        btGenerateInvoice.setOnClickListener(this);
    }

    private void checkIsDiscarded(boolean isDiscarded) {
        if (isDiscarded)
            layoutDiscarded.setVisibility(View.VISIBLE);
        else layoutDiscarded.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_email:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    if (detailCombinedItemListener != null)
                        detailCombinedItemListener.sendEmail("");
                    else
                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_TREATMENT, AddUpdateNameDialogType.EMAIL,
                                referDoctor.getUniqueId(), referDoctor.getDoctorId(), referDoctor.getLocationId(), referDoctor.getHospitalId());
                else onNetworkUnavailable(null);
                break;

            case R.id.bt_discard:
                if (commonEmrClickListener != null) {
                    LogUtils.LOGD(TAG, "Discard");
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        int msgId = R.string.confirm_discard_clinical_notes_message;
                        int titleId = R.string.confirm_discard_refer_doctor;
                        showConfirmationAlert(v.getId(), mActivity.getResources().getString(titleId), mActivity.getResources().getString(msgId));
                    } else onNetworkUnavailable(null);
                }
                break;
            case R.id.bt_generate_invoice:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
//                    listItemClickListeners.onInvoiceClicked(investigationNote);
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_edit:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    listItemClickListeners.onTreatmentItemClick(referDoctor);
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_print:
                LogUtils.LOGD(TAG, "Print");
                if (detailCombinedItemListener != null) {
                    detailCombinedItemListener.doPrint("");
                } else {
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        mActivity.showLoading(false);
                        WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_TREATMENT_PDF_URL, referDoctor.getUniqueId(), this, this);
                    } else onNetworkUnavailable(null);
                }
                break;
        }
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
                case GET_TREATMENT_PDF_URL:
                    if (response.getData() != null && response.getData() instanceof String) {
                        mActivity.openEnlargedImageDialogFragment(true, (String) response.getData());
                    }
                    break;
                case DISCARD_TREATMENT:
                    LogUtils.LOGD(TAG, "Success DISCARD_TREATMENT");
                    referDoctor.setDiscarded(!referDoctor.isDiscarded());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).addReferDoctor(referDoctor);
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
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
                        onDiscardedClicked();
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

    public void onDiscardedClicked() {
//        if (HealthCocoConstants.isNetworkOnline) {
        referDoctor.setDiscarded(!referDoctor.isDiscarded());
        applyData();
        LocalDataServiceImpl.getInstance(mApp).addReferDoctor(referDoctor);
//        } else onNetworkUnavailable(null);
    }

}
