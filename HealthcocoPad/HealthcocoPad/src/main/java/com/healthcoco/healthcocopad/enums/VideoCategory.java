package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Prashant on 03/07/2018.
 */

public enum VideoCategory {

    HEALTH(R.string.health),
    GENERAL(R.string.health);

    private final int titleId;

    VideoCategory(int titleId) {
        this.titleId = titleId;
    }

    public int getTitleId() {
        return titleId;
    }
}
