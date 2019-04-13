package com.healthcoco.healthcocopad.listeners;

import android.support.v4.widget.SwipeRefreshLayout;

import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;

public interface ContactsItemOptionsForDeletedListener {

    public boolean isPositionVisible(int position);

    public void onDeletePatientClicked(RegisteredPatientDetailsUpdated selecetdPatient);

    public SwipeRefreshLayout getSwipeRefreshLayout();

    public User getUser();
}
