package com.healthcoco.healthcocopad.utilities;

import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

/**
 * Created by Shreshtha on 18-03-2017.
 */

public class ObjectParcelConvertor implements ParcelConverter<Object> {

    @Override
    public void toParcel(Object input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        } else {
            parcel.writeParcelable(Parcels.wrap(input), 0);
        }
    }

    @Override
    public Object fromParcel(Parcel parcel) {
        return Parcels.unwrap(parcel.readParcelable(Object.class.getClassLoader()));
    }
}
