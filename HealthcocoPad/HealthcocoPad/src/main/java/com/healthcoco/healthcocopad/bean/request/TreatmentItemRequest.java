package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.Discount;
import com.healthcoco.healthcocopad.bean.server.Quantity;
import com.healthcoco.healthcocopad.bean.server.TreatmentFields;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Shreshtha on 19-07-2017.
 */
@Parcel
public class TreatmentItemRequest {
    private Discount discount;
    private Quantity quantity;
    private PatientTreatmentStatus status;
    private double cost = 0.0;
    private double finalCost = 0.0;
    private List<TreatmentFields> treatmentFields;
    private String note;
    private String treatmentServiceId;
    private TreatmentServiceRequest treatmentService;
    private List<TreatmentServiceRequest> treatmentServices;

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

    public TreatmentServiceRequest getTreatmentService() {
        return treatmentService;
    }

    public void setTreatmentService(TreatmentServiceRequest treatmentService) {
        this.treatmentService = treatmentService;
    }

    public List<TreatmentServiceRequest> getTreatmentServices() {
        return treatmentServices;
    }

    public void setTreatmentServices(List<TreatmentServiceRequest> treatmentServices) {
        this.treatmentServices = treatmentServices;
    }
}
