package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandAssociationRequest;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.listeners.SelectedBrandListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

public class UpdateBrandListViewHolder extends HealthcocoComonRecylcerViewHolder implements AdapterView.OnItemClickListener {

    private SelectedBrandListener selectedBrandListener;
    private TextView tvVaccineName;
    private VaccineBrandAssociationRequest vaccineBrandAssociationRequest;
    private RecyclerView rvBrandList;
    private HealthcocoRecyclerViewAdapter adapter;

    public UpdateBrandListViewHolder(HealthCocoActivity activity, View itemView, HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
        super(activity, itemView, onItemClickListener, listenerObject);
        this.selectedBrandListener = (SelectedBrandListener) listenerObject;
        initAdapters();
    }

    @Override
    public void initViews(View itemView) {
        tvVaccineName = (TextView) itemView.findViewById(R.id.tv_brand_name);
        rvBrandList = (RecyclerView) itemView.findViewById(R.id.rv_brands_list);
//        rvBrandList.setExpanded(true);
//        rvBrandList.setChoiceMode(ExpandableGridView.CHOICE_MODE_SINGLE);
//        rvBrandList.setOnItemClickListener(this);
//        int spacingInPixelsVerticalBlogs = mActivity.getResources().getDimensionPixelSize(R.dimen.item_spacing_vaccination_list_item);
//        GridLayoutManager gridLayoutManagerVertical = new GridLayoutManager(mActivity, 2, //The number of Columns in the grid
//                LinearLayoutManager.VERTICAL, false);
//        gridLayoutManagerVertical.setSpanSizeLookup(new HomeHealthRecyclerViewSpanSizeLookup(3, 1, 2));
//        rvBrandList.setLayoutManager(gridLayoutManagerVertical);
//        rvBrandList.addItemDecoration(new VerticalRecyclerViewItemDecoration(spacingInPixelsVerticalBlogs));
//        initAdapters();
    }

    private void initAdapters() {
      /*  adapter = new BrandListAdapter(mActivity, selectedBrandListener);
        rvBrandList.setAdapter(adapter);
*/
        GridLayoutManager gridLayoutManagerVertical = new GridLayoutManager(mActivity,
                2, //The number of Columns in the grid
                LinearLayoutManager.VERTICAL,
                false);

        rvBrandList.setLayoutManager(gridLayoutManagerVertical);

        adapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.BRAND_LIST, selectedBrandListener);
        rvBrandList.setAdapter(adapter);
//        notifyAdapter(new ArrayList<>(brandAssociationRequestLinkedHashMap.values()));

    }

    @Override
    public void applyData(Object object) {
        vaccineBrandAssociationRequest = (VaccineBrandAssociationRequest) object;
        if (vaccineBrandAssociationRequest != null) {
            if (!Util.isNullOrBlank(vaccineBrandAssociationRequest.getName()))
                tvVaccineName.setText(Util.getValidatedValue(vaccineBrandAssociationRequest.getName()));
            else tvVaccineName.setText("");
            adapter.setListData(new ArrayList<Object>(vaccineBrandAssociationRequest.getVaccineBrandAssociationResponses()));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView cbSelectBrand = (CheckedTextView) view.findViewById(R.id.cb_select_brand);
        selectedBrandListener.isBrandSelect(!cbSelectBrand.isChecked(), vaccineBrandAssociationRequest.getVaccineBrandAssociationResponses().get(position).getVaccineBrandId(), vaccineBrandAssociationRequest.getVaccineBrandAssociationResponses().get(position));
    }
}
