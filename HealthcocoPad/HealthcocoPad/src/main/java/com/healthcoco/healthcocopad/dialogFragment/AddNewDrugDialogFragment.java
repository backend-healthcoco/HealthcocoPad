package com.healthcoco.healthcocopad.dialogFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddDrugRequest;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.Duration;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewDrugListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shreshtha on 28-03-2017.
 */
public class AddNewDrugDialogFragment extends HealthCocoDialogFragment
        implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, AdapterView.OnItemClickListener {
    private AutoCompleteTextView autoTvDrugType;
    private EditText editName;
    private EditText etDuration;
    private EditText etGenericName;
    private TextView tvFrequency;
    private TextView tvDrugDurationUnit;

    private TextView tvDirection;
    private DrugItem selectedDrug = new DrugItem();
    private ArrayList<DrugDurationUnit> receivedDurationUnitList;
    private ArrayList<DrugDosage> receivedFrequencyDosageList;
    private ArrayList<DrugDirection> receivedDirectionsList;
    private ArrayList<GenericName> genericNames = new ArrayList<>();
    private boolean isDurationUnitLoaded;
    private boolean isDirectionLoaded;
    private boolean isFrequencyDosageLoaded;
    private CommonListDialogFragmentWithTitle commonListDialog;
    private DrugDurationUnit selectedDurationUnit;
    private DrugDosage selectedDosage;
    private DrugDirection selectedDirection;
    private AddNewDrugListener addNewDrugListener;
    private User user;
    private DrugType selectedDrugType;
    private Bundle bundle;
    private String uniqueId;
    private Drug drug;

    public AddNewDrugDialogFragment() {
    }

    public AddNewDrugDialogFragment(AddNewDrugListener addNewDrugListener) {
        this.addNewDrugListener = addNewDrugListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_add_new_drug, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        bundle = getArguments();
        if (bundle != null)
            uniqueId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
        if (!Util.isNullOrBlank(uniqueId)) {
            drug = LocalDataServiceImpl.getInstance(mApp).getDrug(uniqueId);
        }

        init();
        setWidthHeight(0.55, 0.60);
    }

    @Override
    public void init() {
        initViews();
        initData();
        initListeners();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initDefaultData() {
        getListFromLocal(LocalBackgroundTaskType.GET_FREQUENCY_ACTIVATED_LIST);
        getListFromLocal(LocalBackgroundTaskType.GET_DIRECTION_ACTIVATED_LIST);
        getListFromLocal(LocalBackgroundTaskType.GET_DURATION_UNIT);
    }

    private void getListFromLocal(LocalBackgroundTaskType localBackgroundTaskType) {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, localBackgroundTaskType, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        initActionbarTitle(getResources().getString(R.string.add_drug));
        autoTvDrugType = (AutoCompleteTextView) view.findViewById(R.id.autotv_drug_type);
        editName = (EditText) view.findViewById(R.id.edit_drug_name);
        etGenericName = (EditText) view.findViewById(R.id.edit_generic_name);
        etDuration = (EditText) view.findViewById(R.id.edit_duration);
        tvFrequency = (TextView) view.findViewById(R.id.tv_drug_dosage);
        tvDirection = (TextView) view.findViewById(R.id.tv_drug_direction);
        tvDrugDurationUnit = (TextView) view.findViewById(R.id.tv_drug_duration_unit);
    }

    @Override
    public void initListeners() {
        autoTvDrugType.setOnItemClickListener(this);
        tvDirection.setOnClickListener(this);
        tvFrequency.setOnClickListener(this);
        tvDrugDurationUnit.setOnClickListener(this);
        initSaveCancelButton(this);
    }

    private void initAutoTvAdapter(ArrayList<Object> arrayList) {
        try {
            if (!Util.isNullOrEmptyList(arrayList)) {
                AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_grey_background,
                        arrayList, AutoCompleteTextViewType.DRUG_TYPE);
                autoTvDrugType.setThreshold(0);
                autoTvDrugType.setAdapter(adapter);
                autoTvDrugType.setDropDownAnchor(R.id.autotv_drug_type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {
        if (drug != null) {
            if (drug.getDrugType() != null)
                if (!Util.isNullOrBlank(drug.getDrugType().getType()))
                    autoTvDrugType.setText(Util.getValidatedValue(drug.getDrugType().getType()));

            if (!Util.isNullOrBlank(drug.getDrugName()))
                editName.setText(Util.getValidatedValue(drug.getDrugName()));

            if (drug.getDosageTime() != null)
                if (!Util.isNullOrBlank(drug.getDosageTime().getDosage()))
                    tvFrequency.setText(Util.getValidatedValue(drug.getDosageTime().getDosage()));


            if (drug.getDuration() != null) {
                if (!Util.isNullOrBlank(drug.getDuration().getValue()))
                    etDuration.setText(Util.getValidatedValue(drug.getDuration().getValue()));
                if (!Util.isNullOrBlank(drug.getDuration().getDurationUnit().getUnit()))
                    tvDrugDurationUnit.setText(Util.getValidatedValue(drug.getDuration().getDurationUnit().getUnit()));
            }

            if (!Util.isNullOrEmptyList(drug.getDirection()))
                for (DrugDirection drugDirection : drug.getDirection()) {
                    if (!Util.isNullOrBlank(drugDirection.getDirection()))
                        tvDirection.setText(Util.getValidatedValue(drugDirection.getDirection()));
                }

            if (!Util.isNullOrEmptyList(drug.getGenericNames()))
                for (GenericName gName : drug.getGenericNames()) {
                    if (!Util.isNullOrBlank(gName.getName()))
                        etGenericName.setText(Util.getValidatedValue(gName.getName()));
                }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
            case R.id.tv_drug_duration_unit:
                commonListDialog = openCommonListWithTitleDialogFragment(this, CommonListDialogType.DURATION, receivedDurationUnitList);
                break;
            case R.id.tv_drug_dosage:
                commonListDialog = openCommonListWithTitleDialogFragment(this, CommonListDialogType.FREQUENCY, receivedFrequencyDosageList);
                break;
            case R.id.tv_drug_direction:
                commonListDialog = openCommonListWithTitleDialogFragment(this, CommonListDialogType.DIRECTION, receivedDirectionsList);
                break;
            default:
                break;
        }
    }

    private void validateData() {
        String msg = null;
        String name = String.valueOf(editName.getText());
        String generic = String.valueOf(etGenericName.getText());
        GenericName genericName = new GenericName();

//        Util.showToast(mActivity, msg);
        if (selectedDirection != null) {
            ArrayList<DrugDirection> directionsList = new ArrayList<DrugDirection>();
            directionsList.add(getDirection(selectedDirection));
            selectedDrug.setDirection(directionsList);
        }
        if (selectedDurationUnit != null) {
            selectedDrug.setDuration(getDuration(Util.getValidatedValueOrNull(etDuration), selectedDurationUnit));
        }
        if (selectedDosage != null) {
            selectedDrug.setDosage(Util.getValidatedValueOrNull(selectedDosage.getDosage()));
        }
        if (!Util.isNullOrBlank(generic)) {
            genericName.setName(generic);
            genericNames.add(genericName);
        }
        if (Util.isNullOrBlank(name))
            msg = getResources().getString(R.string.please_enter_drug_name);
        if (Util.isNullOrBlank(msg)) {
            addDrug(name);
        } else {
            Util.showToast(mActivity, msg);
        }
    }

    private DrugDirection getDirection(DrugDirection selectedDirection) {
        try {
            DrugDirection direction = new DrugDirection();
            ReflectionUtil.copy(direction, selectedDirection);
            direction.setDiscarded(null);
            return direction;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return selectedDirection;
    }

    private Duration getDuration(String value, DrugDurationUnit drugDurationUnit) {
        if (!Util.isNullOrBlank(value) && drugDurationUnit != null && !Util.isNullOrBlank(drugDurationUnit.getUnit())) {
            Duration duration = new Duration();
            duration.setDurationUnit(drugDurationUnit);
            duration.setValue(value);
            return duration;
        }
        return null;
    }

    private void addDrug(String name) {
        mActivity.showLoading(false);
        AddDrugRequest addDrugRequest = new AddDrugRequest();
        addDrugRequest.setDoctorId(user.getUniqueId());
        addDrugRequest.setLocationId(user.getForeignLocationId());
        addDrugRequest.setHospitalId(user.getForeignHospitalId());

        // setting drugtype and its doctor,hospital and location ids
        if (selectedDrugType != null) {
            DrugType drugType = new DrugType();
            drugType.setUniqueId(selectedDrugType.getUniqueId());
            drugType.setType(selectedDrugType.getType());
            drugType.setDoctorId("globalDoctor");
            addDrugRequest.setDrugType(drugType);
        }
        if (genericNames != null)
            addDrugRequest.setGenericNames(genericNames);
        addDrugRequest.setDrugName(name);
        if (drug != null)
            addDrugRequest.setUniqueId(drug.getUniqueId()
            );
//        addDrugRequest.setDrugId(selectedDrugType.getUniqueId());
        addDrugRequest.setDosage(selectedDrug.getDosage());
        addDrugRequest.setDirection(selectedDrug.getDirection());
        addDrugRequest.setDuration(selectedDrug.getDuration());
//        WebDataServiceImpl.getInstance((HealthCocoApplication) mActivity.getApplication()).addDrug(Drug.class, this, this, addDrugRequest);
        WebDataServiceImpl.getInstance((HealthCocoApplication) mActivity.getApplication()).addDrug(Drug.class, this, this, addDrugRequest);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        if (volleyResponseBean.getWebServiceType() != null) {
            switch (volleyResponseBean.getWebServiceType()) {
                case GET_DRUG_DOSAGE:
                    isFrequencyDosageLoaded = true;
                    break;
                case GET_DURATION_UNIT:
                    isDurationUnitLoaded = true;
                    break;
                case GET_DIRECTION:
                    isDirectionLoaded = true;
                    break;
            }
            if (isFrequencyDosageLoaded && isDurationUnitLoaded && isDirectionLoaded)
                mActivity.hideLoading();
        }
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            switch (response.getWebServiceType()) {
                case GET_DRUG_TYPE:
                    if (response.isValidDataList(response)) {
                        ArrayList<Object> drugTypeList = (ArrayList<Object>) (ArrayList<?>) response.getDataList();
                        if (!Util.isNullOrEmptyList(drugTypeList))
                            initAutoTvAdapter(drugTypeList);
                        initDefaultData();
                    }
                    break;
                case ADD_DRUG:
                    Util.showToast(mActivity, String.format(getResources().getString(R.string.success_drug_added), Util.getValidatedValueOrBlankWithoutTrimming(editName)));
                    if (response.isValidData(response)) {
                        if (addNewDrugListener != null) {
                            Drug drug = (Drug) response.getData();
                            addNewDrugListener.onSaveClicked(drug);
                        } else {
                            LocalDataServiceImpl.getInstance(mApp).addDrug((Drug) response.getData());
                            getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, HealthCocoConstants.RESULT_CODE_REFERENCE_LIST, null);
                        }
                        dismiss();
                    }
                    break;
                case GET_DURATION_UNIT:
                    if (response.isDataFromLocal()) {
                        receivedDurationUnitList = (ArrayList<DrugDurationUnit>) (ArrayList<?>) response.getDataList();
                        if (!Util.isNullOrEmptyList(receivedDurationUnitList) && selectedDurationUnit == null) {
                            LogUtils.LOGD(TAG, "Success onResponse receivedDurationUnitList Size " + receivedDurationUnitList.size() + " isDataFromLocal " + response.isDataFromLocal());
                            setDefaultAsDays(receivedDurationUnitList);
                        }
                    }
                    isDurationUnitLoaded = true;
                    break;
                case GET_DRUG_DOSAGE:
                    if (response.isDataFromLocal()) {
                        receivedFrequencyDosageList = (ArrayList<DrugDosage>) (ArrayList<?>) response.getDataList();
                        if (!Util.isNullOrEmptyList(receivedFrequencyDosageList))
                            LogUtils.LOGD(TAG, "Success onResponse receivedFrequencyDosageList Size " + receivedFrequencyDosageList.size() + " isDataFromLocal " + response.isDataFromLocal());
                    }
                    isFrequencyDosageLoaded = true;
                    break;
                case GET_DIRECTION:
                    if (response.isDataFromLocal()) {
                        receivedDirectionsList = (ArrayList<DrugDirection>) (ArrayList<?>) response.getDataList();
                        if (!Util.isNullOrEmptyList(receivedDirectionsList))
                            LogUtils.LOGD(TAG, "Success onResponse receivedDirectionsList Size " + receivedDirectionsList.size() + " isDataFromLocal " + response.isDataFromLocal());
                    }
                    isDirectionLoaded = true;
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void setDefaultAsDays(ArrayList<DrugDurationUnit> receivedDurationUnitList) {
        for (DrugDurationUnit durationUnit :
                receivedDurationUnitList) {
            if (durationUnit.getUnit().toUpperCase(Locale.ENGLISH).contains("day".toUpperCase(Locale.ENGLISH))) {
                onDialogItemClicked(CommonListDialogType.DURATION, durationUnit);
                break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Object object = parent.getAdapter().getItem(position);
            if (object instanceof DrugType) {
                selectedDrugType = (DrugType) object;
                autoTvDrugType.setText(Util.getValidatedValue(selectedDrugType.getType()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                    volleyResponseBean = LocalDataServiceImpl.getInstance((HealthCocoApplication) mActivity.getApplication())
                            .getDrugTypeListAdResponse(WebServiceType.GET_DRUG_TYPE, user.getUniqueId(), this, this);
                }
                break;
            case ADD_DIRECTIONS:
                LocalDataServiceImpl.getInstance(mApp).addDirectionsList((ArrayList<DrugDirection>) (ArrayList<?>) response.getDataList());
            case GET_DIRECTION_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getGlobalIncludedDosageDurationDirectionList(WebServiceType.GET_DIRECTION, user.getUniqueId(), false, 0, null, null);
                break;
            case ADD_DRUG_DOSAGE:
                LocalDataServiceImpl.getInstance(mApp).addDrugDosageList((ArrayList<DrugDosage>) (ArrayList<?>) response.getDataList());
            case GET_FREQUENCY_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getGlobalIncludedDosageDurationDirectionList(WebServiceType.GET_DRUG_DOSAGE, user.getUniqueId(), false, 0, null, null);
                break;
            case ADD_DURATION_UNIT:
                LocalDataServiceImpl.getInstance(mApp).addDurationUnitList((ArrayList<DrugDurationUnit>) (ArrayList<?>) response.getDataList());
            case GET_DURATION_UNIT:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getGlobalIncludedDosageDurationDirectionList(WebServiceType.GET_DURATION_UNIT, user.getUniqueId(), false, 0, null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {
        switch (commonListDialogType) {
            case DURATION:
                if (object instanceof DrugDurationUnit) {
                    selectedDurationUnit = (DrugDurationUnit) object;
                    tvDrugDurationUnit.setText(selectedDurationUnit.getUnit());
                }
                break;
            case FREQUENCY:
                if (object instanceof DrugDosage) {
                    selectedDosage = (DrugDosage) object;
                    tvFrequency.setText(selectedDosage.getDosage());
                }
                break;
            case DIRECTION:
                if (object instanceof DrugDirection) {
                    selectedDirection = (DrugDirection) object;
                    tvDirection.setText(selectedDirection.getDirection());
                }
                break;
        }
        if (commonListDialog != null)
            commonListDialog.dismiss();
    }
}