package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 07-07-2017.
 */
@Parcel
public class AdvanceReceiptIdWithAmountsResponse extends SugarRecord {
    @Unique
    private String customUniqueId;
    @Unique
    private String receiptId;
    private String uniqueReceiptId;
    private Double usedAdvanceAmount;

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getUniqueReceiptId() {
        return uniqueReceiptId;
    }

    public void setUniqueReceiptId(String uniqueReceiptId) {
        this.uniqueReceiptId = uniqueReceiptId;
    }

    public Double getUsedAdvanceAmount() {
        return usedAdvanceAmount;
    }

    public void setUsedAdvanceAmount(Double usedAdvanceAmount) {
        this.usedAdvanceAmount = usedAdvanceAmount;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }
}
