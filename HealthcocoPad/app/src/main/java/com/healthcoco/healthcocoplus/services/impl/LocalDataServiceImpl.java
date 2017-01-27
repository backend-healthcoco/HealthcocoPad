package com.healthcoco.healthcocoplus.services.impl;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoApplication;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.BloodGroup;
import com.healthcoco.healthcocoplus.bean.server.CalendarEvents;
import com.healthcoco.healthcocoplus.bean.server.CityResponse;
import com.healthcoco.healthcocoplus.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocoplus.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.Complaint;
import com.healthcoco.healthcocoplus.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocoplus.bean.server.Diagnoses;
import com.healthcoco.healthcocoplus.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocoplus.bean.server.Disease;
import com.healthcoco.healthcocoplus.bean.server.DoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.Drug;
import com.healthcoco.healthcocoplus.bean.server.DrugDirection;
import com.healthcoco.healthcocoplus.bean.server.DrugDosage;
import com.healthcoco.healthcocoplus.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocoplus.bean.server.DrugType;
import com.healthcoco.healthcocoplus.bean.server.Hospital;
import com.healthcoco.healthcocoplus.bean.server.Investigation;
import com.healthcoco.healthcocoplus.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.Observation;
import com.healthcoco.healthcocoplus.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocoplus.bean.server.Patient;
import com.healthcoco.healthcocoplus.bean.server.Profession;
import com.healthcoco.healthcocoplus.bean.server.Records;
import com.healthcoco.healthcocoplus.bean.server.Reference;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.bean.server.Specialities;
import com.healthcoco.healthcocoplus.bean.server.TempTemplate;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.bean.server.UserGroups;
import com.healthcoco.healthcocoplus.enums.BooleanTypeValues;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.LocalTabelType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.utilities.DateTimeUtil;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LocalDatabaseUtils;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.StringUtil;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 23-01-2017.
 */
@SuppressWarnings("unchecked")

public class LocalDataServiceImpl {
    private static final String TAG = LocalDataServiceImpl.class.getSimpleName();
    private static LocalDataServiceImpl mInstance;
    private static HealthCocoApplication mApp;

    private LocalDataServiceImpl() {
    }

    public static LocalDataServiceImpl getInstance(HealthCocoApplication application) {
        if (mInstance == null) {
            mInstance = new LocalDataServiceImpl();
            mApp = application;
        }
        Util.checkNetworkStatus(mApp.getApplicationContext());
        return mInstance;
    }

    /**
     * Delete all records
     */
    private void clearDoctor() {
        LoginResponse.deleteAll(LoginResponse.class);
    }

    public void addDoctor(LoginResponse doctor) {
        if (doctor.getUser() != null) {
            // setting user
            if (!Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                doctor.setForeignUserId(doctor.getUser().getUniqueId());
            }
            clearUser();
            doctor.getUser().save();
        }
        clearDoctor();
        doctor.save();
    }

    private void clearUser() {
        //TODO clear rest of linked tables remaining
        User.deleteAll(User.class);
    }

    public void addCities(List<CityResponse> citiesResponse) {
        CityResponse.saveInTx(citiesResponse);
    }

    public void addProfessionsList(List<Profession> list) {
        Profession.saveInTx(list);
    }

    public void addReferenceList(List<Reference> list) {
        Reference.saveInTx(list);
    }

    public void addBloodGroups(List<BloodGroup> list) {
        BloodGroup.saveInTx(list);
    }

    public void addCalendarEventsList(ArrayList<CalendarEvents> list) {
        if (!Util.isNullOrEmptyList(list))
            for (CalendarEvents calendarEvents :
                    list) {
//                addCalendarEventsUpdated(calendarEvents);
            }
    }

    public long getLatestUpdatedTime(LocalTabelType localTabelType) {
        Long latestUpdatedTime = 0l;
        switch (localTabelType) {
            case CITIES:
                List<CityResponse> tempCitiesList = CityResponse.find(CityResponse.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(tempCitiesList))
                    latestUpdatedTime = tempCitiesList.get(0).getUpdatedTime();
                break;
        }
        if (latestUpdatedTime == null)
            latestUpdatedTime = 0l;
        LogUtils.LOGD(TAG, "Latest Updated Time for " + localTabelType + " : " + DateTimeUtil.getFormatedDateAndTime(latestUpdatedTime) + " , " + latestUpdatedTime);
        return latestUpdatedTime;
    }

    public void addSpecialities(ArrayList<Specialities> specialitiesResponse) {
        Specialities.saveInTx(specialitiesResponse);
    }

    public VolleyResponseBean getSpecialitiesListVolleyResponse(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            List<Specialities> list = getSpecialitiesListObject();
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public List<Specialities> getSpecialitiesListObject() {
        return Specialities.listAll(Specialities.class);
    }

    private ArrayList<Object> getObjectsListFromMap(List<?> list) {
        if (list != null) {
            ArrayList<Object> listNew = new ArrayList<Object>();
            listNew.addAll(list);
            return listNew;
        }
        return null;
    }

    private void showErrorLocal(VolleyResponseBean volleyResponseBean, GsonRequest.ErrorListener errorListener) {
        if (errorListener != null)
            errorListener.onErrorResponse(volleyResponseBean, mApp.getResources().getString(R.string
                    .error_local_data));
        else
            volleyResponseBean.setErrMsg(mApp.getResources().getString(R.string
                    .error_local_data));
    }

    public LoginResponse getDoctor() {
        LoginResponse doctor = LoginResponse.first(LoginResponse.class);

        if (doctor != null && !Util.isNullOrBlank(doctor.getForeignUserId())) {
            doctor.setUser(getUser(doctor.getForeignUserId()));

            if (doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                doctor.setHospitals(getHospitals(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctor.getUser().getUniqueId()));
            }
        }
        return doctor;
    }

    private List<Hospital> getHospitals(String key, String uniqueId) {
        List<Hospital> hospitalsList = (List<Hospital>) getListBySelectQuery(Hospital.class, key, uniqueId);
        if (!Util.isNullOrEmptyList(hospitalsList)) {
            for (Hospital hospital : hospitalsList) {
//                hospital.setLocationsAndAccessControl(getLocationAndAccessControl(LocalDatabaseUtils.KEY_FOREIGN_HOSPITAL_ID, hospital.getUniqueId()));
            }
        }
        return hospitalsList;
    }

    private List<?> getListBySelectQuery(Class<?> class1, String key, String value) {
        Select<?> selectQuery = Select.from(class1).where(Condition.prop(key).eq(value));
        return selectQuery.list();
    }

    public User getUser(String uniqueId) {
        User user = Select.from(User.class).where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(uniqueId))
                .first();
        if (user != null) {
//            if (user.getDob() != null) {
//            }
        }
        return user;
    }

    public void getDosageDurationDirectionList(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            switch (webServiceType) {
                case GET_DRUG_DOSAGE:
                    volleyResponseBean.setDataList(getObjectsListFromMap(DrugDosage.listAll(DrugDosage.class)));
                    break;
                case GET_DURATION_UNIT:
                    volleyResponseBean.setDataList(getObjectsListFromMap(DrugDurationUnit.listAll(DrugDurationUnit.class)));
                    break;
                case GET_DIRECTION:
                    volleyResponseBean.setDataList(getObjectsListFromMap(DrugDirection.listAll(DrugDirection.class)));
                    break;
            }
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
    }

    public ArrayList<Object> getSuggestionsList(Class<?> class1) {
        return getObjectsListFromMap(Select.from(class1).list());
    }

    public void addDrugDosageList(ArrayList<DrugDosage> dosageList) {
        DrugDosage.saveInTx(dosageList);
    }

    public void addDrugDosage(DrugDosage drugDosage) {
        drugDosage.save();
    }

    public void addDurationUnitList(ArrayList<DrugDurationUnit> durationList) {
        DrugDurationUnit.saveInTx(durationList);
    }

    public void addDurationUnit(DrugDurationUnit durationUnit) {
        durationUnit.save();
    }

    public void addDirectionsList(ArrayList<DrugDirection> directionsList) {
        DrugDirection.saveInTx(directionsList);
    }

    public void addDrugType(ArrayList<DrugType> drugTypeList) {
        DrugType.saveInTx(drugTypeList);
    }

    public void addDirection(DrugDirection drugDirection) {
        drugDirection.save();
    }

    public void addUserGroupsList(List<UserGroups> groupsList) {
        SQLiteDatabase sqLiteDatabase = SugarRecord.getSugarDataBase();
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.setLockingEnabled(false);
            for (UserGroups userGroup : groupsList) {
                userGroup.save();
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(null, "Error in saving in transaction " + e.getMessage());
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.setLockingEnabled(true);
        }
    }

//    public void addDoctorProfile(DoctorProfile doctorProfile) {
//        // setting DOB
//        if (doctorProfile.getDob() != null) {
//            doctorProfile.getDob().setForeignUniqueId(doctorProfile.getDoctorId());
//            doctorProfile.getDob().save();
//        }
//
//        deleteAdditionalNumbersIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
//        //saving additionalNumbers
//        if (!Util.isNullOrEmptyList(doctorProfile.getAdditionalNumbers())) {
//            addAdditionalNumbers(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getAdditionalNumbers());
//        }
//
//        deleteOtherEmailAddressesIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
//        //saving EmailAddresses
//        if (!Util.isNullOrEmptyList(doctorProfile.getOtherEmailAddresses())) {
//            addOtherEmailAddressesList(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getOtherEmailAddresses());
//        }
//
//        //saving doctor Experience
//        if (doctorProfile.getExperience() != null) {
//            doctorProfile.getExperience().setForeignUniqueId(doctorProfile.getDoctorId());
//            doctorProfile.getExperience().save();
//        }
//        //saving Education Details
//        addEducationsList(doctorProfile.getDoctorId(), doctorProfile.getEducation());
//
//        //saving Specialities
//        addSpecialities(doctorProfile.getDoctorId(), doctorProfile.getSpecialities());
//
//        deleteAchievementsIfAlreadyExists(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
//        //saving Achievement Details
//        if (!Util.isNullOrEmptyList(doctorProfile.getAchievements())) {
//            addAchievements(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getAchievements());
//        }
//        //saving Registration Details
//        addRegistrationDetailsList(doctorProfile.getDoctorId(), doctorProfile.getRegistrationDetails());
//
//        deleteExperienceDetailIfAlreadyExists(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
//        //saving Experience Details
//        if (!Util.isNullOrEmptyList(doctorProfile.getExperienceDetails())) {
//            addExperienceDetailsList(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getExperienceDetails());
//        }
//        deleteProfessionalMembershipsIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
//        //saving Professional Memberships
//        if (!Util.isNullOrEmptyList(doctorProfile.getProfessionalMemberships())) {
//            addProfessionalMemberships(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getProfessionalMemberships());
//        }
//
//
//        //deleteClinicProfile
//        deleteDoctorClinicProfile(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
//        //saving Clinic Profile
//        if (!Util.isNullOrEmptyList(doctorProfile.getClinicProfile())) {
//            for (DoctorClinicProfile clinicProfile :
//                    doctorProfile.getClinicProfile()) {
//                addDoctorClinicProfile(doctorProfile.getDoctorId(), clinicProfile);
//            }
//        }
//
//        doctorProfile.save();
//    }

    //    public void addDoctorClinicProfile(String doctorId, DoctorClinicProfile clinicProfile) {
//        clinicProfile.setForeignUniqueId(doctorId);
//        deleteAppointmentBookingNumbers(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicProfile.getUniqueId());
//        //saving appointmentBookingNumber
//        if (!Util.isNullOrEmptyList(clinicProfile.getAppointmentBookingNumber())) {
//            for (String string :
//                    clinicProfile.getAppointmentBookingNumber()) {
//                ForeignAppointmentBookingNumber appointmentBookingNumber = new ForeignAppointmentBookingNumber();
//                appointmentBookingNumber.setForeignUniqueId(clinicProfile.getUniqueId());
//                appointmentBookingNumber.setAppintmentBookingNumber(string);
//                appointmentBookingNumber.setCustomUniqueId(appointmentBookingNumber.getForeignUniqueId() + appointmentBookingNumber.getAppintmentBookingNumber());
//                appointmentBookingNumber.save();
//            }
//        }
//
//        //saving consultation fee
//        if (clinicProfile.getConsultationFee() != null) {
//            clinicProfile.getConsultationFee().setForeignUniqueId(clinicProfile.getUniqueId());
//            clinicProfile.getConsultationFee().save();
//        }
//
//        //saving AppointmentSlot
//        if (clinicProfile.getAppointmentSlot() != null) {
//            clinicProfile.getAppointmentSlot().setForeignUniqueId(clinicProfile.getUniqueId());
//            clinicProfile.getAppointmentSlot().save();
//        }
//
////                deleteWorkingSchedulesIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getLocationId());
//        deleteAllWorkingSchedules(clinicProfile.getLocationId(), doctorId, DoctorWorkingSchedule.class);
//        //saving working schedules
//        if (!Util.isNullOrEmptyList(clinicProfile.getWorkingSchedules())) {
//            addDoctorWorkingSchedules(doctorId, LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getLocationId(), clinicProfile.getWorkingSchedules());
//        }
//        deleteClinicImages(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getUniqueId());
//        //saving clinic Images
//        if (!Util.isNullOrEmptyList(clinicProfile.getImages())) {
//            addClinicImages(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getUniqueId(), clinicProfile.getImages());
//        }
//        clinicProfile.save();
//    }
    public VolleyResponseBean getClinicDetailsResponse(WebServiceType webServiceType, String locationId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        volleyResponseBean.setDataFromLocal(true);
        try {
            volleyResponseBean.setData(getClinicResponseDetails(locationId));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    private ClinicDetailResponse getClinicResponseDetails(String locationId) {
        Select<ClinicDetailResponse> selectQuery = Select.from(ClinicDetailResponse.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(locationId));
        ClinicDetailResponse clinicDetailResponse = selectQuery.first();
        if (clinicDetailResponse != null) {
//            clinicDetailResponse.setDoctors(getClinicDoctorsList(ClinicalProfileFragment.MAX_DOCTORS_LIST_COUNT, locationId));
//            clinicDetailResponse.setLocation(getLocation(locationId));
        }
        return clinicDetailResponse;
    }

    public VolleyResponseBean getDrugTypeListAdResponse(WebServiceType webServiceType, String doctorId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            volleyResponseBean.setDataList(getObjectsListFromMap(DrugType.listAll(DrugType.class)));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public VolleyResponseBean getUserGroups(WebServiceType webServiceType, ArrayList<String> patientsAssignedGroupIdList, String doctorId, String locationID, String hospitalId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            List<UserGroups> savedGroupsList = Select.from(UserGroups.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId),
                            Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(locationID),
                            Condition.prop(LocalDatabaseUtils.KEY_HOSPITAL_ID).eq(hospitalId),
                            Condition.prop(LocalDatabaseUtils.KEY_DISCARDED).eq(LocalDatabaseUtils.BOOLEAN_FALSE_VALUE)).list();
            if (!Util.isNullOrEmptyList(patientsAssignedGroupIdList)) {
                for (UserGroups group :
                        savedGroupsList) {
                    if (patientsAssignedGroupIdList.contains(group.getUniqueId()))
                        group.setForeignIsAssignedAnyPatient(true);
                }
            }
            volleyResponseBean.setDataList(getObjectsListFromMap(savedGroupsList));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public VolleyResponseBean getProfessionList(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            List<Profession> list = Profession.listAll(Profession.class);
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public VolleyResponseBean getBloodGroup(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            List<BloodGroup> list = BloodGroup.listAll(BloodGroup.class);
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public VolleyResponseBean getDiseaseList(WebServiceType webServiceType, String doctorId, BooleanTypeValues discarded, ArrayList<String> diseaseIds, long updatedTime, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            String whereCondition = "";
            Class<?> class1 = Disease.class;
            switch (discarded) {
                case TRUE:
                    volleyResponseBean.setLocalBackgroundTaskType(LocalBackgroundTaskType.GET_DISEASE_HIDDEN_LIST);
                    whereCondition = "Select * from " + StringUtil.toSQLName(class1.getSimpleName()) + " where "
                            + LocalDatabaseUtils.KEY_DISCARDED + "=" + discarded.getBooleanIntValue();
                    break;
                case FALSE:
                    volleyResponseBean.setLocalBackgroundTaskType(LocalBackgroundTaskType.GET_DISEASE_ACTIVATED_LIST);
                    whereCondition = "Select * from " + StringUtil.toSQLName(class1.getSimpleName()) + " where "
                            + "(" + LocalDatabaseUtils.KEY_DISCARDED + " is null"
                            + " OR "
                            + LocalDatabaseUtils.KEY_DISCARDED + "=" + discarded.getBooleanIntValue()
                            + ")";
                    break;
            }
            whereCondition = whereCondition
                    + " AND " + LocalDatabaseUtils.KEY_UPDATED_TIME + ">=" + updatedTime;
            if (!Util.isNullOrBlank(doctorId)) {
                whereCondition = whereCondition
                        + " AND " + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"" + doctorId + "\"";
            }
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
//            List<?> list = null;
//            list = SugarRecord.findWithQuery(class1, whereCondition);
            List<?> savedDiseaseList = SugarRecord.findWithQuery(class1, whereCondition);
//            if (discardedValue != null) {
//                savedDiseaseList = Select.from(Disease.class)
//                        .where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId),
//                                Condition.prop(LocalDatabaseUtils.KEY_DISCARDED).eq(discardedValue),
//                                Condition.prop(LocalDatabaseUtils.KEY_UPDATED_TIME).gt(updatedTime)).list();
//            } else {
//                savedDiseaseList = Select.from(Disease.class)
//                        .where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId),
//                                Condition.prop(LocalDatabaseUtils.KEY_UPDATED_TIME).gt(updatedTime)).list();
//            }
//            if (!Util.isNullOrEmptyList(savedDiseaseList) && !Util.isNullOrEmptyList(diseaseIds)) {
//                for (Disease disease :
//                        savedDiseaseList) {
//                    if (!Util.isNullOrEmptyList(diseaseIds) && diseaseIds.contains(disease.getUniqueId()))
//                        disease.setIsAssigned(true);
//                }
//            }
            volleyResponseBean.setDataList(getObjectsListFromMap(savedDiseaseList));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

//    public VolleyResponseBean getTemplatesListPageWise(WebServiceType webServiceType, String doctorId,
//                                                       boolean discarded, Long updatedTime, int pageNum,
//                                                       int maxSize, String searchTerm, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
//        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
//        volleyResponseBean.setWebServiceType(webServiceType);
//        volleyResponseBean.setDataFromLocal(true);
//        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
//        try {
//            int discardedValue = LocalDatabaseUtils.BOOLEAN_FALSE_VALUE;
//            if (discarded)
//                discardedValue = LocalDatabaseUtils.BOOLEAN_TRUE_VALUE;
//            //forming where condition query
//            String whereCondition = "Select * from " + StringUtil.toSQLName(TempTemplate.class.getSimpleName())
//                    + " where "
//                    + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"" + doctorId + "\""
//                    + " AND "
//                    + LocalDatabaseUtils.KEY_DISCARDED + "=" + discardedValue;
//            if (!Util.isNullOrBlank(searchTerm))
//                whereCondition = whereCondition
//                        + " AND " + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_NAME, searchTerm);
//
//            //specifying order by limit and offset query
//            String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_CREATED_TIME + " DESC "
//                    + " LIMIT " + maxSize
//                    + " OFFSET " + (pageNum * maxSize);
//
//            whereCondition = whereCondition + conditionsLimit;
//            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
//            List<TempTemplate> list = SugarRecord.findWithQuery(TempTemplate.class, whereCondition);
//            if (!Util.isNullOrEmptyList(list))
//                for (TempTemplate template : list) {
//                    template.setItems(getDrugItemsList(LocalDatabaseUtils.KEY_FOREIGN_TEMPLATE_ID, template.getUniqueId()));
//                }
//            volleyResponseBean.setDataList(getObjectsListFromMap(list));
//            if (responseListener != null)
//                responseListener.onResponse(volleyResponseBean);
//        } catch (Exception e) {
//            e.printStackTrace();
//            showErrorLocal(volleyResponseBean, errorListener);
//        }
//        return volleyResponseBean;
//    }


    public Long getLatestUpdatedTime(User user, LocalTabelType localTabelType) {
        Long latestUpdatedTime = 0l;
        String doctorId = "";
        String locationId = "";
        String hospitalId = "";
        if (user != null) {
            doctorId = user.getUniqueId();
            locationId = user.getForeignLocationId();
            hospitalId = user.getForeignHospitalId();
            switch (localTabelType) {
                case USER_GROUP:
                    List<UserGroups> tempGroupsList = UserGroups.find(UserGroups.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempGroupsList))
                        latestUpdatedTime = tempGroupsList.get(0).getUpdatedTime();
                    break;
                case DISEASE:
                    List<Disease> tempDiseaseList = Disease.find(Disease.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempDiseaseList))
                        latestUpdatedTime = tempDiseaseList.get(0).getUpdatedTime();
                    break;
                case TEMP_DRUG:
                    List<Drug> drugsList = Drug.find(Drug.class, LocalDatabaseUtils.IS_DRUG_FROM_GET_DRUGS_LIST + "= ?", new String[]{"" + LocalDatabaseUtils.BOOLEAN_TRUE_VALUE}, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(drugsList))
                        latestUpdatedTime = drugsList.get(0).getUpdatedTime();
                    break;
                case RECORDS:
                    List<Records> tempRecordsList = Records.find(Records.class,
                            LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_PATIENT_ID + "= ?",
                            new String[]{doctorId, "" + HealthCocoConstants.SELECTED_PATIENTS_USER_ID},
                            null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempRecordsList))
                        latestUpdatedTime = tempRecordsList.get(0).getUpdatedTime();
                    break;
                case TEMPLATE:
                    List<TempTemplate> tempTemplatesList = TempTemplate.find(TempTemplate.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempTemplatesList))
                        latestUpdatedTime = tempTemplatesList.get(0).getUpdatedTime();
                    break;
//                case TEMP_PRESCRIPTION:
//                    List<Prescription> prescriptionList = Prescription.find(Prescription.class,
//                            LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_PATIENT_ID + "= ?",
//                            new String[]{doctorId, "" + HealthCocoConstants.SELECTED_PATIENTS_USER_ID},
//                            null, "updated_time DESC", "1");
//                    if (!Util.isNullOrEmptyList(prescriptionList))
//                        latestUpdatedTime = prescriptionList.get(0).getUpdatedTime();
//                    break;
//                case CLINAL_NOTE:
//                    List<ClinicalNotes> tempClinicalNotes = ClinicalNotes.find(ClinicalNotes.class,
//                            LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_PATIENT_ID + "= ?",
//                            new String[]{doctorId, "" + HealthCocoConstants.SELECTED_PATIENTS_USER_ID},
//                            null, "updated_time DESC", "1");
//                    if (!Util.isNullOrEmptyList(tempClinicalNotes))
//                        latestUpdatedTime = tempClinicalNotes.get(0).getUpdatedTime();
//                    break;
//                case APPOINTMENT:
//                    List<CalendarEvents> tempAppointmentsList = CalendarEvents.find(CalendarEvents.class,
//                            LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_PATIENT_ID + "= ? AND " + LocalDatabaseUtils.KEY_IS_ADDED_ON_SUCCESS + "= ?",
//                            new String[]{doctorId, "" + HealthCocoConstants.SELECTED_PATIENTS_USER_ID, "" + BooleanTypeValues.FALSE.getBooleanIntValue()},
//                            null, "updated_time DESC", "1");
//                    if (!Util.isNullOrEmptyList(tempAppointmentsList))
//                        latestUpdatedTime = tempAppointmentsList.get(0).getUpdatedTime();
//                    break;
                case REGISTERED_PATIENTS_DETAILS:
                    List<RegisteredPatientDetailsUpdated> tempRegisteredPatientDetailUpdateds = null;
                    if (!Util.isNullOrBlank(doctorId) && !Util.isNullOrBlank(hospitalId) && !Util.isNullOrBlank(locationId)) {
                        tempRegisteredPatientDetailUpdateds = RegisteredPatientDetailsUpdated.find(RegisteredPatientDetailsUpdated.class, LocalDatabaseUtils.KEY_LOCATION_ID + "= ? AND " + LocalDatabaseUtils.KEY_HOSPITAL_ID + "= ?",
                                new String[]{locationId, hospitalId},
                                null, "updated_time DESC", "1");
                    } else
                        tempRegisteredPatientDetailUpdateds = RegisteredPatientDetailsUpdated.find(RegisteredPatientDetailsUpdated.class, null, null,
                                null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempRegisteredPatientDetailUpdateds))
                        latestUpdatedTime = tempRegisteredPatientDetailUpdateds.get(0).getUpdatedTime();
                    break;
                case COMPLAINT:
                    List<Complaint> tempComplaintsLists = Complaint.find(Complaint.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempComplaintsLists))
                        latestUpdatedTime = tempComplaintsLists.get(0).getUpdatedTime();
                    break;
                case OBSERVATION:
                    List<Observation> tempObservationList = Observation.find(Observation.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempObservationList))
                        latestUpdatedTime = tempObservationList.get(0).getUpdatedTime();
                    break;
                case INVESTIGATION:
                    List<Investigation> tempInvestigationsList = Investigation.find(Investigation.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempInvestigationsList))
                        latestUpdatedTime = tempInvestigationsList.get(0).getUpdatedTime();
                    break;
                case DIAGNOSIS:
                    List<Diagnoses> tempDiagnosisList = Diagnoses.find(Diagnoses.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempDiagnosisList))
                        latestUpdatedTime = tempDiagnosisList.get(0).getUpdatedTime();
                    break;
                case COMPLAINT_SUGGESTIONS:
                    List<ComplaintSuggestions> tempComplaintSuggestionsLists = ComplaintSuggestions.find(ComplaintSuggestions.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempComplaintSuggestionsLists))
                        latestUpdatedTime = tempComplaintSuggestionsLists.get(0).getUpdatedTime();
                    break;
                case OBSERVATION_SUGGESTIONS:
                    List<ObservationSuggestions> tempObservationSuggestionsList = ObservationSuggestions.find(ObservationSuggestions.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempObservationSuggestionsList))
                        latestUpdatedTime = tempObservationSuggestionsList.get(0).getUpdatedTime();
                    break;
                case INVESTIGATION_SUGGESTIONS:
                    List<InvestigationSuggestions> tempInvestigationSuggestionsList = InvestigationSuggestions.find(InvestigationSuggestions.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempInvestigationSuggestionsList))
                        latestUpdatedTime = tempInvestigationSuggestionsList.get(0).getUpdatedTime();
                    break;
                case DIAGNOSIS_SUGGESTIONS:
                    List<DiagnosisSuggestions> tempDiagnosisSuggestionsList = DiagnosisSuggestions.find(DiagnosisSuggestions.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(tempDiagnosisSuggestionsList))
                        latestUpdatedTime = tempDiagnosisSuggestionsList.get(0).getUpdatedTime();
                    break;
//                case HISTORY_DETAIL_RESPONSE:
//                    List<HistoryDetailsResponse> tempHistoryList = HistoryDetailsResponse.find(HistoryDetailsResponse.class,
//                            LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_PATIENT_ID + "= ?",
//                            new String[]{doctorId, "" + HealthCocoConstants.SELECTED_PATIENTS_USER_ID},
//                            null, "updated_time DESC", "1");
//                    if (!Util.isNullOrEmptyList(tempHistoryList))
//                        latestUpdatedTime = tempHistoryList.get(0).getUpdatedTime();
//                    break;
//                case VISIT_DETAILS:
//                    List<VisitDetails> visitDetailsList = VisitDetails.find(VisitDetails.class,
//                            LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_PATIENT_ID + "= ?",
//                            new String[]{doctorId, "" + HealthCocoConstants.SELECTED_PATIENTS_USER_ID},
//                            null, "updated_time DESC", "1");
//                    if (!Util.isNullOrEmptyList(visitDetailsList))
//                        latestUpdatedTime = visitDetailsList.get(0).getUpdatedTime();
//                    break;
                case DRUG_DOSAGE:
                    List<DrugDosage> doasageList = DrugDosage.find(DrugDosage.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(doasageList))
                        latestUpdatedTime = doasageList.get(0).getUpdatedTime();
                    break;
                case DRUG_DURATION_UNIT:
                    List<DrugDurationUnit> durationList = DrugDurationUnit.find(DrugDurationUnit.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(durationList))
                        latestUpdatedTime = durationList.get(0).getUpdatedTime();
                    break;
                case DRUG_DIRECTION:
                    List<DrugDirection> directionList = DrugDirection.find(DrugDirection.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(directionList))
                        latestUpdatedTime = directionList.get(0).getUpdatedTime();
                    break;
                case DRUG_TYPE:
                    List<DrugType> strengthUnitList = DrugType.find(DrugType.class, null, null, null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(strengthUnitList))
                        latestUpdatedTime = strengthUnitList.get(0).getUpdatedTime();
                    break;
                case REFERENCE:
                    List<Reference> referenceList = Reference.find(Reference.class,
                            LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_IS_FROM_CONTACTS_LIST + "= ?",
                            new String[]{doctorId, "" + LocalDatabaseUtils.BOOLEAN_FALSE_VALUE},
                            null, "updated_time DESC", "1");
                    if (!Util.isNullOrEmptyList(referenceList))
                        latestUpdatedTime = referenceList.get(0).getUpdatedTime();
                    break;
                case PROFESSION:
                    break;
//                case CALENDAR_EVENTS:
//                    List<CalendarEvents> tempCalendarEventsList = CalendarEvents.find(CalendarEvents.class,
//                            LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_IS_FROM_CALENDAR_API + "= ? AND " + LocalDatabaseUtils.KEY_IS_ADDED_ON_SUCCESS + "= ?",
//                            new String[]{doctorId, "" + BooleanTypeValues.TRUE.getBooleanIntValue(), "" + BooleanTypeValues.FALSE.getBooleanIntValue()},
//                            null, "updated_time DESC", "1");
//                    if (!Util.isNullOrEmptyList(tempCalendarEventsList))
//                        latestUpdatedTime = tempCalendarEventsList.get(0).getUpdatedTime();
//                    break;
            }
        }
        if (latestUpdatedTime == null)
            latestUpdatedTime = 0l;
        LogUtils.LOGD(TAG, "Latest Updated Time for " + localTabelType + " : " + DateTimeUtil.getFormatedDateAndTime(latestUpdatedTime) + " , " + latestUpdatedTime);
        return latestUpdatedTime;
    }

    public void addDoctorProfile(DoctorProfile doctorProfile) {
        // setting DOB
        if (doctorProfile.getDob() != null) {
            doctorProfile.getDob().setForeignUniqueId(doctorProfile.getDoctorId());
            doctorProfile.getDob().save();
        }
        doctorProfile.save();
    }

    public void addPatientsList(ArrayList<RegisteredPatientDetailsUpdated> patientsList) {
        for (RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated : patientsList) {
            addPatient(registeredPatientDetailsUpdated);
        }
    }

    public void addPatient(RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        // setting patient
        if (registeredPatientDetailsUpdated.getPatient() != null
                && !Util.isNullOrBlank(registeredPatientDetailsUpdated.getPatient().getPatientId())) {
            Patient patient = registeredPatientDetailsUpdated.getPatient();
            // setting DOB with patientId
            if (registeredPatientDetailsUpdated.getDob() != null) {
                registeredPatientDetailsUpdated.getDob().setForeignUniqueId(patient.getPatientId());
                registeredPatientDetailsUpdated.getDob().save();
            }
            // setting patient's relations
//            addRelations(patient.getRelations(), patient.getPatientId());

//            addNotes(patient.getNotes(), patient.getPatientId());
            registeredPatientDetailsUpdated.setForeignPatientId(registeredPatientDetailsUpdated.getPatient().getPatientId());
            registeredPatientDetailsUpdated.getPatient().save();
        }
        // setting address
        if (registeredPatientDetailsUpdated.getAddress() != null) {
            registeredPatientDetailsUpdated.getAddress().setUserId(registeredPatientDetailsUpdated.getUserId());
            registeredPatientDetailsUpdated.getAddress().save();
        }
//        deletePatientIdGroupIdsList(registeredPatientDetailsUpdated.getUserId());
//        if (!Util.isNullOrEmptyList(registeredPatientDetailsUpdated.getGroups())) {
//            addPatientIdGroupIdsList(registeredPatientDetailsUpdated, registeredPatientDetailsUpdated.getGroups());
//        }
//        deleteReferredByIfAlreadyPresent(LocalDatabaseUtils.KEY_UNIQUE_ID, LocalDatabaseUtils.KEY_IS_FROM_CONTACTS_LIST, registeredPatientDetailsUpdated.getForeignReferredById(), "" + LocalDatabaseUtils.BOOLEAN_TRUE_VALUE);
        if (registeredPatientDetailsUpdated.getReferredBy() != null) {
            registeredPatientDetailsUpdated.getReferredBy().setIsFromContactsList(true);
            registeredPatientDetailsUpdated.setForeignReferredById(registeredPatientDetailsUpdated.getReferredBy().getUniqueId());
            registeredPatientDetailsUpdated.getReferredBy().save();
        }

//        formOtpVerificationAndAdd(registeredPatientDetailsUpdated);
        registeredPatientDetailsUpdated.setUniqueId(registeredPatientDetailsUpdated.getLocationId() + registeredPatientDetailsUpdated.getUserId());
        registeredPatientDetailsUpdated.save();
    }

    public void addSuggestionsList(WebServiceType webServiceType, LocalTabelType localTabelType, ArrayList<?> list, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            switch (localTabelType) {
                case COMPLAINT_SUGGESTIONS:
                    ArrayList<ComplaintSuggestions> complaintSuggestionsList = (ArrayList<ComplaintSuggestions>) (ArrayList<?>) list;
                    ComplaintSuggestions.saveInTx(complaintSuggestionsList);
                    break;
                case OBSERVATION_SUGGESTIONS:
                    ArrayList<ObservationSuggestions> observationSuggestionsList = (ArrayList<ObservationSuggestions>) (ArrayList<?>) list;
                    ObservationSuggestions.saveInTx(observationSuggestionsList);
                    break;
                case INVESTIGATION_SUGGESTIONS:
                    ArrayList<InvestigationSuggestions> investigationSuggestionsList = (ArrayList<InvestigationSuggestions>) (ArrayList<?>) list;
                    InvestigationSuggestions.saveInTx(investigationSuggestionsList);
                    break;
                case DIAGNOSIS_SUGGESTIONS:
                    ArrayList<DiagnosisSuggestions> diagnosisSuggestionsList = (ArrayList<DiagnosisSuggestions>) (ArrayList<?>) list;
                    DiagnosisSuggestions.saveInTx(diagnosisSuggestionsList);
                    break;
            }
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
    }

    public void addClinicDetailResponse(ClinicDetailResponse clinicDetailResponse) {
        if (clinicDetailResponse.getLocation() != null)
            addLocation(clinicDetailResponse.getLocation());

        deleteAllFrom(ClinicDoctorProfile.class, LocalDatabaseUtils.KEY_LOCATION_ID, clinicDetailResponse.getUniqueId());
//        adding doctors of this clinic
        if (!Util.isNullOrEmptyList(clinicDetailResponse.getDoctors())) {
            for (ClinicDoctorProfile clinicDoctorProfile :
                    clinicDetailResponse.getDoctors()) {
                clinicDoctorProfile.setLocationId(clinicDetailResponse.getUniqueId());
                addClinicDocProfile(clinicDoctorProfile);
            }
        }
        clinicDetailResponse.save();
    }

    public void addClinicDocProfile(ClinicDoctorProfile clinicDoctorProfile) {
        //saving Specialities
        addSpecialities(clinicDoctorProfile.getUniqueId(), clinicDoctorProfile.getSpecialities());
        clinicDoctorProfile.save();
    }

    private void addSpecialities(String value, List<String> list) {
//        deleteSpecialitiesIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, value);
//        //saving Specialities
//        if (!Util.isNullOrEmptyList(list)) {
//            for (String string :
//                    list) {
//                ForeignSpecialities specialities = new ForeignSpecialities();
//                specialities.setForeignUniqueId(value);
//                specialities.setSpecialities(string);
//                specialities.save();
//            }
//        }
    }

    private void deleteAllFrom(Class<?> class1, String key, String value) {
        SugarRecord.deleteAll(class1, key + "= ?", value);
    }

    private void deleteAdditionalNumbersIfAlreadyPresent(String key, String value) {
//        ForieignAdditionalNumbers.deleteAll(ForieignAdditionalNumbers.class, key + "= ?", value);
    }

    public void addLocation(Location location) {

//        deleteAdditionalNumbersIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, location.getUniqueId());
        //saving additionalNumbers/alernateNumbers
//        if (!Util.isNullOrEmptyList(location.getAlternateClinicNumbers())) {
//            addAdditionalNumbers(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, location.getUniqueId(), location.getAlternateClinicNumbers());
//        }
//        //delete all clinic images first
//        deleteClinicImages(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, location.getUniqueId());
//        if (!Util.isNullOrEmptyList(location.getImages()))
//            addClinicImages(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, location.getUniqueId(), location.getImages());
//
//        deleteAllWorkingSchedules(location.getUniqueId(), "", ClinicWorkingSchedule.class);
//        if (!Util.isNullOrEmptyList(location.getClinicWorkingSchedules()))
//            addClinicWorkingSchedules(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, location.getUniqueId(), location.getClinicWorkingSchedules());
//
//        location.save();
    }
}