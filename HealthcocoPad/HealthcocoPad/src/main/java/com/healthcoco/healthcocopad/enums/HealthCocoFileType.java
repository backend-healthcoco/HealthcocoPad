package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.utilities.ImageUtil;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 02-02-2017.
 */
public enum HealthCocoFileType {
    REPORT_IMAGE("Reports", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    DOCTOR_IMAGE("Doctor", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    REPORT_PDF("Reports", ".pdf"),
    PATIENT_PROFILE("Patients", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    DIAGRAM_IMAGE("Diagrams", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    CLINIC_IMAGE("Clinic Image", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    DOCTOR_PROFILE_IMAGE("Doctor Profile", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    DOCTOR_COVER_IMAGE("Doctor Cover Image", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    DOCTOR_PROFILE_IMAGE_THUMBNAIL("Doctor Thumbnai Images", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    ENLARGED_IMAGE("Enlarged Images", ImageUtil.DEFAULT_IMAGE_EXTENSION),
    DIAGRAMS_THUMBNAIL("Diagram Thumbnails", ImageUtil.DEFAULT_IMAGE_EXTENSION);

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
