package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AssignedUserUiPermissions;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ClinicalNotesPermissionType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType.GET_VISIT_DETAILS;
import static com.healthcoco.healthcocopad.fragments.AddVisitSuggestionsFragment.TAG_SUGGESTIONS_TYPE;

/**
 * Created by Shreshtha on 16-05-2017.
 */

public class AddEditNormalVisitClinicalNotesFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised, View.OnTouchListener {
    private User user;
    private LinearLayout containerSuggestionsList;
    private AddClinicalNotesVisitFragment addClinicalNotesFragment;
    private LinearLayout parentClinicalNote;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private String visitId;
    public static final String TAG_VISIT_ID = "visitId";
    private AddVisitSuggestionsFragment addVisitSuggestionsFragment;
    private View selectedViewForSuggestionsList;
    private SuggestionType selectedSuggestionType = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_normal_clinical_notes_visit, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = mActivity.getIntent();
        if (intent != null) {
            visitId = Parcels.unwrap(intent.getParcelableExtra(TAG_VISIT_ID));
//            Parcelable isFromCloneParcelable = intent.getParcelableExtra(TAG_IS_FROM_CLONE);
//            if (isFromCloneParcelable != null)
//                isFromClone = Parcels.unwrap(isFromCloneParcelable);
        }
        init();
//        Bundle bundle = getArguments();
//        if (bundle != null && bundle.containsKey(TAG_USER))
//            user = Parcels.unwrap(bundle.getParcelable(TAG_USER));
//        String visitToggleStateFromPreferences = Util.getVisitToggleStateFromPreferences(mActivity);
//        if (visitToggleStateFromPreferences != null) {
//            switch (visitToggleStateFromPreferences) {
//                case PatientVisitDetailFragment.MYSCRIPT_VISIT_TOGGLE_STATE:
//                    break;
//            }
        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initSuggestionsFragment();
        initClinicalNotesFragment();
    }

    private void initData() {
        initUiPermissions(user.getUiPermissions());
    }

    private void initUiPermissions(AssignedUserUiPermissions uiPermissions) {
        addClinicalNotesFragment.initUiPermissions(uiPermissions.getClinicalNotesPermissionsString());
    }

    @Override
    public void initViews() {
        parentClinicalNote = (LinearLayout) view.findViewById(R.id.parent_clinical_note);
        containerSuggestionsList = (LinearLayout) view.findViewById(R.id.container_suggestions_list);
    }

    @Override
    public void initListeners() {

    }

    private void initClinicalNotesFragment() {
        addClinicalNotesFragment = new AddClinicalNotesVisitFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.parent_clinical_note, addClinicalNotesFragment, addClinicalNotesFragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void initSuggestionsFragment() {
        addVisitSuggestionsFragment = new AddVisitSuggestionsFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.container_suggestions_list, addVisitSuggestionsFragment, addVisitSuggestionsFragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void prePopulateVisitDetails(VisitDetails visitDetails) {
        if (!Util.isNullOrEmptyList(visitDetails.getVisitedFor())) {
            for (VisitedForType visitedForType :
                    visitDetails.getVisitedFor()) {
                switch (visitedForType) {
                    case CLINICAL_NOTES:
                        if (!Util.isNullOrEmptyList(visitDetails.getClinicalNotes())) {
                            for (ClinicalNotes clinicalNotes :
                                    visitDetails.getClinicalNotes()) {
                                parentClinicalNote.setVisibility(View.VISIBLE);
                                addClinicalNotesFragment.refreshData(clinicalNotes);
//                                if (!isFromClone)
//                                    clinicalNoteId = clinicalNotes.getUniqueId();
                            }
                        }
                        parentClinicalNote.setVisibility(View.GONE);
                        break;
                }
            }
        }
    }

    public View.OnTouchListener getOnTouchListener() {
        return this;
    }

    private void refreshSuggestionsList(View v, String searchTerm) {
        selectedViewForSuggestionsList = v;
        Object tag = v.getTag();
        if (tag != null) {
            if (tag instanceof String) {
                ClinicalNotesPermissionType permissionType = ClinicalNotesPermissionType.getClinicalNotesPermissionType((String) tag);
                if (permissionType != null) {
                    switch (permissionType) {
                        case PRESENT_COMPLAINT:
                            selectedSuggestionType = SuggestionType.PRESENT_COMPLAINT;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case COMPLAINT:
                            selectedSuggestionType = SuggestionType.COMPLAINTS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case OBSERVATION:
                            selectedSuggestionType = SuggestionType.OBSERVATION;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case INVESTIGATIONS:
                            selectedSuggestionType = SuggestionType.INVESTIGATION;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case DIAGNOSIS:
                            selectedSuggestionType = SuggestionType.DIAGNOSIS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case HISTORY_OF_PRESENT_COMPLAINT:
                            selectedSuggestionType = SuggestionType.HISTORY_OF_PRESENT_COMPLAINT;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case MENSTRUAL_HISTORY:
                            selectedSuggestionType = SuggestionType.MENSTRUAL_HISTORY;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case OBSTETRIC_HISTORY:
                            selectedSuggestionType = SuggestionType.OBSTETRIC_HISTORY;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case GENERAL_EXAMINATION:
                            selectedSuggestionType = SuggestionType.GENERAL_EXAMINATION;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case SYSTEMIC_EXAMINATION:
                            selectedSuggestionType = SuggestionType.SYSTEMIC_EXAMINATION;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case PROVISIONAL_DIAGNOSIS:
                            selectedSuggestionType = SuggestionType.PROVISIONAL_DIAGNOSIS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case NOTES:
                            selectedSuggestionType = SuggestionType.NOTES;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case ECG_DETAILS:
                            selectedSuggestionType = SuggestionType.ECG_DETAILS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case ECHO:
                            selectedSuggestionType = SuggestionType.ECHO;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case X_RAY_DETAILS:
                            selectedSuggestionType = SuggestionType.X_RAY_DETAILS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case HOLTER:
                            selectedSuggestionType = SuggestionType.HOLTER;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case PA:
                            selectedSuggestionType = SuggestionType.PA;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case PV:
                            selectedSuggestionType = SuggestionType.PV;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case PS:
                            selectedSuggestionType = SuggestionType.PS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case INDICATION_OF_USG:
                            selectedSuggestionType = SuggestionType.INDICATION_OF_USG;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        default:
                            selectedSuggestionType = null;
                    }
                }
            } else if (tag instanceof SuggestionType)
                selectedSuggestionType = (SuggestionType) tag;
            else
                selectedSuggestionType = null;
            if (selectedSuggestionType != null) {
                containerSuggestionsList.setVisibility(View.VISIBLE);
                try {
                    Intent intent = new Intent(AddVisitSuggestionsFragment.INTENT_LOAD_DATA);
                    intent.putExtra(AddVisitSuggestionsFragment.TAG_SEARCHED_TERM, searchTerm);
                    intent.putExtra(TAG_SUGGESTIONS_TYPE, selectedSuggestionType.ordinal());
                    LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        containerSuggestionsList.setVisibility(View.GONE);
    }

    private String getLastTextAfterCharacterToBeReplaced(String searchTerm) {
        Pattern p = Pattern.compile(".*" + AddClinicalNotesVisitFragment.CHARACTER_TO_BE_REPLACED + "\\s*(.*)");
        Matcher m = p.matcher(searchTerm.trim());

        if (m.find())
            return m.group(1);
        return searchTerm;
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
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()) && selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getUserId())) {
                    user = doctor.getUser();
                }
                break;
            case GET_VISIT_DETAILS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getVisitDetailResponse(WebServiceType.GET_PATIENT_VISIT_DETAIL, visitId, null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        super.onResponse(response);
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    initData();
                    if (!Util.isNullOrBlank(visitId)) {
                        new LocalDataBackgroundtaskOptimised(mActivity, GET_VISIT_DETAILS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        return;
                    }
                    break;
                case ADD_VISIT:
                    if (response.getData() != null && response.getData() instanceof VisitDetails) {
                        VisitDetails visit = (VisitDetails) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addVisit(visit);
                        Util.sendBroadcast(mApp, PatientVisitDetailFragment.INTENT_GET_VISITS_LIST_FROM_LOCAL);
                        Intent intent = new Intent();
                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_VISIT, intent);
                    }
                    mActivity.finish();
                    break;
                case GET_PATIENT_VISIT_DETAIL:
                    if (response.getData() != null && response.getData() instanceof VisitDetails) {
                        VisitDetails visit = (VisitDetails) response.getData();
                        visit.setSelectedPatient(selectedPatient);
                        prePopulateVisitDetails(visit);
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                refreshSuggestionsList(v, "");
                LogUtils.LOGD(TAG, "Action UP");
                break;
            case MotionEvent.ACTION_DOWN:
                LogUtils.LOGD(TAG, "Action DOWN");
                break;
        }
        return false;
    }
}
