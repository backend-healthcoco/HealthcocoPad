package com.healthcoco.healthcocopad.utilities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import com.healthcoco.healthcocopad.enums.HealthCocoFileType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shreshtha on 02-02-2017.
 */
public class ImageUtil {
    private static final String TAG = ImageUtil.class.getSimpleName();
    public static final String DEFAULT_IMAGE_EXTENSION = "jpg";
    private static final String DEFAULT_FOLDER_NAME = "HealthCococMedia";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String DEFAULT_CLINIC_LOGO_NAME = "clinicLogoImage";
    public static final String DEFAULT_CLINIC_IMAGE_NAME = "clinicImage";
    public static final String DEFAULT_PATIENT_IMAGE_NAME = "patientImage";
    public static byte[] DIAGRAM_SELECTED_BYTE_ARRAY = null;
    public static final String DEFAULT_IMAGE_NAME = "defaultFileName";

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] b = baos.toByteArray();
        return Util.getStringFromByteArray(b);
    }


    public static String getPathToSaveFile(HealthCocoFileType healthCocoFileType, String fileName, String extension) {
        File file = new File(getDeafultStorageFolder(), healthCocoFileType.getFolderName());
        if (!file.exists())
            file.mkdirs();
        String filePath = new File(file.getAbsolutePath(), fileName).getAbsolutePath();
        LogUtils.LOGD(TAG, "File pathToSave " + filePath);
        return filePath;
    }

    private static String getDeafultStorageFolder() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + DEFAULT_FOLDER_NAME);
        if (!file.exists())
            file.mkdirs();
        return file.getAbsolutePath();
    }

    public static Bitmap getDecodedBitmapFromPath(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (reqWidth <= 0 || reqHeight <= 0)
            options.inSampleSize = calculateInSampleSize(options, ScreenDimensions.SCREEN_WIDTH, ScreenDimensions.SCREEN_HEIGHT);
        else {
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap decodeBitmap = BitmapFactory.decodeFile(path, options);
        if (decodeBitmap != null)
            decodeBitmap = ExifUtils.rotateBitmap(path, decodeBitmap);
        return decodeBitmap;
    }

    public static Bitmap getRotatedBitmapIfRequiredFromPath(String filePath, Bitmap orignalBitmap) {
        Bitmap bitmap = ExifUtils.rotateBitmap(filePath, orignalBitmap);
        if (bitmap != null)
            return bitmap;
        return orignalBitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (reqHeight > 0 && reqHeight > 0) {
            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
        }

        return inSampleSize;
    }

    public static boolean isFileExists(String pathToSaveAndGet) {
        File file = new File(pathToSaveAndGet);
        if (file.exists())
            return true;
        return false;
    }

    /**
     * Checking device has camera hardware or not
     */
    public static boolean isDeviceSupportCamera(Activity mActivity) {
        if (mActivity.getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Creating file uri to store image/video
     */
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator
                    + "IMG_" + timeStamp + DEFAULT_IMAGE_EXTENSION);
        }
//        else if (type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "VID_" + timeStamp + ".mp4");
//        }
        else {
            return null;
        }

        return mediaFile;
    }

    public static void deleteFolderIfSizeExceed() {
        try {
            long bytes = folderSize(new File(getDeafultStorageFolder()));
            double sizeInMB = bytes / (1024 * 1024);
            if (sizeInMB <= 10) {
                deleteFileFrom(getDeafultStorageFolder());
            }
            LogUtils.LOGD(TAG, "Folder Size " + sizeInMB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long folderSize(File directory) {
        long length = 0;
        File[] directoryFiles = directory.listFiles();
        if (directoryFiles != null && directoryFiles.length > 0) {
            for (File file : directoryFiles) {
                if (file.isFile())
                    length += file.length();
                else
                    length += folderSize(file);
            }
        }
        return length;
    }

    public static void deleteFileFrom(Uri imageUri) {
        deleteFileFrom(imageUri.getPath());
    }

    public static void deleteFileFrom(String filePath) {
        try {
            File fdelete = new File(filePath);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + filePath);
                } else {
                    System.out.println("file not Deleted :" + filePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

