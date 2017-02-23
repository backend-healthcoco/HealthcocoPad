package com.healthcoco.healthcocoplus.listeners;

import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public interface LocalDoInBackgroundListenerOptimised {
    public VolleyResponseBean doInBackground(VolleyResponseBean response);
    public void onPostExecute(VolleyResponseBean aVoid);
}
