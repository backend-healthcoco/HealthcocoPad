package com.healthcoco.healthcocopad.dialogFragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 18-02-2017.
 */
public class AddEditDoctorContactDialogFragment extends HealthCocoDialogFragment implements GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, View.OnClickListener {
    private EditText editPrimaryMobileNumber;
    private EditText editAlternateMobileNumber;
    private EditText editAlternateEmailId;
    private User user;
    private DoctorProfile doctorProfile;
    private TextView tvTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_doctor_contact_details, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setWidthHeight(0.50, 0.80);
        init();
    }

    @Override
    public void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mActivity.showLoading(false);
        }
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
            getDataFromLocal();
        }
    }

    private void getDataFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DOCTOR_PROFILE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        editPrimaryMobileNumber = (EditText) view.findViewById(R.id.edit_primary_mobile_number);
        editAlternateMobileNumber = (EditText) view.findViewById(R.id.edit_alternate_mobile_number);
        editAlternateEmailId = (EditText) view.findViewById(R.id.edit_alternate_email_id);
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.contact_details));
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case GET_DOCTOR_PROFILE:
                    doctorProfile = (DoctorProfile) response.getData();
                    if (doctorProfile != null)
                        LogUtils.LOGD(TAG, "Success onResponse doctorProfile " + doctorProfile.getFirstName() + " isDataFromLocal " + response.isDataFromLocal());
                    initData();
                    break;
                case ADD_UPDATE_DOCTOR_CONTACT:
                    if (response.getData() != null && response.getData() instanceof DoctorProfile) {
                        DoctorProfile doctorProfileResponse = (DoctorProfile) response.getData();
                        doctorProfile.setMobileNumber(doctorProfileResponse.getMobileNumber());
                        doctorProfile.setAdditionalNumbers(doctorProfileResponse.getAdditionalNumbers());
                        doctorProfile.setOtherEmailAddresses(doctorProfileResponse.getOtherEmailAddresses());
                        LocalDataServiceImpl.getInstance(mApp).addDoctorProfile(doctorProfile);
                    }
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                    getDialog().dismiss();
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void initData() {
        editPrimaryMobileNumber.setText(Util.getValidatedValue(doctorProfile.getMobileNumber()));
        if (!Util.isNullOrEmptyList(doctorProfile.getAdditionalNumbers()))
            editAlternateMobileNumber.setText(Util.getValidatedValue(doctorProfile.getAdditionalNumbers().get(0)));
        if (!Util.isNullOrEmptyList(doctorProfile.getOtherEmailAddresses()))
            editAlternateEmailId.setText(Util.getValidatedValue(doctorProfile.getOtherEmailAddresses().get(0)));

    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
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
            case GET_DOCTOR_PROFILE:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileResponse(WebServiceType.GET_DOCTOR_PROFILE, user.getUniqueId(), null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String mobileNo = Util.getValidatedValueOrNull(editPrimaryMobileNumber);
        String alternateMobileNo = Util.getValidatedValueOrNull(editAlternateMobileNumber);
        String email = Util.getValidatedValueOrNull(editAlternateEmailId);
        if (Util.isNullOrBlank(mobileNo)) {
            errorViewList.add(editPrimaryMobileNumber);
            msg = getResources().getString(R.string.please_enter_mobile_no);
        } else if (!Util.isValidMobileNo(mobileNo)) {
            errorViewList.add(editPrimaryMobileNumber);
            msg = getResources().getString(R.string.please_enter_valid_mobile_no);
        } else if (!Util.isNullOrBlank(alternateMobileNo) && !Util.isValidMobileNo(alternateMobileNo)) {
            errorViewList.add(editAlternateMobileNumber);
            msg = getResources().getString(R.string.please_enter_valid_alternate_mobile_no);
        } else if (!Util.isNullOrBlank(email) && !Util.isValidEmail(email)) {
            errorViewList.add(editAlternateEmailId);
            msg = getResources().getString(R.string.please_enter_valid_email_address);
        }
        if (Util.isNullOrBlank(msg)) {
            addUpdateDoctorContact(alternateMobileNo, email);
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addUpdateDoctorContact(String mobileNumber, String email) {
        mActivity.showLoading(false);
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setMobileNumber(Util.getValidatedValueOrNull(editPrimaryMobileNumber));
        doctorProfile.setDoctorId(user.getUniqueId());
        doctorProfile.setAdditionalNumbers(getAdditionalMobileNumbers(mobileNumber));
        doctorProfile.setOtherEmailAddresses(getOtherEmailAddresses(email));
        WebDataServiceImpl.getInstance(mApp).addUpdateCommonMethod(WebServiceType.ADD_UPDATE_DOCTOR_CONTACT, DoctorProfile.class, doctorProfile, this, this);
    }

    private List<String> getAdditionalMobileNumbers(String mobileNumber) {
        if (!Util.isNullOrBlank(mobileNumber)) {
            List<String> list = new ArrayList<>();
            list.add(mobileNumber);
            return list;
        }
        return null;
    }

    private List<String> getOtherEmailAddresses(String email) {
        if (!Util.isNullOrBlank(email)) {
            List<String> list = new ArrayList<>();
            list.add(email);
            return list;
        }
        return null;
    }
}
