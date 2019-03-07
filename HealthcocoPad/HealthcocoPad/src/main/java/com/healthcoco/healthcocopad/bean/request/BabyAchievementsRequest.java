package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.AchievementsDuration;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

public class BabyAchievementsRequest {
    @Unique
    private String uniqueId;
    private String patientId;
    private String achievement;
    private Long achievementDate;
    private String note;
    @Ignore
    private AchievementsDuration duration;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public Long getAchievementDate() {
        return achievementDate;
    }

    public void setAchievementDate(Long achievementDate) {
        this.achievementDate = achievementDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AchievementsDuration getDuration() {
        return duration;
    }

    public void setDuration(AchievementsDuration duration) {
        this.duration = duration;
    }
}
