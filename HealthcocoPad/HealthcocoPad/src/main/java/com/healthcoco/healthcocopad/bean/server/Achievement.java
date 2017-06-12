package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.AchievementType;
import com.orm.SugarRecord;

@org.parceler.Parcel
public class Achievement extends SugarRecord {
    private String achievementName;

    private int year;

    private AchievementType achievementType;
    protected String foreignUniqueId;

    public Achievement() {
    }

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public AchievementType getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(AchievementType achievementType) {
        this.achievementType = achievementType;
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }
}
