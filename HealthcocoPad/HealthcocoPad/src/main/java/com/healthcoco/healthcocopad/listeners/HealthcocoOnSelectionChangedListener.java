package com.healthcoco.healthcocopad.listeners;

import android.view.View;
import android.widget.EditText;

import com.myscript.atk.core.CaptureInfo;
import com.myscript.atk.sltw.SingleLineWidgetApi;

/**
 * Created by Shreshtha on 12-04-2017.
 */

public interface HealthcocoOnSelectionChangedListener {
    public void onConfigured(View v, SingleLineWidgetApi w, boolean success);

    public void onTextChanged(View v, SingleLineWidgetApi w, String text, boolean intermediate);

    public void onReturnGesture(View v, SingleLineWidgetApi w, int index);

    public void onSingleTapGesture(View v, SingleLineWidgetApi w, int index);

    public void onLongPressGesture(View v, SingleLineWidgetApi w, int index);

    public void onEraseGesture(View v, SingleLineWidgetApi w, int start, int end);

    public void onSelectGesture(View v, SingleLineWidgetApi w, int start, int end);

    public void onUnderlineGesture(View v, SingleLineWidgetApi w, int start, int end);

    public void onInsertGesture(View v, SingleLineWidgetApi w, int index);

    public void onJoinGesture(View v, SingleLineWidgetApi w, int start, int end);

    public void onOverwriteGesture(View v, SingleLineWidgetApi w, int start, int end);

    public void onUserScrollBegin(View v, SingleLineWidgetApi w);

    public void onUserScrollEnd(View v, SingleLineWidgetApi w);

    public void onUserScroll(View v, SingleLineWidgetApi w);

    public void onPenUp(View v, SingleLineWidgetApi singleLineWidgetApi, CaptureInfo captureInfo);

    public void onSelectionChanged(EditText editText, int selStart, int selEnd);
}
