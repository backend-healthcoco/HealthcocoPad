package com.healthcoco.healthcocopad.recyclerview;

import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.popupwindow.HealthcocoPopupWindow;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;

import java.util.List;

/**
 * Created by neha on 12/03/18.
 */

public abstract class HealthcocoComonRecylcerViewHolder extends RecyclerView.ViewHolder {
    protected static final String TAG = HealthCocoViewHolder.class.getSimpleName();
    protected HealthcocoRecyclerViewItemClickListener onItemClickListener;
    protected HealthCocoApplication mApp;
    protected LayoutInflater inflater;
    protected HealthCocoActivity mActivity;
    protected Object listenerObject;
    private final View view;

    public HealthcocoComonRecylcerViewHolder(HealthCocoActivity activity, View itemView,
                                             HealthcocoRecyclerViewItemClickListener onItemClickListener) {
        super(itemView);
        mActivity = activity;
        inflater = mActivity.getLayoutInflater();
        mApp = ((HealthCocoApplication) mActivity.getApplication());
        this.onItemClickListener = onItemClickListener;
        this.view = itemView;
        initViews(itemView);
    }

    public HealthcocoComonRecylcerViewHolder(HealthCocoActivity activity, View itemView,
                                             HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
        super(itemView);
        mActivity = activity;
        inflater = mActivity.getLayoutInflater();
        mApp = ((HealthCocoApplication) mActivity.getApplication());
        this.onItemClickListener = onItemClickListener;
        this.listenerObject = listenerObject;
        this.view = itemView;
        initViews(itemView);
    }

    public HealthcocoComonRecylcerViewHolder(HealthCocoActivity activity, View itemView) {
        this(activity, itemView, null);
    }

    public View getContentView() {
        return view;
    }

    public abstract void initViews(View itemView);

    public abstract void applyData(Object object);


    protected void initPopupWindows(View anchorView, PopupWindowType popupWindowType) {
        initPopupWindows(anchorView, popupWindowType, popupWindowType.getList(), null);
    }

    protected void initPopupWindows(View anchorView, PopupWindowType popupWindowType, List<Object> list) {
        initPopupWindows(anchorView, popupWindowType, list, null);
    }

    protected void initPopupWindows(View anchorView, PopupWindowType popupWindowType, PopupWindowListener popupWindowListener) {
        initPopupWindows(anchorView, popupWindowType, popupWindowType.getList(), popupWindowListener);
    }

    protected void initPopupWindows(View anchorView, PopupWindowType popupWindowType, List<Object> list, PopupWindowListener popupWindowListener) {
        HealthcocoPopupWindow healthcocoPopupWindow = new HealthcocoPopupWindow(mActivity, anchorView, popupWindowType, list, popupWindowListener);
        healthcocoPopupWindow.setOutsideTouchable(true);
        healthcocoPopupWindow.setContentView(healthcocoPopupWindow.getPopupView());
    }
}
