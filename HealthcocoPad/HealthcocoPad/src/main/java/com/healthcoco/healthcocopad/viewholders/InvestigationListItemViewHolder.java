package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.InvestigationNote;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.InvestigationSubType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.listeners.TreatmentListItemClickListeners;
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
public class InvestigationListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {

    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, dd MMM yyyy";
    private static final String TAG = InvestigationListItemViewHolder.class.getSimpleName();
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private VisitDetailCombinedItemListener detailCombinedItemListener;
    private TextView tvTreatmentBy;
    private TextView tvDate;
    private String loginedUser;
    private InvestigationNote investigationNote;
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


    private LinearLayout parentHaematology;
    private LinearLayout layoutHaematology;
    private LinearLayout parentBioChemistry;
    private LinearLayout layoutBioChemistry;
    private LinearLayout parentLipidProfile;
    private LinearLayout layoutLipidProfile;
    private LinearLayout parentKidneyFunctionTest;
    private LinearLayout layoutKidneyFunctionTest;
    private LinearLayout parentLiverFuntionTest;
    private LinearLayout layoutLiverFuntionTest;


    public InvestigationListItemViewHolder(HealthCocoActivity mActivity,
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
        this.investigationNote = (InvestigationNote) object;
    }

    @Override
    public void applyData() {
        if (investigationNote != null) {
            if (!Util.isNullOrBlank(investigationNote.getCreatedBy()))
                tvTreatmentBy.setText(investigationNote.getCreatedBy());
            else
                tvTreatmentBy.setText("");
            tvDate.setText(DateTimeUtil.getFormatedDate(investigationNote.getCreatedTime()));
            if (!Util.isNullOrBlank(investigationNote.getInvestigationId())) {
                tvTreatmentid.setVisibility(View.VISIBLE);
                tvTreatmentid.setText(mActivity.getResources().getString(R.string.investigation_id) + investigationNote.getInvestigationId());
            } else
                tvTreatmentid.setVisibility(View.GONE);


            initUi(investigationNote);
            checkIsDiscarded(investigationNote.getDiscarded());
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
        View contentView = inflater.inflate(R.layout.list_item_investigation_note, null);
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

        parentHaematology = (LinearLayout) contentView.findViewById(R.id.parent_haematology);
        layoutHaematology = (LinearLayout) contentView.findViewById(R.id.layout_haematology_field);

        parentBioChemistry = (LinearLayout) contentView.findViewById(R.id.parent_bio_chemistry);
        layoutBioChemistry = (LinearLayout) contentView.findViewById(R.id.layout_bio_chemistry_field);

        parentLipidProfile = (LinearLayout) contentView.findViewById(R.id.parent_lipid_profile);
        layoutLipidProfile = (LinearLayout) contentView.findViewById(R.id.layout_lipid_profile_field);

        parentKidneyFunctionTest = (LinearLayout) contentView.findViewById(R.id.parent_kidney_function_test);
        layoutKidneyFunctionTest = (LinearLayout) contentView.findViewById(R.id.layout_kidney_function_test_field);

        parentLiverFuntionTest = (LinearLayout) contentView.findViewById(R.id.parent_lever_function_test);
        layoutLiverFuntionTest = (LinearLayout) contentView.findViewById(R.id.layout_lever_function_test_field);

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
            imageView.setVisibility(View.VISIBLE);
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
                                investigationNote.getUniqueId(), investigationNote.getDoctorId(), investigationNote.getLocationId(), investigationNote.getHospitalId());
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
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
//                    listItemClickListeners.onInvoiceClicked(investigationNote);
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_edit:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    listItemClickListeners.onTreatmentItemClick(investigationNote);
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
                        WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_TREATMENT_PDF_URL, investigationNote.getUniqueId(), this, this);
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
                    investigationNote.setDiscarded(!investigationNote.getDiscarded());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).addInvestigationNote(investigationNote);
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
        investigationNote.setDiscarded(!investigationNote.getDiscarded());
        applyData();
        LocalDataServiceImpl.getInstance(mApp).addInvestigationNote(investigationNote);
//        } else onNetworkUnavailable(null);
    }

    public void initUi(InvestigationNote investigationNote) {
//        if (!Util.isNullOrEmptyList(inve)) {
//        parentPermissionItems.removeAllViews();

        layoutHaematology.removeAllViews();
        layoutBioChemistry.removeAllViews();
        layoutLipidProfile.removeAllViews();
        layoutKidneyFunctionTest.removeAllViews();
        layoutLiverFuntionTest.removeAllViews();

        layoutHaematology.setVisibility(View.GONE);
        layoutBioChemistry.setVisibility(View.GONE);
        layoutLipidProfile.setVisibility(View.GONE);
        layoutKidneyFunctionTest.setVisibility(View.GONE);
        layoutLiverFuntionTest.setVisibility(View.GONE);

        parentHaematology.setVisibility(View.GONE);
        parentBioChemistry.setVisibility(View.GONE);
        parentLipidProfile.setVisibility(View.GONE);
        parentKidneyFunctionTest.setVisibility(View.GONE);
        parentLiverFuntionTest.setVisibility(View.GONE);


        addHaemotologyPermissionItem(investigationNote);
//        }
    }

    private void addHaemotologyPermissionItem(InvestigationNote investigationNote) {

        if (!Util.isNullOrBlank(investigationNote.getAbsoluteEosinophilCount())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.AEC));
        }
        if (!Util.isNullOrBlank(investigationNote.getHaemoglobin())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.HAEMOGLOBIN));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getTotalWcbCount())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.TWC));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getHaematocrit())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.HAEMATOCRIT));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getNeutrophils())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.NEUTROPHILS));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getLymphocytes())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.LYMPHOCYTES));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getEosinophils())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.EOSINOPHILS));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getMonocytes())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.MONOCYTES));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getBasophils())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.BASOPHILS));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getRbcRedBloodCells())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.RBC));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getErythrocyteSedimentationrate())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.ESR));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getRbcs())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.RBCS));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getWbcs())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.WBCS));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getPlatelets())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.PLATELETS));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getHaemoparasites())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.HAEMOPARASITES));
            layoutHaematology.setVisibility(View.VISIBLE)
            ;
        }
        if (!Util.isNullOrBlank(investigationNote.getImpression())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.IMPRESSION));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getMeanCorpuscularVolume())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.MCV));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getMeanCorpuscularHaemoglobin())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.MCH));
            layoutHaematology.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getMeanCorpuscularHaemoglobinConcentration())) {
            parentHaematology.setVisibility(View.VISIBLE);
            layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.MCHC));
            layoutHaematology.setVisibility(View.VISIBLE);
        }


        if (!Util.isNullOrBlank(investigationNote.getFastingBloodSugar())) {
            parentBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.FSB));
        }
        if (!Util.isNullOrBlank(investigationNote.getFastingUrineSugar())) {
            parentBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.FUS));
            layoutBioChemistry.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getPostPrandialBloodSugar())) {
            parentBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.PPBS));
            layoutBioChemistry.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getPostPrandialUrineSugar())) {
            parentBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.PPUS));
            layoutBioChemistry.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getGlycosylatedHaemoglobin())) {
            parentBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.GH));
            layoutBioChemistry.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getMeanBloodGlucose())) {
            parentBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.MBG));
            layoutBioChemistry.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getRbs())) {
            parentBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.RBS));
            layoutBioChemistry.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getRandomUrineSugar())) {
            parentBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.RUS));
            layoutBioChemistry.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getKetone())) {
            parentBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.KETONE));
            layoutBioChemistry.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getProtein())) {
            parentBioChemistry.setVisibility(View.VISIBLE);
            layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.PROTEIN));
            layoutBioChemistry.setVisibility(View.VISIBLE);
        }


        if (!Util.isNullOrBlank(investigationNote.getBloodUrea())) {
            parentLipidProfile.setVisibility(View.VISIBLE);
            layoutLipidProfile.setVisibility(View.VISIBLE);
            layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.TC));
        }
        if (!Util.isNullOrBlank(investigationNote.getSerumCreatinine())) {
            parentLipidProfile.setVisibility(View.VISIBLE);
            layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.SHC));
            layoutLipidProfile.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getSerumSodium())) {
            parentLipidProfile.setVisibility(View.VISIBLE);
            layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.ST));
            layoutLipidProfile.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getTotalCholesterol())) {
            parentLipidProfile.setVisibility(View.VISIBLE);
            layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.SLC));
            layoutLipidProfile.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getSerumHdlCholesterol())) {
            parentLipidProfile.setVisibility(View.VISIBLE);
            layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.SVC));
            layoutLipidProfile.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getSerumTriglycerides())) {
            parentLipidProfile.setVisibility(View.VISIBLE);
            layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.LDL));
            layoutLipidProfile.setVisibility(View.VISIBLE);
        }
        if (!Util.isNullOrBlank(investigationNote.getSerumLdlCholesterol())) {
            parentLipidProfile.setVisibility(View.VISIBLE);
            layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.TRIGLYCERIDES));
            layoutLipidProfile.setVisibility(View.VISIBLE);

        }
        if (!Util.isNullOrBlank(investigationNote.getSerumVdlCholesterol())) {
            parentLipidProfile.setVisibility(View.VISIBLE);
            layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.NHC));
            layoutLipidProfile.setVisibility(View.VISIBLE);
        }


        if (!Util.isNullOrBlank(investigationNote.getLdlCholesterol())) {
            parentKidneyFunctionTest.setVisibility(View.VISIBLE);
            layoutKidneyFunctionTest.setVisibility(View.VISIBLE);
            layoutKidneyFunctionTest.addView(addEntSubPermissionItem(InvestigationSubType.BU));
        }
        if (!Util.isNullOrBlank(investigationNote.getTrigkycerides())) {
            parentKidneyFunctionTest.setVisibility(View.VISIBLE);
            layoutKidneyFunctionTest.setVisibility(View.VISIBLE);
            layoutKidneyFunctionTest.addView(addEntSubPermissionItem(InvestigationSubType.SC));
        }
        if (!Util.isNullOrBlank(investigationNote.getNonHdlCholesterol())) {
            parentKidneyFunctionTest.setVisibility(View.VISIBLE);
            layoutKidneyFunctionTest.setVisibility(View.VISIBLE);
            layoutKidneyFunctionTest.addView(addEntSubPermissionItem(InvestigationSubType.SS));
        }

        if (!Util.isNullOrBlank(investigationNote.getSerumBilirubinTotal())) {
            parentLiverFuntionTest.setVisibility(View.VISIBLE);
            layoutLiverFuntionTest.setVisibility(View.VISIBLE);
            layoutLiverFuntionTest.addView(addEntSubPermissionItem(InvestigationSubType.SBT));
        }
        if (!Util.isNullOrBlank(investigationNote.getSerumBilirubinDirect())) {
            parentLiverFuntionTest.setVisibility(View.VISIBLE);
            layoutLiverFuntionTest.setVisibility(View.VISIBLE);
            layoutLiverFuntionTest.addView(addEntSubPermissionItem(InvestigationSubType.SBD));
        }
        if (!Util.isNullOrBlank(investigationNote.getBilirubinIndirect())) {
            parentLiverFuntionTest.setVisibility(View.VISIBLE);
            layoutLiverFuntionTest.setVisibility(View.VISIBLE);
            layoutLiverFuntionTest.addView(addEntSubPermissionItem(InvestigationSubType.BI));
        }

    }

    private LinearLayout addEntSubPermissionItem(InvestigationSubType investigationSubType) {
        LinearLayout layoutItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_sub_item_investigation, null);
        TextView tvTitle = (TextView) layoutItemPermission.findViewById(R.id.tv_title);
        TextView autotvPermission = (TextView) layoutItemPermission.findViewById(R.id.edit_permission_text);
        tvTitle.setText(investigationSubType.getTextId());
        autotvPermission.setHint(investigationSubType.getHintId());
        autotvPermission.setTag(String.valueOf(investigationSubType));
        String text = "";
        if (investigationNote != null) {
            switch (investigationSubType) {
                case AEC:
                    text = investigationNote.getAbsoluteEosinophilCount();
                    break;
                case HAEMOGLOBIN:
                    text = investigationNote.getHaemoglobin();
                    break;
                case TWC:
                    text = investigationNote.getTotalWcbCount();
                    break;
                case HAEMATOCRIT:
                    text = investigationNote.getHaematocrit();
                    break;
                case NEUTROPHILS:
                    text = investigationNote.getNeutrophils();
                    break;
                case LYMPHOCYTES:
                    text = investigationNote.getLymphocytes();
                    break;
                case EOSINOPHILS:
                    text = investigationNote.getEosinophils();
                    break;
                case MONOCYTES:
                    text = investigationNote.getMonocytes();
                    break;
                case BASOPHILS:
                    text = investigationNote.getBasophils();
                    break;
                case RBC:
                    text = investigationNote.getRbcRedBloodCells();
                    break;
                case ESR:
                    text = investigationNote.getErythrocyteSedimentationrate();
                    break;
                case RBCS:
                    text = investigationNote.getRbcs();
                    break;
                case WBCS:
                    text = investigationNote.getWbcs();
                    break;
                case PLATELETS:
                    text = investigationNote.getPlatelets();
                    break;
                case HAEMOPARASITES:
                    text = investigationNote.getHaemoparasites();
                    break;
                case IMPRESSION:
                    text = investigationNote.getImpression();
                    break;
                case MCV:
                    text = investigationNote.getMeanCorpuscularVolume();
                    break;
                case MCH:
                    text = investigationNote.getMeanCorpuscularHaemoglobin();
                    break;
                case MCHC:
                    text = investigationNote.getMeanCorpuscularHaemoglobinConcentration();
                    break;
                case FSB:
                    text = investigationNote.getFastingBloodSugar();
                    break;
                case FUS:
                    text = investigationNote.getFastingUrineSugar();
                    break;
                case PPBS:
                    text = investigationNote.getPostPrandialBloodSugar();
                    break;
                case PPUS:
                    text = investigationNote.getPostPrandialUrineSugar();
                    break;
                case GH:
                    text = investigationNote.getGlycosylatedHaemoglobin();
                    break;
                case MBG:
                    text = investigationNote.getMeanBloodGlucose();
                    break;
                case RBS:
                    text = investigationNote.getRbs();
                    break;
                case RUS:
                    text = investigationNote.getRandomUrineSugar();
                    break;
                case KETONE:
                    text = investigationNote.getKetone();
                    break;
                case PROTEIN:
                    text = investigationNote.getProtein();
                    break;
                case BU:
                    text = investigationNote.getBloodUrea();
                    break;
                case SC:
                    text = investigationNote.getSerumCreatinine();
                    break;
                case SS:
                    text = investigationNote.getSerumSodium();
                    break;
                case TC:
                    text = investigationNote.getTotalCholesterol();
                    break;
                case SHC:
                    text = investigationNote.getSerumHdlCholesterol();
                    break;
                case ST:
                    text = investigationNote.getSerumTriglycerides();
                    break;
                case SLC:
                    text = investigationNote.getSerumLdlCholesterol();
                    break;
                case SVC:
                    text = investigationNote.getSerumVdlCholesterol();
                    break;
                case LDL:
                    text = investigationNote.getLdlCholesterol();
                    break;
                case TRIGLYCERIDES:
                    text = investigationNote.getTrigkycerides();
                    break;
                case NHC:
                    text = investigationNote.getNonHdlCholesterol();
                    break;
                case SBT:
                    text = investigationNote.getSerumBilirubinTotal();
                    break;
                case SBD:
                    text = investigationNote.getSerumBilirubinDirect();
                    break;
                case BI:
                    text = investigationNote.getBilirubinIndirect();
                    break;
            }
            autotvPermission.setText(Util.getValidatedValue(text));
        }
        return layoutItemPermission;
    }


}
