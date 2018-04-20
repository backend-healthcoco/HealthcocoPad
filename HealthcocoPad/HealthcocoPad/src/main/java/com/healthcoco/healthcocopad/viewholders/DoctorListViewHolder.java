package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.listeners.AssignGroupListener;
import com.healthcoco.healthcocopad.listeners.CBSelectedItemTypeListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 23/11/15.
 */
public class DoctorListViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    CBSelectedItemTypeListener cbSelectedItemTypeListener;
    private HealthCocoActivity mActivity;
    private RegisteredDoctorProfile clinicDoctorProfile;
    private LinearLayout layoutImage;
    private LinearLayout layoutDoctorList;
    private TextView tvName;
    private TextView tvInitialAlphabet;
    private ProgressBar progressLoading;
    private ImageView ivContactProfile;
    private CheckBox cbSelectDoctor;

    public DoctorListViewHolder(HealthCocoActivity mActivity, View itemView, Object listenerObject) {
        super(mActivity, itemView);
        this.mActivity = mActivity;
        this.cbSelectedItemTypeListener = (CBSelectedItemTypeListener) listenerObject;
    }


    @Override
    public void applyData(Object object) {
        clinicDoctorProfile = null;
        if (object instanceof RegisteredDoctorProfile)
            clinicDoctorProfile = (RegisteredDoctorProfile) object;
        if (clinicDoctorProfile != null) {
            tvName.setText(Util.getValidatedValue(clinicDoctorProfile.getFirstNameWithTitle()));
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_EMR_HEADER, clinicDoctorProfile, progressLoading, ivContactProfile, tvInitialAlphabet);
        }
        if (cbSelectedItemTypeListener.isInitialLaunch()) {
            if (!Util.isNullOrEmptyList(cbSelectedItemTypeListener.getDoctorProfileArrayList()))
                if (cbSelectedItemTypeListener.getDoctorProfileArrayList().contains(clinicDoctorProfile.getUserId())) {
                    cbSelectDoctor.setChecked(true);
                    cbSelectedItemTypeListener.onCBSelectedItemTypeCheckClicked(true, clinicDoctorProfile);
                }
        } else {
            cbSelectedItemTypeListener.onCBSelectedItemTypeCheckClicked(cbSelectedItemTypeListener.isSelectAll(), clinicDoctorProfile);
            cbSelectDoctor.setChecked(cbSelectedItemTypeListener.isSelectAll());
        }
        layoutImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void initViews(View convertView) {
        layoutImage = (LinearLayout) convertView.findViewById(R.id.layout_image);
        layoutDoctorList = (LinearLayout) convertView.findViewById(R.id.layout_doctor_list);
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
        progressLoading = (ProgressBar) convertView.findViewById(R.id.progress_loading);
        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);
        cbSelectDoctor = (CheckBox) convertView.findViewById(R.id.cb_select_doctor);
        layoutDoctorList.setOnClickListener(this);

        cbSelectDoctor.setOnCheckedChangeListener(this);
        convertView.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        cbSelectedItemTypeListener.onCBSelectedItemTypeCheckClicked(isChecked, clinicDoctorProfile);
    }

    @Override
    public void onClick(View v) {
        cbSelectDoctor.setChecked(!cbSelectDoctor.isChecked());
    }
}
