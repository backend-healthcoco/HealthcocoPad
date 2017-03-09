package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by neha on 22/12/15.
 */
@Parcel
public class NotesTable extends SugarRecord {
    private String note;
    private String foreignTableId;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getForeignTableId() {
        return foreignTableId;
    }

    public void setForeignTableId(String foreignTableId) {
        this.foreignTableId = foreignTableId;
    }
}
