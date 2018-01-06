package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.EcgDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.EchoSuggestions;
import com.healthcoco.healthcocopad.bean.server.GeneralExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.HistoryPresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.HolterSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndicationOfUsgSuggestions;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MenstrualHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.NotesSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObstetricHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.PaSuggestions;
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
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
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Prashant on 03-01-2018.
 */

public class AddNewSuggestionDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private EditText editSuggestion;
    private TextView titleTextView;
    private LoginResponse doctor;
    private AddNewSuggestionListener addNewSuggestionListener;
    private User user;
    private SuggestionType suggestionType;

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
        init();
    }

    @Override
    public void init() {
        doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
        }
    }

    @Override
    public void initViews() {
        editSuggestion = (EditText) view.findViewById(R.id.edit_suggestion);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);

        titleTextView.setText(suggestionType.getHeaderTitleId());
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
    }

    @Override
    public void initData() {

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
        if (Util.isNullOrBlank(suggestion))
            msg = getResources().getString((R.string.please_enter_suggestion));

        if (Util.isNullOrBlank(msg)) {
            addTreatment(suggestion);
        } else
            Util.showToast(mActivity, msg);
    }

    private void addTreatment(String suggestion) {
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
          /*  case ADVICE:
                AdviceSuggestion adviceSuggestion = new AdviceSuggestion();
                adviceSuggestion.setAdvice(suggestion);
                adviceSuggestion.setDoctorId(user.getUniqueId());
                adviceSuggestion.setHospitalId(user.getForeignHospitalId());
                adviceSuggestion.setLocationId(user.getForeignLocationId());
                mActivity.showLoading(false);
                WebDataServiceImpl.getInstance(mApp).addSuggestion(AdviceSuggestion.class, WebServiceType.Add_, adviceSuggestion, this, this);
                break;*/
        }
    }

    private void addInvestigationSuggestions(String suggestion) {
        InvestigationSuggestions investigationSuggestions = new InvestigationSuggestions();
        investigationSuggestions.setInvestigation(suggestion);
        investigationSuggestions.setDoctorId(user.getUniqueId());
        investigationSuggestions.setHospitalId(user.getForeignHospitalId());
        investigationSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(InvestigationSuggestions.class, WebServiceType.ADD_INVESTIGATION_SUGGESTIONS, investigationSuggestions, this, this);
    }

    private void addDiagnosisSuggestions(String suggestion) {
        DiagnosisSuggestions diagnosisSuggestions = new DiagnosisSuggestions();
        diagnosisSuggestions.setDiagnosis(suggestion);
        diagnosisSuggestions.setDoctorId(user.getUniqueId());
        diagnosisSuggestions.setHospitalId(user.getForeignHospitalId());
        diagnosisSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(DiagnosisSuggestions.class, WebServiceType.ADD_DIAGNOSIS_SUGGESTIONS, diagnosisSuggestions, this, this);
    }

    private void addPresentComplaintSuggestions(String suggestion) {
        PresentComplaintSuggestions presentComplaintSuggestions = new PresentComplaintSuggestions();
        presentComplaintSuggestions.setPresentComplaint(suggestion);
        presentComplaintSuggestions.setDoctorId(user.getUniqueId());
        presentComplaintSuggestions.setHospitalId(user.getForeignHospitalId());
        presentComplaintSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PresentComplaintSuggestions.class, WebServiceType.ADD_PRESENT_COMPLAINT_SUGGESTIONS, presentComplaintSuggestions, this, this);
    }

    private void addHistoryPresentComplaintSuggestions(String suggestion) {
        HistoryPresentComplaintSuggestions historyPresentComplaintSuggestions = new HistoryPresentComplaintSuggestions();
        historyPresentComplaintSuggestions.setPresentComplaintHistory(suggestion);
        historyPresentComplaintSuggestions.setDoctorId(user.getUniqueId());
        historyPresentComplaintSuggestions.setHospitalId(user.getForeignHospitalId());
        historyPresentComplaintSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(HistoryPresentComplaintSuggestions.class, WebServiceType.ADD_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, historyPresentComplaintSuggestions, this, this);
    }

    private void addMenstrualHistorySuggestions(String suggestion) {
        MenstrualHistorySuggestions menstrualHistorySuggestions = new MenstrualHistorySuggestions();
        menstrualHistorySuggestions.setMenstrualHistory(suggestion);
        menstrualHistorySuggestions.setDoctorId(user.getUniqueId());
        menstrualHistorySuggestions.setHospitalId(user.getForeignHospitalId());
        menstrualHistorySuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(MenstrualHistorySuggestions.class, WebServiceType.ADD_MENSTRUAL_HISTORY_SUGGESTIONS, menstrualHistorySuggestions, this, this);
    }

    private void addObstetricHistorySuggestions(String suggestion) {
        ObstetricHistorySuggestions obstetricHistorySuggestions = new ObstetricHistorySuggestions();
        obstetricHistorySuggestions.setObstetricHistory(suggestion);
        obstetricHistorySuggestions.setDoctorId(user.getUniqueId());
        obstetricHistorySuggestions.setHospitalId(user.getForeignHospitalId());
        obstetricHistorySuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(ObstetricHistorySuggestions.class, WebServiceType.ADD_OBSTETRIC_HISTORY_SUGGESTIONS, obstetricHistorySuggestions, this, this);
    }

    private void addProvisionalDiagnosisSuggestions(String suggestion) {
        ProvisionalDiagnosisSuggestions provisionalDiagnosisSuggestions = new ProvisionalDiagnosisSuggestions();
        provisionalDiagnosisSuggestions.setProvisionalDiagnosis(suggestion);
        provisionalDiagnosisSuggestions.setDoctorId(user.getUniqueId());
        provisionalDiagnosisSuggestions.setHospitalId(user.getForeignHospitalId());
        provisionalDiagnosisSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(ProvisionalDiagnosisSuggestions.class, WebServiceType.ADD_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, provisionalDiagnosisSuggestions, this, this);
    }

    private void addGeneralExaminationSuggestions(String suggestion) {
        GeneralExaminationSuggestions generalExaminationSuggestions = new GeneralExaminationSuggestions();
        generalExaminationSuggestions.setGeneralExam(suggestion);
        generalExaminationSuggestions.setDoctorId(user.getUniqueId());
        generalExaminationSuggestions.setHospitalId(user.getForeignHospitalId());
        generalExaminationSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(GeneralExaminationSuggestions.class, WebServiceType.ADD_GENERAL_EXAM_SUGGESTIONS, generalExaminationSuggestions, this, this);
    }

    private void addSystemicExaminationSuggestions(String suggestion) {
        SystemicExaminationSuggestions systemicExaminationSuggestions = new SystemicExaminationSuggestions();
        systemicExaminationSuggestions.setSystemExam(suggestion);
        systemicExaminationSuggestions.setDoctorId(user.getUniqueId());
        systemicExaminationSuggestions.setHospitalId(user.getForeignHospitalId());
        systemicExaminationSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(SystemicExaminationSuggestions.class, WebServiceType.ADD_SYSTEM_EXAM_SUGGESTIONS, systemicExaminationSuggestions, this, this);
    }

    private void addNotesSuggestions(String suggestion) {
        NotesSuggestions notesSuggestions = new NotesSuggestions();
        notesSuggestions.setNote(suggestion);
        notesSuggestions.setDoctorId(user.getUniqueId());
        notesSuggestions.setHospitalId(user.getForeignHospitalId());
        notesSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(NotesSuggestions.class, WebServiceType.ADD_NOTES_SUGGESTIONS, notesSuggestions, this, this);
    }

    private void addEcgDetailSuggestions(String suggestion) {
        EcgDetailSuggestions ecgDetailSuggestions = new EcgDetailSuggestions();
        ecgDetailSuggestions.setEcgDetails(suggestion);
        ecgDetailSuggestions.setDoctorId(user.getUniqueId());
        ecgDetailSuggestions.setHospitalId(user.getForeignHospitalId());
        ecgDetailSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(EcgDetailSuggestions.class, WebServiceType.ADD_ECG_DETAILS_SUGGESTIONS, ecgDetailSuggestions, this, this);
    }

    private void addEchoSuggestions(String suggestion) {
        EchoSuggestions echoSuggestions = new EchoSuggestions();
        echoSuggestions.setEcho(suggestion);
        echoSuggestions.setDoctorId(user.getUniqueId());
        echoSuggestions.setHospitalId(user.getForeignHospitalId());
        echoSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(EchoSuggestions.class, WebServiceType.ADD_ECHO_SUGGESTIONS, echoSuggestions, this, this);
    }

    private void addXrayDetailsSuggestion(String suggestion) {
        XrayDetailSuggestions xrayDetailSuggestions = new XrayDetailSuggestions();
        xrayDetailSuggestions.setxRayDetails(suggestion);
        xrayDetailSuggestions.setDoctorId(user.getUniqueId());
        xrayDetailSuggestions.setHospitalId(user.getForeignHospitalId());
        xrayDetailSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(XrayDetailSuggestions.class, WebServiceType.ADD_XRAY_DETAILS_SUGGESTIONS, xrayDetailSuggestions, this, this);
    }

    private void addHolterSuggestion(String suggestion) {
        HolterSuggestions holterSuggestions = new HolterSuggestions();
        holterSuggestions.setHolter(suggestion);
        holterSuggestions.setDoctorId(user.getUniqueId());
        holterSuggestions.setHospitalId(user.getForeignHospitalId());
        holterSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(HolterSuggestions.class, WebServiceType.ADD_HOLTER_SUGGESTIONS, holterSuggestions, this, this);
    }

    private void addPaSuggestion(String suggestion) {
        PaSuggestions paSuggestions = new PaSuggestions();
        paSuggestions.setPa(suggestion);
        paSuggestions.setDoctorId(user.getUniqueId());
        paSuggestions.setHospitalId(user.getForeignHospitalId());
        paSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PaSuggestions.class, WebServiceType.ADD_PA_SUGGESTIONS, paSuggestions, this, this);
    }

    private void addPsSuggestion(String suggestion) {
        PsSuggestions psSuggestions = new PsSuggestions();
        psSuggestions.setPs(suggestion);
        psSuggestions.setDoctorId(user.getUniqueId());
        psSuggestions.setHospitalId(user.getForeignHospitalId());
        psSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PsSuggestions.class, WebServiceType.ADD_PS_SUGGESTIONS, psSuggestions, this, this);
    }

    private void addPvSuggestion(String suggestion) {
        PvSuggestions pvSuggestions = new PvSuggestions();
        pvSuggestions.setPv(suggestion);
        pvSuggestions.setDoctorId(user.getUniqueId());
        pvSuggestions.setHospitalId(user.getForeignHospitalId());
        pvSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(PvSuggestions.class, WebServiceType.ADD_PV_SUGGESTIONS, pvSuggestions, this, this);
    }

    private void addIndicationUsgSuggestions(String suggestion) {
        IndicationOfUsgSuggestions indicationOfUsgSuggestions = new IndicationOfUsgSuggestions();
        indicationOfUsgSuggestions.setIndicationOfUSG(suggestion);
        indicationOfUsgSuggestions.setDoctorId(user.getUniqueId());
        indicationOfUsgSuggestions.setHospitalId(user.getForeignHospitalId());
        indicationOfUsgSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(IndicationOfUsgSuggestions.class, WebServiceType.ADD_INDICATION_OF_USG_SUGGESTIONS, indicationOfUsgSuggestions, this, this);
    }

    private void addObservationSuggestions(String suggestion) {
        ObservationSuggestions observationSuggestions = new ObservationSuggestions();
        observationSuggestions.setObservation(suggestion);
        observationSuggestions.setDoctorId(user.getUniqueId());
        observationSuggestions.setHospitalId(user.getForeignHospitalId());
        observationSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(ObservationSuggestions.class, WebServiceType.ADD_OBSERVATION_SUGGESTIONS, observationSuggestions, this, this);
    }

    private void addComplaintSuggestions(String suggestion) {
        ComplaintSuggestions complaintSuggestions = new ComplaintSuggestions();
        complaintSuggestions.setComplaint(suggestion);
        complaintSuggestions.setDoctorId(user.getUniqueId());
        complaintSuggestions.setHospitalId(user.getForeignHospitalId());
        complaintSuggestions.setLocationId(user.getForeignLocationId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addSuggestion(ComplaintSuggestions.class, WebServiceType.ADD_COMPLAINT_SUGGESTIONS, complaintSuggestions, this, this);

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
//            case ADD_ADVICE_SUGGESTIONS:
//                if (!Util.isNullOrEmptyList(response.getDataList()))
//                    LocalDataServiceImpl.getInstance(mApp).
//                            addSuggestionsList(WebServiceType.GET_SEARCH_ADVICE_SOLR, LocalTabelType.ADVICE_SUGGESTIONS,
//                                    response.getDataList(), null, null);
//                break;
        }
        mActivity.hideLoading();
        addNewSuggestionListener.onSaveClicked(null);
        dismiss();
    }


}
