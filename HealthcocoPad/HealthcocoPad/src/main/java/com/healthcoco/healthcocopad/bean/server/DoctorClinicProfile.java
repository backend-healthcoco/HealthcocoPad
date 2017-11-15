package com.healthcoco.healthcocopad.bean.server;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.healthcoco.healthcocopad.bean.ConsultationFee;
import com.healthcoco.healthcocopad.enums.DoctorFacility;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.util.ArrayList;
import java.util.List;
@org.parceler.Parcel
public class DoctorClinicProfile extends SugarRecord {
    protected String appointmentBookingNumberJsonString;
    protected String consultationFeeJsonString;
    @Ignore
    protected boolean foreignIsClinicSelected;
    @Unique
    private String uniqueId;
    private String locationId;
    private String clinicAddress;
    private String locationName;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String patientInitial;
    private Integer patientCounter;
    @Ignore
    private List<String> appointmentBookingNumber;
    @Ignore
    private ConsultationFee consultationFee;
    @Ignore
    private ConsultationFee revisitConsultationFee;
    private String revisitConsultationFeeJsonString;
    @Ignore
    private AppointmentSlot appointmentSlot;
    private String appointmentSlotJsonString;
    @Ignore
    private List<DoctorWorkingSchedule> workingSchedules;
    private DoctorFacility facility;
    @Ignore
    private List<ClinicImage> images;
    private Boolean twentyFourSevenOpen;
    private String logoUrl;
    private String logoThumbnailUrl;
    private String foreignUniqueId;
    private String doctorId;
    private Double latitude;
    private Double longitude;
    private Integer noOfReviews;
    private Integer noOfRecommenations;
    private String hospitalId;
    @Ignore
    private ArrayList<Role> roles;

    public DoctorClinicProfile() {
    }

    public boolean isForeignIsClinicSelected() {
        return foreignIsClinicSelected;
    }

    public void setForeignIsClinicSelected(boolean foreignIsClinicSelected) {
        this.foreignIsClinicSelected = foreignIsClinicSelected;
    }

    public Integer getNoOfReviews() {
        return noOfReviews;
    }

    public void setNoOfReviews(Integer noOfReviews) {
        this.noOfReviews = noOfReviews;
    }

    public Integer getNoOfRecommenations() {
        return noOfRecommenations;
    }

    public void setNoOfRecommenations(Integer noOfRecommenations) {
        this.noOfRecommenations = noOfRecommenations;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getPatientInitial() {
        return patientInitial;
    }

    public void setPatientInitial(String patientInitial) {
        this.patientInitial = patientInitial;
    }

    public List<String> getAppointmentBookingNumber() {
        return appointmentBookingNumber;
    }

    public void setAppointmentBookingNumber(List<String> appointmentBookingNumber) {
        this.appointmentBookingNumber = appointmentBookingNumber;
    }

    public ConsultationFee getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(ConsultationFee consultationFee) {
        this.consultationFee = consultationFee;
    }

    public AppointmentSlot getAppointmentSlot() {
        return appointmentSlot;
    }

    public void setAppointmentSlot(AppointmentSlot appointmentSlot) {
        this.appointmentSlot = appointmentSlot;
    }

    public List<DoctorWorkingSchedule> getWorkingSchedules() {
        return workingSchedules;
    }

    public void setWorkingSchedules(List<DoctorWorkingSchedule> workingSchedules) {
        this.workingSchedules = workingSchedules;
    }

    public List<ClinicImage> getImages() {
        return images;
    }

    public void setImages(List<ClinicImage> images) {
        this.images = images;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoThumbnailUrl() {
        return logoThumbnailUrl;
    }

    public void setLogoThumbnailUrl(String logoThumbnailUrl) {
        this.logoThumbnailUrl = logoThumbnailUrl;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public DoctorFacility getFacility() {
        return facility;
    }

    public void setFacility(DoctorFacility facility) {
        this.facility = facility;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getPatientCounter() {
        return patientCounter;
    }

    public void setPatientCounter(Integer patientCounter) {
        this.patientCounter = patientCounter;
    }

    public Boolean getTwentyFourSevenOpen() {
        return twentyFourSevenOpen;
    }

    public void setTwentyFourSevenOpen(Boolean twentyFourSevenOpen) {
        this.twentyFourSevenOpen = twentyFourSevenOpen;
    }

    public ConsultationFee getRevisitConsultationFee() {
        return revisitConsultationFee;
    }

    public void setRevisitConsultationFee(ConsultationFee revisitConsultationFee) {
        this.revisitConsultationFee = revisitConsultationFee;
    }

    public String getFormattedClinicAddress(Context context) {
        String address = "";
        if (!Util.isNullOrBlank(getClinicAddress()))
            address = getClinicAddress();
//
//        if (!Util.isNullOrBlank(getCity())) {
//            address = Util.addCharacterToText(",", address);
//            address = address + getCity();
//        }
//        if (!Util.isNullOrBlank(getState())) {
//            address = Util.addCharacterToText(",", address);
//            address = address + getState();
//        }

        if (Util.isNullOrBlank(address))
            return "";
        return address;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public String getAppointmentBookingNumberJsonString() {
        return appointmentBookingNumberJsonString;
    }

    public void setAppointmentBookingNumberJsonString(String appointmentBookingNumberJsonString) {
        this.appointmentBookingNumberJsonString = appointmentBookingNumberJsonString;
    }

    public String getConsultationFeeJsonString() {
        return consultationFeeJsonString;
    }

    public void setConsultationFeeJsonString(String consultationFeeJsonString) {
        this.consultationFeeJsonString = consultationFeeJsonString;
    }

    public String getRevisitConsultationFeeJsonString() {
        return revisitConsultationFeeJsonString;
    }

    public void setRevisitConsultationFeeJsonString(String revisitConsultationFeeJsonString) {
        this.revisitConsultationFeeJsonString = revisitConsultationFeeJsonString;
    }

    public String getAppointmentSlotJsonString() {
        return appointmentSlotJsonString;
    }

    public void setAppointmentSlotJsonString(String appointmentSlotJsonString) {
        this.appointmentSlotJsonString = appointmentSlotJsonString;
    }
}
