package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.AppointmentSlot;
import com.healthcoco.healthcocopad.bean.server.CityResponse;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorExperience;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.Profession;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.Role;
import com.healthcoco.healthcocopad.enums.AdvanceSearchOptionsType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

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
            case COMPLAINT:
                if (object instanceof ComplaintSuggestions) {
                    ComplaintSuggestions complaintSuggestions = (ComplaintSuggestions) object;
                    text = complaintSuggestions.getComplaint();
                }
                break;
            case OBSERVATION:
                if (object instanceof ObservationSuggestions) {
                    ObservationSuggestions observationSuggestions = (ObservationSuggestions) object;
                    text = observationSuggestions.getObservation();
                }
                break;
            case INVESTIGATION:
                if (object instanceof InvestigationSuggestions) {
                    InvestigationSuggestions investigationSuggestions = (InvestigationSuggestions) object;
                    text = investigationSuggestions.getInvestigation();
                }
                break;
            case DIAGNOSIS:
                if (object instanceof DiagnosisSuggestions) {
                    DiagnosisSuggestions diagnosisSuggestions = (DiagnosisSuggestions) object;
                    text = diagnosisSuggestions.getDiagnosis();
                }
                break;
            case APPOINTMENT_SLOT:
                if (object instanceof AppointmentSlot) {
                    AppointmentSlot appointmentSlot = (AppointmentSlot) object;
                    text = Util.getFormattedAppointmentSlot(appointmentSlot);
                }
                break;
            case NUMBERS:
            case YEAR_OF_PASSING:
                if (object instanceof String) {
                    text = (String) object;
                    if (text.equalsIgnoreCase(DateTimeUtil.getCurrentFormattedDate(DateTimeUtil.YEAR_FORMAT)) && autoCompleteTextViewListener != null)
                        autoCompleteTextViewListener.scrollToPosition(position);
                }
                break;
            case EXPERIENCE_LIST:
                if (object instanceof DoctorExperience) {
                    DoctorExperience doctorExperience = (DoctorExperience) object;
                    text = doctorExperience.getExperience() + " " + doctorExperience.getPeriodValue();
                }
                break;
            case COUNTRY:
            case DOCTOR_TITLES:
            case BLOOD_GROUP:
                if (object instanceof String) {
                    text = (String) object;
                }
                break;
            case PROFESSION:
                if (object instanceof Profession) {
                    Profession profession = (Profession) object;
                    text = profession.getProfession();
                }
                break;
            case REFERENCE:
                if (object instanceof Reference) {
                    Reference reference = (Reference) object;
                    text = reference.getReference();
                }
                break;
            case AVAILABLE_BOOKED_APPOINTMENTS:
//                if (object instanceof AvailableTimeSlots) {
//                    AvailableTimeSlots availableTimeSlots = (AvailableTimeSlots) object;
//                    text = DateTimeUtil.convertFormattedDate(DateTimeUtil.TIME_FORMAT_24_HOUR, BookAppointmentDialogFragment.TIME_SLOT_FORMAT_USED_IN_THIS_SCREEN, availableTimeSlots.getTime());
//                    if (view != null)
//                        if (availableTimeSlots.getIsAvailable() != null && availableTimeSlots.getIsAvailable()) {
//                            Util.enableAllChildViews(view, true);
//                        } else
//                            Util.enableAllChildViews(view, false);
//                }
                break;
            case DRUG_TYPE:
                if (object instanceof DrugType) {
                    DrugType drugType = (DrugType) object;
                    text = Util.getValidatedValue(drugType.getType());
                }
                break;
            case ROLES:
                if (object instanceof Role) {
                    Role drugType = (Role) object;
                    text = String.valueOf(drugType.getRole());
                }
                break;
            case CITY:
                if (object instanceof CityResponse) {
                    CityResponse drugType = (CityResponse) object;
                    text = String.valueOf(drugType.getCity());
                }
                break;
            case DOCTOR_CLINIC:
                if (object instanceof DoctorClinicProfile) {
                    DoctorClinicProfile doctorClinicProfile = (DoctorClinicProfile) object;
                    text = String.valueOf(doctorClinicProfile.getLocationName());
                }
                break;
            case ADVANCE_SEARCH_OPTION:
                if (object instanceof AdvanceSearchOptionsType) {
                    AdvanceSearchOptionsType searchOptionsType = (AdvanceSearchOptionsType) object;
                    text = getContext().getResources().getString(searchOptionsType.getValueId());
                    if (view != null) view.setTag(searchOptionsType);
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
                        || autoCompleteTextViewType == AutoCompleteTextViewType.APPOINTMENT_SLOT
                        || autoCompleteTextViewType == AutoCompleteTextViewType.YEAR_OF_PASSING
                        || autoCompleteTextViewType == AutoCompleteTextViewType.NUMBERS
                        || autoCompleteTextViewType == AutoCompleteTextViewType.EXPERIENCE_LIST
                        || autoCompleteTextViewType == AutoCompleteTextViewType.BLOOD_GROUP
                        || autoCompleteTextViewType == AutoCompleteTextViewType.ADVANCE_SEARCH_OPTION
                        || autoCompleteTextViewType == AutoCompleteTextViewType.DRUG_TYPE
                        || autoCompleteTextViewType == AutoCompleteTextViewType.ROLES
                        || autoCompleteTextViewType == AutoCompleteTextViewType.DOCTOR_TITLES
                        || autoCompleteTextViewType == AutoCompleteTextViewType.AVAILABLE_BOOKED_APPOINTMENTS
                        || autoCompleteTextViewType == AutoCompleteTextViewType.COUNTRY) {

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
