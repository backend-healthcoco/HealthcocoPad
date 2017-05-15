package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.DeviceType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.util.ArrayList;

/**
 * Created by neha on 14/06/16.
 */
public class GCMRequest extends SugarRecord {
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
    @Ignore
    private ArrayList<String> userIds;
    protected String userIdsJsonString;
    private DeviceType deviceType;
    @Unique
    private String deviceId;
    private String pushToken;
    private RoleType role;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }

    public String getUserIdsJsonString() {
        return userIdsJsonString;
    }

    public void setUserIdsJsonString(String userIdsJsonString) {
        this.userIdsJsonString = userIdsJsonString;
    }
}
