package com.healthcoco.healthcocopad.bean;

import android.os.Parcelable;

import com.healthcoco.healthcocopad.enums.CurrencyType;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class ConsultationFee implements Parcelable {
    public static final Creator<ConsultationFee> CREATOR = new Creator<ConsultationFee>() {
        @Override
        public ConsultationFee createFromParcel(android.os.Parcel in) {
            return new ConsultationFee(in);
        }

        @Override
        public ConsultationFee[] newArray(int size) {
            return new ConsultationFee[size];
        }
    };
    @Unique
    protected String foreignUniqueId;
    private Integer amount;
    private CurrencyType currency;

    public ConsultationFee() {
    }

    // "De-parcel object
    public ConsultationFee(android.os.Parcel in) {
        foreignUniqueId = in.readString();
        amount = in.readInt();
        currency = CurrencyType.values()[in.readInt()];
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(foreignUniqueId);
        dest.writeInt(amount);
        dest.writeInt(currency.ordinal());
    }
}
