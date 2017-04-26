package com.healthcoco.healthcocopad.dialogFragment;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.custom.DownloadFileFromUrlAsyncTask;
import com.healthcoco.healthcocopad.enums.HealthCocoFileType;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.listeners.NetworkImageViewRequestListener;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;

import static com.healthcoco.healthcocopad.HealthCocoActivity.REQUEST_PERMISSIONS;

public class EnlargedImageViewDialogFragment extends HealthCocoDialogFragment implements NetworkImageViewRequestListener, DownloadFileFromUrlListener {
    public static final String TAG_PRINT_PDF = "printPdf";
    public static final String TAG_IMAGE_URL = "image_url";
    private ProgressBar progressLoadingHorizontal;
    private ImageView ivImage;
    private ProgressBar progressLoadingCircular;
    private boolean isPrintPdf;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            dismiss();
        }
    }

    public void requestPermission() {
        requestAppPermissions(new
                String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        }, R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(getContext(), permission);
        }
        requestPermissions(requestedPermissions, requestCode);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        // Check if we're running on Android 5.0 or higher
//        if (Build.VERSION.SDK_INT >= 23) {
        requestPermission();
//        } else {
//            init();
//        }

    }

    @Override
    public void init() {
        ImageUtil.deleteFolderIfSizeExceed();
        initViews();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String imageUrl = bundle.getString(TAG_IMAGE_URL);
            isPrintPdf = bundle.getBoolean(TAG_PRINT_PDF);
            if (!Util.isNullOrBlank(imageUrl))
//                DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivImage, imageUrl);

                new DownloadFileFromUrlAsyncTask(mActivity, this, HealthCocoFileType.ENLARGED_IMAGE, Util.getFileNameFromUrl(imageUrl), progressLoadingCircular, progressLoadingHorizontal).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUrl);
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
    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            try {
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
    }
}
