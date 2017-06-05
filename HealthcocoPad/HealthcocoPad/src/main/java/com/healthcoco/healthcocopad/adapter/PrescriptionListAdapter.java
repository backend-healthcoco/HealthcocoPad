package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.PrescriptionListItemViewHolder;

import java.util.List;

/**
 * Created by neha on 12/03/16.
 */
public class PrescriptionListAdapter extends BaseAdapter {
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private CommonEMRItemClickListener listItemClickListener;
    private List<Prescription> list;
    private HealthCocoActivity mActivity;
    private PrescriptionListItemViewHolder holder;

    public PrescriptionListAdapter() {
    }

    public PrescriptionListAdapter(HealthCocoActivity activity, CommonEMRItemClickListener listItemClickListener) {
        this.mActivity = activity;
        this.user = user;
        this.listItemClickListener = listItemClickListener;
        this.selectedPatient = selectedPatient;
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
            holder = new PrescriptionListItemViewHolder(mActivity, listItemClickListener, true);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (PrescriptionListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<Prescription> list) {
        this.list = list;
    }

}
