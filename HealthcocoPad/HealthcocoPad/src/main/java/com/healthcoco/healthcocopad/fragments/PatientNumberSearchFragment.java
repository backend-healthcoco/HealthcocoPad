package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.PatientNumberSearchAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.RegisterNewPatientRequest;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.PatientNumberSearchResultsDialogFragment;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.myscript.atk.core.Line;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class PatientNumberSearchFragment extends HealthCocoFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, AdapterView.OnItemClickListener, HealthcocoTextWatcherListener {
    private static final int REQUEST_CODE_PATIENT_NUMBER_SERACH = 101;
    private EditText editMobileNumber;
    private Button btProceed;
    private Button btSkip;
    private boolean isMobileNumberOptional;
    private Button btRegisterNewPatient;
    private ListView lvPatients;
    private LinearLayout layoutRegisterNew;
    private PatientNumberSearchAdapter mAdapter;
    private ArrayList<AlreadyRegisteredPatientsResponse> list = new ArrayList<>();
    private LinearLayout loadingExistingPatientsList;
    private User user;
    private String mobileNumber;
    private PatientRegistrationDetailsListener registrationDetailsListener;
    private boolean isFromRegistration;

    public PatientNumberSearchFragment(PatientRegistrationDetailsListener registrationDetailsListener) {
        super();
        this.registrationDetailsListener = registrationDetailsListener;
        isFromRegistration = registrationDetailsListener.isFromPatientRegistarion();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_number_search, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            DoctorClinicProfile doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
            if (doctorClinicProfile != null && doctorClinicProfile.getMobileNumberOptional() != null)
                isMobileNumberOptional = doctorClinicProfile.getMobileNumberOptional();
//            isMobileNumberOptional = Util.getIsMobileNumberOptional(doctor);
            Intent intent = mActivity.getIntent();
            if (intent != null) {
                mobileNumber = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_MOBILE_NUMBER));
            }
            init();
        }
    }

    @Override
    public void init() {
        initViews();
        initAdapter();
        initListeners();

        if (!Util.isNullOrBlank(mobileNumber)) {
            editMobileNumber.setText(mobileNumber);
        }
    }

    @Override
    public void initViews() {
        editMobileNumber = (EditText) view.findViewById(R.id.edit_mobile_number);
        btProceed = (Button) view.findViewById(R.id.bt_proceed);
        btSkip = (Button) view.findViewById(R.id.bt_skip);
        btRegisterNewPatient = (Button) view.findViewById(R.id.bt_register_new_patient);
        lvPatients = (ListView) view.findViewById(R.id.lv_patients);
        loadingExistingPatientsList = (LinearLayout) view.findViewById(R.id.loading_existing_patients_list);
        layoutRegisterNew = (LinearLayout) view.findViewById(R.id.layout_register_new_patient);
        mActivity.showSoftKeyboard(editMobileNumber);
        Util.setFocusToEditText(mActivity, editMobileNumber);
        layoutRegisterNew.setVisibility(View.GONE);
        if (isMobileNumberOptional) {
            btSkip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initListeners() {
        btProceed.setOnClickListener(this);
        btSkip.setOnClickListener(this);
        btRegisterNewPatient.setOnClickListener(this);
        lvPatients.setOnItemClickListener(this);

        editMobileNumber.addTextChangedListener(new HealthcocoTextWatcher(editMobileNumber, this));

    }

    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_skip:
                openRegistrationFragment(null, null, false);
                break;
            case R.id.bt_register_new_patient:
                if (list.size() < 9) {
                    openRegistrationFragment(String.valueOf(editMobileNumber.getText()), null, false);
                } else
                    Util.showAlert(mActivity, R.string.alert_nine_patients_already_registered);
                break;
        }
    }

    public void validateData() {
        String mobileNo = (String.valueOf(editMobileNumber.getText()));
        String msg = null;
        if (Util.isNullOrBlank(mobileNo)) {
//            if (isMobileNumberOptional)
//                openRegistrationFragment(null);
            msg = getResources().getString(R.string.please_enter_mobile_no);
        } else if (!Util.isValidMobileNo(mobileNo))
            msg = getResources().getString(R.string.please_enter_valid_mobile_no);

        if (Util.isNullOrBlank(msg)) {
            searchPatients(mobileNo);
        } else {
//            if (!isMobileNumberOptional)
            Util.showAlert(mActivity, msg);
        }
    }

    private void searchPatients(String mobileNo) {
        loadingExistingPatientsList.setVisibility(View.VISIBLE);
        WebDataServiceImpl.getInstance(mApp).getAlreadyRegisteredPatients(AlreadyRegisteredPatientsResponse.class, mobileNo, user, this, this);
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
        mActivity.hideLoading();
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case SEARCH_PATIENTS:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {

                        list = (ArrayList<AlreadyRegisteredPatientsResponse>) (ArrayList<?>) response
                                .getDataList();
                        LocalDataServiceImpl.getInstance(mApp).addAlreadyRegisteredPatients(list);
                        notifyAndRefreshPatientList(list);
                        if (!Util.isNullOrBlank(mobileNumber)) {
                            openRegistrationFragment(mobileNumber, null, false);
                        }
                    } else {
                        if (isFromRegistration)
                            openRegistrationFragment(String.valueOf(editMobileNumber.getText()), null, false);
                        else
                            layoutRegisterNew.setVisibility(View.VISIBLE);
                    }
                    layoutRegisterNew.setVisibility(View.VISIBLE);
                    break;
            }
        }
        loadingExistingPatientsList.setVisibility(View.GONE);
    }

    private void notifyAndRefreshPatientList(ArrayList<AlreadyRegisteredPatientsResponse> list) {
        if (!Util.isNullOrEmptyList(list))
            mAdapter.setListData(list);
        mAdapter.notifyDataSetChanged();
    }

    private void openRegistrationFragment(String mobileNo, String patientUniqueId, boolean isEditPatient) {
        RegisterNewPatientRequest patientDetails = new RegisterNewPatientRequest();
//        String mobileNo = String.valueOf(editMobileNumber.getText());
        if (!Util.isNullOrBlank(mobileNo))
            if (Util.isValidMobileNo(mobileNo))
                patientDetails.setMobileNumber(mobileNo);
        if (!Util.isNullOrBlank(patientUniqueId))
            patientDetails.setUserId(patientUniqueId);
        registrationDetailsListener.readyToMoveNext(patientDetails, isEditPatient);
    }


    private void initAdapter() {
        mAdapter = new PatientNumberSearchAdapter(mActivity);
        lvPatients.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlreadyRegisteredPatientsResponse alreadyRegisteredPatient = list.get(position);
        if (alreadyRegisteredPatient.getIsPartOfClinic() != null && alreadyRegisteredPatient.getIsPartOfClinic()) {
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = alreadyRegisteredPatient.getUserId();
            Util.sendBroadcast(mApp, BookAppointmentDialogFragment.INTENT_REFRESH_SELECTED_PATIENT);
            Util.sendBroadcast(mApp, ContactsListFragment.INTENT_FINISH_CONTACTS_LIST_SCREEN);
//            mActivity.finish();
            openRegistrationFragment(null, alreadyRegisteredPatient.getUserId(), true);
        } else {
            boolean isEdit = false;
            if (alreadyRegisteredPatient.getIsPartOfClinic() != null && alreadyRegisteredPatient.getIsPartOfClinic()) {
                if (!Util.isNullOrEmptyList(user.getRoleTypes()))
                    if ((user.getRoleTypes().contains(RoleType.CONSULTANT_DOCTOR) && alreadyRegisteredPatient.isPartOfConsultantDoctor())
                            || !user.getRoleTypes().contains(RoleType.CONSULTANT_DOCTOR)) {
                        openPatientDetailScreen(alreadyRegisteredPatient);
                        return;
                    } else
                        isEdit = true;
            }
            openRegistrationFragment(null, alreadyRegisteredPatient.getUserId(), true);
        }
    }

    private void openPatientDetailScreen(AlreadyRegisteredPatientsResponse alreadyRegisteredPatient) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = alreadyRegisteredPatient.getUserId();
            mActivity.openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_DETAIL, null,
                    REQUEST_CODE_PATIENT_NUMBER_SERACH);
            mActivity.finish();
        } else {
            Util.showToast(mActivity, R.string.user_offline);
        }
    }

    @Override
    public void afterTextChange(View v, String s) {
        switch (v.getId()) {
            case R.id.edit_mobile_number:
                LogUtils.LOGD(TAG, "Edit Mobile Number");
                if (Util.isValidMobileNo(s)) {
                    mActivity.hideSoftKeyboard();
                    validateData();
                } else layoutRegisterNew.setVisibility(View.GONE);

                break;
        }
    }
}
