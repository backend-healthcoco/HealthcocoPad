package com.healthcoco.healthcocopad.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.QueueRecyclerViewAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 01-03-2018.
 */


public class ScheduledQueueFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised {
    private List<CalendarEvents> calendarEventsList = new ArrayList<>();
    private RecyclerView ScheduledQueueRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scheduled_queue, container, false);
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
        initAdapter();
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initAdapter() {
        QueueRecyclerViewAdapter mAdapter = new QueueRecyclerViewAdapter(mActivity, calendarEventsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        ScheduledQueueRecyclerView.setLayoutManager(layoutManager);
        ScheduledQueueRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ScheduledQueueRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void initViews() {
        ScheduledQueueRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_scheduled_queue);
    }

    @Override
    public void initListeners() {
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        return null;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }
}
