package com.healthcoco.healthcocopad.viewholders;

import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.TotalTreatmentCostDiscountValues;
import com.healthcoco.healthcocopad.bean.server.Discount;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.InvoiceItem;
import com.healthcoco.healthcocopad.bean.server.Quantity;
import com.healthcoco.healthcocopad.bean.server.TreatmentFields;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.UnitValue;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.dialogFragment.SelectToothDetailDialogFragment;
import com.healthcoco.healthcocopad.enums.InvoiceItemType;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.QuantityEnum;
import com.healthcoco.healthcocopad.enums.UnitType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.SelectedInvoiceListItemListener;
import com.healthcoco.healthcocopad.listeners.SelectedToothNumberListner;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentsListItemListener;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * Created by Prashant on 16-12-2017.
 */

public class SelectedInvoiceItemsListViewholder extends HealthCocoViewHolder implements View.OnClickListener, PopupWindowListener, HealthcocoTextWatcherListener, SelectedToothNumberListner {

    private final HealthCocoApplication mApp;
    private HealthCocoActivity mActivity;
    private InvoiceItem objData;
    private TextView btDelete;
    private LinearLayout containerTreatmentItem;
    private TextView tvTreatmentName;
    private EditText etQtyPerDay;
    private EditText etCost;
    private EditText etDiscount;
    private EditText etTaxes;
    private TextView tvDiscountType;
    private TextView tvTotalRuppes;
    private TextView tvTreatmentStatus;
    private TextView tvTreatmentToothNo;
    private TextView tvTreatmentMaterial;
    private TextViewFontAwesome textviewFontawesomeInvoiceIcon;
    private EditText etTextTreatmentNote;
    private LinearLayout layoutTreatmentToothNo;
    private LinearLayout layoutWriteNote;
    private DoctorProfile doctorProfile;
    private UnitType discountUnit;
    private SelectedInvoiceListItemListener selectedInvoiceListItemListener;

    public SelectedInvoiceItemsListViewholder(HealthCocoActivity mActivity, SelectedInvoiceListItemListener selectedInvoiceListItemListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.selectedInvoiceListItemListener = selectedInvoiceListItemListener;
        this.mApp = ((HealthCocoApplication) mActivity.getApplication());
        this.doctorProfile = selectedInvoiceListItemListener.getDoctorProfile();
    }

    @Override
    public void setData(Object object) {
        this.objData = (InvoiceItem) object;
    }

    @Override
    public void applyData() {

        if (objData.getType().equals(InvoiceItemType.PRODUCT)) {
            layoutTreatmentToothNo.setVisibility(View.GONE);
            layoutWriteNote.setVisibility(View.INVISIBLE);
            textviewFontawesomeInvoiceIcon.setText(R.string.fa_product);
        } else if (objData.getType().equals(InvoiceItemType.TEST)) {
            layoutTreatmentToothNo.setVisibility(View.GONE);
            layoutWriteNote.setVisibility(View.INVISIBLE);
            textviewFontawesomeInvoiceIcon.setText(R.string.fa_test);
        } else if (objData.getType().equals(InvoiceItemType.SERVICE)) {
            textviewFontawesomeInvoiceIcon.setText(R.string.fa_service);
            layoutWriteNote.setVisibility(View.VISIBLE);

            if (!Util.isNullOrEmptyList(objData.getTreatmentFields())) {
                List<TreatmentFields> treatmentFieldsList = objData.getTreatmentFields();
                for (TreatmentFields treatmentField :
                        treatmentFieldsList) {
                    if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_NUMBER)
                            && !Util.isNullOrBlank(treatmentField.getValue())) {
                        tvTreatmentToothNo.setText(treatmentField.getValue());
                        layoutTreatmentToothNo.setVisibility(View.VISIBLE);
                    } else {
                        layoutTreatmentToothNo.setVisibility(View.GONE);
                        tvTreatmentToothNo.setText("");
                    }

                    if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_MATERIAL)
                            && !Util.isNullOrBlank(treatmentField.getValue())) {
                        if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_MATERIAL)) {
                            tvTreatmentMaterial.setText(treatmentField.getValue());
                            tvTreatmentMaterial.setVisibility(View.VISIBLE);
                        } else {
                            tvTreatmentMaterial.setVisibility(View.GONE);
                            tvTreatmentMaterial.setText("");
                        }
                    }
                }
            } else {
                layoutTreatmentToothNo.setVisibility(View.GONE);
            }
        }

        tvTreatmentName.setText(String.valueOf(objData.getName()));

        if (objData.getQuantity() != null) {
            String formattedQtyPerDay = String.valueOf(objData.getQuantity().getValue());
            etQtyPerDay.setText(formattedQtyPerDay);
        } else etQtyPerDay.setText("1");


        if (objData.getDiscount() != null && objData.getDiscount().getUnit() != null) {
            String formattedDiscount = String.valueOf(Util.formatDoubleNumber(objData.getDiscount().getValue()));
            etDiscount.setText(formattedDiscount);
            discountUnit = objData.getDiscount().getUnit();
            tvDiscountType.setText(objData.getDiscount().getUnit().getSymbolId());
        } else {
            etDiscount.setText("0");
            tvDiscountType.setText(UnitType.PERCENT.getSymbolId());
        }

        if (objData.getTax() != null) {
            String formattedTaxText = String.valueOf(Util.formatDoubleNumber(objData.getTax().getValue()));
            UnitType unitType = objData.getTax().getUnit();
            if (unitType.equals(UnitType.INR))
                etTaxes.setText(formattedTaxText);
            else etTaxes.setText(formattedTaxText);
        } else etTaxes.setText("0");

        etCost.setText(Util.formatDoubleNumber(objData.getCost()));

        tvTotalRuppes.setText(Util.formatDoubleNumber(objData.getFinalCost()));

        if (objData.getStatus() != null) {
            tvTreatmentStatus.setVisibility(View.VISIBLE);
            PatientTreatmentStatus status = objData.getStatus();
            tvTreatmentStatus.setText(status.getTreamentStatus());
        } else {
            tvTreatmentStatus.setTag(PatientTreatmentStatus.NOT_STARTED);
            tvTreatmentStatus.setText(PatientTreatmentStatus.NOT_STARTED.getTreamentStatus());
        }
    }

    @Override
    public View getContentView() {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.list_item_add_new_invoice, null);
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
        etTaxes = (EditText) view.findViewById(R.id.edit_taxes);
        tvTotalRuppes = (TextView) view.findViewById(R.id.tv_total_ruppes);
        tvTreatmentToothNo = (TextView) view.findViewById(R.id.tv_tooth_numbers);
        tvTreatmentMaterial = (TextView) view.findViewById(R.id.tv_tooth_material);
        layoutTreatmentToothNo = (LinearLayout) view.findViewById(R.id.layout_treatment_tooth_number);
        textviewFontawesomeInvoiceIcon = (TextViewFontAwesome) view.findViewById(R.id.textview_fontawesome_invoice_icon);
        layoutWriteNote = (LinearLayout) view.findViewById(R.id.layout_write_notes);

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
        tvTotalRuppes.addTextChangedListener(new HealthcocoTextWatcher(tvTotalRuppes, this));
        tvDiscountType.addTextChangedListener(new HealthcocoTextWatcher(tvDiscountType, this));
        etTaxes.addTextChangedListener(new HealthcocoTextWatcher(etTaxes, this));

        etDiscount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete:
                selectedInvoiceListItemListener.onDeleteItemClicked(objData);
                break;
            case R.id.container_treatment_item:
                selectedInvoiceListItemListener.onInvoiceItemClicked(objData);
                break;
            case R.id.tv_tooth_numbers:
                SelectToothDetailDialogFragment selectToothDetailDialogFragment = new SelectToothDetailDialogFragment(this, getInvoiceItem().getTreatmentFields());
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
                objData.setStatus(patientTreatmentStatus);
                break;
            case MATERIAL_TYPE:
                tvTreatmentMaterial.setText(object.toString());
                break;
            case DISCOUNT_TYPE:
                tvDiscountType.setTag(object);
                if (object instanceof UnitType) {
                    discountUnit = (UnitType) object;
                    tvDiscountType.setText(discountUnit.getSymbolId());
                    tvTotalRuppes.setText(String.valueOf(Util.formatDoubleNumber(getFinalCost())));
                    if (discountUnit == UnitType.INR)
                        etDiscount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etCost.getText().length())});
                    if (discountUnit == UnitType.PERCENT)
                        etDiscount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                }
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


    public double getCalculatedTax() {
        double calculatedTax = 0;
        //final cost=((qty*cost)-((qty*cost)*discount))
        if (discountUnit != null) {
            switch (discountUnit) {
                case INR:
                    calculatedTax = ((((getQuantityValue() * getCostPriceValue()) - getDiscountValue()) * (getTaxValue() / 100.0f)));
                    break;
                case PERCENT:
                    calculatedTax = ((((getQuantityValue() * getCostPriceValue()) - ((getQuantityValue() * getCostPriceValue()) * (getDiscountValue() / 100.0f))) * (getTaxValue() / 100.0f)));
                    break;
            }
        }
        return calculatedTax;
    }

    public double getCalculatedDiscount() {
        double calculatedDiscount = 0;
        //final cost=((qty*cost)-((qty*cost)*discount))
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

    public double getFinalCost() {
        double finalCost = 0;
        //final cost=((qty*cost)-((qty*cost)*discount))+(((qty*cost)-discount)*tax%)
        if (discountUnit != null) {
            switch (discountUnit) {
                case INR:
                    finalCost = (((getQuantityValue() * getCostPriceValue()) - getDiscountValue()) + ((((getQuantityValue() * getCostPriceValue()) - getDiscountValue())) * (getTaxValue() / 100.0f)));
                    break;
                case PERCENT:
                    finalCost = (((getQuantityValue() * getCostPriceValue()) - ((getQuantityValue() * getCostPriceValue()) * (getDiscountValue() / 100.0f))) + (((getQuantityValue() * getCostPriceValue()) - ((getQuantityValue() * getCostPriceValue()) * (getDiscountValue() / 100.0f))) * (getTaxValue() / 100.0f)));
                    break;
            }
        }
        return finalCost;
    }

    public double getTaxValue() {
        double taxAmount = 0;
        //tax percent
        if (!Util.isNullOrBlank(etTaxes.getText().toString()))
            taxAmount = Double.parseDouble(etTaxes.getText().toString());
        else taxAmount = 0;
        return taxAmount;
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

    private UnitType getDiscountUnitType(TextView tvDiscountType) {
        Object tag = tvDiscountType.getTag();
        if (tag != null && tag instanceof UnitType)
            return (UnitType) tag;
        return null;
    }

    private Quantity getQuantity() {
        Quantity quantity = new Quantity();
        quantity.setType(QuantityEnum.QTY);
        quantity.setValue(getQuantityValue());
        return quantity;
    }

    private Discount getTax() {
        Discount tax = new Discount();
        tax.setValue(getTaxValue());
        tax.setUnit(UnitType.PERCENT);
        return tax;
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
            case R.id.edit_cost:
            case R.id.edit_discount:
            case R.id.edit_taxes:
            case R.id.edit_qty_per_day:
                setValidatedDoubleValue(v, s);
                tvTotalRuppes.setText(String.valueOf(Util.formatDoubleNumber(getFinalCost())));
                break;
            case R.id.tv_total_ruppes:
                LogUtils.LOGD(TAG, "TextChange total Cost " + s);
                setValidatedDoubleValue(tvTotalRuppes, s);
                TotalTreatmentCostDiscountValues totalTreatmentCostDiscountValues = new TotalTreatmentCostDiscountValues();
                totalTreatmentCostDiscountValues.setTotalGrandTotal(getValidatedValue(tvTotalRuppes));

                //getting n setting cost as per quantity
                double costPriceValue = getValidatedValue(etCost);
                double quantity = getValidatedValue(etQtyPerDay);

                if (quantity > 0) {
                    totalTreatmentCostDiscountValues.setTotalCost(costPriceValue * quantity);
                } else
                    totalTreatmentCostDiscountValues.setTotalCost(costPriceValue);

                totalTreatmentCostDiscountValues.setTotalTax(getCalculatedTax());
//                getDiscountUnitType(tvDiscountType);
                selectedInvoiceListItemListener.onTotalValueTypeDetailChanged(objData.getItemId(), totalTreatmentCostDiscountValues);
//                selectedTreatmentItemClickListener.onTotalValueTypeDetailChanged(SelectedTreatmentsListFragment.TotalValueType.TOTAL_GRAND_TOTAL, objData.getCustomUniqueId(), parseDouble(s));
                break;
            case R.id.tv_discount_type:
                setValidatedDoubleValue(tvDiscountType, s);
                break;

        }
    }

    private double setValidatedDoubleValue(View view, String value) {
        double doubleValue = 0;
        if (value != null && value instanceof String && !Util.isNullOrBlank(value) && !(view.getId() == tvDiscountType.getId()))
            doubleValue = Double.parseDouble(value);
        switch (view.getId()) {
            case R.id.edit_cost:
                objData.setCost(doubleValue);
                break;
            case R.id.tv_total_ruppes:
                objData.setFinalCost(doubleValue);
                break;
            case R.id.edit_qty_per_day:
                Quantity quantity = objData.getQuantity();
                if (quantity == null)
                    quantity = new Quantity();
                quantity.setValue((int) doubleValue);
                objData.setQuantity(quantity);
                break;
            case R.id.edit_discount:
                Discount discountValue = objData.getDiscount();
                if (discountValue == null)
                    discountValue = new Discount();
                discountValue.setValue(doubleValue);
                objData.setDiscount(discountValue);
                break;
            case R.id.edit_taxes:
                objData.setTax(getTax());
            case R.id.tv_discount_type:
                Discount discountUnit = objData.getDiscount();
                if (discountUnit == null)
                    discountUnit = new Discount();
                discountUnit.setUnit(UnitType.getUnitType(mActivity, value));
                objData.setDiscount(discountUnit);
                break;
        }

        return doubleValue;
    }

    private double getValidatedValue(View view) {
        double doubleValue = 0;
        switch (view.getId()) {
            case R.id.edit_cost:
                doubleValue = getCostPriceValue();
                break;
            case R.id.tv_total_ruppes:
                doubleValue = getFinalCost();
                break;
            case R.id.edit_qty_per_day:
                Quantity quantity = getQuantity();
                if (quantity != null)
                    doubleValue = quantity.getValue();
                break;
        }

        return doubleValue;
    }


    public InvoiceItem getInvoiceItem() {
        objData.setDiscount(getDiscount());
        objData.setQuantity(getQuantity());
        objData.setCost(getCostPriceValue());
        objData.setFinalCost(getFinalCost());
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


    private void addTreatmentFieldsDetail(InvoiceItem invoiceItem) {
        if (!Util.isNullOrEmptyList(invoiceItem.getTreatmentFields())) {
            List<TreatmentFields> treatmentFieldsList = invoiceItem.getTreatmentFields();
            for (TreatmentFields treatmentField :
                    treatmentFieldsList) {
                if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_NUMBER)
                        && !Util.isNullOrBlank(treatmentField.getValue())) {
                    tvTreatmentToothNo.setText(treatmentField.getValue());
                } else tvTreatmentToothNo.setText("");

                if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_MATERIAL)
                        && !Util.isNullOrBlank(treatmentField.getValue())) {
                    if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_MATERIAL)) {
                        tvTreatmentMaterial.setText(treatmentField.getValue());
                    } else tvTreatmentMaterial.setText("");
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
