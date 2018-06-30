package com.healthcoco.healthcocopad.services.impl;

import android.os.Handler;
import android.os.HandlerThread;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.google.gson.GsonBuilder;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.AppointmentFeedback;
import com.healthcoco.healthcocopad.bean.DoctorContactUs;
import com.healthcoco.healthcocopad.bean.DoctorProfileToSend;
import com.healthcoco.healthcocopad.bean.PersonalHistory;
import com.healthcoco.healthcocopad.bean.VersionCheckRequest;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddDrugRequest;
import com.healthcoco.healthcocopad.bean.request.AddEditAchievementsRequest;
import com.healthcoco.healthcocopad.bean.request.AddEditDoctorEducationRequest;
import com.healthcoco.healthcocopad.bean.request.AddEditDoctorExperinceRequest;
import com.healthcoco.healthcocopad.bean.request.AddEditDoctorRegRequest;
import com.healthcoco.healthcocopad.bean.request.AddEditDrugsAndAllergiesRequest;
import com.healthcoco.healthcocopad.bean.request.AddMedicalFamilyHistoryRequest;
import com.healthcoco.healthcocopad.bean.request.AddReceiptRequest;
import com.healthcoco.healthcocopad.bean.request.AppointmentRequestToSend;
import com.healthcoco.healthcocopad.bean.request.AssignGroupRequest;
import com.healthcoco.healthcocopad.bean.request.ClinicImageToSend;
import com.healthcoco.healthcocopad.bean.request.ClinicalNoteToSend;
import com.healthcoco.healthcocopad.bean.request.DoctorSignupHandheldContinueRequest;
import com.healthcoco.healthcocopad.bean.request.DrugInteractionRequest;
import com.healthcoco.healthcocopad.bean.request.EventRequest;
import com.healthcoco.healthcocopad.bean.request.Feedback;
import com.healthcoco.healthcocopad.bean.request.InvoiceRequest;
import com.healthcoco.healthcocopad.bean.request.PrescriptionRequest;
import com.healthcoco.healthcocopad.bean.request.PrintPatientCardRequest;
import com.healthcoco.healthcocopad.bean.request.ProfessionalMembershipRequest;
import com.healthcoco.healthcocopad.bean.request.ProfessionalStatementRequest;
import com.healthcoco.healthcocopad.bean.request.RegisterNewPatientRequest;
import com.healthcoco.healthcocopad.bean.request.TreatmentRequest;
import com.healthcoco.healthcocopad.bean.request.UserPermissionsRequest;
import com.healthcoco.healthcocopad.bean.request.UserVerification;
import com.healthcoco.healthcocopad.bean.server.AdviceSuggestion;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.Events;
import com.healthcoco.healthcocopad.bean.server.GCMRequest;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.NotificationResponse;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.PrintSettings;
import com.healthcoco.healthcocopad.bean.server.Profession;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.RecordState;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.UserState;
import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.GCMRefreshListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LocalDatabaseUtils;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.utilities.Util.checkNetworkStatus;

/**
 * Created by Shreshtha on 20-01-2017.
 */

public class WebDataServiceImpl implements GCMRefreshListener {
    private static final String TAG = Util.class.getSimpleName();
    private static WebDataServiceImpl mInstance;
    private static HealthCocoApplication mApp;
    private static Handler mHandler;

    private WebDataServiceImpl() {
    }

    public static WebDataServiceImpl getInstance(HealthCocoApplication application) {
        if (mInstance == null) {
            mApp = application;
            mInstance = new WebDataServiceImpl();
            HandlerThread mHandlerThread = new HandlerThread("HandlerThread");
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper());
        }
        return mInstance;
    }

    public void sendContactUsRequest(Class<?> class1, DoctorContactUs doctorContactUs, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.DOCTOR_CONTACT_US;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, doctorContactUs, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void signUpContinueVerification(Class<?> class1, DoctorSignupHandheldContinueRequest object, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.SIGN_UP_CONTINUE_VERIFICATION;
        getResponse(webServiceType, class1, webServiceType.getUrl(), object, null, responseListener, errorListener);
    }

    public void checkVersion(Class<Integer> class1, VersionCheckRequest versionCheckRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.VERSION_CONTROL_CHECK;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            LogUtils.LOGI(TAG, url);
            getResponse(webServiceType, class1, url, versionCheckRequest, null, responseListener, errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getPatientStatus(Class<?> class1, String patientId, String doctorId, String locationId, String hospitalId,
                                 Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_PATIENT_STATUS;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + patientId + "/"
                    + doctorId + "/"
                    + locationId + "/"
                    + hospitalId + "/";
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
        }
    }

    public void generateOtp(Class<?> class1, String doctorId, String locationId, String hospitalId, String patientId,
                            Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GENERATE_OTP;
        String url = webServiceType.getUrl()
                + doctorId + "/"
                + locationId + "/"
                + hospitalId + "/"
                + patientId + HealthCocoConstants.PARAM_TAG_GENERATE;
        getResponse(webServiceType, class1, url, null, null, responseListener,
                errorListener);
    }

    public void verifyOtp(Class<?> class1, String doctorId, String locationId, String hospitalId, String patientId, String otpNumber,
                          Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.VERIFY_OTP;
        String url = webServiceType.getUrl()
                + doctorId + "/"
                + locationId + "/"
                + hospitalId + "/"
                + patientId + "/"
                + otpNumber + HealthCocoConstants.PARAM_TAG_VERIFY;
        getResponse(webServiceType, class1, url, null, null, responseListener,
                errorListener);
    }

    private void showUserOffline(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setIsUserOnline(false);
        volleyResponseBean.setIsDataFromLocal(true);
        responseListener.onResponse(volleyResponseBean);
        Util.showToast(mApp.getApplicationContext(), R.string.user_offline);
    }

    public void getResponse(Request.Priority priority, WebServiceType serviceType, Class<?> responseClass, String url, Object objBody,
                            HashMap<String, String> headers, Response.Listener<VolleyResponseBean> responseListener,
                            GsonRequest.ErrorListener errorListener) {
        try {
            String body = null;
            if (objBody != null) {
                body = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED, Modifier.PUBLIC).create().toJson(objBody);
                if (body.contains(LocalDatabaseUtils.ID_REPLACED))
                    body = body.replace(LocalDatabaseUtils.ID_REPLACED, LocalDatabaseUtils.ID);
                if (body.contains(LocalDatabaseUtils.FROM_VALUE))
                    body = body.replace(LocalDatabaseUtils.FROM_VALUE, LocalDatabaseUtils.FROM);
                if (body.contains(LocalDatabaseUtils.TO_VALUE))
                    body = body.replace(LocalDatabaseUtils.TO_VALUE, LocalDatabaseUtils.TO);
            }
            if (url.contains(GsonRequest.SPACE))
                url = url.replace(GsonRequest.SPACE, GsonRequest.SPACE_REPLACED);

            GsonRequest request = new GsonRequest(mApp, priority, serviceType, responseClass, url, body, headers, responseListener,
                    errorListener);
            RetryPolicy policy = new DefaultRetryPolicy(GsonRequest.DEFAULT_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            request.setShouldCache(false);
            mApp.addToRequestQueue(request, serviceType.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param serviceType
     * @param responseClass
     * @param url
     * @param objBody
     * @param headers
     * @param responseListener
     * @param errorListener
     */
    public void getResponse(WebServiceType serviceType, Class<?> responseClass, String url, Object objBody,
                            HashMap<String, String> headers, Response.Listener<VolleyResponseBean> responseListener,
                            GsonRequest.ErrorListener errorListener) {
        try {
            String body = null;
            if (objBody != null) {
                body = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED, Modifier.PUBLIC).create().toJson(objBody);
                if (body.contains(LocalDatabaseUtils.ID_REPLACED))
                    body = body.replace(LocalDatabaseUtils.ID_REPLACED, LocalDatabaseUtils.ID);
                if (body.contains(LocalDatabaseUtils.FROM_VALUE))
                    body = body.replace(LocalDatabaseUtils.FROM_VALUE, LocalDatabaseUtils.FROM);
                if (body.contains(LocalDatabaseUtils.TO_VALUE))
                    body = body.replace(LocalDatabaseUtils.TO_VALUE, LocalDatabaseUtils.TO);
            }
            if (url.contains(GsonRequest.SPACE))
                url = url.replace(GsonRequest.SPACE, GsonRequest.SPACE_REPLACED);
            GsonRequest request = new GsonRequest(mApp, Request.Priority.HIGH, serviceType, responseClass, url, body, headers, responseListener,
                    errorListener);
            RetryPolicy policy = new DefaultRetryPolicy(GsonRequest.DEFAULT_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            request.setShouldCache(false);
            mApp.addToRequestQueue(request, serviceType.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginUser(Class<LoginResponse> class1, User user, Response.Listener<VolleyResponseBean> responseListener,
                          GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.LOGIN;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + HealthCocoConstants.PARAM_IS_MOBILE_APP;
            getResponse(webServiceType, class1, url, user, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void isLocationAdmin(Class<?> class1, Object object, Response.Listener<VolleyResponseBean> responseListener,
                                GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.IS_LOCATION_ADMIN;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, object, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    /**
     * @param webServiceType
     * @param class1
     * @param object
     * @param responseListener
     * @param errorListener
     */
    public void addUpdateCommonMethod(WebServiceType webServiceType, Class<?> class1, Object object, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, object, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }


    public void getContactsList(Class<RegisteredPatientDetailsUpdated> class1, String doctorId,
                                String hospitalId, String locationId, long updatedTime, User user, Response.Listener<VolleyResponseBean> responseListener,
                                GsonRequest.ErrorListener errorListener) {
        getContactsList(class1, doctorId, hospitalId, locationId, updatedTime, user, 0, 0, null, responseListener, errorListener);
    }

    public void getContactsList(Class<RegisteredPatientDetailsUpdated> class1, String doctorId,
                                String hospitalId, String locationId, long updatedTime, User user, int pageNo, int size, String searchTerm, Response.Listener<VolleyResponseBean> responseListener,
                                GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_CONTACTS;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                    + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;

            if (RoleType.isOnlyConsultantOrIsOnlyDoctorOrBoth(user.getRoleTypes())) {
                url = url + HealthCocoConstants.PARAM_ROLE + RoleType.CONSULTANT_DOCTOR
                        + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId;
            }

            if (pageNo >= 0 && size > 0) {
                url = url + HealthCocoConstants.PARAM_PAGE + pageNo + HealthCocoConstants.PARAM_SIZE + size;
            }
            if (!Util.isNullOrBlank(searchTerm))
                url = url + HealthCocoConstants.PARAM_SEARCH_TERM + searchTerm;
            getResponse(Request.Priority.HIGH, WebServiceType.GET_CONTACTS, class1, url, null, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
//            errorListener.onErrorResponse(null, mApp.getResources().getString(R.string.user_offline));
//            LocalDataServiceImpl.getInstance(mApp)
//                    .getPatientsList(WebServiceType.GET_CONTACTS, doctorId, hospitalId, locationId, responseListener, errorListener);
        }
    }


    /**
     * TO get speciality list
     *
     * @param class1
     * @param updatedTime
     * @param responseListener
     * @param errorListener
     */
    public void getSpecialities(Class<?> class1, Long updatedTime, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_SPECIALITIES;
        String url = webServiceType.getUrl()
                + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
        getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
    }

    public void getDosageDirection(WebServiceType webServiceType, Class<?> class1, boolean isDiscarded, String doctorId, Long updatedTime, Response.Listener<VolleyResponseBean> responseListener,
                                   GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl()
                + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + isDiscarded
                + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime
                + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, null, null,
                    responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp)
                    .getDosageDurationDirectionList(webServiceType, responseListener, errorListener);
        }
    }

    /**
     * @param class1
     * @param locationId       : represents clinicId
     * @param responseListener
     * @param errorListener
     */
    public void getClinicDetails(Class<?> class1, String locationId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_CLINIC_PROFILE;
        String url = webServiceType.getUrl() + locationId;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
            LocalDataServiceImpl.getInstance(mApp).getClinicDetailsResponse(webServiceType, locationId, responseListener, errorListener);
        }
    }

    public void getRegisterDoctor(Class<?> class1, String locationId, String hospitalId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_REGISTER_DOCTOR;

        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + locationId
                    + "/" + hospitalId
                    + "/" + HealthCocoConstants.PARAM_ACTIVE_TRUE
                    + HealthCocoConstants.PARAM_ROLE + RoleType.DOCTOR
                    + HealthCocoConstants.PARAM_USER_STATE + UserState.COMPLETED;
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
            LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorResponse(webServiceType, locationId, responseListener, errorListener);
        }
    }

    public void getDrugType(WebServiceType webServiceType, Class<DrugType> class1, boolean isDiscarded, String doctorId, Long updatedTime, Response.Listener<VolleyResponseBean> responseListener,
                            GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + isDiscarded
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;

            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp)
                    .getDrugTypeListAdResponse(WebServiceType.GET_DRUG_TYPE, doctorId, responseListener, errorListener);
        }
    }

    public void getGroupsList(WebServiceType webServiceType, Class<UserGroups> class1, String doctorId, String locationId, String hospitalId, Long updatedTime, ArrayList<String> patientsAssignedGroupIdList, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_DISCARDED_TRUE
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                    + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp).getUserGroups(webServiceType, patientsAssignedGroupIdList, doctorId, locationId, hospitalId, responseListener, errorListener);
        }
    }

    public void getProfession(Class<Profession> class1, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_PROFESSION;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, webServiceType.getUrl(), null, null, responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp).getProfessionList(webServiceType, responseListener, errorListener);
        }
    }

    public void getReference(Class<Reference> class1, String doctorId, long updatedTime, BooleanTypeValues isDiscarded, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_REFERENCE;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + isDiscarded.getBooleanFlag()
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
//            LocalDataServiceImpl.getInstance(mApp).getReferenceListAll(webServiceType, doctorId, isDiscarded, responseListener, errorListener);
        }
    }

    public void getClinicalNoteSuggestionsList(Class<?> class1, WebServiceType webServiceType, String dataTpe, String doctorId, Long latestUpdatedTime, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String dataPermission;
        dataPermission = dataTpe;
        if (Util.isNullOrBlank(dataPermission)) {
            dataPermission = HealthCocoConstants.PARAM_TAG_BOTH;
        }
        String url = webServiceType.getUrl() + dataPermission + "/" + HealthCocoConstants.PARAM_DISCARDED_TRUE + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId + HealthCocoConstants.PARAM_UPDATED_TIME + latestUpdatedTime;
        getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
    }


    public void getAdviceSuggestionsList(Class<?> class1, WebServiceType webServiceType, String doctorId, Long latestUpdatedTime, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl() + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId + HealthCocoConstants.PARAM_UPDATED_TIME + latestUpdatedTime;
        getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
    }


    public void getBloodGroup(Class<?> class1, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_BLOOD_GROUP;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, webServiceType.getUrl(), null, null, responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp).getBloodGroup(webServiceType, responseListener, errorListener);
        }
    }

    public void getDiseaseList(Class<?> class1, String doctorId, Long updatedTime, ArrayList<String> diseaseIds, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_DISEASE_LIST;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_DISCARDED_TRUE
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp)
                    .getDiseaseList(webServiceType, null, BooleanTypeValues.FALSE, null, 0, null, null);
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getTemplatesList(Class<TempTemplate> class1, String doctorId, long updatedTime, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            WebServiceType webServiceType = WebServiceType.GET_TEMPLATES_LIST;
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + false
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
//            LocalDataServiceImpl.getInstance(mApp)
//                    .getTemplatesListPageWise(WebServiceType.GET_TEMPLATES_LIST, doctorId, false, 0l,
//                            0, TemplatesListFragment.MAX_COUNT, "", responseListener, errorListener);
        }
    }

    public void deleteTemplate(Class<?> class1, String templateId, String doctorId, String locationId, String hospitalId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = WebServiceType.DELETE_TEMPLATE.getUrl() + templateId + "/" + doctorId + "/" + locationId + "/" + hospitalId + HealthCocoConstants.PARAM_TAG_DELETE;
        getResponse(WebServiceType.DELETE_TEMPLATE, class1, url, null, null, responseListener,
                errorListener);
    }

    public void getCities(Class<?> class1, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            long updatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.CITIES);
            WebServiceType webServiceType = WebServiceType.GET_CITIES;
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            errorListener.onErrorResponse(null, mApp.getResources().getString(R.string.user_offline));
        }
    }

    public void getDoctorProfile(Class<?> class1, String doctorId, String locationId, String hospitalId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_DOCTOR_PROFILE;
        String url = webServiceType.getUrl() + doctorId + HealthCocoConstants.PARAM_TAG_VIEW + HealthCocoConstants.PARAM_IS_MOBILE_APP;

        if (!Util.isNullOrBlank(locationId))
            url = url + HealthCocoConstants.PARAM_LOCATION_ID + locationId;
        if (!Util.isNullOrBlank(hospitalId))
            url = url + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(Request.Priority.HIGH, webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
            LocalDataServiceImpl.getInstance(mApp).getDoctorProfileResponse(webServiceType, doctorId, responseListener, errorListener);
        }
    }

    public void updateDoctorProfile(Class<?> class1, DoctorProfileToSend profile, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.UPDATE_DOCTOR_PROFILE;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, profile, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateEducation(Class<?> class1, AddEditDoctorEducationRequest doctorEducationRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_EDUCATION;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, doctorEducationRequest, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void getMasterDataFromSolr(WebServiceType webServiceType, Class<?> class1, String locationId, String hospitalId, int pageNum, int size, String searchTerm, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        String url = webServiceType.getUrl();
        if (HealthCocoConstants.isNetworkOnline) {
            switch (webServiceType) {
                case GET_DIAGNOSTIC_TESTS_SOLR:
                    url = url + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                            + HealthCocoConstants.PARAM_LOCATION_ID + locationId;
                    break;
            }
            url = url + HealthCocoConstants.PARAM_PAGE_AND + pageNum
                    + HealthCocoConstants.PARAM_SIZE + size
                    + HealthCocoConstants.PARAM_SEARCH_TERM + searchTerm;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }


    public void addUpdateRegistrationDetail(Class<?> class1, AddEditDoctorRegRequest addEditDoctorRegRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_REGISTRATION_DETAIL;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, addEditDoctorRegRequest, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateAchievments(Class<?> class1, AddEditAchievementsRequest achievementsRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_ACHIEVEMENTS_DETAIL;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, achievementsRequest, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateExperirnce(Class<?> class1, AddEditDoctorExperinceRequest doctorExperinceRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_EXPERIENCE_DETAIL;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, doctorExperinceRequest, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateProfessionalMembership(Class<?> class1, ProfessionalMembershipRequest membershipRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_PROFESSIONAL_MEMBERSHIP_DETAIL;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, membershipRequest, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateProfessionalStatement(Class<?> class1, ProfessionalStatementRequest statementRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_PROFESSIONAL_STATEMENT_DETAIL;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, statementRequest, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateGeneralInfo(Class<?> class1, DoctorClinicProfile doctorClinicProfile, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_GENERAL_INFO;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, doctorClinicProfile, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addClinicLogo(Class<?> class1, ClinicImageToSend imageToSend, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_CLINIC_LOGO;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, imageToSend, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void deleteCLinicImage(Class<?> class1, int counter, String locationId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.DELETE_CLINIC_IMAGE;
        String url = webServiceType.getUrl()
                + locationId + "/"
                + counter + HealthCocoConstants.PARAM_TAG_DELETE;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addClinicImages(Class<?> class1, ClinicImageToSend imageToSend, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_CLINIC_IMAGE;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, imageToSend, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void getBothUIPermissionsForDoctor(Class<?> class1, String doctorId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_BOTH_PERMISSIONS_FOR_DOCTOR;
        String url = webServiceType.getUrl() + doctorId;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void getAlreadyRegisteredPatients(Class<?> class1, String mobileNo, User user, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.SEARCH_PATIENTS;
        checkNetworkStatus(mApp.getApplicationContext());
        String url = webServiceType.getUrl();
        if (HealthCocoConstants.isNetworkOnline) {
            url = url
                    + mobileNo + "/"
                    + user.getUniqueId() + "/"
                    + user.getForeignLocationId() + "/"
                    + user.getForeignHospitalId() + "?";
            if (RoleType.isOnlyConsultant(user.getRoleTypes()))
                url = url + HealthCocoConstants.PARAM_ROLE + RoleType.CONSULTANT_DOCTOR;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            errorListener.onErrorResponse(null, mApp.getResources().getString(R.string.user_offline));
        }
    }

    public void registerNewPatient(Class<?> class1, RegisterNewPatientRequest object, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.REGISTER_PATIENT;
        getResponse(webServiceType, class1, webServiceType.getUrl(), object, null, responseListener, errorListener);
    }

    public void updatePatient(Class<?> class1, RegisterNewPatientRequest object, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.UPDATE_PATIENT;
        getResponse(webServiceType, class1, webServiceType.getUrl(), object, null, responseListener, errorListener);
    }

    public void addUpdateDeleteGroup(WebServiceType webServiceType, Class<?> class1, UserGroups group, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl();
        switch (webServiceType) {
            case ADD_NEW_GROUP:
                break;
            case UPDATE_GROUP:
                url = url + group.getUniqueId() + HealthCocoConstants.PARAM_TAG_UPDATE;
                break;
            case DELETE_GROUP:
                url = url + group.getUniqueId() + HealthCocoConstants.PARAM_TAG_DELETE;
                break;
        }
        getResponse(webServiceType, class1, url, group, null, responseListener,
                errorListener);
    }

    public void addPatientToQueue(WebServiceType webServiceType, Class<?> class1, RegisteredPatientDetailsUpdated object, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl();
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, object, null, responseListener, errorListener);
        } else {
            errorListener.onErrorResponse(null, mApp.getResources().getString(R.string.user_offline));
        }
    }

    public void discardPatient(WebServiceType webServiceType, Class<?> class1, String doctorId, String locationId, String hospitalId, String patientId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + doctorId
                    + "/" + locationId
                    + "/" + hospitalId
                    + "/" + patientId
                    + HealthCocoConstants.PARAM_TAG_DELETE
                    + HealthCocoConstants.PARAM_DISCARDED_TRUE;
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }


    public void assignGroup(Class<AssignGroupRequest> class1, AssignGroupRequest assignGroupRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ASSIGN_GROUP;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline)
            getResponse(webServiceType, class1, webServiceType.getUrl(), assignGroupRequest, null, responseListener,
                    errorListener);
        else
            errorListener.onNetworkUnavailable(webServiceType);
    }

    public void getPatientProfile(Class<RegisteredPatientDetailsUpdated> class1, String userId, String doctorId,
                                  String locationId, String hospitalId, Response.Listener<VolleyResponseBean> responseListener,
                                  GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_PATIENT_PROFILE;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + userId + "?"
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            errorListener.onErrorResponse(null, mApp.getResources().getString(R.string.user_offline));
        }
    }

    public void getHistoryListUpdatedAPI(Class<?> class1, WebServiceType webServiceType, boolean isOtpVerified, String pateintId, String doctorId, String locationId, String hospitalId, boolean isInHistory, Long updatedTime, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + "/" + pateintId
                    + "/" + doctorId
                    + "/" + locationId
                    + "/" + hospitalId;
            getResponse(Request.Priority.HIGH, webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp).getHistoryDetailResponse(WebServiceType.GET_DISEASE_HISTORY_LIST, BooleanTypeValues.FALSE, isOtpVerified, doctorId, locationId, hospitalId, HealthCocoConstants.SELECTED_PATIENTS_USER_ID, responseListener, errorListener);
        }
    }

    public void getMedicalFamilyHistory(Class<?> class1, WebServiceType webServiceType, String patientId, String doctorId, String locationId, String hospitalId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + patientId + "/" + doctorId + "/" + locationId + "/" + hospitalId;
            getResponse(Request.Priority.HIGH, webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
//            LocalDataServiceImpl.getInstance(mApp).getMedicalFAmilyHistory(WebServiceType.GET_MEDICAL_AND_FAMILY_HISTORY, patientId, responseListener, errorListener);
        }
    }

    public void addMedicalFamilyHistory(WebServiceType webServiceType, Class<?> class1, AddMedicalFamilyHistoryRequest object, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        getResponse(webServiceType, class1, webServiceType.getUrl(), object, null, responseListener, errorListener);
    }

    public void addDisease(Class<?> class1, ArrayList<Disease> diseasesList, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_DISEASE;
        getResponse(webServiceType, class1, webServiceType.getUrl(), diseasesList, null, responseListener, errorListener);
    }

    public void addUpdatePersonalHistory(Class<?> class1, PersonalHistory personalHistory, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_PERSONAL_HISTORY_DETAIL;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, personalHistory, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void getDrugsListSolr(Class<?> class1, int pageNum, int size, String doctorId, String hospitalId, String locationId, String searchTerm, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_DRUGS_LIST_SOLR;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            webServiceType = WebServiceType.GET_DRUGS_LIST_SOLR;
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_DISCARDED_FALSE
                    + HealthCocoConstants.PARAM_PAGE_AND + pageNum
                    + HealthCocoConstants.PARAM_SIZE + size

                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                    + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_SEARCH_TERM + searchTerm;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void addUpdateDrugsAndAllergies(Class<?> class1, AddEditDrugsAndAllergiesRequest drugsAndAllergiesRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_DRUGS_AND_ALLERGIES_DETAIL;
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, drugsAndAllergiesRequest, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void getVisitsList(Class<?> class1, WebServiceType webServiceType, String doctorId, String locationId,
                              String hospitalId, String patientId, Long updatedTime, String visitedForType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + null
                    + "/" + locationId
                    + "/" + hospitalId
                    + "/" + patientId
                    + "?"
                    + HealthCocoConstants.PARAM_VISIT_FOR + visitedForType
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp)
                    .getVisitsList(WebServiceType.GET_PATIENT_VISIT, doctorId, locationId, hospitalId, patientId, responseListener, errorListener);
        }
    }

    public void sendGcmRegistrationId(boolean isOnTokenRefresh) {
        if (!isOnTokenRefresh && !mApp.isEmptyRequestsList()) {
            LogUtils.LOGD(TAG, " List nonEmpty");
            mApp.setGcmRegistrationListener(this);
        } else {
            LogUtils.LOGD(TAG, " List Empty");
            refreshGCM(isOnTokenRefresh);
        }
    }

    @Override
    public void refreshGCM(boolean isOnTokenRefresh) {
        if (isOnTokenRefresh)
            HealthCocoActivity.GCM_WAIT_TIME = 5000;
        else
            HealthCocoActivity.GCM_WAIT_TIME = 2000;
        LogUtils.LOGD(TAG, "Interval " + HealthCocoActivity.GCM_WAIT_TIME);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                WebServiceType webServiceType = WebServiceType.SEND_GCM_REGISTRATION_ID;
                GCMRequest gcmRequest = LocalDataServiceImpl.getInstance(mApp).getGCMRequestData();
                checkNetworkStatus(mApp);
                if (HealthCocoConstants.isNetworkOnline) {
                    String url = webServiceType.getUrl();
                    getResponse(webServiceType, Object.class, url, gcmRequest, null, null,
                            null);
                    mApp.setGcmRegistrationListener(null);
                }
            }
        }, HealthCocoActivity.GCM_WAIT_TIME);
    }

    public void sendEmail(WebServiceType webServiceType, String uniqueId, String doctorId, String locationId, String hospitalId, String emailId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl() + uniqueId
                + "/" + doctorId
                + "/" + locationId
                + "/" + hospitalId
                + "/" + emailId
                + HealthCocoConstants.PARAM_TAG_MAIL;
        switch (webServiceType) {
            case SEND_EMAIL_VISIT:
                url = webServiceType.getUrl() + uniqueId
                        + "/" + emailId;
                break;
            default:

                break;
        }
        getResponse(webServiceType, null, url, null, null, responseListener, errorListener);
    }

    public void sendSms(WebServiceType webServiceType, String uniqueId, String doctorId, String locationId, String hospitalId, String mobileNumber, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl() + uniqueId
                + "/" + doctorId
                + "/" + locationId
                + "/" + hospitalId
                + "/" + mobileNumber
                + HealthCocoConstants.PARAM_TAG_SMS;
        getResponse(webServiceType, null, url, null, null, responseListener, errorListener);
    }

    public void getPdfUrl(Class<?> class1, WebServiceType webServiceType, String uniqueId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + uniqueId;
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    /**
     * used to hide/Activate drug as well
     *
     * @param webServiceType
     * @param class1
     * @param object
     * @param responseListener
     * @param errorListener
     */
    public void addUpdateReference(WebServiceType webServiceType, Class<?> class1, Object object, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl();
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            switch (webServiceType) {
                case ADD_REFERENCE:
                    break;
                case ADD_CUSTOM_HISTORY:
                    break;
                case ADD_DOSAGE:
                    break;
                case ADD_DIRECTION:
                    break;
                case DELETE_REFERENCE:
                    if (object instanceof Reference) {
                        Reference reference = (Reference) object;
                        url = url + reference.getUniqueId() + HealthCocoConstants.PARAM_TAG_DELETE;
                    }
                    break;

            }
            getResponse(webServiceType, class1, url, object, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addDrug(Class<Drug> class1, Response.Listener<VolleyResponseBean> responseListener,
                        GsonRequest.ErrorListener errorListener, AddDrugRequest drug) {
        getResponse(WebServiceType.ADD_DRUG, class1, WebServiceType.ADD_DRUG.getUrl(), drug, null, responseListener,
                errorListener);
    }

    public void addTempLate(Class<TempTemplate> class1, TempTemplate template, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        getResponse(WebServiceType.ADD_TEMPLATE, class1, WebServiceType.ADD_TEMPLATE.getUrl(), template, null, responseListener,
                errorListener);
    }

    public void updateTemplate(Class<?> class1, TempTemplate template, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = WebServiceType.UPDATE_TEMPLATE.getUrl() + template.getUniqueId() + HealthCocoConstants.PARAM_TAG_UPDATE;
        getResponse(WebServiceType.UPDATE_TEMPLATE, class1, url, template, null, responseListener,
                errorListener);
    }

    public void changeRecordState(Class<?> class1, String recordId, RecordState recordState, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.CHANGE_RECORD_STATE;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + recordId + "/" + recordState + HealthCocoConstants.PARAM_CHANGE_STATE;
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void discardRecord(Class<?> class1, String recordId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = WebServiceType.DISCARD_REPORT.getUrl() + recordId + HealthCocoConstants.PARAM_TAG_DELETE;
        getResponse(WebServiceType.DISCARD_REPORT, class1, url, null, null, responseListener,
                errorListener);
    }

    public void addToHistory(Class<?> class1, WebServiceType webServiceType, String uniqueId, String patientId, String doctorId, String locationId, String hospitalId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl() + uniqueId + "/" + patientId + "/" + doctorId + "/" + locationId + "/" + hospitalId + HealthCocoConstants.PARAM_TAG_ADD;
        getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
    }

    public void removeFromHistory(Class<?> class1, WebServiceType webServiceType, String uniqueId, String patientId, String doctorId, String locationId, String hospitalId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl() + uniqueId + "/" + patientId + "/" + doctorId + "/" + locationId + "/" + hospitalId;
        getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
    }

    public void getDiagramsList(Class<?> class1, Long updatedTime, String doctorId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_DIAGRAMS_LIST;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_DISCARDED_TRUE
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addDiagram(Class<?> class1, Diagram diagram,
                           Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_DIAGRAM;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, diagram, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void getDrugsList(WebServiceType webServiceType, Class<?> class1, String doctorId, long updatedTime, boolean discarded, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl() +
                HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + discarded
                + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
        getResponse(webServiceType, class1, url, null, null, responseListener,
                errorListener);
    }

    public void sendFeedback(Class<?> class1, Feedback feedback, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            WebServiceType webServiceType = WebServiceType.ADD_FEEDBACK;
            getResponse(webServiceType, class1, webServiceType.getUrl(), feedback, null, responseListener,
                    errorListener);
        }
    }

    public void updateUiPermissions(Class<?> class1, UserPermissionsRequest userPermissionsResponse, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            WebServiceType webServiceType = WebServiceType.POST_UI_PERMISSIONS;
            getResponse(webServiceType, class1, webServiceType.getUrl(), userPermissionsResponse, null, responseListener,
                    errorListener);
        }
    }

    public void addVisit(Class<?> class1, VisitDetails visitDetails,
                         Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_VISIT;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, webServiceType.getUrl(), visitDetails, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getDiagnosticTestsFromSolr(Class<?> class1, String locationId, String hospitalId, int pageNum, int size, String searchTerm, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_DIAGNOSTIC_TESTS_SOLR;
        Util.checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                    + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_PAGE_AND + pageNum
                    + HealthCocoConstants.PARAM_SIZE + size
                    + HealthCocoConstants.PARAM_SEARCH_TERM + searchTerm;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void getDrugsInteractionResponse(Class<?> class1, String patientId, ArrayList<DrugInteractionRequest> drugsList, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_DRUG_INTERACTIONS;
        String url = webServiceType.getUrl()
                + HealthCocoConstants.PARAM_PATIENT_ID + patientId;
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, drugsList, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void getNotificationResponseDataDetail(NotificationResponse notificationResponse, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = notificationResponse.getNotificationType().getWebServiceType();
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            Class class1 = Object.class;
            switch (notificationResponse.getNotificationType()) {
                case REPORTS:
                    class1 = Records.class;
                    url = url + notificationResponse.getRi() + HealthCocoConstants.PARAM_TAG_VIEW;
                    break;
                case APPOINTMENT:
                    class1 = CalendarEvents.class;
                    url = url + notificationResponse.getAi() + HealthCocoConstants.PARAM_TAG_VIEW;
                    break;
            }
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void addAppointment(Class<?> class1, AppointmentRequestToSend appointment,
                               Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_APPOINTMENT;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, appointment, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void addEvent(Class<?> class1, EventRequest eventRequest,
                         Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_EVENT;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, eventRequest, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void sendReminder(Class<Boolean> class1, String appointmentId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.SEND_REMINDER;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + appointmentId;
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getEmrListGeneralMethod(Class<?> class1, WebServiceType webServiceType, boolean isOtpVerified, boolean discarded, String doctorId, String locationId, String hospitalId, String patientId, long updatedTime,
                                        Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + patientId + "?"
                    + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + discarded
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            switch (webServiceType) {
                case GET_APPOINTMENT:
                    url = webServiceType.getUrl() + "?"
                            + HealthCocoConstants.PARAM_PATIENT_ID + patientId
                            + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                            + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                            + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                            + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + discarded
                            + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
                    break;
                case GET_REPORTS_UPDATED:
//                    url = url + HealthCocoConstants.PARAM_IS_DOCTOR_APP_TRUE;
                    break;
            }
            getResponse(Request.Priority.IMMEDIATE, webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
            switch (webServiceType) {
                case GET_CLINICAL_NOTES:
//                    LocalDataServiceImpl.getInstance(mApp).getClinicalNotesList(WebServiceType.GET_CLINICAL_NOTES, isOtpVerified, doctorId, locationId, hospitalId, patientId, responseListener, errorListener);
                    break;
                case GET_REPORTS_UPDATED:
//                    LocalDataServiceImpl.getInstance(mApp).getRecordsList(WebServiceType.GET_REPORTS_UPDATED, isOtpVerified, doctorId, locationId, hospitalId, patientId, responseListener, errorListener);
                    break;
                case GET_PRESCRIPTION:
//                    LocalDataServiceImpl.getInstance(mApp).getPrescriptionsListResponse(webServiceType, isOtpVerified, doctorId, locationId, hospitalId, patientId, responseListener, errorListener);
                    break;
            }
        }
    }

    public void getTreatment(Class<?> class1, WebServiceType webServiceType, boolean discarded,
                             String doctorId, String locationId, String hospitalId, String patientId, long updatedTime,
                             Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + "?"
                    + HealthCocoConstants.PARAM_PATIENT_ID + patientId
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                    + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + discarded
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(Request.Priority.IMMEDIATE, webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void addTreatment(Class<?> class1, TreatmentRequest treatment,
                             Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_TREATMENT;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, treatment, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }


    public void getTreatmentsListSolr(Class<?> class1, WebServiceType webServiceType, int pageNum, int size, String doctorId, String hospitalId, String locationId, String searchTerm, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        String url = null;
        if (HealthCocoConstants.isNetworkOnline) {
            switch (webServiceType) {
                case GET_TREATMENT_LIST_BOTH_SOLR:
                    url = webServiceType.getUrl()
                            + HealthCocoConstants.PARAM_DISCARDED_FALSE
                            + HealthCocoConstants.PARAM_PAGE + pageNum
                            + HealthCocoConstants.PARAM_SIZE + size

                            + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                            + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                            + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                            + HealthCocoConstants.PARAM_SEARCH_TERM + searchTerm;
                    break;
                case GET_TREATMENT_LIST_FEATURED:
                    url = webServiceType.getUrl()
                            + HealthCocoConstants.PARAM_SPECIALITY + "Dentist";
                    break;
            }
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }


    public void getTreatmentsServiceList(Class<?> class1, WebServiceType webServiceType, String doctorId, String hospitalId, String locationId, Long updatedTime, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp.getApplicationContext());
        String url = null;
        if (HealthCocoConstants.isNetworkOnline) {
            switch (webServiceType) {
                case GET_TREATMENT_LIST_BOTH_SOLR:
                    url = webServiceType.getUrl()
                            + HealthCocoConstants.PARAM_DISCARDED_FALSE

                            + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                            + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                            + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                            + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
                    break;
                case GET_TREATMENT_LIST_FEATURED:
                    url = webServiceType.getUrl()
                            + HealthCocoConstants.PARAM_SPECIALITY + "Dentist";
                    break;
            }
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void discardTreatment(Class<?> class1, String treatmentId, String doctorId, String locationId, String hospitalId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = WebServiceType.DISCARD_TREATMENT.getUrl()
                + treatmentId
                + "/" + doctorId
                + "/" + locationId
                + "/" + hospitalId
                + HealthCocoConstants.PARAM_TAG_DELETE + HealthCocoConstants.PARAM_DISCARDED_TRUE;
        getResponse(WebServiceType.DISCARD_TREATMENT, class1, url, null, null, responseListener,
                errorListener);
    }

    public void addTreatmentListSolr(Class<?> class1, TreatmentService treatmentListSolrResponse,
                                     Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_TREATMENT_SERVICE;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, treatmentListSolrResponse, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }


    public void discardVisit(Class<?> class1, String visitId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = WebServiceType.DISCARD_PATIENT_VISIT.getUrl() + visitId + HealthCocoConstants.PARAM_TAG_DELETE;
        getResponse(WebServiceType.DISCARD_PATIENT_VISIT, class1, url, null, null, responseListener,
                errorListener);
    }

    public void getInvoice(Class<?> class1, WebServiceType webServiceType, boolean discarded,
                           String doctorId, String locationId, String hospitalId, String patientId, long updatedTime,
                           Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + "?"
                    + HealthCocoConstants.PARAM_PATIENT_ID + patientId
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                    + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + discarded
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(Request.Priority.IMMEDIATE, webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void discardInvoice(Class<?> class1, String invoiceId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = WebServiceType.DISCARD_INVOICE.getUrl() + invoiceId + HealthCocoConstants.PARAM_TAG_DELETE;
        getResponse(WebServiceType.DISCARD_INVOICE, class1, url, null, null, responseListener,
                errorListener);
    }

    public void addInvoice(Class<?> class1, InvoiceRequest invoice,
                           Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_INVOICE;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, invoice, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getReceipt(Class<?> class1, WebServiceType webServiceType, boolean discarded,
                           String doctorId, String locationId, String hospitalId, String patientId, long updatedTime,
                           Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + "?"
                    + HealthCocoConstants.PARAM_PATIENT_ID + patientId
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_HOSPITAL_ID + hospitalId
                    + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + discarded
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(Request.Priority.IMMEDIATE, webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void discardReceipt(Class<?> class1, String receiptId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = WebServiceType.DISCARD_RECEIPT.getUrl() + receiptId + HealthCocoConstants.PARAM_TAG_DELETE + HealthCocoConstants.PARAM_DISCARDED_TRUE;
        getResponse(WebServiceType.DISCARD_RECEIPT, class1, url, null, null, responseListener,
                errorListener);
    }

    public void addReceipt(Class<?> class1, AddReceiptRequest addReceiptRequest,
                           Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_RECEIPT;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, addReceiptRequest, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getPatientAmountDetails(Class<?> class1, WebServiceType webServiceType, String locationId, String hospitalId, String patientId,
                                        Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + locationId + "/" + hospitalId + "/" + patientId;
            getResponse(Request.Priority.IMMEDIATE, webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }


    public void getAppointmentSlotsDetails(Class<?> class1, String doctorId, String locationId, String hospitalId, long date,
                                           Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_APPOINTMENT_TIME_SLOTS;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + doctorId + "/"
                    + locationId + "/"
                    + date + "/";
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
        }
    }

    public void addPrescription(Class<Prescription> class1, PrescriptionRequest prescription,
                                Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_PRESCRIPTION;
        String url = "";
        if (Util.isNullOrBlank(prescription.getUniqueId())) {
            url = webServiceType.getUrl();
        } else {
            webServiceType = webServiceType.UPDATE_PRESCRIPTION;
            url = webServiceType.getUrl() + prescription.getUniqueId() + HealthCocoConstants.PARAM_TAG_UPDATE;
        }
        getResponse(webServiceType, class1, url, prescription,
                null, responseListener, errorListener);
    }

    public void addClinicalNote(Class<?> class1, ClinicalNoteToSend clinicalNotes, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        getResponse(WebServiceType.ADD_CLINICAL_NOTES, class1, WebServiceType.ADD_CLINICAL_NOTES.getUrl(), clinicalNotes, null, responseListener,
                errorListener);
    }

    public void updateClinicalNote(Class<?> class1, ClinicalNoteToSend clinicalNote, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = WebServiceType.UPDATE_CLINICAL_NOTE.getUrl() + clinicalNote.getUniqueId() + HealthCocoConstants.PARAM_TAG_UPDATE;
        getResponse(WebServiceType.UPDATE_CLINICAL_NOTE, class1, url, clinicalNote, null, responseListener,
                errorListener);
    }

    public void discardClinicalNotes(Class<?> class1, String doctorId, String locationId, String hospitalId, String clinicalNoteId, String patientId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = WebServiceType.DISCARD_CLINICAL_NOTES.getUrl() + clinicalNoteId + HealthCocoConstants.PARAM_TAG_DELETE;
        getResponse(WebServiceType.DISCARD_CLINICAL_NOTES, class1, url, null, null, responseListener,
                errorListener);
    }

    public void discardPrescription(Class<?> class1, String doctorId, String locationId, String hospitalId, String prescriptionId, String patientId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = WebServiceType.DISCARD_PRESCRIPTION.getUrl() + prescriptionId + "/" + doctorId + "/" + locationId + "/" + hospitalId + "/" + patientId + HealthCocoConstants.PARAM_TAG_DELETE;
        getResponse(WebServiceType.DISCARD_PRESCRIPTION, class1, url, null, null, responseListener,
                errorListener);
    }

    public void addRecord(Class<Records> class1, Records record, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        getResponse(WebServiceType.ADD_RECORD, class1, WebServiceType.ADD_RECORD.getUrl(), record, null, responseListener,
                errorListener);
    }


    public void addSuggestion(Class<?> class1, WebServiceType webServiceType, Object obj,
                              Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, obj, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void deleteData(WebServiceType webServiceType, Class<?> class1, String doctorId, String locationId, String hospitalId, String uniqueId, boolean discard, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl();
        switch (webServiceType) {
            case DELETE_DRUG:
            case DELETE_DRUG_DOSAGE:
            case DELETE_DIRECTION:
            case DELETE_DISEASE:
            case DELETE_TREATMENT:
            case DELETE_PRESENT_COMPLAINT:
            case DELETE_COMPLAINT:
            case DELETE_HISTORY_OF_PRESENT_COMPLAINT:
            case DELETE_MENSTRUAL_HISTORY:
            case DELETE_OBSTETRIC_HISTORY:
            case DELETE_GENERAL_EXAMINATION:
            case DELETE_SYSTEMIC_EXAMINATION:
            case DELETE_OBSERVATION:
            case DELETE_INVESTIGATION:
            case DELETE_PROVISIONAL_DIAGNOSIS:
            case DELETE_DIAGNOSIS:
            case DELETE_ECG:
            case DELETE_ECHO:
            case DELETE_XRAY:
            case DELETE_NOTES:
            case DELETE_INDICATION_OF_USG:
            case DELETE_PA:
            case DELETE_PS:
            case DELETE_PV:
            case DELETE_PROCEDURE_NOTE:
            case DELETE_HOLTER:
                url = url + uniqueId
                        + "/" + doctorId
                        + "/" + locationId
                        + "/" + hospitalId
                        + HealthCocoConstants.PARAM_TAG_DELETE + "?"
                        + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + discard;
                break;
//            case DELETE_DRUG:
//            case DELETE_DRUG_DOSAGE:
//            case DELETE_DIRECTION:
            case DELETE_REFERENCE:
                url = url + uniqueId + HealthCocoConstants.PARAM_TAG_DELETE + "?"
                        + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + discard
                        + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId;
                break;
        }
        getResponse(webServiceType, class1, url, null, null, responseListener,
                errorListener);
    }

    public void getVideos(Class<?> class1, WebServiceType webServiceType, String doctorId,
                          Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + "?"
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId;
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getPrintSettings(Class<?> class1, String doctorId, String locationId, String hospitalId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_PRINT_SETTINGS;
        String url = webServiceType.getUrl();
        url = url + HealthCocoConstants.PARAM_TAG_ALL
                + doctorId
                + "/" + locationId
                + "/" + hospitalId;

        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
            LocalDataServiceImpl.getInstance(mApp).getPrintSettingsResponse(webServiceType, doctorId, responseListener, errorListener);
        }
    }

    public void addPrintSettings(Class<?> class1, PrintSettings printSettings, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            WebServiceType webServiceType = WebServiceType.ADD_PRINT_SETTINGS;
            getResponse(webServiceType, class1, webServiceType.getUrl(), printSettings, null, responseListener,
                    errorListener);
        }
    }

    public void getDataPermission(Class<?> class1, String doctorId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_DATA_PERMISSION;
        String url = webServiceType.getUrl();
        url = url + doctorId;

        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
            LocalDataServiceImpl.getInstance(mApp).getDataPermission(webServiceType, responseListener, errorListener);
        }
    }

    public void getCalendarEvents(Class<?> class1, List<RegisteredDoctorProfile> registeredDoctorProfileList,
                                  String locationId, String foreignHospitalId, long selectedDate,
                                  long updatedTime, Response.Listener<VolleyResponseBean> responseListener,
                                  GsonRequest.ErrorListener errorListener) {
        getCalendarEvents(class1, registeredDoctorProfileList, locationId, foreignHospitalId,
                DateTimeUtil.getFirstDayOfMonthMilli(selectedDate), DateTimeUtil.getLastDayOfMonthMilli(selectedDate),
                updatedTime, 0, 0, responseListener, errorListener);
    }

    public void getCalendarEvents(Class<?> class1, List<RegisteredDoctorProfile> registeredDoctorProfileList,
                                  String locationId, String foreignHospitalId, long startDate, long endDate,
                                  long updatedTime, int pageNo, int size, Response.Listener<VolleyResponseBean> responseListener,
                                  GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_CALENDAR_EVENTS;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {

            String url = webServiceType.getUrl();

            if (!Util.isNullOrEmptyList(registeredDoctorProfileList))
                for (RegisteredDoctorProfile doctorProfile : registeredDoctorProfileList) {

                    url = url + HealthCocoConstants.PARAM_MATRIX_DOCTOR_ID + doctorProfile.getUserId();
                }
            url = url + HealthCocoConstants.PARAM_MATRIX_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_HOSPITAL_ID + foreignHospitalId
                    + HealthCocoConstants.PARAM_FROM + startDate
                    + HealthCocoConstants.PARAM_TO + endDate
                    + HealthCocoConstants.PARAM_UPDATED_TIME + +updatedTime;
            if (pageNo >= 0 && size > 0) {
                url = url + HealthCocoConstants.PARAM_PAGE + pageNo + HealthCocoConstants.PARAM_SIZE + size;
            }
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getEvents(Class<?> class1,
                          String locationId, long startDate, long endDate,
                          long updatedTime, int pageNo, int size, Response.Listener<VolleyResponseBean> responseListener,
                          GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_EVENTS;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {

            String url = webServiceType.getUrl();

           /* if (!Util.isNullOrEmptyList(registeredDoctorProfileList))
                for (RegisteredDoctorProfile doctorProfile : registeredDoctorProfileList) {

                    url = url + HealthCocoConstants.PARAM_MATRIX_DOCTOR_ID + doctorProfile.getUserId();
                }*/
            url = url + HealthCocoConstants.TAG_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_FROM + startDate
                    + HealthCocoConstants.PARAM_TO + endDate
                    + HealthCocoConstants.PARAM_UPDATED_TIME + +updatedTime;
            if (pageNo >= 0 && size > 0) {
                url = url + HealthCocoConstants.PARAM_PAGE + pageNo + HealthCocoConstants.PARAM_SIZE + size;
            }
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void changeAppointmentStatus(Class<?> class1, String doctorId, String locationId, String hospitalId, String patientId, String appointmentId, String status, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.CHANGE_APPOINTMENT_STATUS;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + doctorId
                    + "/" + locationId
                    + "/" + hospitalId
                    + "/" + patientId
                    + "/" + appointmentId
                    + "/" + status;/*
                    + HealthCocoConstants.PARAM_IS_OBJECT_REQUIRED;*/

            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getPatientCardPdf(Class<?> class1, PrintPatientCardRequest printPatientCardRequest,
                                  Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_PATIENT_CARD_PDF_URL;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, printPatientCardRequest, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getBlogsList(WebServiceType webServiceType, Class<?> class1, String selectedPatientUserId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
//                    + HealthCocoConstants.PARAM_PAGE_AND + pageNum
//                    + HealthCocoConstants.PARAM_SIZE + maxSize;
//            if (selectedPatientUserId != null)
//                url = url + HealthCocoConstants.PARAM_USER_ID + selectedPatientUserId;
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void getHealthBlogDetail(Class<?> class1, String blogId, String selectedPatientUserId, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_HEALTH_BLOG_BY_ID;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + blogId;
//            if (selectedPatientUserId != null)
//                url = url
//                        + "?"
//                        + HealthCocoConstants.PARAM_USER_ID + selectedPatientUserId;
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    public void addAppointmentFeedback(Class<?> class1, AppointmentFeedback appointmentFeedback,
                                       Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_PRESCRIPTION_APPOINTMENT_FEEDBACK;
        checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            getResponse(webServiceType, class1, url, appointmentFeedback, null, responseListener,
                    errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }


}
