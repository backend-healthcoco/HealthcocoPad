package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

/**
 * Created by Shreshtha on 21-02-2017.
 */

public class ProfessionalMembership extends SugarRecord {
    private String foreignUniqueId;
    private String membership;
    private Long updatedTime;

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
