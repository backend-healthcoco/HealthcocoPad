package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.Leave;
import com.healthcoco.healthcocopad.bean.server.ReferDoctor;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.LeaveListItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.ReferDoctorListItemViewHolder;

import java.util.List;

/**
 * Created by neha on 12/03/16.
 */
public class ReferDoctorListAdapter extends BaseAdapter {
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private CommonEMRItemClickListener commonEMRItemClickListener;
    private List<ReferDoctor> list;
    private HealthCocoActivity mActivity;
    private ReferDoctorListItemViewHolder holder;
    private SelectedTreatmentItemClickListener listItemClickListeners;

    public ReferDoctorListAdapter() {
    }

    public ReferDoctorListAdapter(HealthCocoActivity activity, CommonEMRItemClickListener commonEMRItemClickListener, SelectedTreatmentItemClickListener listItemClickListeners) {
        this.mActivity = activity;
        this.listItemClickListeners = listItemClickListeners;
        this.commonEMRItemClickListener = commonEMRItemClickListener;
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
            holder = new ReferDoctorListItemViewHolder(mActivity, commonEMRItemClickListener, listItemClickListeners);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (ReferDoctorListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<ReferDoctor> list) {
        this.list = list;
    }

}
