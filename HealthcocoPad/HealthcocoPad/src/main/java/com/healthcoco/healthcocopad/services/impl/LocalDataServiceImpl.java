package com.healthcoco.healthcocopad.services.impl;

import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.Address;
import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.bean.PersonalHistory;
import com.healthcoco.healthcocopad.bean.UIPermissions;
import com.healthcoco.healthcocopad.bean.UiPermissionsBoth;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.*;
import com.healthcoco.healthcocopad.enums.AdvanceSearchOptionsType;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.FilterItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.RecordType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.enums.WeekDayNameType;
import com.healthcoco.healthcocopad.fragments.CalendarFragment;
import com.healthcoco.healthcocopad.fragments.ClinicalProfileFragment;
import com.healthcoco.healthcocopad.fragments.MenuDrawerFragment;
import com.healthcoco.healthcocopad.fragments.PatientRegistrationFragment;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LocalDatabaseUtils;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.orm.util.ReflectionUtil.getDomainClasses;

/**
 * Created by Shreshtha on 23-01-2017.
 */
@SuppressWarnings("unchecked")

public class LocalDataServiceImpl {
    private static final String TAG = LocalDataServiceImpl.class.getSimpleName();
    private static LocalDataServiceImpl mInstance;
    private static HealthCocoApplication mApp;
    private static Gson gson;

    private LocalDataServiceImpl() {
    }


    private enum FromTableType {
        ADD_TEMPLATES, ADD_TREATMENT, ADD_PRESCRIPTION
    }

    public static LocalDataServiceImpl getInstance(HealthCocoApplication application) {
        if (mInstance == null) {
            mInstance = new LocalDataServiceImpl();
            mApp = application;
            gson = new Gson();
        }
        Util.checkNetworkStatus(mApp.getApplicationContext());
        return mInstance;
    }

    private String getJsonFromObject(Object object) {
        if (object != null) {
            String json = gson.toJson(object);
//            if (json.contains("'"))
//                json = json.replaceAll("'", "''");
            return json;
        }
        return null;
    }

    private Object getObjectFromJson(Class<?> class1, String jsonString) {
        if (!Util.isNullOrBlank(jsonString)) {
            Object parsedObject = gson.fromJson(jsonString, class1);
            if (parsedObject instanceof LinkedTreeMap)
                parsedObject = GsonRequest.getObjectFromLinkedTreeMap(gson, class1, parsedObject);
            return parsedObject;
        }
        return null;
    }

    private ArrayList<?> getObjectsListFronJson(String jsonString) {
        if (!Util.isNullOrBlank(jsonString)) {
            ArrayList<Object> parsedObjectList = gson.fromJson(jsonString, ArrayList.class);
            if (!Util.isNullOrEmptyList(parsedObjectList) && parsedObjectList.get(0) instanceof LinkedTreeMap) {
                ArrayList<Object> list = new ArrayList<Object>();
                for (Object object : parsedObjectList) {
                    list.add(GsonRequest.getObjectFromLinkedTreeMap(gson, Object.class, object));
                }
                return list;
            }
            return parsedObjectList;
        }
        return null;
    }

    private ArrayList<?> getObjectsListFronJson(Class class1, String jsonString) {
        if (!Util.isNullOrBlank(jsonString)) {
            ArrayList<Object> parsedObjectList = gson.fromJson(jsonString, ArrayList.class);
            if (!Util.isNullOrEmptyList(parsedObjectList) && parsedObjectList.get(0) instanceof LinkedTreeMap) {
                ArrayList<Object> list = new ArrayList<Object>();
                for (Object object : parsedObjectList) {
                    list.add(GsonRequest.getObjectFromLinkedTreeMap(gson, class1, object));
                }
                return list;
            }
            return parsedObjectList;
        }
        return null;
    }

    public void addSyncAllObject(SyncAll syncAll) {
        LogUtils.LOGD(TAG, "SyncAllType add " + syncAll.getSyncAllType());
        syncAll.save();
    }

    public OtpVerification getOtpVerification(String doctorId, String locationId, String hospitalId, String patientId) {
        OtpVerification otpVerification = Select.from(OtpVerification.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId),
                        Condition.prop(LocalDatabaseUtils.KEY_HOSPITAL_ID).eq(hospitalId),
                        Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(locationId),
                        Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(patientId)).first();
        return otpVerification;
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
            doctor.getUser().setDobJsonString(getJsonFromObject(doctor.getUser().getDob()));

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
                            //saving roles
                            addRoles(doctor.getUser().getForeignLocationId(), doctor.getUser().getForeignHospitalId(), locationAndAccessControl.getRoles());
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
//
//    private void deleteDOBRecordIfAlreadyPresent(String key, String value) {
//        DOB.deleteAll(DOB.class, key + "= ?", value);
//    }

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

    public VolleyResponseBean getListSize(LocalTabelType tableType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(WebServiceType.GET_PATIENTS_COUNT);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            switch (tableType) {
                case REGISTERED_PATIENTS_DETAILS:
                    LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                    if (doctor != null) {
                        User user = doctor.getUser();
                        String whereCondition = "SELECT count(*) FROM " + StringUtil.toSQLName(RegisteredPatientDetailsUpdated.class.getSimpleName())
                                + getWhereConditionForPatientsList(user);

                        LogUtils.LOGD(TAG, "Select Query " + whereCondition);
                        SQLiteStatement statement = SugarRecord.getSugarDataBase().compileStatement(whereCondition);
                        long count = statement.simpleQueryForLong();
                        if (count > 0)
                            volleyResponseBean.setData("" + count);
                    }
                    break;
            }
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return volleyResponseBean;
    }

    public long getLatestUpdatedTime(LocalTabelType localTabelType) {
        return getLatestUpdatedTime(null, localTabelType);
    }

    public void addSpecialities(ArrayList<Specialities> specialitiesResponse) {
        Specialities.saveInTx(specialitiesResponse);
    }

    public VolleyResponseBean getSpecialitiesListVolleyResponse(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
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

    private ArrayList<Object> getObjectsListFromMap(ArrayList<?> list) {
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
            if (user.getDob() != null) {
            }
            user.setUiPermissions(getAssignedUserUiPermissions(user.getUniqueId()));
            user.setRoles((ArrayList<Role>) Select.from(Role.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_HOSPITAL_ID).eq(user.getForeignHospitalId()),
                            Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(user.getForeignLocationId())).list());
            if (!Util.isNullOrEmptyList(user.getRoles())) {
                ArrayList<RoleType> roleTypes = new ArrayList<>();
                for (Role role :
                        user.getRoles()) {
                    roleTypes.add(role.getRole());
                }
                user.setRoleTypes(roleTypes);
            }
            user.setUiPermissions(getAssignedUserUiPermissions(user.getUniqueId()));
        }
        return user;
    }
//
//    public UserPermissionsResponse getUserUiPermissionsObject(String doctorId) {
//        UserPermissionsResponse userPermissions = Select.from(UserPermissionsResponse.class)
//                .where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId)).first();
//        if (userPermissions != null) {
//            AssignedUserUiPermissions assignedPermisions = getAssignedUserUiPermissions(userPermissions.getDoctorId());
//            if (assignedPermisions != null) {
//                assignedPermisions.setTabPermissions((ArrayList<String>) (Object) getObjectsListFronJson(assignedPermisions.getTabPermissionsString()));
//                assignedPermisions.setClinicalNotesPermissions((ArrayList<String>) (Object) getObjectsListFronJson(assignedPermisions.getClinicalNotesPermissionsString()));
//                assignedPermisions.setPrescriptionPermissions((ArrayList<String>) (Object) getObjectsListFronJson(assignedPermisions.getPrescriptionPermissionsString()));
//                assignedPermisions.setPatientVisitPermissions((ArrayList<String>) (Object) getObjectsListFronJson(assignedPermisions.getPatientVisitPermissionsString()));
//                assignedPermisions.setProfilePermissions((ArrayList<String>) (Object) getObjectsListFronJson(assignedPermisions.getProfilePermissionsString()));
//            }
//        }
//        return userPermissions;
//    }

    public void getDosageDurationDirectionList(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
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

    public VolleyResponseBean addUserGroupsList(List<UserGroups> groupsList) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(WebServiceType.GET_GROUPS);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            for (UserGroups userGroup : groupsList) {
                userGroup.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return volleyResponseBean;
    }

    public VolleyResponseBean getClinicDetailsResponse(WebServiceType webServiceType, String locationId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        volleyResponseBean.setIsDataFromLocal(true);
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
            location.setAlternateClinicNumbers((ArrayList<String>) (Object) getObjectsListFronJson(String.class, location.getAlternateClinicNumbersJsonString()));
            location.setImages((List<ClinicImage>) getListByKeyValue(ClinicImage.class, LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, location.getUniqueId()));
            location.setClinicWorkingSchedules(getClinicWorkingSchedules(LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, location.getUniqueId()));
        }
        return location;
    }

    public VolleyResponseBean getDrugTypeListAdResponse(WebServiceType webServiceType, String doctorId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
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
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
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
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
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

    public VolleyResponseBean getHardcodedBloodGroupsList(Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(WebServiceType.GET_HARDCODED_BLOOD_GROUPS);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            List<Object> list = PatientRegistrationFragment.BLOOD_GROUPS;
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
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
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
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
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


    public void updateLatestTime(APILatestUpdatedTimes apiLatestUpdatedTimes) {
        apiLatestUpdatedTimes.save();
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
        }
        switch (localTabelType) {
            case CITIES:
                List<CityResponse> tempCitiesList = CityResponse.find(CityResponse.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(tempCitiesList))
                    latestUpdatedTime = tempCitiesList.get(0).getUpdatedTime();
                break;
            case USER_GROUP:
                String whereCondition = " SELECT MAX(updated_time) FROM " + StringUtil.toSQLName(APILatestUpdatedTimes.class.getSimpleName())
                        + " where "
                        + LocalDatabaseUtils.KEY_TABLE_NAME + "=\"" + localTabelType.getTableName() + "\"" + " COLLATE NOCASE ";

                LogUtils.LOGD(TAG, "Select Query " + whereCondition);
                SQLiteStatement statement = SugarRecord.getSugarDataBase().compileStatement(whereCondition);
                latestUpdatedTime = statement.simpleQueryForLong();
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
            case PRESENT_COMPLAINT_SUGGESTIONS:
                List<PresentComplaintSuggestions> tempPresentComplaintSuggestionsList = PresentComplaintSuggestions.find(PresentComplaintSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(tempPresentComplaintSuggestionsList))
                    latestUpdatedTime = tempPresentComplaintSuggestionsList.get(0).getUpdatedTime();
                break;
            case COMPLAINT_SUGGESTIONS:
                List<ComplaintSuggestions> tempComplaintSuggestionsLists = ComplaintSuggestions.find(ComplaintSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(tempComplaintSuggestionsLists))
                    latestUpdatedTime = tempComplaintSuggestionsLists.get(0).getUpdatedTime();
                break;
            case HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS:
                List<HistoryPresentComplaintSuggestions> historyPresentComplaintSuggestionsList = HistoryPresentComplaintSuggestions.find(HistoryPresentComplaintSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(historyPresentComplaintSuggestionsList))
                    latestUpdatedTime = historyPresentComplaintSuggestionsList.get(0).getUpdatedTime();
                break;
            case MENSTRUAL_HISTORY_SUGGESTIONS:
                List<MenstrualHistorySuggestions> menstrualHistorySuggestionsList = MenstrualHistorySuggestions.find(MenstrualHistorySuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(menstrualHistorySuggestionsList))
                    latestUpdatedTime = menstrualHistorySuggestionsList.get(0).getUpdatedTime();
                break;
            case OBSTETRIC_HISTORY_SUGGESTIONS:
                List<ObstetricHistorySuggestions> obstetricHistorySuggestionsList = ObstetricHistorySuggestions.find(ObstetricHistorySuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(obstetricHistorySuggestionsList))
                    latestUpdatedTime = obstetricHistorySuggestionsList.get(0).getUpdatedTime();
                break;
            case GENERAL_EXAMINATION_SUGGESTIONS:
                List<GeneralExaminationSuggestions> generalExaminationSuggestionsList = GeneralExaminationSuggestions.find(GeneralExaminationSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(generalExaminationSuggestionsList))
                    latestUpdatedTime = generalExaminationSuggestionsList.get(0).getUpdatedTime();
                break;
            case SYSTEMIC_EXAMINATION_SUGGESTIONS:
                List<SystemicExaminationSuggestions> systemicExaminationSuggestionsList = SystemicExaminationSuggestions.find(SystemicExaminationSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(systemicExaminationSuggestionsList))
                    latestUpdatedTime = systemicExaminationSuggestionsList.get(0).getUpdatedTime();
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
            case PROVISIONAL_DIAGNOSIS_SUGGESTIONS:
                List<ProvisionalDiagnosisSuggestions> provisionalDiagnosisSuggestionsList = ProvisionalDiagnosisSuggestions.find(ProvisionalDiagnosisSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(provisionalDiagnosisSuggestionsList))
                    latestUpdatedTime = provisionalDiagnosisSuggestionsList.get(0).getUpdatedTime();
                break;
            case DIAGNOSIS_SUGGESTIONS:
                List<DiagnosisSuggestions> tempDiagnosisSuggestionsList = DiagnosisSuggestions.find(DiagnosisSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(tempDiagnosisSuggestionsList))
                    latestUpdatedTime = tempDiagnosisSuggestionsList.get(0).getUpdatedTime();
                break;
            case NOTES_SUGGESTIONS:
                List<NotesSuggestions> notesSuggestionsList = NotesSuggestions.find(NotesSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(notesSuggestionsList))
                    latestUpdatedTime = notesSuggestionsList.get(0).getUpdatedTime();
                break;
            case ECG_DETAILS_SUGGESTIONS:
                List<EcgDetailSuggestions> ecgDetailSuggestionsList = EcgDetailSuggestions.find(EcgDetailSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(ecgDetailSuggestionsList))
                    latestUpdatedTime = ecgDetailSuggestionsList.get(0).getUpdatedTime();
                break;
            case ECHO_SUGGESTIONS:
                List<EchoSuggestions> echoSuggestionsList = EchoSuggestions.find(EchoSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(echoSuggestionsList))
                    latestUpdatedTime = echoSuggestionsList.get(0).getUpdatedTime();
                break;
            case X_RAY_DETAILS_SUGGESTIONS:
                List<XrayDetailSuggestions> xrayDetailSuggestionsList = XrayDetailSuggestions.find(XrayDetailSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(xrayDetailSuggestionsList))
                    latestUpdatedTime = xrayDetailSuggestionsList.get(0).getUpdatedTime();
                break;
            case HOLTER_SUGGESTIONS:
                List<HolterSuggestions> holterSuggestionsList = HolterSuggestions.find(HolterSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(holterSuggestionsList))
                    latestUpdatedTime = holterSuggestionsList.get(0).getUpdatedTime();
                break;
            case PA_SUGGESTIONS:
                List<PaSuggestions> paSuggestionsList = PaSuggestions.find(PaSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(paSuggestionsList))
                    latestUpdatedTime = paSuggestionsList.get(0).getUpdatedTime();
                break;
            case PV_SUGGESTIONS:
                List<PvSuggestions> pvSuggestionsList = PvSuggestions.find(PvSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(pvSuggestionsList))
                    latestUpdatedTime = pvSuggestionsList.get(0).getUpdatedTime();
                break;
            case PS_SUGGESTIONS:
                List<PsSuggestions> psSuggestionsList = PsSuggestions.find(PsSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(psSuggestionsList))
                    latestUpdatedTime = psSuggestionsList.get(0).getUpdatedTime();
                break;
            case INDICATION_OF_USG_SUGGESTIONS:
                List<IndicationOfUsgSuggestions> indicationOfUsgSuggestionsList = IndicationOfUsgSuggestions.find(IndicationOfUsgSuggestions.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(indicationOfUsgSuggestionsList))
                    latestUpdatedTime = indicationOfUsgSuggestionsList.get(0).getUpdatedTime();
                break;
            case ADVICE_SUGGESTIONS:
                List<AdviceSuggestion> adviceSuggestions = AdviceSuggestion.find(AdviceSuggestion.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(adviceSuggestions))
                    latestUpdatedTime = adviceSuggestions.get(0).getUpdatedTime();
                break;
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
                        LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? ",
                        new String[]{doctorId},
                        null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(referenceList))
                    latestUpdatedTime = referenceList.get(0).getUpdatedTime();
                break;
            case PROFESSION:
                break;
            case VISIT_DETAILS:
                List<VisitDetails> visitDetailsList = VisitDetails.find(VisitDetails.class,
                        LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_PATIENT_ID + "= ?",
                        new String[]{doctorId, "" + HealthCocoConstants.SELECTED_PATIENTS_USER_ID},
                        null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(visitDetailsList))
                    latestUpdatedTime = visitDetailsList.get(0).getUpdatedTime();
//                case CALENDAR_EVENTS:
//                    List<CalendarEvents> tempCalendarEventsList = CalendarEvents.find(CalendarEvents.class,
//                            LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_IS_FROM_CALENDAR_API + "= ? AND " + LocalDatabaseUtils.KEY_IS_ADDED_ON_SUCCESS + "= ?",
//                            new String[]{doctorId, "" + BooleanTypeValues.TRUE.getBooleanIntValue(), "" + BooleanTypeValues.FALSE.getBooleanIntValue()},
//                            null, "updated_time DESC", "1");
//                    if (!Util.isNullOrEmptyList(tempCalendarEventsList))
//                        latestUpdatedTime = tempCalendarEventsList.get(0).getUpdatedTime();
                break;
            case CLINAL_NOTE:
                List<ClinicalNotes> tempClinicalNotes = ClinicalNotes.find(ClinicalNotes.class,
                        LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_PATIENT_ID + "= ?",
                        new String[]{doctorId, "" + HealthCocoConstants.SELECTED_PATIENTS_USER_ID},
                        null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(tempClinicalNotes))
                    latestUpdatedTime = tempClinicalNotes.get(0).getUpdatedTime();
                break;
            case PRESCRIPTION:
                List<Prescription> prescriptionList = Prescription.find(Prescription.class,
                        LocalDatabaseUtils.KEY_DOCTOR_ID + "= ? AND " + LocalDatabaseUtils.KEY_PATIENT_ID + "= ?",
                        new String[]{"" + HealthCocoConstants.SELECTED_PATIENTS_USER_ID},
                        null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(prescriptionList))
                    latestUpdatedTime = prescriptionList.get(0).getUpdatedTime();
                break;
        }
        if (latestUpdatedTime == null)
            latestUpdatedTime = 0l;
        LogUtils.LOGD(TAG, "Latest Updated Time for " + localTabelType + " : " + DateTimeUtil.getFormatedDateAndTime(latestUpdatedTime) + " , " + latestUpdatedTime);
        return latestUpdatedTime;
    }

    public void addDoctorProfile(DoctorProfile doctorProfile) {
        // setting DOB
        doctorProfile.setDobJsonString(getJsonFromObject(doctorProfile.getDob()));
        doctorProfile.setAdditionalNumbersJsonString(getJsonFromObject(doctorProfile.getAdditionalNumbers()));
        doctorProfile.setOtherEmailAddressesJsonString(getJsonFromObject(doctorProfile.getOtherEmailAddresses()));

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
        //saving roles
        addRoles(clinicProfile.getLocationId(), clinicProfile.getHospitalId(), clinicProfile.getRoles());
        clinicProfile.save();
    }

    private void addRoles(String locationId, String hospitalId, ArrayList<Role> roles) {
        deleteRoles(locationId, hospitalId);
        if (!Util.isNullOrEmptyList(roles)) {
            for (Role role :
                    roles) {
                if (!Util.isNullOrEmptyList(role.getAccessModules())) {
                    for (AccessModule accessModule : role.getAccessModules()) {
                        if (!Util.isNullOrEmptyList(accessModule.getAccessPermissionTypes()))
                            accessModule.setAccessPermissionTypesJson(new Gson().toJson(accessModule.getAccessPermissionTypes()));
                        accessModule.setForeignRoleId(role.getUniqueId());
                        accessModule.save();
                    }
                }
                role.setHospitalId(hospitalId);
                role.setLocationId(locationId);
                role.setUniqueId(role.getLocationId() + role.getHospitalId() + role.getUniqueId());
                role.save();
            }
            Role.saveInTx(roles);
        }
    }

    private void deleteRoles(String locationId, String hospitalId) {
        Role.deleteInTx(Role.class,
                LocalDatabaseUtils.KEY_LOCATION_ID + "= ? AND " + LocalDatabaseUtils.KEY_HOSPITAL_ID + "= ?",
                new String[]{locationId, hospitalId});
    }


    private void addExperienceDetailsList(String key, String value, List<DoctorExperienceDetail> list) {
        for (DoctorExperienceDetail experienceDetail :
                list) {
            if (key.equals(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID))
                experienceDetail.setForeignUniqueId(value);
        }
        DoctorExperienceDetail.saveInTx(list);
    }

    public VolleyResponseBean addPatientsList(ArrayList<RegisteredPatientDetailsUpdated> patientsList) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(WebServiceType.REGISTER_PATIENT);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            for (RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated : patientsList) {
                addPatient(registeredPatientDetailsUpdated);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return volleyResponseBean;
    }

    public void addPatient(RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        // setting patient
        if (registeredPatientDetailsUpdated.getPatient() != null
                && !Util.isNullOrBlank(registeredPatientDetailsUpdated.getPatient().getPatientId())) {

            Patient patient = registeredPatientDetailsUpdated.getPatient();
            registeredPatientDetailsUpdated.setProfession(patient.getProfession());
            registeredPatientDetailsUpdated.setBloodGroup(patient.getBloodGroup());
            registeredPatientDetailsUpdated.setEmailAddress(patient.getEmailAddress());

            //setting DOB
            registeredPatientDetailsUpdated.setDobJsonString(getJsonFromObject(registeredPatientDetailsUpdated.getDob()));
            // setting patient's relations
            addRelations(patient.getRelations(), patient.getPatientId());

            patient.setNotesJsonString(getJsonFromObject(patient.getNotes()));
            registeredPatientDetailsUpdated.setForeignPatientId(registeredPatientDetailsUpdated.getPatient().getPatientId());
            registeredPatientDetailsUpdated.getPatient().save();
        }
        // setting address
        registeredPatientDetailsUpdated.setAddressJsonString(getJsonFromObject(registeredPatientDetailsUpdated.getAddress()));

        addGroupsToAssignedGroupsTable(registeredPatientDetailsUpdated, registeredPatientDetailsUpdated.getGroups());

//        deleteReferredByIfAlreadyPresent(LocalDatabaseUtils.KEY_UNIQUE_ID, LocalDatabaseUtils.KEY_IS_FROM_CONTACTS_LIST, registeredPatientDetailsUpdated.getForeignReferredById(), "" + LocalDatabaseUtils.BOOLEAN_TRUE_VALUE);
//        if (registeredPatientDetailsUpdated.getReferredBy() != null) {
//            registeredPatientDetailsUpdated.getReferredBy().setIsFromContactsList(true);
//            registeredPatientDetailsUpdated.setForeignReferredById(registeredPatientDetailsUpdated.getReferredBy().getUniqueId());
//            registeredPatientDetailsUpdated.getReferredBy().save();
//        }

        registeredPatientDetailsUpdated.setReferredByJsonString(getJsonFromObject(registeredPatientDetailsUpdated.getReferredBy()));
        formOtpVerificationAndAdd(registeredPatientDetailsUpdated);
        registeredPatientDetailsUpdated.setUniqueId(registeredPatientDetailsUpdated.getLocationId() + registeredPatientDetailsUpdated.getUserId());
        registeredPatientDetailsUpdated.setConsultantDoctorIdsJsonString(getJsonFromObject(registeredPatientDetailsUpdated.getConsultantDoctorIds()));
        registeredPatientDetailsUpdated.save();
    }

    private void formOtpVerificationAndAdd(RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setDoctorId(registeredPatientDetailsUpdated.getDoctorId());
        otpVerification.setLocationId(registeredPatientDetailsUpdated.getLocationId());
        otpVerification.setHospitalId(registeredPatientDetailsUpdated.getHospitalId());
        otpVerification.setPatientId(registeredPatientDetailsUpdated.getUserId());
        deleteOtpVerificationIfAlreadyExists(registeredPatientDetailsUpdated);
        addOtpVerification(otpVerification);
    }

    private void deleteOtpVerificationIfAlreadyExists(RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        try {
            List<OtpVerification> list = Select.from(OtpVerification.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(registeredPatientDetailsUpdated.getDoctorId()),
                            Condition.prop(LocalDatabaseUtils.KEY_HOSPITAL_ID).eq(registeredPatientDetailsUpdated.getHospitalId()),
                            Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(registeredPatientDetailsUpdated.getLocationId()),
                            Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(registeredPatientDetailsUpdated.getUserId())).list();
            OtpVerification.deleteInTx(list);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, "deleteOtpVerificationIfAlreadyExists ");
            e.printStackTrace();
        }
    }

    public void addOtpVerification(OtpVerification otpVerification) {
        otpVerification.save();
    }

    private void addGroupsToAssignedGroupsTable(RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated, List<UserGroups> groupsList) {
        try {
            ArrayList<String> groupIdsList = new ArrayList<String>();
            if (!Util.isNullOrEmptyList(groupsList)) {
                for (UserGroups userGroup :
                        groupsList) {
                    groupIdsList.add(userGroup.getUniqueId());
                }
                addUserGroupsList(groupsList);
            }
            registeredPatientDetailsUpdated.setGroupIdsJsonString(getJsonFromObject(groupIdsList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRelations(List<Relations> list, String patientId) {
        deleteAllRelationsIsAlreadyExists(LocalDatabaseUtils.KEY_FOREIGN_PATIENT_ID, patientId);
        if (!Util.isNullOrEmptyList(list)) {
            for (Relations relation : list) {
                relation.setForeignPatientId(patientId);
                relation.save();
            }
        }
    }

    private void deleteAllRelationsIsAlreadyExists(String key, String value) {
        Relations.deleteAll(Relations.class, key + "= ?", value);
    }

    public void addEducationsList(ArrayList<EducationQualification> list) {
        EducationQualification.deleteAll(EducationQualification.class);
        EducationQualification.saveInTx(list);
    }

    public void addCollegeUniversityInstituteList(ArrayList<CollegeUniversityInstitute> list) {
        CollegeUniversityInstitute.deleteAll(CollegeUniversityInstitute.class);
        CollegeUniversityInstitute.saveInTx(list);
    }

    public void addMedicalCouncilList(ArrayList<MedicalCouncil> list) {
        MedicalCouncil.deleteAll(MedicalCouncil.class);
        MedicalCouncil.saveInTx(list);
    }

    public void addSuggestionsList(WebServiceType webServiceType, LocalTabelType localTabelType, ArrayList<?> list, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            switch (localTabelType) {
                case PRESENT_COMPLAINT_SUGGESTIONS:
                    ArrayList<PresentComplaintSuggestions> presentComplaintSuggestionses = (ArrayList<PresentComplaintSuggestions>) (ArrayList<?>) list;
                    PresentComplaintSuggestions.saveInTx(presentComplaintSuggestionses);
                    break;
                case COMPLAINT_SUGGESTIONS:
                    ArrayList<ComplaintSuggestions> complaintSuggestionsList = (ArrayList<ComplaintSuggestions>) (ArrayList<?>) list;
                    ComplaintSuggestions.saveInTx(complaintSuggestionsList);
                    break;
                case HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS:
                    ArrayList<HistoryPresentComplaintSuggestions> historyPresentComplaintSuggestionses = (ArrayList<HistoryPresentComplaintSuggestions>) (ArrayList<?>) list;
                    HistoryPresentComplaintSuggestions.saveInTx(historyPresentComplaintSuggestionses);
                    break;
                case MENSTRUAL_HISTORY_SUGGESTIONS:
                    ArrayList<MenstrualHistorySuggestions> menstrualHistorySuggestionses = (ArrayList<MenstrualHistorySuggestions>) (ArrayList<?>) list;
                    MenstrualHistorySuggestions.saveInTx(menstrualHistorySuggestionses);
                    break;
                case OBSTETRIC_HISTORY_SUGGESTIONS:
                    ArrayList<ObstetricHistorySuggestions> obstetricHistorySuggestionses = (ArrayList<ObstetricHistorySuggestions>) (ArrayList<?>) list;
                    ObstetricHistorySuggestions.saveInTx(obstetricHistorySuggestionses);
                    break;
                case GENERAL_EXAMINATION_SUGGESTIONS:
                    ArrayList<GeneralExaminationSuggestions> generalExaminationSuggestionses = (ArrayList<GeneralExaminationSuggestions>) (ArrayList<?>) list;
                    GeneralExaminationSuggestions.saveInTx(generalExaminationSuggestionses);
                    break;
                case SYSTEMIC_EXAMINATION_SUGGESTIONS:
                    ArrayList<SystemicExaminationSuggestions> systemicExaminationSuggestionses = (ArrayList<SystemicExaminationSuggestions>) (ArrayList<?>) list;
                    SystemicExaminationSuggestions.saveInTx(systemicExaminationSuggestionses);
                    break;
                case OBSERVATION_SUGGESTIONS:
                    ArrayList<ObservationSuggestions> observationSuggestionsList = (ArrayList<ObservationSuggestions>) (ArrayList<?>) list;
                    ObservationSuggestions.saveInTx(observationSuggestionsList);
                    break;
                case INVESTIGATION_SUGGESTIONS:
                    ArrayList<InvestigationSuggestions> investigationSuggestionsList = (ArrayList<InvestigationSuggestions>) (ArrayList<?>) list;
                    InvestigationSuggestions.saveInTx(investigationSuggestionsList);
                    break;
                case PROVISIONAL_DIAGNOSIS_SUGGESTIONS:
                    ArrayList<ProvisionalDiagnosisSuggestions> provisionalDiagnosisSuggestionses = (ArrayList<ProvisionalDiagnosisSuggestions>) (ArrayList<?>) list;
                    ProvisionalDiagnosisSuggestions.saveInTx(provisionalDiagnosisSuggestionses);
                    break;
                case DIAGNOSIS_SUGGESTIONS:
                    ArrayList<DiagnosisSuggestions> diagnosisSuggestionsList = (ArrayList<DiagnosisSuggestions>) (ArrayList<?>) list;
                    DiagnosisSuggestions.saveInTx(diagnosisSuggestionsList);
                    break;
                case NOTES_SUGGESTIONS:
                    ArrayList<NotesSuggestions> notesSuggestionses = (ArrayList<NotesSuggestions>) (ArrayList<?>) list;
                    NotesSuggestions.saveInTx(notesSuggestionses);
                    break;
                case ECG_DETAILS_SUGGESTIONS:
                    ArrayList<EcgDetailSuggestions> ecgDetailSuggestionses = (ArrayList<EcgDetailSuggestions>) (ArrayList<?>) list;
                    EcgDetailSuggestions.saveInTx(ecgDetailSuggestionses);
                    break;
                case ECHO_SUGGESTIONS:
                    ArrayList<EchoSuggestions> echoSuggestionses = (ArrayList<EchoSuggestions>) (ArrayList<?>) list;
                    EchoSuggestions.saveInTx(echoSuggestionses);
                    break;
                case X_RAY_DETAILS_SUGGESTIONS:
                    ArrayList<XrayDetailSuggestions> xrayDetailSuggestionses = (ArrayList<XrayDetailSuggestions>) (ArrayList<?>) list;
                    XrayDetailSuggestions.saveInTx(xrayDetailSuggestionses);
                    break;
                case HOLTER_SUGGESTIONS:
                    ArrayList<HolterSuggestions> holterSuggestionses = (ArrayList<HolterSuggestions>) (ArrayList<?>) list;
                    HolterSuggestions.saveInTx(holterSuggestionses);
                    break;
                case PA_SUGGESTIONS:
                    ArrayList<PaSuggestions> paSuggestionses = (ArrayList<PaSuggestions>) (ArrayList<?>) list;
                    PaSuggestions.saveInTx(paSuggestionses);
                    break;
                case PV_SUGGESTIONS:
                    ArrayList<PvSuggestions> pvSuggestionses = (ArrayList<PvSuggestions>) (ArrayList<?>) list;
                    PvSuggestions.saveInTx(pvSuggestionses);
                    break;
                case PS_SUGGESTIONS:
                    ArrayList<PsSuggestions> psSuggestionses = (ArrayList<PsSuggestions>) (ArrayList<?>) list;
                    PsSuggestions.saveInTx(psSuggestionses);
                    break;
                case INDICATION_OF_USG_SUGGESTIONS:
                    ArrayList<IndicationOfUsgSuggestions> indicationOfUsgSuggestionses = (ArrayList<IndicationOfUsgSuggestions>) (ArrayList<?>) list;
                    IndicationOfUsgSuggestions.saveInTx(indicationOfUsgSuggestionses);
                    break;
                case ADVICE_SUGGESTIONS:
                    ArrayList<AdviceSuggestion> adviceSuggestions = (ArrayList<AdviceSuggestion>) (ArrayList<?>) list;
                    AdviceSuggestion.saveInTx(adviceSuggestions);
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
//
//    private void deleteAdditionalNumbersIfAlreadyPresent(String key, String value) {
//        ForieignAdditionalNumbers.deleteAll(ForieignAdditionalNumbers.class, key + "= ?", value);
//    }

    public void addLocation(Location location) {
        location.setAlternateClinicNumbersJsonString(getJsonFromObject(location.getAlternateClinicNumbers()));
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

//    private void addAdditionalNumbers(String key, String value, List<String> list) {
//        for (String number :
//                list) {
//            if (!Util.isNullOrBlank(number)) {
//                ForieignAdditionalNumbers additionalNumbers = new ForieignAdditionalNumbers();
//                additionalNumbers.setForeignUniqueId(value);
//                additionalNumbers.setAdditionalNumber(number);
//                additionalNumbers.setCustomUniqueId(additionalNumbers.getForeignUniqueId() + additionalNumbers.getAdditionalNumber());
//                additionalNumbers.save();
//            }
//        }
//    }

    public VolleyResponseBean getDoctorProfileResponse(WebServiceType webServiceType, String doctorId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
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
            doctorProfile.setDob((DOB) getObjectFromJson(DOB.class, doctorProfile.getDobJsonString()));
            doctorProfile.setAdditionalNumbers((ArrayList<String>) (Object) getObjectsListFronJson(String.class, doctorProfile.getAdditionalNumbersJsonString()));
            doctorProfile.setOtherEmailAddresses((ArrayList<String>) (Object) getObjectsListFronJson(String.class, doctorProfile.getOtherEmailAddressesJsonString()));
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
                getDoctorCLinicProfileRestDetails(clinicProfile);
            }
        }
        return list;
    }

    private void getDoctorCLinicProfileRestDetails(DoctorClinicProfile clinicProfile) {
        clinicProfile.setAppointmentBookingNumber(getAppointmentBookingNumber(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicProfile.getUniqueId()));
        clinicProfile.setConsultationFee((ConsultationFee) getObject(ConsultationFee.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, clinicProfile.getUniqueId()));
        clinicProfile.setAppointmentSlot(getAppointmentSlot(clinicProfile.getUniqueId()));
        clinicProfile.setWorkingSchedules(getWorkingSchedulesForDoctor(clinicProfile.getDoctorId(), clinicProfile.getLocationId()));
        clinicProfile.setImages((List<ClinicImage>) getListByKeyValue(ClinicImage.class, LocalDatabaseUtils.KEY_FOREIGN_LOCATION_ID, clinicProfile.getUniqueId()));
        clinicProfile.setRoles((ArrayList<Role>) Select.from(Role.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_HOSPITAL_ID).eq(clinicProfile.getHospitalId()),
                        Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(clinicProfile.getLocationId())).list());

    }

    public DoctorClinicProfile getDoctorClinicProfile(String doctorId, String locationId) {
        DoctorClinicProfile clinicProfile = Select.from(DoctorClinicProfile.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID).eq(doctorId),
                        Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(locationId)).first();
        if (clinicProfile != null) {
            getDoctorCLinicProfileRestDetails(clinicProfile);
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
                if (!Util.isNullOrBlank(accessModule.getAccessPermissionTypesJson()))
                    accessModule.setAccessPermissionTypes(new Gson().fromJson(accessModule.getAccessPermissionTypesJson(), ArrayList.class));
            }
        }
    }

    public VolleyResponseBean getCitiesList(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsFromLocalAfterApiSuccess(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
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

    public VolleyResponseBean getSearchedPatientsListPageWise(WebServiceType webServiceType, User user,
                                                              int pageNum, int maxSize, FilterItemType filterType, AdvanceSearchOptionsType advanceSearchOptionsType, String searchTerm,
                                                              Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(RegisteredPatientDetailsUpdated.class.getSimpleName())
                    + getWhereConditionForPatientsList(user);
            if (advanceSearchOptionsType != null && !Util.isNullOrBlank(searchTerm)) {
                switch (advanceSearchOptionsType) {
                    case GENERAL_SEARCH:
                        whereCondition = whereCondition
                                + " AND "
                                + "(" + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_LOCAL_PATIENT_NAME, searchTerm)
                                + " OR "
                                + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_MOBILE_NUMBER, searchTerm)
                                + ")";
                        break;
                    case PATIENT_NAME:
                        whereCondition = whereCondition
                                + " AND "
                                + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_LOCAL_PATIENT_NAME, searchTerm);
                        break;
                    case MOBILE_NUMBER:
                        whereCondition = whereCondition
                                + " AND "
                                + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_MOBILE_NUMBER, searchTerm);
                        break;
                    case PATIENT_ID:
                        whereCondition = whereCondition
                                + " AND "
                                + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_PID, searchTerm);
                        break;
                    case REFERENCE:
                        whereCondition = whereCondition
                                + " AND "
                                + LocalDatabaseUtils.getSearchFromJsonQuery(LocalDatabaseUtils.KEY_REFERRED_BY_JSON_STRING, LocalDatabaseUtils.JSON_KEY_REFERENCE, searchTerm);
                        break;
                    case PROFESSION:
                        whereCondition = whereCondition
                                + " AND "
                                + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_PROFESSION, searchTerm);
                        break;
                    case BLOOD_GROUP:
                        whereCondition = whereCondition
                                + " AND "
                                + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_BLOOD_GROUP, searchTerm);
                        break;
                    case EMAIL:
                        whereCondition = whereCondition
                                + " AND "
                                + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_EMAIL_ADDRESS, searchTerm);
                        break;
                    default:
                        break;
                }
            }
            //specifying order by limit and offset query
            String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_LOCAL_PATIENT_NAME + " COLLATE NOCASE ASC  "
                    + " LIMIT " + maxSize
                    + " OFFSET " + (pageNum * maxSize);
            if (filterType != null)
                switch (filterType) {
                    case RECENTLY_ADDED:
                        //specifying order by limit and offset query
                        conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_CREATED_TIME + " DESC "
                                + " LIMIT " + maxSize
                                + " OFFSET " + (pageNum * maxSize);
                        break;
                }


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

    private String getWhereConditionForPatientsList(User user) {
        String whereCondition = " where "
                + LocalDatabaseUtils.KEY_HOSPITAL_ID + "=\"" + user.getForeignHospitalId() + "\""
                + " AND "
                + LocalDatabaseUtils.KEY_LOCATION_ID + "=\"" + user.getForeignLocationId() + "\"";
        if (RoleType.isOnlyConsultant(user.getRoleTypes())) {
            whereCondition = whereCondition
                    + " AND " + LocalDatabaseUtils.KEY_IS_PART_OF_CONSULTANT_DOCTOR + "=" + LocalDatabaseUtils.BOOLEAN_TRUE_VALUE
                    + " AND "
                    + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_CONSULTANT_DOCTOR_IDS_JSON_STRING, user.getUniqueId());
        }
        return whereCondition;
    }

    private RegisteredPatientDetailsUpdated getPatientRestDetails(RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        // getting address
        registeredPatientDetailsUpdated.setAddress((Address) getObjectFromJson(Address.class, registeredPatientDetailsUpdated.getAddressJsonString()));
        if (!Util.isNullOrBlank(registeredPatientDetailsUpdated.getForeignPatientId())) {
            // gettiung DOB
            registeredPatientDetailsUpdated.setDob((DOB) getObjectFromJson(DOB.class, registeredPatientDetailsUpdated.getDobJsonString()));
            // getting patient
            registeredPatientDetailsUpdated
                    .setPatient(getPatientDetails(registeredPatientDetailsUpdated.getForeignPatientId()));
            registeredPatientDetailsUpdated.setGroupIds((ArrayList<String>) (Object) getObjectsListFronJson(registeredPatientDetailsUpdated.getGroupIdsJsonString()));
            if (!Util.isNullOrEmptyList(registeredPatientDetailsUpdated.getGroupIds()))
                registeredPatientDetailsUpdated.setGroups(getUserGroupsFromAssignedGroups(registeredPatientDetailsUpdated.getGroupIds()));
//            if (!Util.isNullOrBlank(registeredPatientDetailsUpdated.getForeignReferredById()))
//                registeredPatientDetailsUpdated.setReferredBy((Reference) getObject(Reference.class, LocalDatabaseUtils.KEY_UNIQUE_ID, registeredPatientDetailsUpdated.getForeignReferredById()));
            registeredPatientDetailsUpdated.setReferredBy((Reference) getObjectFromJson(Reference.class, registeredPatientDetailsUpdated.getReferredByJsonString()));
            registeredPatientDetailsUpdated.setConsultantDoctorIds((ArrayList<String>) (Object) getObjectsListFronJson(registeredPatientDetailsUpdated.getConsultantDoctorIdsJsonString()));
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
            patient.setNotes((ArrayList<String>) (Object) getObjectsListFronJson(patient.getNotesJsonString()));
        }
        return patient;
    }

    private List<UserGroups> getUserGroupsFromAssignedGroups(ArrayList<String> groupsIdsList) {
        List<UserGroups> groupsList = new ArrayList<>();
        try {
            if (!Util.isNullOrEmptyList(groupsIdsList)) {
                for (String groupId :
                        groupsIdsList) {
                    UserGroups group = getUserGroup(groupId);
                    if (group != null)
                        groupsList.add(group);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupsList;
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

    public VolleyResponseBean getPatientsListWithGroup(WebServiceType webServiceType, User user, String groupId,
                                                       boolean discarded, int pageNum, int maxSize,
                                                       Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(RegisteredPatientDetailsUpdated.class.getSimpleName())
                    + getWhereConditionForPatientsList(user);
            whereCondition = whereCondition
                    + " AND "
                    + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_GROUP_IDS_JSON_STRING, groupId);
            //specifying order by limit and offset query
//            String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_LOCAL_PATIENT_NAME + " COLLATE NOCASE ASC  "
//                    + " LIMIT " + maxSize
//                    + " OFFSET " + (pageNum * maxSize);
            String conditionsLimit = " LIMIT " + maxSize
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

    public AlreadyRegisteredPatientsResponse getALreadyRegisteredPatient(String uniqueId) {
        AlreadyRegisteredPatientsResponse patient = Select.from(AlreadyRegisteredPatientsResponse.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_USER_ID).eq(uniqueId)).first();
        return patient;
    }

    public UserGroups getUserGroup(String groupId) {
        return Select.from(UserGroups.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(groupId)).first();
    }

    public VolleyResponseBean getReferenceList(WebServiceType webServiceType, String doctorId, String locationId, String hospitalId, BooleanTypeValues isDiscarded, RecordType recordType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            String whereCondition = "";
            List<Reference> list = null;
            switch (recordType) {
                case CUSTOM:
                    whereCondition = "Select * from " + StringUtil.toSQLName(Reference.class.getSimpleName()) + " where "
                            + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"" + doctorId + "\""
                            + " AND " + LocalDatabaseUtils.KEY_LOCATION_ID + "=\"" + locationId + "\""
                            + " AND " + LocalDatabaseUtils.KEY_HOSPITAL_ID + "=\"" + hospitalId + "\"";
                    break;
                case BOTH:
                    whereCondition = "Select * from " + StringUtil.toSQLName(Reference.class.getSimpleName()) + " where " +
                            "(" + LocalDatabaseUtils.KEY_DOCTOR_ID + " is null"
                            + " OR "
                            + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"" + "\""
                            + " OR "
                            + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"" + doctorId + "\""
                            + ")";
                    break;
            }
            if (!Util.isNullOrBlank(whereCondition)) {
                whereCondition = whereCondition
                        + " AND " + "(" + LocalDatabaseUtils.KEY_REFERENCE + " is NOT null"
                        + " OR "
                        + LocalDatabaseUtils.KEY_REFERENCE + "!=\"" + "\""
                        + ")"
                        + " AND " + LocalDatabaseUtils.KEY_DISCARDED + " =" + isDiscarded.getBooleanIntValue();
                switch (isDiscarded) {
                    case TRUE:
                        volleyResponseBean.setLocalBackgroundTaskType(LocalBackgroundTaskType.GET_REFERENCE_HIDDEN_LIST);
                        break;
                    case FALSE:
                        volleyResponseBean.setLocalBackgroundTaskType(LocalBackgroundTaskType.GET_REFERENCE_ACTIVATED_LIST);
                        break;
                }
            }
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            list = SugarRecord.findWithQuery(Reference.class, whereCondition);
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public void addUserGroup(UserGroups userGroup) {
        userGroup.save();
    }

    public VolleyResponseBean getHistoryDetailResponse(WebServiceType webServiceType, BooleanTypeValues discarded, boolean isOtpVerified, String doctorId, String locationId, String hospitalId, String selectedPatientsUserId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(HistoryDetailsResponse.class.getSimpleName())
                    + " where ";
            if (isOtpVerified)
                whereCondition = whereCondition
                        + LocalDatabaseUtils.KEY_PATIENT_ID + "=\"" + selectedPatientsUserId + "\"";
            else {
                whereCondition = whereCondition
                        + LocalDatabaseUtils.KEY_PATIENT_ID + "=\"" + selectedPatientsUserId + "\""
                        + " AND "
                        + LocalDatabaseUtils.KEY_LOCATION_ID + "=\"" + locationId + "\""
                        + " AND "
                        + LocalDatabaseUtils.KEY_HOSPITAL_ID + "=\"" + hospitalId + "\"";
            }

            //specifying order by limit and offset query
            String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_CREATED_TIME + " DESC "
                    + " LIMIT 1";

            whereCondition = whereCondition + conditionsLimit;
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            List<HistoryDetailsResponse> list = SugarRecord.findWithQuery(HistoryDetailsResponse.class, whereCondition);
            if (!Util.isNullOrEmptyList(list)) {
                HistoryDetailsResponse historyDetailResponse = list.get(0);
                getHistoryRestDetail(historyDetailResponse);
                volleyResponseBean.setData(historyDetailResponse);
            }

            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    private void getHistoryRestDetail(HistoryDetailsResponse historyDetailsResponse) {
        ArrayList<String> familyHistoryIdsList = (ArrayList<String>) (Object) getObjectsListFronJson(historyDetailsResponse.getFamilyHistoryIdsJsonString());
        if (!Util.isNullOrEmptyList(familyHistoryIdsList)) {
            historyDetailsResponse.setFamilyhistory(getMedicalFamilyHistoryListHavingIds(MedicalFamilyHistoryDetails.class, familyHistoryIdsList));
        }
        ArrayList<String> medicalHistoryIdsList = (ArrayList<String>) (Object) getObjectsListFronJson(historyDetailsResponse.getMedicalHistoryIdsJsonString());
        if (!Util.isNullOrEmptyList(medicalHistoryIdsList)) {
            historyDetailsResponse.setMedicalhistory(getMedicalFamilyHistoryListHavingIds(MedicalFamilyHistoryDetails.class, medicalHistoryIdsList));
        }
        historyDetailsResponse.setPersonalHistory((PersonalHistory) getObjectFromJson(PersonalHistory.class, historyDetailsResponse.getPersonalHistoryJsonString()));
        historyDetailsResponse.setDrugsAndAllergies(getDrugsAndAllergies(historyDetailsResponse.getPatientId()));
    }

    private DrugsAndAllergies getDrugsAndAllergies(String patientId) {
        DrugsAndAllergies drugsAndAllergies = Select.from(DrugsAndAllergies.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(patientId)).first();
        if (drugsAndAllergies != null) {
            drugsAndAllergies.setDrugs(getDrugsList((ArrayList<String>) (Object) getObjectsListFronJson(drugsAndAllergies.getDrugIdsJsonString())));
        }
        return drugsAndAllergies;
    }

    private List<Drug> getDrugsList(ArrayList<String> drugIdsList) {
        if (!Util.isNullOrEmptyList(drugIdsList)) {
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(Drug.class.getSimpleName())
                    + getWhereConditionForKeyWithValues(LocalDatabaseUtils.KEY_UNIQUE_ID, drugIdsList);
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            List<Drug> drugsList = SugarRecord.findWithQuery(Drug.class, whereCondition);
            if (!Util.isNullOrEmptyList(drugsList)) {
                for (Drug drug :
                        drugsList) {
                    getDrugRestDetails(drug);
                }
                return drugsList;
            }
        }
        return null;

    }

    private List<MedicalFamilyHistoryDetails> getMedicalFamilyHistoryListHavingIds(Class<?> class1, ArrayList<String> idsList) {
        if (!Util.isNullOrEmptyList(idsList)) {
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(class1.getSimpleName())
                    + getWhereConditionForKeyWithValues(LocalDatabaseUtils.KEY_UNIQUE_ID, idsList);

            //specifying order by limit and offset query
            String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_CREATED_TIME + " DESC ";

            whereCondition = whereCondition + conditionsLimit;
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            return SugarRecord.findWithQuery(MedicalFamilyHistoryDetails.class, whereCondition);
        }
        return null;
    }

    private String getWhereConditionForKeyWithValues(String columnName, ArrayList<String> valuesList) {
        String whereCondition = " where " + columnName + " IN (";
        for (Object value :
                valuesList) {
            int index = valuesList.indexOf(value);
            whereCondition = whereCondition + "\"" + value + "\"";
            if (index < valuesList.size() - 1)
                whereCondition = whereCondition + ",";
            else
                whereCondition = whereCondition + ")";

        }
        return whereCondition;
    }

    private ArrayList<String> addMedicalFamilyHistoryDetails(List<MedicalFamilyHistoryDetails> medicalHistoryResponse) {
        ArrayList<String> idsList = new ArrayList<>();
        for (MedicalFamilyHistoryDetails details : medicalHistoryResponse) {
            idsList.add(details.getUniqueId());
        }
        MedicalFamilyHistoryDetails.saveInTx(medicalHistoryResponse);
        return idsList;
    }

    public void addHistoryDetailResponse(HistoryDetailsResponse history) {
        history.setCustomUniqueId(history.getPatientId() + history.getLocationId() + history.getHospitalId());
        //setting medicalHistory list
        if (!Util.isNullOrEmptyList(history.getMedicalhistory())) {
            history.setMedicalHistoryIdsJsonString(getJsonFromObject(addMedicalFamilyHistoryDetails(history.getMedicalhistory())));
        } else
            history.setMedicalHistoryIdsJsonString(null);

        //setting FamilyHistory List
        if (!Util.isNullOrEmptyList(history.getFamilyhistory())) {
            history.setFamilyHistoryIdsJsonString(getJsonFromObject(addMedicalFamilyHistoryDetails(history.getFamilyhistory())));
        } else
            history.setMedicalHistoryIdsJsonString(null);

        //setting personal history
        history.setPersonalHistoryJsonString(getJsonFromObject(history.getPersonalHistory()));
        //setting drugs and Allergies
        addDrugsAndAllergies(history.getPatientId(), history.getDrugsAndAllergies());
        //setting special notes
        history.setSpecialNotesJsonString(getJsonFromObject(history.getSpecialNotes()));
        history.save();
    }

    private void addDrugsAndAllergies(String patientId, DrugsAndAllergies drugsAndAllergies) {
        deleteAllFrom(DrugsAndAllergies.class, LocalDatabaseUtils.KEY_PATIENT_ID, patientId);
        if (drugsAndAllergies != null) {
            if (!Util.isNullOrEmptyList(drugsAndAllergies.getDrugs())) {
                ArrayList<String> drugIdsList = new ArrayList<>();
                for (Drug drug :
                        drugsAndAllergies.getDrugs()) {
                    drugIdsList.add(drug.getUniqueId());
                    addDrug(drug);
                }
                drugsAndAllergies.setDrugIdsJsonString(getJsonFromObject(drugIdsList));
            }
            drugsAndAllergies.setPatientId(patientId);
            drugsAndAllergies.save();
        }
    }

    public void addPrescription(Prescription prescription) {
        List<DrugItem> drugItemsList = prescription.getItems();
        addDrugItemsList(drugItemsList, FromTableType.ADD_PRESCRIPTION, prescription.getUniqueId());
        addDiagnosticTestsPrescription(prescription, prescription.getDiagnosticTests());
        if (prescription.getAppointmentRequest() != null)
            addAppointmentRequest(prescription.getAppointmentRequest());
        prescription.save();
    }

    public void addRecord(Records records) {
        records.save();
    }

    public void addClinicalNote(ClinicalNotes clinicalNote) {
        deleteClinicalNotesAndRelatedData(clinicalNote.getUniqueId());
        if (!Util.isNullOrEmptyList(clinicalNote.getDiagnoses()))
            addDiagnosisList(clinicalNote.getUniqueId(), clinicalNote.getDiagnoses());
        if (!Util.isNullOrEmptyList(clinicalNote.getDiagrams()))
            addDiagramsList(clinicalNote.getUniqueId(), clinicalNote.getDiagrams());
        if (clinicalNote.getVitalSigns() != null) {
            addVitalSigns(clinicalNote.getUniqueId(), clinicalNote.getVitalSigns());
        }
        if (clinicalNote.getAppointmentRequest() != null)
            addAppointmentRequest(clinicalNote.getAppointmentRequest());
        clinicalNote.save();
    }

    private void addVitalSigns(String uniqueId, VitalSigns vitalSigns) {
        vitalSigns.setForeignTableId(uniqueId);
        BloodPressure bloodPressure = vitalSigns.getBloodPressure();
        deleteAllFrom(BloodPressure.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, vitalSigns.getForeignTableId());
        if (bloodPressure != null) {
            bloodPressure.setForeignTableId(vitalSigns.getForeignTableId());
            bloodPressure.save();
        }
        vitalSigns.save();
    }

    private void deleteFromHistoryIfPresent(String customId) {
        HistoryDetailsResponse.deleteAll(HistoryDetailsResponse.class, LocalDatabaseUtils.KEY_CUSTOM_UNIQUE_ID + "= ?", customId);
    }

    public void addDiseaseList(ArrayList<Disease> diseaseList) {
        for (Disease disease : diseaseList) {
            LogUtils.LOGD(TAG, "Unique ID : " +
                    disease.getUniqueId() + " Name : " + disease.getDisease() + " Disease : " + disease.getDoctorId());
        }
        Disease.saveInTx(diseaseList);
    }

    public long getLastGeneratedOtpTime(String patientId) {
        GeneratedOtpTime generatedOtpTime = Select.from(GeneratedOtpTime.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(patientId)).first();
        if (generatedOtpTime != null)
            return generatedOtpTime.getLastGeneratedTime();
        return 0;
    }

    public void addUpdateGeneratedOtpTime(String patientId) {
        GeneratedOtpTime generatedOtpTime = new GeneratedOtpTime();
        generatedOtpTime.setPatientId(patientId);
        generatedOtpTime.setLastGeneratedTime(new Date().getTime());
        generatedOtpTime.save();
    }

    private void addDrugItemsList(List<DrugItem> drugItemsList, FromTableType tableType, String prescriptionTemplateId) {
        String foreignTableKey = null;
        switch (tableType) {
            case ADD_PRESCRIPTION:
                foreignTableKey = LocalDatabaseUtils.KEY_FOREIGN_PRESCRIPTION_ID;
                break;
            case ADD_TEMPLATES:
                foreignTableKey = LocalDatabaseUtils.KEY_FOREIGN_TEMPLATE_ID;
                break;
        }
        //delete rest drugs before saving
        deletePreviousDrugs(foreignTableKey, prescriptionTemplateId);
        //        delete directions for that template/prescription id before saving
        deleteAllFrom(LinkedTableDirection.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, prescriptionTemplateId);
        if (!Util.isNullOrEmptyList(drugItemsList)) {
            for (DrugItem drugItem :
                    drugItemsList) {
//                //saving duration
//                Duration duration = drugItem.getDuration();
//                if (duration != null && duration.getDurationUnit() != null) {
//                    //saving duration Unit
//                    DrugDurationUnit durationUnit = duration.getDurationUnit();
//                    duration.setForeignDrugDurationUnit(durationUnit.getUniqueId());
//                    durationUnit.save();
//                    duration.setDiagnosticTestId(prescriptionTemplateId);
//                    drugItem.setForeignDurationId(duration.getCustomUniqueId());
//                    duration.save();
//                }
                drugItem.setDurationJsonString(getJsonFromObject(drugItem.getDuration()));
                drugItem.setDirectionJsonString(getJsonFromObject(drugItem.getDirection()));
                if (drugItem.getDrug() != null) {
                    Drug drug = drugItem.getDrug();
                    drugItem.setDrugId(drug.getUniqueId());
                    addDrug(drug);
                }
////            adding directions
//                if (!Util.isNullOrEmptyList(drugItem.getDirection()) && !Util.isNullOrBlank(foreignTableKey)) {
//                    addLinkedDirectionsList(foreignTableKey, prescriptionTemplateId, drugItem.getDrugId(), drugItem.getDirection());
//                }
                drugItem.setForeignTableKey(foreignTableKey);
                drugItem.setForeignTableId(prescriptionTemplateId);
                drugItem.setCustomUniqueId(drugItem.getForeignTableKey() + drugItem.getForeignTableId() + drugItem.getDrugId());
                drugItem.save();
            }
        }
    }

    private void deletePreviousDrugs(String foreignTableKey, String foreignTableId) {
        String query = "Select  * from " + DrugItem.TABLE_NAME
                + " where " + DrugItem.TABLE_NAME + "." + LocalDatabaseUtils.KEY_FOREIGN_TABLE_KEY + " = " + "\"" + foreignTableKey + "\""
                + " AND " + DrugItem.TABLE_NAME + "." + LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID + " = " + "\"" + foreignTableId + "\"";
        List<DrugItem> list = SugarRecord.findWithQuery(DrugItem.class, query);
        if (!Util.isNullOrEmptyList(list))
            DrugItem.deleteInTx(list);
    }

    public void addDrug(Drug drug) {
        if (drug.getDrugType() != null) {
            if (!Util.isNullOrEmptyList(drug.getGenericNames()))
                drug.setGenericNamesJsonString(new Gson().toJson(drug.getGenericNames()));
            drug.setForeignDrugTypeId(drug.getDrugType().getUniqueId());
            drug.getDrugType().save();
        }
        drug.save();
    }

    private void addLinkedDirectionsList(String foreignTableKey, String foreignTableId, String drugId, List<DrugDirection> directionsList) {
        for (DrugDirection direction :
                directionsList) {
            LinkedTableDirection linkedTableDirection = new LinkedTableDirection(foreignTableId, foreignTableKey, drugId, direction.getUniqueId());
            linkedTableDirection.save();
            direction.save();
        }
    }

    private void addDiagnosticTestsPrescription(Prescription prescription, List<DiagnosticTestsPrescription> diagnosticTestsPrescriptionsList) {
        ArrayList<String> diagnosticIdsList = null;
        ArrayList<DiagnosticTest> diagnosticTestsList = new ArrayList<>();
        if (!Util.isNullOrEmptyList(diagnosticTestsPrescriptionsList)) {
            diagnosticIdsList = new ArrayList<>();
            for (DiagnosticTestsPrescription diagnosticTest :
                    diagnosticTestsPrescriptionsList) {
                if (!Util.isNullOrBlank(diagnosticTest.getTest().getUniqueId())) {
                    diagnosticTest.setDiagnosticTestId(diagnosticTest.getTest().getUniqueId());
                    diagnosticTest.setDiagnosticTestJsonString(getJsonFromObject(diagnosticTest.getTest()));

                    diagnosticTestsList.add(diagnosticTest.getTest());
                    diagnosticIdsList.add(diagnosticTest.getDiagnosticTestId());
                }
            }
            DiagnosticTest.saveInTx(diagnosticTestsList);
            DiagnosticTestsPrescription.saveInTx(diagnosticTestsPrescriptionsList);
        }
        prescription.setDiagnosticTestsIdsJsonString(getJsonFromObject(diagnosticIdsList));
    }

    private void deleteClinicalNotesAndRelatedData(String clinicalNoteId) {
        VitalSigns.deleteAll(VitalSigns.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID + "= ?", clinicalNoteId);
        ClinicalNotes.deleteAll(ClinicalNotes.class, LocalDatabaseUtils.KEY_UNIQUE_ID + "= ?", clinicalNoteId);
        ForeignComplaintsTable.deleteAll(ForeignComplaintsTable.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID + "= ?", clinicalNoteId);
        ForeignObservationsTable.deleteAll(ForeignObservationsTable.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID + "= ?", clinicalNoteId);
        ForeignInvestigationsTable.deleteAll(ForeignInvestigationsTable.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID + "= ?", clinicalNoteId);
        ForeignDiagnosesTable.deleteAll(ForeignDiagnosesTable.class, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID + "= ?", clinicalNoteId);
        Notes.deleteAll(Notes.class, LocalDatabaseUtils.KEY_FOREIGN_CLINICAL_NOTES_ID + "= ?", clinicalNoteId);
        Diagram.deleteAll(Diagram.class, LocalDatabaseUtils.KEY_FOREIGN_CLINICAL_NOTES_ID + "= ?", clinicalNoteId);
    }

    public void addDiagnosisList(String clinicalNoteId, List<Diagnoses> list) {
        try {
            for (Diagnoses diagnoses : list) {
                ForeignDiagnosesTable foreignDiagnosesTable = new ForeignDiagnosesTable(clinicalNoteId, diagnoses.getUniqueId());
                foreignDiagnosesTable.save();
                diagnoses.save();
            }
        } catch (Exception e) {
            Log.i(TAG, "Error in saving in transaction " + e.getMessage());
        }
    }

    public void addDiagramsList(String clinicalNoteId, List<Diagram> list) {
        try {
            for (Diagram diagram : list) {
                diagram.setForeignClinicalNotesId(clinicalNoteId);
                if (Util.isNullOrBlank(clinicalNoteId))
                    diagram.setRecordType(RecordType.GLOBAL);
                addDiagram(diagram);
            }
        } catch (Exception e) {
            Log.i(TAG, "Error in saving in transaction " + e.getMessage());
        }
    }

    public void addDiagram(Diagram diagram) {
        diagram.setCustomUniqueId(diagram.getDoctorId() + diagram.getUniqueId());
        diagram.save();
    }

    public Duration getDefaultDuration() {
        Duration duration = new Duration();
        duration.setDurationUnit(getDefaultDurationUnit());
        return duration;
    }

    private DrugDurationUnit getDefaultDurationUnit() {
        String query = "Select  " + LocalDatabaseUtils.getPrefixedColumnsString(DrugDurationUnit.class, true)
                + " from " + DrugDurationUnit.TABLE_NAME
                + " where "
                + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_UNIT, "day");
        LogUtils.LOGD(TAG, "Select query : " + query);
        DrugDurationUnit durationUnit = SugarRecord.findObjectWithQuery(DrugDurationUnit.class, query);
        return durationUnit;
    }

    public DrugDurationUnit getDurationUnit(String uniqueId) {
        String query = "Select  " + LocalDatabaseUtils.getPrefixedColumnsString(DrugDurationUnit.class, true)
                + " from " + DrugDurationUnit.TABLE_NAME
                + " where " + DrugDurationUnit.TABLE_NAME + "." + LocalDatabaseUtils.KEY_UNIQUE_ID + " = " + "\"" + uniqueId + "\"";
        LogUtils.LOGD(TAG, "Select query : " + query);
        DrugDurationUnit durationUnit = SugarRecord.findObjectWithQuery(DrugDurationUnit.class, query);
        return durationUnit;
    }

    public DrugDirection getDrugDirection(String uniqueId) {
        return Select.from(DrugDirection.class).where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(uniqueId))
                .first();
    }

    public Drug getDrug(String drugId) {
        String query = "Select  * "
                + " from " + Drug.TABLE_NAME
                + " where " + Drug.TABLE_NAME + "." + LocalDatabaseUtils.KEY_UNIQUE_ID + " = " + "\"" + drugId + "\"";
        LogUtils.LOGD(TAG, "Select query : " + query);
        Drug drug = SugarRecord.findObjectWithQuery(Drug.class, query);
        if (drug != null)
            getDrugRestDetails(drug);
        return drug;
    }

    private void getDrugRestDetails(Drug drug) {
//        if (!Util.isNullOrBlank(drug.getGenericNamesJsonString()))
//            drug.setGenericNames((ArrayList<GenericName>) (Object) getObjectsListFronJson(drug.getGenericNamesJsonString()));

        if (drug != null)
            drug.setDrugType(getDrugType(drug.getForeignDrugTypeId()));
    }

    private DrugType getDrugType(String foreignDrugTypeId) {
        String query = "Select  " + LocalDatabaseUtils.getPrefixedColumnsString(DrugType.class, true)
                + " from " + DrugType.TABLE_NAME
                + " where " + DrugType.TABLE_NAME + "." + LocalDatabaseUtils.KEY_UNIQUE_ID + " = " + "\"" + foreignDrugTypeId + "\"";
        LogUtils.LOGD(TAG, "Select query : " + query);
        DrugType drugType = SugarRecord.findObjectWithQuery(DrugType.class, query);
        return drugType;
    }

    public VolleyResponseBean getVisitsList(WebServiceType webServiceType, String doctorId, String locationId, String hospitalId, String patientId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            Select<VisitDetails> selectQuery = Select.from(VisitDetails.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId), Condition.prop(LocalDatabaseUtils.KEY_HOSPITAL_ID).eq(hospitalId), Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(locationId), Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(patientId));
            List<VisitDetails> list = selectQuery.list();
            if (!Util.isNullOrEmptyList(list)) {
                for (VisitDetails details :
                        list) {
                    LogUtils.LOGD(TAG, "Visit get details size " + list.size() + " Unique Id : " + details.getUniqueId());
                    details = getVisitDetailsAndTypes(details);
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

    private VisitDetails getVisitDetailsAndTypes(VisitDetails details) {
        details.setVisitedFor(getVisitedFor(details.getUniqueId()));
        List<VisitedForType> visitedForTypesList = details.getVisitedFor();
        if (!Util.isNullOrEmptyList(visitedForTypesList)) {
            for (VisitedForType type :
                    visitedForTypesList) {
                LogUtils.LOGD(TAG, "Visit get type size " + visitedForTypesList.size() + " Type : " + type);
                switch (type) {
                    case PRESCRIPTION:
                        details.setPrescriptions(getVisitedPrescriptionsList(details.getPatientId(), details.getUniqueId()));
                        break;
                    case REPORTS:
                        details.setRecords(getVisitedRecordsList(details.getPatientId(), details.getUniqueId()));
                        break;
                    case CLINICAL_NOTES:
                        details.setClinicalNotes(getVisitedClinicalNotesList(details.getPatientId(), details.getUniqueId()));
                        break;
                }
            }
        }
        details.setAppointmentRequest(getAppointmentRequest(details.getUniqueId()));
        return details;
    }

    private AppointmentRequest getAppointmentRequest(String visitId) {
        String query = "Select  * from " + StringUtil.toSQLName(AppointmentRequest.class.getSimpleName())
                + " where "
                + LocalDatabaseUtils.KEY_VISIT_ID + "=\"" + visitId + "\"";
        LogUtils.LOGD(TAG, "Select Query " + query);
        AppointmentRequest appointmentRequest = SugarRecord.findObjectWithQuery(AppointmentRequest.class, query);
        if (appointmentRequest != null)
            appointmentRequest.setTime((WorkingHours) getObject(WorkingHours.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, appointmentRequest.getUniqueId()));
        return appointmentRequest;
    }

    private List<ClinicalNotes> getVisitedClinicalNotesList(String patientId, String visitId) {
        Select<ClinicalNotes> selectQuery = Select.from(ClinicalNotes.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(patientId), Condition.prop(LocalDatabaseUtils.KEY_VISIT_ID).eq(visitId));
        List<ClinicalNotes> list = selectQuery.list();
        if (!Util.isNullOrEmptyList(list))
            for (ClinicalNotes clinicalNote : list) {
                getClinicalNoteDetailsList(clinicalNote);
            }
        return list;
    }

    private List<Records> getVisitedRecordsList(String patientId, String visitId) {
        Select<Records> selectQuery = Select.from(Records.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(patientId), Condition.prop(LocalDatabaseUtils.KEY_VISIT_ID).eq(visitId));
        return selectQuery.list();
    }

    private List<Prescription> getVisitedPrescriptionsList(String patientId, String visitId) {
        Select<Prescription> selectQuery = Select.from(Prescription.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(patientId), Condition.prop(LocalDatabaseUtils.KEY_VISIT_ID).eq(visitId));
        List<Prescription> list = selectQuery.list();
        if (!Util.isNullOrEmptyList(list))
            for (Prescription prescription : list) {
                getPrescriptionDetail(prescription);
            }
        return list;
    }

    private List<VisitedForType> getVisitedFor(String visitId) {
        List<VisitedForTypeTable> visitForTableList = Select.from(VisitedForTypeTable.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_VISIT_ID).eq(visitId)).list();
        ArrayList<VisitedForType> visitedForTypesList = new ArrayList<>();
        if (!Util.isNullOrEmptyList(visitForTableList)) {
            for (VisitedForTypeTable visitedForTypeTable :
                    visitForTableList) {
                visitedForTypesList.add(visitedForTypeTable.getVisitedForType());
            }
        }
        return visitedForTypesList;
    }

    private void getPrescriptionDetail(Prescription prescription) {
        prescription.setItems(getDrugItemsList(LocalDatabaseUtils.KEY_FOREIGN_PRESCRIPTION_ID, prescription.getUniqueId()));
        prescription.setDiagnosticTests(getDiagnosticTestsList(LocalDatabaseUtils.KEY_DIAGNOSTIC_TEST_ID, (ArrayList<String>) (Object) getObjectsListFronJson(prescription.getDiagnosticTestsIdsJsonString())));
        prescription.setAppointmentRequest(getAppointmentRequest(prescription.getVisitId()));
    }

    private List<DiagnosticTestsPrescription> getDiagnosticTestsList(String key, ArrayList<String> diagnosticIdsList) {
        List<DiagnosticTestsPrescription> list = null;
        if (!Util.isNullOrEmptyList(diagnosticIdsList)) {
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(DiagnosticTestsPrescription.class.getSimpleName())
                    + getWhereConditionForKeyWithValues(key, diagnosticIdsList);
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            list = SugarRecord.findWithQuery(DiagnosticTestsPrescription.class, whereCondition);
            if (!Util.isNullOrEmptyList(list)) {
                for (DiagnosticTestsPrescription diagnosticTestsPrescription :
                        list) {
                    diagnosticTestsPrescription.setTest((DiagnosticTest) getObjectFromJson(DiagnosticTest.class, diagnosticTestsPrescription.getDiagnosticTestJsonString()));
                }
                return list;
            }
        }
        return null;
    }

    private void getClinicalNoteDetailsList(ClinicalNotes clinicalNote) {
        clinicalNote.setDiagnoses((List<Diagnoses>) getClinicalNotesDataListFromForeignTable(ForeignDiagnosesTable.class, clinicalNote.getUniqueId(), Diagnoses.class));
        clinicalNote.setDiagrams((List<Diagram>) getListByKeyValue(Diagram.class, LocalDatabaseUtils.KEY_FOREIGN_CLINICAL_NOTES_ID, clinicalNote.getUniqueId()));
        clinicalNote.setVitalSigns(getVitalSigns(clinicalNote.getUniqueId()));
        clinicalNote.setAppointmentRequest(getAppointmentRequest(clinicalNote.getVisitId()));
    }

    private List<?> getClinicalNotesDataListFromForeignTable(Class<?> foreignTableClass, String foreignUniqueId, Class<?> dataTable) {
        List<Object> dataTablesList = null;
        List<?> foreignTablesDataList = getObjectsList(foreignTableClass, LocalDatabaseUtils.KEY_FOREIGN_UNIQUE_ID, foreignUniqueId);
        if (!Util.isNullOrEmptyList(foreignTablesDataList)) {
            String key = "";
            String value = "";
            dataTablesList = new ArrayList<>();
            for (Object object :
                    foreignTablesDataList) {
                if (object instanceof ForeignComplaintsTable) {
                    ForeignComplaintsTable foreignComplaintsTable = (ForeignComplaintsTable) object;
                    key = LocalDatabaseUtils.KEY_UNIQUE_ID;
                    value = foreignComplaintsTable.getComplaintsId();
                } else if (object instanceof ForeignObservationsTable) {
                    ForeignObservationsTable foreignObservationsTable = (ForeignObservationsTable) object;
                    key = LocalDatabaseUtils.KEY_UNIQUE_ID;
                    value = foreignObservationsTable.getObservationId();
                } else if (object instanceof ForeignInvestigationsTable) {
                    ForeignInvestigationsTable foreignInvestigationsTable = (ForeignInvestigationsTable) object;
                    key = LocalDatabaseUtils.KEY_UNIQUE_ID;
                    value = foreignInvestigationsTable.getInvestigationId();
                } else if (object instanceof ForeignDiagnosesTable) {
                    ForeignDiagnosesTable foreignDiagnosesTable = (ForeignDiagnosesTable) object;
                    key = LocalDatabaseUtils.KEY_UNIQUE_ID;
                    value = foreignDiagnosesTable.getDiagnosesId();
                }
                if (!Util.isNullOrBlank(key) && !Util.isNullOrBlank(value))
                    dataTablesList.add(getObject(dataTable, key, value));
            }

        }
        return dataTablesList;
    }

    private List<?> getObjectsList(Class<?> class1, String key, String value) {
        return Select.from(class1).where(Condition.prop(key).eq(value)).list();
    }

    private DiagnosticTest getDiagnosticTest(String uniqueId) {
        return (DiagnosticTest) getObject(DiagnosticTest.class, LocalDatabaseUtils.KEY_UNIQUE_ID, uniqueId);
    }

    private VitalSigns getVitalSigns(String uniqueId) {
        String query = "Select  " + LocalDatabaseUtils.getPrefixedColumnsString(VitalSigns.class, true)
                + " from " + VitalSigns.TABLE_NAME
                + " left outer join " + BloodPressure.TABLE_NAME
                + " on " + VitalSigns.TABLE_NAME + "." + LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID + "=" + BloodPressure.TABLE_NAME + "." + LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID
                + " where " + VitalSigns.TABLE_NAME + "." + LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID + " = " + "\"" + uniqueId + "\"";
        LogUtils.LOGD(TAG, "Select query : " + query);
        VitalSigns vitalSigns = SugarRecord.findObjectWithQuery(VitalSigns.class, query);
        if (vitalSigns != null)
            vitalSigns.setBloodPressure(getBloodPressure(uniqueId));
        return vitalSigns;
    }

    private BloodPressure getBloodPressure(String uniqueId) {
        String query = "Select  " + LocalDatabaseUtils.getPrefixedColumnsString(BloodPressure.class, true)
                + " from " + BloodPressure.TABLE_NAME
                + " where " + BloodPressure.TABLE_NAME + "." + LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID + " = " + "\"" + uniqueId + "\"";
        LogUtils.LOGD(TAG, "Select query : " + query);
        return SugarRecord.findObjectWithQuery(BloodPressure.class, query);
    }

    public ClinicalNotes getClinicalNote(String clinicalNoteId, String patientId) {
        return getClinicalNote(null, clinicalNoteId, patientId);
    }

    public ClinicalNotes getClinicalNote(BooleanTypeValues discarded, String clinicalNoteId, String patientId) {
        Select<ClinicalNotes> selectQuery;
        if (discarded != null) {
            selectQuery = Select.from(ClinicalNotes.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(clinicalNoteId),
                            Condition.prop(LocalDatabaseUtils.KEY_DISCARDED).eq(discarded.getBooleanIntValue()),
                            Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(patientId));
        } else
            selectQuery = Select.from(ClinicalNotes.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(clinicalNoteId),
                            Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(patientId));
        ClinicalNotes clinicalNote = selectQuery.first();
        if (clinicalNote != null) {
            getClinicalNoteDetailsList(clinicalNote);
        }
        return clinicalNote;
    }

    public List<DrugItem> getDrugItemsList(String foreignKey, String uniqueId) {
        List<DrugItem> list = new ArrayList<>();
        String query = "Select  * from " + DrugItem.TABLE_NAME
                + " where " + DrugItem.TABLE_NAME + "." + LocalDatabaseUtils.KEY_FOREIGN_TABLE_KEY + " = " + "\"" + foreignKey + "\""
                + " AND " + DrugItem.TABLE_NAME + "." + LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID + " = " + "\"" + uniqueId + "\"";
        LogUtils.LOGD(TAG, "Select query : " + query);
        list = SugarRecord.findWithQuery(DrugItem.class, query);
        if (!Util.isNullOrEmptyList(list)) {
            for (DrugItem drugItem : list) {
                drugItem.setDrug(getDrug(drugItem.getDrugId()));
                drugItem.setDuration((Duration) getObjectFromJson(Duration.class, drugItem.getDurationJsonString()));
                drugItem.setDirection((ArrayList<DrugDirection>) getObjectsListFronJson(DrugDirection.class, drugItem.getDirectionJsonString()));
            }
        }
        return list;
    }

    private List<DrugDirection> getDirectionsForDrugsList(String key, String prescriptionTemplateId, String drugId) {
        List<DrugDirection> list = new ArrayList<>();
        List<LinkedTableDirection> listLinkedData = new ArrayList<>();
        String query = "Select  " + LocalDatabaseUtils.getPrefixedColumnsString(LinkedTableDirection.class, true)
                + " from " + LinkedTableDirection.TABLE_NAME
                + " where " + LinkedTableDirection.TABLE_NAME + "." + LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID + " = " + "\"" + prescriptionTemplateId + "\""
                + " AND " + LocalDatabaseUtils.KEY_FOREIGN_TABLE_KEY + "=\"" + key + "\""
                + " AND " + LocalDatabaseUtils.KEY_UNIQUE_ID + "=\"" + drugId + "\"";
        LogUtils.LOGD(TAG, "Select query : " + query);
        listLinkedData = SugarRecord.findWithQuery(LinkedTableDirection.class, query);
        if (!Util.isNullOrEmptyList(listLinkedData)) {
            for (LinkedTableDirection linkedTableDirection : listLinkedData) {
                DrugDirection direction = getDrugDirection(linkedTableDirection.getDirectionId());
                if (direction != null)
                    list.add(direction);
            }
        }
        return list;
    }

    public void addVisitsList(ArrayList<VisitDetails> visitsList) {
        for (VisitDetails details :
                visitsList) {
            LogUtils.LOGD(TAG, "Visit add detail size " + visitsList.size() + " UniqueId : " + details.getUniqueId());
            addVisit(details);
        }
    }

    public void addVisit(VisitDetails details) {
        try {
            deleteVisitDetails(details);
            List<VisitedForType> visitForTypesList = details.getVisitedFor();
            if (!Util.isNullOrEmptyList(visitForTypesList)) {
                for (VisitedForType type :
                        visitForTypesList) {
                    LogUtils.LOGD(TAG, "Visit add type size : " + visitForTypesList.size() + " Type : " + type);
                    String customUniqueId = "";
                    switch (type) {
                        case PRESCRIPTION:
                            if (!Util.isNullOrEmptyList(details.getPrescriptions())) {
                                for (Prescription prescription :
                                        details.getPrescriptions()) {
                                    customUniqueId = prescription.getUniqueId();
                                    addPrescription(prescription);
                                }
                            } else
                                continue;
                            break;
                        case REPORTS:
                            if (!Util.isNullOrEmptyList(details.getRecords())) {
                                for (Records record :
                                        details.getRecords()) {
                                    customUniqueId = record.getUniqueId();
                                    addRecord(record);
                                }
                            } else
                                continue;
                            break;
                        case CLINICAL_NOTES:
                            if (!Util.isNullOrEmptyList(details.getClinicalNotes())) {
                                for (ClinicalNotes note :
                                        details.getClinicalNotes()) {
                                    customUniqueId = note.getUniqueId();
                                    note.setPatientId(details.getPatientId());
                                    addClinicalNote(note);
                                }
                            } else
                                continue;
                            break;
                        case TREATMENT:
                            if (!Util.isNullOrEmptyList(details.getPatientTreatments())) {
                                for (PatientTreatment patientTreatment :
                                        details.getPatientTreatments()) {
                                    customUniqueId = patientTreatment.getUniqueId();
                                    addTreatment(patientTreatment);
                                }
                            } else
                                continue;
                            break;
                    }
                    addVisitForTable(details.getUniqueId(), type, customUniqueId);
                }
                if (details.getAppointmentRequest() != null)
                    addAppointmentRequest(details.getAppointmentRequest());
                details.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAppointmentRequest(AppointmentRequest appointmentRequest) {
        addWorkingHour(AppointmentRequest.class.getSimpleName(), appointmentRequest.getUniqueId(), appointmentRequest.getTime());
        appointmentRequest.save();
    }

    private void deleteVisitDetails(VisitDetails visitDetails) {
        SugarRecord.deleteAll(VisitDetails.class, LocalDatabaseUtils.KEY_UNIQUE_ID + "= ?", visitDetails.getUniqueId());
        SugarRecord.deleteAll(Prescription.class, LocalDatabaseUtils.KEY_PATIENT_ID + "= ? AND " + LocalDatabaseUtils.KEY_VISIT_ID + "= ?",
                new String[]{visitDetails.getPatientId(), visitDetails.getUniqueId()});
        SugarRecord.deleteAll(ClinicalNotes.class, LocalDatabaseUtils.KEY_PATIENT_ID + "= ? AND " + LocalDatabaseUtils.KEY_VISIT_ID + "= ?",
                new String[]{visitDetails.getPatientId(), visitDetails.getUniqueId()});
    }

    private void addTreatment(PatientTreatment patientTreatment) {
        List<Treatments> treatmentsList = patientTreatment.getTreatments();
//        addTreatmentList(treatmentsList, FromTableType.ADD_TREATMENT, patientTreatment.getUniqueId());
        patientTreatment.save();
    }

    private void addVisitForTable(String visitId, VisitedForType type, String customUniqueId) {
        VisitedForTypeTable visitedForTypeTable = new VisitedForTypeTable();
        visitedForTypeTable.setVisitedForType(type);
        visitedForTypeTable.setVisitId(visitId);
        visitedForTypeTable.setCustomUniqueId(customUniqueId);
        visitedForTypeTable.save();
    }

    public VolleyResponseBean getVisitsListPageWise(WebServiceType webServiceType, String doctorId, String locationId, String hospitalId,
                                                    String patientId, int pageNum, int maxSize,
                                                    Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(VisitDetails.class.getSimpleName())
                    + " where "
                    + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"" + doctorId + "\""
                    + " AND "
                    + LocalDatabaseUtils.KEY_LOCATION_ID + "=\"" + locationId + "\""
                    + " AND "
                    + LocalDatabaseUtils.KEY_HOSPITAL_ID + "=\"" + hospitalId + "\""
                    + " AND "
                    + LocalDatabaseUtils.KEY_PATIENT_ID + "=\"" + patientId + "\"";

            //specifying order by limit and offset query
            String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_VISITED_TIME + " DESC "
                    + " LIMIT " + maxSize
                    + " OFFSET " + (pageNum * maxSize);

            whereCondition = whereCondition + conditionsLimit;
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            LogUtils.LOGD(TAG, "Visit  pageNum : " + pageNum);
            List<VisitDetails> list = SugarRecord.findWithQuery(VisitDetails.class, whereCondition);
            if (!Util.isNullOrEmptyList(list)) {
                for (VisitDetails details :
                        list) {
                    LogUtils.LOGD(TAG, "Visit get details size " + list.size() + " pageNum : " + pageNum
                            + " Date : " + DateTimeUtil.getFormatedDate(details.getVisitedTime()));
                    details = getVisitDetailsAndTypes(details);
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

    public VolleyResponseBean getVisitDetailResponse(WebServiceType webServiceType, String selectedVisitId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            VisitDetails visitDetails = getVisit(selectedVisitId);
            volleyResponseBean.setData(visitDetails);
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public VisitDetails getVisit(String visitId) {
        VisitDetails visit = Select.from(VisitDetails.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(visitId)).first();
        if (visit != null)
            visit = getVisitDetailsAndTypes(visit);
        return visit;
    }

    public void addGCMRequest(GCMRequest gcmRequest) {
        deleteAllFrom(GCMRequest.class, LocalDatabaseUtils.KEY_DEVICE_ID, gcmRequest.getDeviceId());
        gcmRequest.setUserIdsJsonString(new Gson().toJson(gcmRequest.getUserIds()));
        gcmRequest.save();
    }

    public GCMRequest getGCMRequestData() {
        GCMRequest gcmRequest = GCMRequest.first(GCMRequest.class);
        if (gcmRequest != null && !Util.isNullOrBlank(gcmRequest.getUserIdsJsonString())) {
            gcmRequest.setUserIds(new Gson().fromJson(gcmRequest.getUserIdsJsonString(), ArrayList.class));
        }
        return gcmRequest;
    }

    public void addEmailAddress(String emailAddress, String patientId) {
        EmailAddress addressObj = new EmailAddress();
        addressObj.setEmailAddress(emailAddress);
        addressObj.setPatientId(patientId);
        addressObj.save();
    }

    public VolleyResponseBean getGlobalIncludedDosageDurationDirectionList(WebServiceType webServiceType, String doctorId, Boolean discarded, long updatedTime, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        Integer discardedValue = null;
        if (discarded != null) {
            if (discarded) {
                discardedValue = LocalDatabaseUtils.BOOLEAN_TRUE_VALUE;
                volleyResponseBean.setLocalBackgroundTaskType(LocalBackgroundTaskType.GET_DRUG_HIDDEN_LIST);
            } else {
                volleyResponseBean.setLocalBackgroundTaskType(LocalBackgroundTaskType.GET_DRUG_ACTIVATED_LIST);

                discardedValue = LocalDatabaseUtils.BOOLEAN_FALSE_VALUE;
            }
        }

        try {
            List<?> list = null;
            Class<?> class1 = null;
            String fieldName = "";
            switch (webServiceType) {
                case GET_DRUG_DOSAGE:
                    class1 = DrugDosage.class;
                    fieldName = LocalDatabaseUtils.KEY_DOSAGE;
                    break;
                case GET_DURATION_UNIT:
                    class1 = DrugDurationUnit.class;
                    fieldName = LocalDatabaseUtils.KEY_UNIT;
                    break;
                case GET_DIRECTION:
                    class1 = DrugDirection.class;
                    fieldName = LocalDatabaseUtils.KEY_DIRECTION;
                    break;
            }
            String whereCondition = "Select * from " + StringUtil.toSQLName(class1.getSimpleName()) + " where " +
                    "(" + LocalDatabaseUtils.KEY_DOCTOR_ID + " is null"
                    + " OR "
                    + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"" + doctorId + "\""
                    + ")"
                    + " AND "
                    + "(" + LocalDatabaseUtils.KEY_DISCARDED + " is null"
                    + " OR "
                    + LocalDatabaseUtils.KEY_DISCARDED + "=" + discardedValue
                    + ")"
                    + " AND "
                    + "(" + fieldName + " is NOT null"
                    + " AND "
                    + fieldName + "!=" + "\"\""
                    + ")";

            //specifying order by limit
            whereCondition = whereCondition + " ORDER BY " + LocalDatabaseUtils.KEY_DOCTOR_ID + " DESC ";

            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            list = SugarRecord.findWithQuery(class1, whereCondition);
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }

        return volleyResponseBean;
    }

    public void addTemplatesList(ArrayList<TempTemplate> templatesList) {
        if (!Util.isNullOrEmptyList(templatesList)) {
            for (TempTemplate template : templatesList) {
                addTemplate(template);
            }
        }
    }

    public void addTemplate(TempTemplate template) {
        if (!Util.isNullOrEmptyList(template.getItems())) {
            deleteAllFrom(TempTemplate.class, LocalDatabaseUtils.KEY_UNIQUE_ID, template.getUniqueId());
            addDrugItemsList(template.getItems(), FromTableType.ADD_TEMPLATES, template.getUniqueId());
            template.save();
        }
    }

    public void deleteTemplate(String templateId) {
        deleteAllFrom(TempTemplate.class, LocalDatabaseUtils.KEY_UNIQUE_ID, templateId);
        //delete rest drugs before saving
        deletePreviousDrugs(LocalDatabaseUtils.KEY_FOREIGN_TEMPLATE_ID, templateId);
//        delete directions for that template/prescription id before saving
        deleteAllFrom(LinkedTableDirection.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, templateId);
    }

    public TempTemplate getTemplate(String templateId) {
        TempTemplate template = Select.from(TempTemplate.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(templateId),
                        Condition.prop(LocalDatabaseUtils.KEY_DISCARDED).eq(LocalDatabaseUtils.BOOLEAN_FALSE_VALUE)).first();
        if (template != null)
            template.setItems(getDrugItemsList(LocalDatabaseUtils.KEY_FOREIGN_TEMPLATE_ID, template.getUniqueId()));
        return template;
    }

    public VolleyResponseBean getTemplatesListPageWise(WebServiceType webServiceType, String doctorId,
                                                       boolean discarded, Long updatedTime, int pageNum,
                                                       int maxSize, String searchTerm, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            int discardedValue = LocalDatabaseUtils.BOOLEAN_FALSE_VALUE;
            if (discarded)
                discardedValue = LocalDatabaseUtils.BOOLEAN_TRUE_VALUE;
            //forming where condition query
            String whereCondition = "Select * from " + StringUtil.toSQLName(TempTemplate.class.getSimpleName())
                    + " where "
                    + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"" + doctorId + "\""
                    + " AND "
                    + LocalDatabaseUtils.KEY_DISCARDED + "=" + discardedValue;
            if (!Util.isNullOrBlank(searchTerm))
                whereCondition = whereCondition
                        + " AND " + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(LocalDatabaseUtils.KEY_NAME, searchTerm);

            //specifying order by limit and offset query
            String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_CREATED_TIME + " DESC "
                    + " LIMIT " + maxSize
                    + " OFFSET " + (pageNum * maxSize);

            whereCondition = whereCondition + conditionsLimit;
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            List<TempTemplate> list = SugarRecord.findWithQuery(TempTemplate.class, whereCondition);
            if (!Util.isNullOrEmptyList(list))
                for (TempTemplate template : list) {
                    template.setItems(getDrugItemsList(LocalDatabaseUtils.KEY_FOREIGN_TEMPLATE_ID, template.getUniqueId()));
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

    public VolleyResponseBean getGlobalDiagramsList(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            String whereCondition = "Select * from " + StringUtil.toSQLName(Diagram.class.getSimpleName()) + " where " +
                    "(" + LocalDatabaseUtils.KEY_DOCTOR_ID + " is null"
                    + " OR "
                    + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"\""
                    + ")";
            LogUtils.LOGD(TAG, "Select Query " + whereCondition);
            List<Diagram> diagramsList = SugarRecord.findWithQuery(Diagram.class, whereCondition);
            volleyResponseBean.setDataList(getObjectsListFromMap(diagramsList));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public void clearAllTables(Context context) {
        try {
            List<Class> allClasses = getDomainClasses();
            if (!Util.isNullOrEmptyList(allClasses)) {
                for (Class class1 :
                        allClasses) {
                    if (!class1.equals(GCMRequest.class))
                        SugarRecord.deleteAll(class1);
                }
            }
        } catch (Exception e) {
            LogUtils.LOGD(TAG, "Error Package name not found ", e);
        }
    }

    public void addBothUserUiPermissions(UiPermissionsBoth uiPermissionsBoth) {
        if (uiPermissionsBoth != null) {
            addUiPermissions(uiPermissionsBoth.getDoctorId(), uiPermissionsBoth.getDoctorPermissions());
            addUiPermissions(uiPermissionsBoth.getDoctorId(), uiPermissionsBoth.getAllPermissions());
        }
    }

    public void addUiPermissions(String doctorId, Object object) {
        if (object instanceof AssignedUserUiPermissions) {
            AssignedUserUiPermissions assignedUserUiPermissions = (AssignedUserUiPermissions) object;
            assignedUserUiPermissions.setClinicalNotesPermissionsString(getJsonFromObject(assignedUserUiPermissions.getClinicalNotesPermissions()));
            assignedUserUiPermissions.setPatientVisitPermissionsString(getJsonFromObject(assignedUserUiPermissions.getPatientVisitPermissions()));
            assignedUserUiPermissions.setTabPermissionsString(getJsonFromObject(assignedUserUiPermissions.getTabPermissions()));
            assignedUserUiPermissions.setPrescriptionPermissionsString(getJsonFromObject(assignedUserUiPermissions.getPrescriptionPermissions()));
            assignedUserUiPermissions.setProfilePermissionsString(getJsonFromObject(assignedUserUiPermissions.getProfilePermissions()));
            assignedUserUiPermissions.setDoctorId(doctorId);
            assignedUserUiPermissions.save();
        } else if (object instanceof UIPermissions) {
            UIPermissions allUIPermissions = (UIPermissions) object;
            allUIPermissions.setClinicalNotesPermissionsString(getJsonFromObject(allUIPermissions.getClinicalNotesPermissions()));
            allUIPermissions.setPatientVisitPermissionsString(getJsonFromObject(allUIPermissions.getPatientVisitPermissions()));
            allUIPermissions.setTabPermissionsString(getJsonFromObject(allUIPermissions.getTabPermissions()));
            allUIPermissions.setPrescriptionPermissionsString(getJsonFromObject(allUIPermissions.getPrescriptionPermissions()));
            allUIPermissions.setProfilePermissionsString(getJsonFromObject(allUIPermissions.getProfilePermissions()));
            allUIPermissions.setDoctorId(doctorId);
            allUIPermissions.save();
        }
    }

    public VolleyResponseBean getBothUserUiPermissions(WebServiceType webServiceType, String doctorId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            volleyResponseBean.setData(getBothUserUiPermissions(doctorId));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    private UiPermissionsBoth getBothUserUiPermissions(String doctorId) {
        UiPermissionsBoth uiPermissionsBoth = new UiPermissionsBoth();
        uiPermissionsBoth.setDoctorPermissions(getAssignedUserUiPermissions(doctorId));
        uiPermissionsBoth.setAllPermissions(getAllUiPermissions(doctorId));
        uiPermissionsBoth.setDoctorId(doctorId);
        return uiPermissionsBoth;
    }

    private AssignedUserUiPermissions getAssignedUserUiPermissions(String doctorId) {
        AssignedUserUiPermissions assignedUserUiPermissions = Select.from(AssignedUserUiPermissions.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId)).first();
        if (assignedUserUiPermissions != null) {
            assignedUserUiPermissions.setClinicalNotesPermissions((ArrayList<String>) (Object) getObjectsListFronJson(assignedUserUiPermissions.getClinicalNotesPermissionsString()));
            assignedUserUiPermissions.setPatientVisitPermissions((ArrayList<String>) (Object) getObjectsListFronJson(assignedUserUiPermissions.getPatientVisitPermissionsString()));
            assignedUserUiPermissions.setTabPermissions((ArrayList<String>) (Object) getObjectsListFronJson(assignedUserUiPermissions.getTabPermissionsString()));
            assignedUserUiPermissions.setPrescriptionPermissions((ArrayList<String>) (Object) getObjectsListFronJson(assignedUserUiPermissions.getPrescriptionPermissionsString()));
            assignedUserUiPermissions.setProfilePermissions((ArrayList<String>) (Object) getObjectsListFronJson(assignedUserUiPermissions.getProfilePermissionsString()));
        }
        return assignedUserUiPermissions;
    }

    private UIPermissions getAllUiPermissions(String doctorId) {
        UIPermissions uiPermissions = Select.from(UIPermissions.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId)).first();
        if (uiPermissions != null) {
            uiPermissions.setClinicalNotesPermissions((ArrayList<String>) (Object) getObjectsListFronJson(uiPermissions.getClinicalNotesPermissionsString()));
            uiPermissions.setPatientVisitPermissions((ArrayList<String>) (Object) getObjectsListFronJson(uiPermissions.getPatientVisitPermissionsString()));
            uiPermissions.setTabPermissions((ArrayList<String>) (Object) getObjectsListFronJson(uiPermissions.getTabPermissionsString()));
            uiPermissions.setPrescriptionPermissions((ArrayList<String>) (Object) getObjectsListFronJson(uiPermissions.getPrescriptionPermissionsString()));
            uiPermissions.setProfilePermissions((ArrayList<String>) (Object) getObjectsListFronJson(uiPermissions.getProfilePermissionsString()));
        }
        return uiPermissions;
    }

    public VolleyResponseBean getSuggestionsListAsResponse(WebServiceType webServiceType, Class<?> class1, SuggestionType suggestionType, String searchTerm, int pageNum, int maxSize, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            volleyResponseBean.setDataList(getSuggestionsList(class1, suggestionType, searchTerm, pageNum, maxSize));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public ArrayList<Object> getSuggestionsList(Class<?> class1, SuggestionType suggestionType, String searchTerm, int pageNum, int maxSize) {
        //forming where condition query
        String whereCondition = "Select * from " + StringUtil.toSQLName(class1.getSimpleName());
        if (suggestionType != null && !Util.isNullOrBlank(searchTerm)) {
            String key = "";
            switch (suggestionType) {
                case PRESENT_COMPLAINT:
                    key = LocalDatabaseUtils.KEY_PRESENT_COMPLAINT;
                    break;
                case COMPLAINTS:
                    key = LocalDatabaseUtils.KEY_COMPLAINT;
                    break;
                case HISTORY_OF_PRESENT_COMPLAINT:
                    key = LocalDatabaseUtils.KEY_HISTORY_OF_PRESENT_COMPLAINT;
                    break;
                case MENSTRUAL_HISTORY:
                    key = LocalDatabaseUtils.KEY_MENSTRUAL_HISTORY;
                    break;
                case OBSTETRIC_HISTORY:
                    key = LocalDatabaseUtils.KEY_OBSTETRIC_HISTORY;
                    break;
                case GENERAL_EXAMINATION:
                    key = LocalDatabaseUtils.KEY_GENERAL_EXAMINATION;
                    break;
                case SYSTEMIC_EXAMINATION:
                    key = LocalDatabaseUtils.KEY_SYSTEMIC_EXAMINATION;
                    break;
                case OBSERVATION:
                    key = LocalDatabaseUtils.KEY_OBSERVATION;
                    break;
                case INVESTIGATION:
                    key = LocalDatabaseUtils.KEY_INVESTIGATION;
                    break;
                case PROVISIONAL_DIAGNOSIS:
                    key = LocalDatabaseUtils.KEY_PROVISIONAL_DIAGNOSIS;
                    break;
                case DIAGNOSIS:
                    key = LocalDatabaseUtils.KEY_DIAGNOSIS;
                    break;
                case NOTES:
                    key = LocalDatabaseUtils.KEY_NOTES;
                    break;
                case ECG_DETAILS:
                    key = LocalDatabaseUtils.KEY_ECG_DETAILS;
                    break;
                case ECHO:
                    key = LocalDatabaseUtils.KEY_ECHO;
                    break;
                case X_RAY_DETAILS:
                    key = LocalDatabaseUtils.KEY_X_RAY_DETAILS;
                    break;
                case HOLTER:
                    key = LocalDatabaseUtils.KEY_HOLTER;
                    break;
                case PA:
                    key = LocalDatabaseUtils.KEY_PA;
                    break;
                case PV:
                    key = LocalDatabaseUtils.KEY_PV;
                    break;
                case PS:
                    key = LocalDatabaseUtils.KEY_PS;
                    break;
                case INDICATION_OF_USG:
                    key = LocalDatabaseUtils.KEY_INDICATION_OF_USG;
                    break;
                case ADVICE:
                    key = LocalDatabaseUtils.KEY_ADVICE;
                    break;
                default:
                    break;
            }
            if (!Util.isNullOrBlank(key))
                whereCondition = whereCondition + " where "
                        + LocalDatabaseUtils.getSearchTermEqualsIgnoreCaseQuery(key, searchTerm) + " LIMIT " + maxSize
                        + " OFFSET " + (pageNum * maxSize);
        }

        LogUtils.LOGD(TAG, "Select Query " + whereCondition);
        ArrayList<Object> list = (ArrayList<Object>) (Object) SugarRecord.findWithQuery(class1, whereCondition);
        return list;
    }

    public VolleyResponseBean getMasterData(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        ArrayList<Object> list = null;
        try {
            switch (webServiceType) {
                case GET_EDUCATION_QUALIFICATION:
                    list = (ArrayList<Object>) (ArrayList<?>) EducationQualification.listAll(EducationQualification.class);
                    break;
                case GET_MEDICAL_COUNCILS:
                    list = (ArrayList<Object>) (ArrayList<?>) MedicalCouncil.listAll(MedicalCouncil.class);
                    break;
                case GET_COLLEGE_UNIVERSITY_INSTITUES:
                    list = (ArrayList<Object>) (ArrayList<?>) CollegeUniversityInstitute.listAll(CollegeUniversityInstitute.class);
                    break;
            }

            volleyResponseBean.setDataList(list);
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    private void deleteWorkingHoursIfAlreadyPresent(String key, String value) {
        WorkingHours.deleteAll(WorkingHours.class, key + "= ?", value);
    }

    public void addAppointmentsList(ArrayList<CalendarEvents> list) {
        if (!Util.isNullOrEmptyList(list))
            for (CalendarEvents appointment :
                    list) {
                addAppointment(appointment);
            }
    }

    public void addAppointment(CalendarEvents appointment) {
        deleteWorkingHoursIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, appointment.getUniqueId());
        if (appointment.getTime() != null) {
            addWorkingHour(CalendarEvents.class.getSimpleName(), appointment.getUniqueId(), appointment.getTime());
        }
        if (appointment.getPatient() != null) {
            appointment.setPatientId(appointment.getPatient().getUserId());
        }
        appointment.save();
    }

    public VolleyResponseBean getAppointmentsListResponsePageWise(WebServiceType webServiceType, boolean isOtpVerified, String doctorId, String hospitalId, String locationId,
                                                                  String selectedPatientId, int pageNum, int maxSize,
                                                                  Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            List<CalendarEvents> list = getAppointmentsListPageWise(isOtpVerified, doctorId, locationId, hospitalId, pageNum, maxSize, selectedPatientId);
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    private List<CalendarEvents> getAppointmentsListPageWise(boolean isOtpVerified, String doctorId, String locationId, String hospitalId, int pageNum, int maxSize, String selectedPatientId) {

        String whereCondition = "Select * from " + StringUtil.toSQLName(CalendarEvents.class.getSimpleName())
                + " where "
                + LocalDatabaseUtils.KEY_PATIENT_ID + "=\"" + selectedPatientId + "\"";
        whereCondition = whereCondition + " AND "
                + LocalDatabaseUtils.KEY_DOCTOR_ID + "=\"" + doctorId + "\""
                + " AND "
                + LocalDatabaseUtils.KEY_HOSPITAL_ID + "=\"" + hospitalId + "\""
                + " AND "
                + LocalDatabaseUtils.KEY_LOCATION_ID + "=\"" + locationId + "\"";
        String conditionsLimit = " ORDER BY " + LocalDatabaseUtils.KEY_FROM_DATE + " DESC "
                + " LIMIT " + maxSize
                + " OFFSET " + (pageNum * maxSize);

        whereCondition = whereCondition + conditionsLimit;
        LogUtils.LOGD(TAG, "Select Query " + whereCondition);
        List<CalendarEvents> list = SugarRecord.findWithQuery(CalendarEvents.class, whereCondition);
        if (!Util.isNullOrEmptyList(list)) {
            for (CalendarEvents appointment : list) {
                if (!Util.isNullOrBlank(appointment.getPatientId()))
                    getAppointDetail(appointment);
            }
            return list;
        }
        return null;
    }

    private void getAppointDetail(CalendarEvents appointment) {
        appointment.setTime((WorkingHours) getObject(WorkingHours.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, appointment.getUniqueId()));
        appointment.setPatient(getPatientCard(appointment.getPatientId()));
    }

    private PatientCard getPatientCard(String patientId) {
        Select<PatientCard> selectQuery = Select.from(PatientCard.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_USER_ID).eq(patientId));
        PatientCard patientCard = selectQuery.first();
        if (patientCard != null)
            patientCard.setUser(Select.from(User.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(patientId)).first());
        return patientCard;
    }

    public CalendarEvents getAppointment(String appointmentId) {
        Select<CalendarEvents> selectQuery;
        selectQuery = Select.from(CalendarEvents.class)
                .where(Condition.prop(LocalDatabaseUtils.KEY_APPOINTMENT_ID).eq(appointmentId));
        CalendarEvents calendarEvents = selectQuery.first();
        if (calendarEvents != null) {
            getAppointDetail(calendarEvents);
        }
        return calendarEvents;
    }

    public void addCalendarEventsUpdated(CalendarEvents calendarEvents) {
        calendarEvents.setIsFromCalendarAPI(true);
        if (calendarEvents.getFromDate() != null)
            calendarEvents.setFromDateFormattedMillis(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(CalendarFragment.MONTH_FORMAT_FOR_THIS_SCREEN,
                    DateTimeUtil.getFormattedDateTime(CalendarFragment.MONTH_FORMAT_FOR_THIS_SCREEN, calendarEvents.getFromDate())));
        if (calendarEvents.getToDate() != null)
            calendarEvents.setToDateFormattedMillis(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(CalendarFragment.MONTH_FORMAT_FOR_THIS_SCREEN,
                    DateTimeUtil.getFormattedDateTime(CalendarFragment.MONTH_FORMAT_FOR_THIS_SCREEN, calendarEvents.getToDate())));
        if (calendarEvents.getFromDate() != null && calendarEvents.getToDate() != null && !(calendarEvents.getFromDate().equals(calendarEvents.getToDate()))) {
            addMultipledayEvent(calendarEvents);
        }

        deleteWorkingHoursIfAlreadyPresent(LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, calendarEvents.getUniqueId());
        if (calendarEvents.getTime() != null) {
            addWorkingHour(CalendarEvents.class.getSimpleName(), calendarEvents.getUniqueId(), calendarEvents.getTime());
        }
        if (calendarEvents.getPatient() != null) {
            calendarEvents.setPatientId(calendarEvents.getPatient().getUserId());
            addPatientCard(calendarEvents.getPatient());
        }
        calendarEvents.save();
    }

    private void addPatientCard(PatientCard patient) {
        if (patient != null && patient.getUser() != null) {
            patient.getUser().save();
            patient.save();
        }
    }

    private void addMultipledayEvent(CalendarEvents calendarEvents) {
        List<Long> formattedDatesBetween = DateTimeUtil.getFormattedDatesBetween(calendarEvents.getFromDate(), calendarEvents.getToDate());
        if (!Util.isNullOrEmptyList(formattedDatesBetween))
            for (Long fromDate :
                    formattedDatesBetween) {
                MultipleDayEventTable multipleDayEventTable = new MultipleDayEventTable(calendarEvents.getAppointmentId(), fromDate, calendarEvents.getToDate());
                multipleDayEventTable.save();
            }
    }

    public VolleyResponseBean getLocationResponse(WebServiceType webServiceType, String locationId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        volleyResponseBean.setIsDataFromLocal(true);
        try {
            volleyResponseBean.setData(getLocation(locationId));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public Prescription getPrescription(String prescriptionId) {
        return getPrescription(null, prescriptionId);
    }

    public Prescription getPrescription(BooleanTypeValues discarded, String prescriptionId) {
        Select<Prescription> selectQuery;
        if (discarded != null)
            selectQuery = Select.from(Prescription.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(prescriptionId),
                            Condition.prop(LocalDatabaseUtils.KEY_DISCARDED).eq(discarded.getBooleanIntValue()));
        else
            selectQuery = Select.from(Prescription.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_UNIQUE_ID).eq(prescriptionId));
        Prescription prescription = selectQuery.first();
        if (prescription != null) {
            prescription.setItems(getDrugItemsList(LocalDatabaseUtils.KEY_FOREIGN_PRESCRIPTION_ID, prescription.getUniqueId()));
            prescription.setDiagnosticTests(getDiagnosticTestPrescriptionsList(prescription.getUniqueId()));
        }
        return prescription;
    }

    private List<DiagnosticTestsPrescription> getDiagnosticTestPrescriptionsList(String foreignTableId) {
        List<DiagnosticTestsPrescription> list = (List<DiagnosticTestsPrescription>) getObjectsList(DiagnosticTestsPrescription.class, LocalDatabaseUtils.KEY_FOREIGN_TABLE_ID, foreignTableId);
        if (!Util.isNullOrEmptyList(list)) {
            for (DiagnosticTestsPrescription diagnosticTestsPrescription : list
                    ) {
                DiagnosticTest diagnosticTest = getDiagnosticTest(diagnosticTestsPrescription.getForeignDiagnosticTestId());
                if (diagnosticTest != null) {
                    diagnosticTestsPrescription.setTest(diagnosticTest);
                }
            }
        }
        return list;
    }

    public VolleyResponseBean getClinicalNotesList(WebServiceType webServiceType, boolean isOtpVerified, String doctorId, String locationId, String hospitalId, String selectedPatientId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            Select<ClinicalNotes> selectQuery = null;
            if (!isOtpVerified && !Util.isNullOrBlank(doctorId)) {
                selectQuery = Select.from(ClinicalNotes.class)
                        .where(Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(selectedPatientId),
                                Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(locationId),
                                Condition.prop(LocalDatabaseUtils.KEY_HOSPITAL_ID).eq(hospitalId),
                                Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId));
            } else {
                selectQuery = Select.from(ClinicalNotes.class)
                        .where(Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(selectedPatientId));

            }
            List<ClinicalNotes> list = selectQuery.list();
            if (!Util.isNullOrEmptyList(list))
                for (ClinicalNotes clinicalNote : list) {
                    getClinicalNoteDetailsList(clinicalNote);
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

    public void updateClinicalNote(ClinicalNotes clinicalNote) {
        addClinicalNote(clinicalNote);
    }

    public VolleyResponseBean getPrescriptionsListResponse(WebServiceType webServiceType, boolean isOtpVerified, String doctorId, String locationId, String hospitald, String selectedPatientId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsDataFromLocal(true);
        volleyResponseBean.setIsUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            List<Prescription> list = getPrescriptionsListAsList(isOtpVerified, doctorId, locationId, hospitald, selectedPatientId);
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    private List<Prescription> getPrescriptionsListAsList(boolean isOtpVerified, String doctorId, String locationid, String hospitalId, String selectedPatientId) {
        Select<Prescription> selectQuery = null;
        if (!isOtpVerified && !Util.isNullOrBlank(doctorId)) {
            selectQuery = Select.from(Prescription.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(selectedPatientId),
                            Condition.prop(LocalDatabaseUtils.KEY_LOCATION_ID).eq(locationid),
                            Condition.prop(LocalDatabaseUtils.KEY_HOSPITAL_ID).eq(hospitalId),
                            Condition.prop(LocalDatabaseUtils.KEY_DOCTOR_ID).eq(doctorId));
        } else {
            selectQuery = Select.from(Prescription.class)
                    .where(Condition.prop(LocalDatabaseUtils.KEY_PATIENT_ID).eq(selectedPatientId));

        }
        List<Prescription> list = selectQuery.list();
        if (!Util.isNullOrEmptyList(list)) {
            for (Prescription prescription : list) {
                getPrescriptionDetail(prescription);
            }
            return list;
        }
        return null;
    }

    public void updatePrescription(Prescription prescription) {
        addPrescription(prescription);
    }

    public void addPrescriptionsList(ArrayList<Prescription> list) {
        for (Prescription prescription : list) {
            addPrescription(prescription);
        }
    }

    public void addClinicalNotesList(ArrayList<ClinicalNotes> clinicalNotesList) {
        try {
            for (ClinicalNotes clinicalNote : clinicalNotesList) {
                addClinicalNote(clinicalNote);
            }
        } catch (Exception e) {
            Log.i(null, "Error in saving in transaction " + e.getMessage());
        }
    }
}