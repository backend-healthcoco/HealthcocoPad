package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.UnitValue;
import com.orm.annotation.Ignore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 13-07-2017.
 */

public class InvoiceRequest {
    private String uniqueId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String patientId;
    private Boolean discarded;
    private String createdBy;
    private String uniqueInvoiceId;
    private Long invoiceDate;
    @Ignore
    private ArrayList<String> receiptIds;
    private double balanceAmount = 0.0;
    private double refundAmount = 0.0;
    private double usedAdvanceAmount = 0.0;
    private double grandTotal = 0.0;
    private double totalCost = 0.0;
    @Ignore
    private UnitValue totalTax;
    @Ignore
    private UnitValue totalDiscount;
    @Ignore
    private List<InvoiceItemRequest> invoiceItems;

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

    public String getUniqueInvoiceId() {
        return uniqueInvoiceId;
    }

    public void setUniqueInvoiceId(String uniqueInvoiceId) {
        this.uniqueInvoiceId = uniqueInvoiceId;
    }

    public Long getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Long invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public ArrayList<String> getReceiptIds() {
        return receiptIds;
    }

    public void setReceiptIds(ArrayList<String> receiptIds) {
        this.receiptIds = receiptIds;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public double getUsedAdvanceAmount() {
        return usedAdvanceAmount;
    }

    public void setUsedAdvanceAmount(double usedAdvanceAmount) {
        this.usedAdvanceAmount = usedAdvanceAmount;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public UnitValue getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(UnitValue totalTax) {
        this.totalTax = totalTax;
    }

    public UnitValue getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(UnitValue totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public List<InvoiceItemRequest> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemRequest> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
