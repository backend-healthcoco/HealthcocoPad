package com.healthcoco.healthcocopad.listeners;

import android.widget.AutoCompleteTextView;

/**
 * Created by Shreshtha on 24-01-2017.
 */

public interface AutoCompleteTextViewListener {
    public void onEmptyListFound(AutoCompleteTextView autoCompleteTextView);

    public void scrollToPosition(int position);
}
