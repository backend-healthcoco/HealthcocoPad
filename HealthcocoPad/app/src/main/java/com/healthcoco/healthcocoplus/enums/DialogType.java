package com.healthcoco.healthcocoplus.enums;


import com.healthcoco.healthcocopad.R;

import java.util.ArrayList;

public enum DialogType {
    SELECT_IMAGE(
            R.string.select_image, new ArrayList<OptionsType>() {{
        add(OptionsType.CAMERA);
        add(OptionsType.GALLERY);
    }}),
    SELECT_LOGO_IMAGE(
            R.string.select_option, new ArrayList<OptionsType>() {{
        add(OptionsType.CAMERA);
        add(OptionsType.GALLERY);
        add(OptionsType.PREVIEW);
    }}),
    SELECT_FILE(R.string.select_file, null);

    private ArrayList<OptionsType> optionsType;

    DialogType(int stringId, ArrayList<OptionsType> optionsType) {
        this.stringId = stringId;
        this.optionsType = optionsType;
    }

    private int stringId;

    public int getStringId() {
        return stringId;
    }

    public ArrayList<OptionsType> getOptionsType() {
        return optionsType;
    }
}
