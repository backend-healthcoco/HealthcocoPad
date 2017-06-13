package com.healthcoco.healthcocopad.popupwindow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.AvailableTimeSlots;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.List;

/**
 * Created by neha on 11/05/17.
 */

public class PopupListViewAdapter extends BaseAdapter {
    private int dropDownLayoutId;
    private PopupWindowType popupWindowType;
    private HealthCocoActivity mActivity;
    private List<Object> list;
    private TextView tvBullet;

    public PopupListViewAdapter(HealthCocoActivity mActivity, PopupWindowType popupWindowType, int dropDownLayoutId) {
        this.mActivity = mActivity;
        this.dropDownLayoutId = dropDownLayoutId;
        this.popupWindowType = popupWindowType;

    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(dropDownLayoutId, null);
        }
        TextView tvText = (TextView) convertView.findViewById(R.id.tv_text);
        tvBullet = (TextView) convertView.findViewById(R.id.tv_bullet);
        String text = getText(getItem(position));
        if (!Util.isNullOrBlank(text)) {
            tvText.setText(text);
            convertView.setTag(getItem(position));
        }
        return convertView;
    }

    private String getText(Object object) {
        String text = "";
        switch (popupWindowType) {
            case TIME_SLOTS:
                if (object instanceof AvailableTimeSlots) {
                    AvailableTimeSlots availableTimeSlots = (AvailableTimeSlots) object;
                    text = DateTimeUtil.convertFormattedDate(DateTimeUtil.TIME_FORMAT_24_HOUR, BookAppointmentDialogFragment.TIME_SLOT_FORMAT_USED_IN_THIS_SCREEN, availableTimeSlots.getTime());
                        if (availableTimeSlots.getIsAvailable() != null && availableTimeSlots.getIsAvailable()) {
                            tvBullet.setSelected(false);
                        } else
                            tvBullet.setSelected(true);
                }
                break;
            default:
                if (object instanceof String)
                    text = (String) object;
                break;
        }
        return text;

    }

    public void setListData(List<Object> listData) {
        this.list = listData;
    }

}




