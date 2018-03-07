package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 05/05/16.
 */
public enum AppointmentStatusType {
    CONFIRM("Confirmed", R.string.confirmed, R.integer.id_item_confirmed, OptionsTypePopupWindow.CALENDAR_APPOINTEMNT_CONFIRMED),
    NEW("Pending Confirmation", R.string.pending_confirmation, R.integer.id_item_pending_new, OptionsTypePopupWindow.CALENDAR_APPOINTEMNT_ALL),
    CANCEL("Cancelled", R.string.cancelled, R.integer.id_item_cancelled, OptionsTypePopupWindow.CALENDAR_APPOINTEMNT_ALL),
    RESCHEDULE("Rescheduled", R.string.reschedule, 0, OptionsTypePopupWindow.CALENDAR_APPOINTEMNT_ALL),
    ALL("All", R.string.all, R.integer.id_item_all, OptionsTypePopupWindow.CALENDAR_APPOINTEMNT_ALL);

    private final String text;
    private final int filterTextId;
    private final int itemId;
    private final OptionsTypePopupWindow optionsTypePopupWindow;

    AppointmentStatusType(String text, int filterTextId, int itemId, OptionsTypePopupWindow optionsTypePopupWindow) {
        this.text = text;
        this.filterTextId = filterTextId;
        this.itemId = itemId;
        this.optionsTypePopupWindow = optionsTypePopupWindow;
    }

    public String getText() {
        return text;
    }

    public int getFilterTextId() {
        return filterTextId;
    }

    public int getItemId() {
        return itemId;
    }

    public OptionsTypePopupWindow getOptionsTypePopupWindow() {
        return optionsTypePopupWindow;
    }
}
