package com.healthcoco.healthcocopad.custom;

import android.os.AsyncTask;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListener;


/**
 * Created by neha on 01/11/15.
 */
public class LocalDataBackgroundTask extends AsyncTask<Void, Void, VolleyResponseBean> {
    private final LocalBackgroundTaskType localBackgroundTaskType;
    private LocalDoInBackgroundListener localDoInBackgroundListener;
    private HealthCocoActivity mActivity;

    public LocalDataBackgroundTask(HealthCocoActivity mActivity, LocalBackgroundTaskType localBackgroundTaskType, LocalDoInBackgroundListener localDoInBackgroundListener) {
        this.mActivity = mActivity;
        this.localBackgroundTaskType = localBackgroundTaskType;
        this.localDoInBackgroundListener = localDoInBackgroundListener;
    }

    @Override
    protected void onPreExecute() {
        //TODO comment loading/updating messsage while saving in local
    }

    @Override
    protected VolleyResponseBean doInBackground(Void... params) {
        VolleyResponseBean volleyResponseBean = localDoInBackgroundListener.doInBackground(localBackgroundTaskType);
        return volleyResponseBean;
    }

    @Override
    protected void onPostExecute(VolleyResponseBean aVoid) {
        super.onPostExecute(aVoid);
        localDoInBackgroundListener.onPostExecute(aVoid);
    }
}