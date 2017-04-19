package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.MyCertificate;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.AddVisitsActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.ClinicalNoteToSend;
import com.healthcoco.healthcocopad.bean.server.AssignedUserUiPermissions;
import com.healthcoco.healthcocopad.bean.server.BloodPressure;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.Duration;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.bean.server.VitalSigns;
import com.healthcoco.healthcocopad.custom.HealthcocoOnSelectionChanged;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.custom.MyScriptEditText;
import com.healthcoco.healthcocopad.enums.ClinicalNotesPermissionType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.PrescriptionPermissionType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.VisitsUiType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoOnSelectionChangedListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;
import com.myscript.atk.core.CaptureInfo;
import com.myscript.atk.sltw.SingleLineWidget;
import com.myscript.atk.sltw.SingleLineWidgetApi;
import com.myscript.atk.text.CandidateInfo;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.healthcoco.healthcocopad.enums.ClinicalNotesPermissionType.PRESENT_COMPLAINT;
import static com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA;
import static com.healthcoco.healthcocopad.fragments.AddVisitSuggestionsFragment.TAG_SUGGESTIONS_TYPE;

/**
 * Created by Shreshtha on 05-04-2017.
 */
public class AddVisitsFragment extends HealthCocoFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised,
        HealthcocoOnSelectionChangedListener, View.OnTouchListener, View.OnFocusChangeListener {
    public static final String INTENT_ON_SUGGESTION_ITEM_CLICK = "com.healthcoco.healthcocopad.fragments.AddVisitsFragment.ON_SUGGESTION_ITEM_CLICK";
    public static final String INTENT_ADIVCE_BUTTON_VISIBILITY = "com.healthcoco.healthcocopad.fragments.AddVisitsFragment.ADIVCE_BUTTON_VISIBILITY";
    public static final String INTENT_LAB_TEST_VISIBILITY = "com.healthcoco.healthcocopad.fragments.AddVisitsFragment.LAB_TEST_VISIBILITY";

    public static final String TAG_SELECTED_SUGGESTION_OBJECT = "selectedSuggestionObject";
    public static final String TAG_VISIBILITY = "visibility";
    private static final String CHARACTER_TO_REPLACE_COMMA_WITH_SPACES = " , ";
    private static final String CHARACTER_TO_BE_REPLACED = ",";
    private static final int REQUEST_CODE_ADD_CLINICAL_NOTES = 100;
    private static final float WIDGET_AUTOSCROLL_MARGIN = 150;
    private static final float WIDGET_TEXT_SIZE = 30;
    private static final float WIDGET_INT_WIDTH = 4;
    private static final int WIDGET_TEXT_COLOR = 0xFF0077b5;


    private TextViewFontAwesome btClose;
    private ImageButton btClinicalNote;
    private ImageButton btPrescription;
    private ImageButton btLabTests;
    private ImageButton btDiagrams;
    private ImageButton btAdvice;
    private TextViewFontAwesome btSave;
    private TextView tvHeaderText;
    private LinearLayout parentClinicalNote;
    private LinearLayout parentPrescription;
    private LinearLayout parentDiagnosticTests;
    private LinearLayout parentDiagrams;
    private LinearLayout layoutParentAdvice;
    private TextView tvPatientName;
    private TextView tvDate;
    private TextView tvPatientId;
    private TextView tvRxId;
    private TextView tvMobileNo;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private HashMap<String, String> diagramsList = new HashMap<>();
    private ScrollView svScrollView;
    private LinearLayout containerDiagrams;
    private LinearLayout parentVitalSigns;
    private LinearLayout parentPermissionItems;
    private ClinicalNotes clinicalNotes;
    private SingleLineWidget mWidget;
    private int isCorrectionMode;
    private TextView tvHeartRate;
    private TextView tvBodyTemprature;
    private TextView tvWeight;
    private TextView tvRespiratoryRate;
    private TextView tvSpO2;
    private TextView tvBloodPressure;
    private MyScriptEditText etAdvice;

    private LinearLayout layoutWidget;
    private ImageButton btKeyboard;
    private ImageButton btDel;
    private ImageButton btSpace;
    private ImageButton btEnter;
    private TextView tvCandidateOne;
    private TextView tvCandidateTwo;
    private TextView tvCandidateThree;
    private ArrayList<VisitsUiType> visibleViews = new ArrayList<>();

    //vitalsigns editTexts
    private MyScriptEditText editSystolic;
    private MyScriptEditText editDiastolic;
    private MyScriptEditText editBodyTemperature;
    private MyScriptEditText editRespRate;
    private MyScriptEditText editSpo2;
    private MyScriptEditText editWeight;
    private MyScriptEditText editHeartRate;
    private String visitId;
    //shift this to addClinicNoteFragment after seperate implementation of addCLinicalNotes
    private String clinicalNoteId;
    private LinearLayout mCandidateBar;
    private TextViewFontAwesome tvPreviousArrow;
    private TextViewFontAwesome tvNextArrow;
    private LinearLayout containerSuggestionsList;
    private boolean receiversRegistered;
    private View selectedViewForSuggestionsList;
    private LinearLayout parentWidgetSuggestions;
    private boolean isOnItemClick;

    private AddPrescriptionVisitFragment addPrescriptionVisitFragment;
    private AddVisitSuggestionsFragment addVisitSuggestionsFragment;
    SuggestionType selectedSuggestionType = null;
    private AddLabTestsVisitFragment addLabTestVisitFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_visits_info, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(mActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        validateCertificate();
        initListeners();
        initFragments();
    }

    private void initFragments() {
        initPrescriptionFragment();
        initSuggestionsFragment();
        initLabTestsFragment();
    }

    private void initLabTestsFragment() {
        addLabTestVisitFragment = new AddLabTestsVisitFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.parent_diagnostic_tests, addLabTestVisitFragment, addLabTestVisitFragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void initPrescriptionFragment() {
        addPrescriptionVisitFragment = new AddPrescriptionVisitFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.parent_prescription, addPrescriptionVisitFragment, addPrescriptionVisitFragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void initSuggestionsFragment() {
        addVisitSuggestionsFragment = new AddVisitSuggestionsFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.container_suggestions_list, addVisitSuggestionsFragment, addVisitSuggestionsFragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void validateCertificate() {
        if (!mWidget.registerCertificate(MyCertificate.getBytes())) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mActivity);
            dlgAlert.setMessage("Please use a valid certificate.");
            dlgAlert.setTitle("Invalid certificate");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss the dialog
                }
            });
            dlgAlert.create().show();
            return;
        }
    }

    @Override
    public void initViews() {
        svScrollView = (ScrollView) view.findViewById(R.id.sv_scrollview);
        containerSuggestionsList = (LinearLayout) view.findViewById(R.id.container_suggestions_list);

        //initialising parent Widget SUggestions dimensions
        parentWidgetSuggestions = (LinearLayout) view.findViewById(R.id.parent_widget_suggestions);
        containerSuggestionsList.setVisibility(View.VISIBLE);
        parentPrescription = (LinearLayout) view.findViewById(R.id.parent_prescription);
        parentPrescription.setVisibility(View.GONE);

        initWidgetViews();
        initToolbarView();
        initHeaderView();
        initClinicalNotesView();
        initDiagonsticTestsView();
        initDiagramsViews();
        initAdviceViews();
        setHeightOfWidgetsAndSuggestions();
    }

    private void setHeightOfWidgetsAndSuggestions() {
        ViewGroup.LayoutParams layoutParamsSuggestionsList = containerSuggestionsList.getLayoutParams();
        layoutParamsSuggestionsList.height = (int) (ScreenDimensions.SCREEN_WIDTH * 0.30);
        containerSuggestionsList.setLayoutParams(layoutParamsSuggestionsList);

        ViewGroup.LayoutParams layoutParamsWidget = layoutWidget.getLayoutParams();
        layoutParamsWidget.height = (int) (ScreenDimensions.SCREEN_WIDTH * 0.30);
        layoutWidget.setLayoutParams(layoutParamsWidget);
    }

    private void initWidgetViews() {
        mWidget = (SingleLineWidget) view.findViewById(R.id.widget);
        layoutWidget = (LinearLayout) view.findViewById(R.id.layout_widget);
        btKeyboard = (ImageButton) view.findViewById(R.id.bt_keyboard);
        btDel = (ImageButton) view.findViewById(R.id.bt_del);
        btSpace = (ImageButton) view.findViewById(R.id.bt_space);
        btEnter = (ImageButton) view.findViewById(R.id.bt_enter);
        mCandidateBar = (LinearLayout) view.findViewById(R.id.candidateBar);
        tvCandidateOne = (TextView) view.findViewById(R.id.tv_candidate_one);
        tvCandidateTwo = (TextView) view.findViewById(R.id.tv_candidate_two);
        tvCandidateThree = (TextView) view.findViewById(R.id.tv_candidate_three);
        tvPreviousArrow = (TextViewFontAwesome) view.findViewById(R.id.tv_previous_arrow);
        tvNextArrow = (TextViewFontAwesome) view.findViewById(R.id.tv_next_arrow);
    }

    private void setViewToWidget() {
        mWidget.setBaselinePosition(getResources().getDimension(R.dimen.baseline_position));
        mWidget.setScrollbarResource(R.drawable.sltw_scrollbar_xml);
        mWidget.setScrollbarMaskResource(R.drawable.sltw_scrollbar_mask);
        mWidget.setScrollbarBackgroundResource(R.drawable.sltw_scrollbar_background);
        mWidget.setLeftScrollArrowResource(R.drawable.sltw_arrowleft_xml);
        mWidget.setRightScrollArrowResource(R.drawable.sltw_arrowright_xml);
        mWidget.setCursorResource(R.drawable.sltw_text_cursor_holo_light);
        mWidget.addSearchDir("zip://" + mActivity.getPackageCodePath() + "!/assets/conf");
        mWidget.setAutoScrollMargin(WIDGET_AUTOSCROLL_MARGIN);
        mWidget.setTextSize(WIDGET_TEXT_SIZE);
        mWidget.setInkWidth(WIDGET_INT_WIDTH);
        mWidget.setTextColor(WIDGET_TEXT_COLOR);
        mWidget.setInkColor(WIDGET_TEXT_COLOR);
        mWidget.configure("en_US", "cur_text");
        isCorrectionMode = 0;
    }

    private void initToolbarView() {
        btClose = (TextViewFontAwesome) view.findViewById(R.id.bt_close);
        btClinicalNote = (ImageButton) view.findViewById(R.id.bt_clinical_note);
        btPrescription = (ImageButton) view.findViewById(R.id.bt_prescription);
        btPrescription.setTag(SuggestionType.DRUGS);
        btLabTests = (ImageButton) view.findViewById(R.id.bt_lab_tests);
        btLabTests.setTag(SuggestionType.LAB_TESTS);
        btDiagrams = (ImageButton) view.findViewById(R.id.bt_diagrams);
        btAdvice = (ImageButton) view.findViewById(R.id.bt_advice);
        btSave = (TextViewFontAwesome) view.findViewById(R.id.bt_save);
    }

    private void initHeaderView() {
        tvHeaderText = (TextView) view.findViewById(R.id.tv_header_text);
        tvPatientName = (TextView) view.findViewById(R.id.tv_patient_name);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvPatientId = (TextView) view.findViewById(R.id.tv_patient_id);
        tvRxId = (TextView) view.findViewById(R.id.tv_rx_id);
        tvMobileNo = (TextView) view.findViewById(R.id.tv_mobile_no);
    }

    private void initClinicalNotesView() {
        parentClinicalNote = (LinearLayout) view.findViewById(R.id.parent_clinical_note);
        parentVitalSigns = (LinearLayout) view.findViewById(R.id.parent_add_vital_signs);
        parentPermissionItems = (LinearLayout) view.findViewById(R.id.parent_permissions_items);

        tvHeartRate = (TextView) view.findViewById(R.id.tv_heart_rate);
        tvBloodPressure = (TextView) view.findViewById(R.id.tv_blood_pressure);
        tvBodyTemprature = (TextView) view.findViewById(R.id.tv_body_temprature);
        tvWeight = (TextView) view.findViewById(R.id.tv_weight);
        tvRespiratoryRate = (TextView) view.findViewById(R.id.tv_respiratory_rate);
        tvSpO2 = (TextView) view.findViewById(R.id.tv_spo2);

        editSystolic = (MyScriptEditText) view.findViewById(R.id.edit_systolic);
        editDiastolic = (MyScriptEditText) view.findViewById(R.id.edit_diastolic);
        editBodyTemperature = (MyScriptEditText) view.findViewById(R.id.edit_body_temperature);
        editRespRate = (MyScriptEditText) view.findViewById(R.id.edit_resp_rate);
        editHeartRate = (MyScriptEditText) view.findViewById(R.id.edit_heart_rate);
        editSpo2 = (MyScriptEditText) view.findViewById(R.id.edit_spo2);
        editWeight = (MyScriptEditText) view.findViewById(R.id.edit_weight);

        //default UI ettings
        parentClinicalNote.setVisibility(View.GONE);
        parentVitalSigns.setVisibility(View.GONE);
    }


    private void initDiagonsticTestsView() {
        parentDiagnosticTests = (LinearLayout) view.findViewById(R.id.parent_diagnostic_tests);
        parentDiagnosticTests.setVisibility(View.GONE);
    }

    private void initDiagramsViews() {
        parentDiagrams = (LinearLayout) view.findViewById(R.id.parent_diagrams);
        containerDiagrams = (LinearLayout) view.findViewById(R.id.container_diagrams);
        parentDiagrams.setVisibility(View.GONE);
    }

    private void initAdviceViews() {
        layoutParentAdvice = (LinearLayout) view.findViewById(R.id.layout_parent_advice);
        etAdvice = (MyScriptEditText) view.findViewById(R.id.et_advice);
        layoutParentAdvice.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        btClose.setOnClickListener(this);
        btClinicalNote.setOnClickListener(this);
        btPrescription.setOnClickListener(this);
        btLabTests.setOnClickListener(this);
        btDiagrams.setOnClickListener(this);
        btAdvice.setOnClickListener(this);
        btSave.setOnClickListener(this);
        btKeyboard.setOnClickListener(this);
        btDel.setOnClickListener(this);
        btSpace.setOnClickListener(this);
        tvCandidateOne.setOnClickListener(this);
        tvCandidateTwo.setOnClickListener(this);
        tvCandidateThree.setOnClickListener(this);
        btEnter.setOnClickListener(this);
        tvPreviousArrow.setOnClickListener(this);
        tvNextArrow.setOnClickListener(this);
        etAdvice.setOnTouchListener(this);
        editBodyTemperature.setOnTouchListener(this);
        editWeight.setOnTouchListener(this);
        editHeartRate.setOnTouchListener(this);
        editRespRate.setOnTouchListener(this);
        editSpo2.setOnTouchListener(this);
        editDiastolic.setOnTouchListener(this);
        editSystolic.setOnTouchListener(this);
        btPrescription.setOnFocusChangeListener(this);
        setViewToWidget();
    }

    public void initEditTextForWidget(EditText editText) {
        initEditTextForWidget(editText, this);
    }

    private void initEditTextForWidget(EditText editText, HealthcocoOnSelectionChangedListener onSelectionChangedListener) {
        HealthcocoOnSelectionChanged onSelectionChangeListener = null;
        if (onSelectionChangedListener != null)
            onSelectionChangeListener = new HealthcocoOnSelectionChanged(editText, onSelectionChangedListener);
        switch (editText.getId()) {
            case R.id.edit_body_temperature:
            case R.id.edit_weight:
            case R.id.edit_heart_rate:
            case R.id.edit_systolic:
            case R.id.edit_diastolic:
            case R.id.edit_resp_rate:
            case R.id.edit_spo2:
                mWidget.configure("en_US", "cur_number");
                break;
            default:
                mWidget.configure("en_US", "cur_text");
                break;
        }
        mWidget.setText(editText.getText().toString());
        mWidget.setOnConfiguredListener(onSelectionChangeListener);
        mWidget.setOnTextChangedListener(onSelectionChangeListener);
        mWidget.setOnReturnGestureListener(onSelectionChangeListener);
        mWidget.setOnEraseGestureListener(onSelectionChangeListener);
        mWidget.setOnSelectGestureListener(onSelectionChangeListener);
        mWidget.setOnUnderlineGestureListener(onSelectionChangeListener);
        mWidget.setOnInsertGestureListener(onSelectionChangeListener);
        mWidget.setOnJoinGestureListener(onSelectionChangeListener);
        mWidget.setOnOverwriteGestureListener(onSelectionChangeListener);
        mWidget.setOnSingleTapGestureListener(onSelectionChangeListener);
        mWidget.setOnLongPressGestureListener(onSelectionChangeListener);
        mWidget.setOnUserScrollBeginListener(onSelectionChangeListener);
        mWidget.setOnUserScrollEndListener(onSelectionChangeListener);
        mWidget.setOnUserScrollListener(onSelectionChangeListener);
        mWidget.setOnPenUpListener(onSelectionChangeListener);
        if (editText instanceof MyScriptEditText)
            ((MyScriptEditText) editText).setOnSelectionChangedListener(onSelectionChangeListener);
    }

    private void initData() {
        tvPatientName.setText(Util.getValidatedValue(selectedPatient.getFirstName()));
        tvPatientId.setText(Util.getValidatedValue(selectedPatient.getPid()));
        tvMobileNo.setText(Util.getValidatedValue(selectedPatient.getMobileNumber()));
        tvDate.setText(Util.getValidatedValue(DateTimeUtil.getCurrentDate()));
//        tvRxId.setText(Util.getValidatedValueOrDash(mActivity, selectedPatient.getP()));
        initUiPermissions(user.getUiPermissions());
    }

    private void initVitalSigns(VitalSigns selectedVitalSigns) {
        tvHeartRate.setText(R.string.no_text_dash);
        tvBloodPressure.setText(R.string.no_text_dash);
        tvBodyTemprature.setText(R.string.no_text_dash);
        tvWeight.setText(R.string.no_text_dash);
        tvRespiratoryRate.setText(R.string.no_text_dash);
        tvSpO2.setText(R.string.no_text_dash);

        if (selectedVitalSigns != null) {
            if (!Util.isNullOrBlank(selectedVitalSigns.getPulse()))
                tvHeartRate.setText(selectedVitalSigns.getFormattedPulse(mActivity, selectedVitalSigns.getPulse()));
            if (!Util.isNullOrBlank(selectedVitalSigns.getTemperature()))
                tvBodyTemprature.setText(selectedVitalSigns.getFormattedTemprature(mActivity, selectedVitalSigns.getTemperature()));
            if (!Util.isNullOrBlank(selectedVitalSigns.getWeight()))
                tvWeight.setText(selectedVitalSigns.getFormattedWeight(mActivity, selectedVitalSigns.getWeight()));
            BloodPressure bloodPressure = selectedVitalSigns.getBloodPressure();
            if (bloodPressure != null && !Util.isNullOrBlank(bloodPressure.getSystolic()) && !Util.isNullOrBlank(bloodPressure.getDiastolic())) {
                tvBloodPressure.setText(selectedVitalSigns.getFormattedBloodPressureValue(mActivity, bloodPressure));
            }
            if (!Util.isNullOrBlank(selectedVitalSigns.getBreathing()))
                tvRespiratoryRate.setText(selectedVitalSigns.getFormattedBreathing(mActivity, selectedVitalSigns.getBreathing()));
            if (!Util.isNullOrBlank(selectedVitalSigns.getSpo2()))
                tvSpO2.setText(selectedVitalSigns.getFormattedSpo2(mActivity, selectedVitalSigns.getSpo2()));
        }
    }

    private void initUiPermissions(AssignedUserUiPermissions uiPermissions) {
        if (uiPermissions != null) {
            Gson gson = new Gson();
            //initialising clinical notes UI permissions
            boolean isNullClinicalNotePermissionsList = true;
            if (!Util.isNullOrBlank(uiPermissions.getClinicalNotesPermissionsString())) {
                List<ClinicalNotesPermissionType> clinicalNotesUiPermissionsList = gson.fromJson(uiPermissions.getClinicalNotesPermissionsString(), new TypeToken<ArrayList<ClinicalNotesPermissionType>>() {
                }.getType());
                clinicalNotesUiPermissionsList.removeAll(Collections.singleton(null));
                if (!Util.isNullOrEmptyList(clinicalNotesUiPermissionsList)) {
                    isNullClinicalNotePermissionsList = false;
                    parentPermissionItems.removeAllViews();
                    if (!Util.isNullOrEmptyList(clinicalNotesUiPermissionsList)) {
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.VITAL_SIGNS)) {
                            parentVitalSigns.setVisibility(View.VISIBLE);
                            if (clinicalNotes != null)
                                initVitalSigns(clinicalNotes.getVitalSigns());
                        }
                        if (clinicalNotesUiPermissionsList.contains(PRESENT_COMPLAINT))
                            addPermissionItem(PRESENT_COMPLAINT);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.COMPLAINT))
                            addPermissionItem(ClinicalNotesPermissionType.COMPLAINT);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT))
                            addPermissionItem(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.MENSTRUAL_HISTORY))
                            addPermissionItem(ClinicalNotesPermissionType.MENSTRUAL_HISTORY);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.OBSTETRIC_HISTORY))
                            addPermissionItem(ClinicalNotesPermissionType.OBSTETRIC_HISTORY);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.GENERAL_EXAMINATION))
                            addPermissionItem(ClinicalNotesPermissionType.GENERAL_EXAMINATION);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION))
                            addPermissionItem(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.OBSERVATION))
                            addPermissionItem(ClinicalNotesPermissionType.OBSERVATION);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.INVESTIGATIONS))
                            addPermissionItem(ClinicalNotesPermissionType.INVESTIGATIONS);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS))
                            addPermissionItem(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.DIAGNOSIS))
                            addPermissionItem(ClinicalNotesPermissionType.DIAGNOSIS);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.ECG))
                            addPermissionItem(ClinicalNotesPermissionType.ECG);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.ECHO))
                            addPermissionItem(ClinicalNotesPermissionType.ECHO);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.XRAY))
                            addPermissionItem(ClinicalNotesPermissionType.XRAY);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.HOLTER))
                            addPermissionItem(ClinicalNotesPermissionType.HOLTER);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.NOTES))
                            addPermissionItem(ClinicalNotesPermissionType.NOTES);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.INDICATION_OF_USG))
                            addPermissionItem(ClinicalNotesPermissionType.INDICATION_OF_USG);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PA))
                            addPermissionItem(ClinicalNotesPermissionType.PA);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PS))
                            addPermissionItem(ClinicalNotesPermissionType.PS);
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PV))
                            addPermissionItem(ClinicalNotesPermissionType.PV);
                        parentPermissionItems.removeAllViews();
                        if (!Util.isNullOrEmptyList(clinicalNotesUiPermissionsList)) {
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.VITAL_SIGNS)) {
                                parentVitalSigns.setVisibility(View.VISIBLE);
                                if (clinicalNotes != null)
                                    initVitalSigns(clinicalNotes.getVitalSigns());
                            }
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PRESENT_COMPLAINT))
                                addPermissionItem(PRESENT_COMPLAINT);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.COMPLAINT))
                                addPermissionItem(ClinicalNotesPermissionType.COMPLAINT);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT))
                                addPermissionItem(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.MENSTRUAL_HISTORY))
                                addPermissionItem(ClinicalNotesPermissionType.MENSTRUAL_HISTORY);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.OBSTETRIC_HISTORY))
                                addPermissionItem(ClinicalNotesPermissionType.OBSTETRIC_HISTORY);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.GENERAL_EXAMINATION))
                                addPermissionItem(ClinicalNotesPermissionType.GENERAL_EXAMINATION);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION))
                                addPermissionItem(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.OBSERVATION))
                                addPermissionItem(ClinicalNotesPermissionType.OBSERVATION);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.INVESTIGATIONS))
                                addPermissionItem(ClinicalNotesPermissionType.INVESTIGATIONS);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS))
                                addPermissionItem(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.DIAGNOSIS))
                                addPermissionItem(ClinicalNotesPermissionType.DIAGNOSIS);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.ECG))
                                addPermissionItem(ClinicalNotesPermissionType.ECG);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.ECHO))
                                addPermissionItem(ClinicalNotesPermissionType.ECHO);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.XRAY))
                                addPermissionItem(ClinicalNotesPermissionType.XRAY);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.HOLTER))
                                addPermissionItem(ClinicalNotesPermissionType.HOLTER);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.NOTES))
                                addPermissionItem(ClinicalNotesPermissionType.NOTES);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.INDICATION_OF_USG))
                                addPermissionItem(ClinicalNotesPermissionType.INDICATION_OF_USG);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PA))
                                addPermissionItem(ClinicalNotesPermissionType.PA);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PS))
                                addPermissionItem(ClinicalNotesPermissionType.PS);
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.PV))
                                addPermissionItem(ClinicalNotesPermissionType.PV);

                            //initialising diagrams view visibility
                            if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.DIAGRAM))
                                btDiagrams.setVisibility(View.VISIBLE);
                            else
                                btDiagrams.setVisibility(View.GONE);
                        }
                    }
                    if (isNullClinicalNotePermissionsList) {
                        btDiagrams.setVisibility(View.GONE);
                    } else {
                        if (clinicalNotesUiPermissionsList.contains(ClinicalNotesPermissionType.DIAGRAM) && clinicalNotesUiPermissionsList.size() == 1)
                            btClinicalNote.setVisibility(View.GONE);
                        else
                            btClinicalNote.setVisibility(View.VISIBLE);
                    }
                }
                //initialising Prescription UI permissions
                ArrayList<String> prescriptionsPermissions = user.getUiPermissions().getPrescriptionPermissions();
                for (PrescriptionPermissionType permissionsType : PrescriptionPermissionType.values()) {
                    if (prescriptionsPermissions.contains(String.valueOf(permissionsType))) {
                        setPrescriptionUiPermissionVisibility(permissionsType, View.VISIBLE);
                    } else {
                        setPrescriptionUiPermissionVisibility(permissionsType, View.GONE);
                    }
                }
            }
        }
    }

    private void setPrescriptionUiPermissionVisibility(PrescriptionPermissionType permissionsType, int visibility) {
        switch (permissionsType) {
            case ADVICE:
                btAdvice.setVisibility(visibility);
                break;
            case LAB:
                btLabTests.setVisibility(visibility);
                break;
            case GENERIC_DRUGS:
                break;
            case MYDRUGS:
                break;
        }
    }

    private void addPermissionItem(ClinicalNotesPermissionType clinicalNotesPermissionType) {
        LinearLayout layoutItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_add_clinical_note_permision, null);
        TextView tvTitle = (TextView) layoutItemPermission.findViewById(R.id.tv_title);
        MyScriptEditText autotvPermission = (MyScriptEditText) layoutItemPermission.findViewById(R.id.edit_permission_text);
        autotvPermission.setOnFocusChangeListener(this);
        tvTitle.setText(clinicalNotesPermissionType.getTextId());
        autotvPermission.setId(clinicalNotesPermissionType.getAutotvId());
        autotvPermission.setHint(clinicalNotesPermissionType.getHintId());
        autotvPermission.setTag(String.valueOf(clinicalNotesPermissionType));
        autotvPermission.setOnTouchListener(this);
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
            autotvPermission.setText(text);
        }
        parentPermissionItems.addView(layoutItemPermission);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_close:
                mActivity.onBackPressed();
                break;
            case R.id.bt_clinical_note:
                hideKeyboardOrWidgetIfVisible();
                showHideClinicalNotesLayout();
                break;
            case R.id.bt_prescription:
                if (selectedSuggestionType == null || selectedSuggestionType != SuggestionType.DRUGS) {
                    addVisitSuggestionsFragment.refreshTagOfEditText(SuggestionType.DRUGS);
                }
//                svScrollView.requestChildFocus(parentPrescription, parentPrescription);
                break;
            case R.id.bt_lab_tests:
                if (selectedSuggestionType == null || selectedSuggestionType != SuggestionType.LAB_TESTS) {
                    addVisitSuggestionsFragment.refreshTagOfEditText(SuggestionType.LAB_TESTS);
                }
                break;
            case R.id.bt_diagrams:
                openActivity(CommonOpenUpFragmentType.SELECT_DIAGRAM, null, REQUEST_CODE_ADD_CLINICAL_NOTES);
                break;
            case R.id.bt_advice:
                showHideAdviceLayout();
                break;
            case R.id.bt_delete:
                View parentView = (View) v.getParent();
                String diagramUniqueId = (String) parentView.getTag();
                if (parentView != null && !Util.isNullOrBlank(diagramUniqueId)) {
                    showConfirmationAlert(null, getResources().getString(R.string.confirm_delete_diagram), parentView, diagramUniqueId);
                }
                break;
            case R.id.bt_keyboard:
//                showHideWidgetAndKeyboardLayout();
                break;
            case R.id.bt_del:
                onDeleteButtonClick();
                break;
            case R.id.bt_space:
                onSpaceButtonClick();
                break;
            case R.id.bt_enter:
                onEnterClick();
                break;
            case R.id.tv_candidate_one:
            case R.id.tv_candidate_two:
            case R.id.tv_candidate_three:
                onCandidateButtonClick(v);
                break;
            case R.id.tv_previous_arrow:
                int upId = mActivity.getCurrentFocus().getNextFocusUpId();
                if (upId != View.NO_ID) {
                    view.findViewById(upId).requestFocus();
                }
                System.out.println("Back");
                initEditTextForWidget((MyScriptEditText) mActivity.getCurrentFocus(), this);
                break;
            case R.id.tv_next_arrow:
                int downId = mActivity.getCurrentFocus().getNextFocusDownId();
                if (downId != View.NO_ID) {
                    view.findViewById(downId).requestFocus();
                    System.out.println("Next");
                }
                initEditTextForWidget((MyScriptEditText) mActivity.getCurrentFocus(), this);
                break;
            case R.id.bt_save:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    validateData();
                } else onNetworkUnavailable(null);
                break;
        }
    }

    public void showOnlyWidget() {
        parentWidgetSuggestions.setVisibility(View.VISIBLE);
        containerSuggestionsList.setVisibility(View.GONE);
        layoutWidget.setVisibility(View.VISIBLE);
    }

    private void showWidgetAndSuggestionsBoth() {
        parentWidgetSuggestions.setVisibility(View.VISIBLE);
        containerSuggestionsList.setVisibility(View.VISIBLE);
        layoutWidget.setVisibility(View.VISIBLE);
    }

    private void hideBoth() {
        parentWidgetSuggestions.setVisibility(View.GONE);
    }

    private void showHideWidgetAndKeyboardLayout() {
        if (layoutWidget.getVisibility() == View.VISIBLE) {
            layoutWidget.setVisibility(View.GONE);
            showKeyboard();
        } else {
            layoutWidget.setVisibility(View.VISIBLE);
            hideKeyboard();
        }
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
                    showHideDiagramsLayout(View.GONE);

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

    private void openActivity(CommonOpenUpFragmentType fragmentType, Object intentData, int requestCode) {
        Intent intent = new Intent(mActivity, AddVisitsActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (intentData != null) {
            intent.putExtra(HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA, Parcels.wrap(intentData));
        }
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
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
        svScrollView.fullScroll(ScrollView.FOCUS_UP);
        showHideDiagramsLayout(View.VISIBLE);
    }

    private void showHideLabTestsLayout() {
        if (parentDiagnosticTests.getVisibility() == View.GONE) {
            parentDiagnosticTests.setVisibility(View.VISIBLE);
            visibleViews.add(VisitsUiType.LAB_TEST);
        } else {
            parentDiagnosticTests.setVisibility(View.GONE);
            visibleViews.remove(VisitsUiType.LAB_TEST);
        }
    }

    private void showHideAdviceLayout() {
        if (layoutParentAdvice.getVisibility() == View.GONE) {
            layoutParentAdvice.setVisibility(View.VISIBLE);
            visibleViews.add(VisitsUiType.ADVICE);
            showOnlyWidget();
        } else {
            layoutParentAdvice.setVisibility(View.GONE);
            visibleViews.remove(VisitsUiType.ADVICE);
        }
    }

    private void showHideClinicalNotesLayout() {
        if (parentClinicalNote.getVisibility() == View.GONE) {
            visibleViews.add(VisitsUiType.CLINICAL_NOTES);
            parentClinicalNote.setVisibility(View.VISIBLE);
        } else {
            parentClinicalNote.setVisibility(View.GONE);
            visibleViews.remove(VisitsUiType.CLINICAL_NOTES);
        }
    }

    private void refreshSuggestionsList(View v, String searchTerm) {
        selectedViewForSuggestionsList = v;
        Object tag = v.getTag();
        if (tag != null) {
            if (tag instanceof String) {
                ClinicalNotesPermissionType permissionType = ClinicalNotesPermissionType.getClinicalNotesPermissionType((String) tag);
                if (permissionType != null) {
                    switch (permissionType) {
                        case COMPLAINT:
                            selectedSuggestionType = SuggestionType.COMPLAINTS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case OBSERVATION:
                            selectedSuggestionType = SuggestionType.OBSERVATION;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case INVESTIGATIONS:
                            selectedSuggestionType = SuggestionType.INVESTIGATION;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case DIAGNOSIS:
                            selectedSuggestionType = SuggestionType.DIAGNOSIS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                    }
                }
            } else if (tag instanceof SuggestionType)
                selectedSuggestionType = (SuggestionType) tag;
            if (selectedSuggestionType != null) {
                containerSuggestionsList.setVisibility(View.VISIBLE);
                try {
                    Intent intent = new Intent(AddVisitSuggestionsFragment.INTENT_LOAD_DATA);
                    intent.putExtra(AddVisitSuggestionsFragment.TAG_SEARCHED_TERM, searchTerm);
                    intent.putExtra(TAG_SUGGESTIONS_TYPE, selectedSuggestionType.ordinal());
                    LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        containerSuggestionsList.setVisibility(View.GONE);
    }

    private String getLastTextAfterCharacterToBeReplaced(String searchTerm) {
        Pattern p = Pattern.compile(".*" + CHARACTER_TO_BE_REPLACED + "\\s*(.*)");
        Matcher m = p.matcher(searchTerm.trim());

        if (m.find())
            return m.group(1);
        return searchTerm;
    }

    /**
     * Gets text before last occurance of CHARACTER_TO_BE_REPLACED in searchedTerm
     *
     * @param searchTerm
     * @return : text Before  last occurance of CHARACTER_TO_BE_REPLACED
     */
    private String getTextBeforeLastOccuranceOfCharacter(String searchTerm) {
        Pattern p = Pattern.compile("\\s*(.*)" + CHARACTER_TO_BE_REPLACED + ".*");
        Matcher m = p.matcher(searchTerm.trim());

        if (m.find())
            return m.group(1);
        return "";
    }

    private void showHideDiagramsLayout(int visibility) {
        if (visibility == View.GONE)
            visibleViews.remove(VisitsUiType.DIAGRAMS);
        else if (!visibleViews.contains(VisitsUiType.DIAGRAMS))
            visibleViews.add(VisitsUiType.DIAGRAMS);
        parentDiagrams.setVisibility(visibility);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()) && selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getUserId())) {
                    user = doctor.getUser();
                }
//                if ( selectedPatient != null)
//                    clinicalNotes = LocalDataServiceImpl.getInstance(mApp).getClinicalNote(null, clinicalNoteId, selectedPatient.getUserId());
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        super.onResponse(response);
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    initData();
                    break;
                case ADD_VISIT:
                    if (response.getData() != null && response.getData() instanceof VisitDetails) {
                        VisitDetails visit = (VisitDetails) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addVisit(visit);
                        Util.sendBroadcast(mApp, PatientVisitDetailFragment.INTENT_GET_VISITS_LIST_FROM_LOCAL);
                        Intent intent = new Intent();
                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_VISIT, intent);
                    }
                    mActivity.finish();
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }


    //--------------------------------------------------------------------------------
    // Widget callbacks

    @Override
    public void onConfigured(View view, SingleLineWidgetApi w, boolean success) {
        Log.d(TAG, "Widget configuration " + (success ? "done" : "failed"));
    }

    @Override
    public void onTextChanged(View view, SingleLineWidgetApi w, String text, boolean intermediate) {
        if (!isOnItemClick) {
            Log.d(TAG, "Text changed to \"" + text + "\" (" + (intermediate ? "intermediate" : "stable") + ")");
            if (view instanceof EditText) {
                refreshSuggestionsList(view, text);
                MyScriptEditText editText = (MyScriptEditText) view;
                // temporarily disable selection changed listener to prevent spurious cursor jumps
                editText.setOnSelectionChangedListener(null);
                editText.setTextKeepState(text);
                if (isCorrectionMode == 0) {
                    editText.setSelection(text.length());
                    editText.setOnSelectionChangedListener(new HealthcocoOnSelectionChanged(editText, this));
                    mWidget.setCursorIndex(editText.length());
                    //to set scrolling position
//                mWidget.scrollTo(editText.length() - 3);
                } else {
                    editText.setSelection(mWidget.getCursorIndex());
                    editText.setOnSelectionChangedListener(new HealthcocoOnSelectionChanged(editText, this));
                    isCorrectionMode--;
                }
            }
            updateCandidateBar();
        }
        isOnItemClick = false;
    }

    @Override
    public void onReturnGesture(View view, SingleLineWidgetApi w, int index) {
        Log.d(TAG, "Return gesture detected at index " + index);
        mWidget.replaceCharacters(index, index, "\n");
    }

    @Override
    public void onSingleTapGesture(View view, SingleLineWidgetApi w, int index) {
        Log.d(TAG, "Single tap gesture detected at index=" + index);
        if (view instanceof EditText) {
            MyScriptEditText editText = (MyScriptEditText) view;
            mWidget.setCursorIndex(index);
            editText.setSelection(index);
            updateCandidateBar();
        }
    }

    @Override
    public void onLongPressGesture(View view, SingleLineWidgetApi w, int index) {
        Log.d(TAG, "Long press gesture detected at index=" + index);
        mWidget.setCursorIndex(index);
        isCorrectionMode++;
    }

    @Override
    public void onEraseGesture(View view, SingleLineWidgetApi w, int start, int end) {
        Log.d(TAG, "Erase gesture detected for range [" + start + "-" + end + "]");
        mWidget.setCursorIndex(start);
        isCorrectionMode++;
    }

    @Override
    public void onSelectGesture(View view, SingleLineWidgetApi w, int start, int end) {
        Log.d(TAG, "Select gesture detected for range [" + start + "-" + end + "]");
    }

    @Override
    public void onUnderlineGesture(View view, SingleLineWidgetApi w, int start, int end) {
        Log.d(TAG, "Underline gesture detected for range [" + start + "-" + end + "]");
    }

    @Override
    public void onInsertGesture(View view, SingleLineWidgetApi w, int index) {
        Log.d(TAG, "Insert gesture detected at index " + index);
        mWidget.replaceCharacters(index, index, " ");
        mWidget.setCursorIndex(index + 1);
        isCorrectionMode++;
    }

    @Override
    public void onJoinGesture(View view, SingleLineWidgetApi w, int start, int end) {
        Log.d(TAG, "Join gesture detected for range [" + start + "-" + end + "]");
        mWidget.replaceCharacters(start, end, null);
        mWidget.setCursorIndex(start);
        isCorrectionMode++;
    }

    @Override
    public void onOverwriteGesture(View view, SingleLineWidgetApi w, int start, int end) {
        Log.d(TAG, "Overwrite gesture detected for range [" + start + "-" + end + "]");
        mWidget.setCursorIndex(end);
        isCorrectionMode++;
    }

    @Override
    public void onUserScrollBegin(View view, SingleLineWidgetApi w) {
        Log.d(TAG, "User scroll begin");
    }

    @Override
    public void onUserScrollEnd(View view, SingleLineWidgetApi w) {
        Log.d(TAG, "User scroll end");
    }

    @Override
    public void onUserScroll(View view, SingleLineWidgetApi w) {
        Log.d(TAG, "User scroll");
        if (view instanceof EditText) {
            MyScriptEditText editText = (MyScriptEditText) view;
            if (mWidget.moveCursorToVisibleIndex()) {
                // temporarily disable selection changed listener
                editText.setOnSelectionChangedListener(null);
                editText.setSelection(mWidget.getCursorIndex());
                editText.setOnSelectionChangedListener(new HealthcocoOnSelectionChanged(editText, this));
                updateCandidateBar();
            }
        }
    }

    @Override
    public void onPenUp(View view, SingleLineWidgetApi singleLineWidgetApi, CaptureInfo captureInfo) {

    }

    @Override
    public void onSelectionChanged(EditText editText, int selStart, int selEnd) {
        Log.d(TAG, "Selection changed to [" + selStart + "-" + selEnd + "]");
        if (mWidget.getCursorIndex() != selEnd) {
            mWidget.setCursorIndex(selEnd);
            if (selEnd == mWidget.getText().length()) {
                mWidget.scrollTo(selEnd);
            } else {
                mWidget.centerTo(selEnd);
            }
            updateCandidateBar();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        if (v instanceof MyScriptEditText) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_UP:
//                    initEditTextForWidget((MyScriptEditText) v, this);
//                    hideKeyboard();
//                    LogUtils.LOGD(TAG, "Action UP");
//                    break;
//                case MotionEvent.ACTION_DOWN:
//                    LogUtils.LOGD(TAG, "Action DOWN");
//                    hideKeyboard();
//                    break;
//            }
//        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showOnlyWidget();
            refreshSuggestionsList(v, "");
            if (v instanceof EditText)
                initEditTextForWidget((MyScriptEditText) v, this);
        }
    }

    public View.OnTouchListener getOnTouchListener() {
        return this;
    }

    public View.OnFocusChangeListener getFocusChangeListener() {
        return this;
    }
    //--------------------------------------------------------------------------------
    // UI callbacks

    private class CandidateTag {
        int start;
        int end;
        String text;
    }

    public void onEnterClick() {
        int index = mWidget.getCursorIndex();
        boolean replaced = false;
        View view = mActivity.getCurrentFocus();
        switch (view.getId()) {
            case R.id.edit_body_temperature:
            case R.id.edit_weight:
            case R.id.edit_heart_rate:
            case R.id.edit_systolic:
            case R.id.edit_diastolic:
            case R.id.edit_resp_rate:
            case R.id.edit_spo2:
//                replaced = mWidget.replaceCharacters(index, index, "");
                break;
            default:
                replaced = mWidget.replaceCharacters(index, index, "\n");
                break;
        }
        if (replaced) {
            mWidget.setCursorIndex(index + 1);
            isCorrectionMode++;
        }
    }

    public void onClearButtonClick() {
        mWidget.clear();
    }

    public void onCandidateButtonClick(View v) {
        CandidateTag tag = (CandidateTag) v.getTag();
        if (tag != null) {
            mWidget.replaceCharacters(tag.start, tag.end, tag.text);
        }
    }

    public void onSpaceButtonClick() {
        int index = mWidget.getCursorIndex();
        boolean replaced = mWidget.replaceCharacters(index, index, " ");
        if (replaced) {
            mWidget.setCursorIndex(index + 1);
            isCorrectionMode++;
        }
    }

    public void onDeleteButtonClick() {
        int index = mWidget.getCursorIndex();
        CandidateInfo info = mWidget.getCharacterCandidates(mWidget.getCursorIndex() - 1);
        boolean replaced = mWidget.replaceCharacters(info.getStart(), info.getEnd(), null);
        if (replaced) {
            mWidget.setCursorIndex(index - (info.getEnd() - info.getStart()));
            isCorrectionMode++;
        }
    }


    // update candidates bar
    private void updateCandidateBar() {
        int index = mWidget.getCursorIndex() - 1;
        if (index < 0) {
            index = 0;
        }

        CandidateInfo info = mWidget.getWordCandidates(index);

        int start = info.getStart();
        int end = info.getEnd();
        List<String> labels = info.getLabels();
        List<String> completions = info.getCompletions();

        for (int i = 0; i < 3; i++) {
            TextView textView = (TextView) mCandidateBar.getChildAt(i);

            CandidateTag tag = new CandidateTag();
            if (i < labels.size()) {
                tag.start = start;
                tag.end = end;
                tag.text = labels.get(i) + completions.get(i);
            } else {
                tag = null;
            }
            textView.setTag(tag);

            if (tag != null) {
                textView.setText(tag.text.replace("\n", "\u21B2"));
            } else {
                textView.setText("");
            }
        }

        if (info.isEmpty()) {
            mWidget.clearSelection();
        } else {
            mWidget.selectWord(index);
        }
    }

    private void validateData() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mActivity.showLoading(false);
            }

            @Override
            protected Integer doInBackground(Void... params) {
                int msgId;
                if (isBlankVisit()) {
                    msgId = R.string.alert_blank_visit;
                } else msgId = getValidatedUiMsgId();

                return msgId;
            }

            @Override
            protected void onPostExecute(Integer msgId) {
                super.onPostExecute(msgId);
                if (msgId == 0)
                    addVisit();
                else {
                    Util.showToast(mActivity, msgId);
                    mActivity.hideLoading();
                }
            }
        }.execute();
    }

    private int getValidatedUiMsgId() {
        int msgId = 0;
        for (VisitsUiType visitsUiType :
                visibleViews) {
            switch (visitsUiType) {
                case CLINICAL_NOTES:
                    String systolic = Util.getValidatedValueOrBlankTrimming(editSystolic);
                    String diastolic = Util.getValidatedValueOrBlankTrimming(editDiastolic);
                    if ((Util.isNullOrBlank(systolic) && !Util.isNullOrBlank(diastolic) || (!Util.isNullOrBlank(systolic) && Util.isNullOrBlank(diastolic))))
                        msgId = R.string.please_add_systolic_and_diastolic_both;
                    break;
                case PRESCRIPTION:
                    break;
                case LAB_TEST:
                    break;
                case DIAGRAMS:
                    break;
                case ADVICE:
                    break;
            }
        }
        return msgId;
    }

    private void addVisit() {
        VisitDetails visitDetails = new VisitDetails();
        visitDetails.setDoctorId(user.getUniqueId());
        visitDetails.setLocationId(user.getForeignLocationId());
        visitDetails.setHospitalId(user.getForeignHospitalId());
        visitDetails.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (!Util.isNullOrBlank(visitId))
            visitDetails.setVisitId(visitId);
        visitDetails.setClinicalNote(getClinicalNoteToSendDetails());
//            visitDetails.setPrescription(addNewPrescriptionFragment.getPrescriptionRequestDetails());
        WebDataServiceImpl.getInstance(mApp).addVisit(VisitDetails.class, visitDetails, this, this);
    }

    private boolean isBlankVisit() {
        boolean isBlankClinicalNote = true;
        boolean isBlankPrescription = true;
        boolean isBlankAdvice = true;
        boolean isBlankLabTest = true;
        boolean isBlankDiagram = true;
        for (VisitsUiType visitsUiType :
                visibleViews) {
            switch (visitsUiType) {
                case CLINICAL_NOTES:
                    isBlankClinicalNote = isBlankClinicalNote();
                    break;
                case PRESCRIPTION:
                    isBlankPrescription = addPrescriptionVisitFragment.isBlankPrescription();
                    break;
                case LAB_TEST:
                    break;
                case DIAGRAMS:
                    isBlankDiagram = Util.isNullOrEmptyList(diagramsList);
                    break;
                case ADVICE:
                    isBlankAdvice = Util.isNullOrBlank(Util.getValidatedValueOrBlankTrimming(etAdvice));
                    break;
            }
        }

        return isBlankClinicalNote && isBlankPrescription && isBlankAdvice && isBlankDiagram && isBlankLabTest;
    }

    public boolean isBlankClinicalNote() {
        boolean isBlankClinicalNote = true;
        for (String clinicalNotesPermissionType :
                user.getUiPermissions().getClinicalNotesPermissions()) {
            MyScriptEditText autoTvPermission = (MyScriptEditText) parentClinicalNote.findViewWithTag(clinicalNotesPermissionType);
            if (autoTvPermission != null && !Util.isNullOrBlank(Util.getValidatedValueOrBlankTrimming(autoTvPermission))) {
                isBlankClinicalNote = false;
                break;
            }
        }
        if (!isBlankVitalSigns())
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
        if (Util.isNullOrBlank(heartRate)
                && Util.isNullOrBlank(bodyTemprature)
                && Util.isNullOrBlank(weight)
                && (Util.isNullOrBlank(systolic)
                && Util.isNullOrBlank(diastolic))
                && Util.isNullOrBlank(respiratory)
                && Util.isNullOrBlank(spO2)) {
            return true;
        }
        return false;
    }

    public boolean isBlankLabTests() {
        return true;
    }

    public ClinicalNoteToSend getClinicalNoteToSendDetails() {
        ClinicalNoteToSend clinicalNotes = new ClinicalNoteToSend();
        clinicalNotes.setDoctorId(user.getUniqueId());
        clinicalNotes.setLocationId(user.getForeignLocationId());
        clinicalNotes.setHospitalId(user.getForeignHospitalId());
        clinicalNotes.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (visibleViews.contains(VisitsUiType.CLINICAL_NOTES)) {
            clinicalNotes.setVitalSigns(getVitalSigns());
            for (String permission :
                    user.getUiPermissions().getClinicalNotesPermissions()) {
                ClinicalNotesPermissionType clinicalNotesPermissionType = ClinicalNotesPermissionType.getClinicalNotesPermissionType(permission);
                if (clinicalNotesPermissionType != null) {
                    switch (clinicalNotesPermissionType) {
                        case PRESENT_COMPLAINT:
                            clinicalNotes.setPresentComplaint(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(PRESENT_COMPLAINT.getValue()))));
                            break;
                        case COMPLAINT:
                            clinicalNotes.setComplaint(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.COMPLAINT.getValue()))));
                            break;
                        case HISTORY_OF_PRESENT_COMPLAINT:
                            clinicalNotes.setPresentComplaintHistory(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.HISTORY_OF_PRESENT_COMPLAINT.getValue()))));
                            break;
                        case MENSTRUAL_HISTORY:
                            clinicalNotes.setMenstrualHistory(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.MENSTRUAL_HISTORY.getValue()))));
                            break;
                        case OBSTETRIC_HISTORY:
                            clinicalNotes.setObstetricHistory(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.OBSTETRIC_HISTORY.getValue()))));
                            break;
                        case GENERAL_EXAMINATION:
                            clinicalNotes.setGeneralExam(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.GENERAL_EXAMINATION.getValue()))));
                            break;
                        case SYSTEMIC_EXAMINATION:
                            clinicalNotes.setSystemExam(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.SYSTEMIC_EXAMINATION.getValue()))));
                            break;
                        case OBSERVATION:
                            clinicalNotes.setObservation(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.OBSERVATION.getValue()))));
                            break;
                        case INVESTIGATIONS:
                            clinicalNotes.setInvestigation(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.INVESTIGATIONS.getValue()))));
                            break;
                        case PROVISIONAL_DIAGNOSIS:
                            clinicalNotes.setProvisionalDiagnosis(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PROVISIONAL_DIAGNOSIS.getValue()))));
                            break;
                        case DIAGNOSIS:
                            clinicalNotes.setDiagnosis(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.DIAGNOSIS.getValue()))));
                            break;
                        case ECG:
                            clinicalNotes.setEcgDetails(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.ECG.getValue()))));
                            break;
                        case ECHO:
                            clinicalNotes.setEcho(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.ECHO.getValue()))));
                            break;
                        case XRAY:
                            clinicalNotes.setxRayDetails(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.XRAY.getValue()))));
                            break;
                        case HOLTER:
                            clinicalNotes.setHolter(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.HOLTER.getValue()))));
                            break;
                        case PA:
                            clinicalNotes.setPa(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PA.getValue()))));
                            break;
                        case PV:
                            clinicalNotes.setPv(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PV.getValue()))));
                            break;
                        case PS:
                            clinicalNotes.setPs(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.PS.getValue()))));
                            break;
                        case INDICATION_OF_USG:
                            clinicalNotes.setIndicationOfUSG(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.INDICATION_OF_USG.getValue()))));
                            break;
                        case NOTES:
                            clinicalNotes.setNote(getFormattedText(Util.getValidatedValueOrBlankWithoutTrimming((MyScriptEditText) parentPermissionItems.findViewWithTag(ClinicalNotesPermissionType.NOTES.getValue()))));
                            break;
                    }
                }
            }
        }

//        clinicalNotes.setDiagnoses(diagnosesListToSend);
        if (visibleViews.contains(VisitsUiType.DIAGRAMS) && !Util.isNullOrEmptyList(diagramsList)) {
            ArrayList<String> diagramIdsList = new ArrayList<String>();
            diagramIdsList.addAll(diagramsList.keySet());
            clinicalNotes.setDiagrams(diagramIdsList);
        }
        if (!Util.isNullOrBlank(clinicalNoteId))
            clinicalNotes.setUniqueId(clinicalNoteId);
        return clinicalNotes;
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

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            LogUtils.LOGD(TAG, "onResume " + receiversRegistered);

            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_ON_SUGGESTION_ITEM_CLICK);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(onSuggestionItemClickReceiver, filter);

            //receiver to show/hide btAdvice based on drugs present
            IntentFilter filter1 = new IntentFilter();
            filter1.addAction(INTENT_ADIVCE_BUTTON_VISIBILITY);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(adviceButtonVisibilityReceiver, filter1);
            //receiver to show/hide LabTest Layout based on Tests present
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_LAB_TEST_VISIBILITY);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(labTestLayoutVisibilityReceiver, filter2);

            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onSuggestionItemClickReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(adviceButtonVisibilityReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(labTestLayoutVisibilityReceiver);
    }


    BroadcastReceiver onSuggestionItemClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(TAG_SUGGESTIONS_TYPE) && intent.hasExtra(TAG_SELECTED_SUGGESTION_OBJECT)) {
                int ordinal = intent.getIntExtra(TAG_SUGGESTIONS_TYPE, -1);
                SuggestionType suggestionType = SuggestionType.values()[ordinal];
                Object selectedSuggestionObject = Parcels.unwrap(intent.getParcelableExtra(TAG_SELECTED_SUGGESTION_OBJECT));
                if (suggestionType != null && selectedSuggestionObject != null) {
                    handleSelectedSugestionObject(suggestionType, selectedSuggestionObject);
                }
            }
        }
    };
    BroadcastReceiver adviceButtonVisibilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(TAG_VISIBILITY)) {
                boolean showVisibility = intent.getBooleanExtra(TAG_VISIBILITY, false);
                if (showVisibility) {
                    parentPrescription.setVisibility(View.VISIBLE);
                    btAdvice.setVisibility(View.VISIBLE);
                } else {
                    parentPrescription.setVisibility(View.GONE);
                    btAdvice.setVisibility(View.GONE);
                }
            }
        }
    };
    BroadcastReceiver labTestLayoutVisibilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(TAG_VISIBILITY)) {
                boolean showVisibility = intent.getBooleanExtra(TAG_VISIBILITY, false);
                if (showVisibility) {
                    parentDiagnosticTests.setVisibility(View.VISIBLE);
                } else {
                    parentDiagnosticTests.setVisibility(View.GONE);
                }
            }
        }
    };

    private void handleSelectedSugestionObject(SuggestionType suggestionType, Object selectedSuggestionObject) {
        String text = "";
        switch (suggestionType) {
            case DRUGS:
                DrugItem selectedDrug = new DrugItem();
                String drugId = "";
                String drugName = "";
                String drugType = "";
                String drugTypeId = "";
                String directionId = "";
                String durationUnitId = "";
                String durationtext = "";
                String dosage = "";
                String instructions = "";
                List<GenericName> genericNames = null;
                DrugsListSolrResponse drugsSolr = (DrugsListSolrResponse) selectedSuggestionObject;
                LogUtils.LOGD(TAG, "Add selected drug " + drugsSolr.getDrugName());
                drugId = drugsSolr.getUniqueId();
                drugName = drugsSolr.getDrugName();
                drugType = drugsSolr.getDrugType();
                drugTypeId = drugsSolr.getDrugTypeId();
                if (!Util.isNullOrEmptyList(drugsSolr.getDirection()))
                    directionId = drugsSolr.getDirection().get(0).getUniqueId();
                if (drugsSolr.getDuration() != null && drugsSolr.getDuration().getDurationUnit() != null) {
                    durationtext = drugsSolr.getDuration().getValue();
                    durationUnitId = drugsSolr.getDuration().getDurationUnit().getUniqueId();
                }
                dosage = drugsSolr.getDosage();
                genericNames = drugsSolr.getGenericNames();
                Drug drug = new Drug();
                drug.setDrugName(drugName);
                drug.setUniqueId(drugId);
                drug.setGenericNames(genericNames);

                DrugType drugTypeObj = new DrugType();
                drugTypeObj.setUniqueId(drugTypeId);
                drugTypeObj.setType(drugType);
                drug.setDrugType(drugTypeObj);

                selectedDrug.setDrug(drug);
                selectedDrug.setDrugId(drugId);
                selectedDrug.setDosage(dosage);
                selectedDrug.setDirection(getDirectionsListFromLocal(directionId));
                selectedDrug.setDuration(getDurationAndUnit(durationtext, durationUnitId));
                selectedDrug.setInstructions(instructions);
                if (selectedDrug != null && addPrescriptionVisitFragment != null) {
                    addPrescriptionVisitFragment.addDrug(selectedDrug);
                }
                return;
            case LAB_TESTS:
                if (selectedSuggestionObject instanceof DiagnosticTest) {
                    DiagnosticTest diagnosticTest = (DiagnosticTest) selectedSuggestionObject;
                    addLabTestVisitFragment.addDiagnosticTest(diagnosticTest);
                    LogUtils.LOGD(TAG, "Selected Test " + diagnosticTest.getTestName());
                }
                break;
            case COMPLAINTS:
                if (selectedSuggestionObject instanceof ComplaintSuggestions) {
                    ComplaintSuggestions complaint = (ComplaintSuggestions) selectedSuggestionObject;
                    text = complaint.getComplaint() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case OBSERVATION:
                if (selectedSuggestionObject instanceof ObservationSuggestions) {
                    ObservationSuggestions observation = (ObservationSuggestions) selectedSuggestionObject;
                    text = observation.getObservation() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case INVESTIGATION:
                if (selectedSuggestionObject instanceof InvestigationSuggestions) {
                    InvestigationSuggestions investigation = (InvestigationSuggestions) selectedSuggestionObject;
                    text = investigation.getInvestigation() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case DIAGNOSIS:
                if (selectedSuggestionObject instanceof DiagnosisSuggestions) {
                    DiagnosisSuggestions diagnosis = (DiagnosisSuggestions) selectedSuggestionObject;
                    text = diagnosis.getDiagnosis() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
        }

        if (selectedViewForSuggestionsList != null && selectedViewForSuggestionsList instanceof EditText && !Util.isNullOrBlank(text)) {
            EditText editText = ((EditText) selectedViewForSuggestionsList);
            isOnItemClick = true;
            String textBeforeComma = getTextBeforeLastOccuranceOfCharacter(Util.getValidatedValueOrBlankWithoutTrimming(editText));
            if (!Util.isNullOrBlank(textBeforeComma))
                textBeforeComma = textBeforeComma + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
            editText.setText(textBeforeComma + text);
            editText.setSelection(Util.getValidatedValueOrBlankTrimming(editText).length());
            mWidget.setText(editText.getText().toString());
        }
    }

    /**
     * Any change in this method,change should also be done in AddNewPrescription,AddDrugDetailFragment's getDurationAndUnit() method
     */
    private Duration getDurationAndUnit(String durationText, String durationUnitId) {
        Duration duration = new Duration();
        duration.setValue(durationText);
        duration.setDurationUnit(LocalDataServiceImpl.getInstance(mApp).getDurationUnit(durationUnitId));
        return duration;
    }

    /**
     * Any change in this method,change should also be done in AddNewPrescription,AddDrugDetailFragment's getDirectionsListFromLocal() method
     */
    private List<DrugDirection> getDirectionsListFromLocal(String directionId) {
        List<DrugDirection> list = new ArrayList<>();
        DrugDirection direction = LocalDataServiceImpl.getInstance(mApp).getDrugDirection(directionId);
        if (direction != null)
            list.add(direction);
        return list;
    }

    public boolean hideKeyboardOrWidgetIfVisible() {
        boolean wasKeyboardOrWidgetVisible = false;
        if (parentWidgetSuggestions.getVisibility() == View.VISIBLE) {
            selectedSuggestionType = null;
            wasKeyboardOrWidgetVisible = true;
            hideBoth();
        }
        return wasKeyboardOrWidgetVisible;
    }
}
