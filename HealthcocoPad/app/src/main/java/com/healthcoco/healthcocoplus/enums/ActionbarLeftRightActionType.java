package com.healthcoco.healthcocoplus.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public enum ActionbarLeftRightActionType {
//    WITH_BACK(R.layout.btn_back_arrow),

    WITH_CROSS(R.layout.btn_cross)

//    WITH_DONE(R.layout.tv_done),
//    WITH_ADD(R.layout.btn_actionbar_add),
//    NO_LEFT_RIGHT_ACTION(0);
    ;
    private final int layoutId;

    ActionbarLeftRightActionType(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getLayoutId() {
        return layoutId;
    }
}
