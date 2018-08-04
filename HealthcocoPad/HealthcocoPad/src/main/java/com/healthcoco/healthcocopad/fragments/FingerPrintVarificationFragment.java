package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.BiometricDetails;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

import java.util.ArrayList;

public class FingerPrintVarificationFragment extends HealthCocoFragment implements MFS100Event, View.OnClickListener, LocalDoInBackgroundListenerOptimised, HealthcocoTextWatcherListener {


    TextView lblMessage;
    byte[] Enroll_Template;
    byte[] Verify_Template;
    ScannerAction scannerAction = ScannerAction.Capture;
    int timeout = 1000000;
    MFS100 mfs100 = null;
    private BiometricDetails biometricDetails;
    private BiometricDetails fromDataBase;
    private TextView tvInitialAlphabetDoctor;
    private ImageView ivContactProfileDoctor;
    private TextView tvInitialAlphabet;
    private TextView tvPatientName;
    private TextView tvDoctorName;
    private ImageView ivContactProfile;
    private LinearLayout patientProfileLayout;
    private LinearLayout doctorProfileLayout;
    //    private LinearLayout containerLeftAction;
    private LinearLayout adharNoLayout;
    private LinearLayout patientFingerprintLayout;
    private AppCompatImageView doctorFingerPrintView;
    private AppCompatImageView patientFingerPrintView;
    private TextView tvfingerprintDoctor;
    private TextView tvfingerprintPatient;
    private TextView tvEnterAdharDetails;
    private TextView tvUseFingerPrint;
    private EditText editAdharNo;
    private EditText editMobileNo;
    private EditText editEmployeeNo;
    private ImageView ivAdhar;
    private ImageView ivMobile;
    private ImageView ivEmployee;
    private Button btStartDoctor;
    private Button btStopDoctor;
    private Button btStartPatient;
    private Button btStopPatient;
    private TextView tvDone;
    private ImageButton btnBack;
    private boolean isAdharLayoutVisible;
    private boolean isFingerprintLayoutVisible;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private DoctorProfile doctorProfile;
    private int ordinal;
    private boolean isDoctorVerified;
    private boolean isPatientVerified;
    private LinearLayout loadingMobile;
    private LinearLayout loadingAdhar;
    private LinearLayout loadingEmployee;
    private AnimatedVectorDrawable showFingerprint;
    private AnimatedVectorDrawable fingerprintToTick;
    private AnimatedVectorDrawable fingerprintToCross;
    private FingerData lastCapFingerData = null;
    private boolean isCaptureRunning = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_finger_print_verification, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        initListeners();

        Intent intent = mActivity.getIntent();
        ordinal = intent.getIntExtra(HealthCocoConstants.TAG_TAB_TYPE, 0);

        if (mfs100 == null) {
            mfs100 = new MFS100(this);
            mfs100.SetApplicationContext(mActivity);
        } else {
            InitScanner();
        }
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void init() {

    }

    @Override
    public void initViews() {

        lblMessage = (TextView) view.findViewById(R.id.lblMessage);

        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
        tvPatientName = (TextView) view.findViewById(R.id.tv_name);
        ivContactProfile = (ImageView) view.findViewById(R.id.iv_image);
        patientProfileLayout = (LinearLayout) view.findViewById(R.id.patient_profile_layout);
        doctorProfileLayout = (LinearLayout) view.findViewById(R.id.doctor_profile_layout);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        tvInitialAlphabetDoctor = (TextView) view.findViewById(R.id.tv_initial_aplhabet_doctor);
        ivContactProfileDoctor = (ImageView) view.findViewById(R.id.iv_image_doctor);

        patientFingerPrintView = (AppCompatImageView) view.findViewById(R.id.finger_view_patient);
        doctorFingerPrintView = (AppCompatImageView) view.findViewById(R.id.finger_view_doctor);
        tvfingerprintDoctor = (TextView) view.findViewById(R.id.tv_finger_doctor);
        tvfingerprintPatient = (TextView) view.findViewById(R.id.tv_finger_patient);
        tvUseFingerPrint = (TextView) view.findViewById(R.id.tv_use_finger_print);
        tvEnterAdharDetails = (TextView) view.findViewById(R.id.tv_enter_adhar_card);
        editAdharNo = (EditText) view.findViewById(R.id.edit_adhar_no);
        editMobileNo = (EditText) view.findViewById(R.id.edit_mobile_no);
        editEmployeeNo = (EditText) view.findViewById(R.id.edit_employee_no);
        ivAdhar = (ImageView) view.findViewById(R.id.iv_adhar_done);
        ivMobile = (ImageView) view.findViewById(R.id.iv_mobile_done);
        ivEmployee = (ImageView) view.findViewById(R.id.iv_employee_done);

        btStartDoctor = (Button) view.findViewById(R.id.bt_start_doctor);
        btStopDoctor = (Button) view.findViewById(R.id.bt_stop_doctor);
        btStartPatient = (Button) view.findViewById(R.id.bt_start_patient);
        btStopPatient = (Button) view.findViewById(R.id.bt_stop_patient);

        tvDone = (TextView) view.findViewById(R.id.tv_done);
        btnBack = (ImageButton) view.findViewById(R.id.bt_back);
//        containerLeftAction = (LinearLayout) view.findViewById(R.id.container_left_action);

        adharNoLayout = (LinearLayout) view.findViewById(R.id.layout_adhar_no);
        patientFingerprintLayout = (LinearLayout) view.findViewById(R.id.layout_patient_fingerprint);

        loadingMobile = (LinearLayout) view.findViewById(R.id.loading_mobile);
        loadingAdhar = (LinearLayout) view.findViewById(R.id.loading_adhar);
        loadingEmployee = (LinearLayout) view.findViewById(R.id.loading_employee);

        loadingMobile.setVisibility(View.GONE);
        loadingAdhar.setVisibility(View.GONE);
        loadingEmployee.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {

        btStartDoctor.setOnClickListener(this);
        btStopDoctor.setOnClickListener(this);
        btStartPatient.setOnClickListener(this);
        btStopPatient.setOnClickListener(this);
        tvUseFingerPrint.setOnClickListener(this);
        tvEnterAdharDetails.setOnClickListener(this);

        btnBack.setOnClickListener(this);
        tvDone.setOnClickListener(this);

        editMobileNo.addTextChangedListener(new HealthcocoTextWatcher(editMobileNo, this));
        editEmployeeNo.addTextChangedListener(new HealthcocoTextWatcher(editEmployeeNo, this));
        editAdharNo.addTextChangedListener(new HealthcocoTextWatcher(editAdharNo, this));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.btnInit:
                InitScanner();
                break;
            case R.id.btnUninit:
                UnInitScanner();
                break;
            case R.id.btnSyncCapture:
                scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    StartDoctorCapture();
                }
                break;
            case R.id.btnStopCapture:
                StopCapture();
                break;
            case R.id.btnMatchISOTemplate:
                scannerAction = ScannerAction.Verify;
                if (!isCaptureRunning) {
                    StartDoctorCapture();
                }
                break;*/
            case R.id.bt_start_doctor:
                scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    StartDoctorCapture();
                }
                btStartDoctor.setVisibility(View.GONE);
                btStopDoctor.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_stop_doctor:
                StopCapture();
                btStopDoctor.setVisibility(View.GONE);
                btStartDoctor.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_start_patient:
                scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    StartPatientCapture();
                }
                btStartPatient.setVisibility(View.GONE);
                btStopPatient.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_stop_patient:
                StopCapture();
                btStopPatient.setVisibility(View.GONE);
                btStartPatient.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_enter_adhar_card:
                if (!isAdharLayoutVisible) {
                    patientFingerprintLayout.setVisibility(View.GONE);
                    adharNoLayout.setVisibility(View.VISIBLE);
                    isAdharLayoutVisible = true;
                } else {
                    adharNoLayout.setVisibility(View.GONE);
                    isAdharLayoutVisible = false;
                }
                break;
            case R.id.tv_use_finger_print:
                if (!isFingerprintLayoutVisible) {
                    adharNoLayout.setVisibility(View.GONE);
                    patientFingerprintLayout.setVisibility(View.VISIBLE);
                    isFingerprintLayoutVisible = true;
                } else {
                    patientFingerprintLayout.setVisibility(View.GONE);
                    isFingerprintLayoutVisible = false;
                }
                break;
            case R.id.tv_done:
                validateData();
                break;
            case R.id.bt_back:
                mActivity.finish();
                break;
            default:
                break;
        }
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
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()))
                    user = doctor.getUser();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()) && selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getUserId())) {
                    doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileObject(user.getUniqueId());
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
        super.onResponse(response);
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (selectedPatient != null) {
                        refreshPatientDetails();
                    } else {
                        mActivity.showLoading(false);
                        WebDataServiceImpl.getInstance(mApp).getPatientProfile(RegisteredPatientDetailsUpdated.class, HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
                    }
                    break;
                case GET_PATIENT_PROFILE:
                    if (response.getData() != null && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                        selectedPatient = (RegisteredPatientDetailsUpdated) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addPatient(selectedPatient);
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void refreshPatientDetails() {
        tvPatientName.setText(selectedPatient.getLocalPatientName());
        DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENT_EMR_PROFILE, selectedPatient, null, ivContactProfile, tvInitialAlphabet);

        if (doctorProfile != null) {
            tvDoctorName.setText(Util.getValidatedValue(doctorProfile.getFirstNameWithTitle()));
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENT_EMR_PROFILE, doctorProfile, null, ivContactProfileDoctor, tvInitialAlphabetDoctor);
        }
    }

    private void validateData() {
      /*  ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;

        if (!isDoctorVerified) {
            msg = getResources().getString(R.string.please_complete_doctor_verification);
            errorViewList.add(tvDoctorName);
        } else if (!isPatientVerified) {
            if (!isAdharLayoutVisible) {
                msg = getResources().getString(R.string.please_complete_patient_verification);
                errorViewList.add(tvPatientName);
            } else {
                if (Util.isNullOrBlank(Util.getValidatedValueOrNull(editAdharNo))) {
                    msg = getResources().getString(R.string.please_enter_aadhar_number);
                    errorViewList.add(editAdharNo);
                } else if (Util.isNullOrBlank(Util.getValidatedValueOrNull(editMobileNo))) {
                    msg = getResources().getString(R.string.please_enter_mobile_no);
                    errorViewList.add(editMobileNo);
                } else if (Util.isNullOrBlank(Util.getValidatedValueOrNull(editEmployeeNo))) {
                    msg = getResources().getString(R.string.please_enter_employee_id);
                    errorViewList.add(editEmployeeNo);
                }
               *//* if (Util.isNullOrBlank(msg))
                    openPatientDetails();
                else {
//                    EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
                }*//*
            }
        }
        if (Util.isNullOrBlank(msg))*/
        openPatientDetails();
     /*   else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }*/
    }

    private void openPatientDetails() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, ordinal);
        startActivity(intent);
        mActivity.finish();
    }

    private void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                SetTextOnUIThread("Init success");

            }
        } catch (Exception ex) {
            Toast.makeText(mActivity, "Init failed, unhandled exception",
                    Toast.LENGTH_LONG).show();
            SetTextOnUIThread("Init failed, unhandled exception");
        }
    }

    private void StartDoctorCapture() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SetTextOnUIThread("");
                isCaptureRunning = true;
                try {
                    FingerData fingerData = new FingerData();
                    int ret = mfs100.AutoCapture(fingerData, timeout, false);
                    Log.e("StartSyncCapture.RET", "" + ret);
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        lastCapFingerData = fingerData;

                        SetTextOnUIThreadDoctor("Done");
                        isDoctorVerified = true;
                        SetData2(fingerData);
                    }
                } catch (Exception ex) {
                    SetTextOnUIThread("Error");
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();

    }

    private void StartPatientCapture() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SetTextOnUIThread("");
                isCaptureRunning = true;
                try {
                    FingerData fingerData = new FingerData();
                    int ret = mfs100.AutoCapture(fingerData, timeout, false);
                    Log.e("StartSyncCapture.RET", "" + ret);
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        lastCapFingerData = fingerData;

                        SetTextOnUIThreadPatient("Done");
                        isPatientVerified = true;
                        SetData2(fingerData);
                    }
                } catch (Exception ex) {
                    SetTextOnUIThread("Error");
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();

    }

    private void StopCapture() {
        try {
            mfs100.StopAutoCapture();
        } catch (Exception e) {
            SetTextOnUIThread("Error");
        }
    }

    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                SetTextOnUIThread("Uninit Success");
                lastCapFingerData = null;
            }
        } catch (Exception e) {
            Log.e("UnInitScanner.EX", e.toString());
        }
    }

    private void SetTextOnUIThreadDoctor(final String str) {

        lblMessage.post(new Runnable() {
            public void run() {
                tvfingerprintDoctor.setText(str);
                btStopDoctor.setVisibility(View.GONE);
            }
        });
    }

    private void SetTextOnUIThreadPatient(final String str) {

        lblMessage.post(new Runnable() {
            public void run() {
                tvfingerprintPatient.setText(str);
                btStopPatient.setVisibility(View.GONE);
            }
        });
    }

    private void SetTextOnUIThread(final String str) {

        lblMessage.post(new Runnable() {
            public void run() {
                lblMessage.setText(str);
            }
        });
    }

    public void SetData2(FingerData fingerData) {
        if (scannerAction.equals(ScannerAction.Capture)) {
            Enroll_Template = new byte[fingerData.ISOTemplate().length];
            System.arraycopy(fingerData.ISOTemplate(), 0, Enroll_Template, 0,
                    fingerData.ISOTemplate().length);
        } else if (scannerAction.equals(ScannerAction.Verify)) {
            Verify_Template = new byte[fingerData.ISOTemplate().length];
            System.arraycopy(fingerData.ISOTemplate(), 0, Verify_Template, 0,
                    fingerData.ISOTemplate().length);
            int ret = mfs100.MatchISO(Enroll_Template, Verify_Template);
            if (ret < 0) {
                SetTextOnUIThread("Error: " + ret + "(" + mfs100.GetErrorMsg(ret) + ")");
            } else {
                if (ret >= 1400) {
                    SetTextOnUIThread("Finger matched with score: " + ret);
                } else {
                    SetTextOnUIThread("Finger not matched, score: " + ret);
                }
            }
        }

    }

    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {
        int ret;
        if (!hasPermission) {
            SetTextOnUIThread("Permission denied");
            return;
        }
        if (vid == 1204 || vid == 11279) {
            if (pid == 34323) {
                ret = mfs100.LoadFirmware();
                if (ret != 0) {
                    SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                } else {
                    SetTextOnUIThread("Load firmware success");
                }
            } else if (pid == 4101) {
                String key = "Without Key";
                ret = mfs100.Init();
                if (ret == 0) {
                    showSuccessLog(key);
                } else {
                    SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                }

            }
        }
    }

    private void showSuccessLog(String key) {
        SetTextOnUIThread("Init success");
        String info = "\nKey: " + key + "\nSerial: "
                + mfs100.GetDeviceInfo().SerialNo() + " Make: "
                + mfs100.GetDeviceInfo().Make() + " Model: "
                + mfs100.GetDeviceInfo().Model()
                + "\nCertificate: " + mfs100.GetCertification();
    }

    @Override
    public void OnDeviceDetached() {
        UnInitScanner();
        SetTextOnUIThread("Device removed");
    }

    @Override
    public void OnHostCheckFailed(String err) {
        try {
            Toast.makeText(mActivity, err, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onDestroy() {
        if (mfs100 != null) {
            mfs100.Dispose();
        }
        super.onDestroy();
    }

    @Override
    public void afterTextChange(View v, String s) {
        switch (v.getId()) {
            case R.id.edit_adhar_no:
                LogUtils.LOGD(TAG, "Edit Aadhar Number");
                if (s.length() > 11) {
                    mActivity.hideSoftKeyboard();
                    setProgressThread(ivAdhar, loadingAdhar);
                } else {
                    loadingAdhar.setVisibility(View.GONE);
                    ivAdhar.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.edit_mobile_no:
                LogUtils.LOGD(TAG, "Edit Mobile Number");
                if (Util.isValidMobileNo(s)) {
                    mActivity.hideSoftKeyboard();
                    setProgressThread(ivMobile, loadingMobile);
                } else {
                    loadingMobile.setVisibility(View.GONE);
                    ivMobile.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.edit_employee_no:
                LogUtils.LOGD(TAG, "Edit Employee Number");
                if (s.length() > 7) {
                    mActivity.hideSoftKeyboard();
                    setProgressThread(ivEmployee, loadingEmployee);
                } else {
                    loadingEmployee.setVisibility(View.GONE);
                    ivEmployee.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    private void setProgressThread(final ImageView imageView, final LinearLayout linearLayout) {
        imageView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.postDelayed(new Runnable() {
            public void run() {
                linearLayout.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }


    private enum ScannerAction {
        Capture, Verify
    }
}

