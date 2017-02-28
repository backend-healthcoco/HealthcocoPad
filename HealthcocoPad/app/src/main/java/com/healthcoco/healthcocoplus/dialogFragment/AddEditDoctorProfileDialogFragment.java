package com.healthcoco.healthcocoplus.dialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoDialogFragment;
import com.healthcoco.healthcocoplus.bean.DoctorProfileToSend;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.DoctorExperience;
import com.healthcoco.healthcocoplus.bean.server.DoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.FileDetails;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.Specialities;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocoplus.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocoplus.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocoplus.enums.CommonListDialogType;
import com.healthcoco.healthcocoplus.enums.DialogType;
import com.healthcoco.healthcocoplus.enums.FragmentType;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.OptionsType;
import com.healthcoco.healthcocoplus.enums.PatientProfileScreenType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocoplus.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocoplus.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.BitmapUtil;
import com.healthcoco.healthcocoplus.utilities.DateTimeUtil;
import com.healthcoco.healthcocoplus.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocoplus.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.ImageUtil;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.ScreenDimensions;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.views.TextViewFontAwesome;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Shreshtha on 02-02-2017.
 */
public class AddEditDoctorProfileDialogFragment extends HealthCocoDialogFragment implements GsonRequest.ErrorListener, DownloadFileFromUrlListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, View.OnClickListener, AdapterView.OnItemClickListener, CommonOptionsDialogItemClickListener {
    private FragmentType profile;
    private TextViewFontAwesome tvCloseDialog;
    private TextViewFontAwesome tvSave;
    private static final String DOCTOR_PROFILE_IMAGE = "DoctorProfileImage";
    private static final String DOCTOR_COVER_IMAGE = "DoctorCoverImage";
    public static final int TAG_RESULT_CODE = 112;
    private LinearLayout btAddSpeciality;
    private ImageView ivDoctorCoverPhoto;
    private ImageView ivProfileImage;
    private ImageButton btEditProfileImage;
    private ImageButton btEditCoverPhoto;
    private EditText editName;
    private LinearLayout containerSpecialities;
    private User user;
    private DoctorProfile doctorProfile;
    private TextView selectedSpecialityTextView;
    private List<Specialities> specialitiesList;
    private HealthCocoDialogFragment commonListDialogFragment;
    private DoctorImageType doctorImageType;
    private Uri cameraImageUri;
    private FileDetails doctorCoverImageFileDetails;
    private FileDetails doctorProfileImageFileDetails;
    private DoctorExperience selectedDoctorExperinece;
    private TextView tvInitialAlphabet;
    private AutoCompleteTextView autotvExperience;
    private RadioGroup radioGroupGender;
    private TextView tvDOB;
    private TextView tvTitle;
    private ImageButton containerLeftAction;
    private String selectedFilterTitle;
    private String title;
    private Button bt_save;

    public AddEditDoctorProfileDialogFragment() {
    }

    public AddEditDoctorProfileDialogFragment(FragmentType profile) {
        this.profile = profile;
    }

    private enum DoctorImageType {
        PROFILE_IMAGE, COVER_IMAGE
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_doctor_profile_details, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mActivity.showLoading(false);
        }
        initViews();
        initListeners();
        initAutoTvAdapter();
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        initActionbarTitle();
        ivDoctorCoverPhoto = (ImageView) view.findViewById(R.id.iv_doctor_cover_photo);
        ivProfileImage = (ImageView) view.findViewById(R.id.iv_image);
        btEditProfileImage = (ImageButton) view.findViewById(R.id.bt_edit_profile_image);
        btEditCoverPhoto = (ImageButton) view.findViewById(R.id.bt_edit_cover_image);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
        editName = (EditText) view.findViewById(R.id.edit_name);
        containerSpecialities = (LinearLayout) view.findViewById(R.id.container_specialities);
        btAddSpeciality = (LinearLayout) view.findViewById(R.id.bt_add_speciality);
        autotvExperience = (AutoCompleteTextView) view.findViewById(R.id.autotv_experience);
        //init top layout height
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int)
                (ScreenDimensions.SCREEN_WIDTH * 0.50), (int) ((ScreenDimensions.SCREEN_HEIGHT) * 0.20));
        ivDoctorCoverPhoto.setLayoutParams(layoutParams);

        radioGroupGender = (RadioGroup) view.findViewById(R.id.rg_gender_select);
        tvDOB = (TextView) view.findViewById(R.id.tv_dob);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        containerLeftAction = (ImageButton) view.findViewById(R.id.bt_cross);
        tvTitle.setText(profile.getTitleId());
        bt_save = (Button) view.findViewById(R.id.bt_save);
    }

    @Override
    public void initListeners() {
        bt_save.setOnClickListener(this);
        btEditProfileImage.setOnClickListener(this);
        btEditCoverPhoto.setOnClickListener(this);
        btAddSpeciality.setOnClickListener(this);
        autotvExperience.setOnItemClickListener(this);
        tvDOB.setOnClickListener(this);
        initSaveCancelButton(this);
    }

    private void initAutoTvAdapter() {
        try {
            ArrayList<Object> list = (ArrayList<Object>) (ArrayList<?>) Util.getDefaultExperienceList();
            if (!Util.isNullOrEmptyList(list)) {
                AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_grey_background,
                        list, AutoCompleteTextViewType.EXPERIENCE_LIST);
                autotvExperience.setThreshold(0);
                autotvExperience.setAdapter(adapter);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                    autotvExperience.setDropDownAnchor(R.id.autotv_experience);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {
        if (doctorProfile != null) {
            editName.setText(doctorProfile.getFirstName());
            tvDOB.setText(Util.getDOB(doctorProfile.getDob()));

            String gender = Util.getValidatedValue(doctorProfile.getGender().toUpperCase());
            RadioButton radioButton = (RadioButton) radioGroupGender.findViewWithTag(gender);
            if (radioButton != null)
                radioButton.setChecked(true);
            containerSpecialities.removeAllViews();
            if (!Util.isNullOrEmptyList(doctorProfile.getSpecialities())) {
                for (String speciality :
                        doctorProfile.getSpecialities()) {
                    addSpecialityItem(speciality);
                }
            } else {
                addSpecialityItem(null);
            }
            if (doctorProfile.getExperience() != null) {

                selectedDoctorExperinece = doctorProfile.getExperience();
                String formattedExperineceText = selectedDoctorExperinece.getFormattedExperience();
                try {
                    int experienceIntegerValue = selectedDoctorExperinece.getExperience();
                    if (experienceIntegerValue > 1)
                        formattedExperineceText = formattedExperineceText + "s";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                formattedExperineceText = formattedExperineceText + " " + getResources().getString(R.string.experience);
                autotvExperience.setText(formattedExperineceText);

            }
        }
    }

    private void addSpecialityItem(String speciality) {
        LinearLayout specialityItem = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_add_edit_doctor_speciality, null);
        TextView tvSpeciality = (TextView) specialityItem.findViewById(R.id.tv_speciality);
        TextViewFontAwesome btDelete = (TextViewFontAwesome) specialityItem.findViewById(R.id.bt_delete);
        btDelete.setTag(specialityItem);
        if (!Util.isNullOrBlank(speciality))
            tvSpeciality.setText(speciality);
        tvSpeciality.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        containerSpecialities.addView(specialityItem);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    initData();
                    loadImages();
                    break;
                case GET_DOCTOR_PROFILE:
                    break;
                case UPDATE_DOCTOR_PROFILE:
                    if (response.getData() != null && response.getData() instanceof DoctorProfile) {
                        DoctorProfile doctorProfileResponse = (DoctorProfile) response.getData();
                        doctorProfile.setImageUrl(doctorProfileResponse.getImageUrl());
                        doctorProfile.setThumbnailUrl(doctorProfileResponse.getThumbnailUrl());
                        doctorProfile.setCoverImageUrl(doctorProfileResponse.getCoverImageUrl());
                        doctorProfile.setCoverThumbnailImageUrl(doctorProfileResponse.getCoverThumbnailImageUrl());
                        doctorProfile.setFirstName(doctorProfileResponse.getFirstName());
                        doctorProfile.setGender(doctorProfileResponse.getGender());
                        doctorProfile.setDob(doctorProfileResponse.getDob());
                        doctorProfile.setSpecialities(doctorProfileResponse.getSpecialities());
                        doctorProfile.setExperience(doctorProfileResponse.getExperience());
                        LocalDataServiceImpl.getInstance(mApp).addDoctorProfile(doctorProfile);
                    }
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                    getDialog().dismiss();
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void loadImages() {
        if (doctorProfile != null) {
            //loadng profile image and cover image
            DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivDoctorCoverPhoto, doctorProfile.getCoverImageUrl());

            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_DOCTOR_PROFILE, doctorProfile, null, ivProfileImage, tvInitialAlphabet);
//            if (!Util.isNullOrBlank(doctorProfile.getCoverImageUrl())) {
//                doctorProfile.setCoverImagePath(ImageUtil.getPathToSaveFile(HealthCocoFileType.DOCTOR_COVER_IMAGE, Util.getFileNameFromUrl(doctorProfile.getCoverImageUrl()), Util.getFileExtension(doctorProfile.getCoverImageUrl())));
//                new DownloadFileFromUrlAsyncTask(mActivity, this, HealthCocoFileType.DOCTOR_COVER_IMAGE, Util.getFileNameFromUrl(doctorProfile.getCoverImageUrl()), null, null).execute(doctorProfile.getCoverImageUrl());
//            } else {
//                ivDoctorCoverPhoto.setBackgroundResource(R.drawable.bg_doctor_img);
//            }
        }
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                    doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileObject(user.getUniqueId());
                    specialitiesList = LocalDataServiceImpl.getInstance(mApp).getSpecialitiesListObject();
                }
                return volleyResponseBean;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            if (!Util.isNullOrBlank(doctorProfile.getProfileImagePath()) && doctorProfile.getProfileImagePath().equalsIgnoreCase(filePath)) {
                int width = ivProfileImage.getLayoutParams().width;
                LogUtils.LOGD(TAG, "Image SIze " + width);
                Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, width);
                if (bitmap != null) {
                    ivProfileImage.setImageBitmap(bitmap);
                    ivProfileImage.setVisibility(View.VISIBLE);
                }
            } else if (!Util.isNullOrBlank(doctorProfile.getCoverImagePath()) && doctorProfile.getCoverImagePath().equalsIgnoreCase(filePath)) {
                int width = ScreenDimensions.SCREEN_WIDTH;
                int height = (int) (ScreenDimensions.SCREEN_HEIGHT * 0.25);
                LogUtils.LOGD(TAG, "Image SIze " + width);
                Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, height);
                if (bitmap != null) {
                    ivDoctorCoverPhoto.setImageBitmap(bitmap);
                    ivDoctorCoverPhoto.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_edit_profile_image:
                doctorImageType = DoctorImageType.PROFILE_IMAGE;
                openDialogFragment(DialogType.SELECT_IMAGE, this);
                break;
            case R.id.bt_edit_cover_image:
                doctorImageType = DoctorImageType.COVER_IMAGE;
                openDialogFragment(DialogType.SELECT_IMAGE, this);
                break;

            case R.id.bt_add_speciality:
                if (isValidSpecialityItem())
                    addSpecialityItem(null);
                else
                    Util.showToast(mActivity, R.string.please_select_speciality);
                break;
            case R.id.tv_speciality:
                if (view instanceof TextView)
                    selectedSpecialityTextView = (TextView) view;
                if (!Util.isNullOrEmptyList(specialitiesList)) {
                    commonListDialogFragment = openCommonListDialogFragment(this, CommonListDialogType.SPECIALITY, specialitiesList);
                } else Util.showToast(mActivity, R.string.no_specialisations_found);
                break;
            case R.id.bt_delete:
                showConfirmationAlert(view);
                break;
            case R.id.bt_save:
                validateData();
                break;
            case R.id.tv_dob:
                openBirthDatePickerDialog();
                break;
        }
    }


    private void openBirthDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(calendar, Util.getValidatedValueOrNull(tvDOB));
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvDOB.setText(DateTimeUtil.getBirthDateFormat(dayOfMonth, monthOfYear + 1, year));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Object object = parent.getAdapter().getItem(position);
            if (object instanceof DoctorExperience) {
                selectedDoctorExperinece = (DoctorExperience) object;
                autotvExperience.setText(selectedDoctorExperinece.getExperience() + " "
                        + selectedDoctorExperinece.getPeriodValue() + " "
                        + getResources().getString(R.string.experience));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidSpecialityItem() {
        if (containerSpecialities.getChildCount() > 0) {
            for (int i = 0; i < containerSpecialities.getChildCount(); i++) {
                LinearLayout specialityItem = (LinearLayout) containerSpecialities.getChildAt(i);
                TextView tvSpeciality = (TextView) specialityItem.findViewById(R.id.tv_speciality);
                String speciality = Util.getValidatedValueOrNull(tvSpeciality);
                if (Util.isNullOrBlank(speciality))
                    return false;
            }
        }
        return true;
    }

    private void showConfirmationAlert(final View v) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.this_cannot_be_undone);
        alertBuilder.setMessage(getResources().getString(
                R.string.do_you_want_to_delete_this_item));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                View parent = (View) v.getTag();
                if (parent != null)
                    containerSpecialities.removeView(parent);
                if (containerSpecialities.getChildCount() <= 0)
                    addSpecialityItem(null);
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        EditText selectedEditText = null;
        clearPreviousAlerts();
        String name = Util.getValidatedValueOrNull(editName);
        if (Util.isNullOrBlank(name)) {
            msg = getResources().getString(R.string.please_enter_name);
            errorViewList.add(editName);
        }
        if (Util.isNullOrBlank(msg))
            updateDoctorProfile(name);
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void updateDoctorProfile(String name) {
        mActivity.showLoading(false);
        DoctorProfileToSend profile = new DoctorProfileToSend();
        profile.setFirstName(name);
        profile.setTitle(doctorProfile.getTitle(true));
        //setting gender
        View checkedRadioButton = view.findViewById(radioGroupGender.getCheckedRadioButtonId());
        if (checkedRadioButton != null)
            profile.setGender(String.valueOf(checkedRadioButton.getTag()));
        //setting DOB
        profile.setDob(DateTimeUtil.getDob(String.valueOf(tvDOB.getText())));

        profile.setSpeciality(getSpecialitiesFromViews());
        if (selectedDoctorExperinece != null)
            profile.setExperience(selectedDoctorExperinece.getExperience());
        profile.setDoctorId(doctorProfile.getDoctorId());
        profile.setCoverImage(doctorCoverImageFileDetails);
        profile.setProfileImage(doctorProfileImageFileDetails);
        WebDataServiceImpl.getInstance(mApp).updateDoctorProfile(DoctorProfile.class, profile, this, this);
    }

    private List<String> getSpecialitiesFromViews() {
        List<String> list = new ArrayList<>();
        if (containerSpecialities.getChildCount() > 0) {
            for (int i = 0; i < containerSpecialities.getChildCount(); i++) {
                LinearLayout specialityItem = (LinearLayout) containerSpecialities.getChildAt(i);
                TextView tvSpeciality = (TextView) specialityItem.findViewById(R.id.tv_speciality);
                String speciality = Util.getValidatedValueOrNull(tvSpeciality);
                if (!Util.isNullOrBlank(speciality))
                    list.add(speciality);
            }
        }
        return list;
    }

    private void clearPreviousAlerts() {
        editName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }


    @Override
    public void onOptionsItemSelected(Object object) {
        OptionsType optionsType = (OptionsType) object;
        if (optionsType != null) {
            LogUtils.LOGD(TAG, getResources().getString(optionsType.getStringId()));
            switch (optionsType) {
                case CAMERA:
                    cameraImageUri = mActivity.
                            openCamera(this, "clinicImage");
                    break;
                case GALLERY:
                    mActivity.openGallery(this);
                    break;
            }
        }
    }

    @Override
    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {
        switch (commonListDialogType) {
            case SPECIALITY:
                if (object instanceof Specialities) {
                    Specialities speciality = (Specialities) object;
                    if (isSpecialityNotAdded(speciality))
                        selectedSpecialityTextView.setText(speciality.getSuperSpeciality());
                    else
                        Util.showToast(mActivity, R.string.speciality_already_added);
                }
                break;
        }
        if (commonListDialogFragment != null)
            commonListDialogFragment.dismiss();
    }

    private boolean isSpecialityNotAdded(Specialities selecetdSpeciality) {
        if (containerSpecialities.getChildCount() > 0) {
            for (int i = 0; i < containerSpecialities.getChildCount(); i++) {
                LinearLayout specialityItem = (LinearLayout) containerSpecialities.getChildAt(i);
                TextView tvSpeciality = (TextView) specialityItem.findViewById(R.id.tv_speciality);
                String speciality = Util.getValidatedValueOrNull(tvSpeciality);
                if (selecetdSpeciality.getSuperSpeciality().equalsIgnoreCase(speciality))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == HealthCocoConstants.REQUEST_CODE_CAMERA && cameraImageUri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), cameraImageUri);
                    if (bitmap != null) {
                        showImage(cameraImageUri.getPath(), bitmap);
                    }
                } else if (requestCode == HealthCocoConstants.REQUEST_CODE_GALLERY && data.getData() != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), data.getData());
                    if (bitmap != null) {
                        showImage(data.getData().getPath(), bitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showImage(String filePath, Bitmap originalBitmap) {
        if (doctorImageType != null) {
            originalBitmap = ImageUtil.getRotatedBitmapIfRequiredFromPath(filePath, originalBitmap);
            switch (doctorImageType) {
                case PROFILE_IMAGE:
                    //croping bitmap to show on image as per its dimensions
                    Bitmap bitmap = BitmapUtil.scaleCenterCrop(originalBitmap, ScreenDimensions.SCREEN_WIDTH, ScreenDimensions.SCREEN_HEIGHT);
                    if (bitmap != null) {
                        ivProfileImage.setVisibility(View.VISIBLE);
                        ivProfileImage.setImageBitmap(bitmap);
                    }
                    //passing original bitmap to server
                    doctorProfileImageFileDetails = getFileDetails(DOCTOR_PROFILE_IMAGE, originalBitmap);
                    break;
                case COVER_IMAGE:
                    //croping bitmap to show on image as per its dimensions
                    Bitmap bitmap2 = BitmapUtil.scaleCenterCrop(originalBitmap, ScreenDimensions.SCREEN_WIDTH, ScreenDimensions.SCREEN_HEIGHT);
                    if (bitmap2 != null) {
                        ivDoctorCoverPhoto.setImageBitmap(bitmap2);
                        ivDoctorCoverPhoto.setVisibility(View.VISIBLE);
                    }
                    //passing original bitmap to server
                    doctorCoverImageFileDetails = getFileDetails(DOCTOR_COVER_IMAGE, originalBitmap);
                    break;
            }
        }

    }

    private FileDetails getFileDetails(String fileName, Bitmap bitmap) {
        FileDetails fileDetails = new FileDetails();
        fileDetails.setFileEncoded(ImageUtil.encodeTobase64(bitmap));
        fileDetails.setFileExtension(ImageUtil.DEFAULT_IMAGE_EXTENSION);
        fileDetails.setFileName(fileName);
        fileDetails.setBitmap(bitmap);
        return fileDetails;
    }

    class BitmapWorkerTask extends AsyncTask<Bitmap, Void, Bitmap> {
        WeakReference<ImageView> ivImageReference;
        String filePath = "";

        public BitmapWorkerTask(ImageView imageView, String path) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            ivImageReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            if (params != null)
                return ImageUtil.getRotatedBitmapIfRequiredFromPath(filePath, params[0]);
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (ivImageReference != null && bitmap != null) {
                final ImageView imageView = ivImageReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
