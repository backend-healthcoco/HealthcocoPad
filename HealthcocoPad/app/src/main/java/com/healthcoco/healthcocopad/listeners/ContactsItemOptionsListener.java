package com.healthcoco.healthcocopad.listeners;

import android.support.v4.widget.SwipeRefreshLayout;

import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.enums.ChangeViewType;

public interface ContactsItemOptionsListener {
    public void onAddToGroupClicked(RegisteredPatientDetailsUpdated selecetdPatient);

    public void onCallClicked(RegisteredPatientDetailsUpdated selecetdPatient);

    public boolean isPositionVisible(int position);

    public void onAddPrescriptionClicked(RegisteredPatientDetailsUpdated selecetdPatient);

    public void onItemContactDetailClicked(RegisteredPatientDetailsUpdated selecetdPatient);

    public SwipeRefreshLayout getSwipeRefreshLayout();

    public boolean isInHomeActivity();

    public void onEditClicked(RegisteredPatientDetailsUpdated patientDetailsUpdated);

    void onQueueClicked(RegisteredPatientDetailsUpdated objData);
    public ChangeViewType getChangedViewType();
}
