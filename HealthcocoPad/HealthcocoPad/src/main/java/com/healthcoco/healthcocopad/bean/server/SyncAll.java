package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.SyncAllType;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

/**
 * Created by neha on 08/02/16.
 */
public class SyncAll extends SugarRecord {
    @Unique
    private SyncAllType syncAllType;
    private Long lastSyncedTime;
    private boolean isLoading;
    private int position;

    public SyncAllType getSyncAllType() {
        return syncAllType;
    }

    public void setSyncAllType(SyncAllType syncAllType) {
        this.syncAllType = syncAllType;
    }

    public Long getLastSyncedTime() {
        return lastSyncedTime;
    }

    public void setLastSyncedTime(Long lastSyncedTime) {
        this.lastSyncedTime = lastSyncedTime;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public int getPosition() {
        this.position = syncAllType.getPosition();
        return position;
    }

    public void setPosition(int position) {
        this.position = syncAllType.getPosition();
    }
}
