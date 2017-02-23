package com.healthcoco.healthcocoplus.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 21-01-2017.
 */
public enum VersionCheckType {
    MAJOR(3, true, R.string.title_update_available, R.string.message_force_update, R.string.update, 0),
    MINOR(2, false, R.string.title_update_available, R.string.message_force_update, R.string.update, 0),
    PATCH(1, false, R.string.title_update_available, R.string.message_update_available, R.string.update, R.string.later);

    private final boolean isForceUpdate;
    private final int versionFlag;
    private final int titleId;
    private final int messageId;
    private final int positiveButtonTextId;
    private final int negativeButtonTextId;

    VersionCheckType(int versionFlag, boolean isForceUpdate, int titleId, int messageId, int positiveButtonTextId, int negativeButtonTextId) {
        this.isForceUpdate = isForceUpdate;
        this.versionFlag = versionFlag;
        this.titleId = titleId;
        this.messageId = messageId;
        this.positiveButtonTextId = positiveButtonTextId;
        this.negativeButtonTextId = negativeButtonTextId;
    }

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public int getVersionFlag() {
        return versionFlag;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getPositiveButtonTextId() {
        return positiveButtonTextId;
    }

    public int getNegativeButtonTextId() {
        return negativeButtonTextId;
    }
}
