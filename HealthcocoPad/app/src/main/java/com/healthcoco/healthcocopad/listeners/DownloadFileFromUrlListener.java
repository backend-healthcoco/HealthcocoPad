package com.healthcoco.healthcocopad.listeners;

/**
 * Created by Shreshtha on 02-02-2017.
 */

public interface DownloadFileFromUrlListener {
    public void onPostExecute(String filePath);
    public void onPreExecute();
}

