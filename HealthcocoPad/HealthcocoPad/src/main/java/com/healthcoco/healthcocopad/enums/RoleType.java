package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by neha on 14/06/16.
 */
public enum RoleType {
    SUPER_ADMIN("SUPER_ADMIN"),
    HOSPITAL_ADMIN("HOSPITAL_ADMIN"),
    LOCATION_ADMIN("LOCATION_ADMIN"),
    DOCTOR("DOCTOR"),
    PATIENT("PATIENT"),
    ADMIN("ADMIN"),
    CONSULTANT_DOCTOR("CONSULTANT_DOCTOR"),
    STAFF("STAFF");

    private final String text;

    RoleType(String text) {
        this.text = text;
    }

    public static boolean isOnlyConsultant(ArrayList<RoleType> roleTypes) {
        if (!Util.isNullOrEmptyList(roleTypes)
                && !roleTypes.contains(RoleType.SUPER_ADMIN)
                && !roleTypes.contains(RoleType.ADMIN)
                && !roleTypes.contains(RoleType.HOSPITAL_ADMIN)
                && !roleTypes.contains(RoleType.LOCATION_ADMIN)
                && roleTypes.contains(RoleType.CONSULTANT_DOCTOR))
            return true;
        return false;
    }

    public static boolean isOnlyConsultantOrIsOnlyDoctorOrBoth(ArrayList<RoleType> roleTypes) {
        if (!Util.isNullOrEmptyList(roleTypes)
                && !roleTypes.contains(RoleType.SUPER_ADMIN)
                && !roleTypes.contains(RoleType.ADMIN)
                && !roleTypes.contains(RoleType.HOSPITAL_ADMIN)
                && !roleTypes.contains(RoleType.LOCATION_ADMIN)
                && (roleTypes.contains(RoleType.CONSULTANT_DOCTOR) || roleTypes.contains(RoleType.DOCTOR)))
            return true;
        return false;
    }

    public static boolean isAdmin(ArrayList<RoleType> roleTypes) {
        if (!Util.isNullOrEmptyList(roleTypes)
                && (roleTypes.contains(RoleType.HOSPITAL_ADMIN)
                && roleTypes.contains(RoleType.LOCATION_ADMIN)))
            return true;
        return false;
    }

    public String getText() {
        return text;
    }

}
