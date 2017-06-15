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
import com.healthcoco.healthcocopad.bean.server.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
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
import com.healthcoco.healthcocopad.enums.VisitIdType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;

import static com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType.GET_VISIT_DETAILS;
import static com.healthcoco.healthcocopad.fragments.AddClinicalNotesVisitNormalFragment.TAG_CLINICAL_NOTE_ID;
import static com.healthcoco.healthcocopad.fragments.AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_ID;

/**
 * Created by Shreshtha on 15-05-2017.
 */

public class AddEditNormalVisitsFragment extends HealthCocoFragment implements
        TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        View.OnClickListener {
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "dd-MM-yyyy";
    private static final int REQUEST_CODE_NEXT_REVIEW = 111;
    private ViewPager viewPager;
    private TabHost tabhost;
    private ArrayList<Fragment> fragmentsList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private AddEditNormalVisitPrescriptionFragment addEditNormalVisitPrescriptionFragment;
    private AddClinicalNotesVisitNormalFragment addClinicalNotesVisitNormalFragment;
    private String visitId;
    private FloatingActionButton flBtSwap;
    private int currentPage = 0;
    private LinearLayout btSave;
    private boolean isFromClone;
    private LinearLayout layoutNextReview;
    private AppointmentRequest appointmentRequest;
    private TextView tvNextReviewDate;
    private TextView tvNextReviewTime;
    private VisitDetails visit;
    private String appointmentId;
    private PatientDetailTabType detailTabType;
    private TabWidget tabs;
    private String clinicalNoteId;
    private String prescriptionId;
    private LinearLayout containerDateTime;

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
        if (visitDetails != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_DATA, Parcels.wrap(visitDetails.getPrescriptions()));
            addEditNormalVisitPrescriptionFragment.setArguments(bundle);
        }
        switch (detailTabType) {
            case PATIENT_DETAIL_VISIT:
                addFragment(addClinicalNotesVisitNormalFragment, R.string.clinical_notes, false);
                addFragment(addEditNormalVisitPrescriptionFragment, R.string.prescriptions, true);
                tabs.setVisibility(View.VISIBLE);
                flBtSwap.setVisibility(View.VISIBLE);
                break;
            case PATIENT_DETAIL_CLINICAL_NOTES:
                addFragment(addClinicalNotesVisitNormalFragment, R.string.clinical_notes, false);
                tabs.setVisibility(View.GONE);
                flBtSwap.setVisibility(View.GONE);
                break;
            case PATIENT_DETAIL_PRESCRIPTION:
                addFragment(addEditNormalVisitPrescriptionFragment, R.string.prescriptions, true);
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
        hideKeyboard(view);
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
                        }
                        initTabsAndViewPagerFragments(null);
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
        if (appointmentRequest != null) {
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
                if (blankClinicalNoteMsgId != 0 && blankPrescriptionMsgId != 0)
                    errorMsg = R.string.alert_blank_visit;
                if (errorMsg == 0) {
                    addVisit(blankClinicalNoteMsgId, blankPrescriptionMsgId);
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
        }
    }

    private void addPrescription(int prescriptionMsg) {
        mActivity.showLoading(false);
        LogUtils.LOGD(TAG, "Selected patient " + selectedPatient.getLocalPatientName());
        if (prescriptionMsg == 0) {
            PrescriptionRequest prescription = addEditNormalVisitPrescriptionFragment.getPrescriptionRequestDetails();
            if (Util.isNullOrBlank(prescriptionId) && !isFromClone)
                prescription.setVisitId(Util.getVisitId(VisitIdType.PRESCRIPTION));
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

    private void addVisit(int blankClinicalNoteMsgId, int blankPrescriptionMsgId) {
        mActivity.showLoading(false);
        VisitDetails visitDetails = new VisitDetails();
        visitDetails.setDoctorId(user.getUniqueId());
        visitDetails.setLocationId(user.getForeignLocationId());
        visitDetails.setHospitalId(user.getForeignHospitalId());
        visitDetails.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        if (!Util.isNullOrBlank(visitId) && !isFromClone)
            visitDetails.setVisitId(visitId);
        if (blankClinicalNoteMsgId == 0)
            visitDetails.setClinicalNote(addClinicalNotesVisitNormalFragment.getClinicalNoteToSendDetails());
        if (blankPrescriptionMsgId == 0)
            visitDetails.setPrescription(addEditNormalVisitPrescriptionFragment.getPrescriptionRequestDetails());
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
}
