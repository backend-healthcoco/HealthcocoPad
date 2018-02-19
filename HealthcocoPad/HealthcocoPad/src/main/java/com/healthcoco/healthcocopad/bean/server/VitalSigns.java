package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.BloodPressure;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by neha on 14/04/16.
 */
@Parcel
public class VitalSigns extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(VitalSigns.class.getSimpleName());
    @Unique
    protected String foreignTableId;
    private String weight;
    private String pulse;
    private String temperature;
    private String breathing;
    private String height;
    private String bmi;
    private String bsa;
    @Ignore
    private BloodPressure bloodPressure;
    private String bloodPressureJsonString;

    private String spo2;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBreathing() {
        return breathing;
    }

    public void setBreathing(String breathing) {
        this.breathing = breathing;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getForeignTableId() {
        return foreignTableId;
    }

    public void setForeignTableId(String foreignTableId) {
        this.foreignTableId = foreignTableId;
    }

    public BloodPressure getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(BloodPressure bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public BloodPressure getBloodPressure(String systolic, String diastolic) {
        BloodPressure bloodPressure = new BloodPressure();
        bloodPressure.setSystolic(systolic);
        bloodPressure.setDiastolic(diastolic);
        return bloodPressure;
    }

    public boolean areAllFieldsNull() {
        if ((Util.isNullOrBlank(pulse)
                && Util.isNullOrBlank(temperature)
                && Util.isNullOrBlank(breathing)
                && Util.isNullOrBlank(height)
                && Util.isNullOrBlank(weight)
                && Util.isNullOrBlank(bsa)
                && Util.isNullOrBlank(pulse)
                && Util.isNullOrBlank(bmi)
                && (bloodPressure == null || (bloodPressure != null && bloodPressure.areAllFieldsNull())))
                && Util.isNullOrBlank(spo2))
            return true;
        return false;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public String getFormattedBreathing(HealthCocoActivity mActivity, String breathing) {
        return breathing;
    }

    public String getFormattedPulse(HealthCocoActivity mActivity, String pulse) {
        return pulse;
    }

    public String getFormattedTemprature(HealthCocoActivity mActivity, String temperature) {
        return temperature;
    }

    public String getFormattedWeight(HealthCocoActivity mActivity, String weight) {
        return weight;
    }

    public String getFormattedHeight(HealthCocoActivity mActivity, String height) {
        return height;
    }

    public String getFormattedBmi(HealthCocoActivity mActivity, String value) {
        return value;
    }

    public String getFormattedBsa(HealthCocoActivity mActivity, String value) {
        return value;
    }

    public String getFormattedSpo2(HealthCocoActivity mActivity, String spo2) {
        return spo2;
    }

    public String getFormattedBloodPressureValue(HealthCocoActivity mActivity, BloodPressure bloodPressure) {
        return bloodPressure.getSystolic()
                + "/" + " " + bloodPressure.getDiastolic() + "(" + mActivity.getResources().getString(R.string.mm_hg) + ")";
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getBloodPressureJsonString() {
        return bloodPressureJsonString;
    }

    public void setBloodPressureJsonString(String bloodPressureJsonString) {
        this.bloodPressureJsonString = bloodPressureJsonString;
    }

    public String getBsa() {
        return bsa;
    }

    public void setBsa(String bsa) {
        this.bsa = bsa;
    }


}
