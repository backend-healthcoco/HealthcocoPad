package com.healthcoco.healthcocopad.adapter;

import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType;
import com.healthcoco.healthcocopad.skscustomclasses.CustomListData;
import com.healthcoco.healthcocopad.skscustomclasses.CustomListDataFormatter;
import com.healthcoco.healthcocopad.skscustomclasses.SKSCustomListAdapter;
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

//    @Override
//    protected void onNextPageRequested(int page) {
//
//    }
//
//    @Override
//    protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
//        if (displaySectionHeader) {
//            view.findViewById(R.id.header_text).setVisibility(View.VISIBLE);
//            TextView lSectionTitle = (TextView) view.findViewById(R.id.header_text);
//            lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
//        } else {
//            TextView lSectionTitle = (TextView) view.findViewById(R.id.header_text);
//            lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
//            view.findViewById(R.id.header_text).setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public View getAmazingView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            holder = new AppointmentsListViewholder(mActivity);
//            convertView = holder.getContentView();
//            convertView.setTag(holder);
//        } else
//            holder = (AppointmentsListViewholder) convertView.getTag();
//        holder.setData(getItem(position));
//        holder.applyData();
//        return convertView;
//    }
//
//    @Override
//    public void configurePinnedHeader(View header, int position, int alpha) {
//        TextView lSectionHeader = (TextView) header;
//        lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
//    }
//
//    @Override
//    public int getPositionForSection(int section) {
//        if (section < 0)
//            section = 0;
//        if (!Util.isNullOrEmptyList(all) && section >= all.size())
//            section = all.size() - 1;
//        int c = 0;
//        if (!Util.isNullOrEmptyList(all))
//            for (int i = 0; i < all.size(); i++) {
//                if (section == i) {
//                    return c;
//                }
//                c += all.get(i).second.size();
//            }
//        return 0;
//    }
//
//    @Override
//    public int getSectionForPosition(int position) {
//        int c = 0;
//        if (!Util.isNullOrEmptyList(all))
//            for (int i = 0; i < all.size(); i++) {
//                if (position >= c && position < c + all.get(i).second.size()) {
//                    return i;
//                }
//                c += all.get(i).second.size();
//            }
//        return -1;
//    }
//
//    @Override
//    public String[] getSections() {
//        String[] res = new String[all.size()];
//        for (int i = 0; i < all.size(); i++) {
//            res[i] = all.get(i).first;
//        }
//        return res;
//    }

    public void setListData(List<CustomListData> list) {
        if (!Util.isNullOrEmptyList(list))
            all = CustomListDataFormatter.getAllDataFromHeaderDataList(list);
    }
}
