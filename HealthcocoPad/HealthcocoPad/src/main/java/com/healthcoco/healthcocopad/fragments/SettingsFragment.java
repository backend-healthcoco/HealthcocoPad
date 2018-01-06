package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.SettingsListAdapter;
import com.healthcoco.healthcocopad.bean.server.GCMRequest;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.SettingsItemType;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class SettingsFragment extends HealthCocoFragment implements AdapterView.OnItemClickListener {
    private ListView lvSettings;
    private SettingsListAdapter adapter;
    private List<SettingsItemType> listType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, null);
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
    }

    @Override
    public void initViews() {
        lvSettings = (ListView) view.findViewById(R.id.lv_settings);
    }

    @Override
    public void initListeners() {
        lvSettings.setOnItemClickListener(this);

    }

    private void initAdapters() {
        listType = Arrays.asList(SettingsItemType.values());
        adapter = new SettingsListAdapter(mActivity);
        lvSettings.setAdapter(adapter);
        adapter.setListData(listType);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SettingsItemType itemType = listType.get(position);
        switch (itemType) {
//            case PATIENT:
////                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_PATIENT, itemType.ordinal());
//                break;
//            case CLINICAL_NOTES:
////                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_CLINICAL_NOTES, itemType.ordinal());
//                break;
//            case HISTORY:
////                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_HISTORY, itemType.ordinal());
//                break;
//            case PRESCRIPTION:
////                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_PRESCRIPTION, itemType.ordinal());
//                break;
//            case TEMPLATE:
////                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_TEMPLATE, itemType.ordinal());
//                break;
            case UI_PERMISSION:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTING_UI_PERMISSION, itemType.ordinal());
                break;
            case SYNC:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.SYNC, itemType.ordinal());
                break;
//            case BILLING:
////                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTINGS_BILLING, itemType.ordinal());
//                break;
//            case SMS:
////                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTING_SMS, itemType.ordinal());
//                break;
//            case EMAIL:
//                break;
//            case PRINT:
//                break;
//            case ID_CREATION:
//                break;
            case ABOUT:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.SETTING_ABOUT_US, itemType.ordinal());
                break;
            case SIGN_OUT:
                showConfirmSignOutAlert();
                break;
        }
    }

    private void openCommonOpenUpActivity(CommonOpenUpFragmentType fragmentType, int typeOrdinal) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_NAME_EDIT_TYPE, typeOrdinal);
        startActivity(intent);
    }

    private void showConfirmSignOutAlert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(getResources().getString(
                R.string.confirm_sign_out));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearAndCreateDatabaseStructure();
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

    private void clearAndCreateDatabaseStructure() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mActivity.showLoading(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
//                mActivity.deleteDatabase(getResources().getString(R.string.database_name));
                GCMRequest gcmRequest = LocalDataServiceImpl.getInstance(mApp).getGCMRequestData();
                gcmRequest.setUserIdsJsonString(null);
                gcmRequest.setUserIds(null);
                LocalDataServiceImpl.getInstance(mApp).addGCMRequest(gcmRequest);
                LocalDataServiceImpl.getInstance(mApp).clearAllTables(mActivity);
                mApp.cancelAllPendingRequests();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mActivity.initGCM();
                mActivity.hideLoading();
                mActivity.openLoginSignupActivity();
            }
        }.execute();
    }
}
