package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Discount;
import com.healthcoco.healthcocopad.bean.server.PatientTreatment;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.UnitValue;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.OptionsPopupWindow;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 25-03-2017.
 */
public class TreatmentListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener {
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
    private LinearLayout containerParentTreatmentsList;
    private TextView tvGrandTotal;
    private TextView tvTotalDiscount;
    private TextView tvTotalCost;

    public TreatmentListItemViewHolder(HealthCocoActivity mActivity,
                                       Object listenerObject, boolean isInTreatmentsList) {
        super(mActivity);
        if (!isInTreatmentsList) {
            this.detailCombinedItemListener = (VisitDetailCombinedItemListener) listenerObject;
            this.user = detailCombinedItemListener.getUser();
            this.selectedPatient = detailCombinedItemListener.getSelectedPatient();
        } else {
            this.commonEmrClickListener = (CommonEMRItemClickListener) listenerObject;
            this.user = commonEmrClickListener.getUser();
            this.selectedPatient = commonEmrClickListener.getSelectedPatient();
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
            containerBottomButtons.setVisibility(View.GONE);
        } else {
            btHistory.setVisibility(View.GONE);
            headerCreatedByTreatment.setVisibility(View.VISIBLE);
            containerTreatmentBy.setVisibility(View.VISIBLE);
            containerBottomButtons.setVisibility(View.VISIBLE);
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
                break;
            case R.id.bt_discard:
                break;
            case R.id.bt_generate_invoice:
                break;
            case R.id.bt_prescription:
                break;
            case R.id.bt_edit:
                break;
            case R.id.bt_print:
                break;
        }
    }
}
