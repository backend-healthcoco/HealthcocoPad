package com.healthcoco.healthcocoplus.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocoplus.fragments.MenuDrawerFragment;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.viewholders.MenuClinicListviewHolder;

import java.util.List;

/**
 * Created by Shreshtha on 01-02-2017.
 */
public class MenuClinicListAdapter extends BaseAdapter {
    private List<DoctorClinicProfile> list;
    private HealthCocoActivity mActivity;
    private MenuClinicListviewHolder holder;
    private int selectedPosition;

    public MenuClinicListAdapter(HealthCocoActivity mActivity, List<DoctorClinicProfile> clinicProfile) {
        this.mActivity = mActivity;
        this.list = clinicProfile;
    }

    @Override
    public int getCount() {
        if (Util.isNullOrEmptyList(list))
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        DoctorClinicProfile doctorClinicProfile = (DoctorClinicProfile) getItem(position);
        if (convertView == null) {
            holder = new MenuClinicListviewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (MenuClinicListviewHolder) convertView.getTag();

        holder.setData(doctorClinicProfile);
        holder.applyData();
        if ((Util.isNullOrBlank(MenuDrawerFragment.SELECTED_LOCATION_ID) && position == 0)
                || (doctorClinicProfile.getLocationId() != null && MenuDrawerFragment.SELECTED_LOCATION_ID != null
                && MenuDrawerFragment.SELECTED_LOCATION_ID.equals(doctorClinicProfile.getLocationId())))
            holder.setSelected(true);
        else {
            holder.setSelected(false);
        }
        return convertView;
    }

    public void setListData(List<DoctorClinicProfile> list) {
        this.list = list;
    }
}
