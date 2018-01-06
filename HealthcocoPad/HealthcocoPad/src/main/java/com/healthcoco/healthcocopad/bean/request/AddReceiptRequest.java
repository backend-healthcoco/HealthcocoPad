package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.enums.ModeOfPaymentType;
import com.healthcoco.healthcocopad.enums.ReceiptType;

import java.util.List;

/**
 * Created by Shreshtha on 10-07-2017.
 */

public class AddReceiptRequest {
    private String uniqueId;
    private String uniqueReceiptId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String patientId;
    private ReceiptType receiptType;
    private ModeOfPaymentType modeOfPayment;
    private Double amountPaid;
    private Double usedAdvanceAmount;
    private Long receivedDate;
    private List<String> invoiceIds;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueReceiptId() {
        return uniqueReceiptId;
    }

    public void setUniqueReceiptId(String uniqueReceiptId) {
        this.uniqueReceiptId = uniqueReceiptId;
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

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Double getUsedAdvanceAmount() {
        return usedAdvanceAmount;
    }

    public void setUsedAdvanceAmount(Double usedAdvanceAmount) {
        this.usedAdvanceAmount = usedAdvanceAmount;
    }

    public Long getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Long receivedDate) {
        this.receivedDate = receivedDate;
    }

    public List<String> getInvoiceIds() {
        return invoiceIds;
    }

    public void setInvoiceIds(List<String> invoiceIds) {
        this.invoiceIds = invoiceIds;
    }
}
