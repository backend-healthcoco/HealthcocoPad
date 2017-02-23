package com.healthcoco.healthcocoplus.bean.server;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;


import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.List;
import java.util.Locale;

@org.parceler.Parcel
public class DoctorProfile extends SugarRecord {

    private String uniqueId;
    @Unique
    private String doctorId;

    private String userId;

    private String title;

    private String firstName;

    private String emailAddress;

    private String mobileNumber;

    private String gender;

    private String imageUrl;

    private String thumbnailUrl;

    private DOB dob;

    private String coverImageUrl;

    private String coverThumbnailImageUrl;
    private String colorCode;
    //numbers saved in AdditinalNumbers Table reference to doctorId
    @Ignore
    private List<String> additionalNumbers;


    //email addresses saved in EmailAddresses Table reference to doctorId
    @Ignore
    private List<String> otherEmailAddresses;
    @Ignore
    private DoctorExperience experience;

    @Ignore
    private List<Education> education;

    @Ignore
    private List<String> specialities;
    @Ignore
    private List<Achievement> achievements;

    private String professionalStatement;
    @Ignore
    private List<DoctorRegistrationDetail> registrationDetails;

    @Ignore
    private List<DoctorExperienceDetail> experienceDetails;
    @Ignore
    private List<String> professionalMemberships;

    @Ignore
    private List<DoctorClinicProfile> clinicProfile;

    protected String profileImagePath;
    protected String coverImagePath;

    public DoctorProfile() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle(boolean isOnPost) {
        title = Util.getValidatedValue(title);
        if (isOnPost)
            return Util.getValidatedValue(title);
        return title + " ";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public DOB getDob() {
        return dob;
    }

    public void setDob(DOB dob) {
        this.dob = dob;
    }

    public List<String> getAdditionalNumbers() {
        return additionalNumbers;
    }

    public void setAdditionalNumbers(List<String> additionalNumbers) {
        this.additionalNumbers = additionalNumbers;
    }

    public List<String> getOtherEmailAddresses() {
        return otherEmailAddresses;
    }

    public void setOtherEmailAddresses(List<String> otherEmailAddresses) {
        this.otherEmailAddresses = otherEmailAddresses;
    }

    public DoctorExperience getExperience() {
        return experience;
    }

    public void setExperience(DoctorExperience experience) {
        this.experience = experience;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public List<String> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<String> specialities) {
        this.specialities = specialities;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public String getProfessionalStatement() {
        return professionalStatement;
    }

    public void setProfessionalStatement(String professionalStatement) {
        this.professionalStatement = professionalStatement;
    }

    public List<DoctorRegistrationDetail> getRegistrationDetails() {
        return registrationDetails;
    }

    public void setRegistrationDetails(List<DoctorRegistrationDetail> registrationDetails) {
        this.registrationDetails = registrationDetails;
    }

    public List<DoctorExperienceDetail> getExperienceDetails() {
        return experienceDetails;
    }

    public void setExperienceDetails(List<DoctorExperienceDetail> experienceDetails) {
        this.experienceDetails = experienceDetails;
    }

    public List<String> getProfessionalMemberships() {
        return professionalMemberships;
    }

    public void setProfessionalMemberships(List<String> professionalMemberships) {
        this.professionalMemberships = professionalMemberships;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public List<DoctorClinicProfile> getClinicProfile() {
        return clinicProfile;
    }

    public void setClinicProfile(List<DoctorClinicProfile> clinicProfile) {
        this.clinicProfile = clinicProfile;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverThumbnailImageUrl() {
        return coverThumbnailImageUrl;
    }

    public void setCoverThumbnailImageUrl(String coverThumbnailImageUrl) {
        this.coverThumbnailImageUrl = coverThumbnailImageUrl;
    }

    @Override
    public String toString() {
        return "DoctorProfile [id=" + id + ", userId=" + userId + ", title=" + title + ", firstName=" + firstName + ", emailAddress=" + emailAddress + ", mobileNumber=" + mobileNumber + ", gender=" + gender + ", imageUrl=" + imageUrl
                + ", thumbnailUrl=" + thumbnailUrl + ", dob=" + dob + ", coverImageUrl=" + coverImageUrl + ", coverThumbnailImageUrl=" + coverThumbnailImageUrl
                + ", additionalNumbers=" + additionalNumbers + ", otherEmailAddresses=" + otherEmailAddresses + ", experience=" + experience + ", education="
                + education + ", specialities=" + specialities + ", achievements=" + achievements + ", professionalStatement=" + professionalStatement
                + ", registrationDetails=" + registrationDetails + ", experienceDetails=" + experienceDetails + ", professionalMemberships="
                + professionalMemberships + ", clinicProfile=" + clinicProfile + "]";
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getFormattedExperience(Activity mActivity, boolean getDashForNull) {
        String experience = "";
        //setting experience
        if (getExperience() != null) {
            experience = experience + getExperience().getExperience();
            String period = "";
            if (getExperience().getPeriod() != null) {
                period = String.valueOf(getExperience().getPeriod());
                if (getExperience().getExperience() > 1)
                    period = period + "s";
            } else
                period = mActivity.getResources().getString(R.string.years);
            experience = experience + " " + period.substring(0, 1).toUpperCase(Locale.getDefault()) + period.substring(1).toLowerCase(Locale.getDefault());
        } else if (getDashForNull)
            return mActivity.getResources().getString(R.string.no_text_dash);
        return experience;
    }
}
