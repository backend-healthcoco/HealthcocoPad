package com.healthcoco.healthcocoplus.viewholders;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.HealthCocoViewHolder;
import com.healthcoco.healthcocoplus.bean.server.CityResponse;
import com.healthcoco.healthcocoplus.bean.server.CollegeUniversityInstitute;
import com.healthcoco.healthcocoplus.bean.server.DiagnosticTest;
import com.healthcoco.healthcocoplus.bean.server.DrugDirection;
import com.healthcoco.healthcocoplus.bean.server.DrugDosage;
import com.healthcoco.healthcocoplus.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocoplus.bean.server.EducationQualification;
import com.healthcoco.healthcocoplus.bean.server.ForeignProfessionalMemberships;
import com.healthcoco.healthcocoplus.bean.server.MedicalCouncil;
import com.healthcoco.healthcocoplus.bean.server.ProfessionalMembership;
import com.healthcoco.healthcocoplus.bean.server.Reference;
import com.healthcoco.healthcocoplus.bean.server.Specialities;
import com.healthcoco.healthcocoplus.enums.CommonListDialogType;
import com.healthcoco.healthcocoplus.listeners.CommonListDialogItemClickListener;

import static com.healthcoco.healthcocoplus.enums.CommonListDialogType.SPECIALITY;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public class CommonListDialogViewHolder extends HealthCocoViewHolder implements View.OnClickListener {
    private CommonListDialogType popupType;
    private CommonListDialogItemClickListener commonListDialogItemClickListener;
    private HealthCocoActivity mActivity;
    private Object objData;
    private TextView tvName;
    private View contentView;

    public CommonListDialogViewHolder(HealthCocoActivity mActivity, CommonListDialogItemClickListener commonListDialogItemClickListener, CommonListDialogType popupType) {
        super(mActivity);
        this.mActivity = mActivity;
        this.commonListDialogItemClickListener = commonListDialogItemClickListener;
        this.popupType = popupType;
    }

    @Override
    public void setData(Object object) {
        this.objData = object;
    }

    @Override
    public void applyData() {
        String text = "";
        switch (popupType) {
            case SPECIALITY:
                Specialities specality = (Specialities) objData;
                text = text + specality.getSuperSpeciality();
                break;
            case REFERRED_BY:
                Reference reference = (Reference) objData;
                text = reference.getReference();
                break;
            case QUALIFICATION:
                EducationQualification qualification = (EducationQualification) objData;
                text = qualification.getName();
                break;
            case COLLEGE_UNIVERSITY_INSTITUTE:
                CollegeUniversityInstitute institute = (CollegeUniversityInstitute) objData;
                text = institute.getName();
                break;
            case MEDICAL_COUNCIL:
                MedicalCouncil medicalCouncil = (MedicalCouncil) objData;
                text = medicalCouncil.getMedicalCouncil();
                break;
            case DIAGNOSTIC_TESTS:
                if (objData instanceof DiagnosticTest) {
                    DiagnosticTest diagnosticTest = (DiagnosticTest) objData;
                    text = diagnosticTest.getTestName();
                }
                break;
            case PROFESSIONAL_MEMBERSHIP:
                ProfessionalMembership professionalMemberships = (ProfessionalMembership) objData;
                text = professionalMemberships.getMembership();
                break;
        }
        tvName.setText(text);
    }

    @Override
    public View getContentView() {
        contentView = inflater.inflate(R.layout.list_item_common_list_dialog, null);
        tvName = (TextView) contentView.findViewById(R.id.tv_name);
        contentView.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onClick(View view) {
        commonListDialogItemClickListener.onDialogItemClicked(popupType, objData);
    }
}
