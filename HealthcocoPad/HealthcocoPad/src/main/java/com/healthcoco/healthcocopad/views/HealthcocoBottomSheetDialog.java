package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.popupwindow.PopupListViewAdapter;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 01-Mar-18.
 */

public class HealthcocoBottomSheetDialog extends com.healthcoco.healthcocopad.custom.BottomSheetDialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final View anchorView;
    private final HealthCocoApplication mApp;
    private List<Object> list;
    private PopupWindowListener popupWindowListener;
    private HealthCocoActivity mActivity;
    private PopupWindowType popupWindowType;
    private int dropDownLayoutId;
    private PopupListViewAdapter popupListViewAdapter;
    private boolean isTvAllVisible;
    private TextView textViewAll;

    public HealthcocoBottomSheetDialog(@NonNull Context context, View view, PopupWindowType popupWindowType, List<Object> list, boolean isTvAllVisible, PopupWindowListener popupWindowListener) {
        this(context, view, popupWindowType, list, R.layout.layout_bottom_dialog_item, isTvAllVisible, popupWindowListener);
    }

    public HealthcocoBottomSheetDialog(@NonNull Context context, View view, PopupWindowType popupWindowType, PopupWindowListener popupWindowListener) {
        this(context, view, popupWindowType, null, R.layout.layout_bottom_dialog_item, false, popupWindowListener);
    }

    public HealthcocoBottomSheetDialog(Context context, View view, PopupWindowType popupWindowType, List<Object> list, int dropDownLayoutId, boolean isTvAllVisible, PopupWindowListener popupWindowListener) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        this.popupWindowType = popupWindowType;
        this.mApp = (HealthCocoApplication) ((HealthCocoActivity) context).getApplication();
        this.popupWindowListener = popupWindowListener;
        this.dropDownLayoutId = dropDownLayoutId;
        this.isTvAllVisible = isTvAllVisible;
        this.list = list;
        this.anchorView = view;
    }

    public View getPopupView() {
        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.bottom_dialog_healthcoco, null);
        BottomSheetListView lvList = (BottomSheetListView) linearLayout.findViewById(R.id.lv_popup_options);
        textViewAll = (TextView) linearLayout.findViewById(R.id.tv_all);
        popupListViewAdapter = new PopupListViewAdapter(mActivity, popupWindowType, dropDownLayoutId);
        lvList.setAdapter(popupListViewAdapter);
        lvList.setOnItemClickListener(this);
        if (!isTvAllVisible)
            textViewAll.setVisibility(View.GONE);
        textViewAll.setSelected(false);
        if (!Util.isNullOrEmptyList(list))
            notifyAdapter((ArrayList<Object>) list);
        lvList.smoothScrollToPosition(0);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        if (anchorView != null)
            anchorView.setOnClickListener(this);
        textViewAll.setOnClickListener(this);
        return linearLayout;
    }

    public void showOptionsWindow(View v) {
        show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object tag = view.getTag();
        textViewAll.setSelected(false);
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
        } else {
            switch (v.getId()) {
                case R.id.tv_all:
                    Object tag = v.getTag();
                    if (v instanceof TextView && tag instanceof String) {
                        TextView tvText = (TextView) v;
                        String text = (String) tag;
                        tvText.setText(text);
                        tvText.setSelected(true);
                    }
                    if (popupWindowListener != null)
                        popupWindowListener.onItemSelected(popupWindowType, tag);
                    dismiss();
                    break;
            }
        }
    }

    public void notifyAdapter(ArrayList<Object> list) {
        this.list = list;
        popupListViewAdapter.setListData(list);
        popupListViewAdapter.notifyDataSetChanged();
    }
}
