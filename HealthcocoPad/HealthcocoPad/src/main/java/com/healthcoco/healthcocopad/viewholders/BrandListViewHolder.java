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
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;

public class BrandListViewHolder extends HealthCocoViewHolder implements Checkable, CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    private TextView tvBrandName;
    //    private CheckBox cbSelectBrand;
    private CheckedTextView cbSelectBrand;
    private VaccineBrandResponse vaccineBrandResponse;
    private SelectedBrandListener selectedBrandListener;

    public BrandListViewHolder(HealthCocoActivity activity, View itemView, HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
//        super(activity, itemView, onItemClickListener, listenerObject);
//        this.selectedBrandListener = (SelectedBrandListener) listenerObject;
    }

    public BrandListViewHolder(HealthCocoActivity mActivity, SelectedBrandListener selectedBrandListener) {
        this.mActivity = mActivity;
        this.selectedBrandListener = selectedBrandListener;
    }
//
//    @Override
//    public void initViews(View itemView) {
//        tvBrandName = (TextView) itemView.findViewById(R.id.tv_brands_name);
////        cbSelectBrand = (CheckBox) itemView.findViewById(R.id.cb_select_brand);
//        cbSelectBrand = (LinearLayout) itemView.findViewById(R.id.cb_select_brand);
//        cbSelectBrand.setOnClickListener(this);
////        cbSelectBrand.setOnCheckedChangeListener(this);
//    }

//    @Override
//    public void applyData(Object object) {
//        vaccineBrandResponse = (VaccineBrandResponse) object;
//        if (vaccineBrandResponse != null) {
//            tvBrandName.setText(vaccineBrandResponse.getVaccineBrand().getName());
//        }
//        cbSelectBrand.setSelected(selectedBrandListener.isBrandSelected(vaccineBrandResponse.getVaccineBrandId()));
////        cbSelectBrand.setChecked(selectedBrandListener.isBrandSelected(vaccineBrandResponse.getVaccineBrandId()));
////        if (selectedBrandListener.isBrandSelected(vaccineBrandResponse.getVaccineBrandId())) {
////            cbSelectBrand.setSelected(true);
////            selectedBrandListener.isBrandSelect(true, vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse);
////        }
//    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        selectedBrandListener.isBrandSelect(isChecked, vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_item:
                if (!cbSelectBrand.isChecked())
                    cbSelectBrand.setChecked(true);
                else cbSelectBrand.setChecked(false);
                selectedBrandListener.isBrandSelect(cbSelectBrand.isChecked(), vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse);
                break;
            case R.id.cb_select_brand:
                if (!cbSelectBrand.isChecked())
                    cbSelectBrand.setChecked(true);
                else cbSelectBrand.setChecked(false);
                selectedBrandListener.isBrandSelect(cbSelectBrand.isChecked(), vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse);
                break;
            default:
                break;
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
//            if (selectedBrandListener.isBrandSelected(vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse.getVaccineId())) {
//                cbSelectBrand.setSelected(false);
//                cbSelectBrand.callOnClick();
//            }
            cbSelectBrand.setSelected(selectedBrandListener.isBrandSelected(vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse.getVaccineId()));
        }
    }

    @Override
    public View getContentView() {
        LinearLayout view = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_brand_list, null);
        LinearLayout linearLayout = view.findViewById(R.id.layout_item);
        tvBrandName = (TextView) view.findViewById(R.id.tv_brands_name);
        cbSelectBrand = (CheckedTextView) view.findViewById(R.id.cb_select_brand);
//        cbSelectBrand.setOnClickListener(this);
//        linearLayout.setOnClickListener(this);
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
