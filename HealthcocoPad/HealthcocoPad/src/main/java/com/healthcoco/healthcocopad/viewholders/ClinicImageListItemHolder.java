package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.ClinicImage;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.listeners.CBSelectedItemTypeListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 23/11/15.
 */
public class ClinicImageListItemHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {

    private HealthCocoActivity mActivity;
    private ClinicImage clinicImage;
    private TextView tvName;
    private TextView tvInitialAlphabet;
    private ProgressBar progressLoading;
    private ImageView ivContactProfile;

    public ClinicImageListItemHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
    }


    @Override
    public void applyData(Object object) {
        clinicImage = null;
        if (object instanceof ClinicImage)
            clinicImage = (ClinicImage) object;
        if (clinicImage != null) {
            DownloadImageFromUrlUtil.loadImageUsingImageLoader(progressLoading, ivContactProfile, clinicImage.getImageUrl());
        }
    }

    @Override
    public void initViews(View convertView) {
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
        progressLoading = (ProgressBar) convertView.findViewById(R.id.progress_loading);
        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);

        tvName.setVisibility(View.GONE);

        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(clinicImage);
                }
            });
    }


    @Override
    public void onClick(View v) {
    }
}
