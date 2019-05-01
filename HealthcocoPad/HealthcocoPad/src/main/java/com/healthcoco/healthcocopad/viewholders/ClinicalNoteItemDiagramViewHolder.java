package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;


/**
 * Created by Mohit on 28/02/16.
 */
public class ClinicalNoteItemDiagramViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {
    private static final String TAG = ClinicalNoteItemDiagramViewHolder.class.getSimpleName();
    private Diagram diagram;
    private ImageView ivDiagram;
    private ProgressBar progressLoading;
    private HealthCocoActivity mActivity;
    private TextView tvTag;

    public ClinicalNoteItemDiagramViewHolder(HealthCocoActivity mActivity, View itemView) {
        super(mActivity, itemView);
    }

//    public ClinicalNoteItemDiagramViewHolder(Context context) {
//        super(context);
//        init(context);
//    }
//
//
//    public ClinicalNoteItemDiagramViewHolder(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    public ClinicalNoteItemDiagramViewHolder(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    private void init(Context context) {
//        this.mActivity = (HealthCocoActivity) context;
//        inflate(context, R.layout.item_clinical_note_diagram, this);
//        initViews();
//        initListeners();
//    }
//
//    private void initViews() {
//        ivDiagram = (ImageView) findViewById(R.id.iv_diagram);
//        progressLoading = (ProgressBar) findViewById(R.id.progress_loading);
//        tvTag = (TextView) findViewById(R.id.tv_tag);
//    }
//
//    private void initListeners() {
//        setOnClickListener(this);
//    }

    public void initData(Diagram diagram) {
        this.diagram = diagram;

    }

    @Override
    public void onClick(View v) {
        mActivity.openEnlargedImageDialogFragment(diagram.getDiagramUrl());
    }

    //    @Override
//    public void setData(Object object) {
//        this.diagram = (Diagram) object;
//    }
//
//    @Override
//    public void applyData() {
//        ivDiagram.setBackgroundResource(R.drawable.img_diagnosis);
//        if (!Util.isNullOrBlank(diagram.getTags())) {
//            tvTag.setVisibility(View.VISIBLE);
//            tvTag.setText(diagram.getTags());
//        } else
//            tvTag.setVisibility(View.GONE);
//        DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivDiagram, diagram.getDiagramUrl());
//    }
//
    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.item_clinical_note_diagram, null);
        ivDiagram = (ImageView) contentView.findViewById(R.id.iv_diagram);
        progressLoading = (ProgressBar) contentView.findViewById(R.id.progress_loading);
        tvTag = (TextView) contentView.findViewById(R.id.tv_tag);
        contentView.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void initViews(View contentView) {
        ivDiagram = (ImageView) contentView.findViewById(R.id.iv_diagram);
        progressLoading = (ProgressBar) contentView.findViewById(R.id.progress_loading);
        tvTag = (TextView) contentView.findViewById(R.id.tv_tag);
    }

    @Override
    public void applyData(Object object) {
        this.diagram = (Diagram) object;
        ivDiagram.setBackgroundResource(R.drawable.img_diagnosis);
        if (!Util.isNullOrBlank(diagram.getTags())) {
            tvTag.setVisibility(View.VISIBLE);
            tvTag.setText(diagram.getTags());
        } else
            tvTag.setVisibility(View.GONE);
        DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivDiagram, diagram.getDiagramUrl());
    }
}
