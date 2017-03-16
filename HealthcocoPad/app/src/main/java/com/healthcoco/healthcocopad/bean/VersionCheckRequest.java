package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 20-01-2017.
 */

public class VersionCheckRequest {
    //their positions in array
    public static final int MAJOR = 0;
    public static final int MINOR = 1;
    public static final int PATCH = 2;
    private String uniqueId;
    private AppType appType;
    private DeviceType deviceType;
    private Integer majorVersion;
    private Integer minorVersion;
    private Integer patchVersion;

    public VersionCheckRequest() {
    }

    public VersionCheckRequest(AppType appType, DeviceType deviceType, String[] versionParts) {
        this.appType = appType;
        this.deviceType = deviceType;
        majorVersion = Util.getIntValue(versionParts[MAJOR]);
        minorVersion = Util.getIntValue(versionParts[MINOR]);
        patchVersion = Util.getIntValue(versionParts[PATCH]);
    }

    public static int getMAJOR() {
        return MAJOR;
    }

    public static int getMINOR() {
        return MINOR;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public static int getPATCH() {
        return PATCH;
    }

    public AppType getAppType() {
        return appType;
    }

    public void setAppType(AppType appType) {
        this.appType = appType;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(Integer majorVersion) {
        this.majorVersion = majorVersion;
    }

    public Integer getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(Integer minorVersion) {
        this.minorVersion = minorVersion;
    }

    public Integer getPatchVersion() {
        return patchVersion;
    }

    public void setPatchVersion(Integer patchVersion) {
        this.patchVersion = patchVersion;
    }
}
