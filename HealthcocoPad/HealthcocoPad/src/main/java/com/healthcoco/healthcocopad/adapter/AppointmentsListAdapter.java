package com.healthcoco.healthcocopad.adapter;

import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType;
import com.healthcoco.healthcocopad.skscustomclasses.CustomListData;
import com.healthcoco.healthcocopad.skscustomclasses.CustomListDataFormatter;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.AppointmentsListViewholder;

import java.util.Collections;
import java.util.List;

/**
 * Created by neha on 05/05/16.
 */
public class AppointmentsListAdapter extends BaseAdapter {
    private HealthCocoActivity mActivity;
    private AppointmentsListViewholder holder;
    private List<Pair<String, List<?>>> all;

    public AppointmentsListAdapter() {
    }

    public AppointmentsListAdapter(HealthCocoActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        int res = 0;
        if (!Util.isNullOrEmptyList(all))
            for (int i = 0; i < all.size(); i++) {
                if (!Util.isNullOrEmptyList(all.get(i).second) && all.get(i).second.get(0) instanceof CalendarEvents) {
                    List<CalendarEvents> secondList = (List<CalendarEvents>) all.get(i).second;
                    Collections.sort(secondList, ComparatorUtil.calendarEventsFromToTimeComparator);
                }
                res += all.get(i).second.size();
            }
        return res;
    }

    @Override
    public Object getItem(int position) {
        int c = 0;
        if (!Util.isNullOrEmptyList(all))
            for (int i = 0; i < all.size(); i++) {
                if (position >= c && position < c + all.get(i).second.size()) {
                    return all.get(i).second.get(position - c);
                }
                c += all.get(i).second.size();
            }
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new AppointmentsListViewholder(mActivity, BookAppointmentFromScreenType.APPOINTMENTS_LIST_RESCHEDULE);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (AppointmentsListViewholder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<CustomListData> list) {
        if (!Util.isNullOrEmptyList(list))
            all = CustomListDataFormatter.getAllDataFromHeaderDataList(list);
    }
}
