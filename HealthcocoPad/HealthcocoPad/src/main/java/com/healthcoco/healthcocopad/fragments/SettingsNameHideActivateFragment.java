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
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.NameHideActivateAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
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
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.ExpandableHeightListView;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.custom.ScrollViewWithHeader;
import com.healthcoco.healthcocopad.dialogFragment.AddNewDrugDialogFragment;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.NameHideActivateType;
import com.healthcoco.healthcocopad.enums.RecordType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewDrugListener;
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
public class SettingsNameHideActivateFragment extends HealthCocoFragment implements
        NameHideActivateListener, View.OnClickListener, TextWatcher, Response.Listener<VolleyResponseBean>,
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, SwipeRefreshLayout.OnRefreshListener {
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
        Intent intent = mActivity.getIntent();
        int typeOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_NAME_EDIT_TYPE, -1);
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
            case HISTORY:
//                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_REFERENCE_LIST_ALL, this, this, this).execute();
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DISEASE_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DISEASE_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case TREATMENT:
//                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_REFERENCE_LIST_ALL, this, this, this).execute();
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_TREATMENT_HIDDEN_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_TREATMENT_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
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
            case TREATMENT:
                if (object instanceof TreatmentService) {
                    TreatmentService treatmentService = (TreatmentService) object;
                    isHideClicked = true;
                    clickedObject = object;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_TREATMENT,
                            HistoryPresentComplaintSuggestions.class, user.getUniqueId(), treatmentService.getLocationId(),
                            treatmentService.getHospitalId(), treatmentService.getUniqueId(), true, this, this);
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
                    case TREATMENT:
                        if (object instanceof TreatmentService) {
                            TreatmentService treatmentService = (TreatmentService) object;
                            isHideClicked = false;
                            clickedObject = object;
                            mActivity.showLoading(false);
                            WebDataServiceImpl.getInstance(mApp).deleteData(WebServiceType.DELETE_TREATMENT,
                                    HistoryPresentComplaintSuggestions.class, user.getUniqueId(), treatmentService.getLocationId(),
                                    treatmentService.getHospitalId(), treatmentService.getUniqueId(), false, this, this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_advance_search:
                switch (nameHideActivateType) {

                    case HISTORY:
                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_CUSTOM_HISTORY, AddUpdateNameDialogType.HISTORY, this, user, "", HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                        break;

                    case TREATMENT:
                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_CUSTOM_HISTORY, AddUpdateNameDialogType.HISTORY, this, user, "", HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
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

                        case HISTORY:
                            if (object instanceof Disease) {
                                Disease disease = (Disease) object;
                                if (!Util.isNullOrBlank(disease.getDisease()) && disease.getDisease().toUpperCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;

                        case TREATMENT:
                            if (object instanceof TreatmentService) {
                                TreatmentService treatmentService = (TreatmentService) object;
                                if (!Util.isNullOrBlank(treatmentService.getName()) && treatmentService.getName().toUpperCase(Locale.ENGLISH)
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
                case GET_DISEASE_LIST:
                case GET_HISTORY_LIST:
                case GET_TREATMENT_LIST_BOTH_SOLR:

                    if (response.isDataFromLocal()) {
                        if (response.getLocalBackgroundTaskType() != null) {
                            switch (response.getLocalBackgroundTaskType()) {

                                case GET_DISEASE_HIDDEN_LIST:
                                case GET_TREATMENT_HIDDEN_LIST:

                                    listHidden = (ArrayList<Object>) (ArrayList<?>) response.getDataList();
                                    setSearchEditTextValue(getSearchEditTextValue());
                                    isHiddenListLoaded = true;
                                    break;

                                case GET_DISEASE_ACTIVATED_LIST:
                                case GET_TREATMENT_ACTIVATED_LIST:

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

                case DELETE_DISEASE:
                case DELETE_TREATMENT:

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

            case HISTORY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DISEASE_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;
            case TREATMENT:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_TREATMENT_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                break;

        }
    }

    private Object setDiscardedValueInLocal(Object clickedObject) {
        switch (nameHideActivateType) {

            case HISTORY:
                if (clickedObject != null && clickedObject instanceof Disease) {
                    Disease disease = (Disease) clickedObject;
                    disease.setDiscarded(!disease.getDiscarded());
                    clickedObject = disease;
                }
                break;
            case TREATMENT:
                if (clickedObject != null && clickedObject instanceof TreatmentService) {
                    TreatmentService treatmentService = (TreatmentService) clickedObject;
                    treatmentService.setDiscarded(!treatmentService.getDiscarded());
                    clickedObject = treatmentService;
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

            case GET_TREATMENT_HIDDEN_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getTreatmentServiceList(WebServiceType.GET_TREATMENT_LIST_BOTH_SOLR, user.getUniqueId(), BooleanTypeValues.TRUE, null, 0, null, null);
                break;
            case GET_TREATMENT_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getTreatmentServiceList(WebServiceType.GET_TREATMENT_LIST_BOTH_SOLR, user.getUniqueId(), BooleanTypeValues.FALSE, null, 0, null, null);
                break;
            case ADD_TREATMENT_LIST:
                LocalDataServiceImpl.getInstance(mApp).addTreatmentSericeList((ArrayList<TreatmentService>) (ArrayList<?>) response.getDataList());
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

            case HISTORY:
                Long latestUpdatedTimeDisease = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DISEASE);
                WebDataServiceImpl.getInstance(mApp).getDiseaseList(Disease.class, user.getUniqueId(), latestUpdatedTimeDisease, null, this, this);
                break;

            case TREATMENT:
                Long latestUpdatedTimeTreatentService = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.TREATMENT_SERVICE);
                WebDataServiceImpl.getInstance(mApp).getTreatmentsServiceList(TreatmentService.class, WebServiceType.GET_TREATMENT_LIST_BOTH_SOLR, user.getUniqueId(), user.getForeignHospitalId(), user.getForeignLocationId(), latestUpdatedTimeTreatentService, this, this);
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