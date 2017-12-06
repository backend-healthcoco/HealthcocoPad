package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.bean.server.Records;

import java.io.File;

/**
 * Created by Prashant on 06/12/2017.
 */

public class RecordMultipartRequest {

    private Records data;
    private File file;

    public Records getData() {
        return data;
    }

    public void setData(Records data) {
        this.data = data;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
