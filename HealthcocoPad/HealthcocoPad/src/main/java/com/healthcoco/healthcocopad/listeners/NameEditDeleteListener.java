package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.enums.SettingsItemType;

/**
 * Created by neha on 15/01/16.
 */
public interface NameEditDeleteListener {
    public void onEditClicked(SettingsItemType itemType, Object object);

    public void onDeleteClicked(SettingsItemType itemType, Object object);

}
