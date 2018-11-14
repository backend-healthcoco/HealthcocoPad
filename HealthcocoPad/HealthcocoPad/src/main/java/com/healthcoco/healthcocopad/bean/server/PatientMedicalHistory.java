package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class PatientMedicalHistory extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(PatientMedicalHistory.class.getSimpleName());

    @Unique
    private String uniqueId;
    private Long adminCreatedTime;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
    private String patientId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String assessmentId;


}
