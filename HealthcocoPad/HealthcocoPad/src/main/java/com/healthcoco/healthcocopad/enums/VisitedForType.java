package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public enum VisitedForType {
    COMBINED_CLINICAL_NOTE_PRESCRIPTION(new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_sms_visit);
        add(R.id.bt_print_visit);
        add(R.id.bt_edit_visit);
    }}),
    PRESCRIPTION(new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_sms_visit);
        add(R.id.bt_save_as_template_visit);
        add(R.id.bt_clone_visit);
        add(R.id.bt_print_visit);
        add(R.id.bt_edit_visit);
    }}),
    TREATMENT(new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_print_visit);
        add(R.id.bt_clone_visit);
        add(R.id.bt_edit_visit);
    }}),
    CLINICAL_NOTES(new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_print_visit);
        add(R.id.bt_edit_visit);
    }}),
    REPORTS(new ArrayList() {{
        add(R.id.bt_open_visit);
        add(R.id.bt_email_visit);
        add(R.id.bt_print_visit);
        add(R.id.bt_edit_visit);
    }}),
    FAMILY_HISTORY(new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_sms_visit);
        add(R.id.bt_print_visit);
    }}),
    PERSONAL_HISTORY(new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_sms_visit);
        add(R.id.bt_print_visit);
    }});
    private final ArrayList<Integer> visibleButtonIdsList;

    VisitedForType(ArrayList<Integer> visibleButtonIdsList) {
        this.visibleButtonIdsList = visibleButtonIdsList;
    }

    public ArrayList<Integer> getVisibleButtonIdsList() {
        return visibleButtonIdsList;
    }
}
