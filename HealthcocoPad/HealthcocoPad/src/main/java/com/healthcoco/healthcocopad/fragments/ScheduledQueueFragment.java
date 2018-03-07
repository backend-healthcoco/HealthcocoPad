package com.healthcoco.healthcocopad.fragments;


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
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 01-03-2018.
 */


public class ScheduledQueueFragment extends HealthCocoFragment {
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

}
