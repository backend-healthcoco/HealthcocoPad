package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

public enum InvestigationType {

    HAEMATOLOGY("HAEMATOLOGY", R.string.haematology, R.string.haematology),
    BIO_CHEMISTRY("BIO_CHEMISTRY", R.string.bio_chemistry, R.string.bio_chemistry),
    LIPID_PROFILE("LIPID_PROFILE", R.string.lipid_profile, R.string.lipid_profile),
    KIDNEY_FUNCTION_TEST("KIDNEY_FUNCTION_TEST", R.string.kidney_function_test, R.string.kidney_function_test),
    LIVER_FUNCTION_TEST("LIVER_FUNCTION_TEST", R.string.liver_function_test, R.string.liver_function_test),
    UACR("UACR", R.string.uacr, R.string.uacr),
    URINE_ROUTINE("URINE_ROUTINE", R.string.urine_routine, R.string.urine_routine),
    THYROID_FUNCTION_TEST("THYROID_FUNCTION_TEST", R.string.thyroid_function_test, R.string.thyroid_function_test),
    PCOS("PCOS", R.string.pcos, R.string.pcos),
    OTHERS("OTHERS", R.string.others, R.string.others);

    private final String value;
    private int textId;
    private int hintId;

    private InvestigationType(String value, int textId, int hintId) {
        this.value = value;
        this.textId = textId;
        this.hintId = hintId;
    }

    public int getTextId() {
        return textId;
    }

    public int getHintId() {
        return hintId;
    }

    public String getValue() {
        return value;
    }


}
