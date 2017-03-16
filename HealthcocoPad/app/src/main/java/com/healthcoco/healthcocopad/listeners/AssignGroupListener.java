package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.UserGroups;

/**
 * Created by neha on 23/11/15.
 */
public interface AssignGroupListener {
    public void onAssignGroupCheckClicked(boolean isSelected, UserGroups group);
}
