package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

/**
 * Created by Shreshtha on 05-04-2017.
 */
public class AddVisitsFragment extends HealthCocoFragment implements View.OnClickListener, LocalDoInBackgroundListenerOptimised {
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
        parentClinicalNote.setVisibility(View.GONE);
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
                break;
            case R.id.bt_advice:
                showHideAdviceLayout();
                break;
        }
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
