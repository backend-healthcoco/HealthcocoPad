package com.healthcoco.healthcocopad.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.SplashScreenActivity;
import com.healthcoco.healthcocopad.bean.ConsultationFee;
import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.bean.DoctorExperience;
import com.healthcoco.healthcocopad.bean.server.AppointmentSlot;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.Hospital;
import com.healthcoco.healthcocopad.bean.server.LocationAndAccessControl;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.enums.DoctorExperienceUnit;
import com.healthcoco.healthcocopad.enums.VisitIdType;
import com.healthcoco.healthcocopad.fragments.PatientVisitDetailFragment;

import org.parceler.Parcels;
import org.spongycastle.jcajce.provider.digest.Keccak;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shreshtha on 18-01-2017.
 */

public class Util {
    private static final String TAG = Util.class.getSimpleName();
    private static Toast visibleToast;

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(Activity activity) {
        int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    /**
     * Method to check Network connection of device
     *
     * @param activity
     * @return
     */
    public static boolean checkNetworkStatus(Activity activity) {
        try {
            HealthCocoConstants.isNetworkOnline = false;

            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                HealthCocoConstants.isNetworkOnline = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    HealthCocoConstants.isNetworkOnline = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HealthCocoConstants.isNetworkOnline;
    }

    public static String getStringFromByteArray(byte[] b) {
        if (b != null) {
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
            LogUtils.LOGD(TAG, imageEncoded);
            return imageEncoded;
        }
        return null;
    }

    /**
     * prints screen density as follows :
     * // return 0.75 if it's LDPI
     * // return 1.0 if it's MDPI
     * // return 1.5 if it's HDPI
     * // return 2.0 if it's XHDPI
     * // return 3.0 if it's XXHDPI
     * // return 4.0 if it's XXXHDPI
     *
     * @param activity
     */
    public static void printScreenDensity(Activity activity) {
        try {
            float density = activity.getResources().getDisplayMetrics().density;
            LogUtils.LOGD(TAG, "Screen density " + density);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requesFocus(View view) {
        if (view instanceof EditText && view.isEnabled())
            view.setFocusable(true);
        view.requestFocus();
    }

    public static String getFormattedConsultantFee(ConsultationFee consultationFee) {
        String formattedString = "";
        if (consultationFee != null && consultationFee.getCurrency() != null && consultationFee.getAmount() != null) {
            formattedString = consultationFee.getCurrency() + " " + consultationFee.getAmount();
        }
        return formattedString;
    }

    public static String formatDoubleNumber(double doubleString) {
        NumberFormat formatter = new DecimalFormat("##.##");
        if (doubleString != 0)
            return formatter.format(doubleString);
        return "0";
    }

    public static String getFormattedAppointmentSlot(AppointmentSlot appointmentSlot) {
        String formattedString = "";
        if (appointmentSlot != null && appointmentSlot.getTimeUnit() != null && appointmentSlot.getTime() != null) {
            formattedString = Math.round(appointmentSlot.getTime()) + " " + getValidatedValue(appointmentSlot.getTimeUnit().getValueToDisplay());
        }
        return formattedString;
    }

    public static boolean checkNetworkStatus(Context context) {
        try {
            HealthCocoConstants.isNetworkOnline = false;

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                HealthCocoConstants.isNetworkOnline = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    HealthCocoConstants.isNetworkOnline = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HealthCocoConstants.isNetworkOnline;
    }

    public static int getVersionCodeFromPreferences(Context context) {
        ObscuredSharedPreferences prefs = ObscuredSharedPreferences.getPrefs(context,
                context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        //retrieve data
        int versionCode = prefs.getInt(SplashScreenActivity.TAG_VERSION_CODE, 0);
        return versionCode;
    }

    public static void addVersionCodeInPreferences(Context context, int versionCode) {
        ObscuredSharedPreferences prefs = ObscuredSharedPreferences.getPrefs(context,
                context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);

        //save data
        prefs.edit().putInt(SplashScreenActivity.TAG_VERSION_CODE, versionCode).commit();
    }

    public static boolean isValidWebsite(String websiteUrl) {
        return Patterns.WEB_URL.matcher(websiteUrl).matches();
    }

    public static void hideKeyboard(Context context, View view) {
        try {
            InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, String message) {
        if (visibleToast != null)
            visibleToast.cancel();
        visibleToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        visibleToast.show();
    }

    public static void showToast(Context context, int messageId) {
        if (visibleToast != null)
            visibleToast.cancel();
        visibleToast = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
        visibleToast.show();
    }

    public static int getSizeFromDimen(Activity activity, int dimenId) {
        return activity.getResources().getDimensionPixelSize(dimenId);
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getValidatedValueOrDash(HealthCocoActivity mActivity, String text) {
        if (Util.isNullOrBlank(text))
            return mActivity.getResources().getString(R.string.no_text_dash);
        return text;
    }

    public static String getValidatedValue(String value) {
        if (!Util.isNullOrBlank(value))
            return value.trim();
        return "";
    }

    public static String getValidatedValueWithoutQuotes(String value) {
        if (!Util.isNullOrBlank(value))
            return value.replace("\"", "");
        return "";
    }

    public static String getValidatedValue(Integer value) {
        if (value != null)
            return String.valueOf(value);
        return null;
    }

    public static boolean isNullOrEmptyList(List<?> list) {
        return (list == null || list.size() <= 0) ? true : false;
    }

    public static boolean isNullOrEmptyList(HashMap<?, ?> hashMap) {
        return (hashMap == null || hashMap.size() <= 0) ? true : false;
    }

    public static boolean isNullOrZeroNumber(String text) {
        return (text == null || text.trim().equalsIgnoreCase("") || text.trim().equalsIgnoreCase("null")
                || Integer.valueOf(text.trim()) == 0) ? true : false;
    }

    public static boolean isNullOrZeroNumber(Integer i) {
        String text = getValidatedValue(i);
        return (text == null || text.trim().equalsIgnoreCase("") || text.trim().equalsIgnoreCase("null")
                || Integer.valueOf(text.trim()) == 0) ? true : false;
    }

    public static boolean isNullOrZeroNumber(long l) {
        String text = String.valueOf(l);
        return (text == null || text.trim().equalsIgnoreCase("") || text.trim().equalsIgnoreCase("null")
                || Long.valueOf(text.trim()) == 0) ? true : false;
    }

    public static boolean isNullOrBlank(String text) {
        return (text == null || text.trim().equalsIgnoreCase("") || text.trim().equalsIgnoreCase("null")) ? true
                : false;
    }

    public static boolean isValidMobileNo(String num) {
        String MOBILE_NUMBER = "^[7-9][0-9]{9}$";
        Pattern pattern1 = Pattern.compile(MOBILE_NUMBER);
        return pattern1.matcher(num).matches();
    }

    public static Integer getIntValue(Object object) {
        int intValue = 0;
        if (object instanceof Double) {
            intValue = ((Double) object).intValue();
        } else if (object instanceof String) {
            intValue = Integer.parseInt((String) object);
        } else if (object instanceof Float) {
            intValue = ((Float) object).intValue();
        }
        return intValue;
    }

    public static String getConvertedBase64Value(String text) {
        try {
            // Sending side
            byte[] data = text.getBytes("UTF-8");
            String base64Text = Base64.encodeToString(data, Base64.DEFAULT);
            LogUtils.LOGD(TAG, base64Text);
            return base64Text.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String getValidatedValueOrNull(EditText editText) {
        String value = String.valueOf(editText.getText()).trim();
        if (!Util.isNullOrBlank(value)) {
            return value.trim();
        }
        return null;
    }

    public static String getValidatedValueOrNull(String value) {
        if (!Util.isNullOrBlank(value))
            return value.trim();
        return null;
    }

    public static String getValidatedValueOrNull(TextView textView) {
        String value = String.valueOf(textView.getText());
        if (!Util.isNullOrBlank(value))
            return value.trim();
        return null;
    }

    public static String getValidatedValueOrBlankWithoutTrimming(EditText editPassword) {
        String value = String.valueOf(editPassword.getText());
        if (!Util.isNullOrBlank(value)) {
            return value;
        }
        return "";
    }

    public static String getValidatedValueOrBlankWithoutTrimming(TextView textView) {
        String value = String.valueOf(textView.getText());
        if (!Util.isNullOrBlank(value))
            return value;
        return "";
    }

    public static String getValidatedValueOrBlankTrimming(TextView textView) {
        String value = String.valueOf(textView.getText()).trim();
        if (!Util.isNullOrBlank(value))
            return value;
        return "";
    }

    public static Integer getValidatedIntegerValue(EditText textView) {
        String validatedValue = getValidatedValueOrNull(textView);
        try {
            if (!Util.isNullOrBlank(validatedValue)) {
                Integer value = Integer.parseInt(validatedValue);
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to validate email id
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showAlert(Context context, String msg) {
        if (isNullOrBlank(msg))
            return;
        showAlert(context, "ALERT", msg);
    }

    public static void showAlert(Context context, int msgId) {
        showAlert(context, "ALERT", context.getResources().getString(msgId));
    }

    public static void showAlert(Context context, int tittle, int msg) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(tittle);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    public static void showAlert(Context context, String tittle, String msg) {
        if (isNullOrBlank(msg) || isNullOrBlank(tittle))
            return;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(tittle);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    public static char[] getSHA3SecurePassword(String password) {
        try {
            Keccak.Digest256 digest256 = new Keccak.Digest256();
            digest256.update(password.getBytes("UTF-8"));
            byte[] digest = digest256.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String s = bigInt.toString(16);
            char[] chars = s.toCharArray();
            return chars;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendBroadcast(HealthCocoApplication mApp, String intentFilter, Object extra) {
        try {
            Intent intent = new Intent(intentFilter);
            if (extra != null)
                intent.putExtra(HealthCocoConstants.TAG_BROADCAST_EXTRA, Parcels.wrap(extra));
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendBroadcast(HealthCocoApplication mApp, String intentFilter) {
        sendBroadcastWithParcelData(mApp, intentFilter, null, null);
    }

    public static void sendBroadcastWithParcelData(HealthCocoApplication mApp, String intentFilter, String[] tagArray, Object... intentData) {
        try {
            Intent intent = new Intent(intentFilter);
            if (tagArray != null && intentData != null && tagArray.length == intentData.length) {
                for (int i = 0; i < tagArray.length; i++) {
                    if (tagArray[i] != null && intentData[i] != null)
                        intent.putExtra(tagArray[i], Parcels.wrap(intentData[i]));
                }
            }
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFormattedGenderAge(Object object) {
        String formattedString = "";
        String gender = "";
        String formattedAge = "";
        DOB dob = null;
        if (object instanceof RegisteredPatientDetailsUpdated) {
            RegisteredPatientDetailsUpdated selectedPatient = (RegisteredPatientDetailsUpdated) object;
            gender = selectedPatient.getGender();
            dob = selectedPatient.getDob();
        } else if (object instanceof DoctorProfile) {
            DoctorProfile doctorProfile = (DoctorProfile) object;
            gender = doctorProfile.getGender();
            dob = doctorProfile.getDob();
        }
        if (dob != null) {
            if (object instanceof RegisteredPatientDetailsUpdated) {
                Calendar calendar = Calendar.getInstance();
                LogUtils.LOGD(TAG, "Date patient " + dob.getYears() + "," + dob.getMonths() + "," + dob.getDays());
                calendar.set(dob.getYears(), dob.getMonths() - 1, dob.getDays(), 0, 0);
                formattedAge = DateTimeUtil.getDateDifference(calendar);
            } else if (object instanceof DoctorProfile) {
                formattedAge = dob.getDays() + "/" + dob.getMonths() + "/" + dob.getYears();
            }
        }
        if ((object instanceof DoctorProfile)) {
            if (!Util.isNullOrBlank(gender))
                formattedString = formattedString + gender;
            if (!Util.isNullOrBlank(formattedString) && !Util.isNullOrBlank(formattedAge))
                formattedString = formattedString + " | " + formattedAge;
            else if (Util.isNullOrBlank(formattedString) && !Util.isNullOrBlank(formattedAge))
                formattedString = formattedString + formattedAge;
        } else {
            if (!Util.isNullOrBlank(gender))
                formattedString = formattedString + " | " + gender;
            if (!Util.isNullOrBlank(formattedString) && !Util.isNullOrBlank(formattedAge))
                formattedString = formattedString + " ," + formattedAge;
            else if (Util.isNullOrBlank(formattedString) && !Util.isNullOrBlank(formattedAge))
                formattedString = " | " + formattedString + formattedAge;
        }
        return formattedString;
    }

    public static String getFileExtension(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            ContentResolver cR = context.getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getExtensionFromMimeType(cR.getType(contentUri));
            return type;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static String getFileExtension(String filePathOrUrl) {
        String extension = "";
        if (filePathOrUrl.lastIndexOf(".") != -1) {
            int index = filePathOrUrl.lastIndexOf(".");
            extension = filePathOrUrl.substring(index + 1, filePathOrUrl.length());
        } else {
            extension = ImageUtil.DEFAULT_IMAGE_EXTENSION;
        }
        if (Util.isNullOrBlank(extension))
            extension = ImageUtil.DEFAULT_IMAGE_EXTENSION;
        return extension.toLowerCase();
    }

    public static String getFileNameFromUrl(String imageUrl) {
        if (!Util.isNullOrBlank(imageUrl)) {
            int endIndex = imageUrl.lastIndexOf("/");
            if (endIndex != -1) {
                String newstr = imageUrl.substring(endIndex, imageUrl.length()); // not forgot to put check if(endIndex != -1)
                LogUtils.LOGD(TAG, "Substring value " + newstr);
                return newstr;
            }
        }
        return imageUrl;
    }

    public static List<DoctorExperience> getDefaultExperienceList() {
        List<DoctorExperience> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            DoctorExperience doctorExperience = new DoctorExperience();
            doctorExperience.setExperience(i);
            doctorExperience.setPeriod(DoctorExperienceUnit.YEAR);
            if (i == 1)
                doctorExperience.setPeriodValue(doctorExperience.getPeriod().getExperiencePeriod());
            else
                doctorExperience.setPeriodValue(doctorExperience.getPeriod().getExperiencePeriod() + "s");
            list.add(doctorExperience);
        }
        return list;
    }

    public static String getDOB(DOB dob) {
        if (dob != null)
            return dob.getDays() + "/" + dob.getMonths() + "/" + dob.getYears();
        return "";
    }

    public static int getValidatedWidth(int width) {
        if (width <= 0)
            return ScreenDimensions.SCREEN_WIDTH;
        return width;
    }

    public static int getValidatedHeight(int height) {
        if (height <= 0)
            return ScreenDimensions.SCREEN_HEIGHT;
        return height;
    }

    public static void setFocusToEditText(HealthCocoActivity mActivity, View editName) {
        editName.setFocusableInTouchMode(true);
        editName.requestFocus();
        showKeyboard(mActivity, editName);
    }

    public static void showKeyboard(HealthCocoActivity mActivity, View editName) {
        final InputMethodManager inputMethodManager = (InputMethodManager) mActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void openFile(Context context, String filePath) {
        String fileType = getMimeTypeOfFile(filePath);
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        newIntent.setDataAndType(Uri.fromFile(new File(filePath)), fileType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            showAlertToDownloadApp(context, fileType);
        }
    }

    private static void showAlertToDownloadApp(final Context context, final String fileType) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(R.string.no_app_found);
        alertBuilder.setMessage(String.format(context.getResources().getString(R.string.message_no_app_found_to_open_file), fileType));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = "https://market.android.com/search?q=%s";
                Intent shopIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(str, fileType)));
                shopIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(shopIntent);
            }
        });
        alertBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    public static boolean isValidMobileNoLandlineNumber(String num) {
        String MOBILE_NUMBER = "^[7-9][0-9]{9}$";
        String LANDLINE_NUMBER_WITH_DASH = "^[0-9]{3,5}-[2-9]{1}[0-9]{5,7}$";
        String LANDLINE_NUMBER_WITHOUT_DASH = "^[0-9]{3,5}[2-9]{1}[0-9]{5,7}$";
        Pattern pattern1 = Pattern.compile(MOBILE_NUMBER);
        Pattern pattern2 = Pattern.compile(LANDLINE_NUMBER_WITH_DASH);
        Pattern pattern3 = Pattern.compile(LANDLINE_NUMBER_WITHOUT_DASH);
        return pattern1.matcher(num).matches() || pattern2.matcher(num).matches() || pattern3.matcher(num).matches();
    }

    public static String[] getConvertedStringArray(String uiPermissionString) {
        String replace = uiPermissionString.replace("[", "");
        String replace1 = replace.replace("]", "");
        String[] split = replace1.split(",");
        return split;
    }

    public static boolean isValidAadharId(Context context, String num) {
        if (num.length() < context.getResources().getInteger(R.integer.max_length_aadharid))
            return false;
        return true;
    }

    public static void showErrorOnEditText(EditText editText) {
        if (editText != null)
            editText.setActivated(true);
    }

    public static String getDeviceId(Context context) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    public static String getMimeTypeOfFile(String filePath) {
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (filePath.toString().contains(".doc") || filePath.toString().contains(".docx")) {
            // Word document
            return "application/msword";
        } else if (filePath.toString().contains(".pdf")) {
            // PDF file
            return "application/pdf";
        } else if (filePath.toString().contains(".ppt") || filePath.toString().contains(".pptx")) {
            // Powerpoint file
            return "application/vnd.ms-powerpoint";
        } else if (filePath.toString().contains(".xls") || filePath.toString().contains(".xlsx")) {
            // Excel file
            return "application/vnd.ms-excel";
        } else if (filePath.toString().contains(".zip") || filePath.toString().contains(".rar")) {
            // WAV audio file
            return "application/x-wav";
        } else if (filePath.toString().contains(".rtf")) {
            // RTF file
            return "application/rtf";
        } else if (filePath.toString().contains(".wav") || filePath.toString().contains(".mp3")) {
            // WAV audio file
            return "audio/x-wav";
        } else if (filePath.toString().contains(".gif")) {
            // GIF file
            return "image/gif";
        } else if (filePath.toString().contains(".jpg") || filePath.toString().contains(".jpeg") || filePath.toString().contains(".png")) {
            // JPG file
            return "image/jpeg";
        } else if (filePath.toString().contains(".txt")) {
            // Text file
            return "text/plain";
        } else if (filePath.toString().contains(".3gp") || filePath.toString().contains(".mpg") || filePath.toString().contains(".mpeg") || filePath.toString().contains(".mpe") || filePath.toString().contains(".mp4") || filePath.toString().contains(".avi")) {
            // Video files
            return "video/*";
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            return "*/*";
        }
    }

    public static String getFormattedDeviceInfo(Activity mActivity) {
        String formattedDevice = "";
        try {
            String deviceName = android.os.Build.MODEL;
            if (!isNullOrBlank(deviceName))
                formattedDevice = formattedDevice + mActivity.getResources().getString(R.string.model) + ":" + deviceName + ",";

            String deviceMan = android.os.Build.MANUFACTURER;
            if (!isNullOrBlank(deviceMan))
                formattedDevice = formattedDevice + mActivity.getResources().getString(R.string.manufacturer) + ":" + deviceMan + ",";

            String version = android.os.Build.VERSION.RELEASE;
            if (!isNullOrBlank(version))
                formattedDevice = formattedDevice + mActivity.getResources().getString(R.string.version) + ":" + version + ",";

            int sdkInt = android.os.Build.VERSION.SDK_INT;
            formattedDevice = formattedDevice + mActivity.getResources().getString(R.string.sdk) + ":" + sdkInt + ",";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDevice;
    }

    /**
     * Used to scroll to the given view.
     *
     * @param scrollViewParent Parent ScrollView
     * @param view             View to which we need to scroll.
     */
    public static void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    /**
     * Used to get deep child offset.
     * <p/>
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumalated Offset.
     */
    private static void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    public static String getFormattedStringReplaceBySpace(String text) {
        text = text.replaceAll("_", " ");
        return text;
    }

    public static void enableAllChildViews(View view, boolean isEnabled) {
        if (view instanceof LinearLayout) {
            LinearLayout layout = (LinearLayout) view;
            layout.setEnabled(isEnabled);
            if (layout.getChildCount() > 0) {
                for (int i = 0; i < layout.getChildCount(); i++) {
                    layout.getChildAt(i).setEnabled(isEnabled);
                }
            }
        }
    }

    public static boolean getVisitToggleStateFromPreferences(Context context) {
        ObscuredSharedPreferences prefs = ObscuredSharedPreferences.getPrefs(context,
                context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        //retrieve data
        boolean toggleState = prefs.getBoolean(PatientVisitDetailFragment.TAG_TOGGLE_STATE, false);
        return toggleState;
    }

    public static void addVisitToggleStateInPreference(Context context, boolean tagToggleState) {
        ObscuredSharedPreferences prefs = ObscuredSharedPreferences.getPrefs(context,
                context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);

        //save data
        prefs.edit().putBoolean(PatientVisitDetailFragment.TAG_TOGGLE_STATE, tagToggleState).commit();
    }

    public static String getVisitId(VisitIdType visitIdType) {
        switch (visitIdType) {
            case CLINICAL_NOTES:
                if (!Util.isNullOrBlank(HealthCocoConstants.VISIT_ID_CLINICAL_NOTES)) {
                    LogUtils.LOGD(TAG, "VisitId reseting to null" + HealthCocoConstants.VISIT_ID_TO_SEND);
                    resetAllVisitIds();
                }
                break;
            case REPORTS:
                if (!Util.isNullOrBlank(HealthCocoConstants.VISIT_ID_REPORTS)) {
                    LogUtils.LOGD(TAG, "VisitId reseting to null" + HealthCocoConstants.VISIT_ID_TO_SEND);
                    resetAllVisitIds();
                }
                break;
            case PRESCRIPTION:
                if (!Util.isNullOrBlank(HealthCocoConstants.VISIT_ID_PRESCRIPTION)) {
                    LogUtils.LOGD(TAG, "VisitId reseting to null" + HealthCocoConstants.VISIT_ID_TO_SEND);
                    resetAllVisitIds();
                }
                break;
            case TREATMENT:
                if (!Util.isNullOrBlank(HealthCocoConstants.VISIT_ID_TREATMENT)) {
                    LogUtils.LOGD(TAG, "VisitId reseting to null" + HealthCocoConstants.VISIT_ID_TO_SEND);
                    resetAllVisitIds();
                }
                break;
        }
        LogUtils.LOGD(TAG, "VisitId " + HealthCocoConstants.VISIT_ID_TO_SEND);
        return HealthCocoConstants.VISIT_ID_TO_SEND;
    }

    public static void resetAllVisitIds() {
        HealthCocoConstants.VISIT_ID_TO_SEND = null;
        HealthCocoConstants.VISIT_ID_CLINICAL_NOTES = null;
        HealthCocoConstants.VISIT_ID_REPORTS = null;
        HealthCocoConstants.VISIT_ID_PRESCRIPTION = null;
        HealthCocoConstants.VISIT_ID_TREATMENT = null;
    }

    public static void setVisitId(VisitIdType visitIdType, String visitId) {
        if (isNullOrBlank(HealthCocoConstants.VISIT_ID_TO_SEND))
            HealthCocoConstants.VISIT_ID_TO_SEND = visitId;
        switch (visitIdType) {
            case CLINICAL_NOTES:
                if (!Util.isNullOrBlank(HealthCocoConstants.VISIT_ID_CLINICAL_NOTES)) {
                    HealthCocoConstants.VISIT_ID_TO_SEND = visitId;
                    LogUtils.LOGD(TAG, "VisitId Update new" + HealthCocoConstants.VISIT_ID_TO_SEND);
                }
                HealthCocoConstants.VISIT_ID_CLINICAL_NOTES = visitId;
                break;
            case REPORTS:
                if (!Util.isNullOrBlank(HealthCocoConstants.VISIT_ID_REPORTS)) {
                    HealthCocoConstants.VISIT_ID_TO_SEND = visitId;
                    LogUtils.LOGD(TAG, "VisitId Update new" + HealthCocoConstants.VISIT_ID_TO_SEND);
                }
                HealthCocoConstants.VISIT_ID_REPORTS = visitId;
                break;
            case PRESCRIPTION:
                if (!Util.isNullOrBlank(HealthCocoConstants.VISIT_ID_PRESCRIPTION)) {
                    HealthCocoConstants.VISIT_ID_TO_SEND = visitId;
                    LogUtils.LOGD(TAG, "VisitId Update new" + HealthCocoConstants.VISIT_ID_TO_SEND);
                }
                HealthCocoConstants.VISIT_ID_PRESCRIPTION = visitId;
                break;
            case TREATMENT:
                if (!Util.isNullOrBlank(HealthCocoConstants.VISIT_ID_TREATMENT)) {
                    HealthCocoConstants.VISIT_ID_TO_SEND = visitId;
                    LogUtils.LOGD(TAG, "VisitId Update new" + HealthCocoConstants.VISIT_ID_TO_SEND);
                }
                HealthCocoConstants.VISIT_ID_TREATMENT = visitId;
                break;
        }
    }

    public static void sendBroadcasts(HealthCocoApplication mApp, ArrayList<String> intentFilters) {
        try {
            for (String intentFilter :
                    intentFilters) {
                Intent intent = new Intent(intentFilter);
                LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<?> getValidatedList(ArrayList<?> list) {
        if (Util.isNullOrEmptyList(list))
            return null;
        return list;
    }

    public static String getByteArrayFromUri(Context context, Uri uri) {
        File f = new File(uri.getPath());
        byte[] byteArray = null;
        try {
            InputStream iStream = context.getContentResolver().openInputStream(uri);
            byteArray = getBytes(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getStringFromByteArray(byteArray);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String getFormattedDoubleNumber(double doubleNumber) {
        NumberFormat formatter = new DecimalFormat("##.##");
        if (doubleNumber != 0)
            return formatter.format(Math.abs(doubleNumber));
        return "0";
    }

    public static boolean getValidatedBooleanValue(Boolean discarded) {
        if (discarded != null)
            return discarded;
        return false;
    }

    public static String getFormattedFloatNumber(float number) {
        NumberFormat formatter = new DecimalFormat("##.##");
        if (number != 0)
            return formatter.format(number);
        return "0";
    }

    //Calculate BMI
    public static float calculateBMI(float weight, float height) {
        return (float) (weight / (height * height));
    }


    //Calculate BSA  = squareroot of (weight X height / 3600)
    public static float calculateBSA(float weight, float height) {
        return (float) Math.sqrt(((weight * height) / 3600));
    }

    public static String getColorCode(String colorCode) {
        String color = "#F2ffffff";

        color = colorCode.substring(1, colorCode.length());
        color = "#80" + color;

        return color;
    }

    public static int getTextColor(String colorCode) {
        int color = Color.parseColor(colorCode);
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = (color >> 0) & 0xFF;
        float alpha = (color >> 24) & 0xFF;

        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);

        float hue = hsv[0];
        float sat = hsv[1];
        float val = hsv[2];

        if (val > 0.179)
            return Color.BLACK;
        else
            return Color.WHITE;
    }

   /* public static boolean getIsMobileNumberOptional(LoginResponse doctor) {
        for (Hospital hospital : doctor.getHospitals()) {
            if (hospital != null && (!Util.isNullOrEmptyList(hospital.getLocationsAndAccessControl()))) {
                for (LocationAndAccessControl locationAndAccessControl : hospital.getLocationsAndAccessControl()) {
                    if (locationAndAccessControl != null)
                        return locationAndAccessControl.getMobileNumberOptional();
                }
            }
        }
        return false;
    }
*/

}
