package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Shreshtha on 25-Sep-17.
 */

public class MetricsMediumTextView extends TextViewFontAwesome {

    public MetricsMediumTextView(Context context) {
        super(context);
        init(context);
    }

    public MetricsMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MetricsMediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (!isInEditMode()) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), "Metric-Medium.ttf");
            setTypeface(font);
        }
    }

}
