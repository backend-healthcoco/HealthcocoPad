package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.fragments.CalendarFragment;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by neha on 22/05/16.
 */
@Parcel
public class MultipleDayEventTable extends SugarRecord {
    @Unique
    private String customUniqueId;
    private Long fromDate;
    private String foreignTableId;
    private Long toDate;
    private String formattedFromDate;

    public MultipleDayEventTable() {

    }

    public MultipleDayEventTable(String foreignTableId, Long fromDate, Long toDate) {
        this.foreignTableId = foreignTableId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.formattedFromDate = DateTimeUtil.getFormattedDateTime(CalendarFragment.MONTH_FORMAT_FOR_THIS_SCREEN, fromDate);
        this.customUniqueId = foreignTableId + fromDate;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public String getForeignTableId() {
        return foreignTableId;
    }

    public void setForeignTableId(String foreignTableId) {
        this.foreignTableId = foreignTableId;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public String getFormattedFromDate() {
        return formattedFromDate;
    }

    public void setFormattedFromDate(String formattedFromDate) {
        this.formattedFromDate = formattedFromDate;
    }
}
