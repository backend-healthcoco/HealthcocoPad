package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public enum VisitedForType {
    COMBINED_CLINICAL_NOTE_PRESCRIPTION("COMBINED_CLINICAL_NOTE_PRESCRIPTION", new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_sms_visit);
        add(R.id.bt_print_visit);
        add(R.id.bt_edit_visit);
    }}),
    PRESCRIPTION("PRESCRIPTION", new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_sms_visit);
        add(R.id.bt_save_as_template_visit);
        add(R.id.bt_clone_visit);
        add(R.id.bt_print_visit);
        add(R.id.bt_edit_visit);
    }}),
    TREATMENT("TREATMENT", new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_print_visit);
        add(R.id.bt_clone_visit);
        add(R.id.bt_edit_visit);
    }}),
    CLINICAL_NOTES("CLINICAL_NOTES", new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_print_visit);
        add(R.id.bt_edit_visit);
    }}),
    REPORTS("REPORTS", new ArrayList() {{
        add(R.id.bt_open_visit);
        add(R.id.bt_email_visit);
    }}),
    FAMILY_HISTORY("FAMILY_HISTORY", new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_sms_visit);
        add(R.id.bt_print_visit);
    }}),
    PERSONAL_HISTORY("PERSONAL_HISTORY", new ArrayList() {{
        add(R.id.bt_email_visit);
        add(R.id.bt_sms_visit);
        add(R.id.bt_print_visit);
    }});
    private final ArrayList<Integer> visibleButtonIdsList;
    private String visitFor;

    VisitedForType(String visitFor, ArrayList<Integer> visibleButtonIdsList) {
        this.visibleButtonIdsList = visibleButtonIdsList;
        this.visitFor = visitFor;
    }

    public ArrayList<Integer> getVisibleButtonIdsList() {
        return visibleButtonIdsList;
    }

    public String getVisitFor() {
        return visitFor;
    }

    public void setVisitFor(String visitFor) {
        this.visitFor = visitFor;
    }
}
