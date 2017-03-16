package com.healthcoco.healthcocopad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.OtpVerification;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.dialogFragment.CommonListDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.CommonOptionsDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.GlobalRecordAccessDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.VerifyOtpDialogFragment;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.PatientProfileDetailFragment;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 18-01-2017.
 */

public abstract class HealthCocoFragment extends Fragment implements GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    protected static String SHOW_LOADING = "showLoading";
    protected View view;
    protected HealthCocoActivity mActivity;
    protected FragmentManager mFragmentManager;
    protected String TAG;
    protected HealthCocoApplication mApp;
    private Tracker mTracker;
    private boolean isOTPVerified;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private ImageButton btGlobalRecordAccess;
    private OtpVerification otpVerification;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();
        mActivity = (HealthCocoActivity) getActivity();
        mApp = (HealthCocoApplication) mActivity.getApplication();
        mFragmentManager = mActivity.getSupportFragmentManager();
//        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        view.setFocusable(true);
//        view.setFocusableInTouchMode(true);
        if (savedInstanceState != null) {
            LogUtils.LOGD(TAG, "onCreateView");
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = savedInstanceState.getString(HealthCocoConstants.TAG_SELECTED_USER_ID);
            LogUtils.LOGD(TAG, "onCreateView " + HealthCocoConstants.SELECTED_PATIENTS_USER_ID + mActivity);
        }
        if (mTracker != null) {
            LogUtils.LOGD(TAG, "Setting screen name: " + TAG);
            mTracker.setScreenName("Screen Name " + TAG);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker = ((HealthCocoApplication) getActivity().getApplication()).getDefaultTracker();
    }

    public abstract void init();

    public abstract void initViews();

    public abstract void initListeners();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            LogUtils.LOGD(TAG, "onSaveInstanceState");
            outState.putString(HealthCocoConstants.TAG_SELECTED_USER_ID, HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        }
    }

    protected void initEditSearchView(int hintId, TextWatcher textWatcher, boolean setPaddingTop) {
        LinearLayout layoutEditSearch = (LinearLayout) view.findViewById(R.id.parent_edit_search);
        if (setPaddingTop)
            layoutEditSearch.setPadding(layoutEditSearch.getPaddingLeft(), mActivity.getResources().getDimensionPixelOffset(R.dimen.layout_edit_search_padding), layoutEditSearch.getPaddingRight(), layoutEditSearch.getPaddingBottom());
        final EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
        editSearch.setHint(hintId);
        if (textWatcher != null)
            editSearch.addTextChangedListener(textWatcher);
        ImageButton btClear = (ImageButton) view.findViewById(R.id.bt_clear);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearch.setText("");
            }
        });
    }

    protected void initEditSearchView(String hintId, TextWatcher textWatcher, boolean setPaddingTop) {
        LinearLayout layoutEditSearch = (LinearLayout) view.findViewById(R.id.parent_edit_search);
        if (setPaddingTop)
            layoutEditSearch.setPadding(layoutEditSearch.getPaddingLeft(), mActivity.getResources().getDimensionPixelOffset(R.dimen.layout_edit_search_padding), layoutEditSearch.getPaddingRight(), layoutEditSearch.getPaddingBottom());
        final EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
        editSearch.setHint(hintId);
        if (textWatcher != null)
            editSearch.addTextChangedListener(textWatcher);
        ImageButton btClear = (ImageButton) view.findViewById(R.id.bt_clear);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearch.setText("");
            }
        });
    }

    protected void initEditSearchView(int hintId, View.OnClickListener onClickListener, TextWatcher textWatcher) {
        final EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
        editSearch.setHint(hintId);
        if (textWatcher != null)
            editSearch.addTextChangedListener(textWatcher);
        ImageButton btClear = (ImageButton) view.findViewById(R.id.bt_clear);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearch.setText("");
            }
        });
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        try {
            String errorMsg = errorMessage;
            if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
                errorMsg = volleyResponseBean.getErrMsg();
            }
            mActivity.hideLoading();
            showLoadingOverlay(false);
            Util.showToast(mActivity, errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }


    protected void showLoadingOverlay(boolean showLoading) {
        LinearLayout loadingOverlay = (LinearLayout) view.findViewById(R.id.loading_overlay);
        if (loadingOverlay != null) {
            if (showLoading)
                loadingOverlay.setVisibility(View.VISIBLE);
            else
                loadingOverlay.setVisibility(View.GONE);
        }
    }

    protected void moveCamerToLatLong(GoogleMap googleMap, Double latitude, Double longitude) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)).zoom(12).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    protected void addBullet(LinearLayout containerBullets, int layoutId) {
        LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(layoutId, null);
        layout.setSelected(false);
        containerBullets.addView(layout);
    }

    protected void setBulletSelected(int position, LinearLayout containerBullets) {
        for (int i = 0; i < containerBullets.getChildCount(); i++) {
            View view = containerBullets.getChildAt(i);
            if (i == position)
                view.setSelected(true);
            else view.setSelected(false);
        }
    }

    protected void openDialogFragment(DialogType dialogTypeTitle, CommonOptionsDialogItemClickListener listener) {
        CommonOptionsDialogFragment mDialogFragment = new CommonOptionsDialogFragment(dialogTypeTitle, listener);
        mDialogFragment.show(this.mFragmentManager, CommonOptionsDialogItemClickListener.class.getSimpleName());
    }

    protected void openCommonOpenUpActivity(CommonOpenUpFragmentType fragmentType, Object intentData, int requestCode) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (intentData != null) {
            intent.putExtra(HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA, (Serializable) intentData);
        }
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }

    protected String getSearchEditTextValue() {
        EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
        if (editSearch != null)
            return Util.getValidatedValue(String.valueOf(editSearch.getText()));
        return "";
    }

    protected void clearSearchEditText() {
        try {
            EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
            if (editSearch != null)
                editSearch.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected CommonListDialogFragment openCommonListDialogFragment(CommonListDialogItemClickListener listener, CommonListDialogType popupType, List<?> list) {
        CommonListDialogFragment commonListDialogFragment = new CommonListDialogFragment(listener, popupType, list);
        commonListDialogFragment.show(mFragmentManager, CommonListDialogFragment.class.getSimpleName());
        return commonListDialogFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_GLOBAL_RECORDS_ACCESS && resultCode == HealthCocoConstants.RESULT_CODE_GLOBAL_RECORDS_ACCESS) {
            openVerificationOTPDialogFragment();
        } else if (requestCode == HealthCocoConstants.REQUEST_CODE_VERIFY_OTP && resultCode == HealthCocoConstants.RESULT_CODE_VERIFY_OTP) {
            checkPatientStatus(user, selectedPatient);
        }
    }

    public void checkPatientStatus(User user, RegisteredPatientDetailsUpdated selectedPatient) {
        this.selectedPatient = selectedPatient;
        this.user = user;
        OtpVerification otpVerification = LocalDataServiceImpl.getInstance(mApp).getOtpVerification(selectedPatient.getDoctorId(),
                selectedPatient.getLocationId(), selectedPatient.getHospitalId(), selectedPatient.getUserId());
        LogUtils.LOGD(TAG, "Button generate Otp visible");
        if (otpVerification != null) {
            isOTPVerified = otpVerification.isPatientOTPVerified();
            boolean isDataAvailableWithOtherDoctor = otpVerification.isDataAvailableWithOtherDoctor();
            boolean isPatientOTPVerified = otpVerification.isPatientOTPVerified();
            if ((isDataAvailableWithOtherDoctor && isPatientOTPVerified)
                    || (!isDataAvailableWithOtherDoctor && !isPatientOTPVerified)) {
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    try {
                        WebDataServiceImpl.getInstance(mApp).getPatientStatus(OtpVerification.class, selectedPatient.getUserId(), selectedPatient.getDoctorId(),
                                selectedPatient.getLocationId(), selectedPatient.getHospitalId(), this, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            LogUtils.LOGD(TAG, "Button Global visible");
        }
    }

    private void addOtpVerificationData(OtpVerification otpVerification) {
        otpVerification.setDoctorId(user.getUniqueId());
        otpVerification.setLocationId(user.getForeignLocationId());
        otpVerification.setHospitalId(user.getForeignHospitalId());
        LocalDataServiceImpl.getInstance(mApp).addOtpVerification(otpVerification);
    }

    protected boolean isOtpVerified() {
        return isOTPVerified;
    }


    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case GET_PATIENT_STATUS:
                    if (response.getData() != null && response.getData() instanceof OtpVerification) {
                        otpVerification = (OtpVerification) response.getData();
                        if (otpVerification.isDataAvailableWithOtherDoctor()) {
                            ((CommonOpenUpActivity) mActivity).showRightAction(true);
                            if (!otpVerification.isPatientOTPVerified()) {
                                //set yet to verify icon
                                ((CommonOpenUpActivity) mActivity).enableRightActionButton(true);

                                showOtpVerificationAlert(otpVerification, selectedPatient);
                                return;
                            } else {
                                ((CommonOpenUpActivity) mActivity).enableRightActionButton(false);
                            }
                        } else {
                            ((CommonOpenUpActivity) mActivity).showRightAction(false);
                        }
                        isOTPVerified = otpVerification.isPatientOTPVerified();
                        addOtpVerificationData(otpVerification);
                        if (isOTPVerified)
                            sendBroadcasts();
                    }
                    break;
            }
        }
    }

    public void openGlobalRecordAccessDialogFragment() {
        GlobalRecordAccessDialogFragment dialogFragment = new GlobalRecordAccessDialogFragment();
        dialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_GLOBAL_RECORDS_ACCESS);
        dialogFragment.show(mFragmentManager, dialogFragment.getClass().getSimpleName());
    }

    public void openVerificationOTPDialogFragment() {
        VerifyOtpDialogFragment dialogFragment = new VerifyOtpDialogFragment();
        dialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_GLOBAL_RECORDS_ACCESS);
        dialogFragment.show(mFragmentManager,
                dialogFragment.getClass().getSimpleName());
    }

    private void sendBroadcasts() {
        ArrayList<String> listIntentFilters;
        try {
            listIntentFilters = new ArrayList<String>() {{
                add(PatientProfileDetailFragment.INTENT_GET_HISTORY_LIST_LOCAL);
//                add(PatientDetailFragmentUpdated.INTENT_REFRESH_GLOBAL_RECORDS_ACCESS);
            }};
            for (String intentFilter :
                    listIntentFilters) {
                Intent intent = new Intent(intentFilter);
                intent.putExtra(SHOW_LOADING, true);
                LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showOtpVerificationAlert(OtpVerification otpVerification, RegisteredPatientDetailsUpdated selectedPatient) {
        LinearLayout toastLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.dialog_fragment_otp_alert_top, null);
        toastLayout.setLayoutParams(new LinearLayout.LayoutParams(ScreenDimensions.SCREEN_WIDTH, LinearLayout.LayoutParams.WRAP_CONTENT));
        Toast toast = new Toast(mActivity); //context is object of Context write "this" if you are an Activity
        // Set The layout as Toast View
        toast.setView(toastLayout);
        // Position you toast here toast position is 50 dp from bottom you can give any integral value
        toast.setMargin(0, 0);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
}