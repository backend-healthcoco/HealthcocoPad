package com.healthcoco.healthcocoplus.services.impl;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.google.gson.GsonBuilder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoApplication;
import com.healthcoco.healthcocoplus.bean.DoctorProfileToSend;
import com.healthcoco.healthcocoplus.bean.VersionCheckRequest;
import com.healthcoco.healthcocoplus.bean.request.ClinicImageToSend;
import com.healthcoco.healthcocoplus.bean.request.ProfessionalMembershipRequest;
import com.healthcoco.healthcocoplus.bean.request.ProfessionalStatementRequest;
import com.healthcoco.healthcocoplus.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocoplus.bean.server.DoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.DrugType;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.Profession;
import com.healthcoco.healthcocoplus.bean.server.Reference;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.bean.server.TempTemplate;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.UserGroups;
import com.healthcoco.healthcocoplus.dialogFragment.AddEditDoctorExperienceDialogFragment;
import com.healthcoco.healthcocoplus.dialogFragment.AddEditDoctorMembershipDialogFragment;
import com.healthcoco.healthcocoplus.dialogFragment.AddEditDoctorStatementDialogFragment;
import com.healthcoco.healthcocoplus.enums.BooleanTypeValues;
import com.healthcoco.healthcocoplus.enums.LocalTabelType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LocalDatabaseUtils;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shreshtha on 20-01-2017.
 */

public class WebDataServiceImpl {
    private static final String TAG = Util.class.getSimpleName();
    private static WebDataServiceImpl mInstance;
    private static HealthCocoApplication mApp;

    private WebDataServiceImpl() {
    }

    public static WebDataServiceImpl getInstance(HealthCocoApplication application) {
        if (mInstance == null) {
            mApp = application;
            mInstance = new WebDataServiceImpl();
        }
        return mInstance;
    }

    public void checkVersion(Class<Integer> class1, VersionCheckRequest versionCheckRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.VERSION_CONTROL_CHECK;
        Util.checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl();
            LogUtils.LOGI(TAG, url);
            getResponse(webServiceType, class1, url, versionCheckRequest, null, responseListener, errorListener);
        } else {
            showUserOffline(webServiceType, responseListener);
        }
    }

    private void showUserOffline(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setUserOnline(false);
        volleyResponseBean.setDataFromLocal(true);
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
            mApp.cancelAllPendingRequests();
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
        Util.checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + HealthCocoConstants.PARAM_IS_MOBILE_APP;
            getResponse(webServiceType, class1, url, user, null, responseListener,
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
        Util.checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, object, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void getContactsList(Class<RegisteredPatientDetailsUpdated> class1, String doctorId,
                                String hospitalId, String locationId, long updatedTime, Response.Listener<VolleyResponseBean> responseListener,
                                GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_CONTACTS;
        Util.checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl() + HealthCocoConstants.PARAM_HOSPITAL_ID
                    + hospitalId + HealthCocoConstants.PARAM_LOCATION_ID + locationId
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
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
        Util.checkNetworkStatus(mApp.getApplicationContext());
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
        Util.checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, null, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
            LocalDataServiceImpl.getInstance(mApp).getClinicDetailsResponse(webServiceType, locationId, responseListener, errorListener);
        }
    }

    public void getDrugType(WebServiceType webServiceType, Class<DrugType> class1, boolean isDiscarded, String doctorId, Long updatedTime, Response.Listener<VolleyResponseBean> responseListener,
                            GsonRequest.ErrorListener errorListener) {
        Util.checkNetworkStatus(mApp.getApplicationContext());
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
        Util.checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_DISCARDED_TRUE
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId
                    + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp).getUserGroups(webServiceType, patientsAssignedGroupIdList, doctorId, locationId, hospitalId, responseListener, errorListener);

        }
    }

    public void getProfession(Class<Profession> class1, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_PROFESSION;
        Util.checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, webServiceType.getUrl(), null, null, responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp).getProfessionList(webServiceType, responseListener, errorListener);
        }
    }

    public void getReference(Class<Reference> class1, String doctorId, long updatedTime, BooleanTypeValues isDiscarded, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_REFERENCE;
        Util.checkNetworkStatus(mApp.getApplicationContext());
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

    public void getClinicalNoteSuggestionsList(Class<?> class1, WebServiceType webServiceType, String doctorId, Long latestUpdatedTime, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        String url = webServiceType.getUrl() + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId + HealthCocoConstants.PARAM_UPDATED_TIME + latestUpdatedTime;
        getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);

    }

    public void getBloodGroup(Class<?> class1, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_BLOOD_GROUP;
        Util.checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, webServiceType.getUrl(), null, null, responseListener, errorListener);
        } else {
            LocalDataServiceImpl.getInstance(mApp).getBloodGroup(webServiceType, responseListener, errorListener);
        }
    }

    public void getDiseaseList(Class<?> class1, String doctorId, Long updatedTime, ArrayList<String> diseaseIds, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.GET_DISEASE_LIST;
        Util.checkNetworkStatus(mApp.getApplicationContext());
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
        Util.checkNetworkStatus(mApp.getApplicationContext());
        if (HealthCocoConstants.isNetworkOnline) {
            WebServiceType webServiceType = WebServiceType.GET_TEMPLATES_LIST;
            String url = webServiceType.getUrl()
                    + HealthCocoConstants.PARAM_DISCARDED_AMPERCENT + false
                    + HealthCocoConstants.PARAM_DOCTOR_ID + doctorId + HealthCocoConstants.PARAM_UPDATED_TIME + updatedTime;
            getResponse(webServiceType, class1, url, null, null, responseListener, errorListener);
        } else {
//            LocalDataServiceImpl.getInstance(mApp)
//                    .getTemplatesListPageWise(WebServiceType.GET_TEMPLATES_LIST, doctorId, false, 0l,
//                            0, TemplatesListFragment.MAX_SIZE, "", responseListener, errorListener);
        }
    }

    public void getCities(Class<?> class1, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        Util.checkNetworkStatus(mApp.getApplicationContext());
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
        Util.checkNetworkStatus(mApp);
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
        Util.checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, profile, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateEducation(Class<?> class1, DoctorProfile doctorProfile, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_EDUCATION;
        String url = webServiceType.getUrl();
        Util.checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, doctorProfile, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void getMasterDataFromSolr(WebServiceType webServiceType, Class<?> class1, String locationId, String hospitalId, int pageNum, int size, String searchTerm, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        Util.checkNetworkStatus(mApp.getApplicationContext());
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


    public void addUpdateRegistrationDetail(Class<?> class1, DoctorProfile doctorProfile, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_REGISTRATION_DETAIL;
        String url = webServiceType.getUrl();
        Util.checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, doctorProfile, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateAchievments(Class<?> class1, DoctorProfile doctorProfile, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_ACHIEVEMENTS_DETAIL;
        String url = webServiceType.getUrl();
        Util.checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, doctorProfile, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateExperirnce(Class<?> class1, DoctorProfile doctorProfile, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_EXPERIENCE_DETAIL;
        String url = webServiceType.getUrl();
        Util.checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, doctorProfile, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateProfessionalMembership(Class<?> class1, ProfessionalMembershipRequest membershipRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_PROFESSIONAL_MEMBERSHIP_DETAIL;
        String url = webServiceType.getUrl();
        Util.checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, membershipRequest, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateProfessionalStatement(Class<?> class1, ProfessionalStatementRequest statementRequest, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_PROFESSIONAL_STATEMENT_DETAIL;
        String url = webServiceType.getUrl();
        Util.checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, statementRequest, null, responseListener, errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

    public void addUpdateGeneralInfo(Class<?> class1, DoctorClinicProfile doctorClinicProfile, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        WebServiceType webServiceType = WebServiceType.ADD_UPDATE_GENERAL_INFO;
        String url = webServiceType.getUrl();
        Util.checkNetworkStatus(mApp);
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
        Util.checkNetworkStatus(mApp);
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
        Util.checkNetworkStatus(mApp);
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
        Util.checkNetworkStatus(mApp);
        if (HealthCocoConstants.isNetworkOnline) {
            getResponse(webServiceType, class1, url, imageToSend, null, responseListener,
                    errorListener);
        } else {
            errorListener.onNetworkUnavailable(webServiceType);
        }
    }

}
