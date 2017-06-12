package com.healthcoco.healthcocopad.bean.request;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 21-02-2017.
 */
@Parcel
public class ProfessionalStatementRequest {
    private String doctorId;
    private String professionalStatement;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getProfessionalStatement() {
        return professionalStatement;
    }

    public void setProfessionalStatement(String professionalStatement) {
        this.professionalStatement = professionalStatement;
    }
}
