package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandResponse;
import com.healthcoco.healthcocopad.listeners.SelectedBrandListener;

public class BrandGroupListViewHolder extends HealthCocoViewHolder implements Checkable, CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    private TextView tvBrandName;
    //    private CheckBox cbSelectBrand;
    private CheckedTextView cbSelectBrand;
    private VaccineBrandResponse vaccineBrandResponse;
    private SelectedBrandListener selectedBrandListener;

    public BrandGroupListViewHolder(HealthCocoActivity mActivity, SelectedBrandListener selectedBrandListener) {
        this.mActivity = mActivity;
        this.selectedBrandListener = selectedBrandListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        selectedBrandListener.isBrandSelect(isChecked, vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.layout_item) {
            if (!cbSelectBrand.isChecked())
                cbSelectBrand.setChecked(true);
            else
                cbSelectBrand.setChecked(false);
            selectedBrandListener.isBrandSelect(cbSelectBrand.isChecked(), vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse);

        } else if (id == R.id.cb_select_brand) {
            if (!cbSelectBrand.isChecked())
                cbSelectBrand.setChecked(true);
            else
                cbSelectBrand.setChecked(false);
            selectedBrandListener.isBrandSelect(cbSelectBrand.isSelected(), vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse);

        } else {
            // default: do nothing
        }

    }

    @Override
    public void setData(Object object) {
        vaccineBrandResponse = (VaccineBrandResponse) object;
    }

    @Override
    public void applyData() {
        if (vaccineBrandResponse != null && vaccineBrandResponse.getVaccineBrand() != null) {
            tvBrandName.setText(vaccineBrandResponse.getVaccineBrand().getName());
//            cbSelectBrand.setSelected(selectedBrandListener.isBrandSelected(vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse.getVaccineId()));
            if (selectedBrandListener.isBrandGroupSelected()) {
                cbSelectBrand.setSelected(vaccineBrandResponse.isSelected());
            } else {
                cbSelectBrand.setSelected(false);
            }
        }
    }

    @Override
    public View getContentView() {
        LinearLayout view = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_brand_list, null);
        tvBrandName = (TextView) view.findViewById(R.id.tv_brands_name);
        cbSelectBrand = (CheckedTextView) view.findViewById(R.id.cb_select_brand);
//        cbSelectBrand.setOnClickListener(this);
        return view;
    }

    @Override
    public void setChecked(boolean checked) {
        selectedBrandListener.isBrandSelect(cbSelectBrand.isChecked(), vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse);
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void toggle() {

    }
}
