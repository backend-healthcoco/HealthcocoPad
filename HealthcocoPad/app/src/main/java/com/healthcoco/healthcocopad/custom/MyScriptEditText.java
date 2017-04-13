// Copyright MyScript. All rights reserved.

package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyScriptEditText extends EditText {

    private int mLastSelectionStart;
    private int mLastSelectionEnd;

    private OnSelectionChanged mOnSelectionChangedListener;

    public interface OnSelectionChanged {
        public void onSelectionChanged(EditText editText, int selStart, int selEnd);
    }

    public MyScriptEditText(Context context) {
        super(context);
        init();
    }

    public MyScriptEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScriptEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // prevent input method to show up when tapping into the text field
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        setTextIsSelectable(true);
    }

    @Override
    public void setSelection(int index) {
        int length = getText().length();
        index = Math.min(index, length);
        mLastSelectionStart = index;
        mLastSelectionEnd = index;
        super.setSelection(index);
    }

    @Override
    public void setSelection(int start, int stop) {
        int length = getText().length();
        start = Math.min(start, length);
        stop = Math.min(stop, length);
        mLastSelectionStart = start;
        mLastSelectionEnd = stop;
        super.setSelection(start, stop);
    }

    public void setOnSelectionChangedListener(OnSelectionChanged l) {
        mOnSelectionChangedListener = l;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (selStart != mLastSelectionStart || selEnd != mLastSelectionEnd) {
            if (mOnSelectionChangedListener != null) {
                mOnSelectionChangedListener.onSelectionChanged(this, selStart, selEnd);
                mLastSelectionStart = selStart;
                mLastSelectionEnd = selEnd;
            }
        }
    }
}
