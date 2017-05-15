package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;

/**
 * Created by neha on 01/11/15.
 */
public interface LocalDoInBackgroundListener {
    public VolleyResponseBean doInBackground(LocalBackgroundTaskType localBackgroundTaskType);

    public void onPostExecute(VolleyResponseBean aVoid);
}
