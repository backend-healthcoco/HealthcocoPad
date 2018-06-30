package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.healthcoco.healthcocopad.custom.AutoFitTextView;


/**
 * Created by Shreshtha on 25-Sep-17.
 */

public class MetricsLightTextView extends AutoFitTextView {

    public MetricsLightTextView(Context context) {
        super(context);
        init(context);
    }

    public MetricsLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MetricsLightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (!isInEditMode()) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), "Metric-Light.ttf");
            setTypeface(font);
        }
    }
}
