package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.SelectedInvoiceItemsListAdapter;
import com.healthcoco.healthcocopad.adapter.SelectedTreatmentsItemsListAdapter;
import com.healthcoco.healthcocopad.bean.TotalTreatmentCostDiscountValues;
import com.healthcoco.healthcocopad.bean.request.InvoiceItemRequest;
import com.healthcoco.healthcocopad.bean.request.TreatmentItemRequest;
import com.healthcoco.healthcocopad.bean.request.TreatmentServiceRequest;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.InvoiceItem;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.ExpandableHeightListView;
import com.healthcoco.healthcocopad.listeners.SelectedInvoiceListItemListener;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentsListItemListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectedTreatmentsItemsListViewholder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by prashant on 18-12-2017.
 */

public class SelectedInvoiceListFragment extends HealthCocoFragment implements SelectedInvoiceListItemListener {
    public static final String TAG_INVOICE_ITEM_DETAIL = "invoiceItemDetail";
    public static final String TAG_DOCTOR_PROFILE = "doctorprofile";
    public static final String TAG_TOTAL_COST_DISCOUNT_DETAIL_VALUES = "totalCostDiscountValues";
    private ExpandableHeightListView lvInvoiceList;
    private TextView tvNoInvoiceAdded;
    private SelectedInvoiceItemsListAdapter adapter;
    private LinkedHashMap<String, InvoiceItem> invoiceItemList;/* = new LinkedHashMap<String,TreatmentItem>();*/
    private LinkedHashMap<String, TotalTreatmentCostDiscountValues> totalCostHashMap = new LinkedHashMap<>();
    private InvoiceItem invoiceItem;
    private Invoice invoice;
    private DoctorProfile doctorProfile;
    private SelectedTreatmentsItemsListViewholder viewHolder;

    public SelectedInvoiceListFragment() {
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
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(TAG_INVOICE_ITEM_DETAIL)) {
            invoice = Parcels.unwrap(bundle.getParcelable(TAG_INVOICE_ITEM_DETAIL));
        }
        if (bundle != null && bundle.containsKey(TAG_DOCTOR_PROFILE)) {
            doctorProfile = Parcels.unwrap(bundle.getParcelable(TAG_DOCTOR_PROFILE));
        }
        if (invoice != null) {
            List<InvoiceItem> invoiceItems = invoice.getInvoiceItems();
            for (InvoiceItem invoiceItem :
                    invoiceItems) {
                addInvoiceItem(invoiceItem);
                Util.sendBroadcast(mApp, AddInvoiceFragment.INTENT_GET_MODIFIED_VALUE);
            }
        }
    }

    @Override
    public void initViews() {
        invoiceItemList = new LinkedHashMap<String, InvoiceItem>();

        lvInvoiceList = (ExpandableHeightListView) view.findViewById(R.id.lv_treatment_list);
        tvNoInvoiceAdded = (TextView) view.findViewById(R.id.tv_no_treatment_added);
        tvNoInvoiceAdded.setText(R.string.no_invoice_found);
        lvInvoiceList.setExpanded(true);

    }

    @Override
    public void initListeners() {

    }

    private void initAdapter() {
        adapter = new SelectedInvoiceItemsListAdapter(mActivity, this);
        lvInvoiceList.setAdapter(adapter);
        lvInvoiceList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }


    private void showConfirmationAlert(final InvoiceItem invoiceItem) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(R.string.confirm_remove_invoice_item);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    invoiceItemList.remove(invoiceItem.getItemId());
                    totalCostHashMap.remove(invoiceItem.getItemId());
                    notifyAdapter(invoiceItemList);
                    Util.sendBroadcast(mApp, AddInvoiceFragment.INTENT_GET_MODIFIED_VALUE);
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
    public void onDeleteItemClicked(InvoiceItem invoiceItem) {
        showConfirmationAlert(invoiceItem);
    }

    @Override
    public void onInvoiceItemClicked(InvoiceItem invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    @Override
    public void onTotalValueTypeDetailChanged(String treatmentId, TotalTreatmentCostDiscountValues totalTreatmentCostDiscountValues) {
        totalCostHashMap.put(treatmentId, totalTreatmentCostDiscountValues);

        TotalTreatmentCostDiscountValues finalCostDiscontDetails = new TotalTreatmentCostDiscountValues();
        for (TotalTreatmentCostDiscountValues totalValues :
                totalCostHashMap.values()) {
            finalCostDiscontDetails.setTotalTax(finalCostDiscontDetails.getTotalTax() + totalValues.getTotalTax());
            finalCostDiscontDetails.setTotalCost(finalCostDiscontDetails.getTotalCost() + totalValues.getTotalCost());
            finalCostDiscontDetails.setTotalGrandTotal(finalCostDiscontDetails.getTotalGrandTotal() + totalValues.getTotalGrandTotal());
            finalCostDiscontDetails.setTotalDiscount(finalCostDiscontDetails.getTotalDiscount() + totalValues.getTotalDiscount());
        }
        LogUtils.LOGD(TAG, "Total value for totalCost " + finalCostDiscontDetails.getTotalCost() + " grandTotal : " + finalCostDiscontDetails.getTotalGrandTotal());
        Util.sendBroadcastWithParcelData(mApp, AddInvoiceFragment.INTENT_GET_MODIFIED_VALUE, new String[]{TAG_TOTAL_COST_DISCOUNT_DETAIL_VALUES}, new Object[]{finalCostDiscontDetails});

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

    private void notifyAdapter(HashMap<String, InvoiceItem> drugsListMap) {
        ArrayList<InvoiceItem> list = new ArrayList<>(drugsListMap.values());
        if (!Util.isNullOrEmptyList(list)) {
            lvInvoiceList.setVisibility(View.VISIBLE);
            tvNoInvoiceAdded.setVisibility(View.GONE);
        } else {
            lvInvoiceList.setVisibility(View.GONE);
            tvNoInvoiceAdded.setVisibility(View.VISIBLE);
        }
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
    }

    public void addInvoiceItem(InvoiceItem invoiceItem) {
        if (invoiceItem != null) {
            String key = invoiceItem.getItemId();
            if (!invoiceItemList.containsKey(key)) {
                invoiceItemList.put(key, invoiceItem);
                notifyAdapter(invoiceItemList);
                lvInvoiceList.setSelection(adapter.getCount());
            }
        }
    }


    //setting drug to null and drugId to uniqueId for sending on server
    public List<InvoiceItem> getModifiedInvoiceItemList() {
        if (!Util.isNullOrEmptyList(invoiceItemList)) {
            List<InvoiceItem> itemArrayList = new ArrayList<InvoiceItem>(invoiceItemList.values());
            return itemArrayList;
        }
        return null;
    }

    //setting drug to null and drugId to uniqueId for sending on server
    public List<InvoiceItemRequest> getModifiedInvoiceItemRequestList() {
        if (!Util.isNullOrEmptyList(invoiceItemList)) {
            List<InvoiceItemRequest> invoiceItemRequestList = new ArrayList<InvoiceItemRequest>();
            List<InvoiceItem> itemArrayList = new ArrayList<InvoiceItem>(invoiceItemList.values());
            for (InvoiceItem invoiceItem :
                    itemArrayList) {
                InvoiceItemRequest invoiceItemRequest = new InvoiceItemRequest();
                invoiceItemRequest.setItemId(invoiceItem.getItemId());
                invoiceItemRequest.setDoctorId(invoiceItem.getDoctorId());
                invoiceItemRequest.setDoctorName(invoiceItem.getDoctorName());
                invoiceItemRequest.setName(invoiceItem.getName());
                invoiceItemRequest.setType(invoiceItem.getType());
                invoiceItemRequest.setDiscount(invoiceItem.getDiscount());
                invoiceItemRequest.setQuantity(invoiceItem.getQuantity());
                invoiceItemRequest.setStatus(invoiceItem.getStatus());
                invoiceItemRequest.setCost(invoiceItem.getCost());
                invoiceItemRequest.setTax(invoiceItem.getTax());
                invoiceItemRequest.setFinalCost(invoiceItem.getFinalCost());
                invoiceItemRequest.setTreatmentFields(invoiceItem.getTreatmentFields());
                invoiceItemRequestList.add(invoiceItemRequest);
            }
            return invoiceItemRequestList;
        }
        return null;
    }


    public View getLastChildView() {
        View lastview = null;
        try {
            lastview = lvInvoiceList.getChildAt(adapter.getCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lastview == null)
            lastview = view;
        return lastview;
    }
}
