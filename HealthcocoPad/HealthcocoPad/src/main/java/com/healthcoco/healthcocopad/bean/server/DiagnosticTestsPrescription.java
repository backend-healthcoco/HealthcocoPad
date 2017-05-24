package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.io.Serializable;

/**
 * Created by neha on 22/04/17.
 */

public class DiagnosticTestsPrescription extends SugarRecord implements Serializable {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DiagnosticTestsPrescription.class.getSimpleName());
    @Unique
    protected String diagnosticTestId;
    @Ignore
    private DiagnosticTest test;
    private String diagnosticTestJsonString;
    private String recordId;

    private String foreignDiagnosticTestId;

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

    public String getDiagnosticTestId() {
        return diagnosticTestId;
    }

    public void setDiagnosticTestId(String diagnosticTestId) {
        this.diagnosticTestId = diagnosticTestId;
    }

    public String getDiagnosticTestJsonString() {
        return diagnosticTestJsonString;
    }

    public void setDiagnosticTestJsonString(String diagnosticTestJsonString) {
        this.diagnosticTestJsonString = diagnosticTestJsonString;
    }

    public String getForeignDiagnosticTestId() {
        return foreignDiagnosticTestId;
    }

    public void setForeignDiagnosticTestId(String foreignDiagnosticTestId) {
        this.foreignDiagnosticTestId = foreignDiagnosticTestId;
    }
}
