package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.ModeOfPaymentType;
import com.healthcoco.healthcocopad.enums.ReceiptType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Shreshtha on 07-07-2017.
 */
@Parcel
public class ReceiptResponse extends SugarRecord {
    @Unique
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String patientId;
    private Boolean discarded;
    private String createdBy;
    private String uniqueReceiptId;
    private Long invoiceDate;
    private String InvoiceId;
    private double usedAdvanceAmount;
    private Long receivedDate;
    private double balanceAmount;
    private double remainingAdvanceAmount;
    private double amountPaid;
    private String uniqueInvoiceId;
    private ReceiptType receiptType;
    private ModeOfPaymentType modeOfPayment;
    @Ignore
    private List<AdvanceReceiptIdWithAmountsResponse> advanceReceiptIdWithAmounts;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUniqueReceiptId() {
        return uniqueReceiptId;
    }

    public void setUniqueReceiptId(String uniqueReceiptId) {
        this.uniqueReceiptId = uniqueReceiptId;
    }

    public Long getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Long invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        InvoiceId = invoiceId;
    }

    public double getUsedAdvanceAmount() {
        return usedAdvanceAmount;
    }

    public void setUsedAdvanceAmount(double usedAdvanceAmount) {
        this.usedAdvanceAmount = usedAdvanceAmount;
    }

    public Long getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Long receivedDate) {
        this.receivedDate = receivedDate;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public double getRemainingAdvanceAmount() {
        return remainingAdvanceAmount;
    }

    public void setRemainingAdvanceAmount(double remainingAdvanceAmount) {
        this.remainingAdvanceAmount = remainingAdvanceAmount;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getUniqueInvoiceId() {
        return uniqueInvoiceId;
    }

    public void setUniqueInvoiceId(String uniqueInvoiceId) {
        this.uniqueInvoiceId = uniqueInvoiceId;
    }

    public ReceiptType getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(ReceiptType receiptType) {
        this.receiptType = receiptType;
    }

    public ModeOfPaymentType getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(ModeOfPaymentType modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public List<AdvanceReceiptIdWithAmountsResponse> getAdvanceReceiptIdWithAmounts() {
        return advanceReceiptIdWithAmounts;
    }

    public void setAdvanceReceiptIdWithAmounts(List<AdvanceReceiptIdWithAmountsResponse> advanceReceiptIdWithAmounts) {
        this.advanceReceiptIdWithAmounts = advanceReceiptIdWithAmounts;
    }
}
