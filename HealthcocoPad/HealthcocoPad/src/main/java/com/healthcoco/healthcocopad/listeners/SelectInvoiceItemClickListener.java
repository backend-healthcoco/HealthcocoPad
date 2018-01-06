package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.User;

/**
 * Created by Shreshtha on 15-07-2017.
 */

public interface SelectInvoiceItemClickListener {
    public void onInvoiceItemClick(Object object);

    public User getUser();
}
