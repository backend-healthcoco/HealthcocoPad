package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.AddVisitsActivity;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.ClinicalNoteToSend;
import com.healthcoco.healthcocopad.bean.request.PrescriptionRequest;
import com.healthcoco.healthcocopad.bean.request.TreatmentRequest;
import com.healthcoco.healthcocopad.bean.server.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.NextReviewOnDialogFragment;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.VisitIdType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.HealthcocoPopupWindow;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.myscript.atk.core.Line;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType.GET_VISIT_DETAILS;
import static com.healthcoco.healthcocopad.fragments.AddClinicalNotesVisitNormalFragment.TAG_CLINICAL_NOTE_ID;
import static com.healthcoco.healthcocopad.fragments.AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_ID;
import static com.healthcoco.healthcocopad.fragments.AddNewTreatmentFragment.TAG_TREATMENT_ID;

/**
 * Created by Shreshtha on 15-05-2017.
 */

public class AddEditNormalVisitsFragment extends HealthCocoFragment implements
        TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        View.OnClickListener, PopupWindowListener {
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "dd-MM-yyyy";
    private static final int REQUEST_CODE_NEXT_REVIEW = 111;
    private static final int TAB_POSITION_PRESCRIPTION = 1;
    private static final int TAB_POSITION_TREATMENT = 2;
    private ViewPager viewPager;
    private TabHost tabhost;
    private ArrayList<Fragment> fragmentsList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private AddEditNormalVisitPrescriptionFragment addEditNormalVisitPrescriptionFragment;
    private AddClinicalNotesVisitNormalFragment addClinicalNotesVisitNormalFragment;
    private AddNewTreatmentFragment addNewTreatmentFragment;
    private String visitId;
    private FloatingActionButton flBtSwap;
    private int currentPage = 0;
    private LinearLayout btSave;
    private boolean isFromClone;
    private LinearLayout layoutNextReview;
    private AppointmentRequest appointmentRequest;
    private TextView tvNextReviewDate;
    private TextView tvDoctorName;
    private TextView tvNextReviewTime;
    private LinearLayout lvDoctorName;
    private VisitDetails visit;
    private String appointmentId;
    private PatientDetailTabType detailTabType;
    private TabWidget tabs;
    private List<Treatments> treatment;
    private String clinicalNoteId;
    private String prescriptionId;
    private String treatmentId;
    private LinearLayout containerDateTime;
    private LinkedHashMap<String, ClinicDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();
    private ClinicDetailResponse selectedClinicProfile;
    private DoctorProfile doctorProfile;
    private HealthcocoPopupWindow doctorsListPopupWindow;


    private boolean isPrescriptionTabClicked = false;
    private boolean isClinicalNotesTabClicked = false;
    private boolean isTreatmentTabClicked = false;

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
            detailTabType = (PatientDetailTabType) intent.getExtras().get(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE);
            visitId = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_VISIT_ID));
            clinicalNoteId = intent.getStringExtra(TAG_CLINICAL_NOTE_ID);
            prescriptionId = intent.getStringExtra(TAG_PRESCRIPTION_ID);
            treatmentId = intent.getStringExtra(TAG_TREATMENT_ID);
            treatment = Parcels.unwrap(intent.getParcelableExtra(PatientTreatmentDetailFragment.TAG_TREATMENT_DATA));
            Parcelable isFromCloneParcelable = intent.getParcelableExtra(HealthCocoConstants.TAG_IS_FROM_CLONE);
            if (isFromCloneParcelable != null)
                isFromClone = Parcels.unwrap(isFromCloneParcelable);
        }
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_add_visit);
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabs = (TabWidget) view.findViewById(android.R.id.tabs);
        flBtSwap = (FloatingActionButton) view.findViewById(R.id.fl_bt_swap);
        layoutNextReview = (LinearLayout) view.findViewById(R.id.layout_next_review);
        containerDateTime = (LinearLayout) view.findViewById(R.id.container_date_time);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        lvDoctorName = (LinearLayout) view.findViewById(R.id.lv_doctor_name);
        tvNextReviewDate = (TextView) view.findViewById(R.id.tv_next_review_data);
        tvNextReviewTime = (TextView) view.findViewById(R.id.tv_next_review_time);
        containerDateTime.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        tabhost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);
        flBtSwap.setOnClickListener(this);
        layoutNextReview.setOnClickListener(this);

        ((CommonOpenUpActivity) mActivity).initRightActionView(ActionbarLeftRightActionTypeDrawables.WITH_SAVE, view);
        btSave = ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initTabsFragmentsList(VisitDetails visitDetails) {
        tabhost.setup();
        fragmentsList = new ArrayList<>();

        // init fragment 1
        addClinicalNotesVisitNormalFragment = new AddClinicalNotesVisitNormalFragment();
        if (visitDetails != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AddClinicalNotesSubFragment.TAG_CLINICAL_NOTES_DATA, Parcels.wrap(visitDetails.getClinicalNotes()));
            addClinicalNotesVisitNormalFragment.setArguments(bundle);
        }

        // init fragment 2
        addEditNormalVisitPrescriptionFragment = new AddEditNormalVisitPrescriptionFragment();

        // init fragment 3
        addNewTreatmentFragment = new AddNewTreatmentFragment();

        Bundle bundle = new Bundle();
        switch (detailTabType) {
            case PATIENT_DETAIL_VISIT:
                if (visitDetails != null) {
                    bundle.putParcelable(AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_DATA, Parcels.wrap(visitDetails.getPrescriptions()));
                }
                bundle.putParcelable(HealthCocoConstants.TAG_IS_FROM_VISIT, Parcels.wrap(true));
                addEditNormalVisitPrescriptionFragment.setArguments(bundle);

                Bundle bundle1 = new Bundle();
                if (visitDetails != null) {
                    bundle1.putParcelable(PatientTreatmentDetailFragment.TAG_TREATMENT_DATA, Parcels.wrap(visitDetails.getPatientTreatment()));
                }
                bundle1.putParcelable(HealthCocoConstants.TAG_IS_FROM_VISIT, Parcels.wrap(true));
                addNewTreatmentFragment.setArguments(bundle1);

                addFragment(addClinicalNotesVisitNormalFragment, R.string.clinical_notes, false);
                addFragment(addEditNormalVisitPrescriptionFragment, R.string.prescriptions, false);
                addFragment(addNewTreatmentFragment, R.string.treatment, true);
                tabs.setVisibility(View.VISIBLE);
//                flBtSwap.setVisibility(View.VISIBLE);
                break;

            case PATIENT_DETAIL_CLINICAL_NOTES:
                addFragment(addClinicalNotesVisitNormalFragment, R.string.clinical_notes, true);
                tabs.setVisibility(View.GONE);
                flBtSwap.setVisibility(View.GONE);
                break;
            case PATIENT_DETAIL_PRESCRIPTION:
                if (visitDetails != null) {
                    bundle.putParcelable(AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_DATA, Parcels.wrap(visitDetails.getPrescriptions()));
                }
                addEditNormalVisitPrescriptionFragment.setArguments(bundle);
                addFragment(addEditNormalVisitPrescriptionFragment, R.string.prescriptions, true);
                tabs.setVisibility(View.GONE);
                flBtSwap.setVisibility(View.GONE);
                break;

            case PATIENT_DETAIL_TREATMENT:
                if (!Util.isNullOrEmptyList(treatment)) {
                    bundle.putParcelable(PatientTreatmentDetailFragment.TAG_TREATMENT_DATA, Parcels.wrap(treatment));
                }
                addNewTreatmentFragment.setArguments(bundle);
                addFragment(addNewTreatmentFragment, R.string.treatment, true);
                tabs.setVisibility(View.GONE);
                flBtSwap.setVisibility(View.GONE);
                break;
        }
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
        viewPager.setCurrentItem(tabhost.getCurrentTab());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public Fragment getCurrentFragment() {
        if (currentPage == 0)
            return addClinicalNotesVisitNormalFragment;
        else return addEditNormalVisitPrescriptionFragment;
    }

    public Fragment getCurrentTabFragment(int page) {
        if (page == 0)
            return addClinicalNotesVisitNormalFragment;
        else return addEditNormalVisitPrescriptionFragment;
    }

    @Override
    public void onPageSelected(int position) {
        tabhost.setCurrentTab(position);
        hideKeyboard(view);
        switch (position) {
            case TAB_POSITION_PRESCRIPTION:
                if (!isPrescriptionTabClicked) {
                    addEditNormalVisitPrescriptionFragment.refreshData();
                    isPrescriptionTabClicked = true;
                }
                break;

            case TAB_POSITION_TREATMENT:
                if (!isTreatmentTabClicked) {
                    addNewTreatmentFragment.refreshData();
                    isTreatmentTabClicked = true;
                }
        }
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
                        if (!Util.isNullOrBlank(visitId)) {
                            new LocalDataBackgroundtaskOptimised(mActivity, GET_VISIT_DETAILS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            return;
                        } else {
                            initTabsAndViewPagerFragments(null);
                            refreshDoctorClinicText();
                            initSelectedDoctorClinicData();
                        }
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
                case GET_PATIENT_VISIT_DETAIL:
                    if (response.getData() != null && response.getData() instanceof VisitDetails) {
                        visit = (VisitDetails) response.getData();
                        initTabsAndViewPagerFragments(visit);
                        refreshDoctorClinicText();
                        initSelectedDoctorClinicData();
                    }
                    break;
                case ADD_CLINICAL_NOTES:
                    if (response.isValidData(response)) {
                        ClinicalNotes clinicalNote = (ClinicalNotes) response.getData();
                        Util.setVisitId(VisitIdType.CLINICAL_NOTES, clinicalNote.getVisitId());
                        LocalDataServiceImpl.getInstance(mApp).addClinicalNote(clinicalNote);
                        sendBroadcasts(clinicalNote.getUniqueId());
                        ((CommonOpenUpActivity) mActivity).finish();
                    }
                    break;
                case UPDATE_CLINICAL_NOTE:
                    if (response.isValidData(response)) {
                        ClinicalNotes clinicalNote = (ClinicalNotes) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addClinicalNote(clinicalNote);
                        sendBroadcasts(clinicalNote.getUniqueId());
                        ((CommonOpenUpActivity) mActivity).finish();
                    }
                    break;
                case ADD_PRESCRIPTION:
                case UPDATE_PRESCRIPTION:
                    if (response.getData() != null && response.getData() instanceof Prescription) {
                        Prescription prescription = (Prescription) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addPrescription(prescription);
                        Util.setVisitId(VisitIdType.PRESCRIPTION, prescription.getVisitId());
                        sendBroadcasts(prescription.getUniqueId());
                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_PRESCIPTION, null);
                        ((CommonOpenUpActivity) mActivity).finish();
                    }
                    break;
                case ADD_TREATMENT:
                case GET_TREATMENT:
                    if (response.getData() != null && response.getData() instanceof Treatments) {
                        Treatments treatments = (Treatments) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addTreatment(treatments);
                        Util.setVisitId(VisitIdType.TREATMENT, treatments.getVisitId());
                        sendBroadcasts(treatments.getUniqueId());
                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_NEW_TREATMENT, null);
                        ((CommonOpenUpActivity) mActivity).finish();
                    }
                    break;
                case GET_CLINIC_PROFILE:
                    if (response.getData() != null) {
                        selectedClinicProfile = (ClinicDetailResponse) response.getData();
                        formHashMapAndRefresh(selectedClinicProfile.getDoctors());
                    }
                    if (!response.isUserOnline())
                        onNetworkUnavailable(response.getWebServiceType());
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void sendBroadcasts(String uniqueId) {
        Util.sendBroadcasts(mApp, new ArrayList<String>() {{
            add(PatientVisitDetailFragment.INTENT_GET_VISITS_LIST_FROM_LOCAL);
        }});
        try {
            Intent intent = null;
            switch (detailTabType) {
                case PATIENT_DETAIL_CLINICAL_NOTES:
                    intent = new Intent(PatientClinicalNotesDetailFragment.INTENT_GET_CLINICAL_NOTES_LIST_LOCAL);
                    intent.putExtra(TAG_CLINICAL_NOTE_ID, uniqueId);
                    break;
                case PATIENT_DETAIL_PRESCRIPTION:
                    intent = new Intent(PatientPrescriptionDetailFragment.INTENT_GET_PRESCRIPTION_LIST_LOCAL);
                    intent.putExtra(TAG_PRESCRIPTION_ID, prescriptionId);
                    break;
                case PATIENT_DETAIL_TREATMENT:
                    intent = new Intent(PatientTreatmentDetailFragment.INTENT_GET_TREATMENT_LIST_LOCAL);
                    intent.putExtra(TAG_TREATMENT_ID, treatmentId);
                    break;
            }
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTabsAndViewPagerFragments(VisitDetails visitDetails) {
        initTabsFragmentsList(visitDetails);
        initViewPagerAdapter();
        if (visitDetails != null) {
            appointmentId = visitDetails.getAppointmentId();
            initNextReviewData(visitDetails.getAppointmentRequest());
        }
    }

    private void initNextReviewData(AppointmentRequest appointmentRequest) {
        if (appointmentRequest != null && !isFromClone) {
            containerDateTime.setVisibility(View.VISIBLE);
            tvNextReviewDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, appointmentRequest.getFromDate()));
            tvNextReviewTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(appointmentRequest.getTime().getFromTime())));
        }
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
                doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileObject(user.getUniqueId());
                selectedClinicProfile = LocalDataServiceImpl.getInstance(mApp).getClinicResponseDetails(user.getForeignLocationId());
                break;
            case GET_VISIT_DETAILS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getVisitDetailResponse(WebServiceType.GET_PATIENT_VISIT_DETAIL, visitId, null, null);
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
                validateData();
                break;
            case R.id.fl_bt_swap:
                showToggleConfirmationAlert();
                break;
            case R.id.layout_next_review:
                if (visit != null && visit.getAppointmentRequest() != null && visit.getAppointmentId() != null) {
                    showNextReviewReschedulealert();
                } else
                    openDialogFragment(new NextReviewOnDialogFragment(), REQUEST_CODE_NEXT_REVIEW);
                break;
        }
    }

    private void validateData() {
        int errorMsg = 0;
        switch (detailTabType) {
            case PATIENT_DETAIL_VISIT:
                int blankClinicalNoteMsgId = addClinicalNotesVisitNormalFragment.getBlankClinicalNoteMsgId();
                int blankPrescriptionMsgId = addEditNormalVisitPrescriptionFragment.getBlankPrescriptionMsg();
                int blankTreatmentMsgId = addNewTreatmentFragment.getBlankTreatmentMsg();
                if (blankClinicalNoteMsgId != 0 && blankPrescriptionMsgId != 0 && blankTreatmentMsgId != 0)
                    errorMsg = R.string.alert_blank_visit;
                if (errorMsg == 0) {
                    addVisit(blankClinicalNoteMsgId, blankPrescriptionMsgId, blankTreatmentMsgId);
                } else
                    Util.showToast(mActivity, errorMsg);
                break;
            case PATIENT_DETAIL_CLINICAL_NOTES:
                int noteMsgId = addClinicalNotesVisitNormalFragment.getBlankClinicalNoteMsgId();
                if (noteMsgId != 0)
                    errorMsg = R.string.alert_blank_clinical_note;
                if (errorMsg == 0) {
                    addClinicalNote(noteMsgId);
                } else
                    Util.showToast(mActivity, errorMsg);
                break;
            case PATIENT_DETAIL_PRESCRIPTION:
                int prescriptionMsg = addEditNormalVisitPrescriptionFragment.getBlankPrescriptionMsg();
                if (prescriptionMsg != 0)
                    errorMsg = R.string.alert_blank_prescription;
                if (errorMsg == 0) {
                    addPrescription(prescriptionMsg);
                } else
                    Util.showToast(mActivity, errorMsg);
                break;
            case PATIENT_DETAIL_TREATMENT:
                int msgId = addNewTreatmentFragment.getBlankTreatmentMsg();
                if (msgId == 0) {
//                    addNewTreatmentFragment.refreshListViewUpdatedTreatmentList();
                    addTreatment(msgId);
                } else {
                    Util.showToast(mActivity, msgId);
                }
//                addNewTreatmentFragment.validateData();
                break;
        }
    }

    private void addTreatment(int msgId) {
        mActivity.showLoading(false);
        LogUtils.LOGD(TAG, "Selected patient " + selectedPatient.getLocalPatientName());
        if (msgId == 0) {
            TreatmentRequest treatmentRequest = addNewTreatmentFragment.getTreatmentRequestDetails(false);
            if (Util.isNullOrBlank(treatmentId) && !isFromClone) {
                treatmentRequest.setDoctorId(user.getUniqueId());
                treatmentRequest.setVisitId(Util.getVisitId(VisitIdType.TREATMENT));
            }
            if (Util.isNullOrBlank(appointmentId))
                treatmentRequest.setAppointmentRequest(appointmentRequest);
            WebDataServiceImpl.getInstance(mApp).addTreatment(Treatments.class, treatmentRequest, this, this);
        }
    }

    private void addPrescription(int prescriptionMsg) {
        mActivity.showLoading(false);
        LogUtils.LOGD(TAG, "Selected patient " + selectedPatient.getLocalPatientName());
        if (prescriptionMsg == 0) {
            PrescriptionRequest prescription = addEditNormalVisitPrescriptionFragment.getPrescriptionRequestDetails();
            if (Util.isNullOrBlank(prescriptionId)) {
                prescription.setDoctorId(user.getUniqueId());
                prescription.setVisitId(Util.getVisitId(VisitIdType.PRESCRIPTION));
            }
            if (Util.isNullOrBlank(appointmentId))
                prescription.setAppointmentRequest(appointmentRequest);
            WebDataServiceImpl.getInstance(mApp).addPrescription(Prescription.class, prescription, this, this);
        }
    }

    private void addClinicalNote(int noteMsgId) {
        mActivity.showLoading(false);
        if (noteMsgId == 0) {
            ClinicalNoteToSend clinicalNotes = addClinicalNotesVisitNormalFragment.getClinicalNoteToSendDetails();
            if (!Util.isNullOrBlank(clinicalNoteId)) {
                clinicalNotes.setUniqueId(clinicalNoteId);
                WebDataServiceImpl.getInstance(mApp).updateClinicalNote(ClinicalNotes.class, clinicalNotes, this, this);
            } else {
                clinicalNotes.setDoctorId(user.getUniqueId());
                clinicalNotes.setVisitId(Util.getVisitId(VisitIdType.CLINICAL_NOTES));
                if (Util.isNullOrBlank(appointmentId))
                    clinicalNotes.setAppointmentRequest(appointmentRequest);
                WebDataServiceImpl.getInstance(mApp).addClinicalNote(ClinicalNotes.class, clinicalNotes, this, this);
            }
        }
    }

    private void showNextReviewReschedulealert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.please_reschedule_appointment);
        alertBuilder.setMessage(mActivity.getResources().getString(R.string.change_next_review_date_from_calender) + visit.getAppointmentId());
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create();
        alertBuilder.show();
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
                openCommonVisistActivity(CommonOpenUpFragmentType.ADD_VISITS, HealthCocoConstants.TAG_VISIT_ID, visitId, 0);
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

    private void addVisit(int blankClinicalNoteMsgId, int blankPrescriptionMsgId, int blankTreatmentMsgId) {
        mActivity.showLoading(false);
        VisitDetails visitDetails = new VisitDetails();
        visitDetails.setLocationId(user.getForeignLocationId());
        visitDetails.setHospitalId(user.getForeignHospitalId());
        visitDetails.setDoctorId(user.getUniqueId());
        visitDetails.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (!Util.isNullOrBlank(visitId) && !isFromClone) {
            visitDetails.setVisitId(visitId);
        }
      /*  if (Util.isNullOrBlank(visitId) && !isFromClone) {
        }*/
        if (blankClinicalNoteMsgId == 0) {
            visitDetails.setClinicalNote(addClinicalNotesVisitNormalFragment.getClinicalNoteToSendDetails());
            visitDetails.getClinicalNote().setDoctorId(user.getUniqueId());
        }
        if (blankPrescriptionMsgId == 0) {
            visitDetails.setPrescription(addEditNormalVisitPrescriptionFragment.getPrescriptionRequestDetails());
            visitDetails.getPrescription().setDoctorId(user.getUniqueId());
        }
        if (blankTreatmentMsgId == 0) {
//            addNewTreatmentFragment.refreshListViewUpdatedTreatmentList();
            visitDetails.setTreatmentRequest(addNewTreatmentFragment.getTreatmentRequestDetails(isFromClone));
            visitDetails.getTreatmentRequest().setDoctorId(user.getUniqueId());
        }
        if (Util.isNullOrBlank(appointmentId))
            visitDetails.setAppointmentRequest(appointmentRequest);
        WebDataServiceImpl.getInstance(mApp).addVisit(VisitDetails.class, visitDetails, this, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NEXT_REVIEW) {
            switch (resultCode) {
                case HealthCocoConstants.RESULT_CODE_NEXT_REVIEW:
                    if (data != null) {
                        appointmentRequest = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        appointmentRequest.setVisitId(visitId);
                        if (appointmentRequest != null) {
                            initNextReviewData(appointmentRequest);
                        }
                    }
                    break;
            }
        }
    }

    private void initSelectedDoctorClinicData() {
        if (user != null && RoleType.isAdmin(user.getRoleTypes())) {

            if ((!Util.isNullOrBlank(visitId)) || (!Util.isNullOrBlank(prescriptionId))) {
                tvDoctorName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvDoctorName.setEnabled(false);
                tvDoctorName.setClickable(false);
                lvDoctorName.setVisibility(View.VISIBLE);
                if (visit != null)
                    tvDoctorName.setText(Util.getValidatedValue(visit.getCreatedBy()));
            } else if (!Util.isNullOrBlank(treatmentId)) {
                tvDoctorName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvDoctorName.setEnabled(false);
                tvDoctorName.setClickable(false);
                lvDoctorName.setVisibility(View.VISIBLE);
                if (treatment != null)
                    tvDoctorName.setText(Util.getValidatedValue(treatment.get(0).getCreatedBy()));
            } else {
                tvDoctorName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvDoctorName.setEnabled(false);
                lvDoctorName.setVisibility(View.VISIBLE);
                refreshDoctorsList();
            }
        }
    }


    private void refreshDoctorsList() {
        showLoadingOverlay(true);
        WebDataServiceImpl.getInstance(mApp).getClinicDetails(ClinicDetailResponse.class, user.getForeignLocationId(), this, this);
    }

    private void formHashMapAndRefresh(List<ClinicDoctorProfile> responseList) {
        if (responseList.size() > 1) {
            tvDoctorName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_menu_down, 0);
            tvDoctorName.setEnabled(true);
            lvDoctorName.setVisibility(View.VISIBLE);

            if (!Util.isNullOrEmptyList(responseList)) {
                for (ClinicDoctorProfile clinicDoctorProfile :
                        responseList) {
                    clinicDoctorListHashMap.put(clinicDoctorProfile.getUniqueId(), clinicDoctorProfile);
                }
            }
//        notifyAdapter(new ArrayList<ClinicDoctorProfile>(clinicDoctorListHashMap.values()));
            if (doctorsListPopupWindow != null)
                doctorsListPopupWindow.notifyAdapter(new ArrayList<Object>(clinicDoctorListHashMap.values()));
            else
                mActivity.initPopupWindows(tvDoctorName, PopupWindowType.DOCTOR_LIST, new ArrayList<Object>(clinicDoctorListHashMap.values()), this);

        }
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case DOCTOR_LIST:
                if (object instanceof ClinicDoctorProfile) {
                    ClinicDoctorProfile doctorProfile = (ClinicDoctorProfile) object;
                    tvDoctorName.setText(doctorProfile.getFirstNameWithTitle());
                    user.setUniqueId(doctorProfile.getUniqueId());
                }
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }

    private void refreshDoctorClinicText() {
        tvDoctorName.setText(Util.getValidatedValue(doctorProfile.getFirstNameWithTitle()));
    }

}
