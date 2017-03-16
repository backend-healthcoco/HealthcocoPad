package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

public class GeneralData extends SugarRecord {
    protected String foreignCustomHistoryId;
    @Ignore
    private Object data;
    protected String foreignDataId;

    private String dataType;
    private boolean discarded;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getForeignDataId() {
        return foreignDataId;
    }

    public void setForeignDataId(String foreignDataId) {
        this.foreignDataId = foreignDataId;
    }

    public String getForeignCustomHistoryId() {
        return foreignCustomHistoryId;
    }

    public void setForeignCustomHistoryId(String foreignCustomHistoryId) {
        this.foreignCustomHistoryId = foreignCustomHistoryId;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }
}