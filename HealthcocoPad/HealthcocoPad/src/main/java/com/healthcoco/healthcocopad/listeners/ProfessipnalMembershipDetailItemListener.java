package com.healthcoco.healthcocopad.listeners;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Shreshtha on 21-02-2017.
 */
public interface ProfessipnalMembershipDetailItemListener {
    public void onDeleteProfessionalMembershipDetailClicked(View view, String professionalMemberships);

    public void addProfessionalMembershipDetailToList(String professionalMemberships);

    public void onProfessionalMembershipClicked(TextView tvProfessionalMembership, String professionalMemberships);
}

