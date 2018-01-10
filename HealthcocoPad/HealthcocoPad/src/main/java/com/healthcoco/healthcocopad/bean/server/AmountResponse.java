package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 08-07-2017.
 */
@Parcel
public class AmountResponse extends SugarRecord {
    private double totalDueAmount;
    private double totalRemainingAdvanceAmount;

    public double getTotalDueAmount() {
        return totalDueAmount;
    }

    public void setTotalDueAmount(double totalDueAmount) {
        this.totalDueAmount = totalDueAmount;
    }

    public double getTotalRemainingAdvanceAmount() {
        return totalRemainingAdvanceAmount;
    }

    public void setTotalRemainingAdvanceAmount(double totalRemainingAdvanceAmount) {
        this.totalRemainingAdvanceAmount = totalRemainingAdvanceAmount;
    }
}
