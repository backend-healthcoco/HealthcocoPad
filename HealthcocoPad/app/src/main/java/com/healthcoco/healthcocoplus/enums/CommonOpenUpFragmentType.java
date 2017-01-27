package com.healthcoco.healthcocoplus.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public enum CommonOpenUpFragmentType {
    LOGIN_SIGN_UP(0),
    CONTINUE_SIGN_UP(0),
    TERMS_OF_SERVICE(R.string.terms_of_service),
    PRIVACY_POLICY(R.string.privacy_policy), INITIAL_SYNC(0);

    private final int titleId;
    CommonOpenUpFragmentType(int titleId) {
        this.titleId = titleId;
    }

    public int getTitleId() {
        return titleId;
    }
}
