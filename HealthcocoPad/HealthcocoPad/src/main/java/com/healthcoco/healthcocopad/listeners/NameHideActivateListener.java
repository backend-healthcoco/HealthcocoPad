package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.enums.NameHideActivateType;

/**
 * Created by neha on 15/01/16.
 */
public interface NameHideActivateListener {
    public void onHideClicked(NameHideActivateType itemType, Object object);

    public void onActivateClicked(NameHideActivateType itemType, Object object);

    public boolean isInitialLoading();

    public void onEditClicked(NameHideActivateType itemType, Object object);
}
