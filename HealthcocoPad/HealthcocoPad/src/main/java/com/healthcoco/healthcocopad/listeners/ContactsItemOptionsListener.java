package com.healthcoco.healthcocopad.listeners;

import android.support.v4.widget.SwipeRefreshLayout;

import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsNew;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsNew;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.ChangeViewType;

public interface ContactsItemOptionsListener {
    public void onAddToGroupClicked(RegisteredPatientDetailsNew selecetdPatient);

    public void onCallClicked(RegisteredPatientDetailsNew selecetdPatient);

    public boolean isPositionVisible(int position);

    public void onAddPrescriptionClicked(RegisteredPatientDetailsNew selecetdPatient);

    public void onItemContactDetailClicked(RegisteredPatientDetailsNew selecetdPatient);

    public SwipeRefreshLayout getSwipeRefreshLayout();

    public boolean isInHomeActivity();

    public boolean isMobileNumberOptional();

    public Boolean isPidHasDate();

    public void onEditClicked(RegisteredPatientDetailsNew patientDetailsUpdated);

    public void onDiscardClicked(RegisteredPatientDetailsNew patientDetailsUpdated);

    void onQueueClicked(RegisteredPatientDetailsNew objData);

    public ChangeViewType getChangedViewType();

    public User getUser();

    public void onEditPatientNumberClicked(RegisteredPatientDetailsNew selecetdPatient);

    public void onDeletePatientClicked(RegisteredPatientDetailsNew selecetdPatient);

}
