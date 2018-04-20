package com.healthcoco.healthcocopad.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.viewholders.DoctorListViewHolder;
import com.healthcoco.healthcocopad.viewholders.QueueItemViewHolder;

import java.util.ArrayList;

/**
 * Created by neha on 12/03/18.
 */

public class HealthcocoRecyclerViewAdapter extends RecyclerView.Adapter<HealthcocoComonRecylcerViewHolder> {

    private HealthcocoRecyclerViewItemClickListener onItemClickListener;
    //required by ImageviewHolder
    private boolean takeATour;

    private boolean likedPosts;

    private Object listenerObject;
    private RecyclerView lvChats;
    private AdapterType adapterType;
    private HealthCocoActivity mActivity;
    private ArrayList<Object> list;

    //GeneralConstructor
    public HealthcocoRecyclerViewAdapter(HealthCocoActivity activity, AdapterType adapterType,
                                         HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
        this.mActivity = activity;
        this.adapterType = adapterType;
        this.listenerObject = listenerObject;
        this.onItemClickListener = onItemClickListener;
    }

    public HealthcocoRecyclerViewAdapter(HealthCocoActivity activity, AdapterType adapterType, Object listenerObject) {
        this(activity, adapterType, null, listenerObject);

    }

    //GeneralConstructor
    public HealthcocoRecyclerViewAdapter(HealthCocoActivity activity, AdapterType adapterType, HealthcocoRecyclerViewItemClickListener onItemClickListener) {
        this(activity, adapterType, onItemClickListener, null);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public HealthcocoComonRecylcerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View itemView = layoutInflater.inflate(R.layout.layout_item, parent, false);
        //return new ItemHolder(itemView, this);
        HealthcocoComonRecylcerViewHolder viewHolder = null;
        View convertView = null;
        LayoutInflater mInflater = mActivity.getLayoutInflater();
        switch (adapterType) {
            case APOINTMENT_QUEUE:

                convertView = mInflater.inflate(R.layout.list_item_queue, null);
                viewHolder = new QueueItemViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                break;
            case DOCTOR_POPUP_LIST:

                convertView = mInflater.inflate(R.layout.item_doctor_popup_list, parent, false);
                viewHolder = new DoctorListViewHolder(mActivity, convertView, listenerObject);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HealthcocoComonRecylcerViewHolder holder, int position) {
        Object object = list.get(position);
        switch (adapterType) {
            case APOINTMENT_QUEUE:
                if (holder instanceof QueueItemViewHolder && object instanceof CalendarEvents) {
                    QueueItemViewHolder queueItemViewHolder = ((QueueItemViewHolder) holder);
                    queueItemViewHolder.applyData(object);
                }
                break;
            case DOCTOR_POPUP_LIST:
                if (holder instanceof DoctorListViewHolder && object instanceof RegisteredDoctorProfile) {
                    DoctorListViewHolder doctorListViewHolder = ((DoctorListViewHolder) holder);
                    holder.applyData(object);
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setListData(ArrayList<Object> list) {
        this.list = list;
    }


    public Object getItem(int position) {
        return list.get(position);
    }
}


