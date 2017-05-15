package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.ClinicImage;

/**
 * Created by neha on 02/02/16.
 */
public interface AddEditClinicImageListener {
    public void onAddClinicImageClicked();

    public void onClinicImageClicked(ClinicImage clinicImage);

    public void onDeleteImageClicked(ClinicImage clinicImage);
}
