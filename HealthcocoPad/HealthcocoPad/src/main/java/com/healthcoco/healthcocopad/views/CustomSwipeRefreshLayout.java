package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.healthcoco.healthcocopad.R;


/**
 * Created by neha on 19/03/16.
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    public CustomSwipeRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setColorSchemeResources(R.color.blue_action_bar);
    }
}
