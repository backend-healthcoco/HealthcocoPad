package com.healthcoco.healthcocopad.viewholders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.listeners.DiagramsGridItemListener;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

public class DiagramGridViewHolder extends HealthCocoViewHolder implements DownloadFileFromUrlListener {

    private static final String TAG = DiagramGridViewHolder.class.getSimpleName();
    private DiagramsGridItemListener diagramsGridItemListener;
    private Diagram diagram;
    private TextView tvtag;
    private ImageView btDiagram;
    private ProgressBar progressLoading;
    private ImageView ivDiagram;

    public DiagramGridViewHolder(HealthCocoActivity activity, DiagramsGridItemListener diagramsGridItemListener) {
        super(activity);
        this.diagramsGridItemListener = diagramsGridItemListener;
    }

    @Override
    public void setData(Object object) {
        diagram = (Diagram) object;
    }

    @Override
    public void applyData() {
        if (!Util.isNullOrBlank(diagram.getTags())) {
            tvtag.setVisibility(View.VISIBLE);
            tvtag.setText(diagram.getTags());
        } else {
            tvtag.setVisibility(View.GONE);
        }
        if (!Util.isNullOrBlank(diagram.getDiagramUrl())) {
            DownloadImageFromUrlUtil.loadImageUsingImageLoaderUsingDefaultImage(R.color.white, ivDiagram, diagram.getDiagramUrl(), null);
        } else {
            progressLoading.setVisibility(View.GONE);
        }

    }

    @Override
    public View getContentView() {
        View convertView = inflater.inflate(R.layout.grid_item_diagram, null);
        tvtag = (TextView) convertView.findViewById(R.id.tv_tag);
        ivDiagram = (ImageView) convertView.findViewById(R.id.iv_diagram);
        progressLoading = (ProgressBar) convertView.findViewById(R.id.progress_loading);
        return convertView;
    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            if (!Util.isNullOrBlank(diagram.getDiagramFilePath()) && diagram.getDiagramFilePath().equalsIgnoreCase(filePath)) {
                int width = ivDiagram.getLayoutParams().width;
                LogUtils.LOGD(TAG, "Image SIze " + width);
                Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, width);
                if (bitmap != null) {
                    ivDiagram.setImageBitmap(bitmap);
                    ivDiagram.setVisibility(View.VISIBLE);
                    return;
                }
            }
        }
        diagramsGridItemListener.onErrorImageLoading(diagram);
    }

    @Override
    public void onPreExecute() {

    }
}
