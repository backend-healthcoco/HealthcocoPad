package com.healthcoco.healthcocoplus.listeners;

import android.view.View;

import com.healthcoco.healthcocoplus.bean.server.Achievement;

/**
 * Created by Shreshtha on 20-02-2017.
 */
public interface AchievementDetailItemListner {
    public void onDeleteAchievementDetailClicked(View view, Achievement achievement);

    public void addAchievementDetailToList(Achievement achievement);
}
