package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Shreshtha on 24-03-2017.
 */
@Parcel
public class Treatments extends SugarRecord {
    @Ignore
    private TreatmentService treatmentService;
    private String treatmentServiceId;
    private PatientTreatmentStatus status;
    private double cost = 0.0;
    private String note;
    private double finalCost = 0.0;
    @Ignore
    private Discount discount;
    @Ignore
    private Quantity quantity;
    @Ignore
    private List<TreatmentService> treatmentServices;

    public TreatmentService getTreatmentService() {
        return treatmentService;
    }

    public void setTreatmentService(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    public String getTreatmentServiceId() {
        return treatmentServiceId;
    }

    public void setTreatmentServiceId(String treatmentServiceId) {
        this.treatmentServiceId = treatmentServiceId;
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

    public List<TreatmentService> getTreatmentServices() {
        return treatmentServices;
    }

    public void setTreatmentServices(List<TreatmentService> treatmentServices) {
        this.treatmentServices = treatmentServices;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }
}
