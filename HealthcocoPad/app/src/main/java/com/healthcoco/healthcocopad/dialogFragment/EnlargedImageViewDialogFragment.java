package com.healthcoco.healthcocopad.dialogFragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.listeners.NetworkImageViewRequestListener;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;

public class EnlargedImageViewDialogFragment extends HealthCocoDialogFragment implements NetworkImageViewRequestListener, DownloadFileFromUrlListener {
    public static final String TAG_PRINT_PDF = "printPdf";
    public static final String TAG_IMAGE_URL = "image_url";
    private ProgressBar progressLoadingHorizontal;
    private ImageView ivImage;
    private ProgressBar progressLoadingCircular;
    private boolean isPrintPdf;
    private String filePath;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_enlarged_image, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ImageUtil.deleteFolderIfSizeExceed();
        init();
    }

    @Override
    public void init() {
        initViews();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String imageUrl = bundle.getString(TAG_IMAGE_URL);
            isPrintPdf = bundle.getBoolean(TAG_PRINT_PDF);
            if (!Util.isNullOrBlank(imageUrl))
                DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivImage, imageUrl);

//            new DownloadFileFromUrlAsyncTask(mActivity, this, HealthCocoFileType.ENLARGED_IMAGE, Util.getFileNameFromUrl(imageUrl), progressLoadingCircular, progressLoadingHorizontal).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUrl);
        } else dismiss();
    }


    @Override
    public void initViews() {
        progressLoadingHorizontal = (ProgressBar) view.findViewById(R.id.progress_loading_horizontal);
        progressLoadingCircular = (ProgressBar) view.findViewById(R.id.progress_loading_circular);
        ivImage = (ImageView) view.findViewById(R.id.iv_image);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onSuccessResponse() {
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        dismiss();
//        Util.showToast(mActivity, getResources().getString(R.string.unable_to_load_image));
    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            try {
                this.filePath = filePath;
                Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, ScreenDimensions.SCREEN_WIDTH, ScreenDimensions.SCREEN_HEIGHT);
                if (bitmap != null) {
                    ivImage.setVisibility(View.VISIBLE);
                    ivImage.setImageBitmap(bitmap);
                } else {
                    if (isPrintPdf) {
                        mActivity.doPrinting(filePath);
                    } else {
                        Util.openFile(mActivity, filePath);
                    }
                    dismiss();
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        onErrorResponse(null);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (isPrintPdf && !Util.isNullOrBlank(filePath)) {
//            ImageUtil.deleteFileFrom(filePath);
//        }
    }
}
