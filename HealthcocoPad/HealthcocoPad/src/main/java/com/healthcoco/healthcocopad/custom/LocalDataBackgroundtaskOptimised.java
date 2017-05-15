package com.healthcoco.healthcocopad.custom;

import android.os.AsyncTask;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 */
public class LocalDataBackgroundtaskOptimised extends AsyncTask<VolleyResponseBean, VolleyResponseBean, VolleyResponseBean> {
    private LocalBackgroundTaskType localBackgroundTaskType;
    private GsonRequest.ErrorListener errorListener;
    private Response.Listener<VolleyResponseBean> responseListener;
    private LocalDoInBackgroundListenerOptimised localDoInBackgroundListener;
    private HealthCocoActivity mActivity;

    public LocalDataBackgroundtaskOptimised(HealthCocoActivity mActivity, LocalBackgroundTaskType localBackgroundTaskType, LocalDoInBackgroundListenerOptimised localDoInBackgroundListener, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        this.mActivity = mActivity;
        this.localBackgroundTaskType = localBackgroundTaskType;
        this.localDoInBackgroundListener = localDoInBackgroundListener;
        this.responseListener = responseListener;
        this.errorListener = errorListener;
    }
    @Override
    protected void onPreExecute() {
        //TODO comment loading/updating messsage while saving in local
//        mActivity.showLoading(mActivity.getResources().getString(R.string.updating_database));

    }

    @Override
    protected VolleyResponseBean doInBackground(VolleyResponseBean... response) {
        VolleyResponseBean responseObject = new VolleyResponseBean();
        if (response != null && response.length > 0) {
            responseObject = response[0];
            if (responseObject == null)
                responseObject = new VolleyResponseBean();
        }
        responseObject.setLocalBackgroundTaskType(localBackgroundTaskType);
        VolleyResponseBean volleyResponseBean = localDoInBackgroundListener.doInBackground(responseObject);
        if (volleyResponseBean != null)
            volleyResponseBean.setIsFromLocalAfterApiSuccess(responseObject.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    protected void onPostExecute(VolleyResponseBean volleyResponseBean) {
        super.onPostExecute(volleyResponseBean);
        if (volleyResponseBean != null) {
            if (!Util.isNullOrBlank(volleyResponseBean.getErrMsg()))
                errorListener.onErrorResponse(volleyResponseBean, "");
            else
                responseListener.onResponse(volleyResponseBean);
        }
        localDoInBackgroundListener.onPostExecute(volleyResponseBean);
    }
}
