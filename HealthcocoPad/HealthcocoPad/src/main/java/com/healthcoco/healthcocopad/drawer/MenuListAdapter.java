package com.healthcoco.healthcocopad.drawer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.enums.FragmentType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.viewholders.MenuListViewHolder;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 30-01-2017.
 */
public class MenuListAdapter extends BaseAdapter {

    private ArrayList<Object> list;
    private HealthCocoActivity mActivity;
    private MenuListViewHolder holder;
    private int selectedPosition = 0;

    public MenuListAdapter() {
    }

    public MenuListAdapter(HealthCocoActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = new MenuListViewHolder(mActivity);
        convertView = holder.getContentView(list.get(position));
        convertView.setTag(list.get(position));
        if (position == this.selectedPosition) {
            this.holder.setSelected(true);
        } else {
            this.holder.setSelected(false);
        }


        holder.applyData(list.get(position));
        return convertView;
    }

    public void setListData(ArrayList<Object> objectArrayList) {
        this.list = objectArrayList;
    }

    public void setSetSelectedPosition(Object object) {
        if (object != null && object instanceof FragmentType) {
            FragmentType fragmentType = (FragmentType) object;
            if (fragmentType != FragmentType.HELP_IMPROVE && list.contains(fragmentType)) {
                this.selectedPosition = list.indexOf(fragmentType);
                notifyDataSetChanged();
            }
        } else if (object != null && object instanceof PatientDetailTabType) {
            PatientDetailTabType patientDetailTabType = (PatientDetailTabType) object;
            this.selectedPosition = list.indexOf(patientDetailTabType);
            notifyDataSetChanged();
        }

    }

}