package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.VolleyResponseBean;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public interface LocalDoInBackgroundListenerOptimised {
    public VolleyResponseBean doInBackground(VolleyResponseBean response);

    public void onPostExecute(VolleyResponseBean aVoid);
}
