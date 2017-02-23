package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 25-01-2017.
 */
public class Hospital extends SugarRecord {
    @Unique
    private String uniqueId;

    private String hospitalName;

    private String hospitalPhoneNumber;

    private String hospitalImageUrl;

    private String hospitalDescription;
    protected String foreignUniqueId;
    @Ignore
    private List<LocationAndAccessControl> locationsAndAccessControl = new ArrayList<LocationAndAccessControl>();

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalPhoneNumber() {
        return hospitalPhoneNumber;
    }

    public void setHospitalPhoneNumber(String hospitalPhoneNumber) {
        this.hospitalPhoneNumber = hospitalPhoneNumber;
    }

    public String getHospitalImageUrl() {
        return hospitalImageUrl;
    }

    public void setHospitalImageUrl(String hospitalImageUrl) {
        this.hospitalImageUrl = hospitalImageUrl;
    }

    public String getHospitalDescription() {
        return hospitalDescription;
    }

    public void setHospitalDescription(String hospitalDescription) {
        this.hospitalDescription = hospitalDescription;
    }

    public List<LocationAndAccessControl> getLocationsAndAccessControl() {
        return locationsAndAccessControl;
    }

    public void setLocationsAndAccessControl(List<LocationAndAccessControl> locationsAndAccessControl) {
        this.locationsAndAccessControl = locationsAndAccessControl;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }
}
