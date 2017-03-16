package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.utilities.ImageUtil;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 02-02-2017.
 */
public enum HealthCocoFileType {
    ENLARGED_IMAGE("Enlarged Images", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    CLINIC_IMAGE("Clinic Image", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    PATIENT_PROFILE("Patients", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    DOCTOR_COVER_IMAGE("Doctor Cover Image", ImageUtil.DEFAULT_IMAGE_EXTENSION);

    private final String folderName;
    private ArrayList<Integer> optionIdsList;
    private String extension;

    private HealthCocoFileType(String folderName, String extension) {
        this.folderName = folderName;
        this.extension = extension;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getExtension() {
        return extension;
    }
}
