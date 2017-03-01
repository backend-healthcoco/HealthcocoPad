package com.healthcoco.healthcocoplus.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 01-02-2017.
 */
public enum PatientProfileScreenType {
    IN_DOCTOR_PROFILE(R.dimen.initial_alphabet_doctor_profile, R.dimen.text_size_initial_alphabet_doctor_profile),
    IN_PATIENTS_LIST(R.dimen.image_patient_profile_alphabet, R.dimen.text_size_initial_alphabet_patient_profile_menu),
    IN_MENU(R.dimen.image_patient_profile_menu, R.dimen.text_size_initial_alphabet_patient_profile_menu);
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
