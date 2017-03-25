package com.healthcoco.healthcocopad;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.healthcoco.healthcocopad.listeners.GCMRefreshListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.orm.SugarContext;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Shreshtha on 20-01-2017.
 */

public class HealthCocoApplication extends MultiDexApplication {
    private static final String TAG = HealthCocoApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ArrayList<Activity> listLoginSignUpActivity = new ArrayList<>();
    private ArrayList<Request> requestList = new ArrayList<>();
    private GCMRefreshListener gcmRefreshListener;

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        initImageLoader();
        Fabric.with(this, new Crashlytics());
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        getRequestQueue().getCache().remove(tag);
        cancelPendingRequests(tag);
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
        requestList.add(req);
        LogUtils.LOGD(TAG, "RequestList size : " + requestList.size() + " Added : " + req.getTag());
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void cancelAllPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
        cancelAllPendingRequests();
    }

    public void clearLoginSignupActivityStack() {
        for (Activity activity :
                listLoginSignUpActivity) {
            activity.finish();
        }
        listLoginSignUpActivity.clear();
    }

    public void addActivityToStack(Activity activity) {
        listLoginSignUpActivity.add(activity);
    }

    public void initImageLoader() {
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
    }

    public void removeVolleyRequest(GsonRequest gsonRequest) {
        if (requestList.contains(gsonRequest))
            requestList.remove(gsonRequest);
        LogUtils.LOGD(TAG, "RequestList size : " + requestList.size() + "Removed : " + gsonRequest.getTag());
        if (gcmRefreshListener != null)
            gcmRefreshListener.refreshGCM(false);
    }


    public boolean isEmptyRequestsList() {
        LogUtils.LOGD(TAG, "RequestList size : " + requestList.size());
        return Util.isNullOrEmptyList(requestList);
    }

    public void setGcmRegistrationListener(GCMRefreshListener gcmRefreshListener) {
        this.gcmRefreshListener = gcmRefreshListener;
    }

}
