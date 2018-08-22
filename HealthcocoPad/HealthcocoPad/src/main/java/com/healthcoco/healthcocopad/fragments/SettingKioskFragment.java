package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.SettingsListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.BottomTextStyle;
import com.healthcoco.healthcocopad.bean.server.ContentSetup;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.FooterSetup;
import com.healthcoco.healthcocopad.bean.server.HeaderSetup;
import com.healthcoco.healthcocopad.bean.server.LeftText;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.PageSetup;
import com.healthcoco.healthcocopad.bean.server.PatientDetails;
import com.healthcoco.healthcocopad.bean.server.PrintSettings;
import com.healthcoco.healthcocopad.bean.server.RightText;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.EditContentSetupDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.EditFooterSetupDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.EditHeaderSetupDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.EditPageSetupDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.EditPatientDetailsSetupDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.UploadVideoDialogFragment;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.KioskSettingsItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddPrintSettingsListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.healthcoco.healthcocopad.enums.UIPermissionsItemType.PATIENT_TAB_PERMISSION;

/**
 * Created by Prashant on 05/02/2018.
 */

public class SettingKioskFragment extends HealthCocoFragment implements GsonRequest.ErrorListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, View.OnClickListener, AdapterView.OnItemClickListener {

    private User user;
    private boolean isKiosk;
    private ListView lvSettings;
    private TextView tvKioskNotAvailable;
    private SettingsListAdapter adapter;
    private List<KioskSettingsItemType> listType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kiosk_settings, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Util.checkNetworkStatus(mActivity);
        init();
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
        lvSettings = (ListView) view.findViewById(R.id.lv_settings);
        tvKioskNotAvailable = (TextView) view.findViewById(R.id.tv_kiosk_not_available);
    }

    @Override
    public void initListeners() {
        lvSettings.setOnItemClickListener(this);

    }

    private void initAdapters() {
        listType = Arrays.asList(KioskSettingsItemType.values());
        adapter = new SettingsListAdapter(mActivity);
        lvSettings.setAdapter(adapter);
    }

    private void initData() {
        tvKioskNotAvailable.setVisibility(View.GONE);
        lvSettings.setVisibility(View.VISIBLE);

        adapter.setListData((List<Object>) (List<?>) listType);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
//                addPrintSettings();
                break;
        }
    }


    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                }
                DoctorClinicProfile doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
                if (doctorClinicProfile != null && doctorClinicProfile.getIskiosk() != null)
                    isKiosk = doctorClinicProfile.getIskiosk();

                return volleyResponseBean;
        }
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        if (isKiosk) {
                            initData();
                        } else {
                            nodataFound();
                        }
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void nodataFound() {
        tvKioskNotAvailable.setVisibility(View.VISIBLE);
        lvSettings.setVisibility(View.GONE);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        KioskSettingsItemType itemType = listType.get(position);
        switch (itemType) {
            case ADD_VIDEO:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.EDUCATION_VIDEO, HealthCocoConstants.TAG_IS_FROM_SETTINGS, true, 0);
//                openDialogFragment(new UploadVideoDialogFragment(), 0);
                break;
            case CHANGE_PIN:
                mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_LOCATION_ADMIN, SuggestionType.PASSWORD);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_LOCATION_ADMIN) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_LOCATION_ADMIN) {
                openCommonOpenUpActivity(CommonOpenUpFragmentType.CHANGE_PIN, "CHANGE_PIN", null);
            }
        }
    }
}
