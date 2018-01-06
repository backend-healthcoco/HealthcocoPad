package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.Discount;
import com.healthcoco.healthcocopad.bean.server.Quantity;
import com.healthcoco.healthcocopad.bean.server.TreatmentFields;
import com.healthcoco.healthcocopad.enums.InvoiceItemType;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.orm.annotation.Ignore;

import java.util.List;

/**
 * Created by Shreshtha on 13-07-2017.
 */

public class InvoiceItemRequest {
    private String itemId;
    private String doctorId;
    private String doctorName;
    private String name;
    private InvoiceItemType type;
    @Ignore
    private Discount discount;
    @Ignore
    private Quantity quantity;
    private PatientTreatmentStatus status;
    private double cost = 0.0;
    @Ignore
    private Discount tax;
    private double finalCost = 0.0;
    @Ignore
    private List<TreatmentFields> treatmentFields;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InvoiceItemType getType() {
        return type;
    }

    public void setType(InvoiceItemType type) {
        this.type = type;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public PatientTreatmentStatus getStatus() {
        return status;
    }

    public void setStatus(PatientTreatmentStatus status) {
        this.status = status;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Discount getTax() {
        return tax;
    }

    public void setTax(Discount tax) {
        this.tax = tax;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }

    public List<TreatmentFields> getTreatmentFields() {
        return treatmentFields;
    }

    public void setTreatmentFields(List<TreatmentFields> treatmentFields) {
        this.treatmentFields = treatmentFields;
    }
}
