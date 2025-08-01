package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Achievement;
import com.healthcoco.healthcocopad.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocopad.bean.server.DoctorExperienceDetail;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorRegistrationDetail;
import com.healthcoco.healthcocopad.bean.server.Education;
import com.healthcoco.healthcocopad.bean.server.Location;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.DownloadFileFromUrlAsyncTask;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDoctorProfileDialogFragment;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.FeedbackType;
import com.healthcoco.healthcocopad.enums.FragmentType;
import com.healthcoco.healthcocopad.enums.HealthCocoFileType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.SyncUtility;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class DoctorDetailsFragment extends HealthCocoFragment implements GsonRequest.ErrorListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, View.OnClickListener,
        DownloadFileFromUrlListener {

    public static final int REQUEST_CODE_DOCTOR_PROFILE = 111;
    public static final String TAG_DOCTOR_PROFILE = "doctorProfile";
    private static final String CHARACTER_BETWEEN_EDUCATION_AND_REGISTRATION_DETAILS = " ; ";
    private boolean isClickedOnce;
    private String doctorId;
    private User user;
    private ClinicDetailResponse clinicDetailResponse;

    private TextView tvName;
    private ImageView ivDoctorCoverPhoto;
    private ImageView ivProfileImage;
    private TextView tvSpecialities;
    private TextView tvInitialAlphabet;
    private TextView tvExperience;
    private TextView tvGenderDate;
    private boolean receiversRegistered;

    private TextView tvClinicNumbers;
    private TextView tvWebsite;
    private TextView tvEmailAddress;

    private LinearLayout containerEducationDetail;
    private LinearLayout containerRegistrationDetail;
    private LinearLayout containerAwardsAndPublication;
    private LinearLayout containerExperience;
    private LinearLayout containerProfessionalMembership;
    private LinearLayout containerProfessionalStatement;

    private TextView tvNoAwardsAndPublication;
    private TextView tvNoRegistrationDetail;
    private TextView tvNoProfessionalMembership;
    private TextView tvNoProfessionalStatement;
    private TextView tvNoExperience;
    private TextView tvNoEducation;

    private DoctorProfile doctorProfile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctor_details, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Util.checkNetworkStatus(mActivity);
        init();
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        if (intent != null) {
            doctorId = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_UNIQUE_ID));
        }
        initViews();
        initListeners();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        tvName = (TextView) view.findViewById(R.id.tv_name);
        ivDoctorCoverPhoto = (ImageView) view.findViewById(R.id.iv_doctor_cover_photo);
        ivProfileImage = (ImageView) view.findViewById(R.id.iv_image);
        tvExperience = (TextView) view.findViewById(R.id.tv_experience);
        tvGenderDate = (TextView) view.findViewById(R.id.tv_gender_date);
        tvSpecialities = (TextView) view.findViewById(R.id.tv_specialities);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);

        containerEducationDetail = (LinearLayout) view.findViewById(R.id.container_education_detail);
        containerRegistrationDetail = (LinearLayout) view.findViewById(R.id.container_registration_number_detail);
        containerAwardsAndPublication = (LinearLayout) view.findViewById(R.id.container_awards_and_publication);
        containerExperience = (LinearLayout) view.findViewById(R.id.container_experience);
        containerProfessionalMembership = (LinearLayout) view.findViewById(R.id.container_professional_membership);
        containerProfessionalStatement = (LinearLayout) view.findViewById(R.id.container_professional_statement);

        tvNoEducation = (TextView) view.findViewById(R.id.tv_no_education);
        tvNoRegistrationDetail = (TextView) view.findViewById(R.id.tv_no_registration_detail);
        tvNoProfessionalMembership = (TextView) view.findViewById(R.id.tv_no_professional_membership);
        tvNoProfessionalStatement = (TextView) view.findViewById(R.id.tv_no_professional_statement);
        tvNoExperience = (TextView) view.findViewById(R.id.tv_no_experience);
        tvNoAwardsAndPublication = (TextView) view.findViewById(R.id.tv_no_awards_and_publication);

        tvClinicNumbers = (TextView) view.findViewById(R.id.tv_clinic_numbers);
        tvWebsite = (TextView) view.findViewById(R.id.tv_website);
        tvEmailAddress = (TextView) view.findViewById(R.id.tv_clinic_email_address);
    }

    @Override
    public void initListeners() {
//        ivProfileImage.setOnClickListener(this);
//        ivDoctorCoverPhoto.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_image) {
            if (doctorProfile != null && !Util.isNullOrBlank(doctorProfile.getImageUrl()))
                mActivity.openEnlargedImageDialogFragment(doctorProfile.getImageUrl());
        } else if (id == R.id.iv_doctor_cover_photo) {
            if (doctorProfile != null && !Util.isNullOrBlank(doctorProfile.getCoverImageUrl()))
                mActivity.openEnlargedImageDialogFragment(doctorProfile.getCoverImageUrl());
        }
    }

    private void getDoctorProfileFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised
                (mActivity, LocalBackgroundTaskType.GET_DOCTOR_PROFILE, this,
                        this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                    if (!Util.isNullOrBlank(doctorId)) {
                        getDoctorProfileFromLocal();
                        return;
                    }
                    break;
                case GET_DOCTOR_PROFILE:
                    if (response.getData() != null) {
                        if (response.isDataFromLocal()) {
                            doctorProfile = (DoctorProfile) response.getData();
                            initData(doctorProfile);
                            mActivity.refreshMenuFragment(doctorProfile);
                            if (doctorProfile == null) {
                                WebDataServiceImpl.getInstance(mApp).getDoctorProfile(DoctorProfile.class, doctorId, null, null, this, this);
                                return;
                            }
                        } else {
                            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DOCTOR_PROFILE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        }
                    } else {
                        WebDataServiceImpl.getInstance(mApp).getDoctorProfile(DoctorProfile.class, doctorId, null, null, this, this);
                        return;
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void loadImages(DoctorProfile doctorProfile) {
        if (doctorProfile != null) {
            //loadng profile image and cover image
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_DOCTOR_PROFILE, this.doctorProfile, null, ivProfileImage, tvInitialAlphabet);
            if (!Util.isNullOrBlank(doctorProfile.getCoverImageUrl())) {
                doctorProfile.setCoverImagePath(ImageUtil.getPathToSaveFile(HealthCocoFileType.DOCTOR_COVER_IMAGE, Util.getFileNameFromUrl(doctorProfile.getCoverImageUrl()), Util.getFileExtension(doctorProfile.getCoverImageUrl())));
                new DownloadFileFromUrlAsyncTask(mActivity, this, HealthCocoFileType.DOCTOR_COVER_IMAGE, Util.getFileNameFromUrl(doctorProfile.getCoverImageUrl()), null, null).execute(doctorProfile.getCoverImageUrl());
            } else {
                ivDoctorCoverPhoto.setBackgroundResource(R.drawable.bg_doctor_img);
            }
        }
    }

    public void initData(DoctorProfile doctorProfile) {
        refreshDoctorProfileData(this.doctorProfile);
        initDoctorDetails(doctorProfile);
        if (clinicDetailResponse != null) {
            Location location = clinicDetailResponse.getLocation();
            if (location != null) {
                refreshClinicContacDetail(location);
            }
        }
    }

    private void refreshDoctorProfileData(DoctorProfile doctorProfile) {
        if (doctorProfile != null) {
            String title = doctorProfile.getTitle(false);
            if (Util.isNullOrBlank(title))
                title = getResources().getString(R.string.dr);
            tvName.setText(title + Util.getValidatedValue(doctorProfile.getFirstName()));

            if (!Util.isNullOrEmptyList(doctorProfile.getSpecialities())) {
                tvSpecialities.setVisibility(View.VISIBLE);
                getMergedSpecialities(doctorProfile.getSpecialities());
            } else {
                tvSpecialities.setVisibility(View.GONE);
                tvSpecialities.setText("");
            }
            if (doctorProfile.getExperience() != null && doctorProfile.getExperience().getExperience() != null) {
                tvExperience.setText(doctorProfile.getFormattedExperience(mActivity, false));
                tvExperience.setVisibility(View.VISIBLE);
            } else {
                tvExperience.setVisibility(View.GONE);
                tvExperience.setText("");
            }
            String formattedGenderAge = Util.getFormattedGenderAge(doctorProfile);
            if (!Util.isNullOrBlank(formattedGenderAge)) {
                tvGenderDate.setVisibility(View.VISIBLE);
                tvGenderDate.setText(formattedGenderAge);
            } else {
                tvGenderDate.setVisibility(View.GONE);
                tvGenderDate.setText("");
            }
            loadImages(doctorProfile);
        }
    }

    private void getMergedSpecialities(List<String> list) {
        String mergedText = "";
        for (String speciality :
                list) {
            int index = list.indexOf(speciality);
            if (list.size() > 1 && index != list.size() - 1)
                speciality = speciality + ",";
            mergedText = mergedText + speciality;
        }
        tvSpecialities.setText(mergedText);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_DOCTOR_PROFILE:
                LocalDataServiceImpl.getInstance(mApp).
                        addDoctorProfile((DoctorProfile) response.getData());
            case GET_DOCTOR_PROFILE:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileResponse(WebServiceType.GET_DOCTOR_PROFILE, doctorId, null, null);
                break;
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                    if (user != null)
                        clinicDetailResponse = LocalDataServiceImpl.getInstance(mApp).getClinicResponseDetails(user.getForeignLocationId());
                }
                return volleyResponseBean;
        }
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
    public void onPause() {
        super.onPause();
    }


    public void initDoctorDetails(DoctorProfile doctorProfile) {
        clearPreviuosDetails();
        if (doctorProfile != null) {
            this.doctorProfile = doctorProfile;
            refreshEducationDetails(doctorProfile);
            refreshRegistrationDetails(doctorProfile);
            refreshAwardsAndPublicationDetails(doctorProfile);
            refreshExperienceDetails(doctorProfile);
            refreshProfessionalMembershipDetails(doctorProfile);
            refreshProfessionalStatementDetails(doctorProfile);
        }
    }


    private void refreshProfessionalStatementDetails(DoctorProfile doctorProfile) {
        //setting professional statement
        if (doctorProfile.getProfessionalStatement() != null) {
            containerProfessionalStatement.setVisibility(View.VISIBLE);
            tvNoProfessionalStatement.setVisibility(View.GONE);
            addProfessionalStatementDetailItem(doctorProfile.getProfessionalStatement());
        } else {
            containerProfessionalStatement.setVisibility(View.GONE);
            tvNoProfessionalStatement.setVisibility(View.VISIBLE);
        }
    }

    private void refreshRegistrationDetails(DoctorProfile doctorProfile) {
        //setting registration details
        if (!Util.isNullOrEmptyList(doctorProfile.getRegistrationDetails())) {
            containerRegistrationDetail.setVisibility(View.VISIBLE);
            tvNoRegistrationDetail.setVisibility(View.GONE);
            addRegistrationDetailItem(doctorProfile.getRegistrationDetails());
        } else {
            containerRegistrationDetail.setVisibility(View.GONE);
            tvNoRegistrationDetail.setVisibility(View.VISIBLE);
        }
    }

    private void refreshAwardsAndPublicationDetails(DoctorProfile doctorProfile) {
        //setting awards and publication
        if (!Util.isNullOrEmptyList(doctorProfile.getAchievements())) {
            containerAwardsAndPublication.setVisibility(View.VISIBLE);
            tvNoAwardsAndPublication.setVisibility(View.GONE);
            addAwardsAndPublicationDetailItem(doctorProfile.getAchievements());
        } else {
            containerAwardsAndPublication.setVisibility(View.GONE);
            tvNoAwardsAndPublication.setVisibility(View.VISIBLE);
        }
    }

    private void refreshExperienceDetails(DoctorProfile doctorProfile) {
        //setting experience
        if (!Util.isNullOrEmptyList(doctorProfile.getExperienceDetails())) {
            containerExperience.setVisibility(View.VISIBLE);
            tvNoExperience.setVisibility(View.GONE);
            addExperienceDetailItem(doctorProfile.getExperienceDetails());
        } else {
            containerExperience.setVisibility(View.GONE);
            tvNoExperience.setVisibility(View.VISIBLE);
        }
    }

    private void refreshProfessionalMembershipDetails(DoctorProfile doctorProfile) {
        //setting professional membership
        if (!Util.isNullOrEmptyList(doctorProfile.getProfessionalMemberships())) {
            containerProfessionalMembership.setVisibility(View.VISIBLE);
            tvNoProfessionalMembership.setVisibility(View.GONE);
            addProfessionalMembershipDetailItem(doctorProfile.getProfessionalMemberships());
        } else {
            containerProfessionalMembership.setVisibility(View.GONE);
            tvNoProfessionalMembership.setVisibility(View.VISIBLE);
        }
    }


    private void refreshEducationDetails(DoctorProfile doctorProfile) {
        //setting education details
        if (!Util.isNullOrEmptyList(doctorProfile.getEducation())) {
            containerEducationDetail.setVisibility(View.VISIBLE);
            tvNoEducation.setVisibility(View.GONE);
            addEducationDetailItem(doctorProfile.getEducation());
        } else {
            containerEducationDetail.setVisibility(View.GONE);
            tvNoEducation.setVisibility(View.VISIBLE);
        }
    }

    private void addExperienceDetailItem(List<DoctorExperienceDetail> list) {
        containerExperience.removeAllViews();
        for (DoctorExperienceDetail experienceDetail : list) {
            LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_doctor_profile_experience_detail, null);
            TextView tvAwardsAndPublicationOrganization = (TextView) layout.findViewById(R.id.tv_awards_and_publication_organization);
            TextView tvAwardsAndPublicationYear = (TextView) layout.findViewById(R.id.tv_awards_and_publication_year);
            TextView tvAwardsAndPublicationCity = (TextView) layout.findViewById(R.id.tv_awards_and_publication_city);

            tvAwardsAndPublicationOrganization.setText(Util.getValidatedValue(experienceDetail.getOrganization()));
            tvAwardsAndPublicationYear.setText(Util.getValidatedValue(experienceDetail.getFromValue() + ":" + experienceDetail.getToValue()));
            tvAwardsAndPublicationCity.setText(Util.getValidatedValue(experienceDetail.getCity()));
            containerExperience.addView(layout);
        }
    }

    private void addProfessionalMembershipDetailItem(List<String> list) {
        containerProfessionalMembership.removeAllViews();
        for (String string : list) {
            LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_doctor_professional_membership_detail, null);
            TextView tvProfessionalMembershipDetail = (TextView) layout.findViewById(R.id.tv_professional_membership_detail);

            tvProfessionalMembershipDetail.setText(Util.getValidatedValue(string));
            containerProfessionalMembership.addView(layout);
        }
    }

    private void addProfessionalStatementDetailItem(String list) {
        containerProfessionalStatement.removeAllViews();
        LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_doctor_professional_membership_detail, null);
        TextView tvProfessionalMembershipDetail = (TextView) layout.findViewById(R.id.tv_professional_membership_detail);

        tvProfessionalMembershipDetail.setText(Util.getValidatedValue(list));
        containerProfessionalStatement.addView(layout);
    }

    private void addAwardsAndPublicationDetailItem(List<Achievement> list) {
        containerAwardsAndPublication.removeAllViews();
        for (Achievement achievement : list) {
            LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_doctor_profile_acheivements_detail, null);
            TextView tvAwardsAndPublicationDetail = (TextView) layout.findViewById(R.id.tv_awards_and_publication_detail);
            TextView tvAwardsAndPublicationYearDetail = (TextView) layout.findViewById(R.id.tv_awards_and_publication_year_detail);
            tvAwardsAndPublicationDetail.setText(Util.getValidatedValue(achievement.getAchievementName()));
            tvAwardsAndPublicationYearDetail.setText(Util.getValidatedValue(achievement.getYear()));
            containerAwardsAndPublication.addView(layout);
        }
    }

    private void addEducationDetailItem(List<Education> list) {
        containerEducationDetail.removeAllViews();
        for (Education education : list) {
            LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_doctor_profile_education_detail, null);
            TextView tvEducationYear = (TextView) layout.findViewById(R.id.tv_education_year);
            TextView tvEducationDetails = (TextView) layout.findViewById(R.id.tv_education_detail);
            tvEducationYear.setText(Util.getValidatedValue(education.getYearOfPassing()));
            tvEducationDetails.setText(Util.getValidatedValue(education.getQualification()) + CHARACTER_BETWEEN_EDUCATION_AND_REGISTRATION_DETAILS + Util.getValidatedValue(education.getCollegeUniversity()));
            containerEducationDetail.addView(layout);
        }
    }

    private void addRegistrationDetailItem(List<DoctorRegistrationDetail> list) {
        containerRegistrationDetail.removeAllViews();
        for (DoctorRegistrationDetail registrationDetail : list) {
            LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_doctor_profile_registration_detail, null);
            TextView tvRegistrationYear = (TextView) layout.findViewById(R.id.tv_registration_year);
            TextView tvRegistrationdetail = (TextView) layout.findViewById(R.id.tv_registration_detail);
            tvRegistrationYear.setText(Util.getValidatedValue(registrationDetail.getYearOfPassing()));
            tvRegistrationdetail.setText(Util.getValidatedValue(registrationDetail.getRegistrationId()) + CHARACTER_BETWEEN_EDUCATION_AND_REGISTRATION_DETAILS + Util.getValidatedValue(registrationDetail.getMedicalCouncil()));
            containerRegistrationDetail.addView(layout);
        }
    }

    private void clearPreviuosDetails() {
        containerEducationDetail.removeAllViews();
        containerRegistrationDetail.removeAllViews();
        containerAwardsAndPublication.removeAllViews();
        containerExperience.removeAllViews();
        containerProfessionalMembership.removeAllViews();
        containerProfessionalStatement.removeAllViews();

        containerEducationDetail.setVisibility(View.GONE);
        containerRegistrationDetail.setVisibility(View.GONE);
        containerAwardsAndPublication.setVisibility(View.GONE);
        containerExperience.setVisibility(View.GONE);
        containerProfessionalMembership.setVisibility(View.GONE);
        containerProfessionalStatement.setVisibility(View.GONE);

        tvNoEducation.setVisibility(View.VISIBLE);
        tvNoRegistrationDetail.setVisibility(View.VISIBLE);
        tvNoAwardsAndPublication.setVisibility(View.VISIBLE);
        tvNoProfessionalMembership.setVisibility(View.VISIBLE);
        tvNoProfessionalStatement.setVisibility(View.VISIBLE);
        tvNoExperience.setVisibility(View.VISIBLE);
    }

    public void refreshClinicContacDetail(Location location) {
        tvClinicNumbers.setText(getClinicNumbers(location));
        tvWebsite.setText(Util.getValidatedValueOrDash(mActivity, location.getWebsiteUrl()));

        tvEmailAddress.setText(Util.getValidatedValueOrDash(mActivity, location.getLocationEmailAddress()));
    }

    private String getClinicNumbers(Location location) {
        String numbers = "";
        if (!Util.isNullOrBlank(location.getClinicNumber()))
            numbers = location.getClinicNumber();
        if (!Util.isNullOrEmptyList(location.getAlternateClinicNumbers())) {
            for (String alternateNumber :
                    location.getAlternateClinicNumbers()) {
                if (!Util.isNullOrBlank(alternateNumber)) {
                    numbers = addCharacterToText("\n", numbers);
                    numbers = numbers + alternateNumber;
                }
            }
        }
        if (!Util.isNullOrBlank(location.getLocationPhoneNumber())) {
            numbers = addCharacterToText("\n", numbers);
            numbers = numbers + location.getLocationPhoneNumber();
        }
        if (Util.isNullOrBlank(numbers))
            return getResources().getString(R.string.no_text_dash);
        return numbers;
    }

    private String addCharacterToText(String character, String numbers) {
        if (!Util.isNullOrBlank(numbers))
            numbers = numbers + character;
        return numbers;
    }

}
