package com.healthcoco.healthcocoplus.listeners;

import com.healthcoco.healthcocoplus.enums.HistoryFilterType;

/**
 * Created by neha on 16/12/15.
 */
public interface HistoryDiseaseIdsListener {
    public void addDiseaseId(HistoryFilterType historyFilterType, String diseaseId);
}
