package com.healthcoco.healthcocoplus.services;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.healthcoco.healthcocoplus.HealthCocoApplication;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.utilities.DevConfig;
import com.healthcoco.healthcocoplus.utilities.LocalDatabaseUtils;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shreshtha on 20-01-2017.
 */

public class GsonRequest extends JsonRequest<VolleyResponseBean> {
    private static final String TAG = GsonRequest.class.getSimpleName();

    public static final String SPACE = " ";
    public static final String SPACE_REPLACED = "%20";

    public static final int DEFAULT_TIME_OUT = 90 * 1000;   //90 seconds
    //    private static final String OAUTH_VALUE = "Basic aGVhbHRoY29jb0AxNjoxR1dMRjlPUk1LOUI4UUZW";
    private HealthCocoApplication mApp;
    private Priority priority;
    private HashMap<String, String> headersAdded = new HashMap<String, String>();
    private Response.Listener<VolleyResponseBean> listener;
    private WebServiceType webServiceType;
    private Class<?> responseClass;
    private static Gson gson;
    private ErrorListener errorListener;
    private ArrayList<Object> dataList = new ArrayList<Object>();

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

    /**
     * @param mApp
     * @param priority
     * @param webServiceType
     * @param classUser
     * @param url
     * @param requestBody
     * @param headers
     * @param listener
     * @param errorListener
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
    protected Response<VolleyResponseBean> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            VolleyResponseBean response = new VolleyResponseBean();
            String jsonString = new String(networkResponse.data, "UTF-8");
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

    public interface ErrorListener {
        /**
         * Callback method that an error has occurred with the provided error
         * code, optional user-readable message and web service type for which
         * the error occurred.
         */
        public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage);

        public void onNetworkUnavailable(WebServiceType webServiceType);
    }
}
