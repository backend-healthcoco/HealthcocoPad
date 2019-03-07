package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.VaccineBrandResponse;

public interface SelectedBrandListener {
    void isBrandSelect(boolean isChecked, String vaccineBrandId, VaccineBrandResponse vaccineBrandResponse);

    void isBrandCancel(String vaccineBrandId, VaccineBrandResponse vaccineBrandResponse);

    boolean isBrandSelected(String vaccineBrandId, String vaccineId);

    void isBrandSelectForGroup(boolean isChecked, String vaccineBrandId, VaccineBrandResponse vaccineBrandResponse);

    boolean isBrandGroupSelected();
}
