package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.SettingsUIPermissionListAdapter;
import com.healthcoco.healthcocopad.bean.UiPermissionsBoth;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.UIPermissionsItemType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;

import static com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType.SETTINGS_UI_PERMISSION_CLINICAL_NOTES;
import static com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType.SETTINGS_UI_PERMISSION_PRESCRIPTION;
import static com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType.SETTINGS_UI_PERMISSION_VISITS;
import static com.healthcoco.healthcocopad.enums.UIPermissionsItemType.PATIENT_TAB_PERMISSION;

/**
 * Created by Shreshtha on 27-02-2017.
 */
public class SettingUIPermissionsFragment extends HealthCocoFragment implements AdapterView.OnItemClickListener, LocalDoInBackgroundListenerOptimised, View.OnClickListener {
    public static final String INTENT_REFRESH_UI_PERMISSIONS_FROM_LOCAL = "com.healthcoco.REFRESH_UI_PERMISSIONS_FROM_LOCAL";
    BroadcastReceiver refreshUiPermissionsLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getBothUserUIPermissionsFromLocal();
        }
    };
    private ListView lvSettingsUIPermission;
    private SettingsUIPermissionListAdapter adapter;
    private List<UIPermissionsItemType> permissionTypesList;
    private User user;
    private UiPermissionsBoth uiPermissionsBoth;
    private boolean isInitialLoading = true;
    private boolean receiversRegistered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_ui_permission, null);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            LogUtils.LOGD(TAG, "onResume " + receiversRegistered);
            //receiver ro refresh ui permissions from local
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_REFRESH_UI_PERMISSIONS_FROM_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshUiPermissionsLocalReceiver, filter);
            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshUiPermissionsLocalReceiver);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapters();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        lvSettingsUIPermission = (ListView) view.findViewById(R.id.lv_settings_ui_permission);
    }

    @Override
    public void initListeners() {
        lvSettingsUIPermission.setOnItemClickListener(this);
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initAdapters() {
        permissionTypesList = Arrays.asList(UIPermissionsItemType.values());
        adapter = new SettingsUIPermissionListAdapter(mActivity);
        lvSettingsUIPermission.setAdapter(adapter);
        adapter.setUIListData(permissionTypesList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIPermissionsItemType itemType = permissionTypesList.get(position);
        switch (itemType) {
//            case PRESCRIPTION_UI_PERMISSION:
//                openCommonOpenUpActivity(SETTINGS_UI_PERMISSION_PRESCRIPTION, itemType.ordinal());
//                break;
            case CLINICAL_NOTES:
                openCommonOpenUpActivity(SETTINGS_UI_PERMISSION_CLINICAL_NOTES, itemType.ordinal());
                break;
//            case VISITS:
//                openCommonOpenUpActivity(SETTINGS_UI_PERMISSION_VISITS, itemType.ordinal());
//                break;
            case PATIENT_TAB_PERMISSION:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_UI_PERMISSION_PATIENT_TAB, PATIENT_TAB_PERMISSION.ordinal());
//                mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PASSWORD);
                break;
        }
    }

    private void openCommonOpenUpActivity(CommonOpenUpFragmentType fragmentType, int typeOrdinal) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        intent.putExtra(CommonUiPermissionsFragment.TAG_BOTH_USER_UI_PERMISSIONS, Parcels.wrap(uiPermissionsBoth));
        intent.putExtra(HealthCocoConstants.TAG_NAME_EDIT_TYPE, typeOrdinal);
        startActivity(intent);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        super.onResponse(response);
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    getBothUserUIPermissionsFromLocal();
                    return;
                case GET_BOTH_PERMISSIONS_FOR_DOCTOR:
                    uiPermissionsBoth = (UiPermissionsBoth) response.getData();
                    if (isInitialLoading && response.isDataFromLocal() && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getBothUserUIPermissionsFromServer(true);
                        return;
                    } else if (uiPermissionsBoth != null) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_BOTH_USER_UI_PERMISSIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    }
                    isInitialLoading = false;
                    break;
            }
        }

        mActivity.hideLoading();
    }

    private void getBothUserUIPermissionsFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_BOTH_USER_UI_PERMISSIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void getBothUserUIPermissionsFromServer(boolean showLoading) {
        if (showLoading)
            mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getBothUIPermissionsForDoctor(UiPermissionsBoth.class, user.getUniqueId(), this, this);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                }
                break;
            case ADD_BOTH_USER_UI_PERMISSIONS:
                LocalDataServiceImpl.getInstance(mApp).addBothUserUiPermissions((UiPermissionsBoth) response.getData());
                break;
            case GET_BOTH_USER_UI_PERMISSIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getBothUserUiPermissions(WebServiceType.GET_BOTH_PERMISSIONS_FOR_DOCTOR, user.getUniqueId(), null, null);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    getBothUserUIPermissionsFromServer(true);
                else
                    onNetworkUnavailable(null);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_REFERENCE_LIST) {
                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_UI_PERMISSION_PATIENT_TAB, PATIENT_TAB_PERMISSION.ordinal());
            }
        }
    }

}
