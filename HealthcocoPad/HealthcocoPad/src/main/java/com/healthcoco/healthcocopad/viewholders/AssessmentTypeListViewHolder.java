package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.AssessmentFormType;

/**
 * Created by Prashant on 18-09-2018.
 */

public class AssessmentTypeListViewHolder extends HealthCocoViewHolder {
    public static final String DATE_FORMAT = "dd MMM";
    private AssessmentFormType assessmentFormType;
    private TextView tvAssessmentType;
    private ImageView ivThumbnail;

    public AssessmentTypeListViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    public void setData(Object object) {
        this.assessmentFormType = (AssessmentFormType) object;
    }

    @Override
    public void applyData() {
        if (assessmentFormType != null) {
            tvAssessmentType.setText(assessmentFormType.getTitleId());
        } else tvAssessmentType.setText("");
    }

    @Override
    public View getContentView() {
        View view = (View) inflater.inflate(R.layout.list_item_assessment_type, null);
        initViews(view);
        initListeners();
        return view;
    }

    private void initViews(View view) {
        tvAssessmentType = (TextView) view.findViewById(R.id.tv_assessment_title);
        ivThumbnail = (ImageView) view.findViewById(R.id.iv_assessment_type);
    }

    private void initListeners() {

    }
}
