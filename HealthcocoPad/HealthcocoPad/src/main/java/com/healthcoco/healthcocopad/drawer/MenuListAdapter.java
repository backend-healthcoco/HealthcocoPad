package com.healthcoco.healthcocopad.drawer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.MenuItem;
import com.healthcoco.healthcocopad.enums.FragmentType;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.MenuListViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Shreshtha on 30-01-2017.
 */
public class MenuListAdapter extends BaseAdapter {

    private LinkedHashMap<FragmentType, MenuItem> menuItemListHashmap;
    private ArrayList<MenuItem> list;
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
        if (Util.isNullOrEmptyList(list))
            return 0;
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

    public void setListData(ArrayList<MenuItem> menuItems) {
        if (!Util.isNullOrEmptyList(menuItems)) {
            menuItemListHashmap = new LinkedHashMap<>();
            for (MenuItem menuItem :
                    menuItems) {
                menuItemListHashmap.put(menuItem.getFragmentType(), menuItem);
            }
        } else menuItemListHashmap = null;
        this.list = menuItems;
    }

    public void setSetSelectedPosition(FragmentType fragmentType) {
        if (!Util.isNullOrEmptyList(list)) {
            if (fragmentType != FragmentType.HELP_IMPROVE && list.contains(fragmentType)) {
                MenuItem menuItem = menuItemListHashmap.get(fragmentType);
                if (list.contains(menuItem)) {
                    this.selectedPosition = list.indexOf(menuItem);
                    notifyDataSetChanged();
                }
            }
        }
    }
}