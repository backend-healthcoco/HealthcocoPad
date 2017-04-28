package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.listeners.SelectedClinicListener;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 01-02-2017.
 */
public class MenuClinicListviewHolder extends HealthCocoViewHolder implements View.OnClickListener {
    private SelectedClinicListener selectedClinicListener;
    private DoctorClinicProfile doctorClinicProfile;
    private TextView tvName;
    private ImageView ivClinicImage;
    private LinearLayout convertView;
    private CheckBox cbGrouped;

    public MenuClinicListviewHolder(HealthCocoActivity mActivity, SelectedClinicListener selectedClinicListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.selectedClinicListener = selectedClinicListener;
    }

    @Override
    public void setData(Object object) {
        this.doctorClinicProfile = (DoctorClinicProfile) object;
    }

    @Override
    public void applyData() {
        tvName.setText(Util.getValidatedValue(doctorClinicProfile.getLocationName()));
        if (doctorClinicProfile.isForeignIsClinicSelected())
            cbGrouped.setChecked(true);
        else
            cbGrouped.setChecked(false);
        DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivClinicImage, doctorClinicProfile.getLogoThumbnailUrl());
    }

    @Override
    public View getContentView() {
        convertView = (LinearLayout) inflater.inflate(R.layout.item_menu_clinic, null);
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
        ivClinicImage = (ImageView) convertView.findViewById(R.id.iv_clinic_image);
        cbGrouped = (CheckBox) convertView.findViewById(R.id.cb_grouped);
        convertView.setOnClickListener(this);
        return convertView;
    }

    public void setSelected(boolean isSelected) {
        tvName.setSelected(isSelected);
        doctorClinicProfile.setForeignIsClinicSelected(isSelected);
    }

    @Override
    public void onClick(View v) {
        selectedClinicListener.onSelectedClinicCheckClicked(doctorClinicProfile);
    }
}
