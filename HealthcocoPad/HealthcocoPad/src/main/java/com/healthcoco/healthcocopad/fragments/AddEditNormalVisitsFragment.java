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
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.AddVisitsActivity;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
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
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProvisionalDiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.PsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PvSuggestions;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.healthcoco.healthcocopad.fragments.AddClinicalNotesMyScriptVisitFragment.CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
import static com.healthcoco.healthcocopad.fragments.AddVisitSuggestionsFragment.TAG_SUGGESTIONS_TYPE;
import static com.healthcoco.healthcocopad.fragments.MyScriptAddVisitsFragment.TAG_SELECTED_SUGGESTION_OBJECT;

/**
 * Created by Shreshtha on 15-05-2017.
 */

public class AddEditNormalVisitsFragment extends HealthCocoFragment implements
        TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        View.OnClickListener {
    public static final String INTENT_ON_SUGGESTION_ITEM_CLICK = "com.healthcoco.healthcocopad.fragments.AddEditNormalVisitsFragment.ON_SUGGESTION_ITEM_CLICK";
    private ViewPager viewPager;
    private TabHost tabhost;
    private ArrayList<Fragment> fragmentsList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private AddEditNormalVisitPrescriptionFragment addEditNormalVisitPrescriptionFragment;
    private AddEditNormalVisitClinicalNotesFragment addEditNormalVisitClinicalNotesFragment;
    private String visitId;
    private VisitDetails visitDetails;
    private boolean isFromClone;
    private FloatingActionButton flBtSwap;
    private int currentPage = 0;
    private boolean receiversRegistered;
    private View selectedViewForSuggestionsList;
    private boolean isOnItemClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_normal_visit, container, false);
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
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initTabsFragmentsList();
        initViewPagerAdapter();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_add_visit);
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        flBtSwap = (FloatingActionButton) view.findViewById(R.id.fl_bt_swap);
    }

    @Override
    public void initListeners() {
        tabhost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);
        flBtSwap.setOnClickListener(this);
        ((CommonOpenUpActivity) mActivity).initRightActionView(ActionbarLeftRightActionTypeDrawables.WITH_SAVE, view);
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initTabsFragmentsList() {
        tabhost.setup();
        fragmentsList = new ArrayList<>();
        // init fragment 1
        addEditNormalVisitClinicalNotesFragment = new AddEditNormalVisitClinicalNotesFragment();
        // init fragment 2
        addEditNormalVisitPrescriptionFragment = new AddEditNormalVisitPrescriptionFragment();
        Bundle bundle = new Bundle();
//        bundle.putBoolean(AddEditNormalVisitPrescriptionFragment.TAG_HIDE_PATIENT_DETAIL_HEADER, true);
        addEditNormalVisitPrescriptionFragment.setArguments(bundle);
        addFragment(addEditNormalVisitClinicalNotesFragment, R.string.clinical_notes, false);
        addFragment(addEditNormalVisitPrescriptionFragment, R.string.prescriptions, true);
    }

    private void addFragment(Fragment fragment, int tabIndicatorId, boolean isLastTab) {
        fragmentsList.add(fragment);
        tabhost.addTab(getTabSpec(fragment.getClass().getSimpleName(), tabIndicatorId, isLastTab));
    }

    private TabHost.TabSpec getTabSpec(String simpleName, int textId, boolean isLastTab) {
        View view1 = mActivity.getLayoutInflater().inflate(R.layout.tab_indicator_add_visit, null);
        TextView tvTabText = (TextView) view1.findViewById(R.id.tv_tab_text);
        tvTabText.setText(textId);
        return tabhost.newTabSpec(simpleName).setIndicator(view1).setContent(new DummyTabFactory(mActivity));
    }

    private void initViewPagerAdapter() {
        viewPager.setOffscreenPageLimit(fragmentsList.size());
        ContactsDetailViewPagerAdapter viewPagerAdapter = new ContactsDetailViewPagerAdapter(
                mActivity.getSupportFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onTabChanged(String tabId) {
        hideKeyboard(view);
        viewPager.setCurrentItem(tabhost.getCurrentTab());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public Fragment getCurrentFragment() {
        if (currentPage == 0)
            return addEditNormalVisitClinicalNotesFragment;
        else return addEditNormalVisitPrescriptionFragment;
    }

    @Override
    public void onPageSelected(int position) {
        tabhost.setCurrentTab(position);
        currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && selectedPatient != null) {
                        LogUtils.LOGD(TAG, "Selected patient " + selectedPatient.getLocalPatientName());
                        initActionPatientDetailActionBar(PatientProfileScreenType.IN_ADD_VISIT_HEADER, view, selectedPatient);
                    }
                    break;
                case ADD_VISIT:
                    if (response.getData() != null && response.getData() instanceof VisitDetails) {
                        VisitDetails visit = (VisitDetails) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addVisit(visit);
                        Util.sendBroadcast(mApp, PatientVisitDetailFragment.INTENT_GET_VISITS_LIST_FROM_LOCAL);
                        Intent intent = new Intent();
                        intent.putExtra(HealthCocoConstants.TAG_VISIT_ID, visit.getUniqueId());
                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_VISIT, intent);
                    }
                    mActivity.finish();
                    break;
                default:
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
                if (doctor != null)
                    user = doctor.getUser();
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                if (!Util.isNullOrBlank(visitId))
                    visitDetails = LocalDataServiceImpl.getInstance(mApp).getVisit(visitId);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                int errorMsg = 0;
//                int blankClinicalNoteMsgId = addEditNormalVisitClinicalNotesFragment.getBlankClinicalNoteMsgId();
//                int blankPrescriptionMsgId = addEditNormalVisitPrescriptionFragment.getBlankPrescriptionMsg();
//                if (blankClinicalNoteMsgId != 0 && blankPrescriptionMsgId != 0)
//                    errorMsg = R.string.error_blank_visit;
//                if (errorMsg == 0) {
//                    addVisit(blankClinicalNoteMsgId, blankPrescriptionMsgId);
//                } else
//                    Util.showToast(mActivity, errorMsg);
                break;
            case R.id.fl_bt_swap:
                showToggleConfirmationAlert();
                break;
        }
    }

    public void showToggleConfirmationAlert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.alert);
        alertBuilder.setMessage(R.string.are_you_sure_want_to_toggle_this_page_view);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.toggle, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Util.addVisitToggleStateInPreference(mActivity, true);
                openCommonVisistActivity(CommonOpenUpFragmentType.ADD_VISITS, null, null, 0);
                mActivity.onBackPressed();
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

    private void openCommonVisistActivity(CommonOpenUpFragmentType fragmentType, String tag, Object intentData, int requestCode) {
        Intent intent = new Intent(mActivity, AddVisitsActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (!Util.isNullOrBlank(tag) && intentData != null)
            intent.putExtra(tag, Parcels.wrap(intentData));
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }

    private void addVisit(int blankClinicalNoteMsgId, int blankPrescriptionMsgId) {
        mActivity.showLoading(false);
        VisitDetails visitDetails = new VisitDetails();
        visitDetails.setDoctorId(user.getUniqueId());
        visitDetails.setLocationId(user.getForeignLocationId());
        visitDetails.setHospitalId(user.getForeignHospitalId());
        visitDetails.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (!Util.isNullOrBlank(visitId))
            visitDetails.setVisitId(visitId);
//        if (blankClinicalNoteMsgId == 0)
//            visitDetails.setClinicalNote(addEditNormalVisitClinicalNotesFragment.getClinicalNoteToSendDetails());
//        if (blankPrescriptionMsgId == 0)
//            visitDetails.setPrescription(addEditNormalVisitPrescriptionFragment.getPrescriptionRequestDetails());
        WebDataServiceImpl.getInstance(mApp).addVisit(VisitDetails.class, visitDetails, this, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            LogUtils.LOGD(TAG, "onResume " + receiversRegistered);

            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_ON_SUGGESTION_ITEM_CLICK);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(onSuggestionItemClickReceiver, filter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onSuggestionItemClickReceiver);
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

    private void handleSelectedSugestionObject(SuggestionType suggestionType, Object selectedSuggestionObject) {
        String text = "";
        switch (suggestionType) {
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
            String textBeforeComma = addEditNormalVisitClinicalNotesFragment.getTextBeforeLastOccuranceOfCharacter(Util.getValidatedValueOrBlankWithoutTrimming(editText));
            if (!Util.isNullOrBlank(textBeforeComma))
                textBeforeComma = textBeforeComma + CHARACTER_TO_REPLACE_COMMA_WITH_SPACES;
            editText.setText(textBeforeComma + text);
            editText.setSelection(Util.getValidatedValueOrBlankTrimming(editText).length());
        }
    }
}
