package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.listeners.PopupWindowListener;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by neha on 03/05/17.
 */

public class HealthcocoPopupWindow extends PopupWindow implements View.OnClickListener {
    private ArrayList<Object> list;
    private PopupWindowListener popupWindowListener;
    private HealthCocoActivity mActivity;
    private PopupWindowType popupWindowType;
    private int dropDownLayoutId;
    private Button btDone;

    public HealthcocoPopupWindow(Context context, PopupWindowType popupWindowType, ArrayList<Object> list, PopupWindowListener popupWindowListener) {
        this(context, popupWindowType, list, R.layout.spinner_drop_down_item_grey_background, popupWindowListener);
    }

    public HealthcocoPopupWindow(Context context, PopupWindowType popupWindowType, ArrayList<Object> list, int dropDownLayoutId, PopupWindowListener popupWindowListener) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        this.popupWindowType = popupWindowType;
        this.popupWindowListener = popupWindowListener;
        this.dropDownLayoutId = dropDownLayoutId;
        this.list = list;
    }

    public View getPopupView() {
        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.popup_window_healthcoco, null);
        LinearLayout containerBloodGroups = (LinearLayout) linearLayout.findViewById(R.id.container_popup_options);
//        if (list != null) {
//            for (Object object :
//                    list) {
        View view = mActivity.getLayoutInflater().inflate(dropDownLayoutId, null);
        popupWindowListener.onItemSelected(popupWindowType, view);

//        TextView textView = null;
//        if (view instanceof TextView)
//            textView = (TextView) view;
//        else
//            textView = (TextView) view.findViewById(R.id.tv_text);
////                String text = getText(object);
////                if (!Util.isNullOrBlank(text)) {
////                    textView.setText(text);
////                    textView.setTag(object);
//        textView.setOnClickListener(this);
        containerBloodGroups.addView(view);
//                }
//            }
//        }
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        setFocusable(true);
//        setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
        return linearLayout;
    }

    private String getText(Object object) {
        String text = "";
        switch (popupWindowType) {
            case NEXT_REVIEW:
                if (object instanceof String)
                    text = (String) object;
                break;
        }
        return text;
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

    public void showOptionsWindowAtRightCenter(View v) {
        showAtLocation(v, Gravity.RIGHT | Gravity.CENTER, 0, 0);
        update(v, 0, 0, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void hidePopupOption(int viewId) {
        View popupView = getPopupView();
        if (popupView != null) {
            View view = popupView.findViewById(viewId);
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
