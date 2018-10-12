package com.healthcoco.healthcocopad.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.ClinicImage;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.Events;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.IngredientResponse;
import com.healthcoco.healthcocopad.bean.server.Meal;
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.KioskSubItemType;
import com.healthcoco.healthcocopad.viewholders.AboutDoctorListViewHolder;
import com.healthcoco.healthcocopad.viewholders.ClinicImageListItemHolder;
import com.healthcoco.healthcocopad.viewholders.DoctorListViewHolder;
import com.healthcoco.healthcocopad.viewholders.EventItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.FoodSubItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.IngredientListItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.IngredientListSolrViewHolder;
import com.healthcoco.healthcocopad.viewholders.KioskSubItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.QueueItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.RecipeListItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.RecipeListSolrViewHolder;

import java.util.ArrayList;

/**
 * Created by neha on 12/03/18.
 */

public class HealthcocoRecyclerViewAdapter extends RecyclerView.Adapter<HealthcocoComonRecylcerViewHolder> {

    private HealthcocoRecyclerViewItemClickListener onItemClickListener;
    //required by ImageviewHolder
    private boolean takeATour;

    private boolean likedPosts;

    private Object listenerObject;
    private RecyclerView lvChats;
    private AdapterType adapterType;
    private HealthCocoActivity mActivity;
    private ArrayList<Object> list;

    //GeneralConstructor
    public HealthcocoRecyclerViewAdapter(HealthCocoActivity activity, AdapterType adapterType,
                                         HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
        this.mActivity = activity;
        this.adapterType = adapterType;
        this.listenerObject = listenerObject;
        this.onItemClickListener = onItemClickListener;
    }

    public HealthcocoRecyclerViewAdapter(HealthCocoActivity activity, AdapterType adapterType, Object listenerObject) {
        this(activity, adapterType, null, listenerObject);

    }

    //GeneralConstructor
    public HealthcocoRecyclerViewAdapter(HealthCocoActivity activity, AdapterType adapterType, HealthcocoRecyclerViewItemClickListener onItemClickListener) {
        this(activity, adapterType, onItemClickListener, null);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public HealthcocoComonRecylcerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View itemView = layoutInflater.inflate(R.layout.layout_item, parent, false);
        //return new ItemHolder(itemView, this);
        HealthcocoComonRecylcerViewHolder viewHolder = null;
        View convertView = null;
        LayoutInflater mInflater = mActivity.getLayoutInflater();
        switch (adapterType) {
            case APOINTMENT_QUEUE:
                convertView = mInflater.inflate(R.layout.list_item_queue, null);
                viewHolder = new QueueItemViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                break;

            case EVENT_LIST:
                convertView = mInflater.inflate(R.layout.list_item_event, null);
                viewHolder = new EventItemViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                break;

            case KIOSK_SUB_ITEM:
                convertView = mInflater.inflate(R.layout.list_item_kiosk_tab, null);
                viewHolder = new KioskSubItemViewHolder(mActivity, convertView, onItemClickListener);
                break;

            case FOOD_SUB_ITEM:
                convertView = mInflater.inflate(R.layout.sub_item_add_food, parent, false);
                viewHolder = new FoodSubItemViewHolder(mActivity, convertView, onItemClickListener);
                break;

            case RECIPE_ITEM:
                convertView = mInflater.inflate(R.layout.list_item_selected_recipe, parent, false);
                viewHolder = new RecipeListItemViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                break;
            case RECIPE_ITEM_SOLR:
                convertView = mInflater.inflate(R.layout.sub_item_add_food, parent, false);
                viewHolder = new RecipeListSolrViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                break;
            case INGREDIENT_ITEM_SOLR:
                convertView = mInflater.inflate(R.layout.sub_item_add_food, parent, false);
                viewHolder = new IngredientListSolrViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                break;
            case INGREDIENT_ITEM:
                convertView = mInflater.inflate(R.layout.list_item_selected_recipe, parent, false);
                viewHolder = new IngredientListItemViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                break;
            case DOCTOR_POPUP_LIST:
                convertView = mInflater.inflate(R.layout.item_doctor_popup_list, parent, false);
                viewHolder = new DoctorListViewHolder(mActivity, convertView, listenerObject);
                break;
            case ABOUT_DOCTOR:
                convertView = mInflater.inflate(R.layout.list_item_about_doctor, parent, false);
                viewHolder = new AboutDoctorListViewHolder(mActivity, convertView, onItemClickListener);
                break;
            case CLINIC_IMAGE:
                convertView = mInflater.inflate(R.layout.list_item_clinic_images, parent, false);
                viewHolder = new ClinicImageListItemHolder(mActivity, convertView, onItemClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HealthcocoComonRecylcerViewHolder holder, int position) {
        Object object = list.get(position);
        switch (adapterType) {
            case APOINTMENT_QUEUE:
                if (holder instanceof QueueItemViewHolder && object instanceof CalendarEvents) {
                    QueueItemViewHolder queueItemViewHolder = ((QueueItemViewHolder) holder);
                    queueItemViewHolder.applyData(object);
                }
                break;
            case EVENT_LIST:
                if (holder instanceof EventItemViewHolder && object instanceof Events) {
                    EventItemViewHolder eventItemViewHolder = ((EventItemViewHolder) holder);
                    eventItemViewHolder.applyData(object);
                }
                break;
            case KIOSK_SUB_ITEM:
                if (holder instanceof KioskSubItemViewHolder && object instanceof KioskSubItemType) {
                    KioskSubItemViewHolder kioskSubItemViewHolder = ((KioskSubItemViewHolder) holder);
                    kioskSubItemViewHolder.applyData(object);
                }
                break;
            case DOCTOR_POPUP_LIST:
                if (holder instanceof DoctorListViewHolder && object instanceof RegisteredDoctorProfile) {
                    DoctorListViewHolder doctorListViewHolder = ((DoctorListViewHolder) holder);
                    doctorListViewHolder.applyData(object);
                }
                break;
            case ABOUT_DOCTOR:
                if (holder instanceof AboutDoctorListViewHolder && object instanceof ClinicDoctorProfile) {
                    AboutDoctorListViewHolder aboutDoctorListViewHolder = (AboutDoctorListViewHolder) holder;
                    aboutDoctorListViewHolder.applyData(object);
                }
                break;
            case CLINIC_IMAGE:
                if (holder instanceof ClinicImageListItemHolder && object instanceof ClinicImage) {
                    ClinicImageListItemHolder clinicImageListItemHolder = (ClinicImageListItemHolder) holder;
                    clinicImageListItemHolder.applyData(object);
                }
                break;
            case RECIPE_ITEM:
                if (holder instanceof RecipeListItemViewHolder && object instanceof DietPlanRecipeItem) {
                    RecipeListItemViewHolder recipeListItemViewHolder = (RecipeListItemViewHolder) holder;
                    recipeListItemViewHolder.applyData(object);
                }
                break;
            case INGREDIENT_ITEM:
                if (holder instanceof IngredientListItemViewHolder && object instanceof Ingredient) {
                    IngredientListItemViewHolder listItemViewHolder = (IngredientListItemViewHolder) holder;
                    listItemViewHolder.applyData(object);
                }
                break;
            case RECIPE_ITEM_SOLR:
                if (holder instanceof RecipeListSolrViewHolder && object instanceof RecipeResponse) {
                    RecipeListSolrViewHolder recipeListSolrViewHolder = (RecipeListSolrViewHolder) holder;
                    recipeListSolrViewHolder.applyData(object);
                }
                break;
            case INGREDIENT_ITEM_SOLR:
                if (holder instanceof IngredientListSolrViewHolder && object instanceof IngredientResponse) {
                    IngredientListSolrViewHolder listSolrViewHolder = (IngredientListSolrViewHolder) holder;
                    listSolrViewHolder.applyData(object);
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setListData(ArrayList<Object> list) {
        this.list = list;
    }


    public Object getItem(int position) {
        return list.get(position);
    }
}


