package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.FilterFragment;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by neha on 05/12/15.
 */
public class AddUpdateNameDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, TextWatcher, LocalDoInBackgroundListenerOptimised {
    public static final String TAG_DOCTOR_ID = "doctorId";
    public static final String TAG_LOCATION_ID = "locationId";
    public static final String TAG_HOSPITAL_ID = "hospitalId";
    private AutoCompleteTextView editName;
    private AddUpdateNameDialogType addUpdateDialogType;
    private String uniqueId;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private Bundle bundle;
    private WebServiceType webserviceType;
    private String selectedLocationId;
    private String selectedHospitalId;
    private String doctorId;
    private User user;

    public AddUpdateNameDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_update_name, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get bundle data
        bundle = getArguments();
        int ordinalDialogType = bundle.getInt(HealthCocoConstants.TAG_ORDINAL_DIALOG_TYPE);
        addUpdateDialogType = AddUpdateNameDialogType.values()[ordinalDialogType];
        int ordinalWebserviceType = bundle.getInt(HealthCocoConstants.TAG_ORDINAL_WEB_SERVICE_TYPE);
        webserviceType = WebServiceType.values()[ordinalWebserviceType];
        uniqueId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
        doctorId = bundle.getString(TAG_DOCTOR_ID);

        selectedLocationId = bundle.getString(TAG_LOCATION_ID);
        selectedHospitalId = bundle.getString(TAG_HOSPITAL_ID);

        if (addUpdateDialogType != null) {
            init();
            mActivity.showLoading(false);
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        editName = (AutoCompleteTextView) view.findViewById(R.id.edit_name);
        initActionbarTitle(getResources().getString(addUpdateDialogType.getTitleId()));
        editName.setHint(addUpdateDialogType.getHintId());
        try {
            switch (addUpdateDialogType) {
                case EMAIL:
                    break;
                case ADD_PATIENT_MOBILE_NUMBER:
                    int maxLength = 10;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    editName.setFilters(fArray);
                    editName.setInputType(InputType.TYPE_CLASS_NUMBER);
                case ADD_NEW_PATIENT_NAME:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListeners() {
//        btSave.setOnClickListener(this);
//        btCancel.setOnClickListener(this);
        editName.addTextChangedListener(this);
        initSaveCancelButton(this);
    }

    @Override
    public void initData() {
        String text = "";
        switch (addUpdateDialogType) {
            case GROUPS:
                if (!Util.isNullOrBlank(uniqueId)) {
                    UserGroups group = LocalDataServiceImpl.getInstance(mApp).getUserGroup(uniqueId);
                    if (group != null)
                        text = Util.getValidatedValue(group.getName());
                }
                break;
            case EMAIL:
                try {
                    if (selectedPatient != null && selectedPatient.getPatient() != null && !Util.isNullOrBlank(selectedPatient.getPatient().getEmailAddress()))
                        text = selectedPatient.getPatient().getEmailAddress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REFERENCE:
                if (!Util.isNullOrBlank(uniqueId)) {
//                    Reference reference = LocalDataServiceImpl.getInstance(mApp).getReference(uniqueId);
//                    if (reference != null)
//                        text = Util.getValidatedValue(reference.getReference());
                }
                break;
        }
        if (!Util.isNullOrBlank(text)) {
            enableSaveButton(true);
            editName.setText(text);
        } else
            enableSaveButton(false);
        editName.setSelection(editName.getText().length());
        Util.setFocusToEditText(mActivity, editName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                hideKeyboard(view);
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    validateData();
                else
                    Util.showToast(mActivity, getResources().getString(R.string.user_offline));
                break;
        }
    }

    private void validateData() {
        String msg = null;
        String name = String.valueOf(editName.getText());
        if (Util.isNullOrBlank(name))
            msg = getResources().getString(addUpdateDialogType.getAlertId());
        else if (addUpdateDialogType == AddUpdateNameDialogType.EMAIL && !Util.isValidEmail(name))
            msg = getResources().getString(R.string.please_enter_valid_email_address);
        if (addUpdateDialogType == AddUpdateNameDialogType.ADD_PATIENT_MOBILE_NUMBER && !Util.isValidMobileNo(name))
            msg = getResources().getString(R.string.please_enter_valid_mobile_no);
        if (Util.isNullOrBlank(msg)) {
            addData(name);
        } else
            Util.showToast(mActivity, msg);
    }

    private void addData(String name) {
        mActivity.showLoading(false);
        switch (addUpdateDialogType) {
            case GROUPS:
                addNewGroup(name);
                break;
            case EMAIL:
                sendEmail(name);
                break;
            case REFERENCE:
                addreference(name);
                break;
            case HISTORY:
                addHistory(name);
                break;
            case DIRECTION:
                addDirection(name);
                break;
            case FREQUENCY:
                addDrugDosage(name);
                break;
            case DISEASE:
                addDisease(name);
                break;
            case LOCAL_STRING_SAVE:
                addNote(name);
                break;
            case ADD_DIAGNOSTIC_TEST:
                addDiagnosticTest(name);
                break;
            case ADD_PATIENT_MOBILE_NUMBER:
                searchPatients(name);
                break;
            case ADD_NEW_PATIENT_NAME:
                registerPatient(name);
                break;
        }
    }

    private void registerPatient(String name) {
        mActivity.showLoading(false);
        String mobileNumber = bundle.getString(HealthCocoConstants.TAG_MOBILE_NUMBER);
//        AlreadyRegisteredPatientsResponse alreadyRegisteredPatient = LocalDataServiceImpl.getInstance(mApp).getALreadyRegisteredPatient(uniqueId);
//        RegisterNewPatientRequest patientDetails = new RegisterNewPatientRequest();
//        patientDetails.setLocalPatientName(name);
//        patientDetails.setMobileNumber(mobileNumber);
//        patientDetails.setDoctorId(doctorId);
//        patientDetails.setLocationId(selectedLocationId);
//        patientDetails.setHospitalId(selectedHospitalId);
//        if (alreadyRegisteredPatient != null)
//            patientDetails.setUserId(alreadyRegisteredPatient.getUserId());
//        WebDataServiceImpl.getInstance(mApp).registerNewPatient(RegisteredPatientDetailsUpdated.class, patientDetails, this, this);
    }

    private void searchPatients(String mobileNo) {
//        mActivity.showLoading(false);
//        WebDataServiceImpl.getInstance(mApp).getAlreadyRegisteredPatients(AlreadyRegisteredPatientsResponse.class, mobileNo, doctorId, selectedLocationId, selectedHospitalId, this, this);
    }

    private void addDiagnosticTest(String name) {
        DiagnosticTest diagnosticTest = new DiagnosticTest();
        diagnosticTest.setTestName(name);
        diagnosticTest.setLocationId(selectedPatient.getLocationId());
        diagnosticTest.setHospitalId(selectedPatient.getHospitalId());
        WebDataServiceImpl.getInstance(mApp).addUpdateCommonMethod(WebServiceType.ADD_DIAGNOSTIC_TESTS, DiagnosticTest.class, diagnosticTest, this, this);

    }

    private void addNote(String name) {
        getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_ADD_STRING, new Intent().putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(name)));
        mActivity.hideLoading();
        dismiss();
    }

    private void addNewGroup(String groupName) {
        UserGroups groupToSend = new UserGroups();
        groupToSend.setName(groupName);
        groupToSend.setDoctorId(doctorId);
        groupToSend.setHospitalId(selectedHospitalId);
        groupToSend.setLocationId(selectedLocationId);
        if (!Util.isNullOrBlank(uniqueId)) {
            groupToSend.setUniqueId(uniqueId);
            WebDataServiceImpl.getInstance(mApp).addUpdateDeleteGroup(WebServiceType.UPDATE_GROUP, UserGroups.class, groupToSend, this, this);
        } else
            WebDataServiceImpl.getInstance(mApp).addUpdateDeleteGroup(WebServiceType.ADD_NEW_GROUP, UserGroups.class, groupToSend, this, this);
    }


    private void sendEmail(String emailId) {
        if (webserviceType != null)
            WebDataServiceImpl.getInstance(mApp).sendEmail(webserviceType, uniqueId, doctorId, selectedLocationId, selectedHospitalId, emailId, this, this);
    }

    private void addreference(String referenceName) {
        Reference body = new Reference();
        body.setReference(referenceName);
        body.setDoctorId(doctorId);
        body.setHospitalId(selectedHospitalId);
        body.setLocationId(selectedLocationId);
        if (!Util.isNullOrBlank(uniqueId)) {
            body.setUniqueId(uniqueId);
            WebDataServiceImpl.getInstance(mApp).addUpdateReference(WebServiceType.UPDATE_REFERENCE, Reference.class, body, this, this);
        } else
            WebDataServiceImpl.getInstance(mApp).addUpdateReference(WebServiceType.ADD_REFERENCE, Reference.class, body, this, this);
    }

    private void addDisease(String diseaseName) {
        ArrayList<Disease> diseasesList = new ArrayList<Disease>();
        Disease disease = new Disease();
        disease.setDisease(diseaseName);
        disease.setDoctorId(doctorId);
        disease.setHospitalId(selectedHospitalId);
        disease.setLocationId(selectedLocationId);
        diseasesList.add(disease);
        WebDataServiceImpl.getInstance(mApp).addDisease(Disease.class, diseasesList, this, this);
    }

    private void addHistory(String name) {
        ArrayList<Disease> list = new ArrayList<>();
        Disease disease = new Disease();
        disease.setDisease(name);
        disease.setDoctorId(doctorId);
        disease.setHospitalId(selectedHospitalId);
        disease.setLocationId(selectedLocationId);
        list.add(disease);
        WebDataServiceImpl.getInstance(mApp).addUpdateReference(WebServiceType.ADD_CUSTOM_HISTORY, Disease.class, list, this, this);
    }

    private void addDirection(String name) {
        DrugDirection direction = new DrugDirection();
        direction.setDirection(name);
        direction.setDoctorId(doctorId);
        direction.setHospitalId(selectedHospitalId);
        direction.setLocationId(selectedLocationId);
        WebDataServiceImpl.getInstance(mApp).addUpdateReference(WebServiceType.ADD_DIRECTION, DrugDirection.class, direction, this, this);
    }

    private void addDrugDosage(String name) {
        DrugDosage dosage = new DrugDosage();
        dosage.setDosage(name);
        dosage.setDoctorId(doctorId);
        dosage.setHospitalId(selectedHospitalId);
        dosage.setLocationId(selectedLocationId);
        WebDataServiceImpl.getInstance(mApp).addUpdateReference(WebServiceType.ADD_DOSAGE, DrugDosage.class, dosage, this, this);
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
        if (response != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && (Util.isNullOrBlank(doctorId) || Util.isNullOrBlank(selectedLocationId) || Util.isNullOrBlank(selectedHospitalId))) {
                        doctorId = user.getUniqueId();
                        selectedLocationId = user.getForeignLocationId();
                        selectedHospitalId = user.getForeignHospitalId();
                    }
                    if (!Util.isNullOrBlank(doctorId) && !Util.isNullOrBlank(selectedLocationId) && !Util.isNullOrBlank(selectedHospitalId)) {
                        initData();
                        mActivity.hideLoading();
                        return;
                    }
                    break;
                case ADD_NEW_GROUP:
                case UPDATE_GROUP:
                    if (response.isValidData(response) && response.getData() instanceof UserGroups) {
//                        mActivity.syncGroups(user);
                        UserGroups group = (UserGroups) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addUserGroup(group);
                        Util.sendBroadcast(mApp, FilterFragment.INTENT_REFRESH_GROUPS_LIST_LOCAL);
                        Util.sendBroadcast(mApp, ContactsListFragment.INTENT_REFRESH_GROUPS_LIST_FROM_SERVER);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_ADD_GROUP, null);
                    }
                    break;
                case SEND_EMAIL_CLINICAL_NOTES:
                case SEND_EMAIL_REPORTS:
                case SEND_EMAIL_PRESCRIPTION:
                case SEND_EMAIL_TREATMENT:
                case SEND_EMAIL_VISIT:
                    mActivity.hideLoading();
                    Util.showToast(mActivity, getResources().getString(R.string.email_sent_to) + String.valueOf(editName.getText()));
                    if (!Util.isNullOrBlank(HealthCocoConstants.SELECTED_PATIENTS_USER_ID))
                        LocalDataServiceImpl.getInstance(mApp).addEmailAddress(String.valueOf(editName.getText()), HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                    break;
                case ADD_DISEASE:
                    mActivity.hideLoading();
                    if (!Util.isNullOrEmptyList(response.getDataList()) && response.getDataList().get(0) instanceof Disease) {
                        LocalDataServiceImpl.getInstance(mApp).addDiseaseList((ArrayList<Disease>) (ArrayList<?>) response.getDataList());
                    }
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_REFERENCE_LIST, new Intent().putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(response.getDataList())));
                    break;
                case ADD_DOSAGE:
                    mActivity.hideLoading();
                    if (response.getData() != null && response.getData() instanceof DrugDosage) {
                        LocalDataServiceImpl.getInstance(mApp).addDrugDosage((DrugDosage) response.getData());
                    }
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_REFERENCE_LIST, null);
                    break;
                case ADD_DIRECTION:
                    mActivity.hideLoading();
                    if (response.getData() != null && response.getData() instanceof DrugDirection) {
                        LocalDataServiceImpl.getInstance(mApp).addDirection((DrugDirection) response.getData());
                    }
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_REFERENCE_LIST, null);
                    break;
                case ADD_CUSTOM_HISTORY:
                    mActivity.hideLoading();
                    if (response.getData() != null && response.getData() instanceof Disease) {
//                        LocalDataServiceImpl.getInstance(mApp).addDisease((Disease) response.getData());
                    }
//                    getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_DISEASED_LIST, HealthCocoConstants.RESULT_CODE_REFERENCE_LIST, null);
                    break;
                case ADD_REFERENCE:
                    mActivity.hideLoading();
                    if (response.getData() != null && response.getData() instanceof Reference) {
//                        LocalDataServiceImpl.getInstance(mApp).addReference((Reference) response.getData());
                    }
//                    getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_DISEASED_LIST, HealthCocoConstants.RESULT_CODE_REFERENCE_LIST, null);
                    break;
                case ADD_DIAGNOSTIC_TESTS:
                    //not adding tests in local(Since tests are got from Solar)
                    if (response.getData() != null && response.getData() instanceof DiagnosticTest) {
                        DiagnosticTest diagnosticTest = (DiagnosticTest) response.getData();
                        mActivity.hideLoading();
//                        getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_DIAGNOSTICS_TESTS, HealthCocoConstants.RESULT_CODE_DIAGNOSTICS_TESTS, new Intent().putExtra(DiagnosticTestsListFragment.TAG_ADDED_DIAGNOSTIC_TEST, diagnosticTest));
                    }
                    break;
                case SEARCH_PATIENTS:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
//                        ArrayList<AlreadyRegisteredPatientsResponse> list = (ArrayList<AlreadyRegisteredPatientsResponse>) (ArrayList<?>) response
//                                .getDataList();
//                        LocalDataServiceImpl.getInstance(mApp).addAlreadyRegisteredPatients(list);
                        openPatientNumberSearchResultsFragment();
                    } else {
                        openAddNewPatientDialog(AddUpdateNameDialogType.ADD_NEW_PATIENT_NAME, this, "", 0);
                    }
                    break;
                case REGISTER_PATIENT:
                    if (response.isValidData(response) && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                        RegisteredPatientDetailsUpdated patientDetails = (RegisteredPatientDetailsUpdated) response.getData();
                        LogUtils.LOGD(TAG, "REGISTER_PATIENT SYNC_COMPLETE" + patientDetails.getLocalPatientName());
                        LocalDataServiceImpl.getInstance(mApp).addPatient(patientDetails);
                        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = patientDetails.getUserId();
//                        Util.sendBroadcast(mApp, BookAppointmentDialogFragment.INTENT_REFRESH_SELECTED_PATIENT);
                        mActivity.hideLoading();
                        mActivity.finish();
                    }
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
        dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        String text = String.valueOf(s).toLowerCase(Locale.ENGLISH);
//        if (Util.isNullOrBlank(text))
//            enableSaveButton(false);
//        else
//            enableSaveButton(true);
    }

    private void enableSaveButton(boolean isEnabled) {
//        btSave.setClickable(isEnabled);
//        btSave.setEnabled(isEnabled);
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
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                }
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void openPatientNumberSearchResultsFragment() {
//        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
//        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_NUMBER_SEARCH_RESULTS.ordinal());
//        intent.putExtra(HealthCocoConstants.TAG_MOBILE_NUMBER, String.valueOf(editName.getText()));
//        intent.putExtra(PatientNumberSearchResultsFragment.TAG_IS_FROM_HOME_ACTIVITY, false);
//        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST);
    }

    protected void openAddNewPatientDialog(AddUpdateNameDialogType dialogType,
                                           Fragment fragment, String uniqueId, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, uniqueId);
        bundle.putString(HealthCocoConstants.TAG_MOBILE_NUMBER, String.valueOf(editName.getText()));
        bundle.putInt(HealthCocoConstants.TAG_ORDINAL_DIALOG_TYPE, dialogType.ordinal());
        AddUpdateNameDialogFragment addUpdateNameDialogFragment = new AddUpdateNameDialogFragment();
        addUpdateNameDialogFragment.setArguments(bundle);
        addUpdateNameDialogFragment.setTargetFragment(fragment, requestCode);
        addUpdateNameDialogFragment.show(mActivity.getSupportFragmentManager(),
                addUpdateNameDialogFragment.getClass().getSimpleName());
    }
}
