package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Prashant on 19-10-2018.
 */

public class AddEquivalentMeasurementDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener {


    public ArrayList<QuantityType> servingTypeList = new ArrayList<QuantityType>() {{
        add(QuantityType.TABLE_SPOON);
        add(QuantityType.TEA_SPOON);
        add(QuantityType.GLASS);
        add(QuantityType.BOWL);
        add(QuantityType.CUP);
        add(QuantityType.G);
        add(QuantityType.MG);
        add(QuantityType.LITRE);
        add(QuantityType.MILI_LITRE);
    }};
    private TextView titleTextView;

    private Bundle bundle;

    private List<EquivalentQuantities> quantitiesList = new ArrayList<>();
    private List<EquivalentQuantities> quantitiesListReceived = new ArrayList<>();
    private LinkedHashMap<QuantityType, EquivalentQuantities> quantitiesHashMap = new LinkedHashMap<>();

    private RecyclerView rvEquivalentMeasurement;
    private HealthcocoRecyclerViewAdapter equivalentMeasurementAdapter;


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

        bundle = getArguments();
        if (bundle != null)
            quantitiesListReceived = Parcels.unwrap(bundle.getParcelable(HealthCocoConstants.TAG_INTENT_DATA));

        setWidthHeight(0.70, 0.90);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapters();
        initData();
    }

    @Override
    public void initViews() {
        titleTextView = view.findViewById(R.id.tv_title);
        rvEquivalentMeasurement = view.findViewById(R.id.rv_equivalent_measurement);

        titleTextView.setText(R.string.add_equivalent);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
    }

    @Override
    public void initData() {
        notifyAdapter();
    }


    private void initAdapters() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvEquivalentMeasurement.setLayoutManager(layoutManager);
        rvEquivalentMeasurement.setItemAnimator(new DefaultItemAnimator());

        equivalentMeasurementAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.EQUIVALENT_MEASUREMENT, this);
        equivalentMeasurementAdapter.setListData((ArrayList<Object>) (Object) quantitiesList);
        rvEquivalentMeasurement.setAdapter(equivalentMeasurementAdapter);
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

        if (Util.isNullOrBlank(msg)) {
            addEquivalentMeasurement();
        } else
            Util.showToast(mActivity, msg);
    }

    private void addEquivalentMeasurement() {
        quantitiesList.clear();

        for (int position = 0; position < quantitiesHashMap.size(); position++) {
            View childView = rvEquivalentMeasurement.getChildAt(position);

            if (childView != null) {
                TextView tvType = childView.findViewById(R.id.tv_type);
                TextView tvServingType = childView.findViewById(R.id.tv_serving_type);
                EditText editValue = childView.findViewById(R.id.edit_value);

                String value = Util.getValidatedValueOrNull(editValue);
                if (!Util.isNullOrBlank(value)) {
                    EquivalentQuantities equivalentQuantities = new EquivalentQuantities();
                    equivalentQuantities.setValue(Util.getValidatedDoubleValue(editValue));
                    equivalentQuantities.setServingType((QuantityType) tvServingType.getTag());
                    equivalentQuantities.setType((QuantityType) tvType.getTag());
                    quantitiesList.add(equivalentQuantities);
                }
            }
        }

        Intent data = new Intent();
        data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(quantitiesList));
        getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_ADD_MEASUREMENT, mActivity.RESULT_OK, data);
        dismiss();
    }

    private void notifyAdapter() {
        for (QuantityType quantityType : servingTypeList) {
            EquivalentQuantities equivalentQuantity = new EquivalentQuantities();
            equivalentQuantity.setServingType(quantityType);
            equivalentQuantity.setType(QuantityType.G);
            quantitiesHashMap.put(quantityType, equivalentQuantity);
        }

        if (!Util.isNullOrEmptyList(quantitiesListReceived))
            for (EquivalentQuantities equivalentQuantities : quantitiesListReceived) {
                quantitiesHashMap.put(equivalentQuantities.getServingType(), equivalentQuantities);
            }

        quantitiesList.clear();
        quantitiesList.addAll(new ArrayList<>(quantitiesHashMap.values()));

        equivalentMeasurementAdapter.notifyDataSetChanged();
    }

}
