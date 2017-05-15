package com.healthcoco.healthcocopad.listeners;

/**
 * Created by neha on 16/12/15.
 */
public interface MedicalFamilyHistoryItemListener {
    public void onAddDiseaseClicked(String diseaseId);

    public void onRemoveDiseaseClicked(String diseaseId);

    public boolean isDiseaseAdded(String uniqueId);
}
