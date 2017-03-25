package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public enum VisitedForType {
    COMBINED_CLINICAL_NOTE_PRESCRIPTION(new ArrayList() {{
        add(R.id.bt_email);
        add(R.id.bt_sms);
        add(R.id.bt_print);
        add(R.id.bt_edit);
    }}),
    PRESCRIPTION(new ArrayList() {{
        add(R.id.bt_email);
        add(R.id.bt_sms);
        add(R.id.bt_save_as_template);
        add(R.id.bt_clone);
        add(R.id.bt_print);
        add(R.id.bt_edit);
    }}),
    TREATMENT(new ArrayList() {{
        add(R.id.bt_email);
        add(R.id.bt_print);
        add(R.id.bt_clone);
        add(R.id.bt_edit);
    }}),
    CLINICAL_NOTES(new ArrayList() {{
        add(R.id.bt_email);
        add(R.id.bt_print);
        add(R.id.bt_edit);
    }}),
    REPORTS(new ArrayList() {{
        add(R.id.bt_open);
        add(R.id.bt_email);
        add(R.id.bt_print);
        add(R.id.bt_edit);
    }}),
    FAMILY_HISTORY(new ArrayList() {{
        add(R.id.bt_email);
        add(R.id.bt_sms);
        add(R.id.bt_print);
    }}),
    PERSONAL_HISTORY(new ArrayList() {{
        add(R.id.bt_email);
        add(R.id.bt_sms);
        add(R.id.bt_print);
    }});
    private final ArrayList<Integer> visibleButtonIdsList;

    VisitedForType(ArrayList<Integer> visibleButtonIdsList) {
        this.visibleButtonIdsList = visibleButtonIdsList;
    }

    public ArrayList<Integer> getVisibleButtonIdsList() {
        return visibleButtonIdsList;
    }
}
