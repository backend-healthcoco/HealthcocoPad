package com.healthcoco.healthcocoplus.listeners;

import com.android.volley.VolleyError;

/**
 * Created by neha on 26/11/15.
 */
public interface NetworkImageViewRequestListener {
    public void onSuccessResponse();

    public void onErrorResponse(VolleyError error);
}
