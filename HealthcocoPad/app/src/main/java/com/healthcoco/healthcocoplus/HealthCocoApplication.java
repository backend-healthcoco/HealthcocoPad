package com.healthcoco.healthcocoplus;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
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
    private Tracker mTracker;
    private ArrayList<Activity> listLoginSignUpActivity = new ArrayList<>();
    private ArrayList<Request> requestList = new ArrayList<>();

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

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
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
//        if (gcmRefreshListener != null)
//            gcmRefreshListener.refreshGCM(false);
    }
}
