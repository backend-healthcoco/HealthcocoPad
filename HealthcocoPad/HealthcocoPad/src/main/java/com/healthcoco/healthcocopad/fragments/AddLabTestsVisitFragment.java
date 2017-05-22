package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.DiagnosticTestsListAdapter;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.custom.MyScriptEditText;
import com.healthcoco.healthcocopad.listeners.DiagnosticTestItemListener;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by neha on 19/04/17.
 */

public class AddLabTestsVisitFragment extends HealthCocoFragment implements View.OnFocusChangeListener,
        DiagnosticTestItemListener {
    private LinkedHashMap<String, DiagnosticTest> diagnosticTestsListHashMap = new LinkedHashMap<>();
    private DiagnosticTestsListAdapter adapter;
    private ListView lvDiagnoticTests;
    private MyScriptAddVisitsFragment myScriptAddVisitsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lab_tests_add_visits, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
    }

    @Override
    public void initViews() {
        lvDiagnoticTests = (ListView) view.findViewById(R.id.lv_diagnostic_tests);
    }

    @Override
    public void initListeners() {
    }

    private void initAdapter() {
        adapter = new DiagnosticTestsListAdapter(mActivity, this);
        lvDiagnoticTests.setAdapter(adapter);
        lvDiagnoticTests.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvDiagnoticTests.setStackFromBottom(true);
    }

    private void notifyAdapter(ArrayList<DiagnosticTest> list) {
        if (!Util.isNullOrEmptyList(list))
            sendBroadcastToShowLabTestLayout(true);
        else
            sendBroadcastToShowLabTestLayout(false);
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
    }

    private void sendBroadcastToShowLabTestLayout(boolean showVisibility) {
        try {
            Intent intent = new Intent(MyScriptAddVisitsFragment.INTENT_LAB_TEST_VISIBILITY);
            intent.putExtra(MyScriptAddVisitsFragment.TAG_VISIBILITY, showVisibility);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationAlertForDrugs(final DiagnosticTest diagnosticTest) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(R.string.confirm_remove_test);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    diagnosticTestsListHashMap.remove(diagnosticTest.getUniqueId());
                    notifyAdapter(new ArrayList<DiagnosticTest>(diagnosticTestsListHashMap.values()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void addDiagnosticTest(DiagnosticTest diagnosticTests) {
        diagnosticTestsListHashMap.put(diagnosticTests.getUniqueId(), diagnosticTests);
        notifyAdapter(new ArrayList<DiagnosticTest>(diagnosticTestsListHashMap.values()));
        lvDiagnoticTests.setSelection(adapter.getCount());
    }

    public View getLastChildView() {
        View lastview = null;
        try {
            lastview = lvDiagnoticTests.getChildAt(adapter.getCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lastview == null)
            lastview = view;
        return lastview;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v instanceof EditText) {
                myScriptAddVisitsFragment.initEditTextForWidget((MyScriptEditText) v);
                myScriptAddVisitsFragment.showOnlyWidget();
            }
        }
    }

    @Override
    public void onAddClicked(DiagnosticTest diagnosticTest) {

    }

    @Override
    public void onAddedClicked(DiagnosticTest diagnosticTest) {

    }

    @Override
    public DiagnosticTest getDiagnosticTest(String uniqueId) {
        return null;
    }

    @Override
    public void onDeleteItemClicked(DiagnosticTest diagnosticTest) {
        showConfirmationAlertForDrugs(diagnosticTest);
    }

    @Override
    public void onDiagnosticTestClicked(DiagnosticTest diagnosticTest) {

    }

    public boolean isBlankLabTestsList() {
        return Util.isNullOrEmptyList(diagnosticTestsListHashMap);
    }

    public List<DiagnosticTest> getLabTestsList() {
        if (!Util.isNullOrEmptyList(diagnosticTestsListHashMap))
            return new ArrayList<>(diagnosticTestsListHashMap.values());
        return null;
    }
}
