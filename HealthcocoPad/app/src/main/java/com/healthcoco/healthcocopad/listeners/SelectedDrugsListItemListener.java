package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugItem;

public interface SelectedDrugsListItemListener {
    public void onDeleteItemClicked(DrugItem drug);

    public void onDrugItemClicked(DrugItem drug);
}
