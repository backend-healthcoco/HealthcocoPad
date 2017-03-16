package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.SettingsUIPermissionListAdapter;
import com.healthcoco.healthcocopad.bean.UserPermissionsResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.UIPermissionsItemType;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;

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
