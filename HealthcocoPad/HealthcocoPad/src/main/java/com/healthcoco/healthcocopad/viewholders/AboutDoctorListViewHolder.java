package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.listeners.CBSelectedItemTypeListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 23/11/15.
 */
public class AboutDoctorListViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {

    CBSelectedItemTypeListener cbSelectedItemTypeListener;
    private HealthCocoActivity mActivity;
    private ClinicDoctorProfile clinicDoctorProfile;
    private TextView tvName;
    private TextView tvInitialAlphabet;
    private ProgressBar progressLoading;
    private ImageView ivContactProfile;

    public AboutDoctorListViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
    }


    @Override
    public void applyData(Object object) {
        clinicDoctorProfile = null;
        if (object instanceof ClinicDoctorProfile)
            clinicDoctorProfile = (ClinicDoctorProfile) object;
        if (clinicDoctorProfile != null) {
            tvName.setText(Util.getValidatedValue(clinicDoctorProfile.getFirstNameWithTitle()));
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_ABOUT_DOCTOR, null, clinicDoctorProfile, progressLoading, ivContactProfile);
        }
    }

    @Override
    public void initViews(View convertView) {
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
        progressLoading = (ProgressBar) convertView.findViewById(R.id.progress_loading);
        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);


        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(clinicDoctorProfile);
                }
            });
    }


    @Override
    public void onClick(View v) {
    }
}
