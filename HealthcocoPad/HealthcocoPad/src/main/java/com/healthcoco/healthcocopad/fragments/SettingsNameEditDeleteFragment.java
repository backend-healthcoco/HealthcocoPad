package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.SettingsNameEditDeleteListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.SettingsItemType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.NameEditDeleteListener;
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
import java.util.Locale;

/**
 * Created by neha on 15/01/16.
 */
public class SettingsNameEditDeleteFragment extends HealthCocoFragment implements NameEditDeleteListener, View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, TextWatcher {

    private SettingsNameEditDeleteListAdapter adapter;
    private SettingsItemType settingsItemType;
    private ListView lvList;
    private User user;
    private TextView tvNoResults;
    private Object objectToEditDelete;
    private ArrayList<Object> list;
    private List<DoctorClinicProfile> doctorClinicsList;
    private AutoCompleteTextView autotvClinicName;
    private TextView tvAddNew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings_edit_delete, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
//        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getCustomUniqueId())) {
//            user = doctor.getUser();
        init();
//        }
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        int typeOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_NAME_EDIT_TYPE, -1);
        if (typeOrdinal > -1)
            settingsItemType = SettingsItemType.values()[typeOrdinal];
        if (settingsItemType != null) {
            initViews();
            initListeners();
            initAdapter();
            mActivity.showLoading(false);
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//            getListFromLocal(true);
        }
    }

    @Override
    public void initViews() {
//        ((CommonOpenUpActivity) mActivity).initAddButton(this);
        initEditSearchView(settingsItemType.getSearchHintId(), this, this);
        lvList = (ListView) view.findViewById(R.id.lv_list);
        tvNoResults = (TextView) view.findViewById(R.id.tv_no_results_found);
        tvNoResults.setText(settingsItemType.getNoDataText());
        autotvClinicName = (AutoCompleteTextView) view.findViewById(R.id.autotv_clinic_name);
        tvAddNew = (TextView) view.findViewById(R.id.bt_advance_search);
        tvAddNew.setText(R.string.add_new);
    }

    @Override
    public void initListeners() {
        tvAddNew.setOnClickListener(this);
    }

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, final AutoCompleteTextViewType autoCompleteTextViewType, List<Object> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
                autoCompleteTextView.setVisibility(View.VISIBLE);
                final AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.drop_down_item_doctor_profile_my_clinic,
                        list, autoCompleteTextViewType);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (autoCompleteTextViewType) {
                            case DOCTOR_CLINIC:
                                Object object = adapter.getSelectedObject(position);
                                if (object != null && object instanceof DoctorClinicProfile) {
                                    DoctorClinicProfile doctorClinicProfile = (DoctorClinicProfile) object;
                                    refreshSelectedClinicProfileData(doctorClinicProfile);
                                }
                                break;
                        }

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshSelectedClinicProfileData(DoctorClinicProfile doctorClinicProfile) {
        autotvClinicName.setText(Util.getValidatedValue(doctorClinicProfile.getLocationName()));
        user.setForeignHospitalId(doctorClinicProfile.getHospitalId());
        user.setForeignLocationId(doctorClinicProfile.getLocationId());
        getListFromLocal(true);
    }

    private DoctorClinicProfile getDoctorClinicProfileFromList(String locationId, List<DoctorClinicProfile> list) {
        for (DoctorClinicProfile doctorClinicProfile :
                list) {
            if (doctorClinicProfile.getLocationId().equalsIgnoreCase(locationId))
                return doctorClinicProfile;
        }
        return list.get(0);
    }

    private void getListFromLocal(boolean isInitialLoading) {
        if (isInitialLoading)
            mActivity.showLoading(false);
        switch (settingsItemType) {
            case GROUPS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_GROUPS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }

    }

    private void getDataFromServer() {
        switch (settingsItemType) {
            case GROUPS:
                mActivity.showLoading(false);
                Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.USER_GROUP);
                WebDataServiceImpl.getInstance(mApp).getGroupsList(WebServiceType.GET_GROUPS, UserGroups.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), latestUpdatedTime, null, this, this);
                break;
        }
    }

    private void initAdapter() {
        adapter = new SettingsNameEditDeleteListAdapter(mActivity, this);
        lvList.setAdapter(adapter);

    }

    private void notifyAdapter(List<?> list) {
        if (!Util.isNullOrEmptyList(list)) {
            LogUtils.LOGD(TAG, "Size  List Notify Adapter" + list.size());
            sortList(list);
            lvList.setVisibility(View.VISIBLE);
//            tvNoGroups.setVisibility(View.GONE);
            adapter.setListData(settingsItemType, list);
            adapter.notifyDataSetChanged();
        } else {
            lvList.setVisibility(View.GONE);
//            tvNoGroups.setVisibility(View.VISIBLE);
        }
    }

    private void sortList(List<?> list) {
        switch (settingsItemType) {
            case GROUPS:
                Collections.sort(list, ComparatorUtil.groupDateComparator);
                break;
        }
    }

    @Override
    public void onEditClicked(SettingsItemType itemType, Object object) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            objectToEditDelete = object;
            switch (itemType) {
                case GROUPS:
                    UserGroups group = (UserGroups) object;
                    mActivity.openAddUpdateNameDialogFragment(WebServiceType.UPDATE_GROUP, AddUpdateNameDialogType.GROUPS, this, user, group.getUniqueId(), HealthCocoConstants.REQUEST_CODE_GROUPS_LIST);
                    break;
            }
        } else
            onNetworkUnavailable(null);
    }

    @Override
    public void onDeleteClicked(SettingsItemType itemType, Object object) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            switch (itemType) {
                case GROUPS:
                    UserGroups group = (UserGroups) object;
                    showConfirmationAlert(group);
                    break;
            }
        } else
            onNetworkUnavailable(null);
    }

    public void showConfirmationAlert(final UserGroups group) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(getResources().getString(
                R.string.confirm_delete_group) + group.getName() + "?");
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteGroup(group);
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

    private void deleteGroup(UserGroups group) {
        objectToEditDelete = group;
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addUpdateDeleteGroup(WebServiceType.DELETE_GROUP, UserGroups.class, group, this, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_advance_search) {
            if (settingsItemType == SettingsItemType.GROUPS) {
                mActivity.openAddUpdateNameDialogFragment(
                        WebServiceType.ADD_NEW_GROUP,
                        AddUpdateNameDialogType.GROUPS,
                        this,
                        user,
                        "",
                        HealthCocoConstants.REQUEST_CODE_GROUPS_LIST
                );
            }
        }
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        DoctorClinicProfile doctorClinicProfile = null;
                        if (!Util.isNullOrEmptyList(doctorClinicsList))
                            switch (settingsItemType) {
                                case GROUPS:
                                    autotvClinicName.setVisibility(View.VISIBLE);
                                    initAutoTvAdapter(autotvClinicName, AutoCompleteTextViewType.DOCTOR_CLINIC, (ArrayList<Object>) (ArrayList<?>) doctorClinicsList);
                                    String selectedLocationId = user.getForeignLocationId();
                                    doctorClinicProfile = getDoctorClinicProfileFromList(selectedLocationId, doctorClinicsList);
                                    break;
                                default:
                                    autotvClinicName.setVisibility(View.GONE);
                                    break;
                            }
                        refreshSelectedClinicProfileData(doctorClinicProfile);

                    }
                    break;
                case GET_GROUPS:
                    if (response.isDataFromLocal()) {
                        list = response
                                .getDataList();
                        if (!Util.isNullOrEmptyList(list))
                            LogUtils.LOGD(TAG, "Success onResponse list Size " + list.size() + " isDataFromLocal " + response.isDataFromLocal());
                        setSearchEditTextValue(getSearchEditTextValue());
                        if (!response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            getDataFromServer();
                            return;
                        }
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_GROUPS_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    break;
                case DELETE_GROUP:
                    if (objectToEditDelete != null) {
                        list.remove(objectToEditDelete);
                        setSearchEditTextValue(getSearchEditTextValue());
                        response.setData(objectToEditDelete);
                        removeObjectFromLocal(response);
                    } else
                        getDataFromServer();
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void removeObjectFromLocal(VolleyResponseBean response) {
        switch (settingsItemType) {
            case GROUPS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.DELETE_GROUP, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
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

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                    doctorClinicsList = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId());
                }

                break;
            case ADD_GROUPS_LIST:
                LocalDataServiceImpl.getInstance(mApp).addUserGroupsList((ArrayList<UserGroups>) (ArrayList<?>) response.getDataList());
            case GET_GROUPS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getUserGroups(WebServiceType.GET_GROUPS, null, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), null, null);
                break;
            case DELETE_GROUP:
                LocalDataServiceImpl.getInstance(mApp).deleteUserGroup((UserGroups) response.getData());
                return null;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String search = String.valueOf(s).toLowerCase(Locale.ENGLISH);
        sortListWithText(search);
    }

    private void sortListWithText(String search) {
        ArrayList tempList = new ArrayList<>();
        if (!Util.isNullOrEmptyList(list)) {
            if (search.length() == 0) {
                tempList.addAll(list);
            } else {
                for (Object object : list) {
                    switch (settingsItemType) {
                        case GROUPS:
                            UserGroups group = (UserGroups) object;
                            if (group.getName().toLowerCase(Locale.ENGLISH)
                                    .contains(search)) {
                                tempList.add(object);
                            }
                            break;
                    }
                }
            }
        }
        notifyAdapter(tempList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_GROUPS_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_GROUP) {
                getListFromLocal(true);
            }
        }
    }
}
