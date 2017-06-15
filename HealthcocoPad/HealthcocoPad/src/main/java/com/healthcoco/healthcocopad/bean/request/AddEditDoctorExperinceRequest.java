package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.DoctorExperienceDetail;
import com.orm.annotation.Unique;

import java.util.List;

/**
 * Created by Shreshtha on 15-06-2017.
 */

public class AddEditDoctorExperinceRequest {
    @Unique
    private String doctorId;
    private List<DoctorExperienceDetail> experienceDetails;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public List<DoctorExperienceDetail> getExperienceDetails() {
        return experienceDetails;
    }

    public void setExperienceDetails(List<DoctorExperienceDetail> experienceDetails) {
        this.experienceDetails = experienceDetails;
    }
}
