package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Shreshtha on 24-03-2017.
 */
@Parcel
public class Treatments extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(Treatments.class.getSimpleName());

    protected String workingHoursJson;
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
    private Boolean inHistory;
    private String visitId;
    private String uniqueEmrId;
    private double grandTotal = 0.0;
    private double totalCost = 0.0;
    @Ignore
    private UnitValue totalDiscount;
    private String totalDiscountJsonString;
    @Ignore
    private WorkingHours time;
    private String appointmentId;
    private Long fromDate;
    @Ignore
    private AppointmentRequest appointmentRequest;
    @Ignore
    private List<TreatmentItem> treatments;

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

    public Boolean getInHistory() {
        return inHistory;
    }

    public void setInHistory(Boolean inHistory) {
        this.inHistory = inHistory;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getUniqueEmrId() {
        return uniqueEmrId;
    }

    public void setUniqueEmrId(String uniqueEmrId) {
        this.uniqueEmrId = uniqueEmrId;
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

    public UnitValue getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(UnitValue totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getTotalDiscountJsonString() {
        return totalDiscountJsonString;
    }

    public void setTotalDiscountJsonString(String totalDiscountJsonString) {
        this.totalDiscountJsonString = totalDiscountJsonString;
    }

    public WorkingHours getTime() {
        return time;
    }

    public void setTime(WorkingHours time) {
        this.time = time;
    }

    public String getWorkingHoursJson() {
        return workingHoursJson;
    }

    public void setWorkingHoursJson(String workingHoursJson) {
        this.workingHoursJson = workingHoursJson;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public AppointmentRequest getAppointmentRequest() {
        return appointmentRequest;
    }

    public void setAppointmentRequest(AppointmentRequest appointmentRequest) {
        this.appointmentRequest = appointmentRequest;
    }

    public List<TreatmentItem> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<TreatmentItem> treatments) {
        this.treatments = treatments;
    }
}
