package com.healthcoco.healthcocoplus.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoDialogFragment;
import com.healthcoco.healthcocoplus.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class PatientNumberSearchDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private EditText editMobileNumber;
    private Button btProceed;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_patient_number_search, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            init();
            setWidthHeight(0.50, 0.55);
        }
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        editMobileNumber = (EditText) view.findViewById(R.id.edit_mobile_number);
        btProceed = (Button) view.findViewById(R.id.bt_proceed);
        mActivity.showSoftKeyboard(editMobileNumber);
        Util.setFocusToEditText(mActivity, editMobileNumber);
    }

    @Override
    public void initListeners() {
        btProceed.setOnClickListener(this);
        initCrossButton();
        initActionbarTitle(getResources().getString(R.string.patient_search));
    }

    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_proceed:
                validateData();
                break;
        }
    }

    private void validateData() {
        String mobileNo = (String.valueOf(editMobileNumber.getText()));
        String msg = null;
        if (Util.isNullOrBlank(mobileNo))
            msg = getResources().getString(R.string.please_enter_mobile_no);
        else if (!Util.isValidMobileNo(mobileNo))
            msg = getResources().getString(R.string.please_enter_valid_mobile_no);

        if (Util.isNullOrBlank(msg)) {
            searchPatients(mobileNo);
        } else
            Util.showAlert(mActivity, msg);
    }

    private void searchPatients(String mobileNo) {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getAlreadyRegisteredPatients(AlreadyRegisteredPatientsResponse.class, mobileNo, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
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
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case SEARCH_PATIENTS:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        ArrayList<AlreadyRegisteredPatientsResponse> list = (ArrayList<AlreadyRegisteredPatientsResponse>) (ArrayList<?>) response
                                .getDataList();
                        LocalDataServiceImpl.getInstance(mApp).addAlreadyRegisteredPatients(list);
                        openPatientNumberSearchResultsFragment();
                    } else {
                        openRegistrationFragment();
                    }
                    break;
            }
        }
        mActivity.hideLoading();
        getDialog().dismiss();
    }

    private void openPatientNumberSearchResultsFragment() {
        Bundle args = new Bundle();
        PatientNumberSearchResultsDialogFragment patientNumberSearchResultsDialogFragment = new PatientNumberSearchResultsDialogFragment();
        args.putString(HealthCocoConstants.TAG_MOBILE_NUMBER, String.valueOf(editMobileNumber.getText()));
        args.putBoolean(PatientNumberSearchResultsDialogFragment.TAG_IS_FROM_HOME_ACTIVITY, true);
        patientNumberSearchResultsDialogFragment.setArguments(args);
        patientNumberSearchResultsDialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST);
        patientNumberSearchResultsDialogFragment.show(mFragmentManager, patientNumberSearchResultsDialogFragment.getClass().getSimpleName());
    }

    public void openRegistrationFragment() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_REGISTRATION.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_MOBILE_NUMBER, String.valueOf(editMobileNumber.getText()));
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST
                && resultCode == HealthCocoConstants.RESULT_CODE_REGISTRATION) {
            getTargetFragment().onActivityResult(requestCode, resultCode, data);
            dismiss();
        }
    }
}
