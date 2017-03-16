package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public enum UserState {
    USERSTATECOMPLETE("USERSTATECOMPLETE", R.string.userstate_verified),
    USERSTATEINCOMPLETE("USERSTATEINCOMPLETE", R.string.userstate_incomplete),
    NOTVERIFIED("NOTVERIFIED", R.string.userstate_not_verified),
    NOTACTIVATED("NOTACTIVATED", R.string.userstate_not_activated);
    private int msgId;
    String state;

    UserState(String state, int msgId) {
        this.state = state;
        this.msgId = msgId;
    }


    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
