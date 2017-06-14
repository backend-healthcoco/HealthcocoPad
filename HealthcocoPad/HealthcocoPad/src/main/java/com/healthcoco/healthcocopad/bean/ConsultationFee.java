package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.enums.CurrencyType;

import org.parceler.Parcel;

@Parcel
public class ConsultationFee {
    private Integer amount;

    private CurrencyType currency;

    public ConsultationFee() {
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
}
