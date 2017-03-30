package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.CommonListDialogFragmentWithTitle;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shreshtha on 29-03-2017.
 */
public class DrugDoseTemplateItemViewHolder extends LinearLayout implements View.OnClickListener, CommonListDialogItemClickListener, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean> {

    private static final String GENERIC_NAME_SEPARATOR = ",";
    private static final String TAG = "";
    private HealthCocoApplication mApp;
    private TextView tvName;
    private TextView tvDirections;
    private TextView tvFrequency;
    private TextView etDuration;
    private DrugItem drugItem;
    private EditText etInstruction;
    private TextView tvGenericName;
    private TextView tvDrugDurationUnit;
    private CommonListDialogFragmentWithTitle commonListDialog;
    private DrugDurationUnit selectedDurationUnit;
    private DrugDosage selectedDosage;
    private DrugDirection selectedDirection;
    private ArrayList<DrugDurationUnit> receivedDurationUnitList;
    private ArrayList<DrugDosage> receivedFrequencyDosageList;
    private ArrayList<DrugDirection> receivedDirectionsList;
    private HealthCocoActivity mActivity;
    private boolean isDurationUnitLoaded;
    private boolean isDirectionLoaded;
    private boolean isFrequencyDosageLoaded;
    private User user;

    public DrugDoseTemplateItemViewHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DrugDoseTemplateItemViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrugDoseTemplateItemViewHolder(Context context) {
        super(context);
        this.mApp = ((HealthCocoApplication) context.getApplicationContext());
        this.mActivity = (HealthCocoActivity) context;
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item_tablet_dose_template, this);
//        initDefaultData();
        initViews();
        initListeners();
    }

    private void initDefaultData() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
        }
        getListFromLocal(LocalBackgroundTaskType.GET_FREQUENCY_ACTIVATED_LIST);
        getListFromLocal(LocalBackgroundTaskType.GET_DIRECTION_ACTIVATED_LIST);
        getListFromLocal(LocalBackgroundTaskType.GET_DURATION_UNIT);
    }

    private void getListFromLocal(LocalBackgroundTaskType localBackgroundTaskType) {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, localBackgroundTaskType, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initListeners() {
        tvDirections.setOnClickListener(this);
        tvFrequency.setOnClickListener(this);
        tvDrugDurationUnit.setOnClickListener(this);
    }

    private void initViews() {
        etInstruction = (EditText) findViewById(R.id.et_instruction);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvGenericName = (TextView) findViewById(R.id.tv_generic_name);
        tvDirections = (TextView) findViewById(R.id.tv_directions);
        tvFrequency = (TextView) findViewById(R.id.tv_frequency);
        tvDrugDurationUnit = (TextView) findViewById(R.id.tv_drug_duration_unit);
        etDuration = (TextView) findViewById(R.id.et_duration);
    }

    public void setData(DrugItem item) {
        this.drugItem = item;
        Drug drug = null;
        if (drugItem.getDrug() != null) {
            drug = drugItem.getDrug();
            drugItem.setDrugId(drug.getUniqueId());
        } else if (!Util.isNullOrBlank(drugItem.getDrugId())) {
            drug = LocalDataServiceImpl.getInstance(mApp).getDrug(drugItem.getDrugId());
            drugItem.setDrug(drug);
        }
        if (drug != null) {
            String drugName = Util.getValidatedValue(drug.getDrugName());
            if (drug.getDrugType() != null && !Util.isNullOrBlank(drug.getDrugType().getType()))
                drugName = drug.getDrugType().getType() + " " + drugName;
            tvName.setText(drugName);
        }
        String genericNamesFormatted = "";
        if (!Util.isNullOrEmptyList(drug.getGenericNames())) {
            for (GenericName genericName : drug.getGenericNames()) {
                int index = drug.getGenericNames().indexOf(genericName);
                genericNamesFormatted = genericNamesFormatted + " " + genericName.getName();
                if (index != drug.getGenericNames().size() - 1)
                    genericNamesFormatted = genericNamesFormatted + GENERIC_NAME_SEPARATOR;
            }
            tvGenericName.setText(genericNamesFormatted);
        }
        if (!Util.isNullOrEmptyList(drug.getDirection())) {
            DrugDirection direction = drug.getDirection().get(0);
            tvDirections.setText(Util.getValidatedValue((direction.getDirection())));
        }

        if (!Util.isNullOrBlank(drug.getDosage()))
            tvFrequency.setText(drug.getDosage());

        if (drug.getDuration() != null && !Util.isNullOrBlank(drug.getDuration().getValue()))
            etDuration.setText(drug.getDuration().getValue());

        if (drug.getDuration() != null && drug.getDuration().getDurationUnit() != null && !Util.isNullOrBlank(drug.getDuration().getDurationUnit().getUnit()))
            tvDrugDurationUnit.setText(drug.getDuration().getDurationUnit().getUnit());

        //initialising instructions popup
        if (!Util.isNullOrBlank(item.getInstructions())) {
            etInstruction.setVisibility(View.VISIBLE);
            etInstruction.setText(Util.getValidatedValue(item.getInstructions()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_drug_duration_unit:
                commonListDialog = openCommonListWithTitleDialogFragment(this, CommonListDialogType.DURATION, receivedDurationUnitList);
                break;
            case R.id.tv_frequency:
                commonListDialog = openCommonListWithTitleDialogFragment(this, CommonListDialogType.FREQUENCY, receivedFrequencyDosageList);
                break;
            case R.id.tv_directions:
                commonListDialog = openCommonListWithTitleDialogFragment(this, CommonListDialogType.DIRECTION, receivedDirectionsList);
                break;
            default:
                break;
        }
    }

    protected CommonListDialogFragmentWithTitle openCommonListWithTitleDialogFragment(CommonListDialogItemClickListener listener, CommonListDialogType popupType, List<?> list) {
        CommonListDialogFragmentWithTitle commonListDialogFragmentWithTitle = new CommonListDialogFragmentWithTitle(listener, popupType, list);
        commonListDialogFragmentWithTitle.show(mActivity.getSupportFragmentManager(), commonListDialogFragmentWithTitle.getClass().getSimpleName());
        return commonListDialogFragmentWithTitle;
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
                    tvDirections.setText(selectedDirection.getDirection());
                }
                break;
        }
        if (commonListDialog != null)
            commonListDialog.dismiss();
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

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            switch (response.getWebServiceType()) {
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
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {

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
}
