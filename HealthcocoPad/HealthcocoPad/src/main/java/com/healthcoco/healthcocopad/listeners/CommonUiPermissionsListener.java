package com.healthcoco.healthcocopad.listeners;

/**
 * Created by neha on 13/04/17.
 */

public interface CommonUiPermissionsListener {
    boolean isAssigned(String permission);

    void assignPermission(String permission);

    void removePermission(String permission);
}
