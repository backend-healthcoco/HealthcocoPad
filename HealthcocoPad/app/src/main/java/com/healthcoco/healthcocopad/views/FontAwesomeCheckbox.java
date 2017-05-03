package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.healthcoco.healthcocopad.HealthCocoActivity;


/**
 * Created by neha on 29/09/16.
 */
public class FontAwesomeCheckbox extends CheckBox {
    private HealthCocoActivity mActivity;

    public FontAwesomeCheckbox(Context context) {
        super(context);
        init(context);
    }

    public FontAwesomeCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontAwesomeCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setTypeface(font);
    }
}
