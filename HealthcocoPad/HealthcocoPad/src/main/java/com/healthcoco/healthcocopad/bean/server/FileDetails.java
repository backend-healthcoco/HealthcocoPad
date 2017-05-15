package com.healthcoco.healthcocopad.bean.server;

import android.graphics.Bitmap;

import org.parceler.Parcel;

@Parcel
public class FileDetails {

    private String fileEncoded;

    private String fileDecoded;

    private String fileName;

    private String fileExtension;
    protected Bitmap bitmap;

    public String getFileEncoded() {
        return fileEncoded;
    }

    public void setFileEncoded(String fileEncoded) {
        this.fileEncoded = fileEncoded;
    }

    public String getFileDecoded() {
        return fileDecoded;
    }

    public void setFileDecoded(String fileDecoded) {
        this.fileDecoded = fileDecoded;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
