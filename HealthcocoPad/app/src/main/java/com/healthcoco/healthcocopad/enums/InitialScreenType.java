package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 22/01/17.
 */
public enum InitialScreenType {
    HCC_RECORDS(R.drawable.front_icon_all_records, R.string.title_hcc_records, R.string.text_hcc_records, R.dimen.initial_screen_icon_sizes),
    HCC_PRESCRIPTION(R.drawable.front_icon_quick_prescription, R.string.title_hcc_prescription, R.string.text_hcc_rx, R.dimen.initial_screen_icon_sizes),
    HCC_PROFILE(R.drawable.front_icon_profile, R.string.title_hcc_profile, R.string.text_hcc_profile, R.dimen.initial_screen_icon_sizes),
    HCC_TWENTY_FOUR_SEVEN(R.drawable.front_icon_twenty_four_seven, R.string.title_hcc_twenty_four_seven, R.string.text_hcc_twenty_four_seven, R.dimen.initial_screen_icon_sizes),
    HCC_CONNECT(R.drawable.front_icon_connection, R.string.title_hcc_conn, R.string.text_hcc_conn, R.dimen.initial_screen_icon_sizes),
    HCC_SYNC(R.drawable.front_icon_connection, R.string.title_hcc_sync, R.string.text_hcc_sync, R.dimen.initial_screen_icon_sizes);

    private final int messageId;
    private final int drawableId;
    private final int iconSizeId;
    private final int titleId;

    InitialScreenType(int drawableId, int titleId, int messageId, int iconSizeId) {
        this.messageId = messageId;
        this.drawableId = drawableId;
        this.iconSizeId = iconSizeId;
        this.titleId = titleId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getIconSizeId() {
        return iconSizeId;
    }

    public int getTitleId() {
        return titleId;
    }
}
