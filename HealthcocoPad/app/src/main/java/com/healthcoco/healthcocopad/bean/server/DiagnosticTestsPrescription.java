package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.io.Serializable;

/**
 * Created by neha on 15/04/16.
 */
public class DiagnosticTestsPrescription extends SugarRecord implements Serializable {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DiagnosticTestsPrescription.class.getSimpleName());
    private String foreignTableId;
    @Ignore
    private DiagnosticTest test;
    private String foreignDiagnosticTestId;
    private String recordId;
    @Unique
    private String customUniqueId;


    public DiagnosticTest getTest() {
        return test;
    }

    public void setTest(DiagnosticTest test) {
        this.test = test;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getForeignTableId() {
        return foreignTableId;
    }

    public void setForeignTableId(String foreignTableId) {
        this.foreignTableId = foreignTableId;
    }

    public String getForeignDiagnosticTestId() {
        return foreignDiagnosticTestId;
    }

    public void setForeignDiagnosticTestId(String foreignDiagnosticTestId) {
        this.foreignDiagnosticTestId = foreignDiagnosticTestId;
    }

    public String getCustomUniqueId() {
        customUniqueId = foreignTableId + foreignDiagnosticTestId;
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = foreignTableId + foreignDiagnosticTestId;
    }
}
