package com.healthcoco.healthcocopad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.healthcoco.healthcocopad.activities.AddVisitsActivity;
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
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Shreshtha on 18-01-2017.
 */

public abstract class HealthCocoFragment extends Fragment implements GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    protected static final String TAG_USER = "user";
    protected static String SHOW_LOADING = "showLoading";
    protected View view;
    protected HealthCocoActivity mActivity;
    protected FragmentManager mFragmentManager;
    protected String TAG;
    protected HealthCocoApplication mApp;
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
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        setupUI(view);
        if (savedInstanceState != null) {
            LogUtils.LOGD(TAG, "onCreateView");
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = savedInstanceState.getString(HealthCocoConstants.TAG_SELECTED_USER_ID);
            LogUtils.LOGD(TAG, "onCreateView " + HealthCocoConstants.SELECTED_PATIENTS_USER_ID + mActivity);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    protected void setupUI(final View viewToSet) {
        try {
            viewToSet.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (!(viewToSet instanceof EditText) && !(viewToSet instanceof ScrollView) && !(viewToSet instanceof TextInputLayout)
                            && viewToSet.getParent() != null && !(viewToSet.getParent() instanceof ScrollView))
                        hideKeyboard(viewToSet);
                    viewToSet.setActivated(false);
                    EditTextTextViewErrorUtil.resetFocusToAllEditText(view);
                    return false;
                }
            });

            //If a layout container, iterate over children and seed recursion.
            if (viewToSet instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) viewToSet).getChildCount(); i++) {
                    View innerView = ((ViewGroup) viewToSet).getChildAt(i);
                    setupUI(innerView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hides virtual keyboard
     *
     * @author nehas
     */
    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
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

    protected EditText initEditSearchView(int hintId, View.OnClickListener onClickListener, TextWatcher textWatcher) {
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
        return editSearch;
    }

    protected EditText initEditSearchView(int hintId) {
        return initEditSearchView(hintId, null);
    }

    protected EditText initEditSearchView(int hintId, TextWatcher textWatcher) {
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
        return editSearch;
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

    protected void openDialogFragment(HealthCocoDialogFragment dialogFragment) {
        openDialogFragment(dialogFragment, null, null, 0, null, null);
    }

    protected void openDialogFragment(HealthCocoDialogFragment dialogFragment, int requestCode) {
        openDialogFragment(dialogFragment, null, null, requestCode, null, null);
    }

    protected void openDialogFragment(HealthCocoDialogFragment dialogFragment, int requestCode, CommonOpenUpFragmentType openUpFragmentType) {
        openDialogFragment(dialogFragment, null, null, requestCode, openUpFragmentType, null);
    }

    protected void openDialogFragment(HealthCocoDialogFragment dialogFragment, String intentTag, Object intentData, int requestCode, CommonOpenUpFragmentType openUpFragmentType) {
        openDialogFragment(dialogFragment, intentTag, intentData, requestCode, openUpFragmentType, null);
    }

    protected void openDialogFragment(HealthCocoDialogFragment dialogFragment, int requestCode, CommonOpenUpFragmentType openUpFragmentType, String selectedUserId) {
        openDialogFragment(dialogFragment, null, null, requestCode, openUpFragmentType, selectedUserId);
    }

    protected void openDialogFragment(HealthCocoDialogFragment dialogFragment, String intentTag, Object intentData, int requestCode) {
        openDialogFragment(dialogFragment, intentTag, intentData, requestCode, null, null);
    }

    protected void openDialogFragment(HealthCocoDialogFragment dialogFragment, String intentTag, Object intentData, int requestCode, CommonOpenUpFragmentType openUpFragmentType, String selectedUserId) {
        Bundle bundle = new Bundle();
        if (intentData != null)
            bundle.putParcelable(intentTag, Parcels.wrap(intentData));
        if (openUpFragmentType != null)
            bundle.putInt(HealthCocoConstants.TAG_FRAGMENT_NAME, openUpFragmentType.ordinal());
        if (selectedUserId != null)
            bundle.putString(HealthCocoConstants.TAG_SELECTED_USER_ID, selectedUserId);
        dialogFragment.setArguments(bundle);

        if (requestCode > 0)
            dialogFragment.setTargetFragment(this, requestCode);
        dialogFragment.show(mFragmentManager, dialogFragment.getClass().getSimpleName());
    }

    protected void openCommonOpenUpActivity(CommonOpenUpFragmentType fragmentType, String tag, Object intentData) {
        openCommonOpenUpActivity(fragmentType, tag, intentData, 0);
    }

    protected void openCommonOpenUpActivity(CommonOpenUpFragmentType fragmentType, Object intentData, int requestCode) {
        openCommonOpenUpActivity(fragmentType, HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA, intentData, requestCode);
    }

    protected void openCommonOpenUpActivity(CommonOpenUpFragmentType fragmentType, String tag, Object intentData, int requestCode) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (!Util.isNullOrBlank(tag) && intentData != null)
            intent.putExtra(tag, Parcels.wrap(intentData));
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
        openDialogFragment(new GlobalRecordAccessDialogFragment(), HealthCocoConstants.REQUEST_CODE_GLOBAL_RECORDS_ACCESS);
    }

    public void openVerificationOTPDialogFragment() {
        openDialogFragment(new VerifyOtpDialogFragment(), HealthCocoConstants.REQUEST_CODE_GLOBAL_RECORDS_ACCESS);
    }

    private void sendBroadcasts() {
        ArrayList<String> listIntentFilters;
        try {
            listIntentFilters = new ArrayList<String>() {{
                add(PatientProfileDetailFragment.INTENT_GET_HISTORY_LIST_LOCAL);
//                add(PatientProfileDetailFragment.INTENT_REFRESH_GLOBAL_RECORDS_ACCESS);
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

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
    }

    protected void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(mActivity.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
    }

    public void openCommonOpenUpVisitActivity(CommonOpenUpFragmentType fragmentType, int requestCode) {
        openCommonOpenUpVisitActivity(fragmentType, null, null, requestCode);
    }

    public void openCommonOpenUpVisitActivity(CommonOpenUpFragmentType fragmentType, Object intentData, int requestCode) {
        openCommonOpenUpVisitActivity(fragmentType, HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA, intentData, requestCode);
    }

    public void openCommonOpenUpVisitActivity(CommonOpenUpFragmentType fragmentType, String tag, Object intentData, int requestCode) {
        Intent intent = new Intent(mActivity, AddVisitsActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (!Util.isNullOrBlank(tag) && intentData != null)
            intent.putExtra(tag, Parcels.wrap(intentData));
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }
}