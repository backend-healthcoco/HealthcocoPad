package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.UserVerification;
import com.healthcoco.healthcocopad.bean.server.AdviceSuggestion;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.EarsExamSuggestions;
import com.healthcoco.healthcocopad.bean.server.EcgDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.EchoSuggestions;
import com.healthcoco.healthcocopad.bean.server.GeneralExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.HistoryPresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.HolterSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndicationOfUsgSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndirectLarygoscopyExamSuggestions;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MenstrualHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.NeckExamSuggestions;
import com.healthcoco.healthcocopad.bean.server.NoseExamSuggestions;
import com.healthcoco.healthcocopad.bean.server.NotesSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObstetricHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.OralCavityThroatExamSuggestions;
import com.healthcoco.healthcocopad.bean.server.PaSuggestions;
import com.healthcoco.healthcocopad.bean.server.PcEarsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PcNoseSuggestions;
import com.healthcoco.healthcocopad.bean.server.PcOralCavitySuggestions;
import com.healthcoco.healthcocopad.bean.server.PcThroatSuggestions;
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProcedureNoteSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProvisionalDiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.PsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PvSuggestions;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewSuggestionListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Prashant on 03-01-2018.
 */

public class AddNewSuggestionDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    public static final String TAG_SGGESTION = "suggestionTag";
    private EditText editSuggestion;
    private TextView titleTextView;
    private TextView suggessionTypeTextView;
    private LoginResponse doctor;
    private AddNewSuggestionListener addNewSuggestionListener;
    private Bundle bundle;
    private User user;
    private String uniqueId;
    private SuggestionType suggestionType;
    private String editedSuggestion;


    public AddNewSuggestionDialogFragment(SuggestionType suggestionType) {
        this.suggestionType = suggestionType;
    }

    public AddNewSuggestionDialogFragment(AddNewSuggestionListener addNewSuggestionListener) {
        this.addNewSuggestionListener = addNewSuggestionListener;
        this.suggestionType = addNewSuggestionListener.getSuggestionType();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_new_suggestion, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bundle = getArguments();
        if (bundle != null) {
            uniqueId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
            editedSuggestion = bundle.getString(TAG_SGGESTION);
        }
        init();
    }

    @Override
    public void init() {
        doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initData();
        }
    }

    @Override
    public void initViews() {
        editSuggestion = (EditText) view.findViewById(R.id.edit_suggestion);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);
        suggessionTypeTextView = (TextView) view.findViewById(R.id.tv_suggesion_type);

        titleTextView.setText(suggestionType.getHeaderTitleId());

        if (!Util.isNullOrBlank(editedSuggestion))
            editSuggestion.setText(editedSuggestion);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this, getString(R.string.save));
    }

    @Override
    public void initData() {
        switch (suggestionType) {
            case PASSWORD:
                suggessionTypeTextView.setText(R.string.password);
                editSuggestion.setHint(R.string.please_enter_password);
                editSuggestion.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                initSaveCancelButton(this, getString(R.string.submit));
                break;
            default:
                initListeners();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
        }
    }

    private void validateData() {
        String msg = null;
        String suggestion = String.valueOf(editSuggestion.getText());
        if (Util.isNullOrBlank(suggestion)) {
            switch (suggestionType) {
                case PASSWORD:
                    msg = getResources().getString(R.string.please_enter_password);
                    break;
                default:
                    msg = getResources().getString(R.string.please_enter_suggestion);
                    break;
            }
        }
        if (Util.isNullOrBlank(msg)) {
            addSuggestion(suggestion);
        } else
            Util.showToast(mActivity, msg);
    }

    private void addSuggestion(String suggestion) {
        switch (suggestionType) {
            case COMPLAINTS:
                addComplaintSuggestions(suggestion);
                break;
            case OBSERVATION:
                addObservationSuggestions(suggestion);
                break;
            case INVESTIGATION:
                addInvestigationSuggestions(suggestion);
                break;
            case DIAGNOSIS:
                addDiagnosisSuggestions(suggestion);
                break;
            case PRESENT_COMPLAINT:
                addPresentComplaintSuggestions(suggestion);
                break;
            case HISTORY_OF_PRESENT_COMPLAINT:
                addHistoryPresentComplaintSuggestions(suggestion);
                break;
            case MENSTRUAL_HISTORY:
                addMenstrualHistorySuggestions(suggestion);
                break;
            case OBSTETRIC_HISTORY:
                addObstetricHistorySuggestions(suggestion);
                break;
            case PROVISIONAL_DIAGNOSIS:
                addProvisionalDiagnosisSuggestions(suggestion);
                break;
            case GENERAL_EXAMINATION:
                addGeneralExaminationSuggestions(suggestion);
                break;
            case SYSTEMIC_EXAMINATION:
                addSystemicExaminationSuggestions(suggestion);
                break;
            case NOTES:
                addNotesSuggestions(suggestion);
                break;
            case ECG_DETAILS:
                addEcgDetailSuggestions(suggestion);
                break;
            case ECHO:
                addEchoSuggestions(suggestion);
                break;
            case X_RAY_DETAILS:
                addXrayDetailsSuggestion(suggestion);
                break;
            case HOLTER:
                addHolterSuggestion(suggestion);
                break;
            case PA:
                addPaSuggestion(suggestion);
                break;
            case PS:
                addPsSuggestion(suggestion);
                break;
            case PV:
                addPvSuggestion(suggestion);
                break;
            case INDICATION_OF_USG:
                addIndicationUsgSuggestions(suggestion);
                break;
            case ADVICE:
                addAdviceSuggestion(suggestion);
                break;
            case PC_EARS:
                addPcEarsSuggestion(suggestion);
                break;
            case PC_NOSE:
                addPcNoseSuggestion(suggestion);
                break;
            case PC_ORAL_CAVITY:
                addPcOralCavitySuggestion(suggestion);
                break;
            case PC_THROAT:
                addPcThroatSuggestion(suggestion);
                break;
            case EARS_EXAM:
                addEarsExamSuggestion(suggestion);
                break;
            case NOSE_EXAM:
                addNoseExamSuggestion(suggestion);
                break;
            case ORAL_CAVITY_THROAT_EXAM:
                addOralCavityThroatExamSuggestion(suggestion);
                break;
            case INDIRECT_LARYGOSCOPY_EXAM:
                addIndirectLarygoscopyExamSuggestion(suggestion);
                break;
            case NECK_EXAM:
                addNeckExamSuggestion(suggestion);
                break;
            case PROCEDURES:
                addProcedureSuggestion(suggestion);
                break;
            case PASSWORD:
                checkIsLocationAdmin(doctor.getUser().getEmailAddress(), suggestion, doctor.getUser().getForeignLocationId());
                break;
        }
    }

    private void addPcEarsSuggestion(String suggestion) {
        PcEarsSuggestions pcEarsSuggestions = new PcEarsSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            pcEarsSuggestions.setUniqueId(uniqueId);
        pcEarsSuggestions.setPcEars(suggestion);
        pcEarsSuggestions.setDoctorId(user.getUniqueId());
        pcEarsSuggestions.setHospitalId(user.getForeignHospitalId());
        pcEarsSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PcEarsSuggestions.class, WebServiceType.ADD_PC_EARS_SUGGESTIONS, pcEarsSuggestions, this, this);
    }

    private void addPcNoseSuggestion(String suggestion) {
        PcNoseSuggestions pcNoseSuggestions = new PcNoseSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            pcNoseSuggestions.setUniqueId(uniqueId);
        pcNoseSuggestions.setPcNose(suggestion);
        pcNoseSuggestions.setDoctorId(user.getUniqueId());
        pcNoseSuggestions.setHospitalId(user.getForeignHospitalId());
        pcNoseSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PcNoseSuggestions.class, WebServiceType.ADD_PC_NOSE_SUGGESTIONS, pcNoseSuggestions, this, this);
    }

    private void addPcOralCavitySuggestion(String suggestion) {
        PcOralCavitySuggestions pcOralCavitySuggestions = new PcOralCavitySuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            pcOralCavitySuggestions.setUniqueId(uniqueId);
        pcOralCavitySuggestions.setPcOralCavity(suggestion);
        pcOralCavitySuggestions.setDoctorId(user.getUniqueId());
        pcOralCavitySuggestions.setHospitalId(user.getForeignHospitalId());
        pcOralCavitySuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PcOralCavitySuggestions.class, WebServiceType.ADD_PC_ORAL_CAVITY_SUGGESTIONS, pcOralCavitySuggestions, this, this);
    }

    private void addPcThroatSuggestion(String suggestion) {
        PcThroatSuggestions pcThroatSuggestions = new PcThroatSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            pcThroatSuggestions.setUniqueId(uniqueId);
        pcThroatSuggestions.setPcThroat(suggestion);
        pcThroatSuggestions.setDoctorId(user.getUniqueId());
        pcThroatSuggestions.setHospitalId(user.getForeignHospitalId());
        pcThroatSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PcThroatSuggestions.class, WebServiceType.ADD_PC_THROAT_SUGGESTIONS, pcThroatSuggestions, this, this);
    }

    private void addEarsExamSuggestion(String suggestion) {
        EarsExamSuggestions earsExamSuggestions = new EarsExamSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            earsExamSuggestions.setUniqueId(uniqueId);
        earsExamSuggestions.setEarsExam(suggestion);
        earsExamSuggestions.setDoctorId(user.getUniqueId());
        earsExamSuggestions.setHospitalId(user.getForeignHospitalId());
        earsExamSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(EarsExamSuggestions.class, WebServiceType.ADD_EARS_EXAM_SUGGESTIONS, earsExamSuggestions, this, this);
    }

    private void addNoseExamSuggestion(String suggestion) {
        NoseExamSuggestions noseExamSuggestions = new NoseExamSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            noseExamSuggestions.setUniqueId(uniqueId);
        noseExamSuggestions.setNoseExam(suggestion);
        noseExamSuggestions.setDoctorId(user.getUniqueId());
        noseExamSuggestions.setHospitalId(user.getForeignHospitalId());
        noseExamSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(NoseExamSuggestions.class, WebServiceType.ADD_NOSE_EXAM_SUGGESTIONS, noseExamSuggestions, this, this);
    }

    private void addOralCavityThroatExamSuggestion(String suggestion) {
        OralCavityThroatExamSuggestions oralCavityThroatExamSuggestions = new OralCavityThroatExamSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            oralCavityThroatExamSuggestions.setUniqueId(uniqueId);
        oralCavityThroatExamSuggestions.setOralCavityThroatExam(suggestion);
        oralCavityThroatExamSuggestions.setDoctorId(user.getUniqueId());
        oralCavityThroatExamSuggestions.setHospitalId(user.getForeignHospitalId());
        oralCavityThroatExamSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(OralCavityThroatExamSuggestions.class, WebServiceType.ADD_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS, oralCavityThroatExamSuggestions, this, this);
    }

    private void addIndirectLarygoscopyExamSuggestion(String suggestion) {
        IndirectLarygoscopyExamSuggestions indirectLarygoscopyExamSuggestions = new IndirectLarygoscopyExamSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            indirectLarygoscopyExamSuggestions.setUniqueId(uniqueId);
        indirectLarygoscopyExamSuggestions.setIndirectLarygoscopyExam(suggestion);
        indirectLarygoscopyExamSuggestions.setDoctorId(user.getUniqueId());
        indirectLarygoscopyExamSuggestions.setHospitalId(user.getForeignHospitalId());
        indirectLarygoscopyExamSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(IndirectLarygoscopyExamSuggestions.class, WebServiceType.ADD_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS, indirectLarygoscopyExamSuggestions, this, this);
    }

    private void addNeckExamSuggestion(String suggestion) {
        NeckExamSuggestions neckExamSuggestions = new NeckExamSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            neckExamSuggestions.setUniqueId(uniqueId);
        neckExamSuggestions.setNeckExam(suggestion);
        neckExamSuggestions.setDoctorId(user.getUniqueId());
        neckExamSuggestions.setHospitalId(user.getForeignHospitalId());
        neckExamSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(NeckExamSuggestions.class, WebServiceType.ADD_NECK_EXAM_SUGGESTIONS, neckExamSuggestions, this, this);
    }

    private void addProcedureSuggestion(String suggestion) {
        ProcedureNoteSuggestions procedureNoteSuggestions = new ProcedureNoteSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            procedureNoteSuggestions.setUniqueId(uniqueId);
        procedureNoteSuggestions.setProcedureNote(suggestion);
        procedureNoteSuggestions.setDoctorId(user.getUniqueId());
        procedureNoteSuggestions.setHospitalId(user.getForeignHospitalId());
        procedureNoteSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(ProcedureNoteSuggestions.class, WebServiceType.ADD_PROCEDURE_NOTE_SUGGESTIONS, procedureNoteSuggestions, this, this);
    }

    private void addAdviceSuggestion(String suggestion) {
        AdviceSuggestion adviceSuggestion = new AdviceSuggestion();
        if (!Util.isNullOrBlank(uniqueId))
            adviceSuggestion.setUniqueId(uniqueId);
        adviceSuggestion.setAdvice(suggestion);
        adviceSuggestion.setDoctorId(user.getUniqueId());
        adviceSuggestion.setHospitalId(user.getForeignHospitalId());
        adviceSuggestion.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(AdviceSuggestion.class, WebServiceType.ADD_ADVICE_SUGGESTIONS, adviceSuggestion, this, this);
    }

    private void addInvestigationSuggestions(String suggestion) {
        InvestigationSuggestions investigationSuggestions = new InvestigationSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            investigationSuggestions.setUniqueId(uniqueId);
        investigationSuggestions.setInvestigation(suggestion);
        investigationSuggestions.setDoctorId(user.getUniqueId());
        investigationSuggestions.setHospitalId(user.getForeignHospitalId());
        investigationSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(InvestigationSuggestions.class, WebServiceType.ADD_INVESTIGATION_SUGGESTIONS, investigationSuggestions, this, this);
    }

    private void addDiagnosisSuggestions(String suggestion) {
        DiagnosisSuggestions diagnosisSuggestions = new DiagnosisSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            diagnosisSuggestions.setUniqueId(uniqueId);
        diagnosisSuggestions.setDiagnosis(suggestion);
        diagnosisSuggestions.setDoctorId(user.getUniqueId());
        diagnosisSuggestions.setHospitalId(user.getForeignHospitalId());
        diagnosisSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(DiagnosisSuggestions.class, WebServiceType.ADD_DIAGNOSIS_SUGGESTIONS, diagnosisSuggestions, this, this);
    }

    private void addPresentComplaintSuggestions(String suggestion) {
        PresentComplaintSuggestions presentComplaintSuggestions = new PresentComplaintSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            presentComplaintSuggestions.setUniqueId(uniqueId);
        presentComplaintSuggestions.setPresentComplaint(suggestion);
        presentComplaintSuggestions.setDoctorId(user.getUniqueId());
        presentComplaintSuggestions.setHospitalId(user.getForeignHospitalId());
        presentComplaintSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PresentComplaintSuggestions.class, WebServiceType.ADD_PRESENT_COMPLAINT_SUGGESTIONS, presentComplaintSuggestions, this, this);
    }

    private void addHistoryPresentComplaintSuggestions(String suggestion) {
        HistoryPresentComplaintSuggestions historyPresentComplaintSuggestions = new HistoryPresentComplaintSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            historyPresentComplaintSuggestions.setUniqueId(uniqueId);
        historyPresentComplaintSuggestions.setPresentComplaintHistory(suggestion);
        historyPresentComplaintSuggestions.setDoctorId(user.getUniqueId());
        historyPresentComplaintSuggestions.setHospitalId(user.getForeignHospitalId());
        historyPresentComplaintSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(HistoryPresentComplaintSuggestions.class, WebServiceType.ADD_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, historyPresentComplaintSuggestions, this, this);
    }

    private void addMenstrualHistorySuggestions(String suggestion) {
        MenstrualHistorySuggestions menstrualHistorySuggestions = new MenstrualHistorySuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            menstrualHistorySuggestions.setUniqueId(uniqueId);
        menstrualHistorySuggestions.setMenstrualHistory(suggestion);
        menstrualHistorySuggestions.setDoctorId(user.getUniqueId());
        menstrualHistorySuggestions.setHospitalId(user.getForeignHospitalId());
        menstrualHistorySuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(MenstrualHistorySuggestions.class, WebServiceType.ADD_MENSTRUAL_HISTORY_SUGGESTIONS, menstrualHistorySuggestions, this, this);
    }

    private void addObstetricHistorySuggestions(String suggestion) {
        ObstetricHistorySuggestions obstetricHistorySuggestions = new ObstetricHistorySuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            obstetricHistorySuggestions.setUniqueId(uniqueId);
        obstetricHistorySuggestions.setObstetricHistory(suggestion);
        obstetricHistorySuggestions.setDoctorId(user.getUniqueId());
        obstetricHistorySuggestions.setHospitalId(user.getForeignHospitalId());
        obstetricHistorySuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(ObstetricHistorySuggestions.class, WebServiceType.ADD_OBSTETRIC_HISTORY_SUGGESTIONS, obstetricHistorySuggestions, this, this);
    }

    private void addProvisionalDiagnosisSuggestions(String suggestion) {
        ProvisionalDiagnosisSuggestions provisionalDiagnosisSuggestions = new ProvisionalDiagnosisSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            provisionalDiagnosisSuggestions.setUniqueId(uniqueId);
        provisionalDiagnosisSuggestions.setProvisionalDiagnosis(suggestion);
        provisionalDiagnosisSuggestions.setDoctorId(user.getUniqueId());
        provisionalDiagnosisSuggestions.setHospitalId(user.getForeignHospitalId());
        provisionalDiagnosisSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(ProvisionalDiagnosisSuggestions.class, WebServiceType.ADD_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, provisionalDiagnosisSuggestions, this, this);
    }

    private void addGeneralExaminationSuggestions(String suggestion) {
        GeneralExaminationSuggestions generalExaminationSuggestions = new GeneralExaminationSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            generalExaminationSuggestions.setUniqueId(uniqueId);
        generalExaminationSuggestions.setGeneralExam(suggestion);
        generalExaminationSuggestions.setDoctorId(user.getUniqueId());
        generalExaminationSuggestions.setHospitalId(user.getForeignHospitalId());
        generalExaminationSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(GeneralExaminationSuggestions.class, WebServiceType.ADD_GENERAL_EXAM_SUGGESTIONS, generalExaminationSuggestions, this, this);
    }

    private void addSystemicExaminationSuggestions(String suggestion) {
        SystemicExaminationSuggestions systemicExaminationSuggestions = new SystemicExaminationSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            systemicExaminationSuggestions.setUniqueId(uniqueId);
        systemicExaminationSuggestions.setSystemExam(suggestion);
        systemicExaminationSuggestions.setDoctorId(user.getUniqueId());
        systemicExaminationSuggestions.setHospitalId(user.getForeignHospitalId());
        systemicExaminationSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(SystemicExaminationSuggestions.class, WebServiceType.ADD_SYSTEM_EXAM_SUGGESTIONS, systemicExaminationSuggestions, this, this);
    }

    private void addNotesSuggestions(String suggestion) {
        NotesSuggestions notesSuggestions = new NotesSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            notesSuggestions.setUniqueId(uniqueId);
        notesSuggestions.setNote(suggestion);
        notesSuggestions.setDoctorId(user.getUniqueId());
        notesSuggestions.setHospitalId(user.getForeignHospitalId());
        notesSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(NotesSuggestions.class, WebServiceType.ADD_NOTES_SUGGESTIONS, notesSuggestions, this, this);
    }

    private void addEcgDetailSuggestions(String suggestion) {
        EcgDetailSuggestions ecgDetailSuggestions = new EcgDetailSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            ecgDetailSuggestions.setUniqueId(uniqueId);
        ecgDetailSuggestions.setEcgDetails(suggestion);
        ecgDetailSuggestions.setDoctorId(user.getUniqueId());
        ecgDetailSuggestions.setHospitalId(user.getForeignHospitalId());
        ecgDetailSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(EcgDetailSuggestions.class, WebServiceType.ADD_ECG_DETAILS_SUGGESTIONS, ecgDetailSuggestions, this, this);
    }

    private void addEchoSuggestions(String suggestion) {
        EchoSuggestions echoSuggestions = new EchoSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            echoSuggestions.setUniqueId(uniqueId);
        echoSuggestions.setEcho(suggestion);
        echoSuggestions.setDoctorId(user.getUniqueId());
        echoSuggestions.setHospitalId(user.getForeignHospitalId());
        echoSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(EchoSuggestions.class, WebServiceType.ADD_ECHO_SUGGESTIONS, echoSuggestions, this, this);
    }

    private void addXrayDetailsSuggestion(String suggestion) {
        XrayDetailSuggestions xrayDetailSuggestions = new XrayDetailSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            xrayDetailSuggestions.setUniqueId(uniqueId);
        xrayDetailSuggestions.setxRayDetails(suggestion);
        xrayDetailSuggestions.setDoctorId(user.getUniqueId());
        xrayDetailSuggestions.setHospitalId(user.getForeignHospitalId());
        xrayDetailSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(XrayDetailSuggestions.class, WebServiceType.ADD_XRAY_DETAILS_SUGGESTIONS, xrayDetailSuggestions, this, this);
    }

    private void addHolterSuggestion(String suggestion) {
        HolterSuggestions holterSuggestions = new HolterSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            holterSuggestions.setUniqueId(uniqueId);
        holterSuggestions.setHolter(suggestion);
        holterSuggestions.setDoctorId(user.getUniqueId());
        holterSuggestions.setHospitalId(user.getForeignHospitalId());
        holterSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(HolterSuggestions.class, WebServiceType.ADD_HOLTER_SUGGESTIONS, holterSuggestions, this, this);
    }

    private void addPaSuggestion(String suggestion) {
        PaSuggestions paSuggestions = new PaSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            paSuggestions.setUniqueId(uniqueId);
        paSuggestions.setPa(suggestion);
        paSuggestions.setDoctorId(user.getUniqueId());
        paSuggestions.setHospitalId(user.getForeignHospitalId());
        paSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PaSuggestions.class, WebServiceType.ADD_PA_SUGGESTIONS, paSuggestions, this, this);
    }

    private void addPsSuggestion(String suggestion) {
        PsSuggestions psSuggestions = new PsSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            psSuggestions.setUniqueId(uniqueId);
        psSuggestions.setPs(suggestion);
        psSuggestions.setDoctorId(user.getUniqueId());
        psSuggestions.setHospitalId(user.getForeignHospitalId());
        psSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PsSuggestions.class, WebServiceType.ADD_PS_SUGGESTIONS, psSuggestions, this, this);
    }

    private void addPvSuggestion(String suggestion) {
        PvSuggestions pvSuggestions = new PvSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            pvSuggestions.setUniqueId(uniqueId);
        pvSuggestions.setPv(suggestion);
        pvSuggestions.setDoctorId(user.getUniqueId());
        pvSuggestions.setHospitalId(user.getForeignHospitalId());
        pvSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PvSuggestions.class, WebServiceType.ADD_PV_SUGGESTIONS, pvSuggestions, this, this);
    }

    private void addIndicationUsgSuggestions(String suggestion) {
        IndicationOfUsgSuggestions indicationOfUsgSuggestions = new IndicationOfUsgSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            indicationOfUsgSuggestions.setUniqueId(uniqueId);
        indicationOfUsgSuggestions.setIndicationOfUSG(suggestion);
        indicationOfUsgSuggestions.setDoctorId(user.getUniqueId());
        indicationOfUsgSuggestions.setHospitalId(user.getForeignHospitalId());
        indicationOfUsgSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(IndicationOfUsgSuggestions.class, WebServiceType.ADD_INDICATION_OF_USG_SUGGESTIONS, indicationOfUsgSuggestions, this, this);
    }

    private void addObservationSuggestions(String suggestion) {
        ObservationSuggestions observationSuggestions = new ObservationSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            observationSuggestions.setUniqueId(uniqueId);
        observationSuggestions.setObservation(suggestion);
        observationSuggestions.setDoctorId(user.getUniqueId());
        observationSuggestions.setHospitalId(user.getForeignHospitalId());
        observationSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(ObservationSuggestions.class, WebServiceType.ADD_OBSERVATION_SUGGESTIONS, observationSuggestions, this, this);
    }

    private void addComplaintSuggestions(String suggestion) {
        ComplaintSuggestions complaintSuggestions = new ComplaintSuggestions();
        if (!Util.isNullOrBlank(uniqueId))
            complaintSuggestions.setUniqueId(uniqueId);
        complaintSuggestions.setComplaint(suggestion);
        complaintSuggestions.setDoctorId(user.getUniqueId());
        complaintSuggestions.setHospitalId(user.getForeignHospitalId());
        complaintSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(ComplaintSuggestions.class, WebServiceType.ADD_COMPLAINT_SUGGESTIONS, complaintSuggestions, this, this);

    }

    private void checkIsLocationAdmin(String userName, String password, String locationId) {
        mActivity.showLoading(false);
        UserVerification userVerification = new UserVerification();
        userVerification.setUsername(userName);
        userVerification.setPassword(Util.getSHA3SecurePassword(password));
        userVerification.setLocationId(locationId);
        WebDataServiceImpl.getInstance(mApp).isLocationAdmin(UserVerification.class, userVerification, this, this);
    }


    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        Util.showToast(mActivity, errorMsg);
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case ADD_PRESENT_COMPLAINT_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_PRESENT_COMPLAINT_SUGGESTIONS, LocalTabelType.PRESENT_COMPLAINT_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_COMPLAINT_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_COMPLAINT_SUGGESTIONS, LocalTabelType.COMPLAINT_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, LocalTabelType.HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_MENSTRUAL_HISTORY_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_MENSTRUAL_HISTORY_SUGGESTIONS, LocalTabelType.MENSTRUAL_HISTORY_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_OBSTETRIC_HISTORY_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_OBSTETRIC_HISTORY_SUGGESTIONS, LocalTabelType.OBSTETRIC_HISTORY_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_GENERAL_EXAM_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_GENERAL_EXAMINATION_SUGGESTIONS, LocalTabelType.GENERAL_EXAMINATION_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_SYSTEM_EXAM_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_SYSTEMIC_EXAMINATION_SUGGESTIONS, LocalTabelType.SYSTEMIC_EXAMINATION_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_OBSERVATION_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_OBSERVATION_SUGGESTIONS, LocalTabelType.OBSERVATION_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_INVESTIGATION_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_INVESTIGATION_SUGGESTIONS, LocalTabelType.INVESTIGATION_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_PROVISIONAL_DIAGNOSIS_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, LocalTabelType.PROVISIONAL_DIAGNOSIS_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_DIAGNOSIS_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_DIAGNOSIS_SUGGESTIONS, LocalTabelType.DIAGNOSIS_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_ECG_DETAILS_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_ECG_SUGGESTIONS, LocalTabelType.ECG_DETAILS_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_ECHO_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_ECHO_SUGGESTIONS, LocalTabelType.ECHO_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_XRAY_DETAILS_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_XRAY_SUGGESTIONS, LocalTabelType.X_RAY_DETAILS_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_HOLTER_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_HOLTER_SUGGESTIONS, LocalTabelType.HOLTER_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_PA_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_PA_SUGGESTIONS, LocalTabelType.PA_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_PV_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_PV_SUGGESTIONS, LocalTabelType.PV_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_PS_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_PS_SUGGESTIONS, LocalTabelType.PS_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_INDICATION_OF_USG_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_INDICATION_OF_USG_SUGGESTIONS, LocalTabelType.INDICATION_OF_USG_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_NOTES_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_NOTES_SUGGESTIONS, LocalTabelType.NOTES_SUGGESTIONS,
                                    response.getData(), null, null);
                break;

            case ADD_PC_EARS_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_PC_EARS_SUGGESTIONS, LocalTabelType.PC_EARS_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_PC_NOSE_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_PC_NOSE_SUGGESTIONS, LocalTabelType.PC_NOSE_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_PC_ORAL_CAVITY_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_PC_ORAL_CAVITY_SUGGESTIONS, LocalTabelType.PC_ORAL_CAVITY_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_PC_THROAT_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_PC_THROAT_SUGGESTIONS, LocalTabelType.PC_THROAT_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_EARS_EXAM_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_EAR_EXAM_SUGGESTIONS, LocalTabelType.EAR_EXAM_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_NOSE_EXAM_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_NOSE_EXAM_SUGGESTIONS, LocalTabelType.NOSE_EXAM_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS, LocalTabelType.ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS, LocalTabelType.INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_NECK_EXAM_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_NECK_EXAM_SUGGESTIONS, LocalTabelType.NECK_EXAM_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_PROCEDURE_NOTE_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_PROCEDURE_NOTE_SUGGESTIONS, LocalTabelType.PROCEDURE_NOTE_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case ADD_ADVICE_SUGGESTIONS:
                if (response.isValidData(response))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestions(WebServiceType.GET_SEARCH_ADVICE_SOLR, LocalTabelType.ADVICE_SUGGESTIONS,
                                    response.getData(), null, null);
                break;
            case IS_LOCATION_ADMIN:
                if (response.isValidData(response)) {
                    boolean isAdmin = (boolean) response.getData();
                    mActivity.hideLoading();
                    if (isAdmin) {
                        getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, HealthCocoConstants.RESULT_CODE_REFERENCE_LIST, null);
                        dismiss();
                    } else {
                        return;
                    }
                }
                break;
        }
        mActivity.hideLoading();
       /* addNewSuggestionListener.onSaveClicked(null);
        dismiss();
*/
        if (addNewSuggestionListener != null) {
            addNewSuggestionListener.onSaveClicked(null);
        } else {
            getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, HealthCocoConstants.RESULT_CODE_REFERENCE_LIST, null);
        }
        dismiss();

    }


}
