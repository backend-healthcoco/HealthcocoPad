package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.fragments.AddVisitsFragment;

/**
 * Created by neha on 22/04/17.
 */

public interface AddNewPrescriptionListener {
    public void onDeleteItemClicked(DrugItem drug);

    public void onDrugItemClicked(DrugItem drug);

    public String getDurationUnit();

    public boolean isDurationSet();

    public AddVisitsFragment getAddVisitFragment();
}
