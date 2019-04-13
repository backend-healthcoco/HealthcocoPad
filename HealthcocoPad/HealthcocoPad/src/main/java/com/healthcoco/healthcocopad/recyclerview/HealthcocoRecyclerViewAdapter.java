package com.healthcoco.healthcocopad.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.BabyAchievementsResponse;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.ClinicImage;
import com.healthcoco.healthcocopad.bean.server.Events;
import com.healthcoco.healthcocopad.bean.server.GrowthChartResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandAssociationRequest;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandResponse;
import com.healthcoco.healthcocopad.bean.server.VaccineCustomResponse;
import com.healthcoco.healthcocopad.bean.server.VaccineResponse;
import com.healthcoco.healthcocopad.bean.server.VaccineSolarResponse;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.KioskSubItemType;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.AboutDoctorListViewHolder;
import com.healthcoco.healthcocopad.viewholders.BabyAchievementViewHolder;
import com.healthcoco.healthcocopad.viewholders.BrandListRecycleViewHolder;
import com.healthcoco.healthcocopad.viewholders.ClinicImageListItemHolder;
import com.healthcoco.healthcocopad.viewholders.ContactsListViewForDeletedPatientHolder;
import com.healthcoco.healthcocopad.viewholders.DoctorListViewHolder;
import com.healthcoco.healthcocopad.viewholders.EventItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.GrowthChartViewHolder;
import com.healthcoco.healthcocopad.viewholders.KioskSubItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.QueueItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.SelectedBrandListViewHolder;
import com.healthcoco.healthcocopad.viewholders.UpdateBrandListViewHolder;
import com.healthcoco.healthcocopad.viewholders.UpdateVaccineListViewHolder;
import com.healthcoco.healthcocopad.viewholders.VaccinationListViewHolder;
import com.healthcoco.healthcocopad.viewholders.VaccinationSolrListViewHolder;

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
            case VACCINATION:
                convertView = mInflater.inflate(R.layout.list_item_vaccination, null);
                convertView.setLayoutParams(getLayoutParams(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new VaccinationListViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                return viewHolder;
            case SEARCH_VACCINATION:
                convertView = mInflater.inflate(R.layout.list_item_solr_vaccination, null);
                convertView.setLayoutParams(getLayoutParams(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new VaccinationSolrListViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                return viewHolder;
            case UPDATE_VACCINE:
                convertView = mInflater.inflate(R.layout.list_item_upadate_vaccine, null);
                convertView.setLayoutParams(getLayoutParams(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new UpdateVaccineListViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                return viewHolder;
            case UPDATE_BRAND:
                convertView = mInflater.inflate(R.layout.list_item_upadate_brand_old, null);
                convertView.setLayoutParams(getLayoutParams(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new UpdateBrandListViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                return viewHolder;
            case BRAND_LIST:
                convertView = mInflater.inflate(R.layout.list_item_brand_list, null);
                convertView.setLayoutParams(getLayoutParams(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new BrandListRecycleViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                return viewHolder;
            case SELECTED_BRAND:
                convertView = mInflater.inflate(R.layout.sub_item_selected_brand, null);
                convertView.setLayoutParams(getLayoutParams(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new SelectedBrandListViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                return viewHolder;
            case GROWTH_CHART:
                convertView = mInflater.inflate(R.layout.item_growth_chart_list, null);
                convertView.setLayoutParams(getLayoutParams(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new GrowthChartViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                return viewHolder;
            case BABY_ACHIEVEMENTS:
                convertView = mInflater.inflate(R.layout.item_baby_achievements_list, null);
                convertView.setLayoutParams(getLayoutParams(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new BabyAchievementViewHolder(mActivity, convertView, onItemClickListener, listenerObject);
                return viewHolder;
            case CONTACT_LIST_FOR_DELETED:
                convertView = mInflater.inflate(R.layout.grid_item_deleted_contacts, null);
                convertView.setLayoutParams(getLayoutParams(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new ContactsListViewForDeletedPatientHolder(mActivity, convertView, onItemClickListener, listenerObject, viewType);
                return viewHolder;
        }
        return viewHolder;
    }

    private ViewGroup.LayoutParams getLayoutParams(View convertView, int width, int height) {
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        if (layoutParams == null)
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        if (width > 0)
        layoutParams.width = width;
//        if (height > 0)
        layoutParams.height = height;
        return layoutParams;
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
            case VACCINATION:
                if (holder instanceof VaccinationListViewHolder && object instanceof VaccineCustomResponse) {
                    VaccinationListViewHolder vaccinationListViewHolder = ((VaccinationListViewHolder) holder);
                    vaccinationListViewHolder.applyData(object);
                }
                break;
            case SEARCH_VACCINATION:
                if (holder instanceof VaccinationSolrListViewHolder && object instanceof VaccineSolarResponse) {
                    VaccinationSolrListViewHolder vaccinationSolrListViewHolder = ((VaccinationSolrListViewHolder) holder);
                    vaccinationSolrListViewHolder.applyData(object);
                }
                break;
            case UPDATE_VACCINE:
                if (holder instanceof UpdateVaccineListViewHolder && object instanceof VaccineResponse) {
                    UpdateVaccineListViewHolder updateVaccineListViewHolder = ((UpdateVaccineListViewHolder) holder);
                    updateVaccineListViewHolder.applyData(object);
                }
                break;
            case UPDATE_BRAND:
                if (holder instanceof UpdateBrandListViewHolder && object instanceof VaccineBrandAssociationRequest) {
                    UpdateBrandListViewHolder updateVaccineListViewHolder = ((UpdateBrandListViewHolder) holder);
                    updateVaccineListViewHolder.applyData(object);
                }
                break;
            case BRAND_LIST:
                if (holder instanceof BrandListRecycleViewHolder && object instanceof VaccineBrandResponse) {
                    BrandListRecycleViewHolder brandListViewHolder = ((BrandListRecycleViewHolder) holder);
                    brandListViewHolder.applyData(object);
                }
                break;
            case SELECTED_BRAND:
                if (holder instanceof SelectedBrandListViewHolder && object instanceof VaccineBrandResponse) {
                    SelectedBrandListViewHolder selectedBrandListViewHolder = ((SelectedBrandListViewHolder) holder);
                    selectedBrandListViewHolder.applyData(object);
                }
                break;
            case GROWTH_CHART:
                if (holder instanceof GrowthChartViewHolder && object instanceof GrowthChartResponse) {
                    GrowthChartViewHolder growthChartViewHolder = ((GrowthChartViewHolder) holder);
                    growthChartViewHolder.applyData(object);
                }
                break;
            case BABY_ACHIEVEMENTS:
                if (holder instanceof BabyAchievementViewHolder && object instanceof BabyAchievementsResponse) {
                    BabyAchievementViewHolder babyAchievementViewHolder = ((BabyAchievementViewHolder) holder);
                    babyAchievementViewHolder.applyData(object);
                }
                break;
            case CONTACT_LIST_FOR_DELETED:
                if (holder instanceof ContactsListViewForDeletedPatientHolder) {
                    ContactsListViewForDeletedPatientHolder selectedBrandListViewHolder = ((ContactsListViewForDeletedPatientHolder) holder);
                    selectedBrandListViewHolder.applyData(object);
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
         if (!Util.isNullOrEmptyList(list))
            return list.size();
        return 0;
    }

    public void setListData(ArrayList<Object> list) {
        this.list = list;
    }


    public Object getItem(int position) {
        if (!Util.isNullOrEmptyList(list))
            return list.get(position);
        return null;
    }
}


