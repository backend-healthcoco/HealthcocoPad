package com.healthcoco.healthcocoplus.bean.request;

import com.healthcoco.healthcocoplus.bean.server.FileDetails;

import java.util.ArrayList;

/**
 * Created by neha on 07/02/16.
 */
public class ClinicImageToSend {
    private String id;
    private ArrayList<FileDetails> images;
    private FileDetails image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<FileDetails> getImages() {
        return images;
    }

    public void setImages(ArrayList<FileDetails> images) {
        this.images = images;
    }

    public FileDetails getImage() {
        return image;
    }

    public void setImage(FileDetails image) {
        this.image = image;
    }
}
