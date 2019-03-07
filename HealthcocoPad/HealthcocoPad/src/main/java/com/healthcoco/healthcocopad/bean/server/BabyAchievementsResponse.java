package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class BabyAchievementsResponse extends SugarRecord {
    @Unique
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
    private String patientId;
    private String achievement;
    private Long achievementDate;

    protected String foreignDurationId;
    private String note;
    @Ignore
    private AchievementsDuration duration;
    protected String durationJsonString;

    public AchievementsDuration getDuration() {
        return duration;
    }


    public String getDurationJsonString() {
        return durationJsonString;
    }

    public void setDurationJsonString(String durationJsonString) {
        this.durationJsonString = durationJsonString;
    }


    public String getForeignDurationId() {
        return foreignDurationId;
    }

    public void setForeignDurationId(String foreignDurationId) {
        this.foreignDurationId = foreignDurationId;
    }


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

    public void setDuration(AchievementsDuration duration) {
        this.duration = duration;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
