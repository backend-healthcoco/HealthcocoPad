package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Discount;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.Quantity;
import com.healthcoco.healthcocopad.bean.server.TreatmentFields;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.dialogFragment.SelectToothDetailDialogFragment;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.QuantityEnum;
import com.healthcoco.healthcocopad.enums.UnitType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.SelectedToothNumberListner;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentsListItemListener;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shreshtha on 18-07-2017.
 */

public class SelectedTreatmentsItemsListViewholder extends HealthCocoViewHolder implements View.OnClickListener, PopupWindowListener, HealthcocoTextWatcherListener, SelectedToothNumberListner {

    private final HealthCocoApplication mApp;
    private HealthCocoActivity mActivity;
    private TreatmentItem objData;
    private TextView btDelete;
    private SelectedTreatmentsListItemListener selectedTreatmentItemClickListener;
    private LinearLayout containerTreatmentItem;
    private TextView tvTreatmentName;
    private EditText etQtyPerDay;
    private EditText etCost;
    private EditText etDiscount;
    private TextView tvDiscountType;
    private TextView tvTotalRuppes;
    private TextView tvTreatmentStatus;
    private TextView tvTreatmentToothNo;
    private TextView tvTreatmentMaterial;
    private EditText etTextTreatmentNote;
    private LinearLayout layoutTreatmentToothNo;
    private DoctorProfile doctorProfile;

    public SelectedTreatmentsItemsListViewholder(HealthCocoActivity mActivity, SelectedTreatmentsListItemListener selectedTreatmentItemClickListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.selectedTreatmentItemClickListener = selectedTreatmentItemClickListener;
        this.mApp = ((HealthCocoApplication) mActivity.getApplication());
        this.doctorProfile = selectedTreatmentItemClickListener.getDoctorProfile();
    }

    @Override
    public void setData(Object object) {
        this.objData = (TreatmentItem) object;
    }

    @Override
    public void applyData() {
        tvTreatmentName.setText(String.valueOf(objData.getTreatmentService().getName()));
        if (doctorProfile != null) {
            if (doctorProfile.getSpecialities().contains("Dentist")) {
                if (!Util.isNullOrEmptyList(objData.getTreatmentService().getFieldsRequired())) {
                    ArrayList<String> fieldsRequired = objData.getTreatmentService().getFieldsRequired();

                    if (fieldsRequired.contains(HealthCocoConstants.TAG_TOOTH_NUMBER)) {
                        layoutTreatmentToothNo.setVisibility(View.VISIBLE);
                        tvTreatmentToothNo.setVisibility(View.VISIBLE);
                        tvTreatmentToothNo.setText("");
                    } else {
                        layoutTreatmentToothNo.setVisibility(View.GONE);
                    }
                    if (fieldsRequired.contains(HealthCocoConstants.TAG_TOOTH_MATERIAL)) {
                        tvTreatmentMaterial.setVisibility(View.VISIBLE);
                    } else {
                        tvTreatmentMaterial.setVisibility(View.INVISIBLE);
                    }

                } else {
                    layoutTreatmentToothNo.setVisibility(View.GONE);
                }
            } else {
                layoutTreatmentToothNo.setVisibility(View.GONE);
            }
        }
        if (objData.getQuantity() != null)

        {
            String formattedQtyPerDay = String.valueOf(objData.getQuantity().getValue());
            etQtyPerDay.setText(formattedQtyPerDay);
        } else etQtyPerDay.setText("1");

        if (objData.getDiscount() != null)

        {
            String formattedDiscount = String.valueOf(Util.formatDoubleNumber(objData.getDiscount().getValue()));
            UnitType unitType = objData.getDiscount().getUnit();
            if (unitType.equals(UnitType.INR)) {
                etDiscount.setText(formattedDiscount);
                tvDiscountType.setTag(objData.getDiscount().getUnit());
                tvDiscountType.setText(objData.getDiscount().getUnit().getSymbolId());
            } else {
                etDiscount.setText(formattedDiscount);
                tvDiscountType.setTag(UnitType.PERCENT);
                tvDiscountType.setText(UnitType.PERCENT.getSymbolId());
            }
        } else

        {
            etDiscount.setText("0");
            tvDiscountType.setTag(UnitType.PERCENT);
        }

        etCost.setText(String.valueOf(Util.formatDoubleNumber(objData.getCost())));

        tvTotalRuppes.setText(String.valueOf(Util.formatDoubleNumber(objData.getFinalCost())));

        addTreatmentFieldsDetail(objData);

        if (objData.getStatus() != null)

        {
            tvTreatmentStatus.setVisibility(View.VISIBLE);
            tvTreatmentStatus.setTag(objData.getStatus());
            tvTreatmentStatus.setText(objData.getStatus().getTreamentStatus());
        } else

        {
            tvTreatmentStatus.setTag(PatientTreatmentStatus.NOT_STARTED);
            tvTreatmentStatus.setText(PatientTreatmentStatus.NOT_STARTED.getTreamentStatus());
        }

        if (!Util.isNullOrBlank(objData.getNote())) {
            etTextTreatmentNote.setText(objData.getNote());
            etTextTreatmentNote.setVisibility(View.VISIBLE);
        } else {
//            etTextTreatmentNote.setText("");
        }

    }

    @Override
    public View getContentView() {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.list_item_add_new_treatment, null);
        initViews(view);
        initListeners();
        return view;
    }

    private void initViews(LinearLayout view) {
        btDelete = (TextView) view.findViewById(R.id.bt_delete);
        containerTreatmentItem = (LinearLayout) view.findViewById(R.id.container_treatment_item);
        tvTreatmentName = (TextView) view.findViewById(R.id.tv_text_treatment_name);
        tvTreatmentStatus = (TextView) view.findViewById(R.id.tv_text_treatment_status);
        tvDiscountType = (TextView) view.findViewById(R.id.tv_discount_type);
        etTextTreatmentNote = (EditText) view.findViewById(R.id.edit_text_treatment_note);
        etQtyPerDay = (EditText) view.findViewById(R.id.edit_qty_per_day);
        etCost = (EditText) view.findViewById(R.id.edit_cost);
        etDiscount = (EditText) view.findViewById(R.id.edit_discount);
        tvTotalRuppes = (TextView) view.findViewById(R.id.tv_total_ruppes);
        tvTreatmentToothNo = (TextView) view.findViewById(R.id.tv_tooth_numbers);
        tvTreatmentMaterial = (TextView) view.findViewById(R.id.tv_tooth_material);
        layoutTreatmentToothNo = (LinearLayout) view.findViewById(R.id.layout_treatment_tooth_number);

        mActivity.initPopupWindows(tvTreatmentStatus, PopupWindowType.STATUS_TYPE, PopupWindowType.STATUS_TYPE.getList(), this);
        mActivity.initPopupWindows(tvDiscountType, PopupWindowType.DISCOUNT_TYPE, PopupWindowType.DISCOUNT_TYPE.getList(), this);
        mActivity.initPopupWindows(tvTreatmentMaterial, PopupWindowType.MATERIAL_TYPE, PopupWindowType.MATERIAL_TYPE.getList(), this);

        etQtyPerDay.addTextChangedListener(new HealthcocoTextWatcher(etQtyPerDay, this));
        etCost.addTextChangedListener(new HealthcocoTextWatcher(etCost, this));
        etDiscount.addTextChangedListener(new HealthcocoTextWatcher(etDiscount, this));
    }

    private void initListeners() {
        btDelete.setOnClickListener(this);
        containerTreatmentItem.setOnClickListener(this);
        tvTreatmentToothNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete:
                selectedTreatmentItemClickListener.onDeleteItemClicked(objData);
                break;
            case R.id.container_treatment_item:
                selectedTreatmentItemClickListener.onTreatmentItemClicked(objData);
                break;
            case R.id.tv_tooth_numbers:
                SelectToothDetailDialogFragment selectToothDetailDialogFragment = new SelectToothDetailDialogFragment(this, getTreatmentItem().getTreatmentFields());
                selectToothDetailDialogFragment.show(mActivity.getSupportFragmentManager(),
                        selectToothDetailDialogFragment.getClass().getSimpleName());
                break;
        }
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case STATUS_TYPE:
                tvTreatmentStatus.setTag(object);
                PatientTreatmentStatus patientTreatmentStatus = getPatientTreatmentStatus(tvTreatmentStatus);
                tvTreatmentStatus.setText(patientTreatmentStatus.getTreamentStatus());
                break;
            case MATERIAL_TYPE:
                tvTreatmentMaterial.setText(object.toString());
                break;
            case DISCOUNT_TYPE:
                tvDiscountType.setTag(object);
                UnitType discountUnit = getDiscountUnitType(tvDiscountType);
                tvDiscountType.setText(discountUnit.getSymbolId());
                tvTotalRuppes.setText(String.valueOf(Util.formatDoubleNumber(getFinalCost())));
//                selectedTreatmentItemClickListener.onTotalCostChange();
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }

    private PatientTreatmentStatus getPatientTreatmentStatus(TextView tvStatus) {
        Object tag = tvStatus.getTag();
        if (tag != null && tag instanceof PatientTreatmentStatus)
            return (PatientTreatmentStatus) tag;
        return null;
    }

    private UnitType getDiscountUnitType(TextView tvDiscountType) {
        Object tag = tvDiscountType.getTag();
        if (tag != null && tag instanceof UnitType)
            return (UnitType) tag;
        return null;
    }

    public double getFinalCost() {
        double finalCost = 0;
        //final cost=((qty*cost)-((qty*cost)*discount))+(((qty*cost)-discount)*tax%)
        UnitType discountUnit = getDiscountUnitType(tvDiscountType);
        if (discountUnit != null) {
            switch (discountUnit) {
                case INR:
                    finalCost = (((getQuantityValue() * getCostPriceValue()) - getDiscountValue()));
                    break;
                case PERCENT:
                    finalCost = (((getQuantityValue() * getCostPriceValue()) - ((getQuantityValue() * getCostPriceValue()) * (getDiscountValue() / 100.0f))));
                    break;
            }
        }

        return finalCost;
    }

    private int getQuantityValue() {
        int quantityValue = 0;
        //tax percent
        if (!Util.isNullOrBlank(etQtyPerDay.getText().toString()))
            quantityValue = Integer.parseInt(etQtyPerDay.getText().toString());
        else quantityValue = 0;
        return quantityValue;
    }

    private double getDiscountValue() {
        double discount = 0;
        //discount percent
        if (!Util.isNullOrBlank(etDiscount.getText().toString()))
            discount = Double.parseDouble(etDiscount.getText().toString());
        else discount = 0;
        return discount;
    }

    private double getCostPriceValue() {
        double costPrice = 0;
        //cost price
        if (!Util.isNullOrBlank(etCost.getText().toString()))
            costPrice = Double.parseDouble(etCost.getText().toString());
        else costPrice = 0;
        return costPrice;
    }

    public double getCalculatedDiscount() {
        double calculatedDiscount = 0;
        //final cost=((qty*cost)-((qty*cost)*discount))
        UnitType discountUnit = getDiscountUnitType(tvDiscountType);
        if (discountUnit != null) {
            switch (discountUnit) {
                case INR:
                    calculatedDiscount = getDiscountValue();
                    break;
                case PERCENT:
                    calculatedDiscount = ((getQuantityValue() * getCostPriceValue()) * (getDiscountValue() / 100.0f));
                    break;
            }
        }
        return calculatedDiscount;
    }

    private Quantity getQuantity() {
        Quantity quantity = new Quantity();
        quantity.setType(QuantityEnum.QTY);
        quantity.setValue(getQuantityValue());
        return quantity;
    }

    private Discount getDiscount() {
        Discount discount = new Discount();
        discount.setValue(getDiscountValue());
        discount.setUnit(getDiscountUnitType(tvDiscountType));
        return discount;
    }

    @Override
    public void afterTextChange(View v, String s) {
        switch (v.getId()) {
            case R.id.edit_qty_per_day:
            case R.id.edit_cost:
            case R.id.edit_discount:
                tvTotalRuppes.setText(String.valueOf(Util.formatDoubleNumber(getFinalCost())));
//                selectedTreatmentItemClickListener.onTotalCostChange();
                break;
        }
    }


    public TreatmentItem getTreatmentItem() {
        objData.setDiscount(getDiscount());
        objData.setQuantity(getQuantity());
        objData.setCost(getCostPriceValue());
        objData.setFinalCost(getFinalCost());
        if (!Util.isNullOrBlank(etTextTreatmentNote.getText().toString()))
            objData.setNote(String.valueOf(etTextTreatmentNote.getText()));
        objData.setStatus(getPatientTreatmentStatus(tvTreatmentStatus));
        List<TreatmentFields> fieldsArrayList = new ArrayList<>();
        if (!Util.isNullOrBlank(tvTreatmentMaterial.getText().toString())) {
            TreatmentFields fields = new TreatmentFields();
            fields.setKey(HealthCocoConstants.TAG_TOOTH_MATERIAL);
            fields.setValue(tvTreatmentMaterial.getText().toString());
            fieldsArrayList.add(fields);
        }

        if (!Util.isNullOrBlank(tvTreatmentToothNo.getText().toString())) {
            TreatmentFields fields = new TreatmentFields();
            fields.setKey(HealthCocoConstants.TAG_TOOTH_NUMBER);
            fields.setValue(tvTreatmentToothNo.getText().toString());
            fieldsArrayList.add(fields);
        }
        objData.setTreatmentFields(fieldsArrayList);
        objData.setCalculatedDiscount(getCalculatedDiscount());
        return objData;
    }

    private void addTreatmentFieldsDetail(TreatmentItem treatmentItem) {
        if (!Util.isNullOrEmptyList(treatmentItem.getTreatmentFields())) {
            List<TreatmentFields> treatmentFieldsList = treatmentItem.getTreatmentFields();
            for (TreatmentFields treatmentField :
                    treatmentFieldsList) {
                if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_NUMBER)
                        && !Util.isNullOrBlank(treatmentField.getValue())) {
                    tvTreatmentToothNo.setText(treatmentField.getValue());
                } else tvTreatmentToothNo.setText("");

                if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_MATERIAL)
                        && !Util.isNullOrBlank(treatmentField.getValue())
                        && treatmentItem.getTreatmentService() != null
                        && !Util.isNullOrEmptyList(treatmentItem.getTreatmentService().getFieldsRequired())) {
                    ArrayList<String> serviceFieldsList = treatmentItem.getTreatmentService().getFieldsRequired();
                    for (String serviceField :
                            serviceFieldsList) {
                        if (serviceField.equals(HealthCocoConstants.TAG_TOOTH_MATERIAL)) {
                            if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_MATERIAL)) {
                                tvTreatmentMaterial.setText(treatmentField.getValue());
                            } else tvTreatmentMaterial.setText("");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDoneClicked(Object object) {
        HashMap<String, TreatmentFields> fieldsList = (HashMap<String, TreatmentFields>) object;
        ArrayList<TreatmentFields> list = new ArrayList<>();
        for (TreatmentFields fields : fieldsList.values()) {
            list.add(fields);
        }
        objData.setTreatmentFields(list);
        addTreatmentFieldsDetail(objData);
    }
}
