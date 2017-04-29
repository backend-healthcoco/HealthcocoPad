package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.Duration;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.dialogFragment.CommonListDialogFragmentWithTitle;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewPrescriptionListener;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shreshtha on 17-04-2017.
 */

public class SelectedPrescriptionDrugDoseItemsListViewHolder extends HealthCocoViewHolder implements View.OnClickListener, CommonListDialogItemClickListener,
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean> {

    private DrugItem objData;
    private TextViewFontAwesome btDelete;
    private AddNewPrescriptionListener addNewPrescriptionListener;
    private LinearLayout containerDrugDose;
    private static final String GENERIC_NAME_SEPARATOR = ",";
    private static final String TAG = "";
    private HealthCocoApplication mApp;
    private TextView tvName;
    private TextView tvDirections;
    private TextView tvFrequency;
    private EditText editDuration;
    private EditText etInstruction;
    private TextView tvGenericName;
    private TextView tvDrugDurationUnit;
    private CommonListDialogFragmentWithTitle commonListDialog;
    private ArrayList<DrugDurationUnit> receivedDurationUnitList;
    private ArrayList<DrugDosage> receivedFrequencyDosageList;
    private ArrayList<DrugDirection> receivedDirectionsList;
    private HealthCocoActivity mActivity;
    private boolean isDurationUnitLoaded;
    private boolean isDirectionLoaded;
    private boolean isFrequencyDosageLoaded;
    private User user;

    public SelectedPrescriptionDrugDoseItemsListViewHolder(HealthCocoActivity mActivity, AddNewPrescriptionListener addNewPrescriptionListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.addNewPrescriptionListener = addNewPrescriptionListener;
        this.mApp = ((HealthCocoApplication) mActivity.getApplication());
    }

    @Override
    public void setData(Object object) {
        this.objData = (DrugItem) object;
    }

    @Override
    public void applyData() {
        if (objData.getDrug() != null) {
            Drug drug = objData.getDrug();
            objData.setDrugId(drug.getUniqueId());
            String drugName = Util.getValidatedValue(drug.getDrugName());
            if (drug.getDrugType() != null && !Util.isNullOrBlank(drug.getDrugType().getType()))
                drugName = drug.getDrugType().getType() + " " + drugName;
            tvName.setText(drugName);
            String genericNamesFormatted = "";
            if (!Util.isNullOrEmptyList(drug.getGenericNames())) {
                for (GenericName genericName : drug.getGenericNames()) {
                    int index = drug.getGenericNames().indexOf(genericName);
                    genericNamesFormatted = genericNamesFormatted + " " + genericName.getName();
                    if (index != drug.getGenericNames().size() - 1)
                        genericNamesFormatted = genericNamesFormatted + GENERIC_NAME_SEPARATOR;
                }
                tvGenericName.setText(genericNamesFormatted);
                tvGenericName.setVisibility(View.VISIBLE);
            } else
                tvGenericName.setVisibility(View.GONE);
        }

        if (!Util.isNullOrEmptyList(objData.getDirection())) {
            DrugDirection direction = objData.getDirection().get(0);
            tvDirections.setText(Util.getValidatedValue(Util.getValidatedValue((direction.getDirection()))));
        } else
            tvDirections.setText("");

        tvFrequency.setText(Util.getValidatedValue(objData.getDosage()));

        if (objData.getDuration() != null)
            editDuration.setText(Util.getValidatedValue(objData.getDuration().getValue()));
        else
            editDuration.setText("");

        if (objData.getDuration() != null && objData.getDuration().getDurationUnit() != null)
            tvDrugDurationUnit.setText(Util.getValidatedValue(objData.getDuration().getDurationUnit().getUnit()));

        if (!Util.isNullOrBlank(objData.getInstructions())) {
            etInstruction.setVisibility(View.VISIBLE);
            etInstruction.setText(Util.getValidatedValue(objData.getInstructions()));
        } else
            etInstruction.setText("");
    }

    @Override
    public View getContentView() {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.list_item_selected_drug_add_visits, null);
        btDelete = (TextViewFontAwesome) view.findViewById(R.id.bt_delete);
        containerDrugDose = (LinearLayout) view.findViewById(R.id.container_drug_dose);
        tvName = (TextView) view.findViewById(R.id.tv_drug_name);
        tvGenericName = (TextView) view.findViewById(R.id.tv_generic_name);
        tvDirections = (TextView) view.findViewById(R.id.tv_directions);
        tvFrequency = (TextView) view.findViewById(R.id.tv_frequency);
        tvDrugDurationUnit = (TextView) view.findViewById(R.id.tv_drug_duration_unit);
        etInstruction = (EditText) view.findViewById(R.id.edit_instruction);
        editDuration = (EditText) view.findViewById(R.id.edit_duration);
        initListeners();
        return view;
    }

    private void initListeners() {
        btDelete.setOnClickListener(this);
        containerDrugDose.setOnClickListener(this);
        tvDirections.setOnClickListener(this);
        tvFrequency.setOnClickListener(this);
        tvDrugDurationUnit.setOnClickListener(this);
        if (addNewPrescriptionListener.getAddVisitFragment() != null) {
            editDuration.setOnTouchListener(addNewPrescriptionListener.getAddVisitFragment().getOnTouchListener());
            etInstruction.setOnTouchListener(addNewPrescriptionListener.getAddVisitFragment().getOnTouchListener());
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
            case R.id.bt_delete:
                addNewPrescriptionListener.onDeleteItemClicked(objData);
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
                    DrugDurationUnit selectedDurationUnit = (DrugDurationUnit) object;
                    if (selectedDurationUnit != null) {
                        objData.setDuration(getDuration(Util.getValidatedValueOrNull(editDuration), selectedDurationUnit));
                    }
                    tvDrugDurationUnit.setText(selectedDurationUnit.getUnit());
                }
                break;
            case FREQUENCY:
                if (object instanceof DrugDosage) {
                    DrugDosage selectedDosage = (DrugDosage) object;
                    objData.setDosage(Util.getValidatedValueOrNull(selectedDosage.getDosage()));
                    tvFrequency.setText(selectedDosage.getDosage());
                }
                break;
            case DIRECTION:
                if (object instanceof DrugDirection) {
                    DrugDirection selectedDirection = (DrugDirection) object;
                    ArrayList<DrugDirection> directionsList = null;
                    if (selectedDirection != null) {
                        directionsList = new ArrayList<DrugDirection>();
                        directionsList.add(getDirection(selectedDirection));
                    }
                    objData.setDirection(directionsList);
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
            errorMsg = mActivity.getResources().getString(R.string.error);
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
                        if (!Util.isNullOrEmptyList(receivedDurationUnitList) && objData.getDuration().getDurationUnit() == null) {
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

    public void setDurationUnitToAll(String unit) {
        if (!Util.isNullOrBlank(unit)) {
            editDuration.setText(unit);
        }
    }

    public DrugItem getDrug() {
        if (objData.getDuration() != null
                && objData.getDuration().getDurationUnit() != null
                && !Util.isNullOrBlank(objData.getDuration().getDurationUnit().getUnit()))
            objData.setDuration(getDuration(Util.getValidatedValueOrNull(editDuration), objData.getDuration().getDurationUnit()));
        else
            objData.setDuration(getDurationForTaggedViews(Util.getValidatedValueOrNull(editDuration), null));

        objData.setInstructions(Util.getValidatedValueOrNull(etInstruction));
        return objData;
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

    private Duration getDurationForTaggedViews(String value, DrugDurationUnit drugDurationUnit) {
        if (!Util.isNullOrBlank(value)) {
            Duration duration = new Duration();
            duration.setValue(value);
            duration.setDurationUnit(drugDurationUnit);
            return duration;
        }
        return null;
    }

    private Duration getDuration(String value, DrugDurationUnit drugDurationUnit) {
        if (drugDurationUnit != null && !Util.isNullOrBlank(drugDurationUnit.getUnit())) {
            Duration duration = new Duration();
            duration.setDurationUnit(drugDurationUnit);
            duration.setValue(value);
            return duration;
        }
        return null;
    }

}
