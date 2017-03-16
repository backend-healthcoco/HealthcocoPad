package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Shreshtha on 27-02-2017.
 */

public class FontAwesomeButton extends Button {

    public FontAwesomeButton(Context context) {
        super(context);
        init(context);
    }

    public FontAwesomeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontAwesomeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setTypeface(font);
    }
}
