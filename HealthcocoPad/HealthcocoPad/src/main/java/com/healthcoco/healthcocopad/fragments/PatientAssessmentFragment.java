package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.AssessmentTypeListAdapter;
import com.healthcoco.healthcocopad.bean.PatientAssessment;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AssessmentPersonalDetail;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AssessmentFormType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Prashant on 20-09-2018.
 */

public class PatientAssessmentFragment extends HealthCocoFragment implements View.OnClickListener,
        LocalDoInBackgroundListenerOptimised, AdapterView.OnItemClickListener {

    private GridView gvAssessment;
    private User user;
    //    private ArrayList<PatientAssessment> patientAssessmentList;
    private List<AssessmentFormType> typeArrayList;
    private ProgressBar progressLoading;
    private AssessmentTypeListAdapter adapter;
    private String assessmentId;
    private String patientId;
    private AssessmentPersonalDetail assessmentPersonalDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_assessment, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = mActivity.getIntent();
        if (intent != null) {
            assessmentId = intent.getStringExtra(HealthCocoConstants.TAG_ASSESSMENT_ID);
            patientId = intent.getStringExtra(HealthCocoConstants.TAG_PATIENT_ID);
            if (patientId != null)
                init();
        }

        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapters();
        initData();
    }

    @Override
    public void initViews() {
        gvAssessment = (GridView) view.findViewById(R.id.gv_assessment_details);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
    }

    @Override
    public void initListeners() {
        gvAssessment.setOnItemClickListener(this);
    }

    private void initAdapters() {
        adapter = new AssessmentTypeListAdapter(mActivity);
        gvAssessment.setAdapter(adapter);
    }

    private void initData() {
        typeArrayList = (List<AssessmentFormType>) Arrays.asList(AssessmentFormType.values());
        notifyAdapter(typeArrayList);
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

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId())) {

                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        Util.showToast(mActivity, errorMsg);
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        mActivity.hideLoading();
        Util.showToast(mActivity, R.string.user_offline);
    }


    private void notifyAdapter(List<AssessmentFormType> list) {
        if (!Util.isNullOrEmptyList(list)) {
            gvAssessment.setVisibility(View.VISIBLE);
        } else {
            gvAssessment.setVisibility(View.GONE);
        }
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Object object = parent.getAdapter().getItem(position);
            if (object instanceof AssessmentFormType) {
                AssessmentFormType formType = (AssessmentFormType) object;
                openAssessmentForm(formType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAssessmentForm(AssessmentFormType formType) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, formType.getOpenUpFragmentType().ordinal());
        intent.putExtra(HealthCocoConstants.TAG_ASSESSMENT_ID, assessmentId);
        intent.putExtra(HealthCocoConstants.TAG_PATIENT_ID, patientId);
//        if (!Util.isNullOrBlank(tag) && intentData != null)
//            intent.putExtra(tag, Parcels.wrap(intentData));
//        if (requestCode == 0)
        startActivity(intent);
//        else
//            startActivityForResult(intent, requestCode);
    }

    public void initDataFromPreviousFragment(Object object, boolean isEditPatient) {
        init();
        if (object instanceof AssessmentPersonalDetail) {
            AssessmentPersonalDetail personalDetail = (AssessmentPersonalDetail) object;
            if (personalDetail != null) {
                assessmentId = personalDetail.getUniqueId();
                patientId = personalDetail.getPatientId();
            }
        }
    }
}
