package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.enums.HistoryFilterType;

/**
 * Created by neha on 16/12/15.
 */
public interface HistoryDiseaseIdsListener {
    public void addDiseaseId(HistoryFilterType historyFilterType, String diseaseId);
}
