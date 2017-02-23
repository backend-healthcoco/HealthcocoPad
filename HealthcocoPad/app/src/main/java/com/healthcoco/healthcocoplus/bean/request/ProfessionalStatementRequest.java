package com.healthcoco.healthcocoplus.bean.request;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Shreshtha on 21-02-2017.
 */
@Parcel
public class ProfessionalStatementRequest {
    public String doctorId;
    public String professionalStatement;

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
