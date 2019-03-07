package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.listeners.PopupWindowListener;
import com.healthcoco.healthcocopad.popupwindow.PopupListViewAdapter;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.List;

public class HealthcocoBottomSheetGridViewDialog extends BottomSheetDialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final View anchorView;
    private final HealthCocoApplication mApp;
    private List<Object> list;
    private PopupWindowListener popupWindowListener;
    private HealthCocoActivity mActivity;
    private PopupWindowType popupWindowType;
    private int dropDownLayoutId;
    private PopupListViewAdapter popupListViewAdapter;
    private String formattedTextforPopup;

    public HealthcocoBottomSheetGridViewDialog(@NonNull Context context, View view, PopupWindowType popupWindowType, String formattedTextforPopup, List<Object> list, PopupWindowListener popupWindowListener) {
        this(context, view, popupWindowType, formattedTextforPopup, list, R.layout.layout_bottom_dialog_grid_item, popupWindowListener);
    }

    public HealthcocoBottomSheetGridViewDialog(@NonNull Context context, View view, PopupWindowType popupWindowType, String formattedTextforPopup, PopupWindowListener popupWindowListener) {
        this(context, view, popupWindowType, formattedTextforPopup, null, R.layout.layout_bottom_dialog_grid_item, popupWindowListener);
    }

    public HealthcocoBottomSheetGridViewDialog(Context context, View view, PopupWindowType popupWindowType, String formattedTextforPopup, List<Object> list, int dropDownLayoutId, PopupWindowListener popupWindowListener) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        this.popupWindowType = popupWindowType;
        this.mApp = (HealthCocoApplication) ((HealthCocoActivity) context).getApplication();
        this.popupWindowListener = popupWindowListener;
        this.dropDownLayoutId = dropDownLayoutId;
        this.formattedTextforPopup = formattedTextforPopup;
        this.list = list;
        this.anchorView = view;
    }

    public View getPopupView() {
        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.gridview_bottom_dialog_healthcoco, null);
        GridView gridView = (GridView) linearLayout.findViewById(R.id.gv_popup_options);
        TextView textView = (TextView) linearLayout.findViewById(R.id.tv_bottom_popup);
        popupListViewAdapter = new PopupListViewAdapter(mActivity, popupWindowType, dropDownLayoutId);
        gridView.setAdapter(popupListViewAdapter);
        gridView.setOnItemClickListener(this);
        textView.setText(formattedTextforPopup);
        if (!Util.isNullOrEmptyList(list))
            notifyAdapter((ArrayList<Object>) list);
        gridView.smoothScrollToPosition(0);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        if (anchorView != null)
            anchorView.setOnClickListener(this);
        return linearLayout;
    }

    public void showOptionsWindow(View v) {
        show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object tag = view.getTag();
        if (anchorView != null && anchorView instanceof TextView && tag instanceof String) {
            TextView tvText = (TextView) anchorView;
            String text = (String) tag;
            if (text.contains("_"))
                text = text.replaceAll("_", " ");
            tvText.setText(text);
        }
        if (popupWindowListener != null)
            popupWindowListener.onItemSelected(popupWindowType, tag);
        dismiss();
    }

    @Override
    public void onClick(View v) {
        if (anchorView != null && anchorView.getId() == v.getId() && !Util.isNullOrEmptyList(list)) {
            showOptionsWindow(v);
        }
    }

    public void notifyAdapter(ArrayList<Object> list) {
        this.list = list;
        popupListViewAdapter.setListData(list);
        popupListViewAdapter.notifyDataSetChanged();
    }
}