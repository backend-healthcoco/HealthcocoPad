package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 24-01-2017.
 */

public class CustomAutoCompleteTextView extends AutoCompleteTextView {

    private static final String TAG = CustomAutoCompleteTextView.class.getSimpleName();
    private final float SCROLL_THRESHOLD = 10;
    private AutoCompleteTextViewListener autoCompleteTextViewListener;
    private boolean isOnClick;
    private float mDownX;
    private float mDownY;

    public CustomAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    public CustomAutoCompleteTextView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        init();
    }

    public CustomAutoCompleteTextView(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        init();
    }

    private void init() {
        setFocusable(false);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        if (focused) {
//            performFiltering(getText(), 0);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                isOnClick = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isOnClick) {
                    LogUtils.LOGD(TAG, "onClick ");
                    onClickView();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isOnClick && (Math.abs(mDownX - event.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - event.getY()) > SCROLL_THRESHOLD)) {
                    LogUtils.LOGD(TAG, "movement detected");
                    isOnClick = false;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void onClickView() {
        try {
            Util.hideKeyboard(getContext(), this);
            ListAdapter adapter = getAdapter();
            if (autoCompleteTextViewListener != null && adapter != null && adapter.getCount() <= 0)
                autoCompleteTextViewListener.onEmptyListFound(this);
            else {
                showDropDown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAutoTvListener(AutoCompleteTextViewListener autoCompleteTextViewListener) {
        this.autoCompleteTextViewListener = autoCompleteTextViewListener;
    }
}
