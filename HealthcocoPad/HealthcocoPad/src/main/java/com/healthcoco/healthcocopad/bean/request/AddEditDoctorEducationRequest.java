package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.Education;
import com.orm.annotation.Unique;

import java.util.List;

/**
 * Created by Shreshtha on 15-06-2017.
 */

public class AddEditDoctorEducationRequest {
    @Unique
    private String doctorId;
    private List<Education> education;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }
}
