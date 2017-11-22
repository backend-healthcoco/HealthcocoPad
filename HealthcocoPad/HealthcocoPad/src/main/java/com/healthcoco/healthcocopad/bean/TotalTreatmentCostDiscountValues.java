package com.healthcoco.healthcocopad.bean;

import org.parceler.Parcel;

/**
 * Created by neha on 21/11/17.
 */
@Parcel
public class TotalTreatmentCostDiscountValues {
    private double totalCost;
    private double totalDiscount;
    private double totalGrandTotal;

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalGrandTotal() {
        return totalGrandTotal;
    }

    public void setTotalGrandTotal(double totalGrandTotal) {
        this.totalGrandTotal = totalGrandTotal;
    }
}
