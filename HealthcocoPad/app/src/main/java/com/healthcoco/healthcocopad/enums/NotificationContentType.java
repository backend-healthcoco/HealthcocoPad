package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 03/07/16.
 */
public enum NotificationContentType {
    REPORTS(R.string.report_details, WebServiceType.GET_RECORD_BY_RECORD_ID),
    CALENDAR_REMINDER;

    private final int titleId;
    private final WebServiceType webServiceType;

    NotificationContentType(int titleId, WebServiceType webServiceType) {
        this.titleId = titleId;
        this.webServiceType = webServiceType;
    }


    NotificationContentType() {
        this.titleId = 0;
        this.webServiceType = null;
    }

    public int getTitleId() {
        return titleId;
    }

    public WebServiceType getWebServiceType() {
        return webServiceType;
    }
}
