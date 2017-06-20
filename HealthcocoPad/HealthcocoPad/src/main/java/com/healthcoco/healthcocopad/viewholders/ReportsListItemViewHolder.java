package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.OptionsPopupWindow;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.OptionsTypePopupWindow;
import com.healthcoco.healthcocopad.enums.RecordState;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.PatientReportsDetailFragment;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.ImageLoadedListener;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.Locale;

/**
 * Created by neha on 19/03/16.
 */
public class ReportsListItemViewHolder extends HealthCocoViewHolder
        implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, ImageLoadedListener {
    private static final String TAG = ReportsListItemViewHolder.class.getSimpleName();
    private final String DEFAULT_RECORD_TYPE = "IMAGE";
    private CommonEMRItemClickListener commonEmrClickListener;
    private User user;
    private LinearLayout btHistory;
    private LinearLayout btEmail;
    private TextView tvReportName;
    private ImageView ivReportType;
    private TextView tvDescription;
    private TextView tvUploadedBy;
    private LinearLayout btOpen;
    private TextView tvDate;
    private Records record;
    private VisitDetailCombinedItemListener detailCombinedItemListener;
    private LinearLayout layoutDiscarded;
    private TextView tvRid;
    private LinearLayout containerBottomButtons;
    private TextView tvLabelGlobalRecord;
    private TextView tvLabelUploadedBy;
    private LinearLayout btApprove;
    private LinearLayout btDecline;
    private LinearLayout btDiscard;
    private ImageView imageView;
    private TextView tvDiscard;

    public ReportsListItemViewHolder(HealthCocoActivity mActivity,
                                     Object listItemClickListener, boolean isInEmrList) {
        super(mActivity);
        if (!isInEmrList) {
            this.detailCombinedItemListener = (VisitDetailCombinedItemListener) listItemClickListener;
            this.user = detailCombinedItemListener.getUser();
        } else {
            this.commonEmrClickListener = (CommonEMRItemClickListener) listItemClickListener;
            this.user = commonEmrClickListener.getUser();
        }
    }

    @Override
    public void setData(Object object) {
        this.record = (Records) object;
    }

    @Override
    public void applyData() {
        if (record.getInHistory() != null && record.getInHistory())
            btHistory.setSelected(true);
        else
            btHistory.setSelected(false);
        tvReportName.setText(record.getRecordsLabel());
        if (!Util.isNullOrBlank(record.getExplanation()))
            tvDescription.setText(Util.getValidatedValue(record.getExplanation()));
        else
            tvDescription.setVisibility(View.GONE);

        tvDate.setText(DateTimeUtil.getFormatedDate(record.getCreatedTime()));
        if (!Util.isNullOrBlank(record.getUniqueEmrId())) {
            tvRid.setVisibility(View.VISIBLE);
            tvRid.setText(mActivity.getResources().getString(R.string.rid) + record.getUniqueEmrId());
        } else
            tvRid.setVisibility(View.GONE);

        tvUploadedBy.setText(Util.getValidatedValue(record.getUploadedByLocation()));

        LogUtils.LOGD(TAG, "Record type " + record.getRecordsType());
        if (record.getRecordsType() != null && record.getRecordsType().equalsIgnoreCase(DEFAULT_RECORD_TYPE)) {
            if (!Util.isNullOrBlank(record.getRecordsUrl())) {
                if (record.getRecordsImageBitmap() != null) {
                    LogUtils.LOGD(TAG, "Already bitmap loaded");
                    ivReportType.setImageBitmap(record.getRecordsImageBitmap());
                } else {
                    LogUtils.LOGD(TAG, "Loading url");
                    DownloadImageFromUrlUtil.loadImageUsingImageLoaderUsingDefaultImage(R.drawable.img_report_normal, ivReportType, record.getRecordsUrl(), this);
                }
            }
        } else {
            ivReportType.setBackgroundResource(R.drawable.img_report_doc_normal);
        }
        checkIsDiscarded(record.getDiscarded());
        if (detailCombinedItemListener != null) {
            containerBottomButtons.setVisibility(View.GONE);
            tvLabelGlobalRecord.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        } else {
            if (!user.getUniqueId().equalsIgnoreCase(record.getDoctorId())) {
                containerBottomButtons.setVisibility(View.GONE);
                tvLabelGlobalRecord.setVisibility(View.VISIBLE);
            } else {
                containerBottomButtons.setVisibility(View.VISIBLE);
                tvLabelGlobalRecord.setVisibility(View.GONE);
            }
            imageView.setVisibility(View.VISIBLE);
        }
        //handling approve decline view visibility
        if (record.getRecordsState() != null) {
            hideAllOptions();
            switch (record.getRecordsState()) {
                case DECLINED_BY_DOCTOR:
                    checkIsDiscarded(true);
                    tvDiscard.setText(R.string.declined);
                    layoutDiscarded.setOnClickListener(this);
                case APPROVED_BY_DOCTOR:
                case APPROVAL_NOT_REQUIRED:
//                    btHistory.setVisibility(View.VISIBLE);
                    btEmail.setVisibility(View.VISIBLE);
                    break;
                case APPROVAL_REQUIRED:
                    btApprove.setVisibility(View.VISIBLE);
                    btDecline.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            btApprove.setVisibility(View.GONE);
            btDecline.setVisibility(View.GONE);
        }
    }

    private void hideAllOptions() {
        btHistory.setVisibility(View.GONE);
        btEmail.setVisibility(View.GONE);
        btApprove.setVisibility(View.GONE);
        btDecline.setVisibility(View.GONE);
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.item_report, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View contentView) {
        tvDate = (TextView) contentView.findViewById(R.id.tv_date);
        tvRid = (TextView) contentView.findViewById(R.id.tv_rid);
        tvReportName = (TextView) contentView.findViewById(R.id.tv_report_name);
        ivReportType = (ImageView) contentView.findViewById(R.id.iv_report_type);
        tvDescription = (TextView) contentView.findViewById(R.id.tv_report_description);
        tvUploadedBy = (TextView) contentView.findViewById(R.id.tv_uploaded_by);
        imageView = (ImageView) contentView.findViewById(R.id.image_view);
        btHistory = (LinearLayout) contentView.findViewById(R.id.bt_history);
        btOpen = (LinearLayout) contentView.findViewById(R.id.bt_open);
        btEmail = (LinearLayout) contentView.findViewById(R.id.bt_email);
        btDiscard = (LinearLayout) contentView.findViewById(R.id.bt_discard);
        btApprove = (LinearLayout) contentView.findViewById(R.id.bt_approve);
        btDecline = (LinearLayout) contentView.findViewById(R.id.bt_decline);
        tvDiscard = (TextView) contentView.findViewById(R.id.tv_discard);
        containerBottomButtons = (LinearLayout) contentView.findViewById(R.id.container_bottom_buttons_report);
        tvLabelGlobalRecord = (TextView) contentView.findViewById(R.id.tv_label_global_record);
        tvLabelUploadedBy = (TextView) contentView.findViewById(R.id.tv_label_uploaded_by);
        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded);
        View headerCreatedByReport = contentView.findViewById(R.id.container_header_created_by_report);
        View containerReportedBy = contentView.findViewById(R.id.container_reported_by);
        if (detailCombinedItemListener != null) {
            containerReportedBy.setVisibility(View.GONE);
            headerCreatedByReport.setVisibility(View.GONE);
            containerBottomButtons.setVisibility(View.GONE);
        } else {
            containerReportedBy.setVisibility(View.VISIBLE);
            headerCreatedByReport.setVisibility(View.VISIBLE);
            containerBottomButtons.setVisibility(View.VISIBLE);
        }
    }

    private void initListeners() {
        btHistory.setOnClickListener(this);
        btOpen.setOnClickListener(this);
        btEmail.setOnClickListener(this);
        btDiscard.setOnClickListener(this);
        ivReportType.setOnClickListener(this);
        btApprove.setOnClickListener(this);
        btDecline.setOnClickListener(this);
    }

    private void checkIsDiscarded(Boolean isDiscarded) {
        if (isDiscarded != null && isDiscarded)
            layoutDiscarded.setVisibility(View.VISIBLE);
        else
            layoutDiscarded.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_history:
                if (commonEmrClickListener != null) {
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        if (record.getInHistory() != null && record.getInHistory()) {
                            int msgId = R.string.confirm_remove_reports_from_history;
                            showConfirmationAlert(v.getId(), null, mActivity.getResources().getString(msgId));
                        } else
                            onAddRemoveHistoryClicked(record);
                    } else onNetworkUnavailable(null);
                }
                break;
            case R.id.bt_open:
            case R.id.iv_report_type:
                if (!Util.isNullOrBlank(record.getRecordsUrl()))
                    mActivity.openEnlargedImageDialogFragment(record.getRecordsUrl());
                break;
            case R.id.bt_email:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_REPORTS, AddUpdateNameDialogType.EMAIL, record.getUniqueId());
                else onNetworkUnavailable(null);
                break;
            case R.id.bt_discard:
                if (commonEmrClickListener != null) {
                    Util.checkNetworkStatus(mActivity);
                    if (HealthCocoConstants.isNetworkOnline) {
                        LogUtils.LOGD(TAG, "Discard");
                        int msgId = R.string.confirm_discard_reports_message;
                        int titleId = R.string.confirm_discard_reports_title;
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
                        mActivity.openEnlargedImageDialogFragment(true, record.getRecordsUrl());
                        mActivity.showLoading(false);
                        WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_REPORT_PDF_URL, record.getUniqueId(), this, this);
                    } else onNetworkUnavailable(null);
                }
                break;
            case R.id.bt_approve:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    changeRecordsState(RecordState.APPROVED_BY_DOCTOR);
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_decline:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    changeRecordsState(RecordState.DECLINED_BY_DOCTOR);
                } else onNetworkUnavailable(null);
                break;
            default:
                break;
        }
    }

    private void changeRecordsState(RecordState recordState) {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).changeRecordState(Records.class, record.getUniqueId(), recordState, this, this);
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
                        onDiscardedClicked(record);
                        break;
                    case R.id.bt_history:
                        onAddRemoveHistoryClicked(record);
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
        Records record = (Records) object;
        if (commonEmrClickListener != null)
            commonEmrClickListener.showLoading(true);
        WebDataServiceImpl.getInstance(mApp).discardRecord(Records.class, record.getUniqueId(), this, this);
    }

    public void onAddRemoveHistoryClicked(Object object) {
        Records record = (Records) object;
        if (!record.getInHistory()) {
            if (commonEmrClickListener != null)
                commonEmrClickListener.showLoading(true);
            WebDataServiceImpl.getInstance(mApp).addToHistory(Records.class, WebServiceType.ADD_TO_HISTORY_REPORT, record.getUniqueId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
        } else {
            if (commonEmrClickListener != null)
                commonEmrClickListener.showLoading(true);
            WebDataServiceImpl.getInstance(mApp).removeFromHistory(Prescription.class, WebServiceType.REMOVE_HISTORY_REPORT, record.getUniqueId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
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
        if (commonEmrClickListener != null)
            commonEmrClickListener.showLoading(false);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case DISCARD_REPORT:
                    LogUtils.LOGD(TAG, "Success DISCARD_PRESCRIPTION");
                    record.setDiscarded(!record.getDiscarded());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updateRecord(record);
                    Util.sendBroadcast(mApp, PatientReportsDetailFragment.INTENT_GET_REPORTS_LIST_LOCAL);
                    break;
                case ADD_TO_HISTORY_REPORT:
                    Util.showToast(mActivity, mActivity.getResources().getString(R.string.added_to_history));
                    record.setInHistory(!record.getInHistory());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updateRecord(record);
                    Util.sendBroadcast(mApp, PatientReportsDetailFragment.INTENT_GET_REPORTS_LIST_LOCAL);
                    break;
                case REMOVE_HISTORY_REPORT:
                    Util.showToast(mActivity, mActivity.getResources().getString(R.string.removed_from_history));
                    record.setInHistory(!record.getInHistory());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updateRecord(record);
                    Util.sendBroadcast(mApp, PatientReportsDetailFragment.INTENT_GET_REPORTS_LIST_LOCAL);
                    break;
                case CHANGE_RECORD_STATE:
                    if (response != null && response.getData() instanceof Records) {
                        Records recordResponse = (Records) response.getData();
                        record.setRecordsState(recordResponse.getRecordsState());
                        applyData();
                    }
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
        if (commonEmrClickListener != null)
            commonEmrClickListener.showLoading(false);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        if (bitmap != null) {
            record.setRecordsImageBitmap(bitmap);
            record.setLoadedBitmapsRecordUrl(record.getRecordsUrl());
        }
    }
}
