package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.IngredientResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.NutrientResponse;
import com.healthcoco.healthcocopad.bean.server.Nutrients;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectedRecipeItemClickListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.NutrientListItemViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Prashant on 27-08-2018.
 */
public class AddEditNutrientValueFragment extends HealthCocoFragment implements View.OnClickListener,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised {

    //variables need for pagination
    public static final int MAX_SIZE = 100;
    private int PAGE_NUMBER = 0;

    private User user;

    private RecyclerView rvGeneral;
    private RecyclerView rvCarbs;
    private RecyclerView rvLipid;
    private RecyclerView rvProteinAminoAcid;
    private RecyclerView rvVitamin;
    private RecyclerView rvMineral;
    private RecyclerView rvOther;

    private List<Nutrients> generalNutrients = new ArrayList<>();
    private List<Nutrients> carbNutrients = new ArrayList<>();
    private List<Nutrients> lipidNutrients = new ArrayList<>();
    private List<Nutrients> proteinAminoAcidNutrients = new ArrayList<>();
    private List<Nutrients> vitaminNutrients = new ArrayList<>();
    private List<Nutrients> mineralNutrients = new ArrayList<>();
    private List<Nutrients> otherNutrients = new ArrayList<>();

    private LinkedHashMap<String, Nutrients> generalNutrientMap = new LinkedHashMap<>();
    private LinkedHashMap<String, Nutrients> carbNutrientMap = new LinkedHashMap<>();
    private LinkedHashMap<String, Nutrients> lipidNutrientMap = new LinkedHashMap<>();
    private LinkedHashMap<String, Nutrients> proteinAminoAcidNutrientMap = new LinkedHashMap<>();
    private LinkedHashMap<String, Nutrients> vitaminNutrientMap = new LinkedHashMap<>();
    private LinkedHashMap<String, Nutrients> mineralNutrientMap = new LinkedHashMap<>();
    private LinkedHashMap<String, Nutrients> otherNutrientMap = new LinkedHashMap<>();


    private HealthcocoRecyclerViewAdapter generalAdapter;
    private HealthcocoRecyclerViewAdapter carbAdapter;
    private HealthcocoRecyclerViewAdapter lipidAdapter;
    private HealthcocoRecyclerViewAdapter proteinAminoAcidAdapter;
    private HealthcocoRecyclerViewAdapter vitaminAdapter;
    private HealthcocoRecyclerViewAdapter mineralAdapter;
    private HealthcocoRecyclerViewAdapter otherAdapter;

    private boolean isFromRecipe;
    private Ingredient ingredient;
    private DietPlanRecipeItem dietPlanRecipeItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_nutrient_value, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = mActivity.getIntent();
        isFromRecipe = intent.getBooleanExtra(HealthCocoConstants.TAG_IS_FROM_RECIPE, false);
        dietPlanRecipeItem = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_RECIPE_DATA));
        ingredient = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_INGREDIENT_DATA));

        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapters();
        initData();
    }

    @Override
    public void initViews() {
//        tvTypeQuantity = view.findViewById(R.id.tv_type_quantity);

        rvGeneral = view.findViewById(R.id.rv_general);
        rvCarbs = view.findViewById(R.id.rv_cabs);
        rvLipid = view.findViewById(R.id.rv_lipid);
        rvProteinAminoAcid = view.findViewById(R.id.rv_protein_amino_acid);
        rvVitamin = view.findViewById(R.id.rv_vitamin);
        rvMineral = view.findViewById(R.id.rv_mineral);
        rvOther = view.findViewById(R.id.rv_other);
    }

    private void initAdapters() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvGeneral.setLayoutManager(layoutManager);
        rvGeneral.setItemAnimator(new DefaultItemAnimator());

        generalAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.NUTRIENT_ITEM, this);
        generalAdapter.setListData((ArrayList<Object>) (Object) generalNutrients);
        rvGeneral.setAdapter(generalAdapter);


        LinearLayoutManager layoutManagerCarbs = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvCarbs.setLayoutManager(layoutManagerCarbs);
        rvCarbs.setItemAnimator(new DefaultItemAnimator());

        carbAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.NUTRIENT_ITEM, this);
        carbAdapter.setListData((ArrayList<Object>) (Object) carbNutrients);
        rvCarbs.setAdapter(carbAdapter);


        LinearLayoutManager layoutManagerLipid = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvLipid.setLayoutManager(layoutManagerLipid);
        rvLipid.setItemAnimator(new DefaultItemAnimator());

        lipidAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.NUTRIENT_ITEM, this);
        lipidAdapter.setListData((ArrayList<Object>) (Object) lipidNutrients);
        rvLipid.setAdapter(lipidAdapter);


        LinearLayoutManager layoutManagerProtein = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvProteinAminoAcid.setLayoutManager(layoutManagerProtein);
        rvProteinAminoAcid.setItemAnimator(new DefaultItemAnimator());

        proteinAminoAcidAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.NUTRIENT_ITEM, this);
        proteinAminoAcidAdapter.setListData((ArrayList<Object>) (Object) proteinAminoAcidNutrients);
        rvProteinAminoAcid.setAdapter(proteinAminoAcidAdapter);


        LinearLayoutManager layoutManagerVitamin = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvVitamin.setLayoutManager(layoutManagerVitamin);
        rvVitamin.setItemAnimator(new DefaultItemAnimator());

        vitaminAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.NUTRIENT_ITEM, this);
        vitaminAdapter.setListData((ArrayList<Object>) (Object) vitaminNutrients);
        rvVitamin.setAdapter(vitaminAdapter);


        LinearLayoutManager layoutManagerMineral = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvMineral.setLayoutManager(layoutManagerMineral);
        rvMineral.setItemAnimator(new DefaultItemAnimator());

        mineralAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.NUTRIENT_ITEM, this);
        mineralAdapter.setListData((ArrayList<Object>) (Object) mineralNutrients);
        rvMineral.setAdapter(mineralAdapter);


        LinearLayoutManager layoutManagerOther = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvOther.setLayoutManager(layoutManagerOther);
        rvOther.setItemAnimator(new DefaultItemAnimator());

        otherAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.NUTRIENT_ITEM, this);
        otherAdapter.setListData((ArrayList<Object>) (Object) otherNutrients);
        rvOther.setAdapter(otherAdapter);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initData() {
    }

    private void getIngredientList() {
        mApp.cancelPendingRequests(String.valueOf(WebServiceType.GET_RECIPE_LIST_SOLR));
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getListSolr(NutrientResponse.class,
                WebServiceType.GET_NUTRIENT_LIST_SOLR, PAGE_NUMBER, MAX_SIZE, "", this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                validateData();
                break;
        }
    }

    public void validateData() {
        if (!Util.isNullOrEmptyList(generalNutrients))
            generalNutrients = getDataFromList(rvGeneral, generalNutrients.size());
        if (!Util.isNullOrEmptyList(carbNutrients))
            carbNutrients = getDataFromList(rvCarbs, carbNutrients.size());
        if (!Util.isNullOrEmptyList(lipidNutrients))
            lipidNutrients = getDataFromList(rvLipid, lipidNutrients.size());
        if (!Util.isNullOrEmptyList(proteinAminoAcidNutrients))
            proteinAminoAcidNutrients = getDataFromList(rvProteinAminoAcid, proteinAminoAcidNutrients.size());
        if (!Util.isNullOrEmptyList(vitaminNutrients))
            vitaminNutrients = getDataFromList(rvVitamin, vitaminNutrients.size());
        if (!Util.isNullOrEmptyList(mineralNutrients))
            mineralNutrients = getDataFromList(rvMineral, mineralNutrients.size());
        if (!Util.isNullOrEmptyList(otherNutrients))
            otherNutrients = getDataFromList(rvOther, otherNutrients.size());

        if (isFromRecipe) {
            if (dietPlanRecipeItem == null)
                dietPlanRecipeItem = new DietPlanRecipeItem();
            dietPlanRecipeItem.setGeneralNutrients(generalNutrients);
            dietPlanRecipeItem.setCarbNutrients(carbNutrients);
            dietPlanRecipeItem.setLipidNutrients(lipidNutrients);
            dietPlanRecipeItem.setProteinAminoAcidNutrients(proteinAminoAcidNutrients);
            dietPlanRecipeItem.setVitaminNutrients(vitaminNutrients);
            dietPlanRecipeItem.setMineralNutrients(mineralNutrients);
            dietPlanRecipeItem.setOtherNutrients(otherNutrients);

            Intent data = new Intent();
            data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(dietPlanRecipeItem));
            getActivity().setResult(mActivity.RESULT_OK, data);
            getActivity().finish();
        } else {
            if (ingredient == null)
                ingredient = new Ingredient();
            ingredient.setGeneralNutrients(generalNutrients);
            ingredient.setCarbNutrients(carbNutrients);
            ingredient.setLipidNutrients(lipidNutrients);
            ingredient.setProteinAminoAcidNutrients(proteinAminoAcidNutrients);
            ingredient.setVitaminNutrients(vitaminNutrients);
            ingredient.setMineralNutrients(mineralNutrients);
            ingredient.setOtherNutrients(otherNutrients);

            Intent data = new Intent();
            data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(ingredient));
            getActivity().setResult(mActivity.RESULT_OK, data);
            getActivity().finish();
        }
    }

    private List<Nutrients> getDataFromList(RecyclerView recyclerView, int listSize) {
        List<Nutrients> nutrientList = new ArrayList<>();
        for (int position = 0; position < listSize; position++) {
            View view = recyclerView.getChildAt(position);
            EditText editValue = view.findViewById(R.id.edit_value);
            TextView tvType = view.findViewById(R.id.tv_type);

            String value = Util.getValidatedValueOrNull(editValue);
            if (!Util.isNullOrBlank(value)) {
                Nutrients nutrient = (Nutrients) editValue.getTag();
                QuantityType type = (QuantityType) tvType.getTag();

                nutrient.setValue(Double.parseDouble(value));
                nutrient.setType(type);
                nutrientList.add(nutrient);
            }
        }
        return nutrientList;
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();

            if (volleyResponseBean.getWebServiceType() != null) {
                mActivity.hideLoading();
                Util.showToast(mActivity, volleyResponseBean.getWebServiceType() + errorMsg);
                return;
            }
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case FRAGMENT_INITIALISATION:
                getIngredientList();
                break;
            case GET_NUTRIENT_LIST_SOLR:
                ArrayList<NutrientResponse> list = (ArrayList<NutrientResponse>) (ArrayList<?>) response.getDataList();
                if (!Util.isNullOrEmptyList(list)) {
                    LogUtils.LOGD(TAG, "onResponse Nutrient Size " + list.size() + " isDataFromLocal " + response.isDataFromLocal());
                    notifyAdapter(list);
                }
                mActivity.hideLoading();
                break;
        }
        mActivity.hideLoading();
    }

    private void notifyAdapter(ArrayList<NutrientResponse> list) {
        for (NutrientResponse nutrientItem : list) {
            Nutrients nutrient = new Nutrients();
            nutrient.setUniqueId(nutrientItem.getUniqueId());
            nutrient.setName(nutrientItem.getName());
            nutrient.setNutrientCode(nutrientItem.getNutrientCode());

            switch (nutrientItem.getCategory()) {
                case GENERAL:
                    generalNutrientMap.put(nutrient.getUniqueId(), nutrient);
                    generalNutrients.add(nutrient);
                    break;
                case CARBOHYDRATE:
                    carbNutrientMap.put(nutrient.getUniqueId(), nutrient);
                    carbNutrients.add(nutrient);
                    break;
                case LIPIDS:
                    lipidNutrientMap.put(nutrient.getUniqueId(), nutrient);
                    lipidNutrients.add(nutrient);
                    break;
                case PROTEIN_AMINOACIDS:
                    proteinAminoAcidNutrientMap.put(nutrient.getUniqueId(), nutrient);
                    proteinAminoAcidNutrients.add(nutrient);
                    break;
                case VITAMINS:
                    vitaminNutrientMap.put(nutrient.getUniqueId(), nutrient);
                    vitaminNutrients.add(nutrient);
                    break;
                case MINERALS:
                    mineralNutrientMap.put(nutrient.getUniqueId(), nutrient);
                    mineralNutrients.add(nutrient);
                    break;
                case OTHERS:
                    otherNutrientMap.put(nutrient.getUniqueId(), nutrient);
                    otherNutrients.add(nutrient);
                    break;
            }
        }

        updateReceivedData();

        generalAdapter.notifyDataSetChanged();
        carbAdapter.notifyDataSetChanged();
        lipidAdapter.notifyDataSetChanged();
        proteinAminoAcidAdapter.notifyDataSetChanged();
        vitaminAdapter.notifyDataSetChanged();
        mineralAdapter.notifyDataSetChanged();
        otherAdapter.notifyDataSetChanged();
    }

    private void updateReceivedData() {

        if (dietPlanRecipeItem != null) {
            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getGeneralNutrients())) {
                generalNutrients.clear();
                generalNutrients.addAll(getReceivedData(dietPlanRecipeItem.getGeneralNutrients(), generalNutrientMap));
            }
            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getCarbNutrients())) {
                carbNutrients.clear();
                carbNutrients.addAll(getReceivedData(dietPlanRecipeItem.getCarbNutrients(), carbNutrientMap));
            }
            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getLipidNutrients())) {
                lipidNutrients.clear();
                lipidNutrients.addAll(getReceivedData(dietPlanRecipeItem.getLipidNutrients(), lipidNutrientMap));
            }
            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getProteinAminoAcidNutrients())) {
                proteinAminoAcidNutrients.clear();
                proteinAminoAcidNutrients.addAll(getReceivedData(dietPlanRecipeItem.getProteinAminoAcidNutrients(), proteinAminoAcidNutrientMap));
            }
            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getVitaminNutrients())) {
                vitaminNutrients.clear();
                vitaminNutrients.addAll(getReceivedData(dietPlanRecipeItem.getVitaminNutrients(), vitaminNutrientMap));
            }
            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getMineralNutrients())) {
                mineralNutrients.clear();
                mineralNutrients.addAll(getReceivedData(dietPlanRecipeItem.getMineralNutrients(), mineralNutrientMap));
            }
            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getOtherNutrients())) {
                otherNutrients.clear();
                otherNutrients.addAll(getReceivedData(dietPlanRecipeItem.getOtherNutrients(), otherNutrientMap));
            }
        } else if (ingredient != null) {

            if (!Util.isNullOrEmptyList(ingredient.getGeneralNutrients())) {
                generalNutrients.clear();
                generalNutrients.addAll(getReceivedData(ingredient.getGeneralNutrients(), generalNutrientMap));
            }
            if (!Util.isNullOrEmptyList(ingredient.getCarbNutrients())) {
                carbNutrients.clear();
                carbNutrients.addAll(getReceivedData(ingredient.getCarbNutrients(), carbNutrientMap));
            }
            if (!Util.isNullOrEmptyList(ingredient.getLipidNutrients())) {
                lipidNutrients.clear();
                lipidNutrients.addAll(getReceivedData(ingredient.getLipidNutrients(), lipidNutrientMap));
            }
            if (!Util.isNullOrEmptyList(ingredient.getProteinAminoAcidNutrients())) {
                proteinAminoAcidNutrients.clear();
                proteinAminoAcidNutrients.addAll(getReceivedData(ingredient.getProteinAminoAcidNutrients(), proteinAminoAcidNutrientMap));
            }
            if (!Util.isNullOrEmptyList(ingredient.getVitaminNutrients())) {
                vitaminNutrients.clear();
                vitaminNutrients.addAll(getReceivedData(ingredient.getVitaminNutrients(), vitaminNutrientMap));
            }
            if (!Util.isNullOrEmptyList(ingredient.getMineralNutrients())) {
                mineralNutrients.clear();
                mineralNutrients.addAll(getReceivedData(ingredient.getMineralNutrients(), mineralNutrientMap));
            }
            if (!Util.isNullOrEmptyList(ingredient.getOtherNutrients())) {
                otherNutrients.clear();
                otherNutrients.addAll(getReceivedData(ingredient.getOtherNutrients(), otherNutrientMap));
            }
        }
    }

    private List<Nutrients> getReceivedData(List<Nutrients> nutrientsList, LinkedHashMap<String, Nutrients> nutrientsHashMap) {
        for (Nutrients nutrient : nutrientsList) {
            nutrientsHashMap.put(nutrient.getUniqueId(), nutrient);
        }
        return new ArrayList<>(nutrientsHashMap.values());
    }

    /**
     * dont add break statement after add(since we need to call get statement also after add
     *
     * @param response
     * @return
     */

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                }
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
    }
}
