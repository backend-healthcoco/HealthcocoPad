package com.healthcoco.healthcocopad.fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.SyncAllAdapter;
import com.healthcoco.healthcocopad.bean.UiPermissionsBoth;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AdviceSuggestion;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotesDynamicField;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DataPermissions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.DrugType;
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
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.SyncAll;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.SyncAllType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SyncAllItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neha on 08/02/16.
 */
public class SyncFragment extends HealthCocoFragment implements View.OnClickListener, SyncAllItemListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, DownloadFileFromUrlListener, AdapterView.OnItemClickListener {
    public static final String DATE_FORMAT = "dd/MM/yyyy hh:mm  aaa";
    public static final int MAX_NUMBER_OF_EVENTS = 50;
    public long MAX_COUNT;
    Long latestUpdatedTimeContact = 0l;
    private TextView tvDoctorName;
    private LinearLayout btSyncAll;
    private TextView tvRefresh;
    private HashMap<SyncAllType, SyncAll> syncAllHashmap = new HashMap<>();
    private ArrayList<SyncAllType> syncAllTypeList = new ArrayList<>();
    private Animation animation;
    private User user;
    private ImageView ivImage;
    private ProgressBar progressLoading;
    private DoctorProfile doctorProfile;
    private TextView tvInitialAlphabet;
    private ListView lvSyncAll;
    private boolean isEndOfListAchieved = true;
    private int PAGE_NUMBER = 0;
    private SyncAllAdapter adapter;
    private boolean isSyncAllClicked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sync_all, container, false);
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
        //init animation
        animation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_inifinite);
        initViews();
        initListeners();
        initAdapters();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        ivImage = (ImageView) view.findViewById(R.id.iv_image);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        btSyncAll = (LinearLayout) view.findViewById(R.id.bt_sync_all);
        tvRefresh = (TextView) view.findViewById(R.id.tv_refresh);
        lvSyncAll = (ListView) view.findViewById(R.id.lv_sync_all);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
    }

    @Override
    public void initListeners() {
        btSyncAll.setOnClickListener(this);
        lvSyncAll.setOnItemClickListener(this);
    }

    private void initAdapters() {
        adapter = new SyncAllAdapter(mActivity, this);
        lvSyncAll.setAdapter(adapter);
    }

    private void initDefaultData() {
        List<SyncAll> syncAllList = LocalDataServiceImpl.getInstance(mApp).getSyncAllData();
        if (Util.isNullOrEmptyList(syncAllList) || syncAllList.size() < SyncAllType.values().length) {
            syncAllList = new ArrayList<>();
            for (SyncAllType syncAllType :
                    SyncAllType.values()) {
                SyncAll syncAll = LocalDataServiceImpl.getInstance(mApp).getSynAll(syncAllType);
                if (syncAllType.getIsIncludedAsItem()) {
                    if (syncAll == null) {
                        syncAll = new SyncAll();
                        syncAll.setSyncAllType(syncAllType);
                        syncAll.setPosition(syncAllType.getPosition());
                    }
                    syncAllList.add(syncAll);
                }
            }
            LocalDataServiceImpl.getInstance(mApp).addSyncAllData(syncAllList);
        }

        for (SyncAll syncAll : syncAllList) {
            syncAllHashmap.put(syncAll.getSyncAllType(), syncAll);
        }
    }

    private void initData() {
        if (doctorProfile != null) {
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_EMR_HEADER, doctorProfile, progressLoading, ivImage, tvInitialAlphabet);
            String title = doctorProfile.getTitle(false);
            if (Util.isNullOrBlank(title))
                title = getResources().getString(R.string.dr);
            tvDoctorName.setText(title + Util.getValidatedValue(doctorProfile.getFirstName()));
        }
        notifyAdapter(new ArrayList<SyncAll>(syncAllHashmap.values()));
    }

    private void notifyAdapter(ArrayList<SyncAll> syncAllList) {
        Collections.sort(syncAllList, ComparatorUtil.syncListComparator);
        adapter.setListData(syncAllList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sync_all:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    syncAll();
                else
                    Util.showToast(mActivity, R.string.user_offline);
                break;
        }
    }

    private void syncAll() {
        isSyncAllClicked = true;
        new Thread(new Runnable() {
            public void run() {
                tvRefresh.post(new Runnable() {
                    public void run() {
                        tvRefresh.startAnimation(animation);
                    }
                });
            }
        }).start();

        for (SyncAllType syncAllType : syncAllHashmap.keySet()) {
            SyncAll syncAll = syncAllHashmap.get(syncAllType);
            syncAll.setIsLoading(true);
            syncAllTypeList.add(syncAllType);
//            startSyning(syncAllType);
        }
        notifyAdapter(new ArrayList<SyncAll>(syncAllHashmap.values()));
        startSyning(SyncAllType.CONTACT);
//        adapter.setSyncAll(true);
//        notifyAdapter(syncAllList);
    }

    private void syncNext(SyncAllType syncAllType) {
        SyncAllType nextSyncType = (syncAllType.getNextSyncType(syncAllType));
        if (nextSyncType != null)
            startSyning(nextSyncType);
    }

    private void startSyning(SyncAllType syncAllType) {
        if (isEndOfListAchieved) {
            startAnimation(syncAllType);
            ClinicalNotesDynamicField clinicalNotesDynamicField = LocalDataServiceImpl.getInstance(mApp).getClinicalNotesDynamicField();
            switch (syncAllType) {
                case CONTACT:

                    syncContact();

                    break;
                case DATA_PERMISSIONS:
                    //getting contacts data
                    WebDataServiceImpl.getInstance(mApp).getDataPermission(DataPermissions.class, user.getUniqueId(), this, this);

                    break;
                case GROUP:
                    //getting GroupsList
                    Long latestUpdatedTimeGroup = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.USER_GROUP);
                    WebDataServiceImpl.getInstance(mApp).getGroupsList(WebServiceType.GET_GROUPS, UserGroups.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), latestUpdatedTimeGroup, null, this, this);
                    break;
                case HISTORY:
                    Long latestUpdatedTimeHistory = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DISEASE);
                    WebDataServiceImpl.getInstance(mApp).getDiseaseList(Disease.class, user.getUniqueId(), latestUpdatedTimeHistory, null, this, this);

                    break;
                case DRUG_CUSTOM:
                    Long latestUpdatedTimeDrug = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.TEMP_DRUG);
                    WebDataServiceImpl.getInstance(mApp).getDrugsList(WebServiceType.GET_DRUGS_LIST_CUSTOM, Drug.class, user.getUniqueId(), latestUpdatedTimeDrug, true, this, this);
                    break;
                case FREQUENCY:
                    //getting dosages/frequency
                    Long latestUpdatedTimeDosage = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DOSAGE);
                    WebDataServiceImpl.getInstance(mApp)
                            .getDosageDirection(WebServiceType.GET_DRUG_DOSAGE, DrugDosage.class, true, this.user.getUniqueId(), latestUpdatedTimeDosage, this, this);
                    break;
                case DIRECTION:
                    //getting directions
                    Long latestUpdatedTimeDirection = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DIRECTION);
                    WebDataServiceImpl.getInstance(mApp)
                            .getDosageDirection(WebServiceType.GET_DIRECTION, DrugDirection.class, true, this.user.getUniqueId(), latestUpdatedTimeDirection, this, this);
                    break;
                case NOTES_DIAGRAM:
                    //getting diagrams list
                    Long latestUpdatedTimeNotes = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DIAGRAMS);
                    WebDataServiceImpl.getInstance(mApp).getDiagramsList(Diagram.class, latestUpdatedTimeNotes, user.getUniqueId(), this, this);
                    break;
                case CLINICAL_NOTES_DATA:
                case CLINICAL_NOTE_COMPLAINT_SUGGESTIONS:
                    Long latestUpdatedTimeComplaint = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.COMPLAINT);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ComplaintSuggestions.class, WebServiceType.GET_COMPLAINT_SUGGESTIONS, clinicalNotesDynamicField.getComplaint(), user.getUniqueId(),
                            latestUpdatedTimeComplaint, this, this);
                    break;
                case CLINICAL_NOTE_OBSERVATION_SUGGESTIONS:
                    Long latestUpdatedTimeObservation = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.OBSERVATION);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ObservationSuggestions.class, WebServiceType.GET_OBSERVATION_SUGGESTIONS, clinicalNotesDynamicField.getObservation(), user.getUniqueId(),
                            latestUpdatedTimeObservation, this, this);
                    break;
                case CLINICAL_NOTE_INVESTIGATION_SUGGESTIONS:
                    Long latestUpdatedTimeInvestigation = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.INVESTIGATION);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(InvestigationSuggestions.class, WebServiceType.GET_INVESTIGATION_SUGGESTIONS, clinicalNotesDynamicField.getInvestigation(), user.getUniqueId(),
                            latestUpdatedTimeInvestigation, this, this);
                    break;
                case CLINICAL_NOTE_OBSTETRIC_HISTORY_SUGGESTIONS:
                    Long latestUpdatedTimeObstetricHistory = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.OBSTETRIC_HISTORY_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ObstetricHistorySuggestions.class, WebServiceType.GET_OBSTETRIC_HISTORY_SUGGESTIONS, clinicalNotesDynamicField.getObstetricHistory(), user.getUniqueId(),
                            latestUpdatedTimeObstetricHistory, this, this);
                    break;
                case CLINICAL_NOTE_GENERAL_EXAMINATION_SUGGESTIONS:
                    Long latestUpdatedTimeGeneralExamination = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.GENERAL_EXAMINATION_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(GeneralExaminationSuggestions.class, WebServiceType.GET_GENERAL_EXAMINATION_SUGGESTIONS, clinicalNotesDynamicField.getGeneralExam(), user.getUniqueId(),
                            latestUpdatedTimeGeneralExamination, this, this);
                    break;
                case CLINICAL_NOTE_PRESENT_COMPLAINT_SUGGESTIONS:
                    Long latestUpdatedTimePresentComplaint = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PRESENT_COMPLAINT_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PresentComplaintSuggestions.class, WebServiceType.GET_PRESENT_COMPLAINT_SUGGESTIONS, clinicalNotesDynamicField.getPresentComplaint(), user.getUniqueId(),
                            latestUpdatedTimePresentComplaint, this, this);
                    break;
                case CLINICAL_NOTE_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS:
                    Long latestUpdatedTimeHistoryPresentComplaint = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(HistoryPresentComplaintSuggestions.class, WebServiceType.GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, clinicalNotesDynamicField.getPresentComplaintHistory(), user.getUniqueId(),
                            latestUpdatedTimeHistoryPresentComplaint, this, this);
                    break;
                case CLINICAL_NOTE_SYSTEMIC_EXAMINATION_SUGGESTIONS:
                    Long latestUpdatedTimeSystemicExamination = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.SYSTEMIC_EXAMINATION_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(SystemicExaminationSuggestions.class, WebServiceType.GET_SYSTEMIC_EXAMINATION_SUGGESTIONS, clinicalNotesDynamicField.getSystemExam(), user.getUniqueId(),
                            latestUpdatedTimeSystemicExamination, this, this);
                    break;
                case CLINICAL_NOTE_PROVISIONAL_DIAGNOSIS_SUGGESTIONS:
                    Long latestUpdatedTimeProvisionalDiagnosis = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PROVISIONAL_DIAGNOSIS_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ProvisionalDiagnosisSuggestions.class, WebServiceType.GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, clinicalNotesDynamicField.getProvisionalDiagnosis(), user.getUniqueId(),
                            latestUpdatedTimeProvisionalDiagnosis, this, this);
                    break;
                case CLINICAL_NOTE_ECG_DETAILS_SUGGESTIONS:
                    Long latestUpdatedTimeEcgDetails = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.ECG_DETAILS_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(EcgDetailSuggestions.class, WebServiceType.GET_ECG_SUGGESTIONS, clinicalNotesDynamicField.getEcgDetails(), user.getUniqueId(),
                            latestUpdatedTimeEcgDetails, this, this);
                    break;
                case CLINICAL_NOTE_ECHO_SUGGESTIONS:
                    Long latestUpdatedTimeEco = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.ECHO_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(EchoSuggestions.class, WebServiceType.GET_ECHO_SUGGESTIONS, clinicalNotesDynamicField.getEcho(), user.getUniqueId(),
                            latestUpdatedTimeEco, this, this);
                    break;
                case CLINICAL_NOTE_X_RAY_DETAILS_SUGGESTIONS:
                    Long latestUpdatedTimeXrayDetails = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.X_RAY_DETAILS_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(XrayDetailSuggestions.class, WebServiceType.GET_XRAY_SUGGESTIONS, clinicalNotesDynamicField.getGetxRayDetails(), user.getUniqueId(),
                            latestUpdatedTimeXrayDetails, this, this);
                    break;
                case CLINICAL_NOTE_HOLTER_SUGGESTIONS:
                    Long latestUpdatedTimeHolder = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.HOLTER_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(HolterSuggestions.class, WebServiceType.GET_HOLTER_SUGGESTIONS, clinicalNotesDynamicField.getHolter(), user.getUniqueId(),
                            latestUpdatedTimeHolder, this, this);
                    break;
                case CLINICAL_NOTE_PA_SUGGESTIONS:
                    Long latestUpdatedTimePa = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PA_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PaSuggestions.class, WebServiceType.GET_PA_SUGGESTIONS, clinicalNotesDynamicField.getPa(), user.getUniqueId(),
                            latestUpdatedTimePa, this, this);
                    break;
                case CLINICAL_NOTE_PV_SUGGESTIONS:
                    Long latestUpdatedTimePv = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PV_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PvSuggestions.class, WebServiceType.GET_PV_SUGGESTIONS, clinicalNotesDynamicField.getPv(), user.getUniqueId(),
                            latestUpdatedTimePv, this, this);
                    break;
                case CLINICAL_NOTE_PS_SUGGESTIONS:
                    Long latestUpdatedTimePs = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PS_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PsSuggestions.class, WebServiceType.GET_PS_SUGGESTIONS, clinicalNotesDynamicField.getPs(), user.getUniqueId(),
                            latestUpdatedTimePs, this, this);
                    break;
                case CLINICAL_NOTE_INDICATION_OF_USG_SUGGESTIONS:
                    Long latestUpdatedTimeIndicationOfUsg = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.INDICATION_OF_USG_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(IndicationOfUsgSuggestions.class, WebServiceType.GET_INDICATION_OF_USG_SUGGESTIONS, clinicalNotesDynamicField.getIndicationOfUSG(), user.getUniqueId(),
                            latestUpdatedTimeIndicationOfUsg, this, this);
                    break;
                case CLINICAL_NOTE_NOTES_SUGGESTIONS:
                    Long latestUpdatedTimeNote = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.NOTES_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(NotesSuggestions.class, WebServiceType.GET_NOTES_SUGGESTIONS, clinicalNotesDynamicField.getNote(), user.getUniqueId(),
                            latestUpdatedTimeNote, this, this);
                    break;
                case CLINICAL_NOTE_MENSTRUAL_HISTORY_SUGGESTIONS:
                    Long latestUpdatedTimeMenstrualHistory = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.MENSTRUAL_HISTORY_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(MenstrualHistorySuggestions.class, WebServiceType.GET_MENSTRUAL_HISTORY_SUGGESTIONS, clinicalNotesDynamicField.getMenstrualHistory(), user.getUniqueId(),
                            latestUpdatedTimeMenstrualHistory, this, this);
                    break;
                case CLINICAL_NOTE_DIAGNOSIS_SUGGESTIONS:
                    Long latestUpdatedTimeDiagnosis = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DIAGNOSIS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(DiagnosisSuggestions.class, WebServiceType.GET_DIAGNOSIS_SUGGESTIONS, clinicalNotesDynamicField.getDiagnosis(), user.getUniqueId(),
                            latestUpdatedTimeDiagnosis, this, this);
                    break;
                case CLINICAL_NOTE_EAR_EXAM_SUGGESTIONS:
                    Long latestUpdatedTimeEarExam = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.EAR_EXAM_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(EarsExamSuggestions.class, WebServiceType.GET_EAR_EXAM_SUGGESTIONS, clinicalNotesDynamicField.getEarsExam(), user.getUniqueId(),
                            latestUpdatedTimeEarExam, this, this);
                    break;
                case CLINICAL_NOTE_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS:
                    Long latestUpdatedTimeIndirectLarygocsopyExam = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(IndirectLarygoscopyExamSuggestions.class, WebServiceType.GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS, clinicalNotesDynamicField.getIndirectLarygoscopyExam(), user.getUniqueId(),
                            latestUpdatedTimeIndirectLarygocsopyExam, this, this);

                    break;
                case CLINICAL_NOTE_NECK_EXAM_SUGGESTIONS:
                    Long latestUpdatedTimeNeckExam = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.NECK_EXAM_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(NeckExamSuggestions.class, WebServiceType.GET_NECK_EXAM_SUGGESTIONS, clinicalNotesDynamicField.getNeckExam(), user.getUniqueId(),
                            latestUpdatedTimeNeckExam, this, this);
                    break;
                case CLINICAL_NOTE_NOSE_EXAM_SUGGESTIONS:
                    Long latestUpdatedTimeNoseExam = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.NOSE_EXAM_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(NoseExamSuggestions.class, WebServiceType.GET_NOSE_EXAM_SUGGESTIONS, clinicalNotesDynamicField.getNoseExam(), user.getUniqueId(),
                            latestUpdatedTimeNoseExam, this, this);
                    break;
                case CLINICAL_NOTE_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS:
                    Long latestUpdatedTimeOralCavityExam = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(OralCavityThroatExamSuggestions.class, WebServiceType.GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS, clinicalNotesDynamicField.getOralCavityThroatExam(), user.getUniqueId(),
                            latestUpdatedTimeOralCavityExam, this, this);
                    break;
                case CLINICAL_NOTE_PC_EARS_SUGGESTIONS:
                    Long latestUpdatedTimePcEars = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PC_EARS_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PcEarsSuggestions.class, WebServiceType.GET_PC_EARS_SUGGESTIONS, clinicalNotesDynamicField.getPcEars(), user.getUniqueId(),
                            latestUpdatedTimePcEars, this, this);
                    break;
                case CLINICAL_NOTE_PC_NOSE_SUGGESTIONS:
                    Long latestUpdatedTimePcNose = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PC_NOSE_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PcNoseSuggestions.class, WebServiceType.GET_PC_NOSE_SUGGESTIONS, clinicalNotesDynamicField.getPcNose(), user.getUniqueId(),
                            latestUpdatedTimePcNose, this, this);
                    break;
                case CLINICAL_NOTE_PC_ORAL_CAVITY_SUGGESTIONS:
                    Long latestUpdatedTimePcOralCavity = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PC_ORAL_CAVITY_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PcOralCavitySuggestions.class, WebServiceType.GET_PC_ORAL_CAVITY_SUGGESTIONS, clinicalNotesDynamicField.getPcOralCavity(), user.getUniqueId(),
                            latestUpdatedTimePcOralCavity, this, this);
                    break;
                case CLINICAL_NOTE_PC_THROAT_SUGGESTIONS:
                    Long latestUpdatedTimePcThroat = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PC_THROAT_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PcThroatSuggestions.class, WebServiceType.GET_PC_THROAT_SUGGESTIONS, clinicalNotesDynamicField.getPcThroat(), user.getUniqueId(),
                            latestUpdatedTimePcThroat, this, this);
                    break;
                case CLINICAL_NOTE_PROCEDURE_NOTE_SUGGESTIONS:
                    Long latestUpdatedTimeProcedureNote = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PROCEDURE_NOTE_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ProcedureNoteSuggestions.class, WebServiceType.GET_PROCEDURE_NOTE_SUGGESTIONS, clinicalNotesDynamicField.getProcedureNote(), user.getUniqueId(),
                            latestUpdatedTimeProcedureNote, this, this);
                    break;

          /*  case CLINICAL_NOTE_EAR_EXAM_SUGGESTIONS:
            case CLINICAL_NOTE_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS:
            case CLINICAL_NOTE_NECK_EXAM_SUGGESTIONS:
            case CLINICAL_NOTE_NOSE_EXAM_SUGGESTIONS:
            case CLINICAL_NOTE_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS:
            case CLINICAL_NOTE_PC_EARS_SUGGESTIONS:
            case CLINICAL_NOTE_PC_NOSE_SUGGESTIONS:
            case CLINICAL_NOTE_PC_ORAL_CAVITY_SUGGESTIONS:
            case CLINICAL_NOTE_PC_THROAT_SUGGESTIONS:
            case CLINICAL_NOTE_PROCEDURE_NOTE_SUGGESTIONS:*/
                case CLINICAL_NOTE_ADVICE_SUGGESTIONS:
                    Long latestUpdatedTimeAdvice = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.ADVICE_SUGGESTIONS);
                    WebDataServiceImpl.getInstance(mApp).getAdviceSuggestionsList(AdviceSuggestion.class, WebServiceType.GET_SEARCH_ADVICE_SOLR, user.getUniqueId(),
                            latestUpdatedTimeAdvice, this, this);
                    break;

                case DRUG_TYPE:
                    //getting drugTypesList
                    Long latestUpdatedTimeStrengthUnit = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.STRENGTH_UNIT);
                    WebDataServiceImpl.getInstance(mApp)
                            .getDrugType(WebServiceType.GET_DRUG_TYPE, DrugType.class, false, this.user.getUniqueId(), latestUpdatedTimeStrengthUnit, this, this);
                    break;
                case DRUG_DURATION_UNIT:
                    //getting durationUnit
                    Long latestUpdatedTimeDuration = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DURATION_UNIT);
                    WebDataServiceImpl.getInstance(mApp)
                            .getDosageDirection(WebServiceType.GET_DURATION_UNIT, DrugDurationUnit.class, true, this.user.getUniqueId(), latestUpdatedTimeDuration, this, this);
                    break;
                case REFERENCES:
                    //getting references
                    Long latestUpdatedTimeReference = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.REFERENCE);
                    WebDataServiceImpl.getInstance(mApp)
                            .getReference(Reference.class, user.getUniqueId(), latestUpdatedTimeReference, BooleanTypeValues.TRUE, this, this);
                    break;
                case UI_PERMISSIONS:
                    WebDataServiceImpl.getInstance(mApp).getBothUIPermissionsForDoctor(UiPermissionsBoth.class, user.getUniqueId(), this, this);
                    break;
            }
        }
    }

    private void syncContact() {
        if (isEndOfListAchieved) {
            latestUpdatedTimeContact = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.REGISTERED_PATIENTS_DETAILS_SYNC);
        }
        WebDataServiceImpl.getInstance(mApp).getContactsList(RegisteredPatientDetailsUpdated.class, user.getUniqueId(),
                user.getForeignHospitalId(), user.getForeignLocationId(), latestUpdatedTimeContact, user, PAGE_NUMBER, MAX_NUMBER_OF_EVENTS, null, this, this);
//                WebDataServiceImpl.getInstance(mApp).getContactsList(RegisteredPatientDetailsUpdated.class, user.getUniqueId(),
//                        user.getForeignHospitalId(), user.getForeignLocationId(), latestUpdatedTimeContact, user,
//                        this, this);
    }

    private void startAnimation(SyncAllType syncAllType) {
        try {
            SyncAll syncAll = syncAllHashmap.get(syncAllType);
            if (syncAll != null) {
                syncAll.setIsLoading(true);
                notifyAdapter(new ArrayList<SyncAll>(syncAllHashmap.values()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void stopAnimation(SyncAll syncAll) {
//        try {
//            SyncAll syncAll = syncAllHashmap.get(syncAllType);
//            syncAll.setIsLoading(true);
//            notifyAdapter(new ArrayList<SyncAll>(syncAllHashmap.values()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void stopAnimation(boolean updateLastSyncTime, SyncAllType syncAllType) {
        try {
            SyncAll syncAll = syncAllHashmap.get(syncAllType);
            if (syncAll != null) {
                if (updateLastSyncTime)
                    syncAll.setLastSyncedTime(new Date().getTime());
                syncAll.setIsLoading(false);
                notifyAdapter(new ArrayList<SyncAll>(syncAllHashmap.values()));
                if (syncAllTypeList.contains(syncAllType))
                    syncAllTypeList.remove(syncAllType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAnimation() {
        animation.cancel();
    }

    @Override
    public void onSyncItemClicked(SyncAll syncAll) {
        startSyning(syncAll.getSyncAllType());
    }

    @Override
    public void formSyncTypesList(SyncAllType syncAllType) {
//        if (!syncAllTypesList.contains(syncAllType)) {
//            syncAllTypesList.add(syncAllType);
//        }
    }

    @Override
    public void removeFromSyncTypesList(SyncAllType syncAllType) {
//        if (syncAllTypesList.contains(syncAllType))
//            syncAllTypesList.remove(syncAllType);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideProgressDialog();
        LogUtils.LOGD(TAG, "Success " + String.valueOf(volleyResponseBean.getWebServiceType()));
        stopAnimationAndStartNext(volleyResponseBean.getWebServiceType());
        if (Util.isNullOrEmptyList(syncAllTypeList))
            for (SyncAllType syncAllType : syncAllTypeList)
                stopAnimation(false, syncAllType);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        mActivity.hideProgressDialog();
        if (Util.isNullOrEmptyList(syncAllTypeList))
            for (SyncAllType syncAllType : syncAllTypeList)
                stopAnimation(false, syncAllType);

        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (!Util.isNullOrEmptyList(syncAllHashmap))
                        initData();
                    break;
                case GET_CONTACTS:
//                    SyncAllType syncAllTypeContact = SyncAllType.CONTACT;
                 /*   if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PATIENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }*/
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        if (Util.isNullOrEmptyList(response.getDataList()) || response.getDataList().size() < MAX_NUMBER_OF_EVENTS || Util.isNullOrEmptyList(response.getDataList())) {
                            isEndOfListAchieved = true;
                            mActivity.updateProgressDialog(10, 10);
                        } else {
                            showProgressDialog(response);
                        }
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PATIENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    break;

                case GET_DATA_PERMISSION:
//                    SyncAllType syncAllTypeContact = SyncAllType.CONTACT;
                    if (response.getData() != null) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DATA_PERMISSIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
//                    stopAnimation(true, syncAllTypeContact);
//                    if (isSyncAllClicked)
//                        syncNext(syncAllTypeContact);
                    break;
                case GET_GROUPS:
//                    SyncAllType syncAllTypeGroup = SyncAllType.GROUP;
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_GROUPS_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
//                    stopAnimation(true, syncAllTypeGroup);
//                    if (isSyncAllClicked)
//                        syncNext(syncAllTypeGroup);
                    break;
                case GET_DISEASE_LIST:
//                    SyncAllType syncAllTypeHistory = SyncAllType.HISTORY;
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DISEASE_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
//                    stopAnimation(true, syncAllTypeHistory);
//                    if (isSyncAllClicked)
//                        syncNext(syncAllTypeHistory);
                    break;
                case GET_DRUGS_LIST_CUSTOM:
//                    SyncAllType syncAllTypeDrugCustom = SyncAllType.DRUG_CUSTOM;
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DRUGS_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
//                    stopAnimation(true, syncAllTypeDrugCustom);
//                    if (isSyncAllClicked)
//                        syncNext(syncAllTypeDrugCustom);
                    break;
                case GET_DRUG_DOSAGE:
//                    SyncAllType syncAllTypeFrequency = SyncAllType.FREQUENCY;
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DRUG_DOSAGE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
//                    stopAnimation(true, syncAllTypeFrequency);
//                    if (isSyncAllClicked)
//                        syncNext(syncAllTypeFrequency);
                    break;
                case GET_DIRECTION:
//                    SyncAllType syncAllTypeDirection = SyncAllType.DIRECTION;
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIRECTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
//                    stopAnimation(true, syncAllTypeDirection);
//                    if (isSyncAllClicked)
//                        syncNext(syncAllTypeDirection);
                    break;
                case GET_DIAGRAMS_LIST:
//                    SyncAllType syncAllTypeDiagramNotes = SyncAllType.NOTES_DIAGRAM;
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIAGRAMS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
//                    stopAnimation(true, syncAllTypeDiagramNotes);
//                    if (isSyncAllClicked)
//                        syncNext(syncAllTypeDiagramNotes);
                    break;
                case GET_PRESENT_COMPLAINT_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PRESENT_COMPLAINT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_PRESENT_COMPLAINT_SUGGESTIONS);
                    return;
                case GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS);
                    return;
                case GET_MENSTRUAL_HISTORY_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_MENSTRUAL_HISTORY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_MENSTRUAL_HISTORY_SUGGESTIONS);
                    return;
                case GET_OBSTETRIC_HISTORY_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_OBSTETRIC_HISTORY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_OBSTETRIC_HISTORY_SUGGESTIONS);
                    return;
                case GET_GENERAL_EXAMINATION_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_GENERAL_EXAMINATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_GENERAL_EXAMINATION_SUGGESTIONS);
                    return;
                case GET_SYSTEMIC_EXAMINATION_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_SYSTEMIC_EXAMINATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_SYSTEMIC_EXAMINATION_SUGGESTIONS);
                    return;
                case GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_PROVISIONAL_DIAGNOSIS_SUGGESTIONS);
                    return;
                case GET_ECG_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_ECG_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_ECG_DETAILS_SUGGESTIONS);
                    return;
                case GET_ECHO_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_ECHO_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_ECHO_SUGGESTIONS);
                    return;
                case GET_XRAY_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_XRAY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_X_RAY_DETAILS_SUGGESTIONS);
                    return;
                case GET_HOLTER_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_HOLTER_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_HOLTER_SUGGESTIONS);
                    return;
                case GET_PA_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PA_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_PA_SUGGESTIONS);
                    return;
                case GET_PV_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PV_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_PV_SUGGESTIONS);
                    return;
                case GET_PS_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_PS_SUGGESTIONS);
                    return;
                case GET_EAR_EXAM_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_EARS_EXAM_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_EAR_EXAM_SUGGESTIONS);
                    return;
                case GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS);
                    return;
                case GET_NECK_EXAM_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_NECK_EXAM_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_NECK_EXAM_SUGGESTIONS);
                    return;
                case GET_NOSE_EXAM_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_NOSE_EXAM_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_NOSE_EXAM_SUGGESTIONS);
                    return;
                case GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS);
                    return;
                case GET_PC_EARS_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PC_EARS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_PC_EARS_SUGGESTIONS);
                    return;
                case GET_PC_NOSE_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PC_NOSE_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_PC_NOSE_SUGGESTIONS);
                    return;
                case GET_PC_ORAL_CAVITY_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PC_ORAL_CAVITY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_PC_ORAL_CAVITY_SUGGESTIONS);
                    return;
                case GET_PC_THROAT_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PC_THROAT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_PC_THROAT_SUGGESTIONS);
                    return;
                case GET_PROCEDURE_NOTE_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PROCEDURE_NOTE_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_PROCEDURE_NOTE_SUGGESTIONS);
                    return;

                case GET_INDICATION_OF_USG_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_INDICATION_OF_USG_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_INDICATION_OF_USG_SUGGESTIONS);
                    return;
                case GET_NOTES_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_NOTES_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_NOTES_SUGGESTIONS);
                    return;
                case GET_COMPLAINT_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_COMPLAINT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_COMPLAINT_SUGGESTIONS);
                    return;
                case GET_OBSERVATION_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_OBSERVATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_OBSERVATION_SUGGESTIONS);
                    return;
                case GET_INVESTIGATION_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_INVESTIGATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_INVESTIGATION_SUGGESTIONS);
                    return;
                case GET_DIAGNOSIS_SUGGESTIONS:
//                    SyncAllType syncAllClinicalNotesData = SyncAllType.CLINICAL_NOTES_DATA;
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIAGNOSIS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    syncNext(SyncAllType.CLINICAL_NOTE_DIAGNOSIS_SUGGESTIONS);
                    return;
                case GET_SEARCH_ADVICE_SOLR:
//                    SyncAllType syncAllClinicalNotesData = SyncAllType.CLINICAL_NOTES_DATA;
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_ADVICE_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    break;
                case GET_REFERENCE:
//                    SyncAllType syncAllTypeReferences = SyncAllType.REFERENCES;
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_REFERENCE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
//                    stopAnimation(true, syncAllTypeReferences);
//                    if (isSyncAllClicked)
//                        syncNext(syncAllTypeReferences);
                    break;
                case GET_DRUG_TYPE:
//                    SyncAllType syncAllTypeDrugType = SyncAllType.DRUG_TYPE;
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DRUG_TYPE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
//                    stopAnimation(true, syncAllTypeDrugType);
//                    if (isSyncAllClicked)
//                        syncNext(syncAllTypeDrugType);
                    break;
                case GET_DURATION_UNIT:
//                    SyncAllType syncAllTypeDurationUnit = SyncAllType.DRUG_DURATION_UNIT;
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DURATION_UNIT, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
//                    stopAnimation(true, syncAllTypeDurationUnit);
                    break;
                case GET_BOTH_PERMISSIONS_FOR_DOCTOR:
                    if (response.getData() != null) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_BOTH_USER_UI_PERMISSIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    break;
                default:
                    break;
            }

            stopAnimationAndStartNext(response.getWebServiceType());
            if (Util.isNullOrEmptyList(syncAllTypeList))
                stopAnimation();
        }
        mActivity.hideLoading();
    }

    private boolean isPaginationRequired(VolleyResponseBean response) {
        if (response.getData() instanceof Long)
            MAX_COUNT = (long) response.getData();
        else if (response.getData() instanceof Double) {
            Double data = (Double) response.getData();
            MAX_COUNT = Math.round(data);
        }
        long count = LocalDataServiceImpl.getInstance(mApp).getListCount(user);

        if (count < MAX_COUNT) {
            mActivity.showProgressDialog();
            PAGE_NUMBER = (int) (count / MAX_NUMBER_OF_EVENTS);
            int progess = (int) ((PAGE_NUMBER * MAX_NUMBER_OF_EVENTS * 100) / MAX_COUNT);
            mActivity.updateProgressDialog(MAX_COUNT, progess);
            isEndOfListAchieved = false;
            latestUpdatedTimeContact = 0l;
            syncContact();
            return true;
        } else return false;
    }

    private void showProgressDialog(VolleyResponseBean response) {
        if (response.getData() instanceof Long)
            MAX_COUNT = (long) response.getData();
        else if (response.getData() instanceof Double) {
            Double data = (Double) response.getData();
            MAX_COUNT = Math.round(data);
            if (MAX_COUNT > (2 * MAX_NUMBER_OF_EVENTS)) {
                if (isEndOfListAchieved)
                    mActivity.showProgressDialog();
            }
            isEndOfListAchieved = false;
            PAGE_NUMBER = PAGE_NUMBER + 1;
            int progess = (int) ((MAX_NUMBER_OF_EVENTS * 100) / MAX_COUNT);
            mActivity.updateProgressDialog(MAX_COUNT, progess);

        }

    }

    private void stopAnimationAndStartNext(WebServiceType webServiceType) {
        SyncAllType syncAllType = getSyncAllType(webServiceType);
        if (syncAllType != null) {
            stopAnimation(true, syncAllType);
            if (isSyncAllClicked)
                syncNext(syncAllType);
        } else
            stopAnimation(true, syncAllType);
    }

    private SyncAllType getSyncAllType(WebServiceType webServiceType) {
        SyncAllType syncAllType = null;
        if (webServiceType != null)
            switch (webServiceType) {
                case GET_CONTACTS:
                    syncAllType = SyncAllType.CONTACT;
                    break;
                case GET_DATA_PERMISSION:
                    syncAllType = SyncAllType.DATA_PERMISSIONS;
                    break;
                case GET_GROUPS:
                    syncAllType = SyncAllType.GROUP;
                    break;
                case GET_DISEASE_LIST:
                    syncAllType = SyncAllType.HISTORY;
                    break;
                case GET_DRUGS_LIST_CUSTOM:
                    syncAllType = SyncAllType.DRUG_CUSTOM;
                    break;
                case GET_DRUG_DOSAGE:
                    syncAllType = SyncAllType.FREQUENCY;
                    break;
                case GET_DIRECTION:
                    syncAllType = SyncAllType.DIRECTION;
                    break;
                case GET_DIAGRAMS_LIST:
                    syncAllType = SyncAllType.NOTES_DIAGRAM;
                    break;
                case GET_COMPLAINT_SUGGESTIONS:
                    syncAllType = SyncAllType.CLINICAL_NOTE_COMPLAINT_SUGGESTIONS;
                    break;
                case GET_OBSERVATION_SUGGESTIONS:
                    syncAllType = SyncAllType.CLINICAL_NOTE_OBSERVATION_SUGGESTIONS;
                    break;
                case GET_INVESTIGATION_SUGGESTIONS:
                    syncAllType = SyncAllType.CLINICAL_NOTE_INVESTIGATION_SUGGESTIONS;
                    break;
                case GET_DIAGNOSIS_SUGGESTIONS:
                    syncAllType = SyncAllType.CLINICAL_NOTE_DIAGNOSIS_SUGGESTIONS;
                    break;
                case GET_SEARCH_ADVICE_SOLR:
                    syncAllType = SyncAllType.CLINICAL_NOTES_DATA;
                    break;
                case GET_REFERENCE:
                    syncAllType = SyncAllType.REFERENCES;
                    break;
                case GET_DRUG_TYPE:
                    syncAllType = SyncAllType.DRUG_TYPE;
                    break;
                case GET_DURATION_UNIT:
                    syncAllType = SyncAllType.DRUG_DURATION_UNIT;
                    break;
                case GET_BOTH_PERMISSIONS_FOR_DOCTOR:
                    syncAllType = SyncAllType.UI_PERMISSIONS;
                    break;
            }
        return syncAllType;
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                user = doctor.getUser();
                doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileObject(user.getUniqueId());
                initDefaultData();
                return volleyResponseBean;
            case ADD_PATIENTS:
                LocalDataServiceImpl.getInstance(mApp).
                        addPatientsList((ArrayList<RegisteredPatientDetailsUpdated>) (ArrayList<?>) response.getDataList());
//                updateSyncObjectInHashMap(SyncAllType.CONTACT);

                if (!isEndOfListAchieved) {
                    syncContact();
                } else {
                    if (!isPaginationRequired(response)) {
                        mActivity.hideProgressDialog();
                        resetListAndPagingAttributes();
                        updateSyncObjectInHashMap(SyncAllType.CONTACT);
                    }
                }
                break;
            case ADD_DATA_PERMISSIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addDataPermission((DataPermissions) response.getData());
                updateSyncObjectInHashMap(SyncAllType.DATA_PERMISSIONS);
                break;
            case ADD_GROUPS_LIST:
                LocalDataServiceImpl.getInstance(mApp).addUserGroupsList((ArrayList<UserGroups>) (ArrayList<?>) response.getDataList());
                updateSyncObjectInHashMap(SyncAllType.GROUP);
                break;
            case ADD_DISEASE_LIST:
                LocalDataServiceImpl.getInstance(mApp).addDiseaseList((ArrayList<Disease>) (ArrayList<?>) response.getDataList());
                updateSyncObjectInHashMap(SyncAllType.HISTORY);
                break;
            case ADD_DRUGS_LIST:
                LocalDataServiceImpl.getInstance(mApp).addDrugsList((ArrayList<Drug>) (ArrayList<?>) response.getDataList());
                updateSyncObjectInHashMap(SyncAllType.DRUG_CUSTOM);
                break;
            case ADD_DRUG_DOSAGE:
                LocalDataServiceImpl.getInstance(mApp).addDrugDosageList((ArrayList<DrugDosage>) (ArrayList<?>) response.getDataList());
                updateSyncObjectInHashMap(SyncAllType.FREQUENCY);
                break;
            case ADD_DIRECTIONS:
                LocalDataServiceImpl.getInstance(mApp).addDirectionsList((ArrayList<DrugDirection>) (ArrayList<?>) response.getDataList());
                updateSyncObjectInHashMap(SyncAllType.DIRECTION);
                break;
            case ADD_DRUG_TYPE:
                LocalDataServiceImpl.getInstance(mApp).addDrugType((ArrayList<DrugType>) (ArrayList<?>) response.getDataList());
                updateSyncObjectInHashMap(SyncAllType.DRUG_TYPE);
                break;
            case ADD_DURATION_UNIT:
                LocalDataServiceImpl.getInstance(mApp).addDurationUnitList((ArrayList<DrugDurationUnit>) (ArrayList<?>) response.getDataList());
                updateSyncObjectInHashMap(SyncAllType.DRUG_DURATION_UNIT);
                break;
            case ADD_REFERENCE:
                LocalDataServiceImpl.getInstance(mApp).addReferenceList((ArrayList<Reference>) (ArrayList<?>) response.getDataList());
                updateSyncObjectInHashMap(SyncAllType.REFERENCES);
                break;
            case ADD_DIAGRAMS:
                LocalDataServiceImpl.getInstance(mApp).addDiagramsList(null, (ArrayList<Diagram>) (ArrayList<?>) response
                        .getDataList());
                updateSyncObjectInHashMap(SyncAllType.NOTES_DIAGRAM);
                break;
            case ADD_COMPLAINT_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_COMPLAINT_SUGGESTIONS, LocalTabelType.COMPLAINT_SUGGESTIONS,
                                response.getDataList(), null, null);
                break;
            case ADD_OBSERVATION_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_OBSERVATION_SUGGESTIONS, LocalTabelType.OBSERVATION_SUGGESTIONS,
                                response.getDataList(), null, null);
                break;
            case ADD_INVESTIGATION_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_INVESTIGATION_SUGGESTIONS, LocalTabelType.INVESTIGATION_SUGGESTIONS,
                                response.getDataList(), null, null);
                break;
            case ADD_DIAGNOSIS_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_DIAGNOSIS_SUGGESTIONS, LocalTabelType.DIAGNOSIS_SUGGESTIONS,
                                response.getDataList(), null, null);
//                updateSyncObjectInHashMap(SyncAllType.CLINICAL_NOTES_DATA);
                break;
            case ADD_PRESENT_COMPLAINT_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_PRESENT_COMPLAINT_SUGGESTIONS, LocalTabelType.PRESENT_COMPLAINT_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, LocalTabelType.HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_MENSTRUAL_HISTORY_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_MENSTRUAL_HISTORY_SUGGESTIONS, LocalTabelType.MENSTRUAL_HISTORY_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_OBSTETRIC_HISTORY_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_OBSTETRIC_HISTORY_SUGGESTIONS, LocalTabelType.OBSTETRIC_HISTORY_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_GENERAL_EXAMINATION_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_GENERAL_EXAMINATION_SUGGESTIONS, LocalTabelType.GENERAL_EXAMINATION_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_SYSTEMIC_EXAMINATION_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_SYSTEMIC_EXAMINATION_SUGGESTIONS, LocalTabelType.SYSTEMIC_EXAMINATION_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_PROVISIONAL_DIAGNOSIS_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, LocalTabelType.PROVISIONAL_DIAGNOSIS_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_ECG_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_ECG_SUGGESTIONS, LocalTabelType.ECG_DETAILS_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_ECHO_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_ECHO_SUGGESTIONS, LocalTabelType.ECHO_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_XRAY_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_XRAY_SUGGESTIONS, LocalTabelType.X_RAY_DETAILS_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_HOLTER_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_HOLTER_SUGGESTIONS, LocalTabelType.HOLTER_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_PA_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_PA_SUGGESTIONS, LocalTabelType.PA_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_PV_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_PV_SUGGESTIONS, LocalTabelType.PV_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_PS_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_PS_SUGGESTIONS, LocalTabelType.PS_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_INDICATION_OF_USG_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_INDICATION_OF_USG_SUGGESTIONS, LocalTabelType.INDICATION_OF_USG_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_NOTES_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_NOTES_SUGGESTIONS, LocalTabelType.NOTES_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS, LocalTabelType.ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_NECK_EXAM_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_NECK_EXAM_SUGGESTIONS, LocalTabelType.NECK_EXAM_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_NOSE_EXAM_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_NOSE_EXAM_SUGGESTIONS, LocalTabelType.NOSE_EXAM_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_PC_EARS_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_PC_EARS_SUGGESTIONS, LocalTabelType.PC_EARS_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_PC_NOSE_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_PC_NOSE_SUGGESTIONS, LocalTabelType.PC_NOSE_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_PC_ORAL_CAVITY_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_PC_ORAL_CAVITY_SUGGESTIONS, LocalTabelType.PC_ORAL_CAVITY_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_PC_THROAT_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_PC_THROAT_SUGGESTIONS, LocalTabelType.PC_THROAT_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_PROCEDURE_NOTE_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_PROCEDURE_NOTE_SUGGESTIONS, LocalTabelType.PROCEDURE_NOTE_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS, LocalTabelType.INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_EARS_EXAM_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_EAR_EXAM_SUGGESTIONS, LocalTabelType.EAR_EXAM_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_ADVICE_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_SEARCH_ADVICE_SOLR, LocalTabelType.ADVICE_SUGGESTIONS,
                                    response.getDataList(), null, null);
                updateSyncObjectInHashMap(SyncAllType.CLINICAL_NOTE_ADVICE_SUGGESTIONS);
                break;
            case ADD_BOTH_USER_UI_PERMISSIONS:
                if (response.getData() != null)
                    LocalDataServiceImpl.getInstance(mApp).
                            addBothUserUiPermissions((UiPermissionsBoth) response.getData());
                updateSyncObjectInHashMap(SyncAllType.UI_PERMISSIONS);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new

                    VolleyResponseBean();
        volleyResponseBean.setWebServiceType(response.getWebServiceType());
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    private void updateSyncObjectInHashMap(SyncAllType contact) {

    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            if (!Util.isNullOrBlank(filePath)) {
                int width = ivImage.getLayoutParams().width;
                LogUtils.LOGD(TAG, "Image SIze " + width);
                Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, width);
                if (bitmap != null) {
                    ivImage.setImageBitmap(bitmap);
                    ivImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onPreExecute() {

    }

    private void resetListAndPagingAttributes() {
        PAGE_NUMBER = 0;
        isEndOfListAchieved = true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            isSyncAllClicked = false;
            SyncAll syncAll = (SyncAll) parent.getItemAtPosition(position);
            if (syncAll != null && syncAll.getSyncAllType() != null) {
                onSyncItemClicked(syncAllHashmap.get(syncAll.getSyncAllType()));
            }
        } else
            onNetworkUnavailable(null);
    }
}
