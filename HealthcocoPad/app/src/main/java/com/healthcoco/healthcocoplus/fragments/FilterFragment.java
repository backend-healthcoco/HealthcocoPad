package com.healthcoco.healthcocoplus.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.activities.HomeActivity;
import com.healthcoco.healthcocoplus.enums.FilterItemType;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class FilterFragment extends HealthCocoFragment {
    private String selectedFilterTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void init() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }

    private void refreshHomeScreenTitle(String title) {
        this.selectedFilterTitle = title;
        ((HomeActivity) mActivity).setActionbarTitle(title);
    }

    public void setSelectedItem(FilterItemType filterItemType) {
//        clearPreviuosFilters(null);
//        switch (filterItemType) {
//            case RECENTLY_VISITED:
//                tvRecentlyVisited.setSelected(true);
//                break;
//            case RECENTLY_ADDED:
//                tvRecentlyAdded.setSelected(true);
//                break;
//            case ALL_PATIENTS:
//                tvAllPatients.setSelected(true);
//                break;
//            case MOST_VISITED:
//                tvMostVisited.setSelected(true);
//                break;
//        }
    }

    public String getSelectedFilterTitle() {
        return selectedFilterTitle;
    }
}
