package com.healthcoco.healthcocopad.dialogFragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class PatientNumberSearchDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private static final int REQUEST_CODE_PATIENT_NUMBER_SERACH = 101;
    private static final int PICK_CONTACT = 1000;
    private EditText editMobileNumber;
    private Button btProceed;
    private Button btSkip;
    private User user;
    private boolean isMobileNumberOptional;
    private ImageView ivPhonebook;

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
            DoctorClinicProfile doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
            if (doctorClinicProfile != null && doctorClinicProfile.getMobileNumberOptional() != null)
                isMobileNumberOptional = doctorClinicProfile.getMobileNumberOptional();
//            isMobileNumberOptional = Util.getIsMobileNumberOptional(doctor);
            init();
            setWidthHeight(0.50, 0.52);
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
        ivPhonebook = (ImageView) view.findViewById(R.id.iv_phonebook);
        btProceed = (Button) view.findViewById(R.id.bt_proceed);
        btSkip = (Button) view.findViewById(R.id.bt_skip);
        mActivity.showSoftKeyboard(editMobileNumber);
        Util.setFocusToEditText(mActivity, editMobileNumber);

        if (isMobileNumberOptional) {
            btSkip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initListeners() {
        btProceed.setOnClickListener(this);
        btSkip.setOnClickListener(this);
        initCrossButton();
        initActionbarTitle(getResources().getString(R.string.add_new_patient));
        ivPhonebook.setOnClickListener(this);
    }

    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_proceed:
                validateData();
                break;
            case R.id.bt_skip:
                openRegistrationFragment(null);
                break;
            case R.id.iv_phonebook:
                requestPermission();
                break;
        }
    }

    public void requestPermission() {
        requestAppPermissions(new
                String[]{Manifest.permission.READ_CONTACTS}, HealthCocoActivity.REQUEST_PERMISSIONS);
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int requestCode) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(getContext(), permission);
        }
        requestPermissions(requestedPermissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }

    private void validateData() {
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
        mActivity.showLoading(false);
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
                        ArrayList<AlreadyRegisteredPatientsResponse> list = (ArrayList<AlreadyRegisteredPatientsResponse>) (ArrayList<?>) response
                                .getDataList();
                        LocalDataServiceImpl.getInstance(mApp).addAlreadyRegisteredPatients(list);
                        openPatientNumberSearchResultsFragment();
                    } else {
                        openRegistrationFragment(String.valueOf(editMobileNumber.getText()));
                    }
                    break;
            }
        }
        mActivity.hideLoading();
        getDialog().dismiss();
    }

    private void openRegistrationFragment(String mobileNumber) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_REGISTRATION.ordinal());
        if (!Util.isNullOrBlank(mobileNumber))
            intent.putExtra(HealthCocoConstants.TAG_MOBILE_NUMBER, mobileNumber);
        startActivityForResult(intent, REQUEST_CODE_PATIENT_NUMBER_SERACH);
    }

    private void openPatientNumberSearchResultsFragment() {
        Bundle args = new Bundle();
        PatientNumberSearchResultsDialogFragment patientNumberSearchResultsDialogFragment = new PatientNumberSearchResultsDialogFragment();
        args.putString(HealthCocoConstants.TAG_MOBILE_NUMBER, String.valueOf(editMobileNumber.getText()));
        args.putBoolean(PatientNumberSearchResultsDialogFragment.TAG_IS_FROM_HOME_ACTIVITY, true);
        patientNumberSearchResultsDialogFragment.setArguments(args);
        patientNumberSearchResultsDialogFragment.show(mFragmentManager, patientNumberSearchResultsDialogFragment.getClass().getSimpleName());
    }

//    public void openRegistrationFragment() {
//        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
//        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_REGISTRATION.ordinal());
//        intent.putExtra(HealthCocoConstants.TAG_MOBILE_NUMBER, String.valueOf(editMobileNumber.getText()));
//        startActivity(intent);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PATIENT_NUMBER_SERACH
                && resultCode == HealthCocoConstants.RESULT_CODE_REGISTRATION) {
            getTargetFragment().onActivityResult(requestCode, resultCode, data);
            dismiss();
        } else if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                ContentResolver cr = mActivity.getContentResolver();
                Cursor cursor = cr.query(contactData, null, null, null, null);
                if (cursor.moveToFirst()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String phone = null;
                    if (hasPhone > 0) {
                        Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (cp != null && cp.moveToFirst()) {
                            phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            cp.close();
                        }                    // Todo something when contact number selected
                        editMobileNumber.setText(Util.getValidMobileNumber(phone));
                    }
                }
            }
        }
    }
}
