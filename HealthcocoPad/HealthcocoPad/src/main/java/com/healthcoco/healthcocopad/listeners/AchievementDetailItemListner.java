package com.healthcoco.healthcocopad.listeners;

import android.view.View;

import com.healthcoco.healthcocopad.bean.server.Achievement;

/**
 * Created by Shreshtha on 20-02-2017.
 */
public interface AchievementDetailItemListner {
    public void onDeleteAchievementDetailClicked(View view, Achievement achievement);

    public void addAchievementDetailToList(Achievement achievement);
}
