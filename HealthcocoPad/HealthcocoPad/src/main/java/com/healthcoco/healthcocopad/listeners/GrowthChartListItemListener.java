package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.GrowthChartResponse;

public interface GrowthChartListItemListener {
    public void editGrowthChart(GrowthChartResponse growthChartResponse);

    public void discardGrowthChart(GrowthChartResponse growthChartResponse);
}
