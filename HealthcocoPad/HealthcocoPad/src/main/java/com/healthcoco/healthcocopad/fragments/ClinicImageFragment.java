package com.healthcoco.healthcocopad.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.ClinicImage;
import com.healthcoco.healthcocopad.custom.DownloadFileFromUrlAsyncTask;
import com.healthcoco.healthcocopad.enums.HealthCocoFileType;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 01/02/16.
 */
public class ClinicImageFragment extends HealthCocoFragment implements DownloadFileFromUrlListener, View.OnClickListener {
    private ImageView ivClinicImage;
    private ClinicImage clinicImage;
    private ProgressBar progressLoading;

    public ClinicImageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_clinic_image, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        Bundle bundle = getArguments();
        String uniqueId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
        clinicImage = LocalDataServiceImpl.getInstance(mApp).getClinicImage(uniqueId);
        if (clinicImage != null) {
            initViews();
            initListeners();
            loadClinicImage();
        }
    }

    private void loadClinicImage() {
        if (!Util.isNullOrBlank(clinicImage.getImageUrl()))
            new DownloadFileFromUrlAsyncTask(mActivity, this, HealthCocoFileType.CLINIC_IMAGE, Util.getFileNameFromUrl(clinicImage.getImageUrl()), null, null).execute(clinicImage.getImageUrl());
    }

    @Override
    public void initViews() {
        ivClinicImage = (ImageView) view.findViewById(R.id.iv_clinic_image);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
    }

    @Override
    public void initListeners() {
        ivClinicImage.setOnClickListener(this);
    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            int width = ScreenDimensions.SCREEN_WIDTH;
            LogUtils.LOGD(TAG, "Image SIze " + width);
            Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, width);
            if (bitmap != null) {
                ivClinicImage.setImageBitmap(bitmap);
                ivClinicImage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clinic_image:
                mActivity.openEnlargedImageDialogFragment(clinicImage.getImageUrl());
                break;
        }
    }
}
