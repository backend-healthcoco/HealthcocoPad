package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.enums.FragmentType;

import java.util.LinkedHashMap;

/**
 * Created by neha on 01/04/17.
 */

public class MenuItem {
    private FragmentType fragmentType;
    private String notifNo;
    private boolean showLoader;

    public MenuItem(FragmentType fragmentType, String notifNo, boolean showLoader) {
        this.fragmentType = fragmentType;
        this.notifNo = notifNo;
        this.showLoader = showLoader;
    }

    public static LinkedHashMap<FragmentType, MenuItem> getMenuItemsList() {
        LinkedHashMap<FragmentType, MenuItem> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(FragmentType.CONTACTS, new MenuItem(FragmentType.CONTACTS, "", false));
        linkedHashMap.put(FragmentType.CALENDAR, new MenuItem(FragmentType.CALENDAR, "", false));
        linkedHashMap.put(FragmentType.PROFILE, new MenuItem(FragmentType.PROFILE, "", false));
        linkedHashMap.put(FragmentType.CLINIC_PROFILE, new MenuItem(FragmentType.CLINIC_PROFILE, "", false));
        linkedHashMap.put(FragmentType.SETTINGS, new MenuItem(FragmentType.SETTINGS, "", false));
        linkedHashMap.put(FragmentType.REGISTER, new MenuItem(FragmentType.REGISTER, "", false));
        linkedHashMap.put(FragmentType.VIDEOS, new MenuItem(FragmentType.VIDEOS, "", false));
        return linkedHashMap;
    }

    public FragmentType getFragmentType() {
        return fragmentType;
    }

    public void setFragmentType(FragmentType fragmentType) {
        this.fragmentType = fragmentType;
    }

    public String getNotifNo() {
        return notifNo;
    }

    public void setNotifNo(String notifNo) {
        this.notifNo = notifNo;
    }

    public boolean isShowLoader() {
        return showLoader;
    }

    public void setShowLoader(boolean showLoader) {
        this.showLoader = showLoader;
    }
}
