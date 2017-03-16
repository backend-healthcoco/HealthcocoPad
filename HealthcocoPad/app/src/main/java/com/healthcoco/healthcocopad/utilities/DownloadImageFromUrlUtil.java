package com.healthcoco.healthcocopad.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.custom.CircularImageView;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.listeners.ImageLoadedListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shreshtha on 02-02-2017.
 */
public class DownloadImageFromUrlUtil {
    private static final String TAG = DownloadImageFromUrlUtil.class.getSimpleName();
    private ImageView ivImage;
    private static HashMap<String, ArrayList<ImageView>> imagesLoadingHashmap = new HashMap<>();

    public static void setAlphabetBackground(int imageSize, String colorCode, char firstCharacter, TextView tvInitialAlphabet, ImageView ivContactProfile) {
        tvInitialAlphabet.setVisibility(View.VISIBLE);
        tvInitialAlphabet.setText(String.valueOf(Character.toUpperCase(firstCharacter)));
        ShapeDrawable roundedBackgroundDrawable = new ShapeDrawable(new OvalShape());
        roundedBackgroundDrawable.getPaint().setColor(Color.parseColor(colorCode));
        roundedBackgroundDrawable.setIntrinsicHeight(imageSize);
        roundedBackgroundDrawable.setIntrinsicWidth(imageSize);
        roundedBackgroundDrawable.setBounds(new Rect(0, 0, imageSize, imageSize));
        tvInitialAlphabet.setBackground(roundedBackgroundDrawable);
        ivContactProfile.setVisibility(View.GONE);
    }

    public static void loadImageWithInitialAlphabet(Context context, PatientProfileScreenType patientProfileScreenType, Object object, final ProgressBar progressLoading, final ImageView ivContactProfile, final TextView tvInitialAlphabet) {
        loadImageWithInitialAlphabet(context, patientProfileScreenType, null, object, progressLoading, ivContactProfile, tvInitialAlphabet);
    }

    public static void loadImageWithInitialAlphabet(Context context, PatientProfileScreenType patientProfileScreenType, ImageLoadedListener imageLoadedListener, Object object, final ProgressBar progressLoading, final ImageView ivContactProfile, final TextView tvInitialAlphabet) {
        try {
            String url = "";
            String colorCode = String.valueOf(context.getResources().getColor(R.color.grey_light_text));
            String name = "";
            if (object instanceof RegisteredPatientDetailsUpdated) {
                RegisteredPatientDetailsUpdated registeredPatientDetailsDetailsUpdated = (RegisteredPatientDetailsUpdated) object;
                url = registeredPatientDetailsDetailsUpdated.getThumbnailUrl();
                colorCode = registeredPatientDetailsDetailsUpdated.getColorCode();
                name = registeredPatientDetailsDetailsUpdated.getLocalPatientName();
            } else if (object instanceof AlreadyRegisteredPatientsResponse) {
                AlreadyRegisteredPatientsResponse alreadyRegisteredPatientsResponse = (AlreadyRegisteredPatientsResponse) object;
                url = alreadyRegisteredPatientsResponse.getThumbnailUrl();
                colorCode = alreadyRegisteredPatientsResponse.getColorCode();
                name = alreadyRegisteredPatientsResponse.getFirstName();
            } else if (object instanceof DoctorProfile) {
                DoctorProfile doctor = (DoctorProfile) object;
                url = doctor.getThumbnailUrl();
                colorCode = doctor.getColorCode();
                name = doctor.getFirstName();
            } else if (object instanceof ClinicDoctorProfile) {
                ClinicDoctorProfile clinicDoctorProfile = (ClinicDoctorProfile) object;
                url = clinicDoctorProfile.getThumbnailUrl();
                colorCode = clinicDoctorProfile.getColorCode();
                name = clinicDoctorProfile.getFirstName();
            }
            int imageSize = context.getResources().getDimensionPixelOffset(patientProfileScreenType.getImageViewSize());
            int textSize = context.getResources().getDimensionPixelOffset(patientProfileScreenType.getTextSize());

            //resizing imageview
            FrameLayout.LayoutParams layoutParamsImageView = new FrameLayout.LayoutParams(imageSize, imageSize);
            layoutParamsImageView.gravity = Gravity.CENTER;
            ivContactProfile.setLayoutParams(layoutParamsImageView);
            //resizing textView and its text size
            FrameLayout.LayoutParams layoutParamsTextView = new FrameLayout.LayoutParams(imageSize, imageSize);
            layoutParamsTextView.gravity = Gravity.CENTER;
            tvInitialAlphabet.setLayoutParams(layoutParamsTextView);
            tvInitialAlphabet.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            setAlphabetBackground(ivContactProfile.getLayoutParams().width, colorCode, name.charAt(0), tvInitialAlphabet, ivContactProfile);
            if (ivContactProfile instanceof CircularImageView) {
                CircularImageView circularImageView = (CircularImageView) ivContactProfile;
                circularImageView.setBorderColor(Color.parseColor(colorCode));
            }
            if (!Util.isNullOrBlank(url)) {
                loadImageUsingImageLoader(progressLoading, ivContactProfile, url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadRingWithColorCode(Context context, PatientProfileScreenType patientProfileScreenType, final String colorCode, final ImageView ivContactProfile) {
        try {
            int imageSize = context.getResources().getDimensionPixelOffset(patientProfileScreenType.getImageViewSize());
            ivContactProfile.setBackgroundResource(R.drawable.shape_circle_solid_white_grey_border);
            //resizing imageview
            FrameLayout.LayoutParams layoutParamsImageView = new FrameLayout.LayoutParams(imageSize, imageSize);
            layoutParamsImageView.gravity = Gravity.CENTER;
            ivContactProfile.setLayoutParams(layoutParamsImageView);
            GradientDrawable gradientDrawable = (GradientDrawable) ivContactProfile.getBackground();
            gradientDrawable.setStroke(2, Color.parseColor(colorCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageUsingImageLoader(final ProgressBar progressLoading, final ImageView imageView, final String url, final ImageLoadedListener imageLoadedListener) {
        try {
            ArrayList<ImageView> imagesList = new ArrayList<>();
            imagesList.add(imageView);
            if (imagesLoadingHashmap.containsKey(url)) {
                imagesList.addAll(imagesLoadingHashmap.get(url));
            }
            imagesLoadingHashmap.put(url, imagesList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                try {
                    File file = DiskCacheUtils.findInCache(imageUri, ImageLoader.getInstance().getDiskCache());
                    if (file != null && !Util.isNullOrBlank(file.getAbsolutePath())) {
                        String path = file.getAbsolutePath();
                        if (!Util.isNullOrBlank(path))
                            loadedImage = ExifUtils.rotateBitmap(path, loadedImage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(loadedImage);
                removeImageFromListInHashMap(url, imageView, progressLoading, loadedImage);
                imageView.setVisibility(View.VISIBLE);
                if (imageLoadedListener != null)
                    imageLoadedListener.onImageLoaded(loadedImage);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                imageView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
                imageView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
            }
        });
    }

    /**
     * Common metrhod which loads image from url using Image Loader
     *
     * @param progressLoading :  progress bar which is to be displayed until image is being loaded
     * @param imageView       :  imageView on which image is t be diaplayed
     * @param url             : url from which image is to be fetched
     */
    public static void loadImageUsingImageLoader(final ProgressBar progressLoading, final ImageView imageView, final String url) {
        loadImageUsingImageLoader(progressLoading, imageView, url, null);
    }

    /**
     * Common method which loads image from url specified.
     *
     * @param defaultImageDrawable : default drawable id which is to be shown if error occured or cancelled.
     * @param imageView            :  imageView on which image is t be diaplayed
     * @param url                  : url from which image is to be fetched
     * @param imageLoadedListener: to pass he loaded bitmap
     */
    public static void loadImageUsingImageLoaderUsingDefaultImage(final int defaultImageDrawable, final ImageView imageView,
                                                                  final String url, final ImageLoadedListener imageLoadedListener) {
        if (!Util.isNullOrBlank(url)) {
            ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    try {
                        File file = DiskCacheUtils.findInCache(imageUri, ImageLoader.getInstance().getDiskCache());
                        if (file != null && !Util.isNullOrBlank(file.getAbsolutePath())) {
                            String path = file.getAbsolutePath();
                            if (!Util.isNullOrBlank(path))
                                loadedImage = ExifUtils.rotateBitmap(path, loadedImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(loadedImage);
                    if (imageLoadedListener != null)
                        imageLoadedListener.onImageLoaded(loadedImage);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    super.onLoadingFailed(imageUri, view, failReason);
                    if (defaultImageDrawable > 0)
                        imageView.setBackgroundResource(defaultImageDrawable);
                }

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    super.onLoadingStarted(imageUri, view);
                    if (defaultImageDrawable > 0)
                        imageView.setBackgroundResource(defaultImageDrawable);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    super.onLoadingCancelled(imageUri, view);
                    if (defaultImageDrawable > 0)
                        imageView.setBackgroundResource(defaultImageDrawable);
                }
            });
        } else {
            imageView.setBackgroundResource(defaultImageDrawable);
        }
    }

    private static void removeImageFromListInHashMap(String url, ImageView ivContactProfile, ProgressBar progressLoading, Bitmap loadedImage) {
        if (imagesLoadingHashmap.containsKey(url)) {
            ArrayList<ImageView> imagesList = imagesLoadingHashmap.get(url);
            if (imagesList.contains(ivContactProfile) && !Util.isNullOrEmptyList(imagesList)) {
                for (ImageView imageView : imagesList) {
                    imageView.setImageBitmap(loadedImage);
                    imageView.setVisibility(View.VISIBLE);
                    if (progressLoading != null)
                        progressLoading.setVisibility(View.GONE);
                }
                imagesLoadingHashmap.remove(url);
            }
        }
    }
}
