package com.healthcoco.healthcocoplus.enums;

import com.healthcoco.healthcocoplus.bean.server.Disease;

/**
 * Created by Shreshtha on 25-02-2017.
 */
public enum ClassType {
    HISTORY(Disease.class), PATIENT(null), CLINICAL_NOTES(null), PRESCRIPTION(null),
    UI_PERMISSION(null), BILLING(null), TEMPLATE(null);

    private final Class<?> class1;

    ClassType(Class<?> class1) {
        this.class1 = class1;
    }

    public Class<?> getClassType() {
        return class1;
    }
}
