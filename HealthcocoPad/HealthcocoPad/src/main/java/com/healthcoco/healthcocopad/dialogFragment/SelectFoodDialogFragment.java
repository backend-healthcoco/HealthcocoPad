package com.healthcoco.healthcocopad.dialogFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.TotalTreatmentCostDiscountValues;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.TreatmentRequest;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Meal;
import com.healthcoco.healthcocopad.bean.server.Quantity;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.UnitValue;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.QuantityEnum;
import com.healthcoco.healthcocopad.enums.UnitType;
import com.healthcoco.healthcocopad.enums.VisitIdType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.PatientTreatmentDetailFragment;
import com.healthcoco.healthcocopad.fragments.SelectedTreatmentsListFragment;
import com.healthcoco.healthcocopad.fragments.TreatmentCustomListFragment;
import com.healthcoco.healthcocopad.fragments.TreatmentListFragment;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ScrollViewWithHeaderNewPrescriptionLayout;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Prashant on 09/11/2017.
 */


public class SelectFoodDialogFragment extends HealthCocoDialogFragment implements
        View.OnClickListener, SelectedTreatmentItemClickListener {
    public static final String INTENT_GET_MODIFIED_VALUE = "com.healthcoco.MODIFIED_VALUE";
    public static final String TAG_SELECTED_TREATMENT_OBJECT = "selectedTreatmentItemOrdinal";
    public static final String TAG_TREATMENT_ID = "treatmentId";
    private User user;

    private TreatmentListFragment treatmentListFragment;

    private RecyclerView selectedRecipeRecyclerView;
    private HealthcocoRecyclerViewAdapter mAdapter;
    private LinkedHashMap<String, Meal> mealHashMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_select_food, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
     /*   if (bundle != null && bundle.containsKey(PatientTreatmentDetailFragment.TAG_TREATMENT_DATA)) {
            treatmentsList = Parcels.unwrap(bundle.getParcelable(PatientTreatmentDetailFragment.TAG_TREATMENT_DATA));
            if (!Util.isNullOrEmptyList(treatmentsList)) {
                for (Treatments treatments : treatmentsList) {
                    treatment = treatments;
                }
            }
        }
        if (bundle != null && bundle.containsKey(HealthCocoConstants.TAG_IS_FROM_VISIT)) {
            isFromVisit = Parcels.unwrap(bundle.getParcelable(HealthCocoConstants.TAG_IS_FROM_VISIT));
        }*/
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
        selectedRecipeRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_selected_recipe);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initSaveButton(this);
    }

    @Override
    public void initData() {

    }


    private void initAdapter() {
        mealHashMap = new LinkedHashMap<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        selectedRecipeRecyclerView.setLayoutManager(layoutManager);
        selectedRecipeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.FOOD_SUB_ITEM, this);
//        mAdapter.setListData();
        selectedRecipeRecyclerView.setAdapter(mAdapter);

    }

    private void initSelectedTreatmentsListFragment() {
//        Bundle bundle = new Bundle();
//        selectedTreatmentsListFragment = new SelectedTreatmentsListFragment();
//        if (!Util.isNullOrEmptyList(treatmentsList))
//            bundle.putParcelable(SelectedTreatmentsListFragment.TAG_TREATMENT_ITEM_DETAIL, Parcels.wrap(treatmentsList.get(0)));
//        bundle.putParcelable(SelectedTreatmentsListFragment.TAG_DOCTOR_PROFILE, Parcels.wrap(doctorProfile));
//        selectedTreatmentsListFragment.setArguments(bundle);
//        mFragmentManager.beginTransaction().add(R.id.layout_recipe_list, selectedTreatmentsListFragment, selectedTreatmentsListFragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
            default:
                break;
        }
    }

    public void validateData() {
        int msgId = getBlankTreatmentMsg();
        if (msgId == 0) {
//            addTreatment();
        } else {
            Util.showToast(mActivity, msgId);
        }
    }


    public int getBlankTreatmentMsg() {
        int msgId = R.string.alert_add_treatment;
//        if (!Util.isNullOrEmptyList(selectedTreatmentsListFragment.getModifiedTreatmentsItemList()))
        return 0;
//        return msgIzd;
    }


    @Override
    public void onTreatmentItemClick(Object object) {
       /* TreatmentService treatmentService = (TreatmentService) object;
        addTreatmentService(treatmentService);
        if (object != null && selectedTreatmentsListFragment != null) {
            svContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    svContainer.removeOnLayoutChangeListener(this);
                    Log.d(TAG, "list got updated, do what ever u want");
                    svContainer.requestChildFocus(selectedTreatmentsListFragment.getLastChildView(), selectedTreatmentsListFragment.getLastChildView());
                }
            });
        }*/
        Meal meal = new Meal();
        notifyAdapter(mealHashMap);
    }

    private void notifyAdapter(HashMap<String, Meal> mealHashMap) {
        ArrayList<Meal> list = new ArrayList<>(mealHashMap.values());
        if (!Util.isNullOrEmptyList(list)) {
            selectedRecipeRecyclerView.setVisibility(View.VISIBLE);
//            tvNoTreatmentAdded.setVisibility(View.GONE);
        } else {
            selectedRecipeRecyclerView.setVisibility(View.GONE);
//            tvNoTreatmentAdded.setVisibility(View.VISIBLE);
        }
        mAdapter.setListData((ArrayList<Object>) (Object) list);
        mAdapter.notifyDataSetChanged();
//        selectedRecipeRecyclerView.smoothScrollToPosition(list.size() - 1);
    }

    @Override
    public User getUser() {
        return null;
    }
}
