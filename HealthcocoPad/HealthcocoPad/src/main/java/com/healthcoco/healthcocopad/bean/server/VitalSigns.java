package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
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
    @Ignore
    private BloodPressure bloodPressure;
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

    public BloodPressure getBloodPressure(String systolic, String diastolic) {
        BloodPressure bloodPressure = new BloodPressure();
        bloodPressure.setSystolic(systolic);
        bloodPressure.setDiastolic(diastolic);
        return bloodPressure;
    }

    public void setBloodPressure(BloodPressure bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public boolean areAllFieldsNull() {
        if ((Util.isNullOrBlank(pulse)
                && Util.isNullOrBlank(temperature)
                && Util.isNullOrBlank(breathing)
                && Util.isNullOrBlank(height)
                && Util.isNullOrBlank(weight)
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
        return breathing + "(" + mActivity.getResources().getString(R.string.breaths_per_min) + ")";
    }

    public String getFormattedPulse(HealthCocoActivity mActivity, String pulse) {
        return pulse + "(" + mActivity.getResources().getString(R.string.bmp) + ")";
    }

    public String getFormattedTemprature(HealthCocoActivity mActivity, String temperature) {
        return temperature + "(" + mActivity.getResources().getString(R.string.fahrenheit) + ")";
    }

    public String getFormattedWeight(HealthCocoActivity mActivity, String weight) {
        return weight + "(" + mActivity.getResources().getString(R.string.kg) + ")";
    }

    public String getFormattedSpo2(HealthCocoActivity mActivity, String spo2) {
        return spo2 + "(" + mActivity.getResources().getString(R.string.percentage) + ")";
    }

    public String getFormattedBloodPressureValue(HealthCocoActivity mActivity, BloodPressure bloodPressure) {
        return mActivity.getResources().getString(R.string.fa_angle_down_thin) + " " + bloodPressure.getSystolic()
                + "/" + " " + mActivity.getResources().getString(R.string.fa_angle_up_thin) + bloodPressure.getDiastolic() + "(" + mActivity.getResources().getString(R.string.mm_hg) + ")";
    }
}
