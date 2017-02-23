package com.healthcoco.healthcocoplus.custom;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neha on 03/03/16.
 */
public class CustomTimePickerDialog
        extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 15;
    private TimePicker timePicker;
    private final OnTimeSetListener callback;

    public CustomTimePickerDialog(Context context, OnTimeSetListener callBack,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, callBack, hourOfDay, minute / TIME_PICKER_INTERVAL,
                is24HourView);
        this.callback = callBack;
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // This is hiding the "Cancel" button:
                getButton(Dialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (callback != null && timePicker != null) {
            timePicker.clearFocus();
            callback.onTimeSet(timePicker, timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
        }
    }

    @Override
    protected void onStop() {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            this.timePicker = (TimePicker) findViewById(timePickerField
                    .getInt(null));
            Field field = classForid.getField("minute");

            NumberPicker mMinuteSpinner = (NumberPicker) timePicker
                    .findViewById(field.getInt(null));
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            mMinuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
//        extends TimePickerDialog {
//
//    private int minHour = -1;
//    private int minMinute = -1;
//
//    private int maxHour = 25;
//    private int maxMinute = 25;
//
//    private int currentHour = 0;
//    private int currentMinute = 0;
//
//    private Calendar calendar = Calendar.getInstance();
//    private DateFormat dateFormat;
//
//
//    public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
//        super(context, callBack, hourOfDay, minute, is24HourView);
//        currentHour = hourOfDay;
//        currentMinute = minute;
//        dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
//
//        try {
//            Class<?> superclass = getClass().getSuperclass();
//            Field mTimePickerField = superclass.getDeclaredField("mTimePicker");
//            mTimePickerField.setAccessible(true);
//            TimePicker mTimePicker = (TimePicker) mTimePickerField.get(this);
//            mTimePicker.setOnTimeChangedListener(this);
//        } catch (NoSuchFieldException e) {
//        } catch (IllegalArgumentException e) {
//        } catch (IllegalAccessException e) {
//        }
//    }
//
//    public void setMin(int hour, int minute) {
//        minHour = hour;
//        minMinute = minute;
//    }
//
//    public void setMax(int hour, int minute) {
//        maxHour = hour;
//        maxMinute = minute;
//    }
//
//    @Override
//    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//
//        boolean validTime = true;
//        if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)) {
//            validTime = false;
//        }
//
//        if (hourOfDay > maxHour || (hourOfDay == maxHour && minute > maxMinute)) {
//            validTime = false;
//        }
//
//        if (validTime) {
//            currentHour = hourOfDay;
//            currentMinute = minute;
//        } else {
//            currentHour = minHour;
//            currentMinute = minMinute;
//        }
////        if (currentMinute % 5 != 0) {
////            currentMinute = currentMinute + (5 - (currentMinute % 5));
////        }
//
//        updateTime(currentHour, currentMinute);
//
////        updateDialogTitle(view, currentHour, currentMinute);
//    }
//
//
//    private void updateDialogTitle(TimePicker timePicker, int hourOfDay, int minute) {
//        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//        calendar.set(Calendar.MINUTE, minute);
//        String title = dateFormat.format(calendar.getTime());
//        setTitle(title);
//    }
//}