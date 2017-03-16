package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Shreshtha on 02-02-2017.
 */

public class SlidingPaneDrawerLayout extends SlidingPaneLayout {
    public SlidingPaneDrawerLayout(Context context) {
        super(context);
    }

    public SlidingPaneDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingPaneDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // ===========================================================
// Methods for/from SuperClass/Interfaces
// ===========================================================
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.isOpen()) {
            this.closePane();
        }
        return false; // here it returns false so that another event's listener
        // should be called, in your case the MapFragment
        // listener
    }
}
