package com.healthcoco.healthcocopad.bean.server;

import org.parceler.Parcel;

/**
 * Created by Prashant on 21/02/2018.
 */
@Parcel
public class TreatmentDynamicFields {
    private String services;

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }
}
