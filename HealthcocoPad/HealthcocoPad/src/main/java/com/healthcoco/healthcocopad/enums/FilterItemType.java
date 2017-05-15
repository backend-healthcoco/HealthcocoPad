package com.healthcoco.healthcocopad.enums;

/**
 * Created by Shreshtha on 01-02-2017.
 */
public enum FilterItemType {
    SEARCH_PATIENT(LocalBackgroundTaskType.SEARCH_PATIENTS),
    ALL_PATIENTS(LocalBackgroundTaskType.GET_PATIENTS),
    RECENTLY_VISITED(LocalBackgroundTaskType.SORT_LIST_BY_RECENTLY_VISITED),
    MOST_VISITED(LocalBackgroundTaskType.SORT_LIST_BY_MOSTLY_VISITED),
    RECENTLY_ADDED(LocalBackgroundTaskType.SORT_LIST_BY_RECENTLY_ADDED),
    REFRESH_CONTACTS(LocalBackgroundTaskType.GET_PATIENTS),
    GROUP_ITEM(LocalBackgroundTaskType.SORT_LIST_BY_GROUP);

    private final LocalBackgroundTaskType localBackgroundTaskType;

    FilterItemType(LocalBackgroundTaskType localBackgroundTaskType) {
        this.localBackgroundTaskType = localBackgroundTaskType;
    }

    public LocalBackgroundTaskType getLocalBackgroundTaskType() {
        return localBackgroundTaskType;
    }
}
