package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.User;

/**
 * Created by Prashant on 13/03/2018.
 */

public interface QueueListitemlistener {

    public void onCheckInClicked(Object object);

    public User getUser();
}
