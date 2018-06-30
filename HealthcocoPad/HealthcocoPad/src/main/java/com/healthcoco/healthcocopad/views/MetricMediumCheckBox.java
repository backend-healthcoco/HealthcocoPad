package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by Shreshtha on 20-Nov-17.
 */

public class MetricMediumCheckBox extends CheckBox {
    public MetricMediumCheckBox(Context context) {
        super(context);
        init(context);
    }

    public MetricMediumCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MetricMediumCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
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
