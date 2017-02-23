package com.healthcoco.healthcocoplus.views;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.healthcoco.healthcocoplus.custom.SavedState;
import com.healthcoco.healthcocoplus.listeners.LoadMorePageListener;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

/**
 * Created by nehas on 02/03/16.
 */
public class ListViewLoadMore extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = ListViewLoadMore.class.getSimpleName();
    private int preLast;
    private LoadMorePageListener loadMoreListener;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int stateToSave;

    public ListViewLoadMore(Context context) {
        super(context);
        init(context);
    }

    public ListViewLoadMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListViewLoadMore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState != 0) {
            Util.hideKeyboard(getContext(), this);
        }
    }

    @Override
    public void onScroll(AbsListView lw, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
        // Make your calculation stuff here. You have all your
        // needed info from the parameters of this function.

        // Sample calculation to determine if the last
        // item is fully visible.
        try {
            if (swipeRefreshLayout != null) {
                if (firstVisibleItem <= 0) {
                    View child = getChildAt(firstVisibleItem);
                    if (child != null && child.getTop() < 0) {
                        LogUtils.LOGD(TAG, "Visible Partially");
                        swipeRefreshLayout.setEnabled(false);
                    } else {
                        LogUtils.LOGD(TAG, "Visible Fully");
                        swipeRefreshLayout.setEnabled(true);
                    }

                } else
                    swipeRefreshLayout.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int lastItem = firstVisibleItem + visibleItemCount;
        if (lastItem == totalItemCount) {
//            if (preLast != lastItem && loadMoreListener != null) { //to avoid multiple calls for last item
            if (preLast != lastItem && loadMoreListener != null && !loadMoreListener.isEndOfListAchieved()) { //to avoid multiple calls for last item
                LogUtils.LOGD(TAG, "Last item " + lastItem);
                loadMoreListener.loadMore();
                preLast = lastItem;
            }
        }
    }

    public void setLoadMoreListener(LoadMorePageListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void resetPreLastPosition(int i) {
        this.preLast = 0;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        LogUtils.LOGD(TAG, "onSaveInstanceState");
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.stateToSave = this.stateToSave;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        LogUtils.LOGD(TAG, "onRestoreInstanceState");
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.stateToSave = ss.stateToSave;
    }

}
