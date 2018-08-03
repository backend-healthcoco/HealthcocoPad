package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class BiometricDetails extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(BiometricDetails.class.getSimpleName());


    private Long createdTime;
    private Long updatedTime;
    @Unique
    private String uniqueId;

    private byte[] bioDetails;

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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public byte[] getBioDetails() {
        return bioDetails;
    }

    public void setBioDetails(byte[] bioDetails) {
        this.bioDetails = bioDetails;
    }
}
