package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 04-02-2017.
 */

public class CustomScrollView extends ScrollView {
    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Util.hideKeyboard(getContext(), this);
    }
}
