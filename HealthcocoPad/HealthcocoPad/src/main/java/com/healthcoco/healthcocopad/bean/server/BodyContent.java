package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by Prashant on 20/09/2018.
 */
@Parcel
public class BodyContent extends SugarRecord {

    private double sfat;
    private double muscles;

    public double getSfat() {
        return sfat;
    }

    public void setSfat(double sfat) {
        this.sfat = sfat;
    }

    public double getMuscles() {
        return muscles;
    }

    public void setMuscles(double muscles) {
        this.muscles = muscles;
    }
}
