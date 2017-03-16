package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.AccessPermissionType;
import com.orm.SugarRecord;

import org.parceler.Parcel;

@Parcel
public class AccessPermission extends SugarRecord {
    private AccessPermissionType accessPermissionType;

    private boolean accessPermissionValue;
    protected String foreignAccessModuleId;

    public AccessPermissionType getAccessPermissionType() {
        return accessPermissionType;
    }

    public void setAccessPermissionType(AccessPermissionType accessPermissionType) {
        this.accessPermissionType = accessPermissionType;
    }

    public boolean isAccessPermissionValue() {
        return accessPermissionValue;
    }

    public void setAccessPermissionValue(boolean accessPermissionValue) {
        this.accessPermissionValue = accessPermissionValue;
    }

    public String getForeignAccessModuleId() {
        return foreignAccessModuleId;
    }

    public void setForeignAccessModuleId(String foreignAccessModuleId) {
        this.foreignAccessModuleId = foreignAccessModuleId;
    }
}
