package com.healthcoco.healthcocopad.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.android.material.textfield.TextInputLayout;
import com.healthcoco.healthcocopad.HealthCocoActivity;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public class EditTextTextViewErrorUtil {

    public static void showErrorOnEditText(Activity context, View parentView, ArrayList<View> list, String msg) {
        removeFocusFromEditText(parentView);
        if (!Util.isNullOrEmptyList(list)) {
            for (View view : list) {
                int index = list.indexOf(view);
                if (index == 0)
                    Util.requesFocus(view);
                view.setActivated(true);
            }
        }
        if (!Util.isNullOrBlank(msg))
            Util.showAlert(context, msg);
    }

    public static void showErrorOnEditText(Context context, ArrayList<View> list, String msg) {
        for (View view :
                list) {
            view.setActivated(true);
        }
        Util.showAlert(context, msg);
    }

    private static void removeFocusFromEditText(View view) {
        try {
            view.setFocusable(false);
            view.setActivated(false);
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    removeFocusFromEditText(innerView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetFocusToAllEditText(final View view) {
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (view instanceof EditText)
                if (((EditText) view).isEnabled()) {
                    view.setFocusableInTouchMode(true);
                    view.setFocusable(true);
                }
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    resetFocusToAllEditText(innerView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUpUI(final HealthCocoActivity mActivity, final View viewToSet) {
        try {
            viewToSet.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (!(viewToSet instanceof EditText) && !(viewToSet instanceof ScrollView) && !(viewToSet instanceof TextInputLayout)
                            && viewToSet.getParent() != null && !(viewToSet.getParent() instanceof ScrollView))
                        Util.hideKeyboard(mActivity, viewToSet);
                    viewToSet.setActivated(false);
                    EditTextTextViewErrorUtil.resetFocusToAllEditText(viewToSet);
                    return false;
                }
            });
            //If a layout container, iterate over children and seed recursion.
            if (viewToSet instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) viewToSet).getChildCount(); i++) {
                    View innerView = ((ViewGroup) viewToSet).getChildAt(i);
                    setUpUI(mActivity, innerView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
