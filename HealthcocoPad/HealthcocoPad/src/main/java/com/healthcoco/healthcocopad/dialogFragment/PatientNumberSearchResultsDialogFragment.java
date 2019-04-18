package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.PatientNumberSearchAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class PatientNumberSearchResultsDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    public static final String TAG_IS_FROM_HOME_ACTIVITY = "isFromHomeActivity";
    private static final int REQUEST_CODE_PATIENT_SEARCH = 101;
    private Button btRegisterNewPatient;
    private ListView lvPatients;
    private PatientNumberSearchAdapter mAdapter;
    private List<AlreadyRegisteredPatientsResponse> list;
    private User user;
    private String mobileNumber;
    private boolean isFromHomeActivity;
    private boolean isForMobileNoEdit;
    private RegisteredPatientDetailsUpdated selectedPatient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_patient_number_search_results, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (bundle != null) {
            isForMobileNoEdit = bundle.getBoolean(ContactsListFragment.TAG_IS_FOR_MOBILE_NUMBER_EDIT, false);
            selectedPatient = Parcels.unwrap(bundle.getParcelable(ContactsListFragment.TAG_PATIENT_DATA));
            mobileNumber = bundle.getString(HealthCocoConstants.TAG_MOBILE_NUMBER);
            isFromHomeActivity = bundle.getBoolean(TAG_IS_FROM_HOME_ACTIVITY, true);
        }
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            list = LocalDataServiceImpl.getInstance(mApp).getAlreadyRegisteredPatientsList(WebServiceType.SEARCH_PATIENTS, null, null);
            init();
            setWidthHeight(0.50, 0.70);
        }
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        if (!Util.isNullOrEmptyList(list)) {
            initAdapter();
        } else
            openRegistrationFragment(false, "");
    }

    @Override
    public void initViews() {
        btRegisterNewPatient = (Button) view.findViewById(R.id.bt_register_new_patient);
        lvPatients = (ListView) view.findViewById(R.id.lv_patients);
    }

    @Override
    public void initListeners() {
        btRegisterNewPatient.setOnClickListener(this);
        lvPatients.setOnItemClickListener(this);
        initCrossButton();
        initActionbarTitle(getResources().getString(R.string.searched_patients));
    }

    @Override
    public void initData() {

    }

    private void initAdapter() {
        mAdapter = new PatientNumberSearchAdapter(mActivity);
        mAdapter.setListData(list);
        lvPatients.setAdapter(mAdapter);
    }

    public void openRegistrationFragment(boolean isEdit, String patientUniqueId) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_REGISTRATION.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_UNIQUE_ID, patientUniqueId);
        intent.putExtra(HealthCocoConstants.TAG_MOBILE_NUMBER, mobileNumber);
        intent.putExtra(HealthCocoConstants.TAG_IS_EDIT_PATIENT, isEdit);
        startActivityForResult(intent, REQUEST_CODE_PATIENT_SEARCH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register_new_patient:
                if (list.size() < 9) {
                    if (!isFromHomeActivity)
                        openAddNewPatientDialog(AddUpdateNameDialogType.ADD_NEW_PATIENT_NAME, this, "", 0);
                    else if (isForMobileNoEdit) {
                        mActivity.showLoading(false);
                        WebDataServiceImpl.getInstance(mApp).updatePatientMobileNumber(Boolean.class, WebServiceType.UPDATE_PATINET_MOBILE_NUMBER,
                                user, selectedPatient.getUserId(), null, mobileNumber, this, this);
                    } else
                        openRegistrationFragment(false, "");
                } else
                    Util.showAlert(mActivity, R.string.alert_nine_patients_already_registered);
                break;
        }

    }

    protected void openAddNewPatientDialog(AddUpdateNameDialogType dialogType,
                                           Fragment fragment, String uniqueId, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, uniqueId);
        bundle.putString(HealthCocoConstants.TAG_MOBILE_NUMBER, mobileNumber);
        bundle.putInt(HealthCocoConstants.TAG_ORDINAL_DIALOG_TYPE, dialogType.ordinal());
        AddUpdateNameDialogFragment addUpdateNameDialogFragment = new AddUpdateNameDialogFragment();
        addUpdateNameDialogFragment.setArguments(bundle);
        addUpdateNameDialogFragment.setTargetFragment(fragment, requestCode);
        addUpdateNameDialogFragment.show(mActivity.getSupportFragmentManager(),
                addUpdateNameDialogFragment.getClass().getSimpleName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PATIENT_SEARCH && resultCode == HealthCocoConstants.RESULT_CODE_REGISTRATION) {
            closeThisActivity();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlreadyRegisteredPatientsResponse alreadyRegisteredPatient = list.get(position);
        if (!isFromHomeActivity) {
            if (alreadyRegisteredPatient.getIsPartOfClinic() != null && alreadyRegisteredPatient.getIsPartOfClinic()) {
                HealthCocoConstants.SELECTED_PATIENTS_USER_ID = alreadyRegisteredPatient.getUserId();
                Util.sendBroadcast(mApp, BookAppointmentDialogFragment.INTENT_REFRESH_SELECTED_PATIENT);
                Util.sendBroadcast(mApp, ContactsListFragment.INTENT_FINISH_CONTACTS_LIST_SCREEN);
            } else {
                openAddNewPatientDialog(AddUpdateNameDialogType.ADD_NEW_PATIENT_NAME, this, "", 0);
            }
            mActivity.finish();
        } else if (isForMobileNoEdit) {
            updatePatientsMobileNumber(alreadyRegisteredPatient);
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
            openRegistrationFragment(isEdit, alreadyRegisteredPatient.getUserId());
        }
    }

    private void updatePatientsMobileNumber(AlreadyRegisteredPatientsResponse alreadyRegisteredPatient) {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).updatePatientMobileNumber(Boolean.class, WebServiceType.UPDATE_PATINET_MOBILE_NUMBER,
                user, selectedPatient.getUserId(), alreadyRegisteredPatient.getUserId(), null, this, this);
    }

    private void openPatientDetailScreen(AlreadyRegisteredPatientsResponse alreadyRegisteredPatient) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = alreadyRegisteredPatient.getUserId();
            mActivity.openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_DETAIL, null,
                    REQUEST_CODE_PATIENT_SEARCH);
            closeThisActivity();
        } else {
            Util.showToast(mActivity, R.string.user_offline);
        }
    }


    private void closeThisActivity() {
        getDialog().dismiss();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case UPDATE_PATINET_MOBILE_NUMBER:
                if (response.getData() instanceof Boolean) {
                    boolean isDataSuccess = (boolean) response.getData();
                    if (isDataSuccess) {
                        LocalDataServiceImpl.getInstance(mApp).deletePatient(selectedPatient.getUserId());
                        Util.sendBroadcast(mApp, ContactsListFragment.INTENT_REFRESH_CONTACTS_LIST_FROM_SERVER);
                        closeThisActivity();
                        Util.showToast(mActivity, R.string.mobile_number_updated);
                    } else Util.showToast(mActivity, R.string.mobile_number_not_updated);
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
    }
}
