package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *
 */
public class TextViewFontAwesome extends TextView {

    public TextViewFontAwesome(Context context) {
        super(context);
        init(context);
    }

    public TextViewFontAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewFontAwesome(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setTypeface(font);
    }
}
