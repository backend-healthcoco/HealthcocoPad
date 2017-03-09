package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 24-01-2017.
 */
@Parcel
public class CityResponse extends SugarRecord {
    @Unique
    private String uniqueId;
    private String description;
    private Double latitude;
    private Double longitude;
    private String city;
    private String stateId;
    private Boolean isActivated;
    private Long createdTime;
    private Long updatedTime;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
