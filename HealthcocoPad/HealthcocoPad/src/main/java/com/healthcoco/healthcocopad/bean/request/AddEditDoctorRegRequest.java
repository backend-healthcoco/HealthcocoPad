package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.DoctorRegistrationDetail;
import com.orm.annotation.Unique;

import java.util.List;

/**
 * Created by Shreshtha on 15-06-2017.
 */

public class AddEditDoctorRegRequest {
    @Unique
    private String doctorId;
    private List<DoctorRegistrationDetail> registrationDetails;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public List<DoctorRegistrationDetail> getRegistrationDetails() {
        return registrationDetails;
    }

    public void setRegistrationDetails(List<DoctorRegistrationDetail> registrationDetails) {
        this.registrationDetails = registrationDetails;
    }
}
