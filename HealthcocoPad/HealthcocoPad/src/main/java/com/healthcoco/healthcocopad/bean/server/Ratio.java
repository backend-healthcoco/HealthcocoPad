package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Prashant on 20/09/2018.
 */
@Parcel
public class Ratio extends SugarRecord {

    private int numerator;
    private int dunomenitor;

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDunomenitor() {
        return dunomenitor;
    }

    public void setDunomenitor(int dunomenitor) {
        this.dunomenitor = dunomenitor;
    }
}
