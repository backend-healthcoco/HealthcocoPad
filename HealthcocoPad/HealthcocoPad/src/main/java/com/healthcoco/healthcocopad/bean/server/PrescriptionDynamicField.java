package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Prashant on 21/02/2018.
 */
@Parcel
public class PrescriptionDynamicField extends SugarRecord {
    private String services;

    private String drug;
    private String dosage;
    private String duration;
    private String direction;
    private String diagnosticTest;

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDiagnosticTest() {
        return diagnosticTest;
    }

    public void setDiagnosticTest(String diagnosticTest) {
        this.diagnosticTest = diagnosticTest;
    }
}
