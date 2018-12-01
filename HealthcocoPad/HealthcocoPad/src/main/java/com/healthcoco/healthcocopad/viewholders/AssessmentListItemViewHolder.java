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
import com.healthcoco.healthcocopad.bean.server.AssessmentPersonalDetail;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.InvoiceItem;
import com.healthcoco.healthcocopad.bean.server.UnitValue;
import com.healthcoco.healthcocopad.custom.OptionsPopupWindow;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.InvoiceItemType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DietItemClickListeners;
import com.healthcoco.healthcocopad.listeners.InvoiceItemClickListeners;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.List;

/**
 * Created by Shreshtha on 05-07-2017.
 */

public class AssessmentListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener
        , Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private AssessmentPersonalDetail personalDetail;
    private TextView tvDate;
    private TextView tvAssessmentID;
    private TextView tvCreatedBy;
    private LinearLayout btEdit;
    private LinearLayout btPay;
    private LinearLayout btPrint;
    private LinearLayout btDiscard;
    private LinearLayout layoutDiscarded;
    private LinearLayout btEmail;
    private LinearLayout containerAssessment;
    private TextView tvPatientName;
    private TextView tvAge;
    private TextView tvBloodGroup;
    private TextView tvCommunity;
    private TextView tvProfession;
    private TextView tvGoal;
    private HealthCocoActivity mActivity;
    private DietItemClickListeners dietItemClickListeners;

    public AssessmentListItemViewHolder(HealthCocoActivity mActivity, DietItemClickListeners dietItemClickListeners) {
        super(mActivity);
        this.mActivity = mActivity;
        this.dietItemClickListeners = dietItemClickListeners;
    }

    @Override
    public void setData(Object object) {
        this.personalDetail = (AssessmentPersonalDetail) object;
    }

    @Override
    public void applyData() {
        if (personalDetail.getCreatedTime() != null)
            tvDate.setText(DateTimeUtil.getFormatedDate(personalDetail.getCreatedTime()));

        if (!Util.isNullOrBlank(personalDetail.getAssessmentUniqueId())) {
            tvAssessmentID.setVisibility(View.VISIBLE);
            tvAssessmentID.setText(mActivity.getResources().getString(R.string.assessment_id) + personalDetail.getAssessmentUniqueId());
        } else tvAssessmentID.setVisibility(View.GONE);
        if (!Util.isNullOrBlank(personalDetail.getCreatedBy()))
            tvCreatedBy.setText(personalDetail.getCreatedBy());
        else
            tvCreatedBy.setText("");

        if (!Util.isNullOrBlank(personalDetail.getFirstName()))
            tvPatientName.setText(personalDetail.getFirstName());

        if (!Util.isNullOrBlank(personalDetail.getBloodGroup()))
            tvBloodGroup.setText(personalDetail.getBloodGroup());

        if (personalDetail.getDob() != null)
            tvAge.setText(Util.getDOB(personalDetail.getDob()));
        else if (Util.isNullOrZeroNumber(personalDetail.getAge()))
            tvAge.setText(Util.getValidatedValue(personalDetail.getAge()));

        if (!Util.isNullOrBlank(personalDetail.getCommunity()))
            tvCommunity.setText(personalDetail.getCommunity());

        if (!Util.isNullOrBlank(personalDetail.getProfession()))
            tvProfession.setText(personalDetail.getProfession());

        if (!Util.isNullOrBlank(personalDetail.getGoal()))
            tvGoal.setText(personalDetail.getGoal());

        checkDiscarded(personalDetail.getDiscarded());
    }

    private void checkDiscarded(Boolean isDiscarded) {
        if (isDiscarded != null && isDiscarded) {
            layoutDiscarded.setVisibility(View.VISIBLE);
        } else layoutDiscarded.setVisibility(View.GONE);
    }


    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_assessment, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View contentView) {
        tvDate = (TextView) contentView.findViewById(R.id.tv_date);
        tvAssessmentID = (TextView) contentView.findViewById(R.id.tv_assessment_id);
        tvCreatedBy = (TextView) contentView.findViewById(R.id.tv_created_by);
        tvPatientName = (TextView) contentView.findViewById(R.id.tv_patient_name);
        btEmail = (LinearLayout) contentView.findViewById(R.id.bt_email);
        tvAge = (TextView) contentView.findViewById(R.id.tv_age);
        tvBloodGroup = (TextView) contentView.findViewById(R.id.tv_blood_group);
        tvProfession = (TextView) contentView.findViewById(R.id.tv_profession);
        tvCommunity = (TextView) contentView.findViewById(R.id.tv_community);
        tvGoal = (TextView) contentView.findViewById(R.id.tv_goal);
        initBottomButtonViews(contentView);
    }

    private void initBottomButtonViews(View contentView) {
        btEdit = (LinearLayout) contentView.findViewById(R.id.bt_edit);
        btPay = (LinearLayout) contentView.findViewById(R.id.bt_pay);
        btPrint = (LinearLayout) contentView.findViewById(R.id.bt_print);
        btDiscard = (LinearLayout) contentView.findViewById(R.id.bt_discard);
        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded);
    }

    private void initListeners() {
        btEdit.setOnClickListener(this);
        btEmail.setOnClickListener(this);
        btPay.setOnClickListener(this);
        btPrint.setOnClickListener(this);
        btDiscard.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    dietItemClickListeners.onEditDietClicked(personalDetail);
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_discard:
                LogUtils.LOGD(TAG, "Discard");
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    int msgId = R.string.confirm_discard_clinical_notes_message;
                    int titleId = R.string.confirm_discard_invoice_title;
                    showConfirmationAlert(v.getId(), mActivity.getResources().getString(titleId), mActivity.getResources().getString(msgId));
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_print:
            case R.id.tv_print:
                LogUtils.LOGD(TAG, "Print");
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_INVOICE_PDF_URL, personalDetail.getUniqueId(), this, this);
                } else onNetworkUnavailable(null);
                break;
            case R.id.tv_email:
            case R.id.bt_email:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_INVOICE, AddUpdateNameDialogType.EMAIL,
                            personalDetail.getUniqueId(), personalDetail.getDoctorId(), personalDetail.getLocationId(), personalDetail.getHospitalId());

//                mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_INVOICE, AddUpdateNameDialogType.EMAIL, personalDetail.getUniqueId());
                else onNetworkUnavailable(null);
                break;
        }
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
                        onDiscardedClicked(personalDetail);
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

    public void onDiscardedClicked(Object object) {
        Invoice invoice = (Invoice) object;
        if (HealthCocoConstants.isNetworkOnline) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).discardInvoice(Invoice.class, invoice.getUniqueId(), this, this);
        } else onNetworkUnavailable(null);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case GET_INVOICE_PDF_URL:
                    if (response.getData() != null && response.getData() instanceof String) {
                        mActivity.openEnlargedImageDialogFragment(true, (String) response.getData());
                    }
                    break;
                case DISCARD_INVOICE:
                    LogUtils.LOGD(TAG, "Success DISCARD_INVOICE");
                    personalDetail.setDiscarded(!personalDetail.getDiscarded());
                    applyData();
//                    LocalDataServiceImpl.getInstance(mApp).updateInvoice(personalDetail);
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }
}
