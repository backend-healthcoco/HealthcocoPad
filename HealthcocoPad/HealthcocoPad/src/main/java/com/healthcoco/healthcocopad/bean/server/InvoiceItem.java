package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.InvoiceItemType;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Shreshtha on 04-07-2017.
 */
@Parcel
public class InvoiceItem extends SugarRecord {
    @Unique
    private String customUniqueId;
    private String invoiceId;
    private String itemId;
    private String doctorId;
    private String doctorName;
    private String name;
    private InvoiceItemType type;
    @Ignore
    private Discount discount;
    private String discountJsonString;
    @Ignore
    private Quantity quantity;
    private String quantityJsonString;
    private PatientTreatmentStatus status;
    private double cost = 0.0;
    @Ignore
    private Discount tax;
    private String taxJsonString;
    private double finalCost = 0.0;
    @Ignore
    private List<TreatmentFields> treatmentFields;
    private String treatmentFieldsJsonString;

    @Ignore
    private double calculatedDiscount;
    @Ignore
    private double calculatedTax;

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

    public String getDiscountJsonString() {
        return discountJsonString;
    }

    public void setDiscountJsonString(String discountJsonString) {
        this.discountJsonString = discountJsonString;
    }

    public String getQuantityJsonString() {
        return quantityJsonString;
    }

    public void setQuantityJsonString(String quantityJsonString) {
        this.quantityJsonString = quantityJsonString;
    }

    public String getTreatmentFieldsJsonString() {
        return treatmentFieldsJsonString;
    }

    public void setTreatmentFieldsJsonString(String treatmentFieldsJsonString) {
        this.treatmentFieldsJsonString = treatmentFieldsJsonString;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
        this.customUniqueId = this.invoiceId + itemId;
    }

    public String getTaxJsonString() {
        return taxJsonString;
    }

    public void setTaxJsonString(String taxJsonString) {
        this.taxJsonString = taxJsonString;
    }

    public double getCalculatedDiscount() {
        return calculatedDiscount;
    }

    public void setCalculatedDiscount(double calculatedDiscount) {
        this.calculatedDiscount = calculatedDiscount;
    }

    public double getCalculatedTax() {
        return calculatedTax;
    }

    public void setCalculatedTax(double calculatedTax) {
        this.calculatedTax = calculatedTax;
    }

    public List<TreatmentFields> getTreatmentFields() {
        return treatmentFields;
    }

    public void setTreatmentFields(List<TreatmentFields> treatmentFields) {
        this.treatmentFields = treatmentFields;
    }
}
