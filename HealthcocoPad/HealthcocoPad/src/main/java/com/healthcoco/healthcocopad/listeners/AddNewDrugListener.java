package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.Drug;

public interface AddNewDrugListener {
    public void onSaveClicked(Drug drug);
}
