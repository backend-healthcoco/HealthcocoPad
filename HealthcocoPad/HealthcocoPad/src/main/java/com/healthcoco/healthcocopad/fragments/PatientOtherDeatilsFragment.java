package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.GroupsGridViewAdapter;
import com.healthcoco.healthcocopad.adapter.NotesListViewAdapter;
import com.healthcoco.healthcocopad.adapter.PersonalFamilyHistoryDiseaseAdapter;
import com.healthcoco.healthcocopad.bean.Address;
import com.healthcoco.healthcocopad.bean.FileDetails;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddMedicalFamilyHistoryRequest;
import com.healthcoco.healthcocopad.bean.request.RegisterNewPatientRequest;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.BloodGroup;
import com.healthcoco.healthcocopad.bean.server.CityResponse;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MedicalFamilyHistoryDetails;
import com.healthcoco.healthcocopad.bean.server.Profession;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.ExistingPatientAutoCompleteAdapter;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.OptionsType;
import com.healthcoco.healthcocopad.enums.RecordType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.MedicalFamilyHistoryItemListener;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.BitmapUtil;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class PatientOtherDeatilsFragment extends HealthCocoFragment implements View.OnClickListener,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised,
        DownloadFileFromUrlListener, PatientRegistrationListener, MedicalFamilyHistoryItemListener {
    private static final int REQUEST_CODE_REGISTER_PATIENT = 101;
    private static final String SEPARATOR_GROUP_NOTES = "; ";
    RegisterNewPatientRequest patientDetails = new RegisterNewPatientRequest();
    PatientRegistrationDetailsListener registrationDetailsListener;
    private User user;
    private boolean isEditPatient;
    private LinearLayout loadingExistingPatientsList;
    private ExistingPatientAutoCompleteAdapter existingPatientAutotvAdapter;
    private AlreadyRegisteredPatientsResponse alreadyRegisteredPatient;
    private ArrayList<String> notesListLastAdded = new ArrayList<>();
    private TextView btAddNote;
    private ListView lvNotes;
    private GridView gvGroups;
    private NotesListViewAdapter notesListViewAdapter;
    private GroupsGridViewAdapter groupsListViewAdapter;
    private ArrayList<String> groupIdsToAssign = new ArrayList<String>();
    private TextView tvNoNotes;
    private TextView tvNoGroups;
    private GridView gvPastHistory;
    private PersonalFamilyHistoryDiseaseAdapter adapter;
    private ArrayList<Disease> diseaseList;
    private TextView tvNoDiseaseHistory;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private ArrayList<String> addPastDiseasesList = new ArrayList<String>();
    private ArrayList<String> removePastDiseasesList = new ArrayList<String>();
    private ArrayList<String> pastDiseaseIdsList = new ArrayList<String>();
    private ArrayList<String> addFamilyDiseasesList = new ArrayList<String>();
    private HistoryDetailsResponse historyDetailsResponse;

    public PatientOtherDeatilsFragment(PatientRegistrationDetailsListener registrationDetailsListener) {
        super();
        this.registrationDetailsListener = registrationDetailsListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_other_details, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
            initAdapters();
            getGroupListFromLocal();
            initData();
            initDefaultData();
        }
    }

    private void initAdapters() {
        initNoteListAdapter();
        initGroupAdapter();

        adapter = new PersonalFamilyHistoryDiseaseAdapter(mActivity, this);
        adapter.setListData(diseaseList);
        gvPastHistory.setAdapter(adapter);

    }

    private void getGroupListFromLocal() {
        LocalDataServiceImpl.getInstance(mApp).getUserGroups(WebServiceType.GET_GROUPS, null, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
    }

    private void initGroupAdapter() {
        groupsListViewAdapter = new GroupsGridViewAdapter(mActivity, this);
        gvGroups.setAdapter(groupsListViewAdapter);
    }

    public void getHistoryListFromServer(boolean showLoading) {
        try {
            if (user != null) {
                if (showLoading)
                    showLoadingOverlay(true);
                else showLoadingOverlay(false);
                Long updatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.HISTORY_DETAIL_RESPONSE);
                boolean isInHistory = false;
                if (updatedTime == 0) {
                    isInHistory = true;
                }
                WebDataServiceImpl.getInstance(mApp).getHistoryListUpdatedAPI(HistoryDetailsResponse.class, WebServiceType.GET_HISTORY_LIST, isOtpVerified(), patientDetails.getUserId(), user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                        isInHistory, updatedTime, this, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getHistoryListFromLocal() {
        if (user != null) {
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_HISTORY_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void initViews() {

        btAddNote = (TextView) view.findViewById(R.id.bt_add_note);
        lvNotes = (ListView) view.findViewById(R.id.lv_notes);
        tvNoNotes = (TextView) view.findViewById(R.id.tv_no_notes);
        tvNoGroups = (TextView) view.findViewById(R.id.tv_no_groups);
        gvGroups = (GridView) view.findViewById(R.id.gv_groups);
        loadingExistingPatientsList = (LinearLayout) view.findViewById(R.id.loading_existing_patients_list);

        gvPastHistory = (GridView) view.findViewById(R.id.gv_past_history_list);
        tvNoDiseaseHistory = (TextView) view.findViewById(R.id.tv_no_past_history);

    }

    @Override
    public void initListeners() {
//        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

        btAddNote.setOnClickListener(this);
//        editMobileNumber.addTextChangedListener(new HealthcocoTextWatcher(editMobileNumber, this));

    }

    private void initDefaultData() {
    }

    private void initNoteListAdapter() {
        notesListViewAdapter = new NotesListViewAdapter(mActivity, this);
        lvNotes.setAdapter(notesListViewAdapter);
    }


    private void initData() {
        Intent intent = mActivity.getIntent();
        String patientUniqueId = intent.getStringExtra(HealthCocoConstants.TAG_UNIQUE_ID);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    validateData();
                else
                    onNetworkUnavailable(null);
                break;
            case R.id.bt_add_note:
                mActivity.openAddUpdateNameDialogFragment(WebServiceType.LOCAL_STRING_SAVE, AddUpdateNameDialogType.LOCAL_STRING_SAVE, this, null, "", REQUEST_CODE_REGISTER_PATIENT);
                break;
        }
    }

    public void initDataFromPreviousFragment(Object object, boolean isEditPatient) {
        this.isEditPatient = isEditPatient;
        patientDetails = (RegisterNewPatientRequest) object;
        notesListLastAdded = patientDetails.getNotes();
        groupIdsToAssign = patientDetails.getGroups();

        if (!Util.isNullOrEmptyList(notesListLastAdded))
            notifyNoteListAdapter(notesListLastAdded);
        if (!Util.isNullOrEmptyList(groupIdsToAssign))
            getGroupsList();


        if (isEditPatient)
            getHistoryListFromServer(false);
        else
            getListFromLocal();

    }


    public void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        EditText selectedEditText = null;
        String name = "";

        if (Util.isNullOrBlank(msg)) {
            registerPatient(name);
        } else {
            errorViewList.add(selectedEditText);
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void registerPatient(String name) {
//        mActivity.showLoading(false);
        patientDetails.setNotes(notesListLastAdded);
        patientDetails.setGroups(groupIdsToAssign);

        addPastMedicalHistory();

        registrationDetailsListener.readyToMoveNext(patientDetails, isEditPatient);
    }

    private void addPastMedicalHistory() {

        AddMedicalFamilyHistoryRequest addPastHistoryRequest = new AddMedicalFamilyHistoryRequest();
        addPastHistoryRequest.setHospitalId(user.getForeignHospitalId());
        addPastHistoryRequest.setLocationId(user.getForeignLocationId());
        addPastHistoryRequest.setDoctorId(user.getUniqueId());
        addPastHistoryRequest.setAddDiseases(addPastDiseasesList);
        addPastHistoryRequest.setRemoveDiseases(removePastDiseasesList);
        if (isEditPatient)
            addPastHistoryRequest.setPatientId(patientDetails.getUserId());

        patientDetails.setPastMedicalHistoryHandler(addPastHistoryRequest);

        if (historyDetailsResponse != null) {
            AddMedicalFamilyHistoryRequest addFamilyHistoryRequest = new AddMedicalFamilyHistoryRequest();
            addFamilyHistoryRequest.setHospitalId(user.getForeignHospitalId());
            addFamilyHistoryRequest.setLocationId(user.getForeignLocationId());
            addFamilyHistoryRequest.setDoctorId(user.getUniqueId());
            addFamilyHistoryRequest.setAddDiseases(addFamilyDiseasesList);
            if (isEditPatient)
                addFamilyHistoryRequest.setPatientId(patientDetails.getUserId());

            patientDetails.setFamilyMedicalHistoryHandler(addFamilyHistoryRequest);

            if (isEditPatient)
                patientDetails.setPersonalHistoryAddRequest(historyDetailsResponse.getPersonalHistory());
        }
    }


    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();

            if (volleyResponseBean.getWebServiceType() != null) {
                mActivity.hideLoading();
                Util.showToast(mActivity, volleyResponseBean.getWebServiceType() + errorMsg);
                return;
            }
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
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case GET_DISEASE_LIST:
                if (response.isDataFromLocal()) {
                    diseaseList = (ArrayList<Disease>) (ArrayList<?>) response.getDataList();
                    LogUtils.LOGD(TAG, "Success onResponse diseaseList Size " + diseaseList.size() + " isDataFromLocal " + response.isDataFromLocal());
                }
                notifyAdapter(diseaseList);
                break;
            case GET_HISTORY_LIST:
                historyDetailsResponse = (HistoryDetailsResponse) response.getData();
                if (historyDetailsResponse != null)
                    initPastHistory(historyDetailsResponse.getMedicalhistory());
                getListFromLocal();
                break;
            case GET_GROUPS:
                if (response.isDataFromLocal()) {
                    ArrayList<UserGroups> groupsList = (ArrayList<UserGroups>) (ArrayList<?>) response
                            .getDataList();
                    if (!Util.isNullOrEmptyList(groupsList))
                        LogUtils.LOGD(TAG, "Success onResponse groupsList Size " + groupsList.size() + " isDataFromLocal " + response.isDataFromLocal());
                    notifyGroupListAdapter(groupsList);
                    if (!response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getGroupsList();
                        return;
                    }
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_GROUPS_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    response.setIsFromLocalAfterApiSuccess(true);
                    return;
                }
                mActivity.hideLoading();
                break;
        }
        mActivity.hideLoading();
    }

    private void getGroupsList() {
        mActivity.showLoading(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.USER_GROUP);
        WebDataServiceImpl.getInstance(mApp).getGroupsList(WebServiceType.GET_GROUPS, UserGroups.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), latestUpdatedTime, groupIdsToAssign, this, this);
    }

    private void notifyGroupListAdapter(List<UserGroups> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.groupDateComparator);
            gvGroups.setVisibility(View.VISIBLE);
            tvNoGroups.setVisibility(View.GONE);
        } else {
            gvGroups.setVisibility(View.GONE);
            tvNoGroups.setVisibility(View.VISIBLE);
        }
        groupsListViewAdapter.setListData(list);
        groupsListViewAdapter.notifyDataSetChanged();
    }

    private void notifyNoteListAdapter(ArrayList<String> notesList) {
        if (!Util.isNullOrEmptyList(notesList)) {
            lvNotes.setVisibility(View.VISIBLE);
            tvNoNotes.setVisibility(View.GONE);
        } else {
            lvNotes.setVisibility(View.GONE);
            tvNoNotes.setVisibility(View.VISIBLE);
        }
        notesListViewAdapter.setListData(notesList);
        notesListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
        } else if (requestCode == REQUEST_CODE_REGISTER_PATIENT) {
            LogUtils.LOGD(TAG, "Contacts List onActivityResult ");
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_STRING && data != null) {
                String note = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                if (!Util.isNullOrBlank(note)) {
                    if (notesListLastAdded == null)
                        notesListLastAdded = new ArrayList<>();
                    notesListLastAdded.add(note);
                    notifyNoteListAdapter(notesListLastAdded);
                }
                mActivity.hideLoading();
            }
        }
    }


    /**
     * dont add break statement after add(since we need to call get statement also after add
     *
     * @param response
     * @return
     */
    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_GROUPS_LIST:
                LocalDataServiceImpl.getInstance(mApp).addUserGroupsList((ArrayList<UserGroups>) (ArrayList<?>) response.getDataList());
            case GET_GROUPS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getUserGroups(WebServiceType.GET_GROUPS, groupIdsToAssign, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), null, null);
                break;
            case GET_DISEASE_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDiseaseList(WebServiceType.GET_DISEASE_LIST, null, BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case GET_HISTORY_LIST:
                if (isEditPatient)
                    volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getHistoryDetailResponse(WebServiceType.GET_HISTORY_LIST, BooleanTypeValues.FALSE, isOtpVerified(), user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), patientDetails.getUserId(), null, null);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            if (!Util.isNullOrBlank(filePath)) {

            }
        }
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onDeleteNotesClicked(String notes) {
        showConfirmDeleteAlert(notes);
    }

    private void showConfirmDeleteAlert(final String notes) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(getResources().getString(
                R.string.confirm_delete_note));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (notesListLastAdded.contains(notes)) {
                    notesListLastAdded.remove(notes);
                    notifyNoteListAdapter(notesListLastAdded);
                }
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

    @Override
    public void onAssignGroupCheckClicked(boolean isSelected, UserGroups group) {
        String groupId = group.getUniqueId();
        if (isSelected) {
            if (!groupIdsToAssign.contains(groupId)) {
                groupIdsToAssign.add(groupId);
            }
        } else if (groupIdsToAssign.contains(groupId)) {
            groupIdsToAssign.remove(groupId);
        }
    }

    @Override
    public boolean isGroupAssigned(String groupId) {
        return groupIdsToAssign.contains(groupId);
    }

    private void getListFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DISEASE_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void notifyAdapter(ArrayList<Disease> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.diseaseDateComparator);
            gvPastHistory.setVisibility(View.VISIBLE);
            tvNoDiseaseHistory.setVisibility(View.GONE);
            adapter.setListData(list);
            adapter.notifyDataSetChanged();
        } else {
            gvPastHistory.setVisibility(View.GONE);
            tvNoDiseaseHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAddDiseaseClicked(String diseaseId) {
        if (!addPastDiseasesList.contains(diseaseId))
            addPastDiseasesList.add(diseaseId);
        if (removePastDiseasesList.contains(diseaseId)) {
            removePastDiseasesList.remove(diseaseId);
            if (pastDiseaseIdsList.contains(diseaseId))
                pastDiseaseIdsList.remove(diseaseId);
        }
    }

    @Override
    public void onRemoveDiseaseClicked(String diseaseId) {
        if (!removePastDiseasesList.contains(diseaseId)) {
            removePastDiseasesList.add(diseaseId);
            if (pastDiseaseIdsList.contains(diseaseId))
                pastDiseaseIdsList.remove(diseaseId);
        }
        if (addPastDiseasesList.contains(diseaseId))
            addPastDiseasesList.remove(diseaseId);
    }

    @Override
    public boolean isDiseaseAdded(String uniqueId) {
        if (addPastDiseasesList.contains(uniqueId) || pastDiseaseIdsList.contains(uniqueId))
            return true;
        return false;
    }

    private void initPastHistory(List<MedicalFamilyHistoryDetails> medicalhistory) {
        pastDiseaseIdsList.clear();
        if (!Util.isNullOrEmptyList(medicalhistory)) {
            for (MedicalFamilyHistoryDetails familyHistoryDetails :
                    medicalhistory) {
                pastDiseaseIdsList.add(familyHistoryDetails.getUniqueId());
            }
        }
        addFamilyDiseasesList.clear();
        if (!Util.isNullOrEmptyList(historyDetailsResponse.getFamilyhistory())) {
            for (MedicalFamilyHistoryDetails familyHistoryDetails :
                    historyDetailsResponse.getFamilyhistory()) {
                addFamilyDiseasesList.add(familyHistoryDetails.getUniqueId());
            }
        }
    }

}
