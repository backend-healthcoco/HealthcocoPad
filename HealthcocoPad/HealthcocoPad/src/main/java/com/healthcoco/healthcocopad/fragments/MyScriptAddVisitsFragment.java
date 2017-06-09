package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.MyCertificate;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.PrescriptionRequest;
import com.healthcoco.healthcocopad.bean.server.AssignedUserUiPermissions;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTestsPrescription;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.EcgDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.EchoSuggestions;
import com.healthcoco.healthcocopad.bean.server.GeneralExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.GenericName;
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
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProvisionalDiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.PsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PvSuggestions;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.custom.HealthcocoOnSelectionChanged;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.custom.MyScriptEditText;
import com.healthcoco.healthcocopad.enums.ClinicalNotesPermissionType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.PrescriptionPermissionType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.healthcoco.healthcocopad.enums.VisitsUiType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoOnSelectionChangedListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA;
import static com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType.GET_VISIT_DETAILS;
import static com.healthcoco.healthcocopad.fragments.AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
import static com.healthcoco.healthcocopad.fragments.AddVisitSuggestionsFragment.TAG_SUGGESTIONS_TYPE;

/**
 * Created by Shreshtha on 05-04-2017.
 */
public class MyScriptAddVisitsFragment extends HealthCocoFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised,
        HealthcocoOnSelectionChangedListener, View.OnTouchListener {
    public static final String INTENT_ON_SUGGESTION_ITEM_CLICK = "com.healthcoco.healthcocopad.fragments.MyScriptAddVisitsFragment.ON_SUGGESTION_ITEM_CLICK";
    public static final String INTENT_ADIVCE_BUTTON_VISIBILITY = "com.healthcoco.healthcocopad.fragments.MyScriptAddVisitsFragment.ADIVCE_BUTTON_VISIBILITY";
    public static final String INTENT_DIAGRAM_LAYOUT_VISIBILITY = "com.healthcoco.healthcocopad.fragments.MyScriptAddVisitsFragment.DIAGRAM_LAYOUT_VISIBILITY";
    public static final String INTENT_DIAGRAM_BUTTON_VISIBILITY = "com.healthcoco.healthcocopad.fragments.MyScriptAddVisitsFragment.DIAGRAM_BUTTON_VISIBILITY";
    public static final String INTENT_CLINCIAL_NOTE_LAYOUT_VISIBILITY = "com.healthcoco.healthcocopad.fragments.MyScriptAddVisitsFragment.CLINCIAL_NOTE_LAYOUT_VISIBILITY";
    public static final String INTENT_CLINCIAL_NOTE_BUTTON_VISIBILITY = "com.healthcoco.healthcocopad.fragments.MyScriptAddVisitsFragment.CLINCIAL_NOTE_BUTTON_VISIBILITY";

    public static final String INTENT_LAB_TEST_VISIBILITY = "com.healthcoco.healthcocopad.fragments.MyScriptAddVisitsFragment.LAB_TEST_VISIBILITY";

    public static final String TAG_SELECTED_SUGGESTION_OBJECT = "selectedSuggestionObject";
    public static final String TAG_VISIBILITY = "visibility";

    private static final float WIDGET_AUTOSCROLL_MARGIN = 250;
    private static final float WIDGET_TEXT_SIZE = 40;
    private static final float WIDGET_INT_WIDTH = 4;
    private static final int WIDGET_TEXT_COLOR = 0xFF0077b5;

    private LinearLayout btClose;
    private ImageButton btClinicalNote;
    private ImageButton btPrescription;
    private ImageButton btLabTests;
    private ImageButton btDiagrams;
    private ImageButton btAdvice;
    private LinearLayout btSave;
    private TextView tvHeaderText;
    private LinearLayout parentClinicalNote;
    private LinearLayout parentPrescription;
    private LinearLayout parentDiagnosticTests;
    private LinearLayout parentDiagrams;
    private LinearLayout parentAdvice;
    private TextView tvPatientName;
    private TextView tvDate;
    private TextView tvPatientId;
    private TextView tvRxId;
    private TextView tvMobileNo;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private ScrollView svScrollView;
    private ClinicalNotes clinicalNotes;
    private SingleLineWidget mWidget;
    private int isCorrectionMode;

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

    private String visitId;
    //shift this to addClinicNoteFragment after seperate implementation of addCLinicalNotes
    private String clinicalNoteId;
    private String prescriptionId;

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
    private boolean mKeyboardStatus = false;
    private AddClinicalNotesMyScriptVisitFragment addClinicalNotesFragment;
    private boolean isFromClone;
    private FloatingActionButton flBtSwap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_visits_info, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = mActivity.getIntent();
        if (intent != null) {
            visitId = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_VISIT_ID));
            Parcelable isFromCloneParcelable = intent.getParcelableExtra(HealthCocoConstants.TAG_IS_FROM_CLONE);
            if (isFromCloneParcelable != null)
                isFromClone = Parcels.unwrap(isFromCloneParcelable);
        }
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
        initClinicalNotesFragment();
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

    private void initClinicalNotesFragment() {
        addClinicalNotesFragment = new AddClinicalNotesMyScriptVisitFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.parent_clinical_note, addClinicalNotesFragment, addClinicalNotesFragment.getClass().getSimpleName());
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
        parentClinicalNote = (LinearLayout) view.findViewById(R.id.parent_clinical_note);
        parentPrescription = (LinearLayout) view.findViewById(R.id.parent_prescription);
        parentDiagnosticTests = (LinearLayout) view.findViewById(R.id.parent_diagnostic_tests);
        parentDiagrams = (LinearLayout) view.findViewById(R.id.parent_diagrams);
        parentAdvice = (LinearLayout) view.findViewById(R.id.layout_parent_advice);
        etAdvice = (MyScriptEditText) view.findViewById(R.id.et_advice);
        flBtSwap = (FloatingActionButton) view.findViewById(R.id.fl_bt_swap);

        parentClinicalNote.setVisibility(View.GONE);
        parentPrescription.setVisibility(View.GONE);
        parentDiagnosticTests.setVisibility(View.GONE);
        parentDiagrams.setVisibility(View.GONE);
        parentAdvice.setVisibility(View.GONE);
        initWidgetViews();
        initToolbarView();
        initHeaderView();
        setHeightOfWidgetsAndSuggestions();
    }

    private void setHeightOfWidgetsAndSuggestions() {
        ViewGroup.LayoutParams layoutParamsSuggestionsList = containerSuggestionsList.getLayoutParams();
        layoutParamsSuggestionsList.height = (int) (ScreenDimensions.SCREEN_WIDTH * 0.25);
        containerSuggestionsList.setLayoutParams(layoutParamsSuggestionsList);

        ViewGroup.LayoutParams layoutParamsWidget = layoutWidget.getLayoutParams();
        layoutParamsWidget.height = (int) (ScreenDimensions.SCREEN_WIDTH * 0.25);
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
        mWidget.setBaselinePosition((float) (ScreenDimensions.SCREEN_WIDTH * 0.10));
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
        btClose = (LinearLayout) view.findViewById(R.id.bt_close);
        btClinicalNote = (ImageButton) view.findViewById(R.id.bt_clinical_note);
        btPrescription = (ImageButton) view.findViewById(R.id.bt_prescription);
        btPrescription.setTag(SuggestionType.DRUGS);
        btLabTests = (ImageButton) view.findViewById(R.id.bt_lab_tests);
        btLabTests.setTag(SuggestionType.LAB_TESTS);
        btDiagrams = (ImageButton) view.findViewById(R.id.bt_diagrams);
        btAdvice = (ImageButton) view.findViewById(R.id.bt_advice);
        btAdvice.setVisibility(View.GONE);
        btSave = (LinearLayout) view.findViewById(R.id.bt_save);
    }

    private void initHeaderView() {
        tvHeaderText = (TextView) view.findViewById(R.id.tv_header_text);
        tvPatientName = (TextView) view.findViewById(R.id.tv_patient_name);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvPatientId = (TextView) view.findViewById(R.id.tv_patient_id);
        tvRxId = (TextView) view.findViewById(R.id.tv_rx_id);
        tvMobileNo = (TextView) view.findViewById(R.id.tv_mobile_no);
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
        flBtSwap.setOnClickListener(this);

        btPrescription.setOnTouchListener(this);
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
            case R.id.edit_duration_common:
            case R.id.edit_duration:
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

    private void initUiPermissions(AssignedUserUiPermissions uiPermissions) {
        addClinicalNotesFragment.initUiPermissions(user.getUiPermissions().getClinicalNotesPermissionsString());
        addPrescriptionVisitFragment.initUiPermissions(user.getUiPermissions().getPrescriptionPermissions());
    }

    private void prePopulateVisitDetails(VisitDetails visitDetails) {
        if (!Util.isNullOrEmptyList(visitDetails.getVisitedFor())) {
            for (VisitedForType visitedForType :
                    visitDetails.getVisitedFor()) {
                switch (visitedForType) {
                    case CLINICAL_NOTES:
                        if (!Util.isNullOrEmptyList(visitDetails.getClinicalNotes())) {
                            for (ClinicalNotes clinicalNotes :
                                    visitDetails.getClinicalNotes()) {
                                parentClinicalNote.setVisibility(View.VISIBLE);
                                addClinicalNotesFragment.refreshData(clinicalNotes);
                                if (!isFromClone)
                                    clinicalNoteId = clinicalNotes.getUniqueId();
                            }
                        }
                        parentClinicalNote.setVisibility(View.GONE);
                        break;
                    case PRESCRIPTION:
                        if (!Util.isNullOrEmptyList(visitDetails.getPrescriptions())) {
                            for (Prescription prescription :
                                    visitDetails.getPrescriptions()) {
                                //initialising drugs List
                                if (!Util.isNullOrEmptyList(prescription.getItems())) {
                                    parentPrescription.setVisibility(View.VISIBLE);
                                    for (DrugItem drugItem :
                                            prescription.getItems()) {
                                        if (drugItem.getDrug() != null && !Util.isNullOrBlank(drugItem.getDrug().getUniqueId()))
                                            addPrescriptionVisitFragment.addDrug(drugItem);
                                    }
                                }
                                //initialising Advice
                                if (!Util.isNullOrBlank(prescription.getAdvice())) {
                                    btAdvice.setVisibility(View.VISIBLE);
                                    etAdvice.setText(prescription.getAdvice());
                                    parentAdvice.setVisibility(View.VISIBLE);
                                    addVisibileUiType(VisitsUiType.ADVICE);
                                }

                                //initialising DiagnosticTests(LabTests)
                                if (!Util.isNullOrEmptyList(prescription.getDiagnosticTests())) {
                                    parentDiagnosticTests.setVisibility(View.VISIBLE);
                                    for (DiagnosticTestsPrescription diagnosticTest :
                                            prescription.getDiagnosticTests()) {
                                        if (diagnosticTest.getTest() != null)
                                            addLabTestVisitFragment.addDiagnosticTest(diagnosticTest.getTest());
                                    }
                                }
                                if (!isFromClone)
                                    prescriptionId = prescription.getUniqueId();
                            }
                        } else parentPrescription.setVisibility(View.GONE);
                        break;
                }
            }
        }
    }

    private void setPrescriptionUiPermissionVisibility(PrescriptionPermissionType permissionsType, int visibility) {
        switch (permissionsType) {
//            case ADVICE:
//                btAdvice.setVisibility(visibility);
//                break;
            case LAB:
                btLabTests.setVisibility(visibility);
                break;
            case GENERIC_DRUGS:
                break;
            case MYDRUGS:
                break;
        }
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
                svScrollView.requestChildFocus(parentPrescription, parentPrescription);
                if (selectedSuggestionType == null || selectedSuggestionType != SuggestionType.DRUGS) {
                    addVisitSuggestionsFragment.refreshTagOfEditText(SuggestionType.DRUGS);
                }
                break;
            case R.id.bt_lab_tests:
                svScrollView.requestChildFocus(parentDiagnosticTests, parentDiagnosticTests);
                if (selectedSuggestionType == null || selectedSuggestionType != SuggestionType.LAB_TESTS) {
                    addVisibileUiType(VisitsUiType.LAB_TEST);
                    addVisitSuggestionsFragment.refreshTagOfEditText(SuggestionType.LAB_TESTS);
                }
                break;
            case R.id.bt_diagrams:
                addClinicalNotesFragment.openDiagramsListFragment();
                break;
            case R.id.bt_advice:
                selectedSuggestionType = null;
                showHideAdviceLayout();
                break;

            case R.id.bt_keyboard:
//                showHideKeyboardLayout();
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
            case R.id.fl_bt_swap:
                showToggleConfirmationAlert();
                break;
        }
    }

    private void showToggleConfirmationAlert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.alert);
        alertBuilder.setMessage(R.string.are_you_sure_want_to_toggle_this_page_view);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.toggle, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Util.addVisitToggleStateInPreference(mActivity, false);
                Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
                intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_VISITS.ordinal());
                intent.putExtra(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_VISIT);
                intent.putExtra(HealthCocoConstants.TAG_VISIT_ID, Parcels.wrap(visitId));
                startActivityForResult(intent, 0);
                mActivity.finish();
            }
        });
        alertBuilder.setNegativeButton(R.string.stay, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create();
        alertBuilder.show();
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

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(mActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mKeyboardStatus = false;
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(mActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        mKeyboardStatus = true;
    }

    private boolean isKeyboardActive() {
        return mKeyboardStatus;
    }

    private void showHideKeyboardLayout() {
        if (!isKeyboardActive()) {
            showKeyboard();
        } else {
            dismissKeyboard();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void showHideAdviceLayout() {
        if (parentAdvice.getVisibility() == View.GONE) {
            Util.requesFocus(etAdvice);
            requestFocus(etAdvice);
            parentAdvice.setVisibility(View.VISIBLE);
            addVisibileUiType(VisitsUiType.ADVICE);
            showOnlyWidget();
        } else {
            parentAdvice.setVisibility(View.GONE);
            removeVisibileUiType(VisitsUiType.ADVICE);
        }
    }

    private void showHideClinicalNotesLayout() {
        if (parentClinicalNote.getVisibility() == View.GONE) {
            addVisibileUiType(VisitsUiType.CLINICAL_NOTES);
            parentClinicalNote.setVisibility(View.VISIBLE);
        } else {
            parentClinicalNote.setVisibility(View.GONE);
            removeVisibileUiType(VisitsUiType.CLINICAL_NOTES);
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
                        case PRESENT_COMPLAINT:
                            selectedSuggestionType = SuggestionType.PRESENT_COMPLAINT;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
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
                        case HISTORY_OF_PRESENT_COMPLAINT:
                            selectedSuggestionType = SuggestionType.HISTORY_OF_PRESENT_COMPLAINT;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case MENSTRUAL_HISTORY:
                            selectedSuggestionType = SuggestionType.MENSTRUAL_HISTORY;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case OBSTETRIC_HISTORY:
                            selectedSuggestionType = SuggestionType.OBSTETRIC_HISTORY;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case GENERAL_EXAMINATION:
                            selectedSuggestionType = SuggestionType.GENERAL_EXAMINATION;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case SYSTEMIC_EXAMINATION:
                            selectedSuggestionType = SuggestionType.SYSTEMIC_EXAMINATION;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case PROVISIONAL_DIAGNOSIS:
                            selectedSuggestionType = SuggestionType.PROVISIONAL_DIAGNOSIS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case NOTES:
                            selectedSuggestionType = SuggestionType.NOTES;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case ECG:
                            selectedSuggestionType = SuggestionType.ECG_DETAILS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case ECHO:
                            selectedSuggestionType = SuggestionType.ECHO;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case XRAY:
                            selectedSuggestionType = SuggestionType.X_RAY_DETAILS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case HOLTER:
                            selectedSuggestionType = SuggestionType.HOLTER;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case PA:
                            selectedSuggestionType = SuggestionType.PA;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case PV:
                            selectedSuggestionType = SuggestionType.PV;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case PS:
                            selectedSuggestionType = SuggestionType.PS;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        case INDICATION_OF_USG:
                            selectedSuggestionType = SuggestionType.INDICATION_OF_USG;
                            searchTerm = getLastTextAfterCharacterToBeReplaced(searchTerm);
                            break;
                        default:
                            selectedSuggestionType = null;
                    }
                }
            } else if (tag instanceof SuggestionType)
                selectedSuggestionType = (SuggestionType) tag;
            else
                selectedSuggestionType = null;
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
        Pattern p = Pattern.compile(".*" + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_BE_REPLACED + "\\s*(.*)");
        Matcher m = p.matcher(searchTerm.trim());

        if (m.find())
            return m.group(1);
        return searchTerm;
    }


    private void showHideDiagramsLayout(int visibility) {
        if (visibility == View.GONE)
            removeVisibileUiType(VisitsUiType.DIAGRAMS);
        else if (!visibleViews.contains(VisitsUiType.DIAGRAMS))
            addVisibileUiType(VisitsUiType.DIAGRAMS);
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
                break;
            case GET_VISIT_DETAILS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getVisitDetailResponse(WebServiceType.GET_PATIENT_VISIT_DETAIL, visitId, null, null);
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
                    if (!Util.isNullOrBlank(visitId)) {
                        new LocalDataBackgroundtaskOptimised(mActivity, GET_VISIT_DETAILS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        return;
                    }
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
                case GET_PATIENT_VISIT_DETAIL:
                    if (response.getData() != null && response.getData() instanceof VisitDetails) {
                        VisitDetails visit = (VisitDetails) response.getData();
                        visit.setSelectedPatient(selectedPatient);
                        prePopulateVisitDetails(visit);
                    }
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
                    if (view.getId() == R.id.edit_duration_common) {
                        setDurationUnitToAll(text);
                    }
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

    private void setDurationUnitToAll(String unit) {
        addPrescriptionVisitFragment.modifyDurationUnit(unit);
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
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                requestFocus(v);
                LogUtils.LOGD(TAG, "Action UP");
                break;
            case MotionEvent.ACTION_DOWN:
                LogUtils.LOGD(TAG, "Action DOWN");
                break;
        }
        return false;
    }

    public void requestFocus(View v) {
        showOnlyWidget();
        refreshSuggestionsList(v, "");
        if (v instanceof EditText)
            initEditTextForWidget((MyScriptEditText) v, this);
        else if (selectedSuggestionType != null)
            addVisitSuggestionsFragment.refreshTagOfEditText(selectedSuggestionType);
    }

    public View.OnTouchListener getOnTouchListener() {
        return this;
    }

    public boolean isClinicalNoteViewVisible() {
        return visibleViews.contains(VisitsUiType.CLINICAL_NOTES);
    }

    public boolean isDiagramViewVisible() {
        return visibleViews.contains(VisitsUiType.DIAGRAMS);
    }

    public LinearLayout getDiagramContainer() {
        return (LinearLayout) view.findViewById(R.id.container_diagrams);
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
        try {
            int index = mWidget.getCursorIndex();
            CandidateInfo info = mWidget.getCharacterCandidates(mWidget.getCursorIndex() - 1);
            boolean replaced = mWidget.replaceCharacters(info.getStart(), info.getEnd(), null);
            if (replaced) {
                mWidget.setCursorIndex(index - (info.getEnd() - info.getStart()));
                isCorrectionMode++;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mActivity.showLoading(false);
            }

            @Override
            protected Object doInBackground(Void... params) {
                int msgId;
                if (isBlankVisit()) {
                    msgId = R.string.alert_blank_visit;
                } else msgId = getValidatedUiMsgId();
                if (msgId == 0)
                    return getVisitRequestObjectToSend();
                else {
                    return msgId;
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);
                if (object instanceof VisitDetails)
                    addVisit((VisitDetails) object);
                else if (object instanceof Integer) {
                    Util.showToast(mActivity, (Integer) object);
                    mActivity.hideLoading();
                }
            }
        }.execute();
    }

    private VisitDetails getVisitRequestObjectToSend() {
        VisitDetails visitDetails = new VisitDetails();
        visitDetails.setDoctorId(user.getUniqueId());
        visitDetails.setLocationId(user.getForeignLocationId());
        visitDetails.setHospitalId(user.getForeignHospitalId());
        visitDetails.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (!Util.isNullOrBlank(visitId) && !isFromClone)
            visitDetails.setVisitId(visitId);
        if ((visibleViews.contains(VisitsUiType.CLINICAL_NOTES) || visibleViews.contains(VisitsUiType.DIAGRAMS)) && !addClinicalNotesFragment.isBlankClinicalNote())
            visitDetails.setClinicalNote(addClinicalNotesFragment.getClinicalNoteToSendDetails());
        if (!addPrescriptionVisitFragment.isBlankDrugsList() || !addLabTestVisitFragment.isBlankLabTestsList()) {
            PrescriptionRequest prescription = new PrescriptionRequest();
            LogUtils.LOGD(TAG, "Selected patient " + selectedPatient.getLocalPatientName());
            if (!Util.isNullOrBlank(prescriptionId))
                prescription.setUniqueId(prescriptionId);
            prescription.setPatientId(selectedPatient.getUserId());
            prescription.setDoctorId(user.getUniqueId());
            prescription.setLocationId(user.getForeignLocationId());
            prescription.setHospitalId(user.getForeignHospitalId());
            if (visibleViews.contains(VisitsUiType.ADVICE))
                prescription.setAdvice(Util.getValidatedValueOrNull(etAdvice));
            prescription.setItems(addPrescriptionVisitFragment.getModifiedDrugsList());
            prescription.setDiagnosticTests(addLabTestVisitFragment.getLabTestsList());
            visitDetails.setPrescription(prescription);
        }
        return visitDetails;
    }

    private int getValidatedUiMsgId() {
        int msgId = 0;
        for (VisitsUiType visitsUiType :
                visibleViews) {
            switch (visitsUiType) {
                case CLINICAL_NOTES:
                    msgId = addClinicalNotesFragment.getValidatedMsgId();
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

    private void addVisit(VisitDetails visitDetails) {
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
                    isBlankClinicalNote = addClinicalNotesFragment.isBlankClinicalNote();
                    break;
                case PRESCRIPTION:
                    isBlankPrescription = addPrescriptionVisitFragment.isBlankDrugsList();
                    break;
                case LAB_TEST:
                    isBlankLabTest = addLabTestVisitFragment.isBlankLabTestsList();
                    break;
                case DIAGRAMS:
                    isBlankDiagram = Util.isNullOrEmptyList(addClinicalNotesFragment.getDiagramsList());
                    break;
                case ADVICE:
                    isBlankAdvice = Util.isNullOrBlank(Util.getValidatedValueOrBlankTrimming(etAdvice));
                    break;
            }
        }
        return isBlankClinicalNote && isBlankPrescription && isBlankAdvice && isBlankDiagram && isBlankLabTest;
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
            //receiver to show/hide btDiagrams based on drugs present
            IntentFilter filter3 = new IntentFilter();
            filter3.addAction(INTENT_DIAGRAM_LAYOUT_VISIBILITY);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(diagramLayoutVisibilityReceiver, filter3);

            //receiver to show/hide parentClinicalNOtes based on drugs present
            IntentFilter filter4 = new IntentFilter();
            filter4.addAction(INTENT_CLINCIAL_NOTE_LAYOUT_VISIBILITY);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(clinicalNoteLayoutVisibilityReceiver, filter4);

            //receiver to show/hide btClinicalNotes based on drugs present
            IntentFilter filter5 = new IntentFilter();
            filter5.addAction(INTENT_CLINCIAL_NOTE_BUTTON_VISIBILITY);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(clinicalNoteButtonVisibilityReceiver, filter5);

            //receiver to show/hide btDiagrams based on drugs present
            IntentFilter filter6 = new IntentFilter();
            filter6.addAction(INTENT_CLINCIAL_NOTE_BUTTON_VISIBILITY);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(diagramButtonVisibilityReceiver, filter6);

            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onSuggestionItemClickReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(adviceButtonVisibilityReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(labTestLayoutVisibilityReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(diagramLayoutVisibilityReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(clinicalNoteLayoutVisibilityReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(clinicalNoteButtonVisibilityReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(diagramButtonVisibilityReceiver);
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
                Util.enableAllChildViews(btSave, true);
                boolean showVisibility = intent.getBooleanExtra(TAG_VISIBILITY, false);
                if (showVisibility) {
                    addVisibileUiType(VisitsUiType.PRESCRIPTION);
                    parentPrescription.setVisibility(View.VISIBLE);
                    btAdvice.setVisibility(View.VISIBLE);
                } else {
                    if (!Util.isNullOrBlank(prescriptionId))
                        Util.enableAllChildViews(btSave, false);
                    removeVisibileUiType(VisitsUiType.PRESCRIPTION);
                    parentPrescription.setVisibility(View.GONE);
                    btAdvice.setVisibility(View.GONE);
                    parentAdvice.setVisibility(View.GONE);
                }
            }
        }
    };
    BroadcastReceiver diagramLayoutVisibilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(TAG_VISIBILITY)) {
                boolean showVisibility = intent.getBooleanExtra(TAG_VISIBILITY, false);
                if (showVisibility) {
                    addVisibileUiType(VisitsUiType.DIAGRAMS);
                    parentDiagrams.setVisibility(View.VISIBLE);
                } else {
                    removeVisibileUiType(VisitsUiType.DIAGRAMS);
                    parentDiagrams.setVisibility(View.GONE);
                }
            }
        }
    };
    BroadcastReceiver diagramButtonVisibilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(TAG_VISIBILITY)) {
                boolean showVisibility = intent.getBooleanExtra(TAG_VISIBILITY, false);
                if (showVisibility) {
//                    addVisibileUiType(VisitsUiType.DIAGRAMS);
                    btDiagrams.setVisibility(View.VISIBLE);
                } else {
//                    removeVisibileUiType(VisitsUiType.DIAGRAMS);
                    btDiagrams.setVisibility(View.GONE);
                }
            }
        }
    };
    BroadcastReceiver clinicalNoteLayoutVisibilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(TAG_VISIBILITY)) {
                boolean showVisibility = intent.getBooleanExtra(TAG_VISIBILITY, false);
                if (showVisibility) {
                    addVisibileUiType(VisitsUiType.CLINICAL_NOTES);
                    parentClinicalNote.setVisibility(View.VISIBLE);
                } else {
                    removeVisibileUiType(VisitsUiType.CLINICAL_NOTES);
                    parentClinicalNote.setVisibility(View.GONE);
                }
            }
        }
    };
    BroadcastReceiver clinicalNoteButtonVisibilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(TAG_VISIBILITY)) {
                boolean showVisibility = intent.getBooleanExtra(TAG_VISIBILITY, false);
                if (showVisibility) {
//                    addVisibileUiType(VisitsUiType.CLINICAL_NOTES);
                    btClinicalNote.setVisibility(View.VISIBLE);
                } else {
//                    removeVisibileUiType(VisitsUiType.CLINICAL_NOTES);
                    btClinicalNote.setVisibility(View.GONE);
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
                    addVisibileUiType(VisitsUiType.LAB_TEST);
                    parentDiagnosticTests.setVisibility(View.VISIBLE);
                } else {
                    removeVisibileUiType(VisitsUiType.LAB_TEST);
                    parentDiagnosticTests.setVisibility(View.GONE);
                }
            }
        }
    };

    private void addVisibileUiType(VisitsUiType visitsUiType) {
        if (!visibleViews.contains(visitsUiType))
            visibleViews.add(visitsUiType);
    }

    private void removeVisibileUiType(VisitsUiType visitsUiType) {
        if (visibleViews.contains(visitsUiType))
            visibleViews.remove(visitsUiType);
    }

    private void handleSelectedSugestionObject(SuggestionType suggestionType, Object selectedSuggestionObject) {
        String text = "";
        switch (suggestionType) {
            case DRUGS:
                DrugItem selectedDrug = new DrugItem();
                String drugId = "";
                String drugName = "";
                String drugType = "";
                List<GenericName> genericNames = null;

                DrugsListSolrResponse drugsSolr = (DrugsListSolrResponse) selectedSuggestionObject;
                LogUtils.LOGD(TAG, "Add selected drug " + drugsSolr.getDrugName());
                drugId = drugsSolr.getUniqueId();
                drugName = drugsSolr.getDrugName();
                drugType = drugsSolr.getDrugType();
                genericNames = drugsSolr.getGenericNames();

                //setting drug object
                Drug drug = new Drug();
                drug.setDrugName(drugName);
                drug.setUniqueId(drugId);
                drug.setGenericNames(genericNames);

                DrugType drugTypeObj = new DrugType();
                drugTypeObj.setUniqueId(drugsSolr.getDrugTypeId());
                drugTypeObj.setType(drugType);
                drug.setDrugType(drugTypeObj);

                selectedDrug.setDrug(drug);
                selectedDrug.setDrugId(drugId);
                selectedDrug.setDosage(drugsSolr.getDosage());
                selectedDrug.setDirection(drugsSolr.getDirection());
                selectedDrug.setDuration(drugsSolr.getDuration());
                if (selectedDrug != null && addPrescriptionVisitFragment != null) {
                    svScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            svScrollView.removeOnLayoutChangeListener(this);
                            Log.d(TAG, "list got updated, do what ever u want");
                            svScrollView.requestChildFocus(addPrescriptionVisitFragment.getLastChildView(), addPrescriptionVisitFragment.getLastChildView());

                        }
                    });
                    addPrescriptionVisitFragment.addDrug(selectedDrug);
                    mWidget.clear();
                }
                return;
            case LAB_TESTS:
                if (selectedSuggestionObject instanceof DiagnosticTest) {
                    DiagnosticTest diagnosticTest = (DiagnosticTest) selectedSuggestionObject;
                    if (diagnosticTest != null && addLabTestVisitFragment != null) {
                        svScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                            @Override
                            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                svScrollView.removeOnLayoutChangeListener(this);
                                Log.d(TAG, "list got updated, do what ever u want");
                                svScrollView.requestChildFocus(addLabTestVisitFragment.getLastChildView(), addLabTestVisitFragment.getLastChildView());

                            }
                        });
                        addLabTestVisitFragment.addDiagnosticTest(diagnosticTest);
                        LogUtils.LOGD(TAG, "Selected Test " + diagnosticTest.getTestName());
                    }
                }
                mWidget.clear();
                break;
            case PRESENT_COMPLAINT:
                if (selectedSuggestionObject instanceof PresentComplaintSuggestions) {
                    PresentComplaintSuggestions complaint = (PresentComplaintSuggestions) selectedSuggestionObject;
                    text = complaint.getPresentComplaint() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
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
            case HISTORY_OF_PRESENT_COMPLAINT:
                if (selectedSuggestionObject instanceof HistoryPresentComplaintSuggestions) {
                    HistoryPresentComplaintSuggestions complaint = (HistoryPresentComplaintSuggestions) selectedSuggestionObject;
                    text = complaint.getPresentComplaintHistory() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case MENSTRUAL_HISTORY:
                if (selectedSuggestionObject instanceof MenstrualHistorySuggestions) {
                    MenstrualHistorySuggestions complaint = (MenstrualHistorySuggestions) selectedSuggestionObject;
                    text = complaint.getMenstrualHistory() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case OBSTETRIC_HISTORY:
                if (selectedSuggestionObject instanceof ObstetricHistorySuggestions) {
                    ObstetricHistorySuggestions complaint = (ObstetricHistorySuggestions) selectedSuggestionObject;
                    text = complaint.getObstetricHistory() + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case GENERAL_EXAMINATION:
                if (selectedSuggestionObject instanceof GeneralExaminationSuggestions) {
                    GeneralExaminationSuggestions complaint = (GeneralExaminationSuggestions) selectedSuggestionObject;
                    text = complaint.getGeneralExam() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case SYSTEMIC_EXAMINATION:
                if (selectedSuggestionObject instanceof SystemicExaminationSuggestions) {
                    SystemicExaminationSuggestions complaint = (SystemicExaminationSuggestions) selectedSuggestionObject;
                    text = complaint.getSystemExam() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case PROVISIONAL_DIAGNOSIS:
                if (selectedSuggestionObject instanceof ProvisionalDiagnosisSuggestions) {
                    ProvisionalDiagnosisSuggestions complaint = (ProvisionalDiagnosisSuggestions) selectedSuggestionObject;
                    text = complaint.getProvisionalDiagnosis() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case NOTES:
                if (selectedSuggestionObject instanceof NotesSuggestions) {
                    NotesSuggestions complaint = (NotesSuggestions) selectedSuggestionObject;
                    text = complaint.getNote() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case ECG_DETAILS:
                if (selectedSuggestionObject instanceof EcgDetailSuggestions) {
                    EcgDetailSuggestions complaint = (EcgDetailSuggestions) selectedSuggestionObject;
                    text = complaint.getEcgDetails() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case ECHO:
                if (selectedSuggestionObject instanceof EchoSuggestions) {
                    EchoSuggestions complaint = (EchoSuggestions) selectedSuggestionObject;
                    text = complaint.getEcho() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case X_RAY_DETAILS:
                if (selectedSuggestionObject instanceof XrayDetailSuggestions) {
                    XrayDetailSuggestions complaint = (XrayDetailSuggestions) selectedSuggestionObject;
                    text = complaint.getxRayDetails() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case HOLTER:
                if (selectedSuggestionObject instanceof HolterSuggestions) {
                    HolterSuggestions complaint = (HolterSuggestions) selectedSuggestionObject;
                    text = complaint.getHolter() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case PA:
                if (selectedSuggestionObject instanceof PaSuggestions) {
                    PaSuggestions complaint = (PaSuggestions) selectedSuggestionObject;
                    text = complaint.getPa() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case PV:
                if (selectedSuggestionObject instanceof PvSuggestions) {
                    PvSuggestions complaint = (PvSuggestions) selectedSuggestionObject;
                    text = complaint.getPv() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case PS:
                if (selectedSuggestionObject instanceof PsSuggestions) {
                    PsSuggestions complaint = (PsSuggestions) selectedSuggestionObject;
                    text = complaint.getPs() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
            case INDICATION_OF_USG:
                if (selectedSuggestionObject instanceof IndicationOfUsgSuggestions) {
                    IndicationOfUsgSuggestions complaint = (IndicationOfUsgSuggestions) selectedSuggestionObject;
                    text = complaint.getIndicationOfUSG() + AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
                }
                break;
        }

        if (selectedViewForSuggestionsList != null && selectedViewForSuggestionsList instanceof EditText && !Util.isNullOrBlank(text)) {
            EditText editText = ((EditText) selectedViewForSuggestionsList);
            isOnItemClick = true;
            String textBeforeComma = addClinicalNotesFragment.getTextBeforeLastOccuranceOfCharacter(Util.getValidatedValueOrBlankWithoutTrimming(editText));
            if (!Util.isNullOrBlank(textBeforeComma))
                textBeforeComma = textBeforeComma + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
            editText.setText(textBeforeComma + text);
            editText.setSelection(Util.getValidatedValueOrBlankTrimming(editText).length());
            mWidget.setText(editText.getText().toString());
            mWidget.setCursorIndex(editText.length());
        }
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
