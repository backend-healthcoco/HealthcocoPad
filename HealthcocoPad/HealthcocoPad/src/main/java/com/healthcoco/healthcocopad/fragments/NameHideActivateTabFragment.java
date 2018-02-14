package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.NameHideActivateAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Complaint;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.EcgDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.EchoSuggestions;
import com.healthcoco.healthcocopad.bean.server.GeneralExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocopad.bean.server.HistoryPresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.HolterSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndicationOfUsgSuggestions;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MenstrualHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.NotesSuggestions;
import com.healthcoco.healthcocopad.bean.server.Observation;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObstetricHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.PaSuggestions;
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProvisionalDiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.PsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PvSuggestions;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.ExpandableHeightListView;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.custom.ScrollViewWithHeader;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.NameHideActivateType;
import com.healthcoco.healthcocopad.enums.RecordType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.NameHideActivateListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ListViewSizeHelper;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by neha on 15/01/16.
 */
public class NameHideActivateTabFragment extends HealthCocoFragment implements
        NameHideActivateListener, View.OnClickListener, TextWatcher, Response.Listener<VolleyResponseBean>,
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, SwipeRefreshLayout.OnRefreshListener {
    int typeOrdinal;
    private ExpandableHeightListView lvHidden;
    private ExpandableHeightListView lvActivatedList;
    private NameHideActivateAdapter adapterHidden;
    private NameHideActivateAdapter adapterActivated;
    private User user;
    private NameHideActivateType nameHideActivateType;
    private List<Object> listHidden = new ArrayList<>();
    private List<Object> listActivated = new ArrayList<>();
    private boolean isHiddenListLoaded;
    private boolean isActivatedListLoaded;
    private ScrollViewWithHeader svContainer;
    private TextView tvHeader;
    private TextView tvHeaderTwo;
    private TextView tvHeaderOne;
    private TextView tvAddNew;
    private boolean isHashMapFormed;
    private long latestUpdatedTime;
    private Object clickedObject;
    private boolean isHideClicked;
    private boolean isInitialLoading;
    //    private CustomSwipeRefreshLayout customSwiperefreshLayout;
    private LinearLayout containerHiddenList;
    private LinearLayout containerActivatedList;
    private TextView tvNoDataFound;
    private LinearLayout containerHideActivate;
    private boolean isActivatedListEmpty;
    private boolean isHiddenListEmpty;
    private AutoCompleteTextView autotvClinicName;
    private List<DoctorClinicProfile> doctorClinicsList;
//    HealthCocoActivity mActivity;

    public NameHideActivateTabFragment(int ordinal) {
        super();
        typeOrdinal = ordinal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings_hide_activate, container, false);
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
//        this.mActivity = mActivity;
//        Intent intent = mActivity.getIntent();
//        typeOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_NAME_EDIT_TYPE, -1);
        if (typeOrdinal > -1)
            nameHideActivateType = NameHideActivateType.values()[typeOrdinal];
        if (nameHideActivateType != null) {
            initViews();
            initScrollView();
            initListeners();
            initAdapters();
            mActivity.showLoading(false);
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void initViews() {
//        ((CommonOpenUpActivity) mActivity).initAddButton(this);
        initEditSearchView(nameHideActivateType.getSearchHintId(), this, this);
        lvHidden = (ExpandableHeightListView) view.findViewById(R.id.lv_hidden_list);
        lvActivatedList = (ExpandableHeightListView) view.findViewById(R.id.lv_activated_list);
        svContainer = (ScrollViewWithHeader) view
                .findViewById(R.id.sv_container);
        containerHideActivate = (LinearLayout) view.findViewById(R.id.container_hide_activate);
        tvNoDataFound = (TextView) view.findViewById(R.id.tv_no_data_found);
        tvNoDataFound.setText(nameHideActivateType.getNoDataText());
        tvHeader = (TextView) view.findViewById(R.id.tv_header);
        tvHeaderTwo = (TextView) view.findViewById(R.id.tv_header_two);
        tvHeaderOne = (TextView) view.findViewById(R.id.tv_header_one);
        containerHiddenList = (LinearLayout) view.findViewById(R.id.container_hidden_list);
        containerActivatedList = (LinearLayout) view.findViewById(R.id.container_activated_list);
        autotvClinicName = (AutoCompleteTextView) view.findViewById(R.id.autotv_clinic_name);
        tvAddNew = (TextView) view.findViewById(R.id.bt_advance_search);
        tvAddNew.setText(R.string.add_new);
        lvHidden.setExpanded(true);
        lvActivatedList.setExpanded(true);
    }

    @Override
    public void initListeners() {
//        customSwiperefreshLayout.setOnRefreshListener(this);
        tvAddNew.setOnClickListener(this);
    }

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, final AutoCompleteTextViewType autoCompleteTextViewType, List<Object> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
//                initClinicNameViewVisibility();
                final AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.drop_down_item_doctor_profile_my_clinic,
                        list, autoCompleteTextViewType);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (autoCompleteTextViewType) {
                            case DOCTOR_CLINIC:
                                Object object = adapter.getSelectedObject(position);
                                if (object != null && object instanceof DoctorClinicProfile) {
                                    DoctorClinicProfile doctorClinicProfile = (DoctorClinicProfile) object;
                                    refreshSelectedClinicProfileData(doctorClinicProfile);
                                }
                                break;
                        }

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshSelectedClinicProfileData(DoctorClinicProfile doctorClinicProfile) {
        if (doctorClinicProfile != null) {
            autotvClinicName.setText(Util.getValidatedValue(doctorClinicProfile.getLocationName()));
            user.setForeignHospitalId(doctorClinicProfile.getHospitalId());
            user.setForeignLocationId(doctorClinicProfile.getLocationId());
        }
        getListFromLocal(true, null);
    }

    private DoctorClinicProfile getDoctorClinicProfileFromList(String locationId, List<DoctorClinicProfile> list) {
        for (DoctorClinicProfile doctorClinicProfile :
                list) {
            if (doctorClinicProfile.getLocationId().equalsIgnoreCase(locationId))
                return doctorClinicProfile;
        }
        return list.get(0);
    }

    private void initScrollView() {
        svContainer.addFixedHeader(tvHeader);
        svContainer.addChildHeaders(tvHeaderTwo);
        svContainer.addChildHeaders(tvHeaderOne);
        svContainer.build();
    }

    private void initAdapters() {
        adapterHidden = new NameHideActivateAdapter(mActivity, this, NameHideActivateAdapter.HIDDEN_LIST_ADAPTER);
        adapterHidden.setListData(nameHideActivateType, listHidden);
        lvHidden.setAdapter(adapterHidden);

        adapterActivated = new NameHideActivateAdapter(mActivity, this, NameHideActivateAdapter.ACTIVATED_LIST_ADAPTER);
        adapterActivated.setListData(nameHideActivateType, listActivated);
        lvActivatedList.setAdapter(adapterActivated);
    }

    private void notifyHiddenAdapter(List hiddenList) {
        if (!Util.isNullOrEmptyList(hiddenList)) {
            ComparatorUtil.commonDateComparator(nameHideActivateType.getClassType(), hiddenList);
            isHiddenListEmpty = false;
            svContainer.setHeaderVisiblilty(tvHeaderTwo.getId(), true);
            containerHideActivate.setVisibility(View.VISIBLE);
            tvNoDataFound.setVisibility(View.GONE);
            containerHiddenList.setVisibility(View.VISIBLE);
            adapterHidden.setListData(nameHideActivateType, hiddenList);
            adapterHidden.notifyDataSetChanged();
            ListViewSizeHelper.getListViewSize(lvHidden);
        } else {
            svContainer.setHeaderVisiblilty(tvHeaderTwo.getId(), false);
            containerHiddenList.setVisibility(View.GONE);
            isHiddenListEmpty = true;
            if (isActivatedListEmpty) {
                containerHideActivate.setVisibility(View.GONE);
                tvNoDataFound.setVisibility(View.VISIBLE);
            }
        }
    }

    private void notifyActivatedAdapter(List<?> activatedList) {
        if (!Util.isNullOrEmptyList(activatedList)) {
            ComparatorUtil.commonDateComparator(nameHideActivateType.getClassType(), activatedList);
            isActivatedListEmpty = false;
            containerHideActivate.setVisibility(View.VISIBLE);
            tvNoDataFound.setVisibility(View.GONE);
            containerActivatedList.setVisibility(View.VISIBLE);
            adapterActivated.setListData(nameHideActivateType, activatedList);
            adapterActivated.notifyDataSetChanged();
            ListViewSizeHelper.getListViewSize(lvActivatedList);
        } else {
            containerActivatedList.setVisibility(View.GONE);
            isActivatedListEmpty = true;
            if (isHiddenListEmpty) {
                containerHideActivate.setVisibility(View.GONE);
                tvNoDataFound.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getListFromLocal(boolean isInitialLoading, VolleyResponseBean response) {
        this.isInitialLoading = isInitialLoading;
        if (isInitialLoading)
            mActivity.showLoading(false);
        isHiddenListEmpty = false;
        isActivatedListEmpty = false;
//        mActivity.showLoading();
        switch (nameHideActivateType) {
            case REFERENCE:
//                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_REFERENCE_LIST_ALL, this, this, this).execute();
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_REFERENCE_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_REFERENCE_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case DIRECTION:
//                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_REFERENCE_LIST_ALL, this, this, this).execute();
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DIRECTION_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DIRECTION_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case HISTORY:
//                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_REFERENCE_LIST_ALL, this, this, this).execute();
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DISEASE_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DISEASE_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case FREQUENCY:
//                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_REFERENCE_LIST_ALL, this, this, this).execute();
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FREQUENCY_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FREQUENCY_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case DRUG:
//                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_REFERENCE_LIST_ALL, this, this, this).execute();
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DRUG_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DRUG_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case PRESENT_COMPLAINT:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PRESENT_COMPLAINT_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PRESENT_COMPLAINT_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case COMPLAINT:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_COMPLAINT_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_COMPLAINT_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case HISTORY_OF_PRESENT_COMPLAINT:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_HISTORY_OF_PRESENT_COMPLAINT_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_HISTORY_OF_PRESENT_COMPLAINT_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case MENSTRUAL_HISTORY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_MENSTRUAL_HISTORY_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_MENSTRUAL_HISTORY_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case OBSTETRIC_HISTORY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_OBSTETRIC_HISTORY_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_OBSTETRIC_HISTORY_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case GENERAL_EXAMINATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_GENERAL_EXAMINATION_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_GENERAL_EXAMINATION_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case SYSTEMIC_EXAMINATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_SYSTEMIC_EXAMINATION_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_SYSTEMIC_EXAMINATION_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case OBSERVATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_OBSERVATION_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_OBSERVATION_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case INVESTIGATIONS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INVESTIGATIONS_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INVESTIGATIONS_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case PROVISIONAL_DIAGNOSIS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PROVISIONAL_DIAGNOSIS_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PROVISIONAL_DIAGNOSIS_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case DIAGNOSIS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DIAGNOSIS_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DIAGNOSIS_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case ECG:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_ECG_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_ECG_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case ECHO:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_ECHO_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_ECHO_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case XRAY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_XRAY_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_XRAY_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case HOLTER:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_HOLTER_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_HOLTER_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case NOTES:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_NOTES_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_NOTES_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case INDICATION_OF_USG:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INDICATION_OF_USG_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INDICATION_OF_USG_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case PA:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PA_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PA_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case PS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PS_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PS_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case PV:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PV_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PV_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
        }
    }

    @Override
    public void onHideClicked(NameHideActivateType nameHideActivateType, Object object) {
        Util.checkNetworkStatus(mActivity);
        LogUtils.LOGD(TAG, "" + nameHideActivateType);
        if (HealthCocoConstants.isNetworkOnline) {
            if (object != null)
                showConfirmationAlert(object, this.nameHideActivateType);
        } else
            onNetworkUnavailable(null);
    }

    public void showConfirmationAlert(final Object object, final NameHideActivateType nameHideActivateType) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(getResources().getString(
                R.string.are_you_sure_you_Want_to_hide_this) + getResources().getString(nameHideActivateType.getTitleId()) + "?");
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                continueHideItem(object, nameHideActivateType);
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

    private void continueHideItem(Object object, NameHideActivateType nameHideActivateType) {
        switch (nameHideActivateType) {
            case REFERENCE:
                if (object instanceof Reference) {
                    Reference reference = (Reference) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_REFERENCE,
                            Reference.class, user.getUniqueId(), reference.getLocationId(),
                            reference.getHospitalId(), reference.getUniqueId(), true, this, this);
                }
                break;
            case DIRECTION:
                if (object instanceof DrugDirection) {
                    DrugDirection direction = (DrugDirection) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_DIRECTION,
                            DrugDirection.class, user.getUniqueId(), direction.getLocationId(),
                            direction.getHospitalId(), direction.getUniqueId(), true, this, this);
                }
                break;
            case HISTORY:
                if (object instanceof Disease) {
                    Disease disease = (Disease) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_DISEASE,
                            HistoryPresentComplaintSuggestions.class, user.getUniqueId(), disease.getLocationId(),
                            disease.getHospitalId(), disease.getUniqueId(), true, this, this);
                }
                break;
            case FREQUENCY:
                if (object instanceof DrugDosage) {
                    DrugDosage drugDosage = (DrugDosage) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_DRUG_DOSAGE,
                            DrugDosage.class, user.getUniqueId(), drugDosage.getLocationId(),
                            drugDosage.getHospitalId(), drugDosage.getUniqueId(), true, this, this);
                }
                break;
            case DRUG:
                if (object instanceof Drug) {
                    Drug drug = (Drug) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_DRUG,
                            Drug.class, user.getUniqueId(), drug.getLocationId(),
                            drug.getHospitalId(), drug.getUniqueId(), true, this, this);
                }
                break;
            case PRESENT_COMPLAINT:
                if (object instanceof PresentComplaintSuggestions) {
                    PresentComplaintSuggestions presentComplaintSuggestions = (PresentComplaintSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PRESENT_COMPLAINT,
                            PresentComplaintSuggestions.class, user.getUniqueId(), presentComplaintSuggestions.getLocationId(),
                            presentComplaintSuggestions.getHospitalId(), presentComplaintSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case COMPLAINT:
                if (object instanceof ComplaintSuggestions) {
                    ComplaintSuggestions complaintSuggestions = (ComplaintSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_COMPLAINT,
                            ComplaintSuggestions.class, user.getUniqueId(), complaintSuggestions.getLocationId(),
                            complaintSuggestions.getHospitalId(), complaintSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case HISTORY_OF_PRESENT_COMPLAINT:
                if (object instanceof HistoryPresentComplaintSuggestions) {
                    HistoryPresentComplaintSuggestions historyDetailsResponse = (HistoryPresentComplaintSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_HISTORY_OF_PRESENT_COMPLAINT,
                            HistoryPresentComplaintSuggestions.class, user.getUniqueId(), historyDetailsResponse.getLocationId(),
                            historyDetailsResponse.getHospitalId(), historyDetailsResponse.getUniqueId(), true, this, this);
                }
                break;
            case MENSTRUAL_HISTORY:
                if (object instanceof MenstrualHistorySuggestions) {
                    MenstrualHistorySuggestions menstrualHistorySuggestions = (MenstrualHistorySuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_MENSTRUAL_HISTORY,
                            MenstrualHistorySuggestions.class, user.getUniqueId(), menstrualHistorySuggestions.getLocationId(),
                            menstrualHistorySuggestions.getHospitalId(), menstrualHistorySuggestions.getUniqueId(), true, this, this);
                }
                break;
            case OBSTETRIC_HISTORY:
                if (object instanceof ObstetricHistorySuggestions) {
                    ObstetricHistorySuggestions obstetricHistorySuggestions = (ObstetricHistorySuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_OBSTETRIC_HISTORY,
                            ObstetricHistorySuggestions.class, user.getUniqueId(), obstetricHistorySuggestions.getLocationId(),
                            obstetricHistorySuggestions.getHospitalId(), obstetricHistorySuggestions.getUniqueId(), true, this, this);
                }
                break;
            case GENERAL_EXAMINATION:
                if (object instanceof GeneralExaminationSuggestions) {
                    GeneralExaminationSuggestions generalExaminationSuggestions = (GeneralExaminationSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_GENERAL_EXAMINATION,
                            GeneralExaminationSuggestions.class, user.getUniqueId(), generalExaminationSuggestions.getLocationId(),
                            generalExaminationSuggestions.getHospitalId(), generalExaminationSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case SYSTEMIC_EXAMINATION:
                if (object instanceof SystemicExaminationSuggestions) {
                    SystemicExaminationSuggestions systemicExaminationSuggestions = (SystemicExaminationSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_SYSTEMIC_EXAMINATION,
                            SystemicExaminationSuggestions.class, user.getUniqueId(), systemicExaminationSuggestions.getLocationId(),
                            systemicExaminationSuggestions.getHospitalId(), systemicExaminationSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case OBSERVATION:
                if (object instanceof ObservationSuggestions) {
                    ObservationSuggestions observationSuggestions = (ObservationSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_OBSERVATION,
                            ObservationSuggestions.class, user.getUniqueId(), observationSuggestions.getLocationId(),
                            observationSuggestions.getHospitalId(), observationSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case INVESTIGATIONS:
                if (object instanceof InvestigationSuggestions) {
                    InvestigationSuggestions investigationSuggestions = (InvestigationSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_INVESTIGATION,
                            InvestigationSuggestions.class, user.getUniqueId(), investigationSuggestions.getLocationId(),
                            investigationSuggestions.getHospitalId(), investigationSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case PROVISIONAL_DIAGNOSIS:
                if (object instanceof ProvisionalDiagnosisSuggestions) {
                    ProvisionalDiagnosisSuggestions provisionalDiagnosisSuggestions = (ProvisionalDiagnosisSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PROVISIONAL_DIAGNOSIS,
                            ProvisionalDiagnosisSuggestions.class, user.getUniqueId(), provisionalDiagnosisSuggestions.getLocationId(),
                            provisionalDiagnosisSuggestions.getHospitalId(), provisionalDiagnosisSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case DIAGNOSIS:
                if (object instanceof DiagnosisSuggestions) {
                    DiagnosisSuggestions diagnosisSuggestions = (DiagnosisSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PROVISIONAL_DIAGNOSIS,
                            DiagnosisSuggestions.class, user.getUniqueId(), diagnosisSuggestions.getLocationId(),
                            diagnosisSuggestions.getHospitalId(), diagnosisSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case ECG:
                if (object instanceof EcgDetailSuggestions) {
                    EcgDetailSuggestions ecgDetailSuggestions = (EcgDetailSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_ECG,
                            EcgDetailSuggestions.class, user.getUniqueId(), ecgDetailSuggestions.getLocationId(),
                            ecgDetailSuggestions.getHospitalId(), ecgDetailSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case ECHO:
                if (object instanceof EchoSuggestions) {
                    EchoSuggestions echoSuggestions = (EchoSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_ECHO,
                            EchoSuggestions.class, user.getUniqueId(), echoSuggestions.getLocationId(),
                            echoSuggestions.getHospitalId(), echoSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case XRAY:
                if (object instanceof XrayDetailSuggestions) {
                    XrayDetailSuggestions xrayDetailSuggestions = (XrayDetailSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_XRAY,
                            XrayDetailSuggestions.class, user.getUniqueId(), xrayDetailSuggestions.getLocationId(),
                            xrayDetailSuggestions.getHospitalId(), xrayDetailSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case HOLTER:
                if (object instanceof HolterSuggestions) {
                    HolterSuggestions holterSuggestions = (HolterSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_HOLTER,
                            HolterSuggestions.class, user.getUniqueId(), holterSuggestions.getLocationId(),
                            holterSuggestions.getHospitalId(), holterSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case NOTES:
                if (object instanceof NotesSuggestions) {
                    NotesSuggestions notesSuggestions = (NotesSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_NOTES,
                            NotesSuggestions.class, user.getUniqueId(), notesSuggestions.getLocationId(),
                            notesSuggestions.getHospitalId(), notesSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case INDICATION_OF_USG:
                if (object instanceof IndicationOfUsgSuggestions) {
                    IndicationOfUsgSuggestions indicationOfUsgSuggestions = (IndicationOfUsgSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_INDICATION_OF_USG,
                            IndicationOfUsgSuggestions.class, user.getUniqueId(), indicationOfUsgSuggestions.getLocationId(),
                            indicationOfUsgSuggestions.getHospitalId(), indicationOfUsgSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case PA:
                if (object instanceof PaSuggestions) {
                    PaSuggestions paSuggestions = (PaSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PA,
                            PaSuggestions.class, user.getUniqueId(), paSuggestions.getLocationId(),
                            paSuggestions.getHospitalId(), paSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case PS:
                if (object instanceof PsSuggestions) {
                    PsSuggestions psSuggestions = (PsSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PS,
                            PsSuggestions.class, user.getUniqueId(), psSuggestions.getLocationId(),
                            psSuggestions.getHospitalId(), psSuggestions.getUniqueId(), true, this, this);
                }
                break;
            case PV:
                if (object instanceof PvSuggestions) {
                    PvSuggestions pvSuggestions = (PvSuggestions) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PV,
                            PvSuggestions.class, user.getUniqueId(), pvSuggestions.getLocationId(),
                            pvSuggestions.getHospitalId(), pvSuggestions.getUniqueId(), true, this, this);
                }
                break;
        }
    }

    @Override
    public void onActivateClicked(NameHideActivateType itemType, Object object) {
        LogUtils.LOGD(TAG, "" + itemType);
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            if (object != null)
                switch (nameHideActivateType) {
                    case REFERENCE:
                        if (object instanceof Reference) {
                            Reference reference = (Reference) object;
                            isHideClicked = false;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_REFERENCE,
                                    Reference.class, user.getUniqueId(), reference.getLocationId(),
                                    reference.getHospitalId(), reference.getUniqueId(), false, this, this);
                        }
                        break;
                    case DIRECTION:
                        if (object instanceof DrugDirection) {
                            DrugDirection direction = (DrugDirection) object;
                            isHideClicked = false;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_DIRECTION,
                                    Disease.class, user.getUniqueId(), direction.getLocationId(),
                                    direction.getHospitalId(), direction.getUniqueId(), false, this, this);
                        }
                        break;
                    case HISTORY:
                        if (object instanceof Disease) {
                            Disease disease = (Disease) object;
                            isHideClicked = false;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_DISEASE,
                                    HistoryPresentComplaintSuggestions.class, user.getUniqueId(), disease.getLocationId(),
                                    disease.getHospitalId(), disease.getUniqueId(), false, this, this);
                        }
                        break;
                    case FREQUENCY:
                        if (object instanceof DrugDosage) {
                            DrugDosage drugDosage = (DrugDosage) object;
                            isHideClicked = false;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_DRUG_DOSAGE,
                                    DrugDosage.class, user.getUniqueId(), drugDosage.getLocationId(),
                                    drugDosage.getHospitalId(), drugDosage.getUniqueId(), false, this, this);
                        }
                        break;
                    case DRUG:
                        if (object instanceof Drug) {
                            Drug drug = (Drug) object;
                            isHideClicked = false;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_DRUG,
                                    Drug.class, user.getUniqueId(), drug.getLocationId(),
                                    drug.getHospitalId(), drug.getUniqueId(), false, this, this);
                        }
                        break;
                    case PRESENT_COMPLAINT:
                        if (object instanceof PresentComplaintSuggestions) {
                            PresentComplaintSuggestions presentComplaintSuggestions = (PresentComplaintSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PRESENT_COMPLAINT,
                                    PresentComplaintSuggestions.class, user.getUniqueId(), presentComplaintSuggestions.getLocationId(),
                                    presentComplaintSuggestions.getHospitalId(), presentComplaintSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case COMPLAINT:
                        if (object instanceof ComplaintSuggestions) {
                            ComplaintSuggestions complaintSuggestions = (ComplaintSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_COMPLAINT,
                                    ComplaintSuggestions.class, user.getUniqueId(), complaintSuggestions.getLocationId(),
                                    complaintSuggestions.getHospitalId(), complaintSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case HISTORY_OF_PRESENT_COMPLAINT:
                        if (object instanceof HistoryPresentComplaintSuggestions) {
                            HistoryPresentComplaintSuggestions historyDetailsResponse = (HistoryPresentComplaintSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_HISTORY_OF_PRESENT_COMPLAINT,
                                    HistoryPresentComplaintSuggestions.class, user.getUniqueId(), historyDetailsResponse.getLocationId(),
                                    historyDetailsResponse.getHospitalId(), historyDetailsResponse.getUniqueId(), false, this, this);
                        }
                        break;
                    case MENSTRUAL_HISTORY:
                        if (object instanceof MenstrualHistorySuggestions) {
                            MenstrualHistorySuggestions menstrualHistorySuggestions = (MenstrualHistorySuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_MENSTRUAL_HISTORY,
                                    MenstrualHistorySuggestions.class, user.getUniqueId(), menstrualHistorySuggestions.getLocationId(),
                                    menstrualHistorySuggestions.getHospitalId(), menstrualHistorySuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case OBSTETRIC_HISTORY:
                        if (object instanceof ObstetricHistorySuggestions) {
                            ObstetricHistorySuggestions obstetricHistorySuggestions = (ObstetricHistorySuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_OBSTETRIC_HISTORY,
                                    ObstetricHistorySuggestions.class, user.getUniqueId(), obstetricHistorySuggestions.getLocationId(),
                                    obstetricHistorySuggestions.getHospitalId(), obstetricHistorySuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case GENERAL_EXAMINATION:
                        if (object instanceof GeneralExaminationSuggestions) {
                            GeneralExaminationSuggestions generalExaminationSuggestions = (GeneralExaminationSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_GENERAL_EXAMINATION,
                                    GeneralExaminationSuggestions.class, user.getUniqueId(), generalExaminationSuggestions.getLocationId(),
                                    generalExaminationSuggestions.getHospitalId(), generalExaminationSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case SYSTEMIC_EXAMINATION:
                        if (object instanceof SystemicExaminationSuggestions) {
                            SystemicExaminationSuggestions systemicExaminationSuggestions = (SystemicExaminationSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_SYSTEMIC_EXAMINATION,
                                    SystemicExaminationSuggestions.class, user.getUniqueId(), systemicExaminationSuggestions.getLocationId(),
                                    systemicExaminationSuggestions.getHospitalId(), systemicExaminationSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case OBSERVATION:
                        if (object instanceof ObservationSuggestions) {
                            ObservationSuggestions observationSuggestions = (ObservationSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_OBSERVATION,
                                    ObservationSuggestions.class, user.getUniqueId(), observationSuggestions.getLocationId(),
                                    observationSuggestions.getHospitalId(), observationSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case INVESTIGATIONS:
                        if (object instanceof InvestigationSuggestions) {
                            InvestigationSuggestions investigationSuggestions = (InvestigationSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_INVESTIGATION,
                                    InvestigationSuggestions.class, user.getUniqueId(), investigationSuggestions.getLocationId(),
                                    investigationSuggestions.getHospitalId(), investigationSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case PROVISIONAL_DIAGNOSIS:
                        if (object instanceof ProvisionalDiagnosisSuggestions) {
                            ProvisionalDiagnosisSuggestions provisionalDiagnosisSuggestions = (ProvisionalDiagnosisSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PROVISIONAL_DIAGNOSIS,
                                    ProvisionalDiagnosisSuggestions.class, user.getUniqueId(), provisionalDiagnosisSuggestions.getLocationId(),
                                    provisionalDiagnosisSuggestions.getHospitalId(), provisionalDiagnosisSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case DIAGNOSIS:
                        if (object instanceof DiagnosisSuggestions) {
                            DiagnosisSuggestions diagnosisSuggestions = (DiagnosisSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PROVISIONAL_DIAGNOSIS,
                                    DiagnosisSuggestions.class, user.getUniqueId(), diagnosisSuggestions.getLocationId(),
                                    diagnosisSuggestions.getHospitalId(), diagnosisSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case ECG:
                        if (object instanceof EcgDetailSuggestions) {
                            EcgDetailSuggestions ecgDetailSuggestions = (EcgDetailSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_ECG,
                                    EcgDetailSuggestions.class, user.getUniqueId(), ecgDetailSuggestions.getLocationId(),
                                    ecgDetailSuggestions.getHospitalId(), ecgDetailSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case ECHO:
                        if (object instanceof EchoSuggestions) {
                            EchoSuggestions echoSuggestions = (EchoSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_ECHO,
                                    EchoSuggestions.class, user.getUniqueId(), echoSuggestions.getLocationId(),
                                    echoSuggestions.getHospitalId(), echoSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case XRAY:
                        if (object instanceof XrayDetailSuggestions) {
                            XrayDetailSuggestions xrayDetailSuggestions = (XrayDetailSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_XRAY,
                                    XrayDetailSuggestions.class, user.getUniqueId(), xrayDetailSuggestions.getLocationId(),
                                    xrayDetailSuggestions.getHospitalId(), xrayDetailSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case HOLTER:
                        if (object instanceof HolterSuggestions) {
                            HolterSuggestions holterSuggestions = (HolterSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_HOLTER,
                                    HolterSuggestions.class, user.getUniqueId(), holterSuggestions.getLocationId(),
                                    holterSuggestions.getHospitalId(), holterSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case NOTES:
                        if (object instanceof NotesSuggestions) {
                            NotesSuggestions notesSuggestions = (NotesSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_NOTES,
                                    NotesSuggestions.class, user.getUniqueId(), notesSuggestions.getLocationId(),
                                    notesSuggestions.getHospitalId(), notesSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case INDICATION_OF_USG:
                        if (object instanceof IndicationOfUsgSuggestions) {
                            IndicationOfUsgSuggestions indicationOfUsgSuggestions = (IndicationOfUsgSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_INDICATION_OF_USG,
                                    IndicationOfUsgSuggestions.class, user.getUniqueId(), indicationOfUsgSuggestions.getLocationId(),
                                    indicationOfUsgSuggestions.getHospitalId(), indicationOfUsgSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case PA:
                        if (object instanceof PaSuggestions) {
                            PaSuggestions paSuggestions = (PaSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PA,
                                    PaSuggestions.class, user.getUniqueId(), paSuggestions.getLocationId(),
                                    paSuggestions.getHospitalId(), paSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case PS:
                        if (object instanceof PsSuggestions) {
                            PsSuggestions psSuggestions = (PsSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PS,
                                    PsSuggestions.class, user.getUniqueId(), psSuggestions.getLocationId(),
                                    psSuggestions.getHospitalId(), psSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                    case PV:
                        if (object instanceof PvSuggestions) {
                            PvSuggestions pvSuggestions = (PvSuggestions) object;
                            isHideClicked = true;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_PV,
                                    PvSuggestions.class, user.getUniqueId(), pvSuggestions.getLocationId(),
                                    pvSuggestions.getHospitalId(), pvSuggestions.getUniqueId(), false, this, this);
                        }
                        break;
                }
        } else onNetworkUnavailable(null);
    }

    @Override
    public boolean isInitialLoading() {
//        return isRefreshAfterApiSuccess;
        return false;
    }

    @Override
    public void onEditClicked(NameHideActivateType itemType, Object object) {
        if (object != null)
            switch (nameHideActivateType) {
                case REFERENCE:
                    mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_REFERENCE, AddUpdateNameDialogType.REFERENCE, this, user, ((Reference) object).getUniqueId(), ((Reference) object).getReference(), HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                    break;
                case DIRECTION:
                    mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_DIRECTION, AddUpdateNameDialogType.DIRECTION, this, user, ((DrugDirection) object).getUniqueId(), ((DrugDirection) object).getDirection(), HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                    break;
                case FREQUENCY:
                    mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_DOSAGE, AddUpdateNameDialogType.FREQUENCY, this, user, ((DrugDosage) object).getUniqueId(), ((DrugDosage) object).getDosage(), HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                    break;
                case HISTORY:
                    mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_CUSTOM_HISTORY, AddUpdateNameDialogType.HISTORY, this, user, ((Disease) object).getUniqueId(), ((Disease) object).getDisease(), HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                    break;
                case DRUG:
                    mActivity.openAddNewDrugFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                    break;
                case TREATMENT:
                    mActivity.openAddNewTreatmentsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, ((TreatmentService) object).getUniqueId());
                    break;
                case PRESENT_COMPLAINT:
                    PresentComplaintSuggestions presentComplaintSuggestions = (PresentComplaintSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PRESENT_COMPLAINT, presentComplaintSuggestions.getPresentComplaint(), presentComplaintSuggestions.getUniqueId());
                    break;
                case COMPLAINT:
                    ComplaintSuggestions complaint = (ComplaintSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.COMPLAINTS, complaint.getComplaint(), complaint.getUniqueId());
                    break;
                case HISTORY_OF_PRESENT_COMPLAINT:
                    HistoryPresentComplaintSuggestions historyPresentComplaintSuggestions = (HistoryPresentComplaintSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.HISTORY_OF_PRESENT_COMPLAINT, historyPresentComplaintSuggestions.getPresentComplaintHistory(), historyPresentComplaintSuggestions.getUniqueId());
                    break;
                case MENSTRUAL_HISTORY:
                    MenstrualHistorySuggestions menstrualHistorySuggestions = (MenstrualHistorySuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.MENSTRUAL_HISTORY, menstrualHistorySuggestions.getMenstrualHistory(), menstrualHistorySuggestions.getUniqueId());
                    break;
                case OBSTETRIC_HISTORY:
                    ObstetricHistorySuggestions obstetricHistorySuggestions = (ObstetricHistorySuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.OBSTETRIC_HISTORY, obstetricHistorySuggestions.getObstetricHistory(), obstetricHistorySuggestions.getUniqueId());
                    break;
                case GENERAL_EXAMINATION:
                    GeneralExaminationSuggestions generalExaminationSuggestions = (GeneralExaminationSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.GENERAL_EXAMINATION, generalExaminationSuggestions.getGeneralExam(), generalExaminationSuggestions.getUniqueId());
                    break;
                case SYSTEMIC_EXAMINATION:
                    SystemicExaminationSuggestions systemicExaminationSuggestions = (SystemicExaminationSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.SYSTEMIC_EXAMINATION, systemicExaminationSuggestions.getSystemExam(), systemicExaminationSuggestions.getUniqueId());
                    break;
                case OBSERVATION:
                    ObservationSuggestions observation = (ObservationSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.OBSERVATION, observation.getObservation(), observation.getUniqueId());
                    break;
                case INVESTIGATIONS:
                    InvestigationSuggestions investigationSuggestions = (InvestigationSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.INVESTIGATION, investigationSuggestions.getInvestigation(), investigationSuggestions.getUniqueId());
                    break;
                case PROVISIONAL_DIAGNOSIS:
                    ProvisionalDiagnosisSuggestions provisionalDiagnosisSuggestions = (ProvisionalDiagnosisSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PROVISIONAL_DIAGNOSIS, provisionalDiagnosisSuggestions.getProvisionalDiagnosis(), provisionalDiagnosisSuggestions.getUniqueId());
                    break;
                case DIAGNOSIS:
                    DiagnosisSuggestions diagnosisSuggestions = (DiagnosisSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.DIAGNOSIS, diagnosisSuggestions.getDiagnosis(), diagnosisSuggestions.getUniqueId());
                    break;
                case ECG:
                    EcgDetailSuggestions ecgDetailSuggestions = (EcgDetailSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.ECG_DETAILS, ecgDetailSuggestions.getEcgDetails(), ecgDetailSuggestions.getUniqueId());
                    break;
                case ECHO:
                    EchoSuggestions echoSuggestions = (EchoSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.ECHO, echoSuggestions.getEcho(), echoSuggestions.getUniqueId());
                    break;
                case XRAY:
                    XrayDetailSuggestions xrayDetailSuggestions = (XrayDetailSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.X_RAY_DETAILS, xrayDetailSuggestions.getxRayDetails(), xrayDetailSuggestions.getUniqueId());
                    break;
                case HOLTER:
                    HolterSuggestions holterSuggestions = (HolterSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.HOLTER, holterSuggestions.getHolter(), holterSuggestions.getUniqueId());
                    break;
                case INDICATION_OF_USG:
                    IndicationOfUsgSuggestions indicationOfUsgSuggestions = (IndicationOfUsgSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.INDICATION_OF_USG, indicationOfUsgSuggestions.getIndicationOfUSG(), indicationOfUsgSuggestions.getUniqueId());
                    break;
                case NOTES:
                    NotesSuggestions notesSuggestions = (NotesSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.NOTES, notesSuggestions.getNote(), notesSuggestions.getUniqueId());
                    break;
                case PA:
                    PaSuggestions paSuggestions = (PaSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PA, paSuggestions.getPa(), paSuggestions.getUniqueId());
                    break;
                case PS:
                    PsSuggestions psSuggestions = (PsSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PS, psSuggestions.getPs(), psSuggestions.getUniqueId());
                    break;
                case PV:
                    PvSuggestions pvSuggestions = (PvSuggestions) object;
                    mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PV, pvSuggestions.getPv(), pvSuggestions.getUniqueId());
                    break;

            }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_advance_search:
                switch (nameHideActivateType) {
                    case REFERENCE:
                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_REFERENCE, AddUpdateNameDialogType.REFERENCE, this, user, "", HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                        break;
                    case DIRECTION:
                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_DIRECTION, AddUpdateNameDialogType.DIRECTION, this, user, "", HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                        break;
                    case FREQUENCY:
                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_DOSAGE, AddUpdateNameDialogType.FREQUENCY, this, user, "", HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                        break;
                    case HISTORY:
                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_CUSTOM_HISTORY, AddUpdateNameDialogType.HISTORY, this, user, "", HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                        break;
                    case DRUG:
                        mActivity.openAddNewDrugFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                        break;
                    case TREATMENT:
                        mActivity.openAddNewTreatmentsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, null);
                        break;
                    case PRESENT_COMPLAINT:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PRESENT_COMPLAINT);
                        break;
                    case COMPLAINT:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.COMPLAINTS);
                        break;
                    case HISTORY_OF_PRESENT_COMPLAINT:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.HISTORY_OF_PRESENT_COMPLAINT);
                        break;
                    case MENSTRUAL_HISTORY:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.MENSTRUAL_HISTORY);
                        break;
                    case OBSTETRIC_HISTORY:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.OBSTETRIC_HISTORY);
                        break;
                    case GENERAL_EXAMINATION:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.GENERAL_EXAMINATION);
                        break;
                    case SYSTEMIC_EXAMINATION:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.SYSTEMIC_EXAMINATION);
                        break;
                    case OBSERVATION:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.OBSERVATION);
                        break;
                    case INVESTIGATIONS:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.INVESTIGATION);
                        break;
                    case PROVISIONAL_DIAGNOSIS:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PROVISIONAL_DIAGNOSIS);
                        break;
                    case DIAGNOSIS:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.DIAGNOSIS);
                        break;
                    case ECG:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.ECG_DETAILS);
                        break;
                    case ECHO:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.ECHO);
                        break;
                    case XRAY:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.X_RAY_DETAILS);
                        break;
                    case HOLTER:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.HOLTER);
                        break;
                    case INDICATION_OF_USG:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.INDICATION_OF_USG);
                        break;
                    case NOTES:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.NOTES);
                        break;
                    case PA:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PA);
                        break;
                    case PS:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PS);
                        break;
                    case PV:
                        mActivity.openAddNewSuggestionsFragment(this, HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, SuggestionType.PV);
                        break;
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String search = String.valueOf(s).toUpperCase(Locale.ENGLISH);
        ArrayList tempListActivated = getSearchedList(search, listActivated);
        notifyActivatedAdapter(tempListActivated);

        ArrayList tempListHidden = getSearchedList(search, listHidden);
        notifyHiddenAdapter(tempListHidden);
    }

    private ArrayList getSearchedList(String search, List<Object> list) {
        ArrayList tempList = new ArrayList<>();
        if (!Util.isNullOrEmptyList(list)) {
            if (search.length() == 0) {
                tempList.addAll(list);
            } else {
                for (Object object : list) {
                    switch (nameHideActivateType) {
                        case REFERENCE:
                            if (object instanceof Reference) {
                                Reference reference = (Reference) object;
                                if (!Util.isNullOrBlank(reference.getReference()) && reference.getReference().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case HISTORY:
                            if (object instanceof Disease) {
                                Disease disease = (Disease) object;
                                if (!Util.isNullOrBlank(disease.getDisease()) && disease.getDisease().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case DIRECTION:
                            if (object instanceof DrugDirection) {
                                DrugDirection direction = (DrugDirection) object;
                                if (!Util.isNullOrBlank(direction.getDirection()) && direction.getDirection().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case FREQUENCY:
                            if (object instanceof DrugDosage) {
                                DrugDosage dosage = (DrugDosage) object;
                                if (!Util.isNullOrBlank(dosage.getDosage()) && dosage.getDosage().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case DRUG:
                            if (object instanceof Drug) {
                                Drug drug = (Drug) object;
                                if (!Util.isNullOrBlank(drug.getDrugName()) && drug.getDrugName().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case PRESENT_COMPLAINT:
                            if (object instanceof PresentComplaintSuggestions) {
                                PresentComplaintSuggestions presentComplaintSuggestions = (PresentComplaintSuggestions) object;
                                if (!Util.isNullOrBlank(presentComplaintSuggestions.getPresentComplaint()) && presentComplaintSuggestions.getPresentComplaint().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case COMPLAINT:
                            if (object instanceof ComplaintSuggestions) {
                                ComplaintSuggestions complaintSuggestions = (ComplaintSuggestions) object;
                                if (!Util.isNullOrBlank(complaintSuggestions.getComplaint()) && complaintSuggestions.getComplaint().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case HISTORY_OF_PRESENT_COMPLAINT:
                            if (object instanceof HistoryPresentComplaintSuggestions) {
                                HistoryPresentComplaintSuggestions historyPresentComplaintSuggestions = (HistoryPresentComplaintSuggestions) object;
                                if (!Util.isNullOrBlank(historyPresentComplaintSuggestions.getPresentComplaintHistory()) && historyPresentComplaintSuggestions.getPresentComplaintHistory().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case MENSTRUAL_HISTORY:
                            if (object instanceof MenstrualHistorySuggestions) {
                                MenstrualHistorySuggestions menstrualHistorySuggestions = (MenstrualHistorySuggestions) object;
                                if (!Util.isNullOrBlank(menstrualHistorySuggestions.getMenstrualHistory()) && menstrualHistorySuggestions.getMenstrualHistory().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case OBSTETRIC_HISTORY:
                            if (object instanceof ObstetricHistorySuggestions) {
                                ObstetricHistorySuggestions obstetricHistorySuggestions = (ObstetricHistorySuggestions) object;
                                if (!Util.isNullOrBlank(obstetricHistorySuggestions.getObstetricHistory()) && obstetricHistorySuggestions.getObstetricHistory().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case GENERAL_EXAMINATION:
                            if (object instanceof GeneralExaminationSuggestions) {
                                GeneralExaminationSuggestions generalExaminationSuggestions = (GeneralExaminationSuggestions) object;
                                if (!Util.isNullOrBlank(generalExaminationSuggestions.getGeneralExam()) && generalExaminationSuggestions.getGeneralExam().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case SYSTEMIC_EXAMINATION:
                            if (object instanceof SystemicExaminationSuggestions) {
                                SystemicExaminationSuggestions systemicExaminationSuggestions = (SystemicExaminationSuggestions) object;
                                if (!Util.isNullOrBlank(systemicExaminationSuggestions.getSystemExam()) && systemicExaminationSuggestions.getSystemExam().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case OBSERVATION:
                            if (object instanceof ObservationSuggestions) {
                                ObservationSuggestions observationSuggestions = (ObservationSuggestions) object;
                                if (!Util.isNullOrBlank(observationSuggestions.getObservation()) && observationSuggestions.getObservation().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case INVESTIGATIONS:
                            if (object instanceof InvestigationSuggestions) {
                                InvestigationSuggestions investigationSuggestions = (InvestigationSuggestions) object;
                                if (!Util.isNullOrBlank(investigationSuggestions.getInvestigation()) && investigationSuggestions.getInvestigation().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case PROVISIONAL_DIAGNOSIS:
                            if (object instanceof ProvisionalDiagnosisSuggestions) {
                                ProvisionalDiagnosisSuggestions provisionalDiagnosisSuggestions = (ProvisionalDiagnosisSuggestions) object;
                                if (!Util.isNullOrBlank(provisionalDiagnosisSuggestions.getProvisionalDiagnosis()) && provisionalDiagnosisSuggestions.getProvisionalDiagnosis().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case DIAGNOSIS:
                            if (object instanceof DiagnosisSuggestions) {
                                DiagnosisSuggestions diagnosisSuggestions = (DiagnosisSuggestions) object;
                                if (!Util.isNullOrBlank(diagnosisSuggestions.getDiagnosis()) && diagnosisSuggestions.getDiagnosis().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case ECG:
                            if (object instanceof EcgDetailSuggestions) {
                                EcgDetailSuggestions ecgDetailSuggestions = (EcgDetailSuggestions) object;
                                if (!Util.isNullOrBlank(ecgDetailSuggestions.getEcgDetails()) && ecgDetailSuggestions.getEcgDetails().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case ECHO:
                            if (object instanceof EchoSuggestions) {
                                EchoSuggestions echoSuggestions = (EchoSuggestions) object;
                                if (!Util.isNullOrBlank(echoSuggestions.getEcho()) && echoSuggestions.getEcho().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case XRAY:
                            if (object instanceof XrayDetailSuggestions) {
                                XrayDetailSuggestions xrayDetailSuggestions = (XrayDetailSuggestions) object;
                                if (!Util.isNullOrBlank(xrayDetailSuggestions.getxRayDetails()) && xrayDetailSuggestions.getxRayDetails().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case HOLTER:
                            if (object instanceof HolterSuggestions) {
                                HolterSuggestions holterSuggestions = (HolterSuggestions) object;
                                if (!Util.isNullOrBlank(holterSuggestions.getHolter()) && holterSuggestions.getHolter().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case NOTES:
                            if (object instanceof NotesSuggestions) {
                                NotesSuggestions notesSuggestions = (NotesSuggestions) object;
                                if (!Util.isNullOrBlank(notesSuggestions.getNote()) && notesSuggestions.getNote().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case INDICATION_OF_USG:
                            if (object instanceof IndicationOfUsgSuggestions) {
                                IndicationOfUsgSuggestions indicationOfUsgSuggestions = (IndicationOfUsgSuggestions) object;
                                if (!Util.isNullOrBlank(indicationOfUsgSuggestions.getIndicationOfUSG()) && indicationOfUsgSuggestions.getIndicationOfUSG().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case PA:
                            if (object instanceof PaSuggestions) {
                                PaSuggestions paSuggestions = (PaSuggestions) object;
                                if (!Util.isNullOrBlank(paSuggestions.getPa()) && paSuggestions.getPa().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case PS:
                            if (object instanceof PsSuggestions) {
                                PsSuggestions psSuggestions = (PsSuggestions) object;
                                if (!Util.isNullOrBlank(psSuggestions.getPs()) && psSuggestions.getPs().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case PV:
                            if (object instanceof PvSuggestions) {
                                PvSuggestions pvSuggestions = (PvSuggestions) object;
                                if (!Util.isNullOrBlank(pvSuggestions.getPv()) && pvSuggestions.getPv().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                    }
                }
            }
        }
        return tempList;
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

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        DoctorClinicProfile doctorClinicProfile = null;
                        if (!Util.isNullOrEmptyList(doctorClinicsList))
                            switch (nameHideActivateType) {
                                case REFERENCE:
                                    autotvClinicName.setVisibility(View.VISIBLE);
                                    initAutoTvAdapter(autotvClinicName, AutoCompleteTextViewType.DOCTOR_CLINIC, (ArrayList<Object>) (ArrayList<?>) doctorClinicsList);
                                    String selectedLocationId = user.getForeignLocationId();
                                    doctorClinicProfile = getDoctorClinicProfileFromList(selectedLocationId, doctorClinicsList);
                                    break;
                                default:
                                    autotvClinicName.setVisibility(View.GONE);
                                    break;
                            }
                        refreshSelectedClinicProfileData(doctorClinicProfile);
                        return;
                    }
                    break;
                case GET_DRUG_DOSAGE:
                case GET_DISEASE_LIST:
                case GET_HISTORY_LIST:
                case GET_DIRECTION:
                case GET_DRUGS_LIST_CUSTOM:
                case GET_REFERENCE:
                case GET_PRESENT_COMPLAINT_SUGGESTIONS:
                case GET_COMPLAINT_SUGGESTIONS:
                case GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS:
                case GET_MENSTRUAL_HISTORY_SUGGESTIONS:
                case GET_OBSTETRIC_HISTORY_SUGGESTIONS:
                case GET_GENERAL_EXAMINATION_SUGGESTIONS:
                case GET_SYSTEMIC_EXAMINATION_SUGGESTIONS:
                case GET_OBSERVATION_SUGGESTIONS:
                case GET_INVESTIGATION_SUGGESTIONS:
                case GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS:
                case GET_DIAGNOSIS_SUGGESTIONS:
                case GET_ECG_SUGGESTIONS:
                case GET_ECHO_SUGGESTIONS:
                case GET_XRAY_SUGGESTIONS:
                case GET_HOLTER_SUGGESTIONS:
                case GET_NOTES_SUGGESTIONS:
                case GET_INDICATION_OF_USG_SUGGESTIONS:
                case GET_PA_SUGGESTIONS:
                case GET_PS_SUGGESTIONS:
                case GET_PV_SUGGESTIONS:
                    if (response.isDataFromLocal()) {
                        if (response.getLocalBackgroundTaskType() != null) {
                            switch (response.getLocalBackgroundTaskType()) {
                                case GET_DIRECTION_HIDDEN_LIST:
                                case GET_DRUG_HIDDEN_LIST:
                                case GET_FREQUENCY_HIDDEN_LIST:
                                case GET_DISEASE_HIDDEN_LIST:
                                case GET_REFERENCE_HIDDEN_LIST:
                                case GET_PRESENT_COMPLAINT_HIDDEN_LIST:
                                case GET_COMPLAINT_HIDDEN_LIST:
                                case GET_HISTORY_OF_PRESENT_COMPLAINT_HIDDEN_LIST:
                                case GET_MENSTRUAL_HISTORY_HIDDEN_LIST:
                                case GET_OBSTETRIC_HISTORY_HIDDEN_LIST:
                                case GET_GENERAL_EXAMINATION_HIDDEN_LIST:
                                case GET_SYSTEMIC_EXAMINATION_HIDDEN_LIST:
                                case GET_OBSERVATION_HIDDEN_LIST:
                                case GET_INVESTIGATIONS_HIDDEN_LIST:
                                case GET_PROVISIONAL_DIAGNOSIS_HIDDEN_LIST:
                                case GET_DIAGNOSIS_HIDDEN_LIST:
                                case GET_ECG_HIDDEN_LIST:
                                case GET_ECHO_HIDDEN_LIST:
                                case GET_XRAY_HIDDEN_LIST:
                                case GET_HOLTER_HIDDEN_LIST:
                                case GET_NOTES_HIDDEN_LIST:
                                case GET_INDICATION_OF_USG_HIDDEN_LIST:
                                case GET_PA_HIDDEN_LIST:
                                case GET_PS_HIDDEN_LIST:
                                case GET_PV_HIDDEN_LIST:
                                    listHidden = (ArrayList<Object>) (ArrayList<?>) response.getDataList();
                                    setSearchEditTextValue(getSearchEditTextValue());
                                    isHiddenListLoaded = true;
                                    break;
                                case GET_DIRECTION_ACTIVATED_LIST:
                                case GET_DRUG_ACTIVATED_LIST:
                                case GET_FREQUENCY_ACTIVATED_LIST:
                                case GET_DISEASE_ACTIVATED_LIST:
                                case GET_REFERENCE_ACTIVATED_LIST:
                                case GET_PRESENT_COMPLAINT_ACTIVATED_LIST:
                                case GET_COMPLAINT_ACTIVATED_LIST:
                                case GET_HISTORY_OF_PRESENT_COMPLAINT_ACTIVATED_LIST:
                                case GET_MENSTRUAL_HISTORY_ACTIVATED_LIST:
                                case GET_OBSTETRIC_HISTORY_ACTIVATED_LIST:
                                case GET_GENERAL_EXAMINATION_ACTIVATED_LIST:
                                case GET_SYSTEMIC_EXAMINATION_ACTIVATED_LIST:
                                case GET_OBSERVATION_ACTIVATED_LIST:
                                case GET_INVESTIGATIONS_ACTIVATED_LIST:
                                case GET_PROVISIONAL_DIAGNOSIS_ACTIVATED_LIST:
                                case GET_DIAGNOSIS_ACTIVATED_LIST:
                                case GET_ECG_ACTIVATED_LIST:
                                case GET_ECHO_ACTIVATED_LIST:
                                case GET_XRAY_ACTIVATED_LIST:
                                case GET_HOLTER_ACTIVATED_LIST:
                                case GET_NOTES_ACTIVATED_LIST:
                                case GET_INDICATION_OF_USG_ACTIVATED_LIST:
                                case GET_PA_ACTIVATED_LIST:
                                case GET_PS_ACTIVATED_LIST:
                                case GET_PV_ACTIVATED_LIST:
                                    listActivated = (ArrayList<Object>) (ArrayList<?>) response.getDataList();
                                    setSearchEditTextValue("");
                                    isActivatedListLoaded = true;
                                    break;
                            }
                            if (isHiddenListLoaded && isActivatedListLoaded && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                                initData();
                                return;
                            }
                        }
                    } else {
                        if (!Util.isNullOrEmptyList(response.getDataList())) {
                            response.setIsFromLocalAfterApiSuccess(true);
                            addToLocatDatabase(response);
                            return;

                        }
                        response.setIsFromLocalAfterApiSuccess(true);
                    }
                    if ((isHiddenListLoaded && isActivatedListLoaded) || response.isFromLocalAfterApiSuccess()) {
                        mActivity.hideLoading();
                        return;
                    }
                    break;
                case DELETE_DIRECTION:
                case DELETE_DRUG:
                case DELETE_DRUG_DOSAGE:
                case DELETE_DISEASE:
                case DELETE_REFERENCE:
                case DELETE_PRESENT_COMPLAINT:
                case DELETE_COMPLAINT:
                case DELETE_HISTORY_OF_PRESENT_COMPLAINT:
                case DELETE_MENSTRUAL_HISTORY:
                case DELETE_OBSTETRIC_HISTORY:
                case DELETE_GENERAL_EXAMINATION:
                case DELETE_SYSTEMIC_EXAMINATION:
                case DELETE_OBSERVATION:
                case DELETE_INVESTIGATION:
                case DELETE_PROVISIONAL_DIAGNOSIS:
                case DELETE_DIAGNOSIS:
                case DELETE_ECG:
                case DELETE_ECHO:
                case DELETE_XRAY:
                case DELETE_HOLTER:
                case DELETE_NOTES:
                case DELETE_INDICATION_OF_USG:
                case DELETE_PA:
                case DELETE_PS:
                case DELETE_PV:
                    if (isHideClicked) {
                        listHidden.add(clickedObject);
                        listActivated.remove(clickedObject);
                    } else {
                        listActivated.add(clickedObject);
                        listHidden.remove(clickedObject);
                    }
                    Object updatedObject = setDiscardedValueInLocal(clickedObject);
                    if (updatedObject != null) {
                        ArrayList<Object> list = new ArrayList<Object>();
                        list.add(updatedObject);
                        response.setDataList(list);
                        response.setBreakAfterAddRequest(true);
                        addToLocatDatabase(response);
                    }
                    setSearchEditTextValue(getSearchEditTextValue());
                    mActivity.hideLoading();
            }
            mActivity.hideLoading();
        }
        mActivity.hideLoading();
    }

    private void addToLocatDatabase(VolleyResponseBean response) {
        switch (nameHideActivateType) {
            case REFERENCE:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_REFERENCE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case DIRECTION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIRECTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case FREQUENCY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DRUG_DOSAGE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case HISTORY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DISEASE_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case DRUG:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DRUGS_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case PRESENT_COMPLAINT:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PRESENT_COMPLAINT_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case COMPLAINT:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_COMPLAINT_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case HISTORY_OF_PRESENT_COMPLAINT:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case MENSTRUAL_HISTORY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_MENSTRUAL_HISTORY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case OBSTETRIC_HISTORY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_OBSTETRIC_HISTORY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case GENERAL_EXAMINATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_GENERAL_EXAMINATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case SYSTEMIC_EXAMINATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_SYSTEMIC_EXAMINATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case OBSERVATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_OBSERVATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case INVESTIGATIONS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_INVESTIGATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case PROVISIONAL_DIAGNOSIS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case DIAGNOSIS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIAGNOSIS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case ECG:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_ECG_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case ECHO:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_ECHO_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case XRAY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_XRAY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case HOLTER:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_HOLTER_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case NOTES:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_NOTES_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case INDICATION_OF_USG:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_INDICATION_OF_USG_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case PA:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PA_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case PS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case PV:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PV_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
        }
    }

    private Object setDiscardedValueInLocal(Object clickedObject) {
        switch (nameHideActivateType) {
            case REFERENCE:
                if (clickedObject != null && clickedObject instanceof Reference) {
                    Reference reference = (Reference) clickedObject;
                    reference.setDiscarded(!reference.getDiscarded());
                    clickedObject = reference;
                }
                break;
            case DIRECTION:
                if (clickedObject != null && clickedObject instanceof DrugDirection) {
                    DrugDirection direction = (DrugDirection) clickedObject;
                    direction.setDiscarded(!direction.getDiscarded());
                    clickedObject = direction;
                }
                break;
            case FREQUENCY:
                if (clickedObject != null && clickedObject instanceof DrugDosage) {
                    DrugDosage drugDosage = (DrugDosage) clickedObject;
                    drugDosage.setDiscarded(!drugDosage.getDiscarded());
                    clickedObject = drugDosage;
                }
                break;
            case HISTORY:
                if (clickedObject != null && clickedObject instanceof Disease) {
                    Disease disease = (Disease) clickedObject;
                    disease.setDiscarded(!disease.getDiscarded());
                    clickedObject = disease;
                }
                break;
            case DRUG:
                if (clickedObject != null && clickedObject instanceof Drug) {
                    Drug drug = (Drug) clickedObject;
                    drug.setDiscarded(!drug.getDiscarded());
                    clickedObject = drug;
                }
                break;
            case PRESENT_COMPLAINT:
                if (clickedObject != null && clickedObject instanceof PresentComplaintSuggestions) {
                    PresentComplaintSuggestions presentComplaintSuggestions = (PresentComplaintSuggestions) clickedObject;
                    presentComplaintSuggestions.setDiscarded(!presentComplaintSuggestions.getDiscarded());
                    clickedObject = presentComplaintSuggestions;
                }
                break;
            case COMPLAINT:
                if (clickedObject != null && clickedObject instanceof ComplaintSuggestions) {
                    ComplaintSuggestions complaintSuggestions = (ComplaintSuggestions) clickedObject;
                    complaintSuggestions.setDiscarded(!complaintSuggestions.getDiscarded());
                    clickedObject = complaintSuggestions;
                }
                break;
            case HISTORY_OF_PRESENT_COMPLAINT:
                if (clickedObject != null && clickedObject instanceof HistoryPresentComplaintSuggestions) {
                    HistoryPresentComplaintSuggestions historyDetailsResponse = (HistoryPresentComplaintSuggestions) clickedObject;
                    historyDetailsResponse.setDiscarded(!historyDetailsResponse.getDiscarded());
                    clickedObject = historyDetailsResponse;
                }
                break;
            case MENSTRUAL_HISTORY:
                if (clickedObject != null && clickedObject instanceof MenstrualHistorySuggestions) {
                    MenstrualHistorySuggestions menstrualHistorySuggestions = (MenstrualHistorySuggestions) clickedObject;
                    menstrualHistorySuggestions.setDiscarded(!menstrualHistorySuggestions.getDiscarded());
                    clickedObject = menstrualHistorySuggestions;
                }
                break;
            case OBSTETRIC_HISTORY:
                if (clickedObject != null && clickedObject instanceof ObstetricHistorySuggestions) {
                    ObstetricHistorySuggestions obstetricHistorySuggestions = (ObstetricHistorySuggestions) clickedObject;
                    obstetricHistorySuggestions.setDiscarded(!obstetricHistorySuggestions.getDiscarded());
                    clickedObject = obstetricHistorySuggestions;
                }
                break;
            case GENERAL_EXAMINATION:
                if (clickedObject != null && clickedObject instanceof GeneralExaminationSuggestions) {
                    GeneralExaminationSuggestions generalExaminationSuggestions = (GeneralExaminationSuggestions) clickedObject;
                    generalExaminationSuggestions.setDiscarded(!generalExaminationSuggestions.getDiscarded());
                    clickedObject = generalExaminationSuggestions;
                }
                break;
            case SYSTEMIC_EXAMINATION:
                if (clickedObject != null && clickedObject instanceof SystemicExaminationSuggestions) {
                    SystemicExaminationSuggestions systemicExaminationSuggestions = (SystemicExaminationSuggestions) clickedObject;
                    systemicExaminationSuggestions.setDiscarded(!systemicExaminationSuggestions.getDiscarded());
                    clickedObject = systemicExaminationSuggestions;
                }
                break;
            case OBSERVATION:
                if (clickedObject != null && clickedObject instanceof ObservationSuggestions) {
                    ObservationSuggestions observationSuggestions = (ObservationSuggestions) clickedObject;
                    observationSuggestions.setDiscarded(!observationSuggestions.getDiscarded());
                    clickedObject = observationSuggestions;
                }
                break;
            case INVESTIGATIONS:
                if (clickedObject != null && clickedObject instanceof InvestigationSuggestions) {
                    InvestigationSuggestions investigationSuggestions = (InvestigationSuggestions) clickedObject;
                    investigationSuggestions.setDiscarded(!investigationSuggestions.getDiscarded());
                    clickedObject = investigationSuggestions;
                }
                break;
            case PROVISIONAL_DIAGNOSIS:
                if (clickedObject != null && clickedObject instanceof ProvisionalDiagnosisSuggestions) {
                    ProvisionalDiagnosisSuggestions provisionalDiagnosisSuggestions = (ProvisionalDiagnosisSuggestions) clickedObject;
                    provisionalDiagnosisSuggestions.setDiscarded(!provisionalDiagnosisSuggestions.getDiscarded());
                    clickedObject = provisionalDiagnosisSuggestions;
                }
                break;
            case DIAGNOSIS:
                if (clickedObject != null && clickedObject instanceof DiagnosisSuggestions) {
                    DiagnosisSuggestions diagnosisSuggestions = (DiagnosisSuggestions) clickedObject;
                    diagnosisSuggestions.setDiscarded(!diagnosisSuggestions.getDiscarded());
                    clickedObject = diagnosisSuggestions;
                }
                break;
            case ECG:
                if (clickedObject != null && clickedObject instanceof EcgDetailSuggestions) {
                    EcgDetailSuggestions ecgDetailSuggestions = (EcgDetailSuggestions) clickedObject;
                    ecgDetailSuggestions.setDiscarded(!ecgDetailSuggestions.getDiscarded());
                    clickedObject = ecgDetailSuggestions;
                }
                break;
            case ECHO:
                if (clickedObject != null && clickedObject instanceof EchoSuggestions) {
                    EchoSuggestions echoSuggestions = (EchoSuggestions) clickedObject;
                    echoSuggestions.setDiscarded(!echoSuggestions.getDiscarded());
                    clickedObject = echoSuggestions;
                }
                break;
            case XRAY:
                if (clickedObject != null && clickedObject instanceof XrayDetailSuggestions) {
                    XrayDetailSuggestions xrayDetailSuggestions = (XrayDetailSuggestions) clickedObject;
                    xrayDetailSuggestions.setDiscarded(!xrayDetailSuggestions.getDiscarded());
                    clickedObject = xrayDetailSuggestions;
                }
                break;
            case HOLTER:
                if (clickedObject != null && clickedObject instanceof HolterSuggestions) {
                    HolterSuggestions holterSuggestions = (HolterSuggestions) clickedObject;
                    holterSuggestions.setDiscarded(!holterSuggestions.getDiscarded());
                    clickedObject = holterSuggestions;
                }
                break;
            case NOTES:
                if (clickedObject != null && clickedObject instanceof NotesSuggestions) {
                    NotesSuggestions notesSuggestions = (NotesSuggestions) clickedObject;
                    notesSuggestions.setDiscarded(!notesSuggestions.getDiscarded());
                    clickedObject = notesSuggestions;
                }
                break;
            case INDICATION_OF_USG:
                if (clickedObject != null && clickedObject instanceof IndicationOfUsgSuggestions) {
                    IndicationOfUsgSuggestions notesSuggestions = (IndicationOfUsgSuggestions) clickedObject;
                    notesSuggestions.setDiscarded(!notesSuggestions.getDiscarded());
                    clickedObject = notesSuggestions;
                }
                break;
            case PA:
                if (clickedObject != null && clickedObject instanceof PaSuggestions) {
                    PaSuggestions psSuggestions = (PaSuggestions) clickedObject;
                    psSuggestions.setDiscarded(!psSuggestions.getDiscarded());
                    clickedObject = psSuggestions;
                }
                break;
            case PS:
                if (clickedObject != null && clickedObject instanceof PsSuggestions) {
                    PsSuggestions psSuggestions = (PsSuggestions) clickedObject;
                    psSuggestions.setDiscarded(!psSuggestions.getDiscarded());
                    clickedObject = psSuggestions;
                }
                break;
            case PV:
                if (clickedObject != null && clickedObject instanceof PvSuggestions) {
                    PvSuggestions pvSuggestions = (PvSuggestions) clickedObject;
                    pvSuggestions.setDiscarded(!pvSuggestions.getDiscarded());
                    clickedObject = pvSuggestions;
                }
                break;
        }
        return clickedObject;
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                    doctorClinicsList = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId());
                }

                break;
            case GET_REFERENCE_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getReferenceList(WebServiceType.GET_REFERENCE, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), BooleanTypeValues.TRUE, RecordType.CUSTOM, null, null);
                break;
            case GET_REFERENCE_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getReferenceList(WebServiceType.GET_REFERENCE, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), BooleanTypeValues.FALSE, RecordType.CUSTOM, null, null);
                break;
            case ADD_REFERENCE:
                LocalDataServiceImpl.getInstance(mApp).addReferenceList((ArrayList<Reference>) (ArrayList<?>) response.getDataList());
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_DIRECTION_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDosageDurationDirectionList(WebServiceType.GET_DIRECTION, LocalBackgroundTaskType.GET_DIRECTION_HIDDEN_LIST, RecordType.CUSTOM, BooleanTypeValues.TRUE, user.getUniqueId(), true, 0, null, null);
                break;
            case GET_DIRECTION_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDosageDurationDirectionList(WebServiceType.GET_DIRECTION, LocalBackgroundTaskType.GET_DIRECTION_ACTIVATED_LIST, RecordType.CUSTOM, BooleanTypeValues.FALSE, user.getUniqueId(), false, 0, null, null);
                break;
            case ADD_DIRECTIONS:
                LocalDataServiceImpl.getInstance(mApp).addDirectionsList((ArrayList<DrugDirection>) (ArrayList<?>) response.getDataList());
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    break;
                }

            case GET_FREQUENCY_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDosageDurationDirectionList(WebServiceType.GET_DRUG_DOSAGE, LocalBackgroundTaskType.GET_FREQUENCY_HIDDEN_LIST, RecordType.CUSTOM, BooleanTypeValues.TRUE, user.getUniqueId(), true, 0, null, null);
                break;
            case GET_FREQUENCY_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDosageDurationDirectionList(WebServiceType.GET_DRUG_DOSAGE, LocalBackgroundTaskType.GET_FREQUENCY_ACTIVATED_LIST, RecordType.CUSTOM, BooleanTypeValues.FALSE, user.getUniqueId(), false, 0, null, null);
                break;
            case ADD_DRUG_DOSAGE:
                LocalDataServiceImpl.getInstance(mApp).addDrugDosageList((ArrayList<DrugDosage>) (ArrayList<?>) response.getDataList());
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_DISEASE_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDiseaseList(WebServiceType.GET_DISEASE_LIST, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_DISEASE_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDiseaseList(WebServiceType.GET_DISEASE_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_DISEASE_LIST:
                LocalDataServiceImpl.getInstance(mApp).addDiseaseList((ArrayList<Disease>) (ArrayList<?>) response.getDataList());
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_DRUG_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDrugsList(WebServiceType.GET_DRUGS_LIST_CUSTOM, user.getUniqueId(), true, 0, null, null);
                break;
            case GET_DRUG_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDrugsList(WebServiceType.GET_DRUGS_LIST_CUSTOM, user.getUniqueId(), false, 0, null, null);
                break;
            case ADD_DRUGS_LIST:
                LocalDataServiceImpl.getInstance(mApp).addDrugsList((ArrayList<Drug>) (ArrayList<?>) response.getDataList());
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_PRESENT_COMPLAINT_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_PRESENT_COMPLAINT_SUGGESTIONS, PresentComplaintSuggestions.class, LocalBackgroundTaskType.GET_PRESENT_COMPLAINT_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_PRESENT_COMPLAINT_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_PRESENT_COMPLAINT_SUGGESTIONS, PresentComplaintSuggestions.class, null, LocalBackgroundTaskType.GET_PRESENT_COMPLAINT_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_PRESENT_COMPLAINT_LIST:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_PRESENT_COMPLAINT_SUGGESTIONS, LocalTabelType.PRESENT_COMPLAINT_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_COMPLAINT_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_COMPLAINT_SUGGESTIONS, ComplaintSuggestions.class, LocalBackgroundTaskType.GET_COMPLAINT_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_COMPLAINT_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_COMPLAINT_SUGGESTIONS, ComplaintSuggestions.class, null, LocalBackgroundTaskType.GET_COMPLAINT_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_COMPLAINT_LIST:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_COMPLAINT_SUGGESTIONS, LocalTabelType.COMPLAINT_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_HISTORY_OF_PRESENT_COMPLAINT_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, HistoryPresentComplaintSuggestions.class, LocalBackgroundTaskType.GET_HISTORY_OF_PRESENT_COMPLAINT_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_HISTORY_OF_PRESENT_COMPLAINT_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, HistoryPresentComplaintSuggestions.class, null, LocalBackgroundTaskType.GET_HISTORY_OF_PRESENT_COMPLAINT_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, LocalTabelType.HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_MENSTRUAL_HISTORY_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_MENSTRUAL_HISTORY_SUGGESTIONS, MenstrualHistorySuggestions.class, LocalBackgroundTaskType.GET_MENSTRUAL_HISTORY_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_MENSTRUAL_HISTORY_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_MENSTRUAL_HISTORY_SUGGESTIONS, MenstrualHistorySuggestions.class, null, LocalBackgroundTaskType.GET_MENSTRUAL_HISTORY_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_MENSTRUAL_HISTORY_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_MENSTRUAL_HISTORY_SUGGESTIONS, LocalTabelType.MENSTRUAL_HISTORY_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_OBSTETRIC_HISTORY_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_OBSTETRIC_HISTORY_SUGGESTIONS, ObstetricHistorySuggestions.class, LocalBackgroundTaskType.GET_OBSTETRIC_HISTORY_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_OBSTETRIC_HISTORY_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_OBSTETRIC_HISTORY_SUGGESTIONS, ObstetricHistorySuggestions.class, null, LocalBackgroundTaskType.GET_OBSTETRIC_HISTORY_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_OBSTETRIC_HISTORY_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_OBSTETRIC_HISTORY_SUGGESTIONS, LocalTabelType.OBSTETRIC_HISTORY_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_GENERAL_EXAMINATION_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_GENERAL_EXAMINATION_SUGGESTIONS, GeneralExaminationSuggestions.class, LocalBackgroundTaskType.GET_GENERAL_EXAMINATION_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_GENERAL_EXAMINATION_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_GENERAL_EXAMINATION_SUGGESTIONS, GeneralExaminationSuggestions.class, null, LocalBackgroundTaskType.GET_GENERAL_EXAMINATION_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_GENERAL_EXAMINATION_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_GENERAL_EXAMINATION_SUGGESTIONS, LocalTabelType.GENERAL_EXAMINATION_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }
            case GET_SYSTEMIC_EXAMINATION_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_SYSTEMIC_EXAMINATION_SUGGESTIONS, SystemicExaminationSuggestions.class, LocalBackgroundTaskType.GET_SYSTEMIC_EXAMINATION_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_SYSTEMIC_EXAMINATION_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_SYSTEMIC_EXAMINATION_SUGGESTIONS, SystemicExaminationSuggestions.class, null, LocalBackgroundTaskType.GET_SYSTEMIC_EXAMINATION_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_SYSTEMIC_EXAMINATION_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_SYSTEMIC_EXAMINATION_SUGGESTIONS, LocalTabelType.SYSTEMIC_EXAMINATION_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_OBSERVATION_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_OBSERVATION_SUGGESTIONS, ObservationSuggestions.class, LocalBackgroundTaskType.GET_OBSERVATION_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_OBSERVATION_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_OBSERVATION_SUGGESTIONS, ObservationSuggestions.class, null, LocalBackgroundTaskType.GET_OBSERVATION_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_OBSERVATION_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_OBSERVATION_SUGGESTIONS, LocalTabelType.OBSERVATION_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_INVESTIGATIONS_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_INVESTIGATION_SUGGESTIONS, InvestigationSuggestions.class, LocalBackgroundTaskType.GET_INVESTIGATIONS_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_INVESTIGATIONS_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_INVESTIGATION_SUGGESTIONS, InvestigationSuggestions.class, null, LocalBackgroundTaskType.GET_INVESTIGATIONS_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_INVESTIGATION_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_INVESTIGATION_SUGGESTIONS, LocalTabelType.INVESTIGATION_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_PROVISIONAL_DIAGNOSIS_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, ProvisionalDiagnosisSuggestions.class, LocalBackgroundTaskType.GET_PROVISIONAL_DIAGNOSIS_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_PROVISIONAL_DIAGNOSIS_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, ProvisionalDiagnosisSuggestions.class, null, LocalBackgroundTaskType.GET_PROVISIONAL_DIAGNOSIS_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_PROVISIONAL_DIAGNOSIS_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, LocalTabelType.PROVISIONAL_DIAGNOSIS_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_DIAGNOSIS_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_DIAGNOSIS_SUGGESTIONS, DiagnosisSuggestions.class, LocalBackgroundTaskType.GET_DIAGNOSIS_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_DIAGNOSIS_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_DIAGNOSIS_SUGGESTIONS, DiagnosisSuggestions.class, null, LocalBackgroundTaskType.GET_DIAGNOSIS_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_DIAGNOSIS_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_DIAGNOSIS_SUGGESTIONS, LocalTabelType.DIAGNOSIS_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_ECG_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_ECG_SUGGESTIONS, EcgDetailSuggestions.class, LocalBackgroundTaskType.GET_ECG_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_ECG_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_ECG_SUGGESTIONS, EcgDetailSuggestions.class, null, LocalBackgroundTaskType.GET_ECG_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_ECG_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_ECG_SUGGESTIONS, LocalTabelType.ECG_DETAILS_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_ECHO_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_ECHO_SUGGESTIONS, EchoSuggestions.class, LocalBackgroundTaskType.GET_ECHO_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_ECHO_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_ECHO_SUGGESTIONS, EchoSuggestions.class, null, LocalBackgroundTaskType.GET_ECHO_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_ECHO_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_ECHO_SUGGESTIONS, LocalTabelType.ECHO_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_XRAY_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_XRAY_SUGGESTIONS, XrayDetailSuggestions.class, LocalBackgroundTaskType.GET_XRAY_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_XRAY_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_XRAY_SUGGESTIONS, XrayDetailSuggestions.class, null, LocalBackgroundTaskType.GET_XRAY_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_XRAY_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_XRAY_SUGGESTIONS, LocalTabelType.X_RAY_DETAILS_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_HOLTER_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_HOLTER_SUGGESTIONS, HolterSuggestions.class, LocalBackgroundTaskType.GET_HOLTER_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_HOLTER_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_HOLTER_SUGGESTIONS, HolterSuggestions.class, null, LocalBackgroundTaskType.GET_HOLTER_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_HOLTER_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_HOLTER_SUGGESTIONS, LocalTabelType.HOLTER_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_NOTES_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_NOTES_SUGGESTIONS, NotesSuggestions.class, LocalBackgroundTaskType.GET_NOTES_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_NOTES_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_NOTES_SUGGESTIONS, NotesSuggestions.class, null, LocalBackgroundTaskType.GET_NOTES_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_NOTES_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_NOTES_SUGGESTIONS, LocalTabelType.NOTES_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_INDICATION_OF_USG_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_INDICATION_OF_USG_SUGGESTIONS, IndicationOfUsgSuggestions.class, LocalBackgroundTaskType.GET_INDICATION_OF_USG_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_INDICATION_OF_USG_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_INDICATION_OF_USG_SUGGESTIONS, IndicationOfUsgSuggestions.class, null, LocalBackgroundTaskType.GET_INDICATION_OF_USG_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_INDICATION_OF_USG_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_INDICATION_OF_USG_SUGGESTIONS, LocalTabelType.INDICATION_OF_USG_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_PA_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_PA_SUGGESTIONS, PaSuggestions.class, LocalBackgroundTaskType.GET_PA_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_PA_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_PA_SUGGESTIONS, PaSuggestions.class, null, LocalBackgroundTaskType.GET_PA_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_PA_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_PA_SUGGESTIONS, LocalTabelType.PA_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_PS_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_PS_SUGGESTIONS, PsSuggestions.class, LocalBackgroundTaskType.GET_PS_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_PS_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_PS_SUGGESTIONS, PsSuggestions.class, null, LocalBackgroundTaskType.GET_PS_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_PS_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_PS_SUGGESTIONS, LocalTabelType.PS_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }

            case GET_PV_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_PV_SUGGESTIONS, PvSuggestions.class, LocalBackgroundTaskType.GET_PV_HIDDEN_LIST, null, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_PV_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getClinicalNotesSuggestionList(WebServiceType.GET_PV_SUGGESTIONS, PvSuggestions.class, null, LocalBackgroundTaskType.GET_PV_ACTIVATED_LIST, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_PV_SUGGESTIONS:
                LocalDataServiceImpl.getInstance(mApp).
                        addSuggestionsList(WebServiceType.GET_PV_SUGGESTIONS, LocalTabelType.PV_SUGGESTIONS,
                                response.getDataList(), null, null);
                if (response.isBreakAfterAddRequest()) {
                    break;
                } else {
                    getListFromLocal(false, response);
                    return null;
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_REFERENCE_LIST) {
//                initData();
                getListFromLocal(true, null);
//                setSearchEditTextValue("");
            }
        }
    }

    private void initData() {
        isHashMapFormed = false;
        isHiddenListEmpty = false;
        isActivatedListEmpty = false;
        switch (nameHideActivateType) {
            case REFERENCE:
                mActivity.showLoading(false);
                latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.REFERENCE);
                WebDataServiceImpl.getInstance(mApp).getReference(Reference.class, user.getUniqueId(), latestUpdatedTime, BooleanTypeValues.TRUE, this, this);
                break;
            case DIRECTION:
                mActivity.showLoading(false);
                latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DIRECTION);
                Long latestUpdatedTimeDirection = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DIRECTION);
                WebDataServiceImpl.getInstance(mApp)
                        .getDosageDirection(WebServiceType.GET_DIRECTION, DrugDirection.class, true, this.user.getUniqueId(), latestUpdatedTimeDirection, this, this);
                break;
            case FREQUENCY:
                Long latestUpdatedTimeDosage = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DOSAGE);
                WebDataServiceImpl.getInstance(mApp)
                        .getDosageDirection(WebServiceType.GET_DRUG_DOSAGE, DrugDosage.class, true, this.user.getUniqueId(), latestUpdatedTimeDosage, this, this);
                break;
            case HISTORY:
                Long latestUpdatedTimeDisease = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DISEASE);
                WebDataServiceImpl.getInstance(mApp).getDiseaseList(Disease.class, user.getUniqueId(), latestUpdatedTimeDisease, null, this, this);
                break;
            case DRUG:
                Long latestUpdatedTimeDrug = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.TEMP_DRUG);
                WebDataServiceImpl.getInstance(mApp).getDrugsList(WebServiceType.GET_DRUGS_LIST_CUSTOM, Drug.class, user.getUniqueId(), latestUpdatedTimeDrug, true, this, this);
                break;
            case PRESENT_COMPLAINT:
                Long latestUpdatedTimePresentComplaint = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PRESENT_COMPLAINT_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PresentComplaintSuggestions.class, WebServiceType.GET_PRESENT_COMPLAINT_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimePresentComplaint, this, this);
                break;
            case COMPLAINT:
                Long latestUpdatedTimeComplaint = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.COMPLAINT);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ComplaintSuggestions.class, WebServiceType.GET_COMPLAINT_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeComplaint, this, this);
                break;
            case HISTORY_OF_PRESENT_COMPLAINT:
                Long latestUpdatedTimeHistoryPresentComplaint = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(HistoryPresentComplaintSuggestions.class, WebServiceType.GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeHistoryPresentComplaint, this, this);
                break;
            case MENSTRUAL_HISTORY:
                Long latestUpdatedTimeMenstrualHistory = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.MENSTRUAL_HISTORY_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(MenstrualHistorySuggestions.class, WebServiceType.GET_MENSTRUAL_HISTORY_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeMenstrualHistory, this, this);
                break;
            case OBSTETRIC_HISTORY:
                Long latestUpdatedTimeObstetricHistory = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.OBSTETRIC_HISTORY_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ObstetricHistorySuggestions.class, WebServiceType.GET_OBSTETRIC_HISTORY_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeObstetricHistory, this, this);
                break;
            case GENERAL_EXAMINATION:
                Long latestUpdatedTimeGeneralExamination = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.GENERAL_EXAMINATION_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(GeneralExaminationSuggestions.class, WebServiceType.GET_GENERAL_EXAMINATION_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeGeneralExamination, this, this);
                break;
            case SYSTEMIC_EXAMINATION:
                Long latestUpdatedTimeSystemicExamination = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.SYSTEMIC_EXAMINATION_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(SystemicExaminationSuggestions.class, WebServiceType.GET_SYSTEMIC_EXAMINATION_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeSystemicExamination, this, this);
                break;
            case OBSERVATION:
                Long latestUpdatedTimeObservation = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.OBSERVATION);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ObservationSuggestions.class, WebServiceType.GET_OBSERVATION_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeObservation, this, this);
                break;
            case INVESTIGATIONS:
                Long latestUpdatedTimeInvestigation = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.INVESTIGATION);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(InvestigationSuggestions.class, WebServiceType.GET_INVESTIGATION_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeInvestigation, this, this);
                break;
            case PROVISIONAL_DIAGNOSIS:
                Long latestUpdatedTimeProvisionalDiagnosis = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PROVISIONAL_DIAGNOSIS_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ProvisionalDiagnosisSuggestions.class, WebServiceType.GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeProvisionalDiagnosis, this, this);
                break;
            case DIAGNOSIS:
                Long latestUpdatedTimeDiagnosis = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DIAGNOSIS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(DiagnosisSuggestions.class, WebServiceType.GET_DIAGNOSIS_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeDiagnosis, this, this);
                break;
            case ECG:
                Long latestUpdatedTimeEcgDetail = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.ECG_DETAILS_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(EcgDetailSuggestions.class, WebServiceType.GET_ECG_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeEcgDetail, this, this);
                break;
            case ECHO:
                Long latestUpdatedTimeEcho = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.ECHO_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(EchoSuggestions.class, WebServiceType.GET_ECHO_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeEcho, this, this);
                break;
            case XRAY:
                Long latestUpdatedTimeXrayDetail = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.X_RAY_DETAILS_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(XrayDetailSuggestions.class, WebServiceType.GET_XRAY_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeXrayDetail, this, this);
                break;
            case HOLTER:
                Long latestUpdatedTimeHolter = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.HOLTER_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(HolterSuggestions.class, WebServiceType.GET_HOLTER_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeHolter, this, this);
                break;
            case NOTES:
                Long latestUpdatedTimeNotes = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.NOTES_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(NotesSuggestions.class, WebServiceType.GET_NOTES_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeNotes, this, this);
                break;
            case INDICATION_OF_USG:
                Long latestUpdatedTimeIndicationOfUsg = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.INDICATION_OF_USG_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(IndicationOfUsgSuggestions.class, WebServiceType.GET_INDICATION_OF_USG_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimeIndicationOfUsg, this, this);
                break;
            case PA:
                Long latestUpdatedTimePa = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PA_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PaSuggestions.class, WebServiceType.GET_PA_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimePa, this, this);
                break;
            case PS:
                Long latestUpdatedTimePs = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PS_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PsSuggestions.class, WebServiceType.GET_PS_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimePs, this, this);
                break;
            case PV:
                Long latestUpdatedTimePv = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.PV_SUGGESTIONS);
                WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(PvSuggestions.class, WebServiceType.GET_PV_SUGGESTIONS, user.getUniqueId(), latestUpdatedTimePv, this, this);
                break;
        }
    }

    @Override
    public void onRefresh() {
        LogUtils.LOGD(TAG, "onRefresh called from SwipeRefreshLayout");
        initData();
    }

    public void refreshData(User user, NameHideActivateType nameHideActivateType) {
        this.user = user;
        this.nameHideActivateType = nameHideActivateType;
        initData();
    }


}