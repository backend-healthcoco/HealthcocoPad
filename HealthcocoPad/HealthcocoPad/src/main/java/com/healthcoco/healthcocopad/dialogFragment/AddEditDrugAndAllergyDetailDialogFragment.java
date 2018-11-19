package com.healthcoco.healthcocopad.dialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.SelectDrugListSolrAdapter;
import com.healthcoco.healthcocopad.adapter.SelectedDrugItemsListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddEditDrugsAndAllergiesRequest;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.DrugsAndAllergies;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.Duration;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.MyScriptAddVisitsFragment;
import com.healthcoco.healthcocopad.fragments.PatientProfileDetailFragment;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedDrugsListItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shreshtha on 18-03-2017.
 */
public class AddEditDrugAndAllergyDetailDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, TextWatcher, LocalDoInBackgroundListenerOptimised,
        SelectDrugItemClickListener, LoadMorePageListener, SelectedDrugsListItemListener {
    public static final String TAG_DRUGS_AND_ALLERGIES = "drugsAndAllergies";
    public static final String TAG_DRUGS_AND_ALLERGIES_ASSESSMENT = "drugsAndAllergiesAssessment";
    private EditText etAllergy;
    private ListView lvDrugs;
    private LinearLayout layoutAllergy;
    private EditText editSearch;
    private static final int MAX_SIZE = 25;
    private int PAGE_NUMBER = 0;
    private ListViewLoadMore lvSolarDrugsList;
    private TextView tvNoDrugHistory;
    private boolean isEndOfListAchieved = false;
    private List<DrugsListSolrResponse> drugsListSolr = new ArrayList<DrugsListSolrResponse>();
    private SelectDrugListSolrAdapter adapterSolr;
    private ProgressBar progressLoading;
    private boolean isLoadingFromSearch;
    private String lastTextSearched;
    private boolean isInitialLoading = true;
    private LinkedHashMap<String, DrugItem> drugsListHashMap = new LinkedHashMap<>();
    private SelectedDrugItemsListAdapter adapter;
    private HistoryDetailsResponse historyDetailsResponse;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private boolean isFromAssessment;
    private boolean isFromMedication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_drugs_and_allergy, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setWidthHeight(0.70, 0.85);
        init();
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
        initAdapters();
        getDataFromIntent();
    }

    private void getDrugsList(boolean isInitialLoading, int pageNum, int size, String searchTerm) {
        mApp.cancelPendingRequests(String.valueOf(WebServiceType.GET_DRUGS_LIST_SOLR));
        if (isInitialLoading) {
            mActivity.showLoading(false);
            progressLoading.setVisibility(View.GONE);
        } else
            progressLoading.setVisibility(View.VISIBLE);
        WebDataServiceImpl.getInstance(mApp).getDrugsListSolr(DrugsListSolrResponse.class, pageNum, size, user.getUniqueId(), user.getForeignHospitalId(), user.getForeignLocationId(), searchTerm, this, this);
    }

    private void getDataFromIntent() {
        historyDetailsResponse = Parcels.unwrap(getArguments().getParcelable(TAG_DRUGS_AND_ALLERGIES));
        DrugsAndAllergies drugsAllergies = Parcels.unwrap(getArguments().getParcelable(TAG_DRUGS_AND_ALLERGIES_ASSESSMENT));
        isFromAssessment = getArguments().getBoolean(HealthCocoConstants.TAG_IS_FROM_ASSESSMENT, false);
        isFromMedication = getArguments().getBoolean(HealthCocoConstants.TAG_DRUG_DETAILS, false);
        if (historyDetailsResponse != null && historyDetailsResponse.getDrugsAndAllergies() != null) {
            DrugsAndAllergies drugsAndAllergies = historyDetailsResponse.getDrugsAndAllergies();
            if (drugsAndAllergies != null) {
                etAllergy.setText(Util.getValidatedValue(drugsAndAllergies.getAllergies()));
                List<Drug> drugs = drugsAndAllergies.getDrugs();
                if (!Util.isNullOrEmptyList(drugs)) {
                    for (Drug drug : drugs) {
                        DrugItem drugItem = new DrugItem();
                        drugItem.setDrug(drug);
                        drugsListHashMap.put(drug.getUniqueId(), drugItem);
                    }
                    notifyAdapter(new ArrayList<DrugItem>(drugsListHashMap.values()));
                }
            }
        } else if (drugsAllergies != null) {
            etAllergy.setText(Util.getValidatedValue(drugsAllergies.getAllergies()));
            List<Drug> drugs = drugsAllergies.getDrugs();
            if (!Util.isNullOrEmptyList(drugs)) {
                for (Drug drug : drugs) {
                    DrugItem drugItem = new DrugItem();
                    drugItem.setDrug(drug);
                    drugsListHashMap.put(drug.getUniqueId(), drugItem);
                }
                notifyAdapter(new ArrayList<DrugItem>(drugsListHashMap.values()));
            }
        }

        if (isFromMedication)
            layoutAllergy.setVisibility(View.GONE);
        else
            layoutAllergy.setVisibility(View.VISIBLE);
    }

    @Override
    public void initViews() {
        layoutAllergy = (LinearLayout) view.findViewById(R.id.layout_allergy);
        etAllergy = (EditText) view.findViewById(R.id.et_allergy);
        lvSolarDrugsList = (ListViewLoadMore) view.findViewById(R.id.lv_drugs_list);
        lvDrugs = (ListView) view.findViewById(R.id.lv_drugs);
        editSearch = (EditText) view.findViewById(R.id.edit_search);
        initEditSearchView(R.string.search_drug, this, this);
        tvNoDrugHistory = (TextView) view.findViewById(R.id.tv_no_drug_history);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
//        requestFocusOnSearchEditText(R.id.edit_search);
    }

    private void initAdapters() {
        adapter = new SelectedDrugItemsListAdapter(mActivity, this);
        lvDrugs.setAdapter(adapter);

        adapterSolr = new SelectDrugListSolrAdapter(mActivity, this, this);
        lvSolarDrugsList.setAdapter(adapterSolr);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.drug_allergy));
    }

    @Override
    public void initData() {

    }

    private void notifyAdapterSolr(List<DrugsListSolrResponse> list) {
        if (!Util.isNullOrEmptyList(list)) {
            LogUtils.LOGD(TAG, "onResponse DrugsList notifyAdapter " + list.size());
//            Collections.sort(list, ComparatorUtil.dateComparatorDrugsSolrlist);
            lvSolarDrugsList.setVisibility(View.VISIBLE);
            tvNoDrugHistory.setVisibility(View.GONE);
        } else {
            lvSolarDrugsList.setVisibility(View.GONE);
            tvNoDrugHistory.setVisibility(View.VISIBLE);
        }
        adapterSolr.setListData(list);
        adapterSolr.notifyDataSetChanged();
    }

    private void notifyAdapter(ArrayList<DrugItem> drugsListMap) {
        adapter.setListData(drugsListMap);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    validateData();
                else
                    onNetworkUnavailable(null);
                break;
        }
    }

    private void validateData() {
        String allergy = Util.getValidatedValueOrNull(etAllergy);
        addEditDrugsAndAllergies(allergy);
    }

    private void addEditDrugsAndAllergies(String allergy) {
        if (!isFromAssessment) {
            mActivity.showLoading(false);
            AddEditDrugsAndAllergiesRequest addEditDrugsAndAllergiesRequest = new AddEditDrugsAndAllergiesRequest();
            addEditDrugsAndAllergiesRequest.setAllergies(allergy);
            addEditDrugsAndAllergiesRequest.setDoctorId(user.getUniqueId());
            addEditDrugsAndAllergiesRequest.setHospitalId(user.getForeignHospitalId());
            addEditDrugsAndAllergiesRequest.setLocationId(user.getForeignLocationId());
            addEditDrugsAndAllergiesRequest.setPatientId(selectedPatient.getUserId());
            addEditDrugsAndAllergiesRequest.setDrugIds(new ArrayList<String>(drugsListHashMap.keySet()));
            WebDataServiceImpl.getInstance(mApp).addUpdateDrugsAndAllergies(HistoryDetailsResponse.class, addEditDrugsAndAllergiesRequest, this, this);

        } else {
            List<Drug> drugList = new ArrayList<>();
            for (DrugItem drugItem : drugsListHashMap.values()) {
                if (drugItem.getDrug() != null) {
                    drugList.add(drugItem.getDrug());
                }
            }
            DrugsAndAllergies drugsAndAllergies = new DrugsAndAllergies();
            drugsAndAllergies.setDrugs(drugList);
            drugsAndAllergies.setAllergies(allergy);


            Intent data = new Intent();
            data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(drugsAndAllergies));
            if (isFromMedication)
                getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_EXISTING_MEDICATION, data);
            else
                getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_DRUGS_AND_ALLERGIES_DETAIL, data);
            getDialog().dismiss();
//            getTargetFragment().onActivityResult();
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
        if (lastTextSearched == null || !lastTextSearched.equalsIgnoreCase(search)) {
            //sorting solar drugs list from server
            Util.checkNetworkStatus(mActivity);
            if (HealthCocoConstants.isNetworkOnline) {
                PAGE_NUMBER = 0;
                isLoadingFromSearch = true;
                isEndOfListAchieved = false;
                getDrugsList(false, PAGE_NUMBER, MAX_SIZE, search);
            } else
                Util.showToast(mActivity, R.string.user_offline);
        }
        lastTextSearched = search;
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    getDrugsList(true, PAGE_NUMBER, MAX_SIZE, "");
                    break;
                case GET_DRUGS_LIST_SOLR:
                    ArrayList<DrugsListSolrResponse> list = (ArrayList<DrugsListSolrResponse>) (ArrayList<?>) response.getDataList();
                    LogUtils.LOGD(TAG, "onResponse DrugsList Size " + drugsListSolr.size() + " isDataFromLocal " + response.isDataFromLocal());
                    if (drugsListSolr == null)
                        drugsListSolr = new ArrayList<>();
                    if (isLoadingFromSearch) {
                        drugsListSolr.clear();
                        notifyAdapterSolr(drugsListSolr);
                    }
                    if (!Util.isNullOrEmptyList(list)) {
                        drugsListSolr.addAll(list);
                    }
                    if (Util.isNullOrEmptyList(list) || list.size() < MAX_SIZE || Util.isNullOrEmptyList(list))
                        isEndOfListAchieved = true;

                    notifyAdapterSolr(drugsListSolr);
                    mActivity.hideLoading();
                    progressLoading.setVisibility(View.GONE);
                    isLoadingFromSearch = false;
                    isInitialLoading = false;
                    break;
                case ADD_UPDATE_DRUGS_AND_ALLERGIES_DETAIL:
                    if (response.getData() != null && response.getData() instanceof HistoryDetailsResponse) {
                        HistoryDetailsResponse historyDetailsResponse = (HistoryDetailsResponse) response.getData();
                        DrugsAndAllergies drugsAndAllergies = historyDetailsResponse.getDrugsAndAllergies();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_DRUGS_AND_ALLERGIES_DETAIL, new Intent().putExtra(PatientProfileDetailFragment.DRUG_AND_ALLERGIES, Parcels.wrap(drugsAndAllergies)));
                        getDialog().dismiss();
                    }
                    break;
                default:
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
        } else if (!Util.isNullOrBlank(errorMessage)) {
            errorMsg = errorMessage;
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        mActivity.hideLoading();
        progressLoading.setVisibility(View.GONE);
        Util.showToast(mActivity, errorMsg);

    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        progressLoading.setVisibility(View.GONE);
        mActivity.hideLoading();
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
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading && !isLoadingFromSearch) {
            PAGE_NUMBER = PAGE_NUMBER + 1;
            getDrugsList(false, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    public void addSelectedDrug(SelectDrugItemType selectDrugItemType, Object object) {
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
        switch (selectDrugItemType) {
            case ALL_DRUGS:
                DrugsListSolrResponse drugsSolr = (DrugsListSolrResponse) object;
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
                break;
        }

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
        if (selectedDrug != null) {
            addDrug(selectedDrug);
        }
    }

    public void addDrug(DrugItem drug) {
        if (drug != null) {
            drugsListHashMap.put(drug.getDrugId(), drug);
            notifyAdapter(new ArrayList<DrugItem>(drugsListHashMap.values()));
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

    @Override
    public void ondrugItemClick(SelectDrugItemType drugItemType, Object object) {
        addSelectedDrug(drugItemType, object);
    }

    @Override
    public void onDeleteItemClicked(DrugItem drug) {
        showConfirmationAlert(drug);
    }

    private void showConfirmationAlert(final DrugItem drug) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(R.string.confirm_remove_drug);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    drugsListHashMap.remove(drug.getDrugId());
                    notifyAdapter(new ArrayList<DrugItem>(drugsListHashMap.values()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onDrugItemClicked(DrugItem drugItem) {

    }

    @Override
    public MyScriptAddVisitsFragment getAddVisitFragment() {
        return null;
    }
}
