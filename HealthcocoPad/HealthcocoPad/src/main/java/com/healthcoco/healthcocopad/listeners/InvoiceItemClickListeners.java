package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.Invoice;

/**
 * Created by Shreshtha on 04-08-2017.
 */

public interface InvoiceItemClickListeners {
    public void onPayInvoiceClicked(Invoice invoice);

    public void onEditInvoiceClicked(Invoice invoice);
}
