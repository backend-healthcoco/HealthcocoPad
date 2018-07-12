package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 01-02-2017.
 */
public enum PatientProfileScreenType {
    IN_DOCTOR_PROFILE(R.dimen.initial_alphabet_doctor_profile, R.dimen.initial_alphabet_doctor_profile, R.dimen.text_size_initial_alphabet_doctor_profile),
    IN_ABOUT_DOCTOR(R.dimen.width_doctor_profile, R.dimen.height_doctor_profile, R.dimen.about_us_social_icon_size),
    IN_ADD_EDIT_DOCTOR_PROFILE(R.dimen.initial_alphabet_add_edit_doctor_profile, R.dimen.initial_alphabet_add_edit_doctor_profile, R.dimen.text_size_initial_alphabet_add_edit_doctor_profile),
    IN_PATIENTS_LIST(R.dimen.image_patient_profile_alphabet, R.dimen.image_patient_profile_alphabet, R.dimen.text_size_initial_alphabet_patient_profile_menu),
    IN_PATIENT_REGISTRATION(R.dimen.image_patient_profile_alphabet, R.dimen.image_patient_profile_alphabet, R.dimen.text_size_initial_alphabet_patient_profile_menu),
    IN_ADD_VISIT_HEADER(R.dimen.initial_alphabet_patient_profile_emr_header, R.dimen.initial_alphabet_patient_profile_emr_header, R.dimen.text_size_initial_alphabet_patient_profile_emr_header),
    IN_EMR_HEADER(R.dimen.initial_alphabet_patient_profile_emr_header, R.dimen.initial_alphabet_patient_profile_emr_header, R.dimen.text_size_initial_alphabet_patient_profile_emr_header),
    IN_PATIENT_DEATIL_SCREEN_EXCEPT_PROFILE(R.dimen.image_patient_detail_layout, R.dimen.image_patient_detail_layout, R.dimen.text_size_patients_list_initial_alphabet_detail_layout),
    IN_PATIENT_DEATIL_PROFILE(R.dimen.image_patient_detail_profile, R.dimen.image_patient_detail_profile, R.dimen.text_size_initial_alphabet_patient_profile_menu),
    IN_MENU(R.dimen.image_patient_profile_menu, R.dimen.image_patient_profile_menu, R.dimen.text_size_initial_alphabet_patient_profile_menu),
    IN_CLINIC_LIST(R.dimen.clinic_logo_image, R.dimen.clinic_logo_image, R.dimen.text_size_initial_alphabet_clinic_list),;

    private final int imageWidth;
    private final int imageheight;
    private final int initialAlphabetTextSize;

    PatientProfileScreenType(int imageWidth, int imageheight, int initialAlphabetTextSize) {
        this.initialAlphabetTextSize = initialAlphabetTextSize;
        this.imageheight = imageheight;
        this.imageWidth = imageWidth;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageheight() {
        return imageheight;
    }

    public int getTextSize() {
        return initialAlphabetTextSize;
    }


}
