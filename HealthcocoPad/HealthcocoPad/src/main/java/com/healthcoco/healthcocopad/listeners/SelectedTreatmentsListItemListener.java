package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.User;

/**
 * Created by Shreshtha on 18-07-2017.
 */

public interface SelectedTreatmentsListItemListener {
    public void onDeleteItemClicked(TreatmentItem invoiceItem);

    public void onTreatmentItemClicked(TreatmentItem invoiceItem);

    public User getUser();

    public DoctorProfile getDoctorProfile();

    public void onTotalCostChange();
}
