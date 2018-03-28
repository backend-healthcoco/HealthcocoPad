package com.healthcoco.healthcocopad.listeners;

/**
 * Created by Prashant on 28/03/2018.
 */

public interface AppointmentDetailsPopupListener {

    public void onVisitClicked(Object object);

    public void onInvoiceClicked(Object object);

    public void onReceiptClicked(Object object);

    public void onEditClicked(Object object);

    public void onRescheduleClicked(Object object);

    public void onCancelClicked(Object object);
}
