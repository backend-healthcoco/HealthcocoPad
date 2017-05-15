package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

@Parcel
public class Relations extends SugarRecord {

    private String name;

    private String relation;
    private String foreignPatientId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "Relations [name=" + name + ", relation=" + relation + "]";
    }

    public String getForeignPatientId() {
        return foreignPatientId;
    }

    public void setForeignPatientId(String foreignPatientId) {
        this.foreignPatientId = foreignPatientId;
    }

}
