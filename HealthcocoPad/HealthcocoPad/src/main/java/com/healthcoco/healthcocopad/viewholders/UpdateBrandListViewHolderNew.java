package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.BrandGroupListAdapter;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandAssociationRequest;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandResponse;
import com.healthcoco.healthcocopad.listeners.SelectedBrandListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ExpandableGridView;

import java.util.ArrayList;

public class UpdateBrandListViewHolderNew extends HealthCocoViewHolder implements AdapterView.OnItemClickListener {

    private SelectedBrandListener selectedBrandListener;
    private TextView tvVaccineName;
    private VaccineBrandAssociationRequest vaccineBrandAssociationRequest;
    private ExpandableGridView rvBrandList;
    private BrandGroupListAdapter adapter;

    public UpdateBrandListViewHolderNew(HealthCocoActivity mActivity, Object listenerObject) {
        this.mActivity = mActivity;
        this.selectedBrandListener = (SelectedBrandListener) listenerObject;
    }


    private void initAdapters() {
        adapter = new BrandGroupListAdapter(mActivity, selectedBrandListener);
        rvBrandList.setAdapter(adapter);
    }

    @Override
    public void setData(Object object) {
        vaccineBrandAssociationRequest = (VaccineBrandAssociationRequest) object;
    }

    @Override
    public void applyData() {
        if (vaccineBrandAssociationRequest != null) {
            if (!Util.isNullOrBlank(vaccineBrandAssociationRequest.getName()))
                tvVaccineName.setText(Util.getValidatedValue(vaccineBrandAssociationRequest.getName()));
            else tvVaccineName.setText("");
            adapter.setListData((ArrayList<VaccineBrandResponse>) vaccineBrandAssociationRequest.getVaccineBrandAssociationResponses());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View getContentView() {
        LinearLayout itemView = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_upadate_brand, null);
        tvVaccineName = (TextView) itemView.findViewById(R.id.tv_brand_name);
        rvBrandList = (ExpandableGridView) itemView.findViewById(R.id.rv_brands_list);
        rvBrandList.setExpanded(true);
        rvBrandList.setChoiceMode(ExpandableGridView.CHOICE_MODE_SINGLE);
        rvBrandList.setOnItemClickListener(this);
        initAdapters();
        return itemView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView cbSelectBrand = (CheckedTextView) view.findViewById(R.id.cb_select_brand);
        selectedBrandListener.isBrandSelectForGroup(cbSelectBrand.isChecked(), vaccineBrandAssociationRequest.getVaccineBrandAssociationResponses().get(position).getVaccineBrandId(), vaccineBrandAssociationRequest.getVaccineBrandAssociationResponses().get(position));
    }
}
