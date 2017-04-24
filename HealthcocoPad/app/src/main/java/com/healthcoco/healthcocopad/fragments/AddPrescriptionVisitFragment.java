package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.SelectedPrescriptionDrugItemsListAdapter;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.custom.MyScriptEditText;
import com.healthcoco.healthcocopad.listeners.AddNewPrescriptionListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectedPrescriptionDrugDoseItemsListViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by neha on 19/04/17.
 */

public class AddPrescriptionVisitFragment extends HealthCocoFragment implements AddNewPrescriptionListener, View.OnFocusChangeListener {
    private LinkedHashMap<String, DrugItem> drugsListHashMap = new LinkedHashMap<>();
    private SelectedPrescriptionDrugItemsListAdapter adapter;
    private ListView lvPrescriptionItems;
    private EditText editDurationCommon;
    private AddVisitsFragment addVisitsFragment;
    private SelectedPrescriptionDrugDoseItemsListViewHolder viewHolder;
    private boolean isDurationSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_prescription_visit, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
    }

    @Override
    public void initViews() {
        lvPrescriptionItems = (ListView) view.findViewById(R.id.lv_prescription_items);
        editDurationCommon = (MyScriptEditText) view.findViewById(R.id.edit_duration_common);
    }

    @Override
    public void initListeners() {
        addVisitsFragment = (AddVisitsFragment) mFragmentManager.findFragmentByTag(AddVisitsFragment.class.getSimpleName());
        if (addVisitsFragment != null) {
            editDurationCommon.setOnTouchListener(addVisitsFragment.getOnTouchListener());
        }
//        editDurationCommon.setOnFocusChangeListener(this);
//        editDurationCommon.setFilters(new InputFilter[]{filter});
    }

    private void initAdapter() {
        adapter = new SelectedPrescriptionDrugItemsListAdapter(mActivity, this);
        lvPrescriptionItems.setAdapter(adapter);
        lvPrescriptionItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvPrescriptionItems.setStackFromBottom(true);
    }

    private void notifyAdapter(ArrayList<DrugItem> drugsListMap) {
        if (drugsListMap != null && drugsListMap.size() > 0) {
            sendBroadcastForAdviceButtonVisibility(true);
        } else sendBroadcastForAdviceButtonVisibility(false);

        adapter.setListData(drugsListMap);
        adapter.notifyDataSetChanged();
    }

    private void sendBroadcastForAdviceButtonVisibility(boolean showVisibility) {
        try {
            Intent intent = new Intent(AddVisitsFragment.INTENT_ADIVCE_BUTTON_VISIBILITY);
            intent.putExtra(AddVisitsFragment.TAG_VISIBILITY, showVisibility);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteItemClicked(DrugItem drug) {
        showConfirmationAlertForDrugs(drug);
    }

    private void showConfirmationAlertForDrugs(final DrugItem drug) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(R.string.confirm_remove_drug);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    drugsListHashMap.remove(drug.getDrug().getUniqueId());
                    notifyAdapter(new ArrayList<DrugItem>(drugsListHashMap.values()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }


    @Override
    public void onDrugItemClicked(DrugItem drug) {

    }

    @Override
    public String getDurationUnit() {
        return Util.getValidatedValueOrBlankTrimming(editDurationCommon);
    }

    @Override
    public boolean isDurationSet() {
        return isDurationSet;
    }

    @Override
    public AddVisitsFragment getAddVisitFragment() {
        if (addVisitsFragment == null)
            addVisitsFragment = (AddVisitsFragment) mFragmentManager.findFragmentByTag(AddVisitsFragment.class.getSimpleName());
        return addVisitsFragment;
    }

    public void addDrug(DrugItem drug) {
        isDurationSet = false;
        if (drug != null) {
            editDurationCommon.setText("");
            drugsListHashMap.put(drug.getDrug().getUniqueId(), drug);
            refreshListViewUpdatedDrugsList();
            notifyAdapter(new ArrayList<DrugItem>(drugsListHashMap.values()));
            lvPrescriptionItems.setSelection(adapter.getCount());
        }
    }

    public View getLastChildView() {
        View lastview = null;
        try {
            lastview = lvPrescriptionItems.getChildAt(adapter.getCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lastview == null)
            lastview = view;
        return lastview;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v instanceof EditText) {
                addVisitsFragment.initEditTextForWidget((MyScriptEditText) v);
                addVisitsFragment.showOnlyWidget();
            }
        }
    }

    public boolean isBlankDrugsList() {
        return Util.isNullOrEmptyList(drugsListHashMap);
    }

    //setting drug to null and drugId to uniqueId for sending on server
    public List<DrugItem> getModifiedDrugsList() {
        refreshListViewUpdatedDrugsList();
        if (!Util.isNullOrEmptyList(drugsListHashMap)) {
            List<DrugItem> modifiedList = new ArrayList<DrugItem>(drugsListHashMap.values());
//        modifiedList.addAll(drugsList);
            for (DrugItem drugItem :
                    modifiedList) {
                if (drugItem.getDrug() != null) {
                    drugItem.setDrugId(drugItem.getDrug().getUniqueId());
                    drugItem.setDrug(null);
                }
            }
            return modifiedList;
        }
        return null;
    }

    public void modifyDurationUnit(String unit) {
//        if (Util.isNullOrBlank(unit))
//            isDurationSet = false;
//        else
//        if (editDurationCommon.hasFocus()) {
//            isDurationSet = true;
//            adapter.notifyDataSetChanged();
//        }
        if (lvPrescriptionItems.getChildCount() > 0) {
            for (int i = 0; i < lvPrescriptionItems.getChildCount(); i++) {
                View child = lvPrescriptionItems.getChildAt(i);
                if (child.getTag() != null && child.getTag() instanceof SelectedPrescriptionDrugDoseItemsListViewHolder) {
                    viewHolder = (SelectedPrescriptionDrugDoseItemsListViewHolder) child.getTag();
                    viewHolder.setDurationUnitToAll(unit);
                }
            }
        }
    }

    private void refreshListViewUpdatedDrugsList() {
        if (lvPrescriptionItems.getChildCount() > 0) {
            for (int i = 0; i < lvPrescriptionItems.getChildCount(); i++) {
                View child = lvPrescriptionItems.getChildAt(i);
                if (child.getTag() != null && child.getTag() instanceof SelectedPrescriptionDrugDoseItemsListViewHolder) {
                    SelectedPrescriptionDrugDoseItemsListViewHolder viewHolder = (SelectedPrescriptionDrugDoseItemsListViewHolder) child.getTag();
                    DrugItem modifiedDrug = viewHolder.getDrug();
                    LogUtils.LOGD(TAG, "Drug " + modifiedDrug.getDrug().getDrugName());
                    drugsListHashMap.put(modifiedDrug.getDrugId(), modifiedDrug);
                }
            }
        }
    }

    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String text = dest.toString() + source.toString();
            modifyDurationUnit(text);
            return text;
        }
    };
}
