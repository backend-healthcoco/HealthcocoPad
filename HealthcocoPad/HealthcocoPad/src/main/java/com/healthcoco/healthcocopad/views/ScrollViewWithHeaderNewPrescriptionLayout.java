package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by neha on 03/02/17.
 */
public class ScrollViewWithHeaderNewPrescriptionLayout extends ScrollView implements
        ViewTreeObserver.OnScrollChangedListener {

    private static final String TAG = ScrollViewWithHeaderLayout.class.getSimpleName();
    private ArrayList<View> headerList;
    private RelativeLayout tvHeader;
    private LoadMorePageListener loadMoreListener;
    private HashMap<Integer, Boolean> headerVisibilityHashmap = new HashMap<>();
    private Button btHeaderInteraction;

    public ScrollViewWithHeaderNewPrescriptionLayout(Context context) {
        super(context);
        headerList = new ArrayList<View>();

    }

    public ScrollViewWithHeaderNewPrescriptionLayout(Context context, AttributeSet attribute) {
        super(context, attribute);
        headerList = new ArrayList<View>();

    }

    public ScrollViewWithHeaderNewPrescriptionLayout(Context context, AttributeSet attribute,
                                                     int defStyleAttr) {
        super(context, attribute, defStyleAttr);
        headerList = new ArrayList<View>();

    }

    public void build(HealthCocoActivity mActivity, OnClickListener onClickListener) {
        init(onClickListener);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private void init(OnClickListener onClickListener) {
        getViewTreeObserver().addOnScrollChangedListener(this);
    }
//
//    private void initListener(OnClickListener onClickListener) {

//        tvProfile = (TextView) tvHeader.findViewById(R.id.tv_profile);
//        tvVisit = (TextView) tvHeader.findViewById(R.id.tv_visit);
//        tvProfile.setOnClickListener(onClickListener);
//        tvVisit.setOnClickListener(onClickListener);
//    }

    public void addChildHeaders(RelativeLayout v) {
        headerList.add(v);
    }

    public void addChildHeaders(LinearLayout v) {
        headerList.add(v);
    }

    public void addFixedHeader(RelativeLayout v) {
        this.tvHeader = v;
        btHeaderInteraction = (Button) tvHeader.findViewById(R.id.bt_header_interaction);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (getTop() == t) {
            // reaches the top end
            LogUtils.LOGD(TAG, "ScrolView reached top true");
        } else
            LogUtils.LOGD(TAG, "ScrolView reached top false");
    }

    @Override
    public void onScrollChanged() {

        int position[] = new int[2];
        tvHeader.getLocationOnScreen(position);
        int tvHeaderY = position[1];
//        tvHeader.setVisibility(View.GONE);
        if (headerList != null & headerList.size() > 0) {
            for (View v : headerList) {
                if (headerVisibilityHashmap.get(v.getId()) != null && !headerVisibilityHashmap.get(v.getId()))
                    v.setVisibility(View.INVISIBLE);
                else
                    v.setVisibility(View.VISIBLE);
                if (v instanceof LinearLayout || v instanceof View) {
                    v.getLocationOnScreen(position);
                    if (position[1] <= tvHeaderY) {
                        LogUtils.LOGD(TAG, "Position at visible " + position[1] + " headerYPosition " + tvHeaderY);
                        Boolean isVisible = headerVisibilityHashmap.get(v.getId());
                        if (isVisible != null && isVisible) {
                            TextView textView = (TextView) tvHeader.getChildAt(0);
                            if (textView != null)
                                switch (v.getId()) {
                                    case R.id.tv_header_one:
                                        textView.setText(R.string.lab_test);
                                        btHeaderInteraction.setVisibility(View.INVISIBLE);
                                        break;
                                    case R.id.tv_header_two:
                                        textView.setText(R.string.drugs);
                                        btHeaderInteraction.setVisibility(View.VISIBLE);
                                        break;
                                }
                            tvHeader.setVisibility(View.VISIBLE);
                        }
//                        v.setVisibility(View.INVISIBLE);
//                        tvHeader.setText(((TextView) v).getText());
                    }
                }
            }
        }
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

    public void setHeaderVisiblilty(RelativeLayout headerLayout, boolean isHeaderVisible) {
        headerVisibilityHashmap.put(headerLayout.getId(), isHeaderVisible);
        if (isHeaderVisible)
            headerLayout.setVisibility(View.VISIBLE);
        else headerLayout.setVisibility(View.GONE);
        onScrollChanged();
    }

    public void setHeaderVisiblilty(LinearLayout headerLayout, boolean isHeaderVisible) {
        headerVisibilityHashmap.put(headerLayout.getId(), isHeaderVisible);
        if (isHeaderVisible)
            headerLayout.setVisibility(View.VISIBLE);
        else headerLayout.setVisibility(View.GONE);
        onScrollChanged();
    }

    public void showHeader(boolean b) {
        if (b)
            tvHeader.setVisibility(View.VISIBLE);
        else
            tvHeader.setVisibility(View.GONE);

    }
}
