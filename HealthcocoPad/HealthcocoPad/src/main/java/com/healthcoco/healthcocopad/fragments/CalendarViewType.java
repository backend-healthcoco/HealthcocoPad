package com.healthcoco.healthcocopad.fragments;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Prashant on 08/05/2018.
 */

public enum CalendarViewType {

    QUEUE(R.string.calendar_queue),
    ONE_DAY(R.string.one_day_view),
    THREE_DAY(R.string.three_day_view);

    private final int titleId;


    CalendarViewType(int titleId) {
        this.titleId = titleId;
    }

    public int getTitleId() {
        return titleId;
    }
}
