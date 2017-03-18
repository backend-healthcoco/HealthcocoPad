package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;

/**
 * Created by neha on 25/11/15.
 */
public interface SelectDrugItemClickListener {
    public void ondrugItemClick(SelectDrugItemType drugItemType, Object object);
}
