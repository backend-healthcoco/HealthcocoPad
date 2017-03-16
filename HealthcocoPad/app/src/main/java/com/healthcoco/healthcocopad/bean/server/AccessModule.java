package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.AccessPermissionType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;
@Parcel
public class AccessModule extends SugarRecord {
    @Unique
    private String uniqueId;

    private String module;

    private String url;

    @Ignore
    private List<AccessPermission> accessPermissions;

    @Ignore
    private List<AccessPermissionType> accessPermissionTypes;
    private String stringAccessPermissionTypes;

    protected String foreignRoleId;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<AccessPermission> getAccessPermissions() {
        return accessPermissions;
    }

    public void setAccessPermissions(List<AccessPermission> accessPermissions) {
        this.accessPermissions = accessPermissions;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }


    public String getForeignRoleId() {
        return foreignRoleId;
    }

    public void setForeignRoleId(String foreignRoleId) {
        this.foreignRoleId = foreignRoleId;
    }

    public List<AccessPermissionType> getAccessPermissionTypes() {
        return accessPermissionTypes;
    }

    public void setAccessPermissionTypes(List<AccessPermissionType> accessPermissionTypes) {
        this.accessPermissionTypes = accessPermissionTypes;
    }

    public String getStringAccessPermissionTypes() {
        return stringAccessPermissionTypes;
    }

    public void setStringAccessPermissionTypes(String stringAccessPermissionTypes) {
        this.stringAccessPermissionTypes = stringAccessPermissionTypes;
    }
}
