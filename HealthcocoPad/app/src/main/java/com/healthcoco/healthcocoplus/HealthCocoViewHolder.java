package com.healthcoco.healthcocoplus;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Shreshtha on 24-01-2017.
 */

public abstract class HealthCocoViewHolder {
    protected static final String TAG = HealthCocoViewHolder.class.getSimpleName();
    protected HealthCocoApplication mApp;
    protected LayoutInflater inflater;
    protected HealthCocoActivity mActivity;

    public HealthCocoViewHolder() {
    }

    public HealthCocoViewHolder(HealthCocoActivity activity) {
        mActivity = activity;
        inflater = mActivity.getLayoutInflater();
        mApp = ((HealthCocoApplication) mActivity.getApplication());
    }

    public abstract void setData(Object object);

    public abstract void applyData();

    public abstract View getContentView();
}
