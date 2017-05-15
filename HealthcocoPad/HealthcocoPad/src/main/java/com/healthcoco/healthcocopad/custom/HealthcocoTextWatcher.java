package com.healthcoco.healthcocopad.custom;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;

/**
 * Created by neha on 02/10/16.
 */
public class HealthcocoTextWatcher implements TextWatcher {

    private HealthcocoTextWatcherListener textWatcherListener;
    private View view;

    public HealthcocoTextWatcher(View view, HealthcocoTextWatcherListener textWatcherListener) {
        this.view = view;
        this.textWatcherListener = textWatcherListener;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        textWatcherListener.afterTextChange(view, text);
    }
}