package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.UIPermissionItemGridAdapter;
import com.healthcoco.healthcocopad.bean.UiPermissionsBoth;
import com.healthcoco.healthcocopad.bean.UserPermissionsResponse;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AssignedUiPermissionsRequest;
import com.healthcoco.healthcocopad.bean.request.UserPermissionsRequest;
import com.healthcoco.healthcocopad.bean.server.AssignedUserUiPermissions;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.listeners.CommonUiPermissionsListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 28-02-2017.
 */
public class CommonUiPermissionsFragment extends HealthCocoFragment implements AdapterView.OnItemClickListener, View.OnClickListener,
        CommonUiPermissionsListener {
    public static final String TAG_BOTH_USER_UI_PERMISSIONS = "bothUserUiPermissions";
    private GridView gvSettingsUIPermission;
    private UIPermissionItemGridAdapter adapter;
    private CommonOpenUpFragmentType fragmentType;
    private UiPermissionsBoth uiPermissionsBoth;
    private ArrayList<String> assignedPermissionsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_common_ui_permissions, null);
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
        Intent intent = mActivity.getIntent();
        if (intent != null) {
            int fragmentOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, -1);
            fragmentType = CommonOpenUpFragmentType.values()[fragmentOrdinal];
            uiPermissionsBoth = Parcels.unwrap(intent.getParcelableExtra(TAG_BOTH_USER_UI_PERMISSIONS));
            if (fragmentType != null && uiPermissionsBoth != null) {
                initViews();
                initListeners();
                initAdapters();
                initData();
            }
        }
    }

    @Override
    public void initViews() {
        gvSettingsUIPermission = (GridView) view.findViewById(R.id.gv_settings_ui_permission);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
        gvSettingsUIPermission.setOnItemClickListener(this);
    }

    private void initAdapters() {
        adapter = new UIPermissionItemGridAdapter(mActivity, this);
        gvSettingsUIPermission.setAdapter(adapter);
    }

    private void initData() {
        if (uiPermissionsBoth != null && uiPermissionsBoth.getDoctorPermissions() != null) {
            ArrayList<String> list = new ArrayList<>();
            switch (fragmentType) {
                case SETTINGS_UI_PERMISSION_CLINICAL_NOTES:
                    list = uiPermissionsBoth.getAllPermissions().getClinicalNotesPermissions();
                    assignedPermissionsList = uiPermissionsBoth.getDoctorPermissions().getClinicalNotesPermissions();
                    break;
                case SETTINGS_UI_PERMISSION_PRESCRIPTION:
                    list = uiPermissionsBoth.getAllPermissions().getPrescriptionPermissions();
                    assignedPermissionsList = uiPermissionsBoth.getDoctorPermissions().getPrescriptionPermissions();
                    break;
                case SETTINGS_UI_PERMISSION_VISITS:
                    list = uiPermissionsBoth.getAllPermissions().getPatientVisitPermissions();
                    assignedPermissionsList = uiPermissionsBoth.getDoctorPermissions().getPatientVisitPermissions();
                    break;
//                case SETTINGS_UI_PERMISSION_PATIENT_TAB:
//                    list = uiPermissionsBoth.getAllPermissions().getTabPermissions();
//                    assignedPermissionsList = uiPermissionsBoth.getDoctorPermissions().getTabPermissions();
//                    break;
            }
            notifyAdapter(list);
        }
    }

    private void notifyAdapter(List<String> list) {
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                updateUiPermissions();
                break;
        }

    }

    private void updateUiPermissions() {
        try {
            mActivity.showLoading(false);
            if (uiPermissionsBoth.getAllPermissions() != null && uiPermissionsBoth.getDoctorPermissions() != null) {
                AssignedUserUiPermissions doctorPermissions = uiPermissionsBoth.getDoctorPermissions();
                AssignedUiPermissionsRequest assignedUiPermissionsRequest = new AssignedUiPermissionsRequest();
                ReflectionUtil.copy(assignedUiPermissionsRequest, doctorPermissions);
                switch (fragmentType) {
                    case SETTINGS_UI_PERMISSION_PRESCRIPTION:
                        assignedUiPermissionsRequest.setPrescriptionPermissions(assignedPermissionsList);
                        break;
                    case SETTINGS_UI_PERMISSION_CLINICAL_NOTES:
                        assignedUiPermissionsRequest.setClinicalNotesPermissions(assignedPermissionsList);
                        break;
                    case SETTINGS_UI_PERMISSION_VISITS:
                        assignedUiPermissionsRequest.setPatientVisitPermissions(assignedPermissionsList);
                        break;
                    case SETTINGS_UI_PERMISSION_PATIENT_TAB:
                        assignedUiPermissionsRequest.setTabPermissions(assignedPermissionsList);
                        break;
                }
                UserPermissionsRequest userPermissionsResponse = new UserPermissionsRequest();
                userPermissionsResponse.setDoctorId(uiPermissionsBoth.getDoctorId());
                userPermissionsResponse.setUiPermissions(assignedUiPermissionsRequest);
                WebDataServiceImpl.getInstance(mApp).updateUiPermissions(UserPermissionsResponse.class, userPermissionsResponse, this, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mActivity.hideLoading();
        }
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        super.onResponse(response);
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case POST_UI_PERMISSIONS:
                    if (response.getData() != null && response.getData() instanceof UserPermissionsResponse) {
                        UserPermissionsResponse userPermissionsResponse = (UserPermissionsResponse) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addUiPermissions(userPermissionsResponse.getDoctorId(), userPermissionsResponse.getUiPermissions());
                        Util.sendBroadcast(mApp, SettingUIPermissionsFragment.INTENT_REFRESH_UI_PERMISSIONS_FROM_LOCAL);
                        mActivity.finish();
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public boolean isAssigned(String permission) {
        return assignedPermissionsList.contains(permission);
    }

    @Override
    public void assignPermission(String permission) {
        if (!assignedPermissionsList.contains(permission))
            assignedPermissionsList.add(permission);
    }

    @Override
    public void removePermission(String permission) {
        if (assignedPermissionsList.contains(permission))
            assignedPermissionsList.remove(permission);
    }
}
