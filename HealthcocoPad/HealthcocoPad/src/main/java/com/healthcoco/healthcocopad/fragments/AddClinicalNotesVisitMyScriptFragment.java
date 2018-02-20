package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.ClinicalNoteToSend;
import com.healthcoco.healthcocopad.bean.BloodPressure;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VitalSigns;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.custom.MyScriptEditText;
import com.healthcoco.healthcocopad.enums.ClinicalNotesPermissionType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.healthcoco.healthcocopad.enums.ClinicalNotesPermissionType.PRESENT_COMPLAINT;
import static com.healthcoco.healthcocopad.utilities.HealthCocoConstants.REQUEST_CODE_ADD_CLINICAL_NOTES;

/**
 * Created by neha on 24/04/17.
 */

public class AddClinicalNotesVisitMyScriptFragment extends HealthCocoFragment implements
        View.OnClickListener, LocalDoInBackgroundListenerOptimised {
    public static final String CHARACTER_TO_REPLACE_COMMA_WITH_SPACES = " , ";
    public static final String CHARACTER_TO_BE_REPLACED = ",";
    private LinearLayout parentVitalSigns;
    private LinearLayout parentPermissionItems;

    //vitalsigns editTexts
    private MyScriptEditText editSystolic;
    private MyScriptEditText editDiastolic;
    private MyScriptEditText editBodyTemperature;
    private MyScriptEditText editRespRate;
    private MyScriptEditText editSpo2;
    private MyScriptEditText editWeight;
    private MyScriptEditText editHeartRate;
    private ClinicalNotes clinicalNotes;
    private MyScriptAddVisitsFragment myScriptAddVisitsFragment;
    private ArrayList<String> clinicalNotesUiPermissionsList;
    private HashMap<String, String> diagramsList = new HashMap<>();
    private LinearLayout containerDiagrams;
    private User user;
    private boolean isFromClone;
    private String clinicalNoteId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_myscript_clinical_notes_visit, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(TAG_USER))
            user = Parcels.unwrap(bundle.getParcelable(TAG_USER));
        init();

        Intent intent = mActivity.getIntent();
        if (intent != null) {
            Parcelable isFromCloneParcelable = intent.getParcelableExtra(HealthCocoConstants.TAG_IS_FROM_CLONE);
            if (isFromCloneParcelable != null)
                isFromClone = Parcels.unwrap(isFromCloneParcelable);
        }
        if (user == null) {
            mActivity.showLoading(false);
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void init() {
        myScriptAddVisitsFragment = (MyScriptAddVisitsFragment) mFragmentManager.findFragmentByTag(MyScriptAddVisitsFragment.class.getSimpleName());
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        parentVitalSigns = (LinearLayout) view.findViewById(R.id.parent_add_vital_signs);
        parentPermissionItems = (LinearLayout) view.findViewById(R.id.parent_permissions_items);

        editSystolic = (MyScriptEditText) view.findViewById(R.id.edit_systolic);
        editDiastolic = (MyScriptEditText) view.findViewById(R.id.edit_diastolic);
        editBodyTemperature = (MyScriptEditText) view.findViewById(R.id.edit_body_temperature);
        editRespRate = (MyScriptEditText) view.findViewById(R.id.edit_resp_rate);
        editHeartRate = (MyScriptEditText) view.findViewById(R.id.edit_heart_rate);
        editSpo2 = (MyScriptEditText) view.findViewById(R.id.edit_spo2);
        editWeight = (MyScriptEditText) view.findViewById(R.id.edit_weight);
        containerDiagrams = (LinearLayout) view.findViewById(R.id.container_diagrams);
        if (containerDiagrams == null && myScriptAddVisitsFragment != null)
            containerDiagrams = myScriptAddVisitsFragment.getDiagramContainer();
        //default UI ettings
        parentVitalSigns.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        if (myScriptAddVisitsFragment != null) {
            //initialising vital signs editTexts listeners
            View.OnTouchListener touchListener = myScriptAddVisitsFragment.getOnTouchListener();
            editBodyTemperature.setOnTouchListener(touchListener);
            editWeight.setOnTouchListener(touchListener);
            editHeartRate.setOnTouchListener(touchListener);
            editRespRate.setOnTouchListener(touchListener);
            editSpo2.setOnTouchListener(touchListener);
            editDiastolic.setOnTouchListener(touchListener);
            editSystolic.setOnTouchListener(touchListener);
        }
    }

    private void initVitalSigns(VitalSigns vitalSigns) {
        if (vitalSigns != null) {
            editHeartRate.setText(Util.getValidatedValue(vitalSigns.getPulse()));
            editBodyTemperature.setText(Util.getValidatedValue(vitalSigns.getTemperature()));
            editWeight.setText(Util.getValidatedValue(vitalSigns.getWeight()));
            if (vitalSigns.getBloodPressure() != null
                    && !Util.isNullOrBlank(vitalSigns.getBloodPressure().getSystolic())
                    && !Util.isNullOrBlank(vitalSigns.getBloodPressure().getDiastolic())) {
                BloodPressure bloodPressure = vitalSigns.getBloodPressure();
                editSystolic.setText(bloodPressure.getSystolic());
                editDiastolic.setText(bloodPressure.getDiastolic());
            }
            editRespRate.setText(Util.getValidatedValue(vitalSigns.getBreathing()));
            editSpo2.setText(Util.getValidatedValue(vitalSigns.getSpo2()));
        }
    }

    public void initUiPermissions(String clinicalNotesUiPermissionJson) {
        if (!Util.isNullOrBlank(clinicalNotesUiPermissionJson)) {
            Gson gson = new Gson();
            //initialising clinical notes UI permissions
            if (!Util.isNullOrBlank(clinicalNotesUiPermissionJson)) {
                clinicalNotesUiPermissionsList = gson.fromJson(clinicalNotesUiPermissionJson, new TypeToken<ArrayList<String>>() {
                }.getType());
                clinicalNotesUiPermissionsList.removeAll(Collections.singleton(null));
                if (!Util.isNullOrEmptyList(clinicalNotesUiPermissionsList)) {
                    parentPermissionItems.removeAllViews();
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.DIAGRAM.getValue()) && clinicalNotesUiPermissionsList.size() == 1) {
                        sendBroadcastForClinicalNoteButtonVisibility(false);
                        initialiseDiagramsView();
                        return;
                    } else
                        sendBroadcastForClinicalNoteButtonVisibility(true);

                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.VITAL_SIGNS.getValue()) || (clinicalNotes != null && clinicalNotes.getVitalSigns() != null)) {
                        parentVitalSigns.setVisibility(View.VISIBLE);
                        if (clinicalNotes != null && clinicalNotes.getVitalSigns() != null)
                            initVitalSigns(clinicalNotes.getVitalSigns());
                    }
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PRESENT_COMPLAINT.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPresentComplaint())))
                        addPermissionItem(PRESENT_COMPLAINT);
                    if ((clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PC_NOSE.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPcNose()))) || (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PC_ORAL_CAVITY.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPcOralCavity()))) || (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PC_THROAT.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPcThroat()))) || (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PC_EARS.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPcEars()))))
                        addEntPermissionItem();
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PAST_HISTORY.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPastHistory())))
                        addPermissionItem(ClinicalNotesPermissionType.PAST_HISTORY);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PERSONAL_HISTORY.getValue())
                            || (clinicalNotes != null))
                        addPersonalHistoryPermissionItem();
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.FAMILY_HISTORY.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getFamilyHistory())))
                        addPermissionItem(ClinicalNotesPermissionType.FAMILY_HISTORY);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.GENERAL_HISTORY.getValue())
                            || (clinicalNotes != null))
                        addGeneralHistoryPermissionItem();
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.COMPLAINT.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getComplaint())))
                        addPermissionItem(ClinicalNotesPermissionType.COMPLAINT);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPresentComplaintHistory())))
                        addPermissionItem(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.MENSTRUAL_HISTORY.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getMenstrualHistory())))
                        addPermissionItem(ClinicalNotesPermissionType.MENSTRUAL_HISTORY);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.OBSTETRIC_HISTORY.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getObstetricHistory())))
                        addPermissionItem(ClinicalNotesPermissionType.OBSTETRIC_HISTORY);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.GENERAL_EXAMINATION.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getGeneralExam())))
                        addPermissionItem(ClinicalNotesPermissionType.GENERAL_EXAMINATION);
                    if ((clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.NOSE_EXAM.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getNoseExam()))) || (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.ORAL_CAVITY_THROAT_EXAM.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getOralCavityThroatExam()))) || (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.INDIRECT_LARYGOSCOPY_EXAM.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getIndirectLarygoscopyExam()))) || (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.NECK_EXAM.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getNeckExam()))) || (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.EARS_EXAM.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getEarsExam()))))
                        addEntExaminationPermissionItem();
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION.getValue())
                            || (clinicalNotes != null && clinicalNotes.getSystemExam() != null))
                        addPermissionItem(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.INDICATION_OF_USG.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getIndicationOfUSG())))
                        addPermissionItem(ClinicalNotesPermissionType.INDICATION_OF_USG);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PA.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPa())))
                        addPermissionItem(ClinicalNotesPermissionType.PA);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PS.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPs())))
                        addPermissionItem(ClinicalNotesPermissionType.PS);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PV.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPv())))
                        addPermissionItem(ClinicalNotesPermissionType.PV);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.OBSERVATION.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getObservation())))
                        addPermissionItem(ClinicalNotesPermissionType.OBSERVATION);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.INVESTIGATIONS.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getInvestigation())))
                        addPermissionItem(ClinicalNotesPermissionType.INVESTIGATIONS);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getProvisionalDiagnosis())))
                        addPermissionItem(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.DIAGNOSIS.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getDiagnosis())))
                        addPermissionItem(ClinicalNotesPermissionType.DIAGNOSIS);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.ECG.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getEcgDetails())))
                        addPermissionItem(ClinicalNotesPermissionType.ECG);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.ECHO.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getEcho())))
                        addPermissionItem(ClinicalNotesPermissionType.ECHO);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.XRAY.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getxRayDetails())))
                        addPermissionItem(ClinicalNotesPermissionType.XRAY);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.HOLTER.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getHolter())))
                        addPermissionItem(ClinicalNotesPermissionType.HOLTER);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.NOTES.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getNote())))
                        addPermissionItem(ClinicalNotesPermissionType.NOTES);
                    if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PROCEDURES.getValue())
                            || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getProcedureNote())))
                        addPermissionItem(ClinicalNotesPermissionType.PROCEDURES);
                    initialiseDiagramsView();
                }
                return;
            }
        }
        hideClinicalNoteViewsAndButtons();
    }

    private void hideClinicalNoteViewsAndButtons() {
        sendBroadcastForClinicalNoteButtonVisibility(false);
        sendBroadcastForDiagramButtonVisibility(false);
    }

    private void initialiseDiagramsView() {
        //initialising diagrams view visibility
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.DIAGRAM.getValue())
                || (clinicalNotes != null && !Util.isNullOrEmptyList(clinicalNotes.getDiagrams()))) {
            prePopulateDiagrams();
            sendBroadcastForDiagramButtonVisibility(true);
        } else
            sendBroadcastForDiagramButtonVisibility(false);
    }

    private void sendBroadcastForDiagramButtonVisibility(boolean showVisibility) {
        try {
            Intent intent = new Intent(MyScriptAddVisitsFragment.INTENT_DIAGRAM_BUTTON_VISIBILITY);
            intent.putExtra(MyScriptAddVisitsFragment.TAG_VISIBILITY, showVisibility);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBroadcastForDiagramLayoutVisibility(boolean showVisibility) {
        try {
            Intent intent = new Intent(MyScriptAddVisitsFragment.INTENT_DIAGRAM_LAYOUT_VISIBILITY);
            intent.putExtra(MyScriptAddVisitsFragment.TAG_VISIBILITY, showVisibility);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBroadcastForClinicalNoteLayoutVisibility(boolean showVisibility) {
        try {
            Intent intent = new Intent(MyScriptAddVisitsFragment.INTENT_CLINCIAL_NOTE_LAYOUT_VISIBILITY);
            intent.putExtra(MyScriptAddVisitsFragment.TAG_VISIBILITY, showVisibility);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBroadcastForClinicalNoteButtonVisibility(boolean showVisibility) {
        try {
            Intent intent = new Intent(MyScriptAddVisitsFragment.INTENT_CLINCIAL_NOTE_BUTTON_VISIBILITY);
            intent.putExtra(MyScriptAddVisitsFragment.TAG_VISIBILITY, showVisibility);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPermissionItem(ClinicalNotesPermissionType clinicalNotesPermissionType) {
        LinearLayout layoutItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_add_myscript_clinical_note_permision, null);
        TextView tvTitle = (TextView) layoutItemPermission.findViewById(R.id.tv_title);
        MyScriptEditText autotvPermission = (MyScriptEditText) layoutItemPermission.findViewById(R.id.edit_permission_text);
//        autotvPermission.setOnFocusChangeListener(this);
        View viewTopLine = (View) layoutItemPermission.findViewById(R.id.view_top_line);
        viewTopLine.setVisibility(View.GONE);
        tvTitle.setText(clinicalNotesPermissionType.getTextId());
        autotvPermission.setId(clinicalNotesPermissionType.getAutotvId());
        autotvPermission.setHint(clinicalNotesPermissionType.getHintId());
        autotvPermission.setTag(String.valueOf(clinicalNotesPermissionType));
        if (myScriptAddVisitsFragment != null) {
            autotvPermission.setOnTouchListener(myScriptAddVisitsFragment.getOnTouchListener());
        }
        String text = "";
        if (clinicalNotes != null) {
            switch (clinicalNotesPermissionType) {
                case PRESENT_COMPLAINT:
                    text = clinicalNotes.getPresentComplaint();
                    break;
                case COMPLAINT:
                    text = clinicalNotes.getComplaint();
                    break;
                case HISTORY_OF_PRESENT_COMPLAINT:
                    text = clinicalNotes.getPresentComplaintHistory();
                    break;
                case MENSTRUAL_HISTORY:
                    text = clinicalNotes.getMenstrualHistory();
                    break;
                case OBSTETRIC_HISTORY:
                    text = clinicalNotes.getObstetricHistory();
                    break;
                case GENERAL_EXAMINATION:
                    text = clinicalNotes.getGeneralExam();
                    break;
                case SYSTEMIC_EXAMINATION:
                    text = clinicalNotes.getSystemExam();
                    break;
                case OBSERVATION:
                    text = clinicalNotes.getObservation();
                    break;
                case INVESTIGATIONS:
                    text = clinicalNotes.getInvestigation();
                    break;
                case PROVISIONAL_DIAGNOSIS:
                    text = clinicalNotes.getProvisionalDiagnosis();
                    break;
                case DIAGNOSIS:
                    text = clinicalNotes.getDiagnosis();
                    break;
                case ECG:
                    text = clinicalNotes.getEcgDetails();
                    break;
                case ECHO:
                    text = clinicalNotes.getEcho();
                    break;
                case XRAY:
                    text = clinicalNotes.getxRayDetails();
                    break;
                case HOLTER:
                    text = clinicalNotes.getHolter();
                    break;
                case NOTES:
                    text = clinicalNotes.getNote();
                    break;
                case INDICATION_OF_USG:
                    text = clinicalNotes.getIndicationOfUSG();
                    break;
                case PA:
                    text = clinicalNotes.getPa();
                    break;
                case PS:
                    text = clinicalNotes.getPs();
                    break;
                case PV:
                    text = clinicalNotes.getPv();
                    break;
            }
            sendBroadcastForClinicalNoteLayoutVisibility(true);
            autotvPermission.setText(Util.getValidatedValue(text));
        }

        switch (clinicalNotesPermissionType) {
            case COMPLAINT:
                viewTopLine.setVisibility(View.VISIBLE);
                break;
            case PAST_HISTORY:
                viewTopLine.setVisibility(View.VISIBLE);
                break;
            case FAMILY_HISTORY:
                viewTopLine.setVisibility(View.VISIBLE);
                break;
        }
        parentPermissionItems.addView(layoutItemPermission);
    }


    private void addEntPermissionItem() {
        LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_add_clinical_note_ent_compalints, null);
        LinearLayout layout = (LinearLayout) layoutSubItemPermission.findViewById(R.id.layout_clinical_notes_field);
        TextView tvItemTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_title_clinical_notes);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        tvItemTitle.setLayoutParams(llp);
        View viewBottomLine = (View) layoutSubItemPermission.findViewById(R.id.view_bottom_line);
        viewBottomLine.setVisibility(View.GONE);
        tvItemTitle.setText(R.string.ent_compaints);

        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PC_NOSE.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPcNose()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.PC_NOSE);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PC_ORAL_CAVITY.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPcOralCavity()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.PC_ORAL_CAVITY);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PC_THROAT.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPcThroat()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.PC_THROAT);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PC_EARS.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPcEars()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.PC_EARS);
            layout.addView(layoutItemPermission);
        }
        parentPermissionItems.addView(layoutSubItemPermission);
    }

    private void addPersonalHistoryPermissionItem() {
        LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_add_clinical_note_ent_compalints, null);
        LinearLayout layout = (LinearLayout) layoutSubItemPermission.findViewById(R.id.layout_clinical_notes_field);
        TextView tvItemTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_title_clinical_notes);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        tvItemTitle.setLayoutParams(llp);
        View viewBottomLine = (View) layoutSubItemPermission.findViewById(R.id.view_bottom_line);
        viewBottomLine.setVisibility(View.GONE);
        tvItemTitle.setText(R.string.personal_history_colon);


        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PERSONAL_HISTORY.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPersonalHistoryTobacco()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.TOBACCO);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PERSONAL_HISTORY.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPersonalHistoryAlcohol()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.ALCOHOL);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PERSONAL_HISTORY.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPersonalHistorySmoking()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.SMOKING);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PERSONAL_HISTORY.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPersonalHistoryDiet()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.DIET);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PERSONAL_HISTORY.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getPersonalHistoryOccupation()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.OCCUPATION);
            layout.addView(layoutItemPermission);
        }
        parentPermissionItems.addView(layoutSubItemPermission);
    }

    private void addGeneralHistoryPermissionItem() {
        LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_add_clinical_note_ent_compalints, null);
        LinearLayout layout = (LinearLayout) layoutSubItemPermission.findViewById(R.id.layout_clinical_notes_field);
        TextView tvItemTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_title_clinical_notes);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        tvItemTitle.setLayoutParams(llp);
        View viewBottomLine = (View) layoutSubItemPermission.findViewById(R.id.view_bottom_line);
        viewBottomLine.setVisibility(View.GONE);
        tvItemTitle.setText(R.string.general_history_colon);


        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.GENERAL_HISTORY.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getGeneralHistoryDrugs()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.DRUGS);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.GENERAL_HISTORY.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getGeneralHistoryAllergies()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.ALLERGIES);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.GENERAL_HISTORY.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getGeneralHistoryMedicine()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.MEDICINE);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.GENERAL_HISTORY.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getGeneralHistorySurgical()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.SURGICAL);
            layout.addView(layoutItemPermission);
        }
        parentPermissionItems.addView(layoutSubItemPermission);
    }

    private void addEntExaminationPermissionItem() {
        LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_add_clinical_note_ent_compalints, null);
        LinearLayout layout = (LinearLayout) layoutSubItemPermission.findViewById(R.id.layout_clinical_notes_field);
        TextView tvItemTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_title_clinical_notes);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        tvItemTitle.setLayoutParams(llp);
        View viewBottomLine = (View) layoutSubItemPermission.findViewById(R.id.view_bottom_line);
        viewBottomLine.setVisibility(View.VISIBLE);
        tvItemTitle.setText(R.string.ent_examinations);

        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.NOSE_EXAM.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getNoseExam()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.NOSE_EXAM);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.ORAL_CAVITY_THROAT_EXAM.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getOralCavityThroatExam()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.ORAL_CAVITY_THROAT_EXAM);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.INDIRECT_LARYGOSCOPY_EXAM.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getIndirectLarygoscopyExam()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.INDIRECT_LARYGOSCOPY_EXAM);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.NECK_EXAM.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getNeckExam()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.NECK_EXAM);
            layout.addView(layoutItemPermission);
        }
        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.EARS_EXAM.getValue())
                || (clinicalNotes != null && !Util.isNullOrBlank(clinicalNotes.getEarsExam()))) {
            LinearLayout layoutItemPermission = addEntSubPermissionItem(ClinicalNotesPermissionType.EARS_EXAM);
            layout.addView(layoutItemPermission);
        }

        parentPermissionItems.addView(layoutSubItemPermission);
    }


    private LinearLayout addEntSubPermissionItem(ClinicalNotesPermissionType clinicalNotesPermissionType) {
        LinearLayout layoutItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_add_myscript_clinical_note_permision, null);
        TextView tvTitle = (TextView) layoutItemPermission.findViewById(R.id.tv_title);
        MyScriptEditText autotvPermission = (MyScriptEditText) layoutItemPermission.findViewById(R.id.edit_permission_text);
//        autotvPermission.setOnFocusChangeListener(this);
        tvTitle.setText(clinicalNotesPermissionType.getTextId());
        autotvPermission.setId(clinicalNotesPermissionType.getAutotvId());
        autotvPermission.setHint(clinicalNotesPermissionType.getHintId());
        autotvPermission.setTag(String.valueOf(clinicalNotesPermissionType));
        if (myScriptAddVisitsFragment != null) {
            autotvPermission.setOnTouchListener(myScriptAddVisitsFragment.getOnTouchListener());
        }
        String text = "";
        if (clinicalNotes != null) {
            switch (clinicalNotesPermissionType) {
                case PC_ORAL_CAVITY:
                    text = clinicalNotes.getPcOralCavity();
                    break;
                case PC_NOSE:
                    text = clinicalNotes.getPcNose();
                    break;
                case PC_EARS:
                    text = clinicalNotes.getPcEars();
                    break;
                case PC_THROAT:
                    text = clinicalNotes.getPcThroat();
                    break;
                case TOBACCO:
                    text = clinicalNotes.getPersonalHistoryTobacco();
                    break;
                case ALCOHOL:
                    text = clinicalNotes.getPersonalHistoryAlcohol();
                    break;
                case SMOKING:
                    text = clinicalNotes.getPersonalHistorySmoking();
                    break;
                case DIET:
                    text = clinicalNotes.getPersonalHistoryDiet();
                    break;
                case OCCUPATION:
                    text = clinicalNotes.getPersonalHistoryOccupation();
                    break;
                case ALLERGIES:
                    text = clinicalNotes.getGeneralHistoryAllergies();
                    break;
                case DRUGS:
                    text = clinicalNotes.getGeneralHistoryDrugs();
                    break;
                case MEDICINE:
                    text = clinicalNotes.getGeneralHistoryMedicine();
                    break;
                case SURGICAL:
                    text = clinicalNotes.getGeneralHistorySurgical();
                    break;
                case INDIRECT_LARYGOSCOPY_EXAM:
                    text = clinicalNotes.getIndirectLarygoscopyExam();
                    break;
                case NOSE_EXAM:
                    text = clinicalNotes.getNoseExam();
                    break;
                case NECK_EXAM:
                    text = clinicalNotes.getNeckExam();
                    break;
                case EARS_EXAM:
                    text = clinicalNotes.getEarsExam();
                    break;
                case ORAL_CAVITY_THROAT_EXAM:
                    text = clinicalNotes.getOralCavityThroatExam();
                    break;
            }
            autotvPermission.setText(Util.getValidatedValue(text));
        }
        return layoutItemPermission;
    }


    public int getValidatedMsgId() {
        String systolic = Util.getValidatedValueOrBlankTrimming(editSystolic);
        String diastolic = Util.getValidatedValueOrBlankTrimming(editDiastolic);
        if ((Util.isNullOrBlank(systolic) && !Util.isNullOrBlank(diastolic) || (!Util.isNullOrBlank(systolic) && Util.isNullOrBlank(diastolic))))
            return R.string.please_add_systolic_and_diastolic_both;
        return 0;
    }

    public boolean isBlankClinicalNote() {
        boolean isBlankClinicalNote = true;
        if (!Util.isNullOrEmptyList(clinicalNotesUiPermissionsList))
            for (String clinicalNotesPermissionType :
                    clinicalNotesUiPermissionsList) {
                MyScriptEditText autoTvPermission = (MyScriptEditText) view.findViewWithTag(clinicalNotesPermissionType);
                if (autoTvPermission != null && !Util.isNullOrBlank(Util.getValidatedValueOrBlankTrimming(autoTvPermission))) {
                    isBlankClinicalNote = false;
                    break;
                }
            }
        if (!Util.isNullOrEmptyList(diagramsList) || !isBlankVitalSigns())
            isBlankClinicalNote = false;
        return isBlankClinicalNote;
    }

    private boolean isBlankVitalSigns() {
        String heartRate = Util.getValidatedValueOrNull(editHeartRate);
        String bodyTemprature = Util.getValidatedValueOrNull(editBodyTemperature);
        String weight = Util.getValidatedValueOrNull(editWeight);
        String systolic = Util.getValidatedValueOrNull(editSystolic);
        String diastolic = Util.getValidatedValueOrNull(editDiastolic);
        String respiratory = Util.getValidatedValueOrNull(editRespRate);
        String spO2 = Util.getValidatedValueOrNull(editSpo2);
        return Util.isNullOrBlank(heartRate)
                && Util.isNullOrBlank(bodyTemprature)
                && Util.isNullOrBlank(weight)
                && (Util.isNullOrBlank(systolic)
                && Util.isNullOrBlank(diastolic))
                && Util.isNullOrBlank(respiratory)
                && Util.isNullOrBlank(spO2);
    }

    public ClinicalNoteToSend getClinicalNoteToSendDetails() {
        ClinicalNoteToSend clinicalNoteToSend = new ClinicalNoteToSend();
        clinicalNoteToSend.setDoctorId(user.getUniqueId());
        clinicalNoteToSend.setLocationId(user.getForeignLocationId());
        clinicalNoteToSend.setHospitalId(user.getForeignHospitalId());
        clinicalNoteToSend.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (myScriptAddVisitsFragment.isClinicalNoteViewVisible()) {
            clinicalNoteToSend.setVitalSigns(getVitalSigns());
            for (String permission :
                    user.getUiPermissions().getClinicalNotesPermissions()) {
                ClinicalNotesPermissionType clinicalNotesPermissionType = ClinicalNotesPermissionType.getClinicalNotesPermissionType(permission);
                if (clinicalNotesPermissionType != null) {
                    switch (clinicalNotesPermissionType) {
                        case PRESENT_COMPLAINT:
                            clinicalNoteToSend.setPresentComplaint(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(PRESENT_COMPLAINT.getValue()))));
                            break;
                        case COMPLAINT:
                            clinicalNoteToSend.setComplaint(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.COMPLAINT.getValue()))));
                            break;
                        case HISTORY_OF_PRESENT_COMPLAINT:
                            clinicalNoteToSend.setPresentComplaintHistory(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT.getValue()))));
                            break;
                        case MENSTRUAL_HISTORY:
                            clinicalNoteToSend.setMenstrualHistory(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.MENSTRUAL_HISTORY.getValue()))));
                            break;
                        case OBSTETRIC_HISTORY:
                            clinicalNoteToSend.setObstetricHistory(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.OBSTETRIC_HISTORY.getValue()))));
                            break;
                        case GENERAL_EXAMINATION:
                            clinicalNoteToSend.setGeneralExam(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.GENERAL_EXAMINATION.getValue()))));
                            break;
                        case SYSTEMIC_EXAMINATION:
                            clinicalNoteToSend.setSystemExam(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION.getValue()))));
                            break;
                        case OBSERVATION:
                            clinicalNoteToSend.setObservation(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.OBSERVATION.getValue()))));
                            break;
                        case INVESTIGATIONS:
                            clinicalNoteToSend.setInvestigation(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.INVESTIGATIONS.getValue()))));
                            break;
                        case PROVISIONAL_DIAGNOSIS:
                            clinicalNoteToSend.setProvisionalDiagnosis(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS.getValue()))));
                            break;
                        case DIAGNOSIS:
                            clinicalNoteToSend.setDiagnosis(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.DIAGNOSIS.getValue()))));
                            break;
                        case ECG:
                            clinicalNoteToSend.setEcgDetails(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.ECG.getValue()))));
                            break;
                        case ECHO:
                            clinicalNoteToSend.setEcho(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.ECHO.getValue()))));
                            break;
                        case XRAY:
                            clinicalNoteToSend.setxRayDetails(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.XRAY.getValue()))));
                            break;
                        case HOLTER:
                            clinicalNoteToSend.setHolter(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.HOLTER.getValue()))));
                            break;
                        case PA:
                            clinicalNoteToSend.setPa(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PA.getValue()))));
                            break;
                        case PV:
                            clinicalNoteToSend.setPv(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PV.getValue()))));
                            break;
                        case PS:
                            clinicalNoteToSend.setPs(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PS.getValue()))));
                            break;
                        case INDICATION_OF_USG:
                            clinicalNoteToSend.setIndicationOfUSG(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.INDICATION_OF_USG.getValue()))));
                            break;
                        case NOTES:
                            clinicalNoteToSend.setNote(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.NOTES.getValue()))));
                            break;
                        case PERSONAL_HISTORY:
                            clinicalNoteToSend.setPersonalHistoryAlcohol(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.ALCOHOL.getValue()))));
                            clinicalNoteToSend.setPersonalHistoryDiet(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.DIET.getValue()))));
                            clinicalNoteToSend.setPersonalHistoryOccupation(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.OCCUPATION.getValue()))));
                            clinicalNoteToSend.setPersonalHistorySmoking(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.SMOKING.getValue()))));
                            clinicalNoteToSend.setPersonalHistoryTobacco(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.TOBACCO.getValue()))));
                            break;
                        case PC_ORAL_CAVITY:
                            clinicalNoteToSend.setPcOralCavity(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PC_ORAL_CAVITY.getValue()))));
                            break;
                        case PC_NOSE:
                            clinicalNoteToSend.setPcNose(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PC_NOSE.getValue()))));
                            break;
                        case PC_EARS:
                            clinicalNoteToSend.setPcEars(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PC_EARS.getValue()))));
                            break;
                        case GENERAL_HISTORY:
                            clinicalNoteToSend.setGeneralHistoryAllergies(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.ALLERGIES.getValue()))));
                            clinicalNoteToSend.setGeneralHistoryDrugs(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.DRUGS.getValue()))));
                            clinicalNoteToSend.setGeneralHistoryMedicine(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.MEDICINE.getValue()))));
                            clinicalNoteToSend.setGeneralHistorySurgical(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.SURGICAL.getValue()))));
                            break;
                        case NOSE_EXAM:
                            clinicalNoteToSend.setNoseExam(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.NOSE_EXAM.getValue()))));
                            break;

                        case NECK_EXAM:
                            clinicalNoteToSend.setNeckExam(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.NECK_EXAM.getValue()))));
                            break;
                        case EARS_EXAM:
                            clinicalNoteToSend.setEarsExam(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.EARS_EXAM.getValue()))));
                            break;
                        case ORAL_CAVITY_THROAT_EXAM:
                            clinicalNoteToSend.setOralCavityThroatExam(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.ORAL_CAVITY_THROAT_EXAM.getValue()))));
                            break;
                        case PROCEDURES:
                            clinicalNoteToSend.setProcedureNote(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PROCEDURES.getValue()))));
                            break;

                        case PAST_HISTORY:
                            clinicalNoteToSend.setPastHistory(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PAST_HISTORY.getValue()))));
                            break;
                        case PC_THROAT:
                            clinicalNoteToSend.setPcThroat(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PC_THROAT.getValue()))));
                            break;
                        case FAMILY_HISTORY:
                            clinicalNoteToSend.setFamilyHistory(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.FAMILY_HISTORY.getValue()))));
                            break;
                        case INDIRECT_LARYGOSCOPY_EXAM:
                            clinicalNoteToSend.setIndirectLarygoscopyExam(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((TextView) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.INDIRECT_LARYGOSCOPY_EXAM.getValue()))));
                            break;

                    }
                }
            }
        }

//        clinicalNotes.setDiagnoses(diagnosesListToSend);
        if (myScriptAddVisitsFragment.isDiagramViewVisible() && !Util.isNullOrEmptyList(diagramsList)) {
            ArrayList<String> diagramIdsList = new ArrayList<String>();
            diagramIdsList.addAll(diagramsList.keySet());
            clinicalNoteToSend.setDiagrams(diagramIdsList);
        }
        if (!Util.isNullOrBlank(clinicalNoteId))
            clinicalNoteToSend.setUniqueId(clinicalNoteId);
        return clinicalNoteToSend;
    }

    private String getFormattedText(String text) {
        if (!Util.isNullOrBlank(text)) {
            if (text.endsWith(CHARACTER_TO_REPLACE_COMMA_WITH_SPACES))
                text = text.replaceAll(CHARACTER_TO_REPLACE_COMMA_WITH_SPACES + "$", "");
//                text = text.replace(CHARACTER_TO_REPLACE_COMMA_WITH_SPACES, "");
            if (text.endsWith(CHARACTER_TO_BE_REPLACED))
                text = text.replaceAll(CHARACTER_TO_BE_REPLACED + "$", "");
//                text = text.replace(CHARACTER_TO_BE_REPLACED, "");
        }
        if (!Util.isNullOrBlank(text))
            text = text.trim();
        return text;
    }

    private VitalSigns getVitalSigns() {
        VitalSigns vitalSigns = new VitalSigns();
        vitalSigns.setPulse(Util.getValidatedValueOrNull(editHeartRate));
        vitalSigns.setTemperature(Util.getValidatedValueOrNull(editBodyTemperature));
        vitalSigns.setWeight(Util.getValidatedValueOrNull(editWeight));
        String systolic = Util.getValidatedValueOrNull(editSystolic);
        String diastolic = Util.getValidatedValueOrNull(editDiastolic);
        if (!Util.isNullOrBlank(systolic) && !Util.isNullOrBlank(diastolic))
            vitalSigns.setBloodPressure(vitalSigns.getBloodPressure(systolic, diastolic));
        vitalSigns.setBreathing(Util.getValidatedValueOrNull(editRespRate));
        vitalSigns.setSpo2(Util.getValidatedValueOrNull(editSpo2));
        return vitalSigns;
    }

    /**
     * Gets text before last occurance of CHARACTER_TO_BE_REPLACED in searchedTerm
     *
     * @param searchTerm
     * @return : text Before  last occurance of CHARACTER_TO_BE_REPLACED
     */
    public String getTextBeforeLastOccuranceOfCharacter(String searchTerm) {
        Pattern p = Pattern.compile("\\s*(.*)" + CHARACTER_TO_BE_REPLACED + ".*");
        Matcher m = p.matcher(searchTerm.trim());

        if (m.find())
            return m.group(1);
        return "";
    }

    private void showConfirmationAlert(String title, String msg, final View parentView, final String diagramUniqueId) {
        if (Util.isNullOrBlank(title))
            title = mActivity.getResources().getString(R.string.confirm);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                containerDiagrams.removeView(parentView);
                diagramsList.remove(diagramUniqueId);
                if (Util.isNullOrEmptyList(diagramsList) && containerDiagrams.getChildCount() <= 0)
                    Util.sendBroadcast(mApp, MyScriptAddVisitsFragment.INTENT_DIAGRAM_LAYOUT_VISIBILITY);

            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    public HashMap<?, ?> getDiagramsList() {
        return diagramsList;
    }

    private void addDiagramInContainer(String diagramTag, String diagramUniqueId, Object object) {
        RelativeLayout frameLayout = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.diagram_add_visits_item, null);
        ImageView ivDiagram = (ImageView) frameLayout.findViewById(R.id.iv_diagram);
        ImageButton btDelete = (ImageButton) frameLayout.findViewById(R.id.bt_delete);
        TextView tvtag = (TextView) frameLayout.findViewById(R.id.tv_tag);
        btDelete.setOnClickListener(this);
        Bitmap bitmap = null;
        if (object instanceof byte[]) {
            byte[] byteArray = (byte[]) object;
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            if (bitmap != null)
                ivDiagram.setImageBitmap(bitmap);
            else
                return;
        } else if (object instanceof String) {
            String imageUrl = (String) object;
            DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivDiagram, imageUrl);
        }
        tvtag.setText(diagramTag);
        frameLayout.setTag(diagramUniqueId);
        diagramsList.put(diagramUniqueId, diagramUniqueId);
        containerDiagrams.addView(frameLayout);
//        svScrollView.fullScroll(ScrollView.FOCUS_UP);
        sendBroadcastForDiagramLayoutVisibility(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_CLINICAL_NOTES) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_DIAGRAM_DETAIL && data != null) {
                String diagramUniqueId = data.getStringExtra(HealthCocoConstants.TAG_UNIQUE_ID);
                String diagramTag = data.getStringExtra(SelectedDiagramDetailFragment.SELECTED_DIAGRAM_TAG);
                if (!Util.isNullOrBlank(diagramUniqueId) && ImageUtil.DIAGRAM_SELECTED_BYTE_ARRAY != null && ImageUtil.DIAGRAM_SELECTED_BYTE_ARRAY.length > 0) {
                    addDiagramInContainer(diagramTag, diagramUniqueId, ImageUtil.DIAGRAM_SELECTED_BYTE_ARRAY);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete:
                View parentView = (View) v.getParent();
                String diagramUniqueId = (String) parentView.getTag();
                if (parentView != null && !Util.isNullOrBlank(diagramUniqueId)) {
                    showConfirmationAlert(null, getResources().getString(R.string.confirm_delete_diagram), parentView, diagramUniqueId);
                }
                break;
        }
    }

    public void refreshData(ClinicalNotes clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
        initUiPermissions(user.getUiPermissions().getClinicalNotesPermissionsString());
        prePopulateDiagrams();
    }

    private void prePopulateDiagrams() {
        if (clinicalNotes != null) {
            //initialising diagrams in container
            containerDiagrams.removeAllViews();
            if (!Util.isNullOrEmptyList(clinicalNotes.getDiagrams())) {
                sendBroadcastForDiagramLayoutVisibility(true);
                containerDiagrams.setVisibility(View.VISIBLE);
                for (Diagram diagram :
                        clinicalNotes.getDiagrams()) {
                    if (!Util.isNullOrBlank(diagram.getUniqueId()) && !Util.isNullOrBlank(diagram.getDiagramUrl())) {
                        addDiagramInContainer(diagram.getTags(), diagram.getUniqueId(), diagram.getDiagramUrl());
                    }
                }
            } else
                sendBroadcastForDiagramLayoutVisibility(false);

            if (!isFromClone)
                clinicalNoteId = clinicalNotes.getUniqueId();
        }
    }

    public void openDiagramsListFragment() {
        openCommonOpenUpVisitActivity(CommonOpenUpFragmentType.SELECT_DIAGRAM, REQUEST_CODE_ADD_CLINICAL_NOTES);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId())
                            && !Util.isNullOrBlank(user.getForeignHospitalId())
                            && !Util.isNullOrBlank(user.getForeignLocationId()) && user.getUiPermissions() != null) {
//                        if (isNullClinicalNotePermissionsList) {
//                            sendBroadcastForDiagramButtonVisibility(false);
//                            sendBroadcastForClinicalNoteLayoutVisibility(false);
//                        } else {
//
//                        }

                        initUiPermissions(user.getUiPermissions().getClinicalNotesPermissionsString());
                    } else {
                        mActivity.hideLoading();
                        mActivity.finish();
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                }
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }
}
