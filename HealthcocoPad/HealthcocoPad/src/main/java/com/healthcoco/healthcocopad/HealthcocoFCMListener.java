package com.healthcoco.healthcocopad;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.healthcoco.healthcocopad.activities.SplashScreenActivity;
import com.healthcoco.healthcocopad.bean.server.NotificationResponse;
import com.healthcoco.healthcocopad.fragments.BabyAchievementsListFragment;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.GrowthChartListFragment;
import com.healthcoco.healthcocopad.fragments.QueueFragment;
import com.healthcoco.healthcocopad.fragments.VaccinationListFragment;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Created by neha on 02/04/16.
 */
public class HealthcocoFCMListener extends FirebaseMessagingService {
    public static final String TAG_NOTIFICATION_RESPONSE = "notificationResponse";
    private static final String TAG = "HealthcocoFCMListener";
    String KEY = "message";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        Map data = message.getData();
        //Log data to Log Cat
        if (data.get(KEY) != null) {
            LogUtils.LOGD(TAG, "From: " + message.getFrom());
            LogUtils.LOGD(TAG, "Message : " + (String) data.get(KEY));
            generateNotification((String) data.get(KEY));
        }
    }

    private void generateNotification(String message) {
        NotificationResponse notificationResponse = new Gson().fromJson(message, NotificationResponse.class);
        if (notificationResponse != null) {
            if (notificationResponse.getNotificationType() != null) {
                switch (notificationResponse.getNotificationType()) {
                    case APPOINTMENT:
//                        Util.sendBroadcast((HealthCocoApplication) getApplicationContext(), QueueFragment.INTENT_GET_APPOINTMENT_LIST_SERVER);
                        break;
                    case APPOINTMENT_STATUS_CHANGE:
                    case APPOINTMENT_REFRESH:
                        Util.sendBroadcast((HealthCocoApplication) getApplicationContext(), QueueFragment.INTENT_GET_APPOINTMENT_LIST_SERVER);
                        return;
                    case PATIENT_REFRESH:
                        Util.sendBroadcast((HealthCocoApplication) getApplicationContext(), ContactsListFragment.INTENT_GET_CONTACT_LIST_SERVER);
                        return;
                    case REFRESH_VACCINATION:
                        Util.sendBroadcast((HealthCocoApplication) getApplicationContext(), VaccinationListFragment.INTENT_REFRESH_REQUEST_LIST_FROM_SERVER);
                        return;
                    case REFRESH_GROWTH_CHART:
                        Util.sendBroadcast((HealthCocoApplication) getApplicationContext(), GrowthChartListFragment.INTENT_REFRESH_REQUEST_LIST_FROM_SERVER);
                        return;
                    case REFRESH_BABY_ACHIEVEMENTS:
                        Util.sendBroadcast((HealthCocoApplication) getApplicationContext(), BabyAchievementsListFragment.INTENT_REFRESH_REQUEST_LIST_FROM_SERVER);
                        return;
                    case CLINICAL_NOTES_REFRESH:
                    case PATIENT_VISIT_REFRESH:
                    case PRESCRIPTION_REFRESH:
                    case RECORDS_REFRESH:
                    case TREATMENTS_REFRESH:
                    case INVOICE_REFRESH:
                    case RECEIPT_REFRESH:
                    case DISCHARGE_SUMMARY_REFRESH:
                    case REFRESH:
                        return;

                }
            }

            //setting title
            String title = Util.getValidatedValue(notificationResponse.getTitle());
            String text = Util.getValidatedValue(notificationResponse.getText());
            if (Util.isNullOrBlank(title))
                title = getApplicationContext().getResources().getString(R.string.app_name);

            //setting image from url if received
            Bitmap imageBitmap = null;
            if (!Util.isNullOrBlank(notificationResponse.getImg())) {
                try {
                    imageBitmap = BitmapFactory.decodeStream((InputStream) new URL(notificationResponse.getImg()).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(getNotificationIcon())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.app_logo))
                    .setColor(getResources().getColor(R.color.blue_action_bar))
                    .setContentTitle(title)
                    .setContentText(text).setStyle(new NotificationCompat.BigTextStyle().bigText(text));

            //setting style for notification if bitmap is not null
            NotificationCompat.BigPictureStyle notifStyle = new NotificationCompat.BigPictureStyle();
            if (imageBitmap != null) {
                notifStyle.setSummaryText(text);
                notifStyle.bigPicture(imageBitmap);
                mBuilder.setStyle(notifStyle);
            }

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent notificationIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            notificationIntent.putExtra(TAG_NOTIFICATION_RESPONSE, message);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);
            mBuilder.setSound(defaultSoundUri);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(DateTimeUtil.getCurrentDateLong().intValue(), mBuilder.build());
        }
    }

    private int getNotificationIcon() {
//        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
//        return useWhiteIcon ? R.drawable.img_info_normal : R.drawable.ic_launcher;
        return R.drawable.ic_notification;
    }
}