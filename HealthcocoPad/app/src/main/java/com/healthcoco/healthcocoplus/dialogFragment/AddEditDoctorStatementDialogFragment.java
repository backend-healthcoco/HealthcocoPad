package com.healthcoco.healthcocoplus.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoDialogFragment;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.request.ProfessionalStatementRequest;
import com.healthcoco.healthcocoplus.bean.server.DoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.fragments.MyProfileFragment;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 18-02-2017.
 */
public class AddEditDoctorStatementDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    private User user;
    private EditText etProfessionalStatement;
    private String professionalStatement;
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
        setWidthHeight(0.50, 0.75);
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileObject(user.getUniqueId());
            initViews();
            initListeners();
            initData();
        }
    }

    @Override
    public void initData() {
        String professionalStatement = doctorProfile.getProfessionalStatement();
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
        switch (v.getId()) {
            case R.id.bt_save:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    validateData();
                else
                    onNetworkUnavailable(null);
                break;
        }
    }

    private void addEditProfessionalStatementDetails(String professionalStatement) {
        LogUtils.LOGD(TAG, "Professioanal Statement " + this.professionalStatement);
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
}
