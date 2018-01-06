package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.TotalTreatmentCostDiscountValues;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.InvoiceItem;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.User;

/**
 * Created by Shreshtha on 18-07-2017.
 */

public interface SelectedInvoiceListItemListener {
    public void onDeleteItemClicked(InvoiceItem invoiceItem);

    public void onInvoiceItemClicked(InvoiceItem invoiceItem);

    public void onTotalValueTypeDetailChanged(String invoiceId, TotalTreatmentCostDiscountValues totalTreatmentCostDiscountValues);

    public User getUser();

    public DoctorProfile getDoctorProfile();

    public void onTotalCostChange();
}
