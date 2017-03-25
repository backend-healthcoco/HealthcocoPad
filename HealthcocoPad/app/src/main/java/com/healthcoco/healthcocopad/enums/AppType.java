package com.healthcoco.healthcocopad.enums;

/**
 * Created by Shreshtha on 20-01-2017.
 */
public enum AppType {
    HEALTHCOCO("HEALTHCOCO"), HEALTHCOCO_PLUS("HEALTHCOCO_PLUS");

    private final String appName;

    AppType(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }
}
