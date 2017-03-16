package com.healthcoco.healthcocopad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.adapter.UIPermissionItemGridAdapter;
import com.healthcoco.healthcocopad.bean.server.AllUIPermission;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shreshtha on 28-02-2017.
 */
public class PrescriptionUIPermissionFragment extends HealthCocoFragment implements AdapterView.OnItemClickListener {
    private User user;
    private AllUIPermission allUIPermission;
    private GridView gvSettingsUIPermission;
    private List<String> listType;
    private UIPermissionItemGridAdapter adapter;
    private CommonOpenUpFragmentType fragmentType;
    private String uiPermissionString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ui_permission_prescription, null);
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
        int fragmentOrdinal = getArguments().getInt(HealthCocoConstants.TAG_FRAGMENT_NAME);
        fragmentType = CommonOpenUpFragmentType.values()[fragmentOrdinal];
        initViews();
        initListeners();
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
        }
        getUIPermissions();
        initAdapters();
    }

    private void getUIPermissions() {
        allUIPermission = LocalDataServiceImpl.getInstance(mApp).getAllUIPermissions();
    }

    @Override
    public void initViews() {
        gvSettingsUIPermission = (GridView) view.findViewById(R.id.gv_settings_ui_permission);
    }

    @Override
    public void initListeners() {
        gvSettingsUIPermission.setOnItemClickListener(this);
    }

    private void initAdapters() {
        switch (fragmentType) {
            case SETTINGS_UI_PERMISSION_PRESCRIPTION:
                uiPermissionString = allUIPermission.getPrescriptionPermissionsString();
                break;
            case SETTINGS_UI_PERMISSION_CLINICAL_NOTES:
                uiPermissionString = allUIPermission.getClinicalNotesPermissionsString();
                break;
            case SETTINGS_UI_PERMISSION_VISITS:
                uiPermissionString = allUIPermission.getPatientVisitPermissionsString();
                break;
            case SETTINGS_UI_PERMISSION_PATIENT_TAB:
                uiPermissionString = allUIPermission.getTabPermissionsString();
                break;
        }
        listType = Arrays.asList(Util.getConvertedStringArray(uiPermissionString));
        adapter = new UIPermissionItemGridAdapter(mActivity);
        gvSettingsUIPermission.setAdapter(adapter);
        adapter.setUIListData(listType);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
