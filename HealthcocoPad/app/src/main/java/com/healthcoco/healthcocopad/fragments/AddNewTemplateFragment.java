package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;

/**
 * Created by Shreshtha on 27-03-2017.
 */
public class AddNewTemplateFragment extends HealthCocoFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragemnt_add_new_template, container, false);
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
        initData();
        initAdapters();
        getDataFromIntent();
//        showLoadingOverlay(true);
//        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getDataFromIntent() {
        Intent intent = mActivity.getIntent();
        String stringExtra = intent.getStringExtra(HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA);
    }

    private void initData() {

    }

    private void initAdapters() {

    }


    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }
}
