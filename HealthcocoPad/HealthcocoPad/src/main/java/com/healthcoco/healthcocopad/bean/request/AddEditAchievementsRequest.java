package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.Achievement;
import com.orm.annotation.Unique;

import java.util.List;

/**
 * Created by Shreshtha on 15-06-2017.
 */

public class AddEditAchievementsRequest {
    @Unique
    private String doctorId;
    private List<Achievement> achievements;

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
