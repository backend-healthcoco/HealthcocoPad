package com.healthcoco.healthcocoplus.custom;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.enums.HealthCocoFileType;
import com.healthcoco.healthcocoplus.fragments.DoctorProfileFragment;
import com.healthcoco.healthcocoplus.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.ImageUtil;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Shreshtha on 02-02-2017.
 */
public class DownloadFileFromUrlAsyncTask extends AsyncTask<String, String, String> {
    private static final String TAG = DownloadFileFromUrlAsyncTask.class.getSimpleName();
    private static final int ERROR = R.string.error_loading_image;
    private ProgressBar progressBarCircular;
    private ProgressBar progressBarHorizontal;
    private String fileName;
    private HealthCocoFileType healthCocoFileType;
    //    private ProgressDialog pDialog;
    private DownloadFileFromUrlListener urlListener;
    private HealthCocoActivity mActivity;
    private String urlString;

    public DownloadFileFromUrlAsyncTask(HealthCocoActivity mActivity, DownloadFileFromUrlListener urlListener, HealthCocoFileType healthCocoFileType, String fileName, ProgressBar progressBarCircular, ProgressBar progressBarHorizontal) {
        this.mActivity = mActivity;
        this.urlListener = urlListener;
        this.healthCocoFileType = healthCocoFileType;
        this.fileName = fileName;
        this.progressBarHorizontal = progressBarHorizontal;
        this.progressBarCircular = progressBarCircular;
//        pDialog = new ProgressDialog(mActivity);
//        pDialog.setMessage("Downloading file. Please wait...");
//        pDialog.setIndeterminate(false);
//        pDialog.setMax(100);
//        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        pDialog.setCancelable(true);
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Util.checkNetworkStatus(mActivity);
        urlListener.onPreExecute();
        if (progressBarHorizontal != null)
            progressBarHorizontal.setVisibility(View.VISIBLE);
        showProgressCircular();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        String pathToSaveAndGet = ImageUtil.getPathToSaveFile(healthCocoFileType, fileName, Util.getFileExtension(f_url[0]));
        if (!ImageUtil.isFileExists(pathToSaveAndGet)) {
            if (HealthCocoConstants.isNetworkOnline) {
                try {

                    this.urlString = f_url[0];
                    urlString = urlString.replace(" ", "%20");
                    LogUtils.LOGD(TAG, "File Url : " + urlString + " Type : " + healthCocoFileType);
                    URL url = new URL(urlString);
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    int lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    // Output stream to write file
                    OutputStream output = new FileOutputStream(pathToSaveAndGet);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                    return pathToSaveAndGet;
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                    return null;
                }
            } else
                return null;
        }
        return pathToSaveAndGet;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        if (progressBarHorizontal != null) {
            hideProgressCircular();
            progressBarHorizontal.setProgress(Integer.parseInt(progress[0]));
        }

    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String path) {
        try {
            if (Util.isNullOrBlank(path)) {
                if (!HealthCocoConstants.isNetworkOnline)
                    Util.showToast(mActivity, R.string.user_offline);
//                else Util.showToast(mActivity, ERROR);
            }
            LogUtils.LOGD(TAG, "File saved at path : " + path);
            if (progressBarHorizontal != null)
                progressBarHorizontal.setVisibility(View.GONE);
            hideProgressCircular();
            urlListener.onPostExecute(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showProgressCircular() {
        if (progressBarCircular != null)
            progressBarCircular.setVisibility(View.VISIBLE);

    }

    private void hideProgressCircular() {
        if (progressBarCircular != null)
            progressBarCircular.setVisibility(View.GONE);

    }
}
