package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

@org.parceler.Parcel
public class DoctorExperienceDetail extends SugarRecord {
    protected String foreignUniqueId;
    private String organization;

    private String city;

    private int fromValue;

    private int toValue;

    public DoctorExperienceDetail() {
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getFromValue() {
        return fromValue;
    }

    public void setFromValue(int fromValue) {
        this.fromValue = fromValue;
    }

    public int getToValue() {
        return toValue;
    }

    public void setToValue(int toValue) {
        this.toValue = toValue;
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }
}
