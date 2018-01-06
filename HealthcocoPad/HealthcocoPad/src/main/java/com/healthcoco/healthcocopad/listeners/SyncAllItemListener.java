package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.SyncAll;
import com.healthcoco.healthcocopad.enums.SyncAllType;

/**
 * Created by neha on 08/02/16.
 */
public interface SyncAllItemListener {
    public void onSyncItemClicked(SyncAll syncAll);

    public void formSyncTypesList(SyncAllType syncAllType);

    public void removeFromSyncTypesList(SyncAllType syncAllType);
}
