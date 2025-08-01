package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.ProfessionalStatementRequest;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.MyProfileFragment;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 18-02-2017.
 */
public class AddEditDoctorStatementDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised {
    public static final String TAG_DOCTOR_STATEMENT_DETAIL = "doctorsStatementDetail";
    private User user;
    private EditText etProfessionalStatement;
    private DoctorProfile doctorProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_professional_statement_detail, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setWidthHeight(0.50, 0.80);
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
    }

    @Override
    public void initData() {
        String professionalStatement = Parcels.unwrap(getArguments().getParcelable(TAG_DOCTOR_STATEMENT_DETAIL));
        etProfessionalStatement.setText(Util.getValidatedValue(professionalStatement));
    }

    @Override
    public void initViews() {
        etProfessionalStatement = (EditText) view.findViewById(R.id.et_professional_statement);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.professional_statement));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_save) {
            Util.checkNetworkStatus(mActivity);
            if (HealthCocoConstants.isNetworkOnline) {
                validateData();
            } else {
                onNetworkUnavailable(null);
            }
        }

    }

    private void addEditProfessionalStatementDetails(String professionalStatement) {
        LogUtils.LOGD(TAG, "Professioanal Statement " + professionalStatement);
        mActivity.showLoading(false);
        ProfessionalStatementRequest statementRequest = new ProfessionalStatementRequest();
        statementRequest.setDoctorId(user.getUniqueId());
        statementRequest.setProfessionalStatement(professionalStatement);
        WebDataServiceImpl.getInstance(mApp).addUpdateProfessionalStatement(ProfessionalStatementRequest.class, statementRequest, this, this);
    }

    private void validateData() {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String professionalStatement = Util.getValidatedValueOrNull(etProfessionalStatement);
        if (Util.isNullOrBlank(professionalStatement)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(etProfessionalStatement);
        }
        if (Util.isNullOrBlank(msg)) {
            addEditProfessionalStatementDetails(professionalStatement);
        }
        EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, errorViewList, msg);
    }

    private void clearPreviousAlerts() {
        etProfessionalStatement.setActivated(false);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case ADD_UPDATE_PROFESSIONAL_STATEMENT_DETAIL:
                if (response.getData() != null && response.getData() instanceof ProfessionalStatementRequest) {
                    ProfessionalStatementRequest statementRequest = (ProfessionalStatementRequest) response.getData();
                    doctorProfile = new DoctorProfile();
                    doctorProfile.setProfessionalStatement(statementRequest.getProfessionalStatement());
                    LocalDataServiceImpl.getInstance(mApp).addDoctorProfile(doctorProfile);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_DOCTOR_PROFESSIONAL_STATEMENT_DETAIL, new Intent().putExtra(MyProfileFragment.TAG_DOCTOR_PROFILE, Parcels.wrap(doctorProfile)));
                    getDialog().dismiss();
                }
                break;
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
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null)
                    user = doctor.getUser();
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }
}
