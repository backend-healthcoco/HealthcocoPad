package com.healthcoco.healthcocopad.custom;

import android.util.Log;
import android.widget.EditText;

import com.healthcoco.healthcocopad.listeners.HealthcocoOnSelectionChangedListener;
import com.myscript.atk.core.CaptureInfo;
import com.myscript.atk.sltw.SingleLineWidgetApi;

/**
 * Created by Shreshtha on 12-04-2017.
 */

public class HealthcocoOnSelectionChanged implements MyScriptEditText.OnSelectionChanged,
        SingleLineWidgetApi.OnConfiguredListener,
        SingleLineWidgetApi.OnTextChangedListener,
        SingleLineWidgetApi.OnSingleTapGestureListener,
        SingleLineWidgetApi.OnLongPressGestureListener,
        SingleLineWidgetApi.OnReturnGestureListener,
        SingleLineWidgetApi.OnEraseGestureListener,
        SingleLineWidgetApi.OnSelectGestureListener,
        SingleLineWidgetApi.OnUnderlineGestureListener,
        SingleLineWidgetApi.OnInsertGestureListener,
        SingleLineWidgetApi.OnJoinGestureListener,
        SingleLineWidgetApi.OnOverwriteGestureListener,
        SingleLineWidgetApi.OnUserScrollBeginListener,
        SingleLineWidgetApi.OnUserScrollEndListener,
        SingleLineWidgetApi.OnUserScrollListener,
        SingleLineWidgetApi.OnPenUpListener {
    private final String TAG = HealthcocoOnSelectionChanged.class.getSimpleName();

    private HealthcocoOnSelectionChangedListener onSelectionChangedListener;
    private EditText editText;
    private int isCorrectionMode;

    public HealthcocoOnSelectionChanged(EditText editText, HealthcocoOnSelectionChangedListener onSelectionChangedListener) {
        this.editText = editText;
        this.onSelectionChangedListener = onSelectionChangedListener;
    }


    public void onConfigured(SingleLineWidgetApi w, boolean success) {
        Log.d(TAG, "Widget configuration " + (success ? "done" : "failed"));
    }

    public void onTextChanged(SingleLineWidgetApi w, String text, boolean intermediate) {
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onTextChanged(editText, w, text, intermediate);
    }

    public void onReturnGesture(SingleLineWidgetApi w, int index) {
        Log.d(TAG, "Return gesture detected at index " + index);
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onReturnGesture(editText, w, index);
    }

    public void onSingleTapGesture(SingleLineWidgetApi w, int index) {
        Log.d(TAG, "Single tap gesture detected at index=" + index);
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onSingleTapGesture(editText, w, index);
    }

    public void onLongPressGesture(SingleLineWidgetApi w, int index) {
        Log.d(TAG, "Long press gesture detected at index=" + index);
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onLongPressGesture(editText, w, index);
    }

    public void onEraseGesture(SingleLineWidgetApi w, int start, int end) {
        Log.d(TAG, "Erase gesture detected for range [" + start + "-" + end + "]");
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onEraseGesture(editText, w, start, end);
    }

    public void onSelectGesture(SingleLineWidgetApi w, int start, int end) {
        Log.d(TAG, "Select gesture detected for range [" + start + "-" + end + "]");
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onSelectGesture(editText, w, start, end);
    }

    public void onUnderlineGesture(SingleLineWidgetApi w, int start, int end) {
        Log.d(TAG, "Underline gesture detected for range [" + start + "-" + end + "]");
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onUnderlineGesture(editText, w, start, end);
    }

    public void onInsertGesture(SingleLineWidgetApi w, int index) {
        Log.d(TAG, "Insert gesture detected at index " + index);
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onInsertGesture(editText, w, index);
    }

    public void onJoinGesture(SingleLineWidgetApi w, int start, int end) {
        Log.d(TAG, "Join gesture detected for range [" + start + "-" + end + "]");
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onJoinGesture(editText, w, start, end);
    }

    public void onOverwriteGesture(SingleLineWidgetApi w, int start, int end) {
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onOverwriteGesture(editText, w, start, end);
    }

    public void onUserScrollBegin(SingleLineWidgetApi w) {
        Log.d(TAG, "User scroll begin");
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onUserScrollBegin(editText, w);
    }

    public void onUserScrollEnd(SingleLineWidgetApi w) {
        Log.d(TAG, "User scroll end");
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onUserScrollEnd(editText, w);
    }

    public void onUserScroll(SingleLineWidgetApi w) {
        Log.d(TAG, "User scroll");
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onUserScroll(editText, w);
    }

    public void onPenUp(SingleLineWidgetApi singleLineWidgetApi, CaptureInfo captureInfo) {
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onPenUp(editText, singleLineWidgetApi, captureInfo);
    }

    public void onSelectionChanged(EditText editText, int selStart, int selEnd) {
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onSelectionChanged(editText, selStart, selEnd);
    }
}