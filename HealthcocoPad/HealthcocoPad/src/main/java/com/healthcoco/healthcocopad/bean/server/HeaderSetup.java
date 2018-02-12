package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Prashant on 06/02/2018.
 */
@Parcel
public class HeaderSetup extends SugarRecord {

    private String logoType;
    private String headerHtml;
    private Boolean customHeader;
    private Boolean customLogo;

    @Ignore
    private PatientDetails patientDetails;
    @Ignore
    private List<LeftText> topLeftText;
    @Ignore
    private List<RightText> topRightText;

    public String getLogoType() {
        return logoType;
    }

    public void setLogoType(String logoType) {
        this.logoType = logoType;
    }

    public String getHeaderHtml() {
        return headerHtml;
    }

    public void setHeaderHtml(String headerHtml) {
        this.headerHtml = headerHtml;
    }

    public Boolean getCustomHeader() {
        return customHeader;
    }

    public void setCustomHeader(Boolean customHeader) {
        this.customHeader = customHeader;
    }

    public Boolean getCustomLogo() {
        return customLogo;
    }

    public void setCustomLogo(Boolean customLogo) {
        this.customLogo = customLogo;
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }

    public List<LeftText> getTopLeftText() {
        return topLeftText;
    }

    public void setTopLeftText(List<LeftText> topLeftText) {
        this.topLeftText = topLeftText;
    }

    public List<RightText> getTopRightText() {
        return topRightText;
    }

    public void setTopRightText(List<RightText> topRightText) {
        this.topRightText = topRightText;
    }
}
