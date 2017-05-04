package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 01-02-2017.
 */
public enum PatientProfileScreenType {
    IN_DOCTOR_PROFILE(R.dimen.initial_alphabet_doctor_profile, R.dimen.text_size_initial_alphabet_doctor_profile),
    IN_PATIENTS_LIST(R.dimen.image_patient_profile_alphabet, R.dimen.text_size_initial_alphabet_patient_profile_menu),
    IN_PATIENT_REGISTRATION(R.dimen.image_patient_profile_alphabet, R.dimen.text_size_initial_alphabet_patient_profile_menu),
    IN_ADD_VISIT_HEADER(R.dimen.initial_alphabet_patient_profile_emr_header, R.dimen.text_size_initial_alphabet_patient_profile_emr_header),
    IN_EMR_HEADER(R.dimen.initial_alphabet_patient_profile_emr_header, R.dimen.text_size_initial_alphabet_patient_profile_emr_header),
    IN_PATIENT_DEATIL_SCREEN_EXCEPT_PROFILE(R.dimen.image_patient_detail_layout, R.dimen.text_size_patients_list_initial_alphabet_detail_layout),
    IN_PATIENT_DEATIL_PROFILE(R.dimen.image_patient_detail_profile, R.dimen.text_size_initial_alphabet_patient_profile_menu),
    IN_MENU(R.dimen.image_patient_profile_menu, R.dimen.text_size_initial_alphabet_patient_profile_menu),
    IN_CLINIC_LIST(R.dimen.clinic_logo_image, R.dimen.text_size_initial_alphabet_clinic_list), ;
    private final int imageSize;
    private final int initialAlphabetTextSize;

    PatientProfileScreenType(int imageSize, int initialAlphabetTextSize) {
        this.initialAlphabetTextSize = initialAlphabetTextSize;
        this.imageSize = imageSize;
    }

    public int getImageViewSize() {
        return imageSize;
    }

    public int getTextSize() {
        return initialAlphabetTextSize;
    }

}
