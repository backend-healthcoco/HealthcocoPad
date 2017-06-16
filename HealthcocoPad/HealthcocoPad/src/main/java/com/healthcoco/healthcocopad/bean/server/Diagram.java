package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.FileDetails;
import com.healthcoco.healthcocopad.enums.RecordType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 07-02-2017.
 */
@Parcel
public class Diagram extends SugarRecord {
    private String uniqueId;

    protected String foreignClinicalNotesId;

    private String diagramUrl;

    private String tags;

    private String doctorId;

    private String locationId;

    private String hospitalId;
    @Ignore
    private FileDetails diagram;

    private String fileExtension;
    private Long createdTime;
    private Long updatedTime;
    protected String diagramFilePath;
    @Unique
    protected String customUniqueId;
    protected RecordType recordType;

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

    public String getDiagramUrl() {
        return diagramUrl;
    }

    public void setDiagramUrl(String diagramUrl) {
        this.diagramUrl = diagramUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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

    public FileDetails getDiagram() {
        return diagram;
    }

    public void setDiagram(FileDetails diagram) {
        this.diagram = diagram;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getForeignClinicalNotesId() {
        return foreignClinicalNotesId;
    }

    public void setForeignClinicalNotesId(String foreignClinicalNotesId) {
        this.foreignClinicalNotesId = foreignClinicalNotesId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getCustomUniqueId() {
//        this.uniqueId = doctorId + uniqueId;
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public String getDiagramFilePath() {
        return diagramFilePath;
    }

    public void setDiagramFilePath(String diagramFilePath) {
        this.diagramFilePath = diagramFilePath;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }
}