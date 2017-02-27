package com.healthcoco.healthcocoplus.enums;

import android.support.v7.widget.AppCompatButton;
import android.widget.ImageButton;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public enum ActionbarLeftRightActionType {
    WITH_BACK(true, ImageButton.class, R.drawable.selector_actionbar_back, R.layout.actionbar_image_button),
//    WITH_FILTER(true, ImageButton.class, R.drawable.img_filter_contact_selector, R.layout.actionbar_image_button),

    WITH_CROSS(true, ImageButton.class, R.drawable.close_selector, R.layout.actionbar_image_button),
//    WITH_HOME(ImageButton.class, R.drawable.img_filter_contact_selector),

    WITH_DONE(false, AppCompatButton.class, R.string.done, R.layout.actionbar_button),
    WITH_SAVE(false, AppCompatButton.class, R.string.save, R.layout.actionbar_button),
    //    WITH_CONFIRM(false, AppCompatButton.class, R.string.confirm, R.layout.actionbar_button),
//    WITH_ADD(true, ImageButton.class, R.drawable.actionbar_add_selector, R.layout.actionbar_image_button),
    NO_LEFT_RIGHT_ACTION(false, null, 0, 0);

    private final int drawableTitleId;
    private final Class<?> class1;
    private final int layoutId;
    private final boolean isDrawableBackground;

    ActionbarLeftRightActionType(boolean isDrawableBackground, Class<?> class1, int drawableTitleId, int layoutId) {
        this.class1 = class1;
        this.drawableTitleId = drawableTitleId;
        this.layoutId = layoutId;
        this.isDrawableBackground = isDrawableBackground;
    }

    public Class<?> getClassType() {
        return class1;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getDrawableTitleId() {
        return drawableTitleId;
    }

    public boolean isDrawableBackground() {
        return isDrawableBackground;
    }
}
