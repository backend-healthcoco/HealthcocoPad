package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Achievement;
import com.healthcoco.healthcocopad.bean.server.DoctorExperienceDetail;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorRegistrationDetail;
import com.healthcoco.healthcocopad.bean.server.Education;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDoctorAwardAndPublicationDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDoctorContactDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDoctorEducationDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDoctorExperienceDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDoctorMembershipDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDoctorRegistartionDetailDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDoctorStatementDialogFragment;
import com.healthcoco.healthcocopad.listeners.DoctorProfileListener;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by Shreshtha on 02-02-2017.
 */
public class MyProfileFragment extends HealthCocoFragment implements View.OnClickListener {
    private static final String CHARACTER_BETWEEN_EDUCATION_AND_REGISTRATION_DETAILS = " ; ";
    public static final int REQUEST_CODE_MY_PROFILE = 113;
    public static final String TAG_DOCTOR_PROFILE = "doctor_profile";

    private LinearLayout containerEducationDetail;
    private LinearLayout containerRegistrationDetail;
    private LinearLayout containerAwardsAndPublication;
    private LinearLayout containerExperience;
    private LinearLayout containerProfessionalMembership;
    private LinearLayout containerProfessionalStatement;

    private TextView tvPersonalMobileNumber;
    private TextView tvAdditionalMobileNumber;
    private TextView tvSecondaryEmail;
    private TextView tvPrimaryEmail;

    private TextView tvNoAwardsAndPublication;
    private TextView tvNoRegistrationDetail;
    private TextView tvNoProfessionalMembership;
    private TextView tvNoProfessionalStatement;
    private TextView tvNoExperience;
    private TextView tvNoEducation;

    private TextViewFontAwesome btEditEducation;
    private TextViewFontAwesome btEditAwardsAndPublication;
    private TextViewFontAwesome btEditRegistrationDetail;
    private TextViewFontAwesome btEditContact;
    private TextViewFontAwesome btEditExperience;
    private TextViewFontAwesome btEditProfessionalMembership;
    private TextViewFontAwesome btEditProfessionalStatement;
    private DoctorProfile doctorProfile;

    public MyProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
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
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
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


        tvPersonalMobileNumber = (TextView) view.findViewById(R.id.tv_personal_mobile_number);
        tvAdditionalMobileNumber = (TextView) view.findViewById(R.id.tv_additional_mobile_number);
        tvPrimaryEmail = (TextView) view.findViewById(R.id.tv_primary_email);
        tvSecondaryEmail = (TextView) view.findViewById(R.id.tv_secondary_email);

        btEditEducation = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_education);
        btEditRegistrationDetail = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_registration_detail);
        btEditEducation = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_education);
        btEditAwardsAndPublication = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_awards_and_publication);
        btEditExperience = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_experience);
        btEditProfessionalMembership = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_professional_membership);
        btEditProfessionalStatement = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_professional_statement);
        btEditContact = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_contact);
    }

    @Override
    public void initListeners() {
        btEditContact.setOnClickListener(this);
        btEditAwardsAndPublication.setOnClickListener(this);
        btEditEducation.setOnClickListener(this);
        btEditRegistrationDetail.setOnClickListener(this);
        btEditExperience.setOnClickListener(this);
        btEditProfessionalMembership.setOnClickListener(this);
        btEditProfessionalStatement.setOnClickListener(this);
    }

    public void initData(DoctorProfile doctorProfile) {
        clearPreviuosDetails();
        if (doctorProfile != null) {
            this.doctorProfile = doctorProfile;
            refreshEducationDetails(doctorProfile);
            refreshRegistrationDetails(doctorProfile);
            refreshAwardsAndPublicationDetails(doctorProfile);
            refreshExperienceDetails(doctorProfile);
            refreshProfessionalMembershipDetails(doctorProfile);
            refreshProfessionalStatementDetails(doctorProfile);
            refreshContactDetails(doctorProfile);
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

    private void refreshContactDetails(DoctorProfile doctorProfile) {
        //setting contact details
        String mobileNumber = "";
        String additionalMobileNumber = "";
        String primaryEmailAddress = "";
        String secondaryEmailAddres = "";

        mobileNumber = doctorProfile.getMobileNumber();
        if (!Util.isNullOrEmptyList(doctorProfile.getAdditionalNumbers()))
            additionalMobileNumber = doctorProfile.getAdditionalNumbers().get(0);
        primaryEmailAddress = doctorProfile.getEmailAddress();
        if (!Util.isNullOrEmptyList(doctorProfile.getOtherEmailAddresses()))
            secondaryEmailAddres = doctorProfile.getOtherEmailAddresses().get(0);
        tvPersonalMobileNumber.setText(Util.getValidatedValueOrDash(mActivity, mobileNumber));
        tvAdditionalMobileNumber.setText(Util.getValidatedValueOrDash(mActivity, additionalMobileNumber));
        tvPrimaryEmail.setText(Util.getValidatedValueOrDash(mActivity, primaryEmailAddress));
        tvSecondaryEmail.setText(Util.getValidatedValueOrDash(mActivity, secondaryEmailAddres));
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

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.bt_edit_contact) {
            openDialogFragment(
                    new AddEditDoctorContactDialogFragment(),
                    AddEditDoctorContactDialogFragment.TAG_DOCTOR_CONTACT_DETAIL,
                    doctorProfile,
                    REQUEST_CODE_MY_PROFILE
            );
        } else if (id == R.id.bt_edit_awards_and_publication) {
            openDialogFragment(
                    new AddEditDoctorAwardAndPublicationDialogFragment(),
                    AddEditDoctorAwardAndPublicationDialogFragment.TAG_DOCTOR_ACHIEVEMENTS,
                    doctorProfile.getAchievements(),
                    REQUEST_CODE_MY_PROFILE
            );
        } else if (id == R.id.bt_edit_education) {
            openDialogFragment(
                    new AddEditDoctorEducationDialogFragment(),
                    AddEditDoctorEducationDialogFragment.TAG_DOCTOR_EDUCATION,
                    doctorProfile.getEducation(),
                    REQUEST_CODE_MY_PROFILE
            );
        } else if (id == R.id.bt_edit_registration_detail) {
            openDialogFragment(
                    new AddEditDoctorRegistartionDetailDialogFragment(),
                    AddEditDoctorRegistartionDetailDialogFragment.TAG_DOCTOR_REGISTRATION_DETAIL,
                    doctorProfile.getRegistrationDetails(),
                    REQUEST_CODE_MY_PROFILE
            );
        } else if (id == R.id.bt_edit_experience) {
            openDialogFragment(
                    new AddEditDoctorExperienceDialogFragment(),
                    AddEditDoctorExperienceDialogFragment.TAG_DOCTOR_EXPERIENCE_DETAIL,
                    doctorProfile.getExperienceDetails(),
                    REQUEST_CODE_MY_PROFILE
            );
        } else if (id == R.id.bt_edit_professional_membership) {
            openDialogFragment(
                    new AddEditDoctorMembershipDialogFragment(),
                    AddEditDoctorMembershipDialogFragment.TAG_DOCTOR_MEMBERSHIP_DETAIL,
                    doctorProfile.getProfessionalMemberships(),
                    REQUEST_CODE_MY_PROFILE
            );
        } else if (id == R.id.bt_edit_professional_statement) {
            openDialogFragment(
                    new AddEditDoctorStatementDialogFragment(),
                    AddEditDoctorStatementDialogFragment.TAG_DOCTOR_STATEMENT_DETAIL,
                    doctorProfile.getProfessionalStatement(),
                    REQUEST_CODE_MY_PROFILE
            );
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MY_PROFILE
                && data != null && data.hasExtra(TAG_DOCTOR_PROFILE)
                && doctorProfile != null) {
            DoctorProfile profile = Parcels.unwrap(data.getParcelableExtra(TAG_DOCTOR_PROFILE));
            switch (resultCode) {
                case HealthCocoConstants.RESULT_CODE_DOCTOR_PROFESSIONAL_STATEMENT_DETAIL:
                    doctorProfile.setProfessionalStatement(profile.getProfessionalStatement());
                    refreshProfessionalStatementDetails(doctorProfile);
                    break;
                case HealthCocoConstants.RESULT_CODE_DOCTOR_PROFESSIONAL_MEMBERSHIP_DETAIL:
                    doctorProfile.setProfessionalMemberships(profile.getProfessionalMemberships());
                    refreshProfessionalMembershipDetails(doctorProfile);
                    break;
                case HealthCocoConstants.RESULT_CODE_AWARDS_AND_PUNLICATION:
                    doctorProfile.setAchievements(profile.getAchievements());
                    refreshAwardsAndPublicationDetails(doctorProfile);
                    break;
                case HealthCocoConstants.RESULT_CODE_EDUCATIONAL_DETAIL:
                    doctorProfile.setEducation(profile.getEducation());
                    refreshEducationDetails(doctorProfile);
                    break;
                case HealthCocoConstants.RESULT_CODE_REGISTRATION_DETAIL:
                    doctorProfile.setRegistrationDetails(profile.getRegistrationDetails());
                    refreshRegistrationDetails(doctorProfile);
                    break;
                case HealthCocoConstants.RESULT_CODE_EXPERIENCE_DETAIL:
                    doctorProfile.setExperienceDetails(profile.getExperienceDetails());
                    refreshExperienceDetails(doctorProfile);
                    break;
                case HealthCocoConstants.RESULT_CODE_DOCTOR_PROFILE_CONTACT:
                    doctorProfile = profile;
                    refreshContactDetails(doctorProfile);
                    break;
            }
        }
    }
}
