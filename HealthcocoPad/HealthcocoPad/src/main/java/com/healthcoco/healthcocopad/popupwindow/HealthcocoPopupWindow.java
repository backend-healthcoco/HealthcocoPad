package com.healthcoco.healthcocopad.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.PopupWindowType;

import java.util.List;

/**
 * Created by neha on 03/05/17.
 */

public class HealthcocoPopupWindow extends PopupWindow implements View.OnClickListener, AdapterView.OnItemClickListener {
    private List<Object> list;
    private PopupWindowListener popupWindowListener;
    private HealthCocoActivity mActivity;
    private PopupWindowType popupWindowType;
    private int dropDownLayoutId;
    private final View anchorView;

    public HealthcocoPopupWindow(Context context, View view, PopupWindowType popupWindowType, List<Object> list, PopupWindowListener popupWindowListener) {
        this(context, view, popupWindowType, list, R.layout.spinner_drop_down_item_grey_background, popupWindowListener);
    }

    public HealthcocoPopupWindow(Context context, View view, PopupWindowType popupWindowType, List<Object> list, int dropDownLayoutId, PopupWindowListener popupWindowListener) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        this.popupWindowType = popupWindowType;
        this.popupWindowListener = popupWindowListener;
        this.dropDownLayoutId = dropDownLayoutId;
        this.list = list;
        this.anchorView = view;
    }

    public View getPopupView() {
        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.popup_window_healthcoco, null);
        ListView lvList = (ListView) linearLayout.findViewById(R.id.lv_popup_options);
        PopupListViewAdapter popupListViewAdapter = new PopupListViewAdapter(mActivity, popupWindowType, dropDownLayoutId);
        popupListViewAdapter.setListData(list);
        lvList.setAdapter(popupListViewAdapter);
        lvList.setOnItemClickListener(this);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
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
        showAtLocation(v, Gravity.LEFT | Gravity.CENTER, 0, 0);
        update(v, 0, 0, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object tag = view.getTag();
        if (anchorView != null && anchorView instanceof TextView && tag instanceof String) {
            TextView tvText = (TextView) anchorView;
            tvText.setText((String) tag);
        }
        if (popupWindowListener != null)
            popupWindowListener.onItemSelected(popupWindowType, tag);
        dismiss();
    }

    @Override
    public void onClick(View v) {
        if (anchorView != null && anchorView.getId() == v.getId()) {
            if (list != null && list.size() > 0)
                showOptionsWindow(v);
            else popupWindowListener.onEmptyListFound();
        }
    }
}
