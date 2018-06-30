package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Shreshtha on 28-Sep-17.
 */

public class MetricsMediumEditText extends EditText {

    public MetricsMediumEditText(Context context) {
        super(context);
        init(context);
    }

    public MetricsMediumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MetricsMediumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
