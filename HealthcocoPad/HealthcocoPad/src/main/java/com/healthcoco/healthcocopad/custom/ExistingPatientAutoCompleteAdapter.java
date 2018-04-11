package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.enums.HealthCocoFileType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.PatientNumberSearchViewholder;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by neha on 03/10/16.
 */
public class ExistingPatientAutoCompleteAdapter extends ArrayAdapter<AlreadyRegisteredPatientsResponse> {
    private final String TAG = AutoCompleteTextViewAdapter.class.getSimpleName();
    private HealthCocoActivity mActivity;
    private AutoCompleteTextViewListener autoCompleteTextViewListener;
    private List<AlreadyRegisteredPatientsResponse> items;
    private List<AlreadyRegisteredPatientsResponse> itemsAll = new ArrayList<>();
    private List<AlreadyRegisteredPatientsResponse> suggestions;
    Filter nameFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            try {
                suggestions.clear();
                if (constraint != null) {
                    for (AlreadyRegisteredPatientsResponse object : itemsAll) {
                        String text = object.getFirstName();
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
                items = (List<AlreadyRegisteredPatientsResponse>) results.values;
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private int viewResourceId;
    private PatientNumberSearchViewholder holder;

    public ExistingPatientAutoCompleteAdapter(HealthCocoActivity mActivity, int viewResourceId, List<AlreadyRegisteredPatientsResponse> items) {
        super(mActivity, viewResourceId, items);
        this.mActivity = mActivity;
        if (!Util.isNullOrEmptyList(items)) {
            this.items = Arrays.asList(items.toArray(new AlreadyRegisteredPatientsResponse[items.size()]));
            this.itemsAll = Arrays.asList(items.toArray(new AlreadyRegisteredPatientsResponse[items.size()]));
            this.suggestions = new ArrayList<AlreadyRegisteredPatientsResponse>();
            suggestions.addAll(itemsAll);
            this.viewResourceId = viewResourceId;
        }
    }

    @Override
    public int getCount() {
        if (Util.isNullOrEmptyList(items))
            return 0;
        return items.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(viewResourceId, null);
        }
        try {
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            TextView tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
            ProgressBar progressLoading = (ProgressBar) convertView.findViewById(R.id.progress_loading);
            TextViewFontAwesome ivIcon = (TextViewFontAwesome) convertView.findViewById(R.id.iv_icon);
            ImageView ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);

            AlreadyRegisteredPatientsResponse objData = getItem(position);
            if (objData.getIsPartOfClinic()) {
                tvName.setText(Util.getValidatedValue(objData.getLocalPatientName()));
                ivIcon.setText(R.string.fa_hospital);
            } else {
                tvName.setText(Util.getValidatedValue(objData.getFirstName()));
                ivIcon.setText(R.string.fa_globe);
            }
           /* if (objData.getIsPartOfClinic()) {
                ivIcon.setText(R.string.fa_hospital);
            } else {
                ivIcon.setText(R.string.fa_hospital);
            }*/
            if (!Util.isNullOrBlank(objData.getImageUrl()))
                objData.setImageFilePath(ImageUtil.getPathToSaveFile(HealthCocoFileType.PATIENT_PROFILE, Util.getFileNameFromUrl(objData.getImageUrl()), Util.getFileExtension(objData.getImageUrl())));

            try {
                RegisteredPatientDetailsUpdated patientDetails = new RegisteredPatientDetailsUpdated();
                ReflectionUtil.copy(patientDetails, objData);
                DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, patientDetails, progressLoading, ivContactProfile, tvInitialAlphabet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public AlreadyRegisteredPatientsResponse getItem(int position) {
        return items.get(position);
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    public AlreadyRegisteredPatientsResponse getSelectedObject(int position) {
        if (suggestions != null)
            return suggestions.get(position);
        return null;
    }
}