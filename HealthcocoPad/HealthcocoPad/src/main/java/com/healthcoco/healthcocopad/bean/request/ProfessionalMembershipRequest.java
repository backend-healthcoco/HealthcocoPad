package com.healthcoco.healthcocopad.bean.request;

import java.util.List;

/**
 * Created by Shreshtha on 21-02-2017.
 */

public class ProfessionalMembershipRequest {
    private String doctorId;
    private List<String> membership;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public List<String> getMembership() {
        return membership;
    }

    public void setMembership(List<String> membership) {
        this.membership = membership;
    }
}
