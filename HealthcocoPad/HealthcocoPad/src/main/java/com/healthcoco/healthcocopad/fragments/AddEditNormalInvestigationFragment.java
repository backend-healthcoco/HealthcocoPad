package com.healthcoco.healthcocopad.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.InvestigationNote;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.NextReviewOnDialogFragment;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.InvestigationSubType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Prashant on 19-07-2018.
 */

public class AddEditNormalInvestigationFragment extends HealthCocoFragment implements View.OnClickListener,
        LocalDoInBackgroundListenerOptimised {

    public static final String TAG_INVESTIGATION_NOTE_ID = "investigationNoteId";

    private LinearLayout parentPermissionItems;
    private InvestigationNote investigationNote;
    private User user;
    private ScrollView svScrollView;
    private String investigationNoteId;
    private LinearLayout layoutNextReview;
    private TextView tvNextReviewDate;
    private TextView tvDoctorName;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvDate;
    private TextView tvNextReviewTime;
    private LinearLayout lvDoctorName;
    private LinearLayout containerDateTime;
    private LinearLayout btSave;
    private InvestigationNote investigationNoteToSend;
    private long selectedFromDateTimeMillis;

    private boolean isInitialLoading = true;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_investigation_notes, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = mActivity.getIntent();
        if (intent != null) {
            investigationNoteId = intent.getStringExtra(TAG_INVESTIGATION_NOTE_ID);
        }
        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {

        layoutNextReview = (LinearLayout) view.findViewById(R.id.layout_next_review);
        containerDateTime = (LinearLayout) view.findViewById(R.id.container_date_time);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        lvDoctorName = (LinearLayout) view.findViewById(R.id.lv_doctor_name);
        tvNextReviewDate = (TextView) view.findViewById(R.id.tv_next_review_data);
        tvNextReviewTime = (TextView) view.findViewById(R.id.tv_next_review_time);
        containerDateTime.setVisibility(View.GONE);
        layoutNextReview.setVisibility(View.GONE);

        parentPermissionItems = (LinearLayout) view.findViewById(R.id.layout_parent);

        parentHaematology = (LinearLayout) view.findViewById(R.id.parent_haematology);
        layoutHaematology = (LinearLayout) view.findViewById(R.id.layout_haematology_field);

        parentBioChemistry = (LinearLayout) view.findViewById(R.id.parent_bio_chemistry);
        layoutBioChemistry = (LinearLayout) view.findViewById(R.id.layout_bio_chemistry_field);

        parentLipidProfile = (LinearLayout) view.findViewById(R.id.parent_lipid_profile);
        layoutLipidProfile = (LinearLayout) view.findViewById(R.id.layout_lipid_profile_field);

        parentKidneyFunctionTest = (LinearLayout) view.findViewById(R.id.parent_kidney_function_test);
        layoutKidneyFunctionTest = (LinearLayout) view.findViewById(R.id.layout_kidney_function_test_field);

        parentLiverFuntionTest = (LinearLayout) view.findViewById(R.id.parent_lever_function_test);
        layoutLiverFuntionTest = (LinearLayout) view.findViewById(R.id.layout_lever_function_test_field);

        svScrollView = (ScrollView) view.findViewById(R.id.sv_scrollview);
        //default UI ettings
//        parentPermissionItems.removeAllViews();
    }

    @Override
    public void initListeners() {
        layoutNextReview.setOnClickListener(this);
        tvDate.setOnClickListener(this);

        ((CommonOpenUpActivity) mActivity).initRightActionView(ActionbarLeftRightActionTypeDrawables.WITH_SAVE, view);
        btSave = ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

    /*    parentHaematology.setOnClickListener(this);
        parentBioChemistry.setOnClickListener(this);
        parentKidneyFunctionTest.setOnClickListener(this);
        parentLipidProfile.setOnClickListener(this);
        parentLiverFuntionTest.setOnClickListener(this);*/
    }


    public void initUiPermissions() {
//        if (!Util.isNullOrEmptyList(inve)) {
//        parentPermissionItems.removeAllViews();

        layoutHaematology.removeAllViews();
        layoutBioChemistry.removeAllViews();
        layoutLipidProfile.removeAllViews();
        layoutKidneyFunctionTest.removeAllViews();
        layoutLiverFuntionTest.removeAllViews();

        addHaemotologyPermissionItem();
        addBioChemistryPermissionItem();
        addLipidProfilePesrmissionItem();
        addKidneyFunctionTestPesrmissionItem();
        addLiverFuntionTestPesrmissionItem();
//        }
    }


    private void addHaemotologyPermissionItem() {
        parentHaematology.setVisibility(View.VISIBLE);
        layoutHaematology.setVisibility(View.VISIBLE);

        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.AEC));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.HAEMOGLOBIN));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.TWC));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.HAEMATOCRIT));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.NEUTROPHILS));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.LYMPHOCYTES));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.EOSINOPHILS));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.MONOCYTES));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.BASOPHILS));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.RBC));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.ESR));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.RBCS));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.WBCS));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.PLATELETS));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.HAEMOPARASITES));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.IMPRESSION));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.MCV));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.MCH));
        layoutHaematology.addView(addEntSubPermissionItem(InvestigationSubType.MCHC));
    }

    private void addBioChemistryPermissionItem() {
        parentBioChemistry.setVisibility(View.VISIBLE);
        layoutBioChemistry.setVisibility(View.VISIBLE);

        layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.FSB));
        layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.FUS));
        layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.PPBS));
        layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.PPUS));
        layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.GH));
        layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.MBG));
        layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.RBS));
        layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.RUS));
        layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.KETONE));
        layoutBioChemistry.addView(addEntSubPermissionItem(InvestigationSubType.PROTEIN));
    }

    private void addKidneyFunctionTestPesrmissionItem() {
        parentKidneyFunctionTest.setVisibility(View.VISIBLE);
        layoutKidneyFunctionTest.setVisibility(View.VISIBLE);

        layoutKidneyFunctionTest.addView(addEntSubPermissionItem(InvestigationSubType.BU));
        layoutKidneyFunctionTest.addView(addEntSubPermissionItem(InvestigationSubType.SC));
        layoutKidneyFunctionTest.addView(addEntSubPermissionItem(InvestigationSubType.SS));
    }

    private void addLipidProfilePesrmissionItem() {
        parentLipidProfile.setVisibility(View.VISIBLE);
        layoutLipidProfile.setVisibility(View.VISIBLE);

        layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.TC));
        layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.SHC));
        layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.ST));
        layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.SLC));
        layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.SVC));
        layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.LDL));
        layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.TRIGLYCERIDES));
        layoutLipidProfile.addView(addEntSubPermissionItem(InvestigationSubType.NHC));
    }

    private void addLiverFuntionTestPesrmissionItem() {
        parentLiverFuntionTest.setVisibility(View.VISIBLE);
        layoutLiverFuntionTest.setVisibility(View.VISIBLE);

        layoutLiverFuntionTest.addView(addEntSubPermissionItem(InvestigationSubType.SBT));
        layoutLiverFuntionTest.addView(addEntSubPermissionItem(InvestigationSubType.SBD));
        layoutLiverFuntionTest.addView(addEntSubPermissionItem(InvestigationSubType.BI));
    }

    private LinearLayout addEntSubPermissionItem(InvestigationSubType investigationSubType) {
        LinearLayout layoutItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_investigation_permision, null);
        TextView tvTitle = (TextView) layoutItemPermission.findViewById(R.id.tv_title);
        EditText autotvPermission = (EditText) layoutItemPermission.findViewById(R.id.edit_permission_text);
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


    public void getInvestigationNoteToSend() {
        investigationNoteToSend = new InvestigationNote();
        investigationNoteToSend.setCreatedBy(user.getFirstName());
        investigationNoteToSend.setDoctorId(user.getUniqueId());
        investigationNoteToSend.setLocationId(user.getForeignLocationId());
        investigationNoteToSend.setHospitalId(user.getForeignHospitalId());
        investigationNoteToSend.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        for (InvestigationSubType investigationSubType :
                InvestigationSubType.values()) {
            switch (investigationSubType) {
                case AEC:
                    investigationNoteToSend.setAbsoluteEosinophilCount(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.AEC.getValue())));
                    break;
                case HAEMOGLOBIN:
                    investigationNoteToSend.setHaemoglobin(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.HAEMOGLOBIN.getValue())));
                    break;
                case TWC:
                    investigationNoteToSend.setTotalWcbCount(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.TWC.getValue())));
                    break;
                case HAEMATOCRIT:
                    investigationNoteToSend.setHaematocrit(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.HAEMATOCRIT.getValue())));
                    break;
                case NEUTROPHILS:
                    investigationNoteToSend.setNeutrophils(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.NEUTROPHILS.getValue())));
                    break;
                case LYMPHOCYTES:
                    investigationNoteToSend.setLymphocytes(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.LYMPHOCYTES.getValue())));
                    break;
                case EOSINOPHILS:
                    investigationNoteToSend.setEosinophils(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.EOSINOPHILS.getValue())));
                    break;
                case MONOCYTES:
                    investigationNoteToSend.setMonocytes(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.MONOCYTES.getValue())));
                    break;
                case BASOPHILS:
                    investigationNoteToSend.setBasophils(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.BASOPHILS.getValue())));
                    break;
                case RBC:
                    investigationNoteToSend.setRbcRedBloodCells(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.RBC.getValue())));
                    break;
                case ESR:
                    investigationNoteToSend.setErythrocyteSedimentationrate(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.ESR.getValue())));
                    break;
                case RBCS:
                    investigationNoteToSend.setRbcs(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.RBCS.getValue())));
                    break;
                case WBCS:
                    investigationNoteToSend.setWbcs(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.WBCS.getValue())));
                    break;
                case PLATELETS:
                    investigationNoteToSend.setPlatelets(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.PLATELETS.getValue())));
                    break;
                case HAEMOPARASITES:
                    investigationNoteToSend.setHaemoparasites(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.HAEMOPARASITES.getValue())));
                    break;
                case IMPRESSION:
                    investigationNoteToSend.setImpression(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.IMPRESSION.getValue())));
                    break;
                case MCV:
                    investigationNoteToSend.setMeanCorpuscularVolume(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.MCV.getValue())));
                    break;
                case MCH:
                    investigationNoteToSend.setMeanCorpuscularHaemoglobin(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.MCH.getValue())));
                    break;
                case MCHC:
                    investigationNoteToSend.setMeanCorpuscularHaemoglobinConcentration(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.MCHC.getValue())));
                    break;
                case FSB:
                    investigationNoteToSend.setFastingBloodSugar(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.FSB.getValue())));
                    break;
                case FUS:
                    investigationNoteToSend.setFastingUrineSugar(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.FUS.getValue())));
                    break;
                case PPBS:
                    investigationNoteToSend.setPostPrandialBloodSugar(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.PPBS.getValue())));
                    break;
                case PPUS:
                    investigationNoteToSend.setPostPrandialUrineSugar(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.PPUS.getValue())));
                    break;
                case GH:
                    investigationNoteToSend.setGlycosylatedHaemoglobin(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.GH.getValue())));
                    break;
                case MBG:
                    investigationNoteToSend.setMeanBloodGlucose(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.MBG.getValue())));
                    break;
                case RBS:
                    investigationNoteToSend.setRbs(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.RBS.getValue())));
                    break;
                case RUS:
                    investigationNoteToSend.setRandomUrineSugar(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.RUS.getValue())));
                    break;
                case KETONE:
                    investigationNoteToSend.setKetone(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.KETONE.getValue())));
                    break;
                case PROTEIN:
                    investigationNoteToSend.setProtein(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.PROTEIN.getValue())));
                    break;
                case BU:
                    investigationNoteToSend.setBloodUrea(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.BU.getValue())));
                    break;
                case SC:
                    investigationNoteToSend.setSerumCreatinine(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.SC.getValue())));
                    break;
                case SS:
                    investigationNoteToSend.setSerumSodium(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.SS.getValue())));
                    break;
                case TC:
                    investigationNoteToSend.setTotalCholesterol(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.TC.getValue())));
                    break;
                case SHC:
                    investigationNoteToSend.setSerumHdlCholesterol(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.SHC.getValue())));
                    break;
                case ST:
                    investigationNoteToSend.setSerumTriglycerides(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.ST.getValue())));
                    break;
                case SLC:
                    investigationNoteToSend.setSerumLdlCholesterol(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.SLC.getValue())));
                    break;
                case SVC:
                    investigationNoteToSend.setSerumVdlCholesterol(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.SVC.getValue())));
                    break;
                case LDL:
                    investigationNoteToSend.setLdlCholesterol(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.LDL.getValue())));
                    break;
                case TRIGLYCERIDES:
                    investigationNoteToSend.setTrigkycerides(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.TRIGLYCERIDES.getValue())));
                    break;
                case NHC:
                    investigationNoteToSend.setNonHdlCholesterol(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.NHC.getValue())));
                    break;
                case SBT:
                    investigationNoteToSend.setSerumBilirubinTotal(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.SBT.getValue())));
                    break;
                case SBD:
                    investigationNoteToSend.setSerumBilirubinDirect(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.SBD.getValue())));
                    break;
                case BI:
                    investigationNoteToSend.setBilirubinIndirect(Util.getValidatedValueOrBlankTrimming((TextView) parentPermissionItems.findViewWithTag(InvestigationSubType.BI.getValue())));
                    break;
            }
        }
        if (!Util.isNullOrBlank(investigationNoteId))
            investigationNoteToSend.setUniqueId(investigationNoteId);
        else
            investigationNoteToSend.setUniqueId(DateTimeUtil.getCurrentDateLong().toString());

        if (selectedFromDateTimeMillis > 1)
            investigationNoteToSend.setCreatedTime(selectedFromDateTimeMillis);
        else
            investigationNoteToSend.setCreatedTime(DateTimeUtil.getCurrentDateLong());

        if (investigationNote != null && !Util.isNullOrBlank(investigationNote.getInvestigationId())) {
            investigationNoteToSend.setInvestigationId(investigationNote.getInvestigationId());
        } else {
            investigationNoteToSend.setInvestigationId(Util.generateKey("IV"));
        }
        investigationNoteToSend.setUpdatedTime(DateTimeUtil.getCurrentDateLong());

        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_INVESTIGATION_NOTE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId())
                            && !Util.isNullOrBlank(user.getForeignHospitalId())
                            && !Util.isNullOrBlank(user.getForeignLocationId()) && user.getUiPermissions() != null) {
                        initUiPermissions();
                        if (investigationNote != null) {
                            tvDate.setText(DateTimeUtil.getFormatedDate(investigationNote.getCreatedTime()));
                            selectedFromDateTimeMillis = investigationNote.getCreatedTime();
                        }
                        initActionPatientDetailActionBar(PatientProfileScreenType.IN_ADD_VISIT_HEADER, view, selectedPatient);
                    } else {
                        mActivity.hideLoading();
                        mActivity.finish();
                    }
                    break;
                case ADD_INVESTIGATION_NOTE:
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_NEW_IVESTIGATION, null);
                    ((CommonOpenUpActivity) mActivity).finish();
                    mActivity.hideLoading();
                    break;
            }
        }
        isInitialLoading = false;
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                }
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                if (!Util.isNullOrBlank(investigationNoteId))
                    investigationNote = LocalDataServiceImpl.getInstance(mApp).getInvestigationNote(investigationNoteId);
                break;
            case ADD_INVESTIGATION_NOTE:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.ADD_INVESTIGATION_NOTE);

                LocalDataServiceImpl.getInstance(mApp).addInvestigationNote(investigationNoteToSend);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    public boolean isInitialLoading() {
        return isInitialLoading;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                validateData();
                break;
            case R.id.parent_haematology:
                toggleLayoutView(layoutHaematology);
                break;
            case R.id.parent_bio_chemistry:
                toggleLayoutView(layoutBioChemistry);
                break;
            case R.id.parent_kidney_function_test:
                toggleLayoutView(layoutKidneyFunctionTest);
                break;
            case R.id.parent_lipid_profile:
                toggleLayoutView(layoutLipidProfile);
                break;
            case R.id.parent_lever_function_test:
                toggleLayoutView(layoutLiverFuntionTest);
                break;
            case R.id.tv_date:
                openDatePickerDialog();
                break;
            case R.id.layout_next_review:
//                if (visit != null && visit.getAppointmentRequest() != null && visit.getAppointmentId() != null && !isFromClone) {
//                    showNextReviewReschedulealert();
//                } else
//                    openDialogFragment(new NextReviewOnDialogFragment(), REQUEST_CODE_NEXT_REVIEW);
                break;
        }
    }

    private void validateData() {
        if (isBlankClinicalNote()) {
            mActivity.showLoading(false);
            getInvestigationNoteToSend();
        } else {
            Util.showToast(mActivity, mActivity.getString(R.string.blank_investigation_note));
        }
    }

    public boolean isBlankClinicalNote() {
        mActivity.showLoading(false);
        boolean isDataPresent = false;
        for (InvestigationSubType investigationSubType :
                InvestigationSubType.values()) {
            EditText autoTvPermission = (EditText) view.findViewWithTag(investigationSubType.getValue());
            if (autoTvPermission != null && !Util.isNullOrBlank(Util.getValidatedValueOrBlankTrimming(autoTvPermission))) {
                isDataPresent = true;
                break;
            }
        }
        mActivity.hideLoading();
        return isDataPresent;
    }

    private void toggleLayoutView(LinearLayout linearLayout) {
        if (linearLayout.isShown()) {
            Util.slideUp(mActivity, linearLayout);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            Util.slideDown(mActivity, linearLayout);
        }
    }

    private void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        calendar = DateTimeUtil.setCalendarDefaultvalue(DateTimeUtil.DATE_FORMAT_DAY_MONTH_YEAR_SLASH, calendar, tvDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                tvDate.setText(DateTimeUtil.getFormattedTime(DateTimeUtil.DATE_FORMAT_DAY_MONTH_YEAR_SLASH, year, monthOfYear, dayOfMonth, 0, 0, 0));
                int msg = 0;
                selectedFromDateTimeMillis = DateTimeUtil.getSelectedFromDateTime(year, monthOfYear, dayOfMonth);
                tvDate.setText(DateTimeUtil.getFormatedDate(selectedFromDateTimeMillis));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

}
