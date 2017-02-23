package com.healthcoco.healthcocoplus.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.healthcoco.healthcocoplus.enums.CurrencyType;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class ConsultationFee extends SugarRecord implements Parcelable {
    @Unique
    protected String foreignUniqueId;
    private Integer amount;

    private CurrencyType currency;

    public ConsultationFee() {
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foreignUniqueId);
        dest.writeInt(amount);
        dest.writeInt(currency.ordinal());
    }

    // "De-parcel object
    public ConsultationFee(Parcel in) {
        foreignUniqueId = in.readString();
        amount = in.readInt();
        currency = CurrencyType.values()[in.readInt()];
    }

    public static final Creator<ConsultationFee> CREATOR = new Creator<ConsultationFee>() {
        @Override
        public ConsultationFee createFromParcel(Parcel in) {
            return new ConsultationFee(in);
        }

        @Override
        public ConsultationFee[] newArray(int size) {
            return new ConsultationFee[size];
        }
    };
}
