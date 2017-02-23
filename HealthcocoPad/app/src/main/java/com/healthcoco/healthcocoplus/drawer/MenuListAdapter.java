package com.healthcoco.healthcocoplus.drawer;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.enums.FragmentType;
import com.healthcoco.healthcocoplus.enums.MenuActionItem;
import com.healthcoco.healthcocoplus.viewholders.MenuListViewHolder;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 30-01-2017.
 */
public class MenuListAdapter extends BaseAdapter {

    private ArrayList<FragmentType> list;
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

    public void setListData(ArrayList<FragmentType> fragmentType) {
        this.list = fragmentType;
    }

    public void setSetSelectedPosition(FragmentType fragmentType) {
        if (fragmentType != FragmentType.HELP_IMPROVE && list.contains(fragmentType)) {
            this.selectedPosition = list.indexOf(fragmentType);
            notifyDataSetChanged();
        }
    }

}