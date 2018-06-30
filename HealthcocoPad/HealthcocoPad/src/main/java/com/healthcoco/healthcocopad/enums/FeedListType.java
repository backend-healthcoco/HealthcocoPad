package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 23-Sep-17.
 */

public enum FeedListType {
    HEALTH(0, R.string.health, R.string.health_feeds, R.string.no_health_feeds_added, R.drawable.shape_rounded_rectangle_my_health_blue, R.drawable.ic_noreports),
    FAVORITE(1, R.string.favorite, R.string.my_articles, R.string.no_fav_feeds_added, R.drawable.shape_rounded_rectangle_my_health_blue, R.drawable.ic_noreports);


    public static final String TAG_LIST_TYPE = "listType";
    private final int textId;
    private final int tabPosition;
    private final int titleId;
    private final int noDataFoundTextId;
    private final int noDataImageId;
    private final int backgroundShapeDrawableId;


    FeedListType(int tabPosition, int titleId, int textId, int noDataFoundTextId, int backgroundShapeDrawableId, int noDataImageId) {
        this.textId = textId;
        this.titleId = titleId;
        this.noDataFoundTextId = noDataFoundTextId;
        this.noDataImageId = noDataImageId;
        this.tabPosition = tabPosition;
        this.backgroundShapeDrawableId = backgroundShapeDrawableId;
    }

    public int getTextId() {
        return textId;
    }

    public int getNoDataImageId() {
        return noDataImageId;
    }

    public int getNoDataFoundTextId() {
        return noDataFoundTextId;
    }

    public int getTabPosition() {
        return tabPosition;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getBackgroundShapeDrawableId() {
        return backgroundShapeDrawableId;
    }
}
