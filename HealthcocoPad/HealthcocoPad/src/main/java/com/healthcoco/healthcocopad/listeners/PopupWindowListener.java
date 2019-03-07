package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.enums.PopupWindowType;

/**
 * Created by neha on 03/05/17.
 */

public interface PopupWindowListener {
    public void onItemSelected(PopupWindowType popupWindowType, Object object);
}
