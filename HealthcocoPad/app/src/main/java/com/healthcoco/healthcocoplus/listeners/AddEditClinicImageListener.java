package com.healthcoco.healthcocoplus.listeners;

import com.healthcoco.healthcocoplus.bean.server.ClinicImage;

/**
 * Created by neha on 02/02/16.
 */
public interface AddEditClinicImageListener {
    public void onAddClinicImageClicked();

    public void onClinicImageClicked(ClinicImage clinicImage);

    public void onDeleteImageClicked(ClinicImage clinicImage);
}
