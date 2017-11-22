package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.UnitValue;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.TreatmentListItemClickListeners;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 25-03-2017.
 */
public class TreatmentListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    private static final String TAG = TreatmentListItemViewHolder.class.getSimpleName();
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private VisitDetailCombinedItemListener detailCombinedItemListener;
    private TextView tvTreatmentBy;
    private TextView tvDate;
    private Treatments treatments;
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
    private TreatmentListItemClickListeners listItemClickListeners;

    public TreatmentListItemViewHolder(HealthCocoActivity mActivity,
                                       Object listenerObject, boolean isInTreatmentsList, TreatmentListItemClickListeners listItemClickListeners) {
        super(mActivity);
        if (!isInTreatmentsList) {
            this.detailCombinedItemListener = (VisitDetailCombinedItemListener) listenerObject;
            this.user = detailCombinedItemListener.getUser();
            this.selectedPatient = detailCombinedItemListener.getSelectedPatient();
            this.listItemClickListeners = listItemClickListeners;
        } else {
            this.commonEmrClickListener = (CommonEMRItemClickListener) listenerObject;
            this.user = commonEmrClickListener.getUser();
            this.selectedPatient = commonEmrClickListener.getSelectedPatient();
            this.listItemClickListeners = listItemClickListeners;
        }
    }

    @Override
    public void setData(Object object) {
        this.treatments = (Treatments) object;
    }

    @Override
    public void applyData() {
        if (!Util.isNullOrBlank(treatments.getCreatedBy()))
            tvTreatmentBy.setText(treatments.getCreatedBy());
        else
            tvTreatmentBy.setText("");
        tvDate.setText(DateTimeUtil.getFormatedDate(treatments.getCreatedTime()));
        if (!Util.isNullOrBlank(treatments.getUniqueEmrId())) {
            tvTreatmentid.setVisibility(View.VISIBLE);
            tvTreatmentid.setText(mActivity.getResources().getString(R.string.treatment_id) + treatments.getUniqueEmrId());
        } else
            tvTreatmentid.setVisibility(View.GONE);
        containerTreatmentList.removeAllViews();
        if (!Util.isNullOrEmptyList(treatments.getTreatments())) {
            containerParentTreatmentsList.setVisibility(View.VISIBLE);
            for (TreatmentItem treatmentItem : treatments.getTreatments()) {
                TreatmentDetailItemViewholder view = new TreatmentDetailItemViewholder(mActivity);
                view.setData(treatmentItem);
                containerTreatmentList.addView(view);
            }
        } else
            containerParentTreatmentsList.setVisibility(View.GONE);

        //set TotalCost
        double totalCost = treatments.getTotalCost();
        if (totalCost != 0)
            tvTotalCost.setText(Util.getIntValue(totalCost) + "");
        else
            tvTotalCost.setText(mActivity.getResources().getString(R.string.no_text_dash));

        //set TotalDiscount
        UnitValue totalDiscount = treatments.getTotalDiscount();
        double value = totalDiscount.getValue();
        if (totalDiscount != null && totalDiscount.getUnit() != null && value != 0)
            tvTotalDiscount.setText(Util.getIntValue(value) + "");
        else
            tvTotalDiscount.setText(mActivity.getResources().getString(R.string.no_text_dash));

        //set TotalGrandTotal
        double grandTotal = treatments.getGrandTotal();
        if (grandTotal != 0)
            tvGrandTotal.setText(Util.getIntValue(grandTotal) + "");
        else
            tvGrandTotal.setText(mActivity.getResources().getString(R.string.no_text_dash));

        checkIsDiscarded(treatments.getDiscarded());
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.item_treatment, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View contentView) {
        tvDate = (TextView) contentView.findViewById(R.id.tv_date);
        tvTotalCost = (TextView) contentView.findViewById(R.id.tv_total_cost);
        tvTotalDiscount = (TextView) contentView.findViewById(R.id.tv_total_discount);
        tvGrandTotal = (TextView) contentView.findViewById(R.id.tv_grand_total);
        tvTreatmentBy = (TextView) contentView.findViewById(R.id.tv_treatment_by);
        containerTreatmentList = (LinearLayout) contentView.findViewById(R.id.container_treatment_list);
        containerParentTreatmentsList = (LinearLayout) contentView.findViewById(R.id.parent_treatment_layout);
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

        View headerCreatedByTreatment = contentView.findViewById(R.id.container_header_created_by_treatment);
        View containerTreatmentBy = contentView.findViewById(R.id.container_treatment_by);
        if (detailCombinedItemListener != null) {
            btEdit.setVisibility(View.VISIBLE);
            btPrint.setVisibility(View.VISIBLE);
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
            imageView.setVisibility(View.VISIBLE);
        }
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
                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_TREATMENT, AddUpdateNameDialogType.EMAIL, treatments.getUniqueId());
                else onNetworkUnavailable(null);
                break;

            case R.id.bt_discard:
                if (commonEmrClickListener != null) {
                    LogUtils.LOGD(TAG, "Discard");
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        int msgId = R.string.confirm_discard_clinical_notes_message;
                        int titleId = R.string.confirm_discard_treatment_title;
                        showConfirmationAlert(v.getId(), mActivity.getResources().getString(titleId), mActivity.getResources().getString(msgId));
                    } else onNetworkUnavailable(null);
                }
                break;
            case R.id.bt_generate_invoice:
                break;
            case R.id.bt_edit:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    listItemClickListeners.onAddTreatmentClicked(treatments);
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
                        WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_TREATMENT_PDF_URL, treatments.getUniqueId(), this, this);
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
                    treatments.setDiscarded(!treatments.getDiscarded());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).addTreatment(treatments);
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
        if (HealthCocoConstants.isNetworkOnline) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).discardTreatment(Treatments.class, treatments.getUniqueId(), user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
        } else onNetworkUnavailable(null);
    }

}
