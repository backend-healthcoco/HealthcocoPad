package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 01-02-2017.
 */
public class MenuClinicListviewHolder extends HealthCocoViewHolder {
    private DoctorClinicProfile doctorClinicProfile;
    private TextView tvName;
    private LinearLayout convertView;

    public MenuClinicListviewHolder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    public void setData(Object object) {
        this.doctorClinicProfile = (DoctorClinicProfile) object;
    }

    @Override
    public void applyData() {
        tvName.setText(Util.getValidatedValue(doctorClinicProfile.getLocationName()));
    }

    @Override
    public View getContentView() {
        convertView = (LinearLayout) inflater.inflate(R.layout.item_menu_clinic, null);
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
        return convertView;
    }

    public void setSelected(boolean isSelected) {
        tvName.setSelected(isSelected);
    }
}
