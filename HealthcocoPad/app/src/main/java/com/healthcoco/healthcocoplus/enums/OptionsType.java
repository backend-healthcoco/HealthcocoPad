package com.healthcoco.healthcocoplus.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 04-02-2017.
 */
public enum OptionsType {
    CAMERA(R.string.camera), GALLERY(R.string.gallery), PREVIEW(R.string.preview);

    OptionsType(int stringId) {
        this.stringId = stringId;
    }

    private int stringId;

    public int getStringId() {
        return stringId;
    }
}
