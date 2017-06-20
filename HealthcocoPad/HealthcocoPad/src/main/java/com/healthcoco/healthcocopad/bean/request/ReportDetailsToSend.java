package com.healthcoco.healthcocopad.bean.request;

import android.net.Uri;

import com.healthcoco.healthcocopad.enums.ReportFileType;


/**
 * Created by neha on 03/01/16.
 */
public class ReportDetailsToSend {
    private ReportFileType reportFileType;
    private Uri reportUri;
    private String fileExtension;

    public ReportFileType getReportFileType() {
        return reportFileType;
    }

    public void setReportFileType(ReportFileType reportFileType) {
        this.reportFileType = reportFileType;
    }

    public Uri getReportUri() {
        return reportUri;
    }

    public void setReportUri(Uri reportUri) {
        this.reportUri = reportUri;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
