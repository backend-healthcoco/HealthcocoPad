package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.CurrencyType;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;
@org.parceler.Parcel
public class ConsultationFee extends SugarRecord{
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
}
