package com.healthcoco.healthcocoplus.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.healthcoco.healthcocoplus.HealthCocoActivity;

/**
 * Created by neha on 04/01/16.
 */
public class FontAwesomeRadioButton extends RadioButton {
    private HealthCocoActivity mActivity;

    public FontAwesomeRadioButton(Context context) {
        super(context);
        init(context);
    }

    public FontAwesomeRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontAwesomeRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setTypeface(font);
    }
}
