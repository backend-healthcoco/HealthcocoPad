package com.healthcoco.healthcocopad.utilities;

import android.app.Activity;
import android.os.Process;

import com.crashlytics.android.Crashlytics;

/**
 * Created by Shreshtha on 03-02-2017.
 */

public class MyExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    public static final String TAG_EXIT = "exit";
    private final Activity mActivity;

    public MyExceptionHandler(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        exception.printStackTrace();
//send crashes to non-fatal tab of Fabric crashlytics
        Crashlytics.logException(exception);

//        //for restarting the Activity
//        if (mActivity != null) {
//            mActivity.finishAffinity(); // Get tracker.
//            Tracker t = ((HealthCocoApplication) mActivity.getApplication()).getDefaultTracker();
//
//            t.send(new HitBuilders.ExceptionBuilder()
//                            .setDescription(
//                                    new StandardExceptionParser(mActivity, null)
//                                            .getDescription(Thread.currentThread().getName(), exception))
//                            .setFatal(false)
//                            .build()
//            );
//        }
        mActivity.finishAffinity();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
