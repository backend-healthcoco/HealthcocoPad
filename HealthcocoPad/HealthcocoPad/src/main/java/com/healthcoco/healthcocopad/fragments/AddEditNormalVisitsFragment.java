package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.AddVisitsActivity;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AppointmentTimeSlotDetails;
import com.healthcoco.healthcocopad.bean.server.AvailableTimeSlots;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PopupWindowListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;
import com.healthcoco.healthcocopad.views.HealthcocoPopupWindow;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType.GET_VISIT_DETAILS;

/**
 * Created by Shreshtha on 15-05-2017.
 */

public class AddEditNormalVisitsFragment extends HealthCocoFragment implements
        TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        View.OnClickListener, PopupWindowListener, AdapterView.OnItemClickListener, HealthcocoTextWatcherListener, AutoCompleteTextViewListener {
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    private ViewPager viewPager;
    private TabHost tabhost;
    private ArrayList<Fragment> fragmentsList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private AddEditNormalVisitPrescriptionFragment addEditNormalVisitPrescriptionFragment;
    private AddEditNormalVisitClinicalNotesFragment addEditNormalVisitClinicalNotesFragment;
    private String visitId;
    private FloatingActionButton flBtSwap;
    private int currentPage = 0;
    private LinearLayout btSave;
    private boolean isFromClone;
    private TextView tvNextReview;
    private HealthcocoPopupWindow userStatePopupWindow;
    private Button btDone;
    private TextView tvSelectedDate;
    private CustomAutoCompleteTextView autotvSelectedTimeSlot;
    private AutoCompleteTextViewAdapter adapter;
    private AvailableTimeSlots selectedTimeSlot;
    private AppointmentTimeSlotDetails appointmentTimeSlotDetails;

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
        flBtSwap = (FloatingActionButton) view.findViewById(R.id.fl_bt_swap);
        tvNextReview = (TextView) view.findViewById(R.id.tv_next_review);

    }

    @Override
    public void initListeners() {
        tabhost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);
        flBtSwap.setOnClickListener(this);
        tvNextReview.setOnClickListener(this);

        ((CommonOpenUpActivity) mActivity).initRightActionView(ActionbarLeftRightActionTypeDrawables.WITH_SAVE, view);
        btSave = ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initUserStatePopUpWindow() {
        userStatePopupWindow = new HealthcocoPopupWindow(mActivity, PopupWindowType.NEXT_REVIEW, null, R.layout.layout_next_review_on, this);
        userStatePopupWindow.setOutsideTouchable(true);
        userStatePopupWindow.setContentView(userStatePopupWindow.getPopupView());
    }

    private void initTabsFragmentsList(VisitDetails visitDetails) {
        tabhost.setup();
        fragmentsList = new ArrayList<>();

        // init fragment 1
        addEditNormalVisitClinicalNotesFragment = new AddEditNormalVisitClinicalNotesFragment();
        if (visitDetails != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AddClinicalNotesVisitFragment.TAG_CLINICAL_NOTES_DATA, Parcels.wrap(visitDetails.getClinicalNotes()));
            addEditNormalVisitClinicalNotesFragment.setArguments(bundle);
        }

        // init fragment 2
        addEditNormalVisitPrescriptionFragment = new AddEditNormalVisitPrescriptionFragment();
        if (visitDetails != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_DATA, Parcels.wrap(visitDetails.getPrescriptions()));
            addEditNormalVisitPrescriptionFragment.setArguments(bundle);
        }
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

    public Fragment getCurrentTabFragment(int page) {
        if (page == 0)
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

    private void initData() {
        autotvSelectedTimeSlot.setText("");
        if (appointmentTimeSlotDetails != null) {
            if (appointmentTimeSlotDetails.getAppointmentSlot() != null) {
                LogUtils.LOGD(TAG, "Time " + appointmentTimeSlotDetails.getAppointmentSlot().getTime());
            }
            if (!Util.isNullOrEmptyList(appointmentTimeSlotDetails.getSlots())) {
                LogUtils.LOGD(TAG, "Slots Available " + appointmentTimeSlotDetails.getSlots().size());
                initAutoTvAdapter(autotvSelectedTimeSlot, AutoCompleteTextViewType.AVAILABLE_BOOKED_APPOINTMENTS, (ArrayList<Object>) (ArrayList<?>) appointmentTimeSlotDetails.getSlots());
            }
        }
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
                        tvSelectedDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
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
                        VisitDetails visit = (VisitDetails) response.getData();
                        initTabsAndViewPagerFragments(visit);
                    }
                    break;
                case GET_APPOINTMENT_TIME_SLOTS:
                    if (response.getData() != null && response.getData() instanceof AppointmentTimeSlotDetails) {
                        appointmentTimeSlotDetails = (AppointmentTimeSlotDetails) response.getData();
                        initData();
                    }
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void initTabsAndViewPagerFragments(VisitDetails visitDetails) {
        initTabsFragmentsList(visitDetails);
        initViewPagerAdapter();
    }

    private void prePopulateVisitDetails(VisitDetails visitDetails) {
        if (!Util.isNullOrEmptyList(visitDetails.getVisitedFor())) {
            for (VisitedForType visitedForType :
                    visitDetails.getVisitedFor()) {
                switch (visitedForType) {
                    case CLINICAL_NOTES:
                        addEditNormalVisitClinicalNotesFragment.prePopulateVisitDetails(visitDetails);
                        break;
                    case PRESCRIPTION:
                        addEditNormalVisitPrescriptionFragment.prePopulateVisitDetails(visitDetails.getPrescriptions());
                        break;
                }
            }
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
                initUserStatePopUpWindow();
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
                int errorMsg = 0;
                int blankClinicalNoteMsgId = addEditNormalVisitClinicalNotesFragment.getBlankClinicalNoteMsgId();
                int blankPrescriptionMsgId = addEditNormalVisitPrescriptionFragment.getBlankPrescriptionMsg();
                if (blankClinicalNoteMsgId != 0 && blankPrescriptionMsgId != 0)
                    errorMsg = R.string.alert_blank_visit;
                if (errorMsg == 0) {
                    addVisit(blankClinicalNoteMsgId, blankPrescriptionMsgId);
                } else
                    Util.showToast(mActivity, errorMsg);
                break;
            case R.id.fl_bt_swap:
                showToggleConfirmationAlert();
                break;
            case R.id.tv_next_review:
                userStatePopupWindow.showOptionsWindowAtRightCenter(v);
                break;
            case R.id.bt_done:
                break;
            case R.id.tv_selected_date:
                openDatePickerDialog((TextView) v);
        }
    }

    private void openDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, calendar, textView);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(DateTimeUtil.getFormattedTime(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, year, monthOfYear, dayOfMonth, 0, 0, 0));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
        datePickerDialog.show();
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
            visitDetails.setClinicalNote(addEditNormalVisitClinicalNotesFragment.getClinicalNoteToSendDetails());
        if (blankPrescriptionMsgId == 0)
            visitDetails.setPrescription(addEditNormalVisitPrescriptionFragment.getPrescriptionRequestDetails());
        WebDataServiceImpl.getInstance(mApp).addVisit(VisitDetails.class, visitDetails, this, this);
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case NEXT_REVIEW:
                if (object instanceof View) {
                    View view = (View) object;
                    initPopupviews(view);
                }
                break;
        }
    }

    private void initPopupviews(View view) {
        tvSelectedDate = (TextView) view.findViewById(R.id.tv_selected_date);
        autotvSelectedTimeSlot = (CustomAutoCompleteTextView) view.findViewById(R.id.autotv_selected_time_slot);
        btDone = (Button) view.findViewById(R.id.bt_done);
        initAutoTvAdapter(autotvSelectedTimeSlot, AutoCompleteTextViewType.AVAILABLE_BOOKED_APPOINTMENTS, null);
        tvSelectedDate.setOnClickListener(this);
        tvSelectedDate.addTextChangedListener(new HealthcocoTextWatcher(tvSelectedDate, this));
        autotvSelectedTimeSlot.setOnItemClickListener(this);
        autotvSelectedTimeSlot.setAutoTvListener(this);
        btDone.setOnClickListener(this);
        refreshSelectedDate(DateTimeUtil.getCurrentDateLong());
    }

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, AutoCompleteTextViewType autoCompleteTextViewType, ArrayList<Object> list) {
        try {
            autotvSelectedTimeSlot.setText("");
            adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_available_time_slots,
                    list, autoCompleteTextViewType);
            autoCompleteTextView.setThreshold(0);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshSelectedDate(long date) {
        tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, date));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.item_drop_down:
                Object object = adapter.getItem(position);
                clearPreviousAlerts();
                if (object != null && object instanceof AvailableTimeSlots) {
                    AvailableTimeSlots availableTimeSlots = (AvailableTimeSlots) object;
                    if (!availableTimeSlots.getIsAvailable()) {
                        selectedTimeSlot = null;
                        Util.showToast(mActivity, R.string.selected_time_slot_is_not_available);
                        EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, new ArrayList<View>() {{
                            add(autotvSelectedTimeSlot);
                        }}, null);
                    } else {
                        selectedTimeSlot = availableTimeSlots;
                        autotvSelectedTimeSlot.setText(adapter.getText(position, null, AutoCompleteTextViewType.AVAILABLE_BOOKED_APPOINTMENTS, object));
                    }
                }
                break;
        }
    }

    private void clearPreviousAlerts() {
        tvSelectedDate.setActivated(false);
        autotvSelectedTimeSlot.setActivated(false);
    }

    @Override
    public void afterTextChange(View v, String s) {
        switch (v.getId()) {
            case R.id.tv_selected_date:
                LogUtils.LOGD(TAG, "TextVieew Selected Date ");
                getAppointmentSlotNew(DATE_FORMAT_USED_IN_THIS_SCREEN, s);
                break;
        }
    }

    private void getAppointmentSlotNew(String format, String displayedDate) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            mActivity.showLoading(false);
            if (displayedDate.equalsIgnoreCase(DateTimeUtil.getCurrentFormattedDate(format))) {
                displayedDate = DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN);
            }
            WebDataServiceImpl.getInstance(mApp).getAppointmentSlotsDetails(AppointmentTimeSlotDetails.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, displayedDate), this, this);
        } else
            Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onEmptyListFound(AutoCompleteTextView autoCompleteTextView) {
        switch (autoCompleteTextView.getId()) {
            case R.id.autotv_selected_time_slot:
                Util.showToast(mActivity, R.string.no_slots_available);
                break;
        }
    }

    @Override
    public void scrollToPosition(int position) {

    }
}
