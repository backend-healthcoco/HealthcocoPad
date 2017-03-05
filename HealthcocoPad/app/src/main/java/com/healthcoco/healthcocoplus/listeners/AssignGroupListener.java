package com.healthcoco.healthcocoplus.listeners;

import com.healthcoco.healthcocoplus.bean.server.UserGroups;

/**
 * Created by neha on 23/11/15.
 */
public interface AssignGroupListener {
    public void onAssignGroupCheckClicked(boolean isSelected, UserGroups group);
}
