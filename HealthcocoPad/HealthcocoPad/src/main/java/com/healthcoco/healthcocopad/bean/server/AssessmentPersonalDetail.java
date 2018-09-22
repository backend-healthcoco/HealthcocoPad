package com.healthcoco.healthcocopad.bean.server;

import android.graphics.Bitmap;

import com.healthcoco.healthcocopad.bean.Address;
import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class AssessmentPersonalDetail extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(AssessmentPersonalDetail.class.getSimpleName());
    protected String physicalStatusTypeJsonString;
    protected String dobJsonString;
    protected String addressJsonString;
    @Unique
    private String uniqueId;
    private Long adminCreatedTime;
    private String createdBy;
    private String patientId;
    private String bloodGroup;
    @Ignore
    private ArrayList<String> physicalStatusType;
    private String goal;
    private String community;
    private int noOfAdultMember;
    private int noOfChildMember;
    private String profession;
    private String firstName;
    @Ignore
    private DOB dob;
    private int age;
    private String mobileNumber;
    private String gender;
    @Ignore
    private Address address;
    private String assessmentUniqueId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private Long createdTime;
    private Long updatedTime;
    private Boolean discarded;


    public String getAddressJsonString() {
        return addressJsonString;
    }

    public void setAddressJsonString(String addressJsonString) {
        this.addressJsonString = addressJsonString;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getAdminCreatedTime() {
        return adminCreatedTime;
    }

    public void setAdminCreatedTime(Long adminCreatedTime) {
        this.adminCreatedTime = adminCreatedTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public int getNoOfAdultMember() {
        return noOfAdultMember;
    }

    public void setNoOfAdultMember(int noOfAdultMember) {
        this.noOfAdultMember = noOfAdultMember;
    }

    public int getNoOfChildMember() {
        return noOfChildMember;
    }

    public void setNoOfChildMember(int noOfChildMember) {
        this.noOfChildMember = noOfChildMember;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public DOB getDob() {
        return dob;
    }

    public void setDob(DOB dob) {
        this.dob = dob;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public String getPhysicalStatusTypeJsonString() {
        return physicalStatusTypeJsonString;
    }

    public void setPhysicalStatusTypeJsonString(String physicalStatusTypeJsonString) {
        this.physicalStatusTypeJsonString = physicalStatusTypeJsonString;
    }

    public String getDobJsonString() {
        return dobJsonString;
    }

    public void setDobJsonString(String dobJsonString) {
        this.dobJsonString = dobJsonString;
    }

    public String getAssessmentUniqueId() {
        return assessmentUniqueId;
    }

    public void setAssessmentUniqueId(String assessmentUniqueId) {
        this.assessmentUniqueId = assessmentUniqueId;
    }

    public ArrayList<String> getPhysicalStatusType() {
        return physicalStatusType;
    }

    public void setPhysicalStatusType(ArrayList<String> physicalStatusType) {
        this.physicalStatusType = physicalStatusType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
