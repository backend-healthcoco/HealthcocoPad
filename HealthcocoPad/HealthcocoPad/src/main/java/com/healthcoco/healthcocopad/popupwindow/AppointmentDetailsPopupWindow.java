package com.healthcoco.healthcocopad.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.listeners.DoctorListPopupWindowListener;

/**
 * Created by neha on 03/05/17.
 */

public class AppointmentDetailsPopupWindow extends PopupWindow implements View.OnClickListener {
    private final View anchorView;
    private HealthCocoActivity mActivity;
    private int dropDownLayoutId;

    public AppointmentDetailsPopupWindow(Context context, View view, DoctorListPopupWindowListener doctorListPopupWindowListener) {
        this(context, view);
    }

    public AppointmentDetailsPopupWindow(Context context, View view) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        this.dropDownLayoutId = dropDownLayoutId;
        this.anchorView = view;
    }

    public View getPopupView() {
        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.popup_window_patient_appointment_details, null);


        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(false);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
//        setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
        if (anchorView != null)
            anchorView.setOnClickListener(this);

        return linearLayout;
    }

    public void showOptionsWindow(View v) {
//        showAsDropDown(v, -5, 0);
        showAsDropDown(v);
//        showAsDropDown(v, 0, 0, Gravity.NO_GRAVITY);
        update(v, 0, 0, anchorView.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void showOptionsWindowAtLeftCenter(View v) {
        showAtLocation(v, Gravity.LEFT, 0, 0);
        update(v, -v.getWidth() * 2, -v.getHeight() * 2, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//        showAtLocation(v, Gravity.LEFT, 0, 0);
//        update(v, 0, 0, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        if (anchorView != null && anchorView.getId() == v.getId()) {
            showOptionsWindowAtLeftCenter(v);
        }
        switch (v.getId()) {
            case R.id.layout_select_all:

                break;

            case R.id.tv_apply:

                dismiss();
                break;

        }
    }

}
