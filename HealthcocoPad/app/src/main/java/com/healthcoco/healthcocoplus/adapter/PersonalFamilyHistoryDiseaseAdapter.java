package com.healthcoco.healthcocoplus.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.bean.server.Disease;
import com.healthcoco.healthcocoplus.listeners.MedicalFamilyHistoryItemListener;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.viewholders.PersonalFamilyHistoryDiseaseViewHolder;

import java.util.ArrayList;

/**
 * Created by neha on 11/12/15.
 */
public class PersonalFamilyHistoryDiseaseAdapter extends BaseAdapter {
    private MedicalFamilyHistoryItemListener itemListener;
    private ArrayList<Disease> list;
    private HealthCocoActivity mActivity;
    private PersonalFamilyHistoryDiseaseViewHolder holder;

    public PersonalFamilyHistoryDiseaseAdapter() {
    }

    public PersonalFamilyHistoryDiseaseAdapter(HealthCocoActivity activity, MedicalFamilyHistoryItemListener itemListener) {
        this.mActivity = activity;
        this.itemListener = itemListener;
    }

    @Override
    public int getCount() {
        if (!Util.isNullOrEmptyList(list))
            return list.size();
        return 0;
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
        if (convertView == null) {
            holder = new PersonalFamilyHistoryDiseaseViewHolder(mActivity,itemListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (PersonalFamilyHistoryDiseaseViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(ArrayList<Disease> list) {
        this.list = list;
    }

}
