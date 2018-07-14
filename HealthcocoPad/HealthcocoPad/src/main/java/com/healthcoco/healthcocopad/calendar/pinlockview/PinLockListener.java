package com.healthcoco.healthcocopad.calendar.pinlockview;


public interface PinLockListener {

    void onComplete(String pin);

    void onEmpty();

    void onPinEntered(String key);

    void onPinDeleted();

    boolean isReset();
}
