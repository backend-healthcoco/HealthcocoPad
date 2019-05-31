package com.healthcoco.healthcocopad.bean.server;

import android.graphics.Bitmap;

import com.healthcoco.healthcocopad.bean.Address;
import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class RegisteredPatientDetailsUpdated extends SugarRecord {

    protected String foreignPatientId;
    protected String addressJsonString;
    @Ignore
    protected ArrayList<String> groupIds;
    protected String groupIdsJsonString;
    protected String imageFilePath;
    protected String foreignReferredById;
    protected String referredByJsonString;
    protected String bloodGroup;
    protected String profession;
    protected String consultantDoctorIdsJsonString;
    @Unique
    private String backendPatientId;
    private String uniqueId;
    @Ignore
    private Bitmap profileImageBitmap;
    private String localPatientName;
    private String firstName;
    private String lastName;
    private String middleName;
    private String imageUrl;
    @Ignore
    private DOB dob;
    private String dobJsonString;
    private String userId;
    private String userName;
    private String mobileNumber;
    private String gender;
    @Ignore
    private Patient patient;
    @Ignore
    private Address address;
    @Ignore
    private List<UserGroups> groups;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String pid;
    private String pnum;
    private String colorCode;
    private Long createdTime;
    private Long updatedTime;
    @Ignore
    private Reference referredBy;
    private String thumbnailUrl;
    private Boolean discarded;
    private String emailAddress;
    private boolean isPatientDiscarded;
    private boolean isPartOfConsultantDoctor;
    @Ignore
    private ArrayList<String> consultantDoctorIds;
    private double totalDueAmount;
    private double totalRemainingAdvanceAmount;

    private boolean isChild;

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }
    public boolean isPartOfConsultantDoctor() {
        return isPartOfConsultantDoctor;
    }

    public void setPartOfConsultantDoctor(boolean partOfConsultantDoctor) {
        isPartOfConsultantDoctor = partOfConsultantDoctor;
    }

    public ArrayList<String> getConsultantDoctorIds() {
        return consultantDoctorIds;
    }

    public void setConsultantDoctorIds(ArrayList<String> consultantDoctorIds) {
        this.consultantDoctorIds = consultantDoctorIds;
    }

    public String getConsultantDoctorIdsJsonString() {
        return consultantDoctorIdsJsonString;
    }

    public void setConsultantDoctorIdsJsonString(String consultantDoctorIdsJsonString) {
        this.consultantDoctorIdsJsonString = consultantDoctorIdsJsonString;
    }

    public DOB getDob() {
        return dob;
    }

    public void setDob(DOB dob) {
        this.dob = dob;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getGender() {
        if (!Util.isNullOrBlank(gender))
            gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocalPatientName() {
        if (localPatientName == null) return HealthCocoConstants.NO_NAME_FLAG;
        return localPatientName;
    }

    public void setLocalPatientName(String localPatientName) {
        this.localPatientName = localPatientName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getForeignPatientId() {
        return foreignPatientId;
    }

    public void setForeignPatientId(String foreignPatientId) {
        this.foreignPatientId = foreignPatientId;
    }

    public String getColorCode() {
        if (colorCode == null)
            return "#0077b5";
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public List<UserGroups> getGroups() {
        return groups;
    }

    public void setGroups(List<UserGroups> groups) {
        this.groups = groups;
    }

    public ArrayList<String> getGroupIds() {
        if (groupIds == null)
            groupIds = new ArrayList<>();
        return groupIds;
    }

    public void setGroupIds(ArrayList<String> groupIds) {
        this.groupIds = groupIds;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }


    public Reference getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Reference referredBy) {
        this.referredBy = referredBy;
    }

    public String getForeignReferredById() {
        return foreignReferredById;
    }

    public void setForeignReferredById(String foreignReferredById) {
        this.foreignReferredById = foreignReferredById;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public Bitmap getProfileImageBitmap() {
        return profileImageBitmap;
    }

    public void setProfileImageBitmap(Bitmap profileImageBitmap) {
        this.profileImageBitmap = profileImageBitmap;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getDobJsonString() {
        return dobJsonString;
    }

    public void setDobJsonString(String dobJsonString) {
        this.dobJsonString = dobJsonString;
    }

    public String getAddressJsonString() {
        return addressJsonString;
    }

    public void setAddressJsonString(String addressJsonString) {
        this.addressJsonString = addressJsonString;
    }

    public String getGroupIdsJsonString() {
        return groupIdsJsonString;
    }

    public void setGroupIdsJsonString(String groupIdsJsonString) {
        this.groupIdsJsonString = groupIdsJsonString;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getReferredByJsonString() {
        return referredByJsonString;
    }

    public void setReferredByJsonString(String referredByJsonString) {
        this.referredByJsonString = referredByJsonString;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public double getTotalDueAmount() {
        return totalDueAmount;
    }

    public void setTotalDueAmount(double totalDueAmount) {
        this.totalDueAmount = totalDueAmount;
    }

    public double getTotalRemainingAdvanceAmount() {
        return totalRemainingAdvanceAmount;
    }

    public void setTotalRemainingAdvanceAmount(double totalRemainingAdvanceAmount) {
        this.totalRemainingAdvanceAmount = totalRemainingAdvanceAmount;
    }

    public boolean isPatientDiscarded() {
        return isPatientDiscarded;
    }

    public void setPatientDiscarded(boolean patientDiscarded) {
        isPatientDiscarded = patientDiscarded;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getBackendPatientId() {
        return backendPatientId;
    }

    public void setBackendPatientId(String backendPatientId) {
        this.backendPatientId = backendPatientId;
    }
}
