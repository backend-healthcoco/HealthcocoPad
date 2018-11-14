package com.healthcoco.healthcocopad.dialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Exercise;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.enums.ExerciseType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Prashant on 19-10-2018.
 */

public class AddWorkingHrsDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener {

    public static final String TIME_FORMAT = "hh:mm aaa";
    public static final int DEFAULT_TIME_INTERVAL = 15;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    TimePickerDialog datePickerDialog;

    private TextView tvFromTime;
    private TextView tvToTime;
    private TextView titleTextView;

    private Bundle bundle;
    private WorkingHours workingHours;
    private boolean isTravellingPeriod;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_working_hrs, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bundle = getArguments();
        if (bundle != null) {
            workingHours = Parcels.unwrap(bundle.getParcelable(HealthCocoConstants.TAG_INTENT_DATA));
            isTravellingPeriod = bundle.getBoolean(HealthCocoConstants.TAG_IS_TRAVELLING_PERIOD);
        }

        setWidthHeight(0.80, 0.60);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
    }

    @Override
    public void initViews() {
        titleTextView = view.findViewById(R.id.tv_title);

        tvFromTime = view.findViewById(R.id.tv_from_time);
        tvToTime = view.findViewById(R.id.tv_to_time);

        titleTextView.setText(R.string.add);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);

        tvFromTime.setOnClickListener(this);
        tvToTime.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (workingHours != null) {
            if (!Util.isNullOrZeroNumber(workingHours.getFromTime())) {
                tvFromTime.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) workingHours.getFromTime())));
                tvFromTime.setTag(workingHours.getFromTime());
            }
            if (!Util.isNullOrZeroNumber(workingHours.getToTime())) {
                tvToTime.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) workingHours.getToTime())));
                tvToTime.setTag(workingHours.getFromTime());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
            case R.id.tv_from_time:
            case R.id.tv_to_time:
                openTimePickerDialog(null, (TextView) v);
                break;
        }
    }

    private void validateData() {
        String msg = null;

        if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvToTime)))
            msg = getResources().getString((R.string.please_enter_to_time));
        if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvFromTime)))
            msg = getResources().getString((R.string.please_enter_from_time));

        if (Util.isNullOrBlank(msg)) {
            addWorkingHours();
        } else
            Util.showToast(mActivity, msg);
    }

    private void addWorkingHours() {
        WorkingHours workingHoursToSend = new WorkingHours();

        if (!Util.isNullOrBlank(String.valueOf(tvFromTime.getText())))
            workingHoursToSend.setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvFromTime.getTag()));
        if (!Util.isNullOrBlank(String.valueOf(tvToTime.getText())))
            workingHoursToSend.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvToTime.getTag()));


        if (workingHours != null)
            workingHoursToSend.setCustomUniqueId(workingHours.getCustomUniqueId());
        else
            workingHoursToSend.setCustomUniqueId(DateTimeUtil.getCurrentDateLong() + "");


        Intent data = new Intent();
        data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(workingHoursToSend));
        data.putExtra(HealthCocoConstants.TAG_IS_TRAVELLING_PERIOD, isTravellingPeriod);
        getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_ADD_WORKING_HRS, mActivity.RESULT_OK, data);
        dismiss();

    }

    private void openTimePickerDialog(final String selectedFromTime, final TextView tvToTime) {
        String defaultPickerTime = selectedFromTime;
        String textTime = Util.getValidatedValueOrNull(tvToTime);
        boolean isTextShown = false;
        if (!Util.isNullOrBlank(textTime)) {
            isTextShown = true;
            defaultPickerTime = textTime;
        }
        final Calendar calendar = DateTimeUtil.getCalendarInstanceFromFormattedTime(TIME_FORMAT, defaultPickerTime, isTextShown, DEFAULT_TIME_INTERVAL);

        datePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                long selectedFromDateTimeMillis = getSelectedFromDateTime(hourOfDay, minute);
                int msg = 0;

                if (msg == 0) {
                    LogUtils.LOGD(TAG, "Time lesser");
                    tvToTime.setText(DateTimeUtil.getFormattedDateTime(TIME_FORMAT, selectedFromDateTimeMillis));
                    tvToTime.setTag(selectedFromDateTimeMillis);
                } else {
                    openTimePickerDialog(selectedFromTime, tvToTime);
                    Util.showToast(mActivity, msg);
                    LogUtils.LOGD(TAG, "Time greater");
                }
            }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        if (!datePickerDialog.isShowing()) {
            datePickerDialog.show();
        }
    }

    private long getSelectedFromDateTime(int hourOfDay, int minute) {
        Calendar calendar1 = DateTimeUtil.getCalendarInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar1.set(Calendar.MINUTE, minute);
        return calendar1.getTimeInMillis();
    }

}
