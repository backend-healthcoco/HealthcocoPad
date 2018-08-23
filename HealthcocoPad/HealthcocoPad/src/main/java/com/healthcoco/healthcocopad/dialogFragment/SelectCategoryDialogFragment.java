package com.healthcoco.healthcocopad.dialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
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
import com.healthcoco.healthcocopad.enums.VideoCategory;
import com.healthcoco.healthcocopad.fragments.SettingUIPermissionsFragment;
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
 * Created by Prashant on 06-08-2018.
 */
public class SelectCategoryDialogFragment extends HealthCocoDialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener,
        CommonUiPermissionsListener {
    public static final String TAG_SELECTED_CATEGORY = "selectedCategory";
    public static final String TAG_ALL_CATEGORY = "allCategory";
    private GridView gvSettingsUIPermission;
    private UIPermissionItemGridAdapter adapter;
    private ArrayList<String> allCategory = new ArrayList<>();
    private ArrayList<String> assignedPermissionsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_category, null);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setWidthHeight(0.60, 0.80);
    }

    @Override
    public void init() {
        Bundle bundle = getArguments();

        if (bundle != null && bundle.containsKey(TAG_SELECTED_CATEGORY)) {
            assignedPermissionsList = Parcels.unwrap(bundle.getParcelable(TAG_SELECTED_CATEGORY));
            allCategory = Parcels.unwrap(bundle.getParcelable(TAG_ALL_CATEGORY));
            initViews();
            initListeners();
            initAdapters();
            initData();
        }
    }

    @Override
    public void initViews() {
        gvSettingsUIPermission = (GridView) view.findViewById(R.id.gv_settings_ui_permission);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        gvSettingsUIPermission.setOnItemClickListener(this);
        if (!Util.isNullOrEmptyList(allCategory))
            initActionbarTitle(getResources().getString(R.string.category));
        else
            initActionbarTitle(getResources().getString(R.string.kiosk_tab_permission));

    }

    private void initAdapters() {
        adapter = new UIPermissionItemGridAdapter(mActivity, this);
        gvSettingsUIPermission.setAdapter(adapter);
    }

    public void initData() {
        if (!Util.isNullOrEmptyList(allCategory)) {
            allCategory.add("HEALTH");
            allCategory.add("GENARAL");
            allCategory.add("MEDICAL");
            allCategory.add("HARBAL");
            allCategory.add("KIDS");
//        allCategory = VideoCategory.values();
            notifyAdapter(allCategory);
        } else
            notifyAdapter(allCategory);
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
            case R.id.bt_save:
                updateCategory();
                break;
        }

    }

    private void updateCategory() {
        if (getTargetFragment() != null) {
            Intent intent = new Intent();
            intent.putExtra(TAG_SELECTED_CATEGORY, Parcels.wrap(assignedPermissionsList));
            getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_CATEGORY, Activity.RESULT_OK, intent);
            dismiss();
        }
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
