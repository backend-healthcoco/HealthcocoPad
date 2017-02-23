package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;

/**
 * Created by neha on 04/02/16.
 */
public class ForeignOtherEmailAddresses extends SugarRecord {
    private String foreignUniqueId;
    private String otherEmailAddress;

    public String getOtherEmailAddress() {
        return otherEmailAddress;
    }

    public void setOtherEmailAddress(String otherEmailAddress) {
        this.otherEmailAddress = otherEmailAddress;
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }
}
