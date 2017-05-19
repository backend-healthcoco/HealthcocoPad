package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.healthcoco.healthcocopad.utilities.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by neha on 24/01/17.
 */
public class ScrollViewWithRelativeLayoutHeader extends ScrollView implements
        ViewTreeObserver.OnScrollChangedListener {

    private static final String TAG = ScrollViewWithRelativeLayoutHeader.class.getSimpleName();
    private Context mContext;
    private ArrayList<View> headerList;
    private View tvHeader;
    private HashMap<Integer, Boolean> headerVisibilityHashmap = new HashMap<>();

    public ScrollViewWithRelativeLayoutHeader(Context context) {
        super(context);
        this.mContext = context;
        headerList = new ArrayList<View>();

    }

    public ScrollViewWithRelativeLayoutHeader(Context context, AttributeSet attribute) {
        super(context, attribute);
        this.mContext = context;
        headerList = new ArrayList<View>();

    }

    public ScrollViewWithRelativeLayoutHeader(Context context, AttributeSet attribute,
                                              int defStyleAttr) {
        super(context, attribute, defStyleAttr);
        this.mContext = context;
        headerList = new ArrayList<View>();

    }

    public void build() {
        init();
    }

    private void init() {
        initListener();
    }

    private void initListener() {
        getViewTreeObserver().addOnScrollChangedListener(this);
    }

    public void addChildHeaders(View v) {
        headerList.add(v);
    }

    public void addFixedHeader(View v) {
        this.tvHeader = v;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onScrollChanged() {
        int position[] = new int[2];
        tvHeader.getLocationOnScreen(position);
        int tvHeaderY = position[1];
        tvHeader.setVisibility(View.GONE);
        if (headerList != null & headerList.size() > 0) {
            for (View v : headerList) {

                if (v instanceof RelativeLayout) {
                    v.getLocationOnScreen(position);

                    if (position[1] <= tvHeaderY) {
                        tvHeader.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        LogUtils.LOGD(TAG, "Position top " + getTop());
        LogUtils.LOGD(TAG, "Position x " + getScrollX());
        LogUtils.LOGD(TAG, "Position y " + getScrollY());
        // We take the last son in the scrollview
        View view = (View) getChildAt(getChildCount() - 1);
        int diff = (view.getBottom() - (getHeight() + getScrollY()));
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }
}
