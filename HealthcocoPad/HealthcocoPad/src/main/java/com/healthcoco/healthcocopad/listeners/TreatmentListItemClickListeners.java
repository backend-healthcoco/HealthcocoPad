package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;

/**
 * Created by Shreshtha on 04-08-2017.
 */

public interface TreatmentListItemClickListeners {
    public void onInvoiceClicked(Treatments treatment);

    public void onAddTreatmentClicked(Treatments treatment);

    public User getUser();

}
