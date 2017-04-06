package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.AddVisitsActivity;
import com.healthcoco.healthcocopad.bean.UIPermissions;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.BloodPressure;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VitalSigns;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ClinicalNotesPermissionType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shreshtha on 05-04-2017.
 */
public class AddVisitsFragment extends HealthCocoFragment implements View.OnClickListener, LocalDoInBackgroundListenerOptimised {
    private static final int REQUEST_CODE_ADD_CLINICAL_NOTES = 100;
    private TextViewFontAwesome btClose;
    private ImageButton btClinicalNote;
    private ImageButton btPrescription;
    private ImageButton btLabTests;
    private ImageButton btDiagrams;
    private ImageButton btAdvice;
    private TextViewFontAwesome btSave;
    private TextView tvHeaderText;
    private LinearLayout parentClinicalNote;
    private LinearLayout parentPrescription;
    private LinearLayout parentDiagnosticTests;
    private LinearLayout parentDiagrams;
    private LinearLayout layoutParentAdvice;
    private TextView tvPatientName;
    private TextView tvDate;
    private TextView tvPatientId;
    private TextView tvRxId;
    private TextView tvMobileNo;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private HashMap<String, String> diagramsList = new HashMap<>();
    private ScrollView svScrollView;
    private LinearLayout containerDiagrams;
    private ArrayList<ClinicalNotesPermissionType> clinicalNotesUiPermissionsList;
    private LinearLayout parentVitalSigns;
    private LinearLayout parentPermissionItems;
    private ClinicalNotes clinicalNotes;
    private VitalSigns selectedVitalSigns;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_visits_info, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        svScrollView = (ScrollView) view.findViewById(R.id.sv_scrollview);
        initToolbarView();
        initHeaderView();
        initClinicalNotesView();
        initPrescriptionsView();
        initDiagonsticTestsView();
        initDiagramsViews();
        initAdviceViews();
    }

    private void initToolbarView() {
        btClose = (TextViewFontAwesome) view.findViewById(R.id.bt_close);
        btClinicalNote = (ImageButton) view.findViewById(R.id.bt_clinical_note);
        btPrescription = (ImageButton) view.findViewById(R.id.bt_prescription);
        btLabTests = (ImageButton) view.findViewById(R.id.bt_lab_tests);
        btDiagrams = (ImageButton) view.findViewById(R.id.bt_diagrams);
        btAdvice = (ImageButton) view.findViewById(R.id.bt_advice);
        btSave = (TextViewFontAwesome) view.findViewById(R.id.bt_save);
    }

    private void initHeaderView() {
        tvHeaderText = (TextView) view.findViewById(R.id.tv_header_text);
        tvPatientName = (TextView) view.findViewById(R.id.tv_patient_name);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvPatientId = (TextView) view.findViewById(R.id.tv_patient_id);
        tvRxId = (TextView) view.findViewById(R.id.tv_rx_id);
        tvMobileNo = (TextView) view.findViewById(R.id.tv_mobile_no);
    }

    private void initClinicalNotesView() {
        parentClinicalNote = (LinearLayout) view.findViewById(R.id.parent_clinical_note);
        parentVitalSigns = (LinearLayout) view.findViewById(R.id.parent_add_vital_signs);
        parentPermissionItems = (LinearLayout) view.findViewById(R.id.parent_permissions_items);
        //default UI ettings
        parentPermissionItems.removeAllViews();
        parentClinicalNote.setVisibility(View.GONE);
//        parentVitalSigns.setVisibility(View.GONE);
    }

    private void initPrescriptionsView() {
        parentPrescription = (LinearLayout) view.findViewById(R.id.parent_prescription);
        parentPrescription.setVisibility(View.GONE);
    }

    private void initDiagonsticTestsView() {
        parentDiagnosticTests = (LinearLayout) view.findViewById(R.id.parent_diagnostic_tests);
        parentDiagnosticTests.setVisibility(View.GONE);
    }

    private void initDiagramsViews() {
        parentDiagrams = (LinearLayout) view.findViewById(R.id.parent_diagrams);
        containerDiagrams = (LinearLayout) view.findViewById(R.id.container_diagrams);
        parentDiagrams.setVisibility(View.GONE);
    }

    private void initAdviceViews() {
        layoutParentAdvice = (LinearLayout) view.findViewById(R.id.layout_parent_advice);
        layoutParentAdvice.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        btClose.setOnClickListener(this);
        btClinicalNote.setOnClickListener(this);
        btPrescription.setOnClickListener(this);
        btLabTests.setOnClickListener(this);
        btDiagrams.setOnClickListener(this);
        btAdvice.setOnClickListener(this);
        btSave.setOnClickListener(this);
    }

    private void initData() {
        tvPatientName.setText(Util.getValidatedValue(selectedPatient.getFirstName()));
        tvPatientId.setText(Util.getValidatedValue(selectedPatient.getPid()));
        tvMobileNo.setText(Util.getValidatedValue(selectedPatient.getMobileNumber()));
        tvDate.setText(Util.getValidatedValue(DateTimeUtil.getCurrentDate()));
//        tvRxId.setText(Util.getValidatedValueOrDash(mActivity, selectedPatient.getP()));
        initUiPermissions(user.getUiPermissions());
    }

    private void initVitalSigns(VitalSigns vitalSigns) {
        selectedVitalSigns = vitalSigns;
        TextView tvHeartRate = (TextView) view.findViewById(R.id.tv_heart_rate);
        TextView tvBloodPressure = (TextView) view.findViewById(R.id.tv_blood_pressure);
        TextView tvBodyTemprature = (TextView) view.findViewById(R.id.tv_body_temprature);
        TextView tvWeight = (TextView) view.findViewById(R.id.tv_weight);
        TextView tvRespiratoryRate = (TextView) view.findViewById(R.id.tv_respiratory_rate);
        TextView tvSpO2 = (TextView) view.findViewById(R.id.tv_spo2);

        tvHeartRate.setText(R.string.no_text_dash);
        tvBloodPressure.setText(R.string.no_text_dash);
        tvBodyTemprature.setText(R.string.no_text_dash);
        tvWeight.setText(R.string.no_text_dash);
        tvRespiratoryRate.setText(R.string.no_text_dash);
        tvSpO2.setText(R.string.no_text_dash);

        if (vitalSigns != null) {
            if (!Util.isNullOrBlank(vitalSigns.getPulse()))
                tvHeartRate.setText(vitalSigns.getFormattedPulse(mActivity, vitalSigns.getPulse()));
            if (!Util.isNullOrBlank(vitalSigns.getTemperature()))
                tvBodyTemprature.setText(vitalSigns.getFormattedTemprature(mActivity, vitalSigns.getTemperature()));
            if (!Util.isNullOrBlank(vitalSigns.getWeight()))
                tvWeight.setText(vitalSigns.getFormattedWeight(mActivity, vitalSigns.getWeight()));
            BloodPressure bloodPressure = vitalSigns.getBloodPressure();
            if (bloodPressure != null && !Util.isNullOrBlank(bloodPressure.getSystolic()) && !Util.isNullOrBlank(bloodPressure.getDiastolic())) {
                tvBloodPressure.setText(vitalSigns.getFormattedBloodPressureValue(mActivity, bloodPressure));
            }
            if (!Util.isNullOrBlank(vitalSigns.getBreathing()))
                tvRespiratoryRate.setText(vitalSigns.getFormattedBreathing(mActivity, vitalSigns.getBreathing()));
            if (!Util.isNullOrBlank(vitalSigns.getSpo2()))
                tvSpO2.setText(vitalSigns.getFormattedSpo2(mActivity, vitalSigns.getSpo2()));
        }
    }

    private void initUiPermissions(UIPermissions uiPermissions) {
        if (uiPermissions != null && !Util.isNullOrBlank(uiPermissions.getClinicalNotesPermissionsString())) {
            Gson gson = new Gson();
            clinicalNotesUiPermissionsList = (ArrayList<ClinicalNotesPermissionType>) gson.fromJson(uiPermissions.getClinicalNotesPermissionsString(), new TypeToken<ArrayList<ClinicalNotesPermissionType>>() {
            }.getType());
            if (!Util.isNullOrEmptyList(clinicalNotesUiPermissionsList)) {
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.VITAL_SIGNS)) {
                    parentVitalSigns.setVisibility(View.VISIBLE);
                    if (clinicalNotes != null)
                        initVitalSigns(clinicalNotes.getVitalSigns());
                }
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PRESENT_COMPLAINT))
                    addPermissionItem(ClinicalNotesPermissionType.PRESENT_COMPLAINT);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.COMPLAINT))
                    addPermissionItem(ClinicalNotesPermissionType.COMPLAINT);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT))
                    addPermissionItem(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.MENSTRUAL_HISTORY))
                    addPermissionItem(ClinicalNotesPermissionType.MENSTRUAL_HISTORY);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.OBSTETRIC_HISTORY))
                    addPermissionItem(ClinicalNotesPermissionType.OBSTETRIC_HISTORY);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.GENERAL_EXAMINATION))
                    addPermissionItem(ClinicalNotesPermissionType.GENERAL_EXAMINATION);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION))
                    addPermissionItem(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.OBSERVATION))
                    addPermissionItem(ClinicalNotesPermissionType.OBSERVATION);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.INVESTIGATIONS))
                    addPermissionItem(ClinicalNotesPermissionType.INVESTIGATIONS);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS))
                    addPermissionItem(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.DIAGNOSIS))
                    addPermissionItem(ClinicalNotesPermissionType.DIAGNOSIS);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.ECG))
                    addPermissionItem(ClinicalNotesPermissionType.ECG);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.ECHO))
                    addPermissionItem(ClinicalNotesPermissionType.ECHO);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.XRAY))
                    addPermissionItem(ClinicalNotesPermissionType.XRAY);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.HOLTER))
                    addPermissionItem(ClinicalNotesPermissionType.HOLTER);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.NOTES))
                    addPermissionItem(ClinicalNotesPermissionType.NOTES);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.INDICATION_OF_USG))
                    addPermissionItem(ClinicalNotesPermissionType.INDICATION_OF_USG);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PA))
                    addPermissionItem(ClinicalNotesPermissionType.PA);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PS))
                    addPermissionItem(ClinicalNotesPermissionType.PS);
                if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PV))
                    addPermissionItem(ClinicalNotesPermissionType.PV);
            }
        }
    }

    private void addPermissionItem(ClinicalNotesPermissionType clinicalNotesPermissionType) {
        LinearLayout layoutItempermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_add_clinical_note_permision, null);
        TextView tvTitle = (TextView) layoutItempermission.findViewById(R.id.tv_title);
        AutoCompleteTextView autotvPermission = (AutoCompleteTextView) layoutItempermission.findViewById(R.id.autotv_permission);
        tvTitle.setText(clinicalNotesPermissionType.getTextId());
        autotvPermission.setId(clinicalNotesPermissionType.getAutotvId());
        autotvPermission.setHint(clinicalNotesPermissionType.getHintId());
        autotvPermission.setTag(clinicalNotesPermissionType);
        String text = "";
        if (clinicalNotes != null) {
            switch (clinicalNotesPermissionType) {
                case PRESENT_COMPLAINT:
                    text = clinicalNotes.getPresentComplaint();
                    break;
                case COMPLAINT:
                    text = clinicalNotes.getComplaint();
                    break;
                case HISTORY_OF_PRESENT_COMPLAINT:
                    text = clinicalNotes.getPresentComplaintHistory();
                    break;
                case MENSTRUAL_HISTORY:
                    text = clinicalNotes.getMenstrualHistory();
                    break;
                case OBSTETRIC_HISTORY:
                    text = clinicalNotes.getObstetricHistory();
                    break;
                case GENERAL_EXAMINATION:
                    text = clinicalNotes.getGeneralExam();
                    break;
                case SYSTEMIC_EXAMINATION:
                    text = clinicalNotes.getSystemExam();
                    break;
                case OBSERVATION:
                    text = clinicalNotes.getObservation();
                    break;
                case INVESTIGATIONS:
                    text = clinicalNotes.getInvestigation();
                    break;
                case PROVISIONAL_DIAGNOSIS:
                    text = clinicalNotes.getProvisionalDiagnosis();
                    break;
                case DIAGNOSIS:
                    text = clinicalNotes.getDiagnosis();
                    break;
                case ECG:
                    text = clinicalNotes.getEcgDetails();
                    break;
                case ECHO:
                    text = clinicalNotes.getEcho();
                    break;
                case XRAY:
                    text = clinicalNotes.getxRayDetails();
                    break;
                case HOLTER:
                    text = clinicalNotes.getHolter();
                    break;
                case NOTES:
                    text = clinicalNotes.getNote();
                    break;
                case INDICATION_OF_USG:
                    text = clinicalNotes.getIndicationOfUSG();
                    break;
                case PA:
                    text = clinicalNotes.getPa();
                    break;
                case PS:
                    text = clinicalNotes.getPs();
                    break;
                case PV:
                    text = clinicalNotes.getPv();
                    break;
            }
            autotvPermission.setText(text);
        }
        parentPermissionItems.addView(layoutItempermission);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_close:
                mActivity.onBackPressed();
                break;
            case R.id.bt_clinical_note:
                showHideClinicalNotesLayout();
                break;
            case R.id.bt_prescription:
                break;
            case R.id.bt_lab_tests:
                showHideLabTestsLayout();
                break;
            case R.id.bt_diagrams:
                openActivity(CommonOpenUpFragmentType.SELECT_DIAGRAM, null, REQUEST_CODE_ADD_CLINICAL_NOTES);
                break;
            case R.id.bt_advice:
                showHideAdviceLayout();
                break;
            case R.id.bt_delete:
                View parentView = (View) v.getParent();
                String diagramUniqueId = selectedPatient.getUniqueId();
                if (parentView != null && !Util.isNullOrBlank(diagramUniqueId)) {
                    showConfirmationAlert(null, getResources().getString(R.string.confirm_delete_diagram), parentView, diagramUniqueId);
                }
                break;
        }
    }

    private void showConfirmationAlert(String title, String msg, final View parentView, final String diagramUniqueId) {
        if (Util.isNullOrBlank(title))
            title = mActivity.getResources().getString(R.string.confirm);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                containerDiagrams.removeView(parentView);
                diagramsList.remove(diagramUniqueId);
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

    private void openActivity(CommonOpenUpFragmentType fragmentType, Object intentData, int requestCode) {
        Intent intent = new Intent(mActivity, AddVisitsActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (intentData != null) {
            intent.putExtra(HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA, Parcels.wrap(intentData));
        }
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_CLINICAL_NOTES) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_DIAGRAM_DETAIL && data != null) {
                String diagramUniqueId = data.getStringExtra(HealthCocoConstants.TAG_UNIQUE_ID);
                String diagramTag = data.getStringExtra(SelectedDiagramDetailFragment.SELECTED_DIAGRAM_TAG);
                if (!Util.isNullOrBlank(diagramUniqueId) && ImageUtil.DIAGRAM_SELECTED_BYTE_ARRAY != null && ImageUtil.DIAGRAM_SELECTED_BYTE_ARRAY.length > 0) {
                    addDiagramInContainer(diagramTag, diagramUniqueId, ImageUtil.DIAGRAM_SELECTED_BYTE_ARRAY);
                }
            }
        }
    }

    private void addDiagramInContainer(String diagramTag, String diagramUniqueId, Object object) {
        RelativeLayout frameLayout = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.diagram_add_visits_item, null);
        ImageView ivDiagram = (ImageView) frameLayout.findViewById(R.id.iv_diagram);
        ImageButton btDelete = (ImageButton) frameLayout.findViewById(R.id.bt_delete);
        TextView tvtag = (TextView) frameLayout.findViewById(R.id.tv_tag);
        btDelete.setOnClickListener(this);
        Bitmap bitmap = null;
        if (object instanceof byte[]) {
            byte[] byteArray = (byte[]) object;
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            if (bitmap != null)
                ivDiagram.setImageBitmap(bitmap);
            else
                return;
        } else if (object instanceof String) {
            String imageUrl = (String) object;
            DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivDiagram, imageUrl);
        }
        tvtag.setText(diagramTag);
        frameLayout.setTag(diagramUniqueId);
        diagramsList.put(diagramUniqueId, diagramUniqueId);
        containerDiagrams.addView(frameLayout);
        svScrollView.fullScroll(ScrollView.FOCUS_UP);
        if (parentDiagrams.getVisibility() == View.GONE)
            parentDiagrams.setVisibility(View.VISIBLE);
    }

    private void showHideLabTestsLayout() {
        if (parentDiagnosticTests.getVisibility() == View.GONE) {
            parentDiagnosticTests.setVisibility(View.VISIBLE);
        } else parentDiagnosticTests.setVisibility(View.GONE);
    }

    private void showHideAdviceLayout() {
        if (layoutParentAdvice.getVisibility() == View.GONE) {
            layoutParentAdvice.setVisibility(View.VISIBLE);
        } else layoutParentAdvice.setVisibility(View.GONE);
    }

    private void showHideClinicalNotesLayout() {
        if (parentClinicalNote.getVisibility() == View.GONE) {
            parentClinicalNote.setVisibility(View.VISIBLE);
        } else parentClinicalNote.setVisibility(View.GONE);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()) && selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getUserId())) {
                    user = doctor.getUser();
                    initData();
                }
                break;
        }

        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
        mActivity.hideLoading();
    }
}
