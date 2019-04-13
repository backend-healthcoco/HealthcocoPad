package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.bean.MenuItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Shreshtha on 12-Oct-17.
 */

public enum AccountPackageType {
    ADVANCE("ADVANCE",
            new LinkedHashMap<FragmentType, MenuItem>() {{
                put(FragmentType.CONTACTS, new MenuItem(FragmentType.CONTACTS, "", false));
                put(FragmentType.QUEUE, new MenuItem(FragmentType.QUEUE, "", false));
                put(FragmentType.EVENTS, new MenuItem(FragmentType.EVENTS, "", false));
                put(FragmentType.PROFILE, new MenuItem(FragmentType.PROFILE, "", false));
                put(FragmentType.CLINIC_PROFILE, new MenuItem(FragmentType.CLINIC_PROFILE, "", false));
                put(FragmentType.VIDEOS, new MenuItem(FragmentType.VIDEOS, "", false));
                put(FragmentType.HELP_IMPROVE, new MenuItem(FragmentType.HELP_IMPROVE, "", false));
                put(FragmentType.SETTINGS, new MenuItem(FragmentType.SETTINGS, "", false));
                put(FragmentType.KIOSK, new MenuItem(FragmentType.KIOSK, "", false));
//                put(FragmentType.NEED_HELP, new MenuItem(FragmentType.NEED_HELP, "", false));
//                put(FragmentType.LAB_REPORTS, new MenuItem(FragmentType.LAB_REPORTS, "", false));
//                put(FragmentType.DENTAL_WORKS, new MenuItem(FragmentType.DENTAL_WORKS, "", false));
//                put(FragmentType.REGISTER, new MenuItem(FragmentType.REGISTER, "", false));
//                put(FragmentType.ANALYTICS, new MenuItem(FragmentType.ANALYTICS, "", false));
            }},
            new ArrayList<PatientDetailTabType>() {{
                add(PatientDetailTabType.PATIENT_DETAIL_PROFILE);
                add(PatientDetailTabType.PATIENT_DETAIL_VISIT);
                add(PatientDetailTabType.PATIENT_DETAIL_CLINICAL_NOTES);
                add(PatientDetailTabType.PATIENT_DETAIL_REPORTS);
                add(PatientDetailTabType.PATIENT_DETAIL_PRESCRIPTION);
                add(PatientDetailTabType.PATIENT_DETAIL_APPOINTMENT);
                //                add(PatientDetailTabType.MY_FILES);
                add(PatientDetailTabType.PATIENT_DETAIL_TREATMENT);
                add(PatientDetailTabType.PATIENT_DETAIL_INVOICE);
                add(PatientDetailTabType.PATIENT_DETAIL_RECEIPT);
//                add(PatientDetailTabType.REGISTER);
            }},
            new ArrayList<SettingsItemType>() {{
                add(SettingsItemType.GROUPS);
                add(SettingsItemType.CLINICAL_NOTE);
                add(SettingsItemType.HISTORY);
                add(SettingsItemType.PRESCRIPTION);
                add(SettingsItemType.TEMPLATES);
                add(SettingsItemType.TREATMENT);
                add(SettingsItemType.PRINT);
                add(SettingsItemType.KIOSK);
                add(SettingsItemType.UI_PERMISSION);
                add(SettingsItemType.DELETED_PATIENTS);
                add(SettingsItemType.HELP_IMPROVE);
//                add(SettingsItemType.SYNC_CONTACT);
                add(SettingsItemType.SYNC);
                add(SettingsItemType.ABOUT);
//                add(SettingsItemType.RATE_US);
                add(SettingsItemType.SIGN_OUT);
            }}),

    PRO("PRO", new LinkedHashMap<FragmentType, MenuItem>() {{
        put(FragmentType.CONTACTS, new MenuItem(FragmentType.CONTACTS, "", false));
        put(FragmentType.QUEUE, new MenuItem(FragmentType.QUEUE, "", false));
        put(FragmentType.EVENTS, new MenuItem(FragmentType.EVENTS, "", false));
        put(FragmentType.PROFILE, new MenuItem(FragmentType.PROFILE, "", false));
        put(FragmentType.CLINIC_PROFILE, new MenuItem(FragmentType.CLINIC_PROFILE, "", false));
        put(FragmentType.VIDEOS, new MenuItem(FragmentType.VIDEOS, "", false));
        put(FragmentType.HELP_IMPROVE, new MenuItem(FragmentType.HELP_IMPROVE, "", false));
        put(FragmentType.SETTINGS, new MenuItem(FragmentType.SETTINGS, "", false));
        put(FragmentType.KIOSK, new MenuItem(FragmentType.KIOSK, "", false));
//                put(FragmentType.NEED_HELP, new MenuItem(FragmentType.NEED_HELP, "", false));
//                put(FragmentType.LAB_REPORTS, new MenuItem(FragmentType.LAB_REPORTS, "", false));
//                put(FragmentType.DENTAL_WORKS, new MenuItem(FragmentType.DENTAL_WORKS, "", false));
//                put(FragmentType.REGISTER, new MenuItem(FragmentType.REGISTER, "", false));
//                put(FragmentType.ANALYTICS, new MenuItem(FragmentType.ANALYTICS, "", false));
    }},
            new ArrayList<PatientDetailTabType>() {{
                add(PatientDetailTabType.PATIENT_DETAIL_PROFILE);
                add(PatientDetailTabType.PATIENT_DETAIL_VISIT);
                add(PatientDetailTabType.PATIENT_DETAIL_CLINICAL_NOTES);
                add(PatientDetailTabType.PATIENT_DETAIL_REPORTS);
                add(PatientDetailTabType.PATIENT_DETAIL_PRESCRIPTION);
                add(PatientDetailTabType.PATIENT_DETAIL_APPOINTMENT);
                //                add(PatientDetailTabType.MY_FILES);
                add(PatientDetailTabType.PATIENT_DETAIL_TREATMENT);
                add(PatientDetailTabType.PATIENT_DETAIL_INVOICE);
                add(PatientDetailTabType.PATIENT_DETAIL_RECEIPT);
//                add(PatientDetailTabType.REGISTER);
            }},
            new ArrayList<SettingsItemType>() {{
                add(SettingsItemType.GROUPS);
                add(SettingsItemType.CLINICAL_NOTE);
                add(SettingsItemType.HISTORY);
                add(SettingsItemType.PRESCRIPTION);
                add(SettingsItemType.TEMPLATES);
                add(SettingsItemType.TREATMENT);
                add(SettingsItemType.PRINT);
                add(SettingsItemType.KIOSK);
                add(SettingsItemType.UI_PERMISSION);
                add(SettingsItemType.HELP_IMPROVE);
                add(SettingsItemType.DELETED_PATIENTS);
                add(SettingsItemType.SYNC);
                add(SettingsItemType.ABOUT);
//                add(SettingsItemType.RATE_US);
                add(SettingsItemType.SIGN_OUT);
            }}),

    BASIC("BASIC", new LinkedHashMap<FragmentType, MenuItem>() {{
        put(FragmentType.CONTACTS, new MenuItem(FragmentType.CONTACTS, "", false));
//        put(FragmentType.QUEUE, new MenuItem(FragmentType.QUEUE, "", false));
        put(FragmentType.PROFILE, new MenuItem(FragmentType.PROFILE, "", false));
        put(FragmentType.CLINIC_PROFILE, new MenuItem(FragmentType.CLINIC_PROFILE, "", false));
//        put(FragmentType.VIDEOS, new MenuItem(FragmentType.VIDEOS, "", false));
        put(FragmentType.HELP_IMPROVE, new MenuItem(FragmentType.HELP_IMPROVE, "", false));
        put(FragmentType.SETTINGS, new MenuItem(FragmentType.SETTINGS, "", false));
        put(FragmentType.KIOSK, new MenuItem(FragmentType.KIOSK, "", false));
//        put(FragmentType.LAB_REPORTS, new MenuItem(FragmentType.LAB_REPORTS, "", false));
//        put(FragmentType.DENTAL_WORKS, new MenuItem(FragmentType.DENTAL_WORKS, "", false));
//        put(FragmentType.REGISTER, new MenuItem(FragmentType.REGISTER, "", false));
//        put(FragmentType.ANALYTICS, new MenuItem(FragmentType.ANALYTICS, "", false));
    }},
            new ArrayList<PatientDetailTabType>() {{
                add(PatientDetailTabType.PATIENT_DETAIL_PROFILE);
                add(PatientDetailTabType.PATIENT_DETAIL_APPOINTMENT);
            }},
            new ArrayList<SettingsItemType>() {{
                add(SettingsItemType.GROUPS);
//                add(SettingsItemType.REFERENCE);
                add(SettingsItemType.HELP_IMPROVE);
//                add(SettingsItemType.SYNC_CONTACT);
                add(SettingsItemType.KIOSK);
                add(SettingsItemType.ABOUT);
//                add(SettingsItemType.RATE_US);
                add(SettingsItemType.SIGN_OUT);
            }}),

    FREE("FREE", new LinkedHashMap<FragmentType, MenuItem>() {{
        put(FragmentType.CONTACTS, new MenuItem(FragmentType.CONTACTS, "", false));
        put(FragmentType.PROFILE, new MenuItem(FragmentType.PROFILE, "", false));
        put(FragmentType.CLINIC_PROFILE, new MenuItem(FragmentType.CLINIC_PROFILE, "", false));
//        put(FragmentType.VIDEOS, new MenuItem(FragmentType.VIDEOS, "", false));
        put(FragmentType.HELP_IMPROVE, new MenuItem(FragmentType.HELP_IMPROVE, "", false));
        put(FragmentType.SETTINGS, new MenuItem(FragmentType.SETTINGS, "", false));
//        put(FragmentType.KIOSK, new MenuItem(FragmentType.KIOSK, "", false));
//        put(FragmentType.DENTAL_WORKS, new MenuItem(FragmentType.DENTAL_WORKS, "", false));
//        put(FragmentType.LAB_REPORTS, new MenuItem(FragmentType.LAB_REPORTS, "", false));
//        put(FragmentType.ANALYTICS, new MenuItem(FragmentType.ANALYTICS, "", false));
//        put(FragmentType.NEED_HELP, new MenuItem(FragmentType.NEED_HELP, "", false));
    }},
            new ArrayList<PatientDetailTabType>() {{
                add(PatientDetailTabType.PATIENT_DETAIL_PROFILE);
//                add(PatientDetailTabType.LAB_REPORTS);
            }},
            new ArrayList<SettingsItemType>() {{
                add(SettingsItemType.GROUPS);
//                add(SettingsItemType.REFERENCE);
                add(SettingsItemType.HELP_IMPROVE);
//                add(SettingsItemType.SYNC_CONTACT);
                add(SettingsItemType.ABOUT);
//                add(SettingsItemType.RATE_US);
                add(SettingsItemType.SIGN_OUT);
            }});


    private String value;
    private LinkedHashMap<FragmentType, MenuItem> menuItemList;
    private ArrayList<PatientDetailTabType> tabItemList;
    private ArrayList<SettingsItemType> settingItemList;

    AccountPackageType(String value, LinkedHashMap<FragmentType,
            MenuItem> MenuItemList, ArrayList<PatientDetailTabType> tabItemList,
                       ArrayList<SettingsItemType> settingItemList) {
        this.value = value;
        this.menuItemList = MenuItemList;
        this.tabItemList = tabItemList;
        this.settingItemList = settingItemList;
    }

    public LinkedHashMap<FragmentType, MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public ArrayList<PatientDetailTabType> getTabItemList() {
        return tabItemList;
    }

    public ArrayList<SettingsItemType> getSettingItemList() {
        return settingItemList;
    }

    public String getValue() {
        return value;
    }
}
