package com.healthcoco.healthcocopad.listeners;

/**
 * Created by Prashant on 26/06/2018.
 */

public interface PatientRegistrationDetailsListener {

    public void readyToMoveNext(Object object, boolean isEditPatient);

    public boolean isFromPatientRegistarion();
}
