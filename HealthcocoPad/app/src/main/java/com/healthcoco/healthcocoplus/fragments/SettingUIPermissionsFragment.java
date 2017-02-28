package com.healthcoco.healthcocoplus.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocoplus.adapter.SettingsUIPermissionListAdapter;
import com.healthcoco.healthcocoplus.bean.UserPermissionsResponse;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.UIPermissionsItemType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shreshtha on 27-02-2017.
 */
public class SettingUIPermissionsFragment extends HealthCocoFragment implements AdapterView.OnItemClickListener {
    private ListView lvSettingsUIPermission;
    private SettingsUIPermissionListAdapter adapter;
    private List<UIPermissionsItemType> listType;
    private User user;
    private UserPermissionsResponse permissionsResponse;

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
    public void init() {
        initViews();
        initListeners();
        initAdapters();
//        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
//        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
//            User user = doctor.getUser();
//            init();
//            mActivity.initDefaultData(user);
//        }
    }

    @Override
    public void initViews() {
        lvSettingsUIPermission = (ListView) view.findViewById(R.id.lv_settings_ui_permission);
    }

    @Override
    public void initListeners() {
        lvSettingsUIPermission.setOnItemClickListener(this);
        mActivity.initActionbarTitle();
    }

    private void initAdapters() {
        listType = Arrays.asList(UIPermissionsItemType.values());
        adapter = new SettingsUIPermissionListAdapter(mActivity);
        lvSettingsUIPermission.setAdapter(adapter);
        adapter.setUIListData(listType);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIPermissionsItemType itemType = listType.get(position);
        switch (itemType) {
            case PRESCRIPTION_UI_PERMISSION:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_UI_PERMISSION_PRESCRIPTION, itemType.ordinal());
                break;
            case CLINICAL_NOTES:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_UI_PERMISSION_CLINICAL_NOTES, itemType.ordinal());
                break;
            case VISITS:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_UI_PERMISSION_VISITS, itemType.ordinal());
                break;
            case PATIENT_TAB_PERMISSION:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_UI_PERMISSION_PATIENT_TAB, itemType.ordinal());
                break;
        }
    }

    private void openCommonOpenUpActivity(CommonOpenUpFragmentType fragmentType, int typeOrdinal) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_NAME_EDIT_TYPE, typeOrdinal);
        startActivity(intent);
    }
}
