package com.healthcoco.healthcocoplus.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocoplus.bean.server.DoctorExperience;
import com.healthcoco.healthcocoplus.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocoplus.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocoplus.utilities.DateTimeUtil;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public class AutoCompleteTextViewAdapter extends ArrayAdapter<Object> {
    private final String TAG = AutoCompleteTextViewAdapter.class.getSimpleName();
    private AutoCompleteTextViewListener autoCompleteTextViewListener;
    private AutoCompleteTextViewType autoCompleteTextViewType;
    private List<Object> items;
    private List<Object> itemsAll = new ArrayList<>();
    private List<Object> suggestions;
    private int viewResourceId;
    private Context context;

    public AutoCompleteTextViewAdapter(Context context, int viewResourceId, List<Object> items, AutoCompleteTextViewType autoCompleteTextViewType) {
        super(context, viewResourceId, items);
        if (!Util.isNullOrEmptyList(items)) {
            this.items = Arrays.asList(items.toArray(new Object[items.size()]));
            this.itemsAll = Arrays.asList(items.toArray(new Object[items.size()]));
            this.suggestions = new ArrayList<Object>();
            suggestions.addAll(itemsAll);
            this.viewResourceId = viewResourceId;
            this.autoCompleteTextViewType = autoCompleteTextViewType;
        }
    }

    public AutoCompleteTextViewAdapter(Context context, AutoCompleteTextViewListener autoCompleteTextViewListener, int viewResourceId, List<Object> items, AutoCompleteTextViewType autoCompleteTextViewType) {
        super(context, viewResourceId, items);
        if (!Util.isNullOrEmptyList(items)) {
            this.items = Arrays.asList(items.toArray(new Object[items.size()]));
            this.itemsAll = Arrays.asList(items.toArray(new Object[items.size()]));
            this.suggestions = new ArrayList<Object>();
            this.viewResourceId = viewResourceId;
            this.autoCompleteTextViewType = autoCompleteTextViewType;
            this.autoCompleteTextViewListener = autoCompleteTextViewListener;
        }
    }

    public Object getSelectedObject(int position) {
        if (suggestions != null)
            return suggestions.get(position);
        return null;
    }

    @Override
    public int getCount() {
        if (Util.isNullOrEmptyList(items))
            return 0;
        return items.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        try {
            TextView textView = null;
            if (v instanceof TextView) {
                textView = (TextView) v;
            } else {
                textView = (TextView) v.findViewById(R.id.tv_text);
            }
            Object object = items.get(position);
            String text = getText(position, v, autoCompleteTextViewType, object);
            if (textView != null) {
                textView.setText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public String getText(int position, View view, AutoCompleteTextViewType autoCompleteTextViewType, Object object) {
        String text = "";
        switch (autoCompleteTextViewType) {
            case DOCTOR_CLINIC:
                if (object instanceof DoctorClinicProfile) {
                    DoctorClinicProfile doctorClinicProfile = (DoctorClinicProfile) object;
                    text = String.valueOf(doctorClinicProfile.getLocationName());
                }
                break;
            case EXPERIENCE_LIST:
                if (object instanceof DoctorExperience) {
                    DoctorExperience doctorExperience = (DoctorExperience) object;
                    text = doctorExperience.getExperience() + " " + doctorExperience.getPeriodValue();
                }
                break;
            case YEAR_OF_PASSING:
                if (object instanceof String) {
                    text = (String) object;
                    if (text.equalsIgnoreCase(DateTimeUtil.getCurrentFormattedDate(DateTimeUtil.YEAR_FORMAT)) && autoCompleteTextViewListener != null)
                        autoCompleteTextViewListener.scrollToPosition(position);
                }
                break;
        }
        return text;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String text = getText(0, null, autoCompleteTextViewType, resultValue);
            return text;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            try {
                suggestions.clear();
                if (autoCompleteTextViewType == AutoCompleteTextViewType.DOCTOR_CLINIC
                        || autoCompleteTextViewType == AutoCompleteTextViewType.YEAR_OF_PASSING
                        || autoCompleteTextViewType == AutoCompleteTextViewType.DOCTOR_TITLES
                        || autoCompleteTextViewType == AutoCompleteTextViewType.EXPERIENCE_LIST) {

                    suggestions.addAll(itemsAll);
                } else if (constraint != null) {
                    for (Object object : itemsAll) {
                        String text = getText(0, null, autoCompleteTextViewType, object);
                        if (text.toLowerCase().contains(constraint.toString().toLowerCase()))
                            suggestions.add(object);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                items = (List<Object>) results.values;
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
