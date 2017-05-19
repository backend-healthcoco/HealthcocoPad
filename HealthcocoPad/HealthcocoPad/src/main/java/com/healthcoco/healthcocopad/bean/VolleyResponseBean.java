package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Shreshtha on 20-01-2017.
 */
public class VolleyResponseBean {
    private WebServiceType webServiceType;
    protected boolean isUserOnline = true;
    private Map<String, String> headers;

    // success response keys
    private Object data;
    private ArrayList<Object> dataList = new ArrayList<Object>();
    private String errMsg;

    protected boolean isFromLocalAfterApiSuccess;
    private LocalBackgroundTaskType localBackgroundTaskType;
    protected boolean isDataFromLocal;

    public void setFromLocalAfterApiSuccess(boolean fromLocalAfterApiSuccess) {
        isFromLocalAfterApiSuccess = fromLocalAfterApiSuccess;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public ArrayList<Object> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Object> dataList) {
        this.dataList = dataList;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public WebServiceType getWebServiceType() {
        return webServiceType;
    }

    public void setWebServiceType(WebServiceType webServiceType) {
        this.webServiceType = webServiceType;
    }

    public boolean isDataFromLocal() {
        return isDataFromLocal;
    }

    public void setIsDataFromLocal(boolean dataFromLocal) {
        isDataFromLocal = dataFromLocal;
    }

    public boolean isUserOnline() {
        return isUserOnline;
    }

    public void setIsUserOnline(boolean userOnline) {
        isUserOnline = userOnline;
    }

    public boolean isValidDataList(VolleyResponseBean response) {
        if (response != null && !Util.isNullOrEmptyList(response.getDataList()))
            return true;
        return false;
    }

    public boolean isValidData(VolleyResponseBean response) {
        if (response != null && response.getData() != null)
            return true;
        return false;
    }

    public boolean isFromLocalAfterApiSuccess() {
        return isFromLocalAfterApiSuccess;
    }

    public void setIsFromLocalAfterApiSuccess(boolean isFromLocalAfterApiSuccess) {
        this.isFromLocalAfterApiSuccess = isFromLocalAfterApiSuccess;
    }

    public LocalBackgroundTaskType getLocalBackgroundTaskType() {
        return localBackgroundTaskType;
    }

    public void setLocalBackgroundTaskType(LocalBackgroundTaskType localBackgroundTaskType) {
        this.localBackgroundTaskType = localBackgroundTaskType;
    }
}
