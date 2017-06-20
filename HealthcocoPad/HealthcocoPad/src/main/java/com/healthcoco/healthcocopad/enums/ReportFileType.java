package com.healthcoco.healthcocopad.enums;

/**
 * Created by neha on 02/01/16.
 */
public enum ReportFileType {
    //    PDF(".pdf"), TXT(".txt"),
    IMAGE("jpg"), OTHER_TYPE("");
    private final String fileExtension;

    ReportFileType(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
