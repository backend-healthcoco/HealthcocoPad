package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.TreatmentListItemClickListeners;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.PrescriptionListItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.TreatmentListItemViewHolder;

import java.util.List;

/**
 * Created by neha on 12/03/16.
 */
public class TreatmentListAdapter extends BaseAdapter {
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private CommonEMRItemClickListener commonEMRItemClickListener;
    private List<Treatments> list;
    private HealthCocoActivity mActivity;
    private TreatmentListItemViewHolder holder;
    private TreatmentListItemClickListeners listItemClickListeners;

    public TreatmentListAdapter() {
    }

    public TreatmentListAdapter(HealthCocoActivity activity, CommonEMRItemClickListener commonEMRItemClickListener, TreatmentListItemClickListeners listItemClickListeners) {
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
            holder = new TreatmentListItemViewHolder(mActivity, commonEMRItemClickListener, true, listItemClickListeners);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (TreatmentListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<Treatments> list) {
        this.list = list;
    }

}
