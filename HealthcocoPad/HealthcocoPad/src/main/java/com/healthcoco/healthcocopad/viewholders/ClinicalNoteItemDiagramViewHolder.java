package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;


/**
 * Created by Mohit on 28/02/16.
 */
public class ClinicalNoteItemDiagramViewHolder extends LinearLayout implements View.OnClickListener {
    private static final String TAG = ClinicalNoteItemDiagramViewHolder.class.getSimpleName();
    private Diagram diagram;
    private ImageView ivDiagram;
    private ProgressBar progressLoading;
    private HealthCocoActivity mActivity;
    private TextView tvTag;

    public ClinicalNoteItemDiagramViewHolder(Context context) {
        super(context);
        init(context);
    }


    public ClinicalNoteItemDiagramViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClinicalNoteItemDiagramViewHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (HealthCocoActivity) context;
        inflate(context, R.layout.item_clinical_note_diagram, this);
        initViews();
        initListeners();
    }

    private void initViews() {
        ivDiagram = (ImageView) findViewById(R.id.iv_diagram);
        progressLoading = (ProgressBar) findViewById(R.id.progress_loading);
        tvTag = (TextView) findViewById(R.id.tv_tag);
    }

    private void initListeners() {
        setOnClickListener(this);
    }

    public void initData(Diagram diagram) {
        this.diagram = diagram;
        ivDiagram.setBackgroundResource(R.drawable.img_diagnosis);
        if (!Util.isNullOrBlank(diagram.getTags())) {
            tvTag.setVisibility(View.VISIBLE);
            tvTag.setText(diagram.getTags());
        } else
            tvTag.setVisibility(View.GONE);
        DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivDiagram, diagram.getDiagramUrl());
    }

    @Override
    public void onClick(View v) {
        mActivity.openEnlargedImageDialogFragment(diagram.getDiagramUrl());
    }
}
