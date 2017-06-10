package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.SelectedPrescriptionDrugItemsListAdapter;
import com.healthcoco.healthcocopad.bean.request.DrugInteractionRequest;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.Duration;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.custom.ExpandableHeightListView;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.listeners.AddNewPrescriptionListener;
import com.healthcoco.healthcocopad.listeners.SelectedDrugsListItemListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LocalDatabaseUtils;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectedPrescriptionDrugDoseItemsListViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.fragments.AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_DATA;

/**
 * Created by neha on 27/11/15.
 */
public class SelectedDrugItemsListFragment extends HealthCocoFragment implements
        SelectedDrugsListItemListener, AddNewPrescriptionListener {
    private static final int REQUEST_CODE_SELECTED_DRUGS_LIST = 101;
    private AddNewPrescriptionListener prescriptionListener;
    private ExpandableHeightListView lvDrugsList;
    private HashMap<String, DrugItem> drugsList = new HashMap<String, DrugItem>();
    private HashMap<String, DrugInteractionRequest> drugInteractionRequest = new HashMap<String, DrugInteractionRequest>();
    private SelectedPrescriptionDrugItemsListAdapter adapter;
    private SelectedPrescriptionDrugDoseItemsListViewHolder viewHolder;
    private List<Prescription> prescriptionList;

    public SelectedDrugItemsListFragment() {

    }

    public SelectedDrugItemsListFragment(AddNewPrescriptionListener prescriptionListener) {
        this.prescriptionListener = prescriptionListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_selected_drug_items_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(TAG_PRESCRIPTION_DATA)) {
            prescriptionList = Parcels.unwrap(bundle.getParcelable(TAG_PRESCRIPTION_DATA));
        }
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
        initData();
    }

    private void initData() {
        Intent intent = mActivity.getIntent();
        String prescriptionId = intent.getStringExtra(HealthCocoConstants.TAG_PRESCRIPTION_ID);
        String templateId = intent.getStringExtra(HealthCocoConstants.TAG_TEMPLATE_ID);
        if (!Util.isNullOrBlank(prescriptionId)) {
            addDrugsList(LocalDataServiceImpl.getInstance(mApp).getDrugItemsList(LocalDatabaseUtils.KEY_FOREIGN_PRESCRIPTION_ID, prescriptionId));
        } else if (!Util.isNullOrBlank(templateId))
            addDrugsList(LocalDataServiceImpl.getInstance(mApp).getDrugItemsList(LocalDatabaseUtils.KEY_FOREIGN_TEMPLATE_ID, templateId));
        else {
            addDrugsList(null);
        }
        if (!Util.isNullOrEmptyList(prescriptionList)) {
            for (Prescription prescription :
                    prescriptionList) {
                if (!Util.isNullOrEmptyList(prescription.getItems())) {
                    for (DrugItem drugItem :
                            prescription.getItems()) {
                        if (drugItem.getDrug() != null && !Util.isNullOrBlank(drugItem.getDrug().getUniqueId()))
                            addDrug(drugItem);
                    }
                }
            }
        }
    }

    @Override
    public void initViews() {
        lvDrugsList = (ExpandableHeightListView) view.findViewById(R.id.lv_drugs_list);
        lvDrugsList.setExpanded(true);
    }

    @Override
    public void initListeners() {

    }

    private void initAdapter() {
        adapter = new SelectedPrescriptionDrugItemsListAdapter(mActivity, this);
        lvDrugsList.setAdapter(adapter);
    }

    private void notifyAdapter(HashMap<String, DrugItem> drugsListMap) {
        ArrayList<DrugItem> list = new ArrayList<>(drugsListMap.values());
        if (!Util.isNullOrEmptyList(list)) {
            if (prescriptionListener != null)
                prescriptionListener.setDrugsListparentVisibility(true);
            lvDrugsList.setVisibility(View.VISIBLE);
        } else {
            if (prescriptionListener != null)
                prescriptionListener.setDrugsListparentVisibility(false);
            lvDrugsList.setVisibility(View.GONE);
        }
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setDrugsListparentVisibility(boolean isVisible) {

    }

    @Override
    public void onDeleteItemClicked(DrugItem drug) {
        showConfirmationAlert(drug);
    }

    private void showConfirmationAlert(final DrugItem drug) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(R.string.confirm_remove_drug);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    drugInteractionRequest.remove(drug.getDrugId());
                    drugsList.remove(drug.getDrug().getUniqueId());
                    notifyAdapter(drugsList);
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
    public void onDrugItemClicked(DrugItem drugItem) {
    }

    @Override
    public String getDurationUnit() {
        return null;
    }

    @Override
    public boolean isDurationSet() {
        return false;
    }

    @Override
    public MyScriptAddVisitsFragment getAddVisitFragment() {
        return null;
    }

    public ArrayList<DrugItem> getDrugsList() {
        return new ArrayList<>(drugsList.values());
    }

    public ArrayList<DrugInteractionRequest> getDrugInteractionRequestsList() {
        return new ArrayList<>(drugInteractionRequest.values());
    }

    //setting drug to null and drugId to uniqueId for sending on server
    public List<DrugItem> getModifiedDrugsList() {
        refreshListViewUpdatedDrugsList();
        if (!Util.isNullOrEmptyList(drugsList)) {
            LinkedHashMap<String, DrugItem> newHashMap = new LinkedHashMap<>();
            newHashMap.putAll(drugsList);
            List<DrugItem> modifiedList = new ArrayList<DrugItem>();
            modifiedList.addAll(newHashMap.values());
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

    private void refreshListViewUpdatedDrugsList() {
        if (lvDrugsList.getChildCount() > 0) {
            for (int i = 0; i < lvDrugsList.getChildCount(); i++) {
                View child = lvDrugsList.getChildAt(i);
                if (child.getTag() != null && child.getTag() instanceof SelectedPrescriptionDrugDoseItemsListViewHolder) {
                    SelectedPrescriptionDrugDoseItemsListViewHolder viewHolder = (SelectedPrescriptionDrugDoseItemsListViewHolder) child.getTag();
                    DrugItem modifiedDrug = viewHolder.getDrug();
                    drugsList.put(modifiedDrug.getDrugId(), modifiedDrug);
                }
            }
        }
    }

    public void addDrug(DrugItem drug) {
        formDrugInteractionsRequestList(drug);
        if (drug != null) {
            if (drug.getDuration() == null || (drug.getDuration() != null && Util.isNullOrBlank(drug.getDuration().getValue())))
                drug.setDuration(LocalDataServiceImpl.getInstance(mApp).getDefaultDuration());
            drugsList.put(drug.getDrug().getUniqueId(), drug);
            notifyAdapter(drugsList);
//            lvDrugsList.setSelection(adapter.getCount());
        }
    }

    public void addDrugsList(List<DrugItem> drugItems) {
        if (!Util.isNullOrEmptyList(drugItems)) {
            for (DrugItem drug : drugItems) {
                addDrug(drug);
            }
        }
        notifyAdapter(drugsList);
    }

    /**
     * Any change in this method,change should also be done in AddNewPrescription,AddDrugDetailFragment's initSelectedDrug() method
     */
    public void addSelectedDrug(SelectDrugItemType selectDrugItemType, Object object) {
        DrugItem selectedDrug = new DrugItem();
        String drugId = "";
        String drugName = "";
        String drugType = "";
        String drugTypeId = "";
        String directionId = "";
        String durationUnitId = "";
        String durationtext = "";
        String dosage = "";
        String instructions = "";
        List<GenericName> genericNames = null;
        DrugsListSolrResponse drugsSolr = (DrugsListSolrResponse) object;
        drugId = drugsSolr.getUniqueId();
        drugName = drugsSolr.getDrugName();
        drugType = drugsSolr.getDrugType();
        drugTypeId = drugsSolr.getDrugTypeId();
        if (!Util.isNullOrEmptyList(drugsSolr.getDirection()))
            directionId = drugsSolr.getDirection().get(0).getUniqueId();
        if (drugsSolr.getDuration() != null && drugsSolr.getDuration().getDurationUnit() != null) {
            durationtext = drugsSolr.getDuration().getValue();
            durationUnitId = drugsSolr.getDuration().getDurationUnit().getUniqueId();
        }
        dosage = drugsSolr.getDosage();
        genericNames = drugsSolr.getGenericNames();

        Drug drug = new Drug();
        drug.setDrugName(drugName);
        drug.setUniqueId(drugId);
        drug.setGenericNames(genericNames);

        DrugType drugTypeObj = new DrugType();
        drugTypeObj.setUniqueId(drugTypeId);
        drugTypeObj.setType(drugType);
        drug.setDrugType(drugTypeObj);

        selectedDrug.setDrug(drug);
        selectedDrug.setDrugId(drugId);
        selectedDrug.setDosage(dosage);
        selectedDrug.setDirection(getDirectionsListFromLocal(directionId));
        selectedDrug.setDuration(getDurationAndUnit(durationtext, durationUnitId));
        selectedDrug.setInstructions(instructions);
        if (selectedDrug != null) {
            addDrug(selectedDrug);
        }
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

    /**
     * Any change in this method,change should also be done in AddNewPrescription,AddDrugDetailFragment's getDurationAndUnit() method
     */
    private Duration getDurationAndUnit(String durationText, String durationUnitId) {
        Duration duration = new Duration();
        duration.setValue(durationText);
        duration.setDurationUnit(LocalDataServiceImpl.getInstance(mApp).getDurationUnit(durationUnitId));
        return duration;
    }

    /**
     * Any change in this method,change should also be done in AddNewPrescription,AddDrugDetailFragment's getDirectionsListFromLocal() method
     */
    private List<DrugDirection> getDirectionsListFromLocal(String directionId) {
        List<DrugDirection> list = new ArrayList<>();
        DrugDirection direction = LocalDataServiceImpl.getInstance(mApp).getDrugDirection(directionId);
        if (direction != null)
            list.add(direction);
        return list;
    }

    public void notifyList() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void modifyDurationUnit(String unit) {
        if (lvDrugsList.getChildCount() > 0) {
            for (int i = 0; i < lvDrugsList.getChildCount(); i++) {
                View child = lvDrugsList.getChildAt(i);
                if (child.getTag() != null && child.getTag() instanceof SelectedPrescriptionDrugDoseItemsListViewHolder) {
                    viewHolder = (SelectedPrescriptionDrugDoseItemsListViewHolder) child.getTag();
                    viewHolder.setDurationUnitToAll(unit);
                }
            }
        }
    }
}
