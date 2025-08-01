package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.PatientEducationVideo;
import com.healthcoco.healthcocopad.listeners.PatientVideoListItemClickListeners;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 01-08-2017.
 */

public class PatientEducationVideosListViewHolder extends HealthCocoViewHolder implements View.OnClickListener {
    public static final String DATE_FORMAT = "dd MMM";
    private PatientEducationVideo patientEducationVideo;
    private TextView tvVideoName;
    private TextView tvDate;
    private ImageView ivThumbnail;
    private ImageView ivEdit;
    private ImageView ivDiscard;
    private LinearLayout layoutEdit;
    private PatientVideoListItemClickListeners itemClickListeners;
    private boolean isFromSettings;

    public PatientEducationVideosListViewHolder(HealthCocoActivity mActivity, PatientVideoListItemClickListeners itemClickListeners) {
        super(mActivity);
        this.mActivity = mActivity;
        this.itemClickListeners = itemClickListeners;
        isFromSettings = itemClickListeners.isFromSetting();
    }

    @Override
    public void setData(Object object) {
        this.patientEducationVideo = (PatientEducationVideo) object;
    }

    @Override
    public void applyData() {
        if (patientEducationVideo != null) {
            if (!Util.isNullOrBlank(patientEducationVideo.getName())) {
                tvVideoName.setText(patientEducationVideo.getName());
            } else tvVideoName.setText("");
            if (patientEducationVideo.getCreatedTime() != null) {
                tvDate.setText(DateTimeUtil.getFormatedDateAndTime(DATE_FORMAT, patientEducationVideo.getUpdatedTime()));
            } else tvDate.setText("");

            if (!Util.isNullOrBlank(patientEducationVideo.getVideoUrl())) {
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(patientEducationVideo.getVideoUrl(), MediaStore.Video.Thumbnails.MINI_KIND);
                ivThumbnail.setImageBitmap(thumb);
            }
        }
    }

    @Override
    public View getContentView() {
        View view = (View) inflater.inflate(R.layout.list_item_patient_education_video, null);
        initViews(view);
        initListeners();
        return view;
    }

    private void initViews(View view) {
        tvVideoName = (TextView) view.findViewById(R.id.tv_video_name);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
        ivEdit = (ImageView) view.findViewById(R.id.iv_edit);
        ivDiscard = (ImageView) view.findViewById(R.id.iv_discard);
        layoutEdit = (LinearLayout) view.findViewById(R.id.layout_edit);

        if (isFromSettings)
            layoutEdit.setVisibility(View.VISIBLE);
        else
            layoutEdit.setVisibility(View.GONE);
    }

    private void initListeners() {
        ivEdit.setOnClickListener(this);
        ivDiscard.setOnClickListener(this);
        ivThumbnail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.iv_edit) {
            itemClickListeners.onEditClicked(patientEducationVideo);

        } else if (id == R.id.iv_discard) {
            showConfirmationAlert(
                    mActivity.getResources().getString(R.string.confirm_discard_clinical_notes_message),
                    mActivity.getResources().getString(R.string.confirm_discard_video)
            );

        } else if (id == R.id.iv_thumbnail) {
            itemClickListeners.onVideoClicked(patientEducationVideo);
        }

    }

    private void showConfirmationAlert(String title, String msg) {
        if (Util.isNullOrBlank(title))
            title = mActivity.getResources().getString(R.string.confirm);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                switch (viewId) {
//                    case R.id.bt_discard:
                itemClickListeners.onDiscardClicked(patientEducationVideo);
//                        break;
//                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }
}
