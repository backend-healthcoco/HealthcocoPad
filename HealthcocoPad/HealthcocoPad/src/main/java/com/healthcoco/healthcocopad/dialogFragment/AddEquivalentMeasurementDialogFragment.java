package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Prashant on 19-10-2018.
 */

public class AddEquivalentMeasurementDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener {

    public ArrayList<Object> quantityTypeList = new ArrayList<Object>() {{
        add(QuantityType.GM);
        add(QuantityType.MILI_LITRE);
    }};
    public ArrayList<Object> servingTypeList = new ArrayList<Object>() {{
        add(QuantityType.TABLE_SPOON);
        add(QuantityType.BOWL);
        add(QuantityType.CUP);
    }};
    private TextView titleTextView;
    private TextView tvType;
    private TextView tvServingType;
    private EditText editValue;


    public AddEquivalentMeasurementDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_new_equivalent_measurement, container, false);
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
        initViews();
        initListeners();
        initData();
    }

    @Override
    public void initViews() {
        titleTextView = view.findViewById(R.id.tv_title);
        tvType = view.findViewById(R.id.tv_type);
        tvServingType = view.findViewById(R.id.tv_serving_type);
        editValue = view.findViewById(R.id.edit_value);

        titleTextView.setText(R.string.add);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);

        mActivity.initPopupWindows(tvServingType, PopupWindowType.NUTRIENT_TYPE, servingTypeList, null);
        mActivity.initPopupWindows(tvType, PopupWindowType.NUTRIENT_TYPE, quantityTypeList, null);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
        }
    }

    private void validateData() {
        String msg = null;
        double value = Util.getValidatedDoubleValue(editValue);
        String servingType = String.valueOf(editValue.getText());
        String type = String.valueOf(editValue.getText());
        if (Util.isNullOrBlank(servingType))
            msg = getResources().getString((R.string.please_enter_serving_type));
        if (Util.isNullOrZeroNumber(value))
            msg = getResources().getString((R.string.please_enter_value));
        if (Util.isNullOrBlank(type))
            msg = getResources().getString((R.string.please_enter_type));

        if (Util.isNullOrBlank(msg)) {
            addEquivalentMeasurement(value);
        } else
            Util.showToast(mActivity, msg);
    }

    private void addEquivalentMeasurement(double value) {
        EquivalentQuantities equivalentQuantities = new EquivalentQuantities();
        equivalentQuantities.setValue(value);
        equivalentQuantities.setServingType((QuantityType) tvServingType.getTag());
        equivalentQuantities.setType((QuantityType) tvType.getTag());

        Intent data = new Intent();
        data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(equivalentQuantities));
        getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_ADD_INGREDIENT, mActivity.RESULT_OK, data);
        dismiss();
    }
}
