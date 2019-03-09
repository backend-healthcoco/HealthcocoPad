package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

public class BottomSheetDialog extends android.support.design.widget.BottomSheetDialog{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(500 /*our width*/, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public BottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public BottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected BottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
