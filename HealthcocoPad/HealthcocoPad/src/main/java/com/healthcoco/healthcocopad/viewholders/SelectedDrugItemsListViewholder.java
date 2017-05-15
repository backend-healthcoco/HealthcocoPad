package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.listeners.SelectedDrugsListItemListener;


public class SelectedDrugItemsListViewholder extends HealthCocoViewHolder implements OnClickListener {

    private final HealthCocoApplication mApp;
    private HealthCocoActivity mActivity;
    //    private DrugItem objData;
    private DrugItem objData;
    private ImageButton btDelete;
    private SelectedDrugsListItemListener templateListener;
    private LinearLayout containerDrugDose;

    public SelectedDrugItemsListViewholder(HealthCocoActivity mActivity, SelectedDrugsListItemListener templateListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.templateListener = templateListener;
        this.mApp = ((HealthCocoApplication) mActivity.getApplication());
    }

    @Override
    public void setData(Object object) {
        this.objData = (DrugItem) object;
    }

    @Override
    public void applyData() {
        containerDrugDose.removeAllViews();
        DrugDoseItemViewHolder view = new DrugDoseItemViewHolder(mActivity);
        view.setData(objData);
        containerDrugDose.addView(view);
    }

    @Override
    public View getContentView() {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.list_item_selected_drug, null);
        btDelete = (ImageButton) view.findViewById(R.id.bt_delete);
        containerDrugDose = (LinearLayout) view.findViewById(R.id.container_drug_dose);
        btDelete.setOnClickListener(this);
        containerDrugDose.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete:
                templateListener.onDeleteItemClicked(objData);
                break;
            case R.id.container_drug_dose:
                templateListener.onDrugItemClicked(objData);
                break;
        }
    }
}
