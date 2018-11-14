package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.LifeStyleType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class PatientLifeStyle extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(PatientLifeStyle.class.getSimpleName());

    @Unique
    private String uniqueId;
    private Long adminCreatedTime;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
    private String patientId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String assessmentId;

    @Ignore
    private List<WorkingSchedule> workingschedules;
    @Ignore
    private List<SleepPattern> sleepPatterns;
    @Ignore
    private List<WorkingSchedule> trivalingPeriod;

    private Integer socialMediaTime;
    private Integer tvInBedRoomForMinute;
    private Integer laptopInBedRoomForMinute;

    private Boolean laptopInBedRoom;
    private Boolean tvInBedRoom;
    @Ignore
    private SleepPattern tvViewTime;
    @Ignore
    private SleepPattern loptopUseTime;
    @Ignore
    private SleepPattern mobileUsage;

    private LifeStyleType type;


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

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public List<WorkingSchedule> getWorkingschedules() {
        return workingschedules;
    }

    public void setWorkingschedules(List<WorkingSchedule> workingschedules) {
        this.workingschedules = workingschedules;
    }

    public List<SleepPattern> getSleepPatterns() {
        return sleepPatterns;
    }

    public void setSleepPatterns(List<SleepPattern> sleepPatterns) {
        this.sleepPatterns = sleepPatterns;
    }

    public List<WorkingSchedule> getTrivalingPeriod() {
        return trivalingPeriod;
    }

    public void setTrivalingPeriod(List<WorkingSchedule> trivalingPeriod) {
        this.trivalingPeriod = trivalingPeriod;
    }

    public Integer getSocialMediaTime() {
        return socialMediaTime;
    }

    public void setSocialMediaTime(Integer socialMediaTime) {
        this.socialMediaTime = socialMediaTime;
    }

    public Integer getTvInBedRoomForMinute() {
        return tvInBedRoomForMinute;
    }

    public void setTvInBedRoomForMinute(Integer tvInBedRoomForMinute) {
        this.tvInBedRoomForMinute = tvInBedRoomForMinute;
    }

    public Integer getLaptopInBedRoomForMinute() {
        return laptopInBedRoomForMinute;
    }

    public void setLaptopInBedRoomForMinute(Integer laptopInBedRoomForMinute) {
        this.laptopInBedRoomForMinute = laptopInBedRoomForMinute;
    }

    public Boolean getLaptopInBedRoom() {
        return laptopInBedRoom;
    }

    public void setLaptopInBedRoom(Boolean laptopInBedRoom) {
        this.laptopInBedRoom = laptopInBedRoom;
    }

    public Boolean getTvInBedRoom() {
        return tvInBedRoom;
    }

    public void setTvInBedRoom(Boolean tvInBedRoom) {
        this.tvInBedRoom = tvInBedRoom;
    }

    public SleepPattern getTvViewTime() {
        return tvViewTime;
    }

    public void setTvViewTime(SleepPattern tvViewTime) {
        this.tvViewTime = tvViewTime;
    }

    public SleepPattern getLoptopUseTime() {
        return loptopUseTime;
    }

    public void setLoptopUseTime(SleepPattern loptopUseTime) {
        this.loptopUseTime = loptopUseTime;
    }

    public SleepPattern getMobileUsage() {
        return mobileUsage;
    }

    public void setMobileUsage(SleepPattern mobileUsage) {
        this.mobileUsage = mobileUsage;
    }

    public LifeStyleType getType() {
        return type;
    }

    public void setType(LifeStyleType type) {
        this.type = type;
    }
}
