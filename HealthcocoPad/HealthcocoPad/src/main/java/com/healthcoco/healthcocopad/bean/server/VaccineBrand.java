package com.healthcoco.healthcocopad.bean.server;


import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class VaccineBrand extends SugarRecord {

    @Unique
    private String uniqueId;
    private String name;
    private String groupFrom;

    public String getGroupFrom() {
        return groupFrom;
    }

    public void setGroupFrom(String groupFrom) {
        this.groupFrom = groupFrom;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
