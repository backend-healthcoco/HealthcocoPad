package com.healthcoco.healthcocoplus.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.Display;

import com.android.volley.Response;
import com.crashlytics.android.Crashlytics;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.bean.AppType;
import com.healthcoco.healthcocoplus.bean.DeviceType;
import com.healthcoco.healthcocoplus.bean.VersionCheckRequest;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.VersionCheckType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

import io.fabric.sdk.android.Fabric;

/**
 * Created by neha on 18/01/17.
 */
public class SplashScreenActivity extends HealthCocoActivity implements GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    private static final String TAG = Util.class.getSimpleName();
    public static final String TAG_VERSION_CODE = "versionCode";
    private Handler handler;
    private static final int SPLASH_TIME = 2000;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initScreenDimensions();
        init();
    }

    private void initScreenDimensions() {
//        Util.printScreenDensity(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
//        ScreenDimensions.SCREEN_WIDTH = size.x;
//        ScreenDimensions.SCREEN_HEIGHT = size.y;
    }

    /**
     * if Online : will check for version online and perform screen navigation accordingly
     * else :  will move to next screen and work offline
     */
    private void init() {
        Util.checkNetworkStatus(this);
//        if (HealthCocoConstants.isNetworkOnline) {
//            checkVersion();
//        } else {
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    launchNextActivity();
                }
            };
            handler.postDelayed(runnable, SPLASH_TIME);
//        }
    }

    /**
     * Method to check Version of application.
     */
    private void checkVersion() {
        String versionName = Util.getVersionName(this);
        if (!Util.isNullOrBlank(versionName)) {
            String[] parts = versionName.split("\\.");
            LogUtils.LOGD(TAG, "parts size " + parts.length);
            Util.checkNetworkStatus(this);
            if (HealthCocoConstants.isNetworkOnline) {
                WebDataServiceImpl.getInstance(mApp).checkVersion(Integer.class, new VersionCheckRequest(AppType.HEALTHCOCO_PLUS, DeviceType.ANDROID, parts), this, this);
            }
        }
    }

    /**
     * Method to Launch next activity
     */
    private void launchNextActivity() {
        Intent nextActivityIntent = new Intent(this, CommonActivity.class);
        nextActivityIntent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.LOGIN_SIGN_UP.ordinal());
        startActivity(nextActivityIntent);
        finish();
    }

    private void saveVersionCode() {
       /* try {
            int lastVersionCode = Util.getVersionCodeFromPreferences(this);
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int currentVersionCode = packageInfo.versionCode;
            if (lastVersionCode < currentVersionCode) {
                RegisteredPatientDetailsUpdated.deleteAll(RegisteredPatientDetailsUpdated.class);
                Util.addVersionCodeInPreferences(this, currentVersionCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        super.onErrorResponse(volleyResponseBean, errorMessage);
        launchNextActivity();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(this, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case VERSION_CONTROL_CHECK:
                    if (response.getData() != null) {
//                        initGCM();
                        Integer versionCheckFlag = Util.getIntValue(response.getData());
                        if (versionCheckFlag == VersionCheckType.MAJOR.getVersionFlag() || versionCheckFlag == VersionCheckType.MINOR.getVersionFlag()) {
                            showAlert(VersionCheckType.MAJOR);
                        } else if (versionCheckFlag == VersionCheckType.PATCH.getVersionFlag()) {
                            showAlert(VersionCheckType.PATCH);
//                            openNextActivity();
                        } else
                            launchNextActivity();
                        break;
                    }
            }
        }
    }

    private void showAlert(final VersionCheckType versionCheckType) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(versionCheckType.getTitleId());
        alertBuilder.setMessage(versionCheckType.getMessageId());
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(versionCheckType.getPositiveButtonTextId(), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                openPlayStore();
            }
        });
        if (versionCheckType.getNegativeButtonTextId() != 0) {
            alertBuilder.setNegativeButton(versionCheckType.getNegativeButtonTextId(), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (versionCheckType) {
                        case MAJOR:
                        case MINOR:
                            android.os.Process.killProcess(Process.myPid());
                            System.exit(0);
                            break;
                        case PATCH:
                            launchNextActivity();
                            break;
                    }
                }
            });
        }
        alertBuilder.create();
        alertBuilder.show();
    }

    private void openPlayStore() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//        try {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//        } catch (android.content.ActivityNotFoundException anfe) {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
//        }
        launchNextActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handler != null && runnable != null)
            handler.removeCallbacks(runnable);
    }

}
