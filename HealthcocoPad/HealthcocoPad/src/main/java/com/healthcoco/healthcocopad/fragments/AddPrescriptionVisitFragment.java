package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.SelectedPrescriptionDrugItemsListMyScriptAdapter;
import com.healthcoco.healthcocopad.bean.DrugInteractions;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.DrugInteractionRequest;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.custom.MyScriptEditText;
import com.healthcoco.healthcocopad.listeners.AddNewPrescriptionListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectedPrescriptionDrugItemsListMyScriptViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by neha on 19/04/17.
 */

public class AddPrescriptionVisitFragment extends HealthCocoFragment implements AddNewPrescriptionListener, View.OnFocusChangeListener, View.OnClickListener {
    private LinkedHashMap<String, DrugItem> drugsListHashMap = new LinkedHashMap<>();
    private HashMap<String, DrugInteractionRequest> drugInteractionRequest = new HashMap<String, DrugInteractionRequest>();

    private SelectedPrescriptionDrugItemsListMyScriptAdapter adapter;
    private ListView lvPrescriptionItems;
    private EditText editDurationCommon;
    private MyScriptAddVisitsFragment myScriptAddVisitsFragment;
    private SelectedPrescriptionDrugItemsListMyScriptViewHolder viewHolder;
    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String text = dest.toString() + source.toString();
            modifyDurationUnit(text);
            return text;
        }
    };
    private boolean isDurationSet;
    private Button btDrugInteraction;

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
        btDrugInteraction = (Button) view.findViewById(R.id.bt_header_two_interaction);
    }

    @Override
    public void initListeners() {
        myScriptAddVisitsFragment = (MyScriptAddVisitsFragment) mFragmentManager.findFragmentByTag(MyScriptAddVisitsFragment.class.getSimpleName());
        if (myScriptAddVisitsFragment != null) {
            editDurationCommon.setOnTouchListener(myScriptAddVisitsFragment.getOnTouchListener());
        }
        btDrugInteraction.setOnClickListener(this);
//        editDurationCommon.setOnFocusChangeListener(this);
//        editDurationCommon.setFilters(new InputFilter[]{filter});
    }

    private void initAdapter() {
        adapter = new SelectedPrescriptionDrugItemsListMyScriptAdapter(mActivity, this);
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
            Intent intent = new Intent(MyScriptAddVisitsFragment.INTENT_ADIVCE_BUTTON_VISIBILITY);
            intent.putExtra(MyScriptAddVisitsFragment.TAG_VISIBILITY, showVisibility);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDrugsListparentVisibility(boolean isVisible) {

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
                    drugInteractionRequest.remove(drug.getDrug().getUniqueId());
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
    public MyScriptAddVisitsFragment getAddVisitFragment() {
        if (myScriptAddVisitsFragment == null)
            myScriptAddVisitsFragment = (MyScriptAddVisitsFragment) mFragmentManager.findFragmentByTag(MyScriptAddVisitsFragment.class.getSimpleName());
        return myScriptAddVisitsFragment;
    }

    public void addDrug(DrugItem drug) {
        formDrugInteractionsRequestList(drug);
        isDurationSet = false;
        if (drug != null) {
            editDurationCommon.setText("");
            if (drug.getDuration() == null || (drug.getDuration() != null && Util.isNullOrBlank(drug.getDuration().getValue())))
                drug.setDuration(LocalDataServiceImpl.getInstance(mApp).getDefaultDuration());
            drugsListHashMap.put(drug.getDrug().getUniqueId(), drug);
//            refreshListViewUpdatedDrugsList();
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
                myScriptAddVisitsFragment.initEditTextForWidget((MyScriptEditText) v);
                myScriptAddVisitsFragment.showOnlyWidget();
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
            LinkedHashMap<String, DrugItem> newHashMap = new LinkedHashMap<>();
            newHashMap.putAll(drugsListHashMap);
            List<DrugItem> modifiedList = new ArrayList<DrugItem>();
            modifiedList.addAll(newHashMap.values());
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
                if (child.getTag() != null && child.getTag() instanceof SelectedPrescriptionDrugItemsListMyScriptViewHolder) {
                    viewHolder = (SelectedPrescriptionDrugItemsListMyScriptViewHolder) child.getTag();
                    viewHolder.setDurationUnitToAll(unit);
                }
            }
        }
    }

    private void refreshListViewUpdatedDrugsList() {
        if (lvPrescriptionItems.getChildCount() > 0) {
            for (int i = 0; i < lvPrescriptionItems.getChildCount(); i++) {
                View child = lvPrescriptionItems.getChildAt(i);
                if (child.getTag() != null && child.getTag() instanceof SelectedPrescriptionDrugItemsListMyScriptViewHolder) {
                    SelectedPrescriptionDrugItemsListMyScriptViewHolder viewHolder = (SelectedPrescriptionDrugItemsListMyScriptViewHolder) child.getTag();
                    DrugItem modifiedDrug = viewHolder.getDrug();
                    LogUtils.LOGD(TAG, "Drug " + modifiedDrug.getDrug().getDrugName());
                    drugsListHashMap.put(modifiedDrug.getDrugId(), modifiedDrug);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_header_two_interaction) {
            confirmDrugInteractionsAlert();
        }
    }

    private void confirmDrugInteractionsAlert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(getResources().getString(
                R.string.confirm_drug_interaction));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDrugInteractionsResponse();
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
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case GET_DRUG_INTERACTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    showDrugInteractionsAlert((ArrayList<DrugInteractions>) (ArrayList<?>) response.getDataList());
                else
                    Util.showAlert(mActivity, R.string.title_no_interactions_found, R.string.msg_no_interactions_found);
                break;
            default:
                break;
        }
        mActivity.hideLoading();
    }

    private void showDrugInteractionsAlert(ArrayList<DrugInteractions> interactionsList) {
        String formattedString = "";
        for (DrugInteractions drugInteractions :
                interactionsList) {
            formattedString = formattedString + drugInteractions.getText() + "\n";
        }
        Util.showAlert(mActivity, formattedString);
    }

    private void getDrugInteractionsResponse() {
        mActivity.showLoading(true);
        WebDataServiceImpl.getInstance(mApp).getDrugsInteractionResponse(DrugInteractions.class, HealthCocoConstants.SELECTED_PATIENTS_USER_ID, getDrugInteractionRequestsList(), this, this);
    }

    public ArrayList<DrugInteractionRequest> getDrugInteractionRequestsList() {
        return new ArrayList<>(drugInteractionRequest.values());
    }

    private void formDrugInteractionsRequestList(DrugItem object) {
        try {
            if (object instanceof DrugItem) {
                DrugItem drugsListSolar = (DrugItem) object;
                if (drugsListSolar.getDrug() != null) {
                    Drug drug = drugsListSolar.getDrug();

                    DrugInteractionRequest drugInteractionRequest = new DrugInteractionRequest();
                    drugInteractionRequest.setDrugType(drug.getDrugType());
                    drugInteractionRequest.setDirection(drugsListSolar.getDirection());
                    drugInteractionRequest.setDuration(drugsListSolar.getDuration());
                    drugInteractionRequest.setGenericNames(drug.getGenericNames());
                    drugInteractionRequest.setDosage(drugsListSolar.getDosage());
                    drugInteractionRequest.setDrugName(drug.getDrugName());
                    drugInteractionRequest.setDrugCode(drug.getDrugCode());
                    drugInteractionRequest.setUniqueId(drug.getUniqueId());
                    this.drugInteractionRequest.put(drugInteractionRequest.getUniqueId(), drugInteractionRequest);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initUiPermissions(ArrayList<String> prescriptionUiPermissions) {
        if (!Util.isNullOrEmptyList(prescriptionUiPermissions)) {
            prescriptionUiPermissions.removeAll(Collections.singleton(null));
            if (!Util.isNullOrEmptyList(prescriptionUiPermissions)) {
            }
        }
    }
}
