package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.UserGroups;

/**
 * Created by neha on 22/03/17.
 */

public interface PatientRegistrationListener {
    public void onAssignGroupCheckClicked(boolean isSelected, UserGroups group);

    public boolean isGroupAssigned(String groupId);

    public void onDeleteNotesClicked(String notes);
}
