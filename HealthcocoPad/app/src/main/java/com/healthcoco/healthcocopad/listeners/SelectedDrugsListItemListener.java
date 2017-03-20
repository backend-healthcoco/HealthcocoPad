package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.Drug;

public interface SelectedDrugsListItemListener {
    public void onDeleteItemClicked(Drug drug);

    public void onDrugItemClicked(Drug drug);
}
