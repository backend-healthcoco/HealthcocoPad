package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 21-04-2017.
 */

public class CustomActivatedTextView extends TextView {
    public CustomActivatedTextView(Context context) {
        super(context);
    }

    public CustomActivatedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomActivatedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        String s = String.valueOf(text.toString());
        if (Util.isNullOrBlank(s))
            setActivated(false);
        else
            setActivated(true);
    }
}
