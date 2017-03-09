package com.healthcoco.healthcocoplus.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.healthcoco.healthcocoplus.enums.AchievementType;
import com.orm.SugarRecord;
@org.parceler.Parcel
public class Achievement extends SugarRecord implements Parcelable {
    private String achievementName;

    private int year;

    private AchievementType achievementType;
    protected String foreignUniqueId;

    public Achievement() {
    }

    protected Achievement(Parcel in) {
        achievementName = in.readString();
        year = in.readInt();
        foreignUniqueId = in.readString();
        achievementType = AchievementType.values()[in.readInt()];
    }

    public static final Creator<Achievement> CREATOR = new Creator<Achievement>() {
        @Override
        public Achievement createFromParcel(Parcel in) {
            return new Achievement(in);
        }

        @Override
        public Achievement[] newArray(int size) {
            return new Achievement[size];
        }
    };

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public AchievementType getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(AchievementType achievementType) {
        this.achievementType = achievementType;
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(achievementName);
        dest.writeInt(year);
        dest.writeString(foreignUniqueId);
        dest.writeInt(achievementType.ordinal());
    }
}
