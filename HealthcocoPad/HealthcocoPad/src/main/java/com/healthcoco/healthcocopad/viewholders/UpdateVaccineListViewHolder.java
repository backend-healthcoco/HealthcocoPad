package com.healthcoco.healthcocopad.viewholders;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.VaccineResponse;
import com.healthcoco.healthcocopad.dialogFragment.AddEditVaccineNoteDialogFragment;
import com.healthcoco.healthcocopad.enums.VaccineStatus;
import com.healthcoco.healthcocopad.listeners.AddEditVaccineNoteListener;
import com.healthcoco.healthcocopad.listeners.SelectVaccinationListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.Calendar;

public class UpdateVaccineListViewHolder extends HealthcocoComonRecylcerViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, AddEditVaccineNoteListener {

    public static final String TAG_VACCINE_NOTE = "note";
    private TextView tvVaccineName;
    private CheckBox cbVaccine;
    private SelectVaccinationListener selectVaccinationListener;
    private VaccineResponse vaccineResponse;
    private TextView tvGivenDate;
    private TextView tvDueDate;
    private TextView tvAddNote;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "dd MMM yyyy";
    private ImageView ivCancelDate;

    public UpdateVaccineListViewHolder(HealthCocoActivity activity, View itemView, HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
        super(activity, itemView, onItemClickListener, listenerObject);
        this.selectVaccinationListener = (SelectVaccinationListener) listenerObject;
    }

    @Override
    public void initViews(View itemView) {
        tvVaccineName = (TextView) itemView.findViewById(R.id.tv_vaccine_name);
        tvDueDate = (TextView) itemView.findViewById(R.id.tv_due_date);
        tvGivenDate = (TextView) itemView.findViewById(R.id.tv_given_date);
        cbVaccine = (CheckBox) itemView.findViewById(R.id.cb_select_vaccine);
        tvAddNote = itemView.findViewById(R.id.tv_add_note);
        ivCancelDate = (ImageView) itemView.findViewById(R.id.iv_cancel_date);
        tvAddNote.setOnClickListener(this);
        cbVaccine.setOnCheckedChangeListener(this);
        cbVaccine.setOnClickListener(this);
//        tvDueDate.setOnClickListener(this);
        tvGivenDate.setOnClickListener(this);
        ivCancelDate.setOnClickListener(this);
    }

    @Override
    public void applyData(Object object) {
        vaccineResponse = (VaccineResponse) object;
        if (vaccineResponse != null) {
            if (!Util.isNullOrBlank(vaccineResponse.getName()))
                tvVaccineName.setText(vaccineResponse.getName());
            else tvVaccineName.setText("");

            if (vaccineResponse.getDueDate() != null && vaccineResponse.getDueDate() > 0)
                tvDueDate.setText(DateTimeUtil.getFormatedDateAndTime(DATE_FORMAT_USED_IN_THIS_SCREEN, vaccineResponse.getDueDate()));
            else tvDueDate.setText("");

            if (vaccineResponse.getGivenDate() != null && vaccineResponse.getGivenDate() > 0)
                tvGivenDate.setText(DateTimeUtil.getFormatedDateAndTime(DATE_FORMAT_USED_IN_THIS_SCREEN, vaccineResponse.getGivenDate()));
            else{
                tvGivenDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
                vaccineResponse.setGivenDate(DateTimeUtil. getLongFromFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, tvGivenDate.getText().toString()));
            }
            if (vaccineResponse.getStatus() == VaccineStatus.GIVEN)
                selectVaccinationListener.isVaccinationClicked(true, vaccineResponse);
            cbVaccine.setChecked(selectVaccinationListener.isVaccinationSelected(vaccineResponse.getUniqueId()));

            if (vaccineResponse.getStatus() != null && !vaccineResponse.isSelected()) {
                if (vaccineResponse.getStatus() == VaccineStatus.GIVEN) {
                    cbVaccine.setClickable(false);
                    tvGivenDate.setClickable(false);
                    tvAddNote.setClickable(false);
                    ivCancelDate.setVisibility(View.GONE);
                }
            } else ivCancelDate.setVisibility(View.VISIBLE);
            if (vaccineResponse.isSelected()) {
                cbVaccine.setChecked(true);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        selectVaccinationListener.isVaccinationClicked(isChecked, vaccineResponse);
        selectVaccinationListener.setSelectAll(isChecked);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_due_date:
                openDatePickerDialog((TextView) v);
                break;
            case R.id.tv_given_date:
                openDatePickerDialog((TextView) v);
                break;
            case R.id.tv_add_note:
                openAddEditVaccineNoteDialogFragment(vaccineResponse.getNote());
                break;
            case R.id.iv_cancel_date:
                tvGivenDate.setText("");
                ivCancelDate.setVisibility(View.GONE);
                break;
        }
    }

    private void openAddEditVaccineNoteDialogFragment(String note) {
        Bundle args = new Bundle();
        args.putString(TAG_VACCINE_NOTE, note);
        AddEditVaccineNoteDialogFragment dialogFragment = new AddEditVaccineNoteDialogFragment(this);
        dialogFragment.setArguments(args);
        dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
    }

    private void openDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(DATE_FORMAT_USED_IN_THIS_SCREEN,calendar,textView);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(DateTimeUtil.getFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN, year, monthOfYear, dayOfMonth, 0, 0, 0));
                cbVaccine.setChecked(false);
                if (textView.getId() == R.id.tv_due_date) {
                    vaccineResponse.setDueDate(DateTimeUtil.getLongFromFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, tvDueDate.getText().toString()));
                    cbVaccine.setChecked(true);
                } else if (textView.getId() == R.id.tv_given_date) {
                    vaccineResponse.setGivenDate(DateTimeUtil.getLongFromFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, tvGivenDate.getText().toString()));
                    ivCancelDate.setVisibility(View.VISIBLE);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void getVaccineNote(String note) {
        cbVaccine.setChecked(false);
        vaccineResponse.setNote(note);
        cbVaccine.setChecked(true);
    }
}