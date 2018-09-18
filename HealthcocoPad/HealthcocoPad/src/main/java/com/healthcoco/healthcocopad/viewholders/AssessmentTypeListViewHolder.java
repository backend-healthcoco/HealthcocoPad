package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.DoctorVideos;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 01-08-2017.
 */

public class AssessmentTypeListViewHolder extends HealthCocoViewHolder {
    public static final String DATE_FORMAT = "dd MMM";
    private DoctorVideos doctorVideos;
    private TextView tvVideoName;
    private TextView tvDate;
    private ImageView ivThumbnail;

    public AssessmentTypeListViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    public void setData(Object object) {
        this.doctorVideos = (DoctorVideos) object;
    }

    @Override
    public void applyData() {
        if (doctorVideos != null) {
            if (!Util.isNullOrBlank(doctorVideos.getName())) {
                tvVideoName.setText(doctorVideos.getName());
            } else tvVideoName.setText("");
            if (doctorVideos.getCreatedTime() != null) {
                tvDate.setText(DateTimeUtil.getFormatedDateAndTime(DATE_FORMAT, doctorVideos.getCreatedTime()));
            } else tvDate.setText("");
        }
    }

    @Override
    public View getContentView() {
        View view = (View) inflater.inflate(R.layout.list_item_doctor_video, null);
        initViews(view);
        initListeners();
        return view;
    }

    private void initViews(View view) {
        tvVideoName = (TextView) view.findViewById(R.id.tv_video_name);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
    }

    private void initListeners() {

    }
}
