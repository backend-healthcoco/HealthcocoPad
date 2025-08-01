package com.healthcoco.healthcocopad.viewholders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.ClinicImage;
import com.healthcoco.healthcocopad.custom.DownloadFileFromUrlAsyncTask;
import com.healthcoco.healthcocopad.enums.HealthCocoFileType;
import com.healthcoco.healthcocopad.listeners.AddEditClinicImageListener;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 02/02/16.
 */
public class ClinicImageGridItemViewholder extends HealthCocoViewHolder implements View.OnClickListener, DownloadFileFromUrlListener {

    private static final String TAG = ClinicImageGridItemViewholder.class.getSimpleName();
    private AddEditClinicImageListener addEditClinicImageListener;
    private ClinicImage clinicImage;
    private ImageView ivImage;
    private ImageButton btDelete;
    private ProgressBar progressLoading;

    public ClinicImageGridItemViewholder(HealthCocoActivity activity, AddEditClinicImageListener addEditClinicImageListener) {
        super(activity);
        this.addEditClinicImageListener = addEditClinicImageListener;
    }

    @Override
    public void setData(Object object) {
        if (object instanceof ClinicImage) {
            clinicImage = (ClinicImage) object;
            if (!Util.isNullOrBlank(clinicImage.getImageUrl()) && !Util.isNullOrBlank(clinicImage.getThumbnailUrl()))
                new DownloadFileFromUrlAsyncTask(mActivity, this, HealthCocoFileType.CLINIC_IMAGE, Util.getFileNameFromUrl(clinicImage.getThumbnailUrl()), progressLoading, null).execute(clinicImage.getImageUrl());
        } else if (object instanceof Bitmap) {
            progressLoading.setVisibility(View.GONE);
            ivImage.setImageBitmap((Bitmap) object);
        }
    }

    /**
     * dont call apply data(Since aplly data is implementation is done in setData(Object) method
     */

    @Override
    public void applyData() {
    }

    @Override
    public View getContentView() {
        View convertView = inflater.inflate(R.layout.grid_item_clinic_image, null);
        ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
        btDelete = (ImageButton) convertView.findViewById(R.id.bt_delete);
        progressLoading = (ProgressBar) convertView.findViewById(R.id.progress_loading);
        btDelete.setOnClickListener(this);
        return convertView;
    }

    public View getAddImageContentView() {
        View convertView = inflater.inflate(R.layout.grid_item_add_clinic_image, null);
        LinearLayout btAdd = (LinearLayout) convertView.findViewById(R.id.bt_add);
        btAdd.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_add) {
            addEditClinicImageListener.onAddClinicImageClicked();
        } else if (v.getId() == R.id.bt_delete) {
            addEditClinicImageListener.onDeleteImageClicked(clinicImage);
        }
    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            int width = ScreenDimensions.SCREEN_WIDTH;
            LogUtils.LOGD(TAG, "Image SIze " + width);
            Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, width);
            if (bitmap != null) {
                ivImage.setImageBitmap(bitmap);
                ivImage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPreExecute() {

    }
}
