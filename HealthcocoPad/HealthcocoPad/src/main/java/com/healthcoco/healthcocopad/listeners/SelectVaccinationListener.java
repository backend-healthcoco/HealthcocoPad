package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.VaccineResponse;

public interface SelectVaccinationListener {
    public void isVaccinationClicked(boolean isChecked, VaccineResponse response);
    public boolean isVaccinationSelected(String uniqueId);
    public void setSelectAll(boolean isSelected);
}
