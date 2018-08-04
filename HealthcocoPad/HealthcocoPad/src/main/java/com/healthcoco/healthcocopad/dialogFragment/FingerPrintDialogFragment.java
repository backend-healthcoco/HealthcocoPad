package com.healthcoco.healthcocopad.dialogFragment;

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

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
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
import com.healthcoco.healthcocopad.enums.LeaveFromScreenType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
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

import org.parceler.Parcels;

import java.util.ArrayList;

public class FingerPrintDialogFragment extends HealthCocoDialogFragment implements MFS100Event,
        View.OnClickListener,
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean> {

    public static final String TAG_FROM_SCREEN_TYPE = "isDoctorProfile";


    TextView lblMessage;
    byte[] Enroll_Template;
    byte[] Verify_Template;
    ScannerAction scannerAction = ScannerAction.Capture;
    int timeout = 1000000;
    MFS100 mfs100 = null;
    private BiometricDetails biometricDetails;
    private TextView tvInitialAlphabet;
    private TextView tvDoctorName;
    private ImageView ivContactProfile;
    //    private LinearLayout containerLeftAction;

    private AppCompatImageView doctorFingerPrintView;
    private TextView tvfingerprintDoctor;


    private Button btStartDoctor;
    private Button btStopDoctor;

    private TextView tvDone;
    private ImageButton btnBack;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private DoctorProfile doctorProfile;
    private String uniqueId;
    private boolean isDoctorVerified;
    private FingerData lastCapFingerData = null;
    private boolean isCaptureRunning = false;
    private LeaveFromScreenType leaveFromScreenType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_finger_print, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        initListeners();

        Bundle bundle = getArguments();
        if (bundle != null) {
            int ordinal = Parcels.unwrap(bundle.getParcelable(TAG_FROM_SCREEN_TYPE));
            leaveFromScreenType = LeaveFromScreenType.values()[ordinal];
        }

        if (mfs100 == null) {
            mfs100 = new MFS100(this);
            mfs100.SetApplicationContext(mActivity);
        } else {
            InitScanner();
        }
        setWidthHeight(0.80, 0.90);

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
        ivContactProfile = (ImageView) view.findViewById(R.id.iv_image);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);

        doctorFingerPrintView = (AppCompatImageView) view.findViewById(R.id.finger_view_doctor);
        tvfingerprintDoctor = (TextView) view.findViewById(R.id.tv_finger_doctor);

        btStartDoctor = (Button) view.findViewById(R.id.bt_start_doctor);
        btStopDoctor = (Button) view.findViewById(R.id.bt_stop_doctor);

        tvDone = (TextView) view.findViewById(R.id.tv_done);
        btnBack = (ImageButton) view.findViewById(R.id.bt_back);
//        containerLeftAction = (LinearLayout) view.findViewById(R.id.container_left_action);

    }

    @Override
    public void initListeners() {

        btStartDoctor.setOnClickListener(this);
        btStopDoctor.setOnClickListener(this);

        btnBack.setOnClickListener(this);
        tvDone.setOnClickListener(this);

    }

    @Override
    public void initData() {

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
            case R.id.tv_done:
                validateData();
                break;
            case R.id.bt_back:
                dismiss();
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
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileObject(user.getUniqueId());
                }
                break;
            case ADD_LEAVE:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.ADD_LEAVE);

                LocalDataServiceImpl.getInstance(mApp).addBioMetricDetails(biometricDetails);

                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new

                    VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;

    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    refreshPatientDetails();
                    break;
                case ADD_LEAVE:
                    Util.showToast(mActivity, R.string.fingerprint_added);
//                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_ADD_NEW_LEAVE, null);
                    mActivity.hideLoading();
                    dismiss();
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void refreshPatientDetails() {
        switch (leaveFromScreenType) {
            case ADD_NEW:
                if (selectedPatient != null) {
                    uniqueId = selectedPatient.getUniqueId();
                    tvDoctorName.setText(selectedPatient.getLocalPatientName());
                    DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENT_EMR_PROFILE, selectedPatient, null, ivContactProfile, tvInitialAlphabet);
                }
                break;
            case RESCHEDULE:
                if (doctorProfile != null) {
                    uniqueId = user.getUniqueId();
                    tvDoctorName.setText(Util.getValidatedValue(doctorProfile.getFirstNameWithTitle()));
                    DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENT_EMR_PROFILE, doctorProfile, null, ivContactProfile, tvInitialAlphabet);
                }
                break;
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;

        if (!isDoctorVerified) {
            msg = getResources().getString(R.string.please_add_finger_verification);
            errorViewList.add(tvDoctorName);
        }
        if (Util.isNullOrBlank(msg))

            addFingerPrint();
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addFingerPrint() {
        mActivity.showLoading(false);
        biometricDetails = new BiometricDetails();
        biometricDetails.setUniqueId(uniqueId);
        biometricDetails.setBioDetails(Enroll_Template);

        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_LEAVE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {

    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {

    }

    private enum ScannerAction {
        Capture, Verify
    }
}

