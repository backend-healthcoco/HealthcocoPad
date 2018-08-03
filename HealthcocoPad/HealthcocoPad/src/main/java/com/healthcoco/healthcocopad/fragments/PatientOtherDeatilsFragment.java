package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.GroupsGridViewAdapter;
import com.healthcoco.healthcocopad.adapter.NotesListViewAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.RegisterNewPatientRequest;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.ExistingPatientAutoCompleteAdapter;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class PatientOtherDeatilsFragment extends HealthCocoFragment implements View.OnClickListener,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised,
        DownloadFileFromUrlListener, PatientRegistrationListener {
    private static final int REQUEST_CODE_REGISTER_PATIENT = 101;
    private static final String SEPARATOR_GROUP_NOTES = "; ";
    RegisterNewPatientRequest patientDetails = new RegisterNewPatientRequest();
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
    }

    private void getGroupListFromLocal() {
        LocalDataServiceImpl.getInstance(mApp).getUserGroups(WebServiceType.GET_GROUPS, null, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
    }

    private void initGroupAdapter() {
        groupsListViewAdapter = new GroupsGridViewAdapter(mActivity, this);
        gvGroups.setAdapter(groupsListViewAdapter);
    }

    @Override
    public void initViews() {

        btAddNote = (TextView) view.findViewById(R.id.bt_add_note);
        lvNotes = (ListView) view.findViewById(R.id.lv_notes);
        tvNoNotes = (TextView) view.findViewById(R.id.tv_no_notes);
        tvNoGroups = (TextView) view.findViewById(R.id.tv_no_groups);
        gvGroups = (GridView) view.findViewById(R.id.gv_groups);
        loadingExistingPatientsList = (LinearLayout) view.findViewById(R.id.loading_existing_patients_list);

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
        mActivity.showLoading(false);
        patientDetails.setNotes(notesListLastAdded);
        patientDetails.setGroups(groupIdsToAssign);

        if (isEditPatient) {
            WebDataServiceImpl.getInstance(mApp).updatePatient(RegisteredPatientDetailsUpdated.class, patientDetails, this, this);
        } else {
            WebDataServiceImpl.getInstance(mApp).registerNewPatient(RegisteredPatientDetailsUpdated.class, patientDetails, this, this);
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
            case UPDATE_PATIENT:
                if (response.isValidData(response) && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                    RegisteredPatientDetailsUpdated patientDetails = (RegisteredPatientDetailsUpdated) response.getData();
                    refreshContactsData(patientDetails);
                    mActivity.hideLoading();
                    //will directly refresh PatientDetailSCreen on its onActivityResult
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_REGISTRATION, new Intent().putExtra(HealthCocoConstants.TAG_PATIENT_PROFILE, Parcels.wrap(patientDetails)));
                    ((CommonOpenUpActivity) mActivity).finish();
                    return;
                }
                break;
            case REGISTER_PATIENT:
                if (response.isValidData(response) && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                    RegisteredPatientDetailsUpdated patientDetails = (RegisteredPatientDetailsUpdated) response.getData();
                    LogUtils.LOGD(TAG, "REGISTER_PATIENT SYNC_COMPLETE" + patientDetails.getLocalPatientName());
                    LocalDataServiceImpl.getInstance(mApp).addPatient(patientDetails);
                    refreshContactsData(patientDetails);
                    mActivity.hideLoading();
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_REGISTRATION);
                    ((CommonOpenUpActivity) mActivity).finish();
                    return;
                }
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

    private void refreshContactsData(RegisteredPatientDetailsUpdated patientDetails) {
        LocalDataServiceImpl.getInstance(mApp).addPatient(patientDetails);
        mActivity.syncContacts(false, user);
        Util.sendBroadcast(mApp, ContactsListFragment.INTENT_GET_CONTACT_LIST_LOCAL);
    }

    private void openPatientDetailScreen(RegisteredPatientDetailsUpdated selecetdPatient) {
        if (selecetdPatient.getPatient() != null && !Util.isNullOrBlank(selecetdPatient.getPatient().getPatientId())) {
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selecetdPatient.getUserId();
            openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_VERIFICATION, null,
                    REQUEST_CODE_REGISTER_PATIENT);
        }
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


}
