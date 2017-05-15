package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by Mohit on 29/02/16.
 */
public enum ColorType {
    GREEN(R.color.green_logo), BLUE(R.color.blue_action_bar), TRANSPARENT(android.R.color.transparent);

    private final int colorId;

    ColorType(int colorId) {
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }
}
