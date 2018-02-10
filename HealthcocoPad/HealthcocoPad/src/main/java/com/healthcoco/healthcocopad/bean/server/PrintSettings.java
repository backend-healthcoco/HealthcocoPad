package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by Prashant on 06/02/2018.
 */
@Parcel
public class PrintSettings extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(PrintSettings.class.getSimpleName());

    @Unique
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String componentType;

    @Ignore
    private PageSetup pageSetup;
    @Ignore
    private HeaderSetup headerSetup;
    @Ignore
    private FooterSetup footerSetup;
    @Ignore
    private Style contentSetup;

    private Boolean discarded;
    private String clinicLogoUrl;
    private String hospitalUId;
    private String contentLineSpace;
    private String contentLineStyle;
    private Boolean showDrugGenericNames;


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public PageSetup getPageSetup() {
        return pageSetup;
    }

    public void setPageSetup(PageSetup pageSetup) {
        this.pageSetup = pageSetup;
    }

    public HeaderSetup getHeaderSetup() {
        return headerSetup;
    }

    public void setHeaderSetup(HeaderSetup headerSetup) {
        this.headerSetup = headerSetup;
    }

    public FooterSetup getFooterSetup() {
        return footerSetup;
    }

    public void setFooterSetup(FooterSetup footerSetup) {
        this.footerSetup = footerSetup;
    }

    public Style getContentSetup() {
        return contentSetup;
    }

    public void setContentSetup(Style contentSetup) {
        this.contentSetup = contentSetup;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public String getClinicLogoUrl() {
        return clinicLogoUrl;
    }

    public void setClinicLogoUrl(String clinicLogoUrl) {
        this.clinicLogoUrl = clinicLogoUrl;
    }

    public String getHospitalUId() {
        return hospitalUId;
    }

    public void setHospitalUId(String hospitalUId) {
        this.hospitalUId = hospitalUId;
    }

    public String getContentLineSpace() {
        return contentLineSpace;
    }

    public void setContentLineSpace(String contentLineSpace) {
        this.contentLineSpace = contentLineSpace;
    }

    public String getContentLineStyle() {
        return contentLineStyle;
    }

    public void setContentLineStyle(String contentLineStyle) {
        this.contentLineStyle = contentLineStyle;
    }

    public Boolean getShowDrugGenericNames() {
        return showDrugGenericNames;
    }

    public void setShowDrugGenericNames(Boolean showDrugGenericNames) {
        this.showDrugGenericNames = showDrugGenericNames;
    }
}
