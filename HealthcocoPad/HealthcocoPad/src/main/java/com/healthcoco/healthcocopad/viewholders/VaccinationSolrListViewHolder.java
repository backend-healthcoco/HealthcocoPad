package com.healthcoco.healthcocopad.viewholders;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.VaccineSolarResponse;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.VaccineStatus;
import com.healthcoco.healthcocopad.listeners.PopupWindowListener;
import com.healthcoco.healthcocopad.listeners.SearchVaccineListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;

public class VaccinationSolrListViewHolder extends HealthcocoComonRecylcerViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, PopupWindowListener, com.healthcoco.healthcocopad.popupwindow.PopupWindowListener {

    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "dd MMM yyyy";
    private TextView tvVaccineName;
    private CheckBox cbVaccine;
    private SearchVaccineListener searchVaccineListener;
    private VaccineSolarResponse vaccineSolarResponse;
    private TextView tvVaccineMilestone;
    private TextView tvDueDate;
    private TextView tvStatus;

    public VaccinationSolrListViewHolder(HealthCocoActivity activity, View itemView, HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
        super(activity, itemView, onItemClickListener, listenerObject);
        this.searchVaccineListener = (SearchVaccineListener) listenerObject;
    }

    @Override
    public void initViews(View itemView) {
        tvVaccineName = (TextView) itemView.findViewById(R.id.tv_vaccine_name);
        tvVaccineMilestone = (TextView) itemView.findViewById(R.id.tv_vaccine_milestone);
        tvDueDate = (TextView) itemView.findViewById(R.id.tv_due_date);
        tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
        cbVaccine = (CheckBox) itemView.findViewById(R.id.cb_vaccine);
        cbVaccine.setOnCheckedChangeListener(this);
        tvDueDate.setOnClickListener(this);
        mActivity.initPopupWindows(tvStatus, PopupWindowType.VACCINE_STATUS, new ArrayList<Object>(PopupWindowType.VACCINE_STATUS.getList()), this);
    }

    @Override
    public void applyData(Object object) {
        vaccineSolarResponse = (VaccineSolarResponse) object;
        if (vaccineSolarResponse != null) {
            String formattedName = "";
            if (!Util.isNullOrBlank(vaccineSolarResponse.getLongName()))
                formattedName = vaccineSolarResponse.getLongName();
            tvVaccineName.setText(formattedName);

            if (!Util.isNullOrBlank(vaccineSolarResponse.getDuration()))
                tvVaccineMilestone.setText("Milestone: " + vaccineSolarResponse.getDuration());
            else tvVaccineMilestone.setText("");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        searchVaccineListener.isVaccinationClicked(isChecked, (Long) tvDueDate.getTag(), (VaccineStatus) tvStatus.getTag(), vaccineSolarResponse);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_due_date) {
            openDatePickerDialog((TextView) v);
        }
    }

    private void openDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue( DATE_FORMAT_USED_IN_THIS_SCREEN,calendar,textView);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cbVaccine.setChecked(false);
                textView.setText(DateTimeUtil.getFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN, year, monthOfYear, dayOfMonth, 0, 0, 0));
                textView.setTag(DateTimeUtil.getLongFromFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, tvDueDate.getText().toString()));
                cbVaccine.setChecked(true);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case VACCINE_STATUS:
                if (object instanceof VaccineStatus) {
                    cbVaccine.setChecked(false);
                    VaccineStatus vaccineStatus = (VaccineStatus) object;
                    tvStatus.setText(vaccineStatus.getStatus());
                    tvStatus.setTag(vaccineStatus);
                    cbVaccine.setChecked(true);
                }
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }
}
