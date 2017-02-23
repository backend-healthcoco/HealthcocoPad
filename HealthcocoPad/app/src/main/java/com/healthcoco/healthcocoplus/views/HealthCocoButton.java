package com.healthcoco.healthcocoplus.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 05-02-2017.
 */

public class HealthCocoButton extends Button {
    public HealthCocoButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public HealthCocoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HealthCocoButton(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.drawable.selector_btn_blue_grey);
        setTextColor(getResources().getColorStateList(R.color.color_selector_white_translucent));
        setGravity(Gravity.CENTER);
    }
}
