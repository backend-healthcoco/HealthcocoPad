package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Shreshtha on 14-07-2017.
 */

@Parcel
public class TreatmentItem extends SugarRecord {
    @Unique
    private String customUniqueId;
    private String treatmentId;
    @Ignore
    private Discount discount;
    private String discountJsonString;
    @Ignore
    private Quantity quantity;
    private String quantityJsonString;
    private PatientTreatmentStatus status;
    private double cost = 0.0;
    private double finalCost = 0.0;
    @Ignore
    private List<TreatmentFields> treatmentFields;
    private String treatmentFieldsJsonString;
    private String note;
    private String treatmentServiceId;
    @Ignore
    private TreatmentService treatmentService;
    @Ignore
    private List<TreatmentService> treatmentServices;
    @Ignore
    private double calculatedDiscount;

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public String getDiscountJsonString() {
        return discountJsonString;
    }

    public void setDiscountJsonString(String discountJsonString) {
        this.discountJsonString = discountJsonString;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public String getQuantityJsonString() {
        return quantityJsonString;
    }

    public void setQuantityJsonString(String quantityJsonString) {
        this.quantityJsonString = quantityJsonString;
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

    public String getTreatmentFieldsJsonString() {
        return treatmentFieldsJsonString;
    }

    public void setTreatmentFieldsJsonString(String treatmentFieldsJsonString) {
        this.treatmentFieldsJsonString = treatmentFieldsJsonString;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTreatmentServiceId() {
        return treatmentServiceId;
    }

    public void setTreatmentServiceId(String treatmentServiceId) {
        this.treatmentServiceId = treatmentServiceId;
    }

    public TreatmentService getTreatmentService() {
        return treatmentService;
    }

    public void setTreatmentService(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    public List<TreatmentService> getTreatmentServices() {
        return treatmentServices;
    }

    public void setTreatmentServices(List<TreatmentService> treatmentServices) {
        this.treatmentServices = treatmentServices;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
        this.customUniqueId = this.treatmentId + treatmentServiceId;
    }

    public double getCalculatedDiscount() {
        return calculatedDiscount;
    }

    public void setCalculatedDiscount(double calculatedDiscount) {
        this.calculatedDiscount = calculatedDiscount;
    }
}
