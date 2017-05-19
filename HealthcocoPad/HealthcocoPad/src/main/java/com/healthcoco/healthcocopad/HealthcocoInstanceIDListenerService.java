package com.healthcoco.healthcocopad;

import android.os.Handler;
import android.util.Log;

import com.android.volley.Response;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.GCMRequest;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.enums.AppType;
import com.healthcoco.healthcocopad.enums.DeviceType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by neha on 02/04/16.
 */
public class HealthcocoInstanceIDListenerService extends FirebaseInstanceIdService implements GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {

    private static final String TAG = "MyInstanceIDLS";
    private static final int REQUEST_PHONE_STATE = 101;
    private Handler handler = new Handler();
    private Runnable runnable = null;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
//        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
//        Intent intent = new Intent(this, RegistrationIntentService.class);
//        startService(intent);
        // Get updated InstanceID token.
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToSerOver(refreshedToken);
    }

    /**
     * Sending registration Id and rest details to server
     *
     * @param token : token(RegistrationId) received from GCM server after successfull registration
     */
    private void sendRegistrationToSerOver(String token) {
        LoginResponse doctor = LocalDataServiceImpl.getInstance((HealthCocoApplication) this.getApplicationContext()).getDoctor();
        GCMRequest gcmRequest = new GCMRequest();
        gcmRequest.setDeviceId(Util.getDeviceId(this) + AppType.HEALTHCOCO_PAD);
        gcmRequest.setPushToken(token);
        gcmRequest.setDeviceType(DeviceType.ANDROID_PAD);
        gcmRequest.setRole(RoleType.DOCTOR);
        if (doctor != null && doctor.getUser() != null) {
            ArrayList<String> userIdsList = new ArrayList<>();
            userIdsList.add(doctor.getUser().getUniqueId());
            gcmRequest.setUserIds(userIdsList);
        }
        LocalDataServiceImpl.getInstance((HealthCocoApplication) this.getApplicationContext()).addGCMRequest(gcmRequest);
        WebDataServiceImpl.getInstance((HealthCocoApplication) this.getApplicationContext()).sendGcmRegistrationId(true);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {

    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {

    }

    @Override
    public void onResponse(VolleyResponseBean response) {

    }
    // [END refresh_token]
}