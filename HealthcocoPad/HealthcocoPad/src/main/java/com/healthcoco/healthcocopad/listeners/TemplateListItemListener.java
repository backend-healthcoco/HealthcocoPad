package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.TempTemplate;

/**
 * Created by neha on 28/11/15.
 */
public interface TemplateListItemListener {
    public void onPrescriptionClicked(TempTemplate template);

    public void onEditClicked(TempTemplate template);

    public void onDeleteClicked(TempTemplate template);

    public void onItemClicked(TempTemplate template);

    public boolean isFromSettingsScreen();
}
