package com.healthcoco.healthcocopad.services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.SyncAll;
import com.healthcoco.healthcocopad.enums.SyncAllType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.ClinicalProfileFragment;
import com.healthcoco.healthcocopad.fragments.PatientProfileDetailFragment;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DevConfig;
import com.healthcoco.healthcocopad.utilities.LocalDatabaseUtils;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shreshtha on 20-01-2017.
 */

public class GsonRequest extends JsonRequest<VolleyResponseBean> {

    public static final String SPACE = " ";
    public static final String SPACE_REPLACED = "%20";

    public static final int DEFAULT_TIME_OUT = 90 * 1000;   //90 seconds
    //    private static final String OAUTH_VALUE = "Basic aGVhbHRoY29jb0AxNjoxR1dMRjlPUk1LOUI4UUZW";
    private HealthCocoApplication mApp;
    private Priority priority;

    public interface ErrorListener {
        /**
         * Callback method that an error has occurred with the provided error
         * code, optional user-readable message and web service type for which
         * the error occurred.
         */
        public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage);

        public void onNetworkUnavailable(WebServiceType webServiceType);
    }

    private static final String TAG = GsonRequest.class.getSimpleName();

    private HashMap<String, String> headersAdded = new HashMap<String, String>();
    private Response.Listener<VolleyResponseBean> listener;
    private WebServiceType webServiceType;
    private Class<?> responseClass;
    private static Gson gson;
    private ErrorListener errorListener;

    /**
     * Constructor
     *
     * @param webServiceType : represents which service API type is called
     * @param classUser      : : class in which the response data is supposed to cast
     * @param url            : url of service type
     * @param requestBody    : body which is to be attached along with the request
     * @param headers        : extra headers that are to be included along with API call
     * @param listener       : listener where the response is to be passed
     * @param errorListener  : listener where the error is to be fetched.
     */
    public GsonRequest(HealthCocoApplication mApp, Priority priority, WebServiceType webServiceType, Class<?> classUser, String url, String requestBody,
                       Map<String, String> headers, Response.Listener<VolleyResponseBean> listener, ErrorListener errorListener) {
        // pass default error listener as null
        // since deliverError is overridden
        // use errorListener to deliver the error
        super(webServiceType.getMethodType(), DevConfig.buildType.getServerUrl() + url, requestBody, listener, null);
        this.mApp = mApp;
        this.priority = priority;
        LogUtils.LOGD(TAG, "GsonRequest Url " + webServiceType + " " + DevConfig.buildType.getServerUrl() + url);
        LogUtils.LOGD(TAG, "GsonRequest body " + webServiceType + " " + requestBody + "requestType " + webServiceType.getMethodType());
        this.gson = new GsonBuilder().create();
        this.webServiceType = webServiceType;
        this.listener = listener;
        if (headers != null)
            this.headersAdded.putAll(headers);
        this.headersAdded.put(DevConfig.buildType.getOAUTH_KEY(), "Basic " + Util.getConvertedBase64Value(DevConfig.buildType.getOAuthValue()));
        this.responseClass = classUser;
        this.errorListener = errorListener;

    }

    @Override
    public Priority getPriority() {
        if (priority != null)
            return priority;
        return super.getPriority();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
//        headersAdded.put(OAUTH_KEY, "Basic " + Util.getConvertedBase64Value(OAUTH_VALUE));
        if (webServiceType.getMethodType() == Method.DELETE) {
            headersAdded.put("content-type", "application/json; charset=utf-8");
            headersAdded.put("Accept", "application/json");
        }
        if (!Util.isNullOrEmptyList(headersAdded))
            return headersAdded;
        return super.getHeaders();
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        headersAdded.put("content-type", "application/json; Charset=UTF-8");
        headersAdded.put("Accept", "application/json;  Charset=UTF-8");
//        headersAdded.put(OAUTH_KEY, "Basic " + Util.getConvertedBase64Value(OAUTH_VALUE));
        return headersAdded;
    }

    public Map<String, String> getHeadersAdded() {
        return headersAdded;
    }

    @Override
    protected void deliverResponse(VolleyResponseBean response) {
        if (listener != null) {
            listener.onResponse(response);
        }
        updateSyncAllTable(webServiceType);
        sendBroadCasts(webServiceType);
    }

    @Override
    public void deliverError(VolleyError error) {
        mApp.removeVolleyRequest(this);
        String errorMessage = "Error";
        VolleyResponseBean responseErrorObject = new VolleyResponseBean();
        if (errorListener != null) {
            try {
                if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
                    String dataReceived = new String(error.networkResponse.data);
                    responseErrorObject = gson.fromJson(dataReceived, VolleyResponseBean.class);
                }
            } catch (Exception e) {
            }
            // just for loggin error displaying this block
            if ((responseErrorObject == null || (responseErrorObject != null) && Util.isNullOrBlank(responseErrorObject.getErrMsg())) && error != null) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    LogUtils.LOGE(TAG, "GsonRequest. HTTP Status Code:" + networkResponse.statusCode);
                }

                if (error instanceof TimeoutError) {
                    errorMessage = "TimeoutError";
                    LogUtils.LOGE(TAG, "GsonRequest. TimeoutError");
                } else if (error instanceof NoConnectionError) {
                    errorMessage = "NoConnectionError";
                    LogUtils.LOGE(TAG, "GsonRequest. NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    errorMessage = "AuthFailureError";
                    LogUtils.LOGE(TAG, "GsonRequest. AuthFailureError");
                } else if (error instanceof ServerError) {
                    errorMessage = "ServerError";
                    LogUtils.LOGE(TAG, "GsonRequest. ServerError");
                } else if (error instanceof NetworkError) {
                    errorMessage = "NetworkError";
                    LogUtils.LOGE(TAG, "GsonRequest. NetworkError");
                } else if (error instanceof ParseError) {
                    errorMessage = "ParseError";
                    LogUtils.LOGE(TAG, "GsonRequest. ParseError");
                }
            }
            if (responseErrorObject == null)
                responseErrorObject = new VolleyResponseBean();
            responseErrorObject.setWebServiceType(webServiceType);
            // deliver error as well the request type
            // to identify which type of request failed
            errorListener.onErrorResponse(responseErrorObject, errorMessage);
        }
        String errorMsg = errorMessage;
        if (responseErrorObject != null && !Util.isNullOrBlank(responseErrorObject.getErrMsg())) {
            errorMsg = responseErrorObject.getErrMsg();
        }
        if (webServiceType != WebServiceType.SEND_GCM_REGISTRATION_ID && webServiceType != WebServiceType.VERSION_CONTROL_CHECK)
            Util.showToast(mApp.getApplicationContext(), errorMsg);
    }

    @Override
    protected Response<VolleyResponseBean> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            mApp.removeVolleyRequest(this);
            VolleyResponseBean response = new VolleyResponseBean();
            String jsonString = new String(networkResponse.data, "UTF-8");
//            String jsonString = new String(networkResponse.data,
//                    HttpHeaderParser.parseCharset(networkResponse.headers));
            LogUtils.LOGD(TAG, "GsonRequest Response " + webServiceType + " " + jsonString);
            Map<String, String> responseHeaders = networkResponse.headers;
            if (!TextUtils.isEmpty(jsonString)) {
                // cast data and datalist to the responseClass
                if (jsonString.contains(LocalDatabaseUtils.ID))
                    jsonString = jsonString.replace(LocalDatabaseUtils.ID, LocalDatabaseUtils.ID_REPLACED);
                if (jsonString.contains(LocalDatabaseUtils.FROM))
                    jsonString = jsonString.replace(LocalDatabaseUtils.FROM, LocalDatabaseUtils.FROM_VALUE);
                if (jsonString.contains(LocalDatabaseUtils.TO))
                    jsonString = jsonString.replace(LocalDatabaseUtils.TO, LocalDatabaseUtils.TO_VALUE);
                response = gson.fromJson(jsonString, VolleyResponseBean.class);
                if (response != null && responseClass != null) {
                    if (response.getData() != null) {
                        if (response.getData() instanceof Map)
                            response.setData(getObjectFromLinkedTreeMap(responseClass, response.getData()));
                    }
                    if (!Util.isNullOrEmptyList(response.getDataList()) && response.getDataList().get(0) instanceof Map) {
                        ArrayList<Object> list = new ArrayList<Object>();
                        for (Object object : response.getDataList()) {
                            list.add(getObjectFromLinkedTreeMap(responseClass, object));
                        }
                        response.setDataList(list);
                    }
                }
            }
            response.setHeaders(responseHeaders);
            response.setWebServiceType(webServiceType);

//            listener.onResponse(response);
            Response<VolleyResponseBean> result = Response.success(response,
                    HttpHeaderParser.parseCacheHeaders(networkResponse));

            return result;
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    private void sendBroadCasts(WebServiceType webServiceType) {
        try {
            Intent intent = new Intent();
            switch (webServiceType) {
//                case UPDATE_PRESCRIPTION:
//                case UPDATE_CLINICAL_NOTE:
                case ADD_PRESCRIPTION:
                case ADD_RECORD:
                case ADD_CLINICAL_NOTES:
                case ADD_VISIT:
//                    intent.setAction(PatientDetailVisitFragment.INTENT_GET_VISITS_LIST);
                    break;

                case ADD_UPDATE_CLINIC_ADDRESS:
                case ADD_CLINIC_IMAGE:
                case DELETE_CLINIC_IMAGE:
                case ADD_UPDATE_CLINIC_CONTACT:
                case ADD_UPDATE_CLINIC_HOURS:
                    intent.setAction(ClinicalProfileFragment.INTENT_GET_CLINIC_PROFILE_DETAILS);
                    break;

                case ADD_MEDICAL_HISTORY:
                case ADD_FAMILY_HISTORY:
                    intent.setAction(PatientProfileDetailFragment.INTENT_GET_MEDICAL_PERSONAL_FAMILY_LIST);
                    break;
                case GET_PRESCRIPTION:
                case GET_REPORTS_UPDATED:
                case GET_CLINICAL_NOTES:
                    sendBroadcastToHistory();
            }
            if (intent != null && intent.getAction() != null)
                LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBroadcastToHistory() {
        try {
            Intent intent = new Intent();
            intent.setAction(PatientProfileDetailFragment.INTENT_GET_HISTORY_LIST);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSyncAllTable(WebServiceType webServiceType) {
        SyncAll syncAll = new SyncAll();
        syncAll.setLastSyncedTime(new Date().getTime());
        switch (webServiceType) {
            case GET_CONTACTS:
                syncAll.setSyncAllType(SyncAllType.CONTACT);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            case GET_GROUPS:
                syncAll.setSyncAllType(SyncAllType.GROUP);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            case GET_DISEASE_LIST:
                syncAll.setSyncAllType(SyncAllType.HISTORY);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            case GET_DRUGS_LIST_CUSTOM:
                syncAll.setSyncAllType(SyncAllType.DRUG_CUSTOM);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            case GET_DRUG_DOSAGE:
                syncAll.setSyncAllType(SyncAllType.FREQUENCY);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            case GET_DIRECTION:
                syncAll.setSyncAllType(SyncAllType.DIRECTION);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            case GET_DIAGRAMS_LIST:
                syncAll.setSyncAllType(SyncAllType.NOTES_DIAGRAM);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            case GET_COMPLAINT_SUGGESTIONS:
            case GET_INVESTIGATION_SUGGESTIONS:
            case GET_OBSERVATION_SUGGESTIONS:
            case GET_DIAGNOSIS_SUGGESTIONS:
                syncAll.setSyncAllType(SyncAllType.CLINICAL_NOTES_DATA);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            case GET_REFERENCE:
                syncAll.setSyncAllType(SyncAllType.REFERENCES);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            case GET_DRUG_TYPE:
                syncAll.setSyncAllType(SyncAllType.DRUG_TYPE);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            case GET_DURATION_UNIT:
                syncAll.setSyncAllType(SyncAllType.DRUG_DURATION_UNIT);
                LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
                break;
            default:
                break;
        }

    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    /**
     * converting a linked map object to the specified class object
     *
     * @param classType : response class
     * @param object    : object that is to be converted
     */
    public static Object getObjectFromLinkedTreeMap(Class<?> classType, Object object) {
        try {
            String dataJson = gson.toJson(object, Map.class);
            if (!TextUtils.isEmpty(dataJson)) {
                return gson.fromJson(dataJson, classType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
