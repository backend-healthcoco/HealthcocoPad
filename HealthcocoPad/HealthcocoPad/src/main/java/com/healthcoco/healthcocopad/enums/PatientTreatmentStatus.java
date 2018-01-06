package com.healthcoco.healthcocopad.enums;

public enum PatientTreatmentStatus {
    NOT_STARTED("NOT STARTED"), IN_PROGRESS("IN PROGRESS"), COMPLETED("COMPLETED"), PLANNED("PLANNED");

    private String treamentStatus;

    private PatientTreatmentStatus(String treamentStatus) {
        this.treamentStatus = treamentStatus;
    }

    public String getTreamentStatus() {
        return treamentStatus;
    }

}
