package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by neha on 25/11/15.
 */
public class ScrollViewWithHeader extends ScrollView implements
        OnScrollChangedListener {

    private static final String TAG = ScrollViewWithHeader.class.getSimpleName();
    private Context mContext;
    private ArrayList<View> headerList;
    private TextView tvHeader;
    private LoadMorePageListener loadMoreListener;
    private HashMap<Integer, Boolean> headerVisibilityHashmap = new HashMap<>();

    public ScrollViewWithHeader(Context context) {
        super(context);
        this.mContext = context;
        headerList = new ArrayList<View>();

    }

    public ScrollViewWithHeader(Context context, AttributeSet attribute) {
        super(context, attribute);
        this.mContext = context;
        headerList = new ArrayList<View>();

    }

    public ScrollViewWithHeader(Context context, AttributeSet attribute,
                                int defStyleAttr) {
        super(context, attribute, defStyleAttr);
        this.mContext = context;
        headerList = new ArrayList<View>();

    }

    public void build() {
        init();
    }

    private void init() {
        initMember();
        initListener();
    }

    private void initMember() {
        if (tvHeader != null) {
            if (headerList != null && headerList.size() > 0) {
                TextView textView = (TextView) headerList.get(0);
                String firstHeader = (String) textView.getText();
                tvHeader.setText(firstHeader);
            }
        }
    }

    private void initListener() {
        getViewTreeObserver().addOnScrollChangedListener(this);
    }

    public void addChildHeaders(View v) {
        headerList.add(v);
    }

    public void addFixedHeader(TextView v) {
        this.tvHeader = v;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Util.hideKeyboard(getContext(), this);
    }

    @Override
    public void onScrollChanged() {
        int position[] = new int[2];
        tvHeader.getLocationOnScreen(position);
        int tvHeaderY = position[1];
        tvHeader.setVisibility(View.GONE);
        if (headerList != null & headerList.size() > 0) {
            for (View v : headerList) {

                if (v instanceof TextView) {
                    v.getLocationOnScreen(position);

                    if (position[1] <= tvHeaderY) {
                        Boolean isVisible = headerVisibilityHashmap.get(v.getId());
                        if (isVisible != null && isVisible) {
                            tvHeader.setVisibility(View.VISIBLE);
                            tvHeader.setText(((TextView) v).getText());
                        }
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

        // if diff is zero, then the bottom has been reached
//        if (diff == 0 && loadMoreListener != null) {
        if (diff == 0 && loadMoreListener != null && !loadMoreListener.isEndOfListAchieved()) {
            loadMoreListener.loadMore();
        }
    }

    public void setLoadMoreListener(LoadMorePageListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setHeaderVisiblilty(int headerId, boolean isHeaderVisible) {
        headerVisibilityHashmap.put(headerId, isHeaderVisible);
        onScrollChanged();
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }
}
