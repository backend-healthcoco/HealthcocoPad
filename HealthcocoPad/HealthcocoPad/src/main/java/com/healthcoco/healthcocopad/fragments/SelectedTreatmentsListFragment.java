package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.SelectedTreatmentsItemsListAdapter;
import com.healthcoco.healthcocopad.bean.TotalTreatmentCostDiscountValues;
import com.healthcoco.healthcocopad.bean.request.TreatmentItemRequest;
import com.healthcoco.healthcocopad.bean.request.TreatmentServiceRequest;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.ExpandableHeightListView;
import com.healthcoco.healthcocopad.enums.UnitType;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentsListItemListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectedTreatmentsItemsListViewholder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.fragments.AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_DATA;

/**
 * Created by Shreshtha on 18-07-2017.
 */

public class SelectedTreatmentsListFragment extends HealthCocoFragment implements SelectedTreatmentsListItemListener {
    public static final String TAG_TREATMENT_ITEM_DETAIL = "treatmentItemDetail";
    public static final String TAG_DOCTOR_PROFILE = "doctorprofile";
    public static final String TAG_TOTAL_COST_DISCOUNT_DETAIL_VALUES = "totalCoustDiscountValues";
    AddNewTreatmentFragment addNewTreatmentFragment;
    private ExpandableHeightListView lvTreatmentsList;
    private TextView tvNoTreatmentAdded;
    private SelectedTreatmentsItemsListAdapter adapter;
    private LinkedHashMap<String, TreatmentItem> treatmentItemList;/* = new LinkedHashMap<String,TreatmentItem>();*/
    private LinkedHashMap<String, TotalTreatmentCostDiscountValues> totalCostHashMap = new LinkedHashMap<>();
    private TreatmentItem treatmentItem;
    private Treatments treatment;
    private DoctorProfile doctorProfile;
    private SelectedTreatmentsItemsListViewholder viewHolder;

    public SelectedTreatmentsListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_selected_treatment_items_list, container, false);
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
        initAdapter();
        initIntentData();
    }

    private void initIntentData() {
//        Intent intent = mActivity.getIntent();
//        treatment = Parcels.unwrap(intent.getParcelableExtra(PatientTreatmentDetailFragment.TAG_TREATMENT_DATA));
//        doctorProfile = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_DOCTOR_PROFILE));
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(TAG_TREATMENT_ITEM_DETAIL)) {
            treatment = Parcels.unwrap(bundle.getParcelable(TAG_TREATMENT_ITEM_DETAIL));
        }
        if (bundle != null && bundle.containsKey(TAG_DOCTOR_PROFILE)) {
            doctorProfile = Parcels.unwrap(bundle.getParcelable(TAG_DOCTOR_PROFILE));
        }
        if (treatment != null) {
            List<TreatmentItem> treatments = treatment.getTreatments();
            for (TreatmentItem treatmentItem :
                    treatments) {
                addTreatment(treatmentItem);
                Util.sendBroadcast(mApp, AddNewTreatmentFragment.INTENT_GET_MODIFIED_VALUE);
            }
        }
    }

    @Override
    public void initViews() {
        treatmentItemList = new LinkedHashMap<String, TreatmentItem>();

        lvTreatmentsList = (ExpandableHeightListView) view.findViewById(R.id.lv_treatment_list);
        tvNoTreatmentAdded = (TextView) view.findViewById(R.id.tv_no_treatment_added);
        lvTreatmentsList.setExpanded(true);

    }

    @Override
    public void initListeners() {

    }

    private void initAdapter() {
        adapter = new SelectedTreatmentsItemsListAdapter(mActivity, this);
        lvTreatmentsList.setAdapter(adapter);
        lvTreatmentsList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }

    @Override
    public void onDeleteItemClicked(TreatmentItem treatmentItem) {
        showConfirmationAlert(treatmentItem);
    }

    private void showConfirmationAlert(final TreatmentItem treatmentItem) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(R.string.confirm_remove_invoice_item);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    treatmentItemList.remove(treatmentItem.getTreatmentServiceId());
                    totalCostHashMap.remove(treatmentItem.getTreatmentServiceId());
                    notifyAdapter(treatmentItemList);
                    Util.sendBroadcast(mApp, AddNewTreatmentFragment.INTENT_GET_MODIFIED_VALUE);
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
    public void onTreatmentItemClicked(TreatmentItem treatmentItem) {
        this.treatmentItem = treatmentItem;
//        openCommonOpenUpActivity(CommonOpenUpFragmentType.ADD_TREATMENT_ITEM_DETAIL, TAG_TREATMENT_ITEM_DETAIL, this.treatmentItem, HealthCocoConstants.REQUEST_CODE_TREATMENT_ITEM_DETAIL);
    }

    @Override
    public void onTotalValueTypeDetailChanged(String treatmentId, TotalTreatmentCostDiscountValues totalTreatmentCostDiscountValues) {
        totalCostHashMap.put(treatmentId, totalTreatmentCostDiscountValues);

        TotalTreatmentCostDiscountValues finalCostDiscontDetails = new TotalTreatmentCostDiscountValues();
        for (TotalTreatmentCostDiscountValues totalValues :
                totalCostHashMap.values()) {
            finalCostDiscontDetails.setTotalCost(finalCostDiscontDetails.getTotalCost() + totalValues.getTotalCost());
            finalCostDiscontDetails.setTotalGrandTotal(finalCostDiscontDetails.getTotalGrandTotal() + totalValues.getTotalGrandTotal());
            finalCostDiscontDetails.setTotalDiscount(finalCostDiscontDetails.getTotalDiscount() + totalValues.getTotalDiscount());
        }
        LogUtils.LOGD(TAG, "Total value for totalCost " + finalCostDiscontDetails.getTotalCost() + " grandTotal : " + finalCostDiscontDetails.getTotalGrandTotal());
        Util.sendBroadcastWithParcelData(mApp, AddNewTreatmentFragment.INTENT_GET_MODIFIED_VALUE, new String[]{TAG_TOTAL_COST_DISCOUNT_DETAIL_VALUES}, new Object[]{finalCostDiscontDetails});

    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public DoctorProfile getDoctorProfile() {
        return doctorProfile;
    }

    @Override
    public void onTotalCostChange() {
//        addNewTreatmentFragment.setModifiedValues();
//        Util.sendBroadcast(mApp, AddNewTreatmentFragment.INTENT_GET_MODIFIED_VALUE);
    }

    private void notifyAdapter(HashMap<String, TreatmentItem> drugsListMap) {
        ArrayList<TreatmentItem> list = new ArrayList<>(drugsListMap.values());
        if (!Util.isNullOrEmptyList(list)) {
            lvTreatmentsList.setVisibility(View.VISIBLE);
            tvNoTreatmentAdded.setVisibility(View.GONE);
        } else {
            lvTreatmentsList.setVisibility(View.GONE);
            tvNoTreatmentAdded.setVisibility(View.VISIBLE);
        }
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
//        Util.sendBroadcast(mApp, AddNewTreatmentFragment.INTENT_GET_MODIFIED_VALUE);
    }

    public void addTreatment(TreatmentItem treatmentItem) {
        if (treatmentItem != null) {
            String key = treatmentItem.getTreatmentServiceId();
            if (treatmentItemList.containsKey(key))
                treatmentItemList.remove(key);
            treatmentItemList.put(key, treatmentItem);
            notifyAdapter(treatmentItemList);
            lvTreatmentsList.setSelection(adapter.getCount());
        }
    }

 /*   public void modifyTreatmentsList() {
        if (lvTreatmentsList.getChildCount() > 0) {

            for (int i = 0; i < lvTreatmentsList.getChildCount(); i++) {
                View child = lvTreatmentsList.getChildAt(i);
                if (child.getTag() != null && child.getTag() instanceof SelectedTreatmentsItemsListViewholder) {
                    viewHolder = (SelectedTreatmentsItemsListViewholder) child.getTag();
                    TreatmentItem treatmentItem = viewHolder.getTreatmentItem();
                    if (treatmentItem != null) {
                        String key = treatmentItem.getCustomUniqueId();
                        if (treatmentItemList.containsKey(key))
                            treatmentItemList.remove(key);
                        treatmentItemList.put(key, treatmentItem);
                    }
                }
            }
        }
    }*/


    //setting drug to null and drugId to uniqueId for sending on server
    public List<TreatmentItem> getModifiedTreatmentsItemList() {
        if (!Util.isNullOrEmptyList(treatmentItemList)) {
            List<TreatmentItem> itemArrayList = new ArrayList<TreatmentItem>(treatmentItemList.values());
            return itemArrayList;
        }
        return null;
    }

    //setting drug to null and drugId to uniqueId for sending on server
    public List<TreatmentItemRequest> getModifiedTreatmentsItemRequestList() {
        if (!Util.isNullOrEmptyList(treatmentItemList)) {
            List<TreatmentItemRequest> treatmentItemRequestList = new ArrayList<TreatmentItemRequest>();
            List<TreatmentItem> itemArrayList = new ArrayList<TreatmentItem>(treatmentItemList.values());
            for (TreatmentItem treatmentItem :
                    itemArrayList) {
                TreatmentItemRequest treatmentItemRequest = new TreatmentItemRequest();
                treatmentItemRequest.setTreatmentServiceId(treatmentItem.getTreatmentServiceId());
                treatmentItemRequest.setTreatmentServices(getTreatmentServiceList(treatmentItem.getTreatmentServices()));
                treatmentItemRequest.setTreatmentService(getTreatmentService(treatmentItem.getTreatmentService()));
                treatmentItemRequest.setDiscount(treatmentItem.getDiscount());
                treatmentItemRequest.setQuantity(treatmentItem.getQuantity());
                treatmentItemRequest.setStatus(treatmentItem.getStatus());
                treatmentItemRequest.setCost(treatmentItem.getCost());
                treatmentItemRequest.setNote(treatmentItem.getNote());
                treatmentItemRequest.setFinalCost(treatmentItem.getFinalCost());
                treatmentItemRequest.setTreatmentFields(treatmentItem.getTreatmentFields());
                treatmentItemRequestList.add(treatmentItemRequest);
            }
            return treatmentItemRequestList;
        }
        return null;
    }

    private List<TreatmentServiceRequest> getTreatmentServiceList(List<TreatmentService> treatmentService) {
        if (!Util.isNullOrEmptyList(treatmentService)) {
            List<TreatmentServiceRequest> treatmentServiceRequests = new ArrayList<>();
            for (TreatmentService service :
                    treatmentService) {
                TreatmentServiceRequest serviceRequest = getTreatmentService(service);
                treatmentServiceRequests.add(serviceRequest);
            }
            return treatmentServiceRequests;
        }
        return null;
    }

    private TreatmentServiceRequest getTreatmentService(TreatmentService treatmentService) {
        TreatmentServiceRequest treatmentServiceRequest = new TreatmentServiceRequest();
        treatmentServiceRequest.setUniqueId(treatmentService.getUniqueId());
        treatmentServiceRequest.setDoctorId(treatmentService.getDoctorId());
        treatmentServiceRequest.setHospitalId(treatmentService.getHospitalId());
        treatmentServiceRequest.setLocationId(treatmentService.getLocationId());
        treatmentServiceRequest.setTreatmentCode(treatmentService.getTreatmentCode());
        treatmentServiceRequest.setFieldsRequired(treatmentService.getFieldsRequired());
        treatmentServiceRequest.setCategory(treatmentService.getCategory());
        treatmentServiceRequest.setCreatedBy(treatmentService.getCreatedBy());
        treatmentServiceRequest.setCost(treatmentService.getCost());
        treatmentServiceRequest.setName(treatmentService.getName());
        treatmentServiceRequest.setRankingCount(treatmentService.getRankingCount());
        treatmentServiceRequest.setDiscarded(treatmentService.getDiscarded());
        treatmentServiceRequest.setSpeciality(treatmentService.getSpeciality());
        return treatmentServiceRequest;
    }

  /*  public double getTotalCost() {
        double totalPrice = 0;
        if (getModifiedTreatmentsItemList() != null) {
            for (int i = 0; i < getModifiedTreatmentsItemList().size(); i++) {
                if (getModifiedTreatmentsItemList().get(i).getCost() > 0)
                    if (getModifiedTreatmentsItemList().get(i).getQuantity() != null && getModifiedTreatmentsItemList().get(i).getQuantity().getValue() > 0)
                        totalPrice += (getModifiedTreatmentsItemList().get(i).getCost()) * (getModifiedTreatmentsItemList().get(i).getQuantity().getValue());
                    else
                        totalPrice += (getModifiedTreatmentsItemList().get(i).getCost()) * (1);
            }
        }
        return totalPrice;
    }

    public double getGrandTotalCost() {
        double totalPrice = 0;
        if (getModifiedTreatmentsItemList() != null) {
            for (int i = 0; i < getModifiedTreatmentsItemList().size(); i++) {
                if (getModifiedTreatmentsItemList().get(i).getFinalCost() > 0)
                    totalPrice += getModifiedTreatmentsItemList().get(i).getFinalCost();
            }
        }
        return totalPrice;
    }

    public double getTotalDiscount() {
        double totalPrice = 0;
        if (getModifiedTreatmentsItemList() != null) {
            for (int i = 0; i < getModifiedTreatmentsItemList().size(); i++) {
                if (getModifiedTreatmentsItemList().get(i).getCost() > 0)
                    totalPrice += getCalculatedDiscount(i);
            }
        }
        return totalPrice;
    }*/

   /* private double getCalculatedDiscount(int i) {
        double calculatedDiscount = 0;
        //final cost=((qty*cost)-((qty*cost)*discount))
        if (getModifiedTreatmentsItemList().get(i).getDiscount() != null) {
            UnitType discountUnit = getModifiedTreatmentsItemList().get(i).getDiscount().getUnit();
            if (discountUnit != null) {
                switch (discountUnit) {
                    case INR:
                        calculatedDiscount = getModifiedTreatmentsItemList().get(i).getDiscount().getValue();
                        break;
                    case PERCENT:
                        calculatedDiscount = ((getModifiedTreatmentsItemList().get(i).getQuantity().getValue()
                                * getModifiedTreatmentsItemList().get(i).getCost()) * (getModifiedTreatmentsItemList().get(i).getDiscount().getValue() / 100.0f));
                        break;
                }
            }
        }
        return calculatedDiscount;
    }*/

    public View getLastChildView() {
        View lastview = null;
        try {
            lastview = lvTreatmentsList.getChildAt(adapter.getCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lastview == null)
            lastview = view;
        return lastview;
    }
}
