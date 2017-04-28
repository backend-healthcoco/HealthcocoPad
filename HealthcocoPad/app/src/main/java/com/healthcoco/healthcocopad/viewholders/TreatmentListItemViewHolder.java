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
import com.healthcoco.healthcocopad.bean.server.Treatments;
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
    private TextView tvPrescribedBy;
    private TextView tvDate;
    private PatientTreatment patientTreatment;
    private LinearLayout containerTreatmentList;
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
    private LinearLayout containerParentTreatmentsList;
    private TextView tvGrandTotal;
    private TextView tvTotalDiscount;
    private TextView tvTotalCost;

    public TreatmentListItemViewHolder(HealthCocoActivity mActivity,
                                       Object listenerObject, boolean isInPrescriptionsList) {
        super(mActivity);
        if (!isInPrescriptionsList) {
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
        this.patientTreatment = (PatientTreatment) object;
    }

    @Override
    public void applyData() {
        if (!Util.isNullOrBlank(patientTreatment.getCreatedBy()))
            tvPrescribedBy.setText(patientTreatment.getCreatedBy());
        else
            tvPrescribedBy.setText("");
        tvDate.setText(DateTimeUtil.getFormatedDate(patientTreatment.getCreatedTime()));
        if (!Util.isNullOrBlank(patientTreatment.getUniqueEmrId())) {
            tvPid.setVisibility(View.VISIBLE);
            tvPid.setText(mActivity.getResources().getString(R.string.rx_id) + patientTreatment.getUniqueEmrId());
        } else
            tvPid.setVisibility(View.GONE);
        containerTreatmentList.removeAllViews();
        if (!Util.isNullOrEmptyList(patientTreatment.getTreatments())) {
            containerParentTreatmentsList.setVisibility(View.VISIBLE);
            for (Treatments treatments : patientTreatment.getTreatments()) {
                TreatmentDetailItemViewholder view = new TreatmentDetailItemViewholder(mActivity);
                view.setData(treatments);
                containerTreatmentList.addView(view);
            }
        } else
            containerParentTreatmentsList.setVisibility(View.GONE);

        //set TotalCost
        double totalCost = patientTreatment.getTotalCost();
        if (totalCost != 0)
            tvTotalCost.setText(Util.getIntValue(totalCost));
        else
            tvTotalCost.setText(mActivity.getResources().getString(R.string.no_text_dash));

        //set TotalDiscount
        Discount totalDiscount = patientTreatment.getTotalDiscount();
        double value = totalDiscount.getValue();
        if (totalDiscount != null && totalDiscount.getUnit() != null && value != 0)
            tvTotalDiscount.setText(Util.getIntValue(value));
        else
            tvTotalDiscount.setText(mActivity.getResources().getString(R.string.no_text_dash));

        //set TotalGrandTotal
        double grandTotal = patientTreatment.getGrandTotal();
        if (grandTotal != 0)
            tvGrandTotal.setText(Util.getIntValue(grandTotal));
        else
            tvGrandTotal.setText(mActivity.getResources().getString(R.string.no_text_dash));

        checkIsDiscarded(patientTreatment.getDiscarded());
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
        tvPrescribedBy = (TextView) contentView.findViewById(R.id.tv_prescribed_by);
        containerTreatmentList = (LinearLayout) contentView.findViewById(R.id.container_treatment_list);
        containerParentTreatmentsList = (LinearLayout) contentView.findViewById(R.id.parent_treatment_layout);

        btHistory = (LinearLayout) contentView.findViewById(R.id.bt_history);
        btSms = (LinearLayout) contentView.findViewById(R.id.bt_sms);
        btEmail = (LinearLayout) contentView.findViewById(R.id.bt_email);
        btEdit = (LinearLayout) contentView.findViewById(R.id.bt_edit);
        btPrint = (LinearLayout) contentView.findViewById(R.id.bt_print);

        btOptions = (ImageButton) contentView.findViewById(R.id.bt_options);
        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded);
        tvPid = (TextView) contentView.findViewById(R.id.tv_pid);
        containerBottomButtons = (LinearLayout) contentView.findViewById(R.id.container_bottom_buttons_treatment);
        tvLabelGlobalRecord = (TextView) contentView.findViewById(R.id.tv_label_global_record);
        tvLabelPrescribedBy = (TextView) contentView.findViewById(R.id.tv_label_prescribed_by);

        View headerCreatedByVisit = contentView.findViewById(R.id.container_header_created_by_visit);
        View headerCreatedByPrescription = contentView.findViewById(R.id.container_header_created_by_prescription);
        View containerPrescribedBy = contentView.findViewById(R.id.container_prescribed_by);
        if (detailCombinedItemListener != null) {
            btEdit.setVisibility(View.VISIBLE);
            btPrint.setVisibility(View.VISIBLE);
            btHistory.setVisibility(View.GONE);
            headerCreatedByVisit.setVisibility(View.GONE);
            headerCreatedByPrescription.setVisibility(View.GONE);
            containerPrescribedBy.setVisibility(View.GONE);
            containerBottomButtons.setVisibility(View.GONE);
            detailCombinedItemListener.setVisitHeader(headerCreatedByVisit);
        } else {
            btEdit.setVisibility(View.GONE);
            btPrint.setVisibility(View.GONE);
            btHistory.setVisibility(View.VISIBLE);
            headerCreatedByVisit.setVisibility(View.VISIBLE);
            headerCreatedByPrescription.setVisibility(View.VISIBLE);
            containerPrescribedBy.setVisibility(View.VISIBLE);
            containerBottomButtons.setVisibility(View.VISIBLE);
        }
    }

    private void initListeners() {
        btHistory.setOnClickListener(this);
        btSms.setOnClickListener(this);
        btEmail.setOnClickListener(this);
        btOptions.setOnClickListener(this);
        btPrint.setOnClickListener(this);
        btEdit.setOnClickListener(this);
    }

    private void checkIsDiscarded(boolean isDiscarded) {
        if (isDiscarded)
            layoutDiscarded.setVisibility(View.VISIBLE);
        else layoutDiscarded.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

    }
}
