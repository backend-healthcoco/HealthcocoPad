package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.ClinicalNoteToSend;
import com.healthcoco.healthcocopad.bean.server.AdviceSuggestion;
import com.healthcoco.healthcocopad.bean.server.AssignedUserUiPermissions;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.EcgDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.EchoSuggestions;
import com.healthcoco.healthcocopad.bean.server.GeneralExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.HistoryPresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.HolterSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndicationOfUsgSuggestions;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MenstrualHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.NotesSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObstetricHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.PaSuggestions;
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProvisionalDiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.PsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PvSuggestions;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.bean.server.VitalSigns;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ClinicalNotesPermissionType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.VisitIdType;
import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType.GET_VISIT_DETAILS;
import static com.healthcoco.healthcocopad.fragments.AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_BE_REPLACED;
import static com.healthcoco.healthcocopad.fragments.AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
import static com.healthcoco.healthcocopad.fragments.AddVisitSuggestionsFragment.TAG_SUGGESTIONS_TYPE;
import static com.healthcoco.healthcocopad.fragments.MyScriptAddVisitsFragment.TAG_SELECTED_SUGGESTION_OBJECT;

/**
 * Created by Shreshtha on 16-05-2017.
 */

public class AddEditNormalVisitClinicalNotesFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised, View.OnTouchListener, View.OnFocusChangeListener, View.OnClickListener, TextWatcher, HealthcocoTextWatcherListener {
    public static final String INTENT_ON_SUGGESTION_ITEM_CLICK = "com.healthcoco.healthcocopad.fragments.AddEditNormalVisitClinicalNotesFragment.ON_SUGGESTION_ITEM_CLICK";
    private User user;
    private LinearLayout containerSuggestionsList;
    private LinearLayout parentClinicalNote;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private String visitId;
    public static final String TAG_VISIT_ID = "visitId";
    private AddVisitSuggestionsFragment addVisitSuggestionsFragment;
    private AddClinicalNotesVisitFragment addClinicalNotesFragment;
    private View selectedViewForSuggestionsList;
    private SuggestionType selectedSuggestionType = null;
    private String clinicalNoteId;
    private boolean receiversRegistered;
    private boolean isOnItemClick;

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
//            clinicalNoteId = intent.getStringExtra(TAG_CLINICAL_NOTE_ID);
//            Parcelable isFromCloneParcelable = intent.getParcelableExtra(TAG_IS_FROM_CLONE);
//            if (isFromCloneParcelable != null)
//                isFromClone = Parcels.unwrap(isFromCloneParcelable);
        }
//        Bundle bundle = getArguments();
//        if (bundle != null && bundle.containsKey(TAG_USER))
//            user = Parcels.unwrap(bundle.getParcelable(TAG_USER));
//        String visitToggleStateFromPreferences = Util.getVisitToggleStateFromPreferences(mActivity);
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
        ((CommonOpenUpActivity) mActivity).initSaveButton(this);
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

    public View.OnFocusChangeListener getOnFocusChangeListener() {
        return this;
    }

    public void refreshSuggestionsList(View v, String searchTerm) {
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
                        case ECG:
                            selectedSuggestionType = SuggestionType.ECG_DETAILS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case ECHO:
                            selectedSuggestionType = SuggestionType.ECHO;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case XRAY:
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
        Pattern p = Pattern.compile(".*" + CHARACTER_TO_BE_REPLACED + "\\s*(.*)");
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
//            case GET_VISIT_DETAILS:
//                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getVisitDetailResponse(WebServiceType.GET_PATIENT_VISIT_DETAIL, visitId, null, null);
//                break;
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
//                case ADD_VISIT:
//                    if (response.getData() != null && response.getData() instanceof VisitDetails) {
//                        VisitDetails visit = (VisitDetails) response.getData();
//                        LocalDataServiceImpl.getInstance(mApp).addVisit(visit);
//                        Util.sendBroadcast(mApp, PatientVisitDetailFragment.INTENT_GET_VISITS_LIST_FROM_LOCAL);
//                        Intent intent = new Intent();
//                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_VISIT, intent);
//                    }
//                    mActivity.finish();
//                    break;
//                case GET_PATIENT_VISIT_DETAIL:
//                    if (response.getData() != null && response.getData() instanceof VisitDetails) {
//                        VisitDetails visit = (VisitDetails) response.getData();
//                        visit.setSelectedPatient(selectedPatient);
//                        prePopulateVisitDetails(visit);
//                    }
//                    break;
                case ADD_CLINICAL_NOTES:
                    if (response.isValidData(response)) {
                        ClinicalNotes clinicalNote = (ClinicalNotes) response.getData();
                        Util.setVisitId(VisitIdType.CLINICAL_NOTES, clinicalNote.getVisitId());
                        LocalDataServiceImpl.getInstance(mApp).addClinicalNote(clinicalNote);
//                        sendBroadcasts(clinicalNote.getUniqueId());
                        ((CommonOpenUpActivity) mActivity).finish();
                    }
                    break;
                case UPDATE_CLINICAL_NOTE:
                    if (response.isValidData(response)) {
                        ClinicalNotes clinicalNote = (ClinicalNotes) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addClinicalNote(clinicalNote);
//                        sendBroadcasts(clinicalNote.getUniqueId());
//                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_CLINICAL_NOTE, null);
                        ((CommonOpenUpActivity) mActivity).finish();
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
                LogUtils.LOGD(TAG, "Action UP");
                break;
            case MotionEvent.ACTION_DOWN:
                requestFocus(v);
                LogUtils.LOGD(TAG, "Action DOWN");
                break;
        }
        return false;
    }

    public void requestFocus(View v) {
        refreshSuggestionsList(v, "");
        if (selectedSuggestionType != null)
            addVisitSuggestionsFragment.refreshTagOfEditText(selectedSuggestionType);
    }

    /**
     * Gets text before last occurance of CHARACTER_TO_BE_REPLACED in searchedTerm
     *
     * @param searchTerm
     * @return : text Before  last occurance of CHARACTER_TO_BE_REPLACED
     */
    public String getTextBeforeLastOccuranceOfCharacter(String searchTerm) {
        Pattern p = Pattern.compile("\\s*(.*)" + CHARACTER_TO_BE_REPLACED + ".*");
        Matcher m = p.matcher(searchTerm.trim());

        if (m.find())
            return m.group(1);
        return "";
    }

    public int getBlankClinicalNoteMsgId() {
        return addClinicalNotesFragment.isBlankClinicalNote();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
//        if (hasFocus) {
//            if (v instanceof EditText) {
//                refreshSuggestionsList(v, "");
//                if (selectedSuggestionType != null)
//                    addVisitSuggestionsFragment.refreshTagOfEditText(selectedSuggestionType);
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
        }
    }

    private void validateData() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mActivity.showLoading(false);
            }

            @Override
            protected Integer doInBackground(Void... params) {
                return getBlankClinicalNoteMsgId();
            }

            @Override
            protected void onPostExecute(Integer msgId) {
                super.onPostExecute(msgId);
                if (msgId == 0) {
                    addNote();
                } else {
                    mActivity.hideLoading();
                    Util.showToast(mActivity, msgId);
                }
            }
        }.execute();
    }

    private void addNote() {
        ClinicalNoteToSend clinicalNotes = addClinicalNotesFragment.getClinicalNoteToSendDetails();
        if (!Util.isNullOrBlank(clinicalNoteId)) {
            clinicalNotes.setUniqueId(clinicalNoteId);
            WebDataServiceImpl.getInstance(mApp).updateClinicalNote(ClinicalNotes.class, clinicalNotes, this, this);
        } else {
            clinicalNotes.setVisitId(Util.getVisitId(VisitIdType.CLINICAL_NOTES));
            WebDataServiceImpl.getInstance(mApp).addClinicalNote(ClinicalNotes.class, clinicalNotes, this, this);
        }
    }

    public ClinicalNoteToSend getClinicalNoteToSendDetails() {
        ClinicalNoteToSend clinicalNotes = addClinicalNotesFragment.getClinicalNoteToSendDetails();
        return clinicalNotes;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            LogUtils.LOGD(TAG, "onResume " + receiversRegistered);

            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_ON_SUGGESTION_ITEM_CLICK);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(onSuggestionItemClickReceiver, filter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onSuggestionItemClickReceiver);
    }

    BroadcastReceiver onSuggestionItemClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(TAG_SUGGESTIONS_TYPE) && intent.hasExtra(TAG_SELECTED_SUGGESTION_OBJECT)) {
                int ordinal = intent.getIntExtra(TAG_SUGGESTIONS_TYPE, -1);
                SuggestionType suggestionType = SuggestionType.values()[ordinal];
                Object selectedSuggestionObject = Parcels.unwrap(intent.getParcelableExtra(TAG_SELECTED_SUGGESTION_OBJECT));
                if (suggestionType != null && selectedSuggestionObject != null) {
                    handleSelectedSugestionObject(suggestionType, selectedSuggestionObject);
                }
            }
        }
    };

    private void handleSelectedSugestionObject(SuggestionType suggestionType, Object selectedSuggestionObject) {
        String text = "";
        switch (suggestionType) {
            case PRESENT_COMPLAINT:
                if (selectedSuggestionObject instanceof PresentComplaintSuggestions) {
                    PresentComplaintSuggestions complaint = (PresentComplaintSuggestions) selectedSuggestionObject;
                    text = complaint.getPresentComplaint() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case COMPLAINTS:
                if (selectedSuggestionObject instanceof ComplaintSuggestions) {
                    ComplaintSuggestions complaint = (ComplaintSuggestions) selectedSuggestionObject;
                    text = complaint.getComplaint() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case OBSERVATION:
                if (selectedSuggestionObject instanceof ObservationSuggestions) {
                    ObservationSuggestions observation = (ObservationSuggestions) selectedSuggestionObject;
                    text = observation.getObservation() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case INVESTIGATION:
                if (selectedSuggestionObject instanceof InvestigationSuggestions) {
                    InvestigationSuggestions investigation = (InvestigationSuggestions) selectedSuggestionObject;
                    text = investigation.getInvestigation() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case DIAGNOSIS:
                if (selectedSuggestionObject instanceof DiagnosisSuggestions) {
                    DiagnosisSuggestions diagnosis = (DiagnosisSuggestions) selectedSuggestionObject;
                    text = diagnosis.getDiagnosis() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case HISTORY_OF_PRESENT_COMPLAINT:
                if (selectedSuggestionObject instanceof HistoryPresentComplaintSuggestions) {
                    HistoryPresentComplaintSuggestions complaint = (HistoryPresentComplaintSuggestions) selectedSuggestionObject;
                    text = complaint.getPresentComplaintHistory() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case MENSTRUAL_HISTORY:
                if (selectedSuggestionObject instanceof MenstrualHistorySuggestions) {
                    MenstrualHistorySuggestions complaint = (MenstrualHistorySuggestions) selectedSuggestionObject;
                    text = complaint.getMenstrualHistory() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case OBSTETRIC_HISTORY:
                if (selectedSuggestionObject instanceof ObstetricHistorySuggestions) {
                    ObstetricHistorySuggestions complaint = (ObstetricHistorySuggestions) selectedSuggestionObject;
                    text = complaint.getObstetricHistory() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case GENERAL_EXAMINATION:
                if (selectedSuggestionObject instanceof GeneralExaminationSuggestions) {
                    GeneralExaminationSuggestions complaint = (GeneralExaminationSuggestions) selectedSuggestionObject;
                    text = complaint.getGeneralExam() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case SYSTEMIC_EXAMINATION:
                if (selectedSuggestionObject instanceof SystemicExaminationSuggestions) {
                    SystemicExaminationSuggestions complaint = (SystemicExaminationSuggestions) selectedSuggestionObject;
                    text = complaint.getSystemExam() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case PROVISIONAL_DIAGNOSIS:
                if (selectedSuggestionObject instanceof ProvisionalDiagnosisSuggestions) {
                    ProvisionalDiagnosisSuggestions complaint = (ProvisionalDiagnosisSuggestions) selectedSuggestionObject;
                    text = complaint.getProvisionalDiagnosis() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case NOTES:
                if (selectedSuggestionObject instanceof NotesSuggestions) {
                    NotesSuggestions complaint = (NotesSuggestions) selectedSuggestionObject;
                    text = complaint.getNote() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case ECG_DETAILS:
                if (selectedSuggestionObject instanceof EcgDetailSuggestions) {
                    EcgDetailSuggestions complaint = (EcgDetailSuggestions) selectedSuggestionObject;
                    text = complaint.getEcgDetails() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case ECHO:
                if (selectedSuggestionObject instanceof EchoSuggestions) {
                    EchoSuggestions complaint = (EchoSuggestions) selectedSuggestionObject;
                    text = complaint.getEcho() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case X_RAY_DETAILS:
                if (selectedSuggestionObject instanceof XrayDetailSuggestions) {
                    XrayDetailSuggestions complaint = (XrayDetailSuggestions) selectedSuggestionObject;
                    text = complaint.getxRayDetails() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case HOLTER:
                if (selectedSuggestionObject instanceof HolterSuggestions) {
                    HolterSuggestions complaint = (HolterSuggestions) selectedSuggestionObject;
                    text = complaint.getHolter() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case PA:
                if (selectedSuggestionObject instanceof PaSuggestions) {
                    PaSuggestions complaint = (PaSuggestions) selectedSuggestionObject;
                    text = complaint.getPa() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case PV:
                if (selectedSuggestionObject instanceof PvSuggestions) {
                    PvSuggestions complaint = (PvSuggestions) selectedSuggestionObject;
                    text = complaint.getPv() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case PS:
                if (selectedSuggestionObject instanceof PsSuggestions) {
                    PsSuggestions complaint = (PsSuggestions) selectedSuggestionObject;
                    text = complaint.getPs() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case INDICATION_OF_USG:
                if (selectedSuggestionObject instanceof IndicationOfUsgSuggestions) {
                    IndicationOfUsgSuggestions complaint = (IndicationOfUsgSuggestions) selectedSuggestionObject;
                    text = complaint.getIndicationOfUSG() + AddClinicalNotesVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
        }

        if (selectedViewForSuggestionsList != null && selectedViewForSuggestionsList instanceof EditText && !Util.isNullOrBlank(text)) {
            EditText editText = ((EditText) selectedViewForSuggestionsList);
            isOnItemClick = true;
            String textBeforeComma = getTextBeforeLastOccuranceOfCharacter(Util.getValidatedValueOrBlankWithoutTrimming(editText));
            if (!Util.isNullOrBlank(textBeforeComma))
                textBeforeComma = textBeforeComma + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
            editText.setText(textBeforeComma + text);
            editText.setSelection(Util.getValidatedValueOrBlankTrimming(editText).length());
        }
    }


    public TextWatcher addTextChangedListener(EditText autotvPermission) {
        return new HealthcocoTextWatcher(autotvPermission, this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void afterTextChange(View v, String s) {
        if (v instanceof EditText) {
            refreshSuggestionsList(v, s);
        }
    }
}