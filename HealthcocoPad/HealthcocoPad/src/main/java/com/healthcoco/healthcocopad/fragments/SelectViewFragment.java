package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.HomeActivity;
import com.healthcoco.healthcocopad.adapter.SettingsListAdapter;
import com.healthcoco.healthcocopad.enums.FilterItemType;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class SelectViewFragment extends HealthCocoFragment implements AdapterView.OnItemClickListener {
    public static final String INTENT_REFRESH_SELECTED_VIEW_TYPE = "com.healthcoco.healthcocopad.fragments.FilterFragment.REFRESH_SELECTED_VIEW_TYPE";

    private ListView lvViewTypes;
    private SettingsListAdapter adapter;
    private List<CalendarViewType> listType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_type, null);
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
        lvViewTypes = (ListView) view.findViewById(R.id.lv_settings);
    }

    @Override
    public void initListeners() {
        lvViewTypes.setOnItemClickListener(this);
    }

    private void initAdapters() {
        listType = Arrays.asList(CalendarViewType.values());
        adapter = new SettingsListAdapter(mActivity);
        lvViewTypes.setAdapter(adapter);
        adapter.setListData((List<Object>) (List<?>) listType);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CalendarViewType calendarViewType = listType.get(position);
        sendBroadCastToContactsFragment(calendarViewType);
    }

    private void sendBroadCastToContactsFragment(CalendarViewType calendarViewType) {
        try {
            Intent intent = new Intent();
            intent.putExtra(HealthCocoConstants.TAG_ORDINAL, calendarViewType.ordinal());
            intent.setAction(INTENT_REFRESH_SELECTED_VIEW_TYPE);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
