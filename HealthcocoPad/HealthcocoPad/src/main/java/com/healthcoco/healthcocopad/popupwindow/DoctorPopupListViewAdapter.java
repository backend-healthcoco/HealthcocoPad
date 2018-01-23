package com.healthcoco.healthcocopad.popupwindow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.AvailableTimeSlots;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.AppointmentSlotsType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.UnitType;
import com.healthcoco.healthcocopad.listeners.AssignGroupListener;
import com.healthcoco.healthcocopad.listeners.CBSelectedItemTypeListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.DoctorListViewHolder;
import com.healthcoco.healthcocopad.viewholders.GroupListViewHolder;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.List;

/**
 * Created by neha on 11/05/17.
 */

public class DoctorPopupListViewAdapter extends BaseAdapter {

    CBSelectedItemTypeListener cbSelectedItemTypeListener;
    private DoctorListViewHolder holder;
    private int dropDownLayoutId;
    private PopupWindowType popupWindowType;
    private HealthCocoActivity mActivity;
    private List<Object> list;

    public DoctorPopupListViewAdapter(HealthCocoActivity mActivity, PopupWindowType popupWindowType, int dropDownLayoutId, CBSelectedItemTypeListener cbSelectedItemTypeListener) {
        this.mActivity = mActivity;
        this.dropDownLayoutId = dropDownLayoutId;
        this.popupWindowType = popupWindowType;
        this.cbSelectedItemTypeListener = cbSelectedItemTypeListener;

    }

    @Override
    public int getCount() {
        if (!Util.isNullOrEmptyList(list))
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new DoctorListViewHolder(mActivity, cbSelectedItemTypeListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (DoctorListViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }


    public void setListData(List<Object> listData) {
        this.list = listData;
    }


}




