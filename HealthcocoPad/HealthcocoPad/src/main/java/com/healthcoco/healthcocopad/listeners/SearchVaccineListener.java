package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.VaccineSolarResponse;
import com.healthcoco.healthcocopad.enums.VaccineStatus;

public interface SearchVaccineListener {
    public void isVaccinationClicked(boolean isChecked, Long duration, VaccineStatus status, VaccineSolarResponse response);
}
