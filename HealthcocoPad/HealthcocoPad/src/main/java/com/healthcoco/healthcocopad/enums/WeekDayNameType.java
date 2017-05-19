package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 30/01/16.
 */
public enum WeekDayNameType {
    MONDAY(R.layout.item_weekdayname_add_session, 0, R.string.monday),
    TUESDAY(R.layout.item_weekdayname_add_session, 1, R.string.tuesday),
    WEDNESDAY(R.layout.item_weekdayname_add_session, 2, R.string.wednesday),
    THURSDAY(R.layout.item_weekdayname_add_session, 3, R.string.thursday),
    FRIDAY(R.layout.item_weekdayname_add_session, 4, R.string.friday),
    SATURDAY(R.layout.item_weekdayname_add_session, 5, R.string.saturday),
    SUNDAY(R.layout.item_weekdayname_add_session, 6, R.string.sunday);;
    private int tag;
    private int layoutId;
    private int weekDayNameId;

    WeekDayNameType(int layoutId, int tag, int weekDayNameId) {
        this.layoutId = layoutId;
        this.tag = tag;
        this.weekDayNameId = weekDayNameId;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getTag() {
        return tag;
    }

    public int getWeekDayNameId() {
        return weekDayNameId;
    }

}
