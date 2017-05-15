package com.healthcoco.healthcocopad.custom;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by neha on 14/07/16.
 */
public class SavedState extends View.BaseSavedState {
    public static int stateToSave;

    public SavedState(Parcelable superState) {
        super(superState);
    }

    private SavedState(Parcel in) {
        super(in);
        this.stateToSave = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(this.stateToSave);
    }

    //required field that makes Parcelables from a Parcel
    public static final Creator<SavedState> CREATOR =
            new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
}