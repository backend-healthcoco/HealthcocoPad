package com.healthcoco.healthcocoplus.custom;

import android.content.Context;
import android.widget.ArrayAdapter;


import com.healthcoco.healthcocoplus.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocoplus.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public class AutoCompleteTextViewAdapter extends ArrayAdapter<Object> {
    private final String TAG = AutoCompleteTextViewAdapter.class.getSimpleName();
    private AutoCompleteTextViewListener autoCompleteTextViewListener;
    private AutoCompleteTextViewType autoCompleteTextViewType;
    private List<Object> items;
    private List<Object> itemsAll = new ArrayList<>();
    private List<Object> suggestions;
    private int viewResourceId;
    private Context context;

    public AutoCompleteTextViewAdapter(Context context, int viewResourceId, List<Object> items, AutoCompleteTextViewType autoCompleteTextViewType) {
        super(context,viewResourceId,items);
        if (!Util.isNullOrEmptyList(items)) {
            this.items = Arrays.asList(items.toArray(new Object[items.size()]));
            this.itemsAll = Arrays.asList(items.toArray(new Object[items.size()]));
            this.suggestions = new ArrayList<Object>();
            suggestions.addAll(itemsAll);
            this.viewResourceId = viewResourceId;
            this.autoCompleteTextViewType = autoCompleteTextViewType;
        }
    }
}
