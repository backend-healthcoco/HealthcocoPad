package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.NameHideActivateAdapter;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.EcgDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.EchoSuggestions;
import com.healthcoco.healthcocopad.bean.server.GeneralExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.HistoryPresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.HolterSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndicationOfUsgSuggestions;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.MenstrualHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.ObstetricHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.PaSuggestions;
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProvisionalDiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.PsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PvSuggestions;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.enums.NameHideActivateType;
import com.healthcoco.healthcocopad.listeners.NameHideActivateListener;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 15/01/16.
 */
public class NameHideActivateViewholder extends HealthCocoViewHolder implements View.OnClickListener {
    private int adapterType;
    private int position;
    private NameHideActivateType nameHideActivateType;
    private NameHideActivateListener hideActivateListener;
    private HealthCocoActivity mActivity;
    private Object objData;
    private TextView tvName;
    private TextView btHide;
    private TextView btActivate;

    public NameHideActivateViewholder(HealthCocoActivity mActivity, NameHideActivateListener hideActivateListener, NameHideActivateType nameHideActivateType, int position, int adapterType) {
        super(mActivity);
        this.mActivity = mActivity;
        this.hideActivateListener = hideActivateListener;
        this.nameHideActivateType = nameHideActivateType;
        this.position = position;
        this.adapterType = adapterType;
    }

    @Override
    public void setData(Object object) {
        this.objData = object;
    }

    @Override
    public void applyData() {
        String text = "";
        String uniqueId = "";
        boolean isDiscarded = false;
        switch (nameHideActivateType) {
            case REFERENCE:
                if (objData instanceof Reference) {
                    Reference reference = (Reference) objData;
                    text = reference.getReference();
                    isDiscarded = Util.getValidatedBooleanValue(reference.getDiscarded());
                    uniqueId = reference.getUniqueId();
                }
                break;
            case DRUG:
                if (objData instanceof Drug) {
                    Drug drug = (Drug) objData;
                    if (drug.getDrugType() != null && !Util.isNullOrBlank(drug.getDrugType().getType()))
                        text = text + drug.getDrugType().getType() + " ";
                    text = text + drug.getDrugName();
                    isDiscarded = Util.getValidatedBooleanValue(drug.getDiscarded());
                    uniqueId = drug.getUniqueId();
                }
                break;
            case DIRECTION:
                if (objData instanceof DrugDirection) {
                    DrugDirection direction = (DrugDirection) objData;
                    text = direction.getDirection();
                    isDiscarded = Util.getValidatedBooleanValue(direction.getDiscarded());
                    uniqueId = direction.getUniqueId();
                }
                break;
            case FREQUENCY:
                if (objData instanceof DrugDosage) {
                    DrugDosage dosage = (DrugDosage) objData;
                    text = dosage.getDosage();
                    isDiscarded = Util.getValidatedBooleanValue(dosage.getDiscarded());
                    uniqueId = dosage.getUniqueId();
                }
                break;
            case HISTORY:
                if (objData instanceof Disease) {
                    Disease disease = (Disease) objData;
                    text = disease.getDisease();
                    isDiscarded = Util.getValidatedBooleanValue(disease.getDiscarded());
                    uniqueId = disease.getUniqueId();
                }
                break;
            case PRESENT_COMPLAINT:
                if (objData instanceof PresentComplaintSuggestions) {
                    PresentComplaintSuggestions presentComplaintSuggestions = (PresentComplaintSuggestions) objData;
                    text = presentComplaintSuggestions.getPresentComplaint();
                    isDiscarded = Util.getValidatedBooleanValue(presentComplaintSuggestions.getDiscarded());
                    uniqueId = presentComplaintSuggestions.getUniqueId();
                }
                break;
            case COMPLAINT:
                if (objData instanceof ComplaintSuggestions) {
                    ComplaintSuggestions complaintSuggestions = (ComplaintSuggestions) objData;
                    text = complaintSuggestions.getComplaint();
                    isDiscarded = Util.getValidatedBooleanValue(complaintSuggestions.getDiscarded());
                    uniqueId = complaintSuggestions.getUniqueId();
                }
                break;
            case HISTORY_OF_PRESENT_COMPLAINT:
                if (objData instanceof HistoryPresentComplaintSuggestions) {
                    HistoryPresentComplaintSuggestions historyPresentComplaintSuggestions = (HistoryPresentComplaintSuggestions) objData;
                    text = historyPresentComplaintSuggestions.getPresentComplaintHistory();
                    isDiscarded = Util.getValidatedBooleanValue(historyPresentComplaintSuggestions.getDiscarded());
                    uniqueId = historyPresentComplaintSuggestions.getUniqueId();
                }
                break;
            case MENSTRUAL_HISTORY:
                if (objData instanceof MenstrualHistorySuggestions) {
                    MenstrualHistorySuggestions menstrualHistorySuggestions = (MenstrualHistorySuggestions) objData;
                    text = menstrualHistorySuggestions.getMenstrualHistory();
                    isDiscarded = Util.getValidatedBooleanValue(menstrualHistorySuggestions.getDiscarded());
                    uniqueId = menstrualHistorySuggestions.getUniqueId();
                }
                break;
            case OBSTETRIC_HISTORY:
                if (objData instanceof ObstetricHistorySuggestions) {
                    ObstetricHistorySuggestions obstetricHistorySuggestions = (ObstetricHistorySuggestions) objData;
                    text = obstetricHistorySuggestions.getObstetricHistory();
                    isDiscarded = Util.getValidatedBooleanValue(obstetricHistorySuggestions.getDiscarded());
                    uniqueId = obstetricHistorySuggestions.getUniqueId();
                }
                break;
            case GENERAL_EXAMINATION:
                if (objData instanceof GeneralExaminationSuggestions) {
                    GeneralExaminationSuggestions generalExaminationSuggestions = (GeneralExaminationSuggestions) objData;
                    text = generalExaminationSuggestions.getGeneralExam();
                    isDiscarded = Util.getValidatedBooleanValue(generalExaminationSuggestions.getDiscarded());
                    uniqueId = generalExaminationSuggestions.getUniqueId();
                }
                break;
            case SYSTEMIC_EXAMINATION:
                if (objData instanceof SystemicExaminationSuggestions) {
                    SystemicExaminationSuggestions systemicExaminationSuggestions = (SystemicExaminationSuggestions) objData;
                    text = systemicExaminationSuggestions.getSystemExam();
                    isDiscarded = Util.getValidatedBooleanValue(systemicExaminationSuggestions.getDiscarded());
                    uniqueId = systemicExaminationSuggestions.getUniqueId();
                }
                break;
            case OBSERVATION:
                if (objData instanceof ObstetricHistorySuggestions) {
                    ObstetricHistorySuggestions obstetricHistorySuggestions = (ObstetricHistorySuggestions) objData;
                    text = obstetricHistorySuggestions.getObstetricHistory();
                    isDiscarded = Util.getValidatedBooleanValue(obstetricHistorySuggestions.getDiscarded());
                    uniqueId = obstetricHistorySuggestions.getUniqueId();
                }
                break;
            case INVESTIGATIONS:
                if (objData instanceof InvestigationSuggestions) {
                    InvestigationSuggestions investigationSuggestions = (InvestigationSuggestions) objData;
                    text = investigationSuggestions.getInvestigation();
                    isDiscarded = Util.getValidatedBooleanValue(investigationSuggestions.getDiscarded());
                    uniqueId = investigationSuggestions.getUniqueId();
                }
                break;
            case PROVISIONAL_DIAGNOSIS:
                if (objData instanceof ProvisionalDiagnosisSuggestions) {
                    ProvisionalDiagnosisSuggestions provisionalDiagnosisSuggestions = (ProvisionalDiagnosisSuggestions) objData;
                    text = provisionalDiagnosisSuggestions.getProvisionalDiagnosis();
                    isDiscarded = Util.getValidatedBooleanValue(provisionalDiagnosisSuggestions.getDiscarded());
                    uniqueId = provisionalDiagnosisSuggestions.getUniqueId();
                }
                break;
            case DIAGNOSIS:
                if (objData instanceof DiagnosisSuggestions) {
                    DiagnosisSuggestions diagnosisSuggestions = (DiagnosisSuggestions) objData;
                    text = diagnosisSuggestions.getDiagnosis();
                    isDiscarded = Util.getValidatedBooleanValue(diagnosisSuggestions.getDiscarded());
                    uniqueId = diagnosisSuggestions.getUniqueId();
                }
                break;
            case ECG:
                if (objData instanceof EcgDetailSuggestions) {
                    EcgDetailSuggestions ecgDetailSuggestions = (EcgDetailSuggestions) objData;
                    text = ecgDetailSuggestions.getEcgDetails();
                    isDiscarded = Util.getValidatedBooleanValue(ecgDetailSuggestions.getDiscarded());
                    uniqueId = ecgDetailSuggestions.getUniqueId();
                }
                break;
            case ECHO:
                if (objData instanceof EchoSuggestions) {
                    EchoSuggestions echoSuggestions = (EchoSuggestions) objData;
                    text = echoSuggestions.getEcho();
                    isDiscarded = Util.getValidatedBooleanValue(echoSuggestions.getDiscarded());
                    uniqueId = echoSuggestions.getUniqueId();
                }
                break;
            case XRAY:
                if (objData instanceof XrayDetailSuggestions) {
                    XrayDetailSuggestions xrayDetailSuggestions = (XrayDetailSuggestions) objData;
                    text = xrayDetailSuggestions.getxRayDetails();
                    isDiscarded = Util.getValidatedBooleanValue(xrayDetailSuggestions.getDiscarded());
                    uniqueId = xrayDetailSuggestions.getUniqueId();
                }
                break;
            case HOLTER:
                if (objData instanceof HolterSuggestions) {
                    HolterSuggestions holterSuggestions = (HolterSuggestions) objData;
                    text = holterSuggestions.getHolter();
                    isDiscarded = Util.getValidatedBooleanValue(holterSuggestions.getDiscarded());
                    uniqueId = holterSuggestions.getUniqueId();
                }
                break;
            case NOTES:
                if (objData instanceof ComplaintSuggestions) {
                    ComplaintSuggestions complaintSuggestions = (ComplaintSuggestions) objData;
                    text = complaintSuggestions.getComplaint();
                    isDiscarded = Util.getValidatedBooleanValue(complaintSuggestions.getDiscarded());
                    uniqueId = complaintSuggestions.getUniqueId();
                }
                break;
            case INDICATION_OF_USG:
                if (objData instanceof IndicationOfUsgSuggestions) {
                    IndicationOfUsgSuggestions indicationOfUsgSuggestions = (IndicationOfUsgSuggestions) objData;
                    text = indicationOfUsgSuggestions.getIndicationOfUSG();
                    isDiscarded = Util.getValidatedBooleanValue(indicationOfUsgSuggestions.getDiscarded());
                    uniqueId = indicationOfUsgSuggestions.getUniqueId();
                }
                break;
            case PA:
                if (objData instanceof PaSuggestions) {
                    PaSuggestions paSuggestions = (PaSuggestions) objData;
                    text = paSuggestions.getPa();
                    isDiscarded = Util.getValidatedBooleanValue(paSuggestions.getDiscarded());
                    uniqueId = paSuggestions.getUniqueId();
                }
                break;
            case PS:
                if (objData instanceof PsSuggestions) {
                    PsSuggestions psSuggestions = (PsSuggestions) objData;
                    text = psSuggestions.getPs();
                    isDiscarded = Util.getValidatedBooleanValue(psSuggestions.getDiscarded());
                    uniqueId = psSuggestions.getUniqueId();
                }
                break;
            case PV:
                if (objData instanceof PvSuggestions) {
                    PvSuggestions pvSuggestions = (PvSuggestions) objData;
                    text = pvSuggestions.getPv();
                    isDiscarded = Util.getValidatedBooleanValue(pvSuggestions.getDiscarded());
                    uniqueId = pvSuggestions.getUniqueId();
                }
                break;
        }
        setHideActivateVisibility(adapterType);
        tvName.setTag(uniqueId);
        tvName.setText(text);
    }

    private void setHideActivateVisibility(int adapterType) {
//        if (discarded != null) {
        switch (adapterType) {
            case NameHideActivateAdapter.HIDDEN_LIST_ADAPTER:
                btActivate.setVisibility(View.VISIBLE);
                btHide.setVisibility(View.GONE);
                break;
            case NameHideActivateAdapter.ACTIVATED_LIST_ADAPTER:
                btHide.setVisibility(View.VISIBLE);
                btActivate.setVisibility(View.GONE);
                break;
        }
//            if (NameHideActivateAdapter.HIDDEN_LIST_ADAPTER==) {
//
//            } else {
//
//            }
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_hide_activate, null);
        tvName = (TextView) contentView.findViewById(R.id.tv_name);
        btHide = (TextView) contentView.findViewById(R.id.bt_hide);
        btActivate = (TextView) contentView.findViewById(R.id.bt_activate);
        btHide.setOnClickListener(this);
        btActivate.setOnClickListener(this);
        btActivate.setSelected(true);
        btHide.setSelected(false);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_hide:
                hideActivateListener.onHideClicked(nameHideActivateType, objData);
                break;
            case R.id.bt_activate:
                hideActivateListener.onActivateClicked(nameHideActivateType, objData);
                break;
        }
    }
}
