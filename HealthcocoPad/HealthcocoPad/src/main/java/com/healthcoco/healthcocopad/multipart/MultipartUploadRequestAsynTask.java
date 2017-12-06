package com.healthcoco.healthcocopad.multipart;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.utilities.DevConfig;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LocalDatabaseUtils;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neha on 28/04/17
 */

public class MultipartUploadRequestAsynTask extends AsyncTask<Object, Object, String> {
    private static final String TAG = MultipartUploadRequestAsynTask.class.getSimpleName();
    private static final String KEY_DATA = "data";
    private static final String KEY_FILE = "file";
    private final HealthCocoActivity mActivity;
    private final GsonRequest.ErrorListener errorListener;
    private final WebServiceType webServiceType;
    private final Response.Listener<VolleyResponseBean> responseListener;
    private final Class<?> responseClass;
    private String filePath;
    private HashMap<String, String> params;

    public MultipartUploadRequestAsynTask(HealthCocoActivity mActivity, Class<?> class1, WebServiceType webServiceType, Object object, String filepath,
                                          Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        this.mActivity = mActivity;
        this.webServiceType = webServiceType;
        this.filePath = filepath;
        this.responseListener = responseListener;
        this.errorListener = errorListener;
        this.responseClass = class1;
        if (object != null) {
            String body = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED, Modifier.PUBLIC).create().toJson(object);
            LogUtils.LOGD(TAG, "Body : " + body);
            params = new HashMap<>();
            params.put(KEY_DATA, body);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        LogUtils.LOGD(TAG, "filePath Before Extension " + filePath);
        if (filePath != null)
            filePath = ImageUtil.getCroppedFilePath(filePath);
//        LogUtils.LOGD(TAG, "filePath After Extension " + filePath);
    }

    @Override
    protected String doInBackground(Object... objects) {
        String DEFAULT_MIME_TYPE = "image/png";
        HttpURLConnection connection;
        DataOutputStream outputStream;
        InputStream inputStream;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result;

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;


        try {
            URL url = new URL(DevConfig.buildType.getServerUrl() + webServiceType.getUrl());
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty("Authorization", "Basic " + Util.getConvertedBase64Value(DevConfig.buildType.getOAuthValue()));

            outputStream = new DataOutputStream(connection.getOutputStream());
            if (!Util.isNullOrBlank(filePath)) {
                String[] q = filePath.split("/");
                int idx = q.length - 1;

                File file = new File(filePath);
                FileInputStream fileInputStream = new FileInputStream(file);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + KEY_FILE + "\"; filename=\"" + ImageUtil.appendFileExtensionIfNotPresent(q[idx]) + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: " + DEFAULT_MIME_TYPE + lineEnd);
                outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

                outputStream.writeBytes(lineEnd);


                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                outputStream.writeBytes(lineEnd);
                fileInputStream.close();
            }
            // Upload POST Data
//            Iterator<String> keys = params.keySet().iterator();
            for (String key :
                    params.keySet()) {
                String value = params.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            if (200 != connection.getResponseCode()) {
                String errorText = "Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage();
                LogUtils.LOGD(TAG, errorText);
                return errorText;
            }

            inputStream = connection.getInputStream();

            result = convertStreamToString(inputStream);

            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        LogUtils.LOGD(TAG, "Response : " + result);
        String errorMsg = "";
        if (!TextUtils.isEmpty(result)) {
            // cast data and datalist to the responseClass
            if (result.contains(LocalDatabaseUtils.ID))
                result = result.replace(LocalDatabaseUtils.ID, LocalDatabaseUtils.ID_REPLACED);
            if (result.contains(LocalDatabaseUtils.FROM))
                result = result.replace(LocalDatabaseUtils.FROM, LocalDatabaseUtils.FROM_VALUE);
            if (result.contains(LocalDatabaseUtils.TO))
                result = result.replace(LocalDatabaseUtils.TO, LocalDatabaseUtils.TO_VALUE);

            Object parsedResult = getParsedResult(result);
            if (parsedResult != null) {
                if (parsedResult instanceof String)
                    errorMsg = (String) parsedResult;
                else if (parsedResult instanceof VolleyResponseBean) {
                    VolleyResponseBean responseObject = (VolleyResponseBean) parsedResult;
                    if (!Util.isNullOrBlank(responseObject.getErrMsg())) {
                        errorMsg = responseObject.getErrMsg();
                        errorListener.onErrorResponse(responseObject, errorMsg);
                    } else {
                        responseListener.onResponse((VolleyResponseBean) parsedResult);
                        return;
                    }
                }
            }
            if (Util.isNullOrBlank(errorMsg)) {
                errorMsg = mActivity.getResources().getString(R.string.error_creating_request);
            }
            Util.showToast(mActivity.getApplicationContext(), errorMsg);
            errorListener.onErrorResponse(null, null);

        }
    }

    private Object getParsedResult(String result) {
        try {
            VolleyResponseBean response = new Gson().fromJson(result, VolleyResponseBean.class);
            if (response != null && responseClass != null) {
                if (response.getData() != null) {
                    if (response.getData() instanceof Map)
                        response.setData(GsonRequest.getObjectFromLinkedTreeMap(responseClass, response.getData()));
                }
                response.setWebServiceType(webServiceType);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}