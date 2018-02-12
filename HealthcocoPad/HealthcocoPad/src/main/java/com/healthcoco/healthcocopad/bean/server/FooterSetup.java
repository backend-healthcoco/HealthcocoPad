package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Prashant on 06/02/2018.
 */
@Parcel
public class FooterSetup extends SugarRecord {

    private Boolean customFooter;
    private Boolean showSignature;
    private Boolean showPoweredBy;
    private Boolean showBottomSignText;
    private String bottomSignText;

    @Ignore
    private List<BottomTextStyle> bottomText;

    public Boolean getCustomFooter() {
        return customFooter;
    }

    public void setCustomFooter(Boolean customFooter) {
        this.customFooter = customFooter;
    }

    public Boolean getShowSignature() {
        return showSignature;
    }

    public void setShowSignature(Boolean showSignature) {
        this.showSignature = showSignature;
    }

    public Boolean getShowPoweredBy() {
        return showPoweredBy;
    }

    public void setShowPoweredBy(Boolean showPoweredBy) {
        this.showPoweredBy = showPoweredBy;
    }

    public Boolean getShowBottomSignText() {
        return showBottomSignText;
    }

    public void setShowBottomSignText(Boolean showBottomSignText) {
        this.showBottomSignText = showBottomSignText;
    }

    public String getBottomSignText() {
        return bottomSignText;
    }

    public void setBottomSignText(String bottomSignText) {
        this.bottomSignText = bottomSignText;
    }

    public List<BottomTextStyle> getBottomText() {
        return bottomText;
    }

    public void setBottomText(List<BottomTextStyle> bottomText) {
        this.bottomText = bottomText;
    }
}
