package com.healthcoco.healthcocopad;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.activities.CloudPrintActivity;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.MyPrintDocumentAdapter;
import com.healthcoco.healthcocopad.bean.UserPermissionsResponse;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AllUIPermission;
import com.healthcoco.healthcocopad.bean.server.BloodGroup;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.CityResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocopad.bean.server.CollegeUniversityInstitute;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.EducationQualification;
import com.healthcoco.healthcocopad.bean.server.GCMRequest;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MedicalCouncil;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.Profession;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.Specialities;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddUpdateNameDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.EnlargedImageViewDialogFragment;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.DefaultSyncServiceType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.DoctorProfileFragment;
import com.healthcoco.healthcocopad.fragments.InitialSyncFragment;
import com.healthcoco.healthcocopad.fragments.MenuDrawerFragment;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.MyExceptionHandler;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.FontAwesomeButton;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Shreshtha on 18-01-2017.
 */

public class HealthCocoActivity extends AppCompatActivity implements GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean> {
    protected static final String TAG = HealthCocoActivity.class.getSimpleName();
    public static long GCM_WAIT_TIME = 2000;
    protected HealthCocoApplication mApp;
    private Dialog alertDialog;
    private Handler mHandler;
    private Runnable runnable;
    private static final long DEFAULT_LOADING_TIME = 20000;
    private ArrayList<DefaultSyncServiceType> defaultWebServicesList = new ArrayList<>();
    private boolean isInitialLoading;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (HealthCocoApplication) getApplication();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
    }

    public void showLoading(boolean enableTouchOutside) {
        try {
            if (alertDialog == null) {
                LogUtils.LOGD(TAG, "Initialising Dialog ");
                alertDialog = new Dialog(this, R.style.Dialog_Transparent_Background);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.setCancelable(false);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_loading, null);
                alertDialog.setContentView(dialogView);
            }
            View parentView = alertDialog.findViewById(R.id.parent_view);
            if (parentView != null && enableTouchOutside) {
                parentView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        hideLoading();
                        return false;
                    }
                });
            }
            TextView tvMessage = (TextView) alertDialog.findViewById(R.id.tv_message);
            tvMessage.setVisibility(View.GONE);
            if (alertDialog != null && !alertDialog.isShowing()) {
                alertDialog.show();
                startHandler(tvMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startHandler(final TextView tvMessage) {
        mHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (tvMessage != null) {
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText(R.string.loading_taking_time);
                }
            }
        };
        mHandler.postDelayed(runnable, DEFAULT_LOADING_TIME);
    }

    public void hideLoading() {
        try {
            if (alertDialog != null && alertDialog.isShowing()) {
                if (mHandler != null)
                    mHandler.removeCallbacks(runnable);
                alertDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initDefaultData(User user) {
        isInitialLoading = true;
        this.user = user;
        updateProgress(DefaultSyncServiceType.GET_DRUG_DOSAGE);
    }

    private void updateProgress(DefaultSyncServiceType syncServiceType) {
        try {
            if (syncServiceType != null) {
                sendBroadCastToInitialSyncFragment(syncServiceType);
                switch (syncServiceType) {
                    case GET_DRUG_DOSAGE:
                        //get dosages
                        Long latestUpdatedTimeDosage = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DOSAGE);
                        WebDataServiceImpl.getInstance(mApp)
                                .getDosageDirection(WebServiceType.GET_DRUG_DOSAGE, DrugDosage.class, true, this.user.getUniqueId(), latestUpdatedTimeDosage, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_CONTACTS:
                        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.REGISTERED_PATIENTS_DETAILS);
                        WebDataServiceImpl.getInstance(mApp)
                                .getContactsList(RegisteredPatientDetailsUpdated.class, user.getUniqueId(),
                                        user.getForeignHospitalId(), user.getForeignLocationId(), latestUpdatedTime, this, this);
                        break;
                    case GET_DOCTOR_PROFILE:
                        WebDataServiceImpl.getInstance(mApp).getDoctorProfile(DoctorProfile.class, user.getUniqueId(), null, null, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_CLINIC_PROFILE:
                        WebDataServiceImpl.getInstance(mApp)
                                .getClinicDetails(ClinicDetailResponse.class, user.getForeignLocationId(), this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_DURATION_UNIT:
                        //get durationUnit
                        Long latestUpdatedTimeDuration = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DURATION_UNIT);
                        WebDataServiceImpl.getInstance(mApp)
                                .getDosageDirection(WebServiceType.GET_DURATION_UNIT, DrugDurationUnit.class, true, this.user.getUniqueId(), latestUpdatedTimeDuration, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_DIRECTION:
                        //get directions
                        Long latestUpdatedTimeDirection = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DIRECTION);
                        WebDataServiceImpl.getInstance(mApp)
                                .getDosageDirection(WebServiceType.GET_DIRECTION, DrugDirection.class, true, this.user.getUniqueId(), latestUpdatedTimeDirection, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_DRUG_TYPE:
                        //get drugTypesList
                        Long latestUpdatedTimeStrengthUnit = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.STRENGTH_UNIT);
                        WebDataServiceImpl.getInstance(mApp)
                                .getDrugType(WebServiceType.GET_DRUG_TYPE, DrugType.class, false, this.user.getUniqueId(), latestUpdatedTimeStrengthUnit, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_GROUPS:
                        syncGroups(user);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_CITIES:
                        WebDataServiceImpl.getInstance(mApp)
                                .getCities(CityResponse.class, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_SPECIALITIES:
                        Long latestUpdatedTimeSpecialities = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.SPECIALITIES);
                        WebDataServiceImpl.getInstance(mApp)
                                .getSpecialities(Specialities.class, latestUpdatedTimeSpecialities, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_PROFESSION:
                        WebDataServiceImpl.getInstance(mApp).getProfession(Profession.class, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_REFERENCE:
                        Long latestUpdatedTimeReference = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.REFERENCE);

                        WebDataServiceImpl.getInstance(mApp)
                                .getReference(Reference.class, user.getUniqueId(), latestUpdatedTimeReference, BooleanTypeValues.TRUE, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_COMPLAINT_SUGGESTIONS:
                        getComplaintSuggestions(user);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_OBSERVATION_SUGESTIONS:
                        getObservationSuggestions(user);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_INVESTIGATION_SUGGESTIONS:
                        getInvestigationSuggestions(user);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_DIAGNOSIS_SUGGESTIONS:
                        getDiagnosisSuggestions(user);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_BLOOD_GROUP:
                        WebDataServiceImpl.getInstance(mApp)
                                .getBloodGroup(BloodGroup.class, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_HISTORY:
                        WebDataServiceImpl.getInstance(mApp)
                                .getDiseaseList(Disease.class, user.getUniqueId(), 0l, null, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_TEMPLATES:
                        WebDataServiceImpl.getInstance(mApp).getTemplatesList(TempTemplate.class, user.getUniqueId(), 0l, this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_DOCTORS_UI_PERMISIIONS:
                        getDoctorsUIPermissions();
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case GET_ALL_UI_PERMISSIONS:
                        WebDataServiceImpl.getInstance(mApp).getALLUIPermissions(AllUIPermission.class, user.getUniqueId(), this, this);
                        defaultWebServicesList.add(syncServiceType);
                        break;
                    case SYNC_COMPLETE:
                        isInitialLoading = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDoctorsUIPermissions() {
        WebDataServiceImpl.getInstance(mApp).getDoctorsUIPermissions(UserPermissionsResponse.class, user.getUniqueId(), this, this);
    }

    public void syncGroups(User user) {
        //Get groupsList
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.USER_GROUP);
        WebDataServiceImpl.getInstance(mApp).getGroupsList(WebServiceType.GET_GROUPS, UserGroups.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), latestUpdatedTime, null, this, this);
    }

    private void sendBroadCastToInitialSyncFragment(DefaultSyncServiceType syncServiceType) {
        Intent intent = new Intent();
        intent.putExtra(HealthCocoConstants.TAG_SYNC_SERVICE_TYPE, syncServiceType.ordinal());
        intent.setAction(InitialSyncFragment.INTENT_ACTION_INITIAL_SYNC);
        sendBroadcast(intent);
    }

    private void deleteFromSyncListAndUpdateProgress(WebServiceType webServiceType) {
        LogUtils.LOGD(TAG, "Sync Success " + webServiceType);
        if (isInitialLoading)
            switch (webServiceType) {
                case GET_DRUG_DOSAGE:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_CONTACTS);
                    return;
                case GET_CONTACTS:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_DOCTOR_PROFILE);
                    return;
                case GET_DOCTOR_PROFILE:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_CLINIC_PROFILE);
                    return;
                case GET_CLINIC_PROFILE:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_DURATION_UNIT);
                    return;
                case GET_DURATION_UNIT:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_DIRECTION);
                    return;
                case GET_DIRECTION:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_DRUG_TYPE);
                    break;
                case GET_DRUG_TYPE:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_GROUPS);
                    break;
                case GET_GROUPS:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_CITIES);
                    break;
                case GET_CITIES:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_SPECIALITIES);
                    break;
                case GET_SPECIALITIES:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_PROFESSION);
                    break;
                case GET_PROFESSION:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_REFERENCE);
                    break;
                case GET_REFERENCE:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_COMPLAINT_SUGGESTIONS);
                    break;
//                case GET_CALENDAR_EVENTS:
////                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
////                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
////                    updateProgress(DefaultSyncServiceType.GET_COMPLAINT_SUGGESTIONS);
////                    break;
                case GET_COMPLAINT_SUGGESTIONS:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_OBSERVATION_SUGESTIONS);
                    break;
                case GET_OBSERVATION_SUGGESTIONS:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_INVESTIGATION_SUGGESTIONS);
                    break;
                case GET_INVESTIGATION_SUGGESTIONS:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_DIAGNOSIS_SUGGESTIONS);
                    break;
                case GET_DIAGNOSIS_SUGGESTIONS:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_BLOOD_GROUP);
                    break;
                case GET_BLOOD_GROUP:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_HISTORY);
                    break;
                case GET_DISEASE_LIST:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_TEMPLATES);
                    break;
                case GET_TEMPLATES_LIST:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_DOCTORS_UI_PERMISIIONS);
                    break;
                case GET_UI_PERMISSIONS_FOR_DOCTOR:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.GET_ALL_UI_PERMISSIONS);
                case GET_ALL_UI_PERMISSIONS:
                    if (defaultWebServicesList.contains(DefaultSyncServiceType.getSyncType(webServiceType)))
                        defaultWebServicesList.remove(DefaultSyncServiceType.getSyncType(webServiceType));
                    updateProgress(DefaultSyncServiceType.SYNC_COMPLETE);
                default:
                    break;
            }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (Util.isNullOrBlank(volleyResponseBean.getErrMsg()))
            errorMsg = volleyResponseBean.getErrMsg();
        deleteFromSyncListAndUpdateProgress(volleyResponseBean.getWebServiceType());
        hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        deleteFromSyncListAndUpdateProgress(webServiceType);
        Util.showToast(this, R.string.user_offline);
        hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            LogUtils.LOGD(TAG, "Success " + response.getWebServiceType());
            if (response.isDataFromLocal()) {
                deleteFromSyncListAndUpdateProgress(response.getWebServiceType());
            } else {
                switch (response.getWebServiceType()) {
                    case GET_DRUG_DOSAGE:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_DRUG_DOSAGE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    case GET_CONTACTS:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_PATIENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_DOCTOR_PROFILE:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_DOCTOR_PROFILE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    case GET_CLINIC_PROFILE:
                        if (response.getData() != null && response.getData() instanceof ClinicDetailResponse)
                            new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_LOCATION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_DURATION_UNIT:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_DURATION_UNIT, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    case GET_DIRECTION:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_DIRECTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_DRUG_TYPE:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_DRUG_TYPE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_GROUPS:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_GROUPS_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_CITIES:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_CITIES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_SPECIALITIES:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_SPECIALITIES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_PROFESSION:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_PROFESSION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_REFERENCE:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_REFERENCE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_BLOOD_GROUP:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_BLOOD_GROUPS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_CALENDAR_EVENTS:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_CALENDAR_EVENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_EDUCATION_QUALIFICATION:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_EDUCATION_QUALIFICATION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_COLLEGE_UNIVERSITY_INSTITUES:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_INSTITUTES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_MEDICAL_COUNCILS:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_MEDICAL_COUNCILS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_COMPLAINT_SUGGESTIONS:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_COMPLAINT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_OBSERVATION_SUGGESTIONS:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_OBSERVATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_INVESTIGATION_SUGGESTIONS:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_INVESTIGATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_DIAGNOSIS_SUGGESTIONS:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_DIAGNOSIS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_DISEASE_LIST:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_DISEASE_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_TEMPLATES_LIST:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_TEMPLATES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_UI_PERMISSIONS_FOR_DOCTOR:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_DOCTORS_UI_PERMISSIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    case GET_ALL_UI_PERMISSIONS:
                        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.ADD_ALL_UI_PERMISSIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_DRUG_DOSAGE:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp)
                            .addDrugDosageList((ArrayList<DrugDosage>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_DOCTOR_PROFILE:
                if (response.getData() != null)
                    LocalDataServiceImpl.getInstance(mApp)
                            .addDoctorProfile((DoctorProfile) response.getData());
                break;
            case ADD_LOCATION:
                if (response.getData() != null)
                    LocalDataServiceImpl.getInstance(mApp).
                            addClinicDetailResponse((ClinicDetailResponse) response.getData());
                break;
            case ADD_DURATION_UNIT:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addDurationUnitList((ArrayList<DrugDurationUnit>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_DIRECTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addDirectionsList((ArrayList<DrugDirection>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_DRUG_TYPE:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addDrugType((ArrayList<DrugType>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_GROUPS_LIST:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addUserGroupsList((ArrayList<UserGroups>) (ArrayList<?>) response
                            .getDataList());
                break;
            case ADD_SPECIALITIES:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addSpecialities((ArrayList<Specialities>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_CITIES:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addCities((ArrayList<CityResponse>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_PROFESSION:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addProfessionsList((ArrayList<Profession>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_REFERENCE:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addReferenceList((ArrayList<Reference>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_BLOOD_GROUPS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addBloodGroups((ArrayList<BloodGroup>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_CALENDAR_EVENTS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addCalendarEventsList(
                            (ArrayList<CalendarEvents>) (ArrayList<?>) response
                                    .getDataList());
                break;
            case ADD_PATIENTS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addPatientsList((ArrayList<RegisteredPatientDetailsUpdated>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_EDUCATION_QUALIFICATION:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addEducationsList((ArrayList<EducationQualification>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_INSTITUTES:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addCollegeUniversityInstituteList((ArrayList<CollegeUniversityInstitute>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_MEDICAL_COUNCILS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addMedicalCouncilList((ArrayList<MedicalCouncil>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_COMPLAINT_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_COMPLAINT_SUGGESTIONS, LocalTabelType.COMPLAINT_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_OBSERVATION_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_OBSERVATION_SUGGESTIONS, LocalTabelType.OBSERVATION_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_INVESTIGATION_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_INVESTIGATION_SUGGESTIONS, LocalTabelType.INVESTIGATION_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_DIAGNOSIS_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_DIAGNOSIS_SUGGESTIONS, LocalTabelType.DIAGNOSIS_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_DISEASE_LIST:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addDiseaseList((ArrayList<Disease>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_TEMPLATES:
//                if (!Util.isNullOrEmptyList(response.getDataList()))
//                    LocalDataServiceImpl.getInstance(mApp).addTemplatesList((ArrayList<TempTemplate>) (ArrayList<?>) response.getDataList());
            case ADD_DOCTORS_UI_PERMISSIONS:
                if (response.getData() != null)
                    LocalDataServiceImpl.getInstance(mApp).
                            addUserUiPermissions((UserPermissionsResponse) response.getData());
                break;
            case ADD_ALL_UI_PERMISSIONS:
                if (response.getData() != null)
                    LocalDataServiceImpl.getInstance(mApp).
                            addALLUiPermissions((AllUIPermission) response.getData());
                break;
        }
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(response.getWebServiceType());
        volleyResponseBean.setIsDataFromLocal(true);
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {
    }

    public void refreshMenuFragment(DoctorProfile doctorProfile) {
        MenuDrawerFragment menuFragment = (MenuDrawerFragment) getSupportFragmentManager().findFragmentByTag(MenuDrawerFragment.class.getSimpleName());
        if (menuFragment != null) {
            menuFragment.initData(doctorProfile);
        }
        DoctorProfileFragment doctorProfileFragment = (DoctorProfileFragment) getSupportFragmentManager().findFragmentByTag(DoctorProfileFragment.class.getSimpleName());
        if (doctorProfileFragment != null) {
            doctorProfileFragment.initData(doctorProfile);
        }
    }

    private void getComplaintSuggestions(User user) {
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.COMPLAINT);
        WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ComplaintSuggestions.class, WebServiceType.GET_COMPLAINT_SUGGESTIONS, user.getUniqueId(),
                latestUpdatedTime, this, this);
    }

    private void getObservationSuggestions(User user) {
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.OBSERVATION);
        WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ObservationSuggestions.class, WebServiceType.GET_OBSERVATION_SUGGESTIONS, user.getUniqueId(),
                latestUpdatedTime, this, this);

    }

    private void getInvestigationSuggestions(User user) {
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.INVESTIGATION);
        WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(InvestigationSuggestions.class, WebServiceType.GET_INVESTIGATION_SUGGESTIONS, user.getUniqueId(),
                latestUpdatedTime, this, this);

    }

    private void getDiagnosisSuggestions(User user) {
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DIAGNOSIS);
        WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(DiagnosisSuggestions.class, WebServiceType.GET_DIAGNOSIS_SUGGESTIONS, user.getUniqueId(),
                latestUpdatedTime, this, this);

    }

    public void openEnlargedImageDialogFragment(String url) {
        openEnlargedImageDialogFragment(false, url);
    }

    public void openEnlargedImageDialogFragment(boolean isPrintFile, String url) {
        EnlargedImageViewDialogFragment imageDialogFragment = new EnlargedImageViewDialogFragment();
        Bundle args = new Bundle();
        args.putString(EnlargedImageViewDialogFragment.TAG_IMAGE_URL, url);
        args.putBoolean(EnlargedImageViewDialogFragment.TAG_PRINT_PDF, isPrintFile);
        imageDialogFragment.setArguments(args);
        imageDialogFragment.show(getSupportFragmentManager(), imageDialogFragment.getClass().getSimpleName());
    }

    public void openCommonOpenUpActivity(CommonOpenUpFragmentType fragmentType, Object intentData, int requestCode) {
        Intent intent = new Intent(this, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (intentData != null) {
            intent.putExtras((Intent) intentData);
        }
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }

    public Uri openCamera(HealthCocoFragment currentFragment, String reportImage) {
        if (ImageUtil.isDeviceSupportCamera(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri fileUri = ImageUtil.getOutputMediaFileUri(ImageUtil.MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//// ******** code for crop image
//            intent.putExtra("crop", "true");
//            intent.putExtra("aspectX", 0);
//            intent.putExtra("aspectY", 0);
//            intent.putExtra("outputX", 200);
//            intent.putExtra("outputY", 200);
//
//            intent.putExtra("return-data", true);
            // start the image capture Intent
            currentFragment.startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_CAMERA);
            return fileUri;
        } else {
            Util.showToast(this, getResources().getString(R.string.device_doesnt_support_camera));
        }
        return null;
    }

    public void openGallery(HealthCocoFragment currentFragment) {
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        currentFragment.startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_GALLERY);
    }

    public Uri openCamera(HealthCocoDialogFragment currentFragment, String reportImage) {
        if (ImageUtil.isDeviceSupportCamera(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri fileUri = ImageUtil.getOutputMediaFileUri(ImageUtil.MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//// ******** code for crop image
//            intent.putExtra("crop", "true");
//            intent.putExtra("aspectX", 0);
//            intent.putExtra("aspectY", 0);
//            intent.putExtra("outputX", 200);
//            intent.putExtra("outputY", 200);
//
//            intent.putExtra("return-data", true);
            // start the image capture Intent
            currentFragment.startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_CAMERA);
            return fileUri;
        } else {
            Util.showToast(this, getResources().getString(R.string.device_doesnt_support_camera));
        }
        return null;
    }

    public void openGallery(HealthCocoDialogFragment currentFragment) {
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        currentFragment.startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_GALLERY);
    }

    public void doPrinting(String filePath) {
//         filePath = "/storage/emulated/0/HealthCococMedia/Enlarged Images/testpdf1472488361996.pdf";
//         Make sure we're running on Kitkat or higher to use Android Print Framework
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            doPrintAboveKitkat(filePath);
        } else {
            doCloudPrinting(filePath);
        }
    }

    public String getFormattedVersionName() {
        return String.format(getResources().getString(R.string.app_version_about_us_name),
                getResources().getString(R.string.app_version_name));
    }

    public void initChangeViewButton(View.OnClickListener onClickListener) {
        LinearLayout containerMiddleAction = (LinearLayout) findViewById(R.id.container_middle_action);
        containerMiddleAction.setOnClickListener(onClickListener);
    }

    public void openLoginSignupActivity() {
        Intent intentLoginSignUp = new Intent(this, CommonOpenUpActivity.class);
        intentLoginSignUp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentLoginSignUp.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.LOGIN_SIGN_UP.ordinal());
        startActivity(intentLoginSignUp);
    }

    public void initGCM() {
        if (Util.checkPlayServices(this)) {
            LoginResponse doctor = LocalDataServiceImpl.getInstance((HealthCocoApplication) this.getApplicationContext()).getDoctor();
            GCMRequest gcmRequest = LocalDataServiceImpl.getInstance(mApp).getGCMRequestData();
            if (gcmRequest != null) {
                if (doctor != null && doctor.getUser() != null) {
                    ArrayList<String> userIdsList = new ArrayList<>();
                    userIdsList.add(doctor.getUser().getUniqueId());
                    gcmRequest.setUserIds(userIdsList);
                    LocalDataServiceImpl.getInstance(mApp).addGCMRequest(gcmRequest);
                }
                WebDataServiceImpl.getInstance(mApp).sendGcmRegistrationId(false);
            }
        }
    }

    public void initActionbarTitle() {
        FontAwesomeButton btSync = (FontAwesomeButton) findViewById(R.id.bt_sync);
        if (btSync != null) {
            btSync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    getDoctorsUIPermissions();
                }
            });
        }
    }

    public void refreshMenuFragment(User user) {
//        DoctorProfile doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileResponse(user.getUniqueId());
//        if (doctorProfile != null)
//            refreshMenuFragment(doctorProfile);
    }

    public void showCallConfirmationAlert(final String mobileNo) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(getResources().getString(
                R.string.confirm_call_number) + mobileNo);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobileNo));
                startActivity(callIntent);
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public void syncContacts(User user) {
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.REGISTERED_PATIENTS_DETAILS);
        WebDataServiceImpl.getInstance(mApp).getContactsList(RegisteredPatientDetailsUpdated.class, user.getUniqueId(),
                user.getForeignHospitalId(), user.getForeignLocationId(), latestUpdatedTime, this, this);

    }

    public void showAddedToQueueAlert(String firstName) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(getResources().getString(R.string.patient) + " " +
                firstName + " " + getResources().getString(R.string.patient_successfully_added_to_queue));
        alertBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    public void openAddUpdateNameDialogFragment(WebServiceType webServiceType, AddUpdateNameDialogType dialogType,
                                                Fragment fragment, User user, String uniqueId, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, uniqueId);
        if (webServiceType != null)
            bundle.putInt(HealthCocoConstants.TAG_ORDINAL_WEB_SERVICE_TYPE, webServiceType.ordinal());
        bundle.putInt(HealthCocoConstants.TAG_ORDINAL_DIALOG_TYPE, dialogType.ordinal());
        if (user != null) {
            bundle.putString(AddUpdateNameDialogFragment.TAG_DOCTOR_ID, user.getUniqueId());
            bundle.putString(AddUpdateNameDialogFragment.TAG_LOCATION_ID, user.getForeignLocationId());
            bundle.putString(AddUpdateNameDialogFragment.TAG_HOSPITAL_ID, user.getForeignHospitalId());
        }
        AddUpdateNameDialogFragment addUpdateNameDialogFragment = new AddUpdateNameDialogFragment();
        addUpdateNameDialogFragment.setArguments(bundle);
        addUpdateNameDialogFragment.setTargetFragment(fragment, requestCode);
        addUpdateNameDialogFragment.show(getSupportFragmentManager(),
                addUpdateNameDialogFragment.getClass().getSimpleName());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void doPrintAboveKitkat(String filePath) {
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        String jobName = this.getString(R.string.app_name) + " Document";
        printManager.print(jobName, new MyPrintDocumentAdapter(this, filePath), null);
    }

    private void doCloudPrinting(String filePath) {
        Intent printIntent = new Intent(this, CloudPrintActivity.class);
        printIntent.setDataAndType(Uri.fromFile(new File(filePath)), Util.getMimeTypeOfFile(filePath));
        printIntent.putExtra(CloudPrintActivity.TAG_TITLE, CloudPrintActivity.DOC_TITLE);
        startActivity(printIntent);
    }
}
