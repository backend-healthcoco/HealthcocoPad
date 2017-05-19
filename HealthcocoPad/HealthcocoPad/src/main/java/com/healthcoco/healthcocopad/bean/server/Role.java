package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.RoleType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Role extends SugarRecord {
    @Unique
    private String uniqueId;

    private RoleType role;

    private String description;

    private String locationId;

    private String hospitalId;
    protected String foreignLocationId;
    @Ignore
    private List<AccessModule> accessModules;

    private String createdBy;
    private String explanation;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public List<AccessModule> getAccessModules() {
        return accessModules;
    }

    public void setAccessModules(List<AccessModule> accessModules) {
        this.accessModules = accessModules;
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", role=" + role + ", description=" + description + ", locationId=" + locationId + ", hospitalId=" + hospitalId
                + ", accessModules=" + accessModules + "]";
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getForeignLocationId() {
        return foreignLocationId;
    }

    public void setForeignLocationId(String foreignLocationId) {
        this.foreignLocationId = foreignLocationId;
    }
}
