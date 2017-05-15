package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.fragments.AddVisitsFragment;

public interface SelectedDrugsListItemListener {
    public void onDeleteItemClicked(DrugItem drug);

    public void onDrugItemClicked(DrugItem drug);

    public AddVisitsFragment getAddVisitFragment();
}
