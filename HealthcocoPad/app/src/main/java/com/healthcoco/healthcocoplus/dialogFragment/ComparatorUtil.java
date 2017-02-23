package com.healthcoco.healthcocoplus.dialogFragment;

import android.text.TextUtils;

import com.healthcoco.healthcocoplus.bean.server.Specialities;

import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public class ComparatorUtil {

    public static Comparator<Object> specialityListComparator = new Comparator<Object>() {

        @Override
        public int compare(Object object1, Object object2) {
            Specialities speciality1 = (Specialities) object1;
            Specialities speciality2 = (Specialities) object2;
            String name1 = TextUtils.isEmpty(speciality1.getSpeciality()) ? "" : speciality1.getSpeciality().trim().toUpperCase(Locale.getDefault());
            String name2 = TextUtils.isEmpty(speciality2.getSpeciality()) ? "" : speciality2.getSpeciality().trim().toUpperCase(Locale.getDefault());
            return name1.compareTo(name2);
        }
    };
}
