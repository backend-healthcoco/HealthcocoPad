package com.healthcoco.healthcocoplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.adapter.UIPermissionItemGridAdapter;
import com.healthcoco.healthcocoplus.bean.UserPermissionsResponse;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shreshtha on 28-02-2017.
 */
public class PrescriptionUIPermissionFragment extends HealthCocoFragment implements AdapterView.OnItemClickListener {
    private User user;
    private UserPermissionsResponse userPermissions;
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
        userPermissions = LocalDataServiceImpl.getInstance(mApp).getUserPermissions(user.getUniqueId());
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
                uiPermissionString = userPermissions.getUiPermissions().getPrescriptionPermissionsString();
                break;
            case SETTINGS_UI_PERMISSION_CLINICAL_NOTES:
                uiPermissionString = userPermissions.getUiPermissions().getClinicalNotesPermissionsString();
                break;
            case SETTINGS_UI_PERMISSION_VISITS:
                uiPermissionString = userPermissions.getUiPermissions().getPatientVisitPermissionsString();
                break;
            case SETTINGS_UI_PERMISSION_PATIENT_TAB:
                uiPermissionString = userPermissions.getUiPermissions().getTabPermissionsString();
                break;
        }
        String replace = uiPermissionString.replace("[", "");
        System.out.println(replace);
        String replace1 = replace.replace("]", "");
        System.out.println(replace1);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        System.out.println(myList.toString());
        listType = Arrays.asList(replace1.split(","));
        adapter = new UIPermissionItemGridAdapter(mActivity);
        gvSettingsUIPermission.setAdapter(adapter);
        adapter.setUIListData(listType);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
