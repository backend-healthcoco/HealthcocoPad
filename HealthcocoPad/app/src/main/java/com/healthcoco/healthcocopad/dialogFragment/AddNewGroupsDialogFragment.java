package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.adapter.GroupsListViewAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AssignGroupRequest;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AssignGroupListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shreshtha on 06-03-2017.
 */
public class AddNewGroupsDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, AssignGroupListener {
    private ListView lvGroups;
    private Button btAddNewGroup;
    private GroupsListViewAdapter adapter;
    private TextView tvNoGroups;
    private List<UserGroups> groupsList = new ArrayList<UserGroups>();
    private ArrayList<String> groupIdsToAssign = new ArrayList<String>();
    private ArrayList<UserGroups> groupListToAssign = new ArrayList<>();

    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_groups_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setWidthHeight(0.50, 0.85);
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        String selectedPatientUserId = intent.getStringExtra(HealthCocoConstants.TAG_SELECTED_USER_ID);
        ArrayList<String> list = (ArrayList<String>) intent.getSerializableExtra(HealthCocoConstants.TAG_GROUP_IDS_LIST);
        if (!Util.isNullOrEmptyList(list))
            groupIdsToAssign.addAll(list);
        selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(selectedPatientUserId);
        if (selectedPatient != null && !Util.isNullOrEmptyList(selectedPatient.getGroupIds()))
            groupIdsToAssign.addAll(selectedPatient.getGroupIds());
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
            initAdapter();
            notifyAdapter(groupsList);
            getGrouspListFromLocal();
//            getGroupsListFromLocal();
        }
    }

    public void getGrouspListFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_GROUPS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getGroupsList() {
        mActivity.showLoading(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.USER_GROUP);
        WebDataServiceImpl.getInstance(mApp).getGroupsList(WebServiceType.GET_GROUPS, UserGroups.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), latestUpdatedTime, groupIdsToAssign, this, this);
    }

    @Override
    public void initViews() {
        lvGroups = (ListView) view.findViewById(R.id.lv_groups);
        tvNoGroups = (TextView) view.findViewById(R.id.tv_no_groups);
        btAddNewGroup = (Button) view.findViewById(R.id.bt_add);
    }

    @Override
    public void initListeners() {
        btAddNewGroup.setOnClickListener(this);
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.add_groups));
    }

    @Override
    public void initData() {

    }

    private void initAdapter() {
        adapter = new GroupsListViewAdapter(mActivity, this);
        lvGroups.setAdapter(adapter);
    }

    private void notifyAdapter(List<UserGroups> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.groupDateComparator);
            lvGroups.setVisibility(View.VISIBLE);
            tvNoGroups.setVisibility(View.GONE);
            adapter.setListData(list);
            adapter.notifyDataSetChanged();
        } else {
            lvGroups.setVisibility(View.GONE);
            tvNoGroups.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_NEW_GROUP, AddUpdateNameDialogType.GROUPS, this, user, "", HealthCocoConstants.REQUEST_CODE_GROUPS_LIST);
                break;
            case R.id.bt_save:
                if (selectedPatient != null) {
                    mActivity.showLoading(false);
                    AssignGroupRequest assignGroupRequest = new AssignGroupRequest();
                    assignGroupRequest.setGroupIds(groupIdsToAssign);
                    if (selectedPatient != null)
                        assignGroupRequest.setPatientId(selectedPatient.getUserId());
                    assignGroupRequest.setDoctorId(user.getUniqueId());
                    assignGroupRequest.setLocationId(user.getForeignLocationId());
                    assignGroupRequest.setHospitalId(user.getForeignHospitalId());
                    WebDataServiceImpl.getInstance(mApp).assignGroup(AssignGroupRequest.class, assignGroupRequest, this, this);
                }
                getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_GROUPS_LIST, new Intent().putExtra(HealthCocoConstants.TAG_GROUP_IDS_LIST, groupIdsToAssign));
                dismiss();
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case GET_GROUPS:
                if (response.isDataFromLocal()) {
                    groupsList = (ArrayList<UserGroups>) (ArrayList<?>) response
                            .getDataList();
                    if (!Util.isNullOrEmptyList(groupsList))
                        LogUtils.LOGD(TAG, "Success onResponse groupsList Size " + groupsList.size() + " isDataFromLocal " + response.isDataFromLocal());
                    notifyAdapter(groupsList);
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
            case ASSIGN_GROUP:
                selectedPatient.setGroups(groupListToAssign);
                LocalDataServiceImpl.getInstance(mApp).addPatient(selectedPatient);
                getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_GROUPS_LIST, new Intent());
                dismiss();
                mActivity.syncContacts(user);
                break;
            default:
                break;
        }
        mActivity.hideLoading();
    }


    @Override
    public void onAssignGroupCheckClicked(boolean isSelected, UserGroups group) {
        String groupId = group.getUniqueId();
        if (isSelected) {
            if (!groupIdsToAssign.contains(groupId)) {
                groupListToAssign.add(group);
                groupIdsToAssign.add(groupId);
            }
        } else if (groupIdsToAssign.contains(groupId)) {
            groupIdsToAssign.remove(groupId);
            groupListToAssign.remove(group);
        }
    }

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
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_GROUPS_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_GROUP) {
                getGrouspListFromLocal();
            }
        }
    }
}
