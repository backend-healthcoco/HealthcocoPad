package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Prashant on 06/02/2018.
 */
@Parcel
public class RightText extends SugarRecord {

    protected String fontStyleString;
    private String text;
    private String fontColor;
    private String fontSize;
    private Boolean showTreatmentcost;
    private String instructionAlign;
    @Ignore
    private ArrayList<String> fontStyle;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public Boolean getShowTreatmentcost() {
        return showTreatmentcost;
    }

    public void setShowTreatmentcost(Boolean showTreatmentcost) {
        this.showTreatmentcost = showTreatmentcost;
    }

    public String getInstructionAlign() {
        return instructionAlign;
    }

    public void setInstructionAlign(String instructionAlign) {
        this.instructionAlign = instructionAlign;
    }

    public ArrayList<String> getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(ArrayList<String> fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getFontStyleString() {
        return fontStyleString;
    }

    public void setFontStyleString(String fontStyleString) {
        this.fontStyleString = fontStyleString;
    }
}
