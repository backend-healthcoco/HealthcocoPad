package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.PatientDetailListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;

import java.util.ArrayList;

/**
 * Created by Mohit on 02/03/16.
 */
public class ScrollViewWithHeaderLayout extends ScrollView implements
        ViewTreeObserver.OnScrollChangedListener {

    private static final String TAG = ScrollViewWithHeaderLayout.class.getSimpleName();
    private Context mContext;
    private float alpha = 0;
    private ArrayList<View> headerList;
    private View tvHeader;
    private LoadMorePageListener loadMoreListener;
    private TextView tvVisit;
    private TextView tvProfile;
    private LinearLayout actionbarHeader;
    private LinearLayout tabs;
    private int headerYPosition;
    private PatientDetailListener patientDetailListener;
    private int increaseAlphaValueCount = 0;
    private HealthCocoActivity mActivity;
    private Animation slideInFromTopAnimation;
    private Animation slideOutToTopAnimation;

    public ScrollViewWithHeaderLayout(Context context) {
        super(context);
        this.mContext = context;
        headerList = new ArrayList<View>();

    }

    public ScrollViewWithHeaderLayout(Context context, AttributeSet attribute) {
        super(context, attribute);
        this.mContext = context;
        headerList = new ArrayList<View>();

    }

    public ScrollViewWithHeaderLayout(Context context, AttributeSet attribute,
                                      int defStyleAttr) {
        super(context, attribute, defStyleAttr);
        this.mContext = context;
        headerList = new ArrayList<View>();

    }

    public void build(HealthCocoActivity mActivity, OnClickListener onClickListener) {
        this.mActivity = mActivity;
        init(onClickListener);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private void init(OnClickListener onClickListener) {
        initMember();
        initListener(onClickListener);
        initAnimations();
    }

    private void initMember() {
        actionbarHeader = (LinearLayout) tvHeader.findViewById(R.id.layout_patient_image_gender_age);
        tabs = (LinearLayout) tvHeader.findViewById(R.id.tabs);
        ImageButton btBack = (ImageButton) actionbarHeader.findViewById(R.id.bt_back);
        btBack.setVisibility(View.INVISIBLE);
    }

    private void initListener(OnClickListener onClickListener) {
        getViewTreeObserver().addOnScrollChangedListener(this);
        tvProfile = (TextView) tvHeader.findViewById(R.id.tv_profile);
        tvVisit = (TextView) tvHeader.findViewById(R.id.tv_visit);
        tvProfile.setOnClickListener(onClickListener);
        tvVisit.setOnClickListener(onClickListener);
    }

    private void initAnimations() {
        slideInFromTopAnimation = AnimationUtils.loadAnimation(mActivity,
                R.anim.alpha_appearing);
        slideOutToTopAnimation = AnimationUtils.loadAnimation(mActivity,
                R.anim.alpha_disappearing);
    }

    public void addChildHeaders(LinearLayout v) {
        headerList.add(v);
    }

    public void addFixedHeader(View v) {
        this.tvHeader = v;
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
        tvHeader.setVisibility(View.GONE);
//        tabs.startAnimation(slideOutToTopAnimation);
        tabs.setVisibility(View.GONE);
        patientDetailListener.setHeaderYLocationOnScreen(tvHeaderY);
        if (headerList != null & headerList.size() > 0) {
            for (View v : headerList) {

                if (v instanceof LinearLayout || v instanceof View) {
                    v.getLocationOnScreen(position);
                    if (position[1] <= tvHeaderY) {
                        LogUtils.LOGD(TAG, "Position at visible " + position[1] + " headerYPosition " + tvHeaderY);
//                        actionbarHeader.startAnimation(slideInFromTopAnimation);
                        actionbarHeader.setVisibility(View.VISIBLE);
                        tvHeader.setVisibility(View.VISIBLE);
                        tabs.setVisibility(View.VISIBLE);
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

    public void setSelectedTab(int position) {
        if (position == 0) {
            tvProfile.setSelected(true);
            tvVisit.setSelected(false);
        } else if (position == 1) {
            tvProfile.setSelected(false);
            tvVisit.setSelected(true);
        }
    }

    public void setPatientDetailListener(PatientDetailListener patientDetailListener) {
        this.patientDetailListener = patientDetailListener;
    }
}
