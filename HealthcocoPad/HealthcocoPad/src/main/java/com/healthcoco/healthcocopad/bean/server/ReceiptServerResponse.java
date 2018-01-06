package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 10-07-2017.
 */
@Parcel
public class ReceiptServerResponse extends SugarRecord {
    @Ignore
    private ReceiptResponse doctorPatientReceipt;
    @Ignore
    private Invoice invoice;
    private double totalDueAmount;
    private double totalRemainingAdvanceAmount;

    public ReceiptResponse getDoctorPatientReceipt() {
        return doctorPatientReceipt;
    }

    public void setDoctorPatientReceipt(ReceiptResponse doctorPatientReceipt) {
        this.doctorPatientReceipt = doctorPatientReceipt;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

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
