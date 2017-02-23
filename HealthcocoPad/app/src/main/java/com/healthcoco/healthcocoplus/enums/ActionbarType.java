package com.healthcoco.healthcocoplus.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public enum ActionbarType {
    TITLE(R.layout.action_bar_title),
        TITLE_SAVE(R.layout.action_bar_title_save),
//    ACTION_BAR_EMR(R.layout.actionbar_patient_image_gender_age),
//    TITLE_DONE(R.layout.action_bar_title_done),
//    TITLE_ADD(R.layout.actionbar_title_add),
    HIDDEN(0),
    ACTION_BAR_DIALOG(R.layout.dialog_actionbar_title_left_right_action);
//    TITLE_MENU(R.layout.action_bar_title_menu_options);
    private final int layoutId;

    ActionbarType(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getActionBarLayoutId() {
        return layoutId;
    }
}
