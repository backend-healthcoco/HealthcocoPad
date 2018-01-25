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
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.listeners.AssignGroupListener;
import com.healthcoco.healthcocopad.listeners.CBSelectedItemTypeListener;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 23/11/15.
 */
public class DoctorListViewHolder extends HealthCocoViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    CBSelectedItemTypeListener cbSelectedItemTypeListener;
    private HealthCocoActivity mActivity;
    private ClinicDoctorProfile objData;
    private LinearLayout layoutImage;
    private LinearLayout layoutDoctorList;
    private TextView tvName;
    private TextView tvInitialAlphabet;
    private ProgressBar progressLoading;
    private ImageView ivContactProfile;
    private CheckBox cbSelectDoctor;

    public DoctorListViewHolder(HealthCocoActivity mActivity, CBSelectedItemTypeListener cbSelectedItemTypeListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.cbSelectedItemTypeListener = cbSelectedItemTypeListener;
    }

    @Override
    public void setData(Object object) {
        this.objData = (ClinicDoctorProfile) object;
    }

    @Override
    public void applyData() {
        ClinicDoctorProfile clinicDoctorProfile = null;
        if (objData instanceof ClinicDoctorProfile)
            clinicDoctorProfile = (ClinicDoctorProfile) objData;
        if (clinicDoctorProfile != null) {
            tvName.setText(Util.getValidatedValue(clinicDoctorProfile.getFirstNameWithTitle()));
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_EMR_HEADER, clinicDoctorProfile, progressLoading, ivContactProfile, tvInitialAlphabet);
        }
        if (cbSelectedItemTypeListener.isInitialLaunch()) {
            if (cbSelectedItemTypeListener.getDoctorProfileArrayList().contains(clinicDoctorProfile.getUniqueId())) {
                cbSelectDoctor.setChecked(true);
                cbSelectedItemTypeListener.onCBSelectedItemTypeCheckClicked(true, objData);
            }
        } else {
            cbSelectedItemTypeListener.onCBSelectedItemTypeCheckClicked(cbSelectedItemTypeListener.isSelectAll(), objData);
            cbSelectDoctor.setChecked(cbSelectedItemTypeListener.isSelectAll());
        }
        layoutImage.setVisibility(View.VISIBLE);
    }

    @Override
    public View getContentView() {
        View convertView = inflater.inflate(R.layout.item_doctor_popup_list, null);
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

        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        cbSelectedItemTypeListener.onCBSelectedItemTypeCheckClicked(isChecked, objData);
    }

    @Override
    public void onClick(View v) {
        cbSelectDoctor.setChecked(!cbSelectDoctor.isChecked());
    }
}
