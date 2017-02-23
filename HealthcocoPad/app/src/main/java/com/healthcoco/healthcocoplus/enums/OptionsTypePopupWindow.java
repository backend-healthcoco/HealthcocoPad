package com.healthcoco.healthcocoplus.enums;

import com.healthcoco.healthcocopad.R;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 27-01-2017.
 */
public enum OptionsTypePopupWindow {
//    REPORTS(new ArrayList<Integer>() {{
//        add(R.id.tv_discard);
//        add(R.id.tv_print);
//    }}),
//    VISITS_PRESCRIPTION(new ArrayList<Integer>() {{
//        add(R.id.tv_sms);
//        add(R.id.tv_email);
//    }}),
//    VISITS_OTHERS(new ArrayList<Integer>() {{
//        add(R.id.tv_email);
//    }}),
//    PRESCRIPTIONS(new ArrayList<Integer>() {{
//        add(R.id.tv_discard);
//        add(R.id.tv_edit);
//        add(R.id.tv_save_as_template);
//        add(R.id.tv_print);
//    }}),
//    CLINICAL_NOTES(new ArrayList<Integer>() {{
//        add(R.id.tv_discard);
//        add(R.id.tv_print);
//    }}),
//    CALENDAR_APPOINTEMNT_CONFIRMED(new ArrayList<Integer>() {{
//        add(R.id.tv_cancel_appointment);
//        add(R.id.tv_reschedule);
//    }}),
//    CALENDAR_APPOINTEMNT_ALL(new ArrayList<Integer>() {{
//        add(R.id.tv_confirm);
//        add(R.id.tv_cancel_appointment);
//        add(R.id.tv_reschedule);
//    }}),
//    CALENDAR_ACTIONBAR_ADD(new ArrayList<Integer>() {{
//        add(R.id.tv_appointment);
////        add(R.id.tv_event);
//    }}),
//    CALENDAR_FILTER(new ArrayList<Integer>() {{
//        add(R.id.tv_all);
//        add(R.id.tv_confirmed);
//        add(R.id.tv_pending_confirmation);
//        add(R.id.tv_cancelled);
//    }});
    ;
    private ArrayList<Integer> optionIdsList;

    private OptionsTypePopupWindow(ArrayList<Integer> optionIdsList) {
        this.optionIdsList = optionIdsList;
    }

    public ArrayList<Integer> getOptionIdsList() {
        return optionIdsList;
    }
}
