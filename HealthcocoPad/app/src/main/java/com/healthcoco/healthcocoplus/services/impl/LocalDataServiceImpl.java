package com.healthcoco.healthcocoplus.services.impl;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoApplication;
import com.healthcoco.healthcocoplus.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocoplus.bean.server.AssignedGroupsTable;
import com.healthcoco.healthcocoplus.bean.server.NotesTable;
import com.healthcoco.healthcocoplus.bean.server.PatientIdGroupId;
import com.healthcoco.healthcocoplus.bean.server.Relations;
import com.healthcoco.healthcocoplus.bean.UIPermissions;
import com.healthcoco.healthcocoplus.bean.UserPermissionsResponse;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.AccessModule;
import com.healthcoco.healthcocoplus.bean.server.Achievement;
import com.healthcoco.healthcocoplus.bean.server.Address;
import com.healthcoco.healthcocoplus.bean.server.AllUIPermission;
import com.healthcoco.healthcocoplus.bean.server.AppointmentSlot;
import com.healthcoco.healthcocoplus.bean.server.BloodGroup;
import com.healthcoco.healthcocoplus.bean.server.CalendarEvents;
import com.healthcoco.healthcocoplus.bean.server.CityResponse;
import com.healthcoco.healthcocoplus.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocoplus.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.ClinicImage;
import com.healthcoco.healthcocoplus.bean.server.ClinicWorkingSchedule;
import com.healthcoco.healthcocoplus.bean.server.Complaint;
import com.healthcoco.healthcocoplus.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocoplus.bean.server.ConsultationFee;
import com.healthcoco.healthcocoplus.bean.server.DOB;
import com.healthcoco.healthcocoplus.bean.server.Diagnoses;
import com.healthcoco.healthcocoplus.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocoplus.bean.server.Diagram;
import com.healthcoco.healthcocoplus.bean.server.Disease;
import com.healthcoco.healthcocoplus.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocoplus.bean.server.DoctorExperience;
import com.healthcoco.healthcocoplus.bean.server.DoctorExperienceDetail;
import com.healthcoco.healthcocoplus.bean.server.DoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.DoctorRegistrationDetail;
import com.healthcoco.healthcocoplus.bean.server.DoctorWorkingSchedule;
import com.healthcoco.healthcocoplus.bean.server.Drug;
import com.healthcoco.healthcocoplus.bean.server.DrugDirection;
import com.healthcoco.healthcocoplus.bean.server.DrugDosage;
import com.healthcoco.healthcocoplus.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocoplus.bean.server.DrugType;
import com.healthcoco.healthcocoplus.bean.server.Education;
import com.healthcoco.healthcocoplus.bean.server.ForeignAppointmentBookingNumber;
import com.healthcoco.healthcocoplus.bean.server.ForeignOtherEmailAddresses;
import com.healthcoco.healthcocoplus.bean.server.ForeignProfessionalMemberships;
import com.healthcoco.healthcocoplus.bean.server.ForeignSpecialities;
import com.healthcoco.healthcocoplus.bean.server.ForieignAdditionalNumbers;
import com.healthcoco.healthcocoplus.bean.server.Hospital;
import com.healthcoco.healthcocoplus.bean.server.Investigation;
import com.healthcoco.healthcocoplus.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocoplus.bean.server.Location;
import com.healthcoco.healthcocoplus.bean.server.LocationAndAccessControl;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.Observation;
import com.healthcoco.healthcocoplus.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocoplus.bean.server.Patient;
import com.healthcoco.healthcocoplus.bean.server.Profession;
import com.healthcoco.healthcocoplus.bean.server.Records;
import com.healthcoco.healthcocoplus.bean.server.Reference;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.bean.server.Role;
import com.healthcoco.healthcocoplus.bean.server.Specialities;
import com.healthcoco.healthcocoplus.bean.server.TempTemplate;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.bean.server.UserGroups;
import com.healthcoco.healthcocoplus.bean.server.WorkingHours;
import com.healthcoco.healthcocoplus.bean.server.WorkingSchedule;
import com.healthcoco.healthcocoplus.dialogFragment.ComparatorUtil;
import com.healthcoco.healthcocoplus.enums.BooleanTypeValues;
import com.healthcoco.healthcocoplus.enums.FilterItemType;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.LocalTabelType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.enums.WeekDayNameType;
import com.healthcoco.healthcocoplus.fragments.ClinicalProfileFragment;
import com.healthcoco.healthcocoplus.fragments.MenuDrawerFragment;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.utilities.DateTimeUtil;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LocalDatabaseUtils;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.ReflectionUtil;
import com.healthcoco.healthcocoplus.utilities.StringUtil;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Collections;
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
            // setting DOB
            if (doctor.getUser().getDob() != null) {
                doctor.getUser().getDob().setForeignUniqueId(doctor.getUser().getUniqueId());
                deleteDOBRecordIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctor.getUser().getUniqueId());
                doctor.getUser().getDob().save();
            }

            if (!Util.isNullOrEmptyList(doctor.getHospitals())) {
                if (Util.isNullOrBlank(doctor.getUser().getForeignHospitalId()) && Util.isNullOrBlank(doctor.getUser().getForeignLocationId())) {
                    Hospital defaultHospital = doctor.getHospitals().get(0);
                    //setting foreignHospital id for user

                    if (Util.isNullOrBlank(doctor.getUser().getForeignLocationId()) || Util.isNullOrBlank(doctor.getUser().getForeignLocationId())) {
                        doctor.getUser().setForeignHospitalId(defaultHospital.getUniqueId());
                        doctor.getUser().setForeignLocationId(defaultHospital.getLocationsAndAccessControl().get(0).getUniqueId());
                    }
                }
                deleteAllFrom(Hospital.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctor.getForeignUserId());
                for (Hospital hospital : doctor.getHospitals()) {
                    hospital.setForeignUniqueId(doctor.getForeignUserId());
                    // setting locations
                    if (!Util.isNullOrEmptyList(hospital.getLocationsAndAccessControl())) {
                        for (LocationAndAccessControl locationAndAccessControl : hospital.getLocationsAndAccessControl()) {
                            //saving location
//                            Location location = locationAndAccessControl.getLocation();
//                            if (location != null) {
                            locationAndAccessControl.setDoctorId(doctor.getUser().getUniqueId());
                            locationAndAccessControl.setForeignHospitalId(hospital.getUniqueId());
                            if (!Util.isNullOrEmptyList(locationAndAccessControl.getImages()))
                                addClinicImages(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, locationAndAccessControl.getUniqueId(), locationAndAccessControl.getImages());

                            deleteAllWorkingSchedules(locationAndAccessControl.getUniqueId(), doctor.getUser().getUniqueId(), DoctorWorkingSchedule.class);
                            if (!Util.isNullOrEmptyList(locationAndAccessControl.getWorkingSchedules())) {
                                locationAndAccessControl.setDoctorId(doctor.getUser().getUniqueId());
                                addDoctorWorkingSchedules(doctor.getUser().getUniqueId(), LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, locationAndAccessControl.getUniqueId(), locationAndAccessControl.getWorkingSchedules());
                            }
                            if (!Util.isNullOrEmptyList(locationAndAccessControl.getRoles())) {
                                for (Role role :
                                        locationAndAccessControl.getRoles()) {
                                    if (!Util.isNullOrEmptyList(role.getAccessModules())) {
                                        for (AccessModule accessModule : role.getAccessModules()) {
                                            if (!Util.isNullOrEmptyList(accessModule.getAccessPermissionTypes()))
                                                accessModule.setStringAccessPermissionTypes(new Gson().toJson(accessModule.getAccessPermissionTypes()));
                                            accessModule.setForeignRoleId(role.getUniqueId());
                                            accessModule.save();
                                        }
                                    }
                                    role.save();
                                }
                            }
                            locationAndAccessControl.save();
                        }
                    }
                    hospital.save();
                }
            }
            clearUser();
            doctor.getUser().save();
        }
        clearDoctor();
        doctor.save();
    }

    private void addDoctorWorkingSchedules(String doctorId, String key, String value, List<DoctorWorkingSchedule> workingSchedulesList) {
        try {
            for (DoctorWorkingSchedule workingSchedule :
                    workingSchedulesList) {
                workingSchedule.setDoctorId(doctorId);
                if (key.equals(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID))
                    workingSchedule.setForeignLocationId(value);
                workingSchedule.setCustomUniqueId(workingSchedule.getForeignLocationId() + workingSchedule.getWorkingDay() + workingSchedule.getDoctorId());
//delete all working schedules and hours first
//                deleteDoctorWorkingSchedules(workingSchedule.getDoctorId(), workingSchedule.getCustomUniqueId());
//                deleteWorkingHoursIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, workingSchedule.getCustomUniqueId());
                if (!Util.isNullOrEmptyList(workingSchedule.getWorkingHours()))
                    addWorkingHoursList(DoctorWorkingSchedule.class.getSimpleName(), workingSchedule.getCustomUniqueId(), workingSchedule.getWorkingHours());

//                addWorkingHoursList(DoctorWorkingSchedule.class.getSimpleName(), LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, workingSchedule.getCustomUniqueId(), workingSchedule.getWorkingHours());
                workingSchedule.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteDOBRecordIfAlreadyPresent(String key, String value) {
        DOB.deleteAll(DOB.class, key + "= ?", value);
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
            clinicDetailResponse.setDoctors(getClinicDoctorsList(ClinicalProfileFragment.MAX_DOCTORS_LIST_COUNT, locationId));
            clinicDetailResponse.setLocation(getLocation(locationId));
        }
        return clinicDetailResponse;
    }

    public List<ClinicWorkingSchedule> getClinicWorkingSchedules(String key, String value) {
        List<ClinicWorkingSchedule> list = Select.from(ClinicWorkingSchedule.class)
                .where(Condition.prop(key).eq(value)).list();
        if (!Util.isNullOrEmptyList(list)) {
            for (ClinicWorkingSchedule workingSchedule :
                    list) {
                workingSchedule.setWorkingHours((List<WorkingHours>) getListByKeyValue(WorkingHours.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, workingSchedule.getCustomUniqueId()));
            }
        }
        return list;
    }

    public List<ClinicDoctorProfile> getClinicDoctorsList(int maxSize, String locationId) {
        String whereCondition = "Select * from " + StringUtil.toSQLName(ClinicDoctorProfile.class.getSimpleName())
                + " where "
                + LocalDatabaseUtils.KEY_LOCATION_ID + "=\"" + locationId + "\"";

        //specifying order by limit and offset query
        String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_CREATED_TIME + " DESC ";

        if (maxSize > 0)
            conditionsLimit = conditionsLimit + " LIMIT " + maxSize;

        whereCondition = whereCondition + conditionsLimit;
        LogUtils.LOGD(TAG, "Select Query " + whereCondition);
        List<ClinicDoctorProfile> clinicDoctorProfilesList = SugarRecord.findWithQuery(ClinicDoctorProfile.class, whereCondition);
        if (!Util.isNullOrEmptyList(clinicDoctorProfilesList)) {
            for (ClinicDoctorProfile clinicDoctorProfile :
                    clinicDoctorProfilesList) {
                clinicDoctorProfile.setSpecialities(getSpecialities(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicDoctorProfile.getUniqueId()));
            }
        }
        return clinicDoctorProfilesList;
    }

    public Location getLocation(String locationId) {
        Select<Location> selectQuery = Select.from(Location.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(locationId));
        Location location = selectQuery.first();
        if (location != null) {
            location.setAlternateClinicNumbers(getAdditionalNumbersList(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, location.getUniqueId()));
            location.setImages((List<ClinicImage>) getListByKeyValue(ClinicImage.class, LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, location.getUniqueId()));
            location.setClinicWorkingSchedules(getClinicWorkingSchedules(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, location.getUniqueId()));
        }
        return location;
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
            volleyResponseBean.setDataList(getObjectsListFromMap(savedDiseaseList));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

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

        deleteAdditionalNumbersIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
        //saving additionalNumbers
        if (!Util.isNullOrEmptyList(doctorProfile.getAdditionalNumbers())) {
            addAdditionalNumbers(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getAdditionalNumbers());
        }

        deleteOtherEmailAddressesIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
        //saving EmailAddresses
        if (!Util.isNullOrEmptyList(doctorProfile.getOtherEmailAddresses())) {
            addOtherEmailAddressesList(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getOtherEmailAddresses());
        }

        //saving doctor Experience
        if (doctorProfile.getExperience() != null) {
            doctorProfile.getExperience().setForeignUniqueId(doctorProfile.getDoctorId());
            doctorProfile.getExperience().save();
        }
        //saving Education Details
        addEducationsList(doctorProfile.getDoctorId(), doctorProfile.getEducation());

        //saving Specialities
        addSpecialities(doctorProfile.getDoctorId(), doctorProfile.getSpecialities());

        deleteAchievementsIfAlreadyExists(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
        //saving Achievement Details
        if (!Util.isNullOrEmptyList(doctorProfile.getAchievements())) {
            addAchievements(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getAchievements());
        }
        //saving Registration Details
        addRegistrationDetailsList(doctorProfile.getDoctorId(), doctorProfile.getRegistrationDetails());

        deleteExperienceDetailIfAlreadyExists(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
        //saving Experience Details
        if (!Util.isNullOrEmptyList(doctorProfile.getExperienceDetails())) {
            addExperienceDetailsList(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getExperienceDetails());
        }
        deleteProfessionalMembershipsIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
        //saving Professional Memberships
        if (!Util.isNullOrEmptyList(doctorProfile.getProfessionalMemberships())) {
            addProfessionalMemberships(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getProfessionalMemberships());
        }

        deleteProfessionalStatementIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
        //saving Professional Statement
        if (!Util.isNullOrBlank(doctorProfile.getProfessionalStatement())) {
            addProfessionalStatements(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId(), doctorProfile.getProfessionalStatement());
        }

        //deleteClinicProfile
        deleteDoctorClinicProfile(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId());
        //saving Clinic Profile
        if (!Util.isNullOrEmptyList(doctorProfile.getClinicProfile())) {
            for (DoctorClinicProfile clinicProfile :
                    doctorProfile.getClinicProfile()) {
                addDoctorClinicProfile(doctorProfile.getDoctorId(), clinicProfile);
            }
        }
        doctorProfile.save();
    }

    private void addProfessionalStatements(String keyForeignUniqueId, String doctorId, String professionalStatement) {
        deleteProfessionalStatementIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorId);
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setProfessionalStatement(professionalStatement);
    }

    private void deleteProfessionalStatementIfAlreadyPresent(String keyForeignUniqueId, String doctorId) {

    }

    public void addEducationsList(String doctorId, List<Education> list) {
//        addEducationsList(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorId, list);
        deleteEducationsIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorId);
        //saving Education Details
        if (!Util.isNullOrEmptyList(list)) {
            for (Education education :
                    list) {
                education.setForeignUniqueId(doctorId);
            }
            Education.saveInTx(list);
        }
    }

    public void addAchievements(String key, String value, List<Achievement> list) {
        for (Achievement achievement :
                list) {
            if (key.equals(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID))
                achievement.setForeignUniqueId(value);
        }
        Achievement.saveInTx(list);
    }

    public void addAchievements(String value, List<Achievement> list) {
        deleteAchievementsIfAlreadyExists(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, value);
        //saving Registration Details
        if (!Util.isNullOrEmptyList(list)) {
            for (Achievement registrationDetail : list) {
                registrationDetail.setForeignUniqueId(value);
            }
            DoctorRegistrationDetail.saveInTx(list);
        }
    }

    public void addRegistrationDetailsList(String value, List<DoctorRegistrationDetail> list) {
        deleteDoctorRegistrationDetailIfAlreadyExists(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, value);
        //saving Registration Details
        if (!Util.isNullOrEmptyList(list)) {
            for (DoctorRegistrationDetail registrationDetail :
                    list) {
                registrationDetail.setForeignUniqueId(value);
            }
            DoctorRegistrationDetail.saveInTx(list);
        }
    }

    private void addProfessionalMemberships(String key, String value, List<String> list) {
        for (String string :
                list) {
            ForeignProfessionalMemberships professionalMemberships = new ForeignProfessionalMemberships();
            professionalMemberships.setForeignUniqueId(value);
            professionalMemberships.setProfessionalMemberships(string);
            professionalMemberships.save();
        }
    }

    public void addDoctorClinicProfile(String doctorId, DoctorClinicProfile clinicProfile) {
        clinicProfile.setForeignUniqueId(doctorId);
        deleteAppointmentBookingNumbers(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicProfile.getUniqueId());
        //saving appointmentBookingNumber
        if (!Util.isNullOrEmptyList(clinicProfile.getAppointmentBookingNumber())) {
            for (String string :
                    clinicProfile.getAppointmentBookingNumber()) {
                ForeignAppointmentBookingNumber appointmentBookingNumber = new ForeignAppointmentBookingNumber();
                appointmentBookingNumber.setForeignUniqueId(clinicProfile.getUniqueId());
                appointmentBookingNumber.setAppintmentBookingNumber(string);
                appointmentBookingNumber.setCustomUniqueId(appointmentBookingNumber.getForeignUniqueId() + appointmentBookingNumber.getAppintmentBookingNumber());
                appointmentBookingNumber.save();
            }
        }

        //saving consultation fee
        if (clinicProfile.getConsultationFee() != null) {
            clinicProfile.getConsultationFee().setForeignUniqueId(clinicProfile.getUniqueId());
            clinicProfile.getConsultationFee().save();
        }

        //saving reconsultation fee
        if (clinicProfile.getRevisitConsultationFee() != null) {
            clinicProfile.getRevisitConsultationFee().setForeignUniqueId(clinicProfile.getUniqueId());
            clinicProfile.getRevisitConsultationFee().save();
        }

        //saving AppointmentSlot
        if (clinicProfile.getAppointmentSlot() != null) {
            clinicProfile.getAppointmentSlot().setForeignUniqueId(clinicProfile.getUniqueId());
            clinicProfile.getAppointmentSlot().save();
        }

//                deleteWorkingSchedulesIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getLocationId());
        deleteAllWorkingSchedules(clinicProfile.getLocationId(), doctorId, DoctorWorkingSchedule.class);
        //saving working schedules
        if (!Util.isNullOrEmptyList(clinicProfile.getWorkingSchedules())) {
            addDoctorWorkingSchedules(doctorId, LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getLocationId(), clinicProfile.getWorkingSchedules());
        }
        deleteClinicImages(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getUniqueId());
        //saving clinic Images
        if (!Util.isNullOrEmptyList(clinicProfile.getImages())) {
            addClinicImages(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getUniqueId(), clinicProfile.getImages());
        }
        clinicProfile.save();
    }

    private void addExperienceDetailsList(String key, String value, List<DoctorExperienceDetail> list) {
        for (DoctorExperienceDetail experienceDetail :
                list) {
            if (key.equals(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID))
                experienceDetail.setForeignUniqueId(value);
        }
        DoctorExperienceDetail.saveInTx(list);
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
        deleteSpecialitiesIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, value);
        //saving Specialities
        if (!Util.isNullOrEmptyList(list)) {
            for (String string :
                    list) {
                ForeignSpecialities specialities = new ForeignSpecialities();
                specialities.setForeignUniqueId(value);
                specialities.setSpecialities(string);
                specialities.save();
            }
        }
    }

    private void deleteAllFrom(Class<?> class1, String key, String value) {
        SugarRecord.deleteAll(class1, key + "= ?", value);
    }

    private void deleteAdditionalNumbersIfAlreadyPresent(String key, String value) {
        ForieignAdditionalNumbers.deleteAll(ForieignAdditionalNumbers.class, key + "= ?", value);
    }

    public void addLocation(Location location) {

        deleteAdditionalNumbersIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, location.getUniqueId());
//        saving additionalNumbers/alernateNumbers
        if (!Util.isNullOrEmptyList(location.getAlternateClinicNumbers())) {
            addAdditionalNumbers(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, location.getUniqueId(), location.getAlternateClinicNumbers());
        }
        //delete all clinic images first
        deleteClinicImages(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, location.getUniqueId());
        if (!Util.isNullOrEmptyList(location.getImages()))
            addClinicImages(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, location.getUniqueId(), location.getImages());

        deleteAllWorkingSchedules(location.getUniqueId(), "", ClinicWorkingSchedule.class);
        if (!Util.isNullOrEmptyList(location.getClinicWorkingSchedules()))
            addClinicWorkingSchedules(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, location.getUniqueId(), location.getClinicWorkingSchedules());

        location.save();
    }

    private void addClinicImages(String key, String value, List<ClinicImage> list) {
        for (ClinicImage image :
                list) {
            if (key.equals(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID))
                image.setForeignLocationId(value);
            image.setCustomUniqueId(image.getForeignLocationId() + image.getImageUrl());
            image.save();
        }
    }

    private void deleteClinicImages(String key, String value) {
        List<ClinicImage> list = (List<ClinicImage>) getListByKeyValue(ClinicImage.class, key, value);
        ClinicImage.deleteInTx(list);
    }

    private void addAdditionalNumbers(String key, String value, List<String> list) {
        for (String number :
                list) {
            if (!Util.isNullOrBlank(number)) {
                ForieignAdditionalNumbers additionalNumbers = new ForieignAdditionalNumbers();
                additionalNumbers.setForeignUniqueId(value);
                additionalNumbers.setAdditionalNumber(number);
                additionalNumbers.setCustomUniqueId(additionalNumbers.getForeignUniqueId() + additionalNumbers.getAdditionalNumber());
                additionalNumbers.save();
            }
        }
    }

    private void addOtherEmailAddressesList(String key, String value, List<String> list) {
        for (String string :
                list) {
            ForeignOtherEmailAddresses otherEmailAddresses = new ForeignOtherEmailAddresses();
            otherEmailAddresses.setForeignUniqueId(value);
            otherEmailAddresses.setOtherEmailAddress(string);
            otherEmailAddresses.save();
        }
    }

    public VolleyResponseBean getDoctorProfileResponse(WebServiceType webServiceType, String doctorId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            DoctorProfile doctorProfile = getDoctorProfileObject(doctorId);
            volleyResponseBean.setData(doctorProfile);
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public DoctorProfile getDoctorProfileObject(String doctorId) {
        DoctorProfile doctorProfile = Select.from(DoctorProfile.class).where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId)).first();
        if (doctorProfile != null) {
            doctorProfile.setDob((DOB) getObject(DOB.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId()));
            doctorProfile.setAdditionalNumbers(getAdditionalNumbersList(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId()));
            doctorProfile.setOtherEmailAddresses(getOtherEmailAddresses(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId()));
            doctorProfile.setExperience((DoctorExperience) getObject(DoctorExperience.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId()));
            doctorProfile.setEducation((List<Education>) getListByKeyValue(Education.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId()));
            doctorProfile.setSpecialities(getSpecialities(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId()));
            doctorProfile.setAchievements((List<Achievement>) getListByKeyValue(Achievement.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId()));
            doctorProfile.setRegistrationDetails((List<DoctorRegistrationDetail>) getListByKeyValue(DoctorRegistrationDetail.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId()));
            doctorProfile.setExperienceDetails((List<DoctorExperienceDetail>) getListByKeyValue(DoctorExperienceDetail.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId()));
            doctorProfile.setProfessionalMemberships(getProfessionalMemberships(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorProfile.getDoctorId()));
            doctorProfile.setClinicProfile(getDoctorClinicProfile(doctorProfile.getDoctorId()));
        }
        return doctorProfile;
    }

    private Object getObject(Class<?> class1, String key, String value) {
        return Select.from(class1).where(Condition.prop(key).eq(value)).first();
    }

    public ArrayList<String> getAdditionalNumbersList(String key, String value) {
        ArrayList<ForieignAdditionalNumbers> additionalNumbersList = (ArrayList<ForieignAdditionalNumbers>) getListByKeyValue(ForieignAdditionalNumbers.class, key, value);
        if (!Util.isNullOrEmptyList(additionalNumbersList)) {
            ArrayList<String> numbersList = new ArrayList<>();
            for (ForieignAdditionalNumbers additionalNumbers :
                    additionalNumbersList) {
                numbersList.add(additionalNumbers.getAdditionalNumber());
            }
            return numbersList;
        }
        return null;
    }

    public List<String> getOtherEmailAddresses(String key, String value) {
        List<ForeignOtherEmailAddresses> emailAddressesList = (List<ForeignOtherEmailAddresses>) getListByKeyValue(ForeignOtherEmailAddresses.class, key, value);
        if (!Util.isNullOrEmptyList(emailAddressesList)) {
            List<String> stringList = new ArrayList<>();
            for (ForeignOtherEmailAddresses emailAddress :
                    emailAddressesList) {
                stringList.add(emailAddress.getOtherEmailAddress());
            }
            return stringList;
        }
        return null;
    }

    public List<String> getSpecialities(String key, String value) {
        List<ForeignSpecialities> specialitiesList = (List<ForeignSpecialities>) getListByKeyValue(ForeignSpecialities.class, key, value);
        if (!Util.isNullOrEmptyList(specialitiesList)) {
            List<String> stringList = new ArrayList<>();
            for (ForeignSpecialities specialities :
                    specialitiesList) {
                stringList.add(specialities.getSpecialities());
            }
            return stringList;
        }
        return null;
    }

    public List<String> getProfessionalMemberships(String key, String value) {
        List<ForeignProfessionalMemberships> professionalMembershipList = (List<ForeignProfessionalMemberships>) getListByKeyValue(ForeignProfessionalMemberships.class, key, value);
        if (!Util.isNullOrEmptyList(professionalMembershipList)) {
            List<String> stringList = new ArrayList<>();
            for (ForeignProfessionalMemberships professionalMembership :
                    professionalMembershipList) {
                stringList.add(professionalMembership.getProfessionalMemberships());
            }
            return stringList;
        }
        return null;
    }

    public List<DoctorClinicProfile> getDoctorClinicProfile(String doctorId) {
        List<DoctorClinicProfile> list = Select.from(DoctorClinicProfile.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID).eq(doctorId)).list();
        if (!Util.isNullOrEmptyList(list)) {
            for (DoctorClinicProfile clinicProfile :
                    list) {
                clinicProfile.setAppointmentBookingNumber(getAppointmentBookingNumber(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicProfile.getUniqueId()));
                clinicProfile.setConsultationFee((ConsultationFee) getObject(ConsultationFee.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicProfile.getUniqueId()));
                clinicProfile.setRevisitConsultationFee((ConsultationFee) getObject(ConsultationFee.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicProfile.getUniqueId()));
                clinicProfile.setAppointmentSlot(getAppointmentSlot(clinicProfile.getUniqueId()));
                clinicProfile.setWorkingSchedules(getWorkingSchedulesForDoctor(doctorId, clinicProfile.getLocationId()));
                clinicProfile.setImages((List<ClinicImage>) getListByKeyValue(ClinicImage.class, LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getUniqueId()));
            }
        }
        return list;
    }

    public DoctorClinicProfile getDoctorClinicProfile(String doctorId, String locationId) {
        DoctorClinicProfile clinicProfile = Select.from(DoctorClinicProfile.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID).eq(doctorId),
                        Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(locationId)).first();
        if (clinicProfile != null) {
            clinicProfile.setAppointmentBookingNumber(getAppointmentBookingNumber(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicProfile.getUniqueId()));
            clinicProfile.setConsultationFee((ConsultationFee) getObject(ConsultationFee.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicProfile.getUniqueId()));
            clinicProfile.setRevisitConsultationFee((ConsultationFee) getObject(ConsultationFee.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicProfile.getUniqueId()));
            clinicProfile.setAppointmentSlot(getAppointmentSlot(clinicProfile.getUniqueId()));
            clinicProfile.setWorkingSchedules(getWorkingSchedulesForDoctor(doctorId, clinicProfile.getLocationId()));
            clinicProfile.setImages((List<ClinicImage>) getListByKeyValue(ClinicImage.class, LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getUniqueId()));
        }
        return clinicProfile;
    }

    private AppointmentSlot getAppointmentSlot(String uniqueId) {
        return (AppointmentSlot) getObject(AppointmentSlot.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, uniqueId);
    }

    public List<DoctorWorkingSchedule> getWorkingSchedulesForDoctor(String doctorId, String locationId) {
        if (!Util.isNullOrBlank(doctorId)) {
            List<DoctorWorkingSchedule> list = Select.from(DoctorWorkingSchedule.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId),
                            Condition.prop(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID).eq(locationId)).list();
            if (!Util.isNullOrEmptyList(list)) {
                for (DoctorWorkingSchedule workingSchedule :
                        list) {
                    workingSchedule.setWorkingHours((List<WorkingHours>) getListByKeyValue(WorkingHours.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, workingSchedule.getCustomUniqueId()));
                }
            }
            return list;
        }
        return null;
    }

    public List<String> getAppointmentBookingNumber(String key, String value) {
        List<ForeignAppointmentBookingNumber> appointmentBookingNumbersList = (List<ForeignAppointmentBookingNumber>) getListByKeyValue(ForeignAppointmentBookingNumber.class, key, value);
        if (!Util.isNullOrEmptyList(appointmentBookingNumbersList)) {
            List<String> stringList = new ArrayList<>();
            for (ForeignAppointmentBookingNumber bookingNumber :
                    appointmentBookingNumbersList) {
                stringList.add(bookingNumber.getAppintmentBookingNumber());
            }
            return stringList;
        }
        return null;
    }

    private List<?> getListByKeyValue(Class<?> class1, String key, String value) {
        return Select.from(class1).where(Condition.prop(key).eq(value)).list();
    }

    private void deleteOtherEmailAddressesIfAlreadyPresent(String key, String value) {
        ForeignOtherEmailAddresses.deleteAll(ForeignOtherEmailAddresses.class, key + "= ?", value);
    }

    private void deleteEducationsIfAlreadyPresent(String key, String value) {
        Education.deleteAll(Education.class, key + "= ?", value);
    }

    private void deleteSpecialitiesIfAlreadyPresent(String key, String value) {
        ForeignSpecialities.deleteAll(ForeignSpecialities.class, key + "= ?", value);
    }

    private void deleteProfessionalMembershipsIfAlreadyPresent(String key, String value) {
        ForeignProfessionalMemberships.deleteAll(ForeignProfessionalMemberships.class, key + "= ?", value);
    }

    private void deleteAchievementsIfAlreadyExists(String key, String value) {
        Achievement.deleteAll(Achievement.class, key + "= ?", value);
    }

    private void deleteDoctorRegistrationDetailIfAlreadyExists(String key, String value) {
        DoctorRegistrationDetail.deleteAll(DoctorRegistrationDetail.class, key + "= ?", value);
    }

    private void deleteExperienceDetailIfAlreadyExists(String key, String value) {
        DoctorExperienceDetail.deleteAll(DoctorExperienceDetail.class, key + "= ?", value);
    }

    private void deleteDoctorClinicProfile(String key, String value) {
        DoctorClinicProfile.deleteAll(DoctorClinicProfile.class, key + "= ?", value);
    }

    private void deleteAppointmentBookingNumbers(String key, String value) {
        ForeignAppointmentBookingNumber.deleteAll(ForeignAppointmentBookingNumber.class, key + "= ?", value);
    }

    private void deleteWorkingSchedulesIfAlreadyPresent(String key, String value) {
        WorkingSchedule.deleteAll(WorkingSchedule.class, key + "= ?", value);
    }

    private void deleteWorkingHoursIfAlreadyPresent(String key, String value) {
        WorkingHours.deleteAll(WorkingHours.class, key + "= ?", value);
    }

    private void deleteReferredByIfAlreadyPresent(String key1, String key2, String value1, String value2) {
        Reference.deleteAll(Reference.class, key1 + "= ? AND " + key2 + "= ? ", value1, value2);
    }

    /**
     * @param locationId : id of clinic
     * @param doctorId   : accepts blank doctor Id for general clinic schedules
     *                   and accepts valud doctorId for doctor's clinic schedules
     * @param class1
     */
    private void deleteAllWorkingSchedules(String locationId, String doctorId, Class<?> class1) {
        for (WeekDayNameType weekDayName :
                WeekDayNameType.values()) {
            String customUniqueId = locationId + weekDayName + doctorId;
            if (class1 == ClinicWorkingSchedule.class)
                deleteClinicWorkingSchedules(LocalDatabaseUtils.KEY_CUSTOM_UNIQUE_ID, customUniqueId);
            else if (class1 == DoctorWorkingSchedule.class)
                deleteDoctorWorkingSchedules(doctorId, locationId);
//            deleteWorkingHoursIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, customUniqueId);

        }
    }

    private void deleteClinicWorkingSchedules(String key, String value) {
        List<ClinicWorkingSchedule> listSchedule = getClinicWorkingSchedules(key, value);
        if (!Util.isNullOrEmptyList(listSchedule)) {
            for (ClinicWorkingSchedule workingSchedule :
                    listSchedule) {
                List<WorkingHours> listHours = (List<WorkingHours>) getListByKeyValue(WorkingHours.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, workingSchedule.getCustomUniqueId());
                if (!Util.isNullOrEmptyList(listHours)) {
                    WorkingHours.deleteInTx(listHours);
                }
            }
            ClinicWorkingSchedule.deleteInTx(listSchedule);
        }
    }

    private void deleteDoctorWorkingSchedules(String doctorId, String locationId) {
        List<DoctorWorkingSchedule> listSchedule = getWorkingSchedulesForDoctor(doctorId, locationId);
        if (!Util.isNullOrEmptyList(listSchedule)) {
            for (DoctorWorkingSchedule workingSchedule :
                    listSchedule) {
                List<WorkingHours> listHours = (List<WorkingHours>) getListByKeyValue(WorkingHours.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, workingSchedule.getCustomUniqueId());
                if (!Util.isNullOrEmptyList(listHours)) {
                    WorkingHours.deleteInTx(listHours);
                }
            }
            DoctorWorkingSchedule.deleteInTx(listSchedule);
        }
    }

    private void addClinicWorkingSchedules(String key, String value, List<ClinicWorkingSchedule> workingSchedulesList) {
        try {
            for (ClinicWorkingSchedule workingSchedule :
                    workingSchedulesList) {
                if (key.equals(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID))
                    workingSchedule.setForeignLocationId(value);
                workingSchedule.setCustomUniqueId(workingSchedule.getForeignLocationId() + workingSchedule.getWorkingDay());
//delete all working schedules and hours first
                if (!Util.isNullOrEmptyList(workingSchedule.getWorkingHours()))
                    addWorkingHoursList(ClinicWorkingSchedule.class.getSimpleName(), workingSchedule.getCustomUniqueId(), workingSchedule.getWorkingHours());
                workingSchedule.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addWorkingHoursList(String className, String customUniqueId, List<WorkingHours> list) {
        for (WorkingHours workingHour :
                list) {
            addWorkingHour(className, customUniqueId, workingHour);
        }
    }

    private void addWorkingHour(String className, String foreignTableId, WorkingHours workingHour) {
        String customUniqueId = className + foreignTableId + workingHour.getFromTime() + workingHour.getToTime();
        workingHour.setCustomUniqueId(customUniqueId);
        workingHour.setForeignTableId(foreignTableId);
        workingHour.save();
    }

    public void updatedSelectedLocationDetails(String doctorId, DoctorClinicProfile clinicProfile) {
        String whereCondition = "UPDATE " + StringUtil.toSQLName(User.class.getSimpleName())
                + " SET " + LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID + "=\"" + clinicProfile.getLocationId() + "\","
                + LocalDatabaseUtils.KEY_FOREIGN_HOSPITAL_ID + "=\"" + clinicProfile.getHospitalId() + "\""
                + " where "
                + LocalDatabaseUtils.KEY_UNIQUE_ID + "=\"" + doctorId + "\"";

        LogUtils.LOGD(TAG, "Select Query " + whereCondition);
        SugarRecord.executeQuery(whereCondition);
    }

    /**
     * clears all the saved data if there is any change in speciality of doctor
     */
    public void clearMasterDataOnSpecialityChange() {
        ComplaintSuggestions.deleteAll(ComplaintSuggestions.class);
        ObservationSuggestions.deleteAll(ObservationSuggestions.class);
        InvestigationSuggestions.deleteAll(InvestigationSuggestions.class);
        DiagnosisSuggestions.deleteAll(DiagnosisSuggestions.class);
        Diagram.deleteAll(Diagram.class);
    }

    public List<Education> getEducationDetailsList(String doctorId) {
        try {
            List<Education> list = (List<Education>) getListByKeyValue(Education.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorId);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Achievement> getAwardsPublicationDetailsList(String doctorId) {
        try {
            List<Achievement> list = (List<Achievement>) getListByKeyValue(Achievement.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorId);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DoctorRegistrationDetail> getRegistrationDetailsList(String doctorId) {
        try {
            List<DoctorRegistrationDetail> list = (List<DoctorRegistrationDetail>) getListByKeyValue(DoctorRegistrationDetail.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorId);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DoctorExperienceDetail> getExperiencenDetailsList(String doctorId) {
        try {
            List<DoctorExperienceDetail> list = (List<DoctorExperienceDetail>) getListByKeyValue(DoctorExperienceDetail.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorId);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addExperirnce(String doctorId, List<DoctorExperienceDetail> list) {
        deleteExperienceDetailIfAlreadyExists(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorId);
        //saving Education Details
        if (!Util.isNullOrEmptyList(list)) {
            for (DoctorExperienceDetail experienceDetail :
                    list) {
                experienceDetail.setForeignUniqueId(doctorId);
            }
            DoctorExperienceDetail.saveInTx(list);
        }
    }

    public List<String> getProfessionalMembershipsDetailsList(String doctorId) {
        try {
            List<String> membershipsStringList = null;
            List<ForeignProfessionalMemberships> professionMembershipsList = (List<ForeignProfessionalMemberships>) getListByKeyValue(ForeignProfessionalMemberships.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorId);
            if (!Util.isNullOrEmptyList(professionMembershipsList)) {
                for (ForeignProfessionalMemberships professionalMembership :
                        professionMembershipsList) {
                    if (membershipsStringList == null)
                        membershipsStringList = new ArrayList<>();
                    membershipsStringList.add(professionalMembership.getProfessionalMemberships());
                }
            }
            return membershipsStringList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addProfessionalMembership(String doctorId, List<String> list) {
        deleteProfessionalMembershipsIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, doctorId);
        //saving Education Details
        for (String value : list) {
            if (!Util.isNullOrBlank(value)) {
                ForeignProfessionalMemberships professionalMemberships = new ForeignProfessionalMemberships();
                professionalMemberships.setForeignUniqueId(value);
                professionalMemberships.setProfessionalMemberships(value);
                professionalMemberships.save();
            }
        }
    }

    public String getProfessionalStatement(String doctorId) {
        try {
            String list = (String) getObject(DoctorProfile.class, LocalDatabaseUtils.KEY_DOCTOR_ID, doctorId);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ClinicImage getClinicImage(String uniqueId) {
        return (ClinicImage) getObject(ClinicImage.class, LocalDatabaseUtils.KEY_CUSTOM_UNIQUE_ID, uniqueId);
    }

    public List<Role> getRoles(String locationId, String hospitalId) {
        List<Role> rolesList = Select.from(Role.class).where(Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(locationId),
                Condition.prop(LocalDatabaseUtils.KEY_HOSPITAL_ID).eq(hospitalId)).list();
        if (!Util.isNullOrEmptyList(rolesList)) {
            for (Role role :
                    rolesList) {
                getRestRoleDetails(role);
            }
        }
        return rolesList;
    }

    private void getRestRoleDetails(Role role) {
        role.setAccessModules((List<AccessModule>) getListByKeyValue(AccessModule.class, LocalDatabaseUtils.KEY_FOREIGN_ROLE_ID, role.getUniqueId()));
        if (!Util.isNullOrEmptyList(role.getAccessModules())) {
            for (AccessModule accessModule :
                    role.getAccessModules()) {
                if (!Util.isNullOrBlank(accessModule.getStringAccessPermissionTypes()))
                    accessModule.setAccessPermissionTypes(new Gson().fromJson(accessModule.getStringAccessPermissionTypes(), ArrayList.class));
            }
        }
    }

    public VolleyResponseBean getCitiesList(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsFromLocalAfterApiSuccess(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            List<CityResponse> list = CityResponse.listAll(CityResponse.class);
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public void addUserUiPermissions(UserPermissionsResponse userPermissionsResponse) {
        if (userPermissionsResponse.getUiPermissions() != null) {
            UIPermissions uiPermissions = userPermissionsResponse.getUiPermissions();
            Gson gson = new Gson();
            uiPermissions.setClinicalNotesPermissionsString(gson.toJson(uiPermissions.getClinicalNotesPermissions()));
            uiPermissions.setPatientVisitPermissionsString(gson.toJson(uiPermissions.getPatientVisitPermissions()));
            uiPermissions.setTabPermissionsString(gson.toJson(uiPermissions.getTabPermissions()));
            uiPermissions.setPrescriptionPermissionsString(gson.toJson(uiPermissions.getPrescriptionPermissions()));
            uiPermissions.setProfilePermissionsString(gson.toJson(uiPermissions.getPrescriptionPermissions()));
            uiPermissions.setDoctorId(userPermissionsResponse.getDoctorId());
            uiPermissions.save();
        }
        userPermissionsResponse.save();
    }

    public UserPermissionsResponse getUserPermissions(String doctorId) {
        UserPermissionsResponse userPermissionsResponse = Select.from(UserPermissionsResponse.class).where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId)).first();
        return userPermissionsResponse;
    }

    public AllUIPermission getAllUIPermissions() {
        AllUIPermission allUIPermission = Select.from(AllUIPermission.class).first();
        return allUIPermission;
    }

    public void addALLUiPermissions(AllUIPermission allUIPermission) {
        AllUIPermission allUIPermission1 = allUIPermission;
        if (allUIPermission1 != null) {
            Gson gson = new Gson();
            allUIPermission1.setTabPermissionsString(gson.toJson(allUIPermission.getTabPermissions()));
            allUIPermission1.setClinicalNotesPermissionsString(gson.toJson(allUIPermission.getClinicalNotesPermissions()));
            allUIPermission1.setPatientVisitPermissionsString(gson.toJson(allUIPermission.getPatientVisitPermissions()));
            allUIPermission1.setPrescriptionPermissionsString(gson.toJson(allUIPermission.getTabPermissions()));
            allUIPermission1.setProfilePermissionsString(gson.toJson(allUIPermission.getProfilePermissions()));
            allUIPermission.save();
        }
    }

    public VolleyResponseBean getSearchedPatientsListPageWise(WebServiceType webServiceType, String doctorId, String hospitalId, String locationId,
                                                              int pageNum, int maxSize, String searchTerm,
                                                              Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(RegisteredPatientDetailsUpdated.class.getSimpleName())
                    + " where "
                    + LocalDatabaseUtils.KEY_HOSPITAL_ID + "=\"" + hospitalId + "\""
                    + " AND "
                    + LocalDatabaseUtils.KEY_LOCATION_ID + "=\"" + locationId + "\"";
            if (!Util.isNullOrBlank(searchTerm))
                whereCondition = whereCondition
                        + " AND "
                        + "(" + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_LOCAL_PATIENT_NAME, searchTerm)
                        + " OR "
                        + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_MOBILE_NUMBER, searchTerm)
                        + ")";
            //specifying order by limit and offset query
            String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_LOCAL_PATIENT_NAME + " COLLATE NOCASE ASC  "
                    + " LIMIT " + maxSize
                    + " OFFSET " + (pageNum * maxSize);

            whereCondition = whereCondition + conditionsLimit;
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            List<RegisteredPatientDetailsUpdated> list = SugarRecord.findWithQuery(RegisteredPatientDetailsUpdated.class, whereCondition);

            if (!Util.isNullOrEmptyList(list)) {
                for (RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated : list) {
                    getPatientRestDetails(registeredPatientDetailsUpdated);
                }
            }
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    private RegisteredPatientDetailsUpdated getPatientRestDetails(RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        // getting address
        registeredPatientDetailsUpdated.setAddress(getPatientAddress(registeredPatientDetailsUpdated.getUserId()));
        if (!Util.isNullOrBlank(registeredPatientDetailsUpdated.getForeignPatientId())) {
            // gettiung DOB
            registeredPatientDetailsUpdated.setDob((DOB) getObject(DOB.class,
                    LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, registeredPatientDetailsUpdated.getForeignPatientId()));
            // getting patient
            registeredPatientDetailsUpdated
                    .setPatient(getPatientDetails(registeredPatientDetailsUpdated.getForeignPatientId()));
            registeredPatientDetailsUpdated.setGroupIds(getGroupIds(registeredPatientDetailsUpdated.getUserId()));
            if (!Util.isNullOrEmptyList(registeredPatientDetailsUpdated.getGroupIds()))
                registeredPatientDetailsUpdated.setGroups(getUserGroupsFromAssignedGroups(registeredPatientDetailsUpdated.getGroupIds()));
            if (!Util.isNullOrBlank(registeredPatientDetailsUpdated.getForeignReferredById()))
                registeredPatientDetailsUpdated.setReferredBy((Reference) getObject(Reference.class, LocalDatabaseUtils.KEY_UNIQUE_ID, registeredPatientDetailsUpdated.getForeignReferredById()));


        }
        return registeredPatientDetailsUpdated;
    }

    private Address getPatientAddress(String userId) {
        return Select.from(Address.class).where(Condition.prop(LocalDatabaseUtils.KEY_USER_ID).eq(userId)).first();
    }

    private Patient getPatientDetails(String patientId) {
        Patient patient = (Patient) getObject(Patient.class, LocalDatabaseUtils.KEY_PATIENT_ID, patientId);
        if (patient != null) {
            patient.setRelations((List<Relations>) getListBySelectQuery(Relations.class,
                    LocalDatabaseUtils.KEY_FOREIGN_PATIENT_ID, patientId));
            patient.setNotesTableList((List<NotesTable>) getListBySelectQuery(NotesTable.class,
                    LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, patientId));
            if (!Util.isNullOrEmptyList(patient.getNotesTableList())) {
                ArrayList<String> notesTextList = new ArrayList<>();
                for (NotesTable notesTable : patient.getNotesTableList()) {
                    notesTextList.add(notesTable.getNote());
                }
                patient.setNotes(notesTextList);
            }
        }
        return patient;
    }

    private List<UserGroups> getUserGroupsFromAssignedGroups(ArrayList<String> groupsIdsList) {
        List<UserGroups> groupsList = new ArrayList<>();
        try {
            if (!Util.isNullOrEmptyList(groupsIdsList)) {
                for (String groupId :
                        groupsIdsList) {
                    AssignedGroupsTable assignedGroupsTable = Select.from(AssignedGroupsTable.class)
                            .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(groupId)).first();
                    if (assignedGroupsTable != null) {
                        UserGroups group = new UserGroups();
                        ReflectionUtil.copy(group, assignedGroupsTable);
                        groupsList.add(group);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupsList;
    }

    private ArrayList<String> getGroupIds(String userId) {
        List<PatientIdGroupId> list = Select.from(PatientIdGroupId.class).where(Condition.prop(LocalDatabaseUtils.KEY_FOREIGN_PATIENT_ID).eq(userId)).list();
        ArrayList<String> groupIdsList = new ArrayList<String>();
        if (!Util.isNullOrEmptyList(list)) {
            for (PatientIdGroupId patientIdGroupId :
                    list) {
                groupIdsList.add(patientIdGroupId.getForeignGroupId());
            }
        }
        return groupIdsList;
    }

    public void clearPatientsList() {
        RegisteredPatientDetailsUpdated.deleteAll(RegisteredPatientDetailsUpdated.class);
    }

    public RegisteredPatientDetailsUpdated getPatient(String selecetdPatientId) {
        RegisteredPatientDetailsUpdated patient = Select.from(RegisteredPatientDetailsUpdated.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_USER_ID).eq(selecetdPatientId),
                        Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(MenuDrawerFragment.SELECTED_LOCATION_ID)).first();
        if (patient != null) {
            RegisteredPatientDetailsUpdated patientDetail = getPatientRestDetails(patient);
            return patientDetail;
        }
        return null;
    }

    public VolleyResponseBean getPatientsListWithGroup(WebServiceType webServiceType, String doctorId, String hospitalId, String locationId, String groupId,
                                                       boolean discarded, int pageNum, int maxSize,
                                                       Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(PatientIdGroupId.class.getSimpleName())
                    + " where "
                    + LocalDatabaseUtils.KEY_FOREIGN_GROUP_ID + "=\"" + groupId + "\"";

            //specifying order by limit and offset query
            String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_LOCAL_PATIENT_NAME + " COLLATE NOCASE ASC  "
                    + " LIMIT " + maxSize
                    + " OFFSET " + (pageNum * maxSize);

            whereCondition = whereCondition + conditionsLimit;
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            List<PatientIdGroupId> list = SugarRecord.findWithQuery(PatientIdGroupId.class, whereCondition);
            List<RegisteredPatientDetailsUpdated> patientsList = new ArrayList<>();
            if (!Util.isNullOrEmptyList(list)) {
                for (PatientIdGroupId patientIdGroupId :
                        list) {
                    RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated = getPatient(patientIdGroupId.getForeignPatientId());
                    if (registeredPatientDetailsUpdated != null) {
                        patientsList.add(registeredPatientDetailsUpdated);
                    }
                }
                if (!Util.isNullOrEmptyList(patientsList))
                    Collections.sort(patientsList, ComparatorUtil.patientsNameComparator);
            }
            volleyResponseBean.setDataList(getObjectsListFromMap(patientsList));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public VolleyResponseBean getPatientsListByFilterType(WebServiceType webServiceType, String doctorId, String hospitalId, String locationId, FilterItemType filterItemType,
                                                          boolean discarded, int pageNum, int maxSize,
                                                          Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(RegisteredPatientDetailsUpdated.class.getSimpleName())
                    + " where "
                    + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"" + doctorId + "\""
                    + " AND "
                    + LocalDatabaseUtils.KEY_HOSPITAL_ID + "=\"" + hospitalId + "\""
                    + " AND "
                    + LocalDatabaseUtils.KEY_LOCATION_ID + "=\"" + locationId + "\"";
            //specifying order by limit and offset query
            String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_CREATED_TIME + " DESC "
                    + " LIMIT " + maxSize
                    + " OFFSET " + (pageNum * maxSize);

            whereCondition = whereCondition + conditionsLimit;
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            List<RegisteredPatientDetailsUpdated> list = SugarRecord.findWithQuery(RegisteredPatientDetailsUpdated.class, whereCondition);

            if (!Util.isNullOrEmptyList(list)) {
                for (RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated : list) {
                    getPatientRestDetails(registeredPatientDetailsUpdated);
                }
            }
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public void addAlreadyRegisteredPatients(ArrayList<AlreadyRegisteredPatientsResponse> list) {
        AlreadyRegisteredPatientsResponse.deleteAll(AlreadyRegisteredPatientsResponse.class);
        AlreadyRegisteredPatientsResponse.saveInTx(list);
    }

    public List<AlreadyRegisteredPatientsResponse> getAlreadyRegisteredPatientsList(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        List<AlreadyRegisteredPatientsResponse> list = AlreadyRegisteredPatientsResponse.listAll(AlreadyRegisteredPatientsResponse.class);
        return list;
    }
}