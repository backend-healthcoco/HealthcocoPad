package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.OptionsTypePopupWindow;

import java.util.ArrayList;

/**
 * Created by neha on 28/11/15.
 */
public class OptionsPopupWindow extends PopupWindow {
    private View.OnClickListener onClickListener;
    private HealthCocoActivity mActivity;
    private OptionsTypePopupWindow optionsType;

    public OptionsPopupWindow(Context context, OptionsTypePopupWindow optionsType, View.OnClickListener onClickListener) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        this.optionsType = optionsType;
        this.onClickListener = onClickListener;
    }

    public View getPopupView() {
        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.popup_prescription_item_options, null);
        LinearLayout view = (LinearLayout) linearLayout.findViewById(R.id.container_options);
        ArrayList<Integer> optionIdsList = optionsType.getOptionIdsList();
        for (int i = 0; i < view.getChildCount(); i++) {
            View childView = view.getChildAt(i);
            int childId = childView.getId();
            if (optionIdsList.contains(childId))
                childView.setVisibility(View.VISIBLE);
            else
                childView.setVisibility(View.GONE);
            childView.setOnClickListener(onClickListener);
        }
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setWindowLayoutMode(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
        return linearLayout;
    }

    public void showOptionsWindow(View v) {
//        showAsDropDown(v, -5, 0);
        showAsDropDown(v);
//        showAsDropDown(v, 0, 0, Gravity.NO_GRAVITY);
        update(v, 0, 0, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void showOptionsWindowAtLeftCenter(View v) {
        showAtLocation(v, Gravity.LEFT | Gravity.CENTER, 0, 0);
        update(v, 0, 0, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void hidePopupOption(int viewId) {
        View popupView = getPopupView();
        if (popupView != null) {
            View view = popupView.findViewById(viewId);
            view.setVisibility(View.GONE);
        }
    }
}
